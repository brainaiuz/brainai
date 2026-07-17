package com.edatasite.workforce.rest.v2.release10.core.to.crm;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.ArrayList;

/**
 * Created by Anvar Akramov on 13/10/2017.
 */
public class QuickFiltersTO extends ResponseData {

    private boolean otf_is_active;
    private ArrayList<PeopleTO> people;
    private ArrayList<SavedFilterTO> categories;

    public QuickFiltersTO() {
    }

    public boolean isOtf_is_active() {
        return otf_is_active;
    }

    public void setOtf_is_active(boolean otf_is_active) {
        this.otf_is_active = otf_is_active;
    }

    public ArrayList<PeopleTO> getPeople() {
        return people;
    }

    public void setPeople(ArrayList<PeopleTO> people) {
        this.people = people;
    }

    public ArrayList<SavedFilterTO> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<SavedFilterTO> categories) {
        this.categories = categories;
    }
}

