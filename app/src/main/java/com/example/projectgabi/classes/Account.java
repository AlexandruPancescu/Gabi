package com.example.projectgabi.classes;


//bank account
public class Account {
    private String accountID;
    private String bankName;
    private float balance;
    private String currency;
    private String additionalInfo;

    public Account() {
    }

    public Account(String accountID, String bankName, float balance, String currency, String additionalInfo) {
        this.accountID = accountID;
        this.bankName = bankName;
        this.balance = balance;
        this.currency = currency;
        this.additionalInfo = additionalInfo;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountID='" + accountID + '\'' +
                ", bankName='" + bankName + '\'' +
                ", balance=" + balance +
                ", currency='" + currency + '\'' +
                ", additionalInfo='" + additionalInfo + '\'' +
                '}';
    }
}
