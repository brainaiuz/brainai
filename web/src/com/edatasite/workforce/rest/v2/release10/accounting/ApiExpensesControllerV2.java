package com.edatasite.workforce.rest.v2.release10.accounting;

import com.edatasite.workforce.core.domain.EdsCompanyCustomFieldsSettings;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsExpense;
import com.edatasite.workforce.core.domain.EdsExpenseReport;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsUpload;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.accounting.EdsFixedAsset;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.server.app.AccountingServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.ApprovalListResult;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.CurrencyServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.RbacService;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TaxItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItemMini;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrExpenseReportRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.view.BankTransferNumberData;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.CompanyCustomFieldsManager;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.ExpenseManager;
import com.edatasite.workforce.gwt.core.server.db.ExpenseReportManager;
import com.edatasite.workforce.gwt.core.server.db.FinancialSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.FixedAssetManager;
import com.edatasite.workforce.gwt.core.server.zatca.ZatcaService;
import com.edatasite.workforce.gwt.core.server.zatca.service.errors.ZatcaException;
import com.edatasite.workforce.gwt.documents.client.exceptions.InsufficientPermissionsException;
import com.edatasite.workforce.gwt.documents.client.exceptions.ObjectNotFoundException;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.edatasite.workforce.gwt.documents.server.app.DocumentsServiceLocal;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseListItem;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseReportsListItem;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseServiceLocal;
import com.edatasite.workforce.gwt.hrms.server.app.HrmsServiceLocal;
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceServiceLocal;
import com.edatasite.workforce.rest.aspects.CheckPermission;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.helpers.ListingFilterHelper;
import com.edatasite.workforce.rest.v2.release10.core.BaseApiControllerV2;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.expense.ExpenseAddRequestTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.expense.ExpenseAddTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.expense.ExpenseDetailsTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.expense.ExpenseItemTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.expense.ExpenseListItemTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.expense.ExpenseListResultTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ApproverListStatusTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ApproverStatusTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.AttachmentTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.CategoryTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.CurrencyListTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.CurrencyValueTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.CustomStatusTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.FromValueTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.PagingItemsResultTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseItemsListData;
import com.edatasite.workforce.rest.v2.release10.core.to.base.StatusTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.TitleTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.tax.TaxTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.EmployeeListTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.EmployeeTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.RequestActionTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.leaverequest.RequestUserActionTO;
import com.edatasite.workforce.rest.v2.release10.core.to.payroll.ApproversTO;
import com.edatasite.workforce.rest.v2.release10.core.to.payroll.OwnerTO;
import com.edatasite.workforce.rest.v2.release10.core.to.pm.task.CustomFieldsTO;
import com.edatasite.workforce.rest.v2.release10.enums.CreateTypeEnum;
import com.edatasite.workforce.rest.v2.release10.enums.RequestActionEnum;
import com.edatasite.workforce.rest.v2.release10.enums.RequestStatusEnum;
import com.edatasite.workforce.rest.v2.release10.enums.TaxTypeEnum;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v2.release10.utils.ApiUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Anvar Akramov on 12/7/2017.
 */

