package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.*;
import com.edatasite.workforce.core.domain.approving.EdsApprovable;
import com.edatasite.workforce.core.domain.approving.EdsApprover;
import com.edatasite.workforce.core.domain.customform.EdsModelField;
import com.edatasite.workforce.core.domain.pdf.EdsCompanyPdfTemplate;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.StaticContextAccessor;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.domain.ObjectHistory;
import com.google.common.collect.Lists;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.edatasite.workforce.gwt.core.server.app.Utils.isOk;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 02.09.2010
 * Time: 9:51:06
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "manualjournal")
public class EdsManualJournal extends EdsApprovable implements ObjectHistory {

    public static final String DRAFT = "DRAFT";
    public static final String POST = "POST";
    public static final String REVERSED = "REVERSED";
    public static final String APPROVED = "APPROVED";
    public static final String REJECTED = "REJECTED";
    public static final String SUBMITTED = "SUBMITTED";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Type(type = "text")
    private String narration;

    private Date date;
    private Date creationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creatorId")
    private EdsUser creator;

    private Integer intNumber;
    @Column(name = "number")
    private String number;

    private String reference;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currencyid")
    @ForeignKey(name = "none")
    private EdsCurrency currency;

    @Column(precision = 25, scale = 15)
    private BigDecimal exchangeRate;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "manualjournalid")
    private List<EdsManualJournalItem> items = new ArrayList<>();

    private Boolean showOnCashReports;

    @Column(name = "deleted")
    private Boolean deleted = false;

    private Boolean isMemorizedTransaction = false;
    private Boolean currencyAdjustment = false;

    private Boolean recurringTemplate = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "senderId")
    private EdsUser sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectid")
    private EdsProject project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roleId")
    private EdsRole role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pdfTemplateId")
    private EdsCompanyPdfTemplate pdfTemplate;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "entityID", fetch = FetchType.LAZY)
    @Where(clause = "entityType = 'manualjournal' AND (deleted = 'false' or deleted is null) ")
    @OrderBy(value = "approverOrder ASC")
    @org.hibernate.annotations.ForeignKey(name = "none")
    private List<EdsApprover> approvers = Lists.newArrayList();

    @Transient
    List<CompanyCustomFieldItem> itemCustomFields;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        if (!ServerUtils.equalsString(this.narration, narration)) {
            addChange(CustomFormConstants.ACCOUNTING.MANUAL_JOURNAL.NARRATION);
        }
        this.narration = narration;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        if (!ServerUtils.equalsDate(this.date, date)) {
            addChange(CustomFormConstants.ACCOUNTING.MANUAL_JOURNAL.DATE);
        }
        this.date = date;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        if (!ServerUtils.equalsString(this.reference, reference)) {
            addChange(CustomFormConstants.ACCOUNTING.MANUAL_JOURNAL.REFERENCE);
        }
        this.reference = reference;
    }

    public EdsCurrency getCurrency() {
        return currency;
    }

    public void setCurrency(EdsCurrency currency) {
        if (!ServerUtils.equalsEdsObject(this.currency, currency)) {
            addChange(CustomFormConstants.ACCOUNTING.MANUAL_JOURNAL.CURRENCY);
        }
        this.currency = currency;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public List<EdsManualJournalItem> getItems() {
        return items;
    }

    public void setItems(List<EdsManualJournalItem> items) {
        this.items = items;
    }

    public Boolean isShowOnCashReports() {
        return showOnCashReports != null ? showOnCashReports : false;
    }

    public void setShowOnCashReports(Boolean showOnCashReports) {
        this.showOnCashReports = showOnCashReports;
    }

    public void addItem(EdsManualJournalItem item) {
        item.setManualTransfer(this);
        items.add(item);
    }

    public Boolean isDeleted() {
        return deleted == null ? Boolean.FALSE : deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Boolean isMemorizedTransaction() {
        return isMemorizedTransaction != null ? isMemorizedTransaction : false;
    }

    public void setMemorizedTransaction(Boolean memorizedTransaction) {
        isMemorizedTransaction = memorizedTransaction;
    }

    public Boolean isCurrencyAdjustment() {
        return currencyAdjustment != null ? currencyAdjustment : false;
    }

    public void setCurrencyAdjustment(Boolean currencyAdjustment) {
        this.currencyAdjustment = currencyAdjustment;
    }

    public Integer getIntNumber() {
        return intNumber;
    }

    public void setIntNumber(Integer intNumber) {
        this.intNumber = intNumber;
    }

    public Boolean isRecurringTemplate() {
        return recurringTemplate != null ? recurringTemplate : false;
    }

    public void setRecurringTemplate(Boolean recurringTemplate) {
        this.recurringTemplate = recurringTemplate;
    }

    public EdsUser getSender() {
        return sender;
    }

    public void setSender(EdsUser sender) {
        this.sender = sender;
    }

    public EdsProject getProject() {
        return project;
    }

    public void setProject(EdsProject project) {
        this.project = project;
    }

    public EdsRole getRole() {
        return role;
    }

    public void setRole(EdsRole role) {
        this.role = role;
    }

    public EdsCompanyPdfTemplate getPdfTemplate() {
        return pdfTemplate;
    }

    public void setPdfTemplate(EdsCompanyPdfTemplate pdfTemplate) {
        this.pdfTemplate = pdfTemplate;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        if (!ServerUtils.equalsString(this.number, number)) {
            addChange(CustomFormConstants.ACCOUNTING.MANUAL_JOURNAL.NUMBER);
        }
        this.number = number;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public EdsUser getCreator() {
        return creator;
    }

    @Override
    public void setLastUpdateTime(Date value) {

    }

    @Override
    public void setUpdater(EdsUser user) {

    }

    @Override
    public void setCreationTime(Date value) {
        this.creationDate = value;
    }

    @Override
    public void setCreator(EdsUser creator) {
        if (!ServerUtils.equalsEdsObject(this.creator, creator)) {
            addChange(CustomFormConstants.ACCOUNTING.MANUAL_JOURNAL.CREATOR);
        }
        this.creator = creator;
    }

    @Override
    public List<EdsApprover> getApprovers() {
        return approvers;
    }

    @Override
    public void setApprovers(List<EdsApprover> approvers) {
        this.approvers = approvers;
    }

    public List<CompanyCustomFieldItem> getItemCustomFields() {
        return itemCustomFields;
    }

    public void setItemCustomFields(List<CompanyCustomFieldItem> itemCustomFields) {
        this.itemCustomFields = itemCustomFields;
    }

    @Override
    public void setEntityStatus(EdsReference overallStatus) {
        setOverallStatus(overallStatus);
    }

    @Override
    public boolean isCurrentApproverApproved() {
        return isOk(getCurrentApprover()) && isOk(getCurrentApprover().getStatus()) && APPROVED.equals(getCurrentApprover().getStatus().getCode());
    }

    @Override
    public boolean isCurrentApproverRejected() {
        return isOk(getCurrentApprover()) && isOk(getCurrentApprover().getStatus()) && REJECTED.equals(getCurrentApprover().getStatus().getCode());
    }

    @Override
    protected EdsReference getStatusByMarkedAction(Integer actionId) {
        if (!isOk(actionId)) {
            return null;
        }
        ReferenceManager referenceManager = StaticContextAccessor.getBean(ReferenceManager.class);
        if (actionId.equals(ApproverItem.MARK_AS_REJECTED)) {
            return referenceManager.findReference(Constants.MANUAL_JOURNAL_STATUS, REJECTED);
        } else if (actionId.equals(ApproverItem.MARK_AS_APPROVED)) {
            return referenceManager.findReference(Constants.MANUAL_JOURNAL_STATUS, APPROVED);
        } else if (actionId.equals(ApproverItem.SEND_TO_CREATOR)) {
            return referenceManager.findReference(Constants.MANUAL_JOURNAL_STATUS, REJECTED);
        } else if (actionId.equals(ApproverItem.SEND_TO_DIRECTORS)) {
            return referenceManager.findReference(Constants.MANUAL_JOURNAL_STATUS, REJECTED);
        }
        return null;
    }

    @Override
    public void updateRejectedStatus() {
        if (getOverallStatus() != null && REJECTED.equals(getOverallStatus().getCode())) {
            ReferenceManager referenceManager = StaticContextAccessor.getBean(ReferenceManager.class);
            setEntityStatus(referenceManager.findReference(Constants.MANUAL_JOURNAL_STATUS, SUBMITTED));
        }
    }

    @Override
    public void jumpToPreviousApprover() {
        super.jumpToPreviousApprover();
    }

    @Override
    public Object getRealValue(String fieldID) {
        if (fieldID == null) {
            return null;
        }
        return switch (fieldID) {
            case CustomFormConstants.ACCOUNTING.MANUAL_JOURNAL.NARRATION -> getNarration();
            case CustomFormConstants.ACCOUNTING.MANUAL_JOURNAL.DATE -> getDate();
            case CustomFormConstants.ACCOUNTING.MANUAL_JOURNAL.REFERENCE -> getReference();
            case CustomFormConstants.ACCOUNTING.MANUAL_JOURNAL.NUMBER -> getNumber();
            case CustomFormConstants.ACCOUNTING.MANUAL_JOURNAL.CURRENCY -> getCurrency();
            case CustomFormConstants.ACCOUNTING.MANUAL_JOURNAL.CREATOR -> getCreator();
            default -> super.getRealValue(fieldID);
        };
    }

    @Override
    protected String getStringValueByFieldID(String realFieldID) {
        return super.getStringValueByFieldID(realFieldID);
    }

    @Override
    public void setValueForField(EdsModelField field, Object value) {
        if (field != null && field.getField_ID() != null) {
            String fieldId = field.getField_ID();
            switch (fieldId) {
                case CustomFormConstants.ACCOUNTING.MANUAL_JOURNAL.NARRATION -> setNarration((String) value);
                case CustomFormConstants.ACCOUNTING.MANUAL_JOURNAL.DATE -> setDate((Date) value);
                case CustomFormConstants.ACCOUNTING.MANUAL_JOURNAL.REFERENCE -> setReference((String) value);
            }
        }
        super.setValueForField(field, value);
    }
}
