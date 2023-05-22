package com.example.projectgabi.Utils;

public class Constants {

    // ploscu
   // private static final String ROOT_URL = "http://192.168.1.102/android/v1/";
    // camin
    // private static final String ROOT_URL = "http://192.168.0.149/android/v1/";
    //biblioteca
  //  private  static final String ROOT_URL = "http://10.30.24.134/android/v1/";
    //hotspot
  private  static final String ROOT_URL = "http://192.168.81.217/android/v1/";
    public static final String CREATE_TRANSACTION_URL = ROOT_URL + "createTransaction.php";
    public static final String READ_TRANSACTION_URL = ROOT_URL + "getTransactions.php";
    public static final String CREATE_USER_URL = ROOT_URL + "createUser.php";


    // keys for transactions
    // public static final String KEY_TRANSACTION_ID = "id";
    public static final String KEY_TRANSACTION_TYPE = "type";
    public static final String KEY_TRANSACTION_CATEGORY = "category";
    public static final String KEY_TRANSACTION_VALUE = "value";
    public static final String KEY_TRANSACTION_DATE = "date";
    public static final String KEY_TRANSACTION_DESCRIPTION = "description";
    public static final String KEY_TRANSACTIONS_ARRAY_KEY = "transactions";
    public static final String KEY_TRANSACTION_PARENT_CATEGORY = "parentCategory";


}

