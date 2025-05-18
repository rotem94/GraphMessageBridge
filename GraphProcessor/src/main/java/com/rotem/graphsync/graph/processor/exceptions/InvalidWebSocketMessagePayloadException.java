package com.rotem.graphsync.graph.processor.exceptions;

public class InvalidWebSocketMessagePayloadException extends Exception {

    public InvalidWebSocketMessagePayloadException(String errorMessage) {
        super(errorMessage);
    }
}
