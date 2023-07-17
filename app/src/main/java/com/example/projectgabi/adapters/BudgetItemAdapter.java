package com.example.projectgabi.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.projectgabi.R;
import com.example.projectgabi.classes.Account;
import com.example.projectgabi.classes.Budget;
import com.example.projectgabi.classes.BudgetItem;
import com.example.projectgabi.classes.Category;
import com.example.projectgabi.classes.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BudgetItemAdapter extends ArrayAdapter<BudgetItem> {
    TextView categoryNameTv, spentAmountTv, budgetedAmountTv;
    ProgressBar progressBar;
    private int resource;
    private List<BudgetItem> objects;
    private LayoutInflater inflater;
    Context context;

    Budget budget;
    ArrayList<Category> categoryArrayList;
    ArrayList<BudgetItem> budgetItemArrayList;
    ArrayList<Transaction> transactionArrayList;
    List<String> parentCategoriesList;
    HashMap<String, ArrayList<BudgetItem>> budgetItemHashMap;

    public BudgetItemAdapter(@NonNull Context context, int resource, @NonNull List<BudgetItem> objects , LayoutInflater inflater) {
        super(context, resource, objects);
        this.resource = resource;
        this.inflater = inflater;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = inflater.inflate(resource, parent, false);
        categoryNameTv = row.findViewById(R.id.budgetItemCategoryName);
        spentAmountTv = row.findViewById(R.id.budgetItemAmountTv);
        budgetedAmountTv = row.findViewById(R.id.budgetItemSpentTv);
        progressBar = row.findViewById(R.id.budgetItemProgressBar);

        BudgetItem budgetItem = objects.get(position);

        float spentAmount = this.getSpentAmount(budgetItem);
        double budgetItemAmount = budgetItem.getBudgetItemAmount();
        categoryNameTv.setText(budgetItem.getCategory().getCategoryName());
        spentAmountTv.setText(String.valueOf(budgetItem.getBudgetItemAmount()));
        budgetedAmountTv.setText(String.valueOf( spentAmount));
        progressBar.setProgress((int) spentAmount);

        if (spentAmount < budgetItemAmount) {
            if (spentAmount ==0 ){
                progressBar.setProgress(0);
                progressBar.setProgressTintList(context.getResources().getColorStateList(R.color.switch_light_grey));
            }else{

                Drawable progressDrawable =  progressBar.getProgressDrawable();
//                progressDrawable.setColorFilter(context.getResources().getColor(R.color.progress_bar_green),
//                        PorterDuff.Mode.MULTIPLY);

                progressBar.setProgressDrawable(progressDrawable);

            }
        } else if (spentAmount == budgetItemAmount) {
            progressBar.setProgressTintList(context.getResources().getColorStateList(R.color.progress_bar_orange));
            progressBar.setProgress((int) budgetItemAmount);
            progressBar.setMax((int) budgetItemAmount);
        } else if (spentAmount > budgetItemAmount) {
            progressBar.setProgress((int) budgetItemAmount);
            progressBar.setMax((int) budgetItemAmount);
            progressBar.setProgressTintList(context.getResources().getColorStateList(R.color.progress_bar_red));
        }


        return row;
    }

    private float getSpentAmount(BudgetItem budgetItem) {
        float spentAmount = 0;
        Log.d("BudgetItem Adapter", "getSpentAmount: " + transactionArrayList.toString());
        for (Transaction transaction : transactionArrayList) {


            if (transaction.getCategory().equals(budgetItem.getCategory().getCategoryName())
                    && transaction.getDate().getMonth() == budget.getEndDate().getMonth() ){
                Log.d("BudgetItem Adapter", "Transaction: " + transaction.toString());
                spentAmount += transaction.getValue();
            }
        }
        return spentAmount;
    }

    public ArrayList<Category> getCategoryArrayList() {
        return categoryArrayList;
    }

    public void setCategoryArrayList(ArrayList<Category> categoryArrayList) {
        this.categoryArrayList = categoryArrayList;
    }

    public ArrayList<BudgetItem> getBudgetItemArrayList() {
        return budgetItemArrayList;
    }

    public void setBudgetItemArrayList(ArrayList<BudgetItem> budgetItemArrayList) {
        this.budgetItemArrayList = budgetItemArrayList;
    }

    public ArrayList<Transaction> getTransactionArrayList() {
        return transactionArrayList;
    }

    public void setTransactionArrayList(ArrayList<Transaction> transactionArrayList) {
        this.transactionArrayList = transactionArrayList;
    }

    public List<String> getParentCategoriesList() {
        return parentCategoriesList;
    }

    public void setParentCategoriesList(List<String> parentCategoriesList) {
        this.parentCategoriesList = parentCategoriesList;
    }

    public HashMap<String, ArrayList<BudgetItem>> getBudgetItemHashMap() {
        return budgetItemHashMap;
    }

    public void setBudgetItemHashMap(HashMap<String, ArrayList<BudgetItem>> budgetItemHashMap) {
        this.budgetItemHashMap = budgetItemHashMap;
    }

    public Budget getBudget() {
        return budget;
    }

    public void setBudget(Budget budget) {
        this.budget = budget;
    }

    @NonNull
    @Override
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
