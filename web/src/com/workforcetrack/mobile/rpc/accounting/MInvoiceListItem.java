package com.workforcetrack.mobile.rpc.accounting;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoiceItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.TotalTaxItem;
import com.workforcetrack.mobile.rpc.client.MAdressData;
import com.workforcetrack.mobile.rpc.expense.MCurrencyItem;

import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Aziz
 * Date: 5/23/11
 * Time: 12:52 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement(name = "invoiceListItem")
public class MInvoiceListItem {

    private Integer objectID;
    private String invoiceNumber;
    private Integer clientID;
    private Integer projectID;
    private String clientName;
    private String reference;
    private Integer clientContactID;
    private int currencyID;
    private BigDecimal exchageRate;
    private boolean isRecurringInvoice;
    private String poNumber;
    private BigDecimal subtotal;
    private BigDecimal totalDiscount;
    private BigDecimal totalInInvoiceCurrency;
    private BigDecimal total;
    private BigDecimal totalTaxes;
    private List<MTotalTaxItem> totalTaxItems;
    private String type;
    private Integer taxCalculationType;
    private List<MNewInvoiceItem> items;
    private boolean bookkeep = true;
    private String statusCode = "";
    private Date invoiceDate;
    private Date dueDate;
    private String currencyName;
    private String baseCurrencyName;

    private Integer billAddressID;
    private Integer mailAddressID;
    private Integer[] projectIDs;
    private String addressName;
    private Integer countryID;
    private Integer stateID;

    //FOR EDIT Data
    private MClientSupplierAddressData clientAddressData;
    private MTaxList taxList;
    private MCurrencyItem baseCurrency;
    private List<MCurrencyItem> currencyItems;
    private MAdressData adressData;
    private String relatedProjectName;
    private Integer managerId;
    private String managerName;

    public MInvoiceListItem() {
    }

    public MInvoiceListItem(NewInvoice newInvoice) {
        this.objectID = newInvoice.getID();
        this.invoiceNumber = newInvoice.getInvoiceNumber();
        this.clientID = newInvoice.getClientID();
        this.projectID = newInvoice.getRelatedProjectID();
        this.relatedProjectName = newInvoice.getRelatedProjectName();
        if (newInvoice.getRelatedProject() != null && newInvoice.getRelatedProjectName() == null) {
            this.projectID = newInvoice.getRelatedProject().getId();
            this.relatedProjectName = newInvoice.getRelatedProject().getName();
        }
        if (newInvoice.getCurrentApprover() != null) {
            this.managerId = newInvoice.getCurrentApproverSelectItem().getId();
            this.managerName = newInvoice.getCurrentApproverSelectItem().getName();
        }
        this.reference = newInvoice.getReference();
        this.clientContactID = newInvoice.getClientContactID();
        this.currencyID = newInvoice.getCurrencyID();
        this.exchageRate = newInvoice.getExchageRate();
        this.isRecurringInvoice = newInvoice.isRecurringInvoice();
        this.poNumber = newInvoice.getPoNumber();
        this.subtotal = newInvoice.getSubtotal();
        this.totalDiscount = newInvoice.getTotalDiscount();
        this.totalInInvoiceCurrency = newInvoice.getTotalInInvoiceCurrency();
        this.total = newInvoice.getTotal();
        this.totalTaxes = newInvoice.getTotalTaxes();
        this.type = newInvoice.getType();
        this.taxCalculationType = newInvoice.getTaxCalculationType();
        this.bookkeep = newInvoice.isBookkeep();
        this.statusCode = newInvoice.getStatusCode();
        this.clientName = newInvoice.getClientName();
        this.invoiceDate = newInvoice.getInvoiceDate().getNonConvertedDate();
        this.dueDate = newInvoice.getDueDate().getNonConvertedDate();
        this.billAddressID = newInvoice.getBillAddressID();
        this.mailAddressID = newInvoice.getMailAddressID();
        this.currencyName = newInvoice.getCurrencyName();
        this.baseCurrencyName = newInvoice.getBaseCurrencyName();
        this.baseCurrency = new MCurrencyItem(newInvoice.getBaseCurrency());

        if (newInvoice.getTotalTaxItems() != null) {
            totalTaxItems = new ArrayList<>();
            for (TotalTaxItem totalTaxItem : newInvoice.getTotalTaxItems()) {
                this.totalTaxItems.add(new MTotalTaxItem(totalTaxItem));
            }
        }

        if (newInvoice.getItems() != null) {
            items = new ArrayList<>();
            for (NewInvoiceItem newInvoiceItem : newInvoice.getItems()) {
                this.items.add(new MNewInvoiceItem(newInvoiceItem));
            }
        }

        if (newInvoice.getCurrencies() != null && newInvoice.getCurrencies().length > 0) {
            this.currencyItems = new ArrayList<>();
            for (CurrencyItem currencyItem : newInvoice.getCurrencies()) {
                this.currencyItems.add(new MCurrencyItem(currencyItem));
            }
        }
    }

