package com.edatasite.workforce.rest.v2.release10.core.to.crm.email;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Dilshod Madrahimov on 4/16/2018.
 */
public class EmailReadTO extends ResponseData {

    private boolean read;

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}
