package com.example.projectgabi.views;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.projectgabi.R;
import com.example.projectgabi.classes.Transaction;
import com.example.projectgabi.enums.TransactionType;
import com.example.projectgabi.utils.Constants;
import com.example.projectgabi.utils.DateConverter;
import com.example.projectgabi.utils.RequestHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TransactionActivity extends AppCompatActivity {

    Button saveBtn, cancelBtn, pickDateBtn;
    EditText amountEt, descriptionEt, dateEt;
    Spinner categorySpn;
    RadioGroup typeToggle;
    DatePicker datePicker;
    String transactionDate;
    Intent intent;
    public static final String TRANSACTION_KEY = "transaction";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        initComponents();
        intent = getIntent();
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

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

        pickDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   datePicker.setVisibility(View.VISIBLE);
                final Calendar c = Calendar.getInstance();

                // on below line we are getting
                // our day, month and year.
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                // on below line we are creating a variable for date picker dialog.
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        // on below line we are passing context.
                       TransactionActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our text view.
                                transactionDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;

                            }
                        },
                        // on below line we are passing year,
                        // month and day for selected date in our date picker.
                        year, month, day);
                // at last we are calling show to
                // display our date picker dialog.
                datePickerDialog.show();
            }
        });
    }

    private void sendTransaction(Transaction transaction) {

        // verify if the url is correct
        Log.d("transaction url", Constants.CREATE_TRANSACTION_URL);
        if (Constants.CREATE_TRANSACTION_URL == null) {
            Log.d("transaction url", "url is null");
            return;
        }
        // in this method the created transaction is sent to the database server


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.CREATE_TRANSACTION_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("transaction ", response.toString());
                        Toast.makeText(getApplicationContext(), "Se incarca datele..", Toast.LENGTH_SHORT).show();
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
                    }
                }
                ,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error != null && error.getMessage() != null)
                            Log.d("transaction error", error.getMessage());
                        else {
                            Log.d("transaction error", "Unknown error");
                        }
                        Toast.makeText(getApplicationContext(), "Eroare la incarcarea datelor", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                Log.d("transaction", "Transaction " + transaction.getCategory() + " sent with value: " + transaction.getValue());
                params.put("value", String.valueOf(transaction.getValue()).trim());
                params.put("type", String.valueOf(transaction.getType()).trim());
                params.put("category", transaction.getCategory());
                params.put("date", DateConverter.fromDate(transaction.getDate()));
                params.put("description", transaction.getDescription().trim());
                params.put("id", transaction.getId().toString());
                params.put("parentCategory", transaction.getParentCategory());
                // Log.d("params", params.toString().trim());

                return params;
            }
        };
        RequestHandler.getInstance(this)
                .addToRequestQueue(stringRequest);
    }


    public void initComponents() {
        saveBtn = findViewById(R.id.transactionSavingButton);
        cancelBtn = findViewById(R.id.transactionCancellingButton);
        amountEt = findViewById(R.id.transactionValueEditText);
        descriptionEt = findViewById(R.id.transactionDescriptionEditText);
        typeToggle = findViewById(R.id.transactionTypeToggle);
       // dateEt = findViewById(R.id.transactionDateInput);

        pickDateBtn = findViewById(R.id.transactionPickDateBtn);

        ArrayAdapter<CharSequence> categoryAdapter =
                ArrayAdapter.createFromResource(getApplicationContext(),
                        R.array.spinner_category_list, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);

        categorySpn = findViewById(R.id.transactionCategorySpinner);
        categorySpn.setAdapter(categoryAdapter);
    }


    private Transaction createTransaction() {
        RadioButton checkedVariant = findViewById(typeToggle.getCheckedRadioButtonId());
        TransactionType type = transactionFromString(checkedVariant);
        String category = categorySpn.getSelectedItem().toString();
        double value = Double.parseDouble(amountEt.getText().toString());

        String description = descriptionEt.getText().toString();
        DateConverter dateConverter = new DateConverter();
        // by default the date is the current date
       Date date = Calendar.getInstance().getTime();
        if(transactionDate == null) {
            Toast.makeText(getApplicationContext(), "It shall have the current date", Toast.LENGTH_SHORT).show();
        }else{
            date = dateConverter.fromString(transactionDate);
        }

        UUID uuid = UUID.randomUUID();
        String parentCategory = putParentCategory(category);
        Log.d("Parent category", parentCategory);
        Log.d("Transaction id", uuid.toString());
        return new Transaction(String.valueOf(uuid), type, category, value, date, description, parentCategory);
    }

    private String putParentCategory(String category) {
        // make switch case for each category
        // to do, make a list of frequent categories, like in the array values
        switch (category) {
            case "Mortgage/Rent":
            case "Electricity":
            case "Water":
            case "Phone bills":
            case "Cable-Internet Subscription":
                return "Frequent";

            case "Clothes":
            case "Yard":
            case "Health":
            case "Going out":
            case "Pets":
            case "Car":
            case "Entertainment":
            case "Food":
                return "Non-Frequent";

            case "Savings":
            case "Investments":
                return "Savings and Investments";

            default:
                return "Other";
        }
    }


    public TransactionType transactionFromString(RadioButton checkedVariant) {
        if (checkedVariant.getText().toString().equals("Expense")) {
            return TransactionType.EXPENSE;
        } else return TransactionType.INCOME;
    }


}
