package com.example.springboototel;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.instrumentation.logback.appender.v1_0.OpenTelemetryAppender;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class SpringBootOtelApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(SpringBootOtelApplication.class, args);
        OpenTelemetryAppender.install(context.getBean(OpenTelemetry.class));
    }

}
