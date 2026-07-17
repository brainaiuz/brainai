package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsSpokenLanguages;
import com.edatasite.workforce.gwt.core.server.db.SpokenLanguagesManager;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

/**
 * Created by Ilhom Lutfullaev on 03.11.2017.
 */

@Repository
public class SpokenLanguagesManagerImpl extends BaseManager<EdsSpokenLanguages> implements SpokenLanguagesManager {

    public SpokenLanguagesManagerImpl() {
        super(EdsSpokenLanguages.class);
    }

    @Override
    public ArrayList<EdsSpokenLanguages> getListByRelation(Integer entityId, String entityType) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT sl.*, 0 as clazz_ from").append(getCompanyId()).append(".spokenlanguages sl ");
        sql.append("where sl.entityType = '").append(entityType).append("'").append(" and sl.entityId =").append(entityId).append(" order by sl.id asc");

        return (ArrayList<EdsSpokenLanguages>) findNative(sql.toString(), EdsSpokenLanguages.class);
    }

    @Override
    public EdsSpokenLanguages getByRelation(Integer entityId, String entityType, Integer languageId) {
        return (EdsSpokenLanguages) findSingle("select sl from EdsSpokenLanguages sl where sl.entityType=? and sl.entityId=? and sl.language.objectID=? order by sl.objectID asc", entityType, entityId, languageId);
    }

    @Override
    public void removedLanguages(Integer entityId, String entityType, ArrayList<Integer> removedLanguagesList) {
        updateNative("delete from " + getCompanyId() + ".spokenlanguages sl where sl.entityType='" + entityType + "' and sl.entityId=" + entityId.toString() + " and sl.languageId not in (" + removedLanguagesList.toString().replace("[", "").replace("]", "") + ")");
    }

    @Override
    public void firstRemoveEmployeeLanguages(Integer entityId, String entityType) {
        updateNative("delete from " + getCompanyId() + ".spokenlanguages sl where sl.entityType='" + entityType + "' and sl.entityId=" + entityId.toString());
    }
}
