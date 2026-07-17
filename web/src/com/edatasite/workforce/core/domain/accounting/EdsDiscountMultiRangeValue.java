package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

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

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: Dec 3, 2010
 * Time: 4:00:05 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "discount_multirange_values")
public class EdsDiscountMultiRangeValue extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discount_id")
    private EdsDiscount discount;

    private Integer type;

    private Integer fromQty;
    private Integer toQty;

    @Column(precision = 25, scale = 5)
    private BigDecimal fromAmount;

    @Column(precision = 25, scale = 5)
    private BigDecimal toAmount;

    @Column(precision = 10, scale = 5)
    private BigDecimal percentage;

    @Column(precision = 25, scale = 5)
    private BigDecimal fixedAmount;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsDiscount getDiscount() {
        return discount;
    }

    public void setDiscount(EdsDiscount discount) {
        this.discount = discount;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public BigDecimal getFromAmount() {
        return fromAmount;
    }

    public void setFromAmount(BigDecimal fromAmount) {
        this.fromAmount = fromAmount;
    }

    public BigDecimal getToAmount() {
        return toAmount;
    }

    public void setToAmount(BigDecimal toAmount) {
        this.toAmount = toAmount;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    public BigDecimal getFixedAmount() {
        return fixedAmount;
    }

    public void setFixedAmount(BigDecimal fixedAmount) {
        this.fixedAmount = fixedAmount;
    }

    public Integer getFromQty() {
        return fromQty;
    }

    public void setFromQty(Integer fromQty) {
        this.fromQty = fromQty;
    }

    public Integer getToQty() {
        return toQty;
    }

    public void setToQty(Integer toQty) {
        this.toQty = toQty;
    }
}
