package com.edatasite.workforce.rest.v2.release10.core.to.base.customfield;

import com.edatasite.workforce.rest.v2.release10.core.to.base.CategoryTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.ArrayList;

/**
 * Created by Abdurakhmonov Farrukh on 01/10/2018.
 */
public class CustomFieldCategoriesTO extends ResponseData {
    private ArrayList<CategoryTO> items;

    public CustomFieldCategoriesTO(ArrayList<CategoryTO> items) {
        this.items = items;
    }

    public ArrayList<CategoryTO> getItems() {
        return items;
    }

    public void setItems(ArrayList<CategoryTO> items) {
        this.items = items;
    }
}
