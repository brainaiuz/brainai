package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsVat;
import com.edatasite.workforce.core.domain.customfields.EdsInvoiceCustomFields;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.StaticContextAccessor;
import com.edatasite.workforce.gwt.core.server.db.InvoiceManager;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoiceItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.TotalTaxItem;
import org.hibernate.annotations.Where;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 27.05.2009
 * Time: 17:27:39
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "quote")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class EdsQuote extends EdsBaseInvoice {

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "quote_id")
    @OrderBy("sorder asc")
    private List<EdsQuoteItem> quoteItems = new ArrayList<>();

    @Column(name = "convertedtoinvoice")
    private Boolean convertedToInvoice = false;

    @Column(name = "fourdigitnumber")
    private Integer fourDigitNumber;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "quote_id")
    private List<EdsQuoteTaxTotal> quoteTaxTotals = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private EdsInvoiceCustomFields customFields;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "historicalparent")
    private EdsQuote historicalParent;

    private Boolean convertedToProject;

    private Integer taxCalculationType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bankaccountid")
    private EdsBankAccount bankAccount;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
   @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "converted_items",
            joinColumns = {@JoinColumn(name = "quote_id")},
            inverseJoinColumns = {@JoinColumn(name = "invoice_id")})
    @Where(clause = "deleted = 'false' ")
    private List<EdsInvoice> invoices = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shippingMethodId")
    private EdsShippingMethod shippingMethod;

    @Column(precision = 25, scale = 5)
    private BigDecimal shippingAmount;

    @Column(name = "price_level_id")
    private Integer priceLevelID;

    @Column(precision = 25, scale = 5)
    private BigDecimal totalDiscount;

    private String fromNumber;

    private Date quotationDate;

    public List<EdsQuoteItem> getQuoteItems() {
        return quoteItems;
    }

    public void setQuoteItems(List<EdsQuoteItem> quoteItems) {
        this.quoteItems = quoteItems;
    }

    public Boolean getConvertedToInvoice() {
        return convertedToInvoice;
    }

    public void setConvertedToInvoice(Boolean convertedToInvoice) {
        this.convertedToInvoice = convertedToInvoice;
    }

    public Integer getFourDigitNumber() {
        return fourDigitNumber;
    }

    public void setFourDigitNumber(Integer fourDigitNumber) {
        this.fourDigitNumber = fourDigitNumber;
    }

    public BigDecimal getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(BigDecimal totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public List<EdsQuoteTaxTotal> getQuoteTaxTotals() {
        return quoteTaxTotals;
    }

    public void setQuoteTaxTotals(List<EdsQuoteTaxTotal> quoteTaxTotals) {
        this.quoteTaxTotals = quoteTaxTotals;
    }

    public EdsInvoiceCustomFields getCustomFields() {
        return customFields;
    }

    public void setCustomFields(EdsInvoiceCustomFields customFields) {
        this.customFields = customFields;
    }

    public static NewInvoice getQuoteData(EdsQuote quote) {
        NewInvoice result = getData(quote);
        result.setConvertedToInvoice(quote.getConvertedToInvoice());
        result.setConvertedToProject(quote.getConvertedToProject());

        if (quote.getQuoteTaxTotals().size() > 0) {
            TotalTaxItem[] items = new TotalTaxItem[quote.getQuoteTaxTotals().size()];
            int i = 0;
            for (EdsQuoteTaxTotal taxTotal : quote.getQuoteTaxTotals()) {
                EdsVat vat = taxTotal.getVat();
                items[i] = new TotalTaxItem();
                items[i].setTaxItem(vat.createTaxItem());
                items[i].setTaxAmount(taxTotal.getAmount());
                i++;
            }
            result.setTotalTaxItems(items);
        }

//        if (Constants.PARTIAL_RECEIVED.equals(result.getStatusCode())) {
//            result.setPartialReceive(isPartialReceive(quote.getQuoteItems()));
//        }
        result.setFromQuoteNumber(quote.getFromNumber());
        if (quote.getQuotationDate() != null) {
            result.setQuotationDate(new DateNonConvertable(quote.getQuotationDate()));
        }
        result.setFixedAssetRelated(quote.isFixedAssetRelated());
        if (quote.getInvoices() != null && !quote.getInvoices().isEmpty()) {
            NewInvoice[] invoices = new NewInvoice[quote.getInvoices().size()];
            int i = 0;
            List<EdsInvoice> items = quote.getInvoices().stream().sorted(Comparator.comparingInt(EdsInvoice::getObjectID)).collect(Collectors.toList());
            for (EdsInvoice inv : items) {
                invoices[i] = getData(inv);
                if (inv instanceof EdsSaleInvoice) {
                    invoices[i].setPercentage(((EdsSaleInvoice) inv).getQuotePercent());
                }
                i++;
            }
            result.setInvoicedItems(invoices);
        }
        if (quote instanceof EdsPurchaseOrder && ((EdsPurchaseOrder) quote).getOpportunityID() != null) {
            result.setOpportunityID(((EdsPurchaseOrder) quote).getOpportunityID());
        }
        return result;
    }

    public ItemsData initItems() {
        boolean isContainInvItem = false;
        ArrayList<NewInvoiceItem> items = new ArrayList<>();
        InvoiceManager invoiceManager = StaticContextAccessor.getBean(InvoiceManager.class);
        Map<Integer, NewInvoiceItem> quoteItemsMap = invoiceManager.getInvoiceItems(getObjectID(), true);
        if (quoteItemsMap != null && !quoteItemsMap.isEmpty()) {
            isContainInvItem = quoteItemsMap.entrySet().parallelStream().anyMatch(e -> e.getValue().getItemType() != null && (EdsItem.INVENTORY_ITEM.equals(e.getValue().getItemType()) || EdsItem.RENTAL_ITEM.equals(e.getValue().getItemType())
                    || EdsItem.PRODUCT_KIT.equals(e.getValue().getItemType())));
            for (EdsQuoteItem item : getQuoteItems()) {
                NewInvoiceItem qItem = quoteItemsMap.get(item.getObjectID());

                if (getItemCustomFields() != null && !getItemCustomFields().isEmpty() && item.getCustomFields() != null) {
                    qItem.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(item.getCustomFields(), generateCloneItemCustomFields()));
                }
                items.add(qItem);
            }
        }
        return new ItemsData(items, isContainInvItem);
    }

    public EdsQuote getHistoricalParent() {
        return historicalParent;
    }

    public void setHistoricalParent(EdsQuote historicalParent) {
        this.historicalParent = historicalParent;
    }

    public Boolean getConvertedToProject() {
        return convertedToProject;
    }

    public void setConvertedToProject(Boolean convertedToProject) {
        this.convertedToProject = convertedToProject;
    }

    public List<EdsInvoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<EdsInvoice> invoices) {
        this.invoices = invoices;
    }

    public EdsBankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(EdsBankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    public Integer getTaxCalculationType() {
        return taxCalculationType;
    }

    public void setTaxCalculationType(Integer taxCalculationType) {
        if(!ServerUtils.equalsInteger(this.taxCalculationType, taxCalculationType)){
            addChange(CustomFormConstants.AMOUNT);
        }
        this.taxCalculationType = taxCalculationType;
    }

    public Integer getPriceLevelID() {
        return priceLevelID;
    }

    public void setPriceLevelID(Integer priceLevelID) {
        this.priceLevelID = priceLevelID;
    }

    public String getFromNumber() {
        return fromNumber;
    }

    public void setFromNumber(String fromNumber) {
        this.fromNumber = fromNumber;
    }

    public Date getQuotationDate() {
        return quotationDate;
    }

    public void setQuotationDate(Date quotationDate) {
        this.quotationDate = quotationDate;
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

    public BigDecimal getShippingAmount() {
        return shippingAmount;
    }

    public void setShippingAmount(BigDecimal shippingMethodAmount) {
        this.shippingAmount = shippingMethodAmount;
    }

    public Set<EdsProject> getProjects() {
        Set<EdsProject> projects = new HashSet<>();

        if (getQuoteItems() != null && !getQuoteItems().isEmpty()) {
            for (EdsQuoteItem item : getQuoteItems()) {
                if (item.getProject() != null) {
                    projects.add(item.getProject());
                }
            }
        }

        return projects;
    }

    private ArrayList<CompanyCustomFieldItem> generateCloneItemCustomFields() {

        if (itemCustomFields != null && !itemCustomFields.isEmpty()) {
            ArrayList<CompanyCustomFieldItem> items = new ArrayList<>();

            for (CompanyCustomFieldItem item : itemCustomFields) {
                items.add(item.cloneObject());
            }

            return items;
        }

        return null;
    }
}
