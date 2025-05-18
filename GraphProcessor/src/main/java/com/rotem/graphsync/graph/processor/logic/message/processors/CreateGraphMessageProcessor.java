package com.rotem.graphsync.graph.processor.logic.message.processors;

import com.rotem.graphsync.graph.common.enums.WebSocketMessageType;
import com.rotem.graphsync.graph.common.models.graph.Graph;
import com.rotem.graphsync.graph.common.models.graph.NodeEntity;
import com.rotem.graphsync.graph.common.models.websocket.WebSocketMessage;
import com.rotem.graphsync.graph.processor.exceptions.GraphAlreadyExistsException;
import com.rotem.graphsync.graph.processor.exceptions.GraphDatabaseException;
import com.rotem.graphsync.graph.processor.exceptions.GraphModificationException;
import com.rotem.graphsync.graph.processor.logic.converters.NodeConverter;
import com.rotem.graphsync.graph.processor.logic.services.GraphService;
import com.rotem.graphsync.graph.processor.logic.web.socket.WebSocketMessageBuilder;
import com.rotem.graphsync.graph.processor.logic.web.socket.WebSocketPayloadParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class CreateGraphMessageProcessor extends ModifyGraphMessageProcessor {

    private final GraphService graphService;
    private final WebSocketMessageBuilder webSocketMessageBuilder;

    @Autowired
    public CreateGraphMessageProcessor(WebSocketPayloadParser webSocketPayloadParser,
                                       NodeConverter nodeConverter,
                                       GraphService graphService,
                                       WebSocketMessageBuilder webSocketMessageBuilder) {
        super(webSocketPayloadParser, nodeConverter);

        this.graphService = graphService;
        this.webSocketMessageBuilder = webSocketMessageBuilder;
    }

    @Override
    public WebSocketMessageType getWebSocketMessageType() {
        return WebSocketMessageType.CREATE_GRAPH;
    }

    @Override
    protected CompletableFuture<WebSocketMessage> modifyGraphAsync(String graphName,
                                                                   List<NodeEntity> graphNodes)
            throws GraphDatabaseException, GraphAlreadyExistsException, GraphModificationException {
        Graph graphToCreate = new Graph(graphName, graphNodes);

        System.out.println("Got request to create graph: '" + graphName + "'");

        CompletableFuture<WebSocketMessage> newGraphFuture = graphService
                .doesGraphExistAsync(graphName)
                .thenAccept(doesGraphExists -> throwExceptionIfGraphExists(graphName, doesGraphExists))
                .thenCompose(ignored -> graphService.createGraphAsync(graphToCreate))
                .thenApply(ignored -> webSocketMessageBuilder.buildOkWebSocketMessageWithAckPayload());

        return newGraphFuture;
    }

    private void throwExceptionIfGraphExists(String graphName, boolean doesGraphExists) {
        if (doesGraphExists) {
            throw new GraphAlreadyExistsException("Graph '" + graphName + "' already exists");
        }
    }
}
