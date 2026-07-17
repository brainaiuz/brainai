package com.edatasite.workforce.core.solr.document;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: Dilsh0d Tadjiev on 15.08.2020 23:32.
 */
@SolrDocument(collection = "shippingDataCore")
public class ShippingDataSolrDoc {

    @Id
    @Indexed(name = "oid", type = "string", required = true)
    private String oid;

    @Field("companyId")
    @Indexed(name = "companyId", type = "pint", required = true)
    private Integer companyId;

    @Field("shippingDataId")
    @Indexed(name = "shippingDataId", type = "pint", required = true)
    private Integer shippingDataId;

    @Field("shippingDataNumber")
    private String shippingDataNumber;

    @Field("quoteNumber")
    private String quoteNumber;

    @Field("shippingDate")
    private Date shippingDate;

    @Field("saleInvoiceId")
    private Integer saleInvoiceId;

    @Field("clientId")
    private Integer clientId;

    @Field("clientName")
    private String clientName;

    @Field("clientIdName")
    @Indexed(name = "clientIdName", type = "string", stored = false)
    private String clientIdName;

    @Field("currencyId")
    private Integer currencyId;

    @Field("currencyName")
    private String currencyName;

    @Field("currencyIdName")
    @Indexed(name = "currencyIdName", type = "string", stored = false)
    private String currencyIdName;

    @Field("shippingDataStatusName")
    private String shippingDataStatusName;

    @Field("creatorId")
    private Integer creatorId;

    @Field("creatorLocationId")
    private Integer creatorLocationId;
    @Field("creatorName")
    private String creatorName;

    @Field("creatorIdName")
    @Indexed(name = "creatorIdName", type = "string", stored = false)
    private String creatorIdName;

    @Field("creationDate")
    private Date creationDate;

    @Field("invoiceDate")
    private Date invoiceDate;

    @Field("dueDate")
    private Date dueDate;

    @Field("invoiceNumber")
    private String invoiceNumber;

    @Field("statusId")
    private Integer statusId;

    @Field("statusName")
    private String statusName;

    @Field("statusIdName")
    @Indexed(name = "statusIdName", type = "string", stored = false)
    private String statusIdName;

    @Field("statusSorder")
    private Integer statusSorder;

    @Field("statusCode")
    private String statusCode;

    @Field("isGdn")
    private Boolean isGdn;

    @Field("gdnIsSalesOrder")
    private Boolean gdnIsSalesOrder;

    @Field("warehouseId")
    @Indexed(name = "warehouseId", type = "pints")
    private List<Integer> warehouseId = new ArrayList<>();

    public List<Integer> getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(List<Integer> warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getShippingDataId() {
        return shippingDataId;
    }

    public void setShippingDataId(Integer shippingDataId) {
        this.shippingDataId = shippingDataId;
    }

    public String getShippingDataNumber() {
        return shippingDataNumber;
    }

    public void setShippingDataNumber(String shippingDataNumber) {
        this.shippingDataNumber = shippingDataNumber;
    }

    public String getQuoteNumber() {
        return quoteNumber;
    }

    public void setQuoteNumber(String quoteNumber) {
        this.quoteNumber = quoteNumber;
    }

    public Date getShippingDate() {
        return shippingDate;
    }

    public void setShippingDate(Date shippingDate) {
        this.shippingDate = shippingDate;
    }

    public Integer getSaleInvoiceId() {
        return saleInvoiceId;
    }

    public void setSaleInvoiceId(Integer saleInvoiceId) {
        this.saleInvoiceId = saleInvoiceId;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientIdName() {
        return clientIdName;
    }

    public void setClientIdName(String clientIdName) {
        this.clientIdName = clientIdName;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getCurrencyIdName() {
        return currencyIdName;
    }

    public void setCurrencyIdName(String currencyIdName) {
        this.currencyIdName = currencyIdName;
    }

    public String getShippingDataStatusName() {
        return shippingDataStatusName;
    }

    public void setShippingDataStatusName(String shippingDataStatusName) {
        this.shippingDataStatusName = shippingDataStatusName;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCreatorIdName() {
        return creatorIdName;
    }

    public void setCreatorIdName(String creatorIdName) {
        this.creatorIdName = creatorIdName;
    }

    public Integer getCreatorLocationId() {
        return creatorLocationId;
    }

    public void setCreatorLocationId(Integer creatorLocationId) {
        this.creatorLocationId = creatorLocationId;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getInvoiceNumber() {
        return invoiceNumber != null ? invoiceNumber : "";
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getStatusIdName() {
        return statusIdName;
    }

    public void setStatusIdName(String statusIdName) {
        this.statusIdName = statusIdName;
    }

    public Integer getStatusSorder() {
        return statusSorder;
    }

    public void setStatusSorder(Integer statusSorder) {
        this.statusSorder = statusSorder;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public Boolean getGdn() {
        return isGdn != null && isGdn;
    }

    public void setGdn(Boolean gdn) {
        isGdn = gdn;
    }

    public Boolean getGdnIsSalesOrder() {
        return gdnIsSalesOrder != null && gdnIsSalesOrder;
    }

    public void setGdnIsSalesOrder(Boolean gdnIsSalesOrder) {
        this.gdnIsSalesOrder = gdnIsSalesOrder;
    }
}
