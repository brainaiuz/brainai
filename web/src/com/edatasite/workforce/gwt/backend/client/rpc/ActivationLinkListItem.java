package com.edatasite.workforce.gwt.backend.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ActivationLinkListItem implements IsSerializable {
    private String fisrtName;
    private String lastName;
    private String email;
    private String activationLink;

    public String getFisrtName() {
        return fisrtName;
    }

    public void setFisrtName(String fisrtName) {
        this.fisrtName = fisrtName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getActivationLink() {
        return activationLink;
    }

    public void setActivationLink(String activationLink) {
        this.activationLink = activationLink;
    }
}
