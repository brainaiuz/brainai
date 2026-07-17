package com.edatasite.workforce.rest.v1.release10.accounting;

import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.server.app.AccountingServiceLocal;
import com.edatasite.workforce.gwt.accounting.server.app.FixedAssetServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.TaxItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.view.BankTransferNumberData;
import com.edatasite.workforce.gwt.core.server.app.ListUtils;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseReportsListItem;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseServiceLocal;
import com.edatasite.workforce.gwt.expenses.client.rpc.ReportData;
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceServiceLocal;
import com.edatasite.workforce.gwt.invoice.server.app.QuoteServiceLocal;
import com.edatasite.workforce.rest.base.enums.ApiActionEnum;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.helpers.MListingFilterParameter;
import com.edatasite.workforce.rest.base.helpers.WrapUtils;
import com.edatasite.workforce.rest.base.to.EmployeeTO;
import com.edatasite.workforce.rest.base.to.ExpenseClaimTO;
import com.edatasite.workforce.rest.base.to.ListResultTO;
import com.edatasite.workforce.rest.base.to.SelectItemTO;
import com.edatasite.workforce.rest.base.to.TaxTO;
import com.edatasite.workforce.rest.base.to.UserTO;
import com.edatasite.workforce.rest.v1.release10.core.BaseApiControllerV1;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dilshod Madrahimov on 6/24/15 12:07 PM
 */

