package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.invoice.client.rpc.ProductTrackBatchItem;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Apr 15, 2010
 * Time: 1:48:20 AM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "ProductWarehouseLocation")
public class EdsProductWarehouseLocation extends EdsObject implements Constants {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "qty", precision = 11, scale = 2)
    private BigDecimal qty = new BigDecimal(1);

    @Column(name = "pickingnumber")
    private String pickingNumber;

    @Column(name = "minreorder", precision = 11, scale = 2)
    private BigDecimal minReorderQty = new BigDecimal(1);

    @Column(name = "minreorderpoint", precision = 11, scale = 2)
    private BigDecimal minReorderPoint = new BigDecimal(1);

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouseid")
    private EdsWarehouse warehouse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productlocationid")
    private EdsProductLocation productLocation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productid")
    private EdsItem product;

    @Column(name = "shipdate")
    private Date shipDate;

    @Transient
    private List<String> itemSerials;

    @Transient
    private List<ProductTrackBatchItem> trackBatchItems;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public String getPickingNumber() {
        return pickingNumber;
    }

    public void setPickingNumber(String pickingNumber) {
        this.pickingNumber = pickingNumber;
    }

    public BigDecimal getMinReorderQty() {
        return minReorderQty;
    }

    public void setMinReorderQty(BigDecimal minReorderQty) {
        this.minReorderQty = minReorderQty;
    }

    public BigDecimal getMinReorderPoint() {
        return minReorderPoint;
    }

    public void setMinReorderPoint(BigDecimal minReorderPoint) {
        this.minReorderPoint = minReorderPoint;
    }

    public EdsWarehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(EdsWarehouse warehouse) {
        this.warehouse = warehouse;
    }

    public EdsProductLocation getProductLocation() {
        return productLocation;
    }

    public void setProductLocation(EdsProductLocation productLocation) {
        this.productLocation = productLocation;
    }

    public EdsItem getProduct() {
        return product;
    }

    public void setProduct(EdsItem product) {
        this.product = product;
    }

    public Date getShipDate() {
        return shipDate;
    }

    public void setShipDate(Date shipDate) {
        this.shipDate = shipDate;
    }

    public List<String> getItemSerials() {
        return itemSerials;
    }

    public void setItemSerials(List<String> itemSerials) {
        this.itemSerials = itemSerials;
    }

    public List<ProductTrackBatchItem> getTrackBatchItems() {
        return trackBatchItems;
    }

    public void setTrackBatchItems(List<ProductTrackBatchItem> trackBatchItems) {
        this.trackBatchItems = trackBatchItems;
    }

    public ProductItem getRPC() {
        ProductItem pl = new ProductItem();
        pl.setObjectId(getProduct().getObjectID());
        pl.setProductNumber(getProduct().getProductNumber());
        pl.setName(getProduct().getName());
        pl.setDescription(getProduct().getDescription());
        pl.setUnitpPrice(getProduct().getSellingPrice());
        pl.setCostPrice(getProduct().getUnitPrice());
        if (getProduct().getAccount() != null)
            pl.setAccount(getProduct().getAccount().getName());
        if (getProduct().getCogsAccount() != null)
            pl.setCogsAccount(getProduct().getCogsAccount().getName());
        if (getProduct().getAssetAccount() != null)
            pl.setAssetAccount(getProduct().getAssetAccount().getName());
        pl.setSkuNumber(getProduct().getInternalSKUNumber());
        pl.setMinReorderPoint(getMinReorderPoint());
        pl.setTotalValue(getProduct().getTotalValue());
        if (getProduct().getAsOf() != null) {
            pl.setAsOf(new DateNonConvertable(getProduct().getAsOf()));
        }

        if (getWarehouse() != null) {
            pl.setWarehouseId(getWarehouse().getObjectID());
            pl.setWarehouseName(getWarehouse().getName());
        }

        if (getProduct().getType() != null) {
            pl.setType(getProduct().getType()); //items[i].setType(pl.getProduct().getType().getName());
            pl.setTypeName(getProduct().getTypeName());
        }

        if (getProduct().getAccount() != null) {
            pl.setAccount(getProduct().getAccount().getName());
        }
        if (getProduct().getVat() != null) {
            pl.setTaxRate(getProduct().getVat().getName());
        }
        return pl;
    }
}
