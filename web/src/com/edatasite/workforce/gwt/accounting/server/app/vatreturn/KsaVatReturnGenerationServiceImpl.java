package com.edatasite.workforce.gwt.accounting.server.app.vatreturn;

import com.edatasite.workforce.core.domain.accounting.EdsVatReturn;
import com.edatasite.workforce.gwt.accounting.client.rpc.enums.VatReturnTransactionType;
import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.TaxAmountItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.VatReturnBox;
import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.VatReturnTransactionItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.ksa.KsaVatReturn;
import com.edatasite.workforce.gwt.core.client.enums.TaxKeyEnum;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.accounting.vatreturn.KsaVatReturnManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

@Service("ksaVatReturnGenerationService")
@Order(2)
public class KsaVatReturnGenerationServiceImpl implements VatReturnGenerationService<KsaVatReturn> {
    @Autowired
    private KsaVatReturnManager ksaVatReturnManager;

    @Override
    public KsaVatReturn generateVatReturn(Integer vatReturnId) {
        EdsVatReturn edsVatReturn = ksaVatReturnManager.get(vatReturnId);
        Integer filedVatReturnId = EdsVatReturn.FILED.equals(edsVatReturn.getStatus().getCode()) ? edsVatReturn.getObjectID() : null;
        KsaVatReturn vatReturn = new KsaVatReturn();

        TaxAmountItem standardRateSales = getTaxAmountItem(ksaVatReturnManager.getInvoiceTaxableTransactions(edsVatReturn.getToDate(), filedVatReturnId, TaxKeyEnum.STANDARD_RATE.name(), Constants.RECEIVABLE, true));
        TaxAmountItem outOfScope = getTaxAmountItem(ksaVatReturnManager.getInvoiceTaxableTransactions(edsVatReturn.getToDate(), filedVatReturnId, TaxKeyEnum.OUT_OF_SCOPE.name(), Constants.RECEIVABLE, true));
        TaxAmountItem zeroRateSales = getTaxAmountItem(ksaVatReturnManager.getInvoiceTaxableTransactions(edsVatReturn.getToDate(), filedVatReturnId, TaxKeyEnum.ZERO_RATE.name(), Constants.RECEIVABLE, true));
        TaxAmountItem exports = getTaxAmountItem(ksaVatReturnManager.getInvoiceTaxableTransactions(edsVatReturn.getToDate(), filedVatReturnId, TaxKeyEnum.ZERO_RATE.name(), Constants.RECEIVABLE, false));
        TaxAmountItem exemptSales = getTaxAmountItem(ksaVatReturnManager.getInvoiceTaxableTransactions(edsVatReturn.getToDate(), filedVatReturnId, TaxKeyEnum.EXEMPT.name(), Constants.RECEIVABLE, true));

        vatReturn.setStandardRateSales(standardRateSales);
        vatReturn.setOutOfScope(outOfScope);
        vatReturn.setZeroRateSales(zeroRateSales);
        vatReturn.setExports(exports);
        vatReturn.setExemptSales(exemptSales);

        TaxAmountItem salesTotal = new TaxAmountItem();
        Stream.of(standardRateSales, outOfScope, zeroRateSales, exports, exemptSales).forEach(item -> {
            salesTotal.setTaxableAmount(salesTotal.getTaxableAmount().add(item.getTaxableAmount()));
            salesTotal.setTaxAmount(salesTotal.getTaxAmount().add(item.getTaxAmount()));
        });
        vatReturn.setSalesTotal(salesTotal);
        vatReturn.setPayableTaxTotal(salesTotal.getTaxAmount());

        TaxAmountItem standardRatePurchase = getTaxAmountItem(ksaVatReturnManager.getInvoiceTaxableTransactions(edsVatReturn.getToDate(), filedVatReturnId, TaxKeyEnum.STANDARD_RATE.name(), Constants.PAYABLE, true));
        standardRatePurchase = getTaxAmountItem(standardRatePurchase, ksaVatReturnManager.getExpenseTaxableTransactions(edsVatReturn.getToDate(), filedVatReturnId, TaxKeyEnum.STANDARD_RATE.name()));

        TaxAmountItem importsSubjectPaidAtCustom = new TaxAmountItem();

        TaxAmountItem importsSubjectAccountedReverseCharge = new TaxAmountItem();
        List<Object[]> objects = ksaVatReturnManager.getReverseChargeApplicableTransactions(edsVatReturn.getToDate(), filedVatReturnId);
        objects.forEach(objs -> importsSubjectAccountedReverseCharge.setTaxableAmount(importsSubjectAccountedReverseCharge.getTaxableAmount().add((BigDecimal) objs[3])));

        TaxAmountItem zeroRatePurchase = getTaxAmountItem(ksaVatReturnManager.getInvoiceTaxableTransactions(edsVatReturn.getToDate(), filedVatReturnId, TaxKeyEnum.ZERO_RATE.name(), Constants.PAYABLE, true));
        zeroRatePurchase = getTaxAmountItem(zeroRatePurchase, ksaVatReturnManager.getExpenseTaxableTransactions(edsVatReturn.getToDate(), filedVatReturnId, TaxKeyEnum.ZERO_RATE.name()));

        TaxAmountItem exemptPurchases = getTaxAmountItem(ksaVatReturnManager.getInvoiceTaxableTransactions(edsVatReturn.getToDate(), filedVatReturnId, TaxKeyEnum.EXEMPT.name(), Constants.PAYABLE, true));
        exemptPurchases = getTaxAmountItem(exemptPurchases, ksaVatReturnManager.getExpenseTaxableTransactions(edsVatReturn.getToDate(), filedVatReturnId, TaxKeyEnum.EXEMPT.name()));

        vatReturn.setStandardRatePurchase(standardRatePurchase);
        vatReturn.setImportsSubjectPaidAtCustom(importsSubjectPaidAtCustom);
        vatReturn.setImportsSubjectAccountedReverseCharge(importsSubjectAccountedReverseCharge);
        vatReturn.setZeroRatePurchase(zeroRatePurchase);
        vatReturn.setExemptPurchase(exemptPurchases);

        TaxAmountItem purchaseTotal = new TaxAmountItem();
        Stream.of(standardRatePurchase, importsSubjectPaidAtCustom, importsSubjectAccountedReverseCharge, zeroRatePurchase, exemptPurchases).forEach(item -> {
            purchaseTotal.setTaxableAmount(purchaseTotal.getTaxableAmount().add(item.getTaxableAmount()));
            purchaseTotal.setTaxAmount(purchaseTotal.getTaxAmount().add(item.getTaxAmount()));
        });
        vatReturn.setPurchaseTotal(purchaseTotal);
        vatReturn.setReclaimableTaxTotal(purchaseTotal.getTaxAmount());
        return vatReturn;
    }

