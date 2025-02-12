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

            Statement statement = conn.createStatement();
            String sqlQuery = "SELECT * FROM expenses";
            ResultSet resultSet = statement.executeQuery( sqlQuery );
            while ( resultSet.next() )
            {
                int expenseID = resultSet.getInt("expenseID");
                String title = resultSet.getString("title");
                String category = resultSet.getString("category");
                double amount = resultSet.getDouble("amount");
                Date dateIncurred = resultSet.getDate("dateIncurred");


                System.out.print("Expense ID = " + expenseID + ", ");
                System.out.print("Title = " + title + ", ");
                System.out.print("Category = " + category + ", ");
                System.out.println("Amount : " + amount);
                System.out.println("Date Incurred : " + dateIncurred);
            }

            System.out.println("Closing connection...");
        } catch (SQLException ex) {
            System.out.println("Failed to connect! Make sure the MySQL server is running and the database exists.");
            //ex.printStackTrace();
        }
    }
}