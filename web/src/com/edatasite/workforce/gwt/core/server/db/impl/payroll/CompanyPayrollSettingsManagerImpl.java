package com.edatasite.workforce.gwt.core.server.db.impl.payroll;

import com.edatasite.workforce.core.domain.payrolluk.EdsCompanyPayrollSettings;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.CompanyPayrollSettingsManager;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ?????????????
 * Date: 02.03.2009
 * Time: 15:08:45
 * To change this template use File | Settings | File Templates.
 */
@Repository("companyPayrollSettingsManager")
public class CompanyPayrollSettingsManagerImpl extends BaseManager<EdsCompanyPayrollSettings> implements CompanyPayrollSettingsManager {

    public CompanyPayrollSettingsManagerImpl() {
        super(EdsCompanyPayrollSettings.class);
    }

    public List<EdsCompanyPayrollSettings> getCompanySettings(Integer companyID) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select distinct cs.* ");
        sql.append(" from " + getCompanyId() + ".CompanyPayrollSettings cs ");
        return findNative(sql.toString(), EdsCompanyPayrollSettings.class);
    }

    public EdsCompanyPayrollSettings getCompanySettingValue(String key) {
        StringBuilder sql = new StringBuilder();
        sql.append(" from EdsCompanyPayrollSettings cs where cs.key=? ");
        return (EdsCompanyPayrollSettings) findSingle(sql.toString(), key);
    }

    @Override
    public List<EdsCompanyPayrollSettings> getCompanySettings(String... keys) {
        if (keys == null || keys.length == 0) {
            return Collections.emptyList();
        }
        final String sql = " select cs from EdsCompanyPayrollSettings cs " +
                           "where cs.key in (:keyValues) ";

        return this.slaveEntityManager.createQuery(sql)
                                 .setParameter("keyValues", Lists.newArrayList(keys))
                                 .getResultList();
    }

}