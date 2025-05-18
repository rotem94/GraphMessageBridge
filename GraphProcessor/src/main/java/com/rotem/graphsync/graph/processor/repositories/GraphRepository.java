package com.rotem.graphsync.graph.processor.repositories;

import com.rotem.graphsync.graph.common.models.graph.Graph;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GraphRepository extends Neo4jRepository<Graph, UUID> {

    Optional<Graph> findByName(String name);
}
