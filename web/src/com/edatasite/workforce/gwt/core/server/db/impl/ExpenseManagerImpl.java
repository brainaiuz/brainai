package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsExpense;
import com.edatasite.workforce.core.domain.EdsExpenseReport;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.ExpenseManager;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseReportsListItem;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 21.10.2008
 * Time: 10:26:03
 * To change this template use File | Settings | File Templates.
 */
@Repository("expenseManager")
public class ExpenseManagerImpl extends AttachmentSupportManager<EdsExpense> implements ExpenseManager {

    public static final String EXPENSE_APPROVED = "EXPENSE_APPROVED";
    public static final String EXPENSE_PAID = "EXPENSE_PAID";

    public ExpenseManagerImpl() {
        super(EdsExpense.class);
    }

    public List<EdsEmployee> getEmployeesWithExpenses() {
        return find("select distinct p.employee from EdsExpense p");
    }

    public List<EdsExpense> getExpenses(Integer employeeID) {
        if (employeeID != null) {
            return find("FROM EdsExpense WHERE employee.objectID =?", employeeID);
        } else {
            return find("FROM EdsExpense");
        }
    }

    public List<EdsExpense> getExpenseByReport(Integer reportID) {
        return find("select e from EdsExpense e where e.report.objectID =? and e.isDeleted = false order by e.objectID", reportID);
    }

    public EdsExpense getExpense(Integer objectID) {
        Map<String, Object> map = new HashMap<>();
        map.put("objectID", objectID);
        return (EdsExpense) findSingleByNamedParams("select e from EdsExpense e where e.objectID =:objectID", map);
    }

    @Override
    public List<EdsExpense> getBillableExpenses(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("select e from EdsExpense e ");
        sql.append(" join e.report r ");
        sql.append(" left join e.report.currentApprover cappr ");
        sql.append(" left join cappr.exactEmployee a2 ");
        sql.append(" left join e.report.overallStatus s2 ");
        sql.append(" where (e.client.objectID = ? and e.invoice.objectID  is null) and ").append(ServerUtils.checkForDeleted("e.isDeleted")).append(" and ").append(ServerUtils.checkForDeleted("r.isDeleted"));
        sql.append(" and cappr.exactEmployee = a2.id and a2 is not null and (s2.code = ? or s2.code = ?) ");

        if (fp.getStartDate() != null && fp.getEndDate() != null) {
            sql.append(" and (e.report.startDate between ? and ?) ");
            return find(sql.toString(), fp.getInvoiceClientId(), EXPENSE_APPROVED, EXPENSE_PAID, fp.getStartDate(), fp.getEndDate());
        } else {
            return find(sql.toString(), fp.getInvoiceClientId(), EXPENSE_APPROVED, EXPENSE_PAID);
        }
    }

    @Override
    public List<EdsExpense> getExpenseByInvoice(Integer invoiceID) {
        return find("select e from EdsExpense e where e.invoice.objectID = ?", invoiceID);
    }

    @Override
    public EdsExpenseReport getOldExpense(ExpenseReportsListItem expenseReportsListItem) {
        Map<String, Object> map = new HashMap<>();
        map.put("title", expenseReportsListItem.getTitle());
        map.put("startDate", expenseReportsListItem.getStartDate() != null ? expenseReportsListItem.getStartDate().getDate() : "");
        map.put("reporterId", expenseReportsListItem.getEmployeeId());
        Object ob = findSingleByNamedParams("select e from EdsExpenseReport e where e.title=:title and e.startDate=:startDate and e.reporter.objectID=:reporterId and e.isDeleted<>true", map);
        return ob != null ? (EdsExpenseReport) ob : null;
    }

    @Override
    public Integer[] getRelatedTimesheetsByExpense(Integer expenseId) {
        List<Integer> result = find("select distinct ts.objectID from EdsTimeSheet ts where ts.expenseID = ?", expenseId);
        return result != null ? result.toArray(new Integer[]{}) : null;
    }

    @Override
    public void removeRelatedTimesheetsFromExpense(Integer expenseId) {
        update("update EdsTimeSheet set usedInExpense = false, expenseID = null where expenseID = ?", expenseId);
    }
}
