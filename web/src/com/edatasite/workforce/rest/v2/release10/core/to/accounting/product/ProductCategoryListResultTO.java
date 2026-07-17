package com.edatasite.workforce.rest.v2.release10.core.to.accounting.product;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.ArrayList;

/**
 * Created by Abdurakhmonov Farrukh on 02/1/2018.
 */
public class ProductCategoryListResultTO extends ResponseData {
    private ArrayList<ProductCategoryTO> categories;

    public ProductCategoryListResultTO(ArrayList<ProductCategoryTO> categories) {
        this.categories = categories;
    }

    public ArrayList<ProductCategoryTO> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<ProductCategoryTO> categories) {
        this.categories = categories;
    }
}
