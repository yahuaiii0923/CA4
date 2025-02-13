package org.example.BuisnessObjects; // Feb 2025

import org.example.DAOs.ExpenseDaoInterface;
import org.example.DAOs.IncomeDaoInterface;
import org.example.DAOs.MySqlExpenseDao;
import org.example.DAOs.MySqlIncomeDao;
import org.example.DTOs.Expense;
import org.example.DTOs.Income;
import org.example.Exceptions.DaoException;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ExpenseDaoInterface IExpenseDao = new MySqlExpenseDao();
        IncomeDaoInterface IIncomeDao = new MySqlIncomeDao();
        boolean exit = false;

        while (!exit) {
            System.out.println("----- Income & Expense Tracker -----");
            System.out.println("1. List all expenses and total spend");
            System.out.println("2. Add a new expense");
            System.out.println("3. Delete an expense by ID");
            System.out.println("4. List all income and total income");
            System.out.println("5. Add a new income");
            System.out.println("6. Delete an income by ID");
            System.out.println("7. List income & expenses for a specific month");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    listAllExpenses(IExpenseDao);
                    break;
                case 2:
                    addNewExpense(IExpenseDao, scanner);
                    break;
                case 3:
                    deleteExpense(IExpenseDao, scanner);
                    break;
                case 4:
                    listAllIncome(IIncomeDao);
                    break;
                case 5:
                    addNewIncome(IIncomeDao, scanner);
                    break;
                case 6:
                    deleteIncome(IIncomeDao, scanner);
                    break;
                case 7:
                    listIncomeAndExpensesByMonth(IExpenseDao, IIncomeDao, scanner);
                    break;
                case 0:
                    exit = true;
                    System.out.println("Exiting application...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }

    private static void listAllExpenses(ExpenseDaoInterface expenseDao) {
        try {
            List<Expense> expenses = expenseDao.listAllExpenses();
            double total = expenseDao.getTotalExpenses(); // Optimized total calculation
            System.out.println("\n--- All Expenses ---");
            for (Expense e : expenses) {
                System.out.println(e);
            }
            System.out.printf("Total Expenses: €%.2f\n", total);
        } catch (DaoException e) {
            System.out.println("Error listing expenses: " + e.getMessage());
        }
    }

    private static void addNewExpense(ExpenseDaoInterface expenseDao, Scanner scanner) {
        try {
            System.out.print("Enter title: ");
            String title = scanner.nextLine();
            System.out.print("Enter category: ");
            String category = scanner.nextLine();
            System.out.print("Enter amount: ");
            double amount = scanner.nextDouble();
            System.out.print("Enter date incurred (YYYY-MM-DD): ");
            scanner.nextLine(); // Consume newline
            String date = scanner.nextLine();

            // Ensure your DTO's constructor matches this format
            Expense newExpense = new Expense(title, category, amount, java.sql.Date.valueOf(date));
            expenseDao.addExpense(newExpense);
            System.out.println("✅ Expense added successfully!");
        } catch (DaoException e) {
            System.out.println("Error adding expense: " + e.getMessage());
        }
    }

    private static void deleteExpense(ExpenseDaoInterface expenseDao, Scanner scanner) {
        try {
            System.out.print("Enter expense ID to delete: ");
            int id = scanner.nextInt();
            expenseDao.deleteExpense(id);
            System.out.println("✅ Expense deleted successfully!");
        } catch (DaoException e) {
            System.out.println("Error deleting expense: " + e.getMessage());
        }
    }

    private static void listAllIncome(IncomeDaoInterface incomeDao) {
        try {
            List<Income> incomes = incomeDao.listAllIncome();
            double total = incomeDao.getTotalIncome(); // Optimized total calculation
            System.out.println("\n--- All Income ---");
            for (Income i : incomes) {
                System.out.println(i);
            }
            System.out.printf("Total Income: €%.2f\n", total);
        } catch (DaoException e) {
            System.out.println("Error listing income: " + e.getMessage());
        }
    }

    private static void addNewIncome(IncomeDaoInterface incomeDao, Scanner scanner) {
        try {
            System.out.print("Enter title: ");
            String title = scanner.nextLine();
            System.out.print("Enter amount: ");
            double amount = scanner.nextDouble();
            System.out.print("Enter date earned (YYYY-MM-DD): ");
            scanner.nextLine(); // Consume newline
            String date = scanner.nextLine();

            Income newIncome = new Income(title, amount, java.sql.Date.valueOf(date));
            incomeDao.addIncome(newIncome);
            System.out.println("✅ Income added successfully!");
        } catch (DaoException e) {
            System.out.println("Error adding income: " + e.getMessage());
        }
    }

    private static void deleteIncome(IncomeDaoInterface incomeDao, Scanner scanner) {
        try {
            System.out.print("Enter income ID to delete: ");
            int id = scanner.nextInt();
            incomeDao.deleteIncome(id);
            System.out.println("✅ Income deleted successfully!");
        } catch (DaoException e) {
            System.out.println("Error deleting income: " + e.getMessage());
        }
    }

    private static void listIncomeAndExpensesByMonth(ExpenseDaoInterface expenseDao, IncomeDaoInterface incomeDao, Scanner scanner) {
        try {
            System.out.print("Enter month (1-12): ");
            int month = scanner.nextInt();
            System.out.print("Enter year (YYYY): ");
            int year = scanner.nextInt();

            List<Expense> expenses = expenseDao.findExpensesByMonth(month, year);
            List<Income> incomes = incomeDao.findIncomeByMonth(month, year);

            double totalExpenses = expenseDao.getTotalExpensesByMonth(month, year);
            double totalIncome = incomeDao.getTotalIncomeByMonth(month, year);

            System.out.println("\n--- Expenses for " + month + "/" + year + " ---");
            for (Expense e : expenses) {
                System.out.println(e);
            }
            System.out.printf("Total Expenses: €%.2f\n", totalExpenses);

            System.out.println("\n--- Income for " + month + "/" + year + " ---");
            for (Income i : incomes) {
                System.out.println(i);
            }
            System.out.printf("Total Income: €%.2f\n", totalIncome);

            double balance = totalIncome - totalExpenses;
            System.out.printf("💰 Remaining Balance: €%.2f\n", balance);
        } catch (DaoException e) {
            System.out.println("Error retrieving records: " + e.getMessage());
        }
    }
}