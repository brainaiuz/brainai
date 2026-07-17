package com.edatasite.workforce.gwt.accounting.server.app.vatreturn;

import com.edatasite.workforce.core.domain.EdsRegion;
import com.edatasite.workforce.core.domain.accounting.EdsVatAdjustment;
import com.edatasite.workforce.core.domain.accounting.EdsVatReturn;
import com.edatasite.workforce.gwt.accounting.client.rpc.enums.VatReturnTransactionType;
import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.TaxAmountItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.VatReturnBox;
import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.VatReturnTransactionItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.uea.UaeVatReturn;
import com.edatasite.workforce.gwt.core.client.enums.TaxKeyEnum;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.RegionManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.vatreturn.UaeVatReturnManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.vatreturn.VatReturnManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service("uaeVatReturnGenerationService")
@Order(1)
public class UaeVatReturnGenerationServiceImpl implements VatReturnGenerationService<UaeVatReturn> {

    @Autowired
    private VatReturnManager vatReturnManager;
    @Autowired
    private UaeVatReturnManager uaeVatReturnManager;
    @Autowired
    private RegionManager regionManager;

    @Override
    public UaeVatReturn generateVatReturn(Integer vatReturnId) {
        EdsVatReturn edsVatReturn = vatReturnManager.get(vatReturnId);
        Integer filedVatReturnId = EdsVatReturn.FILED.equals(edsVatReturn.getStatus().getCode()) ? edsVatReturn.getObjectID() : null;

        Integer countryId = vatReturnManager.getUser().getCompany().getCountry().getObjectID();
        EdsRegion region = null;

        UaeVatReturn vatReturn = new UaeVatReturn();

        //VAT on Sales and all other Outputs
        {
            /**
             * Initialize emirates standard tax rates for return
             */
            {
                HashMap<Integer, TaxAmountItem> map = getEmiratesStandardRate(edsVatReturn.getToDate(), filedVatReturnId);

                //Abu Dhabi
                region = regionManager.getRegion(countryId, Constants.UAE_STATES.AB);
                vatReturn.setAbudhabi(Optional.ofNullable(map.get(region.getObjectID())).orElse(new TaxAmountItem()));

                //Dubai
                region = regionManager.getRegion(countryId, Constants.UAE_STATES.DU);
                vatReturn.setDubai(Optional.ofNullable(map.get(region.getObjectID())).orElse(new TaxAmountItem()));

                //Sharjah
                region = regionManager.getRegion(countryId, Constants.UAE_STATES.SH);
                vatReturn.setSharjah(Optional.ofNullable(map.get(region.getObjectID())).orElse(new TaxAmountItem()));

                //Ajman
                region = regionManager.getRegion(countryId, Constants.UAE_STATES.AJ);
                vatReturn.setAjman(Optional.ofNullable(map.get(region.getObjectID())).orElse(new TaxAmountItem()));

                //Umm Al Quwain
                region = regionManager.getRegion(countryId, Constants.UAE_STATES.UM);
                vatReturn.setUmmAlQuwain(Optional.ofNullable(map.get(region.getObjectID())).orElse(new TaxAmountItem()));

                //Ras Al Khaimah
                region = regionManager.getRegion(countryId, Constants.UAE_STATES.RA);
                vatReturn.setRasAlKhalmah(Optional.ofNullable(map.get(region.getObjectID())).orElse(new TaxAmountItem()));

                //Fujairah
                region = regionManager.getRegion(countryId, Constants.UAE_STATES.FU);
                vatReturn.setFujairah(Optional.ofNullable(map.get(region.getObjectID())).orElse(new TaxAmountItem()));
            }

            //Reverse charge
            vatReturn.setReverscharge(uaeVatReturnManager.getReverseChargesAsTaxAmountItem(edsVatReturn.getToDate(), filedVatReturnId));
            //Zero rate
            vatReturn.setZeroRated(getOtherRateAmounts(edsVatReturn.getToDate(), filedVatReturnId, TaxKeyEnum.ZERO_RATE));
            //Exempt rate
            vatReturn.setExempt(getOtherRateAmounts(edsVatReturn.getToDate(), filedVatReturnId, TaxKeyEnum.EXEMPT));
            //Goods Imported
            vatReturn.setGoodsImported(uaeVatReturnManager.geteGoodsImportedAsTaxAmountItem(edsVatReturn.getToDate(), filedVatReturnId));
            //Adjustment
            vatReturn.setAdjustment(vatReturnManager.getVatAdjustment(edsVatReturn.getObjectID()));
        }

        //VAT on Expenses and all other Inputs
        {
            vatReturn.setExpenses(uaeVatReturnManager.getStandardRatedExpensesAsTaxAmountItem(edsVatReturn.getToDate(), filedVatReturnId));
        }

        {
            BigDecimal payableTaxTotal = BigDecimal.ZERO, reclaimableTaxTotal = BigDecimal.ZERO;

            payableTaxTotal = payableTaxTotal.add(vatReturn.getAbudhabi().getTaxAmount()).add(vatReturn.getAbudhabi().getAdjustment())
                    .add(vatReturn.getDubai().getTaxAmount()).add(vatReturn.getDubai().getAdjustment())
                    .add(vatReturn.getSharjah().getTaxAmount()).add(vatReturn.getSharjah().getAdjustment())
                    .add(vatReturn.getAjman().getTaxAmount()).add(vatReturn.getAjman().getAdjustment())
                    .add(vatReturn.getUmmAlQuwain().getTaxAmount()).add(vatReturn.getUmmAlQuwain().getAdjustment())
                    .add(vatReturn.getRasAlKhalmah().getTaxAmount()).add(vatReturn.getRasAlKhalmah().getAdjustment())
                    .add(vatReturn.getFujairah().getTaxAmount()).add(vatReturn.getFujairah().getAdjustment())
                    .add(vatReturn.getReverscharge().getTaxAmount()).add(vatReturn.getReverscharge().getAdjustment())
                    .add(vatReturn.getAdjustment().getTaxAmount()).add(vatReturn.getAdjustment().getAdjustment())
                    .add(vatReturn.getGoodsImported().getTaxAmount()).add(vatReturn.getGoodsImported().getAdjustment());
            vatReturn.setPayableTaxTotal(payableTaxTotal);

            reclaimableTaxTotal = reclaimableTaxTotal.add(vatReturn.getExpenses().getTaxAmount()).add(vatReturn.getExpenses().getAdjustment())
                    .add(vatReturn.getExpenseReverceCharge().getTaxAmount()).add(vatReturn.getExpenseReverceCharge().getAdjustment());
            vatReturn.setReclaimableTaxTotal(reclaimableTaxTotal);
        }
        return vatReturn;
    }

