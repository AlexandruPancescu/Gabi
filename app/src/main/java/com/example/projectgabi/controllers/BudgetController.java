package com.example.projectgabi.controllers;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.projectgabi.classes.Budget;
import com.example.projectgabi.classes.BudgetItem;
import com.example.projectgabi.classes.Category;
import com.example.projectgabi.interfaces.BudgetCallBack;
import com.example.projectgabi.managers.BudgetSingletonManager;
import com.example.projectgabi.utils.Constants;
import com.example.projectgabi.utils.DateConverter;
import com.example.projectgabi.utils.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class BudgetController {

    Budget budget;
    ArrayList<BudgetItem> budgetItems = new ArrayList<>();
    ArrayList<Category> categories = new ArrayList<>();
    BudgetCallBack budgetCallBack;
    CategoryController categoryController;
    BudgetSingletonManager budgetSingletonManager = BudgetSingletonManager.getInstance();


    public void createBudget(Context context, Budget budget) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.CREATE_BUDGET_URL,
                response -> {
                    Log.d("New Budget response", response.toString());
                },
                error -> {
                    Log.d("New Budget ", "error" + error.getMessage());
                }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Constants.KEY_BUDGET_ID, budget.getBudgetID());
                params.put(Constants.KEY_BUDGET_START_DATE, DateConverter.fromDate(budget.getStartDate()));
                params.put(Constants.KEY_BUDGET_END_DATE, DateConverter.fromDate(budget.getEndDate()));
                params.put(Constants.KEY_BUDGET_USER_ID, budget.getUserId());
                Log.d("New Budget ", "params" + params.toString());
                return params;
            }
        };
        RequestHandler.getInstance(context).addToRequestQueue(stringRequest);

    }

    public void createBudgetItem(Context context, BudgetItem budgetItem) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.CREATE_BUDGET_ITEM_URL,
                response -> {
                    Log.d("New Budget Item response", response.toString());
                },
                error -> {
                    Log.d("New Budget Item error ", "error" + error.getMessage());
                }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Constants.KEY_BUDGET_ID, budgetItem.getBudgetID());
                params.put(Constants.KEY_CATEGORY_ID, budgetItem.getCategory().getCategoryID());
                params.put(Constants.KEY_BUDGET_ITEM_AMOUNT, String.valueOf(budgetItem.getBudgetItemAmount()));
                Log.d("New Budget Item", "params" + params.toString());
                return params;
            }
        };
        RequestHandler.getInstance(context).addToRequestQueue(stringRequest);

    }

    // no so good
