package com.example.projectgabi.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.projectgabi.R;

public class MenuActivity extends AppCompatActivity {

    Button transactionBtn, accountBtn, budgetBtn, logoutBtn;
   Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        initComponents();
        transactionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

            }
        });

        accountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), AccountActivity.class);
                startActivity(intent);
            }
        });

        budgetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), BudgetTrackerActivity.class);
                startActivity(intent);
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), LoginPage.class);
                startActivity(intent);
            }
        });
    }


    public void initComponents(){
        transactionBtn = findViewById(R.id.transactionBtn);
        accountBtn = findViewById(R.id.bankAccountBtn);
        budgetBtn = findViewById(R.id.budgetBtn);
        logoutBtn = findViewById(R.id.logoutBtn);
    }
}