package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: May 12, 2009
 * Time: 5:15:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class ManualJournalListItem implements IsSerializable {

    public static final String ACTION = "action";
    public static final String NARRATION = "narration";
    public static final String DATE = "date";
    public static final String DEBIT = "debit";
    public static final String CRETID = "credit";
    public static final String STATUS = "status";
    public static final String REFERENCENUMBER = "reference";
    public static final String TYPE = "type";
    public static final String REPEATS = "repeats";
    public static final String NEXTCREATIONDATE = "nextcreationdate";
    public static final String ENDDATE = "enddate";
    public static final String NUMBER = "number";
    public static final String PROJECT = "project";
    public static final String CREATOR = "creator";
    public static final String CURRENCY = "currency";
    public static final String APPROVER = "approver";
    private Integer objectId;

    private String narration;
    private DateNonConvertable date;
    private BigDecimal debit;
    private BigDecimal credit;
    private String currency;
    private String status;
    private boolean isUsed = false;
    private String referenceNumber;
    private String number;

    private boolean recurringTemplate;
    private String repeats;
    private Date nextCreationDate;
    private Date endDate;
    private String recurrenceStatus;
    private String type;
    private String project;
    private String creator;
    private Integer creatorId;
    private boolean approver;
    private String currentApprover;
    private boolean isSetupAP; //approval process

    public ManualJournalListItem() {

    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public DateNonConvertable getDate() {
        return date;
    }

    public void setDate(DateNonConvertable date) {
        this.date = date;
    }

    public BigDecimal getDebit() {
        return debit;
    }

    public void setDebit(BigDecimal debit) {
        this.debit = debit;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public boolean isRecurringTemplate() {
        return recurringTemplate;
    }

    public void setRecurringTemplate(boolean recurringTemplate) {
        this.recurringTemplate = recurringTemplate;
    }

    public String getRepeats() {
        return repeats;
    }

    public void setRepeats(String repeats) {
        this.repeats = repeats;
    }

    public Date getNextCreationDate() {
        return nextCreationDate;
    }

    public void setNextCreationDate(Date nextCreationDate) {
        this.nextCreationDate = nextCreationDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getRecurrenceStatus() {
        return recurrenceStatus;
    }

    public void setRecurrenceStatus(String recurrenceStatus) {
        this.recurrenceStatus = recurrenceStatus;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {return type;}

    public void setType(String type) {this.type = type;}

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Integer getCreatorId() {
        return this.creatorId;
    }

    public void setCreatorId(final Integer creatorId) {
        this.creatorId = creatorId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public boolean isApprover() {
        return approver;
    }

    public void setApprover(boolean approver) {
        this.approver = approver;
    }

    public String getCurrentApprover() {
        return currentApprover;
    }

    public void setCurrentApprover(String currentApprover) {
        this.currentApprover = currentApprover;
    }

    public boolean isSetupAP() {
        return isSetupAP;
    }

    public void setSetupAP(boolean setupAP) {
        isSetupAP = setupAP;
    }
}
