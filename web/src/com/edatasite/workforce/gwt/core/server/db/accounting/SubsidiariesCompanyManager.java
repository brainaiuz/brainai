package com.edatasite.workforce.gwt.core.server.db.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsSubsidiariesCompany;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dilsh0d
 * Date: 16/11/12
 * Time: 16:39
 * To change this template use File | Settings | File Templates.
 */
public interface SubsidiariesCompanyManager extends Manager<EdsSubsidiariesCompany> {
    List<EdsSubsidiariesCompany> getSubsidiariesCompanies();

    List<EdsSubsidiariesCompany> getSubsidiariesCompanies(ListingFilterParameter filterParameter);

    EdsSubsidiariesCompany getSubsidiaryByCompanyID(Integer companyID);
}
