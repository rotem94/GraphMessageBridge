# 🌐 Graph API Gateway

The **Graph API Gateway** is a Spring Boot microservice that exposes REST endpoints for managing graph data and forwards the requests as WebSocket messages to the `Graph Processor` backend. It acts as a command bridge between HTTP clients and the real-time backend processor.

---

## ✅ Responsibilities

- Accepts and validates REST requests
- Converts requests to structured WebSocket messages
- Communicates with the backend processor over WebSocket
- Handles ACKs and error responses
- Exposes Swagger documentation for all available endpoints

---

## 📡 REST Endpoints

| Method | Path                   | Description              |
| ------ | ---------------------- | ------------------------ |
| POST   | `/api/v1/graphs`       | Create a new graph       |
| PUT    | `/api/v1/graphs`       | Update an existing graph |
| DELETE | `/api/v1/graphs`       | Delete a graph           |
| DELETE | `/api/v1/graphs/nodes` | Delete nodes from graph  |
| GET    | `/api/v1/graphs/{id}`  | Get full graph           |
| GET    | `/api/v1/nodes/{id}`   | Get node descendants     |

---

## 🧱 Architecture

```
com.rotem.graphsync.graph.api.gateway
├── controllers            # REST controller
├── logic.managers         # Coordinates graph operations
├── logic.request.handlers # Per-operation handlers
├── interfaces             # WebSocket interaction abstractions
├── exceptions             # Custom domain exceptions
├── extractors             # Message formatting logic
├── configuration          # Swagger + URI config
├── utils                  # Random ID generation
```

---

## 🚀 Running the Service

### With Maven:

```bash
mvn spring-boot:run
```

### With Docker:

```bash
docker build -t graph-api-gateway .
docker run -p 8081:8081 graph-api-gateway
```

---

## 🔧 Configuration

```properties
spring.application.name=graph-api-gateway
server.port=8081
graph.processor.websocket.url=ws://localhost:8080/ws/graph
```

---

## 📘 Swagger UI

> View API docs at:

```
http://localhost:8081/swagger-ui/index.html
```

---

## 📦 Dependencies

- Spring Boot Web
- Spring WebSocket Client
- Spring Validation
- Springdoc OpenAPI
- Gson
- `graph-common` (shared DTOs and protocol)

---
