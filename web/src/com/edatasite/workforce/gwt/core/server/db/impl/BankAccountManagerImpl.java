package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.accounting.EdsBankAccount;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.BankAccountManager;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 26.02.2009
 * Time: 19:46:35
 * To change this template use File | Settings | File Templates.
 */
@Repository("bankAccountManager")
public class BankAccountManagerImpl extends BaseManager<EdsBankAccount> implements BankAccountManager {

    public BankAccountManagerImpl() {
        super(EdsBankAccount.class);
    }

    public EdsBankAccount getBankAccountByAccountID(Integer accountID) {
        return (EdsBankAccount) findSingle("select ba from EdsBankAccount ba where ba.account.objectID = ?", accountID);
    }

    @Override
    public Integer getBankAccountTransactionsCount(Integer accountID) {
        StringBuilder sql = new StringBuilder();
        String schema = ServerSecurityContext.getInstance().getCompanyId();
        sql.append("select t.* from \"" + schema + "\".transaction t ");
        sql.append("left join \"" + schema + "\".transactionitem ti on ti.transactionid=t.id ");
        sql.append("where ti.accountid=" + accountID);
        sql.append(" and " + ServerUtils.checkForDeleted("t.deleted"));
        return findNative(sql.toString()).size();
    }

    @Override
    public HashMap<String, EdsBankAccount> getBankAccountList(ListingFilterParameter filterParameter) {
        HashMap<String, EdsBankAccount> bankAccountsMap = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("select ba.*, 0 as clazz_ from ").append(getCompanyId()).append(".bankAccount ba ");
        sql.append(" left join ").append(getCompanyId()).append(".account ac on ba.accountid = ac.id ");
        sql.append(" left join ").append(getPublic()).append(".accountType at on ac.accountTypeId = at.id ");
        sql.append(" where ").append(ServerUtils.checkForDeleted("ac.deleted"));
        if (filterParameter.getAccountType() != null) {
            sql.append(" and at.code = '").append(filterParameter.getAccountType()).append("'");
        }

        List<EdsBankAccount> accountList = findNative(sql.toString(), EdsBankAccount.class);
        if (accountList != null && accountList.size() > 0) {
            if (filterParameter.isWithCode()) {
                for (EdsBankAccount bankAccount : accountList) {
                    bankAccountsMap.put(bankAccount.getAccount() != null ? bankAccount.getAccount().getAccountCode().trim() : null, bankAccount);
                }
            } else {
                for (EdsBankAccount bankAccount : accountList) {
                    bankAccountsMap.put(bankAccount.getAccount() != null ? bankAccount.getAccount().getName().trim() : null, bankAccount);
                }
            }
        }
        return bankAccountsMap;
    }
}
