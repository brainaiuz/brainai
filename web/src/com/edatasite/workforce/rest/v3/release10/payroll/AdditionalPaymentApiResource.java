package com.edatasite.workforce.rest.v3.release10.payroll;

import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionObject;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionSelectItem;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.FinancialSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PayrollCategoryManager;
import com.edatasite.workforce.gwt.payroll.client.rpc.AdditionalPayment;
import com.edatasite.workforce.gwt.payroll.client.rpc.CategoryObject;
import com.edatasite.workforce.gwt.payroll.server.app.PayrollServiceLocal;
import com.edatasite.workforce.gwt.profile.client.ui.PayrollConstants;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.core.to.ItemDto;
import com.edatasite.workforce.rest.v3.release10.core.utils.MultiApprovalUtils;
import com.edatasite.workforce.rest.v3.release10.payroll.dto.APaymentItemDto;
import com.edatasite.workforce.rest.v3.release10.payroll.dto.AdditionalPaymentDto;
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
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Tag(name = "Additional Payment/Deduction Api Resource", description = "Here is a additional payment/deduction api resouce that making additional payments or deduction for payroll")
@RestController
@RequestMapping(value = "/additional", headers = {ApiConstants.X_AUTH, ApiConstants.ACCESS_TOKEN},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class AdditionalPaymentApiResource implements ApiConstants {
    @Autowired
    private PayrollServiceLocal payrollServiceLocal;
    @Autowired
    private FinancialSettingsManager financialSettingsManager;
    @Autowired
    private MultiApprovalUtils approverUtils;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private PayrollCategoryManager payrollCategoryManager;

    @RequestMapping(value = "/payment", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<AdditionalPaymentDto> createPayment(@RequestBody AdditionalPaymentDto dto) throws RestException {
        validateCreatePaymentDto(dto);
        Integer objectId = payrollServiceLocal.saveAdditionalPayment(wrapToModel(dto), false);

        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setObjectId(objectId);
        AdditionalPayment payment = payrollServiceLocal.getAdditionalPaymentData(fp);
        return new ResponseEntity<>(wrapToDto(payment, dto), HttpStatus.OK);
    }

    @RequestMapping(value = "/payment", method = RequestMethod.PUT)
    public ResponseEntity<AdditionalPaymentDto> updatePayment(@RequestBody AdditionalPaymentDto dto) throws RestException {
        validateCreatePaymentDto(dto);
        Integer objectId = payrollServiceLocal.saveAdditionalPayment(wrapToModel(dto), false);

        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setObjectId(objectId);
        AdditionalPayment payment = payrollServiceLocal.getAdditionalPaymentData(fp);
        return new ResponseEntity<>(wrapToDto(payment, dto), HttpStatus.OK);
    }

    AdditionalPayment wrapToModel(AdditionalPaymentDto dto) {
        LocalDate currentDate = LocalDate.now();
        dto.setMonth(dto.getMonth() == null ? currentDate.getMonth() : dto.getMonth());

        AdditionalPayment payment = new AdditionalPayment();
        payment.setCategoryType(PayrollConstants.CATEGORY_PAYMENT);
        payment.setObjectID(dto.getId());
        payment.setType(dto.getType());
        payment.setStatusCode(dto.getStatus());
        payment.setReference(dto.getReference());
        payment.setMonth(dto.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH));
        payment.setMonthID(dto.getMonth().getValue() - 1);
        payment.setYear(dto.getYear() != null ? dto.getYear() : currentDate.getYear());
        payment.setShowInPayslip(dto.isShowInPayslip());
        payment.setPaymentType(dto.getPaymentType());
        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        if (fs != null) {
            payment.setCurrency(fs.getCurrency().createCurrencyItem());
        }
        if (dto.getPayrollBatch() != null) {
            payment.setPayrollBatch(new SelectItem(dto.getPayrollBatch().getId()));
        }
        if (dto.getEmployee() != null) {
            ItemDto employee = dto.getEmployee();
            payment.setEmployee(getEmployee(employee));
        }
        if (dto.getApprovers() != null) {
            List<Integer> approverIds = dto.getApprovers().stream().map(ItemDto::getId).collect(Collectors.toList());
            payment.setApprovers(approverUtils.getSelectedApprovers(approverIds, approverUtils.getApprovalSchemes(RelationItem.TYPE_ADDITIONAL_PAYMENT)));
        }

        if (!CollectionUtils.isEmpty(dto.getItems())) {
            boolean isAllowanceType = "BASIC_SALARY_ALLOWANCE".equals(payment.getPaymentType());
            boolean isBasicPaymentType = "BASIC_SALARY".equals(payment.getPaymentType());
            ArrayList<PaymentDeductionObject> list = dto.getItems().stream().map(item -> {
                PaymentDeductionObject object = new PaymentDeductionObject();

                if (payment.getEmployee() != null) {
                    object.setEmployee(payment.getEmployee());
                } else if (item.getEmployee() != null) {
                    object.setEmployee(getEmployee(item.getEmployee()));
                }

                object.setPercentage(item.getPercentage());
                if (isAllowanceType) {
                    object.setBasicPlusAllowance(item.getTotalAmount());
                    item.setAmount(item.getTotalAmount().multiply(item.getPercentage().divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)));
                } else if (isBasicPaymentType) {
                    object.setEmployeeBasicSalary(item.getTotalAmount());
                    item.setAmount(item.getTotalAmount().multiply(item.getPercentage().divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)));
                } else {
                    object.setBasicPlusAllowance(BigDecimal.ZERO);
                    object.setEmployeeBasicSalary(BigDecimal.ZERO);
                    object.setPercentage(BigDecimal.ZERO);
                }
                object.setPaymentAmount(item.getAmount());

                if (item.getCategory() != null) {
                    ItemDto category = item.getCategory();
                    if (category.getId() != null) {
                        object.setCategoryItem(new PaymentDeductionSelectItem(item.getCategory().getId(), item.getCategory().getName(), item.getCategory().getCode(), null));
                    } else if (StringUtils.isNotBlank(category.getCode())) {
                        Optional.ofNullable(payrollCategoryManager.getCategoryByCode(category.getCode())).ifPresent(c -> object.setCategoryItem(c.createPaymentDeductionSelectItem()));
                    } else if (StringUtils.isNotBlank(category.getName())) {
                        String categoryName = category.getName().trim();
                        Optional.ofNullable(payrollCategoryManager.getCategoryByName(categoryName, null)).ifPresent(c -> object.setCategoryItem(c.createPaymentDeductionSelectItem()));

                        if (object.getCategoryItem() == null || object.getCategoryItem().getId() == null) {
                            CategoryObject categoryObject = new CategoryObject();
                            categoryObject.setName(categoryName);
                            categoryObject.setCode(categoryName.replace(" ", "_").replaceAll("\\s", "").toUpperCase());
                            categoryObject.setType(payment.getCategoryType());
                            Integer categoryId = payrollServiceLocal.createCategory(categoryObject);
                            object.setCategoryItem(new PaymentDeductionSelectItem(categoryId, null, null, null));
                        }
                    }

                    if (object.getCategoryItem() != null && object.getCategoryItem().getId() != null) {
                        Optional.ofNullable(payrollServiceLocal.getPredefinedValueOfCategory(object.getEmployee().getId(), object.getCategoryItem().getId())).ifPresent(amount -> {
                            if (object.getPaymentAmount() == null || object.getPaymentAmount().compareTo(BigDecimal.ZERO) == 0) {
                                object.setPaymentAmount(amount);
                            }
                        });
                    }
                }
                object.setAdditionalPaymentDate(new DateNonConvertable(item.getPaymentDate()));

                initialize_object_period:
                {
                    LocalDate startDate = LocalDate.of(payment.getYear(), dto.getMonth(), currentDate.getDayOfMonth());
                    LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
                    object.setStarttDate(new DateNonConvertable(java.sql.Date.valueOf(startDate)));
                    object.setEnddDate(new DateNonConvertable(java.sql.Date.valueOf(endDate)));
                }
                return object;
            }).collect(Collectors.toCollection(ArrayList::new));
            payment.setItems(list);
            payment.setTotal(list.stream().reduce(BigDecimal.ZERO, (total, item) -> total.add(item.getPaymentAmount()), BigDecimal::add));
        }
        return payment;
    }

    AdditionalPaymentDto wrapToDto(AdditionalPayment payment, AdditionalPaymentDto dto) {
        dto = Optional.ofNullable(dto).orElse(new AdditionalPaymentDto());
        dto.setId(payment.getObjectID());
        dto.setStatus(payment.getStatus());

        if (payment.getEmployee() != null) {
            dto.setEmployee(new ItemDto(payment.getEmployee().getId(), payment.getEmployee().getName()));
        }
        if (!CollectionUtils.isEmpty(payment.getApprovers())) {
            dto.setApprovers(payment.getApprovers().stream().filter(apr -> apr.getExactEmployee() != null).map(apr -> new ItemDto(apr.getExactEmployee().getId(), apr.getExactEmployee().getName())).collect(Collectors.toList()));
        }
        if (!CollectionUtils.isEmpty(payment.getItems())) {
            dto.setItems(payment.getItems().stream().map(item -> {
                APaymentItemDto pItem = new APaymentItemDto();
                pItem.setEmployee(new ItemDto(item.getEmployee().getId(), item.getEmployee().getName()));
                pItem.setAmount(item.getAmount());
                pItem.setCategory(new ItemDto(item.getCategoryItem().getId(), item.getCategoryItem().getName()));
                if (item.getAdditionalPaymentDate() != null) {
                    pItem.setPaymentDate(item.getAdditionalPaymentDate().getNonConvertedDate());
                }
                return pItem;
            }).collect(Collectors.toList()));
        }
        return dto;
    }

    void validateCreatePaymentDto(AdditionalPaymentDto dto) throws RestException {
        if (dto.getPayrollBatch() == null && dto.getEmployee() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "You need to provide category, ether payroll group or employee!", ApiConstants.REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(dto.getType())) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "You need to provide type, ether Additional Payment or By Commission!", ApiConstants.REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(dto.getReference())) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "You must provide a reference field!", ApiConstants.REQUIRED, HttpStatus.BAD_REQUEST);
        }
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