    @Override
    public List<VatReturnTransactionItem> getReturnTransactionsByBox(Integer vatReturnId, VatReturnBox box) {
        EdsVatReturn edsVatReturn = vatReturnManager.get(vatReturnId);
        Integer filedVatReturnId = EdsVatReturn.FILED.equals(edsVatReturn.getStatus().getCode()) ? edsVatReturn.getObjectID() : null;

        Integer countryId = vatReturnManager.getUser().getCompany().getCountry().getObjectID();
        EdsRegion region = null;

        if (VatReturnBox.ae_box_1a.equals(box)) {
            region = regionManager.getRegion(countryId, Constants.UAE_STATES.AB);
            return getEmiratesTransactions(edsVatReturn, filedVatReturnId, region.getObjectID());
        } else if (VatReturnBox.ae_box_1b.equals(box)) {
            region = regionManager.getRegion(countryId, Constants.UAE_STATES.DU);
            return getEmiratesTransactions(edsVatReturn, filedVatReturnId, region.getObjectID());
        } else if (VatReturnBox.ae_box_1c.equals(box)) {
            region = regionManager.getRegion(countryId, Constants.UAE_STATES.SH);
            return getEmiratesTransactions(edsVatReturn, filedVatReturnId, region.getObjectID());
        } else if (VatReturnBox.ae_box_1d.equals(box)) {
            region = regionManager.getRegion(countryId, Constants.UAE_STATES.AJ);
            return getEmiratesTransactions(edsVatReturn, filedVatReturnId, region.getObjectID());
        } else if (VatReturnBox.ae_box_1e.equals(box)) {
            region = regionManager.getRegion(countryId, Constants.UAE_STATES.UM);
            return getEmiratesTransactions(edsVatReturn, filedVatReturnId, region.getObjectID());
        } else if (VatReturnBox.ae_box_1f.equals(box)) {
            region = regionManager.getRegion(countryId, Constants.UAE_STATES.RA);
            return getEmiratesTransactions(edsVatReturn, filedVatReturnId, region.getObjectID());
        } else if (VatReturnBox.ae_box_1g.equals(box)) {
            region = regionManager.getRegion(countryId, Constants.UAE_STATES.FU);
            return getEmiratesTransactions(edsVatReturn, filedVatReturnId, region.getObjectID());
        } else if (VatReturnBox.ae_box_2.equals(box)) {
            List<Object[]> items = uaeVatReturnManager.getReverseCharges(edsVatReturn.getToDate(), filedVatReturnId);
            return getReverseChargeTransactions(items);
        } else if (VatReturnBox.ae_box_3.equals(box)) {
            List<Object[]> items = uaeVatReturnManager.getSalesAndOtherOutputs(edsVatReturn.getToDate(), filedVatReturnId, TaxKeyEnum.ZERO_RATE.name(), null);
            return getSalesTransactions(items);
        } else if (VatReturnBox.ae_box_4.equals(box)) {
            List<Object[]> items = uaeVatReturnManager.getSalesAndOtherOutputs(edsVatReturn.getToDate(), filedVatReturnId, TaxKeyEnum.EXEMPT.name(), null);
            return getSalesTransactions(items);
        } else if (VatReturnBox.ae_box_5.equals(box)) {
            List<Object[]> items = uaeVatReturnManager.geteGoodsImported(edsVatReturn.getToDate(), filedVatReturnId);
            return getGoodsImportedTransactions(items);
        } else if (VatReturnBox.ae_box_6.equals(box)) {
            List<EdsVatAdjustment> adjustments = vatReturnManager.getVatAdjustmentList(edsVatReturn.getObjectID());

            List<VatReturnTransactionItem> transactions = new ArrayList<>();

            for (EdsVatAdjustment adjustment : adjustments) {
                VatReturnTransactionItem transaction = new VatReturnTransactionItem();
                transaction.setType(VatReturnTransactionType.ADJUSTMENT);
                transaction.setObjectId(adjustment.getObjectID());
                transaction.setNumber(adjustment.getObjectID().toString());

                TaxAmountItem amountItem = new TaxAmountItem();
                amountItem.setTaxableAmount(BigDecimal.ZERO);
                amountItem.setTaxAmount(adjustment.getAmount());
                transaction.setAmountItem(amountItem);
                transaction.setDate(new DateNonConvertable(adjustment.getDate()));
                transactions.add(transaction);
            }
            return transactions;
        } else if (VatReturnBox.ae_box_8.equals(box)) {
            List<Object[]> items = uaeVatReturnManager.getStandardRatedExpenses(edsVatReturn.getToDate(), filedVatReturnId);
            return getExpensesAndOtherInputsTransactions(items);
        } else if (VatReturnBox.ae_box_9.equals(box)) {

            List<Object[]> items = uaeVatReturnManager.getReverseCharges(edsVatReturn.getToDate(), filedVatReturnId);
            ArrayList<VatReturnTransactionItem> list = new ArrayList<>(getReverseChargeTransactions(items));

            items = uaeVatReturnManager.geteGoodsImported(edsVatReturn.getToDate(), filedVatReturnId);
            list.addAll(getGoodsImportedTransactions(items));
            return list;
        }
        return null;
    }

