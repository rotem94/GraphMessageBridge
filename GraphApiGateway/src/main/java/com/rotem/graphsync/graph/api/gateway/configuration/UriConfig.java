package com.rotem.graphsync.graph.api.gateway.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class UriConfig {

    @Bean
    public URI serviceBUri(@Value("${SERVICE-B-URL}") String serviceBUrl) throws URISyntaxException {
        URI serviceBUri = new URI(serviceBUrl);

        return serviceBUri;
    }
}
