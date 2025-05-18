package com.rotem.graphsync.graph.processor.exceptions;

public class GraphDatabaseException extends Exception {
    public GraphDatabaseException(String errorMessage, Exception e) {
        super(errorMessage, e);
    }
}
