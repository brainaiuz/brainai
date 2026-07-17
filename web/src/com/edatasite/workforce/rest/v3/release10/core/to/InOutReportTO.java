package com.edatasite.workforce.rest.v3.release10.core.to;

import java.util.Date;

public class InOutReportTO {
            private Integer employeeId;
    private Date startDate;
            private  Date endDate;
    private String status;
            private Integer tmeSlotId;

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getTmeSlotId() {
        return tmeSlotId;
    }

    public void setTmeSlotId(Integer tmeSlotId) {
        this.tmeSlotId = tmeSlotId;
    }
}
