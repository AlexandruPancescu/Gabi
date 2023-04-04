package com.example.projectgabi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Intent intent;
    TextView tvWelcome, tvTest;
    Button addTransactionBtn;
    BarChart barChart;


    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_main);
        intent = getIntent();
        initComponents();


        if (intent.hasExtra(LoginPage.USERNAME_KEY)) {
            tvWelcome.setText("Welcome, " + intent.getStringExtra(LoginPage.USERNAME_KEY));
        }

        ArrayList<BarEntry> categories = new ArrayList<>();
        // add entries to the array list
        categories.add(new BarEntry(1, 100));
        categories.add(new BarEntry(2, 200));
        categories.add(new BarEntry(3, 300));
        categories.add(new BarEntry(4, 400));
        categories.add(new BarEntry(5, 500));

        BarDataSet barDataSet = new BarDataSet(categories, "categories");
        barDataSet.setColor(R.color.black);
        barDataSet.setValueTextColor(R.color.purple_200);
        barDataSet.setValueTextSize(16f);
        BarData barData = new BarData(barDataSet);
        barChart.setFitBars(true);
        barChart.setData(barData);
        barChart.getDescription().setText("Categories");
        barChart.animateY(2000);



        addTransactionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), TransactionActivity.class);
                startActivity(intent);


            }
        });


    }

    public void initComponents() {
        barChart = findViewById(R.id.mainBarChart);
        tvTest = findViewById(R.id.testRecycleView);
        tvWelcome = findViewById(R.id.mainWelcomeText);
        addTransactionBtn = findViewById(R.id.mainAddTransactionButton);
    }

}

