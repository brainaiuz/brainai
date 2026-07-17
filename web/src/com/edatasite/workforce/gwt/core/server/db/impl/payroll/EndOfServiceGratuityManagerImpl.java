package com.edatasite.workforce.gwt.core.server.db.impl.payroll;

import com.edatasite.workforce.core.domain.payrolluk.EdsEosCalculation;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.EndOfServiceGratuityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 14.05.14
 * Time: 18:01
 * To change this template use File | Settings | File Templates.
 */
@Repository("endOfServiceGratuityManager")
public class EndOfServiceGratuityManagerImpl extends BaseManager<EdsEosCalculation> implements EndOfServiceGratuityManager {

    public EndOfServiceGratuityManagerImpl() {
        super(EdsEosCalculation.class);
    }

    @Override
    public List<EdsEosCalculation> getEosCalculationList(ListingFilterParameter filterParameter) {
        StringBuilder sql = new StringBuilder();
        sql.append("select p from EdsEosCalculation p where " + ServerUtils.checkForDeleted("p.deleted"));
        if (filterParameter.getSearchKey() != null && !filterParameter.getSearchKey().isEmpty()) {
            sql.append(" and (lower(p.employee.firstName) like ('%").append(filterParameter.getSearchKey().toLowerCase()).append("%')");
            sql.append(" or lower(p.employee.lastName) like ('%").append(filterParameter.getSearchKey().toLowerCase()).append("%')");
            sql.append(" or lower(p.employee.profile.employeeCode)  like ('%").append(filterParameter.getSearchKey().toLowerCase()).append("%'))");
        }
        if (filterParameter.getSortField() != null) {
            String code = filterParameter.getSortField();
            if ("employee".equals(code)) {
                sql.append(" ORDER BY p.employee ");
            } else if ("reason".equals(code)) {
                sql.append(" ORDER BY p.reasonCode ");
            } else if ("month".equals(code)) {
                sql.append(" ORDER BY p.month ");
            } else if ("total".equals(code)) {
                sql.append(" ORDER BY p.totalAmount ");
            } else {
                sql.append(" ORDER BY p.creationDate ");
            }
            sql.append(!filterParameter.isAscending() ? " desc " : " ");
        } else {
            sql.append(" ORDER BY p.creationDate ");
        }
        return findInterval(sql.toString(), filterParameter.getStart(), filterParameter.getLimit());
    }

    @Override
    public Integer getEosCalculationCount() {
        return find("select c from EdsEosCalculation c where c.deleted is not true").size();
    }

    @Override
    public Integer getLastIntNumber() {
        return (Integer) findSingle("select max(fourDigitNumber) from EdsEosCalculation where (deleted is null or deleted <> true)");
    }
}
