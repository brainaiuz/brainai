package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.approving.EdsApprover;
import com.edatasite.workforce.core.domain.customform.EdsModelField;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrPurchaseInvoiceRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrSaleInvoiceRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.StaticContextAccessor;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.solr.common.SolrInputDocument;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.*;

import static com.edatasite.workforce.gwt.core.server.app.Utils.isOk;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "purchaseinvoice")
public class EdsPurchaseInvoice extends EdsBasePurchaseInvoice {

    private Integer recurringBillID;
    private Integer fourDigitNumber;
    @Column(name = "opportunityID")
    private Integer opportunityID;

    @Column(name = "billofentry_id")
    private Integer billOfEntryId;
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "billofentry_id", updatable = false, insertable = false)
    private EdsBillOfEntry billOfEntry;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "entityID", fetch = FetchType.LAZY)
    @Where(clause = "entityType = 'TYPE_PURCHASE_INVOICE' or entityType = 'TYPE_DEBIT_NOTE'")
    @OrderBy(value = "approverOrder ASC")
    @org.hibernate.annotations.ForeignKey(name = "none")
    private List<EdsApprover> approvers = new ArrayList<>();

    public Integer getRecurringBillID() {
        return recurringBillID;
    }

    public void setRecurringBillID(Integer recurringBillID) {
        this.recurringBillID = recurringBillID;
    }

    public Integer getFourDigitNumber() {
        return fourDigitNumber;
    }

    public void setFourDigitNumber(Integer fourDigitNumber) {
        this.fourDigitNumber = fourDigitNumber;
    }

    public void setData(EdsInvoice invoice) {
        setSupplier(invoice.getClientOrSupplier());
        super.setData(invoice);
        setType(Constants.PAYABLE);
    }

    @Override
    public void setEntityStatus(EdsReference overallStatus) {
        setStatus(overallStatus);
        setOverallStatus(overallStatus);
    }

    @Override
    public List<EdsApprover> getApprovers() {
        return approvers;
    }

    @Override
    public void setApprovers(List<EdsApprover> approvers) {
        this.approvers = approvers;
    }

    @Override
    public boolean isCurrentApproverApproved() {
        return isOk(getCurrentApprover()) && isOk(getCurrentApprover().getStatus()) && (Constants.APPROVE.equals(getCurrentApprover().getStatus().getCode()));
    }

    @Override
    public boolean isCurrentApproverRejected() {
        return isOk(getCurrentApprover()) && isOk(getCurrentApprover().getStatus()) && Constants.MANAGER_REJECT.equals(getCurrentApprover().getStatus().getCode());
    }

    @Override
    protected EdsReference getStatusByMarkedAction(Integer actionID) {
        if (!isOk(actionID)) {
            return null;
        }
        ReferenceManager referenceManager = StaticContextAccessor.getBean(ReferenceManager.class);
        if (actionID.equals(ApproverItem.MARK_AS_REJECTED)) {
            return referenceManager.findReference(Constants.INVOICE_STATUS, Constants.MANAGER_REJECT);
        } else if (actionID.equals(ApproverItem.MARK_AS_APPROVED)) {
            return hasInventoryItem() && !isConvertedToGdnGrn() ?
                    referenceManager.findReference(Constants.INVOICE_STATUS, Constants.PENDING) : referenceManager.findReference(Constants.INVOICE_STATUS, Constants.APPROVE);
        } else if (actionID.equals(ApproverItem.SEND_TO_CREATOR)) {
            return referenceManager.findReference(Constants.INVOICE_STATUS, Constants.MANAGER_REJECT);
        } else if (actionID.equals(ApproverItem.SEND_TO_DIRECTORS)) {
            return referenceManager.findReference(Constants.INVOICE_STATUS, Constants.MANAGER_REJECT);
        }
        return null;
    }

    @Override
    public void updateRejectedStatus() {
        if (getStatus() != null && Constants.MANAGER_REJECT.equals(getStatus().getCode())) {
            ReferenceManager referenceManager = StaticContextAccessor.getBean(ReferenceManager.class);
            setEntityStatus(referenceManager.findReference(Constants.INVOICE_STATUS, Constants.SUBMITTED_TO_MANAGER));
        }
    }

    public Integer getOpportunityID() {
        return this.opportunityID;
    }

    public void setOpportunityID(final Integer opportunityID) {
        this.opportunityID = opportunityID;
    }

    public SolrInputDocument wrapToSolrDocument(EdsPurchaseInvoice purchaseInvoice, Integer companyID) {
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField(SolrPurchaseInvoiceRepresenter.FIELD_COMPANY_ID, companyID);
        doc.addField(SolrPurchaseInvoiceRepresenter.FIELD_COMPOSITE_ID, companyID + "_" + purchaseInvoice.getObjectID());
        doc.addField(SolrPurchaseInvoiceRepresenter.FIELD_PURCHASEINVOICE_ID, purchaseInvoice.getObjectID());
        doc.addField(SolrPurchaseInvoiceRepresenter.FIELD_PURCHASEINVOICE_NUMBER, purchaseInvoice.getNumber());
        doc.addField(SolrPurchaseInvoiceRepresenter.FIELD_INVOICE_DATE, purchaseInvoice.getInvoiceDate());
        doc.addField(SolrPurchaseInvoiceRepresenter.FIELD_DUE_DATE, purchaseInvoice.getDueDate());
        doc.addField(SolrPurchaseInvoiceRepresenter.FIELD_IS_CREDIT_NOTE, purchaseInvoice.isCreditNote());
        doc.addField(SolrPurchaseInvoiceRepresenter.HAS_PAYMENT, getPaymentItemsList(this).size() > 0);
        doc.addField(SolrPurchaseInvoiceRepresenter.FIELD_TOTAL_IN_INVOICE_CURRENCY, purchaseInvoice.getTotalInInvoiceCurrency().doubleValue());
        doc.addField(SolrSaleInvoiceRepresenter.FIELD_TOTAL_INVOICE_BASE, purchaseInvoice.getTotal().doubleValue());
        doc.addField(SolrSaleInvoiceRepresenter.FIELD_OPPORTUNITY_ID, purchaseInvoice.getOpportunityID());
        doc.addField(SolrPurchaseInvoiceRepresenter.FIELD_PURCHASEINVOICE_TOTAL_TAXES, purchaseInvoice.getTotalTaxes().doubleValue());
        doc.addField(SolrPurchaseInvoiceRepresenter.FIELD_PURCHASEINVOICE_EXCHANGE_RATE, purchaseInvoice.getExchangeRate().doubleValue());
        doc.addField(SolrPurchaseInvoiceRepresenter.FIELD_PURCHASEINVOICE_TAX_CALCULATION_TYPE, purchaseInvoice.getTaxCalculationType());
        doc.addField(SolrPurchaseInvoiceRepresenter.FIELD_INVOICE_TYPE, purchaseInvoice.getType());
        if (getRelatedProject() != null) {
            doc.addField(SolrPurchaseInvoiceRepresenter.FIELD_RELATED_PROJECT_ID, purchaseInvoice.getRelatedProject().getObjectID());
            doc.addField(SolrPurchaseInvoiceRepresenter.FIELD_RELATED_PROJECT_NAME, purchaseInvoice.getRelatedProject().getName());
            doc.addField(SolrPurchaseInvoiceRepresenter.FIELD_RELATED_PROJECT_NUMBER, purchaseInvoice.getRelatedProject().getNumber());
            doc.addField(SolrPurchaseInvoiceRepresenter.FIELD_RELATED_PROJECT_ID_NAME, purchaseInvoice.getRelatedProject().getObjectID() + SolrPurchaseInvoiceRepresenter.SPLIT + purchaseInvoice.getRelatedProject().getName());
            doc.addField(SolrPurchaseInvoiceRepresenter.FIELD_PURCHASEINVOICE_RELATED_PROJECT_STATUS_CODE, purchaseInvoice.getRelatedProject().getStatus().getCode());
        }

        for (EdsProject project : purchaseInvoice.getProjects()) {
            doc.addField(SolrPurchaseInvoiceRepresenter.FIELD_MULTI_PROJECT_ID, project.getObjectID());
            doc.addField(SolrPurchaseInvoiceRepresenter.FIELD_MULTI_PROJECT_NAME, project.getName());
            doc.addField(SolrPurchaseInvoiceRepresenter.FIELD_MULTI_PROJECT_NUMBER, project.getNumber());
            doc.addField(SolrPurchaseInvoiceRepresenter.FIELD_MULTI_PROJECT_ID_NAME, project.getObjectID() + SolrSaleInvoiceRepresenter.SPLIT + project.getName());
            doc.addField(SolrPurchaseInvoiceRepresenter.FIELD_MULTI_PROJECT_NUMBER_NAME, project.getNumber() + SolrPurchaseInvoiceRepresenter.ARROW + project.getName());
        }

        if (getSupplier() != null) {
            doc.addField(SolrPurchaseInvoiceRepresenter.FIELD_CLIENT_ID, purchaseInvoice.getSupplier().getObjectID());
            doc.addField(SolrPurchaseInvoiceRepresenter.FIELD_CLIENT_NAME, purchaseInvoice.getSupplier().getName());
            doc.addField(SolrPurchaseInvoiceRepresenter.FIELD_CLIENT_ID_NAME, purchaseInvoice.getSupplier().getObjectID() + SolrPurchaseInvoiceRepresenter.SPLIT + purchaseInvoice.getSupplier().getName());
            doc.addField(SolrPurchaseInvoiceRepresenter.FIELD_PURCHASEINVOICE_SUPPLIER_VAT_NUMBER, purchaseInvoice.getSupplier().getVatNumber());
            doc.addField(SolrPurchaseInvoiceRepresenter.FIELD_PURCHASEINVOICE_SUPPLIER_TRN, purchaseInvoice.getSupplier().getTrn());
        }

        if (getCreator() != null) {
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_CREATOR_ID, getCreator().getObjectID());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_CREATOR_NAME, getCreator().getName());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_CREATOR_ID_NAME, getCreator().getObjectID() + SolrSaleInvoiceRepresenter.SPLIT + getCreator().getName());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_CREATOR_FULL_NAME, getCreator().getFullName());
        }

        if (getCurrency() != null) {
            doc.addField(SolrPurchaseInvoiceRepresenter.FIELD_CURRENCY_ID, purchaseInvoice.getCurrency().getObjectID());
            doc.addField(SolrPurchaseInvoiceRepresenter.FIELD_CURRENCY_NAME, purchaseInvoice.getCurrency().getName());
            doc.addField(SolrPurchaseInvoiceRepresenter.FIELD_CURRENCY_ID_NAME, purchaseInvoice.getCurrency().getObjectID() + SolrPurchaseInvoiceRepresenter.SPLIT + purchaseInvoice.getCurrency().getName());
        }
        doc.addField(SolrPurchaseInvoiceRepresenter.FIELD_DUE_AMOUNT, purchaseInvoice.getDueAmount().doubleValue());
        doc.addField(SolrPurchaseInvoiceRepresenter.FIELD_PAID_AMOUNT, purchaseInvoice.getFullPayments().doubleValue());

        if (getStatus() != null) {
            doc.addField(SolrPurchaseInvoiceRepresenter.FIELD_STATUS_ID, purchaseInvoice.getStatus().getObjectID());
            doc.addField(SolrPurchaseInvoiceRepresenter.FIELD_STATUS_NAME, purchaseInvoice.getStatus().getName());
            doc.addField(SolrPurchaseInvoiceRepresenter.FIELD_STATUS_ID_NAME, purchaseInvoice.getStatus().getObjectID() + SolrPurchaseInvoiceRepresenter.SPLIT + purchaseInvoice.getStatus().getName());
            doc.addField(SolrPurchaseInvoiceRepresenter.FIELD_STATUS_CODE, purchaseInvoice.getStatus().getCode());
            doc.addField(SolrPurchaseInvoiceRepresenter.FIELD_STATUS_SORDER, purchaseInvoice.getStatus().getSorder());
        }

        if (CollectionUtils.isNotEmpty(getInvoiceItems())) {
            getInvoiceItems().stream()
                    .filter(edsInvoiceItem -> edsInvoiceItem.getItem() != null)
                    .forEach(edsInvoiceItem
                            -> {
                        doc.addField(SolrPurchaseInvoiceRepresenter.FIELD_ITEM_ID, edsInvoiceItem.getItem().getObjectID());
                        doc.addField(SolrPurchaseInvoiceRepresenter.FIELD_WAREHOUSE_ID, edsInvoiceItem.getWarehouse()!= null ? edsInvoiceItem.getWarehouse().getObjectID() : null);
                    });

        }

        doc.addField(SolrPurchaseInvoiceRepresenter.FIELD_PO_NUMBER, getPoNumber());
        doc.addField(SolrPurchaseInvoiceRepresenter.FIELD_REFERENCE, getReference());
        if (getCurrentApprover() != null && getCurrentApprover().getExactEmployee() != null) {
            doc.addField(SolrPurchaseInvoiceRepresenter.FIELD_CURRENT_APPROVER_ID, getCurrentApprover().getExactEmployee().getObjectID());
            doc.addField(SolrPurchaseInvoiceRepresenter.FIELD_CURRENT_APPROVER_NAME, getCurrentApprover().getExactEmployee().getFullName());
            doc.addField(SolrPurchaseInvoiceRepresenter.FIELD_CURRENT_APPROVER_ID_NAME, getCurrentApprover().getExactEmployee().getObjectID() + SolrPurchaseInvoiceRepresenter.SPLIT + getCurrentApprover().getExactEmployee().getFullName());
        }
        doc.addField(SolrSaleInvoiceRepresenter.FIELD_CREATED_DATE, purchaseInvoice.getCreationDate());
        doc.addField(SolrSaleInvoiceRepresenter.FIELD_UPDATED_DATE, purchaseInvoice.getUpdatedDate());
        CustomFieldsUtils.setInSolrCustomFields(doc, getCustomFields());

        return doc;
    }

    public Integer getBillOfEntryId() {
        return billOfEntryId;
    }

    public void setBillOfEntryId(Integer billOfEntryId) {
        this.billOfEntryId = billOfEntryId;
    }

    public EdsBillOfEntry getBillOfEntry() {
        return billOfEntry;
    }

    public void setBillOfEntry(EdsBillOfEntry billOfEntry) {
        this.billOfEntry = billOfEntry;
    }

    @Override
    protected String getStringValueByFieldID(String realFieldID) {
        return super.getStringValueByFieldID(realFieldID);
    }

    @Override
    public Object getRealValue(String fieldID) {
        if (fieldID == null) {
            return null;
        }
        String[] values = fieldID.split(",");
        fieldID = values.length >= 2 ? values[0] : fieldID;
        if (fieldID.equals(CustomFormConstants.ACCOUNTING.SUPPLIER)) {
            return getSupplier();
        } else if (fieldID.equals(CustomFormConstants.ACCOUNTING.PROJECT)) {
            return getRelatedProject();
        } else if (fieldID.equals(CustomFormConstants.ACCOUNTING.STATUS)) {
            return getStatus();
        } else if (fieldID.equals(CustomFormConstants.START_DATE)) {
            return getInvoiceDate();
        } else if (fieldID.equals(CustomFormConstants.DUE_DATE)) {
            return getDueDate();
        } else if (fieldID.equals(CustomFormConstants.REFERENCE)) {
            return getReference();
        } else if (fieldID.equals(CustomFormConstants.NUMBER)) {
            return getNumber();
        } else if (fieldID.equals(CustomFormConstants.AMOUNT)) {
            return getTaxCalculationType() == null ? "" : (getTaxCalculationType().equals(0) ? "No Tax" : (getTaxCalculationType().equals(1) ? "Tax Inclusive" : "Tax Exclusive"));
        } else if (fieldID.equals(CustomFormConstants.ACCOUNTING.PO_NUMBER)) {
            return getPoNumber();
        } else if (fieldID.equals(CustomFormConstants.ACCOUNTING.TOTAL_AMOUNT)) {
            return getTotal();
        } else if (fieldID.equals(CustomFormConstants.ACCOUNTING.TOTAL_INVOICE_CURRENCY)) {
            return getTotalInInvoiceCurrency();
        } else if (fieldID.equals(CustomFormConstants.ACCOUNTING.PAID_AMOUNT)) {
            return getFullPayments();
        } else if (fieldID.equals(CustomFormConstants.ACCOUNTING.DUE_AMOUNT_INVOICE_CURRENCY)) {
            return getDueAmount();
        } else if (fieldID.equals(CustomFormConstants.CURRENCY)) {
            return getCurrency();
        } else if (fieldID.equals(CustomFormConstants.ACCOUNTING.ACCOUNT_EMAIL)) {
            return getSupplier() != null ? getSupplier().getEmail() : null;
        } else if (fieldID.equals(CustomFormConstants.ACCOUNTING.CONTACT_EMAIL)) {
            return getSupplier().getPrimaryContact() != null ? getSupplier().getPrimaryContact().getPrimaryEmail() : null;
        } else if (fieldID.equals(CustomFormConstants.CREATED_DATE)) {
            return getCreationDate();
        } else if (fieldID.equals(CustomFormConstants.UPDATED_DATE)) {
            return getModificationDate();
        } else if (fieldID.contains("string_value") || fieldID.contains("double_value") || fieldID.contains("date_value")) {
            return getCustomFields() != null ? CustomFieldsUtils.getObjectValue(getCustomFields(), fieldID) : "";
        }
        return super.getRealValue(fieldID);
    }

    @Override
    public void setValueForField(EdsModelField field, Object value) {
        if (field != null && field.getField_ID() != null) {
            String fieldID = field.getField_ID();
            if (field.isCustomField()) {
                Object ob = CustomFieldsUtils.getObjectValue(getCustomFields(), fieldID);
                if (ob != null) {
                    if (ob instanceof String) {
                        String text = (String) ob;
                        if (!text.equals(value)) {
                            addChange(fieldID);
                        }
                    } else if (ob instanceof Number) {
                        String text = String.valueOf(((Double) ob).intValue());
                        if (!text.equals(value)) {
                            addChange(fieldID);
                        }
                    } else if (ob instanceof Date) {
                        Date date = (Date) ob;
                        if (!date.equals(value)) {
                            addChange(fieldID);
                        }
                    }
                } else {
                    addChange(fieldID);
                }
                Map<String, Object> customFieldsMap = new HashMap<>();
                customFieldsMap.put(fieldID, value);
                CustomFieldsUtils.setDomenObjectFieldChange(getCustomFields(), customFieldsMap, fieldID);
            }
        }
        super.setValueForField(field, value);
    }
}
