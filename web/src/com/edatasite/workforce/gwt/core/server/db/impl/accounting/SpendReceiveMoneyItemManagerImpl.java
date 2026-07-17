package com.edatasite.workforce.gwt.core.server.db.impl.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsBankTransferItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.accounting.SpendReceiveMoneyItemManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * User: Dilsh0d Madrahimov
 * Date: 5.24.2017
 * Time: 18:25:30
 */
@Repository("spendReceiveMoneyItemManager")
public class SpendReceiveMoneyItemManagerImpl extends BaseManager<EdsBankTransferItem> implements SpendReceiveMoneyItemManager {

    public SpendReceiveMoneyItemManagerImpl() {
        super(EdsBankTransferItem.class);
    }

    @Override
    public List<EdsBankTransferItem> getBillableExpense(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("select bti from EdsBankTransferItem bti ");
        sql.append(" join bti.bankTransfer bt ");
        sql.append(" where bti.client.objectID = ?  and ").append(ServerUtils.checkForDeleted("bt.deleted"));
        sql.append(" and bti.invoice.objectID is null ");

        if (fp.getStartDate() != null && fp.getEndDate() != null) {
            sql.append(" and (bt.date between ? and ?) ");
            return find(sql.toString(), fp.getInvoiceClientId(), fp.getStartDate(), fp.getEndDate());
        } else {
            return find(sql.toString(), fp.getInvoiceClientId());
        }
    }

    @Override
    public List<EdsBankTransferItem> getItemListAsExpenseByInvoice(Integer invoiceId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select bti from EdsBankTransferItem bti ");
        sql.append(" left join bti.invoice inv ");
        sql.append(" where inv.objectID = " + invoiceId);

        return find(sql.toString());
    }
}
