package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsOnboardingStep;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: User
 * Date: 8/24/12
 * Time: 4:32 PM
 * To change this template use File | Settings | File Templates.
 */
public interface OnboardingStepManager extends Manager<EdsOnboardingStep> {

    List<EdsOnboardingStep> getOnboardingStepList(ListingFilterParameter fp);

    Integer getOnboardingStepTotalCount(ListingFilterParameter fp);

    List<EdsOnboardingStep> getOnboardingStepListByPeriod(Integer period);

    List<EdsOnboardingStep> getOnboardingStepListWithoutPeriodId();

    EdsOnboardingStep getByName(String stepName);

    List<EdsOnboardingStep> getParentSteps(Integer parentID);

    void updateChild(Integer objectID);

    List<EdsOnboardingStep> getStepsForCopy(Integer fromCompanyID, ArrayList<Integer> objectIDs);

    void create(EdsOnboardingStep step, boolean clearTransaction);

    EdsOnboardingStep getByFormID(String formID);
}
