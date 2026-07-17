package com.edatasite.workforce.gwt.core.client.rpc.solr;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * User: Abdulaziz
 * Date: Nov 17, 2009
 * Time: 7:56:13 PM
 */
public class SolrEmployeeAssessmentRepresenter extends SolrDocumentRepresenter implements IsSerializable {

    public static final String FIELD_ASESSMENT_TEMPLATE = "template";
    public static final String FIELD_COMMENTS = "comments";
    public static final String FIELD_MANAGERS_ID = "managersID";
    public static final String FIELD_ASSESSMENT_ID = "assessmentID";
    private Integer assessmentID;
    private String template;
    private String comments;
    private Integer[] managersID;

    public Integer getAssessmentID() {
        return assessmentID;
    }

    public void setAssessmentID(Integer assessmentID) {
        this.assessmentID = assessmentID;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Integer[] getManagersID() {
        return managersID;
    }

    public void setManagersID(Integer[] managersID) {
        this.managersID = managersID;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
}
