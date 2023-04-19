package com.example.projectgabi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.projectgabi.Utils.Constants;
import com.example.projectgabi.Utils.DateConverter;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TransactionActivity extends AppCompatActivity {

    Button saveBtn, cancelBtn;
    EditText amountEt, descriptionEt, dateEt;
    Spinner categorySpn;
    RadioGroup typeToggle;

    Intent intent;
    public static final String TRANSACTION_KEY = "transaction";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        initComponents();
        intent = getIntent();
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Transaction transaction = createTransaction();
                sendTransaction(transaction);
                intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra(TRANSACTION_KEY, transaction);
                setResult(RESULT_OK, intent);
                startActivity(intent);
                finish();


            }
        });
    }

    private void sendTransaction(Transaction transaction) {
        // in this method the created thasaction is sent to the databse server
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.CREATE_TRANSACTION_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("params", "merge");
                      //  Toast.makeText(getApplicationContext(), "Se incarca datele..", Toast.LENGTH_SHORT).show();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                           // Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                ,
                new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                  Log.d("my tag error", error.getMessage());
                  //show the error
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();

                Toast.makeText(getApplicationContext(), "Eroare la incarcarea datelor", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("value", String.valueOf(transaction.getValue()).trim());
                params.put("type", String.valueOf(transaction.getType()).trim());
                params.put("category", transaction.getCategory());
                params.put("date", "2020-01-01");
                params.put("description", transaction.getDescription().trim());
                Log.d("params", params.toString().trim());

                return params;
            }
        };

        // Add the request to the RequestQueue, using the singleton pattern
        // (the request queue is created in the singleton class)
         RequestHandler.getInstance(this)
                .addToRequestQueue(stringRequest);


    }


    public void initComponents() {

        saveBtn = findViewById(R.id.transactionSavingButton);
        cancelBtn = findViewById(R.id.transactionCancellingButton);
        amountEt = findViewById(R.id.transactionValueEditText);
        descriptionEt = findViewById(R.id.transactionDescriptionEditText);
        typeToggle = findViewById(R.id.transactionTypeToggle);
        dateEt = findViewById(R.id.transactionDateInput);

        ArrayAdapter<CharSequence> categoryAdapter =
                ArrayAdapter.createFromResource(getApplicationContext(), R.array.spinner_category_list, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);

        categorySpn = findViewById(R.id.transactionCategorySpinner);
        categorySpn.setAdapter(categoryAdapter);
    }


    private Transaction createTransaction() {
        RadioButton checkedVariant = findViewById(typeToggle.getCheckedRadioButtonId());
        TransactionType type = transactionTypeConverter(checkedVariant);
        String category = categorySpn.getSelectedItem().toString();
        double value = Double.parseDouble(amountEt.getText().toString());
        String description = descriptionEt.getText().toString();
        DateConverter dateConverter = new DateConverter();
        Date date = dateConverter.fromString(dateEt.getText().toString());
        return new Transaction(111, type, category, value, date, description);

    }


    public TransactionType transactionTypeConverter(RadioButton checkedVariant) {
        if (checkedVariant.getText().toString().equals("expense")) {
            return TransactionType.EXPENSE;
        } else return TransactionType.INCOME;
    }


}
