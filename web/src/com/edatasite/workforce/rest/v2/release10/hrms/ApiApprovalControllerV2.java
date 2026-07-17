package com.edatasite.workforce.rest.v2.release10.hrms;

import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsSickRequest;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.approving.EdsApprover;
import com.edatasite.workforce.core.domain.payrolluk.EdsAdditionalPayment;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.availability.client.rpc.BenefitRequestItem;
import com.edatasite.workforce.gwt.availability.server.app.AvailabilityServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.CurrencyServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItemMini;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.view.CashAdvanceItem;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.SickRequestManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.AdditionalPaymentManager;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseReportsListItem;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseServiceLocal;
import com.edatasite.workforce.gwt.payroll.client.rpc.AdditionalPayment;
import com.edatasite.workforce.gwt.payroll.server.app.PayrollServiceLocal;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.helpers.ListingFilterHelper;
import com.edatasite.workforce.rest.v2.release10.accounting.ApiExpensesControllerV2;
import com.edatasite.workforce.rest.v2.release10.core.BaseApiControllerV2;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ApproverListStatusTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.CurrencyValueTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.FromValueTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.ApprovalCashAdvanceTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.ApprovalExpensesClaimTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.ApprovalLeaveRequestTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.ApprovalRequestsTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.ApprovalsListResultTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.ApprovalsListTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.leaverequest.DateRangeTO;
import com.edatasite.workforce.rest.v2.release10.enums.ApprovalStatusEnum;
import com.edatasite.workforce.rest.v2.release10.enums.OtherRequestEnum;
import com.edatasite.workforce.rest.v2.release10.enums.RequestStatusEnum;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v2.release10.payroll.ApiCashAdvanceControllerV2;
import com.edatasite.workforce.rest.v2.release10.utils.ApiUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Tag(name = "Approvals", description = "Approvals API")
@RestController
@RequestMapping(headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
public class ApiApprovalControllerV2 extends BaseApiControllerV2 {
    private static final Logger log = LoggerFactory.getLogger(ApiApprovalControllerV2.class);

    @Autowired
    private AvailabilityServiceLocal availabilityServiceLocal;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private SickRequestManager sickRequestManager;
    @Autowired
    private HttpServletRequest servletRequest;
    @Autowired
    private ExpenseServiceLocal expenseServiceLocal;
    @Autowired
    private PayrollServiceLocal payrollServiceLocal;
    @Autowired
    private CurrencyServiceLocal currencyServiceLocal;
    @Autowired
    private AdditionalPaymentManager additionalPaymentManager;


    @Operation(summary = "Get Approval List", description = "Retrieves requests which current user can approve or reject")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of request that current user can approve or reject"),
            @ApiResponse(responseCode = "401", description = "Session expired")})
    @RequestMapping(value = "/approvals", method = RequestMethod.GET)
    public Object getApprovalsList() throws RestException {
        EdsUser user = userManager.getUser();
        SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);
        String baseCurrency = currencyServiceLocal.getBaseCurrency().getName();

        Integer calculationScale = ServerUtils.getCalculationScale();
        //Final result to return

        ArrayList<ApprovalsListTO> approvals = new ArrayList<>();
        List<EdsReference> referenceList = referenceManager.listReferences(Constants.REQUEST_TYPE);
        boolean isReferenceValid = referenceList != null && referenceList.size() > 0;
        //Leave Request
        if (isReferenceValid) {
            referenceList.forEach(reference -> {
                if (Constants.LEAVE_REQUEST.equals(reference.getCode())) {
                    ApprovalsListTO approvalList = new ApprovalsListTO();
                    approvalList.setId(reference.getObjectID());
                    approvalList.setTitle(reference.getName());
                    approvalList.setOrder_id(reference.getSorder());
                    approvalList.setRequest_type(ApprovalStatusEnum.LEAVE.getStatus());

                    ListingFilterParameter leaveFilterParam = new ListingFilterParameter();
                    leaveFilterParam.setStart(0);
                    leaveFilterParam.setLimit(30);
                    leaveFilterParam.setStatusCode(EdsSickRequest.APPROVED);
                    leaveFilterParam.setFromMobile(true);
                    leaveFilterParam.setApproverID(user.getObjectID());

                    List<EdsSickRequest> edsSickRequests = null;
                    try {
                        edsSickRequests = sickRequestManager.getLeaveRequestList(leaveFilterParam);
                    } catch (Exception e) {
                        log.error("", e);
                    }
                    ArrayList<ApprovalLeaveRequestTO> requests = new ArrayList<>();
                    if (edsSickRequests != null && edsSickRequests.size() > 0) {
                        edsSickRequests.forEach(edsSickRequest -> {
                            ApprovalLeaveRequestTO request = new ApprovalLeaveRequestTO();
                            request.setId(edsSickRequest.getObjectID());
                            if (edsSickRequest.getLeaveReason() != null) {
                                request.setTitle(edsSickRequest.getLeaveReason().getName());
                            }
                            if (edsSickRequest.getEmployee() != null) {
                                request.setRequester(edsSickRequest.getEmployee().getName());
                            }
                            if (StringUtils.isNotBlank(edsSickRequest.getDescription())) {
                                request.setDescription(edsSickRequest.getDescription());
                            }
                            DateRangeTO date_range = new DateRangeTO();
                            if (edsSickRequest.getStartDate() != null) {
                                date_range.setFrom(longDateTimezoneFormat.format(edsSickRequest.getStartDate()));
                            }
                            if (edsSickRequest.getEndDate() != null) {
                                date_range.setTo(longDateTimezoneFormat.format(edsSickRequest.getEndDate()));
                            }
                            request.setDate_range(date_range);
                            String[] allowances = availabilityServiceLocal.getLeaveRequestStats(edsSickRequest);
                            if (StringUtils.isNotBlank(allowances[2])) {
                                Integer paidDays = Double.valueOf(allowances[2]).intValue();
                                if (paidDays > 0) {
                                    request.setDays_paid(paidDays);
                                }
                            }
                            if (StringUtils.isNotBlank(allowances[3])) {
                                Integer daysNotPaid = Double.valueOf(allowances[3]).intValue();
                                if (daysNotPaid > 0) {
                                    request.setDays_not_paid(daysNotPaid);
                                }
                            }
                            List<EdsApprover> approvers = edsSickRequest.getApprovers();
                            if (approvers != null && approvers.size() > 0) {
                                ApproverListStatusTO leaveStatus = new ApproverListStatusTO();
                                //if only one approver
                                if (approvers.size() == 1 && approvers.get(0).getStatus() != null) {
                                    if (EdsSickRequest.APPROVED.equals(approvers.get(0).getStatus().getCode())) {
                                        leaveStatus.setType(RequestStatusEnum.APPROVED.getStatus());
                                    } else if (EdsSickRequest.DENIED.equals(approvers.get(0).getStatus().getCode())) {
                                        leaveStatus.setType(RequestStatusEnum.DECLINED.getStatus());
                                    } else if (EdsSickRequest.NOT_DEFINED.equals(approvers.get(0).getStatus().getCode())) {
                                        leaveStatus.setType(RequestStatusEnum.PENDING.getStatus());
                                    }
                                } else {
                                    //Means there are more than one approvers and we must set statuses based on them
                                    FromValueTO dataTO = new FromValueTO();
                                    dataTO.setFrom(approvers.size());
                                    dataTO.setValue(0);
                                    for (EdsApprover approver : approvers) {
                                        if (approver.getStatus() == null) {
                                            continue;//todo
                                        }
                                        if (EdsSickRequest.APPROVED.equals(approver.getStatus().getCode())) {
                                            dataTO.setValue(dataTO.getValue() + 1);
                                        }
                                    }
                                    leaveStatus.setData(dataTO);
                                    if (edsSickRequest.getOverallStatus() != null && EdsSickRequest.APPROVED.equals(edsSickRequest.getOverallStatus().getCode())) {
                                        leaveStatus.setType(RequestStatusEnum.APPROVED.getStatus());
                                    } else if (edsSickRequest.getOverallStatus() != null && EdsSickRequest.DENIED.equals(edsSickRequest.getOverallStatus().getCode())) {
                                        leaveStatus.setType(RequestStatusEnum.DECLINED.getStatus());
                                    } else {
                                        if (dataTO.getValue() == 0) {
                                            leaveStatus.setType(RequestStatusEnum.PENDING.getStatus());
                                        } else if (dataTO.getFrom().intValue() == dataTO.getValue().intValue()) {
                                            leaveStatus.setType(RequestStatusEnum.APPROVED.getStatus());
                                        } else if (dataTO.getFrom() > dataTO.getValue() && dataTO.getValue() > 0) {
                                            leaveStatus.setType(RequestStatusEnum.PARTIALLY_APPROVED.getStatus());
                                        }
                                    }
                                }
                                request.setStatus(leaveStatus);
                            }

                            requests.add(request);
                        });
                    }
                    approvalList.setRequests(requests);
                    approvals.add(approvalList);

                } else if (Constants.BENEFIT_REQUEST.equals(reference.getCode())) {
                    ApprovalsListTO approvalList = new ApprovalsListTO();
                    approvalList.setId(reference.getObjectID());
                    approvalList.setTitle(reference.getName());
                    approvalList.setOrder_id(reference.getSorder());
                    approvalList.setRequest_type(ApprovalStatusEnum.BENEFIT.getStatus());

                    ListingFilterParameter benefitFilterParameter = new ListingFilterParameter();
                    benefitFilterParameter.setStart(0);
                    benefitFilterParameter.setLimit(30);
                    benefitFilterParameter.setStatusCode(Constants.BR_APPROVED);
                    benefitFilterParameter.setSortField(BenefitRequestItem.DATE);
                    benefitFilterParameter.setAscending(true);
                    ListResult<BenefitRequestItem> benefitRequestResultList = null;
                    try {
                        benefitRequestResultList = availabilityServiceLocal.getBenefitRequestList(benefitFilterParameter);
                    } catch (Exception e) {
                        log.error("", e);
                    }
                    ArrayList<ApprovalRequestsTO> requests = new ArrayList<>();
                    if (benefitRequestResultList != null && benefitRequestResultList.getList() != null) {
                        benefitRequestResultList.getList().forEach(benefitRequestItem -> {
                            if (user.getObjectID().equals(benefitRequestItem.getApproverID())) {
                                ApprovalRequestsTO request = new ApprovalRequestsTO();
                                request.setId(benefitRequestItem.getObjectID());
                                request.setTitle(benefitRequestItem.getBenefitName());
                                if (benefitRequestItem.getApprover() != null) {
                                    if (benefitRequestItem.getApproverID() != null) {
                                        request.setApprover(benefitRequestItem.getApprover());
                                    }
                                }
                                request.setRequester(benefitRequestItem.getRequester());
                                if (StringUtils.isNotBlank(benefitRequestItem.getDescription())) {
                                    request.setDescription(benefitRequestItem.getDescription());
                                }
                                request.setStatus(ApiBenefitRequestControllerV2.getStatus(benefitRequestItem.getStatus()));

                                requests.add(request);
                            }
                        });
                    }
                    approvalList.setRequests(requests);
                    approvals.add(approvalList);

                } else if (Constants.EXPENSES_CLAIM.equals(reference.getCode())) {
                    ApprovalsListTO approvalList = new ApprovalsListTO();
                    approvalList.setId(reference.getObjectID());
                    approvalList.setTitle(reference.getName());
                    approvalList.setOrder_id(reference.getSorder());
                    approvalList.setRequest_type(ApprovalStatusEnum.EXPENSES_CLAIM.getStatus());

                    ListingFilterParameter expenseClaimFilter = ListingFilterHelper.createFilterParameter(servletRequest, ListPanelType.ExpenceReportListPanel);
                    expenseClaimFilter.setSortField(AccountingConstants.PERIOD_COLUMN);
                    expenseClaimFilter.setStatusCode(Constants.EXPENSE_APPROVED);
                    expenseClaimFilter.setAccessEnabled(false);
                    expenseClaimFilter.setFromMobile(true);
                    expenseClaimFilter.setStart(0);
                    expenseClaimFilter.setLimit(30);
                    expenseClaimFilter.setSortField(AccountingConstants.PERIOD_COLUMN);
                    expenseClaimFilter.setAscending(false);
                    ListResult<ExpenseReportsListItem> expensesList = null;
                    try {
                        expensesList = expenseServiceLocal.getExpenseReportsDataFromSolr(expenseClaimFilter);
                    } catch (Exception e) {
                        log.error("", e);
                    }
                    ArrayList<ApprovalExpensesClaimTO> requests = new ArrayList<>();
                    if (expensesList != null && expensesList.getList() != null && expensesList.getList().size() > 0) {
                        expensesList.getList().forEach(expense -> {
                            ApprovalExpensesClaimTO request = new ApprovalExpensesClaimTO();
                            request.setId(expense.getId());
                            request.setTitle(expense.getTitle());
                            if (expense.getStartDate() != null && expense.getStartDate().getDate() != null) {
                                request.setDate(longDateTimezoneFormat.format(expense.getStartDate().getDate()));
                            }
                            SelectItem statusItem = new SelectItem();
                            statusItem.setCode(expense.getStatusCode());
                            statusItem.setName(expense.getStatus());
                            request.setStatus(ApiExpensesControllerV2.getStatus(statusItem));

                            List<ApproverItemMini> expenseApprovers = expense.getApprovers();
                            if (Constants.EXPENSE_SUBMITTED.equals(expense.getStatusCode())) {
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
                                        if (expense.getStatusCode() != null && Constants.EXPENSE_APPROVED.equals(expense.getStatusCode())) {
                                            expenseStatus.setType(RequestStatusEnum.APPROVED.getStatus());
                                        } else if (expense.getStatusCode() != null && Constants.EXPENSE_DECLINED.equals(expense.getStatusCode())) {
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
                                    request.setStatus(expenseStatus);
                                }
                            }

                            if (expense.getExpenseNumber() != null) {
                                request.setNumber(expense.getExpenseNumber());
                            }
                            if (expense.getApproverSelectItem() != null) {
                                request.setApprover(expense.getApproverSelectItem().getName());
                            }
                            if (StringUtils.isNotBlank(expense.getReporterName())) {
                                request.setReporter(expense.getReporterName());
                            }
                            String currencyCode;
                            if (expense.getExpenseCurrency() != null) {
                                currencyCode = expense.getExpenseCurrency().getName();
                            } else if (expense.getBaseCurrency() != null) {
                                currencyCode = expense.getBaseCurrency().getName();
                            } else {
                                currencyCode = baseCurrency;
                            }

                            //Return only if its greater than zero
                            if (expense.getTotal() != null && expense.getTotal().compareTo(BigDecimal.ZERO) > 0) {
                                request.setOriginal(new CurrencyValueTO(expense.getTotal(), currencyCode));
                            }
                            //Return only if its greater than zero
                            if (expense.getDueTotal() != null && expense.getDueTotal().compareTo(BigDecimal.ZERO) > 0) {
                                request.setDue(new CurrencyValueTO(expense.getDueTotal(), currencyCode));
                            }

                            requests.add(request);
                        });
                    }
                    approvalList.setRequests(requests);
                    approvals.add(approvalList);

                } else if (Constants.CASH_ADVANCED.equals(reference.getCode())) {
                    ApprovalsListTO approvalList = new ApprovalsListTO();
                    approvalList.setId(reference.getObjectID());
                    approvalList.setTitle(reference.getName());
                    approvalList.setOrder_id(reference.getSorder());
                    approvalList.setRequest_type(ApprovalStatusEnum.CASH_ADVANCED.getStatus());

                    ListingFilterParameter cashAdvanceFilter = new ListingFilterParameter();
                    cashAdvanceFilter.setStatusCode(Constants.SUBMITTED_TO_MANAGER);
                    cashAdvanceFilter.setFromMobile(true);
                    cashAdvanceFilter.setStart(0);
                    cashAdvanceFilter.setLimit(30);
                    cashAdvanceFilter.setSortField("date");
                    cashAdvanceFilter.setAscending(false);

                    ListResult<CashAdvanceItem> cashAdvanceItemListResult = null;
                    try {
                        cashAdvanceItemListResult = payrollServiceLocal.getCashAdvanceList(cashAdvanceFilter);
                    } catch (Exception e) {
                        log.error("", e);
                    }
                    ArrayList<ApprovalCashAdvanceTO> requests = new ArrayList<>();
                    if (cashAdvanceItemListResult != null && cashAdvanceItemListResult.getList() != null) {
                        cashAdvanceItemListResult.getList().forEach(cashAdvanceItem -> {
                            ApprovalCashAdvanceTO request = new ApprovalCashAdvanceTO();
                            request.setId(cashAdvanceItem.getObjectID());

                            request.setStatus(ApiCashAdvanceControllerV2.getStatus(cashAdvanceItem.getStatus()));

                            List<ApproverItemMini> approvers = cashAdvanceItem.getApprovers();
                            if (cashAdvanceItem.getStatus() != null && Constants.SUBMITTED_TO_MANAGER.equals(cashAdvanceItem.getStatus().getCode())) {
                                if (approvers != null && approvers.size() > 0) {
                                    ApproverListStatusTO cashAdvanceStatus = new ApproverListStatusTO();
                                    if (approvers.size() == 1 && approvers.get(0).getStatus() != null) {
                                        if (Constants.APPROVED.equals(approvers.get(0).getStatus().getCode())) {
                                            cashAdvanceStatus.setType(RequestStatusEnum.APPROVED.getStatus());
                                        } else if (Constants.REJECTED.equals(approvers.get(0).getStatus().getCode())) {
                                            cashAdvanceStatus.setType(RequestStatusEnum.DECLINED.getStatus());
                                        } else if (Constants.SUBMITTED_TO_MANAGER.equals(approvers.get(0).getStatus().getCode())) {
                                            cashAdvanceStatus.setType(RequestStatusEnum.PENDING.getStatus());
                                        }
                                    } else {
                                        //Means there are more than one approvers and we must set statuses based on them
                                        FromValueTO dataTO = new FromValueTO();
                                        dataTO.setFrom(approvers.size());
                                        dataTO.setValue(0);
                                        for (ApproverItemMini approver : approvers) {
                                            if (approver.getStatus() == null) {
                                                continue;//todo
                                            }
                                            if (Constants.APPROVED.equals(approver.getStatus().getCode())) {
                                                dataTO.setValue(dataTO.getValue() + 1);
                                            }
                                        }
                                        cashAdvanceStatus.setData(dataTO);
                                        if (cashAdvanceItem.getStatus() != null && Constants.APPROVED.equals(cashAdvanceItem.getStatus().getCode())) {
                                            cashAdvanceStatus.setType(RequestStatusEnum.APPROVED.getStatus());
                                        } else if (cashAdvanceItem.getStatus() != null && Constants.REJECTED.equals(cashAdvanceItem.getStatus().getCode())) {
                                            cashAdvanceStatus.setType(RequestStatusEnum.DECLINED.getStatus());
                                        } else {
                                            if (dataTO.getValue() == 0) {
                                                cashAdvanceStatus.setType(RequestStatusEnum.PENDING.getStatus());
                                            } else if (dataTO.getFrom().intValue() == dataTO.getValue().intValue()) {
                                                cashAdvanceStatus.setType(RequestStatusEnum.APPROVED.getStatus());
                                            } else if (dataTO.getFrom() > dataTO.getValue() && dataTO.getValue() > 0) {
                                                cashAdvanceStatus.setType(RequestStatusEnum.PARTIALLY_APPROVED.getStatus());
                                            }
                                        }
                                    }
                                    request.setStatus(cashAdvanceStatus);
                                }
                            }

                            if (StringUtils.isNotBlank(cashAdvanceItem.getEmployeeName())) {
                                request.setRequester(cashAdvanceItem.getEmployeeName());
                            }
                            if (cashAdvanceItem.getApprover() != null) {
                                request.setApprover(cashAdvanceItem.getApprover().getName());
                            }
                            String currencyCode = cashAdvanceItem.getCurrency() != null ? cashAdvanceItem.getCurrency().getName() : baseCurrency;
                            if (ApiUtils.getTotal(cashAdvanceItem.getTotalAmount()) != null) {
                                request.setRequested_amount(new CurrencyValueTO(cashAdvanceItem.getTotalAmount().setScale(calculationScale, RoundingMode.HALF_UP), currencyCode));
                            }
                            requests.add(request);
                        });
                    }
                    approvalList.setRequests(requests);
                    approvals.add(approvalList);

                } else if (Constants.OTHER_REQUEST.equals(reference.getCode())) {
                    ApprovalsListTO paymentList = new ApprovalsListTO();
                    paymentList.setId(reference.getObjectID());
                    paymentList.setTitle(reference.getName());
                    paymentList.setOrder_id(reference.getSorder());
                    paymentList.setRequest_type(ApprovalStatusEnum.OTHER.getStatus());

                    // Additional Payment
                    ListingFilterParameter additionalPaymentsFilterParameter = ListingFilterHelper.createFilterParameter(servletRequest, ListPanelType.AdditionalPayment);
                    additionalPaymentsFilterParameter.setStart(0);
                    additionalPaymentsFilterParameter.setLimit(30);
                    additionalPaymentsFilterParameter.setAscending(false);
                    additionalPaymentsFilterParameter.setStatusCode(Constants.PAYMENT_STATUS_APPROVED);
                    additionalPaymentsFilterParameter.setFromMobile(true);
                    additionalPaymentsFilterParameter.setSortField("period");
                    additionalPaymentsFilterParameter.setAscending(false);
                    ListResult<AdditionalPayment> additionalPaymentsList = null;
                    try {
                        additionalPaymentsList = payrollServiceLocal.getAdditionalPaymentList(additionalPaymentsFilterParameter);
                    } catch (Exception e) {
                        log.error("", e);
                    }
                    ArrayList<ApprovalRequestsTO> paymentRequests = new ArrayList<>();
                    if (additionalPaymentsList != null && additionalPaymentsList.getList() != null) {
                        additionalPaymentsList.getList().forEach(additionalPayment -> {
                            ApprovalRequestsTO request = new ApprovalRequestsTO();
                            request.setId(additionalPayment.getObjectID());
                            request.setType(OtherRequestEnum.ADDITIONAL_PAYMENT.getStatus());
                            request.setTitle(additionalPayment.getReference());
                            if (additionalPayment.getApprover() != null) {
                                request.setApprover(additionalPayment.getApprover().getName());
                            }
                            if (additionalPayment.getCreator() != null) {
                                request.setRequester(additionalPayment.getCreator().getName());
                            }
                            //creating description
                            StringBuilder description = new StringBuilder();
                            if (StringUtils.isNotBlank(additionalPayment.getMonth())) {
                                description.append(additionalPayment.getMonth());
                            }
                            if (additionalPayment.getYear() != null) {
                                description.append("-").append(additionalPayment.getYear());
                            }
                            if (StringUtils.isNotBlank(additionalPayment.getReference())) {
                                description.append(" ").append(additionalPayment.getReference());
                            }
                            if (description.length() > 144) {
                                request.setDescription(description.substring(0, 141) + "...");
                            } else {
                                request.setDescription(description.toString());
                            }
                            SelectItem selectItem = new SelectItem();
                            selectItem.setCode(additionalPayment.getStatusCode());
                            selectItem.setName(additionalPayment.getStatus());
                            request.setStatus(ApiOtherRequestControllerV2.getStatus(additionalPayment.getOverallStatus()));

                            EdsAdditionalPayment edsAdditionalPayment = additionalPaymentManager.get(additionalPayment.getObjectID());
                            List<EdsApprover> additionalPaymentApprovers = edsAdditionalPayment != null ? edsAdditionalPayment.getApprovers() : new ArrayList<>();
                            if (Constants.PAYMENT_STATUS_SUBMITTED.equals(additionalPayment.getStatusCode())) {
                                if (additionalPaymentApprovers != null && additionalPaymentApprovers.size() > 0) {
                                    ApproverListStatusTO paymentStatus = new ApproverListStatusTO();
                                    if (additionalPaymentApprovers.size() == 1 && additionalPaymentApprovers.get(0).getStatus() != null) {
                                        if (Constants.PAYMENT_STATUS_APPROVED.equals(additionalPaymentApprovers.get(0).getStatus().getCode())) {
                                            paymentStatus.setType(RequestStatusEnum.APPROVED.getStatus());
                                        } else if (Constants.PAYMENT_STATUS_REJECTED.equals(additionalPaymentApprovers.get(0).getStatus().getCode())) {
                                            paymentStatus.setType(RequestStatusEnum.DECLINED.getStatus());
                                        } else if (Constants.PAYMENT_STATUS_SUBMITTED.equals(additionalPaymentApprovers.get(0).getStatus().getCode())) {
                                            paymentStatus.setType(RequestStatusEnum.PENDING.getStatus());
                                        }
                                    } else {
                                        //Means there are more than one approvers and we must set statuses based on them
                                        FromValueTO dataTO = new FromValueTO();
                                        dataTO.setFrom(additionalPaymentApprovers.size());
                                        dataTO.setValue(0);
                                        for (EdsApprover approver : additionalPaymentApprovers) {
                                            if (approver.getStatus() == null) {
                                                continue;
                                            }
                                            if (Constants.PAYMENT_STATUS_APPROVED.equals(approver.getStatus().getCode())) {
                                                dataTO.setValue(dataTO.getValue() + 1);
                                            }
                                        }
                                        paymentStatus.setData(dataTO);
                                        if (additionalPayment.getStatusCode() != null && Constants.PAYMENT_STATUS_APPROVED.equals(additionalPayment.getStatusCode())) {
                                            paymentStatus.setType(RequestStatusEnum.APPROVED.getStatus());
                                        } else if (additionalPayment.getOverallStatus() != null && Constants.PAYMENT_STATUS_REJECTED.equals(additionalPayment.getStatusCode())) {
                                            paymentStatus.setType(RequestStatusEnum.DECLINED.getStatus());
                                        } else {
                                            if (dataTO.getValue() == 0) {
                                                paymentStatus.setType(RequestStatusEnum.PENDING.getStatus());
                                            } else if (dataTO.getFrom().intValue() == dataTO.getValue().intValue()) {
                                                paymentStatus.setType(RequestStatusEnum.APPROVED.getStatus());
                                            } else if (dataTO.getFrom() > dataTO.getValue() && dataTO.getValue() > 0) {
                                                paymentStatus.setType(RequestStatusEnum.PARTIALLY_APPROVED.getStatus());
                                            }
                                        }
                                    }
                                    request.setStatus(paymentStatus);
                                }
                            }
                            paymentRequests.add(request);
                        });
                    }

                    paymentList.setRequests(paymentRequests);
                    approvals.add(paymentList);

                }
            });
        }
        return successResponse(new ApprovalsListResultTO(approvals));
    }

}
