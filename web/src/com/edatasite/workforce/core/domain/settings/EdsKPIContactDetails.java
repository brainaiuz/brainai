package com.edatasite.workforce.core.domain.settings;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(schema = EdsScope.PUBLIC_SCHEMA, name = "kpicontactdetails")
public class EdsKPIContactDetails extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "phonenumber")
    private String phoneNumber;

    @Column(name = "demourl")
    private String demoURL;

    @Column(name = "countrycode")
    String countryCode;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDemoURL() {
        return demoURL;
    }

    public void setDemoURL(String demoURL) {
        this.demoURL = demoURL;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}
