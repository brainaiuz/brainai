package com.edatasite.workforce.rest.v3.release10.payroll;

import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayrollCategory;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionObject;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionSelectItem;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PayrollCategoryManager;
import com.edatasite.workforce.gwt.payroll.client.rpc.CategoryObject;
import com.edatasite.workforce.gwt.payroll.client.rpc.SinglePayrunItem;
import com.edatasite.workforce.gwt.payroll.server.app.PayrollServiceLocal;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.core.to.ItemDto;
import com.edatasite.workforce.rest.v3.release10.core.utils.MultiApprovalUtils;
import com.edatasite.workforce.rest.v3.release10.payroll.dto.PayslipDto;
import com.edatasite.workforce.rest.v3.release10.payroll.dto.PayslipItemDto;
import io.jsonwebtoken.lang.Collections;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.PAYRUN_STATUS_APPROVED;

@Tag(name = "Payslip Api Resource", description = "Payslip api resource")
@RestController
@RequestMapping(value = "/payslip", headers = {ApiConstants.X_AUTH, ApiConstants.ACCESS_TOKEN},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class PayslipApiResource implements ApiConstants {

    @Autowired
    private MultiApprovalUtils approverUtils;
    @Autowired
    private PayrollServiceLocal payrollServiceLocal;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private PayrollCategoryManager categoryManager;
    @Autowired
    private UserManager userManager;


    @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<Object> createPayslip(@RequestBody PayslipDto dto) {;
        return new ResponseEntity<>(payrollServiceLocal.saveSinglePayrun(wrapToModel(dto)), HttpStatus.OK);
    }

    SinglePayrunItem wrapToModel(PayslipDto dto) {
        LocalDate currentDate = LocalDate.now();
        DateNonConvertable dateNonConvertable= new DateNonConvertable(new Date());
        BigDecimal totalPayment = BigDecimal.valueOf(0);
        BigDecimal totalDeduction = BigDecimal.valueOf(0);
        var payslip = new SinglePayrunItem();
        payslip.setObjectID(dto.getId());
        payslip.setEmployeeID(getEmployee(dto.getEmployee()).getId());
        payslip.setMonthID(dto.getFromDate().getMonth());
        payslip.setFrequency(4);
        payslip.setPayMethodId(1);
        payslip.setPayMethodName(dto.getPaymentMethod());
        payslip.setStatusCode(dto.getStatus());
        payslip.setMonthID(dto.getFromDate().getMonth());
        payslip.setYear(dto.getFromDate() != null ? dto.getFromDate().getYear() : currentDate.getYear());
        payslip.setProcessDate(new DateNonConvertable(dto.getToDate() != null ? dto.getToDate() : new Date()));
        payslip.setFromDate(new DateNonConvertable(dto.getFromDate() != null ? dto.getFromDate() : new Date()));
        payslip.setToDate(new DateNonConvertable(dto.getToDate() != null ? dto.getToDate() : new Date()));
        payslip.setSendNotification(false);
        payslip.setCreator(userManager.getUser().getAsSelectItem());
        payslip.setCreationDate(dateNonConvertable);

        if (PAYRUN_STATUS_APPROVED.equals(dto.getStatus())) {
            payslip.setApprovedDate(dateNonConvertable);
        }

        payslip.setApprover(getEmployee(dto.getEmployee()));
        payslip.setStatus(dto.getStatus());
        if (!Collections.isEmpty(dto.getPayments())) {
            ArrayList<PaymentDeductionObject> paymentObjects = new ArrayList<>();
            for (PayslipItemDto payment : dto.getPayments()) {
                var object = new PaymentDeductionObject();
                try {
                    object.setCategoryItem(validatePayrollCategoryDto(payment.getPayments(), EdsPayrollCategory.PAYMENT));
                } catch (RestException e) {
                    throw new RuntimeException(e);
                }
                object.setPaymentAmount(payment.getAmount());
                object.setRemarks(payment.getRemarks());
                object.setType(0);
                totalPayment = totalPayment.add(payment.getAmount());
                paymentObjects.add(object);
            }
            payslip.setPaymentCategories(paymentObjects);
        }

        if (!Collections.isEmpty(dto.getDeductions())) {
            ArrayList<PaymentDeductionObject> deductionObjects = new ArrayList<>();
            for (PayslipItemDto deduction : dto.getDeductions()) {
                var object = new PaymentDeductionObject();
                try {
                    object.setCategoryItem(validatePayrollCategoryDto(deduction.getPayments(), EdsPayrollCategory.DEDUCTION));
                } catch (RestException e) {
                    throw new RuntimeException(e);
                }
                object.setPaymentAmount(deduction.getAmount());
                object.setRemarks(deduction.getRemarks());
                object.setType(0);
                totalDeduction = totalDeduction.add(deduction.getAmount());
                deductionObjects.add(object);
            }
            payslip.setDeductionCategories(deductionObjects);
        }
        payslip.setAllowance(totalPayment);
        payslip.setDeduction(totalDeduction);
        payslip.setTotal(totalPayment.subtract(totalDeduction));
        payslip.setTotalInBase(totalPayment.subtract(totalDeduction));

        return payslip;
    }
    public PaymentDeductionSelectItem validatePayrollCategoryDto(ItemDto dto, String type) throws RestException {
        try {
            if (dto.getId() == null && dto.getName() == null && dto.getCode() == null) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "You need to provide payment category", ApiConstants.REQUIRED, HttpStatus.BAD_REQUEST);
            }

            PaymentDeductionSelectItem category = null;

            if (dto.getId() != null) {
                category = payrollServiceLocal.getCategoryById(dto.getId());
                if (category == null) {
                    throw new RestException(GENERAL_ERROR_MESSAGE, "Payroll category not found by Id", ApiConstants.REQUIRED, HttpStatus.BAD_REQUEST);
                }
            } else if (dto.getCode() != null) {
                EdsPayrollCategory item = categoryManager.getCategoryByCode(dto.getCode(),type) != null ? categoryManager.getCategoryByCode(dto.getCode(),type) : null;
                if (item == null && dto.getName() != null){
                   Integer edsPayrollCategoryId= saveCategory(dto,type);
                   EdsPayrollCategory edsPayrollCategory= categoryManager.get(edsPayrollCategoryId);
                   category = new PaymentDeductionSelectItem(edsPayrollCategoryId,edsPayrollCategory.getName(),edsPayrollCategory.getCode(),type);
                } else if (item != null) {
                    category=new PaymentDeductionSelectItem(item.getObjectID(),item.getName(),item.getCode(),type);
                }
            }

            if (category == null) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Payroll category not found", ApiConstants.REQUIRED, HttpStatus.BAD_REQUEST);
            }

            return category;
        } catch (Exception e) {
            // Handle exceptions appropriately
            e.printStackTrace(); // Or log the exception
            throw new RestException(GENERAL_ERROR_MESSAGE, "An error occurred", ApiConstants.REQUIRED, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Integer saveCategory(ItemDto itemDto, String type){
        var category = new CategoryObject();
        category.setName(itemDto.getName());
        category.setCode(itemDto.getCode());
        category.setType(type);
        return payrollServiceLocal.createCategory(category);
    }


    SelectItem getEmployee(ItemDto empDto) {
        SelectItem employee = null;
        if (empDto.getId() != null) {
            employee = new SelectItem(empDto.getId());
        } else if (StringUtils.isNotBlank(empDto.getValueByKey(PROPS.EMAIL))) {
            List<EdsEmployee> l = employeeManager.getEmployeesByEmail(empDto.getValueByKey(PROPS.EMAIL));
            if (!CollectionUtils.isEmpty(l)) {
                employee = l.get(0).getAsSelectItem();
            }
        }

        if (employee == null && StringUtils.isNotBlank(empDto.getCode())) {
            EdsEmployee emp = employeeManager.getEmployeeByNumber(empDto.getCode());
            employee = emp != null ? emp.getAsSelectItem() : null;
        }
        if (employee == null && StringUtils.isNotBlank(empDto.getName())) {
            EdsEmployee emp = employeeManager.getEmployeeByFirstNameViaLastName(empDto.getName());
            employee = emp != null ? emp.getAsSelectItem() : null;
        }
        if (employee == null && StringUtils.isNotBlank(empDto.getValueByKey(PROPS.DRIVER_NUMBER))) {
            EdsEmployee emp = employeeManager.getEmployeeByDriverNumber(Long.valueOf(empDto.getValueByKey(PROPS.DRIVER_NUMBER)));
            employee = emp != null ? emp.getAsSelectItem() : null;
        }
        return employee;
    }


}
