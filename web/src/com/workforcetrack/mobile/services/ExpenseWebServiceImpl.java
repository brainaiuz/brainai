package com.workforcetrack.mobile.services;

import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.core.client.Exceptions.UnrealValueException;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.CurrencyService;
import com.edatasite.workforce.gwt.core.client.rpc.EmailTemplateItem;
import com.edatasite.workforce.gwt.core.client.rpc.EmailTemplateService;
import com.edatasite.workforce.gwt.core.client.rpc.EntityToEmailTemplate;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseListItem;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseReportViewParameters;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseReportsListItem;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseService;
import com.edatasite.workforce.gwt.expenses.client.rpc.ReportData;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.workforcetrack.mobile.rpc.accounting.MTaxList;
import com.workforcetrack.mobile.rpc.base.WebServiceUtils;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 6/22/11
 * Time: 2:58 PM
 * To change this template use File | Settings | File Templates.
 */
@Service("expenseWebService")
public class ExpenseWebServiceImpl implements ExpenseWebService, Constants {

    @Autowired
    private ExpenseService expenseService;
    @Autowired
    private EmailTemplateService emailTemplateService;
    @Autowired
    private AllInOneService allInOneService;
    @Autowired
    private AccountingService accountingService;
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private CurrencyService currencyService;


    public void setExpenseService(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    public void setEmailTemplateService(EmailTemplateService emailTemplateService) {
        this.emailTemplateService = emailTemplateService;
    }

    @Override
    public MCurrencyItem getBaseCurrency() {
        CurrencyItem currencyItem = expenseService.getBaseCurrency();

        return new MCurrencyItem(currencyItem);
    }

    @Override
    public MApproverList getApprovers() {
        return getApprovers(null, null);
    }

    @Override
    public MApproverList getApprovers(Integer projectID, String key) {
        SelectItem[] approverList = null;
        SelectItem[] approver2List = null;
        approverList = getforLookupApprovers(key, true);

        return new MApproverList(approverList, approver2List);
    }

    private SelectItem[] getforLookupApprovers(String key, Boolean firstApprover) {
        ListingFilterParameter parameter = new ListingFilterParameter();
        parameter.setNewType(firstApprover);
        if (key.equals("quote")) {
            parameter.setInvoiceType(SALE_QUOTE);
        } else {
            parameter.setInvoiceType(EXPENSE_REPORT);
        }
        parameter.setLookUp(true);
        parameter.setLimit(17);
        parameter.setResignedEmployeesIncluded(true);
        return expenseService.getApproversForLookUp(parameter);
    }

    @Override
    public MCurrencyList getCurrencies() {

        SelectItem[] currencyList = currencyService.getCurrencies(true);

        return new MCurrencyList(currencyList);
    }

    @Override
    public MExpenseList getExpenses() {
        return getExpenses(null);
    }

    @Override
    public MExpenseList getExpenses(Integer reportID) {
        ExpenseListItem[] expenseListItems = expenseService.getExpenses(reportID);

        return new MExpenseList(expenseListItems);
    }

    @Override
    public Double getExchRate(String to) {
        String from = getBaseCurrency().getName();
        return expenseService.getExchRateForExpenseReport(to, from);
    }

    @Override
    public Boolean sendEmail(Integer reportID, String message) {
        if (reportID == null || message == null) {
            return null;
        }
        try {
            message = new String(message.getBytes("UTF8"), "UTF8");
            expenseService.sendEmail(reportID, message, null);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Boolean sendEmail(Integer reportID, Integer emailTemplateID, String message) {

        if (reportID == null || message == null) {
            return null;
        }
        try {
            message = new String(message.getBytes("UTF8"), "UTF8");
            expenseService.sendEmail(reportID, message, emailTemplateID);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    @Override
    public MReportData getReportData() {
        return getReportData(null);
    }

    @Override
    public MReportData getReportData(Integer reportID) {
        ExpenseReportViewParameters viewParameters = new ExpenseReportViewParameters();
        viewParameters.setObjectID(reportID);
        ReportData reportData = expenseService.getReportData(viewParameters);
        MReportData mReportData = new MReportData(reportData);
        mReportData.setProjects(getRelatedProjects().getRelatedProjectList());
        Integer projectID = (reportData.getReport() != null && reportData.getReport().getProject() != null) ? reportData.getReport().getProject().getId() : null;
        mReportData.setApprovers(WebServiceUtils.getAsMSelectItemList(expenseService.getApprover(projectID, true)));

        return mReportData;
    }

    @Override
    public MRelatedProjectList getRelatedProjects() {
        SelectItem[] relatedProjectList = expenseService.getRelatedProjects();
        MRelatedProjectList mRelatedProjectList = new MRelatedProjectList();
        mRelatedProjectList.setRelatedProjectList(WebServiceUtils.getAsMSelectItemList(relatedProjectList));

        return mRelatedProjectList;
    }

    @Override
    public MRelatedProjectList getRelatedProjectList(MFilterParametrs mFilterParametrs) {
        //To change body of implemented methods use File | Settings | File Templates.
        Integer clientId = mFilterParametrs.getProjectID() != null ? mFilterParametrs.getProjectID() : 0;
        String type = mFilterParametrs.getSearchKey() != null ? mFilterParametrs.getSearchKey() : "";
        mFilterParametrs.setSearchKey("");

        ListingFilterParameter filterParametrs = mFilterParametrs.convertToFilterParametrs();
        if (type.isEmpty()){
            filterParametrs.setInvoiceType(Constants.EXPENSE_REPORT);
        } else {
            filterParametrs.setClientId(clientId);
            filterParametrs.setLookUp(true);
        }
        SelectItem[] relatedProjects = allInOneService.getAccountingRelatedProjects(filterParametrs);
        MRelatedProjectList mRelatedProjectList = new MRelatedProjectList();
        mRelatedProjectList.setRelatedProjectList(WebServiceUtils.getAsMSelectItemList(relatedProjects));
        return mRelatedProjectList;
    }

    @Override
    public MEmailTemplateList getEmailTemplates(String template) {

        SelectItem[] temlates = expenseService.getEmailTemplates(template);

        return new MEmailTemplateList(temlates);
    }

    @Override
    public MEmailTemplateItem generateExpenseClaimTemplateItem(MEntityToEmailTemplate mEntityToEmailTemplate) {
        EntityToEmailTemplate entityToEmailTemplate = mEntityToEmailTemplate.convertToEntityToEmailTemplate(null);
        EmailTemplateItem emailTemplateItem = emailTemplateService.generateExpenseClaimTemplateItem(entityToEmailTemplate);
        return new MEmailTemplateItem(emailTemplateItem);
    }


    public MEmailTemplateList getEmailTemplates() {
        SelectItem[] temlates = expenseService.getEmailTemplates(EXPENSE_CLAIM_CATEGORY_SUBMIT);

        return new MEmailTemplateList(temlates);
    }

    @Override
    public String changeStatus(Integer objectID, String statusCode) {
        if (WebServiceUtils.isEmptyOrNull(objectID, statusCode)) {
            return null;
        }
        String result = null;
        try {
            if (statusCode.equalsIgnoreCase("void")) {
                accountingService.voidExpenseTransaction(objectID, null);
                result = "VOID";
            } else {
                result = expenseService.changeExpenseStatus(objectID, statusCode, null, null, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = "false";
        }
        return result;
    }

    @Override
    public MTaxList getTaxList() {
        return new MTaxList(invoiceService.getCompanyTaxList());
    }


    //CRUD METHODs

    @Override
    public MExpenseReportsList getList(MFilterParametrs mFilterParametrs) {
        if (mFilterParametrs == null)
            return null;

        ListingFilterParameter fp = mFilterParametrs.convertToFilterParametrs();
        FacetFilterRpc facetFilter = new FacetFilterRpc();
        facetFilter.setType(ListPanelType.ExpenceReportListPanel);
        fp.setFacetFilter(facetFilter);
        ListResult<ExpenseReportsListItem> expenseReportsList = expenseService.getExpenseReportsDataFromSolr(fp);
        return new MExpenseReportsList(expenseReportsList);
    }

    @Override
    public MExpenseReportsListItem get(Integer reportID) {
        if (reportID == null || reportID == 0)
            return null;

        ExpenseReportsListItem report = expenseService.getReport(reportID);

        return new MExpenseReportsListItem(report);

    }

    @Override
    public MExpenseReportsListItem edit(Integer reportID) {
        return get(reportID);
    }

    @Override
    public Integer save(MExpenseReportsListItem mExpenseReportsListItem) {
        if (mExpenseReportsListItem == null)
            return null;

        Integer result = 0;
        try {
            ExpenseReportsListItem expenseReportsListItem = null;
            if (mExpenseReportsListItem.getObjectID() != null && mExpenseReportsListItem.getObjectID() != 0) {
                expenseReportsListItem = expenseService.getReport(mExpenseReportsListItem.getObjectID());
            }
            expenseReportsListItem = mExpenseReportsListItem.convertToExpenseReportsListItem(expenseReportsListItem);
            expenseReportsListItem.setFromOldMobile(true);
            Integer expenseReportID = expenseService.saveReport(expenseReportsListItem);

            if (expenseReportID == -1) {
                throw new UnrealValueException("Expense Claim with this number already exists.");
            }

            return expenseReportID;
        } catch (Exception e) {
            e.printStackTrace();
            return result;
        }

    }

    @Override
    public Boolean delete(Integer reportID) {
        if (reportID == null || reportID == 0)
            return null;
        try {
            expenseService.deleteExpenseReport(reportID);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
}
