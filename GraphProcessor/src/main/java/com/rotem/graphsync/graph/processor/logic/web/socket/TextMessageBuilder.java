package com.rotem.graphsync.graph.processor.logic.web.socket;

import com.google.gson.Gson;
import com.rotem.graphsync.graph.common.enums.WebSocketMessageType;
import com.rotem.graphsync.graph.common.models.websocket.WebSocketEnvelope;
import com.rotem.graphsync.graph.common.models.websocket.WebSocketMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;

@Service
public class TextMessageBuilder {

    private final WebSocketMessageBuilder webSocketMessageBuilder;
    private final Gson gson;

    @Autowired
    public TextMessageBuilder(
            WebSocketMessageBuilder webSocketMessageBuilder, Gson gson) {
        this.webSocketMessageBuilder = webSocketMessageBuilder;
        this.gson = gson;
    }

    public TextMessage buildErrorTextMessage(WebSocketMessageType webSocketMessageType,
                                             String errorMessage,
                                             String correlationId) {
        WebSocketMessage response =
                webSocketMessageBuilder.buildWebSocketMessage(webSocketMessageType, errorMessage);

        TextMessage responseMessage = buildTextMessage(response, correlationId);

        return responseMessage;
    }

    public TextMessage buildTextMessage(WebSocketMessage responseMessage, String correlationId) {
        WebSocketEnvelope responseEnvelope = new WebSocketEnvelope(responseMessage, correlationId);

        String responseEnvelopeAsString = gson.toJson(responseEnvelope);
        TextMessage wrappedResponseEnvelope = new TextMessage(responseEnvelopeAsString);

        return wrappedResponseEnvelope;
    }
}
