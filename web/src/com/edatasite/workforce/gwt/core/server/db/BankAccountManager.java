package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.accounting.EdsBankAccount;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 26.02.2009
 * Time: 19:43:52
 * To change this template use File | Settings | File Templates.
 */
public interface BankAccountManager extends Manager<EdsBankAccount> {
    EdsBankAccount getBankAccountByAccountID(Integer accountID);

    Integer getBankAccountTransactionsCount(Integer accountID);

    HashMap<String, EdsBankAccount> getBankAccountList(ListingFilterParameter filterParameter);
}
