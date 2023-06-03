package com.example.projectgabi.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.projectgabi.R;
import com.example.projectgabi.classes.Budget;
import com.example.projectgabi.classes.BudgetItem;
import com.example.projectgabi.classes.Category;
import com.example.projectgabi.controllers.BudgetController;
import com.example.projectgabi.controllers.CategoryController;
import com.example.projectgabi.controllers.UserController;
import com.example.projectgabi.utils.DateConverter;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class SetUpBudgetActivity extends AppCompatActivity {

    Intent intent;
    Context context;
    Button createBudgetBtn;
    EditText startDate, endDate, mortgage, utilities, phone,
            cableAndInternet, food, car, health, entertainment,
            pets, clothing, goingOut, other, savings, investments;
    ArrayList<Category> categories;
    ArrayList<BudgetItem> budgetItems;
    ArrayList<EditText> editTexts;
    Budget budget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_budget);
        initializeElements();
        createBudgetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // aici faci get user id
                BudgetController budgetController = new BudgetController();
                budget = initializeBudget();
                budgetController.createBudget(context, budget);

                // for categories
                initializeBudgetItems();

                CategoryController categoryController = new CategoryController();
                createCategoriesInDb(categoryController);


                createBudgetItemInBd(budgetController);


                intent = new Intent(getApplicationContext(), BudgetTrackerActivity.class);
                startActivity(intent);

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

    private Budget initializeBudget() {
        UUID uuid = UUID.randomUUID();
        String id = uuid.toString();
        Date startDate = DateConverter.fromString(this.startDate.getText().toString());
        Date endDate = DateConverter.fromString(this.endDate.getText().toString());
        UserController userController = new UserController();
        String user_foreign_key = userController.getUserID(context);

        return new Budget(id, startDate, endDate, user_foreign_key);
    }

    private void initializeElements() {

        categories = new ArrayList<Category>() {};
        editTexts = new ArrayList<>();
        budgetItems = new ArrayList<BudgetItem>() {};

        // block dor edit texts initalizations
        {
            createBudgetBtn = findViewById(R.id.createBudgetBtn);
            startDate = findViewById(R.id.startDateInput);
            endDate = findViewById(R.id.endDateInput);

            mortgage = findViewById(R.id.editTextMortgageRent);
            utilities = findViewById(R.id.editTextUtilities);
            phone = findViewById(R.id.editTextPhoneBills);
            cableAndInternet = findViewById(R.id.editTextCableInternet);

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
        // add the values of each of the edit text variables into the categories array
        // take the values from the R.array.spinner_category_list and put them into the categories array
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

        categories.add(new Category("Utilities", "Frequent"));
        categories.add(new Category("Phone Bills", "Frequent"));
        categories.add(new Category("Cable/Internet", "Frequent"));

        categories.add(new Category("Food", "Non-Frequent"));
        categories.add(new Category("Pets", "Non-Frequent"));
        categories.add(new Category("Car", "Non-Frequent"));
        categories.add(new Category("Clothing", "Non-Frequent"));
        categories.add(new Category("Health", "Non-Frequent"));
        categories.add(new Category("Going Out", "Non-Frequent"));
        categories.add(new Category("Entertainment", "Non-Frequent"));

        categories.add(new Category("Savings", "Savings and Investments"));
        categories.add(new Category("Investments", "Savings and Investments"));

        categories.add(new Category("Other", "Other"));

        editTexts.add(mortgage);
        editTexts.add(utilities);
        editTexts.add(phone);
        editTexts.add(cableAndInternet);
        editTexts.add(food);
        editTexts.add(pets);
        editTexts.add(car);
        editTexts.add(clothing);
        editTexts.add(health);
        editTexts.add(goingOut);
        editTexts.add(entertainment);
        editTexts.add(savings);
        editTexts.add(investments);
        editTexts.add(other);

    }


}