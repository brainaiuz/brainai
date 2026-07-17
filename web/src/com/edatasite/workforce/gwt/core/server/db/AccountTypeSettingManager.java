package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.accounting.EdsAccountNumberSettings;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: 10/22/11
 * Time: 7:31 PM
 * To change this template use File | Settings | File Templates.
 */
public interface AccountTypeSettingManager extends Manager<EdsAccountNumberSettings> {

    EdsAccountNumberSettings getNumberSetting(Integer accountTypeID);

    String generateNewAccountNumberByAccountType(Integer startNumberingRange, Integer endNumberingRange);

    String getGeneratedAccountNumber(Integer accountTypeID);
}
