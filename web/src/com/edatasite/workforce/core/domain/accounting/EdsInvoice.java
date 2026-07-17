package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.core.domain.EdsAssemblyItem;
import com.edatasite.workforce.core.domain.EdsExpense;
import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.EdsProductKitItems;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsVat;
import com.edatasite.workforce.core.domain.customfields.EdsInvoiceCustomFields;
import com.edatasite.workforce.gwt.accounting.client.rpc.BillableExpenseItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductKitItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.AssemblyItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.server.app.costofgoods.COGSService;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TaxItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.StaticContextAccessor;
import com.edatasite.workforce.gwt.core.server.db.InvoiceManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.WarehouseManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoiceItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.PaymentItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.TotalTaxItem;
import com.google.common.collect.HashBasedTable;
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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 27.05.2009
 * Time: 14:39:27
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "invoice")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class EdsInvoice extends EdsBaseInvoice {
    public static final String AMOUNTS_TYPE = "_AMOUNTS_TYPE";

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "invoiceId")
    @Where(clause = "(deleted = 'false' or deleted is null)")
    //historicalParent parameter added for getting revision history do not remove it
    private List<EdsInvoicePayment> payments = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "creditNoteId")
    @Where(clause = "(deleted = 'false' or deleted is null)")
    //historicalParent parameter added for getting revision history do not remove it
    private List<EdsInvoicePayment> refunds = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id")
    @OrderBy("objectID")
    private List<EdsInvoiceItem> invoiceItems = new ArrayList<>();

    @Column(precision = 25, scale = 5)
    private BigDecimal totalDiscount;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "converted_items",
            joinColumns = {@JoinColumn(name = "invoice_id")},
            inverseJoinColumns = {@JoinColumn(name = "quote_id")})
    @Where(clause = "(deleted = 'false' or deleted is null)")
    private Set<EdsQuote> convertedQuotes = new HashSet<>(); //this is will be SO/PO

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "converted_shipping_data",
            joinColumns = @JoinColumn(name = "invoice_id"),
            inverseJoinColumns = @JoinColumn(name = "shipping_data_id")
    )
    private Set<EdsShippingData> convertedShippingData = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private EdsReference customType; //this is for the invoice customization

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id")
    private List<EdsInvoiceTaxTotal> invoiceTaxTotals = new ArrayList<>();

    private Integer taxCalculationType;

    @Column(name = "calc_scale")
    private Integer calcScale;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private EdsInvoiceCustomFields customFields;

    private Boolean isCreditNote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creditnote_invoiceid")
    private EdsInvoice creditNoteInvoice;

    @Column(precision = 25, scale = 5)
    private BigDecimal billExpTotal;

    @Column(precision = 25, scale = 5)
    private BigDecimal billExpTaxTotal;

    @Column(precision = 25, scale = 5)
    private BigDecimal markupAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "markupAccountId")
    private EdsAccount markupAccount;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY, mappedBy = "invoice")
    private List<EdsExpense> expense = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY, mappedBy = "saleInvoice")
    private List<EdsInvoiceItem> itemsAsExpense = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY, mappedBy = "invoice")
    private List<EdsManualJournalItem> mjItemsAsExpense = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY, mappedBy = "invoice")
    private List<EdsBankTransferItem> btItemsAsExpense = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY, mappedBy = "invoice")
    private List<EdsBankCheckItem> bchItemsAsExpense = new ArrayList<>();

    @Column(name = "isPercent")
    private Boolean isPercent = false;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "historicalparent")
    private EdsInvoice historicalParent;

    @Column(name = "price_level_id")
    private Integer priceLevelID;

    @Column(name = "client_discount_id")
    private Integer clientDiscountID;

    private String zatcaStatus;
    @Column(columnDefinition = "text")
    private String zatcaHash;
    @Column(columnDefinition = "text")
    private String zatcaXml;
    @Column(columnDefinition = "text")
    private String zatcaQRCode;
    // reported invoice date to zatca
    private Date reportedDate;

    private String noteReason;
    private Integer notePaymentCode;

    protected boolean hasInventoryItem() {
        WarehouseManager warehouseManager = StaticContextAccessor.getBean(WarehouseManager.class);
        HashBasedTable<Integer, Integer, BigDecimal> itemsMap = HashBasedTable.create();
        for (EdsInvoiceItem invoiceItem : getInvoiceItems()) {
            Integer warehouseId = invoiceItem.getWarehouse() != null ? invoiceItem.getWarehouse().getObjectID() : warehouseManager.getDefaultWarehouse().getObjectID();
            COGSService.mapRequestedItems(invoiceItem.getItem(), invoiceItem.getQty(), warehouseId, itemsMap);
            if (!itemsMap.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    protected boolean isConvertedToGdnGrn() {
        return !getConvertedShippingData().isEmpty();
    }

    public static NewInvoice getInvoiceData(EdsInvoice invoice) {
        GenericSettingsManager genericSettingsManager = (GenericSettingsManager) ApplicationContextProvider.applicationContext.getBean("genericSettingsManager");
        InvoiceManager invoiceManager = (InvoiceManager) ApplicationContextProvider.applicationContext.getBean("invoiceManager");

        NewInvoice result = getData(invoice);

        result.setTaxCalculationType(invoice.getTaxCalculationType());
        result.setTotalDiscount(invoice.getTotalDiscount());
        result.setPaymentItems(getPaymentItemsList(invoice).toArray(new PaymentItem[]{}));
        result.setPaidAmount(invoice.getFullPayments());
        result.setCalcScale(invoice.getCalcScale());

        if (!genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_MULTI_QUOTE_CONVERT)) {
            if (invoice.getConvertedQuotes() != null && invoice.getConvertedQuotes().size() == 1)
                result.setConvertedItemID(invoice.getConvertedQuotes().iterator().next().getObjectID());//this is will be SO/PO
        } else {
            result.setOrderBaseinvoiceOrderIds(invoiceManager.getConvertedQuoteIds(result.getID()));
        }
        if (!invoice.getInvoiceTaxTotals().isEmpty()) {
            List<TotalTaxItem> items = new LinkedList<>();
            for (EdsInvoiceTaxTotal taxTotal : invoice.getInvoiceTaxTotals()) {
                EdsVat vat = taxTotal.getVat();
                if (vat != null) {
                    TotalTaxItem item = new TotalTaxItem();
                    item.setTaxItem(vat.createTaxItem());
                    item.setTaxAmount(taxTotal.getAmount());
                    items.add(item);
                }
            }
            result.setTotalTaxItems(items.toArray(new TotalTaxItem[]{}));
        }

        //billable Expense Data
        result.setBillableExpenseAmount(invoice.getBillExpTotal());
        result.setBillableExpenseTaxAmount(invoice.getBillExpTaxTotal());
        result.setHasBillableExpense(invoice.getBillExpTotal() != null && invoice.getBillExpTotal().compareTo(BigDecimal.ZERO) != 0);
        result.setMarkupAmount(invoice.getMarkupAmount());

        if (invoice.getMarkupAccount() != null) {
            result.setMarkupAccount(new SelectItem(invoice.getMarkupAccount().getObjectID(), invoice.getMarkupAccount().getName()));
        }

        result.setPercent(invoice.isPercent());
        ArrayList<BillableExpenseItem> beList = null;

        if (invoice.getExpense() != null && invoice.getExpense().size() > 0) {
            beList = new ArrayList<>();
            for (EdsExpense exp : invoice.getExpense()) {
                beList.add(exp.createBillableExpenseItem(true));
            }
        }
        if (invoice.getItemsAsExpense() != null && !invoice.getItemsAsExpense().isEmpty()) {

            if (beList == null) {
                beList = new ArrayList<>();
            }
            for (EdsInvoiceItem exp : invoice.getItemsAsExpense()) {
                beList.add(exp.createBillableExpenseItem(true));
            }
        }
        if (invoice.getMjItemsAsExpense() != null && !invoice.getMjItemsAsExpense().isEmpty()) {

            if (beList == null) {
                beList = new ArrayList<>();
            }
            for (EdsManualJournalItem exp : invoice.getMjItemsAsExpense()) {
                beList.add(exp.createBillableExpenseItem(true));
            }
        }
        if (invoice.getBtItemsAsExpense() != null && !invoice.getBtItemsAsExpense().isEmpty()) {
            if (beList == null) {
                beList = new ArrayList<>();
            }
            for (EdsBankTransferItem exp : invoice.getBtItemsAsExpense()) {
                beList.add(exp.createBillableExpenseItem(true));
            }
        }
        if (invoice.getBchItemsAsExpense() != null && !invoice.getBchItemsAsExpense().isEmpty()) {
            if (beList == null) {
                beList = new ArrayList<>();
            }
            for (EdsBankCheckItem exp : invoice.getBchItemsAsExpense()) {
                beList.add(exp.createBillableExpenseItem(true));
            }
        }

        result.setExpenses(beList);

        result.setFixedAssetRelated(invoice.isFixedAssetRelated());

        if (invoice.getCreditNoteInvoice() != null) {
            result.setRelatedInvoiceNumber(invoice.getCreditNoteInvoice().getNumber());
            result.setRelatedInvoiceDate(invoice.getCreditNoteInvoice().getInvoiceDate());
        }
        result.setCreationDate(invoice.getCreationDate());
        result.setCreditNote(invoice.isCreditNote());
        result.setNoteReason(invoice.getNoteReason());
        result.setPaymentTypeCode(invoice.getNotePaymentCode());
        return result;
    }

    public Integer getNotePaymentCode() {
        return notePaymentCode;
    }

    public void setNotePaymentCode(Integer notePaymentCode) {
        this.notePaymentCode = notePaymentCode;
    }

    public void setData(EdsInvoice invoice) {
        setClientContact(invoice.getClientContact());
        setBillAddressID(invoice.getBillAddressID());
        setMailAddressID(invoice.getMailAddressID());

        setCurrency(invoice.getCurrency());
        setExchangeRate(invoice.getExchangeRate());

        setRelatedProject(invoice.getRelatedProject());
        setPoNumber(invoice.getPoNumber());
        setTaxCalculationType(invoice.getTaxCalculationType());
        setZatcaHash(invoice.getZatcaHash());
        setZatcaQRCode(invoice.getZatcaQRCode());
        setZatcaStatus(invoice.getZatcaStatus());
        setZatcaXml(invoice.getZatcaXml());
        setNoteReason(invoice.getNoteReason());
        setNotePaymentCode(invoice.getNotePaymentCode());

        setReference(invoice.getReference());

        setIntroduction(invoice.getIntroduction());

        List<EdsInvoiceItem> newInvItems = new LinkedList<>();
        List<EdsInvoiceItem> invItems = invoice.getInvoiceItems();
        for (EdsInvoiceItem invItem : invItems) {
            EdsInvoiceItem newItem = new EdsInvoiceItem();
            newItem.setInvoice(this);
            newItem.setItem(invItem.getItem());
            newItem.setItemName(invItem.getItemName());
            newItem.setDescription(invItem.getDescription());
            newItem.setQty(invItem.getQty());
            newItem.setUnitPrice(invItem.getUnitPrice());
            newItem.setItemDiscount(invItem.getItemDiscount());
            newItem.setDiscount(invItem.getDiscount());
            newItem.setDiscountAmount(invItem.getDiscountAmount());
            newItem.setAccount(invItem.getAccount());
            newItem.setVat(invItem.getVat());
            newItem.setAmmount(invItem.getAmmount());
            newItem.setReceiveType(invItem.getReceiveType());
            newItem.setReceivedAmount(invItem.getReceivedAmount());
            newItem.setReceivedQty(invItem.getReceivedQty());
            newItem.setReceive(invItem.getReceive());
            newItem.setComission(invItem.getComission());
            newItem.setNet(invItem.getNet());
            newItem.setWarehouse(invItem.getWarehouse());
            newItem.setProject(invItem.getProject());
            newInvItems.add(newItem);
        }

        setInvoiceItems(newInvItems);
        setPaymentInstruction(invoice.getPaymentInstruction());
        setSubtotal(invoice.getSubtotal());
        setTotal(invoice.getTotal());
        setTotalInInvoiceCurrency(invoice.getTotalInInvoiceCurrency());
        setTotalTaxes(invoice.getTotalTaxes());
        setTotalDiscount(invoice.getTotalDiscount());

        List<EdsInvoiceTaxTotal> newTaxTotals = new LinkedList<>();
        List<EdsInvoiceTaxTotal> taxTotals = invoice.getInvoiceTaxTotals();
        for (EdsInvoiceTaxTotal taxTotal : taxTotals) {
            EdsInvoiceTaxTotal newTaxTotal = new EdsInvoiceTaxTotal();
            newTaxTotal.setInvoice(this);
            newTaxTotal.setVat(taxTotal.getVat());
            newTaxTotal.setAmount(taxTotal.getAmount());
            newTaxTotals.add(newTaxTotal);
        }
        setInvoiceTaxTotals(newTaxTotals);
        setStatus(invoice.getStatus());
        setCreator(invoice.getCreator());
        setCreationDate(new Date());
    }

    public static List<PaymentItem> getPaymentItemsList(EdsInvoice invoice) {
        List<EdsInvoicePayment> paymentsOrRefunds;

        if (invoice.isCreditNote()) {
            paymentsOrRefunds = invoice.getRefunds();
        } else {
            paymentsOrRefunds = invoice.getPayments();
        }

        List<PaymentItem> paymentItems = new ArrayList<>();
        for (EdsInvoicePayment p : paymentsOrRefunds) {
            if (!(p.getStatus() != null && EdsInvoicePayment.REVERSED.equals(p.getStatus().getCode()))) {
                paymentItems.add(p.getPaymentAsRPC());
            }
        }
        paymentItems.sort(Comparator.comparing(PaymentItem::getObjectId));
        return paymentItems;
    }

    public String getZatcaStatus() {
        return zatcaStatus;
    }

    public void setZatcaStatus(String zatcaStatus) {
        this.zatcaStatus = zatcaStatus;
    }

    public String getZatcaHash() {
        return zatcaHash;
    }

    public void setZatcaHash(String zatcaHash) {
        this.zatcaHash = zatcaHash;
    }

    public String getZatcaXml() {
        return zatcaXml;
    }

    public void setZatcaXml(String zatcaXml) {
        this.zatcaXml = zatcaXml;
    }

    public String getZatcaQRCode() {
        return zatcaQRCode;
    }

    public void setZatcaQRCode(String zatcaQRCode) {
        this.zatcaQRCode = zatcaQRCode;
    }

    public ItemsData initItems() {
        boolean isContainInvItem;
        ArrayList<NewInvoiceItem> items = new ArrayList<>();
        ReferenceManager referenceManager = StaticContextAccessor.getBean(ReferenceManager.class);
        InvoiceManager invoiceManager = StaticContextAccessor.getBean(InvoiceManager.class);
        Map<Integer, NewInvoiceItem> invoiceItemsMap = invoiceManager.getInvoiceItems(getObjectID(), false);
        isContainInvItem = invoiceItemsMap.entrySet().parallelStream().anyMatch(e -> e.getValue().getItemType() != null && (EdsItem.INVENTORY_ITEM.equals(e.getValue().getItemType()) || EdsItem.RENTAL_ITEM.equals(e.getValue().getItemType())
                || EdsItem.PRODUCT_KIT.equals(e.getValue().getItemType())));
        Map<Integer, SelectItem> faiCategoryMap;
        if (this instanceof EdsSaleInvoice) {
            faiCategoryMap = referenceManager.listReferences(EdsVat.FAI_CATEGORY).stream()
                    .map(c -> new SelectItem(c.getObjectID(), c.getName()))
                    .collect(Collectors.toMap(SelectItem::getId, Function.identity()));
        } else if (this instanceof EdsPurchaseInvoice) {
            faiCategoryMap = referenceManager.listReferences(EdsVat.FAI_PURCHASE_CATEGORY).stream()
                    .map(c -> new SelectItem(c.getObjectID(), c.getName()))
                    .collect(Collectors.toMap(SelectItem::getId, Function.identity()));
        } else {
            faiCategoryMap = new HashMap<>();
        }
        if (invoiceItemsMap != null && !invoiceItemsMap.isEmpty()) {
            for (EdsInvoiceItem item : invoiceItems) {
                NewInvoiceItem invItem = invoiceItemsMap.get(item.getObjectID());

                if (getItemCustomFields() != null && !getItemCustomFields().isEmpty() && item.getCustomFields() != null) {
                    invItem.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(item.getCustomFields(), generateCloneItemCustomFields()));
                }
                if (item.getVat() != null && invItem.getTaxItem() != null) {
                    TaxItem taxItem = invItem.getTaxItem();
                    if (item.getVat().getFaiCategorieIds() != null) {
                        SelectItem[] categories = item.getVat().getFaiCategorieIds().stream()
                                .map(faiCategoryMap::get)
                                .filter(Objects::nonNull)
                                .toArray(SelectItem[]::new);
                        taxItem.setFaiCategories(categories);
                    }
                    if (item.getVat().getFaiPurchaseCategoryIds() != null) {
                        SelectItem[] purchaseCategories = item.getVat().getFaiPurchaseCategoryIds().stream()
                                .map(faiCategoryMap::get)
                                .filter(Objects::nonNull)
                                .toArray(SelectItem[]::new);
                        taxItem.setFaiPurchaseCategories(purchaseCategories);
                    }
                    invItem.setTaxItem(taxItem);
                }
                invItem.setFaiCategory(faiCategoryMap.getOrDefault(item.getFaiCategoryId(), null));
                items.add(invItem);
            }
        }
        return new ItemsData(items, isContainInvItem);
    }

    public BigDecimal getPaidAmountByPeriod(Date startDate, Date endDate) {
        BigDecimal paymentsInPeriod = AccountingConstants.ZERO;
        if (getPayments() != null) {
            for (EdsInvoicePayment item : getPayments()) {
                if (!item.isReversed()
                        && (item.getPaymentDate().after(startDate) || item.getPaymentDate().getTime() == startDate.getTime())
                        && (item.getPaymentDate().before(endDate) || item.getPaymentDate().getTime() == endDate.getTime())) {
                    paymentsInPeriod = paymentsInPeriod.add(item.getAmountInInvoiceCurrency() != null ? item.getAmountInInvoiceCurrency() : item.getAmount());
                }
            }
        }
        return paymentsInPeriod;
    }

    public BigDecimal getPaymentShare(Date startDate, Date endDate) {
        return getPaidAmountByPeriod(startDate, endDate).divide(getTotalInInvoiceCurrency(), 10, BigDecimal.ROUND_HALF_UP);
    }

    public void setPayments(List<EdsInvoicePayment> payments) {
        this.payments = payments;
    }

    public List<EdsInvoicePayment> getPayments() {
        return payments;
    }

    public List<EdsInvoicePayment> getRefunds() {
        return refunds;
    }

    public void setRefunds(List<EdsInvoicePayment> refunds) {
        this.refunds = refunds;
    }

    public List<EdsInvoiceItem> getInvoiceItems() {
        return invoiceItems;
    }

    public void setInvoiceItems(List<EdsInvoiceItem> invoiceItems) {
        this.invoiceItems = invoiceItems;
    }

    public BigDecimal getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(BigDecimal totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public List<EdsInvoiceTaxTotal> getInvoiceTaxTotals() {
        return invoiceTaxTotals;
    }

    public void setInvoiceTaxTotals(List<EdsInvoiceTaxTotal> invoiceTaxTotals) {
        this.invoiceTaxTotals = invoiceTaxTotals;
    }

    public Integer getTaxCalculationType() {
        return taxCalculationType;
    }

    public void setTaxCalculationType(Integer taxCalculationType) {
        if (!ServerUtils.equalsInteger(this.taxCalculationType, taxCalculationType)) {
            addChange(CustomFormConstants.AMOUNT);
        }
        this.taxCalculationType = taxCalculationType;
    }

    public Set<EdsQuote> getConvertedQuotes() {
        return convertedQuotes;
    }

    public void setConvertedQuotes(Set<EdsQuote> convertedQuotes) {
        this.convertedQuotes = convertedQuotes;
    }

    public EdsReference getCustomType() {
        return customType;
    }

    public void setCustomType(EdsReference customiseType) {
        this.customType = customiseType;
    }

    public EdsInvoiceCustomFields getCustomFields() {
        return customFields;
    }

    public void setCustomFields(EdsInvoiceCustomFields customFields) {
        this.customFields = customFields;
    }

    public EdsInvoice getCreditNoteInvoice() {
        return creditNoteInvoice;
    }

    public void setCreditNoteInvoice(EdsInvoice creditNoteInvoice) {
        this.creditNoteInvoice = creditNoteInvoice;
    }

    public BigDecimal getBillExpTotal() {
        return billExpTotal;
    }

    public void setBillExpTotal(BigDecimal billExpTotal) {
        this.billExpTotal = billExpTotal;
    }

    public BigDecimal getBillExpTaxTotal() {
        return billExpTaxTotal;
    }

    public void setBillExpTaxTotal(BigDecimal billExpTaxTotal) {
        this.billExpTaxTotal = billExpTaxTotal;
    }

    public BigDecimal getMarkupAmount() {
        return markupAmount;
    }

    public void setMarkupAmount(BigDecimal markupAmount) {
        this.markupAmount = markupAmount;
    }

    public EdsAccount getMarkupAccount() {
        return markupAccount;
    }

    public void setMarkupAccount(EdsAccount markupAccount) {
        this.markupAccount = markupAccount;
    }

    public List<EdsExpense> getExpense() {
        return expense;
    }

    public void setExpense(List<EdsExpense> expense) {
        this.expense = expense;
    }

    public List<EdsInvoiceItem> getItemsAsExpense() {
        return itemsAsExpense;
    }

    public List<EdsManualJournalItem> getMjItemsAsExpense() {
        return mjItemsAsExpense;
    }

    public void setMjItemsAsExpense(List<EdsManualJournalItem> mjItemsAsExpense) {
        this.mjItemsAsExpense = mjItemsAsExpense;
    }

    public List<EdsBankTransferItem> getBtItemsAsExpense() {
        return btItemsAsExpense;
    }

    public void setBtItemsAsExpense(List<EdsBankTransferItem> btItemsAsExpense) {
        this.btItemsAsExpense = btItemsAsExpense;
    }

    public List<EdsBankCheckItem> getBchItemsAsExpense() {
        return bchItemsAsExpense;
    }

    public void setBchItemsAsExpense(List<EdsBankCheckItem> bchItemsAsExpense) {
        this.bchItemsAsExpense = bchItemsAsExpense;
    }

    public void setItemsAsExpense(List<EdsInvoiceItem> itemsAsExpense) {
        this.itemsAsExpense = itemsAsExpense;
    }

    public Boolean isPercent() {
        return isPercent != null ? isPercent : false;
    }

    public void setPercent(Boolean percent) {
        isPercent = percent;
    }

    public Boolean isCreditNote() {
        return isCreditNote != null ? isCreditNote : false;
    }

    public void setCreditNote(boolean creditNote) {
        isCreditNote = creditNote;
    }

    public EdsInvoice getHistoricalParent() {
        return historicalParent;
    }

    public void setHistoricalParent(EdsInvoice historicalParent) {
        this.historicalParent = historicalParent;
    }

    public Integer getPriceLevelID() {
        return priceLevelID;
    }

    public void setPriceLevelID(Integer priceLevelID) {
        this.priceLevelID = priceLevelID;
    }

    public Integer getClientDiscountID() {
        return clientDiscountID;
    }

    public void setClientDiscountID(Integer clientDiscountID) {
        this.clientDiscountID = clientDiscountID;
    }

    public Set<EdsShippingData> getConvertedShippingData() {
        return convertedShippingData;
    }

    public void setConvertedShippingData(Set<EdsShippingData> convertedShippingData) {
        this.convertedShippingData = convertedShippingData;
    }

    public Set<EdsProject> getProjects() {
        Set<EdsProject> projects = new HashSet<>();

        if (getInvoiceItems() != null && !getInvoiceItems().isEmpty()) {
            for (EdsInvoiceItem item : getInvoiceItems()) {
                if (item.getProject() != null) {
                    projects.add(item.getProject());
                }
            }
        }

        return projects;
    }

    //getting invoice products sub items by ASSEMBLY_ITEM & PRODUCT_KIT types for custom PDFs
    public static NewInvoice getInvoiceProductSubItemsByTypes(EdsInvoice invoice) {
        NewInvoice result = new NewInvoice();
        ArrayList<AssemblyItem> assemblyItemList = new ArrayList<>();
        ArrayList<ProductKitItem> productKitItemList = new ArrayList<>();
        for (EdsInvoiceItem invoiceItem : invoice.getInvoiceItems()) {
            if (invoiceItem.getItem() != null && invoiceItem.getItem().getType() != null) {
                if (EdsItem.ASSEMBLY_ITEM.equals(invoiceItem.getItem().getType())) {
                    for (EdsAssemblyItem assemblyItem : invoiceItem.getItem().getAssemblyItems()) {
                        AssemblyItem item = new AssemblyItem();
                        if (assemblyItem.getProductItem() != null) {
                            item.setProduct(assemblyItem.getProductItem().getAsProductSelectItem());
                            item.setItemsInStock(assemblyItem.getProductItem().getQty());
                            item.setActive(assemblyItem.getProductItem().isActive());
                        }
                        item.setAssemblyItemId(assemblyItem.getObjectID());
                        item.setDescription(assemblyItem.getDescription());
                        item.setQuantity(assemblyItem.getQty());
                        item.setCostPrice(assemblyItem.getCostPrice());
                        item.setProductType(assemblyItem.getType());

                        assemblyItemList.add(item);
                    }
                } else if (EdsItem.PRODUCT_KIT.equals(invoiceItem.getItem().getType())) {
                    for (EdsProductKitItems productKitItem : invoiceItem.getItem().getProductKitItems()) {
                        ProductKitItem item = new ProductKitItem();
                        item.setProductItem(invoiceItem.getItem().getAsProductSelectItem());
                        item.setQuantity(productKitItem.getQuantity());
                        BigDecimal sellingPrice = invoiceItem.getItem().getSellingPrice() != null ? invoiceItem.getItem().getSellingPrice().setScale(2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO;
                        BigDecimal costPrice = invoiceItem.getItem().getUnitPrice() != null ? invoiceItem.getItem().getUnitPrice().setScale(2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO;
                        item.setPrice(sellingPrice.toString());
                        item.setCost(costPrice.toString());
                        BigDecimal tax = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
                        if (invoiceItem.getItem().getVat() != null) {
                            tax = new BigDecimal(invoiceItem.getItem().getVat().getEffectiveTaxRate()).setScale(2, BigDecimal.ROUND_HALF_UP);
                        }
                        item.setTax(tax.toString());
                        BigDecimal net = sellingPrice.multiply(productKitItem.getQuantity()).setScale(2, BigDecimal.ROUND_HALF_UP);
                        item.setSubtotal(net.toString());

                        productKitItemList.add(item);
                    }
                }
            }
        }
        result.setAssemblyItems(assemblyItemList);
        result.setProductKitItems(productKitItemList);

        return result;
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

    public String getNoteReason() {
        return noteReason;
    }

    public void setNoteReason(String noteReason) {
        this.noteReason = noteReason;
    }

    public Date getReportedDate() {
        return reportedDate;
    }

    public void setReportedDate(Date reportedDate) {
        this.reportedDate = reportedDate;
    }

    public Integer getCalcScale() {
        return calcScale;
    }

    public void setCalcScale(Integer calcScale) {
        this.calcScale = calcScale;
    }
}
