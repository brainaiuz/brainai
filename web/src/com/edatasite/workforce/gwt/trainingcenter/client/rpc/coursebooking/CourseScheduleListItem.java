package com.edatasite.workforce.gwt.trainingcenter.client.rpc.coursebooking;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: dilsh0d
 * Date: 14/08/12
 * Time: 21:22
 * To change this template use File | Settings | File Templates.
 */
public class CourseScheduleListItem extends SelectItem {
    private String itemUUID;
    private Integer courseScheduleId;
    private Integer instructoId;
    private String instructorName;
    private Integer droppableStudentCount = 0;
    private Integer numOfSeatsCount = 0;
    private Integer attendStudentCount = 0;
    private Date startDate;
    private Date endDate;

    private String courseName;
    private String courseCode;

    public String getItemUUID() {
        return itemUUID;
    }

    public void setItemUUID(String itemUUID) {
        this.itemUUID = itemUUID;
    }

    public Integer getCourseScheduleId() {
        return courseScheduleId;
    }

    public void setCourseScheduleId(Integer courseScheduleId) {
        this.courseScheduleId = courseScheduleId;
    }

    public Integer getInstructoId() {
        return instructoId;
    }

    public void setInstructoId(Integer instructoId) {
        this.instructoId = instructoId;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public Integer getDroppableStudentCount() {
        return droppableStudentCount;
    }

    public void setDroppableStudentCount(Integer droppableStudentCount) {
        this.droppableStudentCount = droppableStudentCount;
    }

    public Integer getNumOfSeatsCount() {
        return numOfSeatsCount;
    }

    public void setNumOfSeatsCount(Integer numOfSeatsCount) {
        this.numOfSeatsCount = numOfSeatsCount;
    }

    public Integer getAttendStudentCount() {
        return attendStudentCount;
    }

    public void setAttendStudentCount(Integer attendStudentCount) {
        this.attendStudentCount = attendStudentCount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void incrementCourseStudentCount() {
        attendStudentCount++;
    }

    public void decrementCourseStudentCount() {
        attendStudentCount--;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public boolean validateDatePeriod(CourseScheduleListItem currentCourseScheduleItem) {
        return (currentCourseScheduleItem.getEndDate().compareTo(startDate) < 0 || currentCourseScheduleItem.getEndDate().compareTo(startDate) == 0)
                || (currentCourseScheduleItem.getStartDate().compareTo(endDate) == 0 || currentCourseScheduleItem.getStartDate().compareTo(endDate) > 0);
    }

    public int getAvailableNumbOfSeatsCount() {
        numOfSeatsCount = (numOfSeatsCount == null) ? 0 : numOfSeatsCount;
        attendStudentCount = (attendStudentCount == null) ? 0 : attendStudentCount;

        return (numOfSeatsCount - attendStudentCount);
    }
}
