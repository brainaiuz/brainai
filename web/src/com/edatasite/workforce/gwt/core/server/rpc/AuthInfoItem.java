package com.edatasite.workforce.gwt.core.server.rpc;

import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.controllers.login.BaseLoginController;

import java.io.Serializable;

public class AuthInfoItem implements Constants, Serializable {

    //{FROM_BASIC_LOGIN, FROM_FEDERATED_LOGIN}
    private String authType;

    //BASIC LOGIN INFO
    private String username;
    private String password;

    //LOGIN FROM SOCIAL NETWORK
    private String email;
    private String socialNetworkId;

    private boolean multiCompany;

    private String serviceId;

    public AuthInfoItem() {}

    public AuthInfoItem(String authType, String username, String password, String email, String socialNetworkId, boolean multiCompany, String serviceId) {
        this.authType = authType;
        this.username = username;
        this.password = password;
        this.email = email;
        this.socialNetworkId = socialNetworkId;
        this.multiCompany = multiCompany;
        this.serviceId = serviceId;
    }

    public AuthInfoItem buildForBasicLogin(String username, String password) {
        this.authType = BaseLoginController.FROM_BASIC_LOGIN;
        this.username = username;
        this.password = password;

        return this;
    }

    public AuthInfoItem buildForBasicLogin(String username, String password, String serviceId) {
        this.authType = BaseLoginController.FROM_BASIC_LOGIN;
        this.username = username;
        this.password = password;
        this.serviceId = serviceId;

        return this;
    }

    public AuthInfoItem buildForFederatedLogin(String email, String socialNetworkId) {
        this.authType = BaseLoginController.FROM_FEDERATED_LOGIN;
        this.email = email;
        this.socialNetworkId = socialNetworkId;

        return this;
    }

    public AuthInfoItem setMultiCompany(boolean multiCompany) {
        this.multiCompany = multiCompany;

        return this;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSocialNetworkId() {
        return socialNetworkId;
    }

    public void setSocialNetworkId(String socialNetworkId) {
        this.socialNetworkId = socialNetworkId;
    }

    public boolean isMultiCompany() {
        return multiCompany;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthInfoItem)) return false;

        AuthInfoItem that = (AuthInfoItem) o;

        if (isMultiCompany() != that.isMultiCompany()) return false;
        if (getAuthType() != null ? !getAuthType().equals(that.getAuthType()) : that.getAuthType() != null)
            return false;
        if (getUsername() != null ? !getUsername().equals(that.getUsername()) : that.getUsername() != null)
            return false;
        if (getPassword() != null ? !getPassword().equals(that.getPassword()) : that.getPassword() != null)
            return false;
        if (getEmail() != null ? !getEmail().equals(that.getEmail()) : that.getEmail() != null) return false;
        if (getSocialNetworkId() != null ? !getSocialNetworkId().equals(that.getSocialNetworkId()) : that.getSocialNetworkId() != null)
            return false;
        if (getServiceId() != null ? !getServiceId().equals(that.getServiceId()) : that.getServiceId() != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getAuthType() != null ? getAuthType().hashCode() : 0;
        result = 31 * result + (getUsername() != null ? getUsername().hashCode() : 0);
        result = 31 * result + (getPassword() != null ? getPassword().hashCode() : 0);
        result = 31 * result + (getEmail() != null ? getEmail().hashCode() : 0);
        result = 31 * result + (getSocialNetworkId() != null ? getSocialNetworkId().hashCode() : 0);
        result = 31 * result + (isMultiCompany() ? 1 : 0);
        result = 31 * result + (getServiceId() != null ? getServiceId().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AuthInfoItem{" +
                "authType='" + authType + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", socialNetworkId='" + socialNetworkId + '\'' +
                ", multiCompany=" + multiCompany +
                ", serviceId='" + serviceId + '\'' +
                '}';
    }
}
