package com.example.projectgabi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class LoginPage extends AppCompatActivity {
    Button skipBtn;
  EditText usernameEt ;

    public static final String USERNAME_KEY = "username";
    @Override
    protected void onCreate(Bundle savedInstance){


        super.onCreate(savedInstance);
        setContentView(R.layout.activity_login_page);


        skipBtn = findViewById(R.id.loginButton);
        usernameEt = findViewById(R.id.emailTextInput);


        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra(USERNAME_KEY, usernameEt.getText().toString());
                setResult(RESULT_OK, intent);
                startActivity(intent);
                finish();
            }
        });
    }

}
