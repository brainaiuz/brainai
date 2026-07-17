package com.edatasite.workforce.gwt.core.client.rpc.dashboard;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Dilshod
 * Date: 12.06.2009
 * Time: 21:15:38
 * To change this template use File | Settings | File Templates.
 */
public class DashboardTasks implements IsSerializable {

    private Integer employeeId;
    private String employeeName;
    private Integer notStarted = 0;
    private Integer inProgress = 0;
    private Integer completed = 0;
    private Integer waiting_for = 0;
    private Integer closed = 0;
    private Integer total;


    public DashboardTasks() {
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

    public Integer getNotStarted() {
        return notStarted;
    }

    public void setNotStarted(Integer notStarted) {
        this.notStarted = notStarted;
    }

    public Integer getInProgress() {
        return inProgress;
    }

    public void setInProgress(Integer inProgress) {
        this.inProgress = inProgress;
    }

    public Integer getCompleted() {
        return completed;
    }

    public void setCompleted(Integer completed) {
        this.completed = completed;
    }

    public Integer getWaiting_for() {
        return waiting_for;
    }

    public void setWaiting_for(Integer waiting_for) {
        this.waiting_for = waiting_for;
    }

    public Integer getClosed() {
        return closed;
    }

    public void setClosed(Integer closed) {
        this.closed = closed;
    }

    public Integer getTotal() {
        total = notStarted.intValue() + inProgress.intValue() + completed.intValue() + waiting_for.intValue() + closed.intValue();
        return total;
    }

   /* public void addNotStarted(Integer notStarted) {
        if (this.notStarted == null)
            this.notStarted = 0;
        this.notStarted += notStarted;
    }

    public void addInProgress(Integer inProgress) {
        if (this.notStarted == null)
            this.notStarted = 0;
        this.notStarted += notStarted;
    }

    public void addNotStarted(Integer notStarted) {
        if (this.notStarted == null)
            this.notStarted = 0;
        this.notStarted += notStarted;
    }

    public void addNotStarted(Integer notStarted) {
        if (this.notStarted == null)
            this.notStarted = 0;
        this.notStarted += notStarted;
    }
    */
}
