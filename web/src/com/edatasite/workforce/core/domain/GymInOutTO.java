package com.edatasite.workforce.core.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class GymInOutTO {
    private String userId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startdate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date enddate;

    public GymInOutTO() {}

    public GymInOutTO(String userId, Date startdate, Date enddate) {
        this.userId = userId;
        this.startdate = startdate;
        this.enddate = enddate;
    }

    public String getUserId() {
        return userId;
    }

    public Date getStartdate() {
        return startdate;
    }

    public Date getEnddate() {
        return enddate;
    }
}

