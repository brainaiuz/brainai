package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.*;
import com.edatasite.workforce.core.domain.approving.EdsApprover;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.EdsOpportunity;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.customform.EdsModelField;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrPurchaseInvoiceRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrSaleInvoiceRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.SolrUtils;
import com.edatasite.workforce.gwt.core.server.app.StaticContextAccessor;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.invoice.client.rpc.PurchaseOrderSolrItem;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.solr.common.SolrInputDocument;
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
 * User: Ruslan Muhammadov
 * Date: 27.05.2009
 * Time: 17:28:27
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "purchaseorder")
public class EdsPurchaseOrder extends EdsQuote {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    private EdsCrmAccount supplier;

    private Integer clientID;
    //    private Integer clientBillAddressID;
    private Integer clientMailAddressID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requisitionedContact")
    private EdsCrmContact requisitionedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paymentMethod_id")
    private EdsPaymentMethod paymentMethod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderterms")
    private EdsInvoiceTerms orderTerms;

    @Type(type = "text")
    private String paymentTerms;

    @Type(type = "text")
    private String ShippingTerms;

    private Date shipDate;
    private Date cancelDate;
    private Date receiveDate;

    private String quoteNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approverid")
    private EdsUser approver;

    @Transient
    private Integer expenseAllocationType;

    @Column(name = "quote_id")
    private Integer quoteId;

    @Column(name = "opportunityID")
    private Integer opportunityID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "opportunityID", insertable = false, updatable = false)
    private EdsOpportunity opportunity;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "entityID", fetch = FetchType.LAZY)
    @Where(clause = "entityType = 'TYPE_PURCHASE_ORDER'")
    @OrderBy(value = "approverOrder ASC")
    @org.hibernate.annotations.ForeignKey(name = "none")
    private List<EdsApprover> approvers = Lists.newArrayList();

    public EdsCrmAccount getSupplier() {
        return supplier;
    }

//    @Column(name = "creationTime")
//    private Date creationTime;
//
//    @Column(name = "lastUpdateTime")
//    private Date lastUpdateTime;


    public void setSupplier(EdsCrmAccount supplier) {
        if (!ServerUtils.equalsEdsObject(this.supplier, supplier)) {
            addChange(CustomFormConstants.ACCOUNTING.SUPPLIER);
        }
        this.supplier = supplier;
    }

    public Integer getClientID() {
        return clientID;
    }

    public void setClientID(Integer clientID) {
        this.clientID = clientID;
    }

