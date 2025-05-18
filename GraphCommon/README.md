# 📦 GraphCommon (Shared Library)

`GraphCommon` is a shared library used by the graph microservices system. It contains common models, enums,
message types, validation logic, and configurations that are reused across both services: the API Gateway and
the Graph Processor.

---

## ✅ What's Inside

### 📄 Models

- Core entities: `Graph`, `NodeEntity`
- DTOs: `SetNodesRequest`, `DeleteGraphNodesRequest`, etc.
- WebSocket types: `WebSocketEnvelope`, `WebSocketMessage`, `Ack`, etc.

### 🔁 Enums

- `WebSocketMessageType`: defines supported operations (e.g. `SET_NODES`, `DELETE_GRAPH`)
- `AckType`: describes acknowledgment result types

### 🔐 Validation

- Annotations: `@UniqueNodeIds`, `@ValidNodesNeighbours`
- Matching validators to enforce graph structure constraints

### ⚙️ Configuration

- `AsyncConfig`: enables Spring async task execution
- `GsonConfig`: configures JSON (Gson) serialization

### ❗ Exceptions

- `InvalidWebSocketMessageException`: used when malformed messages are received

### 🔀 Logic

- `WebSocketMessageTypeParser`: safely maps incoming message type strings to enums

---

## 📦 Package Structure (partial)

```
com.rotem.graphsync.graph.common
├── annotations
├── configuration
├── enums
├── exceptions
├── logic
├── models
│   ├── graph
│   ├── requests
│   ├── responses
│   └── websocket
├── validators
```

---

## 🔗 Used By

- **Graph API Gateway** — uses DTOs and validation when exposing REST endpoints
- **Graph Processor** — consumes the shared models and protocol types for processing commands

---

## 📥 Usage

Add this as a Maven dependency in your other modules:

```xml

<dependency>
    <groupId>com.rotem.graphsync</groupId>
    <artifactId>graph-common</artifactId>
    <version>1.0.0</version>
</dependency>
```

---
