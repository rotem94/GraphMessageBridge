package com.rotem.graphsync.graph.api.gateway.exceptions;

public class WebSocketSendMessageException extends Exception {

    public WebSocketSendMessageException(String errorMessage, Exception e) {
        super(errorMessage, e);
    }
}
