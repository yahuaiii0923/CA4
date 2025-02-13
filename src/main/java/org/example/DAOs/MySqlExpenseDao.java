package org.example.DAOs;

import org.example.DTOs.Expense;
import org.example.Exceptions.DaoException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySqlExpenseDao extends MySqlDao implements ExpenseDaoInterface {
    @Override
    public List<Expense> listAllExpenses() throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Expense> expenseList = new ArrayList<>();

        try {
            // Get connection object using the getConnection() method inherited from MySqlDao.java
            connection = this.getConnection();

            String query = "SELECT * FROM Expense";
            preparedStatement = connection.prepareStatement(query);

            // Execute SQL query
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int expenseID = resultSet.getInt("expenseID");
                String title = resultSet.getString("title");
                String category = resultSet.getString("category");
                double amount = resultSet.getDouble("amount");
                Date dateIncurred = resultSet.getDate("dateIncurred");

                Expense expense = new Expense(expenseID, title, category, amount, dateIncurred);
                expenseList.add(expense);
            }
        } catch (SQLException e) {
            throw new DaoException("listAllExpenses() " + e.getMessage());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    freeConnection(connection);
                }
            } catch (SQLException e) {
                throw new DaoException("listAllExpenses() " + e.getMessage());
            }
        }
        return expenseList; // may be empty
    }

    @Override
    public double getTotalExpenses() throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        double totalExpense = 0.0;

        try {
            connection = this.getConnection();
            String query = "SELECT SUM(amount) AS total FROM Expense";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                totalExpense = resultSet.getDouble("total");
            }
        } catch (SQLException e) {
            throw new DaoException("getTotalExpenses() " + e.getMessage());
        } finally {
            closeResources(resultSet, preparedStatement, connection);
        }
        return totalExpense;
    }

}
