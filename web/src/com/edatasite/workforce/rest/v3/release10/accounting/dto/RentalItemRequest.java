package com.edatasite.workforce.rest.v3.release10.accounting.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

public class RentalItemRequest {

    @NotNull(message = "Rental Item Id is required.")
    private Integer rentalItemId;

    @NotNull(message = "From date is required.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    private Date from;

    @NotNull(message = "To date is required.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    private Date to;

    @NotNull(message = "rentalPrice is required.")
    private BigDecimal rentalPrice;

    @DecimalMin(value = "0.01", message = "quantity must be greater than zero.")
    @NotNull(message = "quantity is required.")
    private BigDecimal quantity;

    public Integer getRentalItemId() {
        return rentalItemId;
    }

    public void setRentalItemId(Integer rentalItemId) {
        this.rentalItemId = rentalItemId;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public BigDecimal getRentalPrice() {
        return rentalPrice;
    }

    public void setRentalPrice(BigDecimal rentalPrice) {
        this.rentalPrice = rentalPrice;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }
}
