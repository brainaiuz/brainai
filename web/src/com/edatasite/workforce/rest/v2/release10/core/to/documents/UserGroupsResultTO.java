package com.edatasite.workforce.rest.v2.release10.core.to.documents;

import com.edatasite.workforce.rest.v2.release10.core.to.base.CategoryTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.ArrayList;

/**
 * Created by Abdurakhmonov Farrukh on 03/02/2018.
 */
public class UserGroupsResultTO extends ResponseData {
    private ArrayList<CategoryTO> groups;

    public UserGroupsResultTO(ArrayList<CategoryTO> groups) {
        this.groups = groups;
    }

    public ArrayList<CategoryTO> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<CategoryTO> groups) {
        this.groups = groups;
    }
}
