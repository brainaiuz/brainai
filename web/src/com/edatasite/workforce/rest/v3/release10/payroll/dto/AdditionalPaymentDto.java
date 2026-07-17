package com.edatasite.workforce.rest.v3.release10.payroll.dto;

import com.edatasite.workforce.rest.v3.release10.core.to.ItemDto;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.Month;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AdditionalPaymentDto implements Serializable {
    private Integer id;
    @JsonAlias({"category", "category_type"})
    private String category;
    private String type;
    @JsonAlias({"status", "statusCode"})
    private String status;
    private String reference;
    private Month month;
    private Integer year;
    private ItemDto payrollBatch;
    private ItemDto employee;
    private List<ItemDto> approvers;
    private List<APaymentItemDto> items;
    private boolean showInPayslip;
    @NotNull(message = "paymentType is required")
    @Pattern(regexp = "FIXED_AMOUNT|BASIC_SALARY|BASIC_SALARY_ALLOWANCE",
            message = "paymentType must be one of FIXED_AMOUNT/BASIC_SALARY/BASIC_SALARY_ALLOWANCE")
    private String paymentType;

    public AdditionalPaymentDto() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Month getMonth() {
        return month;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public ItemDto getPayrollBatch() {
        return payrollBatch;
    }

    public void setPayrollBatch(ItemDto payrollBatch) {
        this.payrollBatch = payrollBatch;
    }

    public ItemDto getEmployee() {
        return employee;
    }

    public void setEmployee(ItemDto employee) {
        this.employee = employee;
    }

    public List<ItemDto> getApprovers() {
        return approvers;
    }

    public void setApprovers(List<ItemDto> approvers) {
        this.approvers = approvers;
    }

    public List<APaymentItemDto> getItems() {
        return items;
    }

    public void setItems(List<APaymentItemDto> items) {
        this.items = items;
    }

    public boolean isShowInPayslip() {
        return showInPayslip;
    }

    public void setShowInPayslip(boolean showInPayslip) {
        this.showInPayslip = showInPayslip;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }
}