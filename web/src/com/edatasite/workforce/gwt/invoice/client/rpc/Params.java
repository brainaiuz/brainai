package com.edatasite.workforce.gwt.invoice.client.rpc;

import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 11.08.2010
 * Time: 19:28:13
 * To change this template use File | Settings | File Templates.
 */
public class Params implements IsSerializable {
    private Integer objectID;
    private String type;
    private String formType;
    private String convertFormType;
    private Integer convertFormId;
    private String invoiceCustomType;
    private String progressiveInvoiceType;

    private Integer externalFormID;
    private Integer externalObjectID;
    private ArrayList<Integer> externalObjectIDList;
    private Integer contactID;//SQ,SI  for CRM
    private Integer opportunityID;//SQ,SI  for CRM
    private Integer reservationID;
    private Integer relatedProjectID;
    private String crmFormName;
    private Integer relatedInvoiceId;
    private boolean recurringInvoice;
    private boolean projectBasedInvoice;
    private boolean fromGettingStarted;
    private BigDecimal maximalAmount;
    private Date conversionDate;
    private MultiQuoteConvertItem multiQuoteConvertItem;
    private boolean saleQuote;
    private boolean view;
    private Integer clientId;
    private Integer supplierId;
    private ViewName viewName;

    public Params() {
    }

    public boolean isEditForm() {
        return objectID != null;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getExternalFormID() {
        return externalFormID;
    }

    public void setExternalFormID(Integer externalFormID) {
        this.externalFormID = externalFormID;
    }

    public boolean isExternalForm(Integer externalFormID) {
        return externalFormID.equals(this.externalFormID) && this.externalObjectID != null;
    }

    public Integer getExternalObjectID() {
        return externalObjectID;
    }

    public void setExternalObjectID(Integer externalObjectID) {
        this.externalObjectID = externalObjectID;
    }

    public ArrayList<Integer> getExternalObjectIDList() {
        return externalObjectIDList;
    }

    public void setExternalObjectIDList(ArrayList<Integer> externalObjectIDList) {
        this.externalObjectIDList = externalObjectIDList;
    }

    public Integer getContactID() {
        return contactID;
    }

    public void setContactID(Integer contactID) {
        this.contactID = contactID;
    }

    public Integer getOpportunityID() {
        return opportunityID;
    }

    public void setOpportunityID(Integer opportunityID) {
        this.opportunityID = opportunityID;
    }

    public Integer getReservationID() {
        return reservationID;
    }

    public void setReservationID(Integer reservationID) {
        this.reservationID = reservationID;
    }

    public Integer getRelatedProjectID() {
        return relatedProjectID;
    }

    public void setRelatedProjectID(Integer relatedProjectID) {
        this.relatedProjectID = relatedProjectID;
    }

    public String getCrmFormName() {
        return crmFormName;
    }

    public void setCrmFormName(String crmFormName) {
        this.crmFormName = crmFormName;
    }

    public String getInvoiceCustomType() {
        return invoiceCustomType;
    }

    public void setInvoiceCustomType(String invoiceCustomType) {
        this.invoiceCustomType = invoiceCustomType;
    }

    public boolean isRecurringInvoice() {
        return recurringInvoice;
    }

    public void setRecurringInvoice(boolean recurringInvoice) {
        this.recurringInvoice = recurringInvoice;
    }

    public boolean isProjectBasedInvoice() {
        return projectBasedInvoice;
    }

    public void setProjectBasedInvoice(boolean projectBasedInvoice) {
        this.projectBasedInvoice = projectBasedInvoice;
    }

    public boolean isFromGettingStarted() {
        return fromGettingStarted;
    }

    public void setFromGettingStarted(boolean fromGettingStarted) {
        this.fromGettingStarted = fromGettingStarted;
    }

    public BigDecimal getMaximalAmount() {
        return maximalAmount;
    }

    public void setMaximalAmount(BigDecimal maximalAmount) {
        this.maximalAmount = maximalAmount;
    }

    public Date getConversionDate() {
        return conversionDate;
    }

    public void setConversionDate(Date conversionDate) {
        this.conversionDate = conversionDate;
    }

    public MultiQuoteConvertItem getMultiQuoteConvertItem() {
        return multiQuoteConvertItem;
    }

    public void setMultiQuoteConvertItem(MultiQuoteConvertItem multiQuoteConvertItem) {
        this.multiQuoteConvertItem = multiQuoteConvertItem;
    }

    public boolean isSaleQuote() {
        return saleQuote;
    }

    public void setSaleQuote(boolean saleQuote) {
        this.saleQuote = saleQuote;
    }

    public Integer getRelatedInvoiceId() {
        return relatedInvoiceId;
    }

    public void setRelatedInvoiceId(Integer relatedInvoiceId) {
        this.relatedInvoiceId = relatedInvoiceId;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public String getFormType() {
        return this.formType;
    }

    public void setFormType(final String formType) {
        this.formType = formType;
    }

    public String getConvertFormType() {
        return this.convertFormType;
    }

    public void setConvertFormType(final String convertFormType) {
        this.convertFormType = convertFormType;
    }

    public Integer getConvertFormId() {
        return this.convertFormId;
    }

    public void setConvertFormId(final Integer convertFormId) {
        this.convertFormId = convertFormId;
    }

    public boolean isView() {
        return this.view;
    }

    public void setView(final boolean view) {
        this.view = view;
    }

    public ViewName getViewName() {
        return viewName;
    }

    public void setViewName(ViewName viewName) {
        this.viewName = viewName;
    }

    public String getProgressiveInvoiceType() {
        return progressiveInvoiceType;
    }

    public void setProgressiveInvoiceType(String progressiveInvoiceType) {
        this.progressiveInvoiceType = progressiveInvoiceType;
    }
}
