package com.edatasite.workforce.rest.v3.release10.trainingcenter.dto;

import com.edatasite.workforce.rest.v2.release10.core.to.crm.activity.RecurrenceTO;
import com.edatasite.workforce.rest.v3.release10.core.request.CustomFieldRequest;
import com.edatasite.workforce.rest.v3.release10.core.to.ItemDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CourseScheduleDto {
    private Integer id;
    private ItemDto location;
    private ItemDto language;
    private CourseDto course;
    private ItemDto instructor;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDate;
    private ItemDto assessor;
    private String duration;
    private Integer numberOfSeats;
    private boolean overtime = false;
    private Integer bookedStudentsCount;
    private List<ItemDto> bookedStudents;
    private RecurrenceTO recurrence;

    public CourseScheduleDto(Integer id, ItemDto location, ItemDto language, CourseDto course, ItemDto instructor, Date startDate, ItemDto assessor, String duration, Integer numberOfSeats, boolean overtime, Integer bookedStudentsCount, Integer confirmedStudentsCount) {
        this.id = id;
        this.location = location;
        this.language = language;
        this.course = course;
        this.instructor = instructor;
        this.startDate = startDate;
        this.assessor = assessor;
        this.duration = duration;
        this.numberOfSeats = numberOfSeats;
        this.overtime = overtime;
        this.bookedStudentsCount = bookedStudentsCount;
        this.confirmedStudentsCount = confirmedStudentsCount;
    }

    private Integer confirmedStudentsCount;
    @Valid
    private List<? extends CustomFieldRequest> customFields;


    public CourseScheduleDto(Integer id, ItemDto location, ItemDto language, CourseDto course, ItemDto instructor, Date startDate, ItemDto assessor, String duration, Integer numberOfSeats, boolean overtime, List<ItemDto> bookedStudents, Integer bookedStudentsCount) {
        this.id = id;
        this.location = location;
        this.language = language;
        this.course = course;
        this.instructor = instructor;
        this.startDate = startDate;
        this.assessor = assessor;
        this.duration = duration;
        this.numberOfSeats = numberOfSeats;
        this.overtime = overtime;
        this.bookedStudentsCount = bookedStudentsCount;
        this.bookedStudents = bookedStudents;
    }

    public CourseScheduleDto() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ItemDto getLocation() {
        return location;
    }

    public void setLocation(ItemDto location) {
        this.location = location;
    }

    public ItemDto getLanguage() {
        return language;
    }

    public void setLanguage(ItemDto language) {
        this.language = language;
    }

    public CourseDto getCourse() {
        return course;
    }

    public void setCourse(CourseDto course) {
        this.course = course;
    }

    public ItemDto getInstructor() {
        return instructor;
    }

    public void setInstructor(ItemDto instructor) {
        this.instructor = instructor;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public ItemDto getAssessor() {
        return assessor;
    }

    public void setAssessor(ItemDto assessor) {
        this.assessor = assessor;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Integer getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(Integer numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public boolean isOvertime() {
        return overtime;
    }

    public void setOvertime(boolean overtime) {
        this.overtime = overtime;
    }
    public List<? extends CustomFieldRequest> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(List<? extends CustomFieldRequest> customFields) {
        this.customFields = customFields;
    }

    public Integer getBookedStudentsCount() {
        return bookedStudentsCount;
    }

    public void setBookedStudentsCount(Integer bookedStudentsCount) {
        this.bookedStudentsCount = bookedStudentsCount;
    }

    public Integer getConfirmedStudentsCount() {
        return confirmedStudentsCount;
    }

    public void setConfirmedStudentsCount(Integer confirmedStudentsCount) {
        this.confirmedStudentsCount = confirmedStudentsCount;
    }

    public List<ItemDto> getBookedStudents() {
        return bookedStudents;
    }

    public void setBookedStudents(List<ItemDto> bookedStudents) {
        this.bookedStudents = bookedStudents;
    }

    public RecurrenceTO getRecurrence() {
        return recurrence;
    }

    public void setRecurrence(RecurrenceTO recurrence) {
        this.recurrence = recurrence;
    }

}
