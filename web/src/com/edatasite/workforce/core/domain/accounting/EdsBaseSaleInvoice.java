package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.approving.EdsApprover;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.EdsCustomCrmAccount;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 06.05.2010
 * Time: 21:07:42
 * To change this template use File | Settings | File Templates.
 */
@MappedSuperclass
public class EdsBaseSaleInvoice extends EdsInvoice {
    //Recurring period
    public static final String _RECURRING_PERIOD = "_RECURRING_PERIOD";
    public static final String DAILY = "DAILY";
    public static final String WEEKLY = "WEEKLY";
    public static final String MONTHLY = "MONTHLY";
    public static final String YEARLY = "YEARLY";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private EdsCrmAccount client;

    private Integer fourDigitNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recurrence_pattern")
    private EdsReference recurrence_pattern;

    @JoinColumn(name = "recurrence_number")
    private Integer recurrence_number = 1;

    @Type(type = "text")
    private String quoteNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bankaccountid")
    private EdsBankAccount bankAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shippingMethodId")
    private EdsShippingMethod shippingMethod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crmcontact_Id")
    private EdsCrmContact contact;

    @Column(precision = 25, scale = 5)
    private BigDecimal shippingAmount;

    private Integer paymentInstructionID;

    public EdsShippingMethod getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(EdsShippingMethod shippingMethod) {
        if (!ServerUtils.equalsEdsObject(this.shippingMethod, shippingMethod)) {
            addChange(CustomFormConstants.ACCOUNTING.SHIP_VIA);
        }
        this.shippingMethod = shippingMethod;
    }

    public void setClient(EdsCrmAccount client) {
        if(!ServerUtils.equalsEdsObject(this.client, client)){
            addChange(CustomFormConstants.ACCOUNTING.CUSTOMER);
        }
        this.client = client;
    }

    public EdsCrmAccount getClient() {
        return client;
    }

    public EdsCrmAccount getClientOrSupplier() {
        return client;
    }

    public Integer getFourDigitNumber() {
        return fourDigitNumber;
    }

    public void setFourDigitNumber(Integer fourDigitNumber) {
        this.fourDigitNumber = fourDigitNumber;
    }


    public EdsReference getRecurrence_pattern() {
        return recurrence_pattern;
    }

    public void setRecurrence_pattern(EdsReference recurrence_pattern) {
        this.recurrence_pattern = recurrence_pattern;
    }

    public Integer getRecurrence_number() {
        return recurrence_number;
    }

    public void setRecurrence_number(Integer recurrence_number) {
        if (recurrence_number != null) {
            this.recurrence_number = recurrence_number;
        }
    }

    public EdsBankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(EdsBankAccount bankAccount) {
        if (!ServerUtils.equalsEdsObject(this.bankAccount, bankAccount)) {
            addChange(CustomFormConstants.ACCOUNTING.BANK_ACCOUNT);
        }
        this.bankAccount = bankAccount;
    }

    public EdsCrmContact getContact() {
        return contact;
    }

    public void setContact(EdsCrmContact contact) {
        this.contact = contact;
    }

    public String getQuoteNumber() {
        return quoteNumber;
    }

    public void setQuoteNumber(String quoteNumber) {
        if(!ServerUtils.equalsString(this.quoteNumber, quoteNumber)){
            addChange(CustomFormConstants.ACCOUNTING.SQ_NUMBER);
        }
        this.quoteNumber = quoteNumber;
    }

    public Integer getPaymentInstructionID() {
        return paymentInstructionID;
    }

    public void setPaymentInstructionID(Integer paymentInstructionID) {
        this.paymentInstructionID = paymentInstructionID;
    }

    public BigDecimal getShippingAmount() {
        return shippingAmount;
    }

    public void setShippingAmount(BigDecimal shippingAmount) {
        this.shippingAmount = shippingAmount;
    }

    @Override
    public void setEntityStatus(EdsReference overallStatus) {
        if (!ServerUtils.equalsReference(getOverallStatus(), overallStatus)) {
            addChange(CustomFormConstants.STATUS);
        }
        setOverallStatus(overallStatus);
    }

    @Override
    public List<EdsApprover> getApprovers() {
        return null;
    }

    @Override
    public void setApprovers(List<EdsApprover> approvers) {

    }

    @Override
    public boolean isCurrentApproverApproved() {
        return false;
    }

    @Override
    public boolean isCurrentApproverRejected() {
        return false;
    }

    @Override
    protected EdsReference getStatusByMarkedAction(Integer integer) {
        return null;
    }

}
