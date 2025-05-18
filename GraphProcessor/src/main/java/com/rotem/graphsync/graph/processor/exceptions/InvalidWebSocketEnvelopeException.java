package com.rotem.graphsync.graph.processor.exceptions;

public class InvalidWebSocketEnvelopeException extends Exception {

    public InvalidWebSocketEnvelopeException(String errorMessage) {
        super(errorMessage);
    }

    public InvalidWebSocketEnvelopeException(String errorMessage, Exception e) {
        super(errorMessage, e);
    }
}
