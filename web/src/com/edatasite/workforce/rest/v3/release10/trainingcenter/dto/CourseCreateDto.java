package com.edatasite.workforce.rest.v3.release10.trainingcenter.dto;

import java.util.List;

public class CourseCreateDto {
    private Integer objectID;
    private String name;
    private Integer duration;
    private Integer validity;
    private String description;
    private String otherPrerequisites;
    private Integer subjectId;
    private List<Integer> instructorIds;
    private List<LocationPriceDto> locationPrice;
    private String number;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getValidity() {
        return validity;
    }

    public void setValidity(Integer validity) {
        this.validity = validity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOtherPrerequisites() {
        return otherPrerequisites;
    }

    public void setOtherPrerequisites(String otherPrerequisites) {
        this.otherPrerequisites = otherPrerequisites;
    }

    public Integer getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Integer subjectId) {
        this.subjectId = subjectId;
    }

    public List<Integer> getInstructorIds() {
        return instructorIds;
    }

    public void setInstructorIds(List<Integer> instructorIds) {
        this.instructorIds = instructorIds;
    }

    public List<LocationPriceDto> getLocationPrice() {
        return locationPrice;
    }

    public void setLocationPrice(List<LocationPriceDto> locationPrice) {
        this.locationPrice = locationPrice;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
