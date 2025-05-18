package com.rotem.graphsync.graph.api.gateway.exceptions;

public class WebSocketBadRequestException extends RuntimeException {
    public WebSocketBadRequestException(String errorMessage) {
        super(errorMessage);
    }
}
