package com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.uea;

import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.TaxAmountItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.VatReturnData;

public class UaeVatReturn extends VatReturnData {

    /**
     * Standard rates for emirates
     */
    private TaxAmountItem abudhabi;
    private TaxAmountItem dubai;
    private TaxAmountItem sharjah;
    private TaxAmountItem ajman;
    private TaxAmountItem ummAlQuwain;
    private TaxAmountItem rasAlKhalmah;
    private TaxAmountItem fujairah;

    /**
     * Tax Refunds provided to Tourists under the Tax Refunds for Tourists Scheme
     */
    private TaxAmountItem taxRefundsForTourists;

    /**
     * Supplies subject to the reverse charge provisions
     */
    private TaxAmountItem reverscharge;

    /**
     * Zero rated supplies
     * Total value of all zero-rated goods and services sold within the UAE in the current reporting period.
     */
    private TaxAmountItem zeroRated;

    /**
     * Exempt supplies
     * Total value of all exempted goods and services sold in the UAE in the current reporting period.
     */
    private TaxAmountItem exempt;

    /**
     * Goods imported into the UAE
     */
    private TaxAmountItem goodsImported;

    /**
     * Adjustments and additions to goods imported into the UAE
     */
    private TaxAmountItem adjustment;


    /**
     * Standard rated expenses
     */
    private TaxAmountItem expenses;


    public TaxAmountItem getAbudhabi() {
        return abudhabi;
    }

    public void setAbudhabi(TaxAmountItem abudhabi) {
        this.abudhabi = abudhabi;
    }

    public TaxAmountItem getDubai() {
        return dubai;
    }

    public void setDubai(TaxAmountItem dubai) {
        this.dubai = dubai;
    }

    public TaxAmountItem getSharjah() {
        return sharjah;
    }

    public void setSharjah(TaxAmountItem sharjah) {
        this.sharjah = sharjah;
    }

    public TaxAmountItem getAjman() {
        return ajman;
    }

    public void setAjman(TaxAmountItem ajman) {
        this.ajman = ajman;
    }

    public TaxAmountItem getUmmAlQuwain() {
        return ummAlQuwain;
    }

    public void setUmmAlQuwain(TaxAmountItem ummAlQuwain) {
        this.ummAlQuwain = ummAlQuwain;
    }

    public TaxAmountItem getRasAlKhalmah() {
        return rasAlKhalmah;
    }

    public void setRasAlKhalmah(TaxAmountItem rasAlKhalmah) {
        this.rasAlKhalmah = rasAlKhalmah;
    }

    public TaxAmountItem getFujairah() {
        return fujairah;
    }

    public void setFujairah(TaxAmountItem fujairah) {
        this.fujairah = fujairah;
    }

    public TaxAmountItem getTaxRefundsForTourists() {
        return taxRefundsForTourists;
    }

    public void setTaxRefundsForTourists(TaxAmountItem taxRefundsForTourists) {
        this.taxRefundsForTourists = taxRefundsForTourists;
    }

    public TaxAmountItem getReverscharge() {
        return reverscharge;
    }

    public void setReverscharge(TaxAmountItem reverscharge) {
        this.reverscharge = reverscharge;
    }

    public TaxAmountItem getZeroRated() {
        return zeroRated;
    }

    public void setZeroRated(TaxAmountItem zeroRated) {
        this.zeroRated = zeroRated;
    }

    public TaxAmountItem getExempt() {
        return exempt;
    }

    public void setExempt(TaxAmountItem exempt) {
        this.exempt = exempt;
    }

    public TaxAmountItem getGoodsImported() {
        return goodsImported;
    }

    public void setGoodsImported(TaxAmountItem goodsImported) {
        this.goodsImported = goodsImported;
    }

    public TaxAmountItem getAdjustment() {
        return adjustment;
    }

    public void setAdjustment(TaxAmountItem adjustment) {
        this.adjustment = adjustment;
    }

    public TaxAmountItem getExpenses() {
        return expenses;
    }

    public void setExpenses(TaxAmountItem expenses) {
        this.expenses = expenses;
    }

    public TaxAmountItem getExpenseReverceCharge() {
        TaxAmountItem amountItem = new TaxAmountItem();
        amountItem.setTaxableAmount(reverscharge.getTaxableAmount().add(goodsImported.getTaxableAmount()));
        amountItem.setTaxAmount(reverscharge.getTaxAmount().add(goodsImported.getTaxAmount()));
        amountItem.setAdjustment(reverscharge.getAdjustment().add(goodsImported.getAdjustment()));

        return amountItem;
    }
}
