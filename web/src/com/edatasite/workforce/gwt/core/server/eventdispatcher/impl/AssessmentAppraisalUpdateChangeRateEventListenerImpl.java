package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.assessment.EdsAppraisalsSettings;
import com.edatasite.workforce.core.domain.assessment.EdsAssessment;
import com.edatasite.workforce.core.domain.assessment.EdsGoalRating;
import com.edatasite.workforce.core.domain.assessment.EdsSkillRating;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.gwt.core.server.db.AssessmentManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BusinessEventListener;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * User: Ilhombek
 * Date: 1/31/13
 * Time: 7:05 PM
 */
@Transactional
public class AssessmentAppraisalUpdateChangeRateEventListenerImpl implements BusinessEventListener {

    public static WfmType<EdsAppraisalsSettings> TYPE = new WfmType<>(EventTypes.assessmentAppraisalUpdateChangeRateEventListener);

    @Autowired
    private AssessmentManager assessmentManager;
    @Autowired
    private UserManager userManager;

    @Override
    public void onAddEvent(EdsBusinessEvent event) {
        try {
            Integer currentUserID = event.getSourceID();
            EdsCompany company = userManager.get(currentUserID).getCompany();
            EdsAppraisalsSettings appraisalsSettings = assessmentManager.getAppraisalsSettings(company);
            List<EdsAssessment> companyAssessments = assessmentManager.getCompanyAllAssessments(company);
            for (EdsAssessment assessment : companyAssessments) {
                if (assessment != null && assessment.getKeyEmployeeAssessment() != null) {
                    if (assessment.getKeyEmployeeAssessment().getSkillAssessment() != null) {
                        List<EdsSkillRating> skillRatings = assessment.getKeyEmployeeAssessment().getSkillAssessment().getRatings();
                        for (EdsSkillRating skillRating : skillRatings) {
                            //get old & new rate
                            double fromScale = appraisalsSettings.getFromScale();
                            double stepSize = appraisalsSettings.getStepSize();

                            double toScale = appraisalsSettings.getToScale();
                            double oldToScale = appraisalsSettings.getOldToScale();
                            //get manager & employee rating
                            Double rating = skillRating.getRating();
                            Double employeeRating = skillRating.getEmployeeRating();
                            //calculate rate
                            double exchangeRating = exchangeRatingT(fromScale, stepSize, toScale, oldToScale, rating);
                            double exchangeEmployeeRating = exchangeRatingT(fromScale, stepSize, toScale, oldToScale, employeeRating);
                            //set calculated rate
                            skillRating.setRating(exchangeRating);
                            skillRating.setEmployeeRating(exchangeEmployeeRating);
                        }
                    }
                    if (assessment.getKeyEmployeeAssessment().getGoalAssessment() != null) {
                        List<EdsGoalRating> goalRatings = assessment.getKeyEmployeeAssessment().getGoalAssessment().getRatings();
                        for (EdsGoalRating goalRating : goalRatings) {
                            //get old & new rate
                            double fromScale = appraisalsSettings.getFromScale();
                            double stepSize = appraisalsSettings.getStepSize();

                            double toScale = appraisalsSettings.getToScale();
                            double oldToScale = appraisalsSettings.getOldToScale();
                            //get manager & employee rating
                            Double rating = goalRating.getRating();
                            Double employeeRating = goalRating.getEmployeeRating();
                            //calculate rate
                            double exchangeRating = exchangeRatingT(fromScale, stepSize, toScale, oldToScale, rating);

                            double exchangeEmployeeRating = exchangeRatingT(fromScale, stepSize, toScale, oldToScale, employeeRating);
                            //set calculated rate
                            goalRating.setRating(exchangeRating);
                            goalRating.setEmployeeRating(exchangeEmployeeRating);
                        }
                    }
                }
            }
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (Exception ex) {
            event.setStatus(EventStatus.FAILED.name());
            ex.printStackTrace();
        }
    }

    private double exchangeRatingT(double fromScale, double stepSize, double toScale, double oldToScale, Double rating) {
        double exchangeRating = (toScale * rating) / oldToScale;

        double remainder = (exchangeRating - fromScale) % stepSize;
        if (exchangeRating != toScale) {
            exchangeRating -= remainder;
        }
        if ((remainder > (stepSize / 2)) && ((exchangeRating + stepSize) <= toScale)) {
            exchangeRating += stepSize;
        }
        return exchangeRating;
    }

    @Override
    public void onDeleteEvent(EdsBusinessEvent event) {
    }

    @Override
    public void onEditEvent(EdsBusinessEvent event) {
    }

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {
    }
}