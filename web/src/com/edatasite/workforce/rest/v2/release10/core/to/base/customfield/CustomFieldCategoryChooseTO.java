package com.edatasite.workforce.rest.v2.release10.core.to.base.customfield;

import com.edatasite.workforce.rest.v2.release10.core.to.base.CategoryTO;

/**
 * Created by Abdurakhmonov Farrukh on 01/16/2018.
 */
public class CustomFieldCategoryChooseTO extends CategoryTO {
    private CategoryTO category;

    public CustomFieldCategoryChooseTO() {
    }

    public CustomFieldCategoryChooseTO(Integer id, String title) {
        super(id, title);
    }

    public CustomFieldCategoryChooseTO(Integer id, String title, CategoryTO category) {
        super(id, title);
        this.category = category;
    }

    public CategoryTO getCategory() {
        return category;
    }

    public void setCategory(CategoryTO category) {
        this.category = category;
    }
}
