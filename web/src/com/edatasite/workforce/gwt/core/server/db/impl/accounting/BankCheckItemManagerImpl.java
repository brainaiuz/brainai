package com.edatasite.workforce.gwt.core.server.db.impl.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsBankCheckItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.accounting.BankCheckItemManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * User: Dilsh0d Madrahimov
 * Date: 5.24.2017
 * Time: 18:25:30
 */
@Repository("bankCheckItemManager")
public class BankCheckItemManagerImpl extends BaseManager<EdsBankCheckItem> implements BankCheckItemManager {
    public BankCheckItemManagerImpl() {
        super(EdsBankCheckItem.class);
    }

    @Override
    public List<EdsBankCheckItem> getBillableExpense(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("select chi from EdsBankCheckItem chi ");
        sql.append(" join chi.bankCheck ch ");
        sql.append(" where chi.client.objectID = ?  and ").append(ServerUtils.checkForDeleted("ch.deleted"));
        sql.append(" and chi.invoice.objectID is null ");

        if (fp.getStartDate() != null && fp.getEndDate() != null) {
            sql.append(" and (ch.date between ? and ?) ");
            return find(sql.toString(), fp.getInvoiceClientId(), fp.getStartDate(), fp.getEndDate());
        } else {
            return find(sql.toString(), fp.getInvoiceClientId());
        }
    }

    @Override
    public List<EdsBankCheckItem> getItemListAsExpenseByInvoice(Integer invoiceId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select chi from EdsBankCheckItem chi ");
        sql.append(" left join chi.invoice inv ");
        sql.append(" where inv.objectID = " + invoiceId);

        return find(sql.toString());
    }
}
