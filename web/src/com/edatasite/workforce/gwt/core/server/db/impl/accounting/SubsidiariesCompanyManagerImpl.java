package com.edatasite.workforce.gwt.core.server.db.impl.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsSubsidiariesCompany;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.accounting.SubsidiariesCompanyManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dilsh0d
 * Date: 16/11/12
 * Time: 16:40
 * To change this template use File | Settings | File Templates.
 */
@Repository("subsidiariesCompanyManager")
public class SubsidiariesCompanyManagerImpl extends BaseManager<EdsSubsidiariesCompany> implements SubsidiariesCompanyManager {

    public SubsidiariesCompanyManagerImpl() {
        super(EdsSubsidiariesCompany.class);
    }

    @Override
    public List<EdsSubsidiariesCompany> getSubsidiariesCompanies() {
        return find("SELECT sub FROM EdsSubsidiariesCompany sub");
    }

    @Override
    public List<EdsSubsidiariesCompany> getSubsidiariesCompanies(ListingFilterParameter filterParameter) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT sub FROM EdsSubsidiariesCompany sub WHERE 1=1 \n");

        if (filterParameter.isValidSearchKey()) {
            sql.append("AND lower(sub.companyName) like '").append(filterParameter.getSqlSearchKey()).append("' \n");
        }

        if (filterParameter.isShowHeadOffice() && getUser().getCompany().getParentCompanyId() != null) {
            sql.append("AND sub.companyId = ").append(getUser().getCompany().getParentCompanyId()).append(" \n");
        }

        return find(sql.toString());
    }

    @Override
    public EdsSubsidiariesCompany getSubsidiaryByCompanyID(Integer companyID) {
        return (EdsSubsidiariesCompany) findSingle("select sub from EdsSubsidiariesCompany sub where sub.companyId = ?", companyID);
    }
}