//    public synchronized Budget getBudgetByInterval(Context context, String startDate, String endDate) {
//
//        String request = Constants.GET_BUDGET_BUNDLE_URL + "?startDate=" + startDate + "&endDate=" + endDate;
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(request,
//                response -> {
//                    Log.d("BudgetController date response", response.toString());
//                    try {
//                        this.budget = this.getBudgetFromJSON(response);
//
//                        budgetCallBack.onReceivedBudget(budget, budgetItems, categories);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                },
//                error -> {
//                    Log.d("BudgetController date error ", "error" + error.getMessage());
//                });
//
//
//        RequestHandler.getInstance(context).addToRequestQueue(jsonObjectRequest);
//
//
//        return budgetSingletonManager.getBudget();
//
//    }

    // retrieve the budget by interval, with his budget items and categories
    public void getBudgetBundle(Context context, String startDate, String endDate) {

        String request = Constants.GET_BUDGET_BUNDLE_URL + "?startDate=" + startDate + "&endDate=" + endDate;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(request,
                response -> {
                    Log.d("BudgetController date response", response.toString());


                    try {
                        BudgetController.this.makeBudgetBundle(response);
                        budgetCallBack.onReceivedBudget(budget, budgetItems, categories);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                },
                error -> {
                    Log.d("BudgetController date error ", "error" + error.getMessage());
                });

        RequestHandler.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    private void makeBudgetBundle(JSONObject response) throws JSONException {

        // the Bundle consists of a json object with the budget, a json array with the budget items and a json array with the categories
        // the budget is the first element of the json object
        // the budget items are the first element of the json array
        // the categories are the second element of the json array

        JSONObject bundleJSOn = response.getJSONObject(Constants.KEY_BUDGET_BUNDLE_ARRAY_KEY);

        this.budget = getBudgetFromJSON(bundleJSOn.getJSONObject(Constants.KEY_BUDGET_BY_DATE_KEY));
        this.budgetItems =  getBudgetItemsFromJSON(bundleJSOn.getJSONArray(Constants.KEY_BUDGETS));
        this.categories =  getCategoriesFromJSON(bundleJSOn.getJSONArray(Constants.KEY_CATEGORY_ARRAY_KEY), this.budgetItems);


    }


    private ArrayList<Category> getCategoriesFromJSON(JSONArray categoriesJSON, ArrayList<BudgetItem> budgetItems) throws JSONException {


        ArrayList<Category> categories = new ArrayList<>();

        for (int i = 0; i < categoriesJSON.length(); i++) {
            JSONObject categoryJSON = categoriesJSON.getJSONObject(i);
            String categoryID = categoryJSON.getString(Constants.KEY_CATEGORY_ID);
            String categoryName = categoryJSON.getString(Constants.KEY_CATEGORY_NAME);
            String parentCategory = categoryJSON.getString(Constants.KEY_CATEGORY_PARENT);
            double categoryAmount = categoryJSON.getDouble(Constants.KEY_CATEGORY_AMOUNT);
            Category category = new Category(categoryID, categoryName, categoryAmount, parentCategory);
            categories.add(category);
            for (BudgetItem budgetItem : budgetItems) {
                if (budgetItem.getCategoryID().equals(categoryID)) {
                    budgetItem.setCategory(category);
                }
            }

        }

        return categories;
    }

    private ArrayList<BudgetItem> getBudgetItemsFromJSON(JSONArray budgetItemsJSON) throws JSONException {

        ArrayList<BudgetItem> budgetItems = new ArrayList<>();


        for (int i = 0; i < budgetItemsJSON.length(); i++) {
            JSONObject budgetItemJSON = budgetItemsJSON.getJSONObject(i);
            String budgetID = budgetItemJSON.getString(Constants.KEY_BUDGET_ID);
            String categoryID = budgetItemJSON.getString(Constants.KEY_CATEGORY_ID);

            double budgetItemAmount = budgetItemJSON.getDouble(Constants.KEY_BUDGET_ITEM_AMOUNT);

            budgetItems.add(new BudgetItem(budgetID, budgetItemAmount, categoryID));
        }
        return budgetItems;
    }

    private Budget getBudgetFromJSON(JSONObject budgetJSON) throws JSONException {

        // JSONObject budgetJSON = response.optJSONObject(Constants.KEY_BUDGET_BY_DATE_KEY);

        if (budgetJSON != null) {
            String budgetID = budgetJSON.getString(Constants.KEY_BUDGET_ID);
            String startDate = budgetJSON.getString(Constants.KEY_BUDGET_START_DATE);
            String endDate = budgetJSON.getString(Constants.KEY_BUDGET_END_DATE);
            String userID = budgetJSON.getString(Constants.KEY_BUDGET_USER_ID);
            Log.d("BudgetController json id ", "budgetID" + budgetID);
            return new Budget(budgetID, DateConverter.fromString(startDate), DateConverter.fromString(endDate), userID);
        } else {
            Log.d("BudgetController date error ", "error" + "budgetJSON is null");
            return null;
        }


    }

    public Budget getBudget() {
        return budget;
    }

    public void setBudget(Budget budget) {
        this.budget = budget;
    }

    public BudgetCallBack getBudgetCallBack() {
        return budgetCallBack;
    }

    public void setBudgetCallBack(BudgetCallBack budgetCallBack) {
        this.budgetCallBack = budgetCallBack;
    }

    public CategoryController getCategoryController() {
        return categoryController;
    }

    public void setCategoryController(CategoryController categoryController) {
        this.categoryController = categoryController;
    }
}

