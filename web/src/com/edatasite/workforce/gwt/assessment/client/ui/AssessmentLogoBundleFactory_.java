package com.edatasite.workforce.gwt.assessment.client.ui;

import com.google.gwt.core.client.GWT;

/**
 * Created by IntelliJ IDEA.
 * User: Java6
 * Date: 24.10.11
 * Time: 19:30
 * To change this template use File | Settings | File Templates.
 */
public class AssessmentLogoBundleFactory_ implements AssessmentLogoBundleFactory{
    public AssessmentLogoBundle createImageBundle() {
        return (AssessmentLogoBundle) GWT.create(AssessmentLogoBundle.class);
    }

}
