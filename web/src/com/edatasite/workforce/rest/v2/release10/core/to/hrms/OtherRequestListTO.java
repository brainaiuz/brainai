package com.edatasite.workforce.rest.v2.release10.core.to.hrms;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.ArrayList;

/**
 * Created by Ilxom Lutfullaev on 02.12.2017.
 */

public class OtherRequestListTO extends ResponseData {

    private ArrayList<OtherRequestTO> other_requests;

    public OtherRequestListTO(ArrayList<OtherRequestTO> other_requests) {
        this.other_requests = other_requests;
    }

    public ArrayList<OtherRequestTO> getOther_requests() {
        return other_requests;
    }

    public void setOther_requests(ArrayList<OtherRequestTO> other_requests) {
        this.other_requests = other_requests;
    }
}
