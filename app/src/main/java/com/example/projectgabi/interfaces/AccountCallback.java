package com.example.projectgabi.interfaces;

import com.example.projectgabi.classes.Account;

import java.util.ArrayList;

public interface AccountCallback {

    public void onReceivedAccount(ArrayList<Account> accounts);

}
