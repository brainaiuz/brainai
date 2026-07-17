package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsEmployeeAsterisk;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.EmployeeAsteriskManager;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: 7/4/2020
 * Time: 11:23 AM
 * To change this template use File | Settings | File Templates.
 */
@Repository("employeeAsteriskManager")
public class EmployeeAsteriskManagerImpl extends BaseManager<EdsEmployeeAsterisk> implements EmployeeAsteriskManager, Constants {

    public EmployeeAsteriskManagerImpl() {
        super(EdsEmployeeAsterisk.class);
    }

    @Override
    public List<EdsEmployeeAsterisk> list(Integer asteriskSettingsId, ListingFilterParameter fp) {
        return findInterval(getQuery(asteriskSettingsId, fp, false), fp.getStart(), fp.getLimit(), asteriskSettingsId);
    }

    public String getQuery(Integer asteriskSettingsId, ListingFilterParameter fp, boolean counting) {
        Integer companyID = fp.getCompanyID() == null ? SecurityContext.getCompanyID() : fp.getCompanyID();
        StringBuilder s = new StringBuilder();
        if (fp.getSqlSearchKey() != null && !"".equals(fp.getSqlSearchKey())) {
            s.append(" AND (");
            s.append("LOWER(number) LIKE '" + fp.getSqlSearchKey() + "' ");
            s.append(") ");
        }
        if (!counting) {
            s.append(" ORDER BY number");
            if (fp.isAscending()) {
                s.append(" DESC ");
            }
        }
        return "SELECT " + (counting ? "count( distinct t.objectID)" : "t") + " FROM EdsEmployeeAsterisk t WHERE (t.deleted is null or t.deleted<>true) AND (t.asteriskSettings.deleted IS NULL OR t.asteriskSettings.deleted<>true) AND t.asteriskSettings.objectID=? " + s;
    }

    @Override
    public int listCount(Integer asteriskSettingsId, ListingFilterParameter filterParameter) {
        Long count = (Long) findSingle(getQuery(asteriskSettingsId, filterParameter, true), asteriskSettingsId);
        if (count != null) {
            return count.intValue();
        }
        return 0;
    }

    @Override
    public EdsEmployeeAsterisk getEmployeeAsteriskSettings(Integer asteriskSettingsId, Integer employeeId) {
        return (EdsEmployeeAsterisk) findSingle("SELECT s FROM EdsEmployeeAsterisk s WHERE (s.deleted IS NULL OR s.deleted<>true) AND (s.asteriskSettings.deleted IS NULL OR s.asteriskSettings.deleted<>true) AND s.asteriskSettingsId=? AND s.userId=?", asteriskSettingsId, employeeId);
    }

    @Override
    public List<EdsEmployeeAsterisk> getEmployeeAsteriskSettings(Integer employeeId) {
        return (List<EdsEmployeeAsterisk>) find("SELECT s FROM EdsEmployeeAsterisk s WHERE (s.deleted IS NULL OR s.deleted<>true) AND (s.asteriskSettings.deleted IS NULL OR s.asteriskSettings.deleted<>true) AND s.userId=?", employeeId);
    }

    public List<EdsEmployeeAsterisk> getByAsteriskUsername(String asteriskUsername) {
        return (List<EdsEmployeeAsterisk>) find("SELECT s FROM EdsEmployeeAsterisk s WHERE (s.deleted IS NULL OR s.deleted<>true) AND (s.asteriskSettings.deleted IS NULL OR s.asteriskSettings.deleted<>true) AND s.userId is not null AND s.user.accountStatus.code = 'ACTIVE_EMPLOYEE' AND s.username =? ", asteriskUsername);
    }

}
