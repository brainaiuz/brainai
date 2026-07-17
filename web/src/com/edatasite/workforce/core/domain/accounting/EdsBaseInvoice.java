package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.ObjectIdentifier;
import com.edatasite.workforce.core.domain.*;
import com.edatasite.workforce.core.domain.approving.EdsApprovable;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.EdsCustomCrmAccount;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.enums.EntityTypeEnum;
import com.edatasite.workforce.core.domain.pdf.EdsCompanyPdfTemplate;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.StaticContextAccessor;
import com.edatasite.workforce.gwt.core.server.db.CustomCrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoiceItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.TypeItem;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 07.04.2009
 * Time: 12:35:30
 * To change this template use File | Settings | File Templates.
 */

@MappedSuperclass
public abstract class EdsBaseInvoice extends EdsApprovable implements ObjectIdentifier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "objectKey", unique = true, updatable = false)
    private String objectKey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crmcontactid")
    private EdsCrmContact clientContact;

    @Type(type = "text")
    private String number;

    @Type(type = "text")
    private String poNumber;

    @Type(type = "text")
    private String quoteNumberCN;

    private Date invoiceDate;
    private Date dueDate;
    private Date paidDate;

    @Column(precision = 25, scale = 5)
    private BigDecimal discount;

    private String notes;

    @Column(precision = 25, scale = 5)
    private BigDecimal subtotal;//base currency

    @Column(precision = 25, scale = 5)
    private BigDecimal total;//base currency

    @Column(precision = 25, scale = 5)
    private BigDecimal totalInInvoiceCurrency;//inv currency

    @Column(precision = 25, scale = 5)
    private BigDecimal comissionAmount;

    private Boolean sent = false;

    @Column(precision = 25, scale = 15)
    private BigDecimal exchangeRate;

    private Boolean fixedAssetRelated;

    private Date creationDate;
    private Date updatedDate;

    /**
     * Invoice type: payable or receivable
     */
    private String type;

    /**
     * Amount type: tax exclusive and tax inclusive.
     */
    private String amountType;

    @Type(type = "text")
    private String clientMessage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private EdsReference status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id")
    @ForeignKey(name = "none")
    private EdsCurrency currency;

    @Type(type = "text")
    private String paymentInstruction;

    @Type(type = "text")
    private String introduction;

    @Column(precision = 25, scale = 5)
    private BigDecimal totalTaxes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "relatedproject_id")
    private EdsProject relatedProject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receivablePayable")
    private EdsAccount receivablePayable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private EdsUser creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updater_id")
    private EdsUser updater;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mailSender")
    private EdsUser mailSender; //Sender To Client,Supplier

    @Type(type = "text")
    private String reference;

    @Column(name = "deleted")
    private Boolean deleted = false;

    @Column(name = "saasu_guid")
    private String saasuGUID;
    @Column(name = "sasuuLastUpdatedTime")
    private Date sasuuLastUpdatedTime;
    @Column(name = "saasuLastUpdatedUid")
    private String saasuLastUpdatedUid;

    @Column(name = "quickbook_item_id")
    private String quickbookInvoiceID;

    @Column(name = "quickbook_edit_sequence")
    private String quickbookEditSequence;

    @Column(name = "external_guid")
    private String externalGUID;

    private Integer billAddressID;
    private Integer mailAddressID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pdfTemplate_id")
    private EdsCompanyPdfTemplate pdfTemplate;

    @Column(columnDefinition = "boolean default false")
    private Boolean isRegisteredInterCompanyTransaction = Boolean.FALSE;

    @Column(name = "discount_type")
    private Integer discountType;

    @Column(precision = 25, scale = 5)
    private BigDecimal discountAmount;

    @Column(name = "placeofsupply_id")
    private Integer placeOfSupplyId;

    @Column(name = "placeofsupply_category")
    private String placeOfSupplyCategory; //posible values in {REGION, COUNTRY}

    @Column(name = "vat_return_id")
    private Integer vatReturnId;

    @Column(name = "reversecharge_applicable", columnDefinition = "boolean default false")
    private boolean reverseChargeApplicable;

    @Column(name = "short_link")
    private String shortLink;

    //{invoice/quote} item custom field template
    @Transient
    List<CompanyCustomFieldItem> itemCustomFields;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        if (!ServerUtils.equalsDate(this.invoiceDate, invoiceDate)) {
            addChange(CustomFormConstants.ACCOUNTING.INVOICE_DATE);
        }
        this.invoiceDate = invoiceDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        if (!ServerUtils.equalsDate(this.dueDate, dueDate)) {
            addChange(CustomFormConstants.ACCOUNTING.DUE_DATE);
        }
        this.dueDate = dueDate;
    }

    public Date getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(Date paidDate) {
        this.paidDate = paidDate;
    }

    public EdsReference getStatus() {
        return status;
    }

    public void setStatus(EdsReference status) {
        if (!ServerUtils.equalsReference(this.status, status)) {
            addChange(CustomFormConstants.ACCOUNTING.STATUS);
        }
        this.status = status;
    }

    public EdsCrmContact getClientContact() {
        return clientContact;
    }

    public void setClientContact(EdsCrmContact clientContact) {
        this.clientContact = clientContact;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        if (!ServerUtils.equalsString(this.poNumber, poNumber)) {
            addChange(CustomFormConstants.ACCOUNTING.PO_NUMBER);
        }
        this.poNumber = poNumber;
    }

    public String getQuoteNumberCN() {
        return quoteNumberCN;
    }

    public void setQuoteNumberCN(String quoteNumberCN) {
        this.quoteNumberCN = quoteNumberCN;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public EdsCompany getCompany() {
        return getCreator().getCompany();
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        if (!ServerUtils.equalsBigDecimal(this.subtotal, subtotal)) {
            addChange(CustomFormConstants.ACCOUNTING.SUB_TOTAL);
        }
        this.subtotal = subtotal;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        if (!ServerUtils.equalsBigDecimal(this.total, total)) {
            addChange(CustomFormConstants.ACCOUNTING.TOTAL);
        }
        this.total = total;
    }

    public BigDecimal getTotalInInvoiceCurrency() {
        return totalInInvoiceCurrency != null ? totalInInvoiceCurrency : total.multiply(exchangeRate);
    }

    public void setTotalInInvoiceCurrency(BigDecimal totalInInvoiceCurrency) {
        if (!ServerUtils.equalsBigDecimal(this.totalInInvoiceCurrency, totalInInvoiceCurrency)) {
            addChange(CustomFormConstants.ACCOUNTING.TOTAL_INVOICE_CURRENCY);
        }
        this.totalInInvoiceCurrency = totalInInvoiceCurrency;
    }

    public BigDecimal getComissionAmount() {
        return comissionAmount != null ? comissionAmount : BigDecimal.ZERO;
    }

    public void setComissionAmount(BigDecimal comissionAmount) {
        this.comissionAmount = comissionAmount;
    }

    public EdsCurrency getCurrency() {
        return currency;
    }

    public void setCurrency(EdsCurrency currency) {
        this.currency = currency;
    }

    public void setClientMessage(String clientMessage) {
        this.clientMessage = clientMessage;
    }

    public String getClientMessage() {
        return clientMessage;
    }

    public void setSent(Boolean sent) {
        this.sent = sent;
    }

    public Boolean getSent() {
        return sent;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getTotalTaxes() {
        return totalTaxes;
    }

    public void setTotalTaxes(BigDecimal totalTaxes) {
        this.totalTaxes = totalTaxes;
    }

    public String getAmountType() {
        return amountType;
    }

    public void setAmountType(String amountType) {
        this.amountType = amountType;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public Boolean isFixedAssetRelated() {
        return fixedAssetRelated != null ? fixedAssetRelated : false;
    }

    public void setFixedAssetRelated(Boolean fixedAssetRelated) {
        this.fixedAssetRelated = fixedAssetRelated;
    }

    public String getPaymentInstruction() {
        return paymentInstruction;
    }

    public void setPaymentInstruction(String paymentInstruction) {
        if (!ServerUtils.equalsString(this.paymentInstruction, paymentInstruction)) {
            addChange(CustomFormConstants.ACCOUNTING.INSTRUCTIONS);
        }
        this.paymentInstruction = paymentInstruction;
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

    public abstract EdsCrmAccount getClientOrSupplier();

    public abstract ItemsData initItems();

    protected static NewInvoice getData(EdsBaseInvoice baseInvoice) {
        GenericSettingsManager genericSettingsManager = StaticContextAccessor.getBean(GenericSettingsManager.class);
        NewInvoice result = new NewInvoice();
        result.setID(baseInvoice.getObjectID());
        EdsCrmAccount clientSupp = baseInvoice.getClientOrSupplier();
        result.setClientID(clientSupp.getObjectID());
        result.setClientName(clientSupp.getName());
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_CUSTOM_CRM_ACCOUNT)) {
            CustomCrmAccountManager customCrmAccountManager = StaticContextAccessor.getBean(CustomCrmAccountManager.class);
            EdsCustomCrmAccount edsCustomCrmAccount = customCrmAccountManager.getCustomCrmAccountByEntityTypeAndEntityId(baseInvoice.getObjectID(), EntityTypeEnum.SALE_INVOICE.name());
            if (edsCustomCrmAccount != null) {
                result.setCustomCrmAccountId(edsCustomCrmAccount.getObjectID());
                result.setCustomCrmAccountName(edsCustomCrmAccount.getClientName());
            }
        }
        result.setClientNumber(clientSupp.getNumber());
        SelectItem selectItem = new SelectItem();
        selectItem.setId(baseInvoice.getCreator().getObjectID());
        selectItem.setName(baseInvoice.getCreator().getName());
        result.setCreator(selectItem);
        result.setCreatorName(selectItem.getName());

        Integer currencyID = clientSupp.getCurrency() != null ? clientSupp.getCurrency().getObjectID() : null;

        TypeItem typeItem = new TypeItem(clientSupp.getObjectID(), clientSupp.getName(), clientSupp.getNumber(), currencyID);
        typeItem.setSubsidiary(clientSupp.getSubsidiary() != null);
        typeItem.setPlaceOfSupply(clientSupp.getPlaceOfSupply());

        if (clientSupp.getTaxTreatment() != null) {
            SelectItem treatment = clientSupp.getTaxTreatment().getAsSelectItem();
            treatment.setCode(clientSupp.getTaxTreatment().getCode());
            typeItem.setTaxTreatment(treatment);
        }
        if (clientSupp.getTerms() != null) {
            typeItem.setTermsItem(clientSupp.getTerms().getAsRPC());
        }
        if (clientSupp.getPaymentMethod() != null) {
            typeItem.setPaymentType(clientSupp.getPaymentMethod().getName());
        }
        typeItem.setBillAddressID(baseInvoice.getBillAddressID());
        typeItem.setMailAddressID(baseInvoice.getMailAddressID());
        result.setTypeItem(typeItem);
        result.setBillAddressID(baseInvoice.getBillAddressID());
        result.setMailAddressID(baseInvoice.getMailAddressID());
        if (baseInvoice.getReceivablePayable() != null) {
            result.setAccountsReceivablePayable(baseInvoice.getReceivablePayable().createAccountItem());
            typeItem.setAccountsReceivablePayable(baseInvoice.getReceivablePayable().createAccountItem());
        }
        if (baseInvoice.getClientContact() != null) {
            result.setClientContactID(baseInvoice.getClientContact().getObjectID());
            result.setClientContactEmail(baseInvoice.getClientContact().getPrimaryEmail());
        } else if (baseInvoice instanceof EdsSaleInvoice && ((EdsSaleInvoice) baseInvoice).getClient() != null && ((EdsSaleInvoice) baseInvoice).getClient().getPrimaryContact() != null) {
            result.setClientContactID(((EdsSaleInvoice) baseInvoice).getClient().getPrimaryContact().getObjectID());
            result.setClientContactEmail(((EdsSaleInvoice) baseInvoice).getClient().getPrimaryContact().getPrimaryEmail());
        }
        if (baseInvoice.getCurrency() != null) {
            result.setCurrencyID(baseInvoice.getCurrency().getObjectID());
            result.setCurrencyName(baseInvoice.getCurrency().getName());
            result.setCurrencySymbol(baseInvoice.getCurrency().getSymbol());
        }
        result.setPoNumber(baseInvoice.getPoNumber());
        result.setQuoteNumberCN(baseInvoice.getQuoteNumberCN());
        result.setInvoiceNumber(baseInvoice.getNumber());
        result.setInvoiceDate(new DateNonConvertable(baseInvoice.getInvoiceDate()));
        result.setDueDate(new DateNonConvertable(baseInvoice.getDueDate()));
        result.setReference(baseInvoice.getReference());
        EdsProject project = baseInvoice.getRelatedProject();
        if (project != null) {
            result.setRelatedProject(new SelectItem(project.getObjectID(),
                    (project.getNumber() != null && !"".equals(project.getNumber().trim()) ? project.getNumber() + " -> " : "") + project.getName(), project.getNumber()));
            result.setProjectStatusCode(project.getStatus().getCode());
        }
        if (baseInvoice instanceof EdsSaleInvoice) {
            result.setProjectBasedInvoice(((EdsSaleInvoice) baseInvoice).isProjectBasedInvoice());
        }
        if (baseInvoice instanceof EdsInvoice) {
            result.setZatcaStatus(((EdsInvoice) baseInvoice).getZatcaStatus());
        }
        result.setIntroduction(baseInvoice.getIntroduction());

        ItemsData itemsData = baseInvoice.initItems();
        result.setItems(itemsData.getItems().toArray(new NewInvoiceItem[]{}));
        result.setInventoryItemIncluded(itemsData.isInventoryItemExists());
        result.setSubtotal(baseInvoice.getSubtotal() != null ? baseInvoice.getSubtotal() : AccountingConstants.ZERO);
        result.setTotalTaxes(baseInvoice.getTotalTaxes());
        result.setTotal(baseInvoice.getTotal() != null ? baseInvoice.getTotal() : AccountingConstants.ZERO);
        result.setAmount(baseInvoice.getTotalInInvoiceCurrency() != null ? baseInvoice.getTotalInInvoiceCurrency() : AccountingConstants.ZERO);
        result.setTotalInInvoiceCurrency(baseInvoice.getTotalInInvoiceCurrency());
        result.setComissionAmount(baseInvoice.getComissionAmount() != null ? baseInvoice.getComissionAmount() : AccountingConstants.ZERO);
        result.setClientMessage(baseInvoice.getClientMessage());
        result.setStatus(baseInvoice.getStatus() != null ? baseInvoice.getStatus().getName() : "");
        result.setStatusCode(baseInvoice.getStatus() != null ? baseInvoice.getStatus().getCode() : "");
        result.setType(baseInvoice.getType());
        result.setPaymentInstruction(baseInvoice.getPaymentInstruction());
        result.setExchageRate(baseInvoice.getExchangeRate());
        result.setDeleted(baseInvoice.isDeleted());
        result.setLastUpdateDate(baseInvoice.getUpdatedDate());
        result.setLastUpdater(baseInvoice.getUpdater() != null ? baseInvoice.getUpdater().getName() : "");
        return result;
    }

    public NewInvoiceItem getItem(EdsBaseInvoiceItem item) {
        NewInvoiceItem invoiceItem = new NewInvoiceItem();
        invoiceItem.setID(item.getObjectID());
        if (item.getItem() != null) {
            item.getItem().setInvoiceItemData(invoiceItem);
            invoiceItem.setItemDiscountList(EdsDiscount.getItemDiscounts(item.getItem().getDiscounts()));
            invoiceItem.setItemType(item.getItem().getType());
            if (item.getItem().getBarCode() != null) {
                invoiceItem.setItemBarcode(item.getItem().getBarCode());
            }
            invoiceItem.setItemsInStockQty(item.getItem().getItemsInStock());
            invoiceItem.setUnitCost(item.getItem().getUnitPrice());
        } else if (item.getItemName() != null) {
            invoiceItem.setItemName(item.getItemName());
            invoiceItem.setFullItemName(item.getItemName());
        } else if ((item instanceof EdsInvoiceItem) && ((EdsInvoiceItem) item).getProjectBasedInvoiceDescription() != null) {
            invoiceItem.setItemName(((EdsInvoiceItem) item).getProjectBasedInvoiceDescription());
            invoiceItem.setMeasurement(new SelectItem(null, "hours"));
        }
        if ((item instanceof EdsInvoiceItem)) {
            invoiceItem.setExpanceItemId(((EdsInvoiceItem) item).getExpenceItemId());
        }
        if (item.getUnitMeasurement() != null) {
            invoiceItem.setMeasurement(new SelectItem(item.getUnitMeasurement().getObjectID(), item.getUnitMeasurement().getName(), item.getUnitMeasurement().getDescription()));
        }
        invoiceItem.setShortLink(item.getShortLink());
        invoiceItem.setDescription(item.getDescription());
        invoiceItem.setQuantity(item.getQty());
        invoiceItem.setUuid(item.getUuid());
        invoiceItem.setLumpsum(item.isLumpsum());
        invoiceItem.setUnitPrice(item.getUnitPrice());
        invoiceItem.setPriceLevelAmount(item.getPriceLevelAmount());
        invoiceItem.setComission(item.getComission());
        invoiceItem.setDiscountPercent(item.getDiscount());
        invoiceItem.setDiscountAmount(item.getDiscountAmount());
        if (item.getItem() != null) {
            invoiceItem.setCurrentProductDiscountAmount(item.getItem().getDiscountAmount());
        }
        if (item.getItemDiscount() != null) {
            invoiceItem.setItemDiscountID(item.getItemDiscount().getObjectID());
            invoiceItem.setItemDiscount(item.getItemDiscount().getName());
        }
        invoiceItem.setDiscountItemStaticType(item.getDiscountItemStaticType());
        invoiceItem.setDoubleDiscountPercent(item.getDoubleDiscount());
        invoiceItem.setDoubleDiscountAmount(item.getDoubleDiscountAmount());
        if (item.getItemDoubleDiscount() != null) {
            invoiceItem.setItemDoubleDiscountID(item.getItemDoubleDiscount().getObjectID());
            invoiceItem.setItemDoubleDiscount(item.getItemDoubleDiscount().getName());
        }

        invoiceItem.setReceivedAllocation(item.getReceivedAllocation());
        invoiceItem.setNet(item.getNet());

        if (item.getDepartment() != null) {
            invoiceItem.setDepartmentItem(item.getDepartment().getAsSelectItem());
        }
        if (item.getAccount() != null) {
            EdsAccount account = item.getAccount();
            invoiceItem.setAccountID(account.getObjectID());
            invoiceItem.setAccountName(account.getName());
            AccountItem accountItem = account.createAccountItem();
            accountItem.setParentCode(account.getParent() != null ? account.getParent().getAccountCode() : null);
            invoiceItem.setAccountItem(accountItem);
        }

        if (item.getItem() != null && item.getItem().getAccount() != null) {
            invoiceItem.setSalesAccount(item.getItem().getAccount().createAccountItem());
        }

        if (item.getVat() != null) {
            invoiceItem.setTaxItem(item.getVat().createTaxItem());

            Integer calculationType = item.getTaxCalculationType();
            if (item instanceof EdsQuoteItem) {
                if (((EdsQuoteItem) item).getQuote().getTaxCalculationType() != null) {
                    calculationType = ((EdsQuoteItem) item).getQuote().getTaxCalculationType();
                }
            }

            invoiceItem.setTaxAmount(item.getItemCalculatedTaxAmount(false, calculationType));
        }
        if (item.getDoubleVat() != null) {
            invoiceItem.setDoubleTaxItem(item.getDoubleVat().createTaxItem());
            invoiceItem.setDoubleTaxAmount(item.getItemCalculatedTaxAmount(true, null));
        }
        invoiceItem.setTotalAmount(item.getAmmount());
        invoiceItem.setReceiveType(item.getReceiveType());
        invoiceItem.setReceivedAmount(item.getReceivedAmount());
        invoiceItem.setReceivedQty(item.getReceivedQty());

        if (item.getWarehouse() != null) {
            invoiceItem.setWarehouse(item.getWarehouse().getAsSelectItem());
        }

        if (item.getProject() != null) {
            invoiceItem.setProject(new SelectItem(item.getProject().getObjectID(), item.getProject().getNumber() + " -> " + item.getProject().getName()));
            if (item.getProject().getParent() != null) {
                EdsProject edsParentProj = item.getProject().getParent();
                invoiceItem.setParentProject(new SelectItem(edsParentProj.getObjectID(), edsParentProj.getNumber() + " -> " + edsParentProj.getName()));
            }
        }

        if (item instanceof EdsInvoiceItem saleItem) {
            invoiceItem.setQuoteItemId(saleItem.getQuoteItemId());
            invoiceItem.setFromTimesheet(saleItem.isFromTimesheet());
            final EdsSaleInvoice saleInvoice = saleItem.getSaleInvoice();

            if (saleInvoice != null && !saleInvoice.isDeleted()) {
                invoiceItem.setSaleInvoiceId(saleInvoice.getObjectID());
            }
            if (saleItem.getClient() != null) {
                invoiceItem.setClient(((EdsInvoiceItem) item).getClient().getAsSelectItem());
            }
            invoiceItem.setFaiCategoryId(saleItem.getFaiCategoryId());
        }
        invoiceItem.setConvertedQty(item.getConvertedQty());
        invoiceItem.setConvertedAmount(item.getConvertedAmount());
        invoiceItem.setReceive(item.getReceive());

        return invoiceItem;
    }

    public BigDecimal getFullPayments() {
        int calcScale = ServerUtils.getSystemCalculationScale();
        EdsInvoice invoice = (EdsInvoice) this;
        List<EdsInvoicePayment> paymentsOrRefunds = (invoice.isCreditNote() ? invoice.getRefunds() : invoice.getPayments());
        BigDecimal fullPayment = AccountingConstants.ZERO;
        for (EdsInvoicePayment payment : paymentsOrRefunds) {
            if (payment.getCalcScale() != null && calcScale < payment.getCalcScale()) {
                calcScale = payment.getCalcScale();
            }
            if (!(payment.getStatus() != null && EdsInvoicePayment.REVERSED.equals(payment.getStatus().getCode())) && !payment.isDeleted()) {
                Integer currencyId = payment.getCreditNote() != null && payment.getCreditNote().getCurrency() != null ? payment.getCreditNote().getCurrency().getObjectID() : payment.getCurrencyID();
                if (invoice.getCurrency().getObjectID().equals(currencyId)) {
                    fullPayment = fullPayment.add(payment.getAmountInInvoiceCurrency() != null ? payment.getAmountInInvoiceCurrency() : payment.getAmount()).setScale(calcScale, RoundingMode.HALF_UP);
                    ;
                } else {
                    fullPayment = fullPayment.add((payment.getAmountInInvoiceCurrency() != null ? payment.getAmountInInvoiceCurrency() : payment.getAmount())
                            .divide(payment.getExchangeRate(), calcScale, RoundingMode.HALF_UP));
                }
            }
        }
        return fullPayment;
    }

    public BigDecimal getFullPaymentsInBase() {
        EdsInvoice invoice = (EdsInvoice) this;
        List<EdsInvoicePayment> paymentsOrRefunds = (invoice.isCreditNote() ? invoice.getRefunds() : invoice.getPayments());
        BigDecimal fullPayment = AccountingConstants.ZERO;
        for (EdsInvoicePayment payment : paymentsOrRefunds) {
            if (!(payment.getStatus() != null && EdsInvoicePayment.REVERSED.equals(payment.getStatus().getCode())) && !payment.isDeleted()) {
                fullPayment = fullPayment.add((payment.getAmountInInvoiceCurrency() != null ? payment.getAmountInInvoiceCurrency() : payment.getAmount())
                        .divide(payment.getExchangeRate(), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP));
            }
        }
        return fullPayment;
    }


    public BigDecimal getDueAmount() {
        return getTotalInInvoiceCurrency().subtract(getFullPayments());
    }

    public EdsProject getRelatedProject() {
        return relatedProject;
    }

    public void setRelatedProject(EdsProject relatedProject) {
        if (!ServerUtils.equalsEdsObject(this.relatedProject, relatedProject)) {
            addChange(CustomFormConstants.ACCOUNTING.PROJECT);
        }
        this.relatedProject = relatedProject;
    }

    public EdsAccount getReceivablePayable() {
        return receivablePayable;
    }

    public void setReceivablePayable(EdsAccount receivablePayable) {
        this.receivablePayable = receivablePayable;
    }

    public EdsUser getCreator() {
        return creator;
    }

    public void setCreator(EdsUser creator) {
        this.creator = creator;
    }

    public EdsUser getUpdater() {
        return updater;
    }

    public void setUpdater(EdsUser updater) {
        this.updater = updater;
    }

    public EdsUser getMailSender() {
        return mailSender;
    }

    public void setMailSender(EdsUser mailSender) {
        this.mailSender = mailSender;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        if (!ServerUtils.equalsString(this.reference, reference)) {
            addChange(CustomFormConstants.ACCOUNTING.REFERENCE);
        }
        this.reference = reference;
    }

    @Override
    public String getObjectKey() {
        return objectKey;
    }

    @Override
    public void setObjectKey(String objectKey) {
        this.objectKey = objectKey;
    }

    public static class ItemsData {
        private final ArrayList<NewInvoiceItem> items;
        private final boolean isInventoryItemExists;

        public ItemsData(ArrayList<NewInvoiceItem> items, boolean inventoryItemExists) {
            this.items = items;
            isInventoryItemExists = inventoryItemExists;
        }

        public ArrayList<NewInvoiceItem> getItems() {
            return items;
        }

        public boolean isInventoryItemExists() {
            return isInventoryItemExists;
        }
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public Boolean isDeleted() {
        return deleted == null ? false : deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getSaasuGUID() {
        return saasuGUID;
    }

    public void setSaasuGUID(String saasuGUID) {
        this.saasuGUID = saasuGUID;
    }

    public Date getSasuuLastUpdatedTime() {
        return sasuuLastUpdatedTime;
    }

    public void setSasuuLastUpdatedTime(Date sasuuLastUpdatedTime) {
        this.sasuuLastUpdatedTime = sasuuLastUpdatedTime;
    }

    public String getSaasuLastUpdatedUid() {
        return saasuLastUpdatedUid;
    }

    public void setSaasuLastUpdatedUid(String saasuLastUpdatedUid) {
        this.saasuLastUpdatedUid = saasuLastUpdatedUid;
    }

    public String getQuickbookInvoiceID() {
        return quickbookInvoiceID;
    }

    public void setQuickbookInvoiceID(String quickbookInvoiceID) {
        this.quickbookInvoiceID = quickbookInvoiceID;
    }

    public String getQuickbookEditSequence() {
        return quickbookEditSequence;
    }

    public void setQuickbookEditSequence(String quickbookEditSequence) {
        this.quickbookEditSequence = quickbookEditSequence;
    }

    public String getExternalGUID() {
        return externalGUID;
    }

    public void setExternalGUID(String externalGUID) {
        this.externalGUID = externalGUID;
    }

    public EdsCompanyPdfTemplate getPdfTemplate() {
        return pdfTemplate;
    }

    public void setPdfTemplate(EdsCompanyPdfTemplate pdfTemplate) {
        this.pdfTemplate = pdfTemplate;
    }

    public Boolean isRegisteredInterCompanyTransaction() {
        return isRegisteredInterCompanyTransaction != null ? isRegisteredInterCompanyTransaction : (getObjectID() != null ? Boolean.TRUE : Boolean.FALSE);
    }

    public void setRegisteredInterCompanyTransaction(Boolean registeredInterCompanyTransaction) {
        this.isRegisteredInterCompanyTransaction = registeredInterCompanyTransaction;
    }

    public Integer getDiscountType() {
        return discountType;
    }

    public void setDiscountType(Integer discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public List<CompanyCustomFieldItem> getItemCustomFields() {
        return itemCustomFields;
    }

    public void setItemCustomFields(List<CompanyCustomFieldItem> itemCustomFields) {
        this.itemCustomFields = itemCustomFields;
    }

    public Integer getVatReturnId() {
        return vatReturnId;
    }

    public void setVatReturnId(Integer vatReturnId) {
        this.vatReturnId = vatReturnId;
    }

    public Integer getPlaceOfSupplyId() {
        return placeOfSupplyId;
    }

    public void setPlaceOfSupplyId(Integer placeOfSupplyId) {
        this.placeOfSupplyId = placeOfSupplyId;
    }

    public String getPlaceOfSupplyCategory() {
        return placeOfSupplyCategory;
    }

    public void setPlaceOfSupplyCategory(String placeOfSupplyCategory) {
        this.placeOfSupplyCategory = placeOfSupplyCategory;
    }


    public boolean isReverseChargeApplicable() {
        return reverseChargeApplicable;
    }

    public void setReverseChargeApplicable(boolean reverseChargeApplicable) {
        this.reverseChargeApplicable = reverseChargeApplicable;
    }

    public String getShortLink() {
        return shortLink;
    }

    public void setShortLink(String shortLink) {
        this.shortLink = shortLink;
    }
}
