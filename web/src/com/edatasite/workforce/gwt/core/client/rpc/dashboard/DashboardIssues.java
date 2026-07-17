package com.edatasite.workforce.gwt.core.client.rpc.dashboard;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Dilshod
 * Date: 12.06.2009
 * Time: 21:15:01
 * To change this template use File | Settings | File Templates.
 */
public class DashboardIssues implements IsSerializable {
    private Integer employeeId;
    private String employeeName;
    private Integer neww = 0;
    private Integer open = 0;
    private Integer under = 0;
    private Integer inProgress = 0;
    private Integer review = 0;
    private Integer resolved = 0;
    private Integer closed = 0;
    private Integer total;

    public DashboardIssues() {
    }

    public Integer getTotal() {
        total = neww.intValue() + open.intValue() + under.intValue() + inProgress.intValue() + review.intValue() + resolved.intValue() + closed.intValue();
        return total;
    }

    public Integer getEmployeeId() {

        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Integer getNeww() {
        return neww;
    }

    public void setNeww(Integer neww) {
        this.neww = neww;
    }

    public Integer getOpen() {
        return open;
    }

    public void setOpen(Integer open) {
        this.open = open;
    }

    public Integer getUnder() {
        return under;
    }

    public void setUnder(Integer under) {
        this.under = under;
    }

    public Integer getInProgress() {
        return inProgress;
    }

    public void setInProgress(Integer inProgress) {
        this.inProgress = inProgress;
    }

    public Integer getResolved() {
        return resolved;
    }

    public void setResolved(Integer resolved) {
        this.resolved = resolved;
    }

    public Integer getClosed() {
        return closed;
    }

    public void setClosed(Integer closed) {
        this.closed = closed;
    }

    public Integer getReview() {
        return review;
    }

    public void setReview(Integer review) {
        this.review = review;
    }
}