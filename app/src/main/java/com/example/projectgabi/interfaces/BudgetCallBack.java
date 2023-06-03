package com.example.projectgabi.interfaces;

import com.example.projectgabi.classes.Budget;
import com.example.projectgabi.classes.BudgetItem;
import com.example.projectgabi.classes.Category;

import java.util.ArrayList;

public interface BudgetCallBack {

    void onReceivedBudget(Budget budget,ArrayList<BudgetItem> budgetItems, ArrayList<Category> categories);



}
