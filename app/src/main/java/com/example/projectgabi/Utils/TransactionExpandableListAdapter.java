package com.example.projectgabi.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.projectgabi.R;
import com.example.projectgabi.Transaction;

import java.util.HashMap;
import java.util.List;

public class TransactionExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;

    private HashMap<String, List<Transaction>> transactionHashMap;

    public TransactionExpandableListAdapter(Context context, HashMap<String, List<Transaction>> transactionHashMap) {
        this.context = context;
        this.transactionHashMap = transactionHashMap;

    }


    @Override
    public int getGroupCount() {
        return 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
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
        String name = String.valueOf(this.getGroup(groupPosition)); // get the header name
        if (convertView == null) {

            // inflate the view
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.transaction_group, null);
        }
        TextView headerTv = convertView.findViewById(R.id.transactionGroupNameTv);
        headerTv.setText(name);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String childName = String.valueOf(this.getChild(groupPosition, childPosition)); // get the child name
        if (convertView == null) {
            // inflate the view
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.transaction_list_element, null);
        }
        TextView childNameTv = convertView.findViewById(R.id.transactionElementNameTv);
        TextView childAmountTv = convertView.findViewById(R.id.transactionElementValueTv);
        childNameTv.setText(childName);
        childAmountTv.setText(childName);


        return convertView;

    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
