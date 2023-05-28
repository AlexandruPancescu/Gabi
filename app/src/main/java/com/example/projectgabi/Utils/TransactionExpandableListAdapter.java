package com.example.projectgabi.Utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.projectgabi.Controllers.TransactionController;
import com.example.projectgabi.R;
import com.example.projectgabi.Transaction;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TransactionExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;

    private HashMap<String, List<Transaction>> transactionHashMap;
    private ArrayList<String> parentCategories; // keys

    public TransactionExpandableListAdapter(Context context, HashMap<String, List<Transaction>> transactionHashMap) {
        this.context = context;
        this.transactionHashMap = transactionHashMap;

    }

    public TransactionExpandableListAdapter(Context context, ArrayList<String> parentCategories, HashMap<String, List<Transaction>> transactionHashMap) {
        this.context = context;
        this.parentCategories = parentCategories;
        this.transactionHashMap = transactionHashMap;
        // Log.d("Adapter categories  ", parentCategories.toString());
    }

    public boolean update(boolean deleted) {
        return !deleted;
    }


    @Override
    public int getGroupCount() {
        return transactionHashMap.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return transactionHashMap.get(parentCategories.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return parentCategories.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return transactionHashMap.get(parentCategories.get(groupPosition)).get(childPosition);
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
        String name = this.getGroup(groupPosition).toString(); // get the header name

        if (convertView == null) {
            // inflate the view
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.transaction_group, null);
        }

        TextView headerTv = convertView.findViewById(R.id.transactionGroupNameTv);

        double value = this.calculateGroupTotal(name);
        String headerValue = String.format("%s     %.2f", name, value);
        headerTv.setText(headerValue);
        headerTv.setTypeface(null, Typeface.BOLD);
        return convertView;
    }

    private double calculateGroupTotal(String category) {
        return transactionHashMap.get(category).stream().mapToDouble(Transaction::getValue).sum();
    }


    public Object getChild(String categoryName) {
        return transactionHashMap.get(categoryName);
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Transaction childName = (Transaction) this.getChild(groupPosition, childPosition); // get the child name
Log.d("Adapter child", "getChildView: " + childName.getId() + " " + childName.getCategory() + " " + childName.getValue() + " " + childName.getParentCategory());

        if (convertView == null) {
            // inflate the view
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.transaction_list_element, null);
        }


        TextView childNameTv = convertView.findViewById(R.id.transactionElementNameTv);
        String childValue = String.format("%s   %.2f", childName.getCategory(), childName.getValue());
        childNameTv.setText(childValue);
        ImageView delete = convertView.findViewById(R.id.transactionDeleteIV);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Adapter click", "onClick: " + childName.getId());

                for (Transaction transaction : transactionHashMap.get(childName.getParentCategory())) {
                    if (transaction.getId().equals(childName.getId())) {

                        transactionHashMap.get(childName.getParentCategory()).remove(transaction);

                        TransactionController transactionController = new TransactionController();
                        transactionController.deleteTransaction(transaction.getId());
                        Log.d("Adapter" , "passed delete transaction");
                        break;
                    }
                }
                notifyDataSetChanged();
            }
        });
        return convertView;
    }


    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public HashMap<String, List<Transaction>> getTransactionHashMap() {
        return transactionHashMap;
    }

    public void setTransactionHashMap(HashMap<String, List<Transaction>> transactionHashMap) {
        this.transactionHashMap = transactionHashMap;
    }

    public ArrayList<String> getParentCategories() {
        return parentCategories;
    }

    public void setParentCategories(ArrayList<String> parentCategories) {
        this.parentCategories = parentCategories;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
