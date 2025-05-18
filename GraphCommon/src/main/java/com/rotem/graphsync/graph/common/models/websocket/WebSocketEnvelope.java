package com.rotem.graphsync.graph.common.models.websocket;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WebSocketEnvelope {

    private WebSocketMessage webSocketMessage;
    private String correlationId;
}
