package com.edatasite.workforce.rest.v3.release10.payroll.dto;

import com.edatasite.workforce.rest.v3.release10.core.to.ItemDto;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class APaymentItemDto {
    private ItemDto employee;
    private BigDecimal amount;
    private BigDecimal percentage;
    private BigDecimal totalAmount;
    private ItemDto category;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
//    @JsonDeserialize(using = DateDeserializer.class)
    @JsonAlias({"paymentDate", "payment_date"})
    private Date paymentDate;

    public APaymentItemDto() {
    }

    public ItemDto getEmployee() {
        return employee;
    }

    public void setEmployee(ItemDto employee) {
        this.employee = employee;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public ItemDto getCategory() {
        return category;
    }

    public void setCategory(ItemDto category) {
        this.category = category;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }
}
