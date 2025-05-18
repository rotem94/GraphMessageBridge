package com.rotem.graphsync.graph.common.models.responses;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class GraphResponse {
    private UUID id;
    private String name;
    private List<NodeResponse> nodes;
}