    HashMap<Integer, TaxAmountItem> getEmiratesStandardRate(Date date, Integer returnId) {
        List<Object[]> list = uaeVatReturnManager.getSalesAndOtherOutputs(date, returnId, TaxKeyEnum.STANDARD_RATE.name(), null);
        HashMap<Integer, TaxAmountItem> map = new HashMap<>();

        for (Object[] objects : list) {
            Integer placeOfSupplyId = (Integer) objects[0];
            BigDecimal taxableAmount = (BigDecimal) objects[3];
            BigDecimal taxAmount = (BigDecimal) objects[4];

            TaxAmountItem item = map.getOrDefault(placeOfSupplyId, new TaxAmountItem());
            item.setTaxableAmount(item.getTaxableAmount().add(taxableAmount));
            item.setTaxAmount(item.getTaxAmount().add(taxAmount));
            map.put(placeOfSupplyId, item);
        }

        return map;
    }

    TaxAmountItem getOtherRateAmounts(Date date, Integer returnId, TaxKeyEnum rateKey) {
        List<Object[]> list = uaeVatReturnManager.getSalesAndOtherOutputs(date, returnId, rateKey.name(), null);

        TaxAmountItem item = new TaxAmountItem();

        for (Object[] objects : list) {
            BigDecimal taxableAmount = (BigDecimal) objects[3];
            BigDecimal taxAmount = (BigDecimal) objects[4];

            item.setTaxableAmount(item.getTaxableAmount().add(taxableAmount));
            item.setTaxAmount(item.getTaxAmount().add(taxAmount));
        }
        return item;
    }

