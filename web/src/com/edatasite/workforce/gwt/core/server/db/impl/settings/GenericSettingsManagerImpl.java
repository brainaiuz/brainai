package com.edatasite.workforce.gwt.core.server.db.impl.settings;

import com.edatasite.workforce.core.domain.settings.EdsGenericSettings;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 6/3/11
 * Time: 5:58 PM
 * To change this template use File | Settings | File Templates.
 */

@Repository("genericSettingsManager")
public class GenericSettingsManagerImpl extends BaseManager<EdsGenericSettings> implements GenericSettingsManager {

    public GenericSettingsManagerImpl() {
        super(EdsGenericSettings.class);
    }

    @Override
    public String getValueByKey(GenericSettingsEnum key) {
        return (String) findSingle("select value from EdsGenericSettings where key = ?", key);
    }

    @Override
    public boolean isSettingsEnabled(GenericSettingsEnum key) {
        String value = getValueByKey(key);
        return EdsGenericSettings.YES.equals(value);
    }

    @Override
    public boolean isSettingsEnabled(Integer companyId, GenericSettingsEnum key) {
        String value = (String) findNativeSingle("select value from \"" + companyId + "\".genericSettings where key='" + key + "'");
        return EdsGenericSettings.YES.equals(value);
    }

    @Override
    public void saveGenericSettings(Integer companyId, GenericSettingsEnum key, String value) {
        companyId = companyId == null ? SecurityContext.getCompanyID() : companyId;
        String str = (String) findNativeSingle("select value from \"" + companyId + "\".genericSettings where key='" + key + "'");
        if (str != null) {
            updateNative("update \"" + companyId + "\".genericSettings set value='" + value + "' where key='" + key + "'");
        } else {
            updateNative("insert into \"" + companyId + "\".genericSettings (key,value) values('" + key + "', '" + value + "')");
        }
    }

    @Override
    public Set<GenericSettingsEnum> getEnabledGenericSettings() {
        final String sql = "select gs.key from EdsGenericSettings gs" +
                           "    where gs.value=:settingsValue";
        return Sets.newHashSet(this.slaveEntityManager.createQuery(sql, GenericSettingsEnum.class)
                                                 .setParameter("settingsValue", EdsGenericSettings.YES)
                                                 .getResultList());
    }

    @Override
    public List<String> getEnabledGenericSettings(Integer companyID) {
        StringBuilder sql = new StringBuilder();
        sql.append("select gs.key from \"").append(companyID).append("\".genericSettings gs \n");
        sql.append("where gs.value = '").append(EdsGenericSettings.YES).append("' \n");
        return (List<String>) slaveEntityManager.createNativeQuery(sql.toString()).getResultList();
    }

    @Override
    public boolean exists(GenericSettingsEnum key) {
        return findSingle("select objectID from EdsGenericSettings where key = ?", key) != null;
    }

    @Override
    public Set<GenericSettingsEnum> getByKeys(GenericSettingsEnum... keys) {
        if (keys == null || keys.length == 0) {
            return Collections.emptySet();
        }
        final String sql = "select gs.key from EdsGenericSettings gs" +
                           "    where gs.value=:settingsValue" +
                           "            and gs.key in (:keys)";
        return Sets.newHashSet(this.slaveEntityManager.createQuery(sql, GenericSettingsEnum.class)
                                                 .setParameter("settingsValue", EdsGenericSettings.YES)
                                                 .setParameter("keys", Lists.newArrayList(keys))
                                                 .getResultList());
    }

    @Override
    public EdsGenericSettings getByKey(GenericSettingsEnum key) {
        return (EdsGenericSettings)findSingle("select gs from EdsGenericSettings gs where gs.key = ? ", key);
    }
}
