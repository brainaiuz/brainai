package com.edatasite.workforce.rest.v2.release10.core.to.crm.email;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Farrukh Abdurakhmonov on 4/24/2018.
 */
public class SendEmailDataTO extends ResponseData {
    private EmailSendTO data;

    public EmailSendTO getData() {
        return data;
    }

    public void setData(EmailSendTO data) {
        this.data = data;
    }
}
