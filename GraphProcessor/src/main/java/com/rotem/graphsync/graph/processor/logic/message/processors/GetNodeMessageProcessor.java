package com.rotem.graphsync.graph.processor.logic.message.processors;

import com.rotem.graphsync.graph.common.enums.WebSocketMessageType;
import com.rotem.graphsync.graph.common.models.websocket.WebSocketMessage;
import com.rotem.graphsync.graph.processor.exceptions.GraphDatabaseException;
import com.rotem.graphsync.graph.processor.exceptions.GraphNodeNotFoundException;
import com.rotem.graphsync.graph.processor.exceptions.InvalidWebSocketMessagePayloadException;
import com.rotem.graphsync.graph.processor.logic.converters.NodeConverter;
import com.rotem.graphsync.graph.processor.logic.services.GraphService;
import com.rotem.graphsync.graph.processor.logic.web.socket.WebSocketMessageBuilder;
import com.rotem.graphsync.graph.processor.logic.web.socket.WebSocketPayloadParser;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class GetNodeMessageProcessor implements MessageProcessor {

    private final WebSocketPayloadParser webSocketPayloadParser;
    private final GraphService graphService;
    private final NodeConverter nodeConverter;
    private final WebSocketMessageBuilder webSocketMessageBuilder;

    public GetNodeMessageProcessor(
            WebSocketPayloadParser webSocketPayloadParser,
            GraphService graphService,
            NodeConverter nodeConverter,
            WebSocketMessageBuilder webSocketMessageBuilder) {
        this.webSocketPayloadParser = webSocketPayloadParser;
        this.graphService = graphService;
        this.nodeConverter = nodeConverter;
        this.webSocketMessageBuilder = webSocketMessageBuilder;
    }

    @Override
    public WebSocketMessageType getWebSocketMessageType() {
        return WebSocketMessageType.GET_NODE;
    }

    @Override
    public CompletableFuture<WebSocketMessage> processMassageAsync(String message)
            throws InvalidWebSocketMessagePayloadException,
            GraphNodeNotFoundException,
            GraphDatabaseException {
        UUID nodeId = webSocketPayloadParser.parseMessage(message, UUID.class);

        System.out.println("Got new request to fetch the following graph node: '" + nodeId + "'");

        CompletableFuture<WebSocketMessage> responseMessageFuture = graphService
                .fetchGraphNodeAsync(nodeId)
                .thenApply(nodeConverter::convertNodeEntityToNodeRelationshipsResponse)
                .thenApply(webSocketMessageBuilder::buildOkWebSocketMessage);

        return responseMessageFuture;
    }
}
