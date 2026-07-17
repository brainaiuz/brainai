package com.edatasite.workforce.rest.v2.release10.payroll;

import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayslipTableItem;
import com.edatasite.workforce.gwt.core.client.PayslipItemFilter;
import com.edatasite.workforce.gwt.core.client.rpc.CurrencyServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.ExpenseData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionObject;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.payroll.PayslipTableItemManager;
import com.edatasite.workforce.gwt.hrms.server.app.HrmsServiceLocal;
import com.edatasite.workforce.gwt.payroll.client.rpc.SinglePayrunItem;
import com.edatasite.workforce.gwt.payroll.server.app.PayrollServiceLocal;
import com.edatasite.workforce.rest.aspects.CheckPermission;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.to.UserTO;
import com.edatasite.workforce.rest.v2.release10.core.BaseApiControllerV2;
import com.edatasite.workforce.rest.v2.release10.core.to.base.CurrencyValueTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.StatusTO;
import com.edatasite.workforce.rest.v2.release10.core.to.payroll.PayslipDetailsTO;
import com.edatasite.workforce.rest.v2.release10.core.to.payroll.PayslipItemTableTO;
import com.edatasite.workforce.rest.v2.release10.core.to.payroll.PayslipListItemTO;
import com.edatasite.workforce.rest.v2.release10.core.to.payroll.PayslipListResultTO;
import com.edatasite.workforce.rest.v2.release10.core.to.payroll.PayslipRecordTO;
import com.edatasite.workforce.rest.v2.release10.core.to.payroll.PayslipTotalTO;
import com.edatasite.workforce.rest.v2.release10.enums.DiscountTypeEnum;
import com.edatasite.workforce.rest.v2.release10.enums.RequestStatusEnum;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v2.release10.utils.ApiUtils;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Abdurakhmonov Farrukh on 11/28/2017.
 */

