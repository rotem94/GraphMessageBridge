package com.rotem.graphsync.graph.api.gateway.exceptions;

public class WebSocketConnectionException extends Exception {

    public WebSocketConnectionException(String errorMessage) {
        super(errorMessage);
    }
}
