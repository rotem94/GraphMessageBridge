package com.rotem.graphsync.graph.common.logic;

import com.rotem.graphsync.graph.common.enums.WebSocketMessageType;
import com.rotem.graphsync.graph.common.exceptions.InvalidWebSocketMessageException;
import org.springframework.stereotype.Service;

@Service
public class WebSocketMessageTypeParser {

    private static final String ERROR_MESSAGE = "Got invalid web socket message type";

    public WebSocketMessageType getWebSocketMessageType(String messageTypeAsString) {
        if (messageTypeAsString == null) {
            throw new InvalidWebSocketMessageException(ERROR_MESSAGE + ": null");
        }

        try {
            WebSocketMessageType messageResponseType = WebSocketMessageType.valueOf(messageTypeAsString);

            return messageResponseType;
        } catch (IllegalArgumentException e) {
            throw new InvalidWebSocketMessageException(ERROR_MESSAGE + ": '" + messageTypeAsString + "'");
        }
    }
}
