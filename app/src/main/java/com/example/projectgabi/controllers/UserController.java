package com.example.projectgabi.controllers;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.projectgabi.classes.User;
import com.example.projectgabi.interfaces.UserCallback;
import com.example.projectgabi.utils.Constants;
import com.example.projectgabi.utils.RequestHandler;

import org.json.JSONObject;

public class UserController {
    // class for taking care of user related functions
    // get user info from database
    private static int nr = 0;
    private Context context;
    private User user;
    private UserCallback userCallback;

    //using Volley to get data from database
    public void getUser(String email, String password) {

        String url = Constants.GET_USER + "?email=" + email;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("UserController", "getUser: " + response.toString());
                User user = UserController.this.getUserFromJSON(response);
                userCallback.onReceivedUser(user);
            }

        }, error -> {
            // show an error message
            Log.d("UserController", "getUser error: " + error);
        });

        RequestHandler.getInstance(context).addToRequestQueue(jsonObjectRequest);

    }

//    public String getUserID(Context context) {
//        return "1c7b3845-9dd3-461a-8508-8a6015889a10";
//    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public User getUserFromJSON(JSONObject response) {
        try {

            JSONObject user = response.getJSONObject("user");
            String id = user.getString("PK_UserID");
            String email = user.getString("Email");
            String password = user.getString("Password");
            String firstName = user.getString("FirstName");
            String lastName = user.getString("LastName");
            String phoneNumber = user.getString("PhoneNumber");

            return new User(id,firstName, lastName,email,  phoneNumber ,password );

        } catch (Exception e) {
            Log.d("UserController", "getUserFromJSON error: " + e.getMessage());
        }
        return null;
    }


    public static int getNr() {
        return nr;
    }

    public static void setNr(int nr) {
        UserController.nr = nr;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserCallback getUserCallback() {
        return userCallback;
    }

    public void setUserCallback(UserCallback userCallback) {
        this.userCallback = userCallback;
    }
}



