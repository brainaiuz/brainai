package com.edatasite.workforce.gwt.accounting.server.app.vatreturn;

import com.edatasite.workforce.core.domain.accounting.EdsAccount;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.accounting.EdsVatReturn;
import com.edatasite.workforce.gwt.accounting.client.rpc.enums.VatReturnTransactionType;
import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.TaxAmountItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.VatReturnBox;
import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.VatReturnTransactionItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.uk.UKVatReturn;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.FinancialSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.vatreturn.UKVatReturnManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.vatreturn.VatReturnManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service("ukVatReturnGenerationService")
@Order(3)
public class UKVatReturnGenerationServiceImpl implements VatReturnGenerationService<UKVatReturn> {

    @Autowired
    private VatReturnManager vatReturnManager;
    @Autowired
    private UKVatReturnManager ukVatReturnManager;
    @Autowired
    private FinancialSettingsManager financialSettingsManager;

    HashMap<Integer, VatReturnTransactionType> transactionTypeMap = new HashMap<Integer, VatReturnTransactionType>() {{
        put(0, VatReturnTransactionType.RECEIVE_MONEY);
        put(1, VatReturnTransactionType.SPEND_MONEY);
        put(2, VatReturnTransactionType.CASH_RECEIPT);
        put(3, VatReturnTransactionType.CASH_PAYMENT);
    }};

    @Override
    public UKVatReturn generateVatReturn(Integer vatReturnId) {
        EdsVatReturn vatReturn = vatReturnManager.get(vatReturnId);
        BigDecimal salesVat;
        BigDecimal totalSales;
        BigDecimal purchaseVat;
        BigDecimal totalPurchase;
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        Date from = vatReturn.getFromDate();
        Date to = vatReturn.getToDate();
        if (AccountingConstants.ACCRUAL.equals(financialSettings.getVatAccountingBasis())) {
            salesVat = ukVatReturnManager.generateAccrualVATData(from, to, EdsAccount.VAT_OUTPUT);
            totalSales = ukVatReturnManager.generateAccrualVATData(from, to, EdsAccount.ACCOUNTS_RECEIVABLE);
            purchaseVat = ukVatReturnManager.generateAccrualVATData(from, to, EdsAccount.VAT_INPUT);
            totalPurchase = ukVatReturnManager.generateAccrualVATData(from, to, EdsAccount.ACCOUNTS_PAYABLE);
        } else {
            salesVat = ukVatReturnManager.getInvoicePaymentVATData(from, to, Constants.RECEIVABLE, false);
            salesVat = salesVat.add(ukVatReturnManager.getSpendReceiveVATData(from, to, true, false));

            totalSales = ukVatReturnManager.getInvoicePaymentVATData(from, to, Constants.RECEIVABLE, true);
            totalSales = totalSales.add(ukVatReturnManager.getSpendReceiveVATData(from, to, true, true));

            purchaseVat = ukVatReturnManager.getInvoicePaymentVATData(from, to, Constants.PAYABLE, false);
            purchaseVat = purchaseVat.add(ukVatReturnManager.getSpendReceiveVATData(from, to, false, false));
            purchaseVat = purchaseVat.add(ukVatReturnManager.getExpenseVATData(from, to, false));

            totalPurchase = ukVatReturnManager.getInvoicePaymentVATData(from, to, Constants.PAYABLE, true);
            totalPurchase = totalPurchase.add(ukVatReturnManager.getSpendReceiveVATData(from, to, false, true));
            totalPurchase = totalPurchase.add(ukVatReturnManager.getExpenseVATData(from, to, true));
        }

        UKVatReturn result = new UKVatReturn();
        result.setVatOnSales(salesVat.setScale(2, RoundingMode.HALF_UP));
        result.setTotalSales(totalSales.subtract(salesVat).setScale(0, RoundingMode.HALF_UP));

        result.setVatOnPurchase(purchaseVat.setScale(2, RoundingMode.HALF_UP));
        result.setTotalPurchase(totalPurchase.subtract(purchaseVat).setScale(0, RoundingMode.HALF_UP));

        BigDecimal taxTotal = result.getVatOnSales().subtract(result.getVatOnPurchase()).setScale(2, RoundingMode.HALF_UP);
        if(taxTotal.compareTo(BigDecimal.ZERO) >= 0){
            result.setPayableTaxTotal(taxTotal);
            result.setReclaimableTaxTotal(BigDecimal.ZERO);
        } else {
            result.setReclaimableTaxTotal(taxTotal.abs());
            result.setPayableTaxTotal(BigDecimal.ZERO);
        }

        return result;
    }

