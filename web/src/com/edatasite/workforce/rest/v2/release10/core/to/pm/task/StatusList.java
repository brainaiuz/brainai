package com.edatasite.workforce.rest.v2.release10.core.to.pm.task;

import com.edatasite.workforce.rest.v2.release10.core.to.base.CategoryTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.ArrayList;

/**
 * Created by Dilsh0d Madrahimov on 12/27/2017.
 */
public class StatusList extends ResponseData {

    private ArrayList<CategoryTO> statuses;

    public StatusList() {
    }

    public StatusList(ArrayList<CategoryTO> statuses) {
        this.statuses = statuses;
    }

    public ArrayList<CategoryTO> getStatuses() {
        return statuses;
    }

    public void setStatuses(ArrayList<CategoryTO> statuses) {
        this.statuses = statuses;
    }
}
