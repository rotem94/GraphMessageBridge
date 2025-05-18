package com.rotem.graphsync.graph.processor.logic.converters;

import com.rotem.graphsync.graph.common.models.graph.Graph;
import com.rotem.graphsync.graph.common.models.graph.NodeEntity;
import com.rotem.graphsync.graph.common.models.responses.GraphResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GraphConverter {

    private final NodeConverter nodeConverter;

    public GraphConverter(NodeConverter nodeConverter) {
        this.nodeConverter = nodeConverter;
    }

    public GraphResponse convertGraphToGraphResponse(Graph graph) {
        String graphName = graph.getName();
        List<NodeEntity> nodesEntities = graph.getNodes();

        GraphResponse graphResponse = new GraphResponse();

        graphResponse.setId(graph.getId());
        graphResponse.setName(graphName);
        graphResponse.setNodes(nodeConverter.convertNodeEntitiesToNodesResponses(nodesEntities));

        return graphResponse;
    }
}