@Tag(name = "Payslip", description = "Payslip API")
@RestController
@RequestMapping(headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiPayslipControllerV2 extends BaseApiControllerV2 {

    private static final Logger log = LoggerFactory.getLogger(ApiPayslipControllerV2.class);
    @Autowired
    private PayslipTableItemManager payslipTableItemManager;
    @Autowired
    private PayrollServiceLocal payrollServiceLocal;
    @Autowired
    private CurrencyServiceLocal currencyServiceLocal;
    @Autowired
    private HrmsServiceLocal hrmsServiceLocal;

    @Operation(summary = "Get Payslip List", description = "Retrieves Payslip list by year.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have Payslip List."),
            @ApiResponse(responseCode = "400", description = "year field is required")})
    @RequestMapping(value = "/payments/payslips", method = RequestMethod.GET)
    @CheckPermission(permissions = {PermissionConstants.PAYROLL_MAIN_MENU, PermissionConstants.PAYROLL_PAYSLIP_LIST})
    public Object getPayslipList(@RequestParam(value = "year") Integer year) throws RestException {
        if (year == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "year field is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setEmployeeId(userManager.getUser().getObjectID());
        filterParameter.setSelectedYear(year);
        filterParameter.setStatusCode(Constants.PAYRUN_STATUS_APPROVED);

        Integer calculationScale = ServerUtils.getCalculationScale();

        List<Object[]> payslipObjects = payslipTableItemManager.getPayslipApiList(filterParameter);
        ArrayList<PayslipListItemTO> payslipListItemList = new ArrayList<>();
        if (payslipObjects != null && payslipObjects.size() > 0) {
            SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);
            String baseCurrency = currencyServiceLocal.getBaseCurrency().getName();
            for (Object[] objects : payslipObjects) {
                String currency = (String) objects[4] != null ? (String) objects[4] : baseCurrency;
                PayslipListItemTO payslipListItem = new PayslipListItemTO();
                payslipListItem.setId((Integer) objects[0]);
                if (objects[1] != null) {
                    try {
                        payslipListItem.setDate(longDateTimezoneFormat.format((Date) objects[1]));
                    } catch (Exception ignored) {
                    }
                }

                SelectItem statusItem = new SelectItem();
                statusItem.setCode((String) objects[2]);
                statusItem.setName((String) objects[3]);
                payslipListItem.setStatus(getStatus(statusItem));

                if (ApiUtils.getTotal((BigDecimal) objects[5]) != null) {
                    payslipListItem.setPayments(new CurrencyValueTO(((BigDecimal) objects[5]).setScale(calculationScale, RoundingMode.HALF_UP), currency));
                } /*else {
                    payslipListItem.setPayments(new CurrencyValueTO());
                }*/
                if (ApiUtils.getTotal((BigDecimal) objects[6]) != null) {
                    payslipListItem.setDeductions(new CurrencyValueTO(((BigDecimal) objects[6]).setScale(calculationScale, RoundingMode.HALF_UP), currency));
                } /*else {
                    payslipListItem.setDeductions(new CurrencyValueTO());
                }*/
                if (ApiUtils.getTotal((BigDecimal) objects[8]) != null) {
                    payslipListItem.setTotal(new CurrencyValueTO(((BigDecimal) objects[8]).setScale(calculationScale, RoundingMode.HALF_UP), currency));
                } /*else {
                    payslipListItem.setTotal(new CurrencyValueTO());
                }*/

                UserTO employee = new UserTO();
                if (objects[9] != null) {
                    EdsUser user = userManager.get((Integer) objects[9]);
                    employee.setId(user.getObjectID());
                    employee.setFirstName(user.getFirstName());
                    employee.setLastName(user.getLastName());
                    employee.setImageUrl(hrmsServiceLocal.getUserImageUrl(userManager.get(employee.getId())));
                }

                UserTO approver = new UserTO();
                if (objects[10] != null) {
                    EdsUser user = userManager.get((Integer) objects[10]);
                    approver.setId(user.getObjectID());
                    approver.setName(user.getFirstName());
                    approver.setLastName(user.getLastName());
                }
                payslipListItem.setApproverItem(approver);


                payslipListItem.setEmployeeItem(employee);
                payslipListItemList.add(payslipListItem);
            }
        }

        return successResponse(new PayslipListResultTO(payslipListItemList));
    }

    @Operation(summary = "Get Payslip Details", description = "Retrieves Payslip Details by request_id")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have Payslip details."),
            @ApiResponse(responseCode = "400", description = "request_id is required"),
            @ApiResponse(responseCode = "404", description = "Payslip with provided request_id is not found")})
    @RequestMapping(value = "/payments/payslips_details", method = RequestMethod.GET)
    @CheckPermission(permissions = {PermissionConstants.PAYROLL_MAIN_MENU, PermissionConstants.PAYROLL_PAYSLIP_VIEW})
    public Object getPayslipDetails(@RequestParam(value = "request_id") Integer request_id) throws RestException {
        if (request_id == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "request_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        PayslipItemFilter filter = new PayslipItemFilter();
        filter.setObjectID(request_id);
        filter.setFromView(true);
        EdsPayslipTableItem edsPayslipTableItem = payslipTableItemManager.get(request_id);
        if (edsPayslipTableItem == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Payslip with id " + request_id + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        String baseCurrency = currencyServiceLocal.getBaseCurrency().getName();
        SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);

        Integer calculationScale = ServerUtils.getCalculationScale();

        SinglePayrunItem singlePayrunItem = payrollServiceLocal.getSinglePayrunData(filter);

        PayslipDetailsTO payslipDetails = new PayslipDetailsTO();
        payslipDetails.setId(singlePayrunItem.getObjectID());
        if (singlePayrunItem.getFromDate() != null && singlePayrunItem.getFromDate().getNonConvertedDate() != null) {
            payslipDetails.setDate(longDateTimezoneFormat.format(singlePayrunItem.getFromDate().getNonConvertedDate()));
        }

        SelectItem statusItem = new SelectItem();
        statusItem.setCode(singlePayrunItem.getStatusCode());
        statusItem.setName(singlePayrunItem.getStatus());
        payslipDetails.setStatus(getStatus(statusItem));

        String currency = (singlePayrunItem.isEnabledMultiCurrency() && singlePayrunItem.getCurrency() != null) ? singlePayrunItem.getCurrency().getName() : baseCurrency;
        //allowances
        payslipDetails.setAllowances(new PayslipItemTableTO());
        if (singlePayrunItem.getPaymentCategories() != null && !singlePayrunItem.getPaymentCategories().isEmpty()) {
            PayslipItemTableTO allowances = getPayslipItemTable(singlePayrunItem.getPaymentCategories(), currency, calculationScale);
            payslipDetails.setAllowances(allowances != null ? allowances : new PayslipItemTableTO());
        }
        //deductions
        payslipDetails.setDeductions(new PayslipItemTableTO());
        if (singlePayrunItem.getDeductionCategories() != null && !singlePayrunItem.getDeductionCategories().isEmpty()) {
            PayslipItemTableTO deductions = getPayslipItemTable(singlePayrunItem.getDeductionCategories(), currency, calculationScale);
            payslipDetails.setDeductions(deductions != null ? deductions : new PayslipItemTableTO());
        }
        //expenses
        //singlePayrunItem.getEmployeeExpenses();

        BigDecimal total = BigDecimal.ZERO, allowanceTotal = BigDecimal.ZERO, pensionTotal = BigDecimal.ZERO;
        BigDecimal deductionTotal = BigDecimal.ZERO, expenseTotal = BigDecimal.ZERO, expensePaymentTotal = BigDecimal.ZERO, expenseDeductionTotal = BigDecimal.ZERO;

        if (payslipDetails.getAllowances() != null && payslipDetails.getAllowances().getTotal() != null) {
            allowanceTotal = payslipDetails.getAllowances().getTotal().getValue();
        }
        if (payslipDetails.getDeductions() != null && payslipDetails.getDeductions().getTotal() != null) {
            deductionTotal = payslipDetails.getDeductions().getTotal().getValue();
        }

        if (singlePayrunItem.getEmployeeExpenses() != null && singlePayrunItem.getEmployeeExpenses().getExpenses() != null) {
            for (ExpenseData expenseData : singlePayrunItem.getEmployeeExpenses().getExpenses()) {
                if (expenseData.getPaymentType() != null) {
                    if (expenseData.getPaymentType() == 0) {//payment
                        expensePaymentTotal = expensePaymentTotal.add(BigDecimal.valueOf(expenseData.getAmount()));
                    } else {//deduction
                        expenseDeductionTotal = expenseDeductionTotal.add(BigDecimal.valueOf(expenseData.getAmount()));
                    }
                }
                expenseTotal = expenseTotal.add(BigDecimal.valueOf(expenseData.getAmount()));
            }
        }

        total = total.add(allowanceTotal);
        total = total.add(expensePaymentTotal);
        total = total.subtract(expenseDeductionTotal);
        total = total.subtract(deductionTotal);
        if (singlePayrunItem.getPensionAmount() != null) {
            total = total.subtract(singlePayrunItem.getPensionAmount());
            pensionTotal = singlePayrunItem.getPensionAmount();
        }

        ArrayList<PayslipTotalTO> totals = new ArrayList<>();
        int id = 1;
        if (ApiUtils.getTotal(allowanceTotal) != null) {
            PayslipTotalTO totalAllowances = new PayslipTotalTO();
            totalAllowances.setId(id);
            totalAllowances.setTitle(commonLocalizer.localize("totalAllowances"));
            totalAllowances.setSum(new CurrencyValueTO(allowanceTotal.setScale(calculationScale, RoundingMode.HALF_UP), currency));
            totals.add(totalAllowances);
            id++;
        }

        if (ApiUtils.getTotal(deductionTotal) != null) {
            PayslipTotalTO totalDeductions = new PayslipTotalTO();
            totalDeductions.setId(id);
            totalDeductions.setTitle(commonLocalizer.localize("totalDeductions"));
            totalDeductions.setSum(new CurrencyValueTO(deductionTotal.setScale(calculationScale, RoundingMode.HALF_UP), currency));
            totals.add(totalDeductions);
            id++;
        }

        if (ApiUtils.getTotal(expenseTotal) != null) {
            PayslipTotalTO totalExpenses = new PayslipTotalTO();
            totalExpenses.setId(id);
            totalExpenses.setTitle(commonLocalizer.localize("totalExpenses"));
            totalExpenses.setSum(new CurrencyValueTO(expenseTotal.setScale(calculationScale, RoundingMode.HALF_UP), currency));
            totals.add(totalExpenses);
            id++;
        }

        if (ApiUtils.getTotal(pensionTotal) != null) {
            PayslipTotalTO totalPensions = new PayslipTotalTO();
            totalPensions.setId(id);
            totalPensions.setTitle(commonLocalizer.localize("govPension", "Total Gov/Pension"));
            totalPensions.setSum(new CurrencyValueTO(pensionTotal.setScale(calculationScale, RoundingMode.HALF_UP), currency));
            totals.add(totalPensions);
            id++;
        }

        if (singlePayrunItem.isEnabledMultiCurrency() && singlePayrunItem.getCurrency() != null) {
            if (ApiUtils.getTotal(total) != null) {
                PayslipTotalTO grandTotals = new PayslipTotalTO();
                grandTotals.setId(id);
                grandTotals.setTitle("Total(" + currency + ")");
                grandTotals.setSum(new CurrencyValueTO(total.setScale(calculationScale, RoundingMode.HALF_UP), currency));
                totals.add(grandTotals);
                id++;
            }

            if (ApiUtils.getTotal(singlePayrunItem.getTotalInBase()) != null) {
                PayslipTotalTO grandBaseTotals = new PayslipTotalTO();
                grandBaseTotals.setId(id);
                grandBaseTotals.setTitle("Total(" + baseCurrency + ")");
                grandBaseTotals.setSum(new CurrencyValueTO(singlePayrunItem.getTotalInBase().setScale(calculationScale, RoundingMode.HALF_UP), baseCurrency));
                totals.add(grandBaseTotals);
            }
        } else {
            if (ApiUtils.getTotal(total) != null) {
                PayslipTotalTO grandTotals = new PayslipTotalTO();
                grandTotals.setId(id);
                grandTotals.setTitle("Total(" + baseCurrency + ")");
                grandTotals.setSum(new CurrencyValueTO(total.setScale(calculationScale, RoundingMode.HALF_UP), baseCurrency));
                totals.add(grandTotals);

            }
        }

        EdsEmployee approverItem = edsPayslipTableItem.getApprover();
        if (approverItem != null) {
            UserTO approver = new UserTO();
            approver.setFirstName(approverItem.getFirstName());
            approver.setLastName(approverItem.getLastName());
            approver.setMiddleName(approverItem.getMiddleName());
            approver.setId(approverItem.getObjectID());
            approver.setImageUrl(hrmsServiceLocal.getEmployeeImageURL(approverItem.getObjectID()));
            payslipDetails.setApprover(approver);
        }

        EdsEmployee employeeItem = edsPayslipTableItem.getEmployee();
        if (employeeItem != null) {
            UserTO employee = new UserTO();
            employee.setFirstName(employeeItem.getFirstName());
            employee.setLastName(employeeItem.getLastName());
            employee.setMiddleName(employeeItem.getMiddleName());
            employee.setId(employeeItem.getObjectID());
            employee.setImageUrl(hrmsServiceLocal.getEmployeeImageURL(employeeItem.getObjectID()));
            employee.setHireDate(employeeItem.getStartDate());
            payslipDetails.setEmployee(employee);
        }
        payslipDetails.setTotal(totals);

        return successResponse(payslipDetails);
    }

    private PayslipItemTableTO getPayslipItemTable(List<PaymentDeductionObject> paymentDeductionList, String currency, Integer calculationScale) {
        PayslipItemTableTO payslipItemTable = new PayslipItemTableTO();
        BigDecimal totalPayment = BigDecimal.ZERO;
        ArrayList<PayslipRecordTO> records = new ArrayList<>();
        for (PaymentDeductionObject paymentDeduction : paymentDeductionList) {
            if (ApiUtils.getTotal(paymentDeduction.getPaymentAmount()) != null) {
                PayslipRecordTO record = new PayslipRecordTO();
                if (paymentDeduction.getCategoryItem() != null) {
                    record.setId(paymentDeduction.getCategoryItem().getId());
                    record.setTitle(paymentDeduction.getCategoryItem().getName());
                }
                if (paymentDeduction.getType() != null) {
                    if (paymentDeduction.getType() == 0 || paymentDeduction.isLoan()) {
                        record.setRecord_type(new StatusTO(DiscountTypeEnum.FIXED.name()));
                    } else {
                        record.setRecord_type(new StatusTO(DiscountTypeEnum.PERCENT.name()));
                    }
                } else {
                    record.setRecord_type(new StatusTO(DiscountTypeEnum.FIXED.name()));
                }
                if (StringUtils.isNotBlank(paymentDeduction.getRemarks())) {
                    record.setRemarks(paymentDeduction.getRemarks());
                }
                record.setSum(new CurrencyValueTO(paymentDeduction.getPaymentAmount().setScale(calculationScale, RoundingMode.HALF_UP), currency));

                records.add(record);
                totalPayment = totalPayment.add(paymentDeduction.getPaymentAmount().setScale(calculationScale, RoundingMode.HALF_UP));
            }
        }

        if (ApiUtils.getTotal(totalPayment) != null) {
            payslipItemTable.setTotal(new CurrencyValueTO(totalPayment.setScale(calculationScale, RoundingMode.HALF_UP), currency));
            payslipItemTable.setRecords(records);
            return payslipItemTable;
        }
        return null;
    }

    private StatusTO getStatus(SelectItem status) {
        if (status == null || StringUtils.isBlank(status.getCode())) {
            return null;
        }
        return switch (status.getCode()) {
            case Constants.PAYRUN_STATUS_APPROVED -> new StatusTO(RequestStatusEnum.APPROVED.getStatus());
            case Constants.PAYRUN_STATUS_REJECTED -> new StatusTO(RequestStatusEnum.DECLINED.getStatus());
            case Constants.PAYRUN_STATUS_DRAFT -> new StatusTO(RequestStatusEnum.DRAFT.getStatus());
            case Constants.PAYRUN_STATUS_SUBMITTED -> new StatusTO(RequestStatusEnum.PENDING.getStatus());
            case Constants.PAYRUN_STATUS_OPEN -> new StatusTO(RequestStatusEnum.CUSTOM.getStatus(), status.getName());
            default -> new StatusTO(RequestStatusEnum.CUSTOM.getStatus(), status.getName());
        };
    }

}
