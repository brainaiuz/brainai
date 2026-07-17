package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsOnboardingPeriod;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.OnboardingPeriodManager;
import com.edatasite.workforce.gwt.hrms.client.rpc.OnboardingItem;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: User
 * Date: 8/24/12
 * Time: 4:35 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("onboardingPeriodManager")
public class OnboardingPeriodManagerImpl extends BaseManager<EdsOnboardingPeriod> implements OnboardingPeriodManager {

    public OnboardingPeriodManagerImpl() {
        super(EdsOnboardingPeriod.class);
    }

    public List<EdsOnboardingPeriod> getOnboardingPeriodList(ListingFilterParameter fp) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }

        StringBuilder sql = new StringBuilder("select op from EdsOnboardingPeriod op ");
        sql.append("where op.deleted is not true ");
        if (fp.getSearchKey() != null && !"".equals(fp.getSearchKey())) {
            sql.append(" and lower(op.name) like '").append(fp.getSqlSearchKey()).append("'");
        }
        sql.append("order by ");
        if (fp.getSortField() != null && !"".equals(fp.getSortField())) {
            if (OnboardingItem.ONBOARDING_PERIOD_NAME.equals(fp.getSortField())) {
                sql.append("op.name");
            } else if (OnboardingItem.ONBOARDING_PERIOD_DESCRIPTION.equals(fp.getSortField())) {
                sql.append("op.description");
            } else if (OnboardingItem.ONBOARDING_PERIOD_DURATION.equals(fp.getSortField())) {
                sql.append("op.duration");
            } else if (OnboardingItem.ONBOARDING_PERIOD_ACTIVE.equals(fp.getSortField())) {
                sql.append("op.active");
            } else if (OnboardingItem.ONBOARDING_PERIOD_RELIATIVE_START.equals(fp.getSortField())) {
                sql.append("op.relativeStart");
            } else {
                sql.append(" op.name desc");
            }
            if (fp.getSortDir() != null) {
                if (Integer.valueOf(1).equals(fp.getSortDir())) {
                    sql.append(" asc");
                } else {
                    sql.append(" desc");
                }
            } else {
                sql.append(" desc");
            }
        } else {
            sql.append(" op.name desc");
        }
        int limit =20;
        if (fp.getLimit() != null) {
            limit = fp.getLimit();
        }
        return findInterval(sql.toString(), fp.getStart(), limit);
    }

    public Integer getOnboardingPeriodTotalCount(ListingFilterParameter fp) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        StringBuilder sql = new StringBuilder("select count(op.objectID) from EdsOnboardingPeriod op ");
        sql.append("where op.deleted is not true ");
        if (fp.getSearchKey() != null && !"".equals(fp.getSearchKey())) {
            sql.append(" and lower(op.name) like '").append(fp.getSqlSearchKey()).append("'");
        }
        return ((Long) findSingle(sql.toString())).intValue();
    }

    public List<EdsOnboardingPeriod> getOnboardingPeriodListOrderByRelativeStart() {
        return find("select op from EdsOnboardingPeriod op where op.deleted is not true and op.active is true order by op.isBeforeHireDate desc, op.relativeStart asc, op.duration asc");

    }
}
