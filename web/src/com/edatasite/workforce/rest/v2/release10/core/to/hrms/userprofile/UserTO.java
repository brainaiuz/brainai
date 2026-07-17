package com.edatasite.workforce.rest.v2.release10.core.to.hrms.userprofile;

import com.edatasite.workforce.rest.base.enums.NameOrder;
import com.edatasite.workforce.rest.v2.release10.core.to.auth.PhoneTO;
import com.edatasite.workforce.rest.v2.release10.core.to.auth.RoleTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.base.SelectItemTO;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.LanguagesDto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dilsh0d on 9/25/2017.
 */
public class UserTO extends ResponseData {

    private Integer id;
    private String email;
    private String user_name;
    private String firstName;
    private String lastName;
    private String middleName;
    private String avatar_image;
    private String company_name;
    private String user_type;
    private Integer unread_notifications;
    private SubscriptionInfo subscription_info;
    //    private TrialInfoTO trial_info;
    private PhoneTO phone;
    private String birthDate;
    private List<RoleTO> roles;

    private SelectItemTO position;
    private Integer companyId;
    private String lang;
    private boolean isForRelease = true;
    private NameOrder nameOrder;
    private Integer overallDatePickerWeekStart;
    private Integer countryZoneId;
    private String shortDateFormat;
    private String longDateFormat;
    private ArrayList<LanguagesDto> languages;
    private Integer locationId;


    public UserTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getAvatar_image() {
        return avatar_image;
    }

    public void setAvatar_image(String avatar_image) {
        this.avatar_image = avatar_image;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public Integer getUnread_notifications() {
        return unread_notifications;
    }

    public void setUnread_notifications(Integer unread_notifications) {
        this.unread_notifications = unread_notifications;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public SubscriptionInfo getSubscription_info() {
        return subscription_info;
    }

    public void setSubscription_info(SubscriptionInfo subscription_info) {
        this.subscription_info = subscription_info;
    }

    /*public TrialInfoTO getTrial_info() {
        return trial_info;
    }

    public void setTrial_info(TrialInfoTO trial_info) {
        this.trial_info = trial_info;
    }*/

    public PhoneTO getPhone() {
        return phone;
    }

    public void setPhone(PhoneTO phone) {
        this.phone = phone;
    }

    public List<RoleTO> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleTO> roles) {
        this.roles = roles;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public SelectItemTO getPosition() {
        return position;
    }

    public void setPosition(SelectItemTO position) {
        this.position = position;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public boolean isForRelease() {
        return isForRelease;
    }

    public void setForRelease(boolean forRelease) {
        isForRelease = forRelease;
    }

    public NameOrder getNameOrder() {
        return nameOrder;
    }

    public void setNameOrder(NameOrder nameOrder) {
        this.nameOrder = nameOrder;
    }

    public Integer getOverallDatePickerWeekStart() {
        return overallDatePickerWeekStart;
    }

    public void setOverallDatePickerWeekStart(Integer overallDatePickerWeekStart) {
        this.overallDatePickerWeekStart = overallDatePickerWeekStart;
    }

    public Integer getCountryZoneId() {
        return countryZoneId;
    }

    public void setCountryZoneId(Integer countryZoneId) {
        this.countryZoneId = countryZoneId;
    }

    public String getShortDateFormat() {
        return shortDateFormat;
    }

    public void setShortDateFormat(String shortDateFormat) {
        this.shortDateFormat = shortDateFormat;
    }

    public String getLongDateFormat() {
        return longDateFormat;
    }

    public void setLongDateFormat(String longDateFormat) {
        this.longDateFormat = longDateFormat;
    }
    public ArrayList<LanguagesDto> getLanguages() {
        return languages;
    }

    public void setLanguages(ArrayList<LanguagesDto> languages) {
        this.languages = languages;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Integer getLocationId() {
        return locationId;
    }
}
