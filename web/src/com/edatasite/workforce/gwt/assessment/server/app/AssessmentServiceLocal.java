package com.edatasite.workforce.gwt.assessment.server.app;

import com.edatasite.workforce.gwt.assessment.client.rpc.AppraisalsSettingsItem;
import com.edatasite.workforce.gwt.core.client.rpc.ValidityPeriodItem;

/**
 * User: Ilhombek
 * Date: 1/9/13
 * Time: 7:16 PM
 */
public interface AssessmentServiceLocal {

    AppraisalsSettingsItem getAppraisalsSettings(Integer currentUserID);

    ValidityPeriodItem[] getValidityPeriods(String periodType);

    void deleteCompetency(Integer competencyID);

}