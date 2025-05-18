package com.rotem.graphsync.graph.api.gateway.utils;

import java.util.UUID;

public class RandomIdGenerator {

    public static String GenerateRandomId() {
        String randomId = UUID.randomUUID().toString();

        return randomId;
    }
}
