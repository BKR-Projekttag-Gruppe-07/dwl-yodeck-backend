package de.devfelix;
import java.sql.DriverManager;
import java.sql.*;
import java.util.*;

public class MySQLManager {
    private static final String url = "jdbc:mysql://localhost:3306/";
    private static final String username = "dwluser";
    private static final String password = "Hallo.123";

    private static Connection connect() {
        while (true) {
            try {
                Connection connection = DriverManager.getConnection(url, username, password);

                try (Statement statement = connection.createStatement()) {
                    statement.executeUpdate("CREATE DATABASE IF NOT EXISTS dwlscreen");
                }

                Connection dbConnection = DriverManager.getConnection(url + "dwlscreen", username, password);
                System.out.println("Verbindungsaufbau zur Datenbank war erfolgreich.");
                return dbConnection;
            } catch (SQLException e) {
                System.out.println("Verbindungsaufbau zur Datenbank fehlgeschlagen: " + e.getMessage());
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    System.out.println("Verbindungsaufbau wurde unterbrochen.");
                    return null;
                }
            }
        }
    }

    public static List<Map<String, Object>> executeQuery(String query) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        Connection connection = connect();
        if (connection != null) {
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {

                int columnCount = resultSet.getMetaData().getColumnCount();
                while (resultSet.next()) {
                    Map<String, Object> row = new HashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        row.put(resultSet.getMetaData().getColumnName(i), resultSet.getObject(i));
                    }
                    resultList.add(row);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return resultList;
    }

    public static int executeUpdate(String query) {
        Connection connection = connect();
        if (connection != null) {
            try (Statement statement = connection.createStatement()) {
                return statement.executeUpdate(query);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return -1;
    }
}