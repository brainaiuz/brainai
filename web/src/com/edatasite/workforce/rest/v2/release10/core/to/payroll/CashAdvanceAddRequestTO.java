package com.edatasite.workforce.rest.v2.release10.core.to.payroll;

import com.edatasite.workforce.rest.v2.release10.core.to.base.AttachmentTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.ArrayList;

public class CashAdvanceAddRequestTO extends ResponseData {
    private Integer id;
    private Integer requester_id;
    private Integer category;
    private BigDecimal requested_amount;
    private Integer payment_terms;
    private BigDecimal payment_amount;
    @Schema(description = "Leave start DateTime (format: dd-MM-yyyy'T'hh:mm:ssZ)")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy'T'hh:mm:ssZ")
    private String date;
    private Integer payment_method;
    private ArrayList<Integer> approvers;
    private String reference;
    private String purpose;
    private ArrayList<AttachmentTO> draft_attachments;

    public CashAdvanceAddRequestTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRequester_id() {
        return requester_id;
    }

    public void setRequester_id(Integer requester_id) {
        this.requester_id = requester_id;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public BigDecimal getRequested_amount() {
        return requested_amount;
    }

    public void setRequested_amount(BigDecimal requested_amount) {
        this.requested_amount = requested_amount;
    }

    public Integer getPayment_terms() {
        return payment_terms;
    }

    public void setPayment_terms(Integer payment_terms) {
        this.payment_terms = payment_terms;
    }

    public BigDecimal getPayment_amount() {
        return payment_amount;
    }

    public void setPayment_amount(BigDecimal payment_amount) {
        this.payment_amount = payment_amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(Integer payment_method) {
        this.payment_method = payment_method;
    }

    public ArrayList<Integer> getApprovers() {
        return approvers;
    }

    public void setApprovers(ArrayList<Integer> approvers) {
        this.approvers = approvers;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public ArrayList<AttachmentTO> getDraft_attachments() {
        return draft_attachments;
    }

    public void setDraft_attachments(ArrayList<AttachmentTO> draft_attachments) {
        this.draft_attachments = draft_attachments;
    }
}
