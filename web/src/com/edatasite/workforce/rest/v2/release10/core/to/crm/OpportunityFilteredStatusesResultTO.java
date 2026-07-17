package com.edatasite.workforce.rest.v2.release10.core.to.crm;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.ArrayList;

/**
 * Created by Abdurakhmonov Farrukh on 12/26/2017.
 */
public class OpportunityFilteredStatusesResultTO extends ResponseData {
    private ArrayList<OpportunityFilteredStatusItemTO> statuses;

    public OpportunityFilteredStatusesResultTO() {
    }

    public ArrayList<OpportunityFilteredStatusItemTO> getStatuses() {
        return statuses;
    }

    public void setStatuses(ArrayList<OpportunityFilteredStatusItemTO> statuses) {
        this.statuses = statuses;
    }
}
