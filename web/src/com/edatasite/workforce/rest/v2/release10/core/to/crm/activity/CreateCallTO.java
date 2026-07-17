package com.edatasite.workforce.rest.v2.release10.core.to.crm.activity;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.base.link.LinkTO;

import java.util.ArrayList;

/**
 * Created by Anvar Akramov on 3/23/2018.
 */
public class CreateCallTO extends CreateEventTO {

    private String call_type;

    public CreateCallTO() {
    }

    public String getCall_type() {
        return call_type;
    }

    public void setCall_type(String call_type) {
        this.call_type = call_type;
    }

}
