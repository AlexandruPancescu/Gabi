package com.example.projectgabi.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.projectgabi.R;
import com.example.projectgabi.Transaction;
import com.github.mikephil.charting.charts.PieChart;

import java.util.List;

public class TransactionAdapter extends ArrayAdapter<Transaction> {

    private int resource;
    private List<Transaction> objects;
    private LayoutInflater inflater;


    public TransactionAdapter(@NonNull Context context, int resource,  @NonNull List<Transaction> objects, LayoutInflater inflater) {
        super(context, resource,  objects );
        this.resource = resource;
        this.objects = objects;
        this.inflater = inflater;

    }


    @Override
    public View  getView(int position,  @NonNull  View convertView, @NonNull ViewGroup parent) {
        View row = inflater.inflate(this.resource, parent, false );
        PieChart pieChart = row.findViewById(R.id.mainPieChart);
        TextView tvDate = row.findViewById(R.id.testRecycleView);
        Transaction transaction = objects.get(position);

        tvDate.setText(transaction.getDate().toString());

        return row;
    }

}
