package com.example.projectgabi.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projectgabi.R;

public class LoginPage extends AppCompatActivity {
    Button loginBtn, newUserBtn;
  EditText usernameEt ;

    public static final String USERNAME_KEY = "username";
    @Override
    protected void onCreate(Bundle savedInstance){


        super.onCreate(savedInstance);
        setContentView(R.layout.activity_login_page);


        loginBtn = findViewById(R.id.loginButton);
        usernameEt = findViewById(R.id.emailTextInput);
        newUserBtn = findViewById(R.id.loginNewUserBtn);


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra(USERNAME_KEY, usernameEt.getText().toString());
                setResult(RESULT_OK, intent);
                startActivity(intent);
                finish();
            }
        });


        newUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewUserActivity.class);
                startActivity(intent);
            }
        });
    }





}
