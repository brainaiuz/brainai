package com.edatasite.workforce.gwt.core.client.rpc.task;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Abdulaziz
 * Date: 12.08.2009
 * Time: 14:58:54
 * To change this template use File | Settings | File Templates.
 */
public class CloneTaskItem implements IsSerializable {
    private boolean adjustByProjectStartDate = false;
    private boolean copyDocuments = false;
    private boolean copyTaskAssignments = false;
    private Integer status;
    private Date startDate;

    public boolean isAdjustByProjectStartDate() {
        return adjustByProjectStartDate;
    }

    public void setAdjustByProjectStartDate(boolean adjustByProjectStartDate) {
        this.adjustByProjectStartDate = adjustByProjectStartDate;
    }

    public boolean isCopyTaskAssignments() {
        return copyTaskAssignments;
    }

    public void setCopyTaskAssignments(boolean copyTaskAssignments) {
        this.copyTaskAssignments = copyTaskAssignments;
    }

    public boolean isCopyDocuments() {
        return copyDocuments;
    }

    public void setCopyDocuments(boolean copyDocuments) {
        this.copyDocuments = copyDocuments;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
}