    @Override
    public List<VatReturnTransactionItem> getReturnTransactionsByBox(Integer vatReturnId, VatReturnBox box) {
        EdsVatReturn edsVatReturn = ksaVatReturnManager.get(vatReturnId);
        Integer filedVatReturnId = EdsVatReturn.FILED.equals(edsVatReturn.getStatus().getCode()) ? edsVatReturn.getObjectID() : null;

        switch (box) {
            case sa_box_1 -> {
                return getInvoiceTransactionItems(edsVatReturn.getToDate(), filedVatReturnId, TaxKeyEnum.STANDARD_RATE.name(), Constants.RECEIVABLE, true);
            }
            case sa_box_2 -> {
                return getInvoiceTransactionItems(edsVatReturn.getToDate(), filedVatReturnId, TaxKeyEnum.OUT_OF_SCOPE.name(), Constants.RECEIVABLE, true);
            }
            case sa_box_3 -> {
                return getInvoiceTransactionItems(edsVatReturn.getToDate(), filedVatReturnId, TaxKeyEnum.ZERO_RATE.name(), Constants.RECEIVABLE, true);
            }
            case sa_box_4 -> {
                return getInvoiceTransactionItems(edsVatReturn.getToDate(), filedVatReturnId, TaxKeyEnum.ZERO_RATE.name(), Constants.RECEIVABLE, false);
            }
            case sa_box_5 -> {
                return getInvoiceTransactionItems(edsVatReturn.getToDate(), filedVatReturnId, TaxKeyEnum.EXEMPT.name(), Constants.RECEIVABLE, true);
            }
            case sa_box_7 -> {
                List<VatReturnTransactionItem> items = getInvoiceTransactionItems(edsVatReturn.getToDate(), filedVatReturnId, TaxKeyEnum.STANDARD_RATE.name(), Constants.PAYABLE, true);
                items.addAll(getExpenseTransactionItems(edsVatReturn.getToDate(), filedVatReturnId, TaxKeyEnum.STANDARD_RATE.name()));
                return items;
            }
            case sa_box_8 -> {
                //TODO We do not handle this logic
                return Collections.emptyList();
            }
            case sa_box_9 -> {
                List<VatReturnTransactionItem> rtList = new ArrayList<>();
                List<Object[]> objects = ksaVatReturnManager.getReverseChargeApplicableTransactions(edsVatReturn.getToDate(), filedVatReturnId);
                objects.forEach(objs -> {
                    String type = (String) objs[0];
                    if ("INVOICE".equalsIgnoreCase(type)) {
                        rtList.add(wrapToTransactionItem(VatReturnTransactionType.PURCHASE_INVOICE, objs));
                    } else if ("CREDIT_NOTE".equalsIgnoreCase(type)) {
                        rtList.add(wrapToTransactionItem(VatReturnTransactionType.DEBIT_NOTE, objs));
                    } else if ("EXPENSE".equalsIgnoreCase(type)) {
                        rtList.add(wrapToTransactionItem(VatReturnTransactionType.EXPENSE, objs));
                    } else if ("BILL_OF_ENTRY".equalsIgnoreCase(type)) {
                        rtList.add(wrapToTransactionItem(VatReturnTransactionType.BILL_OF_ENTRY, objs));
                    }
                });
                return rtList;
            }
            case sa_box_10 -> {
                List<VatReturnTransactionItem> zitems = getInvoiceTransactionItems(edsVatReturn.getToDate(), filedVatReturnId, TaxKeyEnum.ZERO_RATE.name(), Constants.PAYABLE, true);
                zitems.addAll(getExpenseTransactionItems(edsVatReturn.getToDate(), filedVatReturnId, TaxKeyEnum.ZERO_RATE.name()));
                return zitems;
            }
            case sa_box_11 -> {
                List<VatReturnTransactionItem> eitems = getInvoiceTransactionItems(edsVatReturn.getToDate(), filedVatReturnId, TaxKeyEnum.EXEMPT.name(), Constants.PAYABLE, true);
                eitems.addAll(getExpenseTransactionItems(edsVatReturn.getToDate(), filedVatReturnId, TaxKeyEnum.EXEMPT.name()));
                return eitems;
            }
            default -> Collections.emptyList();
        }
        return Collections.emptyList();
    }

