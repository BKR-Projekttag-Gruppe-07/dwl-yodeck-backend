package de.devfelix;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;

import java.net.InetSocketAddress;
import java.util.ArrayList;

public class WebSocketServer extends org.java_websocket.server.WebSocketServer {

    private ArrayList<WebSocket> connections = new ArrayList<>();

    public WebSocketServer(int port) {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        System.out.println("Neue Verbindung zu " + webSocket.getRemoteSocketAddress().getAddress().getHostAddress());
        connections.add(webSocket);
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        System.out.println("Verbindung geschlossen zu: " + webSocket.getRemoteSocketAddress().getAddress().getHostAddress());
        connections.remove(webSocket);
    }

    @Override
    public void onMessage(WebSocket webSocket, String s) {

        System.out.println("Empfangener String: " + s);

        broadcastMessage(s);
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {
        e.printStackTrace();
    }

    @Override
    public void onStart() {
        System.out.println("WebSocket Server gestartet");
    }

    private void broadcastMessage(String message) {
        for (WebSocket connection : connections) {
            connection.send(message);
        }
        System.out.println("Sende Datenstring an alle Clients: " + message);
    }
}