package com.edatasite.workforce.rest.v3.release10.core.to;

import com.edatasite.workforce.gwt.core.server.rpc.FingerPrintUserItem;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

public class AttendanceDto {

    @NotNull(message = "Device Uid is required")
    @NotBlank(message = "Device Uid cannot be blank")
    private String deviceUid;
    @NotNull(message = "Device Name is required")
    @NotBlank(message = "Device Name cannot be blank")
    private String deviceName;
    private Boolean withStatus;
    private String dateFormat;
    private Integer projectId;
    private String projectName;
    private Integer taskId;
    private String taskName;
    private List<FingerPrintUserItem> data;


    public String getDeviceUid() {
        return deviceUid;
    }

    public void setDeviceUid(String deviceUid) {
        this.deviceUid = deviceUid;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public Boolean getWithStatus() {
        return withStatus;
    }

    public void setWithStatus(Boolean withStatus) {
        this.withStatus = withStatus;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public List<FingerPrintUserItem> getData() {
        return data;
    }

    public void setData(List<FingerPrintUserItem> data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return o instanceof AttendanceDto that
                && Objects.equals(deviceUid, that.deviceUid)
                && Objects.equals(deviceName, that.deviceName)
                && Objects.equals(withStatus, that.withStatus)
                && Objects.equals(dateFormat, that.dateFormat)
                && Objects.equals(projectId, that.projectId)
                && Objects.equals(taskId, that.taskId)
                && Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        int result = deviceUid != null ? deviceUid.hashCode() : 0;
        result = 31 * result + (deviceName != null ? deviceName.hashCode() : 0);
        result = 31 * result + (withStatus != null ? withStatus.hashCode() : 0);
        result = 31 * result + (dateFormat != null ? dateFormat.hashCode() : 0);
        result = 31 * result + (taskId != null ? taskId.hashCode() : 0);
        result = 31 * result + (data != null ? data.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AttendanceDto{" +
                "deviceUid='" + deviceUid + '\'' +
                ", deviceName='" + deviceName + '\'' +
                ", withStatus=" + withStatus +
                ", dateFormat='" + dateFormat + '\'' +
                ", taskId=" + taskId +
                ", data=" + data +
                '}';
    }
}
