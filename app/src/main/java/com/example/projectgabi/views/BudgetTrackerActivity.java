package com.example.projectgabi.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.example.projectgabi.R;
import com.example.projectgabi.classes.Budget;
import com.example.projectgabi.classes.BudgetItem;
import com.example.projectgabi.classes.Category;
import com.example.projectgabi.classes.Transaction;
import com.example.projectgabi.controllers.BudgetController;
import com.example.projectgabi.controllers.CategoryController;
import com.example.projectgabi.controllers.TransactionController;
import com.example.projectgabi.interfaces.BudgetCallBack;
import com.example.projectgabi.interfaces.TransactionCallback;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BudgetTrackerActivity extends AppCompatActivity {

    Budget budget;
    ArrayList<BudgetItem> budgetItems;
    ArrayList<Category> categories;
    Context context;
    BudgetController budgetController;
    CategoryController categoryController;
    TransactionController transactionController;


    @Override
    protected synchronized  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_tracker);
        Log.d("BudgetTrackerActivity", "onCreate: ");

        initializeElements();
        retrieveElementsData();





    }

    private synchronized void retrieveElementsData() {

        Log.d("BudgetTrackerActivity", "retrieveElementsData: ");
        budgetController = new BudgetController();
        budgetController.getBudgetBundle(context,"02-02-2023", "02-03-2023" );

        budgetController.setBudgetCallBack(new BudgetCallBack() {
            @Override
            public void onReceivedBudget(Budget budget, ArrayList<BudgetItem> budgetItems, ArrayList<Category> categories) {

                BudgetTrackerActivity.this.setCategoriesValues(categories, budget);

            }

        });






    }

    private void makeBarChart(Budget budget,  ArrayList<Category> categories) {
        Log.d("BudgetTrackerActivity", "makeBarChart: " + categories.toString());

    }

    private void setCategoriesValues(ArrayList<Category> categories, Budget budget) {

        TransactionController transactionController = new TransactionController();
        transactionController.getTransactionsFromDB(context);

        transactionController.setTransactionCallback(new TransactionCallback() {
            @Override
            public void onReceivedTransaction(HashMap<String, List<Transaction>> transactionHashMap, HashMap<String, List<Transaction>> transactionMapByParentCategory, ArrayList<PieEntry> categories) {

            }

            @Override
            public void getTransactions(ArrayList<Transaction> transactions) {

                Log.d("BudgetTrackerActivity", "getTransactions: " + transactions.toString());
                Log.d("BudgetTrackerActivity", budget.toString());

                for (Category category : categories) {
                    for (Transaction transaction : transactions) {
                        if (category.getCategoryName().equals(transaction.getCategory())) {
                            category.setCategoryAmount(category.getCategoryAmount() + transaction.getValue());
                        }
                    }
                }
                BudgetTrackerActivity.this.makeBarChart(budget, categories);
            }
        });

    }

    private  void  initializeElements() {


        budgetItems = new ArrayList<>();
        categories = new ArrayList<>();
        context = this;


    }
}