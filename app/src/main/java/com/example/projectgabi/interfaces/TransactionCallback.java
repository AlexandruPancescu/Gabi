package com.example.projectgabi.interfaces;

import com.example.projectgabi.classes.Transaction;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface TransactionCallback {
   void onReceivedTransaction(HashMap<String, List<Transaction>> transactionHashMap,
                              HashMap<String, List<Transaction>> transactionMapByParentCategory , ArrayList<PieEntry> categories );

}
