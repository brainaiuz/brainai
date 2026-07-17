package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.accounting.EdsAccountNumberSettings;
import com.edatasite.workforce.gwt.core.server.db.AccountTypeSettingManager;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: 10/22/11
 * Time: 7:32 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("accountTypeSettingManager")
public class AccountTypeSettingManagerImpl extends BaseManager<EdsAccountNumberSettings> implements AccountTypeSettingManager {

    public AccountTypeSettingManagerImpl() {
        super(EdsAccountNumberSettings.class);
    }

    @Override
    public EdsAccountNumberSettings getNumberSetting(Integer accountTypeID) {
        return (EdsAccountNumberSettings)findSingle("SELECT ns FROM EdsAccountNumberSettings ns WHERE ns.accountType.objectID = ?", accountTypeID);
    }

    @Override
    public String generateNewAccountNumberByAccountType(Integer startNumberingRange, Integer endNumberingRange) {
        String schema = ServerSecurityContext.getInstance().getCompanyId();
        Object object = findNativeSingle("SELECT \"" + schema + "\".generateAccountNumber(?, ?)", startNumberingRange, endNumberingRange);
        return (String)object;
    }

    @Override
    public String getGeneratedAccountNumber(Integer accountTypeID) {
        EdsAccountNumberSettings numberSetting = getNumberSetting(accountTypeID);
        return generateNewAccountNumberByAccountType(numberSetting.getStart(), numberSetting.getEnd());
    }
}
