package com.example.projectgabi.Interfaces;

import com.example.projectgabi.Transaction;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface TransactionCallback {
   void onTransactionReceived(HashMap<String, List<Transaction>> transactionHashMap,
                              HashMap<String, List<Transaction>> transactionMapByParentCategory , ArrayList<PieEntry> categories );

}
