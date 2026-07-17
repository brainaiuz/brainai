package com.edatasite.workforce.gwt.contactcategory.server;

import com.edatasite.workforce.gwt.contactcategory.client.rpc.ContactCategoryListItem;

import java.util.ArrayList;

/**
 * User: Dilshod Madrahimov
 * Date: Oct 5, 2018
 * Time: 7:39:50 PM
 */
public interface ContactCategoryServiceLocal {

    ArrayList<ContactCategoryListItem> getContactCategories();

    void createSystemContactCategories(String from);

}
