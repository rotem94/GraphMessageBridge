package com.rotem.graphsync.graph.processor.logic.message.processors;

import com.rotem.graphsync.graph.common.enums.WebSocketMessageType;
import com.rotem.graphsync.graph.common.models.websocket.WebSocketMessage;
import com.rotem.graphsync.graph.processor.exceptions.GraphDatabaseException;
import com.rotem.graphsync.graph.processor.exceptions.GraphModificationException;
import com.rotem.graphsync.graph.processor.exceptions.GraphNotFoundException;
import com.rotem.graphsync.graph.processor.logic.services.GraphService;
import com.rotem.graphsync.graph.processor.logic.web.socket.WebSocketMessageBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class DeleteGraphMessageProcessor implements MessageProcessor {

    private final GraphService graphService;
    private final WebSocketMessageBuilder webSocketMessageBuilder;

    @Autowired
    public DeleteGraphMessageProcessor(GraphService graphService,
                                       WebSocketMessageBuilder webSocketMessageBuilder) {
        this.graphService = graphService;
        this.webSocketMessageBuilder = webSocketMessageBuilder;
    }

    @Override
    public WebSocketMessageType getWebSocketMessageType() {
        return WebSocketMessageType.DELETE_GRAPH;
    }

    @Override
    public CompletableFuture<WebSocketMessage> processMassageAsync(String message)
            throws GraphDatabaseException, GraphNotFoundException, GraphModificationException {
        String graphName = message;

        System.out.println("Got new request to delete the following graph: '" + graphName + "'");

        CompletableFuture<WebSocketMessage> responseMessageFuture = graphService
                .fetchGraphAsync(graphName)
                .thenCompose(graphService::deleteGraphAsync)
                .thenApply(ignored -> webSocketMessageBuilder.buildOkWebSocketMessageWithAckPayload());

        return responseMessageFuture;
    }
}
