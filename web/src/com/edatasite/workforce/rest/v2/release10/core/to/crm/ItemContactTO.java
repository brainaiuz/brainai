package com.edatasite.workforce.rest.v2.release10.core.to.crm;

import com.edatasite.workforce.rest.v2.release10.core.to.auth.PhoneTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

public class ItemContactTO extends ResponseData {
    private PhoneTO phone;
    private String email;

    public PhoneTO getPhone() {
        return phone;
    }

    public void setPhone(PhoneTO phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
