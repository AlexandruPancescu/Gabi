package com.example.projectgabi.classes;

public class BudgetItem {

    private String budgetID;
private String categoryID;
    private Category category;
    private double budgetItemAmount;

    public BudgetItem() {

    }


    public BudgetItem(String budgetID, double budgetItemAmount, String categoryID) {
        this.budgetID = budgetID;
        this.budgetItemAmount = budgetItemAmount;
        this.categoryID = categoryID;
    }

    public BudgetItem(String budgetID, double budgetItemAmount, Category category) {
        this.budgetID = budgetID;
        this.budgetItemAmount = budgetItemAmount;
        this.category = category;
    }

    public String getBudgetID() {
        return budgetID;
    }

    public void setBudgetID(String budgetID) {
        this.budgetID = budgetID;
    }

    public double getBudgetItemAmount() {
        return budgetItemAmount;
    }

    public void setBudgetItemAmount(double budgetItemAmount) {
        this.budgetItemAmount = budgetItemAmount;
    }


    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    @Override
    public String toString() {
        return "BudgetItem{" +
                "budgetID='" + budgetID + '\'' +

                ", category=" + category +
                ", budgetItemAmount=" + budgetItemAmount +
                '}';
    }
}
