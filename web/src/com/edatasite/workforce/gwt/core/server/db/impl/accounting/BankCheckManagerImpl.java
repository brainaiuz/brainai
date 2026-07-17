package com.edatasite.workforce.gwt.core.server.db.impl.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsAccount;
import com.edatasite.workforce.core.domain.accounting.EdsBankCheck;
import com.edatasite.workforce.core.domain.accounting.EdsBankCheckItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.BankCheckData;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.accounting.BankCheckManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 5/15/12
 * Time: 5:32 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("bankCheckManager")
public class BankCheckManagerImpl extends BaseManager<EdsBankCheck> implements BankCheckManager {
    public BankCheckManagerImpl() {
        super(EdsBankCheck.class);
    }

    @Autowired
    private GenericSettingsManager genericSettingsManager;

    @Override
    public List<EdsBankCheck> getBankCheckList(ListingFilterParameter filterParametrs) {
        String order = " order by id desc";
        boolean desc = !filterParametrs.isAscending();
        if (filterParametrs.getSortField() != null) {
            if (BankCheckData.BANK_ACCOUNT.equals(filterParametrs.getSortField())) {
                order = " order by a.name" + (desc ? " desc" : " asc");
            } else {
                String columnName = filterParametrs.getSortField();
                if (BankCheckData.NUMBER.equals(filterParametrs.getSortField())) {
                    columnName = "bc.number";
                } else if (BankCheckData.PAY_TO.equals(filterParametrs.getSortField())) {
                    columnName = "bc.payTo";
                } else if (BankCheckData.DATE.equals(filterParametrs.getSortField())) {
                    columnName = "bc.date";
                } else if (BankCheckData.AMOUNT.equals(filterParametrs.getSortField())) {
                    columnName = "bc.amount";
                } else if (BankCheckData.ADDRESS.equals(filterParametrs.getSortField())) {
                    columnName = "bc.address";
                } else if (BankCheckData.MEMO.equals(filterParametrs.getSortField())) {
                    columnName = "bc.memo";
                } else if (BankCheckData.AMOUNT_STRING_WORD.equals(filterParametrs.getSortField())) {
                    columnName = "bc.amountString";
                } else if (BankCheckData.STATUS.equals(filterParametrs.getSortField())) {
                    columnName = "bc.postDatedTransaction";
                } else if (BankCheckData.CREATOR.equals(filterParametrs.getSortField())) {
                    columnName = "u.firstname " + (desc ? " desc " : "asc ") + ", u.lastname ";
                } else if (BankCheckData.PROJECT.equals(filterParametrs.getSortField())) {
                    columnName = "p.name ";
                }
                order = " order by " + columnName + (desc ? " desc" : " asc");
            }
        }
        StringBuilder sql = new StringBuilder("select t.* from ");
        bankCheckListSql(filterParametrs, order, sql);

        if (filterParametrs.getLimit() > 0) {
            sql.append(" OFFSET " + filterParametrs.getStart() + " LIMIT " + filterParametrs.getLimit() + " ");
        }

        return findNative(sql.toString(), EdsBankCheck.class);
    }


    @Override
    public int getBankCheckListCount(ListingFilterParameter filterParametrs) {
        String schema = ServerSecurityContext.getInstance().getCompanyId();
        StringBuilder sql = new StringBuilder("select count(t.id) from ");
        String order = " order by id desc";
        bankCheckListSql(filterParametrs, order, sql);

        BigInteger totalCount = (BigInteger) findNativeSingle(sql.toString());
        return totalCount != null ? totalCount.intValue() : 0;
    }

    private void bankCheckListSql(ListingFilterParameter filterParametrs, String order, StringBuilder sql) {
        sql.append(" ( select bc.* from ").append(getCompanyId()).append(".bankcheck bc ");
        sql.append("left join").append(getCompanyId()).append(".bankAccount ba on bc.bankaccountid = ba.id ");
        sql.append("left join ").append(getCompanyId()).append(".account a on ba.accountid = a.id ");
        sql.append("left join ").append(getCompanyId()).append(".myuser u on u.id = bc.creatorId ");
        if (filterParametrs.getProjectId() != null && genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE)) {
            sql.append("left join ").append(getCompanyId()).append(".bankcheckitem bi on bi.bankcheckid=bc.id ");
        } else {
            sql.append("left join ").append(getCompanyId()).append(".project p on p.id = bc.projectId ");
        }

        sql.append("where (bc.deleted is null OR bc.deleted is false) ");

