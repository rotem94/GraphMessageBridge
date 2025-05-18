package com.rotem.graphsync.graph.common.models.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModifyGraphRequest {

    @NotNull(message = "Graph name must be provided")
    private String name;

    @NotNull(message = "Nodes request must be provided")
    @Valid
    private SetNodesRequest setNodesRequest;
}