    List<VatReturnTransactionItem> getEmiratesTransactions(EdsVatReturn edsVatReturn, Integer filedVatReturnId, Integer placeOfSupplyId) {
        List<Object[]> items = uaeVatReturnManager.getSalesAndOtherOutputs(edsVatReturn.getToDate(), filedVatReturnId, TaxKeyEnum.STANDARD_RATE.name(), placeOfSupplyId);
        return getSalesTransactions(items);
    }

    List<VatReturnTransactionItem> getSalesTransactions(List<Object[]> items) {
        List<VatReturnTransactionItem> transactions = new ArrayList<>();

        for (Object[] objects : items) {
            VatReturnTransactionItem transaction = new VatReturnTransactionItem();
            transaction.setType(VatReturnTransactionType.SALES_INVOICE);
            transaction.setObjectId((Integer) objects[1]);
            transaction.setNumber((String) objects[2]);

            TaxAmountItem amountItem = new TaxAmountItem();
            amountItem.setTaxableAmount((BigDecimal) objects[3]);
            amountItem.setTaxAmount((BigDecimal) objects[4]);
            transaction.setAmountItem(amountItem);

            if (objects[5] != null) {
                transaction.setDate(new DateNonConvertable((Date) objects[5]));
            }
            if (objects.length > 6) {
                transaction.setTransactionId((Integer) objects[6]);
            }
            if (objects[7] != null) {
                transaction.setCrmAccountName((String) objects[7]);
            }
            if (objects[8] != null) {
                transaction.setCrmAccountTrn((String) objects[8]);
            }

            transactions.add(transaction);
        }
        return transactions;
    }

