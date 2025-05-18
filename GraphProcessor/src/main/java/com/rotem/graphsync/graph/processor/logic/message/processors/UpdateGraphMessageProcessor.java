package com.rotem.graphsync.graph.processor.logic.message.processors;

import com.rotem.graphsync.graph.common.enums.WebSocketMessageType;
import com.rotem.graphsync.graph.common.models.graph.NodeEntity;
import com.rotem.graphsync.graph.common.models.websocket.WebSocketMessage;
import com.rotem.graphsync.graph.processor.exceptions.GraphDatabaseException;
import com.rotem.graphsync.graph.processor.exceptions.GraphModificationException;
import com.rotem.graphsync.graph.processor.exceptions.GraphNotFoundException;
import com.rotem.graphsync.graph.processor.logic.converters.NodeConverter;
import com.rotem.graphsync.graph.processor.logic.services.GraphService;
import com.rotem.graphsync.graph.processor.logic.web.socket.WebSocketMessageBuilder;
import com.rotem.graphsync.graph.processor.logic.web.socket.WebSocketPayloadParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class UpdateGraphMessageProcessor extends ModifyGraphMessageProcessor {

    private final GraphService graphService;
    private final WebSocketMessageBuilder webSocketMessageBuilder;

    @Autowired
    public UpdateGraphMessageProcessor(NodeConverter nodeConverter,
                                       WebSocketPayloadParser webSocketPayloadParser,
                                       GraphService graphService,
                                       WebSocketMessageBuilder webSocketMessageBuilder) {
        super(webSocketPayloadParser, nodeConverter);

        this.graphService = graphService;
        this.webSocketMessageBuilder = webSocketMessageBuilder;
    }

    @Override
    public WebSocketMessageType getWebSocketMessageType() {
        return WebSocketMessageType.UPDATE_GRAPH;
    }

    @Override
    protected CompletableFuture<WebSocketMessage> modifyGraphAsync(String graphName,
                                                                   List<NodeEntity> newGraphNodes)
            throws GraphDatabaseException, GraphNotFoundException, GraphModificationException {
        System.out.println("Got request to update graph: '" + graphName + "'");

        CompletableFuture<WebSocketMessage> newGraphFuture = graphService
                .fetchGraphAsync(graphName)
                .thenCompose(graphToUpdate -> graphService.updateGraphAsync(graphToUpdate, newGraphNodes))
                .thenApply(ignored -> webSocketMessageBuilder.buildOkWebSocketMessageWithAckPayload());

        return newGraphFuture;
    }
}
