package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsUsagePlan;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: S11A
 * Date: Dec 5, 2008
 * Time: 3:29:22 PM
 * To change this template use File | Settings | File Templates.
 */
public interface UsagePlanManager extends Manager<EdsUsagePlan> {

    List<EdsUsagePlan> getUsagePlansbyCompany(ListingFilterParameter filterParameter);

    EdsUsagePlan getCurrentUsagePlan(EdsCompany company);

    EdsUsagePlan getUsagePlanByUID(String unique_guid);

    List<EdsUsagePlan> getNotPayedUsagePlans(EdsReference rf, EdsReference rf2);

    EdsUsagePlan getLastUsagePlan(Integer companyID);

    EdsUsagePlan getCompanyUsagePlan(Integer usagePlanID);

    EdsUsagePlan getUsagePlanCompany(Integer companyId);

    List<EdsUsagePlan> getFreeTrialUsagePlans(EdsReference rf, EdsReference rf2);

    EdsUsagePlan getFreeTrialUsagePlanCompany(EdsReference reference, EdsCompany company);

    List<EdsUsagePlan> getPaidUsagePlan(Integer companyID);

    List<EdsUsagePlan> getPendingUsagePlans(EdsReference pending, EdsCompany company);

    List<EdsUsagePlan> list(ListingFilterParameter fp);

    List<EdsUsagePlan> getCompanyAllUsagePlans(Integer companyID);

    List<EdsUsagePlan> getAllPaidUsagePlans(EdsReference freeTrial, EdsReference expired);

    Integer listCount(ListingFilterParameter fp);

    List<Integer> getNotPayedUsagePlansId(EdsReference free, EdsReference pending);

    List<Integer> getFreeTrialUsagePlansId(EdsReference rf, EdsReference experid);

    List<Integer> getAllPaidUsagePlansId(EdsReference freeTrial, EdsReference expired);

    List<EdsUsagePlan> getUsagePlansbyCompanyByCompanyId(EdsCompany company);

    List<Object[]> getExpiringCompaniesByMonthYear(Integer month, Integer year);

    Map<Integer, EdsUsagePlan> getCurrentUsagePlans();

    void updatePaidStatus(Boolean paid, Integer status, Integer companyID);
}
