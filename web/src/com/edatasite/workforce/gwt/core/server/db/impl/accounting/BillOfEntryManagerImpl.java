package com.edatasite.workforce.gwt.core.server.db.impl.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsBillOfEntry;
import com.edatasite.workforce.gwt.core.server.db.JdbcSpringManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.BillOfEntryManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: 07.05.2019
 * Time: 18:26:22
 * To change this template use File | Settings | File Templates.
 */
@Repository("billOfEntryManager")
public class BillOfEntryManagerImpl extends BaseManager<EdsBillOfEntry> implements BillOfEntryManager {

    public BillOfEntryManagerImpl() {
        super(EdsBillOfEntry.class);
    }

    /*@Autowired
    AccountingManager accountingManager;
*/
    @Autowired
    private JdbcSpringManager jdbcSpringManager;

    @Override
    public EdsBillOfEntry getBillOfEntryByPurchaseInvoiceId(Integer purchaseInvoiceId) {

        return null;

    }

    public List<Integer> deleteBillOfEntryItems(Integer billOfEntryID) {
        List<Integer> itemsDeleted = find("SELECT objectID FROM EdsBillOfEntryItem WHERE billofentry_id = ?", billOfEntryID);
        update("DELETE FROM EdsBillOfEntryItem WHERE billofentry_id = ?", billOfEntryID);
        return itemsDeleted;
    }

