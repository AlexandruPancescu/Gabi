package com.example.projectgabi.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.projectgabi.R;
import com.example.projectgabi.classes.Account;
import com.example.projectgabi.classes.User;
import com.example.projectgabi.models.AccountController;
import com.example.projectgabi.models.UserController;
import com.example.projectgabi.interfaces.UserCallback;

import java.util.UUID;

public class NewBankAccount extends AppCompatActivity {

    EditText bankNameEt, balanceEt, additionalInfoEt;
    Spinner currencySpn;
    Button createBankAccount, cancelBtn;
    Intent intent;
    AccountController accountController;
    Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_bank_account);

        initializeComponents();

        createBankAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkData();
                intent = new Intent(getApplicationContext(), AccountActivity.class);
                startActivity(intent);
            }
        });


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), AccountActivity.class);
                startActivity(intent);
            }
        });


    }


    private void accountHandler() {

        UserController userController = new UserController();
        String email = LoginPage.userEmail;
        String password = LoginPage.userPassword;
        userController.getUser(email, password );
       userController.setUserCallback(new UserCallback() {
           @Override
           public void onReceivedUser(User user) {
               accountController = new AccountController();
               accountController.createAccount(context,
                       createAccount(user.getUserID()));
           }
       });

    }

    private void initializeComponents() {
        bankNameEt = findViewById(R.id.editTextBankName);
        balanceEt = findViewById(R.id.editTextBalance);
        additionalInfoEt = findViewById(R.id.editTextAdditionalInfo);

        createBankAccount = findViewById(R.id.newBankAccountCreateBtn);
        cancelBtn = findViewById(R.id.newBankAccountCancelBtn);

        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(getApplicationContext()
                , R.array.spinnner_currencies
                , androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);

        currencySpn = findViewById(R.id.spinnerCurrency);
        currencySpn.setAdapter(arrayAdapter);

    }

    public void checkData() {

        if (bankNameEt.getText().toString().isEmpty()) {
            bankNameEt.setError("Please enter a bank name");
        } else if (balanceEt.getText().toString().isEmpty()) {
            balanceEt.setError("Please enter a balance");
        } else if (additionalInfoEt.getText().toString().isEmpty()) {
            additionalInfoEt.setError("Please enter additional info");
        } else {
            accountHandler();
        }
    }


    public Account createAccount(String userId) {

        String bankName = bankNameEt.getText().toString();
        float balance = Float.parseFloat(balanceEt.getText().toString());
        String currency = currencySpn.getSelectedItem().toString();
        String info = additionalInfoEt.getText().toString();
        UUID uuid = UUID.randomUUID();
        Log.d("New Bank Creation", bankName + " " + currency + info + " inputs");
        return new Account(String.valueOf(uuid), bankName, balance, currency, info, userId);
    }
}