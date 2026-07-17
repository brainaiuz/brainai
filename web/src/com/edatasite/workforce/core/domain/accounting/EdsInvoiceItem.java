package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsVat;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.enums.DeferredTransactionType;
import com.edatasite.workforce.gwt.accounting.client.rpc.BillableExpenseItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 27.05.2009
 * Time: 17:44:18
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "invoiceItem")
public class EdsInvoiceItem extends EdsBaseInvoiceItem {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id")
    private EdsInvoice invoice;

    @Column(name = "projectBasedInvoiceDescription")
    @Type(type = "text")
    private String projectBasedInvoiceDescription;

    private Integer quoteItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private EdsCrmAccount client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "saleinvoice_id")
    private EdsSaleInvoice saleInvoice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "markupaccount_id")
    private EdsAccount markupAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "markuptaxId")
    private EdsVat markupTax;

    @Column(name = "markupAmount", precision = 25, scale = 5)
    private BigDecimal markupAmount;

    @Column(name = "markupTaxAmount", precision = 25, scale = 5)
    private BigDecimal markupTaxAmount;

    @Column(name = "fromTimesheet", columnDefinition = "boolean default false")
    private boolean fromTimesheet;

    @Column(name = "expenceItemId")
    private Integer expenceItemId;

    @Column(name = "faiCategoryId")
    private Integer faiCategoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faiCategoryId", insertable = false, updatable = false)
    private EdsReference faiCategory;

    @Temporal(TemporalType.DATE)
    private Date fromDate;
    @Temporal(TemporalType.DATE)
    private Date toDate;

    public EdsInvoice getInvoice() {
        return invoice;
    }

    public void setInvoice(EdsInvoice invoice) {
        this.invoice = invoice;
    }

    public String getProjectBasedInvoiceDescription() {
        return projectBasedInvoiceDescription;
    }

    public void setProjectBasedInvoiceDescription(String projectBasedInvoiceDescription) {
        this.projectBasedInvoiceDescription = projectBasedInvoiceDescription;
    }

    public Integer getExpenceItemId() {
        return expenceItemId;
    }

    public void setExpenceItemId(Integer expenceItemId) {
        this.expenceItemId = expenceItemId;
    }

    public Integer getQuoteItemId() {
        return quoteItemId;
    }

    public void setQuoteItemId(Integer quoteItemId) {
        this.quoteItemId = quoteItemId;
    }

    @Override
    public Integer getTaxCalculationType() {
        return invoice.getTaxCalculationType();
    }

    public BigDecimal calculateDiscountedNetInBase() {
        return calculateDiscountedNet().divide(getInvoice().getExchangeRate(), 8, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal calculateDiscountedNet() {
        //Calculating Net Amount in Base Currency
        BigDecimal netAmount = getQty().multiply(getUnitPrice());
        //Calculating Discount Amount
        BigDecimal discountAmount = BigDecimal.ZERO;
        if (getDiscount() != null) {
            discountAmount = netAmount.multiply(getDiscount()).divide(AccountingConstants.HUNDRED, 8, BigDecimal.ROUND_HALF_UP);
        } else if (getDiscountAmount() != null && getDiscountAmount().compareTo(BigDecimal.ZERO) != 0) {
            discountAmount = getDiscountAmount();
        }
        //Calculating Discounted Net
        return netAmount.subtract(discountAmount);
    }

    public EdsCrmAccount getClient() {
        return client;
    }

    public void setClient(EdsCrmAccount client) {
        this.client = client;
    }

    public EdsSaleInvoice getSaleInvoice() {
        return saleInvoice;
    }

    public void setSaleInvoice(EdsSaleInvoice saleInvoice) {
        this.saleInvoice = saleInvoice;
    }

    public boolean isFromTimesheet() {
        return fromTimesheet;
    }

    public void setFromTimesheet(boolean fromTimesheet) {
        this.fromTimesheet = fromTimesheet;
    }

    public EdsAccount getMarkupAccount() {
        return markupAccount;
    }

    public void setMarkupAccount(EdsAccount markupAccount) {
        this.markupAccount = markupAccount;
    }

    public BigDecimal getMarkupAmount() {
        return markupAmount;
    }

    public void setMarkupAmount(BigDecimal markupAmount) {
        this.markupAmount = markupAmount;
    }

    public EdsVat getMarkupTax() {
        return markupTax;
    }

    public void setMarkupTax(EdsVat markupTax) {
        this.markupTax = markupTax;
    }

    public BigDecimal getMarkupTaxAmount() {
        return markupTaxAmount;
    }

    public void setMarkupTaxAmount(BigDecimal markupTaxAmount) {
        this.markupTaxAmount = markupTaxAmount;
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

    public void setFaiCategoryId(Integer faiCategoryId) {
        this.faiCategoryId = faiCategoryId;
    }

    public Integer getFaiCategoryId() {
        return faiCategoryId;
    }

    public EdsReference getFaiCategory() {
        return faiCategory;
    }

    public void setFaiCategory(EdsReference faiCategory) {
        this.faiCategory = faiCategory;
    }

    public boolean isDeferredTransasctionItem() {
        Integer productType = getItem() != null ? getItem().getType() : null;
        return fromDate != null && toDate != null && EdsItem.SERVICE.equals(productType);
    }

    public BillableExpenseItem createBillableExpenseItem(boolean... forEdit) {
        BillableExpenseItem beItem = new BillableExpenseItem();
        beItem.setObjectID(getObjectID());
        beItem.setNumber(invoice.getNumber());
        beItem.setType(BillableExpenseItem.PURCHASE_AS_EXPENSE);
        beItem.setDescription(getDescription());
        beItem.setInvoiceId(invoice.getObjectID());

        if (client != null) {
            beItem.setClient(client.getAsSelectItem());
        }
        if (getAccount() != null) {
            beItem.setAccount(getAccount().getAsSelectItem());
        }

        BigDecimal totalTax = getTaxAmount() != null ? getTaxAmount() : BigDecimal.ZERO;
        totalTax = totalTax.add(getDoubleTaxAmount() != null ? getDoubleTaxAmount() : BigDecimal.ZERO);
        BigDecimal totalInCurrency = totalTax.add(getNet().multiply(invoice.getExchangeRate()));

        beItem.setAmountInCurrency(totalInCurrency);
        beItem.setAmountInBase(totalInCurrency.divide(invoice.getExchangeRate(), ServerUtils.getSystemCalculationScale(), BigDecimal.ROUND_HALF_UP));
        beItem.setCurrencyID(invoice.getCurrency() != null ? invoice.getCurrency().getObjectID() : null);

        beItem.setMarkupAmount(getMarkupAmount());
        beItem.setMarkupAmountInBase(getMarkupAmount() != null ? getMarkupAmount().divide(invoice.getExchangeRate(), ServerUtils.getSystemCalculationScale(), BigDecimal.ROUND_HALF_UP) : null);

        if (getMarkupAccount() != null) {
            beItem.setMarkupAccount(getMarkupAccount().getAsSelectItem());
        }
        beItem.setDate(invoice.getInvoiceDate());

        if (forEdit != null && forEdit.length > 0) {

            if (getMarkupAccount() != null) {
                beItem.setMarkupAccount(getMarkupAccount().getAsSelectItem());
            }
            if (getMarkupTax() != null) {
                beItem.setMarkupTax(getMarkupTax().createTaxItem());
            }
            beItem.setMarkupTaxAmount(getMarkupTaxAmount());
            beItem.setMarkupTaxAmountInBase(getMarkupTaxAmount() != null ? getMarkupTaxAmount().divide(invoice.getExchangeRate(), ServerUtils.getSystemCalculationScale(), BigDecimal.ROUND_HALF_UP) : null);
            beItem.setSelected(forEdit[0]);
        }


        return beItem;
    }

    public EdsDeferredTransactionItem createDeferredTransactionItem() {
        EdsDeferredTransactionItem dtItem = new EdsDeferredTransactionItem();
        EdsInvoice invoice = getInvoice();
        dtItem.setType(Constants.RECEIVABLE.equals(invoice.getType()) ? DeferredTransactionType.SALE_INVOICE : DeferredTransactionType.PURCHASE_INVOICE);
        dtItem.setEntityId(invoice.getObjectID());
        dtItem.setService(getItem());
        dtItem.setServiceId(dtItem.getService().getObjectID());
        dtItem.setFromDate(getFromDate());
        dtItem.setToDate(getToDate());

        long days = TimeUnit.DAYS.convert(getToDate().getTime() - getFromDate().getTime(), TimeUnit.MILLISECONDS) + 1;
        dtItem.setDailyRate(getNet().divide(BigDecimal.valueOf(days), ServerUtils.getSystemCalculationScale(), BigDecimal.ROUND_HALF_UP));

        dtItem.setAccount(getAccount());
        dtItem.setAccountId(dtItem.getAccount().getObjectID());
        return dtItem;
    }
}
