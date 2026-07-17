package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.assessment.EdsBonusDistribution;

/**
 * Created with IntelliJ IDEA.
 * User: Sher
 * Date: 7/27/12
 * Time: 1:55 PM
 * To change this template use File | Settings | File Templates.
 */

public interface BonusDistributionManager extends Manager<EdsBonusDistribution> {

    EdsBonusDistribution getBonusDistributionItemByValidityPeriod(Integer validityPeriodId, Integer departmentId);

    EdsBonusDistribution getApprovedBonusDistributionItem(Integer validityPeriodId, Integer departmentId);
}
