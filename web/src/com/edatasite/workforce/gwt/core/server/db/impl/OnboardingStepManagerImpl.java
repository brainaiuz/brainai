package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsOnboardingStep;
import com.edatasite.workforce.core.domain.documents.EdsAuditInfo;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.OnboardingStepManager;
import com.edatasite.workforce.gwt.hrms.client.rpc.OnboardingItem;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: User
 * Date: 8/24/12
 * Time: 4:34 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("onboardingStepManager")
public class OnboardingStepManagerImpl extends BaseManager<EdsOnboardingStep> implements OnboardingStepManager {
    public OnboardingStepManagerImpl() {
        super(EdsOnboardingStep.class);
    }

    @Override
    public List<EdsOnboardingStep> getOnboardingStepList(ListingFilterParameter fp) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }

        StringBuilder sql = new StringBuilder("select os from EdsOnboardingStep os ");
        sql.append("where os.deleted is not true ");
        if (fp.getSearchKey() != null && !"".equals(fp.getSearchKey())) {
            sql.append(" and lower(os.name) like '").append(fp.getSqlSearchKey()).append("'");
        }
        if(fp.getShowInListing() != null && fp.getShowInListing()){
            sql.append(" and os.createForm is true ");
        }
        sql.append("order by ");
        if (fp.getSortField() != null && !"".equals(fp.getSortField())) {
            if (OnboardingItem.ONBOARDING_STEP_NAME.equals(fp.getSortField())) {
                sql.append("os.name");
            } else if (OnboardingItem.ONBOARDING_STEP_DESCRIPTION.equals(fp.getSortField())) {
                sql.append("os.description");
            } else if (OnboardingItem.ONBOARDING_PERIOD_NAME.equals(fp.getSortField())) {
                sql.append("os.onboardingPeriod.name");
            } else {
                sql.append(" os.name ");
            }
            sql.append(!fp.isAscending() ? " desc " : " ");
        } else {
            sql.append(" os.name ");
        }
        int limit = 20;
        if (fp.getLimit() != null) {
            limit = fp.getLimit();
        }
        return findInterval(sql.toString(), fp.getStart(), limit);
    }

    @Override
    public void create(EdsOnboardingStep onboardingStep) {
        EdsAuditInfo info = onboardingStep.getAuditInfo();
        if (info.getCreatedBy() == null) {
            info.setCreatedBy(getUser());
        }
        if (info.getCreationDate() == null) {
            info.setCreationDate(new Date());
        }
        info.setModificationDate(new Date());
        info.setModifiedBy(getUser());
        onboardingStep.setAuditInfo(info);
        super.create(onboardingStep);
    }

    @Override
    public void create(EdsOnboardingStep onboardingStep, boolean clearTransaction) {
        create(onboardingStep);
        flushAndClear();
    }

    @Override
    public EdsOnboardingStep getByFormID(String formID) {
        return (EdsOnboardingStep) findSingle("select os from EdsOnboardingStep os where os.deleted is not true and os.formID = '" + formID + "'");
    }

    @Override
    public void update(EdsOnboardingStep onboardingStep) {
        EdsAuditInfo info = onboardingStep.getAuditInfo();
        info.setModificationDate(new Date());
        info.setModifiedBy(getUser());
        onboardingStep.setAuditInfo(info);
        super.update(onboardingStep);
    }

    @Override
    public Integer getOnboardingStepTotalCount(ListingFilterParameter fp) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        StringBuilder sql = new StringBuilder("select count(os.objectID) from EdsOnboardingStep os ");
        sql.append("where os.deleted is not true ");
        if (fp.getSearchKey() != null && !"".equals(fp.getSearchKey())) {
            sql.append(" and lower(os.name) like '").append(fp.getSqlSearchKey()).append("'");
        }
        if(fp.getShowInListing() != null && fp.getShowInListing()){
            sql.append(" and os.createForm is true");
        }
        return ((Long) findSingle(sql.toString())).intValue();
    }

    @Override
    public List<EdsOnboardingStep> getOnboardingStepListByPeriod(Integer period) {
        return find("select os from EdsOnboardingStep os where os.deleted is not true and os.showInEmployeeProfile is true and os.onboardingPeriod.objectID=?  order by os.name asc", period);

    }

    @Override
    public List<EdsOnboardingStep> getOnboardingStepListWithoutPeriodId() {
        return find("select os from EdsOnboardingStep os where os.deleted is not true and os.showInEmployeeProfile is true and os.onboardingPeriod.objectID is null  order by os.name asc");

    }

    @Override
    public EdsOnboardingStep getByName(String stepName) {
        return (EdsOnboardingStep) findSingle("select os from EdsOnboardingStep os where os.deleted is not true and lower(os.name) = '" + stepName.toLowerCase() + "'");
    }

    @Override
    public List<EdsOnboardingStep> getParentSteps(Integer parentID) {
        return find("select os from EdsOnboardingStep os where os.deleted is not true and os.objectID not in (select cs.parent.objectID from EdsOnboardingStep cs where cs.deleted is not true and cs.parent is not null" + (parentID != null ? " and cs.objectID!=" + parentID : "") + ")");
    }

    @Override
    public void updateChild(Integer objectID) {
        updateNative("update " + getCompanyId() + ".onboardingStep set parent = null where parent = " + objectID);
    }

    @Override
    public List<EdsOnboardingStep> getStepsForCopy(Integer fromCompanyID, ArrayList<Integer> objectIDs) {
        return findNative("select * from \"" + fromCompanyID + "\".onboardingStep where (deleted is null or deleted is false) and id in (" + ServerUtils.getAsCommoDelimited(objectIDs, "0", ",") + ")", EdsOnboardingStep.class);
    }
}