    public NewInvoice convertToNewInvoice(NewInvoice newInvoice) {
        if (newInvoice == null) {
            newInvoice = new NewInvoice();
        }
        if (this.getObjectID() != null) {
            newInvoice.setID(this.getObjectID());
        }
        newInvoice.setInvoiceNumber(this.getInvoiceNumber());
        newInvoice.setClientID(this.getClientID());
        newInvoice.setRelatedProjectID(this.getProjectID());
        if (this.getManagerId() != null) {
            newInvoice.setCurrentApproverSelectItem(new SelectItem(this.getManagerId(), this.getManagerName()));
        }
        newInvoice.setReference(this.getReference());
        newInvoice.setCurrencyID(this.getCurrencyID());
        newInvoice.setExchageRate(this.getExchageRate());
        newInvoice.setRecurringInvoice(this.isRecurringInvoice());
        newInvoice.setPoNumber(this.getPoNumber());
        newInvoice.setSubtotal(this.getSubtotal());
        newInvoice.setTotalDiscount(this.getTotalDiscount());
        newInvoice.setTotalInInvoiceCurrency(this.getTotalInInvoiceCurrency());
        newInvoice.setTotal(this.getTotal());
        newInvoice.setTotalTaxes(this.getTotalTaxes());
        newInvoice.setType(this.getType());
        newInvoice.setTaxCalculationType(this.getTaxCalculationType());
        newInvoice.setBookkeep(this.isBookkeep());
        newInvoice.setStatusCode(this.getStatusCode());
        newInvoice.setClientName(this.clientName);
        newInvoice.setInvoiceDate(new DateNonConvertable(this.invoiceDate));
        newInvoice.setDueDate(new DateNonConvertable(this.dueDate));
        newInvoice.setBillAddressID(this.billAddressID);
        newInvoice.setMailAddressID(this.mailAddressID);
        newInvoice.setCurrencyName(this.currencyName);
        newInvoice.setBaseCurrencyName(this.baseCurrencyName);
        newInvoice.setBaseCurrency(this.baseCurrency.convertToCurrencyItem());
        newInvoice.setForceSave(true);

        if (totalTaxItems != null) {
            List<TotalTaxItem> totalTaxItems1 = new ArrayList<>();
            for (MTotalTaxItem totalTaxItem : totalTaxItems) {
                totalTaxItems1.add(totalTaxItem.convertToTotalTaxItem(null));
            }
            newInvoice.setTotalTaxItems(totalTaxItems1.toArray(new TotalTaxItem[]{}));
        }

        if (items != null) {
            List<NewInvoiceItem> newInvoiceLineItems = new ArrayList<>();
            for (MNewInvoiceItem newInvoiceItem : items) {
                newInvoiceLineItems.add(newInvoiceItem.convertToNewInvoiceItem(null));
            }
            newInvoice.setItems(newInvoiceLineItems.toArray(new NewInvoiceItem[0]));
        }

        return newInvoice;
    }

    public MTaxList getTaxList() {
        return taxList;
    }

    public void setTaxList(MTaxList taxList) {
        this.taxList = taxList;
    }

