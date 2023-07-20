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
import android.widget.ListView;
import android.widget.Spinner;

import com.example.projectgabi.R;
import com.example.projectgabi.adapters.BudgetItemAdapter;
import com.example.projectgabi.charts.BudgetViewCC;
import com.example.projectgabi.classes.Budget;
import com.example.projectgabi.classes.BudgetItem;
import com.example.projectgabi.classes.Category;
import com.example.projectgabi.classes.Transaction;
import com.example.projectgabi.models.BudgetController;
import com.example.projectgabi.models.CategoryController;
import com.example.projectgabi.models.TransactionController;
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
    Button backBtn, setUpBudgetBtn;
    Intent intent;
    Spinner budgetDateSpinner;
    ListView budgetItemListView;

    CombinedChart combinedChart;



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

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), MenuActivity.class);
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

        BudgetViewCC budgetCombinedChart = new BudgetViewCC();
        budgetCombinedChart.setContext(context);

        Log.d("BudgetTrackerActivity", "budget: " + budget.toString());
        Log.d("BudgetTrackerActivity", "categories: " + categories.toString());

        budgetCombinedChart.generateChart(budget, categories, combinedChart);


    }

    private void setCategoriesValues(ArrayList<Category> categories, Budget budget, ArrayList<BudgetItem> budgetItems) {

        TransactionController transactionController = new TransactionController();
        transactionController.getTransactionsFromDB(context, LoginPage.userID);
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
               // BudgetTrackerActivity.this.initializeListView( context,  categories,budgetItems, transactions,budget );
                BudgetTrackerActivity.this.initializeLV(context, categories, budgetItems, transactions, budget);

            }
        });

    }

    private void initializeLV(Context context, ArrayList<Category> categories, ArrayList<BudgetItem> budgetItems, ArrayList<Transaction> transactions, Budget budget) {
        BudgetItemAdapter budgetItemAdapter = new BudgetItemAdapter(getApplicationContext(),
                R.layout.view_butget_item,
                budgetItems,
                getLayoutInflater());
        budgetItemAdapter.setCategoryArrayList(categories);
        budgetItemAdapter.setBudget(budget);
        budgetItemAdapter.setBudgetItemArrayList(budgetItems);
        budgetItemAdapter.setTransactionArrayList(transactions);
        budgetItemAdapter.setContext(context);

        budgetItemListView.setAdapter(budgetItemAdapter);

    }

    private synchronized void initializeElements() {

        combinedChart = findViewById(R.id.budgetCombinedChart);
        budgetDateSpinner = findViewById(R.id.budgetDateSelector);
        backBtn = findViewById(R.id.budgetReturnToExpenses);
        budgetItems = new ArrayList<>();
        setUpBudgetBtn = findViewById(R.id.budgetSetUpANewBudget);
        categories = new ArrayList<>();
        context = this;
      //  budgetItemsListView = findViewById(R.id.budgetTrackerListView);
        budgetItemListView = findViewById(R.id.budgetTrackerLv);

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

//    private void initializeListView(Context context, ArrayList<Category> categories,
//                                    ArrayList<BudgetItem> budgetItems, ArrayList<Transaction> transactions ,Budget budget) {
//
//
//        BudgetItemExpandableListAdapter budgetAdapter  = new BudgetItemExpandableListAdapter(context,
//                categories,budgetItems, transactions, budget );
//
//        budgetItemsListView.setAdapter(budgetAdapter);
//        budgetItemsListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
//            int previousGroup = -1;
//            @Override
//            public void onGroupExpand(int groupPosition) {
//                if (groupPosition != previousGroup && previousGroup != -1) {
//                    budgetItemsListView.collapseGroup(previousGroup);
//                }
//                previousGroup = groupPosition;
//                Log.d("createList", "groupPosition: " + groupPosition);
//            }
//        });
//
//        HashMap<String, ArrayList<BudgetItem>> finalMap = budgetAdapter.getBudgetItemHashMap();
//        budgetItemsListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//
//            @Override
//            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
//                Log.d("budget tracker", "onChildClick: ");
//                String selected = finalMap.get(finalMap.keySet().toArray()[groupPosition]).get(childPosition).toString();
//                Log.d("Budget tracker", "onChildClick: " + selected);
//                return true;
//            }
//        });
//        }