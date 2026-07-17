package com.edatasite.workforce.core.domain.trainingcenter;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.File;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Sherzod
 * Date: 11/6/12
 * Time: 12:43 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PUBLIC_SCHEMA, name = "tcscheduledtask")
public class EdsTCScheduledTask extends EdsObject {

    public static final String TC_SCHEDULE_FOLDER_NAME = "tc_scheduled_task_files";

    public static Integer STATUS_PENDING = 0;
    public static Integer STATUS_PDF_GENERATED = 1;
    public static Integer STATUS_ZIP_IN_PROGRESS = 2;
    public static Integer STATUS_ZIPPED = 3;
    public static Integer STATUS_COMPLETED = 4;
    public static Integer STATUS_FAILED = 5;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private Date periodStart;
    private Date periodEnd;

    private Integer companyID;
    private Integer userID;
    private Integer customerID;
//    private Integer locationID;

    private Integer status = STATUS_PENDING;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Date getPeriodStart() {
        return periodStart;
    }

    public void setPeriodStart(Date periodStart) {
        this.periodStart = periodStart;
    }

    public Date getPeriodEnd() {
        return periodEnd;
    }

    public void setPeriodEnd(Date periodEnd) {
        this.periodEnd = periodEnd;
    }

    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Integer getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Integer customerID) {
        this.customerID = customerID;
    }

//    public Integer getLocationID() {
//        return locationID;
//    }
//
//    public void setLocationID(Integer locationID) {
//        this.locationID = locationID;
//    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getFolderURL() {
        return getFolderPath() + getObjectID();
    }

    public String getZipFileURL() {
        return getFolderPath() + getObjectID() + ".zip";
    }

    private String getFolderPath() {
        String path = getClass().getResource(File.separator).getPath();
        if (path.toLowerCase().contains("web-inf")) {
            int index = path.toLowerCase().indexOf("web-inf");
            path = path.substring(0, index);
        }
        return path + TC_SCHEDULE_FOLDER_NAME + File.separator;
    }
}