        if (filterParametrs.getStartDate() != null && filterParametrs.getEndDate() != null) {
            sql.append(" and (bc.date between '" + filterParametrs.getStartDate() + "' and '" + filterParametrs.getEndDate() + "')\n");
        }
        if (filterParametrs.getStatusCode() != null) {
            if (Constants.POST_DATED.equals(filterParametrs.getStatusCode())) {
                sql.append(" AND bc.postDatedTransaction is not false");
            }
            if (Constants.POSTED.equals(filterParametrs.getStatusCode())) {
                sql.append(" AND bc.postDatedTransaction is not true");
            }
        }
        if (filterParametrs.getFromAmount() != null) {
            sql.append(" AND bc.amount >= " + filterParametrs.getFromAmount());
        }
        if (filterParametrs.getToAmount() != null) {
            sql.append(" AND bc.amount <= " + filterParametrs.getToAmount());
        }
        if (filterParametrs.getEmployeeId() != null && -1 != filterParametrs.getEmployeeId()) {
            sql.append(" AND bc.creatorId = " + filterParametrs.getEmployeeId());
        }
        if (filterParametrs.getProjectId() != null && -1 != filterParametrs.getProjectId()) {
            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE)) {
                sql.append(" AND bi.projectId = " + filterParametrs.getProjectId());
            } else {
                sql.append(" AND bc.projectId = " + filterParametrs.getProjectId());
            }
        }
        if (filterParametrs.getBankID() != null && -1 != filterParametrs.getBankID()) {
            sql.append(" AND bc.bankaccountid = " + filterParametrs.getBankID());
        }

        String sqlSearchKey = filterParametrs.getSqlSearchKey();
        if (sqlSearchKey != null && !sqlSearchKey.isEmpty()) {
            sql.append("AND ( ");
            sql.append(" lower(bc.number) like '" + sqlSearchKey + "' ");
            sql.append(" OR lower(bc.payTo) like '" + sqlSearchKey + "' ");
            sql.append(" OR lower(bc.memo) like '" + sqlSearchKey + "' ");
            sql.append(" ) ");
        }

        sql.append(order);

        sql.append(") t");
    }

    @Override
    public Integer getBankCheckLastIntNumber() {
        return (Integer) findSingle("select bc.intNumber from EdsBankCheck bc where " + ServerUtils.checkForDeleted("bc.deleted") + " and bc.intNumber is not null order by bc.intNumber desc");
    }

    @Override
    public List<EdsBankCheckItem> getBankCheckItemsBySupplier(Integer supplierID, Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        return (List<EdsBankCheckItem>) find("select bci from EdsBankCheckItem bci join bci.bankCheck ch where " + ServerUtils.checkForDeleted("bci.bankCheck.deleted")
                + " and (ch.postDatedTransaction is null or ch.postDatedTransaction is false) and bci.crmAccount.objectID = ? and bci.account.key = ? " +
                " " + (date != null ? " and to_date(to_char(ch.date, 'yyyy-MM-dd'), 'yyyy-MM-dd') <= '" + dateFormat.format(date) + "'" : ""), supplierID, EdsAccount.ACCOUNTS_PAYABLE);
    }

    @Override
    public EdsBankCheckItem getBankCheckItem(Integer bankCheckItemID) {
        return (EdsBankCheckItem) findSingle("select bci from EdsBankCheckItem bci where bci.objectID = ?", bankCheckItemID);
    }

    @Override
    public void deleteBankCheckItems(Integer bankCheckID) {
        update("delete from EdsBankCheckItem where bankCheck.objectID = ?", bankCheckID);
    }

    @Override
    public boolean isCheckEditable(Integer bankCheckID) {
        Integer bankCheckCount = (Integer) findSingle("select distinct bcph.bankCheckItem.bankCheck.objectID from EdsBankCheckPaymentHistory bcph join bcph.invoicePayment ip where (ip.deleted is false or ip.deleted is null) and bcph.bankCheckItem.bankCheck.objectID = ?", bankCheckID);
        return bankCheckCount == null || bankCheckCount == 0;
    }

    @Override
    public List<EdsBankCheck> getBankCheksByIds(String Ids) {
        return (List<EdsBankCheck>) find("SELECT ch FROM EdsBankCheck ch WHERE " + ServerUtils.checkForDeleted("ch.deleted") + " AND ch.objectID IN (" + Ids + ")");
    }

    @Override
    public EdsBankCheck getBankCheckByCode(String code) {
        return (EdsBankCheck) findSingle("select bc from EdsBankCheck bc where bc.externalGUID=?", code);
    }

    @Override
    public List<String> isUsedAsPayment(Integer bankCheckId) {
        String schema = ServerSecurityContext.getInstance().getCompanyId();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT i.number FROM \"" + schema + "\".bankcheck bc ");
        sql.append("INNER JOIN \"" + schema + "\".bankcheckitem bci ON bci.bankcheckid=bc.id ");
        sql.append("INNER JOIN \"" + schema + "\".bankcheckhistory bch ON bch.bankcheckitemid=bci.id ");
        sql.append("INNER JOIN \"" + schema + "\".invoicepayments ip ON ip.id = bch.invoicepaymentid ");
        sql.append("LEFT JOIN \"" + schema + "\".invoice i ON i.id=ip.invoiceid ");
        sql.append("WHERE ip.deleted is not true and bc.id='" + bankCheckId + "' AND bci.usedaspayment is not null");

        return findNative(sql.toString());
    }

    @Override
    public List<EdsBankCheck> getPostDatedPreCheckList(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT check FROM EdsBankCheck check \n");
        sql.append("WHERE (check.deleted is null OR check.deleted is false) \n");
        sql.append("AND check.postDatedTransaction is true \n");
        sql.append("AND to_date(to_char(check.date, 'yyyy-MM-dd'), 'yyyy-MM-dd') <= '").append(dateFormat.format(date)).append("' ");

        return find(sql.toString());
    }

    @Override
    public void mergeOldCrmAccountToNewOne(List<Integer> oldAccountIDs, Integer newAccountID) {
        updateNative("UPDATE " + getCompanyId() + ".bankcheckitem SET crmaccountid = " + newAccountID + " WHERE crmaccountid in (" + ServerUtils.getAsCommoDelimited(oldAccountIDs, "0", ",") + ")");
    }
}
