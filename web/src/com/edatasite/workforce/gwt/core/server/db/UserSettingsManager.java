package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.EdsUserSettings;
import com.edatasite.workforce.gwt.core.client.enums.UserSettingsTypeEnum;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Apr 17, 2018
 * Time: 4:34:56 PM
 * To change this template use File | Settings | File Templates.
 */
public interface UserSettingsManager extends Manager<EdsUserSettings> {

    List<EdsUserSettings> getUserSettings(EdsUser user);

    EdsUserSettings getUserSettingsValue(EdsUser user, UserSettingsTypeEnum type, String key);

    EdsUserSettings getUserSettingsValue(String key);

}
