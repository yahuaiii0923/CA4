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
                System.err.println("Error closing resources: " + e.getMessage());
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
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        return totalExpense;
    }

    @Override
    public void addExpense(Expense expense) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = this.getConnection();

            String query = "INSERT INTO Expense (title, category, amount, dateIncurred) VALUES (?, ?, ?)";
            preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, expense.getTitle());
            preparedStatement.setDouble(2, expense.getAmount());
            preparedStatement.setString(3, expense.getCategory());
            preparedStatement.setDate(4,  new java.sql.Date(expense.getDateIncurred().getTime()));


            preparedStatement.executeUpdate();

            // Retrieve the generated expenseID
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    expense.setExpenseID(generatedKeys.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new DaoException("addExpense() " + e.getMessage());
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    freeConnection(connection);
                }
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    @Override
    public void deleteExpense(int expenseID) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = this.getConnection();

            String query = "DELETE FROM Expense WHERE expenseID = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, expenseID);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected == 0) {
                throw new DaoException("No expense found with ID: " + expenseID);
            }

        } catch (SQLException e) {
            throw new DaoException("deleteExpense() " + e.getMessage());
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    freeConnection(connection);
                }
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }
    @Override
    public List<Expense> listExpensesByMonth(int month, int year) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Expense> expenseList = new ArrayList<>();

        try {
            connection = this.getConnection();

            String query = "SELECT * FROM Expense WHERE MONTH(dateIncurred) = ? AND YEAR(dateIncurred) = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, month);
            preparedStatement.setInt(2, year);

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
            throw new DaoException("listExpensesByMonth() " + e.getMessage());
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
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        return expenseList; // may be empty
    }

    @Override
    public double getTotalExpensesByMonth(int month, int year) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        double totalExpense = 0.0;

        try {
            connection = this.getConnection();
            String query = "SELECT SUM(amount) AS total FROM Expense WHERE MONTH(dateIncurred) = ? AND YEAR(dateIncurred) = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, month);
            preparedStatement.setInt(2, year);

            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                totalExpense = resultSet.getDouble("total");
            }
        } catch (SQLException e) {
            throw new DaoException("getTotalExpensesByMonth() " + e.getMessage());
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) freeConnection(connection);
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        return totalExpense;
    }

}