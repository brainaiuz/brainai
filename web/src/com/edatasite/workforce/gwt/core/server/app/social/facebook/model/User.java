package com.edatasite.workforce.gwt.core.server.app.social.facebook.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Anvar Akramov on 10/5/17.
 */
public class User extends FacebookObject implements Serializable {

    private String id;
    private CoverPhoto cover;
    private String name;
    private String first_name;
    private String last_name;
    private String middle_name;
    private String name_format;
    private String email;
    private AgeRange ageRange;
    private String link;
    private String gender;
    private List<Device> devices;
    private String about;
    private String birthday;
    private Currency currency;
    private Locale locale;
    private Float timezone;
    private Date updated_time;
    private Boolean verified;
    private boolean installed;
    private String install_type;
    private String website;
    private String religion;
    private int test_group;

    private Location location;
    private Picture picture;
    /*private List<String> interestedIn;
    private boolean isIdentityVerified;
    private List<Reference> languages;
    private Reference location;
    private List<String> meetingFor;
    private String political;
    private String quotes;
    private String relationshipStatus;
    private String thirdPartyId;
    private boolean viewerCanSendGift;*/


    public User() {
    }

    public User(String id, String name, String firstName, String lastName, String gender, Locale locale) {
        this.id = id;
        this.name = name;
        this.first_name = firstName;
        this.last_name = lastName;
        this.gender = gender;
        this.locale = locale;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CoverPhoto getCover() {
        return cover;
    }

    public void setCover(CoverPhoto cover) {
        this.cover = cover;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getMiddle_name() {
        return middle_name;
    }

    public void setMiddle_name(String middle_name) {
        this.middle_name = middle_name;
    }

    public String getName_format() {
        return name_format;
    }

    public void setName_format(String name_format) {
        this.name_format = name_format;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public AgeRange getAgeRange() {
        return ageRange;
    }

    public void setAgeRange(AgeRange ageRange) {
        this.ageRange = ageRange;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public Float getTimezone() {
        return timezone;
    }

    public void setTimezone(Float timezone) {
        this.timezone = timezone;
    }

    public Date getUpdated_time() {
        return updated_time;
    }

    public void setUpdated_time(Date updated_time) {
        this.updated_time = updated_time;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public boolean isInstalled() {
        return installed;
    }

    public void setInstalled(boolean installed) {
        this.installed = installed;
    }

    public String getInstall_type() {
        return install_type;
    }

    public void setInstall_type(String install_type) {
        this.install_type = install_type;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public int getTest_group() {
        return test_group;
    }

    public void setTest_group(int test_group) {
        this.test_group = test_group;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }
}
