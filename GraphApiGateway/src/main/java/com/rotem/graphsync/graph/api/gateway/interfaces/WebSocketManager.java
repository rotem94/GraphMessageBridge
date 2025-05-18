package com.rotem.graphsync.graph.api.gateway.interfaces;

import com.google.gson.Gson;
import com.rotem.graphsync.graph.api.gateway.exceptions.WebSocketConnectionException;
import com.rotem.graphsync.graph.common.models.websocket.WebSocketMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.concurrent.CompletableFuture;

@Service
public class WebSocketManager {

    private final URI serviceBUri;
    private final Gson gson;
    private final long cleanupRequestsInterval;

    private WebSocketInterface webSocketInterface;

    @Autowired
    public WebSocketManager(URI serviceBUri,
                            Gson gson,
                            @Value("${requests.cleanup.interval}") long cleanupRequestsInterval) {
        this.serviceBUri = serviceBUri;
        this.gson = gson;
        this.cleanupRequestsInterval = cleanupRequestsInterval;

        connectToServiceB();
    }

    @Scheduled(fixedRateString = "${web.socket.reconnect.interval}")
    public void scheduledReconnect() {
        connectToServiceB();
    }

    public CompletableFuture<WebSocketMessage> sendMessageAsync(WebSocketMessage webSocketMessage)
            throws WebSocketConnectionException {
        if (isConnectionClosed()) {
            throw new WebSocketConnectionException("Connection to service B is not established.");
        }

        CompletableFuture<WebSocketMessage> responseFuture =
                webSocketInterface.sendMessageAsync(webSocketMessage);

        return responseFuture;
    }

    private synchronized void connectToServiceB() {
        try {
            tryConnectingToServiceB();
        } catch (Exception e) {
            System.err.println("Unknown error occurred while connecting: " + e.getMessage());
        }
    }

    private void tryConnectingToServiceB() throws InterruptedException {
        if (isConnectionClosed()) {
            System.out.println("Trying to connect to service B");

            webSocketInterface = new WebSocketInterface(serviceBUri, gson, cleanupRequestsInterval);
            webSocketInterface.connectBlocking();
        }
    }

    private boolean isConnectionClosed() {
        return webSocketInterface == null || !webSocketInterface.isOpen();
    }
}
