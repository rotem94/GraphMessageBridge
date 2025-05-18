package com.rotem.graphsync.graph.processor.exceptions;

public class GraphNotFoundException extends Exception {

    public GraphNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
