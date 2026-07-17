package com.edatasite.workforce.gwt.hrms.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: unni
 * Date: Dec 2, 2009
 * Time: 12:15:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class AwardItem implements IsSerializable {
    private Integer objectId;
    private Integer employeeId;
    private String awardName;
    private DateNonConvertable issueDate;
    private DateNonConvertable expiryDate;
    private String description;
    private SelectItem country;
    private String city;

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getAwardName() {
        return awardName;
    }

    public void setAwardName(String awardName) {
        this.awardName = awardName;
    }

    public DateNonConvertable getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(DateNonConvertable issueDate) {
        this.issueDate = issueDate;
    }

    public DateNonConvertable getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(DateNonConvertable expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SelectItem getCountry() {
        return country;
    }

    public void setCountry(SelectItem country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
