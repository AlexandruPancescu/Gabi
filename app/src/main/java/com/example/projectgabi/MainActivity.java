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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projectgabi.Utils.TransactionAdapter;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

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

        if (savedInstance != null) {
            pieData = savedInstance.getParcelable("pieData");
        } else {
            //  Log.d("My tag", pieChart.getData().toString());
            initPieChart();
        }


        if (intent.hasExtra(LoginPage.USERNAME_KEY)) {
            tvWelcome.setText("Welcome, " + intent.getStringExtra(LoginPage.USERNAME_KEY));
        }
        // put the transaction  date atribute in the text view

        if (intent.hasExtra(TransactionActivity.TRANSACTION_KEY)) {
            Transaction transaction = (Transaction) intent.getSerializableExtra(TransactionActivity.TRANSACTION_KEY);
            transactionList.add(transaction);
            tvTest.setText(String.valueOf(transaction.getId()));
            updatePieChart(transaction);


        }


        // set the tv test to the value of the spinner
        TransactionAdapter transactionAdapter = new TransactionAdapter(getApplicationContext(),
                R.layout.activity_main,
                transactionList,
                getLayoutInflater());
        //tvTest.setText(intent.getStringExtra((TransactionActivity.TRANSACTION_KEY)));


        addTransactionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), TransactionActivity.class);
                startActivity(intent);


            }
        });


        accountActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), AccountActivity.class);
                startActivity(intent);
            }
        });


    }

    private void updatePieChart(Transaction transaction) {

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
//
//
//
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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);


    }


    private void initPieChart() {

        ArrayList<PieEntry> categories = new ArrayList<>();

        Resources res = getResources();
        String[] categoriesArray = res.getStringArray(R.array.spinner_category_list);
        for (int i = 0; i < categoriesArray.length; i++) {
            categories.add(new PieEntry(i * 100, categoriesArray[i])); // dummy values
        }
        // put in a Log.d the categories of the pie chart

        //  categories.stream().forEach(x -> Log.d("My tag ", x.toString() + x.getLabel()));

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


    }

    public void initComponents() {
        transactionList = new ArrayList<>();
        pieChart = findViewById(R.id.mainPieChart);
        tvTest = findViewById(R.id.testRecycleView);
        tvWelcome = findViewById(R.id.mainWelcomeText);
        addTransactionBtn = findViewById(R.id.mainAddTransactionButton);
        accountActivityBtn = findViewById(R.id.mainAccountButton);
    }

}

