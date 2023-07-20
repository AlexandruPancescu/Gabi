package com.example.projectgabi.utils;

public class Constants {


    // ploscu2
    //  private static final String ROOT_URL = "http://192.168.1.81/android/v1/";

    // ploscu
    // private static final String ROOT_URL = "http://192.168.1.100/android/v1/";
    // camin
      // private static final String ROOT_URL = "http://192.168.1.101/android/v1/";
    //  biblioteca2
    // private  static final String ROOT_URL = "http://10.30.24.134/android/v1/";
    //hotspot
      private  static final String ROOT_URL = "http://192.168.68.217/android/v1/";
    public static final String GET_BANK_ACCOUNTS_URL = ROOT_URL + "getBankAccounts.php";
    public static final String GET_BANK_ACCOUNT_URL = ROOT_URL + "getAccount.php";
    public static final String CREATE_TRANSACTION_URL = ROOT_URL + "createTransaction.php";
    public static final String POST_TRANSACTION_URL = ROOT_URL + "postTransaction.php";
    public static final String GET_TRANSACTION_URL = ROOT_URL + "getTransactions.php";
    public static final String CREATE_USER_URL = ROOT_URL + "createUser.php";
    public static final String DELETE_TRANSACTION_URL = ROOT_URL + "deleteTransaction.php";
    public static final String CREATE_BANK_ACCOUNT_URL = ROOT_URL + "createBankAccount.php";
    public static final String CREATE_BUDGET_URL = ROOT_URL + "createBudget.php";
    public static final String CREATE_BUDGET_ITEM_URL = ROOT_URL + "createBudgetItem.php";
    public static final String CREATE_CATEGORY_URL = ROOT_URL + "createCategory.php";
    public static final String GET_CATEGORIES_URL = ROOT_URL + "getCategories.php";
    public static final String GET_BUDGET_ITEMS_URL = ROOT_URL + "getBudgetItems.php";
    public static final String GET_BUDGET_BY_INTERVAL_URL = ROOT_URL + "getBudgetByDate.php";
    public static final String GET_BUDGET_BUNDLE_URL = ROOT_URL + "getBudgetBundle.php";
    public static final String GET_BUDGETS_DATES_URL = ROOT_URL + "getBudgetDates.php";
    public static final String GET_USER = ROOT_URL + "getUser.php";
    public static final  String UPDATE_BANK_ACCOUNT_URL = ROOT_URL + "updateBankAccount.php";
    public static final String GET_TRANSACTIONS_BY_USER_ID = ROOT_URL + "getTransactionsByUserId.php";



    // keys for transactions
    public static final String KEY_TRANSACTION_TYPE = "type";
    public static final String KEY_TRANSACTION_CATEGORY = "category";
    public static final String KEY_TRANSACTION_VALUE = "value";
    public static final String KEY_TRANSACTION_DATE = "date";
    public static final String KEY_TRANSACTION_DESCRIPTION = "description";
    public static final String KEY_TRANSACTIONS_ARRAY_KEY = "transactions";
    public static final String KEY_TRANSACTION_PARENT_CATEGORY = "parentCategory";
    public static final String KEY_TRANSACTION_ID = "PK_TransactionID";


    // keys for accounts bd operations
    public static final String KEY_ACCOUNT_ARRAY_KEY = "accounts";
    public static final String KEY_ACCOUNT_ID = "id";
    public static final String KEY_ACCOUNT_NAME = "bankName";
    public static final String KEY_ACCOUNT_BALANCE = "balance";
    public static final String KEY_ACCOUNT_CURRENCY = "currency";
    public static final String KEY_ACCOUNT_ADDITIONAL_INFO = "additionalInfo";
    public static final String KEY_ACCOUNT_FK_USER = "FK_UserID";

    // keys for budget bd operations

    public static final String KEY_BUDGET_ARRAY_KEY = "budgets";
    public static final String KEY_BUDGET_BY_DATE_KEY = "budget";

    public static final String KEY_BUDGET_ID = "PK_BudgetID";
    public static final String KEY_BUDGET_START_DATE = "startDate";
    public static final String KEY_BUDGET_END_DATE = "endDate";
    public static final String KEY_BUDGET_USER_ID = "FK_UserID";

    // keys for budget item operations
    public static final String KEY_BUDGET_ITEM_AMOUNT = "BudgetAmount";
    public static final String KEY_BUDGETS = "budgetItems";


    // keys for category bd operations
    public static final String KEY_CATEGORY_ARRAY_KEY = "categories";
    public static final String KEY_CATEGORY_ID = "PK_CategoryID";
    public static final String KEY_CATEGORY_NAME = "Name";
    public static final String KEY_CATEGORY_AMOUNT = "Amount";
    public static final String KEY_CATEGORY_PARENT = "ParentCategory";

    // keys for Budget bundle operations

    public static final String KEY_BUDGET_BUNDLE_ARRAY_KEY = "bundle";


}

