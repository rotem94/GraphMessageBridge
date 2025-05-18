package com.rotem.graphsync.graph.common.models.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeleteGraphNodesRequest {

    private String name;
    private DeleteNodesRequest deleteNodesRequest;
}