    @Override
    public List<VatReturnTransactionItem> getReturnTransactionsByBox(Integer vatReturnId, VatReturnBox box) {
        EdsVatReturn vatReturn = vatReturnManager.get(vatReturnId);
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        if (AccountingConstants.ACCRUAL.equals(financialSettings.getVatAccountingBasis())) {
            return getAccrualTransactions(vatReturn, box);
        } else {
            return getCashTransactions(vatReturn, box);
        }
    }

    private List<VatReturnTransactionItem> getCashTransactions(EdsVatReturn vatReturn, VatReturnBox box) {
        if (VatReturnBox.BOX_1.equals(box) || VatReturnBox.BOX_6.equals(box)) {
            List<Object[]> salesTransactions = ukVatReturnManager.getInvoicePaymentTransactions(vatReturn.getFromDate(), vatReturn.getToDate(), Constants.RECEIVABLE, VatReturnBox.BOX_6.equals(box));
            List<Object[]> receiveTransactions = ukVatReturnManager.getSpendReceiveTransactions(vatReturn.getFromDate(), vatReturn.getToDate(), true, VatReturnBox.BOX_6.equals(box));

            List<VatReturnTransactionItem> transactionItemList = parseToVatReturnTransactionItem(salesTransactions, VatReturnTransactionType.SALES_INVOICE);
            transactionItemList.addAll(receiveTransactions.stream().map(objects -> {
                Integer type = (Integer) objects[4];
                return parseToVatReturnTransactionItem(objects, transactionTypeMap.get(type));
            }).toList());
            return transactionItemList;
        } else if (VatReturnBox.BOX_4.equals(box) || VatReturnBox.BOX_7.equals(box)) {
            List<Object[]> salesTransactions = ukVatReturnManager.getInvoicePaymentTransactions(vatReturn.getFromDate(), vatReturn.getToDate(), Constants.PAYABLE, VatReturnBox.BOX_7.equals(box));
            List<Object[]> spendTransactions = ukVatReturnManager.getSpendReceiveTransactions(vatReturn.getFromDate(), vatReturn.getToDate(), false, VatReturnBox.BOX_7.equals(box));

            List<VatReturnTransactionItem> transactionItemList = parseToVatReturnTransactionItem(salesTransactions, VatReturnTransactionType.PURCHASE_INVOICE);
            transactionItemList.addAll(spendTransactions.stream().map(objects -> {
                Integer type = (Integer) objects[4];
                return parseToVatReturnTransactionItem(objects, transactionTypeMap.get(type));
            }).toList());
            return transactionItemList;
        }
        return null;
    }

    private List<VatReturnTransactionItem> getAccrualTransactions(EdsVatReturn vatReturn, VatReturnBox box) {
        return null;
    }

    private List<VatReturnTransactionItem> parseToVatReturnTransactionItem(List<Object[]> transactions, VatReturnTransactionType type) {
        return transactions.stream().map(objects -> parseToVatReturnTransactionItem(objects, type)).collect(Collectors.toList());
    }

    private VatReturnTransactionItem parseToVatReturnTransactionItem(Object[] transaction, VatReturnTransactionType type) {
        VatReturnTransactionItem vatReturnTransactionItem = new VatReturnTransactionItem();
        vatReturnTransactionItem.setType(type);
        vatReturnTransactionItem.setDate(new DateNonConvertable((Date) transaction[0]));
        vatReturnTransactionItem.setObjectId((Integer) transaction[1]);
        vatReturnTransactionItem.setNumber((String) transaction[2]);

        TaxAmountItem taxAmountItem = new TaxAmountItem();
        taxAmountItem.setTaxAmount((BigDecimal) transaction[3]);
        vatReturnTransactionItem.setAmountItem(taxAmountItem);
        return vatReturnTransactionItem;
    }
}
