/**********************************************************************************************************************
 * LAST CHANGE                                                                                                        *
 * User: Hayot                                                                                                        *
 * Time: 2010/5/5 0:47:32                                                                                             *
 **********************************************************************************************************************/

package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.settings.EdsEmailSetting;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: Mar 19, 2010
 * Time: 4:53:29 PM
 * To change this template use File | Settings | File Templates.
 */
public interface EmailSettingsManager extends Manager<EdsEmailSetting> {

    EdsEmailSetting getCompanyEmailSetting(Integer companyID);

    EdsEmailSetting getEmailSetting(String email);

    EdsEmailSetting getActiveEmailSetting(String email);

    List<EdsEmailSetting> getAllActiveEmailSettings();

    List<String> getAllActiveEmails();

    void undefaultAccounts(Integer objectID, Integer userID);

    void undoCompanyEmails(Integer objectID);

    EdsEmailSetting getUserDefaultEmailAccount();

    EdsEmailSetting getUserEmailAccount();

    void updateFetchingTimes(Integer emailSettingID, Date fetchingStartDate, Date date);
}
