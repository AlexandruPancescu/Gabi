package com.example.projectgabi.models;


import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.projectgabi.classes.Account;
import com.example.projectgabi.classes.Transaction;
import com.example.projectgabi.interfaces.AccountCallback;
import com.example.projectgabi.interfaces.AccountNameCallback;
import com.example.projectgabi.utils.Constants;
import com.example.projectgabi.utils.RequestHandler;
import com.example.projectgabi.views.LoginPage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AccountController implements AccountCallback {

    ArrayList<Account> accounts;
    AccountCallback accountCallback;
    AccountNameCallback accountNameCallback;
    Context context ;

    public AccountController() {
        this.accounts = new ArrayList();
    }

    public void createAccount(Context context, Account account) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.CREATE_BANK_ACCOUNT_URL, response -> {
        }, error -> {
            Log.d("New Bank account", error.toString());
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", account.getAccountID());
                params.put("bankName", account.getBankName().trim());
                params.put("balance", String.valueOf(account.getBalance()).trim() );
                params.put("currency", account.getCurrency().trim());
                params.put("additionalInfo", account.getAdditionalInfo().trim());
                params.put("FK_USER", account.getFk_user().trim());
                Log.d("New Bank account sent", account.toString().trim());
                return params;
            }
        };
        RequestHandler.getInstance(context).addToRequestQueue(stringRequest);


    }

//    Log.d("new Account message", response.toString())  ;
//              Log.d("New Account getAccounts", accounts.toString());

    public synchronized void getAccounts(Context context) {
        String url = Constants.GET_BANK_ACCOUNTS_URL + "?FK_USER=" + LoginPage.userID;
        JsonObjectRequest request = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
              AccountController.this.getBankAccountsFromJSON(response);
              accountCallback.onReceivedAccount(accounts);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error != null) {
                    Log.d("New Account error", error.toString());
                } else {
                    Log.d("New Account error", "error");
                }
            }
        });
        RequestHandler.getInstance(context).addToRequestQueue(request);
    }

    public synchronized void getAccount( String accountName) {
        String url = Constants.GET_BANK_ACCOUNT_URL + "?name=" + accountName;
        JsonObjectRequest request = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //AccountController.this.getBankAccountsFromJSON(response);
                Account account = new Account();
                try {
                    account =  AccountController.getAccountFromJSON(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("AccountController", e.toString());
                }
                Log.d("AccountController", account.toString());
                accountNameCallback.onReceivedAccountName(account);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error != null) {
                    Log.d("New Account error", error.toString());
                } else {
                    Log.d("New Account error", "error");
                }
            }
        });
        RequestHandler.getInstance(context).addToRequestQueue(request);
    }

    private static Account getAccountFromJSON(JSONObject response) throws JSONException {
        Account account = new Account();
        JSONObject accountJSON = response.getJSONObject("bankAccount");
        try {

            String id = accountJSON.getString("PK_AccountID");
            String bankName = accountJSON.getString("BankName");
            float balance = (float) accountJSON.getDouble("Balance");
            String currency = accountJSON.getString("Currency");
            String additionalInfo = accountJSON.getString("AdditionalInfo");
            account = new Account(id, bankName, balance, currency, additionalInfo);
            account.setFk_user(accountJSON.getString("FK_USER"));
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("AccountController", e.toString());
        }
        return account;
    }


    public void getBankAccountsFromJSON(JSONObject jsonObject) {
          Log.d("New Account", "getBankAccountsFromJSON"); 
        try {
            JSONArray jsonArray = jsonObject.getJSONArray(Constants.KEY_ACCOUNT_ARRAY_KEY);
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject accountObject = jsonArray.getJSONObject(i);
                String id = accountObject.getString(Constants.KEY_ACCOUNT_ID);
                String bankName = accountObject.getString(Constants.KEY_ACCOUNT_NAME);
                float balance = (float) accountObject.getDouble(Constants.KEY_ACCOUNT_BALANCE);
                String currency = accountObject.getString(Constants.KEY_ACCOUNT_CURRENCY);
                String additionalInfo = accountObject.getString(Constants.KEY_ACCOUNT_ADDITIONAL_INFO);
                Account account = new Account(id, bankName, balance, currency, additionalInfo);
                this.accounts.add(account);

                Log.d("New Account", account.toString());
            }


        } catch (JSONException e) {
            Log.d("New Account", "error" + e.toString());
            e.printStackTrace();
        }


    }

    public void updateAccount(Transaction transaction,  String accountName) {
        // StringRequest stringRequest = new StringRequest();
        String url = Constants.UPDATE_BANK_ACCOUNT_URL + "?name="+accountName + "&value="+transaction.getValue()
        +  "&type="+transaction.getType() ;
        Log.d("New Account", "updateAccount" + url);

       StringRequest stringRequest = new StringRequest(Request.Method.PUT, url, response -> {
           Log.d("New Account", "updateAccount");
         }, error -> {
            Log.d("New Account", "error" + error.toString());
        }) ;

        RequestHandler.getInstance(context).addToRequestQueue(stringRequest);

    }

    public ArrayList getAccountsList() {
        return accounts;
    }


    public ArrayList<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(ArrayList<Account> accounts) {
        this.accounts = accounts;
    }

    public AccountCallback getAccountCallback() {
        return accountCallback;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setAccountCallback(AccountCallback accountCallback) {
        this.accountCallback = accountCallback;
    }

    public AccountNameCallback getAccountNameCallback() {
        return accountNameCallback;
    }

    public void setAccountNameCallback(AccountNameCallback accountNameCallback) {
        this.accountNameCallback = accountNameCallback;
    }

    @Override
    public void onReceivedAccount(ArrayList<Account> accounts) {

    }
}
