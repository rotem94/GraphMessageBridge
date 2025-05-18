package com.rotem.graphsync.graph.common.models.graph;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Version;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@Node
public class NodeEntity {

    @Id
    private UUID id;

    @Version
    private long version;

    @Relationship(type = "CONNECTED_TO", direction = Relationship.Direction.OUTGOING)
    private List<NodeEntity> neighbors = new LinkedList<>();

    public NodeEntity(UUID id) {
        this.id = id;
    }
}
