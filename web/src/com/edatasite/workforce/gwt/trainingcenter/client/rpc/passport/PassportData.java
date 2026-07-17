package com.edatasite.workforce.gwt.trainingcenter.client.rpc.passport;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.CourseItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: acer
 * Date: 13/06/14
 * Time: 19:39
 * To change this template use File | Settings | File Templates.
 */
public class PassportData implements IsSerializable {
    public static final String NUMBER = "number";
    public static final String STUDENT = "student";
    public static final String TYPE = "type";
    public static final String CREATION_DATE = "creationdate";
    public static final String LEVEL = "level";
    public static final String STATUS = "status";
    //type
    public static final String GREEN = "Green";
    public static final String RED = "Red";
    //Number text
    public static final String PDO = "PDO";
    public static final String KGF = "KGF";
    //level
    public static final String NOOB = "Noob";
    public static final String SUPERVISOR = "Supervisor";
    public static final String NON_SUPERVISOR = "Non Supervisor";

    private Integer objectID;
    private Integer studentID;
    private String studentName;
    private String student;
    private String numberString;
    private String number;
    private String type;
    private String level;
    private CourseItem[] courses;
    private Date creationDate;
    private Integer statusID;
    private String status;
    private SelectItem[] statuses;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getStudentID() {
        return studentID;
    }

    public void setStudentID(Integer studentID) {
        this.studentID = studentID;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public CourseItem[] getCourses() {
        return courses;
    }

    public void setCourses(CourseItem[] courses) {
        this.courses = courses;
    }

    public String getNumberString() {
        return numberString;
    }

    public void setNumberString(String numberString) {
        this.numberString = numberString;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Integer getStatusID() {
        return statusID;
    }

    public void setStatusID(Integer statusID) {
        this.statusID = statusID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public SelectItem[] getStatuses() {
        return statuses;
    }

    public void setStatuses(SelectItem[] statuses) {
        this.statuses = statuses;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
