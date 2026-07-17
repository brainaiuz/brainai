package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.approving.EdsApprover;
import com.edatasite.workforce.core.domain.crm.EdsCustomCrmAccount;
import com.edatasite.workforce.core.domain.crm.EdsOpportunity;
import com.edatasite.workforce.core.domain.customform.EdsModelField;
import com.edatasite.workforce.core.domain.enums.EntityTypeEnum;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrPurchaseInvoiceRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrSaleInvoiceRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.SolrUtils;
import com.edatasite.workforce.gwt.core.server.app.StaticContextAccessor;
import com.edatasite.workforce.gwt.core.server.db.CustomCrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoiceItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.SaleInvoiceSolrItem;
import org.apache.commons.collections.CollectionUtils;
import org.apache.solr.common.SolrInputDocument;
import org.hibernate.annotations.Where;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.core.server.app.Utils.isOk;

/**
 * Created by IntelliJ IDEA.
 * User: Rat
 * Date: 07.11.2007
 * Time: 12:09:12
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "saleinvoice")
@Inheritance(strategy = InheritanceType.JOINED)
public class EdsSaleInvoice extends EdsBaseSaleInvoice {

    private Date fromDate;  //Period Start Date
    private Date toDate;    //Period End Date

    private Boolean isProjectBasedInvoice;

    @Column(precision = 10, scale = 5)
    private BigDecimal quotePercent;

    @Column(precision = 25, scale = 5)
    private BigDecimal quoteAmount;

    @Column(precision = 25, scale = 5)
    private BigDecimal previousBalance;

    @Column(precision = 25, scale = 5)
    private BigDecimal paymentReceived;

    private Integer invoiceType;

    private Integer recurringInvoiceID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoiceterms")
    private EdsInvoiceTerms invoiceTerms;

    private String nimbleUniqueID;

    @Column(name = "clientapproved", columnDefinition = "boolean default false")
    private boolean isClientApproved;

    @Column(name = "in_target", columnDefinition = "boolean default false")
    private Boolean inTarget;

    @Column(name = "targetId")
    private String targetId;

    private Long zapierordernumber;

    @Column(name = "opportunityID")
    private Integer opportunityID;

    @Column(name = "revolut_url")
    private String revolutUrl;

    @Column(name = "stripe_url")
    private String stripeUrl;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "entityID", fetch = FetchType.LAZY)
    @Where(clause = "entityType = 'saleinvoice' OR entityType = 'TYPE_CREDIT_NOTE'")
    @OrderBy(value = "approverOrder ASC")
    @org.hibernate.annotations.ForeignKey(name = "none")
    private List<EdsApprover> approvers = new ArrayList<>();


    public void setData(EdsInvoice invoice) {
        setClient(invoice.getClientOrSupplier());
        super.setData(invoice);
        setType(Constants.RECEIVABLE);

        if (invoice instanceof EdsRecurringInvoice) {
            EdsRecurringInvoice rInv = (EdsRecurringInvoice) invoice;
            setShippingMethod(rInv.getShippingMethod());
            setBankAccount(rInv.getBankAccount());
            setPaymentInstructionID(rInv.getPaymentInstructionID());
        }
    }

    public NewInvoiceItem[] getItemsAsNewInvoiceItem() {
        if (getInvoiceItems() != null && !getInvoiceItems().isEmpty()) {
            List<NewInvoiceItem> newItems = new ArrayList<>();
            for (EdsInvoiceItem invoiceItem : getInvoiceItems()) {
                newItems.add(getItem(invoiceItem));
            }
            return newItems.toArray(new NewInvoiceItem[]{});
        }
        return new NewInvoiceItem[]{};
    }

    public Boolean isProjectBasedInvoice() {
        return isProjectBasedInvoice != null ? isProjectBasedInvoice : false;
    }

    public void setProjectBasedInvoice(Boolean projectBasedInvoice) {
        isProjectBasedInvoice = projectBasedInvoice;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public BigDecimal getQuotePercent() {
        return quotePercent;
    }

    public void setQuotePercent(BigDecimal quotePercent) {
        this.quotePercent = quotePercent;
    }

    public BigDecimal getQuoteAmount() {
        return quoteAmount;
    }

    public void setQuoteAmount(BigDecimal quoteAmount) {
        this.quoteAmount = quoteAmount;
    }

    public BigDecimal getPreviousBalance() {
        return previousBalance;
    }

    public void setPreviousBalance(BigDecimal previousBalance) {
        this.previousBalance = previousBalance;
    }

    public BigDecimal getPaymentReceived() {
        return paymentReceived;
    }

    public void setPaymentReceived(BigDecimal paymentReceived) {
        this.paymentReceived = paymentReceived;
    }

    public Integer getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(Integer invoiceType) {
        if (!ServerUtils.equalsInteger(this.invoiceType, invoiceType)) {
            addChange(CustomFormConstants.INVOICE_TYPE);
        }
        this.invoiceType = invoiceType;
    }

    public Integer getRecurringInvoiceID() {
        return recurringInvoiceID;
    }

    public void setRecurringInvoiceID(Integer recurringInvoiceID) {
        this.recurringInvoiceID = recurringInvoiceID;
    }

    public EdsInvoiceTerms getInvoiceTerms() {
        return invoiceTerms;
    }

    public void setInvoiceTerms(EdsInvoiceTerms invoiceTerms) {
        this.invoiceTerms = invoiceTerms;
    }

    public String getNimbleUniqueID() {
        return nimbleUniqueID;
    }

    public void setNimbleUniqueID(String nimbleUniqueID) {
        this.nimbleUniqueID = nimbleUniqueID;
    }

    public Integer getOpportunityID() {
        return this.opportunityID;
    }

    public void setOpportunityID(final Integer opportunityID) {
        this.opportunityID = opportunityID;
    }

    public boolean isClientApproved() {
        return isClientApproved;
    }

    public void setClientApproved(boolean isClientApproved) {
        this.isClientApproved = isClientApproved;
    }

    public boolean isInTarget() {
        return inTarget != null && inTarget;
    }

    public void setInTarget(Boolean inTarget) {
        this.inTarget = inTarget;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public Long getZapierordernumber() {
        return zapierordernumber;
    }

    public void setZapierordernumber(Long zapierordernumber) {
        this.zapierordernumber = zapierordernumber;
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
        return isOk(getCurrentApprover()) && isOk(getCurrentApprover().getStatus()) && Constants.APPROVE.equals(getCurrentApprover().getStatus().getCode());
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

    public SolrInputDocument wrapToSolrDocument(EdsSaleInvoice invoice, Integer companyID) {
        String compositID = companyID + "_" + invoice.getObjectID();
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField(SolrSaleInvoiceRepresenter.FIELD_COMPOSITE_ID, compositID);
        doc.addField(SolrSaleInvoiceRepresenter.FIELD_COMPANY_ID, companyID);
        doc.addField(SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID, invoice.getObjectID());
        doc.addField(SolrSaleInvoiceRepresenter.FIELD_OPPORTUNITY_ID, invoice.getOpportunityID());

        if (invoice.getClientOrSupplier() != null) {
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_CLIENT_ID, invoice.getClientOrSupplier().getObjectID());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_CLIENT_NAME, invoice.getClientOrSupplier().getName());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_CLIENT_ID_NAME, invoice.getClientOrSupplier().getObjectID() + SolrSaleInvoiceRepresenter.SPLIT + invoice.getClientOrSupplier().getName());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_CLIENT_VAT, invoice.getClientOrSupplier().getVatNumber());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_CLIENT_TRN, invoice.getClientOrSupplier().getTrn());
        }
        GenericSettingsManager genericSettingsManager = StaticContextAccessor.getBean(GenericSettingsManager.class);
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_CUSTOM_CRM_ACCOUNT)) {
            CustomCrmAccountManager customCrmAccountManager = StaticContextAccessor.getBean(CustomCrmAccountManager.class);
            EdsCustomCrmAccount edsCustomCrmAccount = customCrmAccountManager.getCustomCrmAccountByEntityTypeAndEntityId(invoice.getObjectID(), EntityTypeEnum.SALE_INVOICE.name());
            if (edsCustomCrmAccount != null) {
                doc.addField(SolrSaleInvoiceRepresenter.FIELD_CUSTOM_CLIENT_ID, edsCustomCrmAccount.getObjectID());
                doc.addField(SolrSaleInvoiceRepresenter.FIELD_CUSTOM_CLIENT_NAME, edsCustomCrmAccount.getClientName());
                doc.addField(SolrSaleInvoiceRepresenter.FIELD_CUSTOM_CLIENT_ID_NAME, edsCustomCrmAccount.getObjectID() + SolrSaleInvoiceRepresenter.SPLIT + edsCustomCrmAccount.getClientName());
            }
        }
        if (!ServerUtils.isNullOrEmpty(invoice.getZatcaStatus())) {
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_ZATCA_STATUS, invoice.getZatcaStatus());
        }

        if (invoice.getClientContact() != null) {
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_CLIENT_CONTACT_ID, invoice.getClientContact().getObjectID());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_CLIENT_CONTACT_EMAIL, invoice.getClientContact().getPrimaryEmail());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_CLIENT_CONTACT_ID_EMAIL, invoice.getClientContact().getObjectID() + SolrSaleInvoiceRepresenter.SPLIT + invoice.getClientContact().getName());
        }

        if (invoice.getCurrency() != null) {
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_CURRENCY_ID, invoice.getCurrency().getObjectID());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_CURRENCY_NAME, invoice.getCurrency().getName());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_CURRENCY_ID_NAME, invoice.getCurrency().getObjectID() + SolrSaleInvoiceRepresenter.SPLIT + invoice.getCurrency().getName());
        }

        if (invoice.getRelatedProject() != null) {
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_RELATED_PROJECT_ID, invoice.getRelatedProject().getObjectID());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_RELATED_PROJECT_NAME, invoice.getRelatedProject().getName());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_RELATED_PROJECT_NUMBER, invoice.getRelatedProject().getNumber());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_RELATED_PROJECT_ID_NAME, invoice.getRelatedProject().getObjectID() + SolrSaleInvoiceRepresenter.SPLIT + invoice.getRelatedProject().getName());
            if (invoice.getRelatedProject().getStatus() != null) {
                doc.addField(SolrSaleInvoiceRepresenter.FIELD_RELATED_PROJECT_CODE, invoice.getRelatedProject().getStatus().getCode());
            }
        }

        for (EdsProject project : invoice.getProjects()) {
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_MULTI_PROJECT_ID, project.getObjectID());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_MULTI_PROJECT_NAME, project.getName());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_MULTI_PROJECT_NUMBER, project.getNumber());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_MULTI_PROJECT_ID_NAME, project.getObjectID() + SolrSaleInvoiceRepresenter.SPLIT + project.getName());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_MULTI_PROJECT_NUMBER_NAME, project.getNumber() + SolrSaleInvoiceRepresenter.ARROW + project.getName());
        }

        if (invoice.getStatus() != null) {
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_STATUS_ID, invoice.getStatus().getObjectID());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_STATUS_NAME, invoice.isClientApproved() ? (invoice.getStatus().getCode().equals(Constants.APPROVE) ? "Approved By Client" : invoice.getStatus().getName()) : invoice.getStatus().getName());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_STATUS_ID_NAME, invoice.getStatus().getObjectID() + SolrSaleInvoiceRepresenter.SPLIT + invoice.getStatus().getName());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_STATUS_SORDER, invoice.getStatus().getSorder());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_STATUS_CODE, invoice.getStatus().getCode());
        }

        if (invoice.getShippingMethod() != null) {
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_SHPPINGMETHOD_ID, invoice.getShippingMethod().getObjectID());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_SHPPINGMETHOD_NAME, invoice.getShippingMethod().getName());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_SHPPINGMETHOD_ID_NAME, invoice.getShippingMethod().getObjectID() + SolrSaleInvoiceRepresenter.SPLIT + invoice.getShippingMethod().getName());
        }

        if (invoice.getCreator() != null) {
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_CREATOR_ID, invoice.getCreator().getObjectID());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_CREATOR_NAME, invoice.getCreator().getName());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_CREATOR_ID_NAME, invoice.getCreator().getObjectID() + SolrSaleInvoiceRepresenter.SPLIT + invoice.getCreator().getName());
        }

        doc.addField(SolrSaleInvoiceRepresenter.FIELD_INVOICE_NUMBER, invoice.getNumber());
        doc.addField(SolrSaleInvoiceRepresenter.FIELD_INVOICE_DATE, invoice.getInvoiceDate());
        doc.addField(SolrSaleInvoiceRepresenter.FIELD_DUE_DATE, invoice.getDueDate());
        doc.addField(SolrSaleInvoiceRepresenter.FIELD_PO_NUMBER, invoice.getPoNumber());
        doc.addField(SolrSaleInvoiceRepresenter.REFERENCE, invoice.getReference());
        doc.addField(SolrSaleInvoiceRepresenter.FIELD_QUOTE_NUMBER, invoice.getQuoteNumber());
        doc.addField(SolrSaleInvoiceRepresenter.FIELD_INTRODUCTION, invoice.getIntroduction());
        if (invoice.getQuotePercent() != null) {
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_QUOTE_PERCENT, invoice.getQuotePercent().doubleValue());
        }
        doc.addField(SolrSaleInvoiceRepresenter.FIELD_PROJECT_BASED, invoice.isProjectBasedInvoice());
        doc.addField(SolrSaleInvoiceRepresenter.FIELD_TAX_CALCULATION_TYPE, invoice.getTaxCalculationType());
        if (invoice.getPdfTemplate() != null) {
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_PDF_TEMPLATE_ID, invoice.getPdfTemplate().getObjectID());
        }

        if (CollectionUtils.isNotEmpty(invoice.getInvoiceItems())) {
            invoice.getInvoiceItems().stream()
                    .filter(edsInvoiceItem -> edsInvoiceItem.getItem() != null)
                    .forEach(edsInvoiceItem
                            -> {
                        doc.addField(SolrSaleInvoiceRepresenter.FIELD_ITEM_ID, edsInvoiceItem.getItem().getObjectID());
                        doc.addField(SolrPurchaseInvoiceRepresenter.FIELD_WAREHOUSE_ID, edsInvoiceItem.getWarehouse() != null ? edsInvoiceItem.getWarehouse().getObjectID() : null);
                    });
        }

        List<EdsInvoicePayment> paymentsOrRefunds = ((invoice.isCreditNote() ? invoice.getRefunds() : invoice.getPayments()));
        BigDecimal fullPayment = new BigDecimal("0.00");
        for (EdsInvoicePayment payment : paymentsOrRefunds) {
            if (!(payment.getStatus() != null && EdsInvoicePayment.REVERSED.equals(payment.getStatus().getCode()))) {
                fullPayment = fullPayment.add(payment.getAmountInInvoiceCurrency() != null ? payment.getAmountInInvoiceCurrency() : payment.getAmount());
            }
        }
        BigDecimal totalCurrency = invoice.getTotalInInvoiceCurrency() != null ? invoice.getTotalInInvoiceCurrency() :
                invoice.getTotal().multiply(invoice.getExchangeRate());

        doc.addField(SolrSaleInvoiceRepresenter.FIELD_IS_CREDITNODE, invoice.isCreditNote());
        doc.addField(SolrSaleInvoiceRepresenter.FIELD_TOTAL_TAXES, invoice.getTotalTaxes() != null ? invoice.getTotalTaxes().doubleValue() : 0d);
        doc.addField(SolrSaleInvoiceRepresenter.FIELD_EXCHARGE_RATE, invoice.getExchangeRate().doubleValue());
        doc.addField(SolrSaleInvoiceRepresenter.FIELD_TOTAL_INVOICE_CURRENCY, totalCurrency.doubleValue());
        doc.addField(SolrSaleInvoiceRepresenter.FIELD_TOTAL_INVOICE_BASE, invoice.getTotal().doubleValue());
        doc.addField(SolrSaleInvoiceRepresenter.FIELD_PAID_AMOUNT, fullPayment.doubleValue());
        doc.addField(SolrSaleInvoiceRepresenter.FIELD_SUB_TOTAL, invoice.getSubtotal().doubleValue());

        if (invoice.isCreditNote()) {
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_SORTABLE_PAID_AMOUNT, -1 * fullPayment.doubleValue());
        } else {
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_SORTABLE_PAID_AMOUNT, fullPayment.doubleValue());
        }
        doc.addField(SolrSaleInvoiceRepresenter.FIELD_DUE_AMOUNT, totalCurrency.doubleValue() - fullPayment.doubleValue());

        doc.addField(SolrSaleInvoiceRepresenter.FIELD_IN_TARGET, isInTarget());
        doc.addField(SolrSaleInvoiceRepresenter.HAS_PAYMENT, getPaymentItemsList(this).size() > 0);
        if (getCurrentApprover() != null && getCurrentApprover().getExactEmployee() != null) {
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_CURRENT_APPROVER_ID, getCurrentApprover().getExactEmployee().getObjectID());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_CURRENT_APPROVER_NAME, getCurrentApprover().getExactEmployee().getFullName());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_CURRENT_APPROVER_ID_NAME, getCurrentApprover().getExactEmployee().getObjectID() + SolrSaleInvoiceRepresenter.SPLIT + getCurrentApprover().getExactEmployee().getFullName());
        }
        doc.addField(SolrSaleInvoiceRepresenter.FIELD_CREATED_DATE, invoice.getCreationDate());
        doc.addField(SolrSaleInvoiceRepresenter.FIELD_UPDATED_DATE, invoice.getUpdatedDate());
        CustomFieldsUtils.setInSolrCustomFields(doc, getCustomFields());

        return doc;
    }

    public LinkedHashMap<String, Double> getInvoiceAmount(EdsSaleInvoice invoice) {
        LinkedHashMap<String, Double> amountMap = new LinkedHashMap<>();
        List<EdsInvoicePayment> paymentsOrRefunds = ((invoice.isCreditNote() ? invoice.getRefunds() : invoice.getPayments()));
        BigDecimal fullPayment = new BigDecimal("0.00");
        for (EdsInvoicePayment payment : paymentsOrRefunds) {
            if (!(payment.getStatus() != null && EdsInvoicePayment.REVERSED.equals(payment.getStatus().getCode()))) {
                fullPayment = fullPayment.add(payment.getAmountInInvoiceCurrency() != null ? payment.getAmountInInvoiceCurrency() : payment.getAmount());
            }
        }
        BigDecimal totalCurrency = invoice.getTotalInInvoiceCurrency() != null ? invoice.getTotalInInvoiceCurrency() : invoice.getTotal().multiply(invoice.getExchangeRate());
        amountMap.put(SolrSaleInvoiceRepresenter.FIELD_DUE_AMOUNT, totalCurrency.doubleValue() - fullPayment.doubleValue());
        amountMap.put(SolrSaleInvoiceRepresenter.FIELD_PAID_AMOUNT, fullPayment.doubleValue());
        return amountMap;
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

    @Override
    public Object getRealValue(String fieldID) {
        String[] values = fieldID.split(",");
        fieldID = values.length >= 2 ? values[0] : fieldID;
        if (fieldID == null) {
            return null;
        } else if (fieldID.equals(CustomFormConstants.ACCOUNTING.CUSTOMER)) {
            return getClient();
        } else if (fieldID.equals(CustomFormConstants.ACCOUNTING.PROJECT)) {
            return getRelatedProject();
        } else if (fieldID.equals(CustomFormConstants.ACCOUNTING.STATUS)) {
            return getStatus();
        } else if (fieldID.equals(CustomFormConstants.START_DATE)) {
            return getInvoiceDate();
        } else if (fieldID.equals(CustomFormConstants.DUE_DATE)) {
            return getDueDate();
        } else if (fieldID.equals(CustomFormConstants.INVOICE_TYPE)) {
            return getInvoiceType() == null ? "" : (getInvoiceType().equals(0) ? "Product Invoice" : "Service Invoice");
        } else if (fieldID.equals(CustomFormConstants.REFERENCE)) {
            return getReference();
        } else if (fieldID.equals(CustomFormConstants.NUMBER)) {
            return getNumber();
        } else if (fieldID.equals(CustomFormConstants.AMOUNT)) {
            return getTaxCalculationType() == null ? "" : (getTaxCalculationType().equals(0) ? "No Tax" : (getTaxCalculationType().equals(1) ? "Tax Inclusive" : "Tax Exclusive"));
        } else if (fieldID.equals(CustomFormConstants.ACCOUNTING.SQ_NUMBER)) {
            return getQuoteNumber();
        } else if (fieldID.equals(CustomFormConstants.ACCOUNTING.PO_NUMBER)) {
            return getPoNumber();
        } else if (fieldID.equals(CustomFormConstants.ACCOUNTING.SHIP_VIA)) {
            return getShippingMethod();
        } else if (fieldID.equals(CustomFormConstants.ACCOUNTING.BANK_ACCOUNT)) {
            return getBankAccount();
        } else if (fieldID.equals(CustomFormConstants.ACCOUNTING.INSTRUCTIONS)) {
            return getPaymentInstruction();
        } else if (fieldID.equals(CustomFormConstants.ACCOUNTING.TOTAL_AMOUNT)) {
            return getTotal();
        } else if (fieldID.equals(CustomFormConstants.ACCOUNTING.TOTAL_INVOICE_CURRENCY)) {
            return getTotalInInvoiceCurrency();
        } else if (fieldID.equals(CustomFormConstants.ACCOUNTING.PAID_AMOUNT)) {
            return getFullPayments();
        } else if (fieldID.equals(CustomFormConstants.ACCOUNTING.PAID_AMOUNT_BASE_CURRENCY)) {
            return getFullPaymentsInBase();
        } else if (fieldID.equals(CustomFormConstants.ACCOUNTING.DUE_AMOUNT_BASE_CURRENCY)) {
            return getTotal() != null && getFullPaymentsInBase() != null ? getTotal().subtract(getFullPaymentsInBase()) : null;
        } else if (fieldID.equals(CustomFormConstants.ACCOUNTING.DUE_AMOUNT_INVOICE_CURRENCY)) {
            return getDueAmount();
        } else if (fieldID.equals(CustomFormConstants.ACCOUNTING.TOTAL_DISCOUNT)) {
            return getTotalDiscount();
        } else if (fieldID.equals(CustomFormConstants.CURRENCY)) {
            return getCurrency();
        } else if (fieldID.equals(CustomFormConstants.CREATED_DATE)) {
            return getCreationDate();
        } else if (fieldID.equals(CustomFormConstants.UPDATED_DATE)) {
            return getModificationDate();
        } else if (fieldID.equals(CustomFormConstants.ITEM_TABLE_ITEM)) {
            List<EdsItem> invoiceItems = new ArrayList<>();
            for (EdsInvoiceItem invoiceItem : getInvoiceItems()) {
                invoiceItems.add(invoiceItem.getItem());
            }
            return invoiceItems;
        } else if (fieldID.equals(CustomFormConstants.ITEM_TABLE_DESCRIPTION)) {
            List<String> descriptions = new ArrayList<>();
            for (EdsInvoiceItem invoiceItem : getInvoiceItems()) {
                descriptions.add(invoiceItem.getDescription());
            }
            return descriptions;
        } else if (fieldID.equals(CustomFormConstants.ITEM_TABLE_QTY)) {
            List<BigDecimal> quantities = new ArrayList<>();
            for (EdsInvoiceItem invoiceItem : getInvoiceItems()) {
                quantities.add(invoiceItem.getQty());
            }
            return quantities;
        } else if (fieldID.equals(CustomFormConstants.ITEM_TABLE_PRICE)) {
            List<BigDecimal> prices = new ArrayList<>();
            for (EdsInvoiceItem invoiceItem : getInvoiceItems()) {
                prices.add(invoiceItem.getUnitPrice());
            }
            return prices;
        } else if (fieldID.equals(CustomFormConstants.ITEM_TABLE_DISCOUNT)) {
            List<BigDecimal> discounts = new ArrayList<>();
            for (EdsInvoiceItem invoiceItem : getInvoiceItems()) {
                discounts.add(invoiceItem.getDiscountAmount());
            }
            return discounts;
        } else if (fieldID.equals(CustomFormConstants.ITEM_TABLE_ACCOUNT)) {
            List<EdsAccount> accounts = new ArrayList<>();
            for (EdsInvoiceItem invoiceItem : getInvoiceItems()) {
                accounts.add(invoiceItem.getAccount());
            }
            return accounts;
        } else if (fieldID.equals(CustomFormConstants.ITEM_TABLE_NET_AMOUNT)) {
            List<BigDecimal> amounts = new ArrayList<>();
            for (EdsInvoiceItem invoiceItem : getInvoiceItems()) {
                amounts.add(invoiceItem.getNet());
            }
            return amounts;
        } else if (fieldID.equals(CustomFormConstants.ITEM_TABLE_WAREHOUSE)) {
            List<EdsWarehouse> warehouses = new ArrayList<>();
            for (EdsInvoiceItem invoiceItem : getInvoiceItems()) {
                warehouses.add(invoiceItem.getWarehouse());
            }
            return warehouses;
        } else if (fieldID.equals(CustomFormConstants.ITEM_TABLE_DEPARTMENT)) {
            List<EdsDepartment> departments = new ArrayList<>();
            for (EdsInvoiceItem invoiceItem : getInvoiceItems()) {
                departments.add(invoiceItem.getDepartment());
            }
            return departments;
        } else if (fieldID.equals(CustomFormConstants.ITEM_TABLE_MEASUREMENT)) {
            List<EdsUnitMeasurement> measurements = new ArrayList<>();
            for (EdsInvoiceItem invoiceItem : getInvoiceItems()) {
                measurements.add(invoiceItem.getUnitMeasurement());
            }
            return measurements;
        } else if (fieldID.contains("string_value") || fieldID.contains("double_value") || fieldID.contains("date_value")) {
            return getCustomFields() != null ? CustomFieldsUtils.getObjectValue(getCustomFields(), fieldID) : "";
        }
        return super.getRealValue(fieldID);
    }

    @Override
    protected String getStringValueByFieldID(String realFieldID) {
        return super.getStringValueByFieldID(realFieldID);
    }

    public String getRevolutUrl() {
        return revolutUrl;
    }

    public void setRevolutUrl(String revolutUrl) {
        this.revolutUrl = revolutUrl;
    }

    public String getStripeUrl() {
        return stripeUrl;
    }

    public void setStripeUrl(String stripeUrl) {
        this.stripeUrl = stripeUrl;
    }

    public NewInvoice getRPC() {
        NewInvoice newInvoice = new NewInvoice();
        newInvoice.setID(getObjectID());
        if (getOpportunityID() != null) {
            newInvoice.setOpportunityID(getOpportunityID());
        }
        if (getClient() != null) {
            newInvoice.setClientID(getClientOrSupplier().getObjectID());
            newInvoice.setClientName(getClientOrSupplier().getName());
            newInvoice.setClientVatNumber(getClientOrSupplier().getVatNumber());
            newInvoice.setClientTrnNumber(getClientOrSupplier().getTrn());

            if (getClientOrSupplier().getOwners() != null && !getClientOrSupplier().getOwners().isEmpty()) {
                newInvoice.setClientOwners(getClientOrSupplier().getOwners().stream().map(o -> new SelectItem(o.getObjectID(), o.getName())).collect(Collectors.toList()));
            }
        }
        if (getClientContact() != null) {
            newInvoice.setClientContactID(getClientContact().getObjectID());
            newInvoice.setClientContactEmail(getClientContact().getPrimaryEmail());
        }
        if (getCurrency() != null) {
            newInvoice.setCurrencyID(getCurrency().getObjectID());
            newInvoice.setCurrencyName(getCurrency().getName());
        }
        if (getRelatedProject() != null) {
            newInvoice.setRelatedProjectID(getRelatedProject().getObjectID());
            newInvoice.setRelatedProjectName(getRelatedProject().getName());
            newInvoice.setRelatedProjectNumber(getRelatedProject().getNumber());
            if (getRelatedProject().getStatus() != null) {
                newInvoice.setProjectStatusCode(getRelatedProject().getStatus().getCode());
            }
        }
        if (getProjects() != null) {
            getProjects().forEach(edsProject -> {
                newInvoice.getMultiProjectId().add(edsProject.getObjectID());
                newInvoice.getMultiProjectName().add(edsProject.getName());
                newInvoice.getMultiProjectNumber().add(edsProject.getNumber());
                newInvoice.getMultiProjectIdName().add(SolrUtils.getIdName(edsProject.getObjectID(), edsProject.getName()));
                newInvoice.getMultiProjectNumberName().add(edsProject.getNumber() + SolrSaleInvoiceRepresenter.ARROW + edsProject.getName());
            });
        }
        if (getStatus() != null) {
            newInvoice.setStatusID(getStatus().getObjectID());
            newInvoice.setStatus(getStatus().getName());
            newInvoice.setStatusSorder(String.valueOf(getStatus().getSorder()));
            newInvoice.setStatusCode(getStatus().getCode());
        }
        if (getShippingMethod() != null) {
            newInvoice.setShippingMethod(getShippingMethod().getRPC());
        }
        if (getCreator() != null) {
            newInvoice.setCreator(new SelectItem(getCreator().getObjectID(), getCreator().getName()));
        }
        newInvoice.setInvoiceNumber(getNumber());
        newInvoice.setInvoiceDate(new DateNonConvertable(getInvoiceDate()));
        newInvoice.setDueDate(new DateNonConvertable(getDueDate()));
        newInvoice.setPoNumber(getPoNumber());
        newInvoice.setReference(getReference());
        newInvoice.setQuoteNumber(getQuoteNumber());
        newInvoice.setIntroduction(getIntroduction());
        if (getQuotePercent() != null) {
            newInvoice.setQuotePercent(getQuotePercent().doubleValue());
        }
        newInvoice.setProjectBasedInvoice(isProjectBasedInvoice());
        newInvoice.setTaxCalculationType(getTaxCalculationType());
        if (getPdfTemplate() != null) {
            newInvoice.setPdfTemplateID(getPdfTemplate().getObjectID());
        }

        if (CollectionUtils.isNotEmpty(getInvoiceItems())) {
            List<NewInvoiceItem> invoiceItems = getInvoiceItems().stream()
                    .filter(edsInvoiceItem -> edsInvoiceItem.getItem() != null)
                    .map(i -> i.getItem().getTransferObject())
                    .collect(Collectors.toList());

            newInvoice.setItems(invoiceItems.toArray(new NewInvoiceItem[invoiceItems.size()]));
        }
        BigDecimal fullPayment = getFullPayments();
        BigDecimal totalCurrency = getTotalInInvoiceCurrency() != null ? getTotalInInvoiceCurrency() :
                getTotal().multiply(getExchangeRate());

        newInvoice.setCreditNote(isCreditNote());
        newInvoice.setTotalTaxes(getTotalTaxes() != null ? getTotalTaxes() : BigDecimal.ZERO);
        newInvoice.setExchageRate(getExchangeRate());
        newInvoice.setTotalInInvoiceCurrency(totalCurrency);
        newInvoice.setTotal(getTotal());
        newInvoice.setPaidAmount(fullPayment);
        newInvoice.setSubtotal(getSubtotal());

        newInvoice.setOrderDueAmount(totalCurrency.subtract(fullPayment));
        newInvoice.setInTarget(isInTarget());
        newInvoice.setHasPayment(fullPayment.compareTo(BigDecimal.ZERO) > 0);
        if (getCurrentApprover() != null && getCurrentApprover().getExactEmployee() != null) {
            newInvoice.setCurrentApprover(getCurrentApprover().getRPC());
        }
        if (!ServerUtils.isNullOrEmpty(getZatcaStatus())) {
            newInvoice.setZatcaStatus(getZatcaStatus());
        }
        newInvoice.setType(getType());
        newInvoice.setCreationDate(getCreationDate());
        newInvoice.setLastUpdateDate(getUpdatedDate());

        return newInvoice;
    }

    public SaleInvoiceSolrItem getSolrRPC() {
        SaleInvoiceSolrItem invoice = new SaleInvoiceSolrItem();

        invoice.setObjectID(getObjectID());
        if (getOpportunityID() != null) {
            invoice.setOpportunity(getOpportunityID());
        }

        if (getClientOrSupplier() != null) {
            invoice.setClient(getClientOrSupplier().getAsSelectItem());
            invoice.setClientVat(getClientOrSupplier().getVatNumber());
            invoice.setClientTrn(getClientOrSupplier().getTrn());

            if (getClientOrSupplier().getOwners() != null && !getClientOrSupplier().getOwners().isEmpty()) {
                getClientOrSupplier().getOwners().forEach(o -> invoice.getCustomerOwnerIds().add(o.getObjectID()));
            }
        }
        GenericSettingsManager genericSettingsManager = StaticContextAccessor.getBean(GenericSettingsManager.class);
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_CUSTOM_CRM_ACCOUNT)) {
            CustomCrmAccountManager customCrmAccountManager = StaticContextAccessor.getBean(CustomCrmAccountManager.class);
            EdsCustomCrmAccount edsCustomCrmAccount = customCrmAccountManager.getCustomCrmAccountByEntityTypeAndEntityId(getObjectID(), EntityTypeEnum.SALE_INVOICE.name());
            if (edsCustomCrmAccount != null) {
                invoice.setCustomClient(edsCustomCrmAccount.getAsSelectItem());
            }
        }
        if (getClientContact() != null) {
            invoice.setClientContact(new SelectItem(getClientContact().getObjectID(), getClientContact().getName()));
            invoice.setClientContactEmail(invoice.getClientContactEmail());
        }
        if (getCurrency() != null) {
            invoice.setCurrency(new SelectItem(getCurrency().getObjectID(), getCurrency().getName()));
        }
        if (getRelatedProject() != null) {
            SelectItem relatedProject = new SelectItem(getRelatedProject().getObjectID(), getRelatedProject().getName());
            relatedProject.setNumber(getRelatedProject().getNumber());
            invoice.setRelatedProject(relatedProject);
            if (getRelatedProject().getStatus() != null) {
                invoice.setRelatedProjectStatusCode(getRelatedProject().getStatus().getCode());
            }
        }
        if (getProjects() != null) {
            getProjects().forEach(edsProject -> {
                SelectItem multiProject = new SelectItem(edsProject.getObjectID(), edsProject.getName());
                multiProject.setNumber(edsProject.getNumber());
                invoice.getMultiProjects().add(multiProject);
            });
        }
        if (getStatus() != null) {
            invoice.setStatus(getStatus().getRPC());
        }
        if (getShippingMethod() != null) {
            invoice.setShippingMethod(getShippingMethod().getAsSelectItem());
        }
        if (getCreator() != null) {
            invoice.setCreator(getCreator().getAsSelectItem());
        }
        invoice.setInvoiceNumber(getNumber());
        invoice.setInvoiceDate(getInvoiceDate());
        invoice.setDueDate(getDueDate());
        invoice.setPoNumber(invoice.getPoNumber());
        invoice.setReference(invoice.getReference());
        invoice.setQuoteNumber(invoice.getQuoteNumber());
        invoice.setIntroduction(invoice.getIntroduction());
        if (invoice.getQuotePercent() != null) {
            invoice.setQuotePercent(invoice.getQuotePercent().doubleValue());
        }
        invoice.setProjectBased(isProjectBasedInvoice());
        invoice.setTaxCalculationType(invoice.getTaxCalculationType());
        if (getPdfTemplate() != null) {
            invoice.setPdfTemplateId(getPdfTemplate().getObjectID());
        }

        List<EdsInvoiceItem> invoiceItems = getInvoiceItems();
        if (CollectionUtils.isNotEmpty(invoiceItems)) {
            getInvoiceItems().stream()
                    .filter(item -> item != null)
                    .forEach(item -> {
                        invoice.getItemsIds().add(item.getObjectID());
                        invoice.getProductNames().add(item.getName());
                        if (item.getWarehouse() != null) {
                            invoice.getWarehouseIds().add(item.getWarehouse().getObjectID());
                        }
                    });
        }

        BigDecimal fullPayment = getFullPayments();
        BigDecimal totalCurrency = getTotalInInvoiceCurrency() != null ? getTotalInInvoiceCurrency() :
                getTotal().multiply(getExchangeRate());

        invoice.setCreditNode(isCreditNote());
        invoice.setTotalTaxes(invoice.getTotalTaxes() != null ? invoice.getTotalTaxes() : BigDecimal.ZERO);
        invoice.setExchangeRate(getExchangeRate());
        invoice.setTotalInvoiceCurrency(totalCurrency);
        invoice.setTotalInvoiceBase(getTotal());
        invoice.setPaidAmount(invoice.getPaidAmount());
        invoice.setSubTotal(getSubtotal());

        invoice.setDueAmount(totalCurrency.subtract(fullPayment));
        invoice.setInTarget(isInTarget());
        invoice.setHasPayment(fullPayment.compareTo(BigDecimal.ZERO) > 0);
        if (getCurrentApprover() != null && getCurrentApprover().getExactEmployee() != null) {
            invoice.setCurrentApprover(getCurrentApprover().getExactEmployee().getAsSelectItem());
        }
        if (!ServerUtils.isNullOrEmpty(invoice.getZatcaStatus())) {
            invoice.setZatcaStatus(invoice.getZatcaStatus());
        }
        if (getType().equals(Constants.RECEIVABLE)) {
            for (EdsInvoiceItem item : invoiceItems) {
                if (item != null) {
                    invoice.getProductIdsFromInvoice().add(item.getObjectID());
                }
            }
        }
        invoice.setCreatedDate(getCreationDate());
        invoice.setUpdatedDate(getUpdatedDate());

        return invoice;
    }
}
