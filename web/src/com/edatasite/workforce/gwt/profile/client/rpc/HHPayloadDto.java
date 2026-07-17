package com.edatasite.workforce.gwt.profile.client.rpc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HHPayloadDto {
    private String employer_id;
    private String response_date;
    private String resume_id;
    private String topic_id;
    private String vacancy_id;

    public String getEmployer_id() {
        return employer_id;
    }

    public void setEmployer_id(String employer_id) {
        this.employer_id = employer_id;
    }

    public String getResponse_date() {
        return response_date;
    }

    public void setResponse_date(String response_date) {
        this.response_date = response_date;
    }

    public String getResume_id() {
        return resume_id;
    }

    public void setResume_id(String resume_id) {
        this.resume_id = resume_id;
    }

    public String getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(String topic_id) {
        this.topic_id = topic_id;
    }

    public String getVacancy_id() {
        return vacancy_id;
    }

    public void setVacancy_id(String vacancy_id) {
        this.vacancy_id = vacancy_id;
    }
}
