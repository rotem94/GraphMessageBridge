package com.rotem.graphsync.graph.processor.logic.message.processors;

import com.rotem.graphsync.graph.common.enums.WebSocketMessageType;
import com.rotem.graphsync.graph.common.models.requests.DeleteGraphNodesRequest;
import com.rotem.graphsync.graph.common.models.websocket.WebSocketMessage;
import com.rotem.graphsync.graph.processor.exceptions.*;
import com.rotem.graphsync.graph.processor.logic.services.GraphService;
import com.rotem.graphsync.graph.processor.logic.validators.GraphValidator;
import com.rotem.graphsync.graph.processor.logic.web.socket.WebSocketMessageBuilder;
import com.rotem.graphsync.graph.processor.logic.web.socket.WebSocketPayloadParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class DeleteGraphNodeMessageProcessor implements MessageProcessor {

    private final WebSocketPayloadParser webSocketPayloadParser;
    private final GraphService graphService;
    private final GraphValidator graphValidator;
    private final WebSocketMessageBuilder webSocketMessageBuilder;

    @Autowired
    public DeleteGraphNodeMessageProcessor(WebSocketPayloadParser webSocketPayloadParser,
                                           GraphService graphService,
                                           GraphValidator graphValidator,
                                           WebSocketMessageBuilder webSocketMessageBuilder) {
        this.webSocketPayloadParser = webSocketPayloadParser;
        this.graphService = graphService;
        this.graphValidator = graphValidator;
        this.webSocketMessageBuilder = webSocketMessageBuilder;
    }

    @Override
    public WebSocketMessageType getWebSocketMessageType() {
        return WebSocketMessageType.DELETE_GRAPH_NODES;
    }

    @Override
    public CompletableFuture<WebSocketMessage> processMassageAsync(String message)
            throws InvalidWebSocketMessagePayloadException,
            GraphDatabaseException,
            GraphNotFoundException,
            GraphNodeNotFoundException,
            GraphModificationException {
        DeleteGraphNodesRequest deleteGraphNodesRequest =
                webSocketPayloadParser.parseMessage(message, DeleteGraphNodesRequest.class);

        String graphName = deleteGraphNodesRequest.getName();
        List<UUID> nodesToDelete = deleteGraphNodesRequest.getDeleteNodesRequest().getNodesIds();

        System.out.println("Got request to delete the nodes: " + nodesToDelete +
                " from graph: '" + graphName + "'");

        CompletableFuture<WebSocketMessage> newGraphFuture = graphService
                .fetchGraphAsync(graphName)
                .thenAccept(graph ->
                        graphValidator.validateNodesWithGivenIdsExistInGraph(graph, nodesToDelete))
                .thenCompose(ignored -> graphService.deleteGraphNodesByIds(nodesToDelete))
                .thenApply(ignored -> webSocketMessageBuilder.buildOkWebSocketMessageWithAckPayload());

        return newGraphFuture;
    }
}