    List<VatReturnTransactionItem> getReverseChargeTransactions(List<Object[]> items) {
        List<VatReturnTransactionItem> transactions = new ArrayList<>();

        for (Object[] objects : items) {
            VatReturnTransactionItem transaction = new VatReturnTransactionItem();
            String type = (String) objects[0];

            if (VatReturnTransactionType.EXPENSE.name().equalsIgnoreCase(type)) {
                transaction.setType(VatReturnTransactionType.EXPENSE);
            } else {
                transaction.setType(VatReturnTransactionType.PURCHASE_INVOICE);
            }
            transaction.setObjectId((Integer) objects[1]);
            transaction.setNumber((String) objects[2]);

            TaxAmountItem amountItem = new TaxAmountItem();
            amountItem.setTaxableAmount((BigDecimal) objects[3]);
            amountItem.setTaxAmount((BigDecimal) objects[4]);
            transaction.setAmountItem(amountItem);

            if (objects[5] != null) {
                transaction.setDate(new DateNonConvertable((Date) objects[5]));
            }
            if (objects.length > 6) {
                transaction.setTransactionId((Integer) objects[6]);
            }
            transactions.add(transaction);
        }
        return transactions;
    }

    List<VatReturnTransactionItem> getGoodsImportedTransactions(List<Object[]> items) {
        List<VatReturnTransactionItem> transactions = new ArrayList<>();

        for (Object[] objects : items) {
            VatReturnTransactionItem transaction = new VatReturnTransactionItem();
            transaction.setType(VatReturnTransactionType.BILL_OF_ENTRY);
            transaction.setObjectId((Integer) objects[0]);
            transaction.setNumber((String) objects[1]);

            TaxAmountItem amountItem = new TaxAmountItem();
            amountItem.setTaxableAmount((BigDecimal) objects[2]);
            amountItem.setTaxAmount((BigDecimal) objects[3]);
            transaction.setAmountItem(amountItem);

            if (objects[4] != null) {
                transaction.setDate(new DateNonConvertable((Date) objects[4]));
            }
            if (objects.length > 5) {
                transaction.setTransactionId((Integer) objects[5]);
            }
            transactions.add(transaction);
        }
        return transactions;
    }

    List<VatReturnTransactionItem> getExpensesAndOtherInputsTransactions(List<Object[]> items) {
        List<VatReturnTransactionItem> transactions = new ArrayList<>();

        for (Object[] objects : items) {
            VatReturnTransactionItem transaction = new VatReturnTransactionItem();
            String type = (String) objects[0];

            if (VatReturnTransactionType.EXPENSE.name().equalsIgnoreCase(type)) {
                transaction.setType(VatReturnTransactionType.EXPENSE);
            } else if (VatReturnTransactionType.RECEIVE_MONEY.name().equalsIgnoreCase(type)) {
                transaction.setType(VatReturnTransactionType.RECEIVE_MONEY);
            } else if (VatReturnTransactionType.SPEND_MONEY.name().equalsIgnoreCase(type)) {
                transaction.setType(VatReturnTransactionType.SPEND_MONEY);
            } else if (VatReturnTransactionType.CASH_RECEIPT.name().equalsIgnoreCase(type)) {
                transaction.setType(VatReturnTransactionType.CASH_RECEIPT);
            } else if (VatReturnTransactionType.CASH_PAYMENT.name().equalsIgnoreCase(type)) {
                transaction.setType(VatReturnTransactionType.CASH_PAYMENT);
            } else {
                transaction.setType(VatReturnTransactionType.PURCHASE_INVOICE);
            }
            transaction.setObjectId((Integer) objects[1]);
            transaction.setNumber((String) objects[2]);

            TaxAmountItem amountItem = new TaxAmountItem();
            amountItem.setTaxableAmount((BigDecimal) objects[3]);
            amountItem.setTaxAmount((BigDecimal) objects[4]);
            transaction.setAmountItem(amountItem);

            if (objects[5] != null) {
                transaction.setDate(new DateNonConvertable((Date) objects[5]));
            }
            if (objects.length > 6) {
                transaction.setTransactionId((Integer) objects[6]);
            }
            if (objects[7] != null) {
                transaction.setCrmAccountName((String) objects[7]);
            }
            if (objects[8] != null) {
                transaction.setCrmAccountTrn((String) objects[8]);
            }
            transactions.add(transaction);
        }
        return transactions;
    }
}
