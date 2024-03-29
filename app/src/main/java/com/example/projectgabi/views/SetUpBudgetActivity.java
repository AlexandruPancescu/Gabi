package com.example.projectgabi.views;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.projectgabi.R;
import com.example.projectgabi.classes.Budget;
import com.example.projectgabi.classes.BudgetItem;
import com.example.projectgabi.classes.Category;
import com.example.projectgabi.classes.User;
import com.example.projectgabi.models.BudgetController;
import com.example.projectgabi.models.CategoryController;
import com.example.projectgabi.models.UserController;
import com.example.projectgabi.interfaces.UserCallback;
import com.example.projectgabi.utils.DateConverter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class SetUpBudgetActivity extends AppCompatActivity {

    Intent intent;
    Context context;
    Button createBudgetBtn, setUpDatesBtn;
    EditText startDate, water,  endDate, mortgage, electricity, phone,
            cableAndInternet, food, car, health, entertainment,
            pets, clothing, goingOut, other, savings, investments;
    ArrayList<Category> categories;
    ArrayList<BudgetItem> budgetItems;
    ArrayList<EditText> editTexts;
    Budget budget;
    String startDateValue, endDateValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_budget);
        initializeElements();

        setUpDatesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open two date pickers, one with the title start date and one with the title end date
                // and set startDate and endDate to the values from the date pickers
                final Calendar c = Calendar.getInstance();

                // on below line we are getting
                // our day, month and year.
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                // on below line we are creating a variable for date picker dialog.
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        // on below line we are passing context.
                        SetUpBudgetActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our text view.

                                endDateValue = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                endDate.setText(endDateValue);
                            }
                        },

                        year, month, day);

                datePickerDialog.show();

                DatePickerDialog datePickerDialog2 = new DatePickerDialog(
                        // on below line we are passing context.
                        SetUpBudgetActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our text view.


                                startDateValue = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                startDate.setText(startDateValue);

                            }
                        },
                        year, month, day);

                datePickerDialog2.show();
            }
        });

        createBudgetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // aici faci get user id
                BudgetController budgetController = new BudgetController();
                budgetHandler(budgetController);



                // for categories



                intent = new Intent(getApplicationContext(), BudgetTrackerActivity.class);
                startActivity(intent);

            }
        });


    }

    private void budgetHandler(BudgetController budgetController) {
        UserController userController = new UserController();
        userController.setContext(context);
        String email = LoginPage.userEmail;
        String password = LoginPage.userPassword;
        userController.getUser(email, password);

        Log.d("budget handler", "budgetHandler: " + LoginPage.userID);
        userController.setUserCallback(new UserCallback() {
            @Override
            public void onReceivedUser(User user) {
                initializeBudget();
                budgetController.createBudget(context, budget, user.getUserID());
                initializeBudgetItems();
                CategoryController categoryController = new CategoryController();
                createCategoriesInDb(categoryController);
                createBudgetItemInBd(budgetController);
            }
        });
    }

    private void createCategoriesInDb(CategoryController categoryController) {
        Log.d("categories", "initializeCategories: ");
        for (Category category : categories) {
            categoryController.createCategory(context, category);
        }
    }

    private void createBudgetItemInBd(BudgetController budgetController) {

        for (BudgetItem budgetItem : budgetItems) {
            budgetController.createBudgetItem(context, budgetItem);
        }

    }

    private void initializeBudget() {

        String id = UUID.randomUUID().toString();
        Date startDate = DateConverter.fromString(this.startDate.getText().toString());
        Date endDate = DateConverter.fromString(this.endDate.getText().toString());
        UserController userController = new UserController();
        String user_foreign_key =LoginPage.userID;
        budget = new Budget(id, startDate, endDate, user_foreign_key);
        Log.d("Budget setup ",budget.toString() );

    }

    private void initializeElements() {

        setUpDatesBtn = findViewById(R.id.setUpBudgetDatesBtn);
        categories = new ArrayList<Category>() {
        };
        editTexts = new ArrayList<>();
        budgetItems = new ArrayList<BudgetItem>() {
        };

        // block dor edit texts initalizations
        {
            createBudgetBtn = findViewById(R.id.createBudgetBtn);
            startDate = findViewById(R.id.startDateInput);
            endDate = findViewById(R.id.endDateInput);
            startDate.setText(DateConverter.fromDate(Calendar.getInstance().getTime()));
            // plus a month
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, 1);
            endDate.setText(DateConverter.fromDate(calendar.getTime()));


            mortgage = findViewById(R.id.editTextMortgageRent);
            electricity = findViewById(R.id.editTextElectricity);
            phone = findViewById(R.id.editTextPhoneBills);
            cableAndInternet = findViewById(R.id.editTextCableInternet);
            water = findViewById(R.id.editWater);

            food = findViewById(R.id.editTextFood);
            pets = findViewById(R.id.editTextPets);
            car = findViewById(R.id.editTextCar);
            clothing = findViewById(R.id.editTextClothes);
            health = findViewById(R.id.editTextHealth);
            goingOut = findViewById(R.id.editTextGoingOut);
            entertainment = findViewById(R.id.editTextEntertainment);

            savings = findViewById(R.id.editTextSavings);
            investments = findViewById(R.id.editTextInvestments);

            other = findViewById(R.id.editTextOther);
        }

        initializeCategoryList();


    }

    private void initializeBudgetItems() {
        for (int i = 0; i < categories.size(); i++) {
            budgetItems.add(new BudgetItem(budget.getBudgetID(),
                    Double.parseDouble(editTexts.get(i).getText().toString())
                    , categories.get(i)));
        }
    }

    private void initializeCategoryList() {
        categories.add(new Category("Mortgage/Rent", "Frequent"));
        categories.add(new Category("Electricity", "Frequent"));
        categories.add(new Category("Phone bills", "Frequent"));
        categories.add(new Category("Cable-Internet Subscription", "Frequent"));
        categories.add(new Category("Water", "Frequent"));

        categories.add(new Category("Food", "Non-Frequent"));
        categories.add(new Category("Pets", "Non-Frequent"));
        categories.add(new Category("Car", "Non-Frequent"));
        categories.add(new Category("Clothes", "Non-Frequent"));
        categories.add(new Category("Health", "Non-Frequent"));
        categories.add(new Category("Going Out", "Non-Frequent"));
        categories.add(new Category("Entertainment", "Non-Frequent"));

        categories.add(new Category("Savings", "Savings and Investments"));
        categories.add(new Category("Investments", "Savings and Investments"));

        categories.add(new Category("Other", "Other"));

        editTexts.add(mortgage);
        editTexts.add(electricity);
        editTexts.add(phone);
        editTexts.add(cableAndInternet);
        editTexts.add(food);
        editTexts.add(pets);
        editTexts.add(car);
        editTexts.add(clothing);
        editTexts.add(health);
        editTexts.add(goingOut);
        editTexts.add(water);
        editTexts.add(entertainment);
        editTexts.add(savings);
        editTexts.add(investments);
        editTexts.add(other);

    }


}