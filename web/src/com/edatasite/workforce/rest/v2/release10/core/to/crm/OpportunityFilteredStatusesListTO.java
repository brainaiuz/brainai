package com.edatasite.workforce.rest.v2.release10.core.to.crm;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.ArrayList;

/**
 * Created by Anvar Akramov on 26/12/2017.
 */
public class OpportunityFilteredStatusesListTO extends ResponseData {
    private ArrayList<OpportunityFilteredStatusItemTO> statuses;

    public OpportunityFilteredStatusesListTO(ArrayList<OpportunityFilteredStatusItemTO> statuses) {
        this.statuses = statuses;
    }

    public ArrayList<OpportunityFilteredStatusItemTO> getStatuses() {
        return statuses;
    }

    public void setStatuses(ArrayList<OpportunityFilteredStatusItemTO> statuses) {
        this.statuses = statuses;
    }
}

