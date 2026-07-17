package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.*;
import com.edatasite.workforce.core.domain.approving.EdsApprover;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.customform.EdsModelField;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
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
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoiceItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.SaleQuoteSolrItem;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.solr.common.SolrInputDocument;
import org.hibernate.annotations.Where;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static com.edatasite.workforce.gwt.core.server.app.Utils.isOk;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 07.04.2009
 * Time: 13:35:44
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "salequote")
public class EdsSaleQuote extends EdsQuote {

    public static final String PENDING = "PENDING";
    public static final String APPROVE = "APPROVE";
    public static final String SALE_ORDER = "SALE_ORDER";
    public static final String MANAGER_REJECT = "MANAGER_REJECT";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private EdsCrmAccount client;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "supplier_id")
//    private EdsCrmAccount supplier;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "managerid")
//    private EdsEmployee manager;

    @Column(precision = 25, scale = 5)
    private BigDecimal totalDiscount;

    @Column(precision = 25, scale = 2)
    private BigDecimal netAmountTotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shippingMethodId")
    private EdsShippingMethod shippingMethod;

    @Column(name = "opportunityID")
    private Integer opportunityID;

//    @Column(name = "creationTime")
//    private Date creationTime;
//
//    @Column(name = "lastUpdateTime")
//    private Date lastUpdateTime;

    private Integer termsConditionsID;

    private Boolean progressInvoicing;
    private BigDecimal convertedPercent;

    @Column(precision = 25, scale = 2)
    private BigDecimal convertedAmount;
    private String progressInvoicingType;

    @Column(precision = 25, scale = 5)
    private BigDecimal shippingAmount;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "quote")
    private List<EdsComissionAllocateItem> comissionAllocateItems = new ArrayList<>();

    @Column(name = "issalesorder")
    private Boolean isSalesOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoiceterms")
    private EdsInvoiceTerms invoiceTerms;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "entityID", fetch = FetchType.LAZY)
    @Where(clause = "entityType = 'salequote'")
    @OrderBy(value = "approverOrder ASC")
    @org.hibernate.annotations.ForeignKey(name = "none")
    private List<EdsApprover> approvers = Lists.newArrayList();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "entityID", fetch = FetchType.LAZY)
    @Where(clause = "entityType = 'saleorder'")
    @OrderBy(value = "approverOrder ASC")
    @org.hibernate.annotations.ForeignKey(name = "none")
    private List<EdsApprover> orderApprovers = Lists.newArrayList();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reject_reason")
    private EdsReference rejectReason;

    @Column(name = "reject_text")
    private String rejectText;

    public EdsCrmAccount getClient() {
        return client;
    }

    public void setClient(EdsCrmAccount client) {
        if (!ServerUtils.equalsEdsObject(this.client, client)) {
            addChange(CustomFormConstants.ACCOUNTING.CUSTOMER);
        }
        this.client = client;
    }

    public EdsCrmAccount getClientOrSupplier() {
        return client;
    }

    public BigDecimal getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(BigDecimal totalDiscount) {
        if (!ServerUtils.equalsBigDecimal(this.totalDiscount, totalDiscount)) {
            addChange(CustomFormConstants.ACCOUNTING.TOTAL_DISCOUNT);
        }
        this.totalDiscount = totalDiscount;
    }

    public BigDecimal getNetAmountTotal() {
        return netAmountTotal != null ? netAmountTotal : BigDecimal.ZERO;
    }

    public void setNetAmountTotal(BigDecimal netAmountTotal) {
        this.netAmountTotal = netAmountTotal;
    }

    public EdsShippingMethod getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(EdsShippingMethod shippingMethod) {
        if (!ServerUtils.equalsEdsObject(this.shippingMethod, shippingMethod)) {
            addChange(CustomFormConstants.ACCOUNTING.SHIP_VIA);
        }
        this.shippingMethod = shippingMethod;
    }

    public Integer getOpportunityID() {
        return opportunityID;
    }

    public void setOpportunityID(Integer opportunityID) {
        this.opportunityID = opportunityID;
    }

    public Integer getTermsConditionsID() {
        return termsConditionsID;
    }

    public void setTermsConditionsID(Integer termsConditionsID) {
        this.termsConditionsID = termsConditionsID;
    }

    public Boolean isProgressInvoicing() {
        return progressInvoicing != null ? progressInvoicing : false;
    }

    public void setProgressInvoicing(Boolean progressInvoicing) {
        this.progressInvoicing = progressInvoicing;
    }

    public BigDecimal getConvertedPercent() {
        return convertedPercent != null ? convertedPercent : AccountingConstants.ZERO;
    }

    public void setConvertedPercent(BigDecimal convertedPercent) {
        this.convertedPercent = convertedPercent;
    }

    public BigDecimal getConvertedAmount() {
        return convertedAmount != null ? convertedAmount : BigDecimal.ZERO;
    }

    public void setConvertedAmount(BigDecimal convertedAmount) {
        this.convertedAmount = convertedAmount;
    }

    //    public Date getLastUpdateTime() {
