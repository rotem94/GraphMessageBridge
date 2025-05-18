package com.rotem.graphsync.graph.processor.exceptions;

public class GraphModificationException extends RuntimeException {

    public GraphModificationException(String errorMessage, Exception e) {
        super(errorMessage, e);
    }

    public GraphModificationException(String errorMessage) {
        super(errorMessage);
    }
}
