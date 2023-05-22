package com.example.projectgabi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.projectgabi.Utils.Constants;
import com.example.projectgabi.Utils.RequestHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AccountActivity extends AppCompatActivity {

    public  static final String NEW_USER_KEY = "newUser";
    Button cancelBtn, registerBtn;
    EditText passwordEt, emailEt, phoneEt, firstNameEt, lastNameEt;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        initComponents();


        cancelBtn.setOnClickListener(v -> {
            finish();
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra(NEW_USER_KEY, user);
                setResult(RESULT_OK, intent);
                startActivity(intent);
                finish();

            }
        });

    }


    private void createUser() {

        user.setUserID(UUID.randomUUID().toString());
        user.setFirstName(firstNameEt.getText().toString());
        user.setLastName(lastNameEt.getText().toString());
        user.setEmail(emailEt.getText().toString());
        user.setPhoneNumber(phoneEt.getText().toString());
        user.setPassword(passwordEt.getText().toString());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.CREATE_USER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       Log.d("Response", response);
                        //Toast.makeText(getApplicationContext(), "User created successfully", Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    //Log.d("Error user", error.getMessage());
                    //Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    Log.d("Error user", "user databse send error");
                }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map <String, String> params = new HashMap<>();
                params.put("firstName", user.getFirstName());
                params.put("lastName", user.getLastName());
                params.put("email", user.getEmail());
                params.put("phoneNumber", user.getPhoneNumber());
                params.put("password", user.getPassword());
                params.put("id", user.getUserID().trim());
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

    }

    private void initComponents() {
        user = new User();
        cancelBtn = findViewById(R.id.accountCancelBtn);
        registerBtn = findViewById(R.id.accountRegisterBtn);
        passwordEt = findViewById(R.id.etPassword);
        emailEt = findViewById(R.id.etEmail);
        phoneEt = findViewById(R.id.etPhoneNumber);
        firstNameEt = findViewById(R.id.etFirstName);
        lastNameEt = findViewById(R.id.etLastName);

    }
}