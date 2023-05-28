package com.example.projectgabi;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.projectgabi.Controllers.TransactionController;
import com.example.projectgabi.Interfaces.TransactionCallback;
import com.example.projectgabi.Utils.Constants;
import com.example.projectgabi.Utils.DateConverter;
import com.example.projectgabi.Utils.RequestHandler;
import com.example.projectgabi.Utils.TransactionExpandableListAdapter;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements TransactionCallback {
    Intent intent;
    TextView tvWelcome, tvTest;
    Button addTransactionBtn, accountActivityBtn;
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
        // createComponents();



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

        if (intent.hasExtra(AccountActivity.NEW_USER_KEY)) {
            user = (User) intent.getSerializableExtra(AccountActivity.NEW_USER_KEY);
            tvWelcome.setText("Welcome, " + user.getFirstName());
            Log.d("user", user.toString());
        }

        if (deleteTransactionIV != null) {
            deleteTransactionIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Main delete tr", "delete");
                }
            });
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

    }

    private void initializeElements() {

        TransactionController transactionController = new TransactionController();
        transactionController.createComponents(this);
        transactionController.setTransactionCallback(new TransactionCallback() {
            @Override
            public void onTransactionReceived(HashMap<String, List<Transaction>> transactionHashMap, HashMap<String, List<Transaction>> transactionMapByParentCategory,
                                              ArrayList<PieEntry> categories ) {
                Log.d("Main interface test", "test");
               // showMaps(categories, transactionMapByParentCategory);
                addChartValues(categories);
                initPieChart(categories);

                createList( metaCategoryMap );
            }
        });
        transactionMap = transactionController.getTransactionMap();
        metaCategoryMap = transactionController.getTransactionMapByParentCategory();
        categories = (ArrayList<PieEntry>) transactionController.getCategories();
//        Log.d("transactionMap", "size: " + transactionMap.size() + transactionMap.toString());
//        Log.d("metaCategoryMap", metaCategoryMap.size() + " " + metaCategoryMap.toString());
    //    this.onTransactionReceived(this.transactionMap, this.metaCategoryMap);

        addChartValues(categories);


    }


    // update the pie chart based on the transaction [OLD]

    private void addChartValues(Transaction transaction) {

        if (transaction.getType() == TransactionType.EXPENSE) {
            // add the value to the pie chart based on the category
            PieData pieData = pieChart.getData();
            PieDataSet dataSet = (PieDataSet) pieData.getDataSetByIndex(0);
            List<PieEntry> entries = dataSet.getValues();

            List<PieEntry> updatedEntries = new ArrayList<>();
            for (PieEntry entry : entries) {
                if (entry.getLabel().equals(transaction.getCategory())) {
                    updatedEntries.add(new PieEntry((float) (entry.getY() + transaction.getValue()), entry.getLabel()));
                } else {
                    updatedEntries.add(entry);
                }
            }

// Set the values property of the PieDataSet to the new list of PieEntry objects
            dataSet.setValues(updatedEntries);
// Update the PieData and PieChart objects as before
            pieData.setDataSet(dataSet);
            pieChart.setData(pieData);
            pieData.notifyDataChanged();
            pieChart.notifyDataSetChanged();
            pieChart.invalidate();
        }
    }

    //
    private synchronized void createComponents() {


        JsonObjectRequest request = new JsonObjectRequest(Constants.READ_TRANSACTION_URL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("Main JSON", response.toString());
                    Toast.makeText(getApplicationContext(), "Adding data", Toast.LENGTH_SHORT).show();
                    getTransactionFromJson(response);
                    // initialize the pie chart
                    categories = new ArrayList<>();

                    // init piechart sa fie de fapt aici
                    addChartValues(categories); // get the values from the transaction


                    initPieChart(categories);
                    Log.d("Main metaCategoryMap", metaCategoryMap.toString());
                    createList(metaCategoryMap);


                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Main JSON error", Toast.LENGTH_SHORT).show();
                    Log.d("Main json error catch", e.toString());
                }
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

            private void createList(HashMap<String, List<Transaction>> transactionMap) {

                ArrayList<String> keys = new ArrayList<>(transactionMap.keySet());

                expandableListAdapter = new TransactionExpandableListAdapter(getApplicationContext(), keys, metaCategoryMap);

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

                expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                    @Override
                    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                        String selected = metaCategoryMap.get(metaCategoryMap.keySet().toArray()[groupPosition]).get(childPosition).toString();
                        Toast.makeText(getApplicationContext(), selected, Toast.LENGTH_SHORT).show();
                        Log.d("createList", "selected: " + selected);

                        return true;
                    }


                });
                metaCategoryMap = expandableListAdapter.getTransactionHashMap();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Main list error", Toast.LENGTH_SHORT).show();
                if (error != null) {
                    Log.d("Main transaction error ", error.toString());

                } else {
                    Log.d("Main transaction error", "unknown");
                }
            }
        });

        RequestHandler.getInstance(this).addToRequestQueue(request);


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


    }


    public synchronized void getTransactionFromJson(JSONObject response) {
        try {
            JSONArray jsonArray = response.getJSONArray(Constants.KEY_TRANSACTIONS_ARRAY_KEY);

            // should make it into a method
            for (int i = 0; i < jsonArray.length(); i++) {


                JSONObject jsonTransaction = jsonArray.getJSONObject(i);
                // Log.d("transaction json", "s " + jsonTransaction.toString());

                TransactionType type = TransactionType.valueOf(jsonTransaction.getString(Constants.KEY_TRANSACTION_TYPE));
                String category = jsonTransaction.getString(Constants.KEY_TRANSACTION_CATEGORY);
                double value = jsonTransaction.getDouble(Constants.KEY_TRANSACTION_VALUE);
                Date date = DateConverter.fromString(jsonTransaction.getString(Constants.KEY_TRANSACTION_DATE));
                String description = jsonTransaction.getString(Constants.KEY_TRANSACTION_DESCRIPTION);
                String parentCategory = jsonTransaction.getString(Constants.KEY_TRANSACTION_PARENT_CATEGORY);
                Log.d("transaction", "parentCategory: " + parentCategory);
                // generate an UUID for transaction
                UUID uuid = UUID.randomUUID();
                String id = jsonTransaction.getString(Constants.KEY_TRANSACTION_ID);
                Transaction transaction = new Transaction(id, type, category, value, date, description, parentCategory);

                //in the transaction map, add the transaction to the list of transactions, based on the cateogry

                if (transactionMap.containsKey(transaction.getCategory())) {
                    transactionMap.get(transaction.getCategory()).add(transaction);
                } else {
                    transactionMap.put(transaction.getCategory(), new ArrayList<>(Arrays.asList(transaction)));
                    // Log.d("transactionMap", "transactionMap: " + transactionMap.toString() + " size: " + transactionMap.size());
                }

//                // same for meta categories, based on the parent category
                if (metaCategoryMap.containsKey(transaction.getParentCategory())) {
                    metaCategoryMap.get(transaction.getParentCategory()).add(transaction);
                } else {
                    metaCategoryMap.put(transaction.getParentCategory(), new ArrayList<>(Arrays.asList(transaction)));
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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


    private void createList(HashMap<String, List<Transaction>> metaCategoryMap  ) {

        ArrayList<String> keys = new ArrayList<>(metaCategoryMap.keySet());

        expandableListAdapter = new TransactionExpandableListAdapter(getApplicationContext(), keys, metaCategoryMap);
        expandableListAdapter.setTransactionHashMap(metaCategoryMap);
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



    public static void showMaps(ArrayList<PieEntry> categories, HashMap<String, List<Transaction>> transactionMap){

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
    public void onTransactionReceived(HashMap<String, List<Transaction>> transactionHashMap, HashMap<String, List<Transaction>> transactionMapByParentCategory, ArrayList<PieEntry> categories ) {
        Log.d("Main onTransactionReceived", "onTransactionReceived: " + transactionHashMap.toString());
        Log.d("Main onTransactionReceived", "onTransactionReceived by category: " + transactionMapByParentCategory.toString());
        this.transactionMap = transactionHashMap;
        this.metaCategoryMap = transactionMapByParentCategory;

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

