package com.rotem.graphsync.graph.common.exceptions;

public class InvalidWebSocketMessageException extends RuntimeException {

    public InvalidWebSocketMessageException(String errorMessage) {
        super(errorMessage);
    }
}
