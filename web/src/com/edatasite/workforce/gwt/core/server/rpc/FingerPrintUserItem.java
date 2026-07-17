package com.edatasite.workforce.gwt.core.server.rpc;

import com.edatasite.workforce.core.domain.enums.FingerprintSource;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Dilshod Madrahimov on 8/31/15 8:14 PM
 */
public class FingerPrintUserItem implements Serializable {
    private String userId;
    private String userName;
    private String logTime;
    private String status;
    private Double latitude;
    private Double longitude;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<String> snapshots;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<String> snapshotLinks;
    private String note;
    private FingerprintSource source;
    private Boolean isAuto;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLogTime() {
        return logTime;
    }

    public void setLogTime(String logTime) {
        this.logTime = logTime;
    }

    public Date getLogDate(String dateFormatString) {
        try {
            if (dateFormatString == null || dateFormatString.isEmpty()) {
                dateFormatString = "dd.MM.yyyy HH:mm:ss";
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatString);
            return dateFormat.parse(getLogTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public List<String> getSnapshots() {
        return snapshots;
    }

    public void setSnapshots(List<String> snapshots) {
        this.snapshots = snapshots;
    }

    public List<String> getSnapshotLinks() {
        return snapshotLinks;
    }

    public void setSnapshotLinks(List<String> snapshotLinks) {
        this.snapshotLinks = snapshotLinks;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public FingerprintSource getSource() {
        return source;
    }

    public void setSource(FingerprintSource source) {
        this.source = source;
    }

    public Boolean getIsAuto() {
        return isAuto;
    }

    public void setIsAuto(Boolean isAuto) {
        this.isAuto = isAuto;
    }
}
