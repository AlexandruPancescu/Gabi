package com.example.projectgabi.utils;

public class Constants {

    // ploscu
     private static final String ROOT_URL = "http://192.168.1.100/android/v1/";
    // camin
  //  private static final String ROOT_URL = "http://192.168.0.150/android/v1/";
    //biblioteca
    // private  static final String ROOT_URL = "http://10.30.24.134/android/v1/";
    //hotspot
    // private  static final String ROOT_URL = "http://192.168.103.217/android/v1/";
    public static final String GET_BANK_ACCOUNTS_URL = ROOT_URL + "getBankAccounts.php";
    public static final String CREATE_TRANSACTION_URL = ROOT_URL + "createTransaction.php";
    public static final String GET_TRANSACTION_URL = ROOT_URL + "getTransactions.php";
    public static final String CREATE_USER_URL = ROOT_URL + "createUser.php";
    public static final String DELETE_TRANSACTION_URL = ROOT_URL + "deleteTransaction.php";
    public static final String CREATE_BANK_ACCOUNT_URL = ROOT_URL + "createBankAccount.php";



    // keys for transactions
    public static final String KEY_TRANSACTION_TYPE = "type";
    public static final String KEY_TRANSACTION_CATEGORY = "category";
    public static final String KEY_TRANSACTION_VALUE = "value";
    public static final String KEY_TRANSACTION_DATE = "date";
    public static final String KEY_TRANSACTION_DESCRIPTION = "description";
    public static final String KEY_TRANSACTIONS_ARRAY_KEY = "transactions";
    public static final String KEY_TRANSACTION_PARENT_CATEGORY = "parentCategory";
    public static final String KEY_TRANSACTION_ID = "id";


    // keys for accounts bd operations
    public  static final String KEY_ACCOUNT_ARRAY_KEY = "accounts";
    public static  final String KEY_ACCOUNT_ID = "id";
    public static  final String KEY_ACCOUNT_NAME = "bankName";
    public static  final String KEY_ACCOUNT_BALANCE = "balance";
    public static  final String KEY_ACCOUNT_CURRENCY = "currency";
    public static  final String KEY_ACCOUNT_ADDITIONAL_INFO = "additionalInfo";



}

