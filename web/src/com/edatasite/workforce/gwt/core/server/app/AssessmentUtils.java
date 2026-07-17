package com.edatasite.workforce.gwt.core.server.app;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.assessment.EdsEmployeeAssessment;
import com.edatasite.workforce.gwt.core.client.ui.Constants;

public class AssessmentUtils implements Constants {
    private AssessmentUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static String getReviewLinkForMailForEmployee(EdsEmployeeAssessment employeeAssessment) {
        String assessType = "employeeAssessment";
        String type = ASSESSMENT_SIMPLE;
        if (employeeAssessment.getAssessment().getAssessmentType().getCode().equals(ASSESSMENT_360)) {
            type = ASSESSMENT_360;
        }
        return EncryptionHelper.encryptURL(assessType + "/" + employeeAssessment.getObjectID().toString() +
                "/" + employeeAssessment.getStatus().getName() + "/" + type + "/" + PA_360_SIMPLE_VIEW);
    }

    public static String getReviewLinkForMailForManager(EdsEmployeeAssessment employeeAssessment) {
        String assessType = "employeeAssessment";
        String type = ASSESSMENT_SIMPLE;
        if (employeeAssessment.getAssessment().getAssessmentType().getCode().equals(ASSESSMENT_360)) {
            type = ASSESSMENT_360;
        }

        return EncryptionHelper.encryptURL(assessType + "/" + employeeAssessment.getAssessment().getObjectID().toString() +
                "/" + employeeAssessment.getStatus().getName() + "/" + type + "/" + PA_360_MANAGER_VIEW);
    }

    public static String getReviewLinkForMailForOneOffUser(EdsUser reviewer, EdsEmployeeAssessment employeeAssessment) {
        String assessType = "oneoffassessment";
        return assessType + "?link=" + EncryptionHelper.encryptURL(reviewer.getUserName() + "/" + employeeAssessment.getObjectID().toString());
    }

    public static String getRate360LinkForMail(EdsEmployeeAssessment employeeAssessment) {
        return EncryptionHelper.encryptURL("assessment/" + employeeAssessment.getObjectID().toString());
    }
}
