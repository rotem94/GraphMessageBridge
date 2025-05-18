package com.rotem.graphsync.graph.api.gateway.logic.validators;

import com.rotem.graphsync.graph.api.gateway.exceptions.DataNotFoundException;
import com.rotem.graphsync.graph.api.gateway.exceptions.WebSocketBadRequestException;
import com.rotem.graphsync.graph.common.enums.WebSocketMessageType;
import com.rotem.graphsync.graph.common.exceptions.InvalidWebSocketMessageException;
import org.springframework.stereotype.Service;

@Service
public class WebSocketMessageTypeValidator {

    public void validateWebSocketMessageType(WebSocketMessageType messageType, String message) {
        switch (messageType) {
            case OK -> {
                //Message is valid; no action required
            }
            case BAD_REQUEST -> throw new WebSocketBadRequestException(message);
            case NOT_FOUND -> throw new DataNotFoundException(message);
            default -> throw new InvalidWebSocketMessageException(message);
        }
    }
}
