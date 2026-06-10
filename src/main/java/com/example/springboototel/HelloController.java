package com.example.springboototel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    private static final Logger log = LoggerFactory.getLogger(HelloController.class);

    @GetMapping("/hello")
    public String hello() {
        log.info("Hello endpoint aangeroepen");
        return "Hello, World!";
    }

    @GetMapping("/hello/{name}")
    public String helloName(@PathVariable String name) {
        log.info("Hello endpoint aangeroepen voor naam: {}", name);
        return "Hello, " + name + "!";
    }
}
