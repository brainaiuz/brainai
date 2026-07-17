package com.edatasite.workforce.rest.v3.release10.core.to.crm;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.Date;

/**
 * User: Abror Abdukadirov
 * Date: 13.02.2020 17:45
 */
public class CrmActivityDTO extends ResponseData {
    private Integer kpi_event_id;
    private String subject;
    private Date start_date;

    public CrmActivityDTO(Integer kpi_event_id, String subject, Date start_date) {
        this.kpi_event_id = kpi_event_id;
        this.subject = subject;
        this.start_date = start_date;
    }

    public Integer getKpi_event_id() {
        return kpi_event_id;
    }

    public void setKpi_event_id(Integer kpi_event_id) {
        this.kpi_event_id = kpi_event_id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }
}
