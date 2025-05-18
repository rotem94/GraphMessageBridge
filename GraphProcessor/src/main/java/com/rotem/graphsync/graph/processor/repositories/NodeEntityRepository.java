package com.rotem.graphsync.graph.processor.repositories;

import com.rotem.graphsync.graph.common.models.graph.NodeEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NodeEntityRepository extends Neo4jRepository<NodeEntity, UUID> {

}
