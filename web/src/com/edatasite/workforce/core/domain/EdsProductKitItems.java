package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.accounting.EdsWarehouse;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: 30.11.2010
 * Time: 16:26:37
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "product_kit_items")
public class EdsProductKitItems extends EdsObject implements AccountingConstants {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private EdsItem item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_kit_id")
    private EdsItem productKit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse")
    private EdsWarehouse warehouse;

    @Column(precision = 25, scale = 5)
    private BigDecimal quantity = BigDecimal.ZERO;


    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsItem getItem() {
        return item;
    }

    public void setItem(EdsItem item) {
        this.item = item;
    }

    public EdsItem getProductKit() {
        return productKit;
    }

    public void setProductKit(EdsItem productKit) {
        this.productKit = productKit;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public EdsWarehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(EdsWarehouse warehouse) {
        this.warehouse = warehouse;
    }

    public BigDecimal getProductKitItemNetAmount(){
        BigDecimal price;
        if (PRODUCT_KIT.equals(getItem().getType())) {
            price = getItem().getProductKitStandardPrice();
        } else {
            price = getItem().getSellingPrice();
        }

        return price.multiply(quantity);
    }
}
