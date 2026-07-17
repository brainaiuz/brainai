package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsPositionBenefitAllowance;
import com.edatasite.workforce.gwt.core.server.db.PositionBenefitAllowanceManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("positionBenefitAllowanceManager")
public class PositionBenefitAllowanceManagerImpl extends BaseManager<EdsPositionBenefitAllowance> implements PositionBenefitAllowanceManager {

    public PositionBenefitAllowanceManagerImpl() {
        super(EdsPositionBenefitAllowance.class);

    }

    @Override
    public List<EdsPositionBenefitAllowance> getPositionAllowanceByBenefit(Integer benefitID) {
        return find("select p from EdsPositionBenefitAllowance p where p.benefit.objectID=?", benefitID);
    }

    @Override
    public List<EdsPositionBenefitAllowance> listPositionBenefitAllowances(Integer positionId, Integer currentyear) {
        return find("select p from EdsPositionBenefitAllowance p where p.position.objectID=? and p.allowanceYear =?", positionId, currentyear);
    }

    @Override
    public EdsPositionBenefitAllowance getBenefitAllowanceFromPosition(Integer benefitID, Integer pID, Integer currentYear) {
        return (EdsPositionBenefitAllowance) findSingle("select p from EdsPositionBenefitAllowance p where p.benefit.objectID=? and p.position.objectID=? and allowanceYear=?", benefitID, pID, currentYear);
    }

}
