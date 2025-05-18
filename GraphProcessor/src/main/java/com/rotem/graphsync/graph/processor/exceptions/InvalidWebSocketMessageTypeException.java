package com.rotem.graphsync.graph.processor.exceptions;

public class InvalidWebSocketMessageTypeException extends Exception {

    public InvalidWebSocketMessageTypeException(String errorMessage) {
        super(errorMessage);
    }

    public InvalidWebSocketMessageTypeException(String errorMessage, Exception e) {
        super(errorMessage, e);
    }
}
