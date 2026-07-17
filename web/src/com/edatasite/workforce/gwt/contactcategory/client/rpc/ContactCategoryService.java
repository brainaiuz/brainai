package com.edatasite.workforce.gwt.contactcategory.client.rpc;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.util.ArrayList;

/**
 * User: Dilshod Madrahimov
 * Date: 10.05.2018
 * Time: 19:08:57
 */
public interface ContactCategoryService extends RemoteService {

    Boolean deleteContactCategory(Integer categoryID, Integer selectedCategoryID, boolean deleteWithChildren);

    ArrayList<ContactCategoryListItem> getContactCategories();

    ListResult<ContactCategoryListItem> getContactCategoryList(ListingFilterParameter filterParameter);

    ContactCategoryListItem editContactCategory(Integer objectID);

    ContactCategoryListItem saveContactCategory(ContactCategoryListItem selectItem);

    ArrayList<ContactCategoryListItem> getContactCategoriesWithPermissions();

    ArrayList<Integer> changeCategory(Integer categoryId, ArrayList<Integer> iDs, int action);

    class App {
        public static ContactCategoryServiceAsync get() {
            ServiceDefTarget target = GWT.create(ContactCategoryService.class);
            target.setServiceEntryPoint(Utils.getRpcBaseUrl() + "/contactCategory");
            return (ContactCategoryServiceAsync) target;
        }
    }
}