    /*@Override
    public List<EdsManualJournal> getManualJournals(ListingFilterParameter fp) {
        StringBuffer sql = new StringBuffer();
        sql.append("select mj from EdsManualJournal mj, EdsReference r ");
        sql.append(" where mj.deleted<>true ");
        sql.append(" and mj.overallStatus.objectID = r.objectID ");
        sql.append(" and r.code!=? ");
        addManualJournalsFilterQuery(fp, sql);

        String sortField = (fp != null ? fp.getSortField() : "");
        String ascOrDesc = (fp != null && fp.getSortDir() == 2) ? " desc" : " asc";
        if (ManualJournalListItem.NARRATION.equals(sortField)) {
            sql.append(" ORDER BY mj.narration " + ascOrDesc);
        } else if (ManualJournalListItem.DATE.equals(sortField)) {
            sql.append(" ORDER BY mj.date " + ascOrDesc);
        }*//* else if (ManualJournalListItem.DEBIT.equals(sortField)) {
            sql.append(" ORDER BY mj.debit " + ascOrDesc);
        } else if (ManualJournalListItem.CRETID.equals(sortField)) {
            sql.append(" ORDER BY mj.credit " + ascOrDesc);
        }*//* else if (ManualJournalListItem.REFERENCENUMBER.equals(sortField)) {
            sql.append(" ORDER BY mj.reference " + ascOrDesc);
        } else if (ManualJournalListItem.STATUS.equals(sortField)) {
            sql.append(" ORDER BY r.code " + ascOrDesc);
        } else {
            sql.append(" ORDER BY mj.objectID desc ");
        }
        return findInterval(sql.toString(), fp.getStart(), fp.getLimit(), EdsManualJournal.REVERSED);
    }

    @Override
    public Integer getManualJournalsCount(ListingFilterParameter fp) {
        String schema = ServerSecurityContext.getInstance().getCompanyId();
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT distinct count(mj.id) FROM \"" + schema + "\".manualjournal mj ");
        sql.append(" LEFT JOIN  \"" + schema + "\".reference r on r.id = mj.overallStatus ");
        *//*sql.append(" LEFT JOIN  \""+schema+"\".manualjournalitem mji ");
        sql.append(" ON mj.id = mji.manualjournalid ");*//*
        sql.append(" WHERE mj.deleted is not true AND (mj.overallStatus is null OR r.code!='" + EdsManualJournal.REVERSED + "')  ");

        addManualJournalsFilterQuery(fp, sql);

//        sql.append(" GROUP BY mj.id ,mj.narration,mj.date,mj.status,mj.reference ");

        BigInteger totalCount = (BigInteger) findNativeSingle(sql.toString());
        return totalCount != null ? totalCount.intValue() : 0;
    }*/
/*

    private void addManualJournalsFilterQuery(ListingFilterParameter fp, StringBuffer sql) {
        if (fp.getStartDate() != null && fp.getEndDate() != null) {
            String fromDate = ServerUtils.getDateAsString(fp.getStartDate(), true);
            String toDate = ServerUtils.getDateAsString(fp.getEndDate(), true);
            sql.append(" AND mj.date between '" + fromDate + "' and '" + toDate + "' ");
        }
        if (fp.getStatusCode() != null) {
            sql.append(" AND r.code = '" + fp.getStatusCode() + "'");
        }
        if (fp.getViewType() != null) {
            if (Constants.RECURRING.equals(fp.getViewType())) {
                sql.append(" AND (mj.recurringTemplate is null or mj.recurringTemplate is true)");
            }
            if (Constants.SINGLE.equals(fp.getViewType())) {
                sql.append(" AND mj.recurringTemplate is false");
            }
        }
        if (fp.getEmployeeId() != null && -1 != fp.getEmployeeId()) {
            sql.append(" AND mj.creatorId = " + fp.getEmployeeId());
        }
        String sqlSearchKey = fp.getSqlSearchKey();
        if (sqlSearchKey != null) {
            sql.append("AND ( ");
            sql.append(" lower(mj.narration) like '" + sqlSearchKey + "' ");
            sql.append(" OR lower(mj.number) like '" + sqlSearchKey + "' ");
            sql.append(" OR lower(mj.reference) like '" + sqlSearchKey + "' ");
            sql.append(" OR lower(r.code) like '" + sqlSearchKey + "' ");
            sql.append(" ) ");
        }
    }

    public void deleteJournalItems(EdsManualJournal manualJournal) {
        update("delete from EdsManualJournalItem mji where mji.manualJournal = ?", manualJournal);
    }

    @Override
    public List<EdsManualJournal> getMemorizedTransactions(ListingFilterParameter filterParametrs) {
        StringBuilder sql = new StringBuilder();
        sql.append("select mj from EdsManualJournal mj where mj.isMemorizedTransaction = true and " + ServerUtils.checkForDeleted("mj.deleted"));
        String sqlSearchKey = filterParametrs.getSqlSearchKey();
        if (sqlSearchKey != null) {
            sql.append(" and lower(mj.narration) like '" + sqlSearchKey + "' ");
        }
        sql.append(" ORDER BY mj.narration");
        return find(sql.toString());
    }

    @Override
    public List<TransactionAllocateItem> getManualTransactionsByCrmAccount(Integer clientSupplierID, boolean isClient, boolean fromApplyCredit, Date date, FindMatchFilterData filterData, Integer currencyID) {
        ArrayList<Integer> IdList = new ArrayList<>();

        if (clientSupplierID != null) {
            IdList.add(clientSupplierID);
        }
        return getManualTransactionsByCrmAccount(IdList, isClient, fromApplyCredit, date, filterData, currencyID);
    }

    @Override
    public List<TransactionAllocateItem> getManualTransactionsByCrmAccount(List<Integer> clientSupplierIds, boolean isClient, boolean fromApplyCredit, Date date, FindMatchFilterData filterData, Integer currencyID) {
        StringBuffer sql = new StringBuffer();
        List<TransactionAllocateItem> result = new ArrayList<>();
        String schema = ServerSecurityContext.getInstance().getCompanyId();
        if (fromApplyCredit) {
            if (isClient) {
                sql.append("select mj.id as objectID, max(mj.currencyid) as currencyID, max(narration) as narration, max(mj.date) as date, sum(mji.credit) as amount, max(mj.reference) as reference, a.id accountID, ca.id as crmAccountID, ca.name as crmAccountName from \"" + schema + "\".manualjournal mj ");
                sql.append("inner join \"" + schema + "\".manualjournalitem mji on mji.manualjournalid=mj.id ");
                sql.append("inner join ").append(getCompanyId()).append(".account a on a.id = mji.accountid ");
                sql.append("inner join ").append(getCompanyId()).append(".reference r on r.id = mj.overallStatus ");
                sql.append("inner join ").append(getCompanyId()).append(".crmaccount ca on ca.id = mji.client_or_supplier_id ");
                sql.append("where " + ServerUtils.checkForDeleted("mj.deleted") + " and r.code='" + EdsManualJournal.POST + "' and (a.key =" + EdsAccount.ACCOUNTS_RECEIVABLE + " or a.groupKey =" + EdsAccount.ACCOUNTS_RECEIVABLE + ") ");
                sql.append(" and mji.client_or_supplier_id in (" + ServerUtils.getAsCommoDelimited(clientSupplierIds, "0") + ") ");
                sql.append(" and mji.credit is not null ");
                sql.append(date != null ? " and to_date(to_char(mj.date, 'yyyy-MM-dd'),'yyyy-MM-dd') <= '" + ServerUtils.dateFormat.format(date) + "' " : "");
                sql.append("group by mj.id, a.id, ca.id, ca.name");
            } else {
                sql.append("select mj.id as objectID, max(mj.currencyid) as currencyID, max(narration) as narration, max(mj.date) as date, sum(mji.debit) as amount, max(mj.reference) as reference, a.id accountID, ca.id as crmAccountID, ca.name as crmAccountName from \"" + schema + "\".manualjournal mj ");
                sql.append("inner join \"" + schema + "\".manualjournalitem mji on mji.manualjournalid=mj.id ");
                sql.append("inner join ").append(getCompanyId()).append(".account a on a.id = mji.accountid ");
                sql.append("inner join ").append(getCompanyId()).append(".reference r on r.id = mj.overallStatus ");
                sql.append("inner join ").append(getCompanyId()).append(".crmaccount ca on ca.id = mji.client_or_supplier_id ");
                sql.append("where " + ServerUtils.checkForDeleted("mj.deleted") + " and r.code='" + EdsManualJournal.POST + "' and (a.key =" + EdsAccount.ACCOUNTS_PAYABLE + " or a.groupKey =" + EdsAccount.ACCOUNTS_PAYABLE + ") ");
                sql.append(" and mji.client_or_supplier_id in (" + ServerUtils.getAsCommoDelimited(clientSupplierIds, "0") + ") ");
                sql.append(" and mji.debit is not null ");
                sql.append(date != null ? " and to_date(to_char(mj.date, 'yyyy-MM-dd'),'yyyy-MM-dd') <= '" + ServerUtils.dateFormat.format(date) + "' " : "");
                sql.append("group by mj.id, a.id, ca.id, ca.name");
            }
        } else {
            if (isClient) {
                sql.append("select tb.* from (");
                sql.append("select mj.id as objectID, max(mj.currencyid) as currencyID, max(narration) as narration, max(mj.date) as date, sum(mji.debit) as amount, max(mj.reference) as reference, a.id accountID, ca.id as crmAccountID, ca.name as crmAccountName from \"" + schema + "\".manualjournal mj \n");
                sql.append("inner join ").append(getCompanyId()).append(".manualjournalitem mji on mji.manualjournalid=mj.id \n");
                sql.append("inner join ").append(getCompanyId()).append(".account a on a.id = mji.accountid \n");
                sql.append("inner join ").append(getCompanyId()).append(".reference r on r.id = mj.overallStatus ");
                sql.append("inner join ").append(getCompanyId()).append(".crmaccount ca on ca.id = mji.client_or_supplier_id ");
                sql.append("where " + ServerUtils.checkForDeleted("mj.deleted") + " and r.code='" + EdsManualJournal.POST + "'  and mji.client_or_supplier_id in (" + ServerUtils.getAsCommoDelimited(clientSupplierIds, "0") + ") ");
                sql.append(" and (a.key = ").append(EdsAccount.ACCOUNTS_RECEIVABLE).append(" or a.groupKey = ").append(EdsAccount.ACCOUNTS_RECEIVABLE).append(") \n");
                sql.append(" and mji.debit is not null ");

                if (filterData.isMultiEnabled() && filterData.isPaymentDiffCurrency()) {
                    sql.append(" and a.currencyid in (").append(currencyID + "," + filterData.getBaseCurrencyID()).append(") \n");
                } else if (!filterData.isMultiEnabled() && currencyID != null) {
                    sql.append(" and a.currencyid = ").append(currencyID).append(" \n");
                }
                sql.append("group by mj.id, a.id, ca.id, ca.name");
                sql.append(") tb ");
                applyManualTransactionFilter(sql, filterData);

            } else {
                sql.append("select tb.* from (");
                sql.append("select mj.id as objectID, max(mj.currencyid) as currencyID, max(narration) as narration, max(mj.date) as date, sum(mji.credit) as amount, max(mj.reference) as reference, a.id accountID, ca.id as crmAccountID, ca.name as crmAccountName from \"" + schema + "\".manualjournal mj \n");
                sql.append("inner join ").append(getCompanyId()).append(".manualjournalitem mji on mji.manualjournalid=mj.id \n");
                sql.append("inner join ").append(getCompanyId()).append(".account a on a.id = mji.accountid \n");
                sql.append("inner join ").append(getCompanyId()).append(".reference r on r.id = mj.overallStatus ");
                sql.append("inner join ").append(getCompanyId()).append(".crmaccount ca on ca.id = mji.client_or_supplier_id ");
                sql.append("where " + ServerUtils.checkForDeleted("mj.deleted") + " and r.code='" + EdsManualJournal.POST + "' and mji.client_or_supplier_id in (" + ServerUtils.getAsCommoDelimited(clientSupplierIds, "0") + ") ");
                sql.append(" and mji.credit is not null ");
                sql.append(" and (a.key = ").append(EdsAccount.ACCOUNTS_PAYABLE).append(" or a.groupKey = ").append(EdsAccount.ACCOUNTS_PAYABLE).append(") \n");

                if (filterData.isMultiEnabled() && filterData.isPaymentDiffCurrency()) {
                    sql.append(" and a.currencyid in (").append(currencyID + "," + filterData.getBaseCurrencyID()).append(") \n");
                } else if (!filterData.isMultiEnabled() && currencyID != null) {
                    sql.append(" and a.currencyid = ").append(currencyID).append(" \n");
                }
                sql.append("group by mj.id, a.id, ca.id, ca.name");
                sql.append(") tb ");
                applyManualTransactionFilter(sql, filterData);
            }
        }
        result.addAll(jdbcSpringManager.getSimpleJdbcTemplate().query(sql.toString(), BeanPropertyRowMapper.newInstance(TransactionAllocateItem.class)));
        return result;
    }

    private void applyManualTransactionFilter(StringBuffer sql, FindMatchFilterData filterData) {
        sql.append(" where 1=1 ");
        if (filterData.getSearchKey() != null && !"".equals(filterData.getSearchKey())) {
            sql.append(" and lower(narration) like '%" + filterData.getSearchKey().toLowerCase() + "%'");
        }
        if (filterData.getStartAmount() != null) {
            sql.append(" and amount >=" + filterData.getStartAmount());
        }
        if (filterData.getEndAmount() != null) {
            sql.append(" and amount <=" + filterData.getEndAmount());
        }
        if (filterData.getStartDate() != null && filterData.getStartDate().getNonConvertedDate() != null) {
            sql.append(" and to_date(to_char(date,'yyyy-MM-dd'),'yyyy-MM-dd') >='" + ServerUtils.dateFormat.format(filterData.getStartDate().getNonConvertedDate()) + "'");
        }
        if (filterData.getEndDate() != null && filterData.getEndDate().getNonConvertedDate() != null) {
            sql.append(" and to_date(to_char(date,'yyyy-MM-dd'),'yyyy-MM-dd') <='" + ServerUtils.dateFormat.format(filterData.getEndDate().getNonConvertedDate()) + "'");
        }
        */
/*if (filterData.getSortField() != null) {
            if (AccountingConstants.DESCRIPTION_COLUMN.equals(filterData.getSortField())) {
                sql.append(" order by narration ").append(filterData.getSortDirection());
            } else if (AccountingConstants.DATE_COLUMN.equals(filterData.getSortField())) {
                sql.append(" order by date ").append(filterData.getSortDirection());
            } else if (AccountingConstants.AMOUNT_COLUMN.equals(filterData.getSortField())) {
                sql.append(" order by amount ").append(filterData.getSortDirection());
            }
        }*//*

    }

    @Override
    public boolean isUsedForPayments(Integer manualJournalID) {
        boolean result = ((Long)findSingle("select count(csp.objectID) from EdsCustomerSupplierPayment csp where (csp.deleted is false or csp.deleted is null) and csp.manualJournalId=?", manualJournalID)).intValue() > 0;
        if (result) {
            return result;
        }
        return ((Long)findSingle("select count(inp.objectID) from EdsInvoicePayment inp left join inp.status s where (inp.deleted is false or inp.deleted is null) and (s.objectID is null or s.code != '" + EdsInvoicePayment.REVERSED + "') and inp.manualJournalID=?", manualJournalID)).intValue() > 0;
    }

    @Override
    public List<TransactionAllocateItem> getManualTransactionsByCrmAccount(Integer accountID, String accountType) {
        List<TransactionAllocateItem> result = new ArrayList<>();
        EdsAccount account;
        if (EdsCrmAccount.CUSTOMER.equals(accountType)) {
            account = accountingManager.getAccountByKey(EdsAccount.ACCOUNTS_RECEIVABLE);
        } else {
            account = accountingManager.getAccountByKey(EdsAccount.ACCOUNTS_PAYABLE);
        }
        if (account != null) {
            StringBuilder sql = new StringBuilder();
            String schema = ServerSecurityContext.getInstance().getCompanyId();
            sql.append("select * from \"" + schema + "\".manualjournal mj ");
            sql.append("left join \"" + schema + "\".manualjournalitem mji on mji.manualjournalid=mj.id ");
            sql.append("where " + ServerUtils.checkForDeleted("mj.deleted") + " and mji.client_or_supplier_id=" + accountID + " and mji.accountid=" + account.getObjectID());
            result.addAll(jdbcSpringManager.getSimpleJdbcTemplate().query(sql.toString(), BeanPropertyRowMapper.newInstance(TransactionAllocateItem.class)));
        }
        return result;
    }

    @Override
    public List<Object[]> getManualJournalList(ListingFilterParameter fp) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT mj.id, mj.narration, mj.date, SUM(coalesce(mji.credit, 0)) AS credit, SUM(coalesce(mji.debit, 0)) AS debit," +
                " r.code as status, mj.reference," +
                " mj.recurringTemplate," +
                " mj.number,array_to_string(array_agg(coalesce((case when  p.number is not null then null else '' end),  p.number || ' -> ') ||  (p.name)), ', ') project, " +
                " (u.firstname ||' '|| u.lastname) creator ");
        sql.append(" FROM ").append(getCompanyId()).append(".manualjournal mj ");
        sql.append(" LEFT JOIN ").append(getCompanyId()).append(".reference r ON r.id = mj.overallStatus");
        sql.append(" LEFT JOIN ").append(getCompanyId()).append(".manualjournalitem mji ON mj.id = mji.manualjournalid");
        sql.append(" LEFT JOIN ").append(getCompanyId()).append(".project p on mji.project_id = p.id ");
        sql.append(" LEFT JOIN ").append(getCompanyId()).append(".myuser u on mj.creatorId = u.id ");
        sql.append(" WHERE mj.deleted is not true AND (mj.overallStatus is null OR r.code!='" + EdsManualJournal.REVERSED + "')  ");

        addManualJournalsFilterQuery(fp, sql);

        sql.append(" GROUP BY mj.id ,mj.narration,mj.date,r.code,mj.recurringTemplate,mj.reference,mj.number,creator ");

        String sortField = (fp != null ? fp.getSortField() : "");
        String ascOrDesc = (fp != null && fp.getSortDir() == 2) ? " desc" : " asc";
        if (ManualJournalListItem.NARRATION.equals(sortField)) {
            sql.append(" ORDER BY mj.narration " + ascOrDesc);
        } else if (ManualJournalListItem.DATE.equals(sortField)) {
            sql.append(" ORDER BY mj.date " + ascOrDesc);
        } else if (ManualJournalListItem.DEBIT.equals(sortField)) {
            sql.append(" ORDER BY debit " + ascOrDesc);
        } else if (ManualJournalListItem.CRETID.equals(sortField)) {
            sql.append(" ORDER BY credit " + ascOrDesc);
        } else if (ManualJournalListItem.REFERENCENUMBER.equals(sortField)) {
            sql.append(" ORDER BY mj.reference " + ascOrDesc);
        } else if (ManualJournalListItem.STATUS.equals(sortField)) {
            sql.append(" ORDER BY r.code " + ascOrDesc);
        } else if (ManualJournalListItem.NUMBER.equals(sortField)) {
            sql.append(" ORDER BY mj.number " + ascOrDesc);
        } else if (ManualJournalListItem.CREATOR.equals(sortField)) {
            sql.append(" ORDER BY creator " + ascOrDesc);
        } else {
            sql.append(" ORDER BY mj.id desc ");
        }

        return findNativeLimited(sql.toString() + " offset " + fp.getStart(), fp.getLimit());
    }

    @Override
    public TransactionAllocateItem getPaidManualTransaction(Integer objectID, Integer clientSupplierID, Integer accountID, boolean isReceivable) {
        StringBuilder sql = new StringBuilder();
        sql.append("select mj.id objectID, mj.reference, mj.narration, mji.accountid, ").append(isReceivable ? "sum(coalesce(mji.debit,0)) amount " : "sum(coalesce(mji.credit,0)) amount ")
                .append(", mj.currencyid, mj.exchangerate, mj.date  from ").append(getCompanyId()).append(".manualjournal mj \n");
        sql.append("inner join ").append(getCompanyId()).append(".manualjournalitem mji on mji.manualjournalid = mj.id \n");
        sql.append("where mj.id = ").append(objectID).append(" \n");
        sql.append("and mji.client_or_supplier_id = ").append(clientSupplierID).append(" \n");
        sql.append("and mji.accountid = ").append(accountID).append(" \n");
        sql.append("group by mj.id, mj.reference, mj.narration, mji.accountid, mj.currencyid, mj.exchangerate, mj.date \n");

//        TransactionAllocateItem item = jdbcSpringManager.getSimpleJdbcTemplate().queryForObject(sql.toString(), new HashMap<String, Object>(), BeanPropertyRowMapper.newInstance(TransactionAllocateItem.class));

        List<TransactionAllocateItem> list = jdbcSpringManager.getSimpleJdbcTemplate().query(sql.toString(), BeanPropertyRowMapper.newInstance(TransactionAllocateItem.class));
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public void mergeOldCrmAccountToNewOne(List<Integer> oldAccountIDs, Integer newAccountID) {
        updateNative("UPDATE " + getCompanyId() + ".manualjournalitem SET client_or_supplier_id = " + newAccountID + " WHERE client_or_supplier_id in (" + ServerUtils.getAsCommoDelimited(oldAccountIDs, "0", ",") + ")");
    }

    @Override
    public Integer getMTLastIntNumber() {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> values = new HashMap<>();
        sql.append("select mj.intNumber from EdsManualJournal mj where mj.deleted=false and mj.intNumber is not null ");
        Calendar financialYearStart = accountingManager.getFinancialYearStartIfEnabled(null);

        if (financialYearStart != null && financialYearStart.getTime().before(new Date())) {
            values.put("financialYearStart", financialYearStart.getTime());
            sql.append(" and mj.date >= :financialYearStart");
        }
        sql.append(" order by mj.intNumber desc");
        return (Integer) findSingleByNamedParams(sql.toString(), values);
    }

    @Override
    public boolean isDuplicateMTNumber(String numberString, Integer manualJournalObjectID, Date date) {

        if (numberString != null && !"".equals(numberString.trim())) {
            StringBuilder sql = new StringBuilder();
            Map<String, Object> values = new HashMap<>();
            Calendar financialYearStart = accountingManager.getFinancialYearStartIfEnabled(date);

            if (financialYearStart != null && financialYearStart.getTime().before(new Date())) {
                Calendar financialYearEnd = new GregorianCalendar();
                financialYearEnd.setTime(financialYearStart.getTime());
                financialYearEnd.set(Calendar.YEAR, financialYearEnd.get(Calendar.YEAR) + 1);

                values.put("financialYearStart", financialYearStart.getTime());
                values.put("financialYearEnd", financialYearEnd.getTime());
            }

            if (manualJournalObjectID == null) {
                sql.append("select mj.objectID from EdsManualJournal mj where (mj.deleted is null or mj.deleted<>true) and mj.number = :number");
                values.put("number", numberString);

                if (values.get("financialYearStart") != null && values.get("financialYearEnd") != null) {
                    sql.append(" and mj.date between :financialYearStart and :financialYearEnd ");
                }


                return findByNamedParams(sql.toString(), values).size() > 0;
            } else {
                sql.append("select mj.objectID from EdsManualJournal mj where (mj.deleted is null or mj.deleted<>true) and mj.number = :number and mj.objectID <> :manualJournalObjectID");

                if (values.get("financialYearStart") != null && values.get("financialYearEnd") != null) {
                    sql.append(" and mj.date between :financialYearStart and :financialYearEnd ");
                }
                values.put("number", numberString);
                values.put("manualJournalObjectID", manualJournalObjectID);

                return findByNamedParams(sql.toString(), values).size() > 0;
            }
        }
        return false;
    }
*/

}
