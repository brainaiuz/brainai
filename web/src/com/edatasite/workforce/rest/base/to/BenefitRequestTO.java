package com.edatasite.workforce.rest.base.to;

import com.edatasite.workforce.gwt.availability.client.rpc.BenefitRequestItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.rest.base.helpers.WrapUtils;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by Dilshod Madrahimov on 5/13/15 8:02 PM
 */
public class BenefitRequestTO implements IsSerializable {
    Integer id;
    String description;
    Long date;
    SelectItemTO status;
    EmployeeTO requester;
    EmployeeTO approver;
    BenefitTypeTO benefitType;
    Double requestQuantity;
    String totalUsedRequest;
    String totalLeftRequest;
    String quantityType;

    public BenefitRequestTO() {
    }

    public BenefitRequestTO(BenefitRequestItem item) {
        this.id = item.getObjectID();
        this.date = WrapUtils.dateToLong(item.getDate());
        this.status = new SelectItemTO(item.getStatus());
        this.requestQuantity = item.getRequestedQuantity();
        if (item.getRequesterID() != null) {
            EmployeeTO employeeTO = new EmployeeTO();
            employeeTO.setId(item.getRequesterID());
            employeeTO.setName(item.getRequester());
            this.requester = employeeTO;
        }
        if (item.getApproverID() != null) {
            EmployeeTO approverTO = new EmployeeTO();
            approverTO.setId(item.getApproverID());
            approverTO.setName(item.getApprover());
            this.approver = approverTO;
        }
        if (item.getBenefitID() != null) {
            BenefitTypeTO benefitTypeTO = new BenefitTypeTO();
            benefitTypeTO.setId(item.getBenefitID());
            benefitTypeTO.setName(item.getBenefitName());
            benefitTypeTO.setCurrency(item.getBenefitCurrency() != null ? new CurrencyTO(item.getBenefitCurrency()) : null);
            benefitTypeTO.setQuantityType(WrapUtils.wrapSelectItemTO(item.getQuantityType()));
            this.benefitType = benefitTypeTO;
        }
    }

    public BenefitRequestTO(BenefitRequestItem item, boolean fullData) {
        this(item);
        this.description = item.getDescription();
    }

    public BenefitRequestItem wrap(BenefitRequestTO benefitRequestTO) {
        BenefitRequestItem item = new BenefitRequestItem();
        item.setObjectID(benefitRequestTO.getId());
        item.setRequesterID(benefitRequestTO.getRequester().getId());
        item.setApproverID(benefitRequestTO.getApprover().getId());
        item.setRequestedQuantity(benefitRequestTO.getRequestQuantity());
        item.setDescription(benefitRequestTO.getDescription());
        item.setDate(new DateNonConvertable(WrapUtils.longToDate(benefitRequestTO.getDate())));
        if (benefitRequestTO.getBenefitType() != null) {
            item.setBenefitID(benefitRequestTO.getBenefitType().getId());
        }
        item.setStatus(WrapUtils.wrapSelectItem(benefitRequestTO.getStatus()));
        item.setBenefitID(benefitRequestTO.getBenefitType().getId());
        return item;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public EmployeeTO getRequester() {
        return requester;
    }

    public void setRequester(EmployeeTO requester) {
        this.requester = requester;
    }

    public EmployeeTO getApprover() {
        return approver;
    }

    public void setApprover(EmployeeTO approver) {
        this.approver = approver;
    }

    public BenefitTypeTO getBenefitType() {
        return benefitType;
    }

    public void setBenefitType(BenefitTypeTO benefitType) {
        this.benefitType = benefitType;
    }

    public Double getRequestQuantity() {
        return requestQuantity;
    }

    public void setRequestQuantity(Double requestQuantity) {
        this.requestQuantity = requestQuantity;
    }

    public SelectItemTO getStatus() {
        return status;
    }

    public void setStatus(SelectItemTO status) {
        this.status = status;
    }

    public String getTotalUsedRequest() {
        return totalUsedRequest;
    }

    public void setTotalUsedRequest(String totalUsedRequest) {
        this.totalUsedRequest = totalUsedRequest;
    }

    public String getTotalLeftRequest() {
        return totalLeftRequest;
    }

    public void setTotalLeftRequest(String totalLeftRequest) {
        this.totalLeftRequest = totalLeftRequest;
    }

    public String getQuantityType() {
        return quantityType;
    }

    public void setQuantityType(String quantityType) {
        this.quantityType = quantityType;
    }
}
