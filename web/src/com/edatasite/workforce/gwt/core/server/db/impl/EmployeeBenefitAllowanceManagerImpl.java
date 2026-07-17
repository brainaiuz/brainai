package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsEmployeeBenefitAllowance;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.EmployeeBenefitAllowanceManager;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Djuraev on 8/3/15.
 */
@Repository("employeeBenefitAllowanceManager")
public class EmployeeBenefitAllowanceManagerImpl extends BaseManager<EdsEmployeeBenefitAllowance> implements EmployeeBenefitAllowanceManager {

    public EmployeeBenefitAllowanceManagerImpl() {
        super(EdsEmployeeBenefitAllowance.class);
    }

    @Override
    public EdsEmployeeBenefitAllowance getBenefitAllowance(Integer year, Integer employeeID, Integer benefitID) {
        return (EdsEmployeeBenefitAllowance) findSingle("from EdsEmployeeBenefitAllowance eba " +
                                                        "where eba.allowanceYear = ? " +
                                                        "and eba.employee.objectID = ? " +
                                                        "and eba.benefit.objectID = ? ", year, employeeID, benefitID);
    }

    @Override
    public List<EdsEmployeeBenefitAllowance> getBenefitAllowanceByEmpID(Integer year, Integer employeeID) {
        Map params = new HashMap();
        params.put("allowanceYear", year);
        params.put("employeeID", employeeID);
        return findByNamedParams("from EdsEmployeeBenefitAllowance a " +
                                 "where a.allowanceYear = :allowanceYear " +
                                 "and a.employee.objectID = :employeeID " +
                                 "and a.benefit.isActive=true " +
                                 "order by a.benefit.name ", params);
    }

    @Override
    public BigDecimal getBenefitPaymentForEndOfServiceCalculation(Integer year, Integer employeeID) {
        StringBuilder sql = new StringBuilder();
        String companyID = getCompanyId();
        sql.append("SELECT sum(t.total)\n");
        sql.append("FROM (\n");
        sql.append("  SELECT eb.allowance AS total\n");
        sql.append("  FROM ").append(companyID).append(".employeebenefit eb\n");
        sql.append("    LEFT JOIN ").append(companyID).append(".benefit b ON b.id = eb.benefitid\n");
        sql.append("    LEFT JOIN ").append(companyID).append(".reference btype ON btype.id = b.qtytype_id\n");
        sql.append("  WHERE btype.code = 'CURRENCY' and eb.employeeid = ").append(employeeID).append(" AND eb.allowanceyear = ").append(year).append("\n");
        sql.append("  UNION ALL\n");
        sql.append("  SELECT (-1)*coalesce(sum(br.requestedquantity), 0.00) AS total\n");
        sql.append("  FROM ").append(companyID).append(".benefitRequest br\n");
        sql.append("    LEFT JOIN ").append(companyID).append(".benefit b ON b.id = br.benefit_id\n");
        sql.append("    LEFT JOIN ").append(companyID).append(".reference btype ON btype.id = b.qtytype_id\n");
        sql.append("    LEFT JOIN ").append(companyID).append(".reference s ON s.id = br.status\n");
        sql.append("  WHERE br.requester = ").append(employeeID).append(" AND s.code = 'BR_APPROVED' AND btype.code = 'CURRENCY' AND ").append(ServerUtils.checkForDeleted("br.deleted"));
        sql.append(") t");
        return (BigDecimal) findNativeSingle(sql.toString());
    }

    @Override
    public List<EdsEmployeeBenefitAllowance> getEmployeeAllowanceByBenefit(Integer benefitID) {
        return find("select e from EdsEmployeeBenefitAllowance e where e.benefit.objectID=?", benefitID);
    }
}
