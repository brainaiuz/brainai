package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.accounting.client.rpc.enums.ReceiveTypeEnum;
import com.edatasite.workforce.gwt.core.client.enums.ShippingDataItemStatus;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.domain.ObjectHistory;
import com.edatasite.workforce.gwt.invoice.client.rpc.ProductTrackBatchItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.ShippingDataItem;
import org.hibernate.annotations.ForeignKey;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 27.05.2009
 * Time: 17:28:27
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "shipping_data_items")
public class EdsShippingDataItem extends EdsObject implements ObjectHistory, IApplyingStockItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "createdDate")
    private Date creationTime;
    @Column(name = "modifiedDate")
    private Date lastUpdateTime;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "createdBy")
    private EdsUser creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modifiedBy")
    private EdsUser updater;

    private Integer shippingDataId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shippingDataId", updatable = false, insertable = false)
    private EdsShippingData shippingData;

    @Column(precision = 25, scale = 5)
    private BigDecimal receivedAllocation;

    @Column(precision = 25, scale = 5)
    private BigDecimal receivedQty;
    @Column(precision = 25, scale = 5)
    private BigDecimal receivedAmount;
    @Column(precision = 25, scale = 5)
    private BigDecimal converted;

    @Enumerated(EnumType.STRING)
    private ReceiveTypeEnum receiveType;

    @Enumerated(EnumType.STRING)
    private ShippingDataItemStatus status;

    private Integer warehouseId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouseId", updatable = false, insertable = false)
    private EdsWarehouse warehouse;

    private Integer quoteItemId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quoteItemId", updatable = false, insertable = false)
    @ForeignKey(name = "none")
    private EdsQuoteItem quoteItem;
    @Column(columnDefinition = "  boolean DEFAULT false ")
    private Boolean deleted;

    @Transient
    private BigDecimal transactionValue;

    @Transient
    private List<String> serials;

    @Transient
    private List<ProductTrackBatchItem> batchItems;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Date getCreationTime() {
        return this.creationTime != null ? new Date(this.creationTime.getTime()) : null;
    }

    @Override
    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime != null ? new Date(creationTime.getTime()) : null;
    }

    public Date getLastUpdateTime() {
        return this.lastUpdateTime != null ? new Date(this.lastUpdateTime.getTime()) : null;
    }

    @Override
    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime != null ? new Date(lastUpdateTime.getTime()) : null;
    }

    public EdsUser getCreator() {
        return creator;
    }

    @Override
    public void setCreator(EdsUser creator) {
        this.creator = creator;
    }

    public EdsUser getUpdater() {
        return updater;
    }

    @Override
    public void setUpdater(EdsUser updater) {
        this.updater = updater;
    }

    public EdsShippingData getShippingData() {
        return shippingData;
    }

    public Integer getShippingDataId() {
        return shippingDataId;
    }

    public void setShippingDataId(Integer shippingDataId) {
        this.shippingDataId = shippingDataId;
    }

    public BigDecimal getReceivedQty() {
        return receivedQty;
    }

    public void setReceivedQty(BigDecimal receivedQty) {
        this.receivedQty = receivedQty;
    }

    public ReceiveTypeEnum getReceiveType() {
        return Optional.ofNullable(receiveType).orElse(ReceiveTypeEnum.RECEIVE_BY_QTY);
    }

    public void setReceiveType(ReceiveTypeEnum receiveType) {
        this.receiveType = Optional.ofNullable(receiveType).orElse(ReceiveTypeEnum.RECEIVE_BY_QTY);
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public EdsWarehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(EdsWarehouse warehouse) {
        this.warehouse = warehouse;
    }

    public Integer getQuoteItemId() {
        return quoteItemId;
    }

    public void setQuoteItemId(Integer quoteItemId) {
        this.quoteItemId = quoteItemId;
    }

    public EdsQuoteItem getQuoteItem() {
        return quoteItem;
    }

    public void setQuoteItem(EdsQuoteItem quoteItem) {
        this.quoteItem = quoteItem;
    }

    public BigDecimal getReceivedAllocation() {
        return receivedAllocation;
    }

    public void setReceivedAllocation(BigDecimal receivedAllocation) {
        this.receivedAllocation = receivedAllocation;
    }

    public BigDecimal getReceivedAmount() {
        return receivedAmount;
    }

    public void setReceivedAmount(BigDecimal receivedAmount) {
        this.receivedAmount = receivedAmount;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public boolean isDeleted() {
        return Optional.ofNullable(this.getDeleted()).orElse(false);
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
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

    public BigDecimal getConverted() {
        return converted;
    }

    public void setConverted(BigDecimal converted) {
//        this.converted = converted;
    }

    public ShippingDataItemStatus getStatus() {
        return status;
    }

    public void setStatus(ShippingDataItemStatus status) {
        this.status = status;
    }

    @Override
    public EdsItem getItem() {
        return getQuoteItem() != null ? getQuoteItem().getItem() : null;
    }

    @Override
    public BigDecimal getApplyingQuantity() {
        return receivedQty;
    }

    @Override
    public Integer getTransactionItemId() {
        return quoteItemId;
    }

    @Override
    public BigDecimal getTransactionValue() {
        return transactionValue;
    }

    @Override
    public void setTransactionValue(BigDecimal transactionValue) {
        this.transactionValue = transactionValue;
    }

    public BigDecimal getFullQuantityToConvert() {
        if (this.getReceiveType() == ReceiveTypeEnum.RECEIVE_BY_QTY) {
            return Optional.ofNullable(this.getReceivedQty()).orElse(BigDecimal.ZERO);
        }
        return Optional.ofNullable(this.getReceivedAmount()).orElse(BigDecimal.ZERO);
    }

    public ShippingDataItem toTO() {
        final ShippingDataItem item = new ShippingDataItem();

        item.setId(this.getObjectID());
        item.setReceiveType(this.getReceiveType());
        item.setStatus(this.getStatus());
        if (this.getWarehouse() != null) {
            item.setWarehouse(this.getWarehouse().getAsSelectItem());
        }
        item.setAmount(this.getReceivedQty());
        if (this.getReceiveType() != ReceiveTypeEnum.RECEIVE_BY_QTY) {
            item.setAmount(this.getReceivedAmount());
            item.setNet(this.getReceivedAmount());
        }
        if (item.getNet() == null) {
            BigDecimal netAmout = quoteItem.getNet().multiply(getReceivedQty()).divide(quoteItem.calculatedQty(), ServerUtils.getSystemCalculationScale(), BigDecimal.ROUND_HALF_UP);
            netAmout = netAmout.multiply(quoteItem.getQuote().getExchangeRate()).setScale(ServerUtils.getSystemCalculationScale(), BigDecimal.ROUND_HALF_UP);
            item.setNet(netAmout);
        }
        item.setReceivedAllocation(this.getReceivedAllocation() != null ? this.getReceivedAllocation() : BigDecimal.ZERO);
        if (this.getQuoteItem() != null && this.getQuoteItem().getItem() != null) {
            item.setItem(this.getQuoteItem().getItem().getAsProductSelectItem());
        } else {
            SelectItem manualCreatedItem = new SelectItem(getQuoteItem().getItemName());
            manualCreatedItem.setName(getQuoteItem().getItemName());
            item.setItem(manualCreatedItem);
        }
        return item;
    }
}
