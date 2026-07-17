package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.rest.base.enums.DeviceStatusEnum;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by Farrukh Abdurakhmonov on 26.10.2017.
 */
@Entity
@Table(schema = EdsScope.PUBLIC_SCHEMA, name = "fingerPrintDeviceStatusHistory")
public class EdsFingerPrintDeviceStatusHistory extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "deviceStatus")
    @Enumerated(EnumType.STRING)
    private DeviceStatusEnum deviceStatus;

    @Column(name = "statusUpdateTime")
    private Date statusUpdateTime;

    @Column(name = "description")
    @Type(type = "text")
    private String description;

    @Column(name = "deviceUniqueKey")
    private String deviceUniqueKey;

    @Column(name = "deviceName")
    private String deviceName;


    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public DeviceStatusEnum getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(DeviceStatusEnum deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public Date getStatusUpdateTime() {
        return statusUpdateTime;
    }

    public void setStatusUpdateTime(Date statusUpdateTime) {
        this.statusUpdateTime = statusUpdateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDeviceUniqueKey() {
        return deviceUniqueKey;
    }

    public void setDeviceUniqueKey(String companyUniqueKey) {
        this.deviceUniqueKey = companyUniqueKey;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String companyBranchName) {
        this.deviceName = companyBranchName;
    }
}
