package com.rotem.graphsync.graph.processor.logic.web.socket;

import com.google.gson.Gson;
import com.rotem.graphsync.graph.common.enums.AckType;
import com.rotem.graphsync.graph.common.enums.WebSocketMessageType;
import com.rotem.graphsync.graph.common.models.websocket.Ack;
import com.rotem.graphsync.graph.common.models.websocket.WebSocketMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WebSocketMessageBuilder {

    private final Gson gson;

    @Autowired
    public WebSocketMessageBuilder(Gson gson) {
        this.gson = gson;
    }

    public WebSocketMessage buildOkWebSocketMessageWithAckPayload() {
        Ack ack = new Ack(AckType.OK);
        WebSocketMessage webSocketMessage = buildOkWebSocketMessage(ack);

        return webSocketMessage;
    }

    public <T> WebSocketMessage buildOkWebSocketMessage(T payload) {
        String payloadAsString = gson.toJson(payload);
        WebSocketMessage message = buildWebSocketMessage(WebSocketMessageType.OK, payloadAsString);

        return message;
    }

    public WebSocketMessage buildWebSocketMessage(WebSocketMessageType messageType, String payload) {
        System.out.println("Building web socket message of type: " + messageType);

        String responseType = messageType.toString();
        WebSocketMessage responseMessage = new WebSocketMessage(responseType, payload);

        return responseMessage;
    }
}
