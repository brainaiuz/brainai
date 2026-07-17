package com.edatasite.workforce.core.domain.reporting;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(schema = EdsScope.PUBLIC_SCHEMA, name = "reportingTest")
public class EdsReportingTest extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "report_id")
    private Integer reportID;

    @Column(name = "company_id")
    private Integer companyID;

    @Column(name = "is_success")
    private Boolean isSuccess;

    @Column(name = "last_exception")
    @Type(type = "text")
    private String lastException;

    @Column(name = "time_spent")
    private long timeSpent;

    @Column(name = "tested_date")
    private Date testedDate;

    @Column(name = "report_name")
    private String reportName;

    @Column(name = "module_name")
    private String moduleName;


    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getReportID() {
        return reportID;
    }

    public void setReportID(Integer reportID) {
        this.reportID = reportID;
    }

    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
    }

    public Boolean getSuccess() {
        return isSuccess;
    }

    public void setSuccess(Boolean success) {
        isSuccess = success;
    }

    public String getLastException() {
        return lastException;
    }

    public void setLastException(String lastException) {
        this.lastException = lastException;
    }

    public long getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(long timeSpent) {
        this.timeSpent = timeSpent;
    }

    public Date getTestedDate() {
        return testedDate;
    }

    public void setTestedDate(Date testedDate) {
        this.testedDate = testedDate;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
