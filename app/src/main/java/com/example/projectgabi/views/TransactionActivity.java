package com.example.projectgabi.views;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.projectgabi.R;
import com.example.projectgabi.classes.Account;
import com.example.projectgabi.classes.Transaction;
import com.example.projectgabi.controllers.AccountController;
import com.example.projectgabi.controllers.TransactionExcelModel;
import com.example.projectgabi.enums.TransactionType;
import com.example.projectgabi.interfaces.AccountCallback;
import com.example.projectgabi.interfaces.AccountNameCallback;
import com.example.projectgabi.utils.Constants;
import com.example.projectgabi.utils.DateConverter;
import com.example.projectgabi.utils.RequestHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SuppressLint("Range")
public class TransactionActivity extends AppCompatActivity {

    private static int bankNumber = 1;
    Button saveBtn, cancelBtn, pickDateBtn, importBtn;
    EditText amountEt, descriptionEt, dateEt;
    Spinner categorySpn, accountSpn ;
    RadioGroup typeToggle;
ActivityResultLauncher<Intent> mGetContent;
    DatePicker datePicker;
    String transactionDate;
    Intent intent;
    Context context = this;
    public static final String TRANSACTION_KEY = "transaction";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        importHandler();
        initComponents();
        intent = getIntent();

        initializeSpinnerElements();
        importBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                TransactionExcelModel transactionExcelModel = new TransactionExcelModel();
//                transactionExcelModel.setContext(context);
//                transactionExcelModel.handleImportedTransactions(context);
//
               openFileExplorer();
//                intent = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(intent);
//                finish();
            }
        });


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

                //sendTransaction(transaction, );
                String accountName = accountSpn.getSelectedItem().toString();
                handleTransaction(transaction,accountName);
                updateAccount(transaction, accountName);
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



    public void openFileExplorer() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent = Intent.createChooser(intent, "Choose a file");
        mGetContent.launch(intent);

    }

    @SuppressLint("Range")
    private void importHandler() {

        mGetContent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),

                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Uri uri = data.getData();
                        String uriString = uri.toString();
                        File myFile = new File(uriString);
                        String path = myFile.getPath();
                        String displayName = null;

                        if (uriString.startsWith("content://")) {
                            Cursor cursor = null;
                            try {
                                cursor = context.getContentResolver().query(uri, null, null, null, null);
                                if (cursor != null && cursor.moveToFirst()) {

                                    displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                                }
                            } finally {
                                cursor.close();
                            }
                        } else if (uriString.startsWith("file://")) {
                            displayName = myFile.getName();
                        }

                        Log.d("Import", "File Name : " + displayName);
                        Log.d("Import", "File Path : " + path);
                        // Handle the selected file here if needed
                        TransactionExcelModel transactionExcelModel = new TransactionExcelModel();
                        transactionExcelModel.handleImportedTransactions(path, displayName, context, uri);



//                        intent = new Intent(getApplicationContext(), MainActivity.class);
//                        startActivity(intent);
//                        finish();
                    }
                });

    }

    private void updateAccount(Transaction transaction, String accountName) {

        AccountController accountController = new AccountController();
        accountController.setContext(context);
        accountController.updateAccount(transaction, accountName);


    }

    private void initializeSpinnerElements() {

        AccountController accountController = new AccountController();
        accountController.getAccounts(context);
        accountController.setAccountCallback(new AccountCallback() {
            @Override
            public void onReceivedAccount(ArrayList<Account> accounts) {
                ArrayList<String > accountNames = new ArrayList<>();
                accountNames = TransactionActivity.getAccountNames(accounts);
                ArrayAdapter<String> accountArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, accountNames);
                accountArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                accountSpn.setAdapter(accountArrayAdapter);

            }
        });


    }

    private static ArrayList<String> getAccountNames(ArrayList<Account> accounts) {
        ArrayList<String> accountNames = new ArrayList<>();

        for (Account account : accounts) {
            // if the account has the same name, add the bank number to the name
            if (accountNames.contains(account.getBankName())) {
                int i = accountNames.indexOf(account.getBankName());
                accountNames.set(i, account.getBankName() + " #" + bankNumber );
                bankNumber++;
                accountNames.add(account.getBankName() + " #" + bankNumber );

                continue;
            }
            accountNames.add(account.getBankName() );
        }
        return accountNames;
    }

    private void sendTransaction(Transaction transaction, Account account) {
        // in this method the created transaction is sent to the database server

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.POST_TRANSACTION_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("transaction ", response.toString());
                        Toast.makeText(getApplicationContext(), "Se incarca datele..", Toast.LENGTH_SHORT).show();

                    }
                },
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
                Log.d("transaction", "Account id: " + account.getFk_user().toString());
                params.put("FK_AccountID", account.getAccountID());
                params.put("FK_UserID", LoginPage.userID);
                // Log.d("params", params.toString().trim());

                return params;
            }
        };
        RequestHandler.getInstance(this)
                .addToRequestQueue(stringRequest);
    }

    private void handleTransaction(Transaction transaction, String accountName) {
        AccountController accountController = new AccountController();
        accountController.setContext(context);
        accountController.getAccount(accountName);
        Log.d("transaction", "Account name: " + accountName);
        accountController.setAccountNameCallback(new AccountNameCallback() {
            @Override
            public void onReceivedAccountName(Account account) {

                 sendTransaction(transaction, account);

            }
        });


    }

    public void initComponents() {
        saveBtn = findViewById(R.id.transactionSavingButton);
        cancelBtn = findViewById(R.id.transactionCancellingButton);
        amountEt = findViewById(R.id.transactionValueEditText);
        descriptionEt = findViewById(R.id.transactionDescriptionEditText);
        typeToggle = findViewById(R.id.transactionTypeToggle);
       // dateEt = findViewById(R.id.transactionDateInput);
        accountSpn = findViewById(R.id.transactionAccountSpin);
        pickDateBtn = findViewById(R.id.transactionPickDateBtn);
        importBtn = findViewById(R.id.transactionImportBtn);


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
