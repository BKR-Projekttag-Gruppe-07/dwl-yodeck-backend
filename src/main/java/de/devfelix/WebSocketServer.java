package de.devfelix;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;

import java.net.InetSocketAddress;
import java.util.ArrayList;

public class WebSocketServer extends org.java_websocket.server.WebSocketServer {

    WebSocket connection;

    public WebSocketServer(int port) {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        System.out.println("Neue Verbindung von " + webSocket.getRemoteSocketAddress().getAddress().getHostAddress());
        connection = webSocket;
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        System.out.println("Verbindung geschlossen zu: " + webSocket.getRemoteSocketAddress().getAddress().getHostAddress());
        connection = null;
    }

    @Override
    public void onMessage(WebSocket webSocket, String s) {
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {

    }

    @Override
    public void onStart() {
        System.out.println("WebSocket Server gestartet");
    }


    private void sendString(ArrayList<String> data) {
        StringBuilder stringBuilder = new StringBuilder();

        for(String string : data) {
            stringBuilder.append(string + ";");
        }

        connection.send(stringBuilder.toString());
        System.out.println("Sende Datenstring: " + stringBuilder.toString());
    }

}
