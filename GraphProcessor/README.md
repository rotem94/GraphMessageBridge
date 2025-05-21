# ⚙️ Graph Processor

The **Graph Processor** is a Spring Boot microservice responsible for handling graph-related commands received over WebSocket. It validates and processes incoming messages, persists graph structures to a Neo4j graph database, and supports efficient graph traversal and relationship queries. It responds with acknowledgments or data payloads such as node descendants, enabling real-time, graph-driven workflows.

This service works alongside the `graph-api-gateway`, which receives REST requests and forwards them as WebSocket messages.

---

## ✅ Responsibilities

- Parse and validate structured WebSocket messages (via `WebSocketEnvelope`)
- Route each message to the correct handler using `MessageProcessorFactory`
- Store graph and node entities in PostgreSQL via Spring Data repositories
- Return structured `ACK` or data responses through WebSocket

---

## 🧩 WebSocket Protocol

Each WebSocket message follows this structure:

```json
{
  "webSocketMessage": {
    "type": "CREATE_GRAPH",
    "payload": "{...}"
  },
  "correlationId": "abc-123"
}
```

Supported `type` values:

- `CREATE_GRAPH`
- `UPDATE_GRAPH`
- `DELETE_GRAPH`
- `DELETE_GRAPH_NODES`
- `GET_GRAPH`
- `GET_NODE`

---

## 🗃️ Data Layer

- **Database:** PostgreSQL
- **Entities:** `Graph`, `NodeEntity`
- **Repositories:**
  - `GraphRepository`
  - `NodeEntityRepository`
- Persistence powered by Spring Data JPA

---

## 📦 Package Structure (summary)

```
com.rotem.graphsync.graph.processor
├── configuration            # WebSocket + processor config
├── exceptions               # Domain-specific error types
├── logic
│   ├── services             # GraphService - main business logic
│   ├── converters           # DTO/entity mapping helpers
│   ├── validators           # GraphValidator for node/edge integrity
│   └── message/processors   # Handlers for each message type
├── web.socket               # Envelope/message parsing, builders
├── models                   # WebSocketClientSession (per connection state)
├── repositories             # JPA repositories for Graph/NodeEntity
├── utils                    # StackHashMap utility for traversal
```

---

## 🚀 Running the Service

### ✅ With Maven:

```bash
mvn spring-boot:run
```

### 🐳 With Docker:

```bash
docker build -t graph-processor .
docker run -p 8080:8080 graph-processor
```

> Default port: `8080`

---

## ⚙️ Configuration

### `application.properties`

```properties
spring.application.name=graph-processor

spring.datasource.url=jdbc:postgresql://localhost:5432/graphdb
spring.datasource.username=your_user
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

WebSocket endpoint: `/ws/graph`

---

## 🔗 Dependencies

- [`graph-common`](../graph-common): contains shared message types, DTOs, enums, and validators
- Spring Boot WebSocket
- Spring Data JPA
- PostgreSQL
- Gson

---

## 🧠 Notes

This service uses a message-type-based dispatch system and is designed for clean separation of concerns via modular processors. It is stateless at the session level but persists graph state via a relational backend.

---
