package com.example.projectgabi;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import java.util.Map;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    Intent intent;
    TextView tvWelcome, tvTest;
    Button addTransactionBtn, accountActivityBtn;
    PieChart pieChart;
    PieData pieData;
   // List transactionList;
    Map<String, List<Transaction>> transactionMap;
    ArrayList<PieEntry> categories;
    User user ;


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_main);
        intent = getIntent();
        initComponents();
        initPieChart();


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


    private void initPieChart() {

        JsonObjectRequest request = new JsonObjectRequest(Constants.READ_TRANSACTION_URL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("Main JSON", response.toString());
                    Toast.makeText(getApplicationContext(), "Adding data", Toast.LENGTH_SHORT).show();
                    getTransactionFromJson(response);

                  //  Log.d("Main piechart", "init pie chart");
                    // initialize the pie chart
                    categories = new ArrayList<>();
                    addChartValues(categories); // get the values from the transaction



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


                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Main JSON error", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Main JSON error", Toast.LENGTH_SHORT).show();
            }
        });

        RequestHandler.getInstance(this).addToRequestQueue(request);


    }

    public void initComponents() {
        //transactionList = new ArrayList<>();
        user = new User();
        transactionMap = new HashMap<>();
        pieChart = findViewById(R.id.mainPieChart);
        tvTest = findViewById(R.id.testRecycleView);
        tvWelcome = findViewById(R.id.mainWelcomeText);
        addTransactionBtn = findViewById(R.id.mainAddTransactionButton);
        accountActivityBtn = findViewById(R.id.mainAccountButton);
    }


    public void getTransactionFromJson(JSONObject response) {
        try {
            JSONArray jsonArray = response.getJSONArray(Constants.KEY_TRANSACTIONS_ARRAY_KEY);

            // should make it into a method
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonTransaction = jsonArray.getJSONObject(i);

                TransactionType type = TransactionType.valueOf(jsonTransaction.getString(Constants.KEY_TRANSACTION_TYPE));
                String category = jsonTransaction.getString(Constants.KEY_TRANSACTION_CATEGORY);
                double value = jsonTransaction.getDouble(Constants.KEY_TRANSACTION_VALUE);
                Date date = DateConverter.fromString(jsonTransaction.getString(Constants.KEY_TRANSACTION_DATE));
                String description = jsonTransaction.getString(Constants.KEY_TRANSACTION_DESCRIPTION);

                // generate an UUID for transaction
                UUID uuid = UUID.randomUUID();
                Transaction transaction = new Transaction(String.valueOf(uuid), type, category, value, date, description);

                //in the transaction map, add the transaction to the list of transactions, based on the cateogry
                if (transactionMap.containsKey(transaction.getCategory())) {
                    transactionMap.get(transaction.getCategory()).add(transaction);
                } else {
                    transactionMap.put(transaction.getCategory(), new ArrayList<>(Arrays.asList(transaction)));
                }

                transactionMap.forEach((s, transaction1) -> Log.d("transaction " + s, transaction1.toString()));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addChartValues(ArrayList<PieEntry> categories) {

        Log.d("Main transactions", "adding chart values");

        if (transactionMap.isEmpty() || transactionMap == null) {
            // or throw a new  exception
            Toast.makeText(getApplicationContext(), "No transactions, you can add how many you want!", Toast.LENGTH_SHORT).show();
            // end the method
            return;
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


}

//        for (Transaction transaction : transactionMap) {
//            boolean found = false;
//            // if the transaction category is found in the categories list, add the value to the existing value
//            for (PieEntry pieEntry : categories) {
//                if (pieEntry.getLabel().equals(transaction.getCategory())) {
//                    // the value is doubled because the value is added twice
//                    // fix
//                    pieEntry.setY(pieEntry.getY() + (float) transaction.getValue());
//                    found = true;
//                    Log.d("Main pieEntry value ", pieEntry.toString());
//                    break;
//                }
//            }
//
//            if (!found) {
//                PieEntry newPieEntry = new PieEntry((float) transaction.getValue(), transaction.getCategory());
//                categories.add(newPieEntry);
//            }
//        }