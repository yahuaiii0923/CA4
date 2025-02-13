package org.example.DAOs;

import org.example.DTOs.Expense;
import org.example.Exceptions.DaoException;

import java.util.List;

public interface ExpenseDaoInterface {

    // Retrieve all expenses from the database
    List<Expense> listAllExpenses() throws DaoException;

    //Calculate total expenses
    double getTotalExpenses() throws DaoException;

    // Add a new expense record to the database
    void addExpense(Expense expense) throws DaoException;

    // Delete an expense record by its ID
    void deleteExpense(int id) throws DaoException;

    // (Optional) Retrieve expenses for a specific month
    List<Expense> listExpensesByMonth(int month, int year) throws DaoException;

    double getTotalExpensesByMonth(int month, int year) throws DaoException;



}
