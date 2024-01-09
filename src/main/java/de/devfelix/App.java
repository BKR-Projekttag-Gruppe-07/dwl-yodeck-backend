package de.devfelix;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class App extends WebSocketServer {

    private Set<WebSocket> conns;

    public App(int port) {
        super(new InetSocketAddress(port));
        conns = Collections.newSetFromMap(new ConcurrentHashMap<>());
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        conns.add(conn);
        System.out.println("Neue Verbindung von " + conn.getRemoteSocketAddress().getAddress().getHostAddress());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        conns.remove(conn);
        System.out.println("Verbindung geschlossen zu " + conn.getRemoteSocketAddress().getAddress().getHostAddress());
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("Nachricht von Client: " + message);
        // Echo-Nachricht zurück an den Client senden
        conn.send("Echo: " + message);
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
        if (conn != null) {
            conns.remove(conn);
        }
    }

    @Override
    public void onStart() {

    }

    public void startServer() {
        start();
        System.out.println("WebSocket Server gestartet!");
    }

    public static void main(String[] args) {
        App server = new App(8887); // Port 8887
        server.startServer();
    }
}
