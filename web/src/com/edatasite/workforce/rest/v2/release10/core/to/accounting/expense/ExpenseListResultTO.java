package com.edatasite.workforce.rest.v2.release10.core.to.accounting.expense;


import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.ArrayList;

/**
 * Created by Anvar Akramov on 12/7/2017.
 */

public class ExpenseListResultTO extends ResponseData {

    private ArrayList<ExpenseListItemTO> expenses;


    public ExpenseListResultTO() {
    }

    public ExpenseListResultTO(ArrayList<ExpenseListItemTO> expenses) {
        this.expenses = expenses;
    }

    public ArrayList<ExpenseListItemTO> getExpenses() {
        return expenses;
    }

    public void setExpenses(ArrayList<ExpenseListItemTO> expenses) {
        this.expenses = expenses;
    }
}
