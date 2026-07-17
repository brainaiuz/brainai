/*
package com.edatasite.workforce.gwt.core.server.db.impl.reporting;

import com.edatasite.workforce.core.domain.reporting.EdsRoleReportTemplate;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.wfp.RoleReportTemplateManager;

*/
/**
 * Created by IntelliJ IDEA.
 * User: Virusjon
 * Date: 20.10.11
 * Time: 15:10
 * To change this template use File | Settings | File Templates.
 *//*


public class RoleReportTemplateManagerImpl extends BaseManager<EdsRoleReportTemplate> implements RoleReportTemplateManager {

    public RoleReportTemplateManagerImpl() {
        super(EdsRoleReportTemplate.class);
    }

    @Override
    public boolean hasRole(Integer companyID, String reportTemplateCode, String role) {
        return Integer.valueOf("" + findNativeSingle("select count(r.*) from roleReportTemplate r where coalesce(r.companyID," + companyID + ")=" + companyID + " " +
                " and reportTemplateCode='" + reportTemplateCode + "' and  role ='" + role + "'")) > 0 ? true : false;
    }

    @Override
    public EdsRoleReportTemplate getReportTemplate(Integer companyID, String reportTemplateCode, String role) {
        return (EdsRoleReportTemplate) findNativeSingle("select r.* from roleReportTemplate r where reportTemplateCode='" + reportTemplateCode + "' and role='" + role + "' and companyID " + (null == companyID ? " is null " : (" = " + companyID)), EdsRoleReportTemplate.class);
    }

    @Override
    public boolean hasRole(Integer company_id, String reportTemplateCode) {
        return Integer.valueOf("" + findNativeSingle("select count(r.*) from roleReportTemplate r where coalesce(r.companyID," + company_id + ")=" + company_id + " " +
                " and reportTemplateCode='" + reportTemplateCode + "' ")) > 0 ? true : false;
    }
}*/
