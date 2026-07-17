package com.edatasite.workforce.core.solr.document;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.util.Date;

/**
 * @author: Sardorbek Juraboev on 05.09.2023 12:39.
 */

@SolrDocument(collection = "courseScheduleCore")
public class CourseScheduleSolrDoc extends BaseSolrDoc {

    @Id
    @Indexed(name = "oid", type = "string", required = true)
    private String oid;

    @Field("companyId")
    private Integer companyId;

    @Field("courseScheduleId")
    private Integer courseScheduleId;

    @Field("courseScheduleNumber")
    private String courseScheduleNumber;

    @Field("courseId")
    private Integer courseId;

    @Field("courseName")
    private String courseName;

    @Field("courseIdName")
    @Indexed(name = "courseIdName", type = "string", stored = false)
    private String courseIdName;

    @Field("courseCode")
    private String courseCode;

    @Field("languageId")
    private Integer languageId;

    @Field("languageName")
    private String languageName;

    @Field("languageIdName")
    @Indexed(name = "languageIdName", type = "string", stored = false)
    private String languageIdName;

    @Field("enableOvertime")
    private Boolean enableOvertime;

    @Field("startDate")
    private Date startDate;

    @Field("locationId")
    private Integer locationId;

    @Field("locationName")
    private String locationName;

    @Field("locationIdName")
    @Indexed(name = "locationIdName", type = "string", stored = false)
    private String locationIdName;

    @Field("instructorId")
    private Integer instructorId;

    @Field("instructorName")
    private String instructorName;

    @Field("instructorIdName")
    @Indexed(name = "instructorIdName", type = "string", stored = false)
    private String instructorIdName;

    @Field("duration")
    private Integer duration;

    @Field("numberOfSeats")
    private Integer numberOfSeats;

    @Field("assessorId")
    private Integer assessorId;

    @Field("assessorName")
    private String assessorName;

    @Field("statusId")
    private Integer statusId;

    @Field("statusName")
    private String statusName;

    @Field("statusIdName")
    @Indexed(name = "statusIdName", type = "string", stored = false)
    private String statusIdName;

    @Field("statusCode")
    private String statusCode;

    @Field("countOfStudent")
    private Integer countOfStudent;

    @Field("countOfConfirmedStudent")
    private Integer countOfConfirmedStudent;

    @Field("createdAt")
    private Date createdAt;

    @Field("modifiedAt")
    private Date modifiedAt;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getCourseScheduleId() {
        return courseScheduleId;
    }

    public void setCourseScheduleId(Integer courseScheduleId) {
        this.courseScheduleId = courseScheduleId;
    }

    public String getCourseScheduleNumber() {
        return courseScheduleNumber;
    }

    public void setCourseScheduleNumber(String courseScheduleNumber) {
        this.courseScheduleNumber = courseScheduleNumber;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseIdName() {
        return courseIdName;
    }

    public void setCourseIdName(String courseIdName) {
        this.courseIdName = courseIdName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public Integer getLanguageId() {
        return languageId;
    }

    public void setLanguageId(Integer languageId) {
        this.languageId = languageId;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public String getLanguageIdName() {
        return languageIdName;
    }

    public void setLanguageIdName(String languageIdName) {
        this.languageIdName = languageIdName;
    }

    public Boolean getEnableOvertime() {
        return enableOvertime != null && enableOvertime;
    }

    public void setEnableOvertime(Boolean enableOvertime) {
        this.enableOvertime = enableOvertime;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationIdName() {
        return locationIdName;
    }

    public void setLocationIdName(String locationIdName) {
        this.locationIdName = locationIdName;
    }

    public Integer getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(Integer instructorId) {
        this.instructorId = instructorId;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public String getInstructorIdName() {
        return instructorIdName;
    }

    public void setInstructorIdName(String instructorIdName) {
        this.instructorIdName = instructorIdName;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(Integer numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public Integer getAssessorId() {
        return assessorId;
    }

    public void setAssessorId(Integer assessorId) {
        this.assessorId = assessorId;
    }

    public String getAssessorName() {
        return assessorName;
    }

    public void setAssessorName(String assessorName) {
        this.assessorName = assessorName;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getStatusIdName() {
        return statusIdName;
    }

    public void setStatusIdName(String typeIdName) {
        this.statusIdName = typeIdName;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public Integer getCountOfStudent() {
        return countOfStudent;
    }

    public void setCountOfStudent(Integer countOfStudent) {
        this.countOfStudent = countOfStudent;
    }

    public Integer getCountOfConfirmedStudent() {
        return countOfConfirmedStudent;
    }

    public void setCountOfConfirmedStudent(Integer countOfConfirmedStudent) {
        this.countOfConfirmedStudent = countOfConfirmedStudent;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }
}
