package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsUserBankAccount;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 04.08.2010
 * Time: 21:38:20
 * To change this template use File | Settings | File Templates.
 */
public interface UserBankAccountManager extends Manager<EdsUserBankAccount> {
    EdsUserBankAccount getUserBankAccountByUser(EdsUser user);

    EdsUserBankAccount getUserBankAccountByUserId(Integer userID);

    boolean isUserBankAccountExists(EdsUser user);

    List<EdsUserBankAccount> getUserBankAccountList();
}
