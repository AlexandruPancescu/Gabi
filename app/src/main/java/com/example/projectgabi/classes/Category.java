package com.example.projectgabi.classes;

import java.util.ArrayList;
import java.util.UUID;

public class Category {

    private String categoryID;
    private String categoryName;
    private double categoryAmount; // working name for the money spent on the category
    private String parentCategory;
    private ArrayList<Transaction> transactions = new ArrayList<>();

    public Category( String categoryName, String parentCategory) {
        this.categoryID = UUID.randomUUID().toString();
        this.categoryName = categoryName;
        this.parentCategory = parentCategory;
        this.categoryAmount = 0 ;
        this.transactions = new ArrayList<>();
    }

    public Category() {
        transactions = new ArrayList<>();
        this.categoryAmount = 0;
    }

    public Category(String categoryID, String categoryName, double categoryAmount, String parentCategory) {
        this.categoryID = categoryID;
        this.categoryName = categoryName;
        this.categoryAmount = categoryAmount;
        this.parentCategory = parentCategory;
    }

    public Category(String categoryID, String categoryName, String parentCategory) {
        this.categoryID = categoryID;
        this.categoryName = categoryName;
        this.parentCategory = parentCategory;
        transactions = new ArrayList<>();
        this.categoryAmount = 0;
    }

    public Category(String categoryID, String categoryName, ArrayList<Transaction> transactions) {
        this.categoryID = categoryID;
        this.categoryName = categoryName;
        calculateSpentAmount(transactions);
    }

    private void calculateSpentAmount(ArrayList<Transaction> transactions) {
        if (transactions == null || transactions.isEmpty()) {
            transactions = new ArrayList<>();
            this.categoryAmount = 0;
        }else{
            this.categoryAmount = 0;
            for (Transaction transaction : transactions) {
                this.categoryAmount += transaction.getValue();
            }
        }
    }


    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }


    public double getCategoryAmount() {
        return categoryAmount;
    }

    public void setCategoryAmount(double categoryAmount) {
        this.categoryAmount = categoryAmount;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }

    public String showTransactions(){
        String transactions = "";
        for (Transaction transaction : this.transactions) {
            transactions += transaction.toString() + "\n";
        }
        return transactions;
    }

    public String getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(String parentCategory) {
        this.parentCategory = parentCategory;
    }

    @Override
    public String toString() {

        return "Category{" +
                "categoryID='" + categoryID + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", categoryName='" + parentCategory + '\'' +
                ", categoryAmount=" + categoryAmount +
               +
                '}';
    }
}
