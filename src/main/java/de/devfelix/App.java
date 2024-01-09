package de.devfelix;

public class App {

    static WebSocketServer server = new WebSocketServer(8887);

    public static void main(String[] args) {
        server.start();
    }
}
