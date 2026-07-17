package com.edatasite.workforce.gwt.core.server.db.impl.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsBankStatement;
import com.edatasite.workforce.core.domain.accounting.EdsBankStatementItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.BankStatementItemListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.accounting.BankStatementItemManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: May 14, 2010
 * Time: 5:44:56 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("bankStatementItemManager")
public class BankStatementItemManagerImpl extends BaseManager<EdsBankStatementItem> implements BankStatementItemManager {

    public BankStatementItemManagerImpl() {
        super(EdsBankStatementItem.class);
    }

    public List<EdsBankStatementItem> getBankStatementItems(EdsBankStatement bankStatement) {
        return find("SELECT bsi FROM EdsBankStatementItem bsi WHERE "+ ServerUtils.checkForDeleted("bsi.deleted")+" AND  "+ ServerUtils.checkForDeleted("bsi.reconciled")+" AND bsi.bankStatement=? ORDER BY bsi.objectID DESC", bankStatement);
    }

    @Override
    public void deleteUploadFileStatementItems(Integer fileID) {
        updateNative("update "+getCompanyId()+".bankStatementItem set uploadedFileDeleted=true where bankstatementid in " +
                "(select id from "+getCompanyId()+".bankStatement where bankaccountattachementid=" + fileID+")");
    }

    public EdsBankStatementItem getBankStatementItem(Integer objectID,Integer bankStatementID) {
        return (EdsBankStatementItem) findSingle("SELECT bsi FROM EdsBankStatementItem bsi where bsi.objectID=? and bsi.bankStatement.objectID=? and "+ ServerUtils.checkForDeleted("bsi.deleted"),objectID, bankStatementID);
    }

    @Override
    public List<EdsBankStatementItem> getList(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder("select bsi.* from " + getCompanyId() + ".bankStatementItem bsi ");
        sql.append("where bsi.bankstatementid = ").append(fp.getRelationID()).append(" ");
        sql.append("and bsi.deleted is not true ");
        if (fp != null && fp.getSortField() != null) {
            if (BankStatementItemListItem.DATE.equals(fp.getSortField())) {
                sql.append(" order by bsi.transactionDate" + (fp.getSortDir() == 2 ? " desc" : ""));
            } else if (BankStatementItemListItem.SPENT.equals(fp.getSortField())) {
                sql.append(" order by bsi.credit" + (fp.getSortDir() == 2 ? " desc" : ""));
            } else if (BankStatementItemListItem.RECEIVED.equals(fp.getSortField())) {
                sql.append(" order by bsi.debit" + (fp.getSortDir() == 2 ? " desc" : ""));
            } else if (BankStatementItemListItem.BALANCE.equals(fp.getSortField())) {
                sql.append(" order by bsi.balance" + (fp.getSortDir() == 2 ? " desc" : ""));
            } else {
                sql.append(" order by bsi.id desc ");
            }
        } else {
            sql.append(" order by bsi.id desc ");
        }
        if (fp.getStart() > 0 && fp.getLimit() > 0) {
            sql.append(" OFFSET ").append(fp.getStart()).append(" LIMIT ").append(fp.getLimit());
        }
        return findNative(sql.toString(), EdsBankStatementItem.class);
    }

    @Override
    public Integer getTotalCount(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT count(DISTINCT bsi.id) FROM ").append(getCompanyId()).append(".bankStatementItem bsi ");
        sql.append("where bsi.bankstatementid = '").append(fp.getRelationID()).append("' ");
        sql.append("and bsi.deleted is not true ");

        BigInteger count = (BigInteger) findNativeSingle(sql.toString());
        return count != null ? count.intValue() : 0;
    }

}
