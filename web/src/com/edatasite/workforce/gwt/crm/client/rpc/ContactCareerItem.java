package com.edatasite.workforce.gwt.crm.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 01.12.2010
 * Time: 20:41:52
 * To change this template use File | Settings | File Templates.
 */
public class ContactCareerItem implements IsSerializable {

    private Integer careerID;
    private String city;
    private String companyName;
    private Integer contactID;
    private Integer countryID;
    private String countryName;
    private SelectItem[] countries;
    private Integer industryID;
    private String industryName;
    private SelectItem[] industries;
    private boolean isCurrentYear;
    private String jobTitle;
    private Date fromYear;
    private Date toYear;

    public Integer getCareerID() {
        return careerID;
    }

    public void setCareerID(Integer careerID) {
        this.careerID = careerID;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Integer getContactID() {
        return contactID;
    }

    public void setContactID(Integer contactID) {
        this.contactID = contactID;
    }

    public Integer getCountryID() {
        return countryID;
    }

    public void setCountryID(Integer countryID) {
        this.countryID = countryID;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public SelectItem[] getCountries() {
        return countries;
    }

    public void setCountries(SelectItem[] countries) {
        this.countries = countries;
    }

    public Integer getIndustryID() {
        return industryID;
    }

    public void setIndustryID(Integer industryID) {
        this.industryID = industryID;
    }

    public String getIndustryName() {
        return industryName;
    }

    public void setIndustryName(String industryName) {
        this.industryName = industryName;
    }

    public SelectItem[] getIndustries() {
        return industries;
    }

    public void setIndustries(SelectItem[] industries) {
        this.industries = industries;
    }

    public boolean isCurrentYear() {
        return isCurrentYear;
    }

    public void setCurrentYear(boolean currentYear) {
        isCurrentYear = currentYear;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public Date getFromYear() {
        return fromYear;
    }

    public void setFromYear(Date fromYear) {
        this.fromYear = fromYear;
    }

    public Date getToYear() {
        return toYear;
    }

    public void setToYear(Date toYear) {
        this.toYear = toYear;
    }
}
