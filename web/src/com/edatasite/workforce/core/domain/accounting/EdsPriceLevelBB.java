package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 2/14/16
 * Time: 10:54 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "price_level_by_brand")
public class EdsPriceLevelBB extends EdsObject{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private EdsBrand brand;

    @Column(name = "effect_type")
    private Integer effectType;

    private Double percent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "price_level_id")
    private EdsPriceLevel priceLevel;


    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsBrand getBrand() {
        return brand;
    }

    public void setBrand(EdsBrand brand) {
        this.brand = brand;
    }

    public Integer getEffectType() {
        return effectType;
    }

    public void setEffectType(Integer effectType) {
        this.effectType = effectType;
    }

    public Double getPercent() {
        return percent;
    }

    public void setPercent(Double percent) {
        this.percent = percent;
    }

    public EdsPriceLevel getPriceLevel() {
        return priceLevel;
    }

    public void setPriceLevel(EdsPriceLevel priceLevel) {
        this.priceLevel = priceLevel;
    }
}
