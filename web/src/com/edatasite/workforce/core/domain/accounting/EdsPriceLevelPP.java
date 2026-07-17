package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.priceLevel.PriceLevelPPItem;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: Jan 24, 2011
 * Time: 6:12:28 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "price_level_per_product")
public class EdsPriceLevelPP extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "custom_price")
    private Double customPrice = Double.valueOf("0");

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private EdsItem product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "price_level_id")
    private EdsPriceLevel priceLevel;


    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Double getCustomPrice() {
        return customPrice;
    }

    public void setCustomPrice(Double customPrice) {
        this.customPrice = customPrice;
    }

    public EdsItem getProduct() {
        return product;
    }

    public void setProduct(EdsItem product) {
        this.product = product;
    }

    public EdsPriceLevel getPriceLevel() {
        return priceLevel;
    }

    public void setPriceLevel(EdsPriceLevel priceLevel) {
        this.priceLevel = priceLevel;
    }

    public PriceLevelPPItem toRPC() {
        PriceLevelPPItem item = new PriceLevelPPItem();
        item.setObjectId(getObjectID());

        if (getProduct() != null) {
            item.setProductID(getProduct().getObjectID());
            item.setProductName(getProduct().getName());
            item.setStandarPrice(getProduct().getSellingPrice().doubleValue());
        }
        item.setCustomPrice(getCustomPrice());

        return item;
    }
}
