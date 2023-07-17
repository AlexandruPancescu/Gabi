package com.example.projectgabi.classes;


import com.example.projectgabi.enums.TransactionType;

import java.io.Serializable;
import java.util.Date;

public class Transaction implements Serializable {

    private String id;
    private TransactionType type;
    private String category;
    private double value;
    private Date date;
    private String description ;
    private String parentCategory;
    private String fk_account;
    private String fk_user;



    public Transaction(String id, TransactionType type, String category, double value, Date date, String description, String parentCategory) {
        this.id = id;
        this.type = type;
        this.category = category;
        this.value = value;
        this.date = date;
        this.description = description;
        this.parentCategory = parentCategory;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(String parentCategory) {
        this.parentCategory = parentCategory;
    }

    // make a toString method using String format


    public String getFk_account() {
        return fk_account;
    }

    public void setFk_account(String fk_account) {
        this.fk_account = fk_account;
    }

    public String getFk_user() {
        return fk_user;
    }

    public void setFk_user(String fk_user) {
        this.fk_user = fk_user;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + id + '\'' +
                ", type=" + type +
                ", category='" + category + '\'' +
                ", value=" + value +
                ", date=" + date +
                ", description='" + description + '\'' +
                ", parentCategory='" + parentCategory + '\'' +
                '}';
    }
}