//        return lastUpdateTime;
//    }
//
//    @Override
//    public void setLastUpdateTime(Date lastUpdateTime) {
//        this.lastUpdateTime = lastUpdateTime;
//    }
//
//    public Date getCreationTime() {
//        return creationTime;
//    }
//
//    @Override
//    public void setCreationTime(Date creationTime) {
//        this.creationTime = creationTime;
//    }

    public List<EdsComissionAllocateItem> getComissionAllocateItems() {
        return comissionAllocateItems;
    }

    public void setComissionAllocateItems(List<EdsComissionAllocateItem> comissionAllocateItems) {
        this.comissionAllocateItems = comissionAllocateItems;
    }

    public String getProgressInvoicingType() {
        return progressInvoicingType;
    }

    public void setProgressInvoicingType(String progressInvoicingType) {
        this.progressInvoicingType = progressInvoicingType;
    }

//    public EdsCrmAccount getSupplier() {
//        return this.supplier;
//    }

//    public void setSupplier(final EdsCrmAccount supplier) {
//        this.supplier = supplier;
//    }

    public Boolean isSalesOrder() {
        return isSalesOrder != null ? isSalesOrder : false;
    }

    public void setSalesOrder(Boolean salesOrder) {
        isSalesOrder = salesOrder;
    }

    public EdsInvoiceTerms getInvoiceTerms() {
        return invoiceTerms;
    }

    public void setInvoiceTerms(EdsInvoiceTerms invoiceTerms) {
        this.invoiceTerms = invoiceTerms;
    }

    public BigDecimal getShippingAmount() {
        return shippingAmount;
    }

    public void setShippingAmount(BigDecimal shippingMethodAmount) {
        this.shippingAmount = shippingMethodAmount;
    }

    @Override
    public void setEntityStatus(EdsReference overallStatus) {
        setStatus(overallStatus);
        setOverallStatus(overallStatus);
    }

    @Override
    public List<EdsApprover> getApprovers() {
        if (this.isSalesOrder()) {
            return orderApprovers;
        }
        return approvers;
    }

    @Override
    public void setApprovers(List<EdsApprover> approvers) {
        if (this.isSalesOrder()) {
            this.orderApprovers = approvers;
            this.approvers = new ArrayList<>();
        } else {
            this.approvers = approvers;
        }
    }

    public List<EdsApprover> getOrderApprovers() {
        return orderApprovers;
    }

    public void setOrderApprovers(List<EdsApprover> orderApprovers) {
        this.orderApprovers = orderApprovers;
    }

    @Override
    public boolean isCurrentApproverApproved() {
        return isOk(getCurrentApprover()) && isOk(getCurrentApprover().getStatus()) && APPROVE.equals(getCurrentApprover().getStatus().getCode());
    }

    @Override
    public boolean isCurrentApproverRejected() {
        return isOk(getCurrentApprover()) && isOk(getCurrentApprover().getStatus()) && MANAGER_REJECT.equals(getCurrentApprover().getStatus().getCode());
    }

    @Override
    protected EdsReference getStatusByMarkedAction(Integer actionID) {
        if (!isOk(actionID)) {
            return null;
        }
        ReferenceManager referenceManager = StaticContextAccessor.getBean(ReferenceManager.class);
        if (actionID.equals(ApproverItem.MARK_AS_REJECTED)) {
            return referenceManager.findReference(Constants.INVOICE_STATUS, MANAGER_REJECT);
        } else if (actionID.equals(ApproverItem.MARK_AS_APPROVED) && isSalesOrder()) {
            return referenceManager.findReference(Constants.INVOICE_STATUS, SALE_ORDER);
        } else if (actionID.equals(ApproverItem.MARK_AS_APPROVED)) {
            return referenceManager.findReference(Constants.INVOICE_STATUS, APPROVE);
        } else if (actionID.equals(ApproverItem.SEND_TO_CREATOR)) {
            return referenceManager.findReference(Constants.INVOICE_STATUS, MANAGER_REJECT);
        } else if (actionID.equals(ApproverItem.SEND_TO_DIRECTORS)) {
            return referenceManager.findReference(Constants.INVOICE_STATUS, MANAGER_REJECT);
        }
        return null;
    }

    @Override
    public void updateRejectedStatus() {
        if (getStatus() != null && MANAGER_REJECT.equals(getStatus().getCode())) {
            ReferenceManager referenceManager = StaticContextAccessor.getBean(ReferenceManager.class);
            setEntityStatus(referenceManager.findReference(Constants.INVOICE_STATUS, Constants.SUBMITTED_TO_MANAGER));
        }
    }

    public NewInvoiceItem[] wrapToNewItem() {
        List<NewInvoiceItem> quoteItems = new ArrayList<>();
        if (getQuoteItems() != null && getQuoteItems().size() > 0) {
            for (EdsQuoteItem item : getQuoteItems()) {
                quoteItems.add(getItem(item));
            }
        }
        return quoteItems.toArray(new NewInvoiceItem[]{});
    }

    public SolrInputDocument wrapToSolrDocument(EdsSaleQuote saleQuote, Integer companyID, Integer pickID) {
        String compositID = companyID + "_" + saleQuote.getObjectID();
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField(SolrSaleInvoiceRepresenter.FIELD_COMPOSITE_ID, compositID);
        doc.addField(SolrSaleInvoiceRepresenter.FIELD_COMPANY_ID, companyID);
        doc.addField(SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID, saleQuote.getObjectID());
        doc.addField(SolrSaleInvoiceRepresenter.FIELD_OPPORTUNITY_ID, saleQuote.getOpportunityID());

        if (saleQuote.getClientOrSupplier() != null) {
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_CLIENT_ID, saleQuote.getClientOrSupplier().getObjectID());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_CLIENT_NAME, saleQuote.getClientOrSupplier().getName());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_CLIENT_ID_NAME, saleQuote.getClientOrSupplier().getObjectID() + SolrSaleInvoiceRepresenter.SPLIT + saleQuote.getClientOrSupplier().getName());
        }

        if (saleQuote.getClientContact() != null) {
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_CLIENT_CONTACT_ID, saleQuote.getClientContact().getObjectID());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_CLIENT_CONTACT_EMAIL, saleQuote.getClientContact().getPrimaryEmail());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_CLIENT_CONTACT_ID_EMAIL, saleQuote.getClientContact().getObjectID() + SolrSaleInvoiceRepresenter.SPLIT + saleQuote.getClientContact().getName());
        }

        if (saleQuote.getCurrency() != null) {
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_CURRENCY_ID, saleQuote.getCurrency().getObjectID());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_CURRENCY_NAME, saleQuote.getCurrency().getName());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_CURRENCY_ID_NAME, saleQuote.getCurrency().getObjectID() + SolrSaleInvoiceRepresenter.SPLIT + saleQuote.getCurrency().getName());
        }

        if (saleQuote.getRelatedProject() != null) {
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_RELATED_PROJECT_ID, saleQuote.getRelatedProject().getObjectID());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_RELATED_PROJECT_NAME, saleQuote.getRelatedProject().getName());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_RELATED_PROJECT_NUMBER, saleQuote.getRelatedProject().getNumber());
            if (saleQuote.getRelatedProject().getStatus() != null) {
                doc.addField(SolrSaleInvoiceRepresenter.FIELD_RELATED_PROJECT_CODE, saleQuote.getRelatedProject().getStatus().getCode());
            }
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_RELATED_PROJECT_ID_NAME, saleQuote.getRelatedProject().getObjectID() + SolrSaleInvoiceRepresenter.SPLIT + saleQuote.getRelatedProject().getName());
        }

        if (saleQuote.getPdfTemplate() != null) {
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_PDF_TEMPLATE_ID, saleQuote.getPdfTemplate().getObjectID());
        }

        for (EdsProject project : saleQuote.getProjects()) {
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_MULTI_PROJECT_ID, project.getObjectID());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_MULTI_PROJECT_NAME, project.getName());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_MULTI_PROJECT_NUMBER, project.getNumber());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_MULTI_PROJECT_ID_NAME, project.getObjectID() + SolrSaleInvoiceRepresenter.SPLIT + project.getName());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_MULTI_PROJECT_NUMBER_NAME, project.getNumber() + SolrSaleInvoiceRepresenter.ARROW + project.getName());
        }

        if (saleQuote.getStatus() != null) {
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_STATUS_ID, saleQuote.getStatus().getObjectID());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_STATUS_NAME, saleQuote.getStatus().getName());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_STATUS_ID_NAME, saleQuote.getStatus().getObjectID() + SolrSaleInvoiceRepresenter.SPLIT + saleQuote.getStatus().getName());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_STATUS_SORDER, saleQuote.getStatus().getSorder());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_STATUS_CODE, saleQuote.getStatus().getCode());
        }

        if (saleQuote.getShippingMethod() != null) {
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_SHPPINGMETHOD_ID, saleQuote.getShippingMethod().getObjectID());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_SHPPINGMETHOD_NAME, saleQuote.getShippingMethod().getName());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_SHPPINGMETHOD_ID_NAME, saleQuote.getShippingMethod().getObjectID() + SolrSaleInvoiceRepresenter.SPLIT + saleQuote.getShippingMethod().getName());
        }
        if (saleQuote.getCreator() != null) {
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_CREATOR_ID, saleQuote.getCreator().getObjectID());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_CREATOR_NAME, saleQuote.getCreator().getName());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_CREATOR_ID_NAME, saleQuote.getCreator().getObjectID() + SolrSaleInvoiceRepresenter.SPLIT + saleQuote.getCreator().getName());
        }
        if (pickID != null) {
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_PICKLIST_ID, pickID);
        }
        if (getCurrentApprover() != null && getCurrentApprover().getExactEmployee() != null) {
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_CURRENT_APPROVER_ID, getCurrentApprover().getExactEmployee().getObjectID());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_CURRENT_APPROVER_NAME, getCurrentApprover().getExactEmployee().getFullName());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_CURRENT_APPROVER_ID_NAME, getCurrentApprover().getExactEmployee().getObjectID() + SolrSaleInvoiceRepresenter.SPLIT + getCurrentApprover().getExactEmployee().getFullName());
        }
        if (CollectionUtils.isNotEmpty(getQuoteItems())) {
            getQuoteItems().stream()
                    .filter(edsQuoteItem -> edsQuoteItem.getItem() != null)
                    .forEach(edsQuoteItem
                            -> doc.addField(SolrPurchaseInvoiceRepresenter.FIELD_ITEM_ID, edsQuoteItem.getItem().getObjectID()));

        }
        doc.addField(SolrSaleInvoiceRepresenter.FIELD_INVOICE_NUMBER, saleQuote.getNumber());
        doc.addField(SolrSaleInvoiceRepresenter.FIELD_INVOICE_DATE, saleQuote.getInvoiceDate());
        doc.addField(SolrSaleInvoiceRepresenter.FIELD_DUE_DATE, saleQuote.getDueDate());

        BigDecimal totalCurrency = saleQuote.getTotalInInvoiceCurrency() != null ? saleQuote.getTotalInInvoiceCurrency() :
                saleQuote.getTotal().multiply(saleQuote.getExchangeRate());


        doc.addField(SolrSaleInvoiceRepresenter.FIELD_TOTAL_TAXES, saleQuote.getTotalTaxes() != null ? saleQuote.getTotalTaxes().doubleValue() : 0d);
        doc.addField(SolrSaleInvoiceRepresenter.FIELD_EXCHARGE_RATE, saleQuote.getExchangeRate().doubleValue());
        doc.addField(SolrSaleInvoiceRepresenter.FIELD_TOTAL_INVOICE_CURRENCY, totalCurrency.doubleValue());
        doc.addField(SolrSaleInvoiceRepresenter.FIELD_TOTAL_INVOICE_BASE, saleQuote.getTotal().doubleValue());
        doc.addField(SolrSaleInvoiceRepresenter.FIELD_DUE_AMOUNT, totalCurrency.doubleValue());
        doc.addField(SolrSaleInvoiceRepresenter.FIELD_IS_SALES_ORDER, saleQuote.isSalesOrder());
        doc.addField(SolrSaleInvoiceRepresenter.FIELD_PO_NUMBER, saleQuote.getPoNumber());
        doc.addField(SolrSaleInvoiceRepresenter.FIELD_SUB_TOTAL, saleQuote.getSubtotal().doubleValue());
        doc.addField(SolrSaleInvoiceRepresenter.FIELD_NET_AMOUNT_TOTAL, saleQuote.getNetAmountTotal().doubleValue());
        doc.addField(SolrSaleInvoiceRepresenter.IS_PROGRESS_INVOICING, saleQuote.isProgressInvoicing());
        doc.addField(SolrSaleInvoiceRepresenter.INTRODUCTION, saleQuote.getIntroduction());
        doc.addField(SolrSaleInvoiceRepresenter.REFERENCE, saleQuote.getReference());
        doc.addField(SolrSaleInvoiceRepresenter.FIELD_CREATED_DATE, saleQuote.getCreationDate());
        doc.addField(SolrSaleInvoiceRepresenter.FIELD_UPDATED_DATE, saleQuote.getUpdatedDate());
        doc.addField(SolrSaleInvoiceRepresenter.FIELD_TAX_CALCULATION_TYPE, saleQuote.getTaxCalculationType());
        CustomFieldsUtils.setInSolrCustomFields(doc, getCustomFields());
        return doc;
    }

    public SaleQuoteSolrItem getSolrRPC() {
        SaleQuoteSolrItem saleQuote = new SaleQuoteSolrItem();

        saleQuote.setObjectID(getObjectID());
        saleQuote.setOpportunity(new SelectItem(getOpportunityID()));

        if (getClientOrSupplier() != null) {
            EdsCrmAccount clientOrSupplier = getClientOrSupplier();
            saleQuote.setClient(clientOrSupplier.getAsSelectItem());

            if (!getClientOrSupplier().getOwners().isEmpty()) {
                getClientOrSupplier().getOwners().forEach(o -> saleQuote.getCustomerOwnerIds().add(o.getObjectID()));
            }
        }

        if (getClientContact() != null) {
            EdsCrmContact contact = getClientContact();
            SelectItem clientContact = new SelectItem(contact.getObjectID(), contact.getPrimaryEmail());
            saleQuote.setClientContact(clientContact);
        }

        if (getCurrency() != null) {
            EdsCurrency currency = getCurrency();
            saleQuote.setCurrency(currency.getAsSelectItem());
        }

        if (getRelatedProject() != null) {
            EdsProject relatedProject = getRelatedProject();
            SelectItem project = new SelectItem(relatedProject.getObjectID(), relatedProject.getName());
            project.setNumber(relatedProject.getNumber());
            if (relatedProject.getStatus() != null) {
                project.setCode(relatedProject.getStatus().getCode());
            }
            saleQuote.setRelatedProject(project);
        }

        if (getPdfTemplate() != null) {
            saleQuote.setPdfTemplateId(getPdfTemplate().getObjectID());
        }

        if (getProjects() != null && !getProjects().isEmpty()) {
            getProjects().forEach(project -> {
                SelectItem mProject = new SelectItem(project.getObjectID(), project.getName());
                mProject.setNumber(project.getNumber());
                saleQuote.getMultiProject().add(mProject);

            });
        }

        if (getStatus() != null) {
            EdsReference status = getStatus();
            saleQuote.setStatus(status.getRPC());
        }

        if (getShippingMethod() != null) {
            EdsShippingMethod shippingMethod = getShippingMethod();
            saleQuote.setShippingMethod(shippingMethod.getAsSelectItem());
        }

        if (getCreator() != null) {
            saleQuote.setCreator(getCreator().getAsSelectItem());
        }

        Set<EdsProject> projects = getProjects();
        if (projects != null && !projects.isEmpty()) {
            for (EdsProject project : projects) {
                saleQuote.getProjectidsFromEmployeeId().add(project.getManager().getObjectID());
            }
        }

        if (getCurrentApprover() != null && getCurrentApprover().getExactEmployee() != null) {
            EdsUser approver = getCurrentApprover().getExactEmployee();
            saleQuote.setCurrentApprover(approver.getAsSelectItem());
        }
        if (!getQuoteItems().isEmpty()) {
            getQuoteItems().stream()
                    .filter(edsQuoteItem -> edsQuoteItem.getItem() != null)
                    .forEach(edsQuoteItem -> saleQuote.getItemIds().add(edsQuoteItem.getItem().getObjectID()));
        }
        saleQuote.setInvoiceNumber(getNumber());
        saleQuote.setInvoiceDate(getInvoiceDate());
        saleQuote.setDueDate(getDueDate());

        BigDecimal totalCurrency = getTotalInInvoiceCurrency() != null ? getTotalInInvoiceCurrency() :
                getTotal().multiply(getExchangeRate());

        saleQuote.setTotalTaxes(getTotalTaxes() != null ? getTotalTaxes() : BigDecimal.ZERO);
        saleQuote.setExchangeRate(getExchangeRate());
        saleQuote.setTotalInvoiceCurrency(totalCurrency);
        saleQuote.setTotalInvoiceBase(getTotal());
        saleQuote.setDueAmount(totalCurrency);
        saleQuote.setSubTotal(getSubtotal());
        saleQuote.setNetAmountTotal(getNetAmountTotal());
        saleQuote.setSalesOrder(isSalesOrder());
        saleQuote.setPoNumber(getPoNumber());
        saleQuote.setProgressInvoicing(isProgressInvoicing());
        saleQuote.setIntroduction(getIntroduction());
        saleQuote.setReference(getReference());
        saleQuote.setCreatedDate(getCreationDate());
        saleQuote.setUpdatedDate(getUpdatedDate());
        saleQuote.setTaxCalculationType(getTaxCalculationType());

        return saleQuote;
    }

    @Override
    public void setValueForField(EdsModelField field, Object value) {
        if (field != null && field.getField_ID() != null) {
            String fieldID = field.getField_ID();
            switch (fieldID) {
                case CustomFormConstants.ACCOUNTING.CUSTOMER -> setClient((EdsCrmAccount) value);
                case CustomFormConstants.ACCOUNTING.PROJECT -> setRelatedProject((EdsProject) value);
                case CustomFormConstants.ACCOUNTING.INVOICE_DATE -> setInvoiceDate((Date) value);
                case CustomFormConstants.ACCOUNTING.DUE_DATE -> setDueDate((Date) value);
                case CustomFormConstants.ACCOUNTING.REFERENCE -> setReference((String) value);
                case CustomFormConstants.ACCOUNTING.PO_NUMBER -> setPoNumber((String) value);
                case CustomFormConstants.ACCOUNTING.SHIP_VIA -> setShippingMethod((EdsShippingMethod) value);
                case CustomFormConstants.ACCOUNTING.SUB_TOTAL -> setSubtotal((BigDecimal) value);
                case CustomFormConstants.ACCOUNTING.TOTAL -> setTotal((BigDecimal) value);
                case CustomFormConstants.ACCOUNTING.TOTAL_INVOICE_CURRENCY ->
                        setTotalInInvoiceCurrency((BigDecimal) value);
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
        } else if (fieldID.equals(CustomFormConstants.ACCOUNTING.CUSTOMER)) {
            return getClient();
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
        } else if (fieldID.equals(CustomFormConstants.ACCOUNTING.SHIP_VIA)) {
            return getShippingMethod();
        } else if (fieldID.equals(CustomFormConstants.ACCOUNTING.SUB_TOTAL)) {
            return getSubtotal();
        } else if (fieldID.equals(CustomFormConstants.ACCOUNTING.TOTAL)) {
            return getTotal();
        } else if (fieldID.equals(CustomFormConstants.ACCOUNTING.TOTAL_INVOICE_CURRENCY)) {
            return getTotalInInvoiceCurrency();
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

    @Override
    public void jumpToPreviousApprover() {
        EdsApprover prevPrevApprover = null;
        EdsApprover prevApprover = null;
        for (EdsApprover approver : getApprovers()) {
            if (isOk(prevPrevApprover)) {
                prevApprover = approver;
            } else {
                prevPrevApprover = approver;
            }
            if (getCurrentApprover().getObjectID().equals(approver.getObjectID())) {
                int currentIndex = getApprovers().indexOf(prevApprover);
                if (currentIndex > 0) {
                    EdsApprover prev = getApprovers().get(currentIndex - 1);
                    if (prev != null) {
                        setCurrentApprover(prev);
                    }
                } else {
                    setCurrentApprover(prevApprover);
                }
                if (currentIndex >= 2) {
                    EdsApprover prevPrev = getApprovers().get(currentIndex - 2);
                    if (prevPrev != null) {
                        setPrevApprover(prevPrev);
                    }
                } else {
                    setPrevApprover(null);
                }
                break;
            }
        }
    }

    public EdsReference getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(EdsReference rejectReason) {
        this.rejectReason = rejectReason;
    }

    public String getRejectText() {
        return rejectText;
    }

    public void setRejectText(String rejectText) {
        this.rejectText = rejectText;
    }
}
