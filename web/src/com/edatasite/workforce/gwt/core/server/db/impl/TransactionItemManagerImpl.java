package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.accounting.EdsTransactionItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.TransactionItemManager;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 28.05.2009
 * Time: 15:51:03
 * To change this template use File | Settings | File Templates.
 */
@Repository("transactionItemManager")
public class TransactionItemManagerImpl extends BaseManager<EdsTransactionItem> implements TransactionItemManager {

    public TransactionItemManagerImpl() {
        super(EdsTransactionItem.class);
    }

    public EdsTransactionItem getTransactionItemsByAccountKey(Integer accountKey) {
        return (EdsTransactionItem) findSingle("select ti from EdsTransactionItem ti where ti.account.key =?", accountKey);
    }

    @Override
    public List<EdsTransactionItem> getInventoryTransaction(ListingFilterParameter fp) {
        Map<String, Object> map = new HashMap<>();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ti FROM EdsTransactionItem ti ");
        sql.append("join fetch ti.transaction t ");
        sql.append("left join ti.item i ");
        sql.append("WHERE 1=1 ");

        if (fp.getCaseID() != null) {
            sql.append("AND i.objectID = '" + fp.getCaseID() + "' ");
        }

        if (fp.getStartDate() != null && fp.getEndDate() != null) {
            if (fp.getStartDate().compareTo(fp.getEndDate()) > 0) {
                return null;
            }

            map.put("startDate", fp.getStartDate());
            map.put("endDate", fp.getEndDate());

            sql.append("AND t.journalDate between :startDate AND :endDate ");
        }

        sql.append("ORDER BY t.journalDate, t.objectID ");

        return findByNamedParams(sql.toString(), map);
    }

    public EdsTransactionItem getByTransactionIdAndAccountId(Integer transactionId, Integer accountId) {
        return (EdsTransactionItem) findSingle("select ti from EdsTransactionItem ti where ti.transaction.objectID = " + transactionId + " and ti.account.objectID = " + accountId);
    }

}
