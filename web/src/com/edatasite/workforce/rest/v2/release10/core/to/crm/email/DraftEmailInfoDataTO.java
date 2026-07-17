package com.edatasite.workforce.rest.v2.release10.core.to.crm.email;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Farrukh Abdurakhmonov on 4/24/2018.
 */
public class DraftEmailInfoDataTO extends ResponseData {
    private EmailSaveEditTO data;

    public EmailSaveEditTO getData() {
        return data;
    }

    public void setData(EmailSaveEditTO data) {
        this.data = data;
    }
}
