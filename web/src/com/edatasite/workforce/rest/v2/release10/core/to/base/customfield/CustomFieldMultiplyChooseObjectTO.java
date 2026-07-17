package com.edatasite.workforce.rest.v2.release10.core.to.base.customfield;

import com.edatasite.workforce.rest.v2.release10.core.to.base.CategoryTO;

import java.util.ArrayList;

/**
 * Created by Abdurakhmonov Farrukh on 01/16/2018.
 */
public class CustomFieldMultiplyChooseObjectTO extends CategoryTO {

    private ArrayList<CategoryTO> choosed_items;

    public CustomFieldMultiplyChooseObjectTO() {
    }

    public CustomFieldMultiplyChooseObjectTO(Integer id, String title, ArrayList<CategoryTO> choosed_items) {
        super(id, title);
        this.choosed_items = choosed_items;
    }

    public ArrayList<CategoryTO> getChoosed_items() {
        return choosed_items;
    }

    public void setChoosed_items(ArrayList<CategoryTO> choosed_items) {
        this.choosed_items = choosed_items;
    }
}
