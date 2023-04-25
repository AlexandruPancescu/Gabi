package com.example.projectgabi;

import static java.lang.System.out;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.projectgabi.Utils.Constants;
import com.example.projectgabi.Utils.DateConverter;
import com.example.projectgabi.Utils.TransactionAdapter;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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
    ArrayList<Transaction> transactionList;


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

//        if (savedInstance != null) {
//            pieData = savedInstance.getParcelable("pieData");
//        } else {
//            initPieChart();
//        }

        if (intent.hasExtra(LoginPage.USERNAME_KEY)) {
            //tvWelcome.setText("Welcome, " + intent.getStringExtra(LoginPage.USERNAME_KEY));
            String welcomeMessaage = String.format(getResources().getString(R.string.welcome_message), intent.getStringExtra(LoginPage.USERNAME_KEY));
            tvWelcome.setText(welcomeMessaage);
        }
        // put the transaction  date atribute in the text view
        if (intent.hasExtra(TransactionActivity.TRANSACTION_KEY)) {
            Transaction transaction = (Transaction) intent.getSerializableExtra(TransactionActivity.TRANSACTION_KEY);
            transactionList.add(transaction);
        }

        // set the tv test to the value of the spinner
        TransactionAdapter transactionAdapter = new TransactionAdapter(getApplicationContext(),
                R.layout.activity_main,
                transactionList,
                getLayoutInflater());

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
//            for (PieEntry entry : dataSet.getValues()) {
//                if (entry.getLabel().equals(transaction.getCategory())) {
//                    Log.d("test entry", dataSet.getValues().toString());
//                    Log.d("test entry", entry.toString());
//                    float value = entry.getY()+ (float) transaction.getValue();
//                    entry.setY(value);
//                   entries.get ( entries.indexOf(entry)).setY(value);
//
//                   // PieEntry updatedEntry = new PieEntry(value, transaction.getCategory());
//                   // entries.add(updatedEntry);
//
//                    dataSet.setValues(entries);
//
//                    pieData.setDataSet(dataSet);
//                    pieChart.setData(pieData);
//                    pieData.notifyDataChanged();
//                    pieChart.notifyDataSetChanged();
//                    pieChart.invalidate();
//                    break;
//                }
//
//            }

            //  Log.d("Pie test", dataSet.toString());

//            PieEntry entry = dataSet.getEntryForLabel(transaction.getCategory(), 0);
//            entry.setY(entry.getY() + transaction.getAmount());
//            data.notifyDataChanged();
//            pieChart.notifyDataSetChanged();
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

                    Log.d("Main piechart", "init pie chart");
                    // initialize the pie chart
                    ArrayList<PieEntry> categories = addChartValues(); // get the values from the transaction


                    Log.d("Main transaction size", String.valueOf(categories.size()));
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
        transactionList = new ArrayList<>();
        pieChart = findViewById(R.id.mainPieChart);
        tvTest = findViewById(R.id.testRecycleView);
        tvWelcome = findViewById(R.id.mainWelcomeText);
        addTransactionBtn = findViewById(R.id.mainAddTransactionButton);
        accountActivityBtn = findViewById(R.id.mainAccountButton);
    }


    public void getTransactionFromJson(JSONObject response) {
        try {
            JSONArray jsonArray = response.getJSONArray("transactions"); // hardcoded!!
            //  Log.d("Main JSON", jsonArray.getJSONObject(0).getInt("id") + "");

            // should make it into a method
            for (int i = 0; i < jsonArray.length(); i++) {

                // !! hardcoded values

                Log.d("Main Transaction", "...for loop...");
                JSONObject jsonTransaction = jsonArray.getJSONObject(i);


                TransactionType type = TransactionType.valueOf(jsonTransaction.getString(Constants.KEY_TRANSACTION_TYPE));
                String category = jsonTransaction.getString(Constants.KEY_TRANSACTION_CATEGORY);
                double value = jsonTransaction.getDouble(Constants.KEY_TRANSACTION_VALUE);
                Date date = DateConverter.fromString(jsonTransaction.getString(Constants.KEY_TRANSACTION_DATE));
                String description = jsonTransaction.getString(Constants.KEY_TRANSACTION_DESCRIPTION);
                // generate an UUID for transaction
                UUID uuid = UUID.randomUUID();
                Transaction transaction = new Transaction(String.valueOf(uuid), type, category, value, date, description);

                transactionList.add(transaction);
               //  Log.d("Main Transaction", "Transaction " + String.valueOf(transaction.getId()) + " added to the list ");
                // tvTest.setText(String.valueOf(transaction.getId()));
                //put in a log d all the transcactions
                //  transactionList.forEach(transaction1 -> Log.d("transaction", transaction1.toString()));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<PieEntry> addChartValues() {
        ArrayList<PieEntry> categories = new ArrayList<>();
        Log.d("Main transactions", "adding chart values");

        for (Transaction transaction : transactionList) {
            boolean found = false;
            for (PieEntry pieEntry : categories) {
                if (pieEntry.getLabel().equals(transaction.getCategory())) {
                    pieEntry.setY(pieEntry.getY() + (float) transaction.getValue());
                    found = true;
                    break;

                }
                // if the category is not in the list, add it
//                PieEntry newPieEntry = new PieEntry((float) transaction.getValue(), transaction.getCategory());
//                categories.add(pieEntry);
            }
            if(!found){
                PieEntry newPieEntry = new PieEntry((float) transaction.getValue(), transaction.getCategory());
                categories.add(newPieEntry);
            }
//            PieEntry pieEntry = new PieEntry((float) transaction.getValue(), transaction.getCategory());
//            categories.add(pieEntry);


        }

        categories.forEach(pieEntry1 -> Log.d("Main pieEntry", pieEntry1.toString()));
        return categories;
    }


}

