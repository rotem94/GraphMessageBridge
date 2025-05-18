package com.rotem.graphsync.graph.processor.logic.web.socket;

import com.google.gson.Gson;
import com.rotem.graphsync.graph.common.models.websocket.WebSocketEnvelope;
import com.rotem.graphsync.graph.processor.exceptions.InvalidWebSocketEnvelopeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;

@Service
public class WebSocketEnvelopeParser {

    private final Gson gson;

    @Autowired
    public WebSocketEnvelopeParser(Gson gson) {
        this.gson = gson;
    }

    public WebSocketEnvelope parseToWebSocketEnvelope(TextMessage message)
            throws InvalidWebSocketEnvelopeException {
        try {
            String payload = message.getPayload();
            WebSocketEnvelope envelope = gson.fromJson(payload, WebSocketEnvelope.class);

            System.out.println("Got the following web socket envelope: " + envelope);
            System.out.println("Request message: " + envelope.getWebSocketMessage());
            System.out.println("Request correlation id: " + envelope.getCorrelationId());

            return envelope;
        } catch (Exception e) {
            String errorMessage = "Could not parse message: " + message.getPayload() + " to WebSocketRequest";

            throw new InvalidWebSocketEnvelopeException(errorMessage, e);
        }
    }
}
