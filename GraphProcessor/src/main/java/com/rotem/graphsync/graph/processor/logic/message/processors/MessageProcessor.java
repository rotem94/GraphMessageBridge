package com.rotem.graphsync.graph.processor.logic.message.processors;

import com.rotem.graphsync.graph.common.enums.WebSocketMessageType;
import com.rotem.graphsync.graph.common.models.websocket.WebSocketMessage;

import java.util.concurrent.CompletableFuture;

public interface MessageProcessor {

    WebSocketMessageType getWebSocketMessageType();

    CompletableFuture<WebSocketMessage> processMassageAsync(String message) throws Exception;
}

