package com.rotem.graphsync.graph.processor.logic.message.processors;

import com.rotem.graphsync.graph.common.models.graph.NodeEntity;
import com.rotem.graphsync.graph.common.models.requests.ModifyGraphRequest;
import com.rotem.graphsync.graph.common.models.requests.NodeRequest;
import com.rotem.graphsync.graph.common.models.requests.SetNodesRequest;
import com.rotem.graphsync.graph.common.models.websocket.WebSocketMessage;
import com.rotem.graphsync.graph.processor.exceptions.GraphDatabaseException;
import com.rotem.graphsync.graph.processor.exceptions.GraphNotFoundException;
import com.rotem.graphsync.graph.processor.logic.converters.NodeConverter;
import com.rotem.graphsync.graph.processor.logic.web.socket.WebSocketPayloadParser;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public abstract class ModifyGraphMessageProcessor implements MessageProcessor {

    private final WebSocketPayloadParser webSocketPayloadParser;
    private final NodeConverter nodeConverter;

    protected ModifyGraphMessageProcessor(WebSocketPayloadParser webSocketPayloadParser,
                                          NodeConverter nodeConverter) {
        this.webSocketPayloadParser = webSocketPayloadParser;
        this.nodeConverter = nodeConverter;
    }

    protected abstract CompletableFuture<WebSocketMessage> modifyGraphAsync(String graphName,
                                                                            List<NodeEntity> nodesToModify)
            throws GraphDatabaseException, GraphNotFoundException;

    @Override
    public CompletableFuture<WebSocketMessage> processMassageAsync(String message) throws Exception {
        ModifyGraphRequest modifyGraphRequest =
                webSocketPayloadParser.parseMessage(message, ModifyGraphRequest.class);

        String graphName = modifyGraphRequest.getName();
        List<NodeEntity> nodesToModify = buildNodeEntitiesToModify(modifyGraphRequest);

        CompletableFuture<WebSocketMessage> graphModifiedFuture = modifyGraphAsync(graphName, nodesToModify);

        return graphModifiedFuture;
    }

    private List<NodeEntity> buildNodeEntitiesToModify(ModifyGraphRequest modifyGraphRequest) {
        SetNodesRequest setNodesRequest = modifyGraphRequest.getSetNodesRequest();

        if (setNodesRequest == null) {
            return new LinkedList<>();
        } else {
            List<NodeRequest> nodesRequests = setNodesRequest.getNodes();
            List<NodeEntity> nodeEntitiesToModify =
                    nodeConverter.convertNodesRequestsToNodeEntities(nodesRequests);

            return nodeEntitiesToModify;
        }
    }
}
