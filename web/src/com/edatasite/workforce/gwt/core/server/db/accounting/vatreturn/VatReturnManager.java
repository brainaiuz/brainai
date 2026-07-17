package com.edatasite.workforce.gwt.core.server.db.accounting.vatreturn;

import com.edatasite.workforce.core.domain.accounting.EdsVatAdjustment;
import com.edatasite.workforce.core.domain.accounting.EdsVatReturn;
import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.TaxAmountItem;
import com.edatasite.workforce.gwt.core.server.db.Manager;
import org.apache.commons.lang3.tuple.Pair;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface VatReturnManager extends Manager<EdsVatReturn> {

    List<EdsVatReturn> getVatReturnList();

    EdsVatReturn getLastGeneratedVATReturn();

    EdsVatReturn getUnfiledVATReturn();

    TaxAmountItem getVatAdjustment(Integer vatReturnId);

    List<EdsVatAdjustment> getVatAdjustmentList(Integer vatReturnId);

    Pair<BigDecimal, BigDecimal> getTotalTaxAmounts(Integer vatReturnId);

    void fileVatReturnTransactions(ArrayList<Integer> transactionIds, Integer vatReturnId);

    void unfileVatReturnTransactions(Integer vatReturnId);

    Optional<EdsVatReturn> findByPeriodKey(String periodKey);
}
