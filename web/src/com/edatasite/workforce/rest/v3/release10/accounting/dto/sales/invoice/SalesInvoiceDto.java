package com.edatasite.workforce.rest.v3.release10.accounting.dto.sales.invoice;

import com.edatasite.workforce.rest.v2.release10.core.to.base.IdNameTO;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.ApproverTO;
import com.edatasite.workforce.rest.v3.release10.core.to.IdCode;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class SalesInvoiceDto {
    private Integer id;
    private List<Integer> approverIds = List.of();
    private Integer clientId;
    private IdCode client;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private Date invoiceDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private Date dueDate;
    private BigDecimal total;
    private BigDecimal subtotal;
    private BigDecimal dueAmount;
    private BigDecimal paidAmount;
    private String description;
    private String numberString;
    private List<ApproverTO> approvers = List.of();
    private List<Integer> productIds = List.of();
    private List<IdNameTO> items = List.of();
    private IdCode status;
    private IdNameTO currency;
    private String type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Integer> getApproverIds() {
        return approverIds;
    }

    public void setApproverIds(List<Integer> approverIds) {
        this.approverIds = approverIds;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public IdCode getClient() {
        return client;
    }

    public void setClient(IdCode client) {
        this.client = client;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date  invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getDueAmount() {
        return dueAmount;
    }

    public void setDueAmount(BigDecimal dueAmount) {
        this.dueAmount = dueAmount;
    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getNumberString() {
        return numberString;
    }

    public void setNumberString(String numberString) {
        this.numberString = numberString;
    }

    public List<ApproverTO> getApprovers() {
        return approvers;
    }

    public void setApprovers(List<ApproverTO> approvers) {
        this.approvers = approvers;
    }

    public List<Integer> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<Integer> productIds) {
        this.productIds = productIds;
    }

    public List<IdNameTO> getItems() {
        return items;
    }

    public void setItems(List<IdNameTO> items) {
        this.items = items;
    }

    public IdCode getStatus() {
        return status;
    }

    public void setStatus(IdCode status) {
        this.status = status;
    }

    public void setCurrency(IdNameTO currency) {
        this.currency = currency;
    }

    public IdNameTO getCurrency() {
        return currency;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
