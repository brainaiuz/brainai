package com.workforcetrack.mobile.services;

import com.workforcetrack.mobile.rpc.accounting.MTaxList;
import com.workforcetrack.mobile.rpc.client.MFilterParametrs;
import com.workforcetrack.mobile.rpc.expense.MApproverList;
import com.workforcetrack.mobile.rpc.expense.MCurrencyItem;
import com.workforcetrack.mobile.rpc.expense.MCurrencyList;
import com.workforcetrack.mobile.rpc.expense.MEmailTemplateItem;
import com.workforcetrack.mobile.rpc.expense.MEmailTemplateList;
import com.workforcetrack.mobile.rpc.expense.MEntityToEmailTemplate;
import com.workforcetrack.mobile.rpc.expense.MExpenseList;
import com.workforcetrack.mobile.rpc.expense.MExpenseReportsList;
import com.workforcetrack.mobile.rpc.expense.MExpenseReportsListItem;
import com.workforcetrack.mobile.rpc.expense.MRelatedProjectList;
import com.workforcetrack.mobile.rpc.expense.MReportData;

/**
 * Created by IntelliJ IDEA.
 * User: HAveANiceDay
 * Date: 17.06.11
 * Time: 16:38
 * To change this template use File | Settings | File Templates.
 */
public interface ExpenseWebService {

    MCurrencyItem getBaseCurrency();

    MApproverList getApprovers();

    MApproverList getApprovers(Integer projectID, String key);

    MCurrencyList getCurrencies();

    MExpenseList getExpenses();

    MExpenseList getExpenses(Integer reportID);

    Double getExchRate(String to);

    Boolean sendEmail(Integer reportID, String message);

    Boolean sendEmail(Integer reportID, Integer emailTemplateID, String message);

    MReportData getReportData();

    MReportData getReportData(Integer reportID);

    MRelatedProjectList getRelatedProjects();

    MRelatedProjectList getRelatedProjectList(MFilterParametrs mFilterParametrs);

    MEmailTemplateList getEmailTemplates();

    MEmailTemplateList getEmailTemplates(String template);

    MEmailTemplateItem generateExpenseClaimTemplateItem(MEntityToEmailTemplate mEntityToEmailTemplate);

    String changeStatus(Integer objectID, String statusCode);

    MTaxList getTaxList();

    //CRUD methods

    MExpenseReportsList getList(MFilterParametrs mFilterParametrs);

    MExpenseReportsListItem get(Integer reportID);

    MExpenseReportsListItem edit(Integer reportID);

    Integer save(MExpenseReportsListItem mExpenseReportsListItem);

    Boolean delete(Integer reportID);

}
