package com.example.projectgabi;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
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
    ExpandableListAdapter expandableListAdapter;
    HashMap<String, List<Transaction>> metaCategoryMap;


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_main);
        initComponents();
        intent = getIntent();
        createComponents();


        // transactionMap.put("Food", Arrays.asList(new Transaction("1", TransactionType.EXPENSE, "Food", 110, DateConverter.fromString("01-01-2020"), "Food", "Non-Frequent")));
        // transactionMap.put("Bills", Arrays.asList(new Transaction("2", TransactionType.EXPENSE, "Bills", 100, DateConverter.fromString("01-01-2020"), "Food", "Frequent")));
        Log.d("Main transactions ", "size 2: " + transactionMap.size());
       // createList(metaCategoryMap);


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


    // ar trb schimba numele functiei
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

                  //   transactionMap.forEach((key, transaction1) -> Log.d("transaction " + key, transaction1.toString()));
                  //  metaCategoryMap.forEach((key, transaction) -> Log.d("map transaction "  + key , transaction.toString()));

                    initPieChart(categories);
                    Log.d("Main metaCategoryMap", metaCategoryMap.toString());
                    createList(metaCategoryMap);


                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Main JSON error", Toast.LENGTH_SHORT).show();
                    Log.d("Main json error catch", e.toString());
                }
            }

            private void initPieChart(ArrayList<PieEntry> categories) {
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
                Log.d("createList", "createList size: " + String.valueOf(MainActivity.this.metaCategoryMap.size()));
                ArrayList<String> keys = new ArrayList<>(transactionMap.keySet());

                expandableListAdapter = new TransactionExpandableListAdapter(getApplicationContext(), keys, metaCategoryMap);
                Log.d("test", "test");
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

//        metaCategoryMap = new HashMap<>();
//        List<String> metaCategoriesList = new ArrayList<>();
//        metaCategoriesList = Arrays.asList(getResources().getStringArray(R.array.meta_categories));
//
//
//        for (String metaCategory : metaCategoriesList) {
//            metaCategoryMap.put(metaCategory, new ArrayList<String>());
//
//        }


        //  Log.d("initComponents", "metaCategories size: " + metaCategories.size() + metaCategories.toString());


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
                Transaction transaction = new Transaction(String.valueOf(uuid), type, category, value, date, description, parentCategory);


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

        Log.d("Main transactions", "adding chart values");

        if (transactionMap.isEmpty() || transactionMap == null) {
            // or throw a new  exception
            Log.d("Main transactions", "no transactions");
            Toast.makeText(getApplicationContext(), "No transactions, you can add how many you want!", Toast.LENGTH_SHORT).show();
            // end the method
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

        categories.forEach(pieEntry1 -> Log.d("Main pieEntry", pieEntry1.toString()));

    }


    private void createList(HashMap<String, List<Transaction>> transactionMap) {

        Log.d("main createList 2", "createList size: " + String.valueOf(MainActivity.this.transactionMap.size()));

        expandableListAdapter = new TransactionExpandableListAdapter(getApplicationContext(), transactionMap);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            int previousGroup = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if (groupPosition != previousGroup && previousGroup != -1) {
                    expandableListView.collapseGroup(previousGroup);
                }
                Log.d("createList2 ", "groupPosition: " + groupPosition);
                previousGroup = groupPosition;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                String selected = MainActivity.this.transactionMap.get(MainActivity.this.transactionMap.keySet().toArray()[groupPosition]).get(childPosition).toString();
                Toast.makeText(getApplicationContext(), selected, Toast.LENGTH_SHORT).show();
                Log.d("createList2 ", "onChildClick: " + selected);

                return true;
            }
        });


    }
}

