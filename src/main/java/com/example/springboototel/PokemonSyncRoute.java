package com.example.springboototel;

import io.micrometer.core.instrument.MeterRegistry;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ArrayNode;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Component
public class PokemonSyncRoute extends RouteBuilder {

    private final MeterRegistry meterRegistry;

    private final ObjectMapper mapper = new ObjectMapper();

    private final Set<String> pokemonNames = new HashSet<>();

    public PokemonSyncRoute(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Override
    public void configure() {
        onException(Exception.class)
                .handled(true)   // 👈 voorkomt dat de exception verder gaat
                .to("log:error?level=ERROR&showAll=true");

        from("quartz://pokemonSyncRoute?cron=0/10 * * * * ?")
                .routeId("pokemon-sync-route")
                .log("pokemon-sync-route gestart")
                .process(exchange -> {
                    String json = Files.readString(Paths.get("data/pokemons.json"));
                    var jsonNode = mapper.readTree(json);
                    var results = (ArrayNode)jsonNode.get("results");
                    var newPokemonNames = results.elements().stream().map(node -> node.get("name")).collect(toSet());
                    exchange.setProperty("new-pokemon-names", newPokemonNames);;
                })
                .process(exchange -> {
                    @SuppressWarnings("unchecked")
                    Set<String> newPokemonNames = exchange.getProperty("new-pokemon-names", Set.class);
                    Set<String> added = new HashSet<>(newPokemonNames);
                    added.removeAll(pokemonNames);

                    Set<String> removed = new HashSet<>(pokemonNames);
                    removed.removeAll(newPokemonNames);

                    pokemonNames.clear();
                    pokemonNames.addAll(newPokemonNames);

                    meterRegistry.counter("pokemon.added").increment(added.size());;
                    meterRegistry.counter("pokemon.removed").increment(removed.size());;

                    log.info("Names added: {} {}", added.size(), added);
                    log.info("Names removed: {} {}", removed.size(), removed);
                })
                .log("pokemon-sync-route voltooid");
    }
}
