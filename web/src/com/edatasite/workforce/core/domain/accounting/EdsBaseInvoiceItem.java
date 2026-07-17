package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsVat;
import com.edatasite.workforce.core.domain.customfields.EdsInvoiceItemCustomFields;
import com.edatasite.workforce.gwt.accounting.client.rpc.enums.ReceiveTypeEnum;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.invoice.client.rpc.ProductSerialItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.ProductTrackBatchItem;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 21.04.2009
 * Time: 20:15:29
 * To change this template use File | Settings | File Templates.
 */
@MappedSuperclass
public class EdsBaseInvoiceItem extends EdsObject implements IApplyingStockItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private String uuid = String.valueOf(Math.abs(UUID.randomUUID().hashCode()) % 1000000);

    @Column(name = "description")
    @Type(type = "text")
    private String description;

    @Column(name = "unitPrice", precision = 25, scale = 5)
    private BigDecimal unitPrice;

    @Column(precision = 25, scale = 5)
    private BigDecimal priceLevelAmount;

    private Boolean isLumpsum;

    @Column(precision = 25, scale = 5)
    private BigDecimal qty;

    @Column(precision = 25, scale = 5)
    private BigDecimal net;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private EdsItem item;

    @Column(name = "itemName")
    @Type(type = "text")
    private String itemName;

    @Type(type = "text")
    private String categoryName;

    private Boolean deleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    private EdsVat vat;

    @ManyToOne(fetch = FetchType.LAZY)
    private EdsVat doubleVat;

    private Integer sorder;

    @Transient
    private BigDecimal joinedVatValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private EdsAccount account;

    @Column(precision = 25, scale = 5)
    private BigDecimal ammount;

    @Enumerated(value = EnumType.STRING)
    private ReceiveTypeEnum receiveType;

    @Column(precision = 25, scale = 5)
    private BigDecimal receivedAmount;

    @Column(precision = 25, scale = 5)
    private BigDecimal receivedQty;

    @Column(precision = 25, scale = 5)
    private BigDecimal receive; //this is for the partial receive PO

    @Transient
    private BigDecimal allocatedExpense; //Allocated Amount From Expenses

    @Column(precision = 25, scale = 5)
    private BigDecimal receivedAllocation; //Allocated Amount share for received items

    @Column(name = "picking")
    private String picking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departmentid")
    private EdsDepartment department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouseid")
    private EdsWarehouse warehouse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private EdsProject project;

    @Column(name = "discount", precision = 25, scale = 5)
    private BigDecimal discount;

    @Column(name = "double_discount", precision = 25, scale = 5)
    private BigDecimal doubleDiscount;

    @Column(name = "comission", precision = 25, scale = 5)
    private BigDecimal comission;

    @Column(name = "discount_amount", precision = 25, scale = 5)
    private BigDecimal discountAmount;

    @Column(name = "double_discount_amount", precision = 25, scale = 5)
    private BigDecimal doubleDiscountAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_discount_id")
    private EdsDiscount itemDiscount;

    private Integer discountItemStaticType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_double_discount_id")
    private EdsDiscount itemDoubleDiscount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unitmeasurementid")
    private EdsUnitMeasurement unitMeasurement;

    @Transient
    private ProductSerialItem[] assignedSerials;

    @Column(name = "quickbook_check_itemId")
    private String quickbookItemID;

    @Column(name = "convertedQty")
    private BigDecimal convertedQty;

    @Column(name = "convertedAmount", precision = 25, scale = 5)
    private BigDecimal convertedAmount;

    @Column(name = "taxAmount", precision = 25, scale = 5)
    private BigDecimal taxAmount;
    @Column(name = "doubleTaxAmount", precision = 25, scale = 5)
    private BigDecimal doubleTaxAmount;

    @Column(name = "shortLink")
    private String shortLink;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private EdsInvoiceItemCustomFields customFields;

    @Transient
    private BigDecimal transactionValue;

    @Transient
    private List<String> serials;

    @Transient
    private List<ProductTrackBatchItem> batchItems;

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Boolean isLumpsum() {
        return isLumpsum != null ? isLumpsum : false;
    }

    public void setLumpsum(Boolean lumpsum) {
        isLumpsum = lumpsum;
    }

    public BigDecimal getQty() {
        return qty != null ? qty : BigDecimal.ZERO;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public EdsVat getVat() {
        return vat;
    }

    public void setVat(EdsVat vat) {
        this.vat = vat;
    }

    public Integer getSorder() {
        return sorder;
    }

    public void setSorder(Integer sorder) {
        this.sorder = sorder;
    }

    public BigDecimal getNet() {
        return net;
    }

    public void setNet(BigDecimal net) {
        this.net = net;
    }

    public BigDecimal getJoinedVatValue() {
        return joinedVatValue;
    }

    public void setJoinedVatValue(BigDecimal joinedVatValue) {
        this.joinedVatValue = joinedVatValue;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Boolean getDeleted() {
        return deleted != null ? deleted : false;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setItem(EdsItem item) {
        this.item = item;
    }

    public EdsItem getItem() {
        return item;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public EdsAccount getAccount() {
        return account;
    }

    public void setAccount(EdsAccount account) {
        this.account = account;
    }

    public BigDecimal getAmmount() {
        return ammount;
    }

    public void setAmmount(BigDecimal ammount) {
        this.ammount = ammount;
    }

    public ReceiveTypeEnum getReceiveType() {
        return receiveType;
    }

    public void setReceiveType(ReceiveTypeEnum receiveType) {
        this.receiveType = receiveType;
    }

    public BigDecimal getReceivedAmount() {
        return receivedAmount != null ? receivedAmount : BigDecimal.ZERO;
    }

    public void setReceivedAmount(BigDecimal receivedAmount) {
        this.receivedAmount = receivedAmount;
    }

    public BigDecimal getReceivedQty() {
        return receivedQty != null ? receivedQty : BigDecimal.ZERO;
    }

    public void setReceivedQty(BigDecimal receivedQty) {
        this.receivedQty = receivedQty;
    }

    public BigDecimal getReceive() {
        return receive != null ? receive : BigDecimal.ZERO;
    }

    public void setReceive(BigDecimal receive) {
        this.receive = receive;
    }

    public BigDecimal getAllocatedExpense() {
        return allocatedExpense;
    }

    public void setAllocatedExpense(BigDecimal allocatedExpense) {
        this.allocatedExpense = allocatedExpense;
    }

    public BigDecimal getReceivedAllocation() {
        return receivedAllocation != null ? receivedAllocation : BigDecimal.ZERO;
    }

    public void setReceivedAllocation(BigDecimal receivedAllocation) {
        this.receivedAllocation = receivedAllocation;
    }

    public String getPicking() {
        return picking;
    }

    public void setPicking(String picking) {
        this.picking = picking;
    }

    public EdsDepartment getDepartment() {
        return department;
    }

    public void setDepartment(EdsDepartment department) {
        this.department = department;
    }

    public EdsWarehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(EdsWarehouse warehouse) {
        this.warehouse = warehouse;
    }

    public EdsProject getProject() {
        return project;
    }

    public void setProject(EdsProject project) {
        this.project = project;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public EdsDiscount getItemDiscount() {
        return itemDiscount;
    }

    public void setItemDiscount(EdsDiscount itemDiscount) {
        this.itemDiscount = itemDiscount;
    }

    public Integer getDiscountItemStaticType() {
        return discountItemStaticType;
    }

    public void setDiscountItemStaticType(Integer discountItemStaticType) {
        this.discountItemStaticType = discountItemStaticType;
    }

    public BigDecimal getDoubleDiscount() {
        return doubleDiscount;
    }

    public void setDoubleDiscount(BigDecimal doubleDiscount) {
        this.doubleDiscount = doubleDiscount;
    }

    public BigDecimal getDoubleDiscountAmount() {
        return doubleDiscountAmount;
    }

    public void setDoubleDiscountAmount(BigDecimal doubleDiscountAmount) {
        this.doubleDiscountAmount = doubleDiscountAmount;
    }

    public EdsDiscount getItemDoubleDiscount() {
        return itemDoubleDiscount;
    }

    public void setItemDoubleDiscount(EdsDiscount itemDoubleDiscount) {
        this.itemDoubleDiscount = itemDoubleDiscount;
    }

    public BigDecimal getComission() {
        return comission;
    }

    public void setComission(BigDecimal comission) {
        this.comission = comission;
    }

    public EdsUnitMeasurement getUnitMeasurement() {
        return unitMeasurement;
    }

    public void setUnitMeasurement(EdsUnitMeasurement unitMeasurement) {
        this.unitMeasurement = unitMeasurement;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public BigDecimal getPriceLevelAmount() {
        return priceLevelAmount;
    }

    public void setPriceLevelAmount(BigDecimal priceLevelAmount) {
        this.priceLevelAmount = priceLevelAmount;
    }

    public EdsVat getDoubleVat() {
        return doubleVat;
    }

    public void setDoubleVat(EdsVat doubleVat) {
        this.doubleVat = doubleVat;
    }

    public String getQuickbookItemID() {
        return quickbookItemID;
    }

    public void setQuickbookItemID(String quickbookItemID) {
        this.quickbookItemID = quickbookItemID;
    }

    public BigDecimal getConvertedQty() {
        return convertedQty;
    }

    public void setConvertedQty(BigDecimal convertedQty) {
        this.convertedQty = convertedQty;
    }

    public BigDecimal getConvertedAmount() {
        return convertedAmount;
    }

    public void setConvertedAmount(BigDecimal convertedAmount) {
        this.convertedAmount = convertedAmount;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getDoubleTaxAmount() {
        return doubleTaxAmount;
    }

    public void setDoubleTaxAmount(BigDecimal doubleTaxAmount) {
        this.doubleTaxAmount = doubleTaxAmount;
    }

    @Override
    public BigDecimal getApplyingQuantity() {
        return getQty();
    }

    @Override
    public Integer getTransactionItemId() {
        return getObjectID();
    }

    public BigDecimal getItemCalculatedTaxAmount(boolean forDoubleTax, Integer taxCalculationType) {
        BigDecimal netAmount = qty.multiply(unitPrice);
        BigDecimal itemDiscount = BigDecimal.ZERO;
        if (getDiscount() != null) {
            itemDiscount = netAmount.multiply(getDiscount()).divide(AccountingConstants.HUNDRED, ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
        } else {
            itemDiscount = getDiscountAmount() != null ? getDiscountAmount() : AccountingConstants.ZERO;
        }

        if (getDoubleDiscount() != null) {
            itemDiscount = itemDiscount.add(netAmount.multiply(getDoubleDiscount()).divide(AccountingConstants.HUNDRED, ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP));
        } else {
            itemDiscount = itemDiscount.add(getDoubleDiscountAmount() != null ? getDoubleDiscountAmount() : AccountingConstants.ZERO);
        }

        BigDecimal discountedTotal = netAmount.subtract(itemDiscount);
        BigDecimal taxPercent = AccountingConstants.ZERO;
        if (forDoubleTax) {
            if (getDoubleVat() != null) {
                taxPercent = getDoubleVat().getEffectiveRateAsBigDecimal();
            }
        } else {
            if (getVat() != null) {
                taxPercent = getVat().getEffectiveRateAsBigDecimal();
            }
        }
        if (taxCalculationType == null) {
            taxCalculationType = getTaxCalculationType();
        }

        if (AccountingConstants.TAX_CALCULATION_INCLUSIVE.equals(taxCalculationType)) {
            return discountedTotal.multiply(taxPercent).divide(AccountingConstants.HUNDRED.add(taxPercent), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP).setScale(ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
        } else {
            return discountedTotal.multiply(taxPercent).divide(AccountingConstants.HUNDRED, ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP).setScale(ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
        }
    }

    public BigDecimal getItemCalculatedDiscountAmount() {
        BigDecimal discountAmount = AccountingConstants.ZERO;
        BigDecimal pureNet = getQty().multiply(getUnitPrice());

        if (getItemDiscount() != null) {
            EdsDiscount edsDiscount = getItemDiscount();
            if (edsDiscount.getPercentage() != null && edsDiscount.getPercentage().compareTo(AccountingConstants.ZERO) != 0) {
                discountAmount = pureNet.multiply(edsDiscount.getPercentage()).divide(AccountingConstants.HUNDRED, ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
            } else if (edsDiscount.getFixedAmount() != null && edsDiscount.getFixedAmount().compareTo(AccountingConstants.ZERO) != 0) {
                discountAmount = edsDiscount.getFixedAmount();
            }
        } else if (getDiscount() != null) {
            discountAmount = pureNet.multiply(getDiscount()).divide(AccountingConstants.HUNDRED, ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
        } else if (getDiscountAmount() != null) {
            discountAmount = getDiscountAmount();
        }

        BigDecimal doubleDiscountAmount = AccountingConstants.ZERO;
        if (getItemDoubleDiscount() != null) {
            EdsDiscount edsDoubleDiscount = getItemDoubleDiscount();
            if (edsDoubleDiscount.getPercentage() != null && edsDoubleDiscount.getPercentage().compareTo(AccountingConstants.ZERO) != 0) {
                doubleDiscountAmount = pureNet.multiply(edsDoubleDiscount.getPercentage()).divide(AccountingConstants.HUNDRED, ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
            } else if (edsDoubleDiscount.getFixedAmount() != null && edsDoubleDiscount.getFixedAmount().compareTo(AccountingConstants.ZERO) != 0) {
                doubleDiscountAmount = edsDoubleDiscount.getFixedAmount();
            }
        } else if (getDoubleDiscount() != null) {
            doubleDiscountAmount = pureNet.multiply(getDoubleDiscount()).divide(AccountingConstants.HUNDRED, ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
        } else if (getDoubleDiscountAmount() != null) {
            doubleDiscountAmount = getDoubleDiscountAmount();
        }

        return discountAmount.add(doubleDiscountAmount);
    }

    public BigDecimal calculateAllocationAmountForReceive(Integer expenseAllocationType) {
        if (allocatedExpense != null && allocatedExpense.compareTo(BigDecimal.ZERO) > 0) {
            if (AccountingConstants.ALLOCATE_MANUALLY.equals(expenseAllocationType) || AccountingConstants.ALLOCATE_RECEIVE_ITEM_BY_QTY.equals(expenseAllocationType)
                    || AccountingConstants.ALLOCATE_RECEIVE_ITEM_BY_AMOUNT.equals(expenseAllocationType) || AccountingConstants.ALLOCATE_BY_QTY.equals(expenseAllocationType)) {
                return allocatedExpense;
            } else {
                BigDecimal qty = (getQty() != null && getQty().compareTo(BigDecimal.ZERO) > 0) ? getQty() : BigDecimal.ONE;
                BigDecimal previousReceivedQuantity = getReceivedQty().subtract(getReceive());
                BigDecimal qtyNotReceived = qty.subtract(previousReceivedQuantity).setScale(2, RoundingMode.HALF_UP);
                if (qtyNotReceived.compareTo(AccountingConstants.ZERO) > 0) {
                    return allocatedExpense.multiply(getReceive()).divide(qtyNotReceived, ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
                } else {
                    return allocatedExpense;
                }
            }
        }
        return BigDecimal.ZERO;
    }

    public Integer getTaxCalculationType() {
        return AccountingConstants.TAX_CALCULATION_EXCLUSIVE;
    }

    public ProductSerialItem[] getAssignedSerials() {
        return assignedSerials;
    }

    public void setAssignedSerials(ProductSerialItem[] assignedSerials) {
        this.assignedSerials = assignedSerials;
    }

    public EdsInvoiceItemCustomFields getCustomFields() {
        return customFields;
    }

    public void setCustomFields(EdsInvoiceItemCustomFields customFields) {
        this.customFields = customFields;
    }

    public BigDecimal getTransactionValue() {
        return transactionValue;
    }

    public void setTransactionValue(BigDecimal transactionValue) {
        this.transactionValue = transactionValue;
    }

    public List<String> getSerials() {
        return serials;
    }

    public void setSerials(List<String> serials) {
        this.serials = serials;
    }

    public List<ProductTrackBatchItem> getBatchItems() {
        return batchItems;
    }

    public void setBatchItems(List<ProductTrackBatchItem> batchItems) {
        this.batchItems = batchItems;
    }

    public String getShortLink() {
        return shortLink;
    }

    public void setShortLink(String shortLink) {
        this.shortLink = shortLink;
    }
}
