package com.rotem.graphsync.graph.processor.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.socket.WebSocketSession;

@Data
@AllArgsConstructor
public class WebSocketClientSession {
    private WebSocketSession session;
    private String correlationId;
}
