package com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.ksa;

import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.TaxAmountItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.VatReturnData;

public class KsaVatReturn extends VatReturnData {

    private TaxAmountItem standardRateSales;
    private TaxAmountItem outOfScope;
    private TaxAmountItem zeroRateSales;
    private TaxAmountItem exports;
    private TaxAmountItem exemptSales;
    private TaxAmountItem salesTotal;

    private TaxAmountItem standardRatePurchase;
    private TaxAmountItem importsSubjectPaidAtCustom;
    private TaxAmountItem importsSubjectAccountedReverseCharge;
    private TaxAmountItem zeroRatePurchase;
    private TaxAmountItem exemptPurchase;
    private TaxAmountItem purchaseTotal;

    public TaxAmountItem getStandardRateSales() {
        return standardRateSales;
    }

    public void setStandardRateSales(TaxAmountItem standardRateSales) {
        this.standardRateSales = standardRateSales;
    }

    public TaxAmountItem getOutOfScope() {
        return outOfScope;
    }

    public void setOutOfScope(TaxAmountItem outOfScope) {
        this.outOfScope = outOfScope;
    }

    public TaxAmountItem getZeroRateSales() {
        return zeroRateSales;
    }

    public void setZeroRateSales(TaxAmountItem zeroRateSales) {
        this.zeroRateSales = zeroRateSales;
    }

    public TaxAmountItem getExports() {
        return exports;
    }

    public void setExports(TaxAmountItem exports) {
        this.exports = exports;
    }

    public TaxAmountItem getExemptSales() {
        return exemptSales;
    }

    public void setExemptSales(TaxAmountItem exemptSales) {
        this.exemptSales = exemptSales;
    }

    public TaxAmountItem getSalesTotal() {
        return salesTotal;
    }

    public void setSalesTotal(TaxAmountItem salesTotal) {
        this.salesTotal = salesTotal;
    }

    public TaxAmountItem getStandardRatePurchase() {
        return standardRatePurchase;
    }

    public void setStandardRatePurchase(TaxAmountItem standardRatePurchase) {
        this.standardRatePurchase = standardRatePurchase;
    }

    public TaxAmountItem getImportsSubjectPaidAtCustom() {
        return importsSubjectPaidAtCustom;
    }

    public void setImportsSubjectPaidAtCustom(TaxAmountItem importsSubjectPaidAtCustom) {
        this.importsSubjectPaidAtCustom = importsSubjectPaidAtCustom;
    }

    public TaxAmountItem getImportsSubjectAccountedReverseCharge() {
        return importsSubjectAccountedReverseCharge;
    }

    public void setImportsSubjectAccountedReverseCharge(TaxAmountItem importsSubjectAccountedReverseCharge) {
        this.importsSubjectAccountedReverseCharge = importsSubjectAccountedReverseCharge;
    }

    public TaxAmountItem getZeroRatePurchase() {
        return zeroRatePurchase;
    }

    public void setZeroRatePurchase(TaxAmountItem zeroRatePurchase) {
        this.zeroRatePurchase = zeroRatePurchase;
    }

    public TaxAmountItem getExemptPurchase() {
        return exemptPurchase;
    }

    public void setExemptPurchase(TaxAmountItem exemptPurchase) {
        this.exemptPurchase = exemptPurchase;
    }

    public TaxAmountItem getPurchaseTotal() {
        return purchaseTotal;
    }

    public void setPurchaseTotal(TaxAmountItem purchaseTotal) {
        this.purchaseTotal = purchaseTotal;
    }
}
