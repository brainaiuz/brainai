package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.EdsUserEmailSettings;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: Apr 2, 2010
 * Time: 4:34:56 PM
 * To change this template use File | Settings | File Templates.
 */
public interface UserEmailSettingsManager extends Manager<EdsUserEmailSettings> {

    EdsUserEmailSettings getUserSettings(EdsUser user);

    void updateUserSettings(String language);

    Integer getUserId(Integer fid, String companyUniqueID);

}
