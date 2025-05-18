package com.rotem.graphsync.graph.processor.exceptions;

public class GraphAlreadyExistsException extends RuntimeException {
    public GraphAlreadyExistsException(String errorMessage) {
        super(errorMessage);
    }
}
