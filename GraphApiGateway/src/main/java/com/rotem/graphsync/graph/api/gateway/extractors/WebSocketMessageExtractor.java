package com.rotem.graphsync.graph.api.gateway.extractors;

import com.google.gson.Gson;
import com.rotem.graphsync.graph.api.gateway.logic.validators.WebSocketMessageTypeValidator;
import com.rotem.graphsync.graph.common.enums.WebSocketMessageType;
import com.rotem.graphsync.graph.common.exceptions.InvalidWebSocketMessageException;
import com.rotem.graphsync.graph.common.logic.WebSocketMessageTypeParser;
import com.rotem.graphsync.graph.common.models.websocket.WebSocketMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;

@Service
public class WebSocketMessageExtractor {

    public static final String INVALID_WEB_SOCKET_MESSAGE =
            "Invalid web socket message received from service B.";

    private final Gson gson;
    private final WebSocketMessageTypeParser webSocketMessageTypeParser;
    private final WebSocketMessageTypeValidator webSocketMessageTypeValidator;

    @Autowired
    public WebSocketMessageExtractor(Gson gson,
                                     WebSocketMessageTypeParser webSocketMessageTypeParser,
                                     WebSocketMessageTypeValidator webSocketMessageTypeValidator) {
        this.gson = gson;
        this.webSocketMessageTypeParser = webSocketMessageTypeParser;
        this.webSocketMessageTypeValidator = webSocketMessageTypeValidator;
    }

    public <T> T convertWebSocketMessage(WebSocketMessage webSocketMessage, Type payloadType)
            throws InvalidWebSocketMessageException {
        String messageTypeAsString = webSocketMessage.getType();
        String message = webSocketMessage.getPayload();

        WebSocketMessageType messageType =
                webSocketMessageTypeParser.getWebSocketMessageType(messageTypeAsString);

        webSocketMessageTypeValidator.validateWebSocketMessageType(messageType, message);

        T data = gson.fromJson(message, payloadType);

        if (data == null) {
            throw new InvalidWebSocketMessageException(INVALID_WEB_SOCKET_MESSAGE);
        }

        return data;
    }
}

