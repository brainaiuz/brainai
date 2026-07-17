package com.workforcetrack.mobile.rpc.expense;

import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseListItem;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 6/22/11
 * Time: 3:59 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement(name = "expenseList")
public class MExpenseList {

    private List<MExpenseListItem> expenseListItem;

    public MExpenseList(){

    }

    public MExpenseList(ExpenseListItem[] expenseListItems){
        if (expenseListItems != null) {
            this.expenseListItem = new ArrayList<>();
            for (ExpenseListItem expenseListItem : expenseListItems) {
                this.expenseListItem.add(new MExpenseListItem(expenseListItem));
            }
        }

    }

    public List<MExpenseListItem> getExpenseListItem() {
        return expenseListItem;
    }

    public void setExpenseListItem(List<MExpenseListItem> expenseListItem) {
        this.expenseListItem = expenseListItem;
    }
}
