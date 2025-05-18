package com.rotem.graphsync.graph.processor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.rotem.graphsync.graph.processor", "com.rotem.graphsync.graph.common"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

