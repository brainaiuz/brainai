package com.edatasite.workforce.gwt.core.server.db.impl.accounting.vatreturn;

import com.edatasite.workforce.core.domain.accounting.EdsAccount;
import com.edatasite.workforce.core.domain.accounting.EdsVatAdjustment;
import com.edatasite.workforce.core.domain.accounting.EdsVatReturn;
import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.TaxAmountItem;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.accounting.vatreturn.VatReturnManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("vatReturnManager")
public class VatReturnManagerImpl extends BaseManager<EdsVatReturn> implements VatReturnManager {

    public VatReturnManagerImpl() {
        super(EdsVatReturn.class);
    }

    @Override
    public List<EdsVatReturn> getVatReturnList() {
        return find("FROM EdsVatReturn WHERE deleted is not true ORDER BY objectID DESC");
    }

    @Override
    public EdsVatReturn getLastGeneratedVATReturn() {
        return (EdsVatReturn)findSingle("FROM EdsVatReturn WHERE deleted is not true ORDER BY toDate DESC");
    }

    @Override
    public EdsVatReturn getUnfiledVATReturn() {
        return (EdsVatReturn)findSingle("FROM EdsVatReturn WHERE deleted is not true and status.code = ? ORDER BY toDate DESC", EdsVatReturn.UNFILED);
    }

    @Override
    public TaxAmountItem getVatAdjustment(Integer vatReturnId) {
        BigDecimal taxAmount = (BigDecimal)findSingle("SELECT sum(va.amount) FROM EdsVatAdjustment va WHERE va.vatReturn.objectID = ?", vatReturnId);

        TaxAmountItem amountItem = new TaxAmountItem();
        amountItem.setTaxAmount(taxAmount);
        return amountItem;
    }

    @Override
    public List<EdsVatAdjustment> getVatAdjustmentList(Integer vatReturnId) {
        return find("SELECT va FROM EdsVatAdjustment va WHERE va.vatReturn.objectID = ?", vatReturnId);
    }

    @Override
    public Pair<BigDecimal, BigDecimal> getTotalTaxAmounts(Integer vatReturnId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT SUM(CASE WHEN a.key = " + EdsAccount.VAT_OUTPUT + " THEN COALESCE(ti.credit,0) ELSE 0 END) vatOutputCredit,  ")
                .append(" SUM(CASE WHEN a.key = ").append(EdsAccount.VAT_OUTPUT).append(" THEN COALESCE(ti.debit,0) ELSE 0 END) vatOutputDebit, ")
                .append("SUM(CASE WHEN a.key = " + EdsAccount.VAT_INPUT + " THEN COALESCE(ti.debit,0) ELSE 0 END) vatInputDebit,")
                .append("SUM(CASE WHEN a.key = " + EdsAccount.VAT_INPUT + " THEN COALESCE(ti.credit,0) ELSE 0 END) vatInputCredit ")
                .append(" FROM ").append(getCompanyId()).append(".transactionitem ti \n")
                .append("JOIN ").append(getCompanyId()).append(".transaction t ON t.id = ti.transactionid \n")
                .append("JOIN ").append(getCompanyId()).append(".account a ON a.id = ti.accountid \n");
        sql.append("WHERE t.deleted is not true AND a.key in (" + EdsAccount.VAT_OUTPUT + ", " + EdsAccount.VAT_INPUT + ") \n")
                .append("AND t.filed_vat_id = " + vatReturnId);
        Object object = findNativeSingle(sql.toString());

        if (object != null) {
            return Pair.of(((BigDecimal) ((Object[]) object)[0]).subtract((BigDecimal) ((Object[]) object)[1]), ((BigDecimal) ((Object[]) object)[2]).subtract((BigDecimal) ((Object[]) object)[3]));
        }
        return Pair.of(BigDecimal.ZERO, BigDecimal.ZERO);
    }

    @Override
    public void fileVatReturnTransactions(ArrayList<Integer> transactionIds, Integer vatReturnId) {
        updateNative("UPDATE " + getCompanyId() + ".transaction SET filed_vat_id = " + vatReturnId + " WHERE id in (" + ServerUtils.getAsCommoDelimited(transactionIds, "0", ",") + ")");
    }

    @Override
    public void unfileVatReturnTransactions(Integer vatReturnId) {
        updateNative("UPDATE " + getCompanyId() + ".transaction SET filed_vat_id = null WHERE filed_vat_id = " + vatReturnId);
    }

    @Override
    public Optional<EdsVatReturn> findByPeriodKey(String periodKey) {
        return Optional.ofNullable((EdsVatReturn) findSingle("FROM EdsVatReturn WHERE periodKey = ?", periodKey));
    }
}
