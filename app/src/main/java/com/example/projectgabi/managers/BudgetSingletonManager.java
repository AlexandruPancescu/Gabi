package com.example.projectgabi.managers;

import com.example.projectgabi.classes.Budget;

public class BudgetSingletonManager {

    private static BudgetSingletonManager instance = null;
    private Budget budget;

    private BudgetSingletonManager(){

    }

    public synchronized static BudgetSingletonManager getInstance(){
        if(instance == null){
            instance = new BudgetSingletonManager();
        }
        return instance;
    }

    public void setBudget(Budget budget){
        this.budget = budget;
    }

    public Budget getBudget(){
        return this.budget;
    }

}
