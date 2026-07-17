package com.edatasite.workforce.rest.v3.release10.hrms.dto.mygov;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MyGovPropertyDto {
    private String address;
    private Double total_area;
    private String obj_code;
    private String obj_name;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getTotal_area() {
        return total_area;
    }

    public void setTotal_area(Double total_area) {
        this.total_area = total_area;
    }

    public String getObj_code() {
        return obj_code;
    }

    public void setObj_code(String obj_code) {
        this.obj_code = obj_code;
    }

    public String getObj_name() {
        return obj_name;
    }

    public void setObj_name(String obj_name) {
        this.obj_name = obj_name;
    }
}
