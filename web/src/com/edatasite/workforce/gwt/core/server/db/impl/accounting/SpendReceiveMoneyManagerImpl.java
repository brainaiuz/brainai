package com.edatasite.workforce.gwt.core.server.db.impl.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsAccount;
import com.edatasite.workforce.core.domain.accounting.EdsBankTransfer;
import com.edatasite.workforce.core.domain.accounting.EdsInvoicePayment;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewManualTransaction;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.AccountingManager;
import com.edatasite.workforce.gwt.core.server.db.JdbcSpringManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.SpendReceiveMoneyManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.invoice.client.rpc.TransactionAllocateItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.CREATOR;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.POST_DATED;
import static com.edatasite.workforce.gwt.core.server.servlets.pdf.PDFConstants.CHECK_NUMBER;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 16.07.2010
 * Time: 18:40:50
 * To change this template use File | Settings | File Templates.
 */
@Repository("spendReceiveMoneyManager")
public class SpendReceiveMoneyManagerImpl extends BaseManager<EdsBankTransfer> implements SpendReceiveMoneyManager, AccountingConstants {
    @Autowired
    private JdbcSpringManager jdbcSpringManager;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    private AccountingManager accountingManager;

    public SpendReceiveMoneyManagerImpl() {
        super(EdsBankTransfer.class);
    }

    public void deleteMoneyTransferItems(EdsBankTransfer bankTransfer) {
        update("delete from EdsBankTransferItem mti where mti.bankTransfer = ?", bankTransfer);
    }

    @Override
    public List<EdsBankTransfer> getPostDatedTransaction(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT bt FROM EdsBankTransfer bt \n");
        sql.append("WHERE (bt.deleted is null OR bt.deleted is false) \n");
        sql.append("AND bt.postDatedTransaction is true \n");
        sql.append("AND to_date(to_char(bt.date, 'yyyy-MM-dd'), 'yyyy-MM-dd') <= '").append(dateFormat.format(date)).append("' ");

        return find(sql.toString());
    }

    @Override
    public List<NewManualTransaction> list(ListingFilterParameter fp) {
        return list(fp, false);
    }

