package com.edatasite.workforce.rest.v3.release10.accounting.dto;

import com.edatasite.workforce.rest.v3.release10.core.request.CustomFieldRequest;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.List;

public class RentalOrderRequest {

    @Null(groups = Create.class, message = "Rental order ID is specified.")
    @NotNull(groups = Update.class, message = "Rental Order Id is not specified.")
    private Integer rentalOrderId;

    @NotNull(message = "Customer Id is required.")
    private Integer customerId;

    @Valid
    @NotNull(message = "Rental Items are required.")
    private List<RentalItemRequest> rentalItems;

    private String status;

    @Valid
    private List<? extends CustomFieldRequest> customFields;

    public Integer getRentalOrderId() {
        return rentalOrderId;
    }

    public void setRentalOrderId(Integer rentalOrderId) {
        this.rentalOrderId = rentalOrderId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public List<RentalItemRequest> getRentalItems() {
        return rentalItems;
    }

    public void setRentalItems(List<RentalItemRequest> rentalItemDtos) {
        this.rentalItems = rentalItemDtos;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<? extends CustomFieldRequest> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(List<? extends CustomFieldRequest> customFields) {
        this.customFields = customFields;
    }

    @Override
    public String toString() {
        return "RentalOrderDto{" +
                "rentalOrderId=" + rentalOrderId +
                ", customerId=" + customerId +
                ", rentalItemDtos=" + rentalItems +
                ", status='" + status + '\'' +
                ", customFields=" + customFields +
                '}';
    }

    public interface Create {
    }

    public interface Update {
    }

}

