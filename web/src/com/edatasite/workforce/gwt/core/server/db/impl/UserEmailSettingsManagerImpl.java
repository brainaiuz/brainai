package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.EdsUserEmailSettings;
import com.edatasite.workforce.gwt.core.server.db.UserEmailSettingsManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: Apr 2, 2010
 * Time: 4:36:24 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("userEmailSettingsManager")
public class UserEmailSettingsManagerImpl extends BaseManager<EdsUserEmailSettings> implements UserEmailSettingsManager {

    public UserEmailSettingsManagerImpl() {
        super(EdsUserEmailSettings.class);
    }

    @Transactional
    public EdsUserEmailSettings getUserSettings(EdsUser user) {
        EdsUserEmailSettings emailSettings = (EdsUserEmailSettings) findSingle("select es from EdsUserEmailSettings es where es.user.objectID = ?", user.getObjectID());
        if (emailSettings == null || emailSettings.isNew()) {
            emailSettings = new EdsUserEmailSettings();
            emailSettings.setUser(user);
            String defaultLocale = "";
            if (user.getCompany().getCreator() != null) {
                defaultLocale = (String) findSingle("select es.internationalization from EdsUserEmailSettings es where es.user = ?", user.getCompany().getCreator());
            } else {
                defaultLocale = (String) findSingle("select es.internationalization from EdsUserEmailSettings es ");
            }
            emailSettings.setInternationalization(defaultLocale);
            create(emailSettings);
        }
        return emailSettings;
    }

    @Override
    public void updateUserSettings(String language) {
        updateNative("update " + getCompanyId() + ".userEmailSettings set internationalization='" + language + "'");
    }

    @Transactional
    public Integer getUserId(Integer fid, String companyUniqueID) {
        String strfid = String.valueOf(fid);
        if (companyUniqueID != null && !"".equals(companyUniqueID)) {
            return (Integer) findSingle("SELECT es.user.objectID FROM EdsUserFingerPrintDevice es WHERE es.fingerprintId=? and es.deviceId=?", strfid, companyUniqueID);
        }
        return (Integer) findSingle("SELECT es.user.objectID FROM EdsUserFingerPrintDevice es WHERE es.fingerprintId=?", strfid);
    }
}
