package org.example.DAOs;

import org.example.DTOs.Income;
import org.example.Exceptions.DaoException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySqlIncomeDao extends MySqlDao implements IncomeDaoInterface {
    @Override
    public List<Income> listAllIncome() throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Income> incomeList = new ArrayList<>();

        try {
            // Get connection object using the getConnection() method inherited from MySqlDao.java
            connection = this.getConnection();

            String query = "SELECT * FROM Income";
            preparedStatement = connection.prepareStatement(query);

            // Execute SQL query
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int incomeID = resultSet.getInt("incomeID");
                String title = resultSet.getString("title");
                double amount = resultSet.getDouble("amount");
                Date dateEarned = resultSet.getDate("dateEarned");

                Income income = new Income(incomeID, title, amount, dateEarned);
                incomeList.add(income);
            }
        } catch (SQLException e) {
            throw new DaoException("listAllIncome() " + e.getMessage());
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
        return incomeList; // may be empty
    }

    @Override
    public double getTotalIncome() throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        double totalIncome = 0.0;

        try {
            connection = this.getConnection();
            String query = "SELECT SUM AS total FROM Income";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                totalIncome = resultSet.getDouble("total");
            }
        } catch (SQLException e) {
            throw new DaoException("getTotalIncome() " + e.getMessage());
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
        return totalIncome;
    }

    @Override
    public void addIncome(Income income) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = this.getConnection();

            String query = "INSERT INTO Income VALUES (?, ?, ?)";
            preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, income.getTitle());
            preparedStatement.setDouble(2, income.getAmount());
            preparedStatement.setDate(3,  new java.sql.Date(income.getDateEarned().getTime()));


            preparedStatement.executeUpdate();

            // Retrieve the generated incomeID
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    income.setIncomeID(generatedKeys.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new DaoException("addIncome() " + e.getMessage());
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
    public void deleteIncome(int incomeID) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = this.getConnection();

            String query = "DELETE FROM Income WHERE incomeID = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, incomeID);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected == 0) {
                throw new DaoException("No income found with ID: " + incomeID);
            }

        } catch (SQLException e) {
            throw new DaoException("deleteIncome() " + e.getMessage());
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
    public List<Income> listIncomeByMonth(int month, int year) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Income> incomeList = new ArrayList<>();

        try {
            connection = this.getConnection();

            String query = "SELECT * FROM Income WHERE MONTH = ? AND YEAR = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, month);
            preparedStatement.setInt(2, year);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int incomeID = resultSet.getInt("incomeID");
                String title = resultSet.getString("title");
                double amount = resultSet.getDouble("amount");
                Date dateEarned = resultSet.getDate("dateEarned");

                Income income = new Income(incomeID, title, amount, dateEarned);
                incomeList.add(income);
            }
        } catch (SQLException e) {
            throw new DaoException("listIncomeByMonth() " + e.getMessage());
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
        return incomeList; // may be empty
    }

    @Override
    public double getTotalIncomeByMonth(int month, int year) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        double totalIncome = 0.0;

        try {
            connection = this.getConnection();
            String query = "SELECT SUM(amount) AS total FROM Income WHERE MONTH(dateEarned) = ? AND YEAR(dateEarned) = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, month);
            preparedStatement.setInt(2, year);

            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                totalIncome = resultSet.getDouble("total");
            }
        } catch (SQLException e) {
            throw new DaoException("getTotalIncomeByMonth() " + e.getMessage());
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) freeConnection(connection);
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        return totalIncome;
    }

}