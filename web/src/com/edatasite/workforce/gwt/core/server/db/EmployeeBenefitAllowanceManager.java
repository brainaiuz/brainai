package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsEmployeeBenefitAllowance;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Djuraev on 8/3/15.
 */
public interface EmployeeBenefitAllowanceManager extends Manager<EdsEmployeeBenefitAllowance> {

    EdsEmployeeBenefitAllowance getBenefitAllowance(Integer year, Integer employeeID, Integer benefitID);

    List<EdsEmployeeBenefitAllowance> getBenefitAllowanceByEmpID(Integer year, Integer employeeID);

    BigDecimal getBenefitPaymentForEndOfServiceCalculation(Integer year, Integer employeeID);

    List<EdsEmployeeBenefitAllowance> getEmployeeAllowanceByBenefit(Integer benefitID);
}
