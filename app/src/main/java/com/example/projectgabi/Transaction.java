package com.example.projectgabi;


import java.io.Serializable;
import java.util.Date;

public class Transaction implements Serializable {

    private String id;
    private TransactionType type;
    private String category;
    private double value;
    private Date date;
    private String description ;

    public Transaction(String id, TransactionType type, String category, double value, Date date, String description) {
        this.id = id;
        this.type = type;
        this.category = category;
        this.value = value;
        this.date = date;
        this.description = description;
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

 // make a toString method using String format
    @Override
    public String toString() {
        return String.format("Transaction{id=%s, type=%s, category=%s, value=%f, date=%s, description=%s}", id, type, category, value, date, description);
    }
}
