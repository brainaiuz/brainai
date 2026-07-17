package com.workforcetrack.mobile.rpc.expense;

import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseReportsList;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseReportsListItem;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 6/22/11
 * Time: 4:15 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement(name = "expenseReportsList")
public class MExpenseReportsList {

    private List<MExpenseReportsListItem> expenseReportsListItem;
    private Integer totalCount;

    public MExpenseReportsList() {

    }

    public MExpenseReportsList(ExpenseReportsList expenseReportsList) {
        if (expenseReportsList != null) {
            this.totalCount = expenseReportsList.getTotalCount();
            this.expenseReportsListItem = new ArrayList<>();
            for (ExpenseReportsListItem expenseReportsListItem : expenseReportsList.getResult()) {
                this.expenseReportsListItem.add(new MExpenseReportsListItem(expenseReportsListItem));
            }
        }
    }

    public MExpenseReportsList(ListResult<ExpenseReportsListItem> expenseReportsList) {
        if (expenseReportsList != null) {
            this.totalCount = expenseReportsList.getTotal();
            if (expenseReportsList.getList() != null && expenseReportsList.getList().size() > 0) {
                this.expenseReportsListItem = new ArrayList<>();
                for (ExpenseReportsListItem expenseReportsListItem : expenseReportsList.getList()) {
                    this.expenseReportsListItem.add(new MExpenseReportsListItem(expenseReportsListItem));
                }
            }

        }
    }


    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public List<MExpenseReportsListItem> getExpenseReportsListItem() {
        return expenseReportsListItem;
    }

    public void setExpenseReportsListItem(List<MExpenseReportsListItem> expenseReportsListItem) {
        this.expenseReportsListItem = expenseReportsListItem;
    }
}