//    public Integer getClientBillAddressID() {
//        return clientBillAddressID;
//    }
//
//    public void setClientBillAddressID(Integer clientBillAddressID) {
//        this.clientBillAddressID = clientBillAddressID;
//    }

    public Integer getClientMailAddressID() {
        return clientMailAddressID;
    }

    public void setClientMailAddressID(Integer clientMailAddressID) {
        this.clientMailAddressID = clientMailAddressID;
    }

    public EdsCrmContact getRequisitionedBy() {
        return requisitionedBy;
    }

    public void setRequisitionedBy(EdsCrmContact requisitionedBy) {
        this.requisitionedBy = requisitionedBy;
    }

    public EdsInvoiceTerms getOrderTerms() {
        return orderTerms;
    }

    public void setInvoiceTerms(EdsInvoiceTerms orderTerms) {
        this.orderTerms = orderTerms;
    }

    public EdsPaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(EdsPaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentTerms() {
        return paymentTerms;
    }

    public void setPaymentTerms(String paymentTerms) {
        this.paymentTerms = paymentTerms;
    }

    public String getShippingTerms() {
        return ShippingTerms;
    }

    public void setShippingTerms(String shippingTerms) {
        ShippingTerms = shippingTerms;
    }

    public EdsCrmAccount getClientOrSupplier() {
        return supplier;
    }

    public Date getShipDate() {
        return shipDate;
    }

    public void setShipDate(Date shipDate) {
        this.shipDate = shipDate;
    }

    public EdsUser getApprover() {
        return approver;
    }

    public void setApprover(EdsUser approver) {
        this.approver = approver;
    }

    public Date getCancelDate() {
        return cancelDate;
    }

    public void setCancelDate(Date cancelDate) {
        this.cancelDate = cancelDate;
    }

    public Date getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(Date receiveDate) {
        this.receiveDate = receiveDate;
    }

    public Integer getExpenseAllocationType() {
        return expenseAllocationType;
    }

    public void setExpenseAllocationType(Integer expenseAllocationType) {
        this.expenseAllocationType = expenseAllocationType;
    }

//    public Date getLastUpdateTime() {
//        return lastUpdateTime;
//    }
//
//
//    public void setLastUpdateTime(Date lastUpdateTime) {
//        this.lastUpdateTime = lastUpdateTime;
//    }
//
//    public Date getCreationTime() {
//        return creationTime;
//    }
//
//
//    public void setCreationTime(Date creationTime) {
//        this.creationTime = creationTime;
//    }


    public Integer getOpportunityID() {
        return this.opportunityID;
    }

    public void setOpportunityID(final Integer opportunityID) {
        this.opportunityID = opportunityID;
    }

    public EdsOpportunity getOpportunity() {
        return opportunity;
    }

    public String getQuoteNumber() {
        return quoteNumber;
    }

    public void setQuoteNumber(String quoteNumber) {
        if (!ServerUtils.equalsString(this.quoteNumber, quoteNumber)) {
            addChange(CustomFormConstants.ACCOUNTING.SALE_QUOTE);
        }
        this.quoteNumber = quoteNumber;
    }

    public Integer getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(Integer quoteId) {
        this.quoteId = quoteId;
    }

    public String getName() {
        return getNumber();
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
        return isOk(getCurrentApprover()) && isOk(getCurrentApprover().getStatus()) && Constants.REJECT.equals(getCurrentApprover().getStatus().getCode());
    }

    @Override
    protected EdsReference getStatusByMarkedAction(Integer actionID) {
        if (!isOk(actionID)) {
            return null;
        }
        ReferenceManager referenceManager = StaticContextAccessor.getBean(ReferenceManager.class);
        if (actionID.equals(ApproverItem.MARK_AS_REJECTED)) {
            return referenceManager.findReference(Constants.INVOICE_STATUS, Constants.REJECT);
        } else if (actionID.equals(ApproverItem.MARK_AS_APPROVED)) {
            return referenceManager.findReference(Constants.INVOICE_STATUS, Constants.APPROVE);
        } else if (actionID.equals(ApproverItem.SEND_TO_CREATOR)) {
            return referenceManager.findReference(Constants.INVOICE_STATUS, Constants.REJECT);
        } else if (actionID.equals(ApproverItem.SEND_TO_DIRECTORS)) {
            return referenceManager.findReference(Constants.INVOICE_STATUS, Constants.REJECT);
        }
        return null;
    }

    @Override
    public void updateRejectedStatus() {
        if (getStatus() != null && Constants.REJECT.equals(getStatus().getCode())) {
            ReferenceManager referenceManager = StaticContextAccessor.getBean(ReferenceManager.class);
            setEntityStatus(referenceManager.findReference(Constants.INVOICE_STATUS, Constants.SUBMITTED_TO_MANAGER));
        }
    }

    public SolrInputDocument wrapToSolrDocument(EdsPurchaseOrder order, Integer companyID) {
        String compositID = companyID + "_" + order.getObjectID();
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField(SolrSaleInvoiceRepresenter.FIELD_COMPOSITE_ID, compositID);
        doc.addField(SolrSaleInvoiceRepresenter.FIELD_COMPANY_ID, companyID);
        doc.addField(SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID, order.getObjectID());
        doc.addField(SolrSaleInvoiceRepresenter.FIELD_OPPORTUNITY_ID, order.getOpportunityID());
        doc.addField(SolrSaleInvoiceRepresenter.FIELD_OPPORTUNITY_NUMBER, order.getOpportunity().getNumber());
        doc.addField(SolrSaleInvoiceRepresenter.FIELD_CUSTOMER_ID, order.getClientID());

        if (order.getClientOrSupplier() != null) {
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_CLIENT_ID, order.getClientOrSupplier().getObjectID());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_CLIENT_NAME, order.getClientOrSupplier().getName());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_CLIENT_ID_NAME, order.getClientOrSupplier().getObjectID() + SolrSaleInvoiceRepresenter.SPLIT + order.getClientOrSupplier().getName());
        }

        if (order.getCurrency() != null) {
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_CURRENCY_ID, order.getCurrency().getObjectID());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_CURRENCY_NAME, order.getCurrency().getName());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_CURRENCY_ID_NAME, order.getCurrency().getObjectID() + SolrSaleInvoiceRepresenter.SPLIT + order.getCurrency().getName());
        }

        if (order.getRelatedProject() != null) {
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_RELATED_PROJECT_ID, order.getRelatedProject().getObjectID());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_RELATED_PROJECT_NAME, order.getRelatedProject().getName());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_RELATED_PROJECT_NUMBER, order.getRelatedProject().getNumber());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_RELATED_PROJECT_ID_NAME, order.getRelatedProject().getObjectID() + SolrSaleInvoiceRepresenter.SPLIT + order.getRelatedProject().getName());
            if (order.getRelatedProject().getStatus() != null) {
                doc.addField(SolrSaleInvoiceRepresenter.FIELD_RELATED_PROJECT_CODE, order.getRelatedProject().getStatus().getCode());
            }
        }

        for (EdsProject project : order.getProjects()) {
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_MULTI_PROJECT_ID, project.getObjectID());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_MULTI_PROJECT_NAME, project.getName());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_MULTI_PROJECT_NUMBER, project.getNumber());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_MULTI_PROJECT_ID_NAME, project.getObjectID() + SolrSaleInvoiceRepresenter.SPLIT + project.getName());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_MULTI_PROJECT_NUMBER_NAME, project.getNumber() + SolrSaleInvoiceRepresenter.ARROW + project.getName());
        }

        if (order.getStatus() != null) {
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_STATUS_ID, order.getStatus().getObjectID());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_STATUS_NAME, order.getStatus().getName());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_STATUS_ID_NAME, order.getStatus().getObjectID() + SolrSaleInvoiceRepresenter.SPLIT + order.getStatus().getName());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_STATUS_SORDER, order.getStatus().getSorder());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_STATUS_CODE, order.getStatus().getCode());
        }

        if (order.getCreator() != null) {
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_CREATOR_ID, order.getCreator().getObjectID());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_CREATOR_NAME, order.getCreator().getName());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_CREATOR_ID_NAME, order.getCreator().getObjectID() + SolrSaleInvoiceRepresenter.SPLIT + order.getCreator().getName());
        }

        if (order.getApprover() != null) {
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_MANAGER_ID, order.getApprover().getAsSelectItem().getId());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_MANAGER_NAME, order.getApprover().getAsSelectItem().getName());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_MANAGER_ID_NAME, order.getApprover().getAsSelectItem().getId() + SolrSaleInvoiceRepresenter.SPLIT + order.getApprover().getAsSelectItem().getName());
        }
        if (getCurrentApprover() != null && getCurrentApprover().getExactEmployee() != null) {
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_CURRENT_APPROVER_ID, getCurrentApprover().getExactEmployee().getObjectID());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_CURRENT_APPROVER_NAME, getCurrentApprover().getExactEmployee().getFullName());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_CURRENT_APPROVER_ID_NAME, getCurrentApprover().getExactEmployee().getObjectID() + SolrPurchaseInvoiceRepresenter.SPLIT + getCurrentApprover().getExactEmployee().getFullName());
        }

        if (CollectionUtils.isNotEmpty(getQuoteItems())) {
            getQuoteItems().stream()
                    .filter(edsQuoteItem -> edsQuoteItem.getItem() != null)
                    .forEach(edsQuoteItem
                            -> doc.addField(SolrPurchaseInvoiceRepresenter.FIELD_ITEM_ID, edsQuoteItem.getItem().getObjectID()));

        }


        doc.addField(SolrSaleInvoiceRepresenter.FIELD_INVOICE_NUMBER, order.getNumber());
        doc.addField(SolrSaleInvoiceRepresenter.FIELD_INVOICE_DATE, order.getInvoiceDate());
        doc.addField(SolrSaleInvoiceRepresenter.FIELD_DUE_DATE, order.getDueDate());

        BigDecimal totalCurrency = order.getTotalInInvoiceCurrency() != null ? order.getTotalInInvoiceCurrency() :
                order.getTotal().multiply(order.getExchangeRate());
        doc.addField(SolrPurchaseInvoiceRepresenter.FIELD_REFERENCE, getReference());
        doc.addField(SolrSaleInvoiceRepresenter.FIELD_TOTAL_INVOICE_BASE, order.getTotal().doubleValue());
        doc.addField(SolrSaleInvoiceRepresenter.FIELD_TOTAL_INVOICE_CURRENCY, totalCurrency.doubleValue());
        doc.addField(SolrSaleInvoiceRepresenter.FIELD_DUE_AMOUNT, totalCurrency.doubleValue());
        doc.addField(SolrSaleInvoiceRepresenter.FIELD_QUOTE_NUMBER, order.getQuoteNumber());
        doc.addField(SolrSaleInvoiceRepresenter.FIELD_TOTAL_TAXES, order.getTotalTaxes() != null ? order.getTotalTaxes().multiply(order.getExchangeRate() != null ? order.getExchangeRate() : BigDecimal.ONE).doubleValue() : 0d);
        doc.addField(SolrSaleInvoiceRepresenter.FIELD_CREATED_DATE, order.getCreationDate());
        doc.addField(SolrSaleInvoiceRepresenter.FIELD_UPDATED_DATE, order.getUpdatedDate());
        doc.addField(SolrSaleInvoiceRepresenter.FIELD_SUB_TOTAL, order.getSubtotal().doubleValue());
        doc.addField(SolrSaleInvoiceRepresenter.FIELD_EXCHARGE_RATE, order.getExchangeRate() != null ? order.getExchangeRate().doubleValue() : 0d);
        doc.addField(SolrSaleInvoiceRepresenter.FIELD_TAX_CALCULATION_TYPE, order.getTaxCalculationType());
        CustomFieldsUtils.setInSolrCustomFields(doc, getCustomFields());

        return doc;
    }

    public PurchaseOrderSolrItem getSolrRPC() {
        PurchaseOrderSolrItem item = new PurchaseOrderSolrItem();

        item.setOrderId(getObjectID());
        if (getOpportunity() != null) {
            SelectItem opportunity = new SelectItem(getOpportunity().getObjectID());
            opportunity.setNumber(getOpportunity().getNumber());
            item.setOpportunity(opportunity);
        }
        item.setCustomerId(getClientID());

        if (getClientOrSupplier() != null) {
            item.setClient(getClientOrSupplier().getAsSelectItem());

            if (!getClientOrSupplier().getOwners().isEmpty()) {
                getClientOrSupplier().getOwners().forEach(o -> item.getClientOwnerIds().add(o.getObjectID()));
            }
        }

        if (getCurrency() != null) {
            item.setCurrency(getCurrency().getAsSelectItem());
        }

        if (getRelatedProject() != null) {
            SelectItem relatedProject = new SelectItem(getRelatedProject().getObjectID(), getRelatedProject().getName());
            relatedProject.setNumber(getRelatedProject().getNumber());
            if (getRelatedProject().getStatus() != null) {
                relatedProject.setCode(getRelatedProject().getStatus().getCode());
            }
            item.setRelatedProject(relatedProject);
        }

        if (getProjects() != null) {
            getProjects().forEach(edsProject -> {
                SelectItem multiProject = new SelectItem(edsProject.getObjectID(), edsProject.getName());
                multiProject.setNumber(edsProject.getNumber());
                item.getMultiProject().add(multiProject);
            });
        }

        if (getStatus() != null) {
            item.setStatus(getStatus().getRPC());
        }

        if (getCreator() != null) {
            item.setCreator(getCreator().getAsSelectItem());
        }

        if (getApprover() != null) {
            item.setManager(getApprover().getAsSelectItem());
        }

        if (getCurrentApprover() != null && getCurrentApprover().getExactEmployee() != null) {
            SelectItem currentApprover = getCurrentApprover().getExactEmployee().getAsSelectItem();
            currentApprover.setName(getCurrentApprover().getExactEmployee().getFullName());
            item.setCurrentApprover(currentApprover);
        }

        if (!org.springframework.util.CollectionUtils.isEmpty(getQuoteItems())) {
            getQuoteItems().stream()
                    .filter(edsQuoteItem -> edsQuoteItem.getItem() != null)
                    .forEach(edsInvoiceItem -> item.getItemIds().add(edsInvoiceItem.getItem().getObjectID()));
        }

        item.setInvoiceNumber(getNumber());
        item.setInvoiceDate(getInvoiceDate());
        item.setDueDate(getDueDate());

        BigDecimal totalCurrency = getTotalInInvoiceCurrency() != null ? getTotalInInvoiceCurrency() :
                getTotal().multiply(getExchangeRate());

        item.setReference(getReference());
        item.setTotalInvoiceBase(getTotal());
        item.setTotalInvoiceCurrency(totalCurrency);
        item.setDueAmount(totalCurrency);
        item.setQuoteNumber(getQuoteNumber());
        item.setTotalTaxes(getTotalTaxes() != null ? getTotalTaxes().multiply(getExchangeRate() != null ? getExchangeRate() : BigDecimal.ONE) : BigDecimal.ZERO);
        item.setCreatedDate(getCreationDate());
        item.setUpdatedDate(getUpdatedDate());
        item.setSubTotal(getSubtotal());
        item.setExchangeRate(getExchangeRate() != null ? getExchangeRate() : BigDecimal.ZERO);
        item.setTaxCalculationType(getTaxCalculationType());

        return item;
    }


    @Override
    public void setValueForField(EdsModelField field, Object value) {
        if (field != null && field.getField_ID() != null) {
            String fieldID = field.getField_ID();
            switch (fieldID) {
                case CustomFormConstants.ACCOUNTING.SUPPLIER -> setSupplier((EdsCrmAccount) value);
                case CustomFormConstants.ACCOUNTING.PROJECT -> setRelatedProject((EdsProject) value);
                case CustomFormConstants.ACCOUNTING.INVOICE_DATE -> setInvoiceDate((Date) value);
                case CustomFormConstants.ACCOUNTING.DUE_DATE -> setDueDate((Date) value);
                case CustomFormConstants.ACCOUNTING.REFERENCE -> setReference((String) value);
                case CustomFormConstants.ACCOUNTING.PO_NUMBER -> setPoNumber((String) value);
                case CustomFormConstants.ACCOUNTING.SUB_TOTAL -> setSubtotal((BigDecimal) value);
                case CustomFormConstants.ACCOUNTING.TOTAL -> setTotal((BigDecimal) value);
                case CustomFormConstants.ACCOUNTING.TOTAL_INVOICE_CURRENCY -> setTotalInInvoiceCurrency((BigDecimal) value);
                case CustomFormConstants.ACCOUNTING.TOTAL_DISCOUNT -> setDiscount((BigDecimal) value);
                case CustomFormConstants.ACCOUNTING.SHIPPING_TERMS -> setShippingTerms((String) value);
                case CustomFormConstants.ACCOUNTING.PAYMENT_TERMS -> setPaymentTerms((String) value);
                case CustomFormConstants.ACCOUNTING.PAYMENT_TYPE -> setPaymentMethod((EdsPaymentMethod) value);
                case CustomFormConstants.ACCOUNTING.SALE_QUOTE -> setQuoteNumber((String) value);
                case CustomFormConstants.ACCOUNTING.STATUS -> setStatus((EdsReference) value);
            }
        }
        super.setValueForField(field, value);
    }

    @Override
    protected String getStringValueByFieldID(String realFieldID) {
        return super.getStringValueByFieldID(realFieldID);
    }

    @Override
    public Object getRealValue(String fieldID) {
        String[] values = fieldID.split(",");
        fieldID = values.length >= 2 ? values[0] : fieldID;
        if (fieldID == null) {
            return null;
        } else if (fieldID.equals(CustomFormConstants.ACCOUNTING.SUPPLIER)) {
            return getSupplier();
        } else if (fieldID.equals(CustomFormConstants.ACCOUNTING.PROJECT)) {
            return getRelatedProject();
        } else if (fieldID.equals(CustomFormConstants.ACCOUNTING.INVOICE_DATE)) {
            return getInvoiceDate();
        } else if (fieldID.equals(CustomFormConstants.ACCOUNTING.DUE_DATE)) {
            return getDueDate();
        } else if (fieldID.equals(CustomFormConstants.ACCOUNTING.REFERENCE)) {
            return getReference();
        } else if (fieldID.equals(CustomFormConstants.ACCOUNTING.PO_NUMBER)) {
            return getPoNumber();
        } else if (fieldID.equals(CustomFormConstants.ACCOUNTING.SUB_TOTAL)) {
            return getSubtotal();
        } else if (fieldID.equals(CustomFormConstants.ACCOUNTING.TOTAL)) {
            return getTotal();
        } else if (fieldID.equals(CustomFormConstants.ACCOUNTING.TOTAL_INVOICE_CURRENCY)) {
            return getTotalInInvoiceCurrency();
        } else if (fieldID.equals(CustomFormConstants.ACCOUNTING.TOTAL_DISCOUNT)) {
            return getDiscount();
        } else if (fieldID.equals(CustomFormConstants.ACCOUNTING.SHIPPING_TERMS)) {
            return getShippingTerms();
        } else if (fieldID.equals(CustomFormConstants.ACCOUNTING.PAYMENT_TERMS)) {
            return getPaymentTerms();
        } else if (fieldID.equals(CustomFormConstants.ACCOUNTING.PAYMENT_TYPE)) {
            return getPaymentMethod();
        } else if (fieldID.equals(CustomFormConstants.ACCOUNTING.SALE_QUOTE)) {
            return getQuoteNumber();
        } else if (fieldID.equals(CustomFormConstants.ACCOUNTING.STATUS)) {
            return getStatus();
        } else if (fieldID.equals(CustomFormConstants.CREATED_DATE)) {
            return getCreationDate();
        } else if (fieldID.equals(CustomFormConstants.UPDATED_DATE)) {
            return getUpdatedDate();
        } else if (fieldID.equals(CustomFormConstants.ITEM_TABLE_ITEM)) {
            List<EdsItem> quoteItems = new ArrayList<>();
            for (EdsQuoteItem quoteItem : getQuoteItems()) {
                quoteItems.add(quoteItem.getItem());
            }
            return quoteItems;
        } else if (fieldID.equals(CustomFormConstants.ITEM_TABLE_DESCRIPTION)) {
            List<String> descriptions = new ArrayList<>();
            for (EdsQuoteItem quoteItem : getQuoteItems()) {
                descriptions.add(quoteItem.getDescription());
            }
            return descriptions;
        } else if (fieldID.equals(CustomFormConstants.ITEM_TABLE_QTY)) {
            List<BigDecimal> quantities = new ArrayList<>();
            for (EdsQuoteItem quoteItem : getQuoteItems()) {
                quantities.add(quoteItem.getQty());
            }
            return quantities;
        } else if (fieldID.equals(CustomFormConstants.ITEM_TABLE_PRICE)) {
            List<BigDecimal> prices = new ArrayList<>();
            for (EdsQuoteItem quoteItem : getQuoteItems()) {
                prices.add(quoteItem.getUnitPrice());
            }
            return prices;
        } else if (fieldID.equals(CustomFormConstants.ITEM_TABLE_DISCOUNT)) {
            List<BigDecimal> discounts = new ArrayList<>();
            for (EdsQuoteItem quoteItem : getQuoteItems()) {
                discounts.add(quoteItem.getDiscountAmount());
            }
            return discounts;
        } else if (fieldID.equals(CustomFormConstants.ITEM_TABLE_ACCOUNT)) {
            List<EdsAccount> accounts = new ArrayList<>();
            for (EdsQuoteItem quoteItem : getQuoteItems()) {
                accounts.add(quoteItem.getAccount());
            }
            return accounts;
        } else if (fieldID.equals(CustomFormConstants.ITEM_TABLE_NET_AMOUNT)) {
            List<BigDecimal> amounts = new ArrayList<>();
            for (EdsQuoteItem quoteItem : getQuoteItems()) {
                amounts.add(quoteItem.getNet());
            }
            return amounts;
        } else if (fieldID.equals(CustomFormConstants.ITEM_TABLE_WAREHOUSE)) {
            List<EdsWarehouse> warehouses = new ArrayList<>();
            for (EdsQuoteItem quoteItem : getQuoteItems()) {
                warehouses.add(quoteItem.getWarehouse());
            }
            return warehouses;
        } else if (fieldID.equals(CustomFormConstants.ITEM_TABLE_DEPARTMENT)) {
            List<EdsDepartment> departments = new ArrayList<>();
            for (EdsQuoteItem quoteItem : getQuoteItems()) {
                departments.add(quoteItem.getDepartment());
            }
            return departments;
        } else if (fieldID.equals(CustomFormConstants.ITEM_TABLE_MEASUREMENT)) {
            List<EdsUnitMeasurement> measurements = new ArrayList<>();
            for (EdsQuoteItem quoteItem : getQuoteItems()) {
                measurements.add(quoteItem.getUnitMeasurement());
            }
            return measurements;
        } else if (fieldID.contains("string_value") || fieldID.contains("double_value") || fieldID.contains("date_value")) {
            return getCustomFields() != null ? CustomFieldsUtils.getObjectValue(getCustomFields(), fieldID) : "";
        }
        return super.getRealValue(fieldID);
    }
}
