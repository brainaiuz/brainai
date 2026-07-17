package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.workflow.EdsTraceable;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.invoice.client.rpc.PickList;
import com.edatasite.workforce.gwt.invoice.client.rpc.PickListItem;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Apr 24, 2010
 * Time: 5:25:29 AM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "picklist")
public class EdsPickList extends EdsTraceable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "salequote_id")
    private EdsSaleQuote saleQuote;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "picklist_id")
    private List<EdsPickListItem> pickListItems = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private EdsReference status;

    @Column(name = "carrierAccountID")
    private String carrierAccountID;

    @Column(name = "pickdate")
    private Date pickDate;

    @Column(name = "packdate")
    private Date packDate;

    @Column(name = "shipdate")
    private Date shipDate;

    @Column(name = "expectedDate")
    private Date expectedDate;

    @Column(precision = 25, scale = 5)
    private BigDecimal total;

    @Column(precision = 25, scale = 5)
    private BigDecimal taxAmount;

    @Column(precision = 25, scale = 5)
    private BigDecimal discount;

    @Column(precision = 25, scale = 5)
    private BigDecimal grossWeight;

    public EdsPickList() {
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsSaleQuote getSaleQuote() {
        return saleQuote;
    }

    public void setSaleQuote(EdsSaleQuote saleQuote) {
        this.saleQuote = saleQuote;
    }

    public List<EdsPickListItem> getPickListItems() {
        return pickListItems;
    }

    public void setPickListItems(List<EdsPickListItem> pickListItems) {
        this.pickListItems = pickListItems;
    }

    public EdsReference getStatus() {
        return status;
    }

    public void setStatus(EdsReference status) {
        this.status = status;
    }

    public String getCarrierAccountID() {
        return carrierAccountID;
    }

    public void setCarrierAccountID(String carrierAccountID) {
        this.carrierAccountID = carrierAccountID;
    }

    public Date getPickDate() {
        return pickDate;
    }

    public void setPickDate(Date pickDate) {
        this.pickDate = pickDate;
    }

    public Date getPackDate() {
        return packDate;
    }

    public void setPackDate(Date packDate) {
        this.packDate = packDate;
    }

    public Date getShipDate() {
        return shipDate;
    }

    public void setShipDate(Date shipDate) {
        this.shipDate = shipDate;
    }

    public Date getExpectedDate() {
        return expectedDate;
    }

    public void setExpectedDate(Date expectedDate) {
        this.expectedDate = expectedDate;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public PickList getData(EdsWarehouse defaultWarehouse) {
        PickList result = new PickList();
        result.setId(this.getObjectID());
        result.setClientID(this.getSaleQuote().getClient().getObjectID());
        result.setClientName(this.getSaleQuote().getClient().getName());
        result.setQuoteID(this.getSaleQuote().getObjectID());
        result.setOrderNumber(this.getSaleQuote().getNumber());
        result.setPoNumber(this.getSaleQuote().getPoNumber());
        result.setDiscount(discount != null ? discount : new BigDecimal(0)/*this.getSaleQuote().getDiscount() != null ? this.getSaleQuote().getDiscount() : new BigDecimal(0)*/);
        result.setTotal(total != null ? total : new BigDecimal(0) /*this.getSaleQuote().getTotal() != null ? this.getSaleQuote().getTotal() : new BigDecimal(0)*/);
        result.setTaxAmount(taxAmount != null ? taxAmount : new BigDecimal(0));
        result.setDueDate(new DateNonConvertable(this.getSaleQuote().getDueDate()));
        result.setSaleOrderDate(new DateNonConvertable(this.getSaleQuote().getInvoiceDate()));
        result.setExpectedDate(this.getExpectedDate() != null ? new DateNonConvertable(this.getExpectedDate()) : null);
        result.setPickDate(this.getPickDate() != null ? new DateNonConvertable(this.getPickDate()) : null);
        result.setPackDate(this.getPackDate() != null ? new DateNonConvertable(this.getPackDate()) : null);
        result.setShipDate(this.getShipDate() != null ? new DateNonConvertable(this.getShipDate()) : null);
        result.setCarrierAccountID(this.getCarrierAccountID());
        result.setGrossWeight(this.getGrossWeight());
        result.setStatus(saleQuote.getStatus() != null ? saleQuote.getStatus().getCode() : "");
        result.setPaymentInstruction(saleQuote.getPaymentInstruction() != null ? saleQuote.getPaymentInstruction() : "");
        result.setSalesOrder(saleQuote.isSalesOrder() != null ? saleQuote.isSalesOrder() : false);
        result.setReference(this.getSaleQuote().getReference());

        List<PickListItem> items = new ArrayList<>();
        for (EdsQuoteItem item : saleQuote.getQuoteItems()) {

            if (!item.isPickable()) {
                continue;
            }

            PickListItem pickListItem = new PickListItem();
            pickListItem.setObjectID(item.getObjectID());
            pickListItem.setItemType(item.getItem().getType());
            pickListItem.setItemID(item.getItem().getObjectID());
            pickListItem.setItemName(item.getItem().getProductNumber() + " -> " + item.getItem().getName());
            pickListItem.setItemNumber(item.getItem().getProductNumber());
            pickListItem.setDescription(item.getItem().getDescription());
            pickListItem.setReference(item.getReference());
            pickListItem.setPickListID(this.getObjectID());
            pickListItem.setQty(item.getQty());
            pickListItem.setReadyToShip(item.getReadyToShip());
            pickListItem.setShipped(item.getShippedQty());
            pickListItem.setShippedQty(item.getShippedQty());
            pickListItem.setPacked(item.getPacked());
            pickListItem.setPicked(item.getPicked());
            pickListItem.setNumberOfPacks(item.getNumberOfPacks());
            pickListItem.setQtyPerPack(item.getQtyPerPack());
            pickListItem.setInventoryTrackingEnabled(item.getItem().getInventoryTrackingEnabled());
            pickListItem.setBatchTrackingEnabled(item.getItem().getBatchTrackingEnabled());
            pickListItem.setTrackBatchesEnabled(item.getItem().getTrackBatchesEnabled());
            pickListItem.setBookReserve(item.getBookReservation());

            if (item.getWarehouse() != null) {
                pickListItem.setWarehouse(item.getWarehouse().getAsSelectItem());
            } else {
                pickListItem.setWarehouse(defaultWarehouse.getAsSelectItem());
            }
            items.add(pickListItem);
        }

        result.setItems(items.toArray(new PickListItem[0]));
        return result;
    }

    public void setGrossWeight(BigDecimal grossWeight) {
        this.grossWeight = grossWeight;
    }

    public BigDecimal getGrossWeight() {
        return grossWeight;
    }

    public List<PickListItem> getItemData() {
        List<PickListItem> items = new ArrayList<>();
        for (EdsPickListItem item : this.getPickListItems()) {
            PickListItem pickListItem = new PickListItem();
            pickListItem.setItemID(item.getItem().getObjectID());
            pickListItem.setItemName(item.getItem().getName());
            pickListItem.setReference(item.getReference());
            pickListItem.setPickListID(this.getObjectID());
            pickListItem.setNumberOfPacks(item.getNumberOfPacks());
            pickListItem.setQtyPerPack(item.getQtyPerPack());
            items.add(pickListItem);
        }
        return items;
    }
}
