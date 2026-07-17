package com.edatasite.workforce.rest.v3.release10.payroll.dto;

import com.edatasite.workforce.rest.v3.release10.core.to.ItemDto;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.Month;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PayslipDto implements Serializable {
    private Integer id;
    private ItemDto employee;

    @Schema(description = "Date Format (format: yyyy-MM-dd HH:mm)")
    @NotNull(message = "PROCESS DATE is required")
    private Date processDate;

    @Schema(description = "Date Format (format: yyyy-MM-dd HH:mm)")
    @NotNull(message = "FROM DATE is required")
    private Date fromDate;

    @Schema(description = "Date Format (format: yyyy-MM-dd HH:mm)")
    @NotNull(message = "TO DATE is required")
    private Date toDate;
    @JsonAlias({"category", "category_type"})
    private String category;
    @Pattern(regexp = "WEEKLY|MONTHLY|ANNUAL",
            message = "frequency must be one of WEEKLY|MONTHLY|ANNUAL")
    private String frequency;
    @Pattern(regexp = "CASH|CREDIT_CARD|DEBIT_CARD",
            message = "paymentMethod must be one of CASH|CREDIT_CARD|DEBIT_CARD")
    private String paymentMethod;
    private Month month;
    private Integer year;

    private ItemDto approver;
    private List<PayslipItemDto> payments;
    private List<PayslipItemDto> deductions;
    @JsonAlias({"status", "statusCode"})
    private String status;

    public PayslipDto(Integer id, ItemDto employee, Date processDate, Date fromDate, Date toDate, String category, String frequency, String paymentMethod, Month month, Integer year, List<ItemDto> approvers, List<PayslipItemDto> payments, List<PayslipItemDto> deductions, String status) {
        this.id = id;
        this.employee = employee;
        this.processDate = processDate;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.category = category;
        this.frequency = frequency;
        this.paymentMethod = paymentMethod;
        this.month = month;
        this.year = year;
        this.approver = approver;
        this.payments = payments;
        this.deductions = deductions;
        this.status = status;
    }

    public PayslipDto() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ItemDto getEmployee() {
        return employee;
    }

    public void setEmployee(ItemDto employee) {
        this.employee = employee;
    }

    public Date getProcessDate() {
        return processDate;
    }

    public void setProcessDate(Date processDate) {
        this.processDate = processDate;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
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

    public List<PayslipItemDto> getPayments() {
        return payments;
    }

    public void setPayments(List<PayslipItemDto> payments) {
        this.payments = payments;
    }

    public List<PayslipItemDto> getDeductions() {
        return deductions;
    }

    public void setDeductions(List<PayslipItemDto> deductions) {
        this.deductions = deductions;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ItemDto getApprover() {
        return approver;
    }

    public void setApprover(ItemDto approver) {
        this.approver = approver;
    }

    @Override
    public String toString() {
        return "PayslipDto{" +
                "id=" + id +
                ", employee=" + employee +
                ", processDate=" + processDate +
                ", fromDate=" + fromDate +
                ", toDate=" + toDate +
                ", category='" + category + '\'' +
                ", frequency='" + frequency + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", month=" + month +
                ", year=" + year +
                ", approver=" + approver +
                ", payments=" + payments +
                ", deductions=" + deductions +
                ", status='" + status + '\'' +
                '}';
    }
}
