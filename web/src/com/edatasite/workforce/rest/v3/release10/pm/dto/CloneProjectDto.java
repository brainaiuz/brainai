package com.edatasite.workforce.rest.v3.release10.pm.dto;

import com.edatasite.workforce.rest.v3.release10.core.to.ItemDto;

/**
 * User: Akhror
 * Date: 07.07.2021
 */
public class CloneProjectDto {
    private ItemDto project;
    private boolean copyTasks;
    private boolean copyWorkStream;
    private boolean copyClient;
    private boolean copyProjectAssignments;
    private boolean copyTaskAssignments;
    private boolean copyTaskDates;
    private boolean copyLocation;
    private boolean resetTaskStatuses;
    private ItemDto status;

    public CloneProjectDto() {
    }

    public CloneProjectDto(ItemDto project, boolean copyTasks, boolean copyWorkStream, boolean copyClient, boolean copyProjectAssignments, boolean copyTaskAssignments, boolean copyTaskDates, boolean copyLocation, boolean resetTaskStatuses, ItemDto status) {
        this.project = project;
        this.copyTasks = copyTasks;
        this.copyWorkStream = copyWorkStream;
        this.copyClient = copyClient;
        this.copyProjectAssignments = copyProjectAssignments;
        this.copyTaskAssignments = copyTaskAssignments;
        this.copyTaskDates = copyTaskDates;
        this.copyLocation = copyLocation;
        this.resetTaskStatuses = resetTaskStatuses;
        this.status = status;
    }

    public ItemDto getProject() {
        return project;
    }

    public void setProject(ItemDto project) {
        this.project = project;
    }

    public boolean isCopyTasks() {
        return copyTasks;
    }

    public void setCopyTasks(boolean copyTasks) {
        this.copyTasks = copyTasks;
    }

    public boolean isCopyWorkStream() {
        return copyWorkStream;
    }

    public void setCopyWorkStream(boolean copyWorkStream) {
        this.copyWorkStream = copyWorkStream;
    }

    public boolean isCopyClient() {
        return copyClient;
    }

    public void setCopyClient(boolean copyClient) {
        this.copyClient = copyClient;
    }

    public boolean isCopyProjectAssignments() {
        return copyProjectAssignments;
    }

    public void setCopyProjectAssignments(boolean copyProjectAssignments) {
        this.copyProjectAssignments = copyProjectAssignments;
    }

    public boolean isCopyTaskAssignments() {
        return copyTaskAssignments;
    }

    public void setCopyTaskAssignments(boolean copyTaskAssignments) {
        this.copyTaskAssignments = copyTaskAssignments;
    }

    public boolean isCopyTaskDates() {
        return copyTaskDates;
    }

    public void setCopyTaskDates(boolean copyTaskDates) {
        this.copyTaskDates = copyTaskDates;
    }

    public boolean isCopyLocation() {
        return copyLocation;
    }

    public void setCopyLocation(boolean copyLocation) {
        this.copyLocation = copyLocation;
    }

    public boolean isResetTaskStatuses() {
        return resetTaskStatuses;
    }

    public void setResetTaskStatuses(boolean resetTaskStatuses) {
        this.resetTaskStatuses = resetTaskStatuses;
    }

    public ItemDto getStatus() {
        return status;
    }

    public void setStatus(ItemDto status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CloneProjectDto)) return false;

        CloneProjectDto that = (CloneProjectDto) o;

        if (isCopyTasks() != that.isCopyTasks()) return false;
        if (isCopyWorkStream() != that.isCopyWorkStream()) return false;
        if (isCopyClient() != that.isCopyClient()) return false;
        if (isCopyProjectAssignments() != that.isCopyProjectAssignments()) return false;
        if (isCopyTaskAssignments() != that.isCopyTaskAssignments()) return false;
        if (isCopyTaskDates() != that.isCopyTaskDates()) return false;
        if (isCopyLocation() != that.isCopyLocation()) return false;
        if (isResetTaskStatuses() != that.isResetTaskStatuses()) return false;
        if (getProject() != null ? !getProject().equals(that.getProject()) : that.getProject() != null) return false;
        if (getStatus() != null ? !getStatus().equals(that.getStatus()) : that.getStatus() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getProject() != null ? getProject().hashCode() : 0;
        result = 31 * result + (isCopyTasks() ? 1 : 0);
        result = 31 * result + (isCopyWorkStream() ? 1 : 0);
        result = 31 * result + (isCopyClient() ? 1 : 0);
        result = 31 * result + (isCopyProjectAssignments() ? 1 : 0);
        result = 31 * result + (isCopyTaskAssignments() ? 1 : 0);
        result = 31 * result + (isCopyTaskDates() ? 1 : 0);
        result = 31 * result + (isCopyLocation() ? 1 : 0);
        result = 31 * result + (isResetTaskStatuses() ? 1 : 0);
        result = 31 * result + (getStatus() != null ? getStatus().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CloneProjectDto{" +
                "project=" + project +
                ", copyTasks=" + copyTasks +
                ", copyWorkStream=" + copyWorkStream +
                ", copyClient=" + copyClient +
                ", copyProjectAssignments=" + copyProjectAssignments +
                ", copyTaskAssignments=" + copyTaskAssignments +
                ", copyTaskDates=" + copyTaskDates +
                ", copyLocation=" + copyLocation +
                ", resetTaskStatuses=" + resetTaskStatuses +
                ", status=" + status +
                '}';
    }
}
