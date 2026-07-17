package com.edatasite.workforce.rest.v3.release10.hrms.dto.mygov;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MyGovMarriageDto {
    private String h_family;
    private String h_first_name;
    private String h_patronym;
    private String h_birth_day;
    private String h_pnfl;
    private String w_family;
    private String w_family_after;
    private String w_first_name;
    private String w_patronym;
    private String w_birth_day;
    private String w_pnfl;

    public String getH_family() {
        return h_family;
    }

    public void setH_family(String h_family) {
        this.h_family = h_family;
    }

    public String getH_first_name() {
        return h_first_name;
    }

    public void setH_first_name(String h_first_name) {
        this.h_first_name = h_first_name;
    }

    public String getH_patronym() {
        return h_patronym;
    }

    public void setH_patronym(String h_patronym) {
        this.h_patronym = h_patronym;
    }

    public String getH_birth_day() {
        return h_birth_day;
    }

    public void setH_birth_day(String h_birth_day) {
        this.h_birth_day = h_birth_day;
    }

    public String getH_pnfl() {
        return h_pnfl;
    }

    public void setH_pnfl(String h_pnfl) {
        this.h_pnfl = h_pnfl;
    }

    public String getW_family() {
        return w_family;
    }

    public void setW_family(String w_family) {
        this.w_family = w_family;
    }

    public String getW_first_name() {
        return w_first_name;
    }

    public void setW_first_name(String w_first_name) {
        this.w_first_name = w_first_name;
    }

    public String getW_patronym() {
        return w_patronym;
    }

    public void setW_patronym(String w_patronym) {
        this.w_patronym = w_patronym;
    }

    public String getW_birth_day() {
        return w_birth_day;
    }

    public void setW_birth_day(String w_birth_day) {
        this.w_birth_day = w_birth_day;
    }

    public String getW_pnfl() {
        return w_pnfl;
    }

    public void setW_pnfl(String w_pnfl) {
        this.w_pnfl = w_pnfl;
    }

    public String getW_family_after() {
        return w_family_after;
    }

    public void setW_family_after(String w_family_after) {
        this.w_family_after = w_family_after;
    }
}
