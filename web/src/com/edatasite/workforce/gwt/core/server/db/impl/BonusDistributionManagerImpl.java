package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.assessment.EdsBonusDistribution;
import com.edatasite.workforce.gwt.core.server.db.BonusDistributionManager;
import com.edatasite.workforce.gwt.hrms.client.rpc.BonusDistributionItem;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: Sher
 * Date: 7/27/12
 * Time: 2:05 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("bonusDistributionManager")
public class BonusDistributionManagerImpl extends BaseManager<EdsBonusDistribution> implements BonusDistributionManager {

    public BonusDistributionManagerImpl() {
        super(EdsBonusDistribution.class);
    }

    @Override
    public EdsBonusDistribution getBonusDistributionItemByValidityPeriod(Integer validityPeriodId, Integer departmentId) {
        if (departmentId != null) {
            return (EdsBonusDistribution) findSingle("from EdsBonusDistribution where validityPeriod.objectID=? and department.objectID=?", validityPeriodId, departmentId);
        } else {
            return (EdsBonusDistribution) findSingle("from EdsBonusDistribution where validityPeriod.objectID=? and department is null", validityPeriodId);
        }
    }

    @Override
    public EdsBonusDistribution getApprovedBonusDistributionItem(Integer validityPeriodId, Integer departmentId) {
        if (departmentId != null) {
            return (EdsBonusDistribution) findSingle("from EdsBonusDistribution where validityPeriod.objectID=? and department.objectID=? and approvalStatus.code=?", validityPeriodId, departmentId, BonusDistributionItem.BONUS_DISTRIBUTION_APPROVED);
        } else {
            return (EdsBonusDistribution) findSingle("from EdsBonusDistribution where validityPeriod.objectID=? and approvalStatus.code=?", validityPeriodId, BonusDistributionItem.BONUS_DISTRIBUTION_APPROVED);
        }
    }
}
