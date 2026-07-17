package com.edatasite.workforce.rest.v3.release10.hrms.dto.mygov;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MyGovDependentDto {
    private String f_family;
    private String f_first_name;
    private String f_patronym;
    private String f_birth_day;
    private String f_pnfl;
    private String m_family;
    private String m_first_name;
    private String m_patronym;
    private String m_birth_day;
    private String m_pnfl;

    public String getF_family() {
        return f_family;
    }

    public void setF_family(String f_family) {
        this.f_family = f_family;
    }

    public String getF_first_name() {
        return f_first_name;
    }

    public void setF_first_name(String f_first_name) {
        this.f_first_name = f_first_name;
    }

    public String getF_patronym() {
        return f_patronym;
    }

    public void setF_patronym(String f_patronym) {
        this.f_patronym = f_patronym;
    }

    public String getF_birth_day() {
        return f_birth_day;
    }

    public void setF_birth_day(String f_birth_day) {
        this.f_birth_day = f_birth_day;
    }

    public String getF_pnfl() {
        return f_pnfl;
    }

    public void setF_pnfl(String f_pnfl) {
        this.f_pnfl = f_pnfl;
    }

    public String getM_family() {
        return m_family;
    }

    public void setM_family(String m_family) {
        this.m_family = m_family;
    }

    public String getM_first_name() {
        return m_first_name;
    }

    public void setM_first_name(String m_first_name) {
        this.m_first_name = m_first_name;
    }

    public String getM_patronym() {
        return m_patronym;
    }

    public void setM_patronym(String m_patronym) {
        this.m_patronym = m_patronym;
    }

    public String getM_birth_day() {
        return m_birth_day;
    }

    public void setM_birth_day(String m_birth_day) {
        this.m_birth_day = m_birth_day;
    }

    public String getM_pnfl() {
        return m_pnfl;
    }

    public void setM_pnfl(String m_pnfl) {
        this.m_pnfl = m_pnfl;
    }
}
