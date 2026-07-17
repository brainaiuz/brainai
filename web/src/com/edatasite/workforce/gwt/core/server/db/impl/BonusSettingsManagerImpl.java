package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsValidityPeriod;
import com.edatasite.workforce.core.domain.assessment.EdsBonusSettings;
import com.edatasite.workforce.core.domain.assessment.EdsScoreItem;
import com.edatasite.workforce.gwt.core.server.db.BonusSettingsManager;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Sher
 * Date: 7/27/12
 * Time: 2:05 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("bonusSettingsManager")
public class BonusSettingsManagerImpl extends BaseManager<EdsBonusSettings> implements BonusSettingsManager {

    public BonusSettingsManagerImpl() {
        super(EdsBonusSettings.class);
    }

    @Override
    public EdsBonusSettings getBonusSettingsByPeriod(EdsValidityPeriod validityPeriod) {
        return (EdsBonusSettings) findSingle("from EdsBonusSettings where validityPeriod=?", validityPeriod);
    }

    @Override
    public EdsScoreItem getScoreItem(Integer objectId) {
        return (EdsScoreItem) findSingle("from EdsScoreItem where objectID=?", objectId);
    }

    @Override
    public void createOrUpdateScoreItem(EdsScoreItem edsScoreItem) {
        if (edsScoreItem.getObjectID() != null) {
            jpaTemplate.merge(edsScoreItem);
        } else {
            persist(edsScoreItem);
        }
    }

    @Override
    public EdsBonusSettings getBonusSettingsByDate(Date fromDate, Date toDate) {
        Map<String, Object> map = new HashMap<>();
        map.put("fromDate", fromDate);
        map.put("toDate", toDate);
        return (EdsBonusSettings) findSingleByNamedParams(" from EdsBonusSettings where (:fromDate between validityPeriod.fromDate and validityPeriod.toDate) and (:toDate between validityPeriod.fromDate and validityPeriod.toDate)", map);
    }
}
