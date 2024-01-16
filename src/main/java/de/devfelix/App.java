package de.devfelix;

import de.devfelix.util.Employee;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class App {

    static WebSocketServer server = new WebSocketServer(8887);

     ArrayList<String> frontendVariables = new ArrayList<>();

    public static void main(String[] args) {
        server.start();
        Employee.getCurrentEmployees();
    }
}