@Tag(name = "Expenses", description = "Expenses API")
@RestController
@RequestMapping(headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiExpensesControllerV2 extends BaseApiControllerV2 implements Constants {

    private static final Logger log = LoggerFactory.getLogger(ApiExpensesControllerV2.class);
    private final HashMap<String, String> EXPENSE_STATUSES = new HashMap<>();
    @Autowired
    private ExpenseServiceLocal expenseServiceLocal;
    @Autowired
    private AccountingServiceLocal accountingServiceLocal;
    @Autowired
    private FixedAssetManager fixedAssetManager;
    @Autowired
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private RbacService rbacService;
    @Autowired
    private HttpServletRequest servletRequest;
    @Autowired
    private HrmsServiceLocal hrmsServiceLocal;
    @Autowired
    private DocumentsServiceLocal documentsServiceLocal;
    @Autowired
    private FinancialSettingsManager financialSettingsManager;
    @Autowired
    private CurrencyServiceLocal currencyServiceLocal;
    @Autowired
    private CrmAccountManager crmAccountManager;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private ExpenseReportManager expenseReportManager;
    @Autowired
    private CompanyCustomFieldsManager companyCustomFieldsManager;
    @Autowired
    private InvoiceServiceLocal invoiceServiceLocal;
    @Autowired
    private ExpenseManager expenseManager;
    @Autowired
    private ZatcaService zatcaService;

    public static ApproverListStatusTO getStatus(SelectItem status) {
        if (status == null || StringUtils.isBlank(status.getCode())) {
            return null;
        }
        return switch (status.getCode()) {
            case Constants.EXPENSE_DRAFT -> new ApproverListStatusTO(RequestStatusEnum.DRAFT.getStatus());
            case Constants.EXPENSE_APPROVED -> new ApproverListStatusTO(RequestStatusEnum.APPROVED.getStatus());
            case Constants.EXPENSE_SUBMITTED -> new ApproverListStatusTO(RequestStatusEnum.PENDING.getStatus());
            case Constants.EXPENSE_DECLINED -> new ApproverListStatusTO(RequestStatusEnum.DECLINED.getStatus());
            case Constants.EXPENSE_PAID -> new ApproverListStatusTO(RequestStatusEnum.PAID.getStatus());
            default -> new ApproverListStatusTO(RequestStatusEnum.CUSTOM.getStatus(), status.getName());
        };

    }

    private static Object getDefaultStatus(SelectItem status) {
        if (status == null || StringUtils.isBlank(status.getCode())) {
            return null;
        }
        return switch (status.getCode()) {
            case Constants.EXPENSE_DRAFT -> new StatusTO(RequestStatusEnum.DRAFT.getStatus());
            case Constants.EXPENSE_APPROVED -> new StatusTO(RequestStatusEnum.APPROVED.getStatus());
            case Constants.EXPENSE_SUBMITTED -> new StatusTO(RequestStatusEnum.PENDING.getStatus());
            case Constants.EXPENSE_DECLINED -> new StatusTO(RequestStatusEnum.DECLINED.getStatus());
            case Constants.EXPENSE_PAID -> new StatusTO(RequestStatusEnum.PAID.getStatus());
            default -> new CustomStatusTO(RequestStatusEnum.CUSTOM.getStatus(), new TitleTO(status.getName()));
        };

    }

    @PostConstruct
    private void initExpenseStatuses() {
        EXPENSE_STATUSES.put(EXPENSE_DRAFT, RequestStatusEnum.DRAFT.getStatus());
        EXPENSE_STATUSES.put(EXPENSE_SUBMITTED, RequestStatusEnum.PENDING.getStatus());
        EXPENSE_STATUSES.put(EXPENSE_APPROVED, RequestStatusEnum.APPROVED.getStatus());
        EXPENSE_STATUSES.put(EXPENSE_DECLINED, RequestStatusEnum.DECLINED.getStatus());
        EXPENSE_STATUSES.put(EXPENSE_PAID, RequestStatusEnum.PAID.getStatus());
        EXPENSE_STATUSES.put(EXPENSE_CLOSED, RequestStatusEnum.PAID.getStatus());//This is customization doesnt work for all companies so its equal to PAID
        EXPENSE_STATUSES.put(PARTIALLY_PAID, RequestStatusEnum.CUSTOM.getStatus());//We consider it as CUSTOM
    }

    @Operation(summary = "Get Expenses List", description = "Retrieves list of Expenses \n\n Retrieves the expenses for particular employee \n\n if employee_id is not provided, expenses for all employees will be retrieved")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of expenses"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/expenses/expenses_list", method = RequestMethod.GET)
    @CheckPermission(permissions = {PermissionConstants.HRMS_MAIN_MENU})
    public Object getExpensesList(@RequestParam(value = "employee_id", required = false) Integer employee_id,
                                  @RequestParam(value = "month", required = false) String month) throws RestException {

        SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);

        ArrayList<ExpenseListItemTO> expenseListResult = new ArrayList<>();

        ListingFilterParameter filterParameter = ListingFilterHelper.createFilterParameter(servletRequest, ListPanelType.ExpenceReportListPanel);
        //filterParameter.setSortField(AccountingConstants.PERIOD_COLUMN);
        //filterParameter.setAscending(false);
        filterParameter.getFacetFilter().setFilterChanges(true);
        filterParameter.getFacetFilter().setSelectedDateSolrCodeName(SolrExpenseReportRepresenter.FIELD_START_DATE);
        filterParameter.setAccessEnabled(false);
        filterParameter.setStart(0);
        filterParameter.setLimit(MAX_LIMIT);
        filterParameter.setShortList(true);

        if (employee_id != null && employee_id > 0) {
            EdsEmployee employee = employeeManager.get(employee_id);
            if (employee == null /*|| employee.getDeleted()*/) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Employee with id " + employee_id + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
            }
            //user has access to his own expenses or has permission
            if (userManager.getUser().getObjectID().equals(employee.getObjectID()) || ServerUtils.hasPermission(PermissionConstants.HRMS_EXPENCE_REPORT)) {
                SelectItem[] employeeId = {new SelectItem(employee.getObjectID(), "")};
                filterParameter.getFacetFilter().getFacetContentMap().get(FacetContentType.ExpenseReportsClaimsFacetFilter.getContentCode()[1]).setFacetItems(employeeId);
            } else {
                throw new RestException(commonLocalizer.localize("youDontHavePermission"), commonLocalizer.localize("youDontHavePermission"), ACCESS_DENIED, HttpStatus.FORBIDDEN);
            }
        } else {//If employee is not provided, show current employee's expenses
            SelectItem[] employeeId = {new SelectItem(userManager.getUser().getObjectID(), "")};
            filterParameter.getFacetFilter().getFacetContentMap().get(FacetContentType.ExpenseReportsClaimsFacetFilter.getContentCode()[1]).setFacetItems(employeeId);
        } /*else {
            employee = userManager.getUser().isEmployee() ? (EdsEmployee) userManager.getUser() : null;
        }
        if (employee == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, userManager.getUser().getFullName() + " is not employee.", INVALID, HttpStatus.BAD_REQUEST);
        }*/


        if (StringUtils.isNotBlank(month)) {
            try {
                Date rDate = longDateTimezoneFormat.parse(month);
                Date startDate = ServerUtils.getMonthStartDate(rDate);
                Date endDate = ServerUtils.getMonthEndDate(rDate);
                filterParameter.getFacetFilter().setStartDate(startDate);
                filterParameter.getFacetFilter().setEndDate(endDate);
            } catch (Exception e) {
                log.error("", e);
            }
        }

        ListResult<ExpenseReportsListItem> expensesList;
        try {
            expensesList = expenseServiceLocal.getExpenseReportsDataFromSolr(filterParameter);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }


        if (expensesList.getList() != null && !expensesList.getList().isEmpty()) {

            CurrencyItem companyBaseCurrency = currencyServiceLocal.getBaseCurrency();

            for (ExpenseReportsListItem expenseReportsListItem : expensesList.getList()) {

                /*try {
                    EdsExpenseReport report = expenseReportManager.getExpenseReport(expenseReportsListItem.getId());
                    report.initApproverData(expenseReportsListItem);
                } catch (Exception e) {
                    log.error("", e);
                    throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage() != null ? e.getMessage() : e.toString(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
                }*/
                ExpenseListItemTO expenseListItemTO = new ExpenseListItemTO();
                expenseListItemTO.setId(expenseReportsListItem.getId());
                expenseListItemTO.setTitle(expenseReportsListItem.getTitle());
                if (expenseReportsListItem.getStartDate() != null) {
                    expenseListItemTO.setDate(longDateTimezoneFormat.format(expenseReportsListItem.getStartDate().getDate()));
                }
                //Set status
                SelectItem statusItem = new SelectItem();
                statusItem.setCode(expenseReportsListItem.getStatusCode());
                statusItem.setName(expenseReportsListItem.getStatus());
                expenseListItemTO.setStatus(getDefaultStatus(statusItem));

                List<ApproverItemMini> expenseApprovers = expenseReportsListItem.getApprovers();
                if (Constants.EXPENSE_SUBMITTED.equals(expenseReportsListItem.getStatusCode())) {
                    if (expenseApprovers != null && expenseApprovers.size() > 0) {
                        ApproverListStatusTO expenseStatus = new ApproverListStatusTO();
                        if (expenseApprovers.size() == 1 && expenseApprovers.get(0).getStatus() != null) {
                            if (Constants.EXPENSE_APPROVED.equals(expenseApprovers.get(0).getStatus().getCode())) {
                                expenseStatus.setType(RequestStatusEnum.APPROVED.getStatus());
                            } else if (Constants.EXPENSE_DECLINED.equals(expenseApprovers.get(0).getStatus().getCode())) {
                                expenseStatus.setType(RequestStatusEnum.DECLINED.getStatus());
                            } else if (Constants.EXPENSE_SUBMITTED.equals(expenseApprovers.get(0).getStatus().getCode())) {
                                expenseStatus.setType(RequestStatusEnum.PENDING.getStatus());
                            }
                        } else {
                            //Means there are more than one approvers and we must set statuses based on them
                            FromValueTO dataTO = new FromValueTO();
                            dataTO.setFrom(expenseApprovers.size());
                            dataTO.setValue(0);
                            for (ApproverItemMini approver : expenseApprovers) {
                                if (approver.getStatus() != null && Constants.EXPENSE_APPROVED.equals(approver.getStatus().getCode())) {
                                    dataTO.setValue(dataTO.getValue() + 1);
                                }
                            }
                            expenseStatus.setData(dataTO);
                            if (expenseReportsListItem.getStatusCode() != null && Constants.EXPENSE_APPROVED.equals(expenseReportsListItem.getStatusCode())) {
                                expenseStatus.setType(RequestStatusEnum.APPROVED.getStatus());
                            } else if (expenseReportsListItem.getStatusCode() != null && Constants.EXPENSE_DECLINED.equals(expenseReportsListItem.getStatusCode())) {
                                expenseStatus.setType(RequestStatusEnum.DECLINED.getStatus());
                            } else {
                                if (dataTO.getValue() == 0) {
                                    expenseStatus.setType(RequestStatusEnum.PENDING.getStatus());
                                } else if (dataTO.getFrom().intValue() == dataTO.getValue().intValue()) {
                                    expenseStatus.setType(RequestStatusEnum.APPROVED.getStatus());
                                } else if (dataTO.getFrom() > dataTO.getValue() && dataTO.getValue() > 0) {
                                    expenseStatus.setType(RequestStatusEnum.PARTIALLY_APPROVED.getStatus());
                                }
                            }
                        }
                        expenseListItemTO.setStatus(expenseStatus);
                    }
                }

                expenseListItemTO.setNumber(expenseReportsListItem.getExpenseNumber());
                if (expenseReportsListItem.getApproverSelectItem() != null) {
                    expenseListItemTO.setApprover(expenseReportsListItem.getApproverSelectItem().getName());
                }
                expenseListItemTO.setReporter(expenseReportsListItem.getReporterName());

                //There always must be currency,
                //if not based currency used then we will get that otherwise system consider that expense created under base currency
                String currencyCode = null;
                if (expenseReportsListItem.getExpenseCurrency() != null) {
                    currencyCode = expenseReportsListItem.getExpenseCurrency().getName();
                } else if (expenseReportsListItem.getBaseCurrency() != null) {
                    currencyCode = expenseReportsListItem.getBaseCurrency().getName();
                } else {
                    if (companyBaseCurrency != null) {
                        currencyCode = companyBaseCurrency.getName();
                    }
                }

                //Return only if its greater than zero
                if (expenseReportsListItem.getTotal() != null && expenseReportsListItem.getTotal().compareTo(BigDecimal.ZERO) > 0) {
                    expenseListItemTO.setOriginal(new CurrencyValueTO(expenseReportsListItem.getTotal(), currencyCode));
                }
                //Return only if its greater than zero
                if (expenseReportsListItem.getPaidTotal() != null && expenseReportsListItem.getPaidTotal().compareTo(BigDecimal.ZERO) > 0) {
                    expenseListItemTO.setPaid(new CurrencyValueTO(expenseReportsListItem.getPaidTotal(), currencyCode));
                }
                //Return only if its greater than zero
                if (expenseReportsListItem.getDueTotal() != null && expenseReportsListItem.getDueTotal().compareTo(BigDecimal.ZERO) > 0) {
                    expenseListItemTO.setDue(new CurrencyValueTO(expenseReportsListItem.getDueTotal(), currencyCode));
                }

                expenseListResult.add(expenseListItemTO);
            }
        }

        return successResponse(new ExpenseListResultTO(expenseListResult));

    }

    @Operation(summary = "Get Expense Employee List", description = "Retrieves the list of employees, based on filtering in the query \"Get Expenses list\".")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of filtered employees"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/expenses/employee_filter", method = RequestMethod.GET)
    @CheckPermission(permissions = {PermissionConstants.HRMS_EXPENCE_REPORT})
    public Object getExpensesEmployeeList(@RequestParam(value = "query") String query,
                                          @RequestParam(value = "limit", required = false) Integer limit,
                                          @RequestParam(value = "offset", required = false) Integer offset) throws RestException {

        FacetFilterRpc facetFilterRpc;

        try {
            ListingFilterParameter filterParameter = ListingFilterHelper.createFilterParameter(servletRequest, ListPanelType.ExpenceReportListPanel);
            filterParameter.getFacetFilter().setSearchKey(query);
            filterParameter.setLookUp(true);

            ArrayList<String> facetCodes = new ArrayList<>();
            facetCodes.add(FacetContentType.ExpenseReportsClaimsFacetFilter.getContentCode()[1]);
            filterParameter.getFacetFilter().setShowFacetCodeName(facetCodes);

            facetFilterRpc = rbacService.getExpenseReportClaimsFacetFilterData(filterParameter.getFacetFilter(), query, true);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        ArrayList<EmployeeTO> users = new ArrayList<>();
        if (facetFilterRpc != null && facetFilterRpc.getFacetContentMap() != null
                && facetFilterRpc.getFacetContentMap().get(FacetContentType.ExpenseReportsClaimsFacetFilter.getContentCode()[1]) != null) {

            SelectItem[] userList = facetFilterRpc.getFacetContentMap().get(FacetContentType.ExpenseReportsClaimsFacetFilter.getContentCode()[1]).getFacetItems();
            //Result to return
            EmployeeListTO result = new EmployeeListTO();

            if (userList != null && userList.length > 0) {
                if (offset != null && limit != null && offset < userList.length) {
                    try {
                        userList = Arrays.copyOfRange(userList, offset, limit);

                        if (userList.length < (limit + offset)) {
                            result.setLeft(0);
                        } else {
                            result.setLeft(userList.length - (offset + limit));
                        }
                        result.setOffset(offset);

                    } catch (Exception e) {
                        log.error("", e);
                    }
                }
                for (SelectItem userItem : userList) {
                    //There is also id=-1 we should ignore them
                    if (userItem != null && userItem.getId() != null && userItem.getId() > 0) {
                        EmployeeTO employee = new EmployeeTO();
                        employee.setId(userItem.getId());
                        employee.setName(userItem.getName());
                        if (userItem.getName().contains("-")) {
                            employee.setName(userItem.getName().split("-")[1].trim());
                        }
                        EdsEmployee edsEmployee = employeeManager.get(userItem.getId());
                        if (edsEmployee != null) {
                            if (edsEmployee.getTeam() != null) {
                                employee.setDepartment(edsEmployee.getTeam().getName());
                            }

                            EdsUpload photo = edsEmployee.getPhoto();
                            if (photo != null) {
                                employee.setAvatar(commonServiceLocal.getImageUrl(photo.getObjectID()));
                            }
                        }
                        users.add(employee);
                    }
                }
            }

            result.setUsers_list(users);
            result.setTotal_count(userList != null ? userList.length : 0);
            result.setCount(users.size());
            if (result.getOffset() == null) {
                result.setOffset(0);
            }
            if (result.getLeft() == null) {
                result.setLeft(0);
            }

            return successResponse(result);
        } else {
            EmployeeListTO result = new EmployeeListTO(new ArrayList<>());
            result.setLeft(0);
            result.setOffset(0);
            result.setCount(0);
            result.setTotal_count(0);
            return successResponse(result);
        }
        //return successResponse(new EmployeeListTO(users));
    }

    @Operation(summary = "Get Expense Category List", description = "Retrieves the list of expense categories")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of expense categories"),
            @ApiResponse(responseCode = "400", description = "query is required")})
    @RequestMapping(value = "/expenses/item_categories", method = RequestMethod.GET)
    public Object getExpensesCategoryList(@RequestParam(value = "query") String query,
                                          @RequestParam(value = "limit", required = false) Integer limit,
                                          @RequestParam(value = "offset", required = false) Integer offset) throws RestException {

        if (StringUtils.isBlank(query)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "query is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        Integer start = (offset != null && offset > 0) ? offset : 0;
        Integer maxLimit = (limit != null && limit > 0) ? limit : MAX_LIMIT;

        query = query.replace("%20", " ").trim();

        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setStart(start);
        filterParameter.setLimit(maxLimit);
        filterParameter.setSearchKey(query);
        filterParameter.setSearchButton(true);

        ListResult<SelectItem> result;
        try {
            result = accountingServiceLocal.getAccountsForExpenseLookUp(filterParameter);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        PagingItemsResultTO<CategoryTO> pagingResult = new PagingItemsResultTO<>();
        pagingResult.setTotal_count(result.getTotal());
        if (result.getTotal() < (maxLimit + start)) {
            pagingResult.setLeft(0);
        } else {
            pagingResult.setLeft(result.getTotal() - (start + maxLimit));
        }
        pagingResult.setCount(result.getList() != null ? result.getList().size() : 0);
        pagingResult.setOffset(start);

        ArrayList<CategoryTO> items = new ArrayList<>();
        result.getList().forEach(accountItem -> items.add(new CategoryTO(accountItem.getId(), accountItem.getCode() + " " + accountItem.getName())));
        pagingResult.setItems(items);

        return successResponse(pagingResult);
    }

    @Operation(summary = "Get Expense BillTo List", description = "Retrieves the list of BillTo expenses")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of BillTo expenses"),
            @ApiResponse(responseCode = "400", description = "query is required")})
    @RequestMapping(value = "/expenses/item_bill", method = RequestMethod.GET)
    public Object getExpensesBillToList(@RequestParam(value = "query") String query,
                                        @RequestParam(value = "limit", required = false) Integer limit,
                                        @RequestParam(value = "offset", required = false) Integer offset) throws RestException {
        if (StringUtils.isBlank(query)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "query is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        /*if (limit == null || limit < 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "limit is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (offset == null || offset < 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "offset is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (offset.equals(limit) && offset == 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "limit and offset cannot be zero at the same time", REQUIRED, HttpStatus.BAD_REQUEST);
        }*/
        Integer start = (offset != null && offset > 0) ? offset : 0;
        Integer maxLimit = (limit != null && limit > 0) ? limit : MAX_LIMIT;

        query = query.replace("%20", " ").trim();

        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setStart(start);
        filterParameter.setLimit(maxLimit);
        filterParameter.setSearchKey(query);
        filterParameter.setAccountType(CrmConstants.CUSTOMER);
        filterParameter.setSearchByParent(true);
        filterParameter.setLookUp(true);
        filterParameter.setFromMobile(true);
        filterParameter.setSearchButton(true);

        ListResult<SelectItem> result;
        try {
            result = allInOneServiceLocal.getCrmAccountAsSelectItem(CrmConstants.CRM_ACCOUNT_ID, filterParameter);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        PagingItemsResultTO<CategoryTO> pagingResult = new PagingItemsResultTO<>();
        pagingResult.setTotal_count(result.getTotal());
        if (result.getTotal() < (maxLimit + start)) {
            pagingResult.setLeft(0);
        } else {
            pagingResult.setLeft(result.getTotal() - (start + maxLimit));
        }
        pagingResult.setCount(result.getList() != null ? result.getList().size() : 0);
        pagingResult.setOffset(start);

        ArrayList<CategoryTO> items = new ArrayList<>();
        result.getList().forEach(customer -> items.add(new CategoryTO(customer.getId(), customer.getName())));
        pagingResult.setItems(items);

        return successResponse(pagingResult);
    }

    @Operation(summary = "Get Expense Supplier List", description = "Retrieves the list of expense supplier")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of expense supplier")})
    @RequestMapping(value = "/expenses/suppliers_list", method = RequestMethod.GET)
    public Object getExpensesSupplierList() throws RestException {
        return getCustomerOrSupplierList(false);
    }

    @Operation(summary = "Get Related Projects List", description = "Retrieves the list of Related Projects")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of Related Projects"),
            @ApiResponse(responseCode = "400", description = "query is required")})
    @RequestMapping(value = "/expenses/related_projects", method = RequestMethod.GET)
    public Object getRelatedProjectList(@RequestParam(value = "query") String query,
                                        @RequestParam(value = "limit", required = false) Integer limit,
                                        @RequestParam(value = "offset", required = false) Integer offset) throws RestException {
        if (StringUtils.isBlank(query)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "query is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        Integer start = (offset != null && offset > 0) ? offset : 0;
        Integer maxLimit = (limit != null && limit > 0) ? limit : MAX_LIMIT;

        query = query.replace("%20", " ").trim();

        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setInvoiceType(EXPENSE_REPORT);
        filterParameter.setEmployeeId(userManager.getUser().getObjectID());
        filterParameter.setStart(start);
        filterParameter.setLimit(maxLimit);
        filterParameter.setSearchKey(query);
        filterParameter.setFromMobile(true);
        filterParameter.setLookUp(true);
        filterParameter.setSearchButton(true);

        ListResult<SelectItem> result;
        try {
            result = invoiceServiceLocal.getExpenseRelatedProjects(filterParameter);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        PagingItemsResultTO<CategoryTO> pagingResult = new PagingItemsResultTO<>();

        pagingResult.setTotal_count(result.getTotal());
        if (result.getTotal() < (maxLimit + start)) {
            pagingResult.setLeft(0);
        } else {
            pagingResult.setLeft(result.getTotal() - (start + maxLimit));
        }

        pagingResult.setCount(result.getList() != null ? result.getList().size() : 0);
        pagingResult.setOffset(start);

        ArrayList<CategoryTO> projects = new ArrayList<>();
        result.getList().forEach(project -> projects.add(new CategoryTO(project.getId(), (project.getCode() != null ? project.getCode() + " " : "") + project.getName())));

        pagingResult.setItems(projects);

        return successResponse(pagingResult);
    }

    @Operation(summary = "Get Fixed Assets List", description = "Retrieves the list of Fixed Assets")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of Fixed Assets")})
    @RequestMapping(value = "/expenses/fixed_assets", method = RequestMethod.GET)
    public Object getFixedAssetList() throws RestException {
        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setStart(0);
        filterParameter.setLimit(MAX_LIMIT);

        try {
            List<EdsFixedAsset> fixedAssets = fixedAssetManager.getFixedAssetsForLookUp(filterParameter);
            ArrayList<CategoryTO> fixedAssetList = new ArrayList<>();
            for (EdsFixedAsset fixedAsset : fixedAssets) {
                fixedAssetList.add(new CategoryTO(fixedAsset.getObjectID(), fixedAsset.getName()));
            }
            return successResponse(new ResponseItemsListData<>(fixedAssetList));
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(ERROR_MESSAGE, e.getMessage() != null ? e.getMessage() : e.toString(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get Expense Details", description = "Retrieves the details of expense claim based on request_id")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have expense claim details"),
            @ApiResponse(responseCode = "400", description = "request_id is required"),
            @ApiResponse(responseCode = "422", description = "request_id should be more then zero")})
    @RequestMapping(value = "/expenses/expense_claim", method = RequestMethod.GET)
    @CheckPermission(permissions = {PermissionConstants.HRMS_MAIN_MENU, PermissionConstants.HRMS_VIEW_EXPENSE_CLAIM})
    public Object getExpenseClaimDetails(@RequestParam(value = "request_id") Integer request_id) throws RestException {
        if (request_id == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "request_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (request_id <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "request_id should be more then zero", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        ExpenseReportsListItem expenseListItem;
        try {
            expenseListItem = expenseServiceLocal.getReport(request_id);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage() != null ? e.getMessage() : e.toString(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (expenseListItem == null || expenseListItem.getId() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Expense Claim with " + request_id + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        ExpenseDetailsTO expenseDetails = new ExpenseDetailsTO();
        expenseDetails.setId(expenseListItem.getId());
        expenseDetails.setNumber(expenseListItem.getExpenseNumber());

        if (expenseListItem.getReporterId() != null) {
            EdsEmployee employee = employeeManager.get(expenseListItem.getReporterId());
            if (employee != null && !employee.getObjectID().equals(userManager.getUser().getObjectID())) {
                OwnerTO owner = new OwnerTO();
                owner.setId(employee.getObjectID());
                owner.setName(employee.getName());
                owner.setAvatar(hrmsServiceLocal.getEmployeeImageURL(expenseListItem.getReporterId()));
                if (employee.getEmployeeDepartment() != null && employee.getEmployeeDepartment().getTeam() != null) {
                    owner.setDepartment(employee.getEmployeeDepartment().getTeam().getName());
                }
                expenseDetails.setOwner(owner);
            }
        }

        SelectItem statusItem = new SelectItem();
        statusItem.setCode(expenseListItem.getStatusCode());
        statusItem.setName(expenseListItem.getOverallStatusName());
        expenseDetails.setStatus(getDefaultStatus(statusItem));

        List<ApproverItemMini> expenseApprovers = expenseListItem.getApprovers();
        if (Constants.EXPENSE_SUBMITTED.equals(expenseListItem.getStatusCode())) {
            if (expenseApprovers != null && expenseApprovers.size() > 0) {
                ApproverListStatusTO expenseStatus = new ApproverListStatusTO();
                if (expenseApprovers.size() == 1 && expenseApprovers.get(0).getStatus() != null) {
                    if (Constants.EXPENSE_APPROVED.equals(expenseApprovers.get(0).getStatus().getCode())) {
                        expenseStatus.setType(RequestStatusEnum.APPROVED.getStatus());
                    } else if (Constants.EXPENSE_DECLINED.equals(expenseApprovers.get(0).getStatus().getCode())) {
                        expenseStatus.setType(RequestStatusEnum.DECLINED.getStatus());
                    } else if (Constants.EXPENSE_SUBMITTED.equals(expenseApprovers.get(0).getStatus().getCode())) {
                        expenseStatus.setType(RequestStatusEnum.PENDING.getStatus());
                    }
                } else {
                    //Means there are more than one approvers and we must set statuses based on them
                    FromValueTO dataTO = new FromValueTO();
                    dataTO.setFrom(expenseApprovers.size());
                    dataTO.setValue(0);
                    for (ApproverItemMini approver : expenseApprovers) {
                        if (approver.getStatus() != null && Constants.EXPENSE_APPROVED.equals(approver.getStatus().getCode())) {
                            dataTO.setValue(dataTO.getValue() + 1);
                        }
                    }
                    expenseStatus.setData(dataTO);
                    if (expenseListItem.getStatusCode() != null && Constants.EXPENSE_APPROVED.equals(expenseListItem.getStatusCode())) {
                        expenseStatus.setType(RequestStatusEnum.APPROVED.getStatus());
                    } else if (expenseListItem.getStatusCode() != null && Constants.EXPENSE_DECLINED.equals(expenseListItem.getStatusCode())) {
                        expenseStatus.setType(RequestStatusEnum.DECLINED.getStatus());
                    } else {
                        if (dataTO.getValue() == 0) {
                            expenseStatus.setType(RequestStatusEnum.PENDING.getStatus());
                        } else if (dataTO.getFrom().intValue() == dataTO.getValue().intValue()) {
                            expenseStatus.setType(RequestStatusEnum.APPROVED.getStatus());
                        } else if (dataTO.getFrom() > dataTO.getValue() && dataTO.getValue() > 0) {
                            expenseStatus.setType(RequestStatusEnum.PARTIALLY_APPROVED.getStatus());
                        }
                    }
                }
                expenseDetails.setStatus(expenseStatus);
            }
        }

        SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);
        if (expenseListItem.getStartDate() != null) {
            expenseDetails.setDate(longDateTimezoneFormat.format(expenseListItem.getStartDate().getNonConvertedDate()));
        }
        expenseDetails.setReport_title(expenseListItem.getTitle());
        if (expenseListItem.getDescription() != null && !"".equals(expenseListItem.getDescription())) {
            expenseDetails.setDescription(expenseListItem.getDescription());
        }

        if (expenseListItem.getApprovers() != null && expenseListItem.getApprovers().size() > 0) {

            FromValueTO dataTO = new FromValueTO();
            dataTO.setFrom(expenseListItem.getApprovers().size());
            dataTO.setValue(0);

            ArrayList<ApproversTO> approvers = new ArrayList<>();
            for (ApproverItemMini approver : expenseListItem.getApprovers()) {
                if (approver.getExactEmployee() != null) {
                    EdsUser employeeApprover = userManager.get(approver.getExactEmployee().getId());
                    if (employeeApprover != null) {
                        ApproversTO approversTO = new ApproversTO();
                        approversTO.setId(employeeApprover.getObjectID());
                        approversTO.setIndex(approver.getApproverOrder());
                        approversTO.setName(employeeApprover.getName());
                        if (employeeApprover.getPhoto() != null) {
                            approversTO.setAvatar(hrmsServiceLocal.getEmployeeImageURL(employeeApprover.getObjectID()));
                        }
                        EdsEmployee employee = employeeApprover.isEmployee() ? employeeApprover.getEmployee() : null;
                        if (employee != null && employee.getEmployeeDepartment() != null && employee.getEmployeeDepartment().getTeam() != null) {
                            approversTO.setDepartment(employee.getEmployeeDepartment().getTeam().getName());
                        }
                        if (approver.getStatus() != null && approver.getStatus().getCode() != null) {
                            if (Constants.EXPENSE_APPROVED.equals(approver.getStatus().getCode())) {
                                approversTO.setStatus(new ApproverStatusTO(RequestStatusEnum.APPROVED.getStatus()));
                                dataTO.setValue(dataTO.getValue() + 1);
                            } else if (Constants.EXPENSE_DECLINED.equals(approver.getStatus().getCode())) {
                                approversTO.setStatus(new ApproverStatusTO(RequestStatusEnum.DECLINED.getStatus()));
                            } else if (Constants.EXPENSE_PAID.equals(approver.getStatus().getCode())) {
                                approversTO.setStatus(new ApproverStatusTO(RequestStatusEnum.APPROVED.getStatus()));
                            } else {
                                approversTO.setStatus(new ApproverStatusTO(RequestStatusEnum.PENDING.getStatus()));
                            }
                        } else {
                            approversTO.setStatus(new ApproverStatusTO(RequestStatusEnum.PENDING.getStatus()));
                        }
                        approvers.add(approversTO);
                    }
                }
            }
            expenseDetails.setApprovers(approvers);

            /*if (expenseDetails.getStatus() != null && RequestStatusEnum.PARTIALLY_APPROVED.getStatus().equalsIgnoreCase(expenseDetails.getStatus().getType())) {
                expenseDetails.getStatus().setData(dataTO);
            }*/
            if (expenseDetails.getStatus() != null && expenseDetails.getStatus() instanceof ApproverListStatusTO status) {
                if ((RequestStatusEnum.PARTIALLY_APPROVED.getStatus().equalsIgnoreCase(status.getType()))) {
                    status.setData(dataTO);
                    expenseDetails.setStatus(status);
                }
            }
        }


        CurrencyItem baseCurrency = currencyServiceLocal.getBaseCurrency();
        if (expenseListItem.getExpenseCurrency() != null) {
            expenseDetails.setCurrency(new CurrencyListTO(expenseListItem.getExpenseCurrency().getId(), expenseListItem.getExpenseCurrency().getName(), expenseListItem.getExchangeRate() != null ? expenseListItem.getExchangeRate() : BigDecimal.ONE));
        } else if (baseCurrency != null) {
            expenseDetails.setCurrency(new CurrencyListTO(baseCurrency.getId(), baseCurrency.getName(), BigDecimal.ONE));
        }
        if (expenseListItem.getSupplier() != null) {
            expenseDetails.setSupplier(new CategoryTO(expenseListItem.getSupplier().getId(), expenseListItem.getSupplier().getName()));
        }
        if (expenseListItem.getProject() != null) {
            String projectName = expenseListItem.getProject().getName();
            if (StringUtils.isNotBlank(projectName)) {
                projectName = projectName.replace("->", " ").replaceAll(" {2}", "");
            }
            expenseDetails.setRelated_project(new CategoryTO(expenseListItem.getProject().getId(), projectName));
        }


        if (expenseListItem.getFixedAsset() != null) {
            String fixedAssetName = expenseListItem.getFixedAsset().getName();
            if (StringUtils.isNotBlank(fixedAssetName)) {
                fixedAssetName = fixedAssetName.replace("->", " ").replaceAll(" {2}", "");
            }
            expenseDetails.setFixed_asset(new CategoryTO(expenseListItem.getFixedAsset().getId(), fixedAssetName));
        }

        if (expenseListItem.getTaxCalculationType() == null || AccountingConstants.NO_TAX_CALCULATION.equals(expenseListItem.getTaxCalculationType())) {
            expenseDetails.setTax_type(new CategoryTO(TaxTypeEnum.NO_TAX.getId(), TaxTypeEnum.NO_TAX.getName()));
        } else if (AccountingConstants.TAX_CALCULATION_INCLUSIVE.equals(expenseListItem.getTaxCalculationType())) {
            expenseDetails.setTax_type(new CategoryTO(TaxTypeEnum.TAX_INCLUSIVE.getId(), TaxTypeEnum.TAX_INCLUSIVE.getName()));
        } else if (AccountingConstants.TAX_CALCULATION_EXCLUSIVE.equals(expenseListItem.getTaxCalculationType())) {
            expenseDetails.setTax_type(new CategoryTO(TaxTypeEnum.TAX_EXCLUSIVE.getId(), TaxTypeEnum.TAX_EXCLUSIVE.getName()));
        }
        //todo this is temporary solution that we are retvieving the last note
        if (Constants.EXPENSE_DRAFT.equals(expenseListItem.getStatusCode()) && expenseListItem.getNoteItems() != null) {
            expenseListItem.getNoteItems();
            for (HistoryListItem note : expenseListItem.getNoteItems()) {
                expenseDetails.setNotes(note.getComment());
                break;
            }
        }
        //if no attachments, return empty array
        ArrayList<FileResource> attachmentList = documentsServiceLocal.getFileResources(Constants.F_EXP_DOC, expenseListItem.getId(), expenseListItem.getId());
        ArrayList<AttachmentTO> attachments = new ArrayList<>();
        if (attachmentList != null && attachmentList.size() > 0) {
            for (FileResource fileItem : attachmentList) {
                attachments.add(new AttachmentTO(fileItem.getFileName(), fileItem.getDownloadUrl()));
            }
        }
        expenseDetails.setAttachments(attachments);

        EdsFinancialSettings edsFinancialSettings = financialSettingsManager.getFinancialSettings();
        Integer calculationScale = (edsFinancialSettings != null && edsFinancialSettings.getCalculationScale() != null) ? edsFinancialSettings.getCalculationScale() : 2;
        if (expenseListItem.getItems() != null && expenseListItem.getItems().length > 0) {
            ArrayList<ExpenseItemTO> expenseItems = new ArrayList<>();
            for (ExpenseListItem expenseItem : expenseListItem.getItems()) {
                ExpenseItemTO item = new ExpenseItemTO();
                item.setId(expenseItem.getId());
                if (expenseItem.getAccountId() != null) {
                    String categoryCode = expenseItem.getAccountCode();
                    String categoryName = expenseItem.getAccountName();
                    if (StringUtils.isNotBlank(categoryName)) {
                        categoryName = categoryName.replace("->", " ");
                    }
                    item.setCategory(new CategoryTO(expenseItem.getAccountId(), categoryCode + " " + categoryName));
                }
                if (expenseItem.getDescription() != null && !"".equals(expenseItem.getDescription())) {
                    item.setDescription(expenseItem.getDescription());
                }
                if (expenseItem.getUnits() != null && !BigDecimal.ZERO.equals(expenseItem.getUnits())) {
                    item.setUnits(expenseItem.getUnits().setScale(2, RoundingMode.HALF_UP));
                }
                item.setCost_per_unit(expenseItem.getCostPerUnit().setScale(calculationScale, RoundingMode.HALF_UP));
                if (expenseItem.getTax() != null) {
                    item.setTax(new TaxTO(expenseItem.getTax().getId(), expenseItem.getTax().getName(), expenseItem.getTax().getTaxPercent()));
                }

                ArrayList<AttachmentTO> itemAttachments = new ArrayList<>();
                if (expenseItem.getAttachments() != null) {
                    expenseItem.getAttachments();
                    for (FileResource fileItem : expenseItem.getAttachments()) {
                        itemAttachments.add(new AttachmentTO(fileItem.getFileName(), fileItem.getDownloadUrl()));
                    }
                }
                item.setDraft_receipts(itemAttachments);
                if (expenseItem.getClientId() != null) {
                    item.setBill_to(new CategoryTO(expenseItem.getClientId(), expenseItem.getClientName()));
                }
                if (ApiUtils.getTotal(expenseItem.getMarkupAmount()) != null) {
                    item.setMarkup_amount(expenseItem.getMarkupAmount().setScale(calculationScale, RoundingMode.HALF_UP));
                }

                expenseItems.add(item);
            }
            expenseDetails.setItems(expenseItems);
        }

        ArrayList<CustomFieldsTO> customFields = getCustomFields(expenseListItem.getCustomFieldItems());
        if (customFields != null && customFields.size() > 0) {
            expenseDetails.setCustom_fields(customFields);
        }

        expenseDetails.setUser_actions(getUserAction(expenseListItem, userManager.getUser()));
        expenseDetails.setTotal(expenseListItem.getTotal());
        expenseDetails.setDueAmount(expenseListItem.getDueTotal());
        expenseDetails.setSubTotal(calculateSubTotal(expenseListItem));
        return successResponse(expenseDetails);
    }

    private BigDecimal calculateSubTotal(ExpenseReportsListItem expenseListItem) {

        BigDecimal subTotalValue = BigDecimal.ZERO;
        for (int i = 0; i < expenseListItem.getItems().length; i++) {
            BigDecimal units = expenseListItem.getItems()[i].getUnits();
            BigDecimal cost = expenseListItem.getItems()[i].getCostPerUnit();
            BigDecimal net = units.multiply(cost);
            subTotalValue = subTotalValue.add(net.setScale(2, RoundingMode.HALF_UP));
        }
        return subTotalValue;
    }

    @Operation(summary = "Expense Claim Action", description = "Approves or rejects the request based on request_id and request_action \n" +
            "request_action should be APPROVE, APPROVE_FOR_ALL or REJECT")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have true or false with appropriate error message"),
            @ApiResponse(responseCode = "400", description = "request_id is required"),
            @ApiResponse(responseCode = "400", description = "request_action is required"),
            @ApiResponse(responseCode = "422", description = "Request action should be one of APPROVE, REJECT, APPROVE_FOR_ALL")})
    @RequestMapping(value = "/expenses/expense_claim", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object expenseClaimAction(@RequestBody RequestActionTO expenseClaimAction) throws RestException {
        if (expenseClaimAction.getRequest_id() == null || expenseClaimAction.getRequest_id() <= 0) {
            throw new RestException(ERROR_MESSAGE, "valid request_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(expenseClaimAction.getAction())) {
            throw new RestException(ERROR_MESSAGE, "Request action is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        String action = getRequestAction(expenseClaimAction.getAction());
        if (StringUtils.isBlank(action)) {
            throw new RestException(ERROR_MESSAGE, "Request action should be one of APPROVE, REJECT, APPROVE_FOR_ALL", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        ExpenseReportsListItem expenseListItem;
        try {
            expenseListItem = expenseServiceLocal.getReport(expenseClaimAction.getRequest_id());
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage() != null ? e.getMessage() : e.toString(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (expenseListItem == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Expense Claim with " + expenseClaimAction.getRequest_id() + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        SelectItem statusItem = new SelectItem();
        statusItem.setCode(expenseListItem.getStatusCode());
        statusItem.setName(expenseListItem.getOverallStatusName());
        ApproverListStatusTO statusTO = getStatus(statusItem);
        statusTO = statusTO != null ? statusTO : new ApproverListStatusTO(expenseListItem.getStatusCode());

        String status = null;
        RequestUserActionTO userAction = getUserAction(expenseListItem, userManager.getUser());
        if (RequestActionEnum.APPROVE.name().equals(action)) {
            if (userAction.isApprove()) {
                status = Constants.EXPENSE_APPROVED;
            } else if (Constants.EXPENSE_DECLINED.equals(expenseListItem.getOverallStatusCode())) {
                throw new RestException(ERROR_MESSAGE, "Rejected expense claim cannot be approved", CONFLICT, HttpStatus.CONFLICT);
            } else if (Constants.EXPENSE_APPROVED.equals(expenseListItem.getOverallStatusCode())) {
                throw new RestException(ERROR_MESSAGE, "Expense claim has already been approved", CONFLICT, HttpStatus.CONFLICT);
            } else if (!userAction.isApprove()) {
                throw new RestException(ERROR_MESSAGE, "Expense claim with " + statusTO.getType() + " status cannot be approved", CONFLICT, HttpStatus.CONFLICT);
            }
        } else if (RequestActionEnum.REJECT.name().equals(action)) {
            if (userAction.isReject()) {
                status = Constants.EXPENSE_DECLINED;
            } else if (Constants.EXPENSE_DECLINED.equals(expenseListItem.getOverallStatusCode())) {
                throw new RestException(ERROR_MESSAGE, "Expense claim has already been rejected", CONFLICT, HttpStatus.CONFLICT);
            } else if (Constants.EXPENSE_APPROVED.equals(expenseListItem.getOverallStatusCode())) {
                throw new RestException(ERROR_MESSAGE, "Approved expense claim cannot be rejected", CONFLICT, HttpStatus.CONFLICT);
            } else if (!userAction.isReject()) {
                throw new RestException(ERROR_MESSAGE, "Expense claim with " + statusTO.getType() + " status cannot be rejected", CONFLICT, HttpStatus.CONFLICT);
            }
        } else if (RequestActionEnum.APPROVE_FOR_ALL.name().equals(action)) {
            if (userAction.isApprove_for_all()) {
                status = Constants.EXPENSE_APPROVED;
            } else if (Constants.EXPENSE_APPROVED.equals(expenseListItem.getOverallStatusCode())) {
                throw new RestException(ERROR_MESSAGE, "Expense claim has already been approved", CONFLICT, HttpStatus.CONFLICT);
            } else if (Constants.EXPENSE_DECLINED.equals(expenseListItem.getOverallStatusCode())) {
                throw new RestException(ERROR_MESSAGE, "Rejected expense claim cannot be approved", CONFLICT, HttpStatus.CONFLICT);
            } else if (!userAction.isApprove()) {
                throw new RestException(ERROR_MESSAGE, "Expense claim with " + statusTO.getType() + " status cannot be approved", CONFLICT, HttpStatus.CONFLICT);
            }
        }
        expenseListItem.setStatus(status);
        try {
            expenseServiceLocal.changeExpenseStatus(expenseListItem.getId(), status, "", RequestActionEnum.APPROVE_FOR_ALL.name().equals(action), null);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return successResponse(new ResponseData());
    }

    @Operation(summary = "Add Expense Claim", description = "Creates new expense claim based on provided parameters")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have true or false with appropriate error message"),
            @ApiResponse(responseCode = "400", description = "Valid JSON body format is required"),
            @ApiResponse(responseCode = "422", description = "Invalid create_type. It should be one of DRAFT,FINAL")})
    @RequestMapping(value = "/expenses/expense_claim", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @CheckPermission(permissions = {PermissionConstants.HRMS_MAIN_MENU})
    public Object createExpenseClaim(MultipartRequest multipartRequest, @RequestParam(name = "body") String jsonString) throws RestException {

        EdsEmployee currentUser = employeeManager.get(employeeManager.getUser().getObjectID());

        if (currentUser.getDeleted() || Constants.USER_TYPE_BMT_RESPONDENT.equals(currentUser.getUserType())) {
            throw new RestException("Your account was disabled. Please contact your company admin.", "User is deleted/resigned.", ACCESS_DENIED, HttpStatus.FORBIDDEN);
        }

        String currentUserStatus = userManager.getUserStatus(currentUser.getObjectID());
        if (!Constants.EMPLOYEE_STATUS_ACTIVE.equals(currentUserStatus)) {
            throw new RestException("Please verify your registration from a confirmation email sent to you to proceed.", "User is not active.", ACCESS_DENIED, HttpStatus.FORBIDDEN);
        }

        ExpenseAddTO expenseAdd;
        ObjectMapper mapper = new ObjectMapper();

        try {
            expenseAdd = mapper.readValue(jsonString, ExpenseAddTO.class);
        } catch (Exception e) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "JSON body format is wrong." + e.getMessage(), REQUIRED, HttpStatus.BAD_REQUEST);
        }

        ExpenseAddRequestTO expenseAddRequest = expenseAdd.getRequest() != null ? expenseAdd.getRequest() : new ExpenseAddRequestTO();

        if (expenseAddRequest.getId() == null && !ServerUtils.hasPermission(PermissionConstants.HRMS_ADD_NEW_EXPENSE_CLAIM)) {
            throw new RestException(commonLocalizer.localize("youDontHavePermission"), commonLocalizer.localize("youDontHavePermission"), ACCESS_DENIED, HttpStatus.FORBIDDEN);
        }
        if (expenseAddRequest.getId() != null && !ServerUtils.hasPermission(PermissionConstants.HRMS_EXPENSE_REPORT_EDIT)) {
            throw new RestException(commonLocalizer.localize("youDontHavePermission"), commonLocalizer.localize("youDontHavePermission"), ACCESS_DENIED, HttpStatus.FORBIDDEN);
        }

        if (!CreateTypeEnum.DRAFT.name().equals(expenseAdd.getCreate_type()) && !CreateTypeEnum.FINAL.name().equals(expenseAdd.getCreate_type())) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Invalid create_type. It should be one of DRAFT,FINAL", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        EdsEmployee employee;
        if (expenseAddRequest.getEmployee_id() != null && expenseAddRequest.getEmployee_id() > 0) {
            employee = employeeManager.get(expenseAddRequest.getEmployee_id());
            //If the employee status is RESIGNED (equals to removed), we shoudn't be able to create expense for such employees.
            if (employee == null || !employee.isEmployee() || employee.getDeleted()) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Requester with id " + expenseAddRequest.getEmployee_id() + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
            }
        } else {
            employee = currentUser;//employeeManager.get(user.getObjectID());
            if (employee == null || !employee.isEmployee()) {
                throw new RestException(GENERAL_ERROR_MESSAGE, currentUser.getFullName() + " is not employee.", INVALID, HttpStatus.BAD_REQUEST);
            }
        }

        Date date = null;
        SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);
        CurrencyItem currency = null;
        CurrencyItem baseCurrency = currencyServiceLocal.getBaseCurrency();
        BigDecimal exchangeRate = BigDecimal.ONE;
        Integer taxCalculationType = expenseAddRequest.getTax_type() != null ? expenseAddRequest.getTax_type() : AccountingConstants.NO_TAX_CALCULATION;

        //if expense creation type is final, validate all required fields
        if (CreateTypeEnum.FINAL.name().equals(expenseAdd.getCreate_type())) {
            try {
                date = longDateTimezoneFormat.parse(expenseAddRequest.getDate());
            } catch (ParseException e) {
                log.error("", e);
                throw new RestException("Invalid date format", "Invalid date format. Acceptable format is " + longDateTimezoneFormat.toPattern(), INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (StringUtils.isBlank(expenseAddRequest.getReport_title())) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Expense title is required", REQUIRED, HttpStatus.BAD_REQUEST);
            }

            if (expenseAddRequest.getApprovers() == null || expenseAddRequest.getApprovers().size() == 0) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Expense approvers are required", REQUIRED, HttpStatus.BAD_REQUEST);
            }

            ApprovalListResult expenseApprovers = allInOneServiceLocal.getApprovers(RelationItem.TYPE_EXPENSE_CLAIM, null, false, null, false);
            if (expenseAddRequest.getApprovers().size() != expenseApprovers.getList().size()) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Expense approvers count should be " + expenseApprovers.getList().size(), INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
            }

            if (expenseAddRequest.getItems() == null || expenseAddRequest.getItems().size() == 0) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Expense item is required", REQUIRED, HttpStatus.BAD_REQUEST);
            }

            expenseAddRequest.getItems().forEach(expenseItem -> {
                try {
                    if (expenseItem.getCategory() == null || expenseItem.getCategory().getId() == null) {
                        throw new RestException(GENERAL_ERROR_MESSAGE, "Expense category is required", REQUIRED, HttpStatus.BAD_REQUEST);
                    }
                    if (ApiUtils.getTotal(expenseItem.getCost_per_unit()) == null) {
                        throw new RestException(GENERAL_ERROR_MESSAGE, "Expense cost per unit is required", REQUIRED, HttpStatus.BAD_REQUEST);
                    }
                    if (ApiUtils.getTotal(expenseItem.getUnits()) == null) {
                        throw new RestException(GENERAL_ERROR_MESSAGE, "Expense unit is required", REQUIRED, HttpStatus.BAD_REQUEST);
                    }
                } catch (RestException e) {
                    log.error("", e);
                }
            });

            if (expenseAddRequest.getCurrency_id() == null || expenseAddRequest.getCurrency_id() <= 0) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Currency is required", REQUIRED, HttpStatus.BAD_REQUEST);
            }

            currency = currencyServiceLocal.getCurrency(expenseAddRequest.getCurrency_id());
            if (currency == null || currency.getId() == null) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Currency with id " + expenseAddRequest.getCurrency_id() + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
            }
        }

        if (expenseAddRequest.getDate() != null) {
            try {
                date = longDateTimezoneFormat.parse(expenseAddRequest.getDate());
            } catch (ParseException e) {
                log.error("", e);
            }
        }

        if (expenseAddRequest.getCurrency_id() != null && expenseAddRequest.getCurrency_id() > 0) {
            currency = currencyServiceLocal.getCurrency(expenseAddRequest.getCurrency_id());
            if (currency == null) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Currency with id " + expenseAddRequest.getCurrency_id() + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
            }
        }

        //Expense Item attachment pattern item_{item_index}_file_{file_index}. e.g. item_1_file_1, item_2_file_2
        //Expense attachment pattern file{file_index}. e.g file0, file1, file2
        //Expense custom field attachment pattern custom_field_{field_id}_attachment_{attachment_index}  e.g custom_field_217_attachment_1


        //Map key is expense item index,value is attachment
        TreeMap<Integer, ArrayList<MultipartFile>> expenseItemAttachmentsMap = new TreeMap<>();

        ArrayList<MultipartFile> expenseAttachments = new ArrayList<>();

        //Map key is expense custom field id,value is attachment
        TreeMap<Integer, ArrayList<MultipartFile>> expenseCustomFieldAttachmentsMap = new TreeMap<>();

        if (multipartRequest != null && multipartRequest.getFileMap() != null && multipartRequest.getFileMap().size() > 0) {
            for (MultipartFile file : multipartRequest.getFileMap().values()) {
                if (file.getName().matches(entityItemFileNameRegex)) {
                    Integer index = Integer.valueOf(file.getName().split("_")[1]);
                    ArrayList<MultipartFile> files = expenseItemAttachmentsMap.get(index) == null ? new ArrayList<>() : expenseItemAttachmentsMap.get(index);
                    files.add(file);
                    expenseItemAttachmentsMap.put(index, files);
                } else if (file.getName().matches(entityFileNameRegex)) {
                    expenseAttachments.add(file);
                } else if (file.getName().matches(customFieldFileNameRegex)) {
                    Pattern p = Pattern.compile(customFieldFileNameRegex);
                    Matcher m = p.matcher(file.getName());
                    Integer customFieldFileId = 0;
                    Integer customFieldFileIndex = 0;
                    if (m.matches()) {
                        customFieldFileId = Integer.valueOf(m.group(1));
                        customFieldFileIndex = Integer.valueOf(m.group(2));
                        ArrayList<MultipartFile> files = expenseCustomFieldAttachmentsMap.get(customFieldFileId) == null ? new ArrayList<>() : expenseCustomFieldAttachmentsMap.get(customFieldFileId);
                        files.add(file);
                        expenseCustomFieldAttachmentsMap.put(customFieldFileId, files);
                    }
                }
            }
        }

        ExpenseReportsListItem report = new ExpenseReportsListItem();
        if (expenseAddRequest.getId() == null) {//add
            BankTransferNumberData numberData = expenseServiceLocal.generateExpenseReportNumber();
            report.setExpenseNumber(numberData.getTransferNumber());
            report.setExpenseNumberData(numberData);
            try {
                report.setIntNumber(Integer.parseInt(numberData.getFourDigitNumber()));
            } catch (NumberFormatException e) {
                log.error(e.getMessage());
            }
            if (CreateTypeEnum.DRAFT.name().equals(expenseAdd.getCreate_type())) {
                report.setStatusCode(EXPENSE_DRAFT);
            } else {
                report.setStatusCode(Constants.EXPENSE_SUBMITTED);
            }
            if (expenseAddRequest.getApprovers() != null && expenseAddRequest.getApprovers().size() > 0) {
                report.setApprovers(getChosenApprovers(expenseAddRequest.getApprovers(), getAllAvailableApprovers(RelationItem.TYPE_EXPENSE_CLAIM)));
            }
            //Save only a note if expense is creating
            if (StringUtils.isNotBlank(expenseAddRequest.getNotes())) {
                HistoryListItem noteItem = new HistoryListItem();
                noteItem.setComment(expenseAddRequest.getNotes());
                noteItem.setEventDate(new Date());
                noteItem.setEmployeeID(currentUser.getObjectID());
                noteItem.setEmployee(currentUser.getName());
                report.setNoteItems(new HistoryListItem[]{noteItem});
            }
        } else {//edit
            report = expenseServiceLocal.getReport(expenseAddRequest.getId());
            if (report == null || report.getId() == null) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Expense with id " + expenseAddRequest.getId() + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
            }
            if (Constants.EXPENSE_DRAFT.equals(report.getStatusCode())) {//update expense approvers when the expense status is draft
                if (expenseAddRequest.getApprovers() != null && expenseAddRequest.getApprovers().size() > 0) {
                    report.setApprovers(getChosenApprovers(expenseAddRequest.getApprovers(), getAllAvailableApprovers(RelationItem.TYPE_EXPENSE_CLAIM)));
                }
            }
            if (CreateTypeEnum.FINAL.name().equals(expenseAdd.getCreate_type()) && Constants.EXPENSE_DRAFT.equals(report.getStatusCode())) {
                report.setStatusCode(Constants.EXPENSE_SUBMITTED);
            }
            //Before saving a new expense note, get old notes and add new once to them and save while editing expense claim
            if (StringUtils.isNotBlank(expenseAddRequest.getNotes())) {
                ArrayList<HistoryListItem> noteList = (report.getNoteItems() != null && report.getNoteItems().length > 0) ? new ArrayList<>(Arrays.asList(report.getNoteItems())) : new ArrayList<>();
                if (noteList.size() > 0) {
                    noteList.get(0);
                    noteList.get(0).setComment(expenseAddRequest.getNotes());
                    noteList.get(0).setEventDate(new Date());
                    noteList.get(0).setEmployeeID(currentUser.getObjectID());
                    noteList.get(0).setEmployee(currentUser.getName());
                } else {
                    HistoryListItem newNote = new HistoryListItem();
                    newNote.setComment(expenseAddRequest.getNotes());
                    newNote.setEventDate(new Date());
                    newNote.setEmployeeID(currentUser.getObjectID());
                    newNote.setEmployee(currentUser.getName());
                    noteList.add(newNote);
                }

                report.setNoteItems(noteList.toArray(new HistoryListItem[]{}));
            }
        }

        if (date != null) {
            report.setStartDate(new DateNonConvertable(date));
        }
        report.setTitle(expenseAddRequest.getReport_title());
        report.setDescription(expenseAddRequest.getDescription());
        report.setReporterId(employee.getObjectID());
        report.setBaseCurrency(baseCurrency);
        report.setExchangeRate(BigDecimal.ONE);
        if (currency != null) {
            report.setExpenseCurrency(currency);
            exchangeRate = BigDecimal.valueOf(currencyServiceLocal.getCurrencyRateByDate(currency.getId(), new DateNonConvertable(new Date())).getExchangeRate());
            report.setExchangeRate(exchangeRate);
        }

        if (expenseAddRequest.getFixed_asset() != null && expenseAddRequest.getFixed_asset() > 0) {
            EdsFixedAsset edsFixedAsset = fixedAssetManager.get(expenseAddRequest.getFixed_asset());
            if (edsFixedAsset == null || edsFixedAsset.isDeleted()) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Fixed asset with id " + expenseAddRequest.getFixed_asset() + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
            } else {
                report.setFixedAsset(edsFixedAsset.getAsSelectItem());
            }
        }

        if (expenseAddRequest.getSupplier() != null && expenseAddRequest.getSupplier() > 0) {
            EdsCrmAccount edsCrmAccount = crmAccountManager.get(expenseAddRequest.getSupplier());
            if (edsCrmAccount == null || edsCrmAccount.isDeleted()) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Supplier with id " + expenseAddRequest.getSupplier() + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
            } else {
                report.setSupplier(edsCrmAccount.getAsSelectItem());
            }
        }

        if (expenseAddRequest.getRelated_project() != null && expenseAddRequest.getRelated_project() > 0) {
            EdsProject edsProject = projectManager.get(expenseAddRequest.getRelated_project());
            if (edsProject == null || edsProject.getDeleted()) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Project with id " + expenseAddRequest.getRelated_project() + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
            } else {
                report.setProject(edsProject.getAsSelectItem());
            }
        }

        if (TaxTypeEnum.getTaxTypeById(expenseAddRequest.getTax_type()) != null) {
            report.setTaxCalculationType(expenseAddRequest.getTax_type());
        }
        //Start Adding/Updating Expense Items
        if (expenseAddRequest.getItems() != null && expenseAddRequest.getItems().size() > 0) {
            ArrayList<ExpenseListItem> items = new ArrayList<>(expenseAddRequest.getItems().size());
            BigDecimal finalExchangeRate = exchangeRate;
            expenseAddRequest.getItems().forEach(itemTO -> {
                ExpenseListItem expenseItem = new ExpenseListItem();
                if (itemTO.getId() != null && itemTO.getId() > 0) {
                    EdsExpense expense = expenseManager.getExpense(itemTO.getId());
                    if (expense != null) {
                        expenseItem = expense.createExpenseListItem();
                    }
                }
                if (itemTO.getCategory() != null) {
                    expenseItem.setCategoryId(itemTO.getCategory().getId());
                    expenseItem.setAccountId(itemTO.getCategory().getId());
                    expenseItem.setCategoryName(itemTO.getCategory().getTitle());
                }
                expenseItem.setDescription(itemTO.getDescription());
                expenseItem.setUnits(itemTO.getUnits());
                expenseItem.setCostPerUnit(itemTO.getCost_per_unit());
                if (itemTO.getTax() != null) {
                    expenseItem.setTax(new TaxItem(itemTO.getTax().getId(), itemTO.getTax().getTitle(), itemTO.getTax().getPercent()));
                } else {
                    expenseItem.setTax(null);
                }
                if (itemTO.getBill_to() != null) {
                    expenseItem.setClientId(itemTO.getBill_to().getId());
                    expenseItem.setClientName(itemTO.getBill_to().getTitle());
                } else {
                    expenseItem.setClientId(null);
                    expenseItem.setClientName(null);
                }
                expenseItem.setMarkupAmount(itemTO.getMarkup_amount());

                calculateItem(expenseItem, taxCalculationType, itemTO, finalExchangeRate);

                items.add(expenseItem);

            });

            report.setItems(items.toArray(new ExpenseListItem[0]));

            calculateReport(report, exchangeRate);
        }
        //End Of Adding/Updating Expense Items

        //Start Adding/Updating Custom Fields
        LinkedHashMap<Integer, ArrayList<AttachmentTO>> customFieldDraftAttachmentMap = new LinkedHashMap<>();
        ArrayList<CompanyCustomFieldItem> customFieldItems = new ArrayList<>();

        if (expenseAddRequest.getCustom_fields() != null && expenseAddRequest.getCustom_fields().size() > 0) {
            for (Object customFieldObject : expenseAddRequest.getCustom_fields()) {
                if (customFieldObject instanceof LinkedHashMap) {
                    LinkedHashMap<Object, Object> customFieldsMap = (LinkedHashMap<Object, Object>) customFieldObject;
                    if (customFieldsMap.get("id") != null) {
                        EdsCompanyCustomFieldsSettings edsCompanyCustomFieldsSettings = null;
                        Object object = customFieldsMap.get("id");
                        if (object instanceof Integer) {
                            Integer id = (Integer) object;
                            edsCompanyCustomFieldsSettings = companyCustomFieldsManager.get(id);
                        } else if (object instanceof String) {
                            Integer id = Integer.valueOf((String) object);
                            edsCompanyCustomFieldsSettings = companyCustomFieldsManager.get(id);
                        }
                        if (edsCompanyCustomFieldsSettings != null) {
                            CompanyCustomFieldItem companyCustomFieldItem = new CompanyCustomFieldItem();
                            companyCustomFieldItem.setEntityId(edsCompanyCustomFieldsSettings.getObjectID());
                            companyCustomFieldItem.setFieldName(edsCompanyCustomFieldsSettings.getFieldName());
                            companyCustomFieldItem.setAliasName(edsCompanyCustomFieldsSettings.getAliasName());
                            companyCustomFieldItem.setColumnCode(edsCompanyCustomFieldsSettings.getColumnCode());
                            companyCustomFieldItem.setDataType(edsCompanyCustomFieldsSettings.getDataType());
                            companyCustomFieldItem.setUiType(edsCompanyCustomFieldsSettings.getUiType());

                            if (StringUtils.isNotBlank((String) customFieldsMap.get("text"))) {//for text fields

                                companyCustomFieldItem.setFieldStringValue((String) customFieldsMap.get("text"));

                            } else if (customFieldsMap.get("value") != null) {//for number fields
                                if (Constants.DATA_TYPE_NUMBER.equalsIgnoreCase(edsCompanyCustomFieldsSettings.getDataType())) {
                                    companyCustomFieldItem.setFieldStringValue(customFieldsMap.get("value").toString());
                                }

                            } else if (customFieldsMap.get("category_id") != null) {//for number fields

                                if (edsCompanyCustomFieldsSettings.getPredefinedValues() != null && edsCompanyCustomFieldsSettings.getPredefinedValues().length > 0) {
                                    String[] values = edsCompanyCustomFieldsSettings.getPredefinedValues();
                                    try {
                                        Integer index;
                                        if (customFieldsMap.get("category_id") instanceof Integer) {
                                            index = (Integer) customFieldsMap.get("category_id");
                                        } else {
                                            index = Integer.valueOf(customFieldsMap.get("category_id").toString());
                                        }

                                        if (index > 0 && index <= values.length) {
                                            companyCustomFieldItem.setFieldStringValue(values[index - 1]);
                                        }

                                    } catch (Exception e) {
                                        log.error("", e);
                                    }
                                }
                            } else if (customFieldsMap.get("choosed_ids") != null && customFieldsMap.get("choosed_ids") instanceof List) {//for number fields
                                if (edsCompanyCustomFieldsSettings.getPredefinedValues() != null && edsCompanyCustomFieldsSettings.getPredefinedValues().length > 0) {
                                    String[] values = edsCompanyCustomFieldsSettings.getPredefinedValues();
                                    try {
                                        ArrayList<Integer> indexes = new ArrayList<>();
                                        for (Object val : (List) customFieldsMap.get("choosed_ids")) {
                                            if (val instanceof Integer) {
                                                indexes.add((Integer) val);
                                            } else {
                                                indexes.add(Integer.valueOf(val.toString()));
                                            }
                                        }
                                        StringBuilder customFieldValue = new StringBuilder();
                                        for (Integer index : indexes) {
                                            if (index > 0 && index <= values.length) {
                                                customFieldValue.append(values[index - 1]).append(",");
                                            }
                                        }
                                        if (customFieldValue.length() > 1 && customFieldValue.charAt(customFieldValue.length() - 1) == ',') {
                                            customFieldValue = customFieldValue.deleteCharAt(customFieldValue.length() - 1);
                                        }
                                        companyCustomFieldItem.setFieldStringValue(customFieldValue.toString());
                                    } catch (Exception e) {
                                        log.error("", e);
                                    }
                                }
                            } else if (StringUtils.isNotBlank((String) customFieldsMap.get("date"))) {//for date fields
                                try {
//                                    companyCustomFieldItem.setFieldDateValue(longDateTimezoneFormat.parse((String) customFieldsMap.get("date")));
                                    companyCustomFieldItem.setFieldDateNonConvertedValue(new DateNonConvertable(longDateTimezoneFormat.parse((String) customFieldsMap.get("date"))));
                                } catch (ParseException e) {
                                    log.error("", e);
                                }
                            } else if (customFieldsMap.get("draft_files") != null && customFieldsMap.get("draft_files") instanceof List) {//custom field draft files
                                List<LinkedHashMap<Object, Object>> objects = (List) customFieldsMap.get("draft_files");
                                for (LinkedHashMap<Object, Object> objMap : objects) {
                                    AttachmentTO attachmentTO = new AttachmentTO();
                                    attachmentTO.setFile_name((String) objMap.get("file_name"));
                                    attachmentTO.setLink((String) objMap.get("link"));

                                    ArrayList<AttachmentTO> files = customFieldDraftAttachmentMap.get(edsCompanyCustomFieldsSettings.getObjectID()) == null ? new ArrayList<>() : customFieldDraftAttachmentMap.get(edsCompanyCustomFieldsSettings.getObjectID());
                                    files.add(attachmentTO);
                                    customFieldDraftAttachmentMap.put(edsCompanyCustomFieldsSettings.getObjectID(), files);
                                }
                            }

                            customFieldItems.add(companyCustomFieldItem);
                        }
                    }
                }
            }
        } else {
            ArrayList<CompanyCustomFieldItem> companyCustomFieldItems = commonServiceLocal.getCompanyCustomFieldsByRelationship(ViewName.ExpenceReportView, null, null);
            for (CompanyCustomFieldItem customFieldItem : companyCustomFieldItems) {
                customFieldItem.setObjectId(null);
                customFieldItems.add(customFieldItem);
            }
        }

        //Upload custom field attachments
        //if request is draft, there will not be draft attachments, but there may be files. If there are files,they should be uploaded
        FolderResource tempFolder = documentsServiceLocal.getTempFolderByCompany(employee.getCompany().getObjectID());

        if (expenseAddRequest.getId() == null) {
            if (!expenseCustomFieldAttachmentsMap.isEmpty()) {
                for (Integer customFieldId : expenseCustomFieldAttachmentsMap.keySet()) {
                    EdsCompanyCustomFieldsSettings companyCustomFieldsSettings = companyCustomFieldsManager.get(customFieldId);

                    CompanyCustomFieldItem companyCustomFieldItem = new CompanyCustomFieldItem();
                    companyCustomFieldItem.setEntityId(companyCustomFieldsSettings.getObjectID());
                    companyCustomFieldItem.setEntityName(companyCustomFieldsSettings.getEntityName());
                    companyCustomFieldItem.setFieldName(companyCustomFieldsSettings.getFieldName());
                    companyCustomFieldItem.setAliasName(companyCustomFieldsSettings.getAliasName());
                    companyCustomFieldItem.setDataType(companyCustomFieldsSettings.getDataType());
                    companyCustomFieldItem.setUiType(companyCustomFieldsSettings.getUiType());
                    companyCustomFieldItem.setColumnCode(companyCustomFieldsSettings.getColumnCode());
                    companyCustomFieldItem.setFileUploadFieldId(companyCustomFieldsSettings.getObjectID());

                    ArrayList<FileItem> attachments = new ArrayList<>();
                    for (MultipartFile multipartFile : expenseCustomFieldAttachmentsMap.get(customFieldId)) {
                        FileResource fileResource = documentsServiceLocal.saveDocumentFile(multipartFile, tempFolder.getObjectId(), Constants.F_CUSTOM_FIELD_ITEM, null, "");
                        FileItem fileItem = new FileItem();
                        fileItem.setId(fileResource.getObjectId());
                        fileItem.setFileName(fileResource.getFileName());
                        attachments.add(fileItem);
                    }
                    companyCustomFieldItem.setAttachments(attachments.toArray(new FileItem[]{}));

                    customFieldItems.add(companyCustomFieldItem);
                }
            }
            report.setCustomFieldItems(customFieldItems);
        } else {
            //Compare draft files to old files by unique keys: filename & file size. If there is a difference between them by name or size, delete the differ old files
            // but keep other non changed files

            //List<CompanyCustomFieldItem> customFieldItemList = report.getCustomFieldItems();
            //List<CompanyCustomFieldItem> customFieldItemListMerge = new ArrayList<>();
            LinkedHashMap<Integer, CompanyCustomFieldItem> customFieldItemListMap = new LinkedHashMap<>();
            ArrayList<FileResource> oldAttachments = new ArrayList<>();

            if (report.getCustomFieldItems() != null) {
                for (CompanyCustomFieldItem customFieldItem : report.getCustomFieldItems()) {
                    if (Constants.UI_TYPE_FILE_UPLOAD_ITEM.equals(customFieldItem.getUiType())) {
                        ArrayList<FileResource> list = documentsServiceLocal.getFileResources(Constants.F_CUSTOM_FIELD_ITEM, customFieldItem.getEntityId(), customFieldItem.getObjectId());
                        if (list != null && list.size() > 0) {
                            oldAttachments.addAll(list);
                        }
                    }
                    //apply new values
                    for (CompanyCustomFieldItem item : customFieldItems) {
                        if (customFieldItem.getEntityId().equals(item.getEntityId())) {
                            customFieldItem.setColumnCode(item.getColumnCode());
                            customFieldItem.setFieldStringValue(item.getFieldStringValue());
//                            customFieldItem.setFieldDateValue(item.getFieldDateValue());
                            customFieldItem.setFieldDateNonConvertedValue(item.getFieldDateNonConvertedValue());
                            break;
                        }
                    }
                    customFieldItemListMap.put(customFieldItem.getEntityId(), customFieldItem);
                    //customFieldItemListMerge.add(customFieldItem);
                }
            }
            //customFieldItemList = customFieldItemListMerge;
            //report.setCustomFieldItems(customFieldItemList);

            //if draft attachments are empty, remove all old expense attachments.
            if (customFieldDraftAttachmentMap.isEmpty()) {
                if (oldAttachments.size() > 0) {
                    List<Integer> oldAttachmentIDs = new ArrayList<>();
                    oldAttachments.forEach(fileResource -> oldAttachmentIDs.add(fileResource.getObjectId()));
                    try {
                        documentsServiceLocal.deleteFiles(oldAttachmentIDs);
                        oldAttachments.clear();
                    } catch (ObjectNotFoundException | InsufficientPermissionsException e) {
                        log.error("", e);
                    }
                }
                //if draft attachments do not match with custom field old attachments by filename and file size, delete not matched old attachments
            } else {
                if (oldAttachments.size() > 0) {
                    LinkedHashMap<String, String> draftAttachmentMap = new LinkedHashMap<>();
                    for (Integer customFieldId : customFieldDraftAttachmentMap.keySet()) {
                        for (AttachmentTO draftAttachment : customFieldDraftAttachmentMap.get(customFieldId)) {
                            draftAttachmentMap.put(draftAttachment.getFile_name(), draftAttachment.getFile_name());
                        }
                    }

                    HashSet<Integer> deleteIDs = new HashSet<>();
                    for (FileResource oldAttachment : oldAttachments) {
                        String draftFilename = draftAttachmentMap.get(oldAttachment.getFileName());
                        if (StringUtils.isNotBlank(draftFilename)) {
                            FileResource fileResource = documentsServiceLocal.getFileResourceByFileTypeAndName(Constants.F_CUSTOM_FIELD_ITEM, draftFilename);
                            if (fileResource != null && !fileResource.getContentLength().equals(oldAttachment.getContentLength())) {
                                deleteIDs.add(oldAttachment.getObjectId());
                            }
                        } else {
                            deleteIDs.add(oldAttachment.getObjectId());
                        }
                    }

                    if (deleteIDs.size() > 0) {
                        try {
                            documentsServiceLocal.deleteFiles(new ArrayList<>(deleteIDs));
                        } catch (ObjectNotFoundException | InsufficientPermissionsException e) {
                            log.error("", e);
                        }
                    }
                    //after delete old attachments, get not deleted attachments as old attachments
                    if (!customFieldItemListMap.isEmpty()) {
                        oldAttachments.clear();
                        for (CompanyCustomFieldItem customFieldItem : customFieldItemListMap.values()) {
                            if (Constants.UI_TYPE_FILE_UPLOAD_ITEM.equals(customFieldItem.getUiType())) {
                                ArrayList<FileResource> list = documentsServiceLocal.getFileResources(Constants.F_CUSTOM_FIELD_ITEM, customFieldItem.getEntityId(), customFieldItem.getObjectId());
                                if (list != null && list.size() > 0) {
                                    oldAttachments.addAll(list);
                                }
                            }
                        }
                    }
                }
            }
            /////////////////////////////////////////////////////////////////////

            try {
                if (!expenseCustomFieldAttachmentsMap.isEmpty()) {
                    //if old files are empty, upload new files
                    if (oldAttachments.size() == 0) {

                        for (Integer customFieldId : expenseCustomFieldAttachmentsMap.keySet()) {
                            EdsCompanyCustomFieldsSettings companyCustomFieldsSettings = companyCustomFieldsManager.get(customFieldId);

                            CompanyCustomFieldItem companyCustomFieldItem = new CompanyCustomFieldItem();
                            companyCustomFieldItem.setEntityId(companyCustomFieldsSettings.getObjectID());
                            companyCustomFieldItem.setEntityName(companyCustomFieldsSettings.getEntityName());
                            companyCustomFieldItem.setFieldName(companyCustomFieldsSettings.getFieldName());
                            companyCustomFieldItem.setAliasName(companyCustomFieldsSettings.getAliasName());
                            companyCustomFieldItem.setDataType(companyCustomFieldsSettings.getDataType());
                            companyCustomFieldItem.setUiType(companyCustomFieldsSettings.getUiType());
                            companyCustomFieldItem.setColumnCode(companyCustomFieldsSettings.getColumnCode());
                            companyCustomFieldItem.setFileUploadFieldId(companyCustomFieldsSettings.getObjectID());

                            ArrayList<FileItem> attachmentList = new ArrayList<>();
                            for (MultipartFile multipartFile : expenseCustomFieldAttachmentsMap.get(customFieldId)) {
                                FileResource fileResource = documentsServiceLocal.saveDocumentFile(multipartFile, tempFolder.getObjectId(), Constants.F_CUSTOM_FIELD_ITEM, null, "");
                                FileItem fileItem = new FileItem();
                                fileItem.setId(fileResource.getObjectId());
                                fileItem.setFileName(fileResource.getFileName());
                                attachmentList.add(fileItem);
                            }
                            companyCustomFieldItem.setAttachments(attachmentList.toArray(new FileItem[]{}));

                            //customFieldItems.add(companyCustomFieldItem);
                            customFieldItemListMap.put(companyCustomFieldItem.getEntityId(), companyCustomFieldItem);
                        }
                        //report.setCustomFieldItems(customFieldItems);

                    } else {//If old files aren't empty, merge old and new files
                        HashSet<Integer> deleteIDs = new HashSet<>();
                        LinkedHashMap<String, FileResource> oldFilesMap = new LinkedHashMap<>();
                        for (FileResource file : oldAttachments) {
                            oldFilesMap.put(file.getFileName(), file);
                        }
                        for (Integer customFieldId : expenseCustomFieldAttachmentsMap.keySet()) {
                            for (MultipartFile multipartFile : expenseCustomFieldAttachmentsMap.get(customFieldId)) {
                                FileResource oldFile = oldFilesMap.get(multipartFile.getOriginalFilename());
                                if (oldFile != null) {
                                    deleteIDs.add(oldFile.getObjectId());
                                    oldFilesMap.remove(multipartFile.getOriginalFilename(), oldFile);
                                }
                            }
                        }

                        if (deleteIDs.size() > 0) {
                            try {
                                documentsServiceLocal.deleteFiles(new ArrayList<>(deleteIDs));
                            } catch (ObjectNotFoundException | InsufficientPermissionsException e) {
                                log.error("", e);
                            }
                        }

                        for (Integer customFieldId : expenseCustomFieldAttachmentsMap.keySet()) {
                            EdsCompanyCustomFieldsSettings companyCustomFieldsSettings = companyCustomFieldsManager.get(customFieldId);

                            CompanyCustomFieldItem companyCustomFieldItem = new CompanyCustomFieldItem();
                            companyCustomFieldItem.setEntityId(companyCustomFieldsSettings.getObjectID());
                            companyCustomFieldItem.setEntityName(companyCustomFieldsSettings.getEntityName());
                            companyCustomFieldItem.setFieldName(companyCustomFieldsSettings.getFieldName());
                            companyCustomFieldItem.setAliasName(companyCustomFieldsSettings.getAliasName());
                            companyCustomFieldItem.setDataType(companyCustomFieldsSettings.getDataType());
                            companyCustomFieldItem.setUiType(companyCustomFieldsSettings.getUiType());
                            companyCustomFieldItem.setColumnCode(companyCustomFieldsSettings.getColumnCode());
                            companyCustomFieldItem.setFileUploadFieldId(companyCustomFieldsSettings.getObjectID());

                            ArrayList<FileItem> attachmentList = new ArrayList<>();
                            for (MultipartFile multipartFile : expenseCustomFieldAttachmentsMap.get(customFieldId)) {
                                FileResource fileResource = documentsServiceLocal.saveDocumentFile(multipartFile, tempFolder.getObjectId(), Constants.F_CUSTOM_FIELD_ITEM, null, "");
                                FileItem fileItem = new FileItem();
                                fileItem.setId(fileResource.getObjectId());
                                fileItem.setFileName(fileResource.getFileName());
                                attachmentList.add(fileItem);
                            }
                            for (FileResource fileResource : oldFilesMap.values()) {
                                FileItem fileItem = new FileItem();
                                fileItem.setId(fileResource.getObjectId());
                                fileItem.setFileName(fileResource.getFileName());
                                attachmentList.add(fileItem);
                            }

                            companyCustomFieldItem.setAttachments(attachmentList.toArray(new FileItem[]{}));

                            //customFieldItems.add(companyCustomFieldItem);
                            customFieldItemListMap.put(companyCustomFieldItem.getEntityId(), companyCustomFieldItem);
                        }
                        //report.setCustomFieldItems(customFieldItems);
                    }
                }
            } catch (Exception e) {
                log.error("", e);
            }

            ArrayList<CompanyCustomFieldItem> companyCustomFieldItems = new ArrayList<>(customFieldItemListMap.values());
            report.setCustomFieldItems(companyCustomFieldItems);
        }

        //End Adding/Updating Custom Fields

        Integer expenseId;
        try {
            expenseId = expenseServiceLocal.saveReport(report);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (expenseId > 0) {
            //Upload expense files
            mergeAndUploadExpenseFiles(expenseId, expenseAddRequest, expenseAttachments);
            //Upload expense item files
            mergeAndUploadAllExpenseItemFiles(expenseId, expenseAddRequest, expenseItemAttachmentsMap);
        }

        return successResponse(new ResponseData());
    }

    private void mergeAndUploadAllExpenseItemFiles(Integer expenseId, ExpenseAddRequestTO expenseAddRequest, TreeMap<Integer, ArrayList<MultipartFile>> expenseItemAttachmentsMap) {
        EdsExpenseReport expenseReport = expenseReportManager.getExpenseReport(expenseId);
        int index = 0;
        for (EdsExpense edsExpense : expenseReport.getExpenses()) {
            ArrayList<MultipartFile> expenseItemAttachments;
            try {
                expenseItemAttachments = expenseItemAttachmentsMap.get(index);
            } catch (Exception e) {
                expenseItemAttachments = null;
            }
            mergeAndUploadSingleExpenseItemFiles(edsExpense.getObjectID(), expenseAddRequest, expenseAddRequest.getItems().get(index).getDraft_receipts(), expenseItemAttachments);
            index++;
        }
    }

    private void mergeAndUploadExpenseFiles(Integer expenseId, ExpenseAddRequestTO expenseAddRequest, ArrayList<MultipartFile> expenseAttachments) {

        //if request is draft, there will not be draft attachments, but there may be files. If there are files,they should be uploaded
        if (expenseAddRequest.getId() == null) {
            uploadExpenseFiles(expenseId, expenseAttachments, null);
        } else {
            //Compare draft files to old files by unique keys: filename & file size. If there is a difference between them by name or size, delete the differ old files
            // but keep other non changed files
            ArrayList<FileResource> oldAttachments = documentsServiceLocal.getFileResources(Constants.F_EXP_DOC, expenseId, expenseId);
            HashSet<Integer> deleteIDs = new HashSet<>();
            //if draft attachments are empty, remove all old expense attachments.
            if (expenseAddRequest.getDraft_attachments() == null || expenseAddRequest.getDraft_attachments().size() == 0) {
                if (oldAttachments != null && oldAttachments.size() > 0) {
                    List<Integer> oldAttachmentIDs = new ArrayList<>();
                    oldAttachments.forEach(fileResource -> oldAttachmentIDs.add(fileResource.getObjectId()));
                    try {
                        documentsServiceLocal.deleteFiles(oldAttachmentIDs);
                    } catch (ObjectNotFoundException | InsufficientPermissionsException e) {
                        log.error("", e);
                    }
                }
                //if draft attachments do not match with expense old attachments by filename and file size, delete not matched old attachments
            } else if (expenseAddRequest.getDraft_attachments() != null && expenseAddRequest.getDraft_attachments().size() > 0) {
                if (oldAttachments != null && oldAttachments.size() > 0) {
                    LinkedHashMap<String, String> draftAttachmentMap = new LinkedHashMap<>();
                    expenseAddRequest.getDraft_attachments().forEach(draftAttachment -> draftAttachmentMap.put(draftAttachment.getFile_name(), draftAttachment.getFile_name()));

                    oldAttachments.forEach(oldAttachment -> {
                        String draftFilename = draftAttachmentMap.get(oldAttachment.getFileName());
                        if (StringUtils.isNotBlank(draftFilename)) {
                            FileResource fileResource = documentsServiceLocal.getFileResourceByFileTypeAndName(Constants.F_EXP_DOC, draftFilename);
                            if (fileResource != null && !fileResource.getContentLength().equals(oldAttachment.getContentLength())) {
                                deleteIDs.add(oldAttachment.getObjectId());
                            }
                        } else {
                            deleteIDs.add(oldAttachment.getObjectId());
                        }
                    });

                    if (deleteIDs.size() > 0) {
                        try {
                            documentsServiceLocal.deleteFiles(new ArrayList<>(deleteIDs));
                        } catch (ObjectNotFoundException | InsufficientPermissionsException e) {
                            log.error("", e);
                        }
                    }
                    //after delete old attachments, get not deleted attachment as old attachments
                    oldAttachments = documentsServiceLocal.getFileResources(Constants.F_EXP_DOC, expenseAddRequest.getId(), expenseAddRequest.getId());
                }
            }

            uploadExpenseFiles(expenseId, expenseAttachments, oldAttachments);
        }

    }

    private void uploadExpenseFiles(Integer expenseId, ArrayList<MultipartFile> attachments, ArrayList<FileResource> oldAttachments) {
        try {
            if (!attachments.isEmpty()) {
                //if old files are empty, upload new files
                if (oldAttachments == null || oldAttachments.size() == 0) {
                    attachments.forEach(file -> {
                        try {
                            documentsServiceLocal.saveDocumentFile(file, null, Constants.F_EXP_DOC, expenseId, "");
                        } catch (Exception e) {
                            log.error("", e);
                        }
                    });

                } else {//If old files aren't empty, merge old and new files
                    HashSet<Integer> deleteIDs = new HashSet<>();
                    LinkedHashMap<String, FileResource> oldFilesMap = new LinkedHashMap<>();
                    oldAttachments.forEach(file -> oldFilesMap.put(file.getFileName(), file));

                    attachments.forEach(multipartFile -> {
                        FileResource oldFile = oldFilesMap.get(multipartFile.getOriginalFilename());
                        if (oldFile != null) {
                            deleteIDs.add(oldFile.getObjectId());
                        }
                    });

                    if (deleteIDs.size() > 0) {
                        try {
                            documentsServiceLocal.deleteFiles(new ArrayList<>(deleteIDs));
                        } catch (ObjectNotFoundException | InsufficientPermissionsException e) {
                            log.error("", e);
                        }
                    }
                    attachments.forEach(file -> {
                        try {
                            documentsServiceLocal.saveDocumentFile(file, null, Constants.F_EXP_DOC, expenseId, "");
                        } catch (Exception e) {
                            log.error("", e);
                        }
                    });
                }
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }

    private void mergeAndUploadSingleExpenseItemFiles(Integer expenseItemId, ExpenseAddRequestTO expenseAddRequest, ArrayList<AttachmentTO> draftAttachments, ArrayList<MultipartFile> expenseItemAttachments) {

        //if request is draft, there will not be draft attachments, but there may be files. If there are files,they should be uploaded
        if (expenseAddRequest.getId() == null) {
            uploadExpenseItemFiles(expenseItemId, expenseItemAttachments, null);
        } else {
            //Compare draft files to old files by unique keys: filename & file size. If there is a difference between them by name or size, delete the differ old files
            // but keep other non changed files
            ArrayList<FileResource> oldAttachments = new ArrayList<>();
            FileResource[] oldAttachmentArray = expenseServiceLocal.getFileResources(expenseItemId);
            if (oldAttachmentArray != null && oldAttachmentArray.length > 0) {
                oldAttachments.addAll(Arrays.asList(oldAttachmentArray));
            }

            HashSet<Integer> deleteIDs = new HashSet<>();
            //if draft attachments are empty, remove all old expense attachments.
            if (draftAttachments == null || draftAttachments.size() == 0) {
                if (oldAttachments.size() > 0) {
                    List<Integer> oldAttachmentIDs = new ArrayList<>();
                    oldAttachments.forEach(fileResource -> oldAttachmentIDs.add(fileResource.getObjectId()));
                    try {
                        documentsServiceLocal.deleteFiles(oldAttachmentIDs);
                    } catch (ObjectNotFoundException | InsufficientPermissionsException e) {
                        log.error("", e);
                    }
                }
                //if draft attachments do not match with expense old attachments by filename and file size, delete not matched old attachments
            } else if (draftAttachments.size() > 0) {
                if (oldAttachments.size() > 0) {
                    LinkedHashMap<String, String> draftAttachmentMap = new LinkedHashMap<>();
                    draftAttachments.forEach(draftAttachment -> draftAttachmentMap.put(draftAttachment.getFile_name(), draftAttachment.getFile_name()));

                    oldAttachments.forEach(oldAttachment -> {
                        String draftFilename = draftAttachmentMap.get(oldAttachment.getFileName());
                        if (StringUtils.isNotBlank(draftFilename)) {
                            FileResource fileResource = documentsServiceLocal.getFileResourceByFileTypeAndName(Constants.F_EXP, draftFilename);
                            if (fileResource != null && !fileResource.getContentLength().equals(oldAttachment.getContentLength())) {
                                deleteIDs.add(oldAttachment.getObjectId());
                            }
                        } else {
                            deleteIDs.add(oldAttachment.getObjectId());
                        }
                    });

                    if (deleteIDs.size() > 0) {
                        try {
                            documentsServiceLocal.deleteFiles(new ArrayList<>(deleteIDs));
                        } catch (ObjectNotFoundException | InsufficientPermissionsException e) {
                            log.error("", e);
                        }
                    }
                    //after delete old attachments, get not deleted attachment as old attachments
                    oldAttachments = documentsServiceLocal.getFileResources(Constants.F_EXP, expenseItemId, expenseItemId);
                }
            }

            uploadExpenseItemFiles(expenseItemId, expenseItemAttachments, oldAttachments);
        }

    }

    private void uploadExpenseItemFiles(Integer expenseItemId, ArrayList<MultipartFile> attachments, ArrayList<FileResource> oldAttachments) {
        try {
            if (attachments != null && attachments.size() > 0) {
                //if old files are empty, upload new files
                if (oldAttachments == null || oldAttachments.size() == 0) {
                    for (MultipartFile file : attachments) {
                        try {
                            documentsServiceLocal.saveDocumentFile(file, null, Constants.F_EXP, expenseItemId, "");
                        } catch (Exception e) {
                            log.error("", e);
                        }
                    }
                } else {//If old files aren't empty, merge old and new files
                    HashSet<Integer> deleteIDs = new HashSet<>();
                    LinkedHashMap<String, FileResource> oldFilesMap = new LinkedHashMap<>();
                    oldAttachments.forEach(file -> oldFilesMap.put(file.getFileName(), file));

                    for (MultipartFile multipartFile : attachments) {
                        FileResource oldFile = oldFilesMap.get(multipartFile.getOriginalFilename());
                        if (oldFile != null) {
                            deleteIDs.add(oldFile.getObjectId());
                        }
                    }

                    if (deleteIDs.size() > 0) {
                        try {
                            documentsServiceLocal.deleteFiles(new ArrayList<>(deleteIDs));
                        } catch (ObjectNotFoundException | InsufficientPermissionsException e) {
                            log.error("", e);
                        }
                    }
                    for (MultipartFile file : attachments) {
                        try {
                            documentsServiceLocal.saveDocumentFile(file, null, Constants.F_EXP, expenseItemId, "");
                        } catch (Exception e) {
                            log.error("", e);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }

    private void calculateReport(ExpenseReportsListItem report, BigDecimal exchangeRateValue) {
        BigDecimal taxTotal = BigDecimal.ZERO, subTotalValue = BigDecimal.ZERO, totalAll = BigDecimal.ZERO, baseTotalAll = BigDecimal.ZERO;
        for (ExpenseListItem item : report.getItems()) {

            BigDecimal amount = item.getSubtotal();
            BigDecimal baseAmount = item.getBaseSubtotal();

            if (exchangeRateValue.compareTo(BigDecimal.ZERO) != 0) {
                taxTotal = taxTotal.add(item.getTaxAmountInTc());
            } else {
                taxTotal = taxTotal.add(item.getTaxAmountInBase());
            }

            totalAll = totalAll.add(amount);
            baseTotalAll = baseTotalAll.add(baseAmount);
            BigDecimal units = item.getUnits() != null ? item.getUnits() : BigDecimal.ZERO;
            BigDecimal cost = item.getCostPerUnit() != null ? item.getCostPerUnit() : BigDecimal.ZERO;
            BigDecimal net = units.multiply(cost);
            subTotalValue = subTotalValue.add(net.setScale(ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP));
        }

        report.setTotal(totalAll);
        report.setTaxTotal(taxTotal);

    }

    private void calculateItem(ExpenseListItem expenseItem, Integer taxCalculationType, ExpenseItemTO itemTO, BigDecimal exchangeRateValue) {
        BigDecimal units = itemTO.getUnits() != null ? itemTO.getUnits() : BigDecimal.ZERO;
        BigDecimal costPerUnit = itemTO.getCost_per_unit() != null ? itemTO.getCost_per_unit() : BigDecimal.ZERO;
        BigDecimal net = units.multiply(costPerUnit).setScale(ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
        BigDecimal baseTotal = BigDecimal.ZERO;
        BigDecimal total = net;
        BigDecimal taxInExpenseCurrency = BigDecimal.ZERO;

        if (itemTO.getTax() != null) {
            BigDecimal taxPercent = itemTO.getTax() == null ? BigDecimal.ZERO : itemTO.getTax().getPercent().setScale(ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
            if (AccountingConstants.TAX_CALCULATION_INCLUSIVE.equals(taxCalculationType)) {
                taxInExpenseCurrency = (net.multiply(taxPercent)).divide(AccountingConstants.HUNDRED.add(taxPercent), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);

            } else if (AccountingConstants.TAX_CALCULATION_EXCLUSIVE.equals(taxCalculationType)) {
                taxInExpenseCurrency = (net.multiply(taxPercent)).divide(AccountingConstants.HUNDRED, ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
                total = total.add(taxInExpenseCurrency);
            }
        }

        if (exchangeRateValue.compareTo(BigDecimal.ZERO) != 0) {
            expenseItem.setTaxAmountInTc(taxInExpenseCurrency);
            taxInExpenseCurrency = taxInExpenseCurrency.divide(exchangeRateValue, ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
            expenseItem.setTaxAmountInBase(taxInExpenseCurrency);
            baseTotal = baseTotal.add(total.divide(exchangeRateValue, ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP));
        } else {
            expenseItem.setTaxAmountInBase(BigDecimal.ZERO);
            expenseItem.setTaxAmountInTc(BigDecimal.ZERO);
        }

        expenseItem.setSubtotal(total);
        expenseItem.setBaseSubtotal(baseTotal);

    }

    private Object getCustomerOrSupplierList(boolean isCustomer) throws RestException {
        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setStart(0);
        filterParameter.setLimit(MAX_LIMIT);
        filterParameter.setAccountType(isCustomer ? CrmConstants.CUSTOMER : CrmConstants.SUPPLIER);
        filterParameter.setSearchByParent(true);

        try {
            ArrayList<SelectItem> items = allInOneServiceLocal.getCrmAccountAsSelectItem(CrmConstants.CRM_ACCOUNT_ID, filterParameter).getList();
            ArrayList<CategoryTO> resultList = new ArrayList<>();

            items.forEach((SelectItem item) -> resultList.add(new CategoryTO(item.getId(), item.getName())));

            return successResponse(new ResponseItemsListData<>(resultList));
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(ERROR_MESSAGE, e.getMessage() != null ? e.getMessage() : e.toString(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private RequestUserActionTO getUserAction(ExpenseReportsListItem expenseListItem, EdsUser user) {
        RequestUserActionTO userAction = new RequestUserActionTO();
        boolean canApprove = ServerUtils.hasPermission(PermissionConstants.HRMS_CAN_APPROVE_EXPENSE_CLAIM);
        if (canApprove && Constants.EXPENSE_SUBMITTED.equals(expenseListItem.getStatusCode())) {
            userAction.setApprove_for_all(true);
            userAction.setApprove(true);
            userAction.setReject(true);
            return userAction;
        }
        if (!Constants.EXPENSE_SUBMITTED.equals(expenseListItem.getStatusCode())) {
            userAction.setApprove_for_all(false);
            userAction.setApprove(false);
            userAction.setReject(false);
            return userAction;
        }
        if (expenseListItem.getOverallStatus() != null && !Constants.EXPENSE_SUBMITTED.equals(expenseListItem.getOverallStatus().getCode())) {
            userAction.setApprove_for_all(false);
            userAction.setApprove(false);
            userAction.setReject(false);
            return userAction;
        }
        //if current user is one of the approvers
        if (expenseListItem.getCurrentApproverEmployeeID() != null && user.getObjectID().equals(expenseListItem.getCurrentApproverEmployeeID())) {
            userAction.setApprove_for_all(false);
            userAction.setApprove(true);
            userAction.setReject(true);
            return userAction;
        }
        //if current user is not one of the approvers but the user has permission
        if (expenseListItem.getCurrentApproverEmployeeID() != null && !user.getObjectID().equals(expenseListItem.getCurrentApproverEmployeeID())) {
            userAction.setApprove_for_all(canApprove);
            userAction.setApprove(canApprove);
            userAction.setReject(canApprove);
            return userAction;
        }

        return userAction;
    }

    @Operation(summary = "Zatca Company settings", description = "Zatca Company settings")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of Fixed Assets")})
    @RequestMapping(value = "/xml/zatca/settings", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Object initZatcaSettings() throws RestException {
        try {
            zatcaService.initSettings();
        } catch (ZatcaException e) {
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage() != null ? e.getMessage() : e.toString(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return successResponse(new ResponseData());
    }

    @Operation(summary = "Zatca Invoice generator", description = "Xml generator for zatca")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of Fixed Assets")})
    @RequestMapping(value = "/xml/zatca/compliance/invoice/{invoiceId}", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Object getZatcaXmlComplianceInvoice(@PathVariable("invoiceId") Integer invoiceId, @RequestParam String xmlType) throws RestException {
        if (ServerUtils.isNullOrEmpty(xmlType)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Xml type is not be null. It need to be one of TAX_INVOICE, CREDIT_NOTE or DEBIT_NOTE ", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        try {
            zatcaService.complianceInvoice(invoiceId, xmlType);
        } catch (ZatcaException e) {
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage() != null ? e.getMessage() : e.toString(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return successResponse(new ResponseData());
    }

    @Operation(summary = "Zatca Invoice generator", description = "Xml generator for zatca")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of Fixed Assets")})
    @RequestMapping(value = "/xml/zatca/invoice/{invoiceId}", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Object getZatcaXmlInvoice(@PathVariable("invoiceId") Integer invoiceId, @RequestParam String xmlType) throws RestException {
        if (ServerUtils.isNullOrEmpty(xmlType)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Xml type is not be null. It need to be one of TAX_INVOICE, CREDIT_NOTE or DEBIT_NOTE ", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        try {
            zatcaService.clearanceInvoice(invoiceId, xmlType);
        } catch (ZatcaException e) {
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage() != null ? e.getMessage() : e.toString(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return successResponse(new ResponseData());
    }


}
