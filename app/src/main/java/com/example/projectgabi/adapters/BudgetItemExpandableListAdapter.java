package com.example.projectgabi.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.projectgabi.R;
import com.example.projectgabi.classes.Budget;
import com.example.projectgabi.classes.BudgetItem;
import com.example.projectgabi.classes.Category;
import com.example.projectgabi.classes.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BudgetItemExpandableListAdapter extends BaseExpandableListAdapter {

    Context context;
    Budget budget;
    ArrayList<Category> categoryArrayList;
    ArrayList<BudgetItem> budgetItemArrayList;
    ArrayList<Transaction> transactionArrayList;
    List<String> parentCategoriesList;
    HashMap<String, ArrayList<BudgetItem>> budgetItemHashMap;


    public BudgetItemExpandableListAdapter(Context context) {
        categoryArrayList = new ArrayList<>();
        budgetItemArrayList = new ArrayList<>();
        transactionArrayList = new ArrayList<>();
        this.context = context;
    }

    public BudgetItemExpandableListAdapter(Context context, ArrayList<Category> categoryArrayList,
                                           ArrayList<BudgetItem> budgetItemArrayList, ArrayList<Transaction> transactions,
                                           Budget budget) {
        this.context = context;
        this.budget = budget;
        this.categoryArrayList = categoryArrayList;
        this.budgetItemArrayList = budgetItemArrayList;
        this.transactionArrayList = transactions;


        updateBudgetItemHashMap();
        this.parentCategoriesList = new ArrayList<>(this.budgetItemHashMap.keySet());
        Log.d("BudgetItem Adapter", "BudgetItemExpandableListAdapter: " + budgetItemHashMap.toString());
        Log.d("BudgetItem Adapter", "BudgetItemExpandableListAdapter: " + parentCategoriesList.toString());

    }


    @Override
    public int getGroupCount() {
        return budgetItemHashMap.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return budgetItemHashMap.get(parentCategoriesList.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return parentCategoriesList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return budgetItemHashMap.get(parentCategoriesList.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String name = this.getGroup(groupPosition).toString();
        Log.d("BudgetItem Adapter", "getGroupView: " + name);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.parent_category_group, null);
        }

        TextView headetTv = convertView.findViewById(R.id.parentCategoryGroupNameTv);
        headetTv.setText(name);
        headetTv.setTypeface(null, Typeface.BOLD);

        return convertView;
    }

    @SuppressLint("UseCompatLoadingForColorStateLists")
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        BudgetItem budgetItem = (BudgetItem) this.getChild(groupPosition, childPosition);
        Log.d("BudgetItem Adapter", "getChildView: " + budgetItem.toString());
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.view_butget_item, null);
        }

        TextView budgetItemCategory = convertView.findViewById(R.id.budgetItemCategoryName);
        TextView budgetedTv = convertView.findViewById(R.id.budgetItemAmountTv);
        TextView spentTv = convertView.findViewById(R.id.budgetItemSpentTv);
        ProgressBar progressBar = convertView.findViewById(R.id.budgetItemProgressBar);


        float spentAmount = this.getSpentAmount(budgetItem);
        double budgetItemAmount = budgetItem.getBudgetItemAmount();

        budgetItemCategory.setText(budgetItem.getCategory().getCategoryName());
        budgetedTv.setText(String.valueOf(budgetItemAmount));
        spentTv.setText(String.valueOf(spentAmount));

        Log.d("BudgetItem Adapter", "amounts: " + spentAmount + " " + budgetItemAmount + " " + budgetItem.getCategory().getCategoryAmount());
        Log.d("BudgetItem Adapter", "getChildView: " + spentAmount + " " + budgetItemAmount);


        if (spentAmount < budgetItemAmount) {
            if (spentAmount ==0 ){
                progressBar.setProgress(0);
                progressBar.setProgressTintList(context.getResources().getColorStateList(R.color.switch_light_grey));
            }else{

           Drawable progressDrawable =  progressBar.getProgressDrawable();
              progressDrawable.setColorFilter(context.getResources().getColor(R.color.progress_bar_green),
                      PorterDuff.Mode.MULTIPLY);

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
//        else {
//            progressBar.setProgress(0);
//            progressBar.setProgressTintList(context.getResources().getColorStateList(R.color.switch_light_grey));
//        }



        return convertView;
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

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }


    public void updateBudgetItemHashMap() {
        budgetItemHashMap = new HashMap<>();
        Log.d("BudgetItem Adapter", "updateBudgetItemHashMap: " + categoryArrayList.toString());
        Log.d("BudgetItem Adapter", "updateBudgetItemHashMap: " + budgetItemArrayList.toString());

        for (Category category : categoryArrayList) {
            ArrayList<BudgetItem> budgetItems = new ArrayList<>();
            for (BudgetItem budgetItem : budgetItemArrayList) {
                if (budgetItem.getCategory().getParentCategory().equals(category.getParentCategory())) {
                    budgetItems.add(budgetItem);
                }
            }
            Log.d("BudgetItem Adapter", "updateBudgetItemHashMap: " + budgetItems.toString());
            budgetItemHashMap.put(category.getParentCategory(), budgetItems);
        }
    }


    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Budget getBudget() {
        return budget;
    }

    public void setBudget(Budget budget) {
        this.budget = budget;
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
}
