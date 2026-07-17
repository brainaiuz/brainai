package com.edatasite.workforce.gwt.assessment.client.localization;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;

public interface AssessmentMessages extends Messages {

    String degree360appraisal(String p0);

    String degreeAppraisal(String p0);

    String initiator(String p0);

    String reviewAndRate(String p0, String p1, String p2, String p3);

    String willingToLetUsInform(String p0);

    String performanceAppraisal(String p0);

    String sureToDelete(String p0);

    String hasDeleted(String p0);

    String summaryShouldBe100(String p0);

    String summaryShouldBe(String s, String weight);


    class App {
        public static AssessmentMessages get() {
            return (AssessmentMessages) GWT.create(AssessmentMessages.class);
        }
    }
}
