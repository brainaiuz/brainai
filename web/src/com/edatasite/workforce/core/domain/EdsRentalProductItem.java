package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.accounting.client.rpc.RentalProductItem;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "rental_items")
public class EdsRentalProductItem extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "unitCode")
    private String unitCode;

    @Column(name = "price", precision = 25, scale = 5)
    private BigDecimal price;

    @Column(name = "description")
    @Type(type = "text")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private EdsItem item;

    @Override
    public Integer getObjectID() {
        return this.objectID;
    }

    public void setObjectID(final Integer objectID) {
        this.objectID = objectID;
    }

    public String getUnitCode() {
        return this.unitCode;
    }

    public void setUnitCode(final String unitCode) {
        this.unitCode = unitCode;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(final BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public EdsItem getItem() {
        return this.item;
    }

    public void setItem(final EdsItem item) {
        this.item = item;
    }

    public RentalProductItem toDTO() {
        RentalProductItem rentalProductItem = new RentalProductItem();
        rentalProductItem.setObjectId(getObjectID());
        rentalProductItem.setPrice(getPrice());
        rentalProductItem.setUnitCode(getUnitCode());
        rentalProductItem.setDescription(getDescription());
        return rentalProductItem;
    }
}
