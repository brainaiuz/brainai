package com.edatasite.workforce.rest.v3.release10.trainingcenter.dto;

import com.edatasite.workforce.rest.v3.release10.core.to.ItemDto;

import java.util.List;

public class CourseDto extends ItemDto {

    private String duration;
    private String validity;
    private List<ItemDto> instructors;
    private List<ItemDto> prices;


        public CourseDto() {
        }

    public CourseDto(Integer id, String name, String duration, String validity, List<ItemDto> instructors, List<ItemDto> prices) {
        super(id, name);
        this.duration = duration;
        this.validity = validity;
        this.instructors = instructors;
        this.prices = prices;
    }

    public CourseDto(Integer id, String name, String duration, String validity) {
        super(id, name);
        this.duration = duration;
        this.validity = validity;
    }

    public List<ItemDto> getInstructors() {
        return instructors;
    }

    public void setInstructors(List<ItemDto> instructors) {
        this.instructors = instructors;
    }

    public List<ItemDto> getPrices() {
        return prices;
    }

    public void setPrices(List<ItemDto> prices) {
        this.prices = prices;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }
}
