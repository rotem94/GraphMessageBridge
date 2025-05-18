package com.rotem.graphsync.graph.api.gateway.exceptions;

public class DataNotFoundException extends RuntimeException {
    public DataNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
