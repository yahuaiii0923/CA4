package org.example;        // Feb 2025

import java.sql.*;

public class Main {
    public static void main(String[] args) {
        Main.start();
    }
    public static void start() {

        String url = "jdbc:mysql://localhost/";
        String dbName = "finance_tracker";
        String userName = "root";
        String password = "";

        try (Connection conn = DriverManager.getConnection(url + dbName, userName, password)) {
            System.out.println("SUCCESS! Connected to the MySQL Database.");

            System.out.println("Closing connection...");
        } catch (SQLException ex) {
            System.out.println("Failed to connect! Make sure the MySQL server is running and the database exists.");
            //ex.printStackTrace();
        }
    }
}