package com.example.projectgabi.controllers;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.projectgabi.interfaces.TransactionCallback;
import com.example.projectgabi.classes.Transaction;
import com.example.projectgabi.enums.TransactionType;
import com.example.projectgabi.utils.Constants;
import com.example.projectgabi.utils.DateConverter;
import com.example.projectgabi.utils.RequestHandler;
import com.github.mikephil.charting.data.PieEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class TransactionController implements TransactionCallback {

    TransactionCallback transactionCallback;

   private  Context context;
    ArrayList<PieEntry> categoriesPieEntries;
    ArrayList<Transaction> transactions;

    HashMap<String, List<Transaction>> transactionMap;
    HashMap<String, List<Transaction>> transactionMapByParentCategory;




    public TransactionController() {
        categoriesPieEntries = new ArrayList<>();
        transactionMap = new HashMap<>();
        transactionMapByParentCategory = new HashMap<>();
        transactions = new ArrayList<>();
    }

    public void deleteTransaction(String transactionId) {

        // using the Volleyy library to make a request to the server, delete the transaction from the database based on the transactionId
        // if the request is successful, update the adapter
        // if the request is not successful, show an error message

        String deleteRequest = Constants.DELETE_TRANSACTION_URL + "?id=" + transactionId;
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, deleteRequest, response -> {
            // update the adapter
            Log.d("TransactionController", "deleteTransaction: " + response + " with id:" + transactionId); // response is the message from the server
        }, error -> {
            // show an error message
            Log.d("TransactionController", "deleteTransaction error: " + error.getMessage());
        });

        RequestHandler.getInstance().addToRequestQueue(stringRequest);

    }

    public void getTransactionFromJson(JSONObject response) {
        try {
            JSONArray jsonArray = response.getJSONArray(Constants.KEY_TRANSACTIONS_ARRAY_KEY);

            // should make it into a method
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonTransaction = jsonArray.getJSONObject(i);
                // Log.d("transaction json", "s " + jsonTransaction.toString());

                TransactionType type = TransactionType.valueOf(jsonTransaction.getString(Constants.KEY_TRANSACTION_TYPE));
                String category = jsonTransaction.getString(Constants.KEY_TRANSACTION_CATEGORY);
                double value = jsonTransaction.getDouble(Constants.KEY_TRANSACTION_VALUE);
                Date date = DateConverter.fromString(jsonTransaction.getString(Constants.KEY_TRANSACTION_DATE));
                String description = jsonTransaction.getString(Constants.KEY_TRANSACTION_DESCRIPTION);
                String parentCategory = jsonTransaction.getString(Constants.KEY_TRANSACTION_PARENT_CATEGORY);
                String user_id = jsonTransaction.getString("FK_UserID");
                String account_id  = jsonTransaction.getString("FK_AccountID");
                Log.d("transaction", "parentCategory: " + parentCategory);
                // generate an UUID for transaction

                String id = jsonTransaction.getString(Constants.KEY_TRANSACTION_ID);
                Transaction transaction = new Transaction(id, type, category, value, date, description, parentCategory);
                transaction.setFk_account(account_id);
                transaction.setFk_user(user_id);

                //in the transaction map, add the transaction to the list of transactions, based on the cateogry

                this.transactions.add(transaction);

                if (transactionMap.containsKey(transaction.getCategory())) {
                    transactionMap.get(transaction.getCategory()).add(transaction);
                } else {
                    transactionMap.put(transaction.getCategory(), new ArrayList<>(Arrays.asList(transaction)));
                    // Log.d("transactionMap", "transactionMap: " + transactionMap.toString() + " size: " + transactionMap.size());
                }

//                // same for meta categories, based on the parent category
                if (transactionMapByParentCategory.containsKey(transaction.getParentCategory())) {
                    transactionMapByParentCategory.get(transaction.getParentCategory()).add(transaction);
                } else {
                    transactionMapByParentCategory.put(transaction.getParentCategory(), new ArrayList<>(Arrays.asList(transaction)));
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public synchronized void getTransactionsFromDB(Context context, String userID) {

        String url = Constants.GET_TRANSACTIONS_BY_USER_ID+ "?id=" + userID;
        Log.d("TransactionController", "createComponents: " + url);
        JsonObjectRequest request = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("TransactionController JSON", response.toString());

                    TransactionController.this.getTransactionFromJson(response);
//                    Log.d("TransactionController", "transactionMap: " + transactionMap.toString());
//                    Log.d("TransactionController", "transactionMapByParentCategory: " + transactionMapByParentCategory.toString());

                    transactionCallback.onReceivedTransaction(transactionMap, transactionMapByParentCategory, categoriesPieEntries);
                    transactionCallback.getTransactions(transactions);

                } catch (Exception e) {
                    Log.d("TransactionController", "error in json catch"  + " " + e.getLocalizedMessage());
                    Log.d("TransactionController", "error in json catch"  + " " + e.getMessage());

                }
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error != null) {
                    Log.d("TransactionController error1", error.toString());
                    Log.d("TransactionController error1", error.getMessage());

                } else {
                    Log.d("TransactionController error1", "unknown");
                }
            }
        });

        RequestHandler.getInstance(context).addToRequestQueue(request);

    }


    public List<PieEntry> getCategoriesPieEntries() {
        return categoriesPieEntries;
    }

    public void setCategoriesPieEntries(ArrayList<PieEntry> categoriesPieEntries) {
        this.categoriesPieEntries = categoriesPieEntries;
    }

    public HashMap<String, List<Transaction>> getTransactionMap() {
        return transactionMap;
    }

    public void setTransactionMap(HashMap<String, List<Transaction>> transactionMap) {
        this.transactionMap = transactionMap;
    }

    public HashMap<String, List<Transaction>> getTransactionMapByParentCategory() {
        return transactionMapByParentCategory;
    }

    public void setTransactionMapByParentCategory(HashMap<String, List<Transaction>> transactionMapByParentCategory) {
        this.transactionMapByParentCategory = transactionMapByParentCategory;
    }

    public TransactionCallback getTransactionCallback() {
        return transactionCallback;
    }

    public void setTransactionCallback(TransactionCallback transactionCallback) {
        this.transactionCallback = transactionCallback;
    }

    @Override
    public void onReceivedTransaction(HashMap<String, List<Transaction>> transactionHashMap, HashMap<String, List<Transaction>> transactionMapByParentCategory, ArrayList<PieEntry> categories) {

        // MainActivity.showMaps(categories, transactionMap);
    }

    @Override
    public void getTransactions(ArrayList<Transaction> transactions) {


    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }
}
