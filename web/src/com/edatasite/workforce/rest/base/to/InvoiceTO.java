package com.edatasite.workforce.rest.base.to;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.InvoiceTermsItem;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoiceItem;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.helpers.WrapUtils;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by Dilshod Madrahimov.
 */
public class InvoiceTO implements IsSerializable {
    Integer id;
    String number;
    String reference;
    Long invoiceDate;
    Long dueDate;
    BigDecimal total;
    BigDecimal subTotal;

    BigDecimal baseTotal;
    BigDecimal baseSubTotal;
    BigDecimal discountTotal;
    BigDecimal baseDiscountTotal;
    BigDecimal taxTotal;
    BigDecimal baseTaxTotal;
    BigDecimal dueAmount;
    BigDecimal exchangeRate;
    BigDecimal shippingPrice;
    SelectItemTO status;
    SelectItemTO customer;
    SelectItemTO supplier;
    AddressTO billToAddress;
    AddressTO shipToAddress;
    CurrencyTO currency;
    String introduction;
    SelectItemTO invoiceType;
    ShippingMethodTO shippingMethod;
    SelectItemTO terms;
    SelectItemTO taxType;
    SelectItemTO bankAccount;
    SelectItemTO account;
    SelectItemTO emailTemplate;
    ArrayList<InvoiceItemTO> items;

    public InvoiceTO() {

    }

    public InvoiceTO(NewInvoice invoice) {
        this.id = invoice.getID();
        this.number = invoice.getInvoiceNumber();
        if (invoice.getInvoiceDate() != null) {
            this.invoiceDate = invoice.getInvoiceDate().getDateLong();
        }
        if (invoice.getDueDate() != null) {
            this.dueDate = invoice.getDueDate().getDateLong();
        }
        this.status = new SelectItemTO(invoice.getStatus(), invoice.getStatusCode());
        this.currency = new CurrencyTO(invoice.getCurrencyID(), invoice.getCurrencyName(), invoice.getCurrencySymbol());
        this.total = invoice.getTotalInInvoiceCurrency();
    }

    public InvoiceTO(NewInvoice invoice, String type) {
        this(invoice);
        if (invoice.getTypeItem() != null) {
            if (ApiConstants.SALES_INVOICE.equalsIgnoreCase(type) || ApiConstants.SALES_QUOTE.equalsIgnoreCase(type)) {
                this.customer = new SelectItemTO(invoice.getTypeItem().getId(), invoice.getTypeItem().getName());
            } else if (ApiConstants.PURCHASE_INVOICE.equalsIgnoreCase(type) || ApiConstants.PURCHASE_ORDER.equalsIgnoreCase(type)) {
                this.supplier = new SelectItemTO(invoice.getTypeItem().getId(), invoice.getTypeItem().getName());
            } else if (ApiConstants.CREDIT_NOTE.equalsIgnoreCase(type)) {
                if (Constants.PAYABLE.equals(invoice.getType())) {
                    this.supplier = new SelectItemTO(invoice.getTypeItem().getId(), invoice.getTypeItem().getName());
                } else {
                    this.customer = new SelectItemTO(invoice.getTypeItem().getId(), invoice.getTypeItem().getName());
                }
            }
        }
        this.reference = invoice.getReference();
        this.baseTotal = invoice.getTotal();
        this.subTotal = invoice.getSubtotal();
        this.discountTotal = invoice.getTotalDiscount();
        this.taxTotal = invoice.getTotalTaxesInInvoiceCurrency();
        this.baseTaxTotal = invoice.getTotalTaxes();
        this.exchangeRate = invoice.getExchageRate();

        this.billToAddress = new AddressTO(invoice.getBillAddress());
        this.shipToAddress = new AddressTO(invoice.getMailAddress());
        this.introduction = invoice.getIntroduction();
        if (invoice.getShippingMethod() != null) {
            this.shippingMethod = new ShippingMethodTO(invoice.getShippingMethod());
        }
        this.shippingPrice = invoice.getShippingPrice();
        if (invoice.getAccountsReceivablePayable() != null) {
            this.account = new SelectItemTO(invoice.getAccountsReceivablePayable().getId(), invoice.getAccountsReceivablePayable().getName());
        }
        if (invoice.getItems() != null && invoice.getItems().length > 0) {
            ArrayList<InvoiceItemTO> invoiceItemTOs = new ArrayList<>();
            for (NewInvoiceItem item : invoice.getItems()) {
                invoiceItemTOs.add(new InvoiceItemTO(item));
            }
            this.items = invoiceItemTOs;
        }
        if (invoice.getCustomFieldItems() != null && invoice.getCustomFieldItems().size() > 0) {
            ArrayList<InvoiceItemTO> invoiceItemTOs = new ArrayList<>();
            for (NewInvoiceItem item : invoice.getItems()) {
                invoiceItemTOs.add(new InvoiceItemTO(item));
            }
            this.items = invoiceItemTOs;
        }

    }