    public List<MCurrencyItem> getCurrencyItems() {
        return currencyItems;
    }

    public void setCurrencyItems(List<MCurrencyItem> currencyItems) {
        this.currencyItems = currencyItems;
    }

    public MClientSupplierAddressData getClientAddressData() {
        return clientAddressData;
    }

    public void setClientAddressData(MClientSupplierAddressData clientAddressData) {
        this.clientAddressData = clientAddressData;
    }

    public MCurrencyItem getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(MCurrencyItem baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getBaseCurrencyName() {
        return baseCurrencyName;
    }

    public void setBaseCurrencyName(String baseCurrencyName) {
        this.baseCurrencyName = baseCurrencyName;
    }

    public Integer getBillAddressID() {
        return billAddressID;
    }

    public void setBillAddressID(Integer billAddressID) {
        this.billAddressID = billAddressID;
    }

    public Integer getMailAddressID() {
        return mailAddressID;
    }

    public void setMailAddressID(Integer mailAddressID) {
        this.mailAddressID = mailAddressID;
    }

    public Integer[] getProjectIDs() {
        return projectIDs;
    }

    public void setProjectIDs(Integer[] projectIDs) {
        this.projectIDs = projectIDs;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
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

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Integer getClientID() {
        return clientID;
    }

    public void setClientID(Integer clientID) {
        this.clientID = clientID;
    }

    public int getCurrencyID() {
        return currencyID;
    }

    public void setCurrencyID(int currencyID) {
        this.currencyID = currencyID;
    }

    public BigDecimal getExchageRate() {
        return exchageRate;
    }

    public void setExchageRate(BigDecimal exchageRate) {
        this.exchageRate = exchageRate;
    }

    public boolean isRecurringInvoice() {
        return isRecurringInvoice;
    }

    public void setRecurringInvoice(boolean recurringInvoice) {
        isRecurringInvoice = recurringInvoice;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(BigDecimal totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public BigDecimal getTotalInInvoiceCurrency() {
        return totalInInvoiceCurrency;
    }

    public void setTotalInInvoiceCurrency(BigDecimal totalInInvoiceCurrency) {
        this.totalInInvoiceCurrency = totalInInvoiceCurrency;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getTotalTaxes() {
        return totalTaxes;
    }

    public void setTotalTaxes(BigDecimal totalTaxes) {
        this.totalTaxes = totalTaxes;
    }

    public List<MTotalTaxItem> getTotalTaxItems() {
        return totalTaxItems;
    }

    public void setTotalTaxItems(List<MTotalTaxItem> totalTaxItems) {
        this.totalTaxItems = totalTaxItems;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getTaxCalculationType() {
        return taxCalculationType;
    }

    public void setTaxCalculationType(Integer taxCalculationType) {
        this.taxCalculationType = taxCalculationType;
    }

    public List<MNewInvoiceItem> getItems() {
        return items;
    }

    public void setItems(List<MNewInvoiceItem> items) {
        this.items = items;
    }

    public boolean isBookkeep() {
        return bookkeep;
    }

    public void setBookkeep(boolean bookkeep) {
        this.bookkeep = bookkeep;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }


    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public Integer getCountryID() {
        return countryID;
    }

    public void setCountryID(Integer countryID) {
        this.countryID = countryID;
    }

    public Integer getStateID() {
        return stateID;
    }

    public void setStateID(Integer stateID) {
        this.stateID = stateID;
    }

    public MAdressData getAdressData() {
        return adressData;
    }

    public void setAdressData(MAdressData adressData) {
        this.adressData = adressData;
    }

    public Integer getClientContactID() {
        return clientContactID;
    }

    public void setClientContactID(Integer clientContactID) {
        this.clientContactID = clientContactID;
    }

    public Integer getProjectID() {
        return projectID;
    }

    public void setProjectID(Integer projectID) {
        this.projectID = projectID;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getRelatedProjectName() {
        return relatedProjectName;
    }

    public void setRelatedProjectName(String relatedProjectName) {
        this.relatedProjectName = relatedProjectName;
    }

    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }
}