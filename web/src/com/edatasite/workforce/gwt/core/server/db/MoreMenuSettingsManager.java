package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsMoreMenuSettings;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 14.01.2011
 * Time: 15:24:45
 * To change this template use File | Settings | File Templates.
 */
public interface MoreMenuSettingsManager extends Manager<EdsMoreMenuSettings> {

    EdsMoreMenuSettings getMoreMenuSettings(String actionName);

    EdsMoreMenuSettings getMoreMenuSettings(String actionName, Integer companyID);

    List<EdsMoreMenuSettings> getMoreMenuSettingItems(Integer companyID);
}
