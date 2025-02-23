package org.example.DAOs;

import org.example.DTOs.Income;
import java.util.List;
import org.example.Exceptions.DaoException;

public interface IncomeDaoInterface {
    // Retrieve all income records from the database
    List<Income> listAllIncome() throws DaoException;

    //Calculate total expenses
    double getTotalIncome() throws DaoException;

    // Add a new income record to the database
    void addIncome(Income income) throws DaoException;

    // Delete an income record by its ID
    void deleteIncome(int id) throws DaoException;

    // (Optional) Retrieve income records for a specific month
    List<Income> listIncomeByMonth(int month, int year) throws DaoException;

    //Calculate total expenses by month
    double getTotalIncomeByMonth(int month, int year) throws DaoException;
}
