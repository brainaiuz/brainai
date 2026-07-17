package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsScope;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 21.04.2009
 * Time: 20:19:24
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "quoteItem")
public class EdsQuoteItem extends EdsBaseInvoiceItem {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quote_id")
    private EdsQuote quote;

    @Column(precision = 25, scale = 5)
    private BigDecimal picked;

    @Column(precision = 25, scale = 5)
    private BigDecimal packed;

    @Column(precision = 25, scale = 5)
    private BigDecimal ship;

    @Column(precision = 25, scale = 5)
    private BigDecimal readyToShip;

    @Column(precision = 25, scale = 5)
    private BigDecimal shippedQty;

    @Column(precision = 25, scale = 5)
    private BigDecimal numberOfPacks;

    @Column(precision = 25, scale = 5)
    private BigDecimal qtyPerPack;

    @Column(precision = 25, scale = 5)
    private BigDecimal bookReservation;

    @Column(name="pickable", columnDefinition = "boolean default false")
    private boolean pickable = false;

    private String reference;


    public EdsQuote getQuote() {
        return quote;
    }

    public void setQuote(EdsQuote quote) {
        this.quote = quote;
    }

    public BigDecimal calculatedQty(){
        return (getQty() != null && getQty().compareTo(BigDecimal.ZERO) > 0) ? getQty() : BigDecimal.ONE;
    }

    public BigDecimal getPicked() {
        return picked;
    }

    public void setPicked(BigDecimal picked) {
        this.picked = picked;
    }

    public BigDecimal getPacked() {
        return packed;
    }

    public void setPacked(BigDecimal packed) {
        this.packed = packed;
    }

    public BigDecimal getShip() {
        return ship;
    }

    public void setShip(BigDecimal ship) {
        this.ship = ship;
    }

    public BigDecimal getReadyToShip() {
        return readyToShip;
    }

    public void setReadyToShip(BigDecimal readyToShip) {
        this.readyToShip = readyToShip;
    }

    public BigDecimal getShippedQty() {
        return shippedQty;
    }

    public void setShippedQty(BigDecimal shippedQty) {
        this.shippedQty = shippedQty;
    }

    public BigDecimal getNumberOfPacks() {
        return numberOfPacks;
    }

    public void setNumberOfPacks(BigDecimal numberOfPacks) {
        this.numberOfPacks = numberOfPacks;
    }

    public BigDecimal getQtyPerPack() {
        return qtyPerPack;
    }

    public void setQtyPerPack(BigDecimal qtyPerPack) {
        this.qtyPerPack = qtyPerPack;
    }

    public boolean isPickable() {
        return pickable;
    }

    public void setPickable(boolean pickable) {
        this.pickable = pickable;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public BigDecimal getBookReservation() {
        return this.bookReservation;
    }

    public void setBookReservation(final BigDecimal bookReservation) {
        this.bookReservation = bookReservation;
    }

    public BigDecimal getGdnNonConvertedQty() {
        return (getShippedQty() != null ? getShippedQty() : BigDecimal.ZERO).subtract(getConvertedQty() != null ? getConvertedQty() : BigDecimal.ZERO);
    }
}
