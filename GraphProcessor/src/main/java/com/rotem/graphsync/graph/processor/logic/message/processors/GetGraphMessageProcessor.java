package com.rotem.graphsync.graph.processor.logic.message.processors;

import com.rotem.graphsync.graph.common.enums.WebSocketMessageType;
import com.rotem.graphsync.graph.common.models.websocket.WebSocketMessage;
import com.rotem.graphsync.graph.processor.exceptions.GraphDatabaseException;
import com.rotem.graphsync.graph.processor.exceptions.GraphNotFoundException;
import com.rotem.graphsync.graph.processor.logic.converters.GraphConverter;
import com.rotem.graphsync.graph.processor.logic.services.GraphService;
import com.rotem.graphsync.graph.processor.logic.web.socket.WebSocketMessageBuilder;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class GetGraphMessageProcessor implements MessageProcessor {

    private final GraphService graphService;
    private final WebSocketMessageBuilder webSocketMessageBuilder;
    private final GraphConverter graphConverter;

    public GetGraphMessageProcessor(GraphService graphService,
                                    WebSocketMessageBuilder webSocketMessageBuilder,
                                    GraphConverter graphConverter) {
        this.graphService = graphService;
        this.webSocketMessageBuilder = webSocketMessageBuilder;
        this.graphConverter = graphConverter;
    }

    @Override
    public WebSocketMessageType getWebSocketMessageType() {
        return WebSocketMessageType.GET_GRAPH;
    }

    @Override
    public CompletableFuture<WebSocketMessage> processMassageAsync(String message)
            throws GraphDatabaseException, GraphNotFoundException {
        String graphName = message;

        System.out.println("Got new request to fetch the following graph: '" + graphName + "'");

        CompletableFuture<WebSocketMessage> responseMessageFuture = graphService
                .fetchGraphAsync(graphName)
                .thenApply(graphConverter::convertGraphToGraphResponse)
                .thenApply(webSocketMessageBuilder::buildOkWebSocketMessage);

        return responseMessageFuture;
    }
}
