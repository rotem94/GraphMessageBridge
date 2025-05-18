package com.rotem.graphsync.graph.common.models.graph;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Version;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@Node
public class Graph {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    @Version
    private long version;

    @Relationship(type = "CONTAINS", direction = Relationship.Direction.OUTGOING)
    private List<NodeEntity> nodes = new LinkedList<>();

    public Graph(String name, List<NodeEntity> nodes) {
        this.name = name;
        this.nodes = nodes;
    }
}
