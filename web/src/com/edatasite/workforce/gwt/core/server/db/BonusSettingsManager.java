package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsValidityPeriod;
import com.edatasite.workforce.core.domain.assessment.EdsBonusSettings;
import com.edatasite.workforce.core.domain.assessment.EdsScoreItem;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Sher
 * Date: 7/27/12
 * Time: 1:55 PM
 * To change this template use File | Settings | File Templates.
 */

public interface BonusSettingsManager extends Manager<EdsBonusSettings> {

    EdsBonusSettings getBonusSettingsByPeriod(EdsValidityPeriod validityPeriod);

    EdsScoreItem getScoreItem(Integer objectId);

    void createOrUpdateScoreItem(EdsScoreItem edsScoreItem);

    EdsBonusSettings getBonusSettingsByDate(Date fromDate, Date toDate);
}
