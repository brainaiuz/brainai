package com.edatasite.workforce.gwt.core.server.db.settings;

import com.edatasite.workforce.core.domain.settings.EdsGenericSettings;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 6/3/11
 * Time: 6:01 PM
 * To change this template use File | Settings | File Templates.
 */

public interface GenericSettingsManager extends Manager<EdsGenericSettings> {

    String getValueByKey(GenericSettingsEnum key);

    boolean isSettingsEnabled(GenericSettingsEnum key);

    boolean isSettingsEnabled(Integer companyId, GenericSettingsEnum key);

    void saveGenericSettings(Integer companyId, GenericSettingsEnum key, String value);

    Set<GenericSettingsEnum> getEnabledGenericSettings();

    List<String> getEnabledGenericSettings(Integer companyID);

    Set<GenericSettingsEnum> getByKeys(GenericSettingsEnum... keys);

    EdsGenericSettings getByKey(GenericSettingsEnum key);

    boolean exists(GenericSettingsEnum key);
}
