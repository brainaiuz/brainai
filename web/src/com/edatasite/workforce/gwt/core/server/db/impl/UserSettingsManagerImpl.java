package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.EdsUserSettings;
import com.edatasite.workforce.gwt.core.client.enums.UserSettingsTypeEnum;
import com.edatasite.workforce.gwt.core.server.db.UserSettingsManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: Apr 17, 2018
 * Time: 4:36:24 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("userSettingsManager")
public class UserSettingsManagerImpl extends BaseManager<EdsUserSettings> implements UserSettingsManager {

    public UserSettingsManagerImpl() {
        super(EdsUserSettings.class);
    }

    @Transactional
    public List<EdsUserSettings> getUserSettings(EdsUser user) {
        return  (List<EdsUserSettings>) find("select es from EdsUserSettings es where es.user = ?", user);
    }

    @Transactional
    public EdsUserSettings getUserSettingsValue(EdsUser user, UserSettingsTypeEnum type, String key) {
        if (type != null && StringUtils.isNotBlank(key) && user!=null) {
            return (EdsUserSettings) findSingle("SELECT es FROM EdsUserSettings es WHERE es.user=? and es.type=? AND es.key=?", user, type, key);
        }
        return null;
    }

    public EdsUserSettings getUserSettingsValue(String key) {
        return (EdsUserSettings) findSingle("select s from EdsUserSettings s where s.user=? and s.key=?", getUser(), key);
    }

}
