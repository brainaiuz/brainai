package com.edatasite.workforce.gwt.core.server.db.impl.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsManualJournalItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.accounting.ManualJournalItemManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * User: Dilsh0d Madrahimov
 * Date: 5.24.2017
 * Time: 18:25:30
 */
@Repository("manualJournalItemManager")
public class ManualJournalItemManagerImpl extends BaseManager<EdsManualJournalItem> implements ManualJournalItemManager {
    public ManualJournalItemManagerImpl() {
        super(EdsManualJournalItem.class);
    }

    @Override
    public List<EdsManualJournalItem> getBillableExpense(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("select mji from EdsManualJournalItem mji ");
        sql.append(" join mji.manualJournal mj ");

        sql.append(" where mji.client.objectID = ?  and ").append(ServerUtils.checkForDeleted("mj.deleted"));
        sql.append(" and mji.invoice.objectID is null ");

        if (fp.getStartDate() != null && fp.getEndDate() != null) {
            sql.append(" and (mj.date between ? and ?) ");
            return find(sql.toString(), fp.getInvoiceClientId(), fp.getStartDate(), fp.getEndDate());
        } else {
            return find(sql.toString(), fp.getInvoiceClientId());
        }
    }

    @Override
    public List<EdsManualJournalItem> getItemListAsExpenseByInvoice(Integer invoiceId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select mji from EdsManualJournalItem mji ");
        sql.append(" left join mji.invoice inv ");
        sql.append(" where inv.objectID = " + invoiceId);

        return find(sql.toString());
    }
}
