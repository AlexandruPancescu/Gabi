package com.example.projectgabi.views;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projectgabi.R;
import com.example.projectgabi.classes.User;
import com.example.projectgabi.controllers.UserController;
import com.example.projectgabi.interfaces.UserCallback;

public class LoginPage extends AppCompatActivity {
    Button loginBtn, newUserBtn;
    EditText usernameEt, passwordEt;
    public static String userEmail;
    public static String userPassword;
    public static String userID;

    public static final String USERNAME_KEY = "username";

    @Override
    protected void onCreate(Bundle savedInstance) {


        super.onCreate(savedInstance);
        setContentView(R.layout.activity_login_page);


        loginBtn = findViewById(R.id.loginButton);
        usernameEt = findViewById(R.id.emailTextInput);
        newUserBtn = findViewById(R.id.loginNewUserBtn);
        passwordEt = findViewById(R.id.loginPasswordEt);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("LoginPage", "onClick: " + "login button clicked");
                checkData();
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


    public void checkData() {

        if (usernameEt.getText().toString().isEmpty()) {
            usernameEt.setError("Please enter a username");
            return;
        }
        if (passwordEt.getText().toString().isEmpty()) {
            passwordEt.setError("Please enter a password");
            return;
        }

        String email = usernameEt.getText().toString();
        String password = passwordEt.getText().toString();
        Log.d("LoginPage", "checkData: " + "email: " + email + " password: " + password);

        UserController userController = new UserController();
        userController.setContext(this);

        Log.d("LoginPage", "checkData: " + "calling getUser");
        userController.getUser(email, password);
        userController.setUserCallback(new UserCallback() {
            @Override
            public void onReceivedUser(User user) {
                if (user == null) {
                    showWrongCredentialsDialog();
                    return;
                }
                if (user.getPassword().equals(password)) {
                    Log.d("LoginPage", "getUser: " + "password is correct");
                        Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                        LoginPage.userEmail = user.getEmail() ;
                        LoginPage.userPassword = user.getPassword();
                        LoginPage.userID = user.getUserID();
                        intent.putExtra(USERNAME_KEY, usernameEt.getText().toString());
                        setResult(RESULT_OK, intent);
                        startActivity(intent);
                        finish();
                    } else {
                        showWrongCredentialsDialog();
                    }
            }
        });


    }

    private void showWrongCredentialsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Wrong Credentials")
                .setMessage("Wrong email and password.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
