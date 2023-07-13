package com.example.projectgabi.views;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projectgabi.R;
import com.example.projectgabi.classes.Transaction;
import com.example.projectgabi.classes.User;
import com.example.projectgabi.controllers.TransactionController;
import com.example.projectgabi.interfaces.TransactionCallback;
import com.example.projectgabi.adapters.TransactionExpandableListAdapter;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TransactionCallback {
    Intent intent;
    TextView tvWelcome, tvTest;
    Button addTransactionBtn, accountActivityBtn, createBudgetBtn;
    PieChart pieChart;
    PieData pieData;
    ExpandableListView expandableListView;
    // List transactionList;
    HashMap<String, List<Transaction>> transactionMap;
    ArrayList<PieEntry> categories;
    User user;
    TransactionExpandableListAdapter expandableListAdapter;
    HashMap<String, List<Transaction>> metaCategoryMap;
    ImageView deleteTransactionIV;


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_main);
        initComponents();
        initializeElements();
        intent = getIntent();



        if (transactionMap == null) {
            Toast.makeText(this, "not empty", Toast.LENGTH_SHORT).show();
            Log.d("transactionMap", "not empty");
            //createList();
        }


        if (intent.hasExtra(LoginPage.USERNAME_KEY)) {
            //tvWelcome.setText("Welcome, " + intent.getStringExtra(LoginPage.USERNAME_KEY));
            String welcomeMessaage = String.format(getResources().getString(R.string.welcome_message), intent.getStringExtra(LoginPage.USERNAME_KEY));
            tvWelcome.setText(welcomeMessaage);
        }



        addTransactionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), TransactionActivity.class);
                startActivity(intent);
            }
        });

        // go to the account activity
        accountActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), AccountActivity.class);
                startActivity(intent);
            }
        });

        createBudgetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent = new Intent(getApplicationContext(), BudgetTrackerActivity.class);

                startActivity(intent);
            }
        });

    }

    private void initializeElements() {

        TransactionController transactionController = new TransactionController();
        transactionController.getTransactionsFromDB(this);
        transactionController.setTransactionCallback(new TransactionCallback() {
            @Override
            public void onReceivedTransaction(HashMap<String, List<Transaction>> transactionHashMap, HashMap<String, List<Transaction>> transactionMapByParentCategory,
                                              ArrayList<PieEntry> categories) {
                Log.d("Main interface test", "test");
                // showMaps(categories, transactionMapByParentCategory);
                addChartValues(categories);
                initPieChart(categories);
                createList(transactionMapByParentCategory);
                transactionMap = transactionHashMap;

            }

            @Override
            public void getTransactions(ArrayList<Transaction> transactions) {

            }
        });
        transactionMap = transactionController.getTransactionMap();
        metaCategoryMap = transactionController.getTransactionMapByParentCategory();
        categories = (ArrayList<PieEntry>) transactionController.getCategoriesPieEntries();


        addChartValues(categories);


    }





    public void initComponents() {
        //transactionList = new ArrayList<>();

        user = new User();
        this.transactionMap = new HashMap<>();
        pieChart = findViewById(R.id.mainPieChart);
        tvTest = findViewById(R.id.testRecycleView);
        tvWelcome = findViewById(R.id.mainWelcomeText);
        addTransactionBtn = findViewById(R.id.mainAddTransactionButton);
        accountActivityBtn = findViewById(R.id.mainAccountButton);
        metaCategoryMap = new HashMap<>();
        expandableListView = findViewById(R.id.mainTransactionELV);
        deleteTransactionIV = findViewById(R.id.transactionDeleteIV);
        createBudgetBtn = findViewById(R.id.mainCreateBudgetBtn);

    }




    private synchronized void addChartValues(ArrayList<PieEntry> categories) {

        if (transactionMap.isEmpty() || transactionMap == null) {

            //  Toast.makeText(getApplicationContext(), "No transactions, you can add how many you want!", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Log.d("Main transactions", "transactions found");
        }

        if (categories == null) {
            categories = new ArrayList<>();
        }

        // add the value of each object, based on the key set, then  make a new Pientry object and add it to the list

        for (String category : transactionMap.keySet()) {
            double value = 0;
            for (Transaction transaction : transactionMap.get(category)) {
                value += transaction.getValue();
            }
            categories.add(new PieEntry((float) value, category));
        }

        // Pie chart data set
        PieDataSet pieDataSet = new PieDataSet(categories, "Transactions by category");


    }


    private void createList(HashMap<String, List<Transaction>> metaCategoryMap) {

        ArrayList<String> keys = new ArrayList<>(metaCategoryMap.keySet());

        expandableListAdapter = new TransactionExpandableListAdapter(getApplicationContext(), keys, metaCategoryMap);
        // expandableListAdapter.setTransactionHashMap(metaCategoryMap);
        Log.d("Main create list ", metaCategoryMap.toString());

        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            int previousGroup = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if (groupPosition != previousGroup && previousGroup != -1) {
                    expandableListView.collapseGroup(previousGroup);
                }
                previousGroup = groupPosition;
                Log.d("createList", "groupPosition: " + groupPosition);
            }
        });

        HashMap<String, List<Transaction>> finalMetaCategoryMap = metaCategoryMap;
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                String selected = finalMetaCategoryMap.get(finalMetaCategoryMap.keySet().toArray()[groupPosition]).get(childPosition).toString();
                Toast.makeText(getApplicationContext(), selected, Toast.LENGTH_SHORT).show();
                Log.d("createList", "selected: " + selected);

                return true;
            }


        });
        metaCategoryMap = expandableListAdapter.getTransactionHashMap();


    }


    public static void showMaps(ArrayList<PieEntry> categories, HashMap<String, List<Transaction>> transactionMap) {

        Log.d("Main TransactionMap Static method: ", transactionMap.toString());
        // this.addChartValues(categories);
        if (transactionMap.isEmpty() || transactionMap == null) {

            return;
        } else {
            Log.d("Main transactions", "transactions found");
        }

        if (categories == null) {
            categories = new ArrayList<>();
        }

        // add the value of each object, based on the key set, then  make a new Pientry object and add it to the list

        for (String category : transactionMap.keySet()) {
            double value = 0;
            for (Transaction transaction : transactionMap.get(category)) {
                value += transaction.getValue();
            }
            categories.add(new PieEntry((float) value, category));
        }

        // Pie chart data set
        PieDataSet pieDataSet = new PieDataSet(categories, "Transactions by category");
    }

    @Override
    public void onReceivedTransaction(HashMap<String, List<Transaction>> transactionHashMap, HashMap<String, List<Transaction>> transactionMapByParentCategory, ArrayList<PieEntry> categories) {
        Log.d("Main onTransactionReceived", "onTransactionReceived: " + transactionHashMap.toString());
        Log.d("Main onTransactionReceived", "onTransactionReceived by category: " + transactionMapByParentCategory.toString());
        this.transactionMap = transactionHashMap;
        this.metaCategoryMap = transactionMapByParentCategory;

    }

    @Override
    public void getTransactions(ArrayList<Transaction> transactions) {

    }

    public void initPieChart(ArrayList<PieEntry> categories) {
        Log.d("Main transactions", "size : " + transactionMap.size());
        Resources res = getResources();
        PieDataSet pieDataSet = new PieDataSet(categories, "Categories");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(R.color.white);
        pieDataSet.setValueTextSize(16f);

        pieDataSet.setValueFormatter(new DefaultValueFormatter(0));

        pieData = new PieData(pieDataSet);

        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("Categories");
        pieChart.animate();
        pieChart.invalidate();
    }
}

