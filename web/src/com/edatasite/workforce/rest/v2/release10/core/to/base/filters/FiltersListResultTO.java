package com.edatasite.workforce.rest.v2.release10.core.to.base.filters;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.ArrayList;

/**
 * Created by Anvar Akramov on 12/18/2017.
 */
public class FiltersListResultTO extends ResponseData {

    private ArrayList<FilterTO> filters;

    public FiltersListResultTO() {
    }

    public FiltersListResultTO(ArrayList<FilterTO> filters) {
        this.filters = filters;
    }

    public ArrayList<FilterTO> getFilters() {
        if(filters==null) {
            filters = new ArrayList<>();
        }
        return filters;
    }

    public void setFilters(ArrayList<FilterTO> filters) {
        this.filters = filters;
    }
}
