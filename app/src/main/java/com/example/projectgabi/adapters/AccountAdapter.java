package com.example.projectgabi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectgabi.R;
import com.example.projectgabi.classes.Account;
import com.example.projectgabi.views.AccountActivity;

import java.util.ArrayList;
import java.util.List;

public class AccountAdapter extends ArrayAdapter<Account> {

    TextView accountNameTv, accountBalanceTv, accountCurrencyTv, accountAdditionalInfoTv; // accountCurrencyTv
    private int resource;
    private List<Account> objects;
    private LayoutInflater inflater;


    public AccountAdapter(@NonNull Context context, int resource, @NonNull List<Account> objects, LayoutInflater inflater) {
        super(context, resource, objects);
        this.resource = resource;
        this.inflater = inflater;
        this.objects = objects;

    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View row =inflater.inflate(this.resource, parent , false);
        accountNameTv =row.findViewById(R.id.accountBankNameTv);
        accountBalanceTv =row.findViewById(R.id.accountBalanceValueTv);
        accountCurrencyTv =row.findViewById(R.id.accountCurrencyTv);
        accountAdditionalInfoTv =row.findViewById(R.id.accountAdditionalInfoTv);

        Account account = objects.get(position);

        accountNameTv.setText(account.getBankName());
        accountBalanceTv.setText(String.valueOf(account.getBalance()));
        accountCurrencyTv.setText(account.getCurrency());
        accountAdditionalInfoTv.setText(account.getAdditionalInfo());

        return row;
    }
}