    public NewInvoice wrap(InvoiceTO invoiceTO) {
        NewInvoice invoice = new NewInvoice();
        invoice.setID(invoiceTO.getId());
        invoice.setBookkeep(true);
        invoice.setInvoiceNumber(invoiceTO.getNumber());
        if (invoiceTO.getInvoiceDate() != null) {
            invoice.setInvoiceDate(new DateNonConvertable(WrapUtils.longToDate(invoiceTO.getInvoiceDate())));
        }
        if (invoiceTO.getDueDate() != null) {
            invoice.setDueDate(new DateNonConvertable(WrapUtils.longToDate(invoiceTO.getDueDate())));
        }
        if (invoiceTO.getCustomer() != null) {
            invoice.setClientID(invoiceTO.getCustomer().getId());
        } else if (invoiceTO.getSupplier() != null) {
            invoice.setClientID(invoiceTO.getSupplier().getId());
        }
        if (invoiceTO.getInvoiceType() != null) {
            invoice.setInvoiceType(getInvoiceType().getId());
        }
        invoice.setReference(invoiceTO.getReference());
        if (invoiceTO.getBillToAddress() != null) {
            invoice.setBillAddressID(invoiceTO.getBillToAddress().getId());
        }
        if (invoiceTO.getShipToAddress() != null) {
            invoice.setMailAddressID(invoiceTO.getShipToAddress().getId());
        }
        if (invoiceTO.getCurrency() != null) {
            invoice.setCurrencyID(invoiceTO.getCurrency().getId());
        }
        if (invoiceTO.getExchangeRate() != null) {
            invoice.setExchageRate(invoiceTO.getExchangeRate());
        }
        if (invoiceTO.getShippingMethod() != null) {
            invoice.setShippingMethodID(invoiceTO.getShippingMethod().getId());
        }
        if (invoiceTO.getTerms() != null) {
            InvoiceTermsItem invoiceTermsItem = new InvoiceTermsItem();
            invoiceTermsItem.setId(invoiceTO.getTerms().getId());
            invoiceTermsItem.setName(invoiceTO.getTerms().getName());
            invoiceTermsItem.setDays(Integer.valueOf(invoiceTO.getTerms().getCode()));
            invoice.setInvoiceTermsItem(invoiceTermsItem);
        }
        invoice.setIntroduction(invoiceTO.getIntroduction());

        //items
        if (invoiceTO.getItems() != null && !invoiceTO.getItems().isEmpty()) {
            ArrayList<NewInvoiceItem> items = new ArrayList<>();
            for (InvoiceItemTO itemTO : invoiceTO.getItems()) {
                items.add(itemTO.wrap(itemTO));
            }
            invoice.setItems(items.toArray(new NewInvoiceItem[0]));
        }
        invoice.setSubtotal(invoiceTO.getSubTotal());
        invoice.setTotalDiscount(invoiceTO.getDiscountTotal());
        invoice.setShippingPrice(invoiceTO.getShippingPrice());
        invoice.setTotalInInvoiceCurrency(invoiceTO.getTotal());
        if (invoiceTO.getBaseTotal() == null || BigDecimal.ZERO.compareTo(invoiceTO.getBaseTotal()) == 0) {
            invoice.setTotal(invoiceTO.getTotal());
        } else {
            invoice.setTotal(invoiceTO.getBaseTotal());
        }
        invoice.setTotalTaxes(invoiceTO.getTaxTotal());
        invoice.setExchageRate(invoiceTO.getExchangeRate());
        if (invoiceTO.getTaxType() != null) {
            invoice.setTaxCalculationType(invoiceTO.getTaxType().getId());
        }
        if (invoiceTO.getBankAccount() != null) {
            invoice.setBankAccount(invoiceTO.getBankAccount().wrap(invoiceTO.getBankAccount()));
        }
        if (invoiceTO.getAccount() != null) {
            invoice.setAccountsReceivablePayable(new AccountItem(invoiceTO.getAccount().getId(), invoiceTO.getAccount().getName()));
        }
        if (invoiceTO.getEmailTemplate() != null) {
            invoice.setEmailTemplateID(invoiceTO.getEmailTemplate().getId());
        }

        return invoice;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Long getDueDate() {
        return dueDate;
    }

    public void setDueDate(Long dueDate) {
        this.dueDate = dueDate;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public BigDecimal getBaseTotal() {
        return baseTotal;
    }

    public void setBaseTotal(BigDecimal baseTotal) {
        this.baseTotal = baseTotal;
    }

    public BigDecimal getBaseSubTotal() {
        return baseSubTotal;
    }

    public void setBaseSubTotal(BigDecimal baseSubTotal) {
        this.baseSubTotal = baseSubTotal;
    }

    public BigDecimal getDiscountTotal() {
        return discountTotal;
    }

    public void setDiscountTotal(BigDecimal discountTotal) {
        this.discountTotal = discountTotal;
    }

    public BigDecimal getBaseDiscountTotal() {
        return baseDiscountTotal;
    }

    public void setBaseDiscountTotal(BigDecimal baseDiscountTotal) {
        this.baseDiscountTotal = baseDiscountTotal;
    }

    public BigDecimal getTaxTotal() {
        return taxTotal;
    }

    public void setTaxTotal(BigDecimal taxTotal) {
        this.taxTotal = taxTotal;
    }

    public BigDecimal getBaseTaxTotal() {
        return baseTaxTotal;
    }

    public void setBaseTaxTotal(BigDecimal baseTaxTotal) {
        this.baseTaxTotal = baseTaxTotal;
    }

    public BigDecimal getDueAmount() {
        return dueAmount;
    }

    public void setDueAmount(BigDecimal dueAmount) {
        this.dueAmount = dueAmount;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public Long getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Long invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public SelectItemTO getStatus() {
        return status;
    }

    public void setStatus(SelectItemTO status) {
        this.status = status;
    }

    public SelectItemTO getCustomer() {
        return customer;
    }

    public void setCustomer(SelectItemTO customer) {
        this.customer = customer;
    }

    public AddressTO getBillToAddress() {
        return billToAddress;
    }

    public void setBillToAddress(AddressTO billToAddress) {
        this.billToAddress = billToAddress;
    }

    public AddressTO getShipToAddress() {
        return shipToAddress;
    }

    public void setShipToAddress(AddressTO shipToAddress) {
        this.shipToAddress = shipToAddress;
    }

    public CurrencyTO getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyTO currency) {
        this.currency = currency;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public ArrayList<InvoiceItemTO> getItems() {
        return items;
    }

    public void setItems(ArrayList<InvoiceItemTO> items) {
        this.items = items;
    }

    public BigDecimal getShippingPrice() {
        return shippingPrice;
    }

    public void setShippingPrice(BigDecimal shippingPrice) {
        this.shippingPrice = shippingPrice;
    }

    public SelectItemTO getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(SelectItemTO invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public ShippingMethodTO getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(ShippingMethodTO shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    public SelectItemTO getTerms() {
        return terms;
    }

    public void setTerms(SelectItemTO terms) {
        this.terms = terms;
    }

    public SelectItemTO getTaxType() {
        return taxType;
    }

    public void setTaxType(SelectItemTO taxType) {
        this.taxType = taxType;
    }

    public SelectItemTO getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(SelectItemTO bankAccount) {
        this.bankAccount = bankAccount;
    }

    public SelectItemTO getSupplier() {
        return supplier;
    }

    public void setSupplier(SelectItemTO supplier) {
        this.supplier = supplier;
    }

    public SelectItemTO getAccount() {
        return account;
    }

    public void setAccount(SelectItemTO account) {
        this.account = account;
    }

    public SelectItemTO getEmailTemplate() {
        return emailTemplate;
    }

    public void setEmailTemplate(SelectItemTO emailTemplate) {
        this.emailTemplate = emailTemplate;
    }
}
