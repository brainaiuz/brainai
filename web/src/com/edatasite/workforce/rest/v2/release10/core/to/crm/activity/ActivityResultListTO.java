package com.edatasite.workforce.rest.v2.release10.core.to.crm.activity;

import com.edatasite.workforce.rest.v2.release10.core.to.base.PagingResultTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.ArrayList;

/**
 * Created by Dilsh0d Madrahimov on 2/14/2018.
 */
public class ActivityResultListTO extends ResponseData {

    private PagingResultTO positive_pagination;
    private PagingResultTO negative_pagination;
    private ArrayList<ActivityGroupListTO> list;

    public ActivityResultListTO() {
    }

    public PagingResultTO getPositive_pagination() {
        return positive_pagination;
    }

    public void setPositive_pagination(PagingResultTO positive_pagination) {
        this.positive_pagination = positive_pagination;
    }

    public PagingResultTO getNegative_pagination() {
        return negative_pagination;
    }

    public void setNegative_pagination(PagingResultTO negative_pagination) {
        this.negative_pagination = negative_pagination;
    }

    public ArrayList<ActivityGroupListTO> getList() {
        return list;
    }

    public void setList(ArrayList<ActivityGroupListTO> list) {
        this.list = list;
    }
}