    @Override
    public List<NewManualTransaction> list(ListingFilterParameter fp, boolean onlyBT) {
        StringBuilder sql = new StringBuilder();
        sql.append("select * from (");
        sql.append("select bt.id objectId, coalesce(bt.postdatedtransaction, false) postDatedTransaction, bt.number, bt.date ncDate,\n ");
        sql.append("coalesce(coalesce((case when max(btp.number) is not null then null else '' end), max(btp.number) || ' -> ') || max(btp.name),\n ");
        sql.append("array_to_string(array_agg(coalesce((case when  btip.number is not null then null else '' end),  btip.number || ' -> ') ||  (btip.name)), ',')) projectName,\n ");
        sql.append("max(bt.reference) reference, max(bt.total) total,\n ");
        sql.append("max(a.name) accountName, max(c.name) currencyName, '" + Constants.BANK_TRANSFER_TRANSACTION + "' transactionType,\n ");
        sql.append("coalesce(bt.lastupdated, timestamp '1900-01-01 00:00:00') lastupdated,\n ");
        sql.append("max(u.firstname ||' '||u.lastname) creator,\n ");
        sql.append("array_to_string(array_agg(aitem.name), ', ') crmAccountItemName,\n ");
        sql.append(" bt.checkNumber checkNumber ");
        sql.append("from ").append(getCompanyId()).append(".spendreceivemoney bt \n");
        sql.append("inner join ").append(getCompanyId()).append(".spendreceivemoneyitem bti on bti.banktransferid = bt.id \n");
        sql.append("left join ").append(getCompanyId()).append(".project btp on btp.id = bt.projectid \n");
        sql.append("left join ").append(getCompanyId()).append(".project btip on btip.id = bti.projectid \n");
        sql.append("left join ").append(getCompanyId()).append(".bankAccount ba on ba.id = bt.bankaccountid \n");
        sql.append("left join ").append(getCompanyId()).append(".account a on a.id = bt.cashaccountid or a.id = ba.accountid \n");
        sql.append("left join ").append(getCompanyId()).append(".myuser u on u.id = bt.creatorid \n");
        sql.append("left join ").append(getCompanyId()).append(".crmAccount aitem on aitem.id = bti.client_or_supplier_id \n");
        sql.append("left join ").append(getPublic()).append(".currency c on c.id = bt.currencyid \n");
        sql.append("WHERE (bt.deleted is null OR bt.deleted is false) \n");

        if (fp.getType() != null) {
            sql.append("AND bt.transferType = ").append(fp.getType()).append(" \n");
        }
        if (fp.getStartDate() != null && fp.getEndDate() != null) {
            sql.append(" and (bt.date between '" + fp.getStartDate() + "' and '" + fp.getEndDate() + "')\n");
        }
        if (fp.getProjectId() != null && -1 != fp.getProjectId()) {//-1 for reset filter
            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE)) {
                sql.append("AND btip.id = ").append(fp.getProjectId()).append("\n");
            } else {
                sql.append("AND btp.id = ").append(fp.getProjectId()).append("\n");
            }
        }
        if (fp.getFromAmount() != null) {
            sql.append("AND bt.total >= ").append(fp.getFromAmount()).append("\n");
        }
        if (fp.getToAmount() != null) {
            sql.append("AND bt.total <= ").append(fp.getToAmount()).append("\n");
        }
        if (fp.getEmployeeId() != null && -1 != fp.getEmployeeId()) {//-1 for reset filter
            sql.append("AND bt.creatorid = ").append(fp.getEmployeeId()).append("\n");
        }

        if (fp.getSqlSearchKey() != null && !fp.getSqlSearchKey().isEmpty()) {
            sql.append("AND (lower(bt.name) like '").append(fp.getSqlSearchKey()).append("' OR \n");
            sql.append("lower(bt.reference) like '").append(fp.getSqlSearchKey()).append("' OR \n");
            sql.append("lower(a.name) like '").append(fp.getSqlSearchKey()).append("' OR \n");
            sql.append("lower(bt.number) like '").append(fp.getSqlSearchKey()).append("' OR \n");
            sql.append("lower(aitem.number) like '").append(fp.getSqlSearchKey()).append("' OR \n");
            sql.append("lower(aitem.name) like '").append(fp.getSqlSearchKey()).append("') \n");
        }
        sql.append("GROUP BY bt.id, bt.number, bt.date, bt.lastupdated \n");

        if (!onlyBT) {
            sql.append("UNION ALL \n");

            sql.append("select bp.id objectId, false postDatedTransaction, bp.number, bp.date ncDate,\n ");
            sql.append("pr.name projectName, bp.reference, bp.totalAmount total, a.name accountName, c.name currencyName,\n ");
            sql.append("'" + Constants.PAYMENT_TRANSACTION + "' transactionType, ");
            sql.append("coalesce(bp.lastupdated, timestamp '1900-01-01 00:00:00') lastupdated,\n ");
            sql.append("(u.firstname ||' '||u.lastname ) creator,\n ");
            sql.append("array_to_string(array_agg(aitem.name), ', ') crmAccountItemName, '' checkNumber\n ");
            sql.append("from ").append(getCompanyId()).append(".batchpayment  bp\n ");
            sql.append("inner join ").append(getCompanyId()).append(".account a on a.id = bp.accountid \n");
            sql.append("left join ").append(getPublic()).append(".currency c on c.id = bp.currencyid \n");
            sql.append("left join ").append(getCompanyId()).append(".project pr on pr.id = bp.projectId \n");
            sql.append("left join ").append(getCompanyId()).append(".myuser u on u.id = bp.creatorId \n");
            sql.append("left join ").append(getCompanyId()).append(".crmAccount aitem on aitem.id = bp.crmaccountid \n");

            if (RECEIVE_MONEY.equals(fp.getType()) || SPEND_MONEY.equals(fp.getType())) {
                sql.append("inner join ").append(getCompanyId()).append(".bankaccount ba on ba.accountid = a.id \n");
            }
            sql.append("WHERE bp.deleted is not true and bp.reversed is not true \n");

            if (!(RECEIVE_MONEY.equals(fp.getType()) || SPEND_MONEY.equals(fp.getType()))) {
                sql.append("AND a.id not in (select distinct accountid from ").append(getCompanyId()).append(".bankAccount ) \n");
            }
            if (RECEIVE_MONEY.equals(fp.getType()) || CASH_RECEIPT.equals(fp.getType())) {
                sql.append("AND bp.type = '").append(Constants.RECEIVABLE).append("' \n");
            } else {
                sql.append("AND bp.type = '").append(Constants.PAYABLE).append("' \n");
            }
            if (fp.getStartDate() != null && fp.getEndDate() != null) {
                sql.append(" and (bp.date between '" + fp.getStartDate() + "' and '" + fp.getEndDate() + "')\n");
            }
            if (fp.getProjectId() != null && -1 != fp.getProjectId()) {//-1 for reset filter
                sql.append("AND pr.id = ").append(fp.getProjectId()).append("\n");
            }
            if (fp.getFromAmount() != null) {
                sql.append("AND bp.totalAmount >= ").append(fp.getFromAmount()).append("\n");
            }
            if (fp.getToAmount() != null) {
                sql.append("AND bp.totalAmount <= ").append(fp.getToAmount()).append("\n");
            }
            if (fp.getEmployeeId() != null && -1 != fp.getEmployeeId()) {//-1 for reset filter
                sql.append("AND bp.creatorId = ").append(fp.getEmployeeId()).append("\n");
            }

            if (fp.getSqlSearchKey() != null && !fp.getSqlSearchKey().isEmpty()) {
                sql.append("AND (lower(bp.reference) like '").append(fp.getSqlSearchKey()).append("' OR \n");
                sql.append("lower(bp.number) like '").append(fp.getSqlSearchKey()).append("' OR \n");
                sql.append("lower(a.name) like '").append(fp.getSqlSearchKey()).append("' OR \n");
                sql.append("lower(aitem.number) like '").append(fp.getSqlSearchKey()).append("' OR \n");
                sql.append("lower(aitem.name) like '").append(fp.getSqlSearchKey()).append("') \n");
            }
            sql.append("GROUP BY bp.id, pr.name, a.name,c.name,u.firstname ||' '||u.lastname \n");
        }

        sql.append(") t \n");

        String sortField = fp.getSortField();
        String ascOrDesc = fp.getSortDir() == 2 ? " desc" : " asc";

        if (NUMBER_COLUMN.equals(sortField)) {
            sql.append(" ORDER BY t.number " + ascOrDesc);
        } else if (DATE_COLUMN.equals(sortField)) {
            sql.append(" ORDER BY t.ncDate " + ascOrDesc);
        } else if (AMOUNT_COLUMN.equals(sortField)) {
            sql.append(" ORDER BY t.total " + ascOrDesc);
        } else if (REFERENCE_COLUMN.equals(sortField)) {
            sql.append(" ORDER BY t.reference " + ascOrDesc);
        } else if (ACCOUNT_COLUMN.equals(sortField)) {
            sql.append(" ORDER BY t.accountname " + ascOrDesc);
        } else if (PROJECT_COLUMN.equals(sortField)) {
            sql.append(" ORDER BY t.projectname " + ascOrDesc);
        } else if (CURRENCY_COLUMN.equals(sortField)) {
            sql.append(" ORDER BY t.currencyname " + ascOrDesc);
        } else if (CREATOR.equals(sortField)) {
            sql.append(" ORDER BY t.creator " + ascOrDesc);
        } else if (POST_DATED.equals(sortField)) {
            sql.append(" ORDER BY t.postdatedtransaction " + ascOrDesc);
        } else if (CHECK_NUMBER.equals(sortField)) {
            sql.append(" ORDER BY t.checkNumber " + ascOrDesc);
        } else {
            sql.append(" ORDER BY t.lastupdated desc ");
        }

        if (fp.getLimit() > 0) {
            sql.append(" OFFSET " + fp.getStart() + " LIMIT " + fp.getLimit() + " ");
        }

        return jdbcSpringManager.getSimpleJdbcTemplate().query(sql.toString(), BeanPropertyRowMapper.newInstance(NewManualTransaction.class));
        //return findInterval(sql.toString(), fp.getStart(), fp.getLimit());
    }

    @Override
    public Integer listCount(ListingFilterParameter fp) {
        return listCount(fp, false);
    }

    @Override
    public Integer listCount(ListingFilterParameter fp, boolean onlyBT) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        StringBuilder sql = new StringBuilder();
        sql.append("select count(objectId) from (");
        sql.append("select bt.id objectId, coalesce(bt.postdatedtransaction, false) postDatedTransaction, bt.number, bt.date ncDate, coalesce(max(btp.name), array_to_string(array_agg(distinct btip.name), ',')) projectName," +
                " max(bt.reference) reference, max(bt.total) total," +
                " max(a.name) accountName, max(c.name) currencyName, '" + Constants.BANK_TRANSFER_TRANSACTION + "' transactionType from ")
                .append(getCompanyId()).append(".spendreceivemoney bt \n");
        sql.append("inner join ").append(getCompanyId()).append(".spendreceivemoneyitem bti on bti.banktransferid = bt.id \n");
        sql.append("left join ").append(getCompanyId()).append(".project btp on btp.id = bt.projectid \n");
        sql.append("left join ").append(getCompanyId()).append(".project btip on btip.id = bti.projectid \n");
        sql.append("left join ").append(getCompanyId()).append(".account a on a.id = bt.cashaccountid or a.id = bankaccountid \n");
        sql.append("left join ").append(getCompanyId()).append(".myuser u on u.id = bt.creatorid \n");
        sql.append("left join ").append(getPublic()).append(".currency c on c.id = bt.currencyid \n");
        sql.append("WHERE (bt.deleted is null OR bt.deleted is false) \n");

        if (fp.getType() != null) {
            sql.append("AND bt.transferType = ").append(fp.getType()).append(" \n");
        }
        if (fp.getStartDate() != null && fp.getEndDate() != null) {
            sql.append(" and (bt.date between '" + fp.getStartDate() + "' and '" + fp.getEndDate() + "')\n");
        }
        if (fp.getProjectId() != null && -1 != fp.getProjectId()) {//-1 for reset filter
            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE)) {
                sql.append("AND btip.id = ").append(fp.getProjectId()).append("\n");
            } else {
                sql.append("AND btp.id = ").append(fp.getProjectId()).append("\n");
            }
        }
        if (fp.getFromAmount() != null) {
            sql.append("AND bt.total >= ").append(fp.getFromAmount()).append("\n");
        }
        if (fp.getToAmount() != null) {
            sql.append("AND bt.total <= ").append(fp.getToAmount()).append("\n");
        }
        if (fp.getEmployeeId() != null && -1 != fp.getEmployeeId()) {//-1 for reset filter
            sql.append("AND bt.creatorid = ").append(fp.getEmployeeId()).append("\n");
        }
        if (fp.getSqlSearchKey() != null && !fp.getSqlSearchKey().isEmpty()) {
            sql.append("AND (lower(bt.name) like '").append(fp.getSqlSearchKey()).append("' OR \n");
            sql.append("lower(bt.reference) like '").append(fp.getSqlSearchKey()).append("' OR \n");
            sql.append("lower(a.name) like '").append(fp.getSqlSearchKey()).append("' OR \n");
            sql.append("lower(bt.number) like '").append(fp.getSqlSearchKey()).append("') \n");
        }
        sql.append("GROUP BY bt.id, bt.number, bt.date \n");

        if (!onlyBT) {
            sql.append("UNION ALL \n");

            sql.append("select bp.id objectId, false postDatedTransaction, bp.number, bp.date ncDate, pr.name projectName, bp.reference, bp.totalAmount total, a.name accountName, c.name currencyName, " +
                    "'" + Constants.PAYMENT_TRANSACTION + "' transactionType from ").append(getCompanyId()).append(".batchpayment  bp \n");
            sql.append("inner join ").append(getCompanyId()).append(".account a on a.id = bp.accountid \n");
            sql.append("left join ").append(getPublic()).append(".currency c on c.id = bp.currencyid \n");
            sql.append("left join ").append(getCompanyId()).append(".project pr on pr.id = bp.projectId \n");
            sql.append("left join ").append(getCompanyId()).append(".myuser u on u.id = bp.creatorId \n");

            if (RECEIVE_MONEY.equals(fp.getType()) || SPEND_MONEY.equals(fp.getType())) {
                sql.append("inner join ").append(getCompanyId()).append(".bankaccount ba on ba.accountid = a.id \n");
            }
            sql.append("WHERE bp.deleted is not true and bp.reversed is not true \n");

            if (!(RECEIVE_MONEY.equals(fp.getType()) || SPEND_MONEY.equals(fp.getType()))) {
                sql.append("AND a.id not in (select distinct accountid from ").append(getCompanyId()).append(".bankAccount ) \n");
            }

            if (RECEIVE_MONEY.equals(fp.getType()) || CASH_RECEIPT.equals(fp.getType())) {
                sql.append("AND bp.type = '").append(Constants.RECEIVABLE).append("' \n");
            } else {
                sql.append("AND bp.type = '").append(Constants.PAYABLE).append("' \n");
            }
            if (fp.getStartDate() != null && fp.getEndDate() != null) {
                sql.append(" and (bp.date between '" + fp.getStartDate() + "' and '" + fp.getEndDate() + "')\n");
            }
            if (fp.getProjectId() != null && -1 != fp.getProjectId()) {//-1 for reset filter
                sql.append("AND pr.id = ").append(fp.getProjectId()).append("\n");
            }
            if (fp.getFromAmount() != null) {
                sql.append("AND bp.totalAmount >= ").append(fp.getFromAmount()).append("\n");
            }
            if (fp.getToAmount() != null) {
                sql.append("AND bp.totalAmount <= ").append(fp.getToAmount()).append("\n");
            }
            if (fp.getEmployeeId() != null && -1 != fp.getEmployeeId()) {//-1 for reset filter
                sql.append("AND bp.creatorId = ").append(fp.getEmployeeId()).append("\n");
            }
            if (fp.getSqlSearchKey() != null && !fp.getSqlSearchKey().isEmpty()) {
                sql.append("AND (lower(bp.reference) like '").append(fp.getSqlSearchKey()).append("' OR \n");
                sql.append("lower(bp.number) like '").append(fp.getSqlSearchKey()).append("' OR \n");
                sql.append("lower(a.name) like '").append(fp.getSqlSearchKey()).append("') \n");
            }
        }

        sql.append(") t \n");

        return ((BigInteger) findNativeSingle(sql.toString())).intValue();
    }

    @Override
    public List<TransactionAllocateItem> getTransactionsByCrmAccount(Integer crmAccountId, boolean isClient) {
        StringBuilder sql = new StringBuilder();
        sql.append("select bt.id as objectID, bt.number, bt.currencyid as currencyID, bt.date, sum(bti.amount) as amount, bt.reference, a.id accountID from ").append(getCompanyId()).append(".spendreceivemoneyitem bti \n");
        sql.append("inner join ").append(getCompanyId()).append(".account a on a.id = bti.accountid \n");
        sql.append("inner join ").append(getCompanyId()).append(".spendreceivemoney bt on bt.id = bti.banktransferid \n");
        sql.append("where bt.deleted is not true \n");

        if (isClient) {
            sql.append(" and a.key = ").append(EdsAccount.ACCOUNTS_RECEIVABLE).append("\n");
            sql.append(" and bt.transferType in (" + RECEIVE_MONEY + "," + CASH_RECEIPT +") \n");
        } else {
            sql.append(" and a.key = ").append(EdsAccount.ACCOUNTS_PAYABLE).append("\n");
            sql.append(" and bt.transferType in (" + SPEND_MONEY + "," + CASH_PAYMENT +") \n");
        }
        sql.append(" and bti.client_or_supplier_id = ").append(crmAccountId).append("\n");
        sql.append("group by bt.id, bt.currencyid, bt.date, bt.reference, a.id \n");

        return jdbcSpringManager.getSimpleJdbcTemplate().query(sql.toString(), BeanPropertyRowMapper.newInstance(TransactionAllocateItem.class));
    }

    @Override
    public List<EdsBankTransfer> getBankTransferList(ListingFilterParameter filterParametrs, String accountType) {
        Map<String, Object> param = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct bt from EdsBankTransfer bt ");
        sql.append("left join fetch bt.project p ");
        sql.append("left join fetch bt.items it ");

        sql.append(" where " + ServerUtils.checkForDeleted("bt.deleted"));
        if (filterParametrs.getType() != null) {
            sql.append(" and bt.transferType = ").append(filterParametrs.getType());
        }
        if (filterParametrs.getStartDate() != null && filterParametrs.getEndDate() != null) {
            sql.append(" and (bt.date between '" + filterParametrs.getStartDate() + "' and '" + filterParametrs.getEndDate() + "')\n");
        }
        if (filterParametrs.getProjectId() != null && filterParametrs.getProjectId() != 0) {
            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE)) {
                sql.append(" and (it.project.objectID in (:projectIDs) or it.project.parent.objectID=" + filterParametrs.getProjectId() + ") ");
                sql.append(" and ( it.account.accountType.category ='" + accountType + "' ) ");

            } else {
                sql.append(" and (bt.project.objectID in (:projectIDs) or bt.project.parent.objectID=" + filterParametrs.getProjectId() + ") ");
                sql.append(" and ( it.account.accountType.category ='" + accountType + "' ) ");
            }
            if (filterParametrs.getProjectIdList() != null && filterParametrs.getProjectIdList().size() > 0) {
                param.put("projectIDs", filterParametrs.getProjectIdList());
            } else {
                param.put("projectIDs", filterParametrs.getProjectId());
            }
        }

        return findByNamedParams(sql.toString(), param);
    }

    @Override
    public boolean isUsedForPayment(Integer bankTransferID) {
        return ((Long)findSingle("select count(inp.objectID) from EdsInvoicePayment inp left join inp.status s where (inp.deleted is false or inp.deleted is null) and (s.objectID is null or s.code = '" + EdsInvoicePayment.REVERSED + "') and inp.bankTransferID=?", bankTransferID)).intValue() > 0;
    }

    @Override
    public void mergeOldCrmAccountToNewOne(List<Integer> oldAccountIDs, Integer newAccountID) {
        updateNative("UPDATE " + getCompanyId() + ".spendreceivemoneyitem SET client_or_supplier_id = " + newAccountID + " WHERE client_or_supplier_id in (" + ServerUtils.getAsCommoDelimited(oldAccountIDs, "0", ",") + ")");
    }

    public boolean isNumberExists(String number, Integer objectID, Integer transferType) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> values = new HashMap<>();
        sql.append("select count(bt.objectID) from EdsBankTransfer bt where bt.transferType = :transferType and bt.number = :number and (bt.deleted is null or bt.deleted<>true) ");
        if (objectID != null) {
            sql.append(" and bt.objectID != :objectID");
            values.put("objectID", objectID);
        }
        values.put("transferType", transferType);
        values.put("number", number);

        Calendar financialYearStart = accountingManager.getFinancialYearStartIfEnabled(null);
        if (financialYearStart != null && financialYearStart.getTime().before(new Date())) {
            values.put("financialYearStart", financialYearStart.getTime());
            sql.append(" and bt.creationDate > :financialYearStart");
        }
        Long count = (Long) findSingleByNamedParams(sql.toString(),values);
        return count != null && count.intValue() > 0;
    }

    public Integer getLastIntNumber(Integer transferType) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> values = new HashMap<>();
        sql.append("select bt.intNumber from EdsBankTransfer bt where bt.transferType = :transferType and (bt.deleted is null or bt.deleted<>true) and bt.intNumber is not null ");
        values.put("transferType", transferType);
        Calendar financialYearStart = accountingManager.getFinancialYearStartIfEnabled(null);
        if (financialYearStart != null && financialYearStart.getTime().before(new Date())) {
            values.put("financialYearStart", financialYearStart.getTime());
            sql.append(" and bt.creationDate > :financialYearStart");
        }
        sql.append(" order by bt.intNumber desc");

        Integer lastIntNumber = (Integer) findSingleByNamedParams(sql.toString(), values);
        return lastIntNumber != null ? lastIntNumber : 0;
    }
}
