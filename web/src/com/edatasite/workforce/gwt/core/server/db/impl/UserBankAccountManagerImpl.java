package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsUserBankAccount;
import com.edatasite.workforce.gwt.core.server.db.UserBankAccountManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 04.08.2010
 * Time: 21:37:12
 * To change this template use File | Settings | File Templates.
 */
@Repository("userBankAccountManager")
public class UserBankAccountManagerImpl extends BaseManager<EdsUserBankAccount> implements UserBankAccountManager {
    public UserBankAccountManagerImpl() {
        super(EdsUserBankAccount.class);
    }

    public EdsUserBankAccount getUserBankAccountByUser(EdsUser user) {
        return (EdsUserBankAccount) findSingle("from EdsUserBankAccount usb where usb.user.objectID = ?", user.getObjectID());
    }

    public EdsUserBankAccount getUserBankAccountByUserId(Integer userId) {
        return (EdsUserBankAccount) findSingle("from EdsUserBankAccount usb where usb.user.objectID = ?", userId);
    }

    public boolean isUserBankAccountExists(EdsUser user) {
        return find("select uba from EdsUserBankAccount uba where uba.user = ?", user).size() > 0;
    }

    @Override
    public List<EdsUserBankAccount> getUserBankAccountList() {
        return find("select uba from EdsUserBankAccount uba");
    }
}
