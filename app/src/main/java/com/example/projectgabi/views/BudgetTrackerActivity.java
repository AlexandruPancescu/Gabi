package com.example.projectgabi.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Spinner;

import com.example.projectgabi.R;
import com.example.projectgabi.adapters.BudgetItemExpandableListAdapter;
import com.example.projectgabi.charts.BudgetCombinedChart;
import com.example.projectgabi.classes.Budget;
import com.example.projectgabi.classes.BudgetItem;
import com.example.projectgabi.classes.Category;
import com.example.projectgabi.classes.Transaction;
import com.example.projectgabi.controllers.BudgetController;
import com.example.projectgabi.controllers.CategoryController;
import com.example.projectgabi.controllers.TransactionController;
import com.example.projectgabi.interfaces.BudgetCallBack;
import com.example.projectgabi.interfaces.BudgetDatesCallBack;
import com.example.projectgabi.interfaces.TransactionCallback;
import com.github.mikephil.charting.charts.CombinedChart;
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
    Button mainActivityBtn, setUpBudgetBtn;
    Intent intent;
    Spinner budgetDateSpinner;

    CombinedChart combinedChart;

    ExpandableListView budgetItemsListView;

    @Override
    protected synchronized void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_tracker);
        Log.d("BudgetTrackerActivity", "onCreate: ");

        initializeElements();
        //  retrieveElementsData(getDateFromSpinner());
        selectInterval();

        setUpBudgetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), SetUpBudgetActivity.class);
                startActivity(intent);
            }
        });

        mainActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });


    }


    private String getDateFromSpinner() {
        Log.d("BudgetTrackerActivity", "getDateFromSpinner: " + budgetDateSpinner.getSelectedItem().toString());
        return budgetDateSpinner.getSelectedItem().toString();
    }

    private synchronized void retrieveElementsData(String date) {

        String startDate = date.substring(0, 10);
        String endDate = date.substring(13, 23);
        Log.d("BudgetTrackerActivity", "retrieveElementsData: " + startDate + " " + endDate);
        budgetController = new BudgetController();
        budgetController.getBudgetBundle(context, startDate, endDate);
        //  budgetController.getBudgetBundle(context, "02-02-2023", "02-03-2023");

        budgetController.setBudgetCallBack(new BudgetCallBack() {
            @Override
            public void onReceivedBudget(Budget budget, ArrayList<BudgetItem> budgetItems, ArrayList<Category> categories) {

                Log.d("BudgetTrackerActivity", "ONCategories: " + categories.toString());
                budget.setBudgetItems(budgetItems);
                BudgetTrackerActivity.this.setCategoriesValues(categories, budget, budgetItems);



            }

        });

    }

    private void initializeCombineChart(Budget budget, ArrayList<Category> categories) {

        BudgetCombinedChart budgetCombinedChart = new BudgetCombinedChart();
        budgetCombinedChart.setContext(context);

        Log.d("BudgetTrackerActivity", "budget: " + budget.toString());
        Log.d("BudgetTrackerActivity", "categories: " + categories.toString());

        budgetCombinedChart.generateChart(budget, categories, combinedChart);


    }

    private void setCategoriesValues(ArrayList<Category> categories, Budget budget, ArrayList<BudgetItem> budgetItems) {

        TransactionController transactionController = new TransactionController();
        transactionController.getTransactionsFromDB(context);
        Log.d("BudgetTrackerActivity", "setCategoriesValues: " + categories.toString());

        transactionController.setTransactionCallback(new TransactionCallback() {
            @Override
            public void onReceivedTransaction(HashMap<String, List<Transaction>> transactionHashMap, HashMap<String,
                    List<Transaction>> transactionMapByParentCategory, ArrayList<PieEntry> categories) {

            }

            @Override
            public void getTransactions(ArrayList<Transaction> transactions) {

                for (Category category : categories) {
                    for (Transaction transaction : transactions) {
                        if (category.getCategoryName().equals(transaction.getCategory())) {
                            category.getTransactions().add(transaction);
                            category.setCategoryAmount(category.getCategoryAmount() + transaction.getValue());
                        }
                    }
                }
                BudgetTrackerActivity.this.initializeCombineChart(budget, categories);
                BudgetTrackerActivity.this.initializeListView( context,  categories,budgetItems, transactions); ;

            }
        });

    }

    private void initializeListView(Context context, ArrayList<Category> categories,
                                    ArrayList<BudgetItem> budgetItems, ArrayList<Transaction> transactions) {


        BudgetItemExpandableListAdapter budgetAdapter  = new BudgetItemExpandableListAdapter(context,  categories,budgetItems, transactions);
        budgetItemsListView.setAdapter(budgetAdapter);

        budgetItemsListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;
            @Override
            public void onGroupExpand(int groupPosition) {
                if (groupPosition != previousGroup && previousGroup != -1) {
                    budgetItemsListView.collapseGroup(previousGroup);
                }
                previousGroup = groupPosition;
                Log.d("createList", "groupPosition: " + groupPosition);
            }
        });
        }




    private synchronized void initializeElements() {

        combinedChart = findViewById(R.id.budgetCombinedChart);
        budgetDateSpinner = findViewById(R.id.budgetDateSelector);
        mainActivityBtn = findViewById(R.id.budgetReturnToExpenses);
        budgetItems = new ArrayList<>();
        setUpBudgetBtn = findViewById(R.id.budgetSetUpANewBudget);
        categories = new ArrayList<>();
        context = this;
        budgetItemsListView = findViewById(R.id.budgetTrackerListView);

    }

    private synchronized void selectInterval() {

        BudgetController budgetController = new BudgetController();
        budgetController.getBudgetDates(context);
        budgetController.setBudgetDatesCallBack(new BudgetDatesCallBack() {
            @Override
            public void onReceivedBudgetDates(ArrayList<String> budgetDates) {

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, budgetDates);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                budgetDateSpinner.setAdapter(adapter);

                budgetDateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        retrieveElementsData(getDateFromSpinner());


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                //  retrieveElementsData(getDateFromSpinner());

            }
        });

    }

}