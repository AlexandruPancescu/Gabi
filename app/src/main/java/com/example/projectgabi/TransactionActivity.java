package com.example.projectgabi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projectgabi.Utils.DateConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
        saveBtn.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View v) {

                    Transaction transaction = createTransaction();
                        intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra(TRANSACTION_KEY,  transaction);
                        setResult(RESULT_OK, intent);
                        startActivity(intent);
                        finish();

            }
        });
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


    private Transaction createTransaction()  {
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
