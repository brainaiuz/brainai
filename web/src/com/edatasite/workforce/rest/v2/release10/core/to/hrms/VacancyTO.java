package com.edatasite.workforce.rest.v2.release10.core.to.hrms;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.base.SelectItemTO;
import com.edatasite.workforce.rest.v2.release10.core.to.pm.task.CustomFieldsTO;

import java.util.ArrayList;

/**
 * Created by Dilsh0d on 10/28/2017.
 */
public class VacancyTO extends ResponseData {
    private Integer vacancy_id;
    private String vacancy_number;
    private String vacancy_job_title;
    private String vacancy_start_date;
    private String vacancy_end_date;
    private SelectItemTO vacancy_status;
    private PositionTO vacancy_position;
    private String vacancy_description;
    private String vacancy_job_requirements;
    private ArrayList<CustomFieldsTO> custom_fields;

    public Integer getVacancy_id() {
        return vacancy_id;
    }

    public void setVacancy_id(Integer vacancy_id) {
        this.vacancy_id = vacancy_id;
    }

    public String getVacancy_number() {
        return vacancy_number;
    }

    public void setVacancy_number(String vacancy_number) {
        this.vacancy_number = vacancy_number;
    }

    public String getVacancy_start_date() {
        return vacancy_start_date;
    }

    public void setVacancy_start_date(String vacancy_start_date) {
        this.vacancy_start_date = vacancy_start_date;
    }

    public String getVacancy_end_date() {
        return vacancy_end_date;
    }

    public void setVacancy_end_date(String vacancy_end_date) {
        this.vacancy_end_date = vacancy_end_date;
    }

    public String getVacancy_job_title() {
        return vacancy_job_title;
    }

    public void setVacancy_job_title(String vacancy_job_title) {
        this.vacancy_job_title = vacancy_job_title;
    }

    public SelectItemTO getVacancy_status() {
        return vacancy_status;
    }

    public void setVacancy_status(SelectItemTO vacancy_status) {
        this.vacancy_status = vacancy_status;
    }

    public PositionTO getVacancy_position() {
        return vacancy_position;
    }

    public void setVacancy_position(PositionTO vacancy_position) {
        this.vacancy_position = vacancy_position;
    }

    public String getVacancy_description() {
        return vacancy_description;
    }

    public void setVacancy_description(String vacancy_description) {
        this.vacancy_description = vacancy_description;
    }

    public String getVacancy_job_requirements() {
        return vacancy_job_requirements;
    }

    public void setVacancy_job_requirements(String vacancy_job_requirements) {
        this.vacancy_job_requirements = vacancy_job_requirements;
    }

    public ArrayList<CustomFieldsTO> getCustom_fields() {
        return custom_fields;
    }

    public void setCustom_fields(ArrayList<CustomFieldsTO> custom_fields) {
        this.custom_fields = custom_fields;
    }
}
