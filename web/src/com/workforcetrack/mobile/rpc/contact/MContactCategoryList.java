package com.workforcetrack.mobile.rpc.contact;

import com.edatasite.workforce.gwt.core.client.rpc.TreeSelectItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 6/6/11
 * Time: 8:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class MContactCategoryList {

    private List<TreeSelectItem> contactCategory;


    public MContactCategoryList() {

    }

    public MContactCategoryList(TreeSelectItem[] categories) {

        if (categories != null) {
            this.contactCategory = TreeSelectItem.withoutTreeCapability(new ArrayList<TreeSelectItem>(Arrays.asList(categories)));
        }
    }

    public List<TreeSelectItem> getContactCategory() {
        return contactCategory;
    }

    public void setContactCategory(List<TreeSelectItem> contactCategory) {
        this.contactCategory = contactCategory;
    }
}
