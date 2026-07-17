package com.edatasite.workforce.rest.v2.release10.core.to.crm;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.ArrayList;

/**
 * Created by Dilsh0d Madrahimov on 13/10/2017.
 */
public class FilteredStatusesListTO extends ResponseData {
    private ArrayList<FilteredStatusItemTO> statuses;

    public FilteredStatusesListTO(ArrayList<FilteredStatusItemTO> statuses) {
        this.statuses = statuses;
    }

    public ArrayList<FilteredStatusItemTO> getStatuses() {
        return statuses;
    }

    public void setStatuses(ArrayList<FilteredStatusItemTO> statuses) {
        this.statuses = statuses;
    }
}

