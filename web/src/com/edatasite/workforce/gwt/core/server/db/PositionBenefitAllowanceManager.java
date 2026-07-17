package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsPositionBenefitAllowance;

import java.util.List;

public interface PositionBenefitAllowanceManager extends Manager<EdsPositionBenefitAllowance> {

    List<EdsPositionBenefitAllowance> getPositionAllowanceByBenefit(Integer benefitID);

    List<EdsPositionBenefitAllowance> listPositionBenefitAllowances(Integer positionId, Integer currentYear);

    EdsPositionBenefitAllowance getBenefitAllowanceFromPosition(Integer benefitID, Integer pID, Integer currentYear);


}
