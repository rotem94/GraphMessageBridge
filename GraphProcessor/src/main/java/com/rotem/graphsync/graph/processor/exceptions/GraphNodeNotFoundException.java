package com.rotem.graphsync.graph.processor.exceptions;

public class GraphNodeNotFoundException extends RuntimeException {

    public GraphNodeNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
