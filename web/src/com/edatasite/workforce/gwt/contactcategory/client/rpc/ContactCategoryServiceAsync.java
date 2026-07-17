package com.edatasite.workforce.gwt.contactcategory.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;

/**
 * User: Dilshod Madrahimov
 * Date: 10.05.2018
 * Time: 19:08:57
 */
public interface ContactCategoryServiceAsync {

    void deleteContactCategory(Integer categoryID, Integer selectedCategoryID, boolean moveWhitChildren, AsyncCallback<Boolean> asyncCallback);

    void getContactCategories(AsyncCallback<ArrayList<ContactCategoryListItem>> asyncCallback);

    void getContactCategoryList(ListingFilterParameter filterParameter, AsyncCallback<ListResult<ContactCategoryListItem>> asyncCallback);

    void editContactCategory(Integer objectID, AsyncCallback<ContactCategoryListItem> asyncCallback);

    void saveContactCategory(ContactCategoryListItem selectItem, AsyncCallback<ContactCategoryListItem> asyncCallback);

    void getContactCategoriesWithPermissions(AsyncCallback<ArrayList<ContactCategoryListItem>> asyncCallback);

    void changeCategory(Integer categoryId, ArrayList<Integer> iDs, int action, AsyncCallback<ArrayList<Integer>> asyncCallback);

}
