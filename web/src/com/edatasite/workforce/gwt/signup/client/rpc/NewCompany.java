package com.edatasite.workforce.gwt.signup.client.rpc;

import com.edatasite.workforce.gwt.core.client.enums.RegistrationTypeEnum;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class NewCompany implements Serializable {
    private Integer companyId;
    private String name;
    private String adminFName;
    private String adminLName;
    private String adminEmail;
    private String adminPassword;
    private String adminSocialImageUrl;
    private String socialUserName;
    private boolean adminActive = false;
    private String promoCode;
    private Integer countryID;
    private Integer users;

    private String countryName;
    private String countryCode;//US,AF
    private Integer stateID;
    private String phone;
    private String callCode;
    private Integer workArea;
    private RegistrationTypeEnum registrationType;
    private String pricingPackage;
    private String industry;
    private Integer employeeCount;

    private String activationKey;
    private boolean existingUser;

    private String signedUpPage;
    private boolean setUp = false;
    private Integer currencyID;
    private boolean active;
    private String clientSingUpIPAddress;
    private Boolean agreeWithCondition = false;
    private String googleAccessToken;
    private Boolean fromFederatedLogin = false; //if user signup from google,yahoo.. =true
    private String googleAppsDomain;
    private String googleAppsSection;
    private String companySignedUpFrom;
    private boolean isUk = false;
    private String locale;
    private String theme;
    private String value1;
    private String value2;
    private String value3;
    //Used in when sign up with google account is used
    private String host;
    private String serviceId;
    private String parentIframeUrl;
    private String paymentMethod;
    private String utm_campaign;
    private String utm_keyword;
    private String utm_medium;
    private String utm_content;
    private String utm_source;
    private String utm_btn;
    private String utm_term;
    private String selectedApps;
    private String redirected;
    private String referrer;
    private String gclid;
    private boolean redirectToSettings;
    private boolean reset;
    private boolean sendNotification = false;
    private String tfData;

    public String getParentIframeUrl() {
        return parentIframeUrl;
    }

    public void setParentIframeUrl(String parentIframeUrl) {
        this.parentIframeUrl = parentIframeUrl;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getLocale() {
//        if (locale == null) {
//            return Locale.ENGLISH.getLanguage();
//        }
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Integer getCurrencyID() {
        return currencyID;
    }

    public void setCurrencyID(Integer currencyID) {
        this.currencyID = currencyID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdminFName() {
        return adminFName;
    }

    public void setAdminFName(String adminFName) {
        this.adminFName = adminFName;
    }

    public String getAdminLName() {
        return adminLName;
    }

    public void setAdminLName(String adminLName) {
        this.adminLName = adminLName;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public Integer getCountryID() {
        return countryID;
    }

    public void setCountryID(Integer countryID) {
        this.countryID = countryID;
    }

    public Integer getUsers() {
        return users;
    }

    public void setUsers(Integer users) {
        this.users = users;
    }

    public String getPricingPackage() {
        return pricingPackage;
    }

    public void setPricingPackage(String pricingPackage) {
        this.pricingPackage = pricingPackage;
    }

    public String getActivationKey() {
        return activationKey;
    }

    public void setActivationKey(String activationKey) {
        this.activationKey = activationKey;
    }

    public boolean isExistingUser() {
        return existingUser;
    }

    public void setExistingUser(boolean existingUser) {
        this.existingUser = existingUser;
    }

    public Integer getStateID() {
        return stateID;
    }

    public void setStateID(Integer stateID) {
        this.stateID = stateID;
    }

    public Integer getWorkArea() {
        return workArea;
    }

    public void setWorkArea(Integer workArea) {
        this.workArea = workArea;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSignedUpPage() {
        return signedUpPage;
    }

    public void setSignedUpPage(String signedUpPage) {
        this.signedUpPage = signedUpPage;
    }

    public void setSetUp(boolean setUp) {
        this.setUp = setUp;
    }

    public boolean isSetUp() {
        return setUp;
    }

    public boolean isAdminActive() {
        return adminActive;
    }

    public void setAdminActive(boolean adminActive) {
        this.adminActive = adminActive;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getClientSingUpIPAddress() {
        return clientSingUpIPAddress;
    }

    public void setClientSingUpIPAddress(String clientSingUpIPAddress) {
        this.clientSingUpIPAddress = clientSingUpIPAddress;
    }

    public Boolean getAgreeWithCondition() {
        return agreeWithCondition;
    }

    public void setAgreeWithCondition(Boolean agreeWithCondition) {
        this.agreeWithCondition = agreeWithCondition;
    }

    public String getGoogleAccessToken() {
        return googleAccessToken;
    }

    public void setGoogleAccessToken(String googleAccessToken) {
        this.googleAccessToken = googleAccessToken;
    }

    public Boolean getFromFederatedLogin() {
        return fromFederatedLogin;
    }

    public void setFromFederatedLogin(Boolean fromFederatedLogin) {
        this.fromFederatedLogin = fromFederatedLogin;
    }

    public String getGoogleAppsDomain() {
        return googleAppsDomain;
    }

    public void setGoogleAppsDomain(String googleAppsDomain) {
        this.googleAppsDomain = googleAppsDomain;
    }

    public String getGoogleAppsSection() {
        return googleAppsSection;
    }

    public void setGoogleAppsSection(String googleAppsSection) {
        this.googleAppsSection = googleAppsSection;
    }

    public String getCompanySignedUpFrom() {
        return companySignedUpFrom;
    }

    public void setCompanySignedUpFrom(String companySignedUpFrom) {
        this.companySignedUpFrom = companySignedUpFrom;
    }

    public boolean isUk() {
        return isUk;
    }

    public void setUk(boolean uk) {
        isUk = uk;
    }

    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    public String getValue3() {
        return value3;
    }

    public void setValue3(String value3) {
        this.value3 = value3;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getAdminSocialImageUrl() {
        return adminSocialImageUrl;
    }

    public void setAdminSocialImageUrl(String adminSocialImageUrl) {
        this.adminSocialImageUrl = adminSocialImageUrl;
    }

    public String getSocialUserName() {
        return socialUserName;
    }

    public void setSocialUserName(String socialUserName) {
        this.socialUserName = socialUserName;
    }

    public String getSelectedApps() {
        return selectedApps;
    }

    public void setSelectedApps(String selectedApps) {
        this.selectedApps = selectedApps;
    }
    public String getIndustry() {
        return industry;
    }

    public Integer getEmployeeCount() {
        return employeeCount;
    }

    public void setEmployeeCount(Integer employeeCount) {
        this.employeeCount = employeeCount;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }
    @Override
    public String toString() {
        return "\nName:" + name + ";\nAdminEmail:" + adminEmail + ";\nAdminFName:" + adminFName + ";\nAdminLName:" + adminLName
                + ";\nCompanySignedUpFrom:" + companySignedUpFrom + ";\nGoogleAppsDomain:" + googleAppsDomain;
    }

    public static String toString(NewCompany company) {
        JSONObject json = new JSONObject();
        try {
            json.put("name", company.getName());
            if(company.getSelectedApps() != null) {
                json.put("selectedapps", company.getSelectedApps());
            }
            json.put("admin-email", company.getAdminEmail());
            json.put("admin-first-name", company.getAdminFName());
            json.put("admin-last-name", company.getAdminLName());
            json.put("phone", company.getPhone());
            json.put("callCode", company.getCallCode());
            if (company.getCompanyId() != null) {
                json.put("companyid", company.getCompanyId());
            }
            json.put("country-id", company.getCountryID());
            json.put("signed-up-from", company.getCompanySignedUpFrom());
            if (company.getUtm_source() != null)
                json.put("source", company.getUtm_source());

            json.put("campaign", company.getValue2());
            if (company.getUtm_campaign() != null)
                json.put("campaign", company.getUtm_campaign());
            json.put("keyword", company.getUtm_keyword());
            json.put("btn", company.getUtm_btn());
            json.put("medium", company.getUtm_medium());
            json.put("content", company.getUtm_content());
            json.put("utm_term", company.getUtm_term());
            if (company.getReferrer() != null)
                json.put("referrer", company.getReferrer());
            if (company.getGclid() != null)
                json.put("gclid", company.getGclid());
            if (company.getRedirected() != null) {
                json.put("redirected", company.getRedirected());
            }
            if(company.getRegistrationType()!=null) {
                json.put("registration-type", company.getRegistrationType());
            }
            if (company.getTfData() != null) {
                json.put("tf-data", company.getTfData());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!" + json.toString());
        return json.toString();
    }

    public static NewCompany fromString(String data) {
        NewCompany company = new NewCompany();
        System.out.println("?????????????????????????????????" + data);
        try {
            JSONObject json = new JSONObject(data);
            System.out.println("?!?!?!?!?!?!?!?!?!?!?!?!?!?!?!?!?!?!?!?!?!" + json.getString("signed-up-from") + " [[[[[" + json.toString() + "]]]]");
            if (json.has("name")) {
                company.setName(json.getString("name"));
            }
            if (json.has("selectedapps")) {
                company.setSelectedApps(json.getString("selectedapps"));
            }
            if (json.has("admin-email")) {
                company.setAdminEmail(json.getString("admin-email"));
            }
            if (json.has("admin-first-name")) {
                company.setAdminFName(json.getString("admin-first-name"));
            }
            if (json.has("admin-last-name")) {
                company.setAdminLName(json.getString("admin-last-name"));
            }
            if (json.has("phone")) {
                company.setPhone(json.getString("phone"));
            }
            if (json.has("callCode")) {
                company.setCallCode(json.getString("callCode"));
            }
            if (json.has("country-id")) {
                company.setCountryID(json.getInt("country-id"));
            }
            if (json.has("signed-up-from")) {
                company.setCompanySignedUpFrom(json.getString("signed-up-from"));
            }
            if (json.has("source")) {
                company.setValue3(json.getString("source"));
                company.setUtm_source(json.getString("source"));
            }
            if (json.has("campaign")) {
                company.setValue2(json.getString("campaign"));
                company.setUtm_campaign(json.getString("campaign"));
            }
            if (json.has("keyword")) {
                company.setUtm_keyword(json.getString("keyword"));
            }
            if (json.has("medium")) {
                company.setUtm_medium(json.getString("medium"));
            }
            if (json.has("content")) {
                company.setUtm_content(json.getString("content"));
            }
            if (json.has("utm_term")) {
                company.setUtm_term(json.getString("utm_term"));
            }
            if (json.has("redirected")) {
                company.setValue3(json.getString("redirected"));
                company.setRedirected(json.getString("redirected"));
            }
            if (json.has("referrer")) {
                company.setReferrer(json.getString("referrer"));
            }
            if (json.has("gclid")) {
                company.setGclid(json.getString("gclid"));
            }
            if (json.has("companyid")) {
                company.setCompanyId(json.getInt("companyid"));
            }
            if (json.has("btn")) {
                company.setUtm_btn(json.getString("btn"));
            }
            if (json.has("registration-type")) {
                try {
                    company.setRegistrationType(RegistrationTypeEnum.getRegistrationType(json.getString("registration-type")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (json.has("tf-data")) {
                company.setTfData(json.getString("tf-data"));
            }
            return company;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return company;
    }

    public String getUtm_campaign() {
        return utm_campaign;
    }

    public void setUtm_campaign(String utm_campaign) {
        this.utm_campaign = utm_campaign;
    }

    public String getUtm_keyword() {
        return utm_keyword;
    }

    public void setUtm_keyword(String utm_keyword) {
        this.utm_keyword = utm_keyword;
    }

    public String getUtm_medium() {
        return utm_medium;
    }

    public void setUtm_medium(String utm_medium) {
        this.utm_medium = utm_medium;
    }

    public String getUtm_source() {
        return utm_source;
    }

    public void setUtm_source(String utm_source) {
        this.utm_source = utm_source;
    }

    public String getUtm_btn() {
        return utm_btn;
    }

    public void setUtm_btn(String utm_btn) {
        this.utm_btn = utm_btn;
    }

    public String getUtm_content() {
        return utm_content;
    }

    public void setUtm_content(String utm_content) {
        this.utm_content = utm_content;
    }

    public String getUtm_term() {
        return utm_term;
    }

    public void setUtm_term(String utm_term) {
        this.utm_term = utm_term;
    }

    public void setRedirected(String redirected) {
        this.redirected = redirected;
    }

    public String getRedirected() {
        return redirected;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    public String getReferrer() {
        return referrer;
    }

    public void setGclid(String gclid) {
        this.gclid = gclid;
    }

    public String getGclid() {
        return gclid;
    }

    public String getCallCode() {
        return callCode;
    }

    public void setCallCode(String callCode) {
        this.callCode = callCode;
    }

    public boolean isRedirectToSettings() {
        return redirectToSettings;
    }

    public void setRedirectToSettings(boolean redirectToSettings) {
        this.redirectToSettings = redirectToSettings;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }


    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    public RegistrationTypeEnum getRegistrationType() {
        return registrationType;
    }

    public void setRegistrationType(RegistrationTypeEnum registrationType) {
        this.registrationType = registrationType;
    }

    public boolean isReset() {
        return reset;
    }

    public void setReset(boolean reset) {
        this.reset = reset;
    }

    public boolean isSendNotification() {
        return sendNotification;
    }

    public void setSendNotification(boolean sendNotification) {
        this.sendNotification = sendNotification;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public String getTfData() {
        return tfData;
    }

    public void setTfData(String tfData) {
        this.tfData = tfData;
    }
}
