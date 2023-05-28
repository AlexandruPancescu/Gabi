package com.example.projectgabi.Controllers;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.projectgabi.Interfaces.TransactionCallback;
import com.example.projectgabi.MainActivity;
import com.example.projectgabi.R;
import com.example.projectgabi.Transaction;
import com.example.projectgabi.TransactionType;
import com.example.projectgabi.Utils.Constants;
import com.example.projectgabi.Utils.DateConverter;
import com.example.projectgabi.Utils.RequestHandler;
import com.example.projectgabi.Utils.TransactionExpandableListAdapter;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TransactionController implements TransactionCallback {

    TransactionCallback transactionCallback;
    ArrayList<PieEntry> categories;
    HashMap<String, List<Transaction>> transactionMap;
    HashMap<String, List<Transaction>> transactionMapByParentCategory;

    public TransactionController() {
//        transactionCallback = new MainActivity();
        categories = new ArrayList<>();
        transactionMap = new HashMap<>();
        transactionMapByParentCategory = new HashMap<>();
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
                Log.d("transaction", "parentCategory: " + parentCategory);
                // generate an UUID for transaction
                UUID uuid = UUID.randomUUID();
                String id = jsonTransaction.getString(Constants.KEY_TRANSACTION_ID);
                Transaction transaction = new Transaction(id, type, category, value, date, description, parentCategory);

                //in the transaction map, add the transaction to the list of transactions, based on the cateogry


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

    public void addChartValues() {
        if (transactionMap.isEmpty() || transactionMap == null) {
            Log.d("Main transactions", "no transactions found");
            return;
        } else {
            Log.d("Main transactions", "transactions found");
        }

        if (categories == null) {
            categories = new ArrayList<>();
        }

        // add the value of each object, based on the key set, then  make a new Pientry object and add it to the list

        for (String category : transactionMap.keySet()) {
            double value = 0;
            for (Transaction transaction : transactionMap.get(category)) {
                value += transaction.getValue();
            }
            categories.add(new PieEntry((float) value, category));
        }

        // Pie chart data set
        PieDataSet pieDataSet = new PieDataSet(categories, "Transactions by category");
    } // get the values from the transaction


    public synchronized void createComponents(Context context) {
        Log.d("TransactionController", "createComponents: " + Constants.READ_TRANSACTION_URL);
        JsonObjectRequest request = new JsonObjectRequest(Constants.READ_TRANSACTION_URL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("TransactionController JSON", response.toString());

                    TransactionController.this.getTransactionFromJson(response);
                    Log.d("TransactionController", "transactionMap: " + transactionMap.toString());
                    Log.d("TransactionController", "transactionMapByParentCategory: " + transactionMapByParentCategory.toString());

                    transactionCallback.onTransactionReceived(transactionMap, transactionMapByParentCategory, categories);
                    //  TransactionController.this.addChartValues(); // get the values from the transaction


                    // createList(transactionMapByParentCategory);


                } catch (Exception e) {
                    Log.d("TransactionController", "error in json catch" + e.toString());

                }
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error != null) {
                    Log.d("TransactionController error", error.toString());

                } else {
                    Log.d("TransactionController error", "unknown");
                }
            }
        });

        RequestHandler.getInstance(context).addToRequestQueue(request);

    }

    public List<PieEntry> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<PieEntry> categories) {
        this.categories = categories;
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
    public void onTransactionReceived(HashMap<String, List<Transaction>> transactionHashMap, HashMap<String, List<Transaction>> transactionMapByParentCategory, ArrayList<PieEntry> categories) {

        MainActivity.showMaps(categories, transactionMap);
    }
    //    private void initPieChart(ArrayList<PieEntry> categories) {
//
//        Log.d("Main transactions", "size : " + transactionMap.size());
//        Resources res = getResources();
//        PieDataSet pieDataSet = new PieDataSet(categories, "Categories");
//        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
//        pieDataSet.setValueTextColor(R.color.white);
//        pieDataSet.setValueTextSize(16f);
//
//        pieDataSet.setValueFormatter(new DefaultValueFormatter(0));
//
//        pieData = new PieData(pieDataSet);
//
//        pieChart.setData(pieData);
//        pieChart.getDescription().setEnabled(false);
//        pieChart.setCenterText("Categories");
//        pieChart.animate();
//        pieChart.invalidate();
//
//    }
}
//            private void createList(HashMap<String, List<Transaction>> transactionMap) {
//
//                ArrayList<String> keys = new ArrayList<>(transactionMap.keySet());
//
//                expandableListAdapter = new TransactionExpandableListAdapter(getApplicationContext(), keys, metaCategoryMap);
//
//                expandableListView.setAdapter(expandableListAdapter);
//
//                expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
//
//                    int previousGroup = -1;
//
//                    @Override
//                    public void onGroupExpand(int groupPosition) {
//                        if (groupPosition != previousGroup && previousGroup != -1) {
//                            expandableListView.collapseGroup(previousGroup);
//                        }
//                        previousGroup = groupPosition;
//                        Log.d("createList", "groupPosition: " + groupPosition);
//                    }
//                });
//
//                expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//                    @Override
//                    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
//                        String selected = metaCategoryMap.get(metaCategoryMap.keySet().toArray()[groupPosition]).get(childPosition).toString();
//                        Toast.makeText(getApplicationContext(), selected, Toast.LENGTH_SHORT).show();
//                        Log.d("createList", "selected: " + selected);
//
//                        return true;
//                    }
//
//
//                });
//                metaCategoryMap = expandableListAdapter.getTransactionHashMap();
//
//
//            }