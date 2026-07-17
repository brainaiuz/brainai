package com.edatasite.workforce.gwt.trainingcenter.client.rpc.attendencesheet;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created with IntelliJ IDEA.
 * User: dilsh0d
 * Date: 26/07/12
 * Time: 16:56
 * To change this template use File | Settings | File Templates.
 */
public class InstructorItem implements IsSerializable {

    private Integer insSchCourseId;
    private String courseName;
    private String languageName;
    private String instructorName;
    private boolean attended = false;
    private boolean approved = false;

    public Integer getInsSchCourseId() {
        return insSchCourseId;
    }

    public void setInsSchCourseId(Integer insSchCourseId) {
        this.insSchCourseId = insSchCourseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public boolean isAttended() {
        return attended;
    }

    public void setAttended(boolean attended) {
        this.attended = attended;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}