    TaxAmountItem getTaxAmountItem(List<Object[]> objects) {
        return getTaxAmountItem(null, objects);
    }

    TaxAmountItem getTaxAmountItem(TaxAmountItem taxAmountItem, List<Object[]> objects) {
        if (taxAmountItem == null) {
            taxAmountItem = new TaxAmountItem();
        }
        for (Object[] obts : objects) {
            BigDecimal taxableAmount = (BigDecimal) obts[3];

            if (taxableAmount.compareTo(BigDecimal.ZERO) >= 0) {
                taxAmountItem.setTaxableAmount(taxAmountItem.getTaxableAmount().add(taxableAmount));
            } else {
                taxAmountItem.setAdjustment(taxAmountItem.getAdjustment().add(taxableAmount));
            }
            taxAmountItem.setTaxAmount(taxAmountItem.getTaxAmount().add((BigDecimal) obts[4]));
        }
        return taxAmountItem;
    }

    List<VatReturnTransactionItem> getInvoiceTransactionItems(Date date, Integer vatReturnId, String taxRateKey, String transactionType, boolean domestic) {
        List<VatReturnTransactionItem> transactions = new ArrayList<>();
        List<Object[]> list = ksaVatReturnManager.getInvoiceTaxableTransactions(date, vatReturnId, taxRateKey, transactionType, domestic);
        for (Object[] objects : list) {
            VatReturnTransactionType type;
            if (Constants.RECEIVABLE.equals(transactionType)) {
                if (objects[0] != null && "CREDIT_NOTE".equals(objects[0])) {
                    type = VatReturnTransactionType.CREDIT_NOTE;
                } else {
                    type = VatReturnTransactionType.SALES_INVOICE;
                }
            } else {
                if (objects[0] != null && "CREDIT_NOTE".equals(objects[0])) {
                    type = VatReturnTransactionType.DEBIT_NOTE;
                } else {
                    type = VatReturnTransactionType.PURCHASE_INVOICE;
                }
            }
            transactions.add(wrapToTransactionItem(type, objects));
        }
        return transactions;
    }

    List<VatReturnTransactionItem> getExpenseTransactionItems(Date date, Integer vatReturnId, String taxRateKey) {
        List<VatReturnTransactionItem> transactions = new ArrayList<>();
        List<Object[]> list = ksaVatReturnManager.getExpenseTaxableTransactions(date, vatReturnId, taxRateKey);

        for (Object[] objects : list) {
            transactions.add(wrapToTransactionItem(VatReturnTransactionType.EXPENSE, objects));
        }
        return transactions;
    }

    VatReturnTransactionItem wrapToTransactionItem(VatReturnTransactionType type, Object[] objects) {
        VatReturnTransactionItem transaction = new VatReturnTransactionItem();
        transaction.setType(type);
        transaction.setObjectId((Integer) objects[1]);
        transaction.setNumber((String) objects[2]);

        TaxAmountItem amountItem = new TaxAmountItem();
        amountItem.setTaxableAmount((BigDecimal) objects[3]);
        amountItem.setTaxAmount((BigDecimal) objects[4]);
        transaction.setAmountItem(amountItem);

        if (objects[5] != null) {
            transaction.setDate(new DateNonConvertable((Date) objects[5]));
        }
        if (objects.length > 6 && objects[6] instanceof Integer) {
            transaction.setTransactionId((Integer) objects[6]);
        }
        return transaction;
    }
}
