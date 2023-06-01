package com.example.projectgabi.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.projectgabi.R;
import com.example.projectgabi.adapters.AccountAdapter;
import com.example.projectgabi.classes.Account;
import com.example.projectgabi.controllers.AccountController;
import com.example.projectgabi.interfaces.AccountCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class AccountActivity extends AppCompatActivity implements AccountCallback {

    FloatingActionButton createAccountFab;
    ListView accountsListView;
    Intent intent;
    ArrayList<Account> accounts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_account);

        createAccountFab = findViewById(R.id.bankAddAccountBtn);
        accountsListView = findViewById(R.id.bankAccountListView);
        accounts = new ArrayList();
        getDatabaseBankAccounts();


        createAccountFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), NewBankAccount.class);
                startActivity(intent);
            }
        });
    }

    private void getDatabaseBankAccounts() {

        Toast.makeText(this, "Getting data...", Toast.LENGTH_SHORT).show();
        AccountController accountController = new AccountController();
        accountController.getAccounts(this);
        accountController.setAccountCallback(new AccountCallback() {
            @Override
            public void onReceivedAccount(ArrayList<Account> accounts) {
                createAccountAdapter(accounts);
            }
        });
        this.accounts = accountController.getAccounts();

    }

    private void createAccountAdapter(ArrayList<Account> accounts) {

        Log.d("AccountActivity", "createAccountAdapter: " + accounts.size());
        AccountAdapter accountAdapter = new AccountAdapter(getApplicationContext(),
                R.layout.lv_account_row,
                accounts,
                getLayoutInflater());
        accountsListView.setAdapter(accountAdapter);
        // accountAdapter.notifyDataSetChanged();
    }

    @Override
    public void onReceivedAccount(ArrayList<Account> accounts) {
        this.accounts = accounts;
    }
}