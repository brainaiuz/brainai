package com.edatasite.workforce.gwt.availability.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

import java.io.Serializable;
import java.util.List;

public class InOutPairDto implements Serializable {

    private Integer inId;
    private Integer outId;
    private DateNonConvertable inTime;
    private DateNonConvertable outTime;
    private Long durationSeconds;
    private String pairStatus;
    private String inSnapshotLink;
    private String outSnapshotLink;
    private String inDevice;
    private String outDevice;
    private Integer adjustmentId;
    private List<String> inSnapshotLinks;
    private List<String> outSnapshotLinks;
    private Double inLatitude;
    private Double outLatitude;
    private Double inLongitude;
    private Double outLongitude;
    private SelectItem inProject;
    private SelectItem outProject;
    private SelectItem inTask;
    private SelectItem outTask;
    private String inSource;
    private String outSource;
    private Long dailyDuration;
    private String employeeName;
    private String position;
    private String inTerminalName;
    private String outTerminalName;
    private String inLocationName;
    private String outLocationName;

    public Integer getInId() {
        return inId;
    }

    public void setInId(Integer inId) {
        this.inId = inId;
    }

    public Integer getOutId() {
        return outId;
    }

    public void setOutId(Integer outId) {
        this.outId = outId;
    }

    public DateNonConvertable getInTime() {
        return inTime;
    }

    public void setInTime(DateNonConvertable inTime) {
        this.inTime = inTime;
    }

    public DateNonConvertable getOutTime() {
        return outTime;
    }

    public void setOutTime(DateNonConvertable outTime) {
        this.outTime = outTime;
    }

    public Long getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(Long durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public String getPairStatus() {
        return pairStatus;
    }

    public void setPairStatus(String pairStatus) {
        this.pairStatus = pairStatus;
    }

    public boolean isComplete() {
        return inTime != null && outTime != null;
    }

    public Long getDurationMinutes() {
        return durationSeconds;
    }

    public void setAdjustmentId(Integer adjustmentId) {
        this.adjustmentId = adjustmentId;
    }

    public String getInSnapshotLink() {
        return inSnapshotLink;
    }

    public void setInSnapshotLink(String inSnapshotLink) {
        this.inSnapshotLink = inSnapshotLink;
    }

    public String getOutSnapshotLink() {
        return outSnapshotLink;
    }

    public void setOutSnapshotLink(String outSnapshotLink) {
        this.outSnapshotLink = outSnapshotLink;
    }

    public String getInDevice() {
        return inDevice;
    }

    public void setInDevice(String inDevice) {
        this.inDevice = inDevice;
    }

    public String getOutDevice() {
        return outDevice;
    }

    public void setOutDevice(String outDevice) {
        this.outDevice = outDevice;
    }

    public Integer getAdjustmentId() {
        return adjustmentId;
    }

    public List<String> getInSnapshotLinks() {
        return inSnapshotLinks;
    }

    public void setInSnapshotLinks(List<String> inSnapshotLinks) {
        this.inSnapshotLinks = inSnapshotLinks;
    }

    public List<String> getOutSnapshotLinks() {
        return outSnapshotLinks;
    }

    public void setOutSnapshotLinks(List<String> outSnapshotLinks) {
        this.outSnapshotLinks = outSnapshotLinks;
    }

    public Double getInLatitude() {
        return inLatitude;
    }

    public void setInLatitude(Double inLatitude) {
        this.inLatitude = inLatitude;
    }

    public Double getOutLatitude() {
        return outLatitude;
    }

    public void setOutLatitude(Double outLatitude) {
        this.outLatitude = outLatitude;
    }

    public Double getInLongitude() {
        return inLongitude;
    }

    public void setInLongitude(Double inLongitude) {
        this.inLongitude = inLongitude;
    }

    public Double getOutLongitude() {
        return outLongitude;
    }

    public void setOutLongitude(Double outLongitude) {
        this.outLongitude = outLongitude;
    }

    public SelectItem getInProject() {
        return inProject;
    }

    public void setInProject(SelectItem inProject) {
        this.inProject = inProject;
    }

    public SelectItem getOutProject() {
        return outProject;
    }

    public void setOutProject(SelectItem outProject) {
        this.outProject = outProject;
    }

    public SelectItem getInTask() {
        return inTask;
    }

    public void setInTask(SelectItem inTask) {
        this.inTask = inTask;
    }

    public SelectItem getOutTask() {
        return outTask;
    }

    public void setOutTask(SelectItem outTask) {
        this.outTask = outTask;
    }

    public String getInSource() {
        return inSource != null ? inSource : "UNKNOWN";
    }

    public void setInSource(String inSource) {
        this.inSource = inSource;
    }

    public String getOutSource() {
        return outSource != null ? outSource : "UNKNOWN";
    }

    public void setOutSource(String outSource) {
        this.outSource = outSource;
    }

    public Long getDailyDuration() {
        return dailyDuration;
    }

    public void setDailyDuration(Long dailyDuration) {
        this.dailyDuration = dailyDuration;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getInTerminalName() {
        return inTerminalName;
    }

    public void setInTerminalName(String inTerminalName) {
        this.inTerminalName = inTerminalName;
    }

    public String getOutTerminalName() {
        return outTerminalName;
    }

    public void setOutTerminalName(String outTerminalName) {
        this.outTerminalName = outTerminalName;
    }

    public String getInLocationName() {
        return inLocationName;
    }

    public void setInLocationName(String inLocationName) {
        this.inLocationName = inLocationName;
    }

    public String getOutLocationName() {
        return outLocationName;
    }

    public void setOutLocationName(String outLocationName) {
        this.outLocationName = outLocationName;
    }
}