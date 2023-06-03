package com.example.projectgabi.classes;

import java.util.ArrayList;
import java.util.Date;

public class Budget {

    private String budgetID;
    private Date startDate;
    private Date endDate;
    private String userId;
    private ArrayList<BudgetItem> budgetItems;

    public Budget(String budgetID, Date startDate, Date endDate, String userId) {
        this.budgetID = budgetID;
        this.startDate = startDate;
        this.endDate = endDate;
        this.userId = userId;
    }

    public Budget() {
        budgetItems = new ArrayList<>();
    }

    public Budget(String budgetID, Date startDate, Date endDate, String userId, ArrayList<BudgetItem> budgetItems) {
        this.budgetID = budgetID;
        this.startDate = startDate;
        this.endDate = endDate;
        this.userId = userId;
        this.budgetItems = budgetItems;
    }

    public String getBudgetID() {
        return budgetID;
    }

    public void setBudgetID(String budgetID) {
        this.budgetID = budgetID;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ArrayList<BudgetItem> getBudgetItems() {
        return budgetItems;
    }

    public void setBudgetItems(ArrayList<BudgetItem> budgetItems) {
        this.budgetItems = budgetItems;
    }

    @Override
    public String toString() {
        return "Budget{" +
                "budgetID='" + budgetID + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", accountID='" + userId + '\'' +
                ", budgetItems=" + budgetItems +
                '}';
    }
}