@Tag(name = "Expense Claim", description = "Expense Claim API")
@RestController
@RequestMapping(value = "/expenseClaim", headers = {ApiConstants.SESSION_ID, ApiConstants.ACCESS_TOKEN},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiExpenseClaimControllerV1 extends BaseApiControllerV1 implements Constants {

    @Autowired
    private InvoiceServiceLocal invoiceServiceLocal;
    @Autowired
    private QuoteServiceLocal quoteServiceLocal;
    @Autowired
    private FixedAssetServiceLocal fixedAssetServiceLocal;
    @Autowired
    private ExpenseServiceLocal expenseServiceLocal;
    @Autowired
    private AccountingService accountingService;
    @Autowired
    private AccountingServiceLocal accountingServiceLocal;

    @Autowired
    private ReferenceManager referenceManager;

    @RequestMapping(value = "/list", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getList(@RequestBody MListingFilterParameter filterParameter) {
        if (filterParameter == null) {
            filterParameter = new MListingFilterParameter();
        }
        ListingFilterParameter fp = filterParameter.convertToFilterParameters();
        ListResult<ExpenseReportsListItem> expenseClaimList = expenseServiceLocal.getEmployeesReportList(fp);
        ArrayList<ExpenseClaimTO> result = new ArrayList<>();
        for (ExpenseReportsListItem item : expenseClaimList.getList()) {
            ExpenseClaimTO expense = new ExpenseClaimTO();
            expense.setId(item.getId());
            expense.setEmployee(new EmployeeTO(item.getReporterId(), item.getReporterName()));
            expense.setTitle(item.getTitle());
            if (item.getStartDate() != null) {
                expense.setDate(WrapUtils.dateToLong(item.getStartDate().getDate()));
            }
            expense.setTotal(item.getTotal());
            if (item.getExpenseCurrency() != null) {
                expense.setCurrency(new SelectItemTO(item.getExpenseCurrency().getId(), item.getExpenseCurrency().getName(), item.getExpenseCurrency().getSymbol(), ""));
            }
            expense.setStatus(new SelectItemTO(item.getStatus(), item.getStatusCode()));
            result.add(expense);
        }
        return successResponse(new ListResultTO<>(expenseClaimList.getTotal(), result));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Object get(@PathVariable(value = "id") Integer id) {
        if (id == null) {
            return errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }
        ReportData reportData = expenseServiceLocal.getReportSummaryData(id);
        return successResponse(new ExpenseClaimTO(reportData.getReport()));
    }

    @RequestMapping(value = "/listCount", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getListCount(@RequestBody MListingFilterParameter filterParameter) {
        if (filterParameter == null) {
            filterParameter = new MListingFilterParameter();
        }
        return successResponse(expenseServiceLocal.getEmployeesReportListCount(filterParameter.convertToFilterParameters()));
    }

    @RequestMapping(value = "/{status}", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object add(@PathVariable(value = "status") String status, @RequestBody ExpenseClaimTO expenseClaimTO) {
        if (expenseClaimTO == null) {
            return errorResponse(ERROR_INVALID_BODY_PARAM);
        }
        if (status == null) {
            return errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }
        status = getStatus(status);
        if (status == null) {
            return errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }
        expenseClaimTO.setStatus(new SelectItemTO(status, status));
        ExpenseReportsListItem item = expenseClaimTO.wrap(expenseClaimTO);

        if (expenseClaimTO.getApprovers() != null && expenseClaimTO.getApprovers().size() > 0 && expenseClaimTO.getApproverItems() != null && expenseClaimTO.getApproverItems().size() > 0) {
            item.setApprovers(getChosenApprovers(expenseClaimTO.getApprovers(), expenseClaimTO.getApproverItems()));
        }
        if (expenseClaimTO.getId() == null) {
            BankTransferNumberData numberData = expenseServiceLocal.generateExpenseReportNumber();
            item.setExpenseNumber(numberData.getTransferNumber());
            item.setIntNumber(Integer.valueOf(numberData.getFourDigitNumber()));
        }
        try {
            Integer result = expenseServiceLocal.saveReport(item);
            if (result == -1) {
                return errorResponse("Number exists");
            }
            return successResponse(SUCCESS_SAVE, result);
        } catch (Exception e) {
            e.printStackTrace();
            return errorResponse(ERROR_FAILED_SAVE);
        }
    }

    @RequestMapping(value = "/{id}/{status}", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public Object update(@PathVariable(value = "id") Integer id, @PathVariable(value = "status") String status, @RequestBody ExpenseClaimTO expenseClaimTO) {
        if (id == null || status == null) {
            return errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }
        if (expenseClaimTO == null) {
            return errorResponse(ERROR_INVALID_BODY_PARAM);
        }
        expenseClaimTO.setId(id);
        return add(status, expenseClaimTO);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Object delete(@PathVariable(value = "id") Integer id) {
        if (id == null)
            return errorResponse(ERROR_RESOURCE_NOT_FOUND);
        try {
            expenseServiceLocal.deleteExpenseReport(id);
            return successResponse(SUCCESS_DELETE);
        } catch (Exception e) {
            e.printStackTrace();
            return errorResponse(ERROR_FAIL_DELETE);
        }
    }

    @RequestMapping(value = "/employees", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getEmployeeList(@RequestBody MListingFilterParameter mListingFilterParameter) {
        if (mListingFilterParameter == null) {
            mListingFilterParameter = new MListingFilterParameter();
        }
        ListingFilterParameter fp = mListingFilterParameter.convertToFilterParameters();
        fp.setListEmployees(true);
        fp.setListDepartments(false);
        fp.setResignedEmployeesIncluded(true);
        return successResponse(WrapUtils.wrapUserTOs(allInOneServiceLocal.getEmployeesAsSelectItem(null, fp)));
    }

    @RequestMapping(value = "/{employeeId}/projects", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getProjectList(@PathVariable(value = "employeeId") Integer employeeID, @RequestBody MListingFilterParameter mListingFilterParameter) {
        if (employeeID == null) {
            return errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }
        if (mListingFilterParameter == null) {
            mListingFilterParameter = new MListingFilterParameter();
        }
        ListingFilterParameter fp = mListingFilterParameter.convertToFilterParameters();
        fp.setInvoiceType(EXPENSE_REPORT);
        fp.setEmployeeId(employeeID);
        return successResponse(WrapUtils.wrapSelectItemList(invoiceServiceLocal.getExpenseRelatedProjects(fp).getList()));
    }

    @RequestMapping(value = "/purchaseOrders", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getPurchaseOrderList(@RequestBody MListingFilterParameter mListingFilterParameter) {
        if (mListingFilterParameter == null) {
            mListingFilterParameter = new MListingFilterParameter();
        }
        return successResponse(WrapUtils.wrapSelectItemTOs(quoteServiceLocal.getPurchaseOrders(mListingFilterParameter.convertToFilterParameters())));
    }

    @RequestMapping(value = "/approvers", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getApproverList(@RequestBody MListingFilterParameter filterParameter) {
        if (filterParameter == null) {
            filterParameter = new MListingFilterParameter();
        }
        ListingFilterParameter fp = filterParameter.convertToFilterParameters();
        fp.setListEmployees(true);
        fp.setApproverID(fp.getObjectId());
        List<UserTO> result = WrapUtils.wrapUserTOs(allInOneServiceLocal.getEmployeesAsSelectItem(new ListLoadConfig(), fp), true);
        ListLoadConfig config = new ListLoadConfig(fp);
        if (result.size() > config.getStart()) {
            result = ListUtils.getSublist(result, config.getStart(), config.getLimit());
        } else {
            result = new ArrayList<>();
        }
        return successResponse(result);
    }

    @RequestMapping(value = "/approversCount", method = RequestMethod.GET)
    public Object getApproverListCount() {
        return successResponse(getChooseApprovers(RelationItem.TYPE_EXPENSE_CLAIM));
    }

    @RequestMapping(value = "/fixedAssets", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getFixedAssetList(@RequestBody MListingFilterParameter mListingFilterParameter) {
        if (mListingFilterParameter == null) {
            mListingFilterParameter = new MListingFilterParameter();
        }
        return successResponse(WrapUtils.wrapSelectItemTOs(fixedAssetServiceLocal.getFixedAssetsForLookUp(mListingFilterParameter.convertToFilterParameters())));
    }

    @RequestMapping(value = "/categories", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getCategoryList(@RequestBody MListingFilterParameter mListingFilterParameter) {
        if (mListingFilterParameter == null) {
            mListingFilterParameter = new MListingFilterParameter();
        }
        return successResponse(WrapUtils.wrapSelectItemTOs(accountingServiceLocal.getAccountsForExpense(mListingFilterParameter.convertToFilterParameters())));
    }

    @RequestMapping(value = "/paymentAccounts", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getPaymentAccountList(@RequestBody MListingFilterParameter mListingFilterParameter) {
        if (mListingFilterParameter == null) {
            mListingFilterParameter = new MListingFilterParameter();
        }
        ListingFilterParameter fp = mListingFilterParameter.convertToFilterParameters();
        fp.setLookUp(true);
        return successResponse(WrapUtils.wrapSelectItemTOs(allInOneServiceLocal.getAccountsForPayment(fp)));
    }

    @RequestMapping(value = "/taxes", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getTaxList(@RequestBody MListingFilterParameter mListingFilterParameter) {
        if (mListingFilterParameter == null) {
            mListingFilterParameter = new MListingFilterParameter();
        }
        ListingFilterParameter fp = mListingFilterParameter.convertToFilterParameters();
        fp.setLookUp(true);
        fp.setInvoiceType(PAYABLE);
        TaxItem[] taxItems = accountingServiceLocal.getCompanyTaxesWithFilter(fp);
        ArrayList<TaxTO> taxList = new ArrayList<>(taxItems.length);
        for (TaxItem taxItem : taxItems) {
            taxList.add(new TaxTO(taxItem.getId(), taxItem.getName(), taxItem.getTaxPercent(), taxItem.getEffectiveTaxPercent()));
        }
        return successResponse(taxList);
    }

    @RequestMapping(value = "/taxTypes", method = RequestMethod.GET)
    public Object getTaxTypes() {
        ArrayList<SelectItemTO> taxTypes = new ArrayList<>(3);
        taxTypes.add(new SelectItemTO(0, "No Tax"));
        taxTypes.add(new SelectItemTO(1, "Tax Inclusive"));
        taxTypes.add(new SelectItemTO(2, "Tax Exclusive"));
        return successResponse(taxTypes);
    }

    @RequestMapping(value = "/customers", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getCustomerList(@RequestBody MListingFilterParameter mListingFilterParameter) {
        if (mListingFilterParameter == null) {
            mListingFilterParameter = new MListingFilterParameter();
        }
        ListingFilterParameter fp = mListingFilterParameter.convertToFilterParameters();
        fp.setAccountType(CrmConstants.CUSTOMER);
        fp.setSearchByParent(true);
        fp.setWithCode(true);
        return successResponse(WrapUtils.wrapSelectItemList(allInOneServiceLocal.getCrmAccountAsSelectItem(CrmConstants.CRM_ACCOUNT_ID, fp).getList()));
    }

    @RequestMapping(value = "/suppliers", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getSupplierList(@RequestBody MListingFilterParameter mListingFilterParameter) {
        if (mListingFilterParameter == null) {
            mListingFilterParameter = new MListingFilterParameter();
        }
        ListingFilterParameter fp = mListingFilterParameter.convertToFilterParameters();
        fp.setAccountType(CrmConstants.SUPPLIER);
        fp.setSearchByParent(true);
        fp.setWithCode(true);
        return successResponse(WrapUtils.wrapSelectItemList(allInOneServiceLocal.getCrmAccountAsSelectItem(CrmConstants.CRM_ACCOUNT_ID, fp).getList()));
    }

    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public Object getStatus() {
        return successResponse(WrapUtils.wrapSelectItemTOs(referenceManager.getAsSelectItems(EXPENSE_STATUS)));
    }

    @RequestMapping(value = "/departments", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getDepartmentList(@RequestBody MListingFilterParameter mListingFilterParameter) {
        if (mListingFilterParameter == null) {
            mListingFilterParameter = new MListingFilterParameter();
        }
        ListingFilterParameter fp = mListingFilterParameter.convertToFilterParameters();
        fp.setLookUp(true);
        return successResponse(WrapUtils.wrapSelectItemTOs(accountingService.getDepartmentsForAccounting(fp)));
    }

    @RequestMapping(value = "/updateStatus/{id}/{status}", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object doAction(@PathVariable(value = "id") Integer id, @PathVariable(value = "status") String status) {
        if (id == null || status == null) {
            return errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }
        status = getStatus(status);
        try {
            expenseServiceLocal.changeExpenseStatus(id, status, null, null, null);
            return successResponse();
        } catch (Exception e) {
            e.printStackTrace();
            return errorResponse();
        }
    }

    private String getStatus(String status) {
        status = status.toUpperCase();
        if (ApiActionEnum.DRAFT.getCode().equals(status)) {
            return EXPENSE_DRAFT;
        }
        if (ApiActionEnum.SUBMIT.getCode().equals(status)) {
            return EXPENSE_SUBMITTED;
        }
        if (ApiActionEnum.APPROVE.getCode().equals(status)) {
            return EXPENSE_APPROVED;
        }
        if (ApiActionEnum.DECLINE.getCode().equals(status)) {
            return EXPENSE_DECLINED;
        }
        if (ApiActionEnum.APPLY.getCode().equals(status)) {
            return ApiActionEnum.APPLY.getCode();
        }
        return null;
    }

}
