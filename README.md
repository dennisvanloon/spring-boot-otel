## Monitoring van een Spring Boot applicatie met OpenTelemetry
Deze repo bevat een spring boot applicatie die gebruik maakt van OpenTelemetry voor het traces, metrics en logging. Deze data wordt verstuurd naar een Otel Collector. Daarna zijn er twee mogelijkheden:
- We maken gebruik van een stack gebaseerd op Grafana. In deze setup stuurt de Otel collector de logging door naar Grafana Loki, metrics naar Grafana Mimir en traces naar Grafana Tempo.
- We maken gebruik van een stack gebaseerd op Elastic. Deze bestaat uit een APM server die de data ontvangt van de Otel Collector. De APM server stuurt de data vervolgens naar Elasticsearch waarna we op basis van de data dashboard kunnen maken in Kibana

### Starten Kubernetes cluster in colima
De elastic search of grafana stack die we nodig hebben gaan de draaien colima. Start colima met de volgende commando:  

`colima start --cpu 8 --memory 12 --kubernetes`

Voer het volgens command uit en check of je kubernetes enabled ziet:  

`colima status`  

Voer het volgende commando uit, je zou het control-plane nu moeten zien staan:  

`kubectl get nodes`  

Als je klaar bent kun je colima stoppen (en later weer starten) met:  

`colima stop`
`colima start`

Bij gebruik van colima kun je ook gebruik maken van profiles om de verschillende stacks op te bouwen. Je kunt ze dan naast elkaar bewaren. Je kunt er uiteraard maar één tegelijk actief hebben. Als je bijvoorbeeld de grafana stack wil opbouwen onder het profile 'grafana' maak je gebruik van:

`colima start grafana --cpu 8 --memory 12 --kubernetes`  
`colima status grafana`  
`colima stop grafana`  
`colima start grafana`
  
### Gebruik headlamp als console op het cluster
Een standaard cluster bevat geen UI maar dat is tijdens het werk wel prettig om te hebben. HIervoor kun je headlamp gebruiken. Als dit nog niet geinstalleerd kun je het installeren met het volgende commando:  
`helm install headlamp headlamp/headlamp --namespace headlamp --create-namespace`  

Als headlamp al wel geinstalleerd is kun je het gebruiken door eerst een port forward vanuit een console te doen:  
`export POD_NAME=$(kubectl get pods --namespace headlamp -l "app.kubernetes.io/name=headlamp,app.kubernetes.io/instance=headlamp" -o jsonpath="{.items[0].metadata.name}")`  
`export CONTAINER_PORT=$(kubectl get pod --namespace headlamp $POD_NAME -o jsonpath="{.spec.containers[0].ports[0].containerPort}")`  
`kubectl --namespace headlamp port-forward $POD_NAME 8080:$CONTAINER_PORT`  

Vervolgens kun je in een andere terminal een token maken om in te loggen:  
`kubectl create token headlamp --namespace headlamp`

Kopieer het token en gebruik deze op de login pagina op: `http://127.0.0.1:8080`




