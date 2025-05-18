package com.rotem.graphsync.graph.common.models.responses;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class NodeRelationshipsResponse {

    private UUID id;
    private List<UUID> descendants;
    private List<EdgeResponse> edges;
}
