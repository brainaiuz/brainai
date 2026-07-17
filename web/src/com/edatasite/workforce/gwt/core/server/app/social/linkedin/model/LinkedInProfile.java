package com.edatasite.workforce.gwt.core.server.app.social.linkedin.model;

import java.io.Serial;
import java.io.Serializable;

/**
 * Created by Anvar Akramov on 10/6/17.
 */
public class LinkedInProfile extends LinkedInObject implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String id;

    private String firstName;

    private String lastName;

    private String headline;

    private String industry;

    private String emailAddress;

    private UrlResource siteStandardProfileRequest;

    private String publicProfileUrl;

    private String profilePictureUrl;

    private String pictureUrl;

    private PictureUrl pictureUrls;

    private String summary;

    public LinkedInProfile() {

    }

    public LinkedInProfile(String id, String firstName, String lastName, String headline, String industry, String publicProfileUrl, UrlResource siteStandardProfileRequest, String profilePictureUrl) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.headline = headline;
        this.industry = industry;
        this.publicProfileUrl = publicProfileUrl;
        this.siteStandardProfileRequest = siteStandardProfileRequest;
        this.profilePictureUrl = profilePictureUrl;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getHeadline() {
        return headline;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getIndustry() {
        return industry;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setSiteStandardProfileRequest(UrlResource siteStandardProfileRequest) {
        this.siteStandardProfileRequest = siteStandardProfileRequest;
    }

    public UrlResource getSiteStandardProfileRequest() {
        return siteStandardProfileRequest;
    }

    public void setPublicProfileUrl(String publicProfileUrl) {
        this.publicProfileUrl = publicProfileUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getPublicProfileUrl() {
        return publicProfileUrl;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public PictureUrl getPictureUrls() {
        return pictureUrls;
    }

    public void setPictureUrls(PictureUrl pictureUrls) {
        this.pictureUrls = pictureUrls;
    }
}
