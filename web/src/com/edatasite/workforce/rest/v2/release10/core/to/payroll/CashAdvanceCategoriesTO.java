package com.edatasite.workforce.rest.v2.release10.core.to.payroll;

import com.edatasite.workforce.rest.v2.release10.core.to.base.CategoryTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.ArrayList;

public class CashAdvanceCategoriesTO extends ResponseData {
    private ArrayList<CategoryTO> categories;

    public CashAdvanceCategoriesTO() {
    }


    public CashAdvanceCategoriesTO(ArrayList<CategoryTO> categories) {
        this.categories = categories;
    }

    public ArrayList<CategoryTO> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<CategoryTO> categories) {
        this.categories = categories;
    }
}
