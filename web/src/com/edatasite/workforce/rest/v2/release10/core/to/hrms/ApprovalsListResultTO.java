package com.edatasite.workforce.rest.v2.release10.core.to.hrms;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.ArrayList;

/**
 * Created by Farrukh Abdurakhmonov on 11/01/2018.
 */
public class ApprovalsListResultTO extends ResponseData {
    private ArrayList<ApprovalsListTO> categories;

    public ApprovalsListResultTO() {
    }

    public ApprovalsListResultTO(ArrayList<ApprovalsListTO> categories) {
        this.categories = categories;
    }

    public ArrayList<ApprovalsListTO> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<ApprovalsListTO> categories) {
        this.categories = categories;
    }
}
