package com.edatasite.workforce.gwt.core.server.db.impl.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsCustomerSupplierPayment;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.accounting.CustomerSupplierPaymentManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.invoice.client.rpc.PaymentData;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 4/30/11
 * Time: 6:12 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("customerSupplierPaymentManager")
public class CustomerSupplierPaymentManagerImpl extends BaseManager<EdsCustomerSupplierPayment> implements CustomerSupplierPaymentManager {
    public CustomerSupplierPaymentManagerImpl() {
        super(EdsCustomerSupplierPayment.class);
    }

    @Override
    public List<EdsCustomerSupplierPayment> getPayments(Integer customerSupplierID, boolean isClient) {
        return find("select distinct csp from EdsCustomerSupplierPayment csp where (csp.deleted is false or csp.deleted is null) and csp.customerSupplierID = ? and csp.type = ?",
                customerSupplierID, isClient ? EdsCustomerSupplierPayment.CUSTOMER_PAYMENT : EdsCustomerSupplierPayment.SUPPLIER_PAYMENT);
    }

    public BigDecimal getPaidAmount(Integer customerSupplierID, boolean isClient) {
        BigDecimal amount = (BigDecimal) findSingle("select sum(coalesce(csp.amountInEntityCurrency, csp.amount)) from EdsCustomerSupplierPayment csp where (csp.deleted is false or csp.deleted is null) and csp.customerSupplierID = ? and csp.type = ?",
                customerSupplierID, isClient ? EdsCustomerSupplierPayment.CUSTOMER_PAYMENT : EdsCustomerSupplierPayment.SUPPLIER_PAYMENT);
        return amount != null ? amount : AccountingConstants.ZERO;
    }

    @Override
    public boolean isPaymentsExists(Integer customerSupplierID, boolean isClient) {
        return find("select distinct csp.objectID from EdsCustomerSupplierPayment csp where (csp.deleted is false or csp.deleted is null) and csp.customerSupplierID = ? and csp.type = ?",
                customerSupplierID, isClient ? EdsCustomerSupplierPayment.CUSTOMER_PAYMENT : EdsCustomerSupplierPayment.SUPPLIER_PAYMENT).size() > 0;
    }

    @Override
    public BigDecimal getManualPaymentsAmount(Integer manualJournalID, Integer clientSupplierID, boolean isClient, Integer accountID) {
        BigDecimal amount = (BigDecimal) findSingle("select sum(coalesce(csp.amountInEntityCurrency, csp.amount)) from EdsCustomerSupplierPayment csp where csp.customerSupplierID = ? and csp.manualJournalId=? and csp.type = ? and "
                        + ServerUtils.checkForDeleted("csp.deleted") + (accountID != null ? " and csp.accountArAp.objectID = " + accountID + " " : " "),
                clientSupplierID, manualJournalID, isClient ? EdsCustomerSupplierPayment.CUSTOMER_PAYMENT : EdsCustomerSupplierPayment.SUPPLIER_PAYMENT);
        return amount != null ? amount : AccountingConstants.ZERO;
    }

    @Override
    public List<EdsCustomerSupplierPayment> getBatchPaymentItems(Integer batchPaymentID) {
        return find("select csp from EdsCustomerSupplierPayment csp where (csp.deleted is false or csp.deleted is null) and csp.underPaymentID is null and csp.batchPaymentID=?", batchPaymentID);
    }

    @Override
    public List<PaymentData> getPaymentItems(Integer batchPaymentID) {
        StringBuilder sql = new StringBuilder();
        sql.append("select csp.id objectID, csp.amount," +
                " csp.currencyID, csp.exchangeRate, mj.currencyid itemCurrencyId, mj.exchangeRate itemExchangeRate, " +
                " sum((case when csp.type = 1 then coalesce(mji.debit,0) else coalesce(mji.credit,0) end)) total," +
                " csp.date, csp.account_ar_ap as accountID from ").append(getCompanyId()).append(".customerPayment csp \n");
        sql.append("inner join ").append(getCompanyId()).append(".manualjournal mj on mj.id = csp.manualJournalId \n");
        sql.append("inner join ").append(getCompanyId()).append(".manualjournalitem mji on mji.manualjournalid = mj.id \n");
        sql.append("where csp.delete is not true and mj.deleted is not true \n");
        return null;
    }

    @Override
    public void mergeOldCrmAccountToNewOne(List<Integer> oldAccountIDs, Integer newAccountID) {
        updateNative("UPDATE " + getCompanyId() + ".customerPayment SET customerSupplierID = " + newAccountID + " WHERE customerSupplierID in (" + ServerUtils.getAsCommoDelimited(oldAccountIDs, "0", ",") + ")");
    }

    @Override
    public EdsCustomerSupplierPayment getUnderPayment(Integer underPaymentID) {
        return (EdsCustomerSupplierPayment) findSingle("select csp from EdsCustomerSupplierPayment csp where (csp.deleted = false or csp.deleted is null) and csp.underPaymentID=?", underPaymentID);

    }
}
