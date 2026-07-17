package com.edatasite.workforce.rest.v2.release10.core.to.payroll;

import com.edatasite.workforce.rest.v2.release10.core.to.base.AttachmentTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.CategoryTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.CurrencyValueTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.EmployeeTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.leaverequest.RequestUserActionTO;

import java.util.ArrayList;

/**
 * Created by Abdurakhmonov Farrukh on 6/12/2017.
 */
public class CashAdvanceDetailsTO extends ResponseData {
    private Integer id;
    private String number;
    private EmployeeTO owner;
    private Object status;
    private CategoryTO category;
    private CurrencyValueTO requested_amount;
    private CategoryTO payment_terms;
    private CurrencyValueTO payment_amount;
    private String date;
    private CategoryTO payment_method;
    private ArrayList<ApproversTO> approvers;
    private String reference;
    private String purpose;
    private ArrayList<AttachmentTO> attachments;
    private RequestUserActionTO user_actions;

    public CashAdvanceDetailsTO() {
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public EmployeeTO getOwner() {
        return owner;
    }

    public void setOwner(EmployeeTO owner) {
        this.owner = owner;
    }

    public Object getStatus() {
        return status;
    }

    public void setStatus(Object status) {
        this.status = status;
    }

    public CategoryTO getCategory() {
        return category;
    }

    public void setCategory(CategoryTO category) {
        this.category = category;
    }

    public CurrencyValueTO getRequested_amount() {
        return requested_amount;
    }

    public void setRequested_amount(CurrencyValueTO requested_amount) {
        this.requested_amount = requested_amount;
    }

    public CategoryTO getPayment_terms() {
        return payment_terms;
    }

    public void setPayment_terms(CategoryTO payment_terms) {
        this.payment_terms = payment_terms;
    }

    public CurrencyValueTO getPayment_amount() {
        return payment_amount;
    }

    public void setPayment_amount(CurrencyValueTO payment_amount) {
        this.payment_amount = payment_amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public CategoryTO getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(CategoryTO payment_method) {
        this.payment_method = payment_method;
    }

    public ArrayList<ApproversTO> getApprovers() {
        return approvers;
    }

    public void setApprovers(ArrayList<ApproversTO> approvers) {
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

    public ArrayList<AttachmentTO> getAttachments() {
        return attachments;
    }

    public void setAttachments(ArrayList<AttachmentTO> attachments) {
        this.attachments = attachments;
    }

    public RequestUserActionTO getUser_actions() {
        return user_actions;
    }

    public void setUser_actions(RequestUserActionTO user_actions) {
        this.user_actions = user_actions;
    }
}
