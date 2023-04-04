package com.example.projectgabi;


import java.io.Serializable;
import java.util.Date;

public class Transaction implements Serializable {

    private int id;
    private TransactionType type;
    private String category;
    private double value;
    private Date data;
    private String description ;

    public Transaction(int id, TransactionType type, String category, double value, Date data, String description) {
        this.id = id;
        this.type = type;
        this.category = category;
        this.value = value;
        this.data = data;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
