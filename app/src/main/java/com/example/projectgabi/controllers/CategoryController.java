package com.example.projectgabi.controllers;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.projectgabi.classes.Category;
import com.example.projectgabi.utils.Constants;
import com.example.projectgabi.utils.RequestHandler;

import java.util.HashMap;
import java.util.Map;

public class CategoryController {

    public void createCategory(Context context, Category category) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.CREATE_CATEGORY_URL,
                response -> {
                    Log.d("CategoryController response", "createCategory: " + response);
                },

                error -> {
                    Log.d("CategoryController error", "createCategory: " + error.getMessage());
                }) {

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String > params = new  HashMap<>();
                params.put(Constants.KEY_CATEGORY_ID, category.getCategoryID().toString());
                params.put(Constants.KEY_CATEGORY_NAME, category.getCategoryName());
                params.put(Constants.KEY_CATEGORY_AMOUNT, String.valueOf(category.getCategoryAmount()));
                params.put(Constants.KEY_CATEGORY_PARENT, category.getParentCategory());
                Log.d("CategoryController params", "getParams: " + params.toString());
                return params;
            }
        };
        RequestHandler.getInstance(context).addToRequestQueue(stringRequest);
    }


}
