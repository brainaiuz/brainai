package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.contact.EdsContactCategory;
import com.edatasite.workforce.gwt.contactcategory.client.rpc.ContactCategoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface ContactCategoryManager extends Manager<EdsContactCategory> {

    List<EdsContactCategory> getList();

    List<EdsContactCategory> getAllCategories(Integer userID);

    List<EdsContactCategory> getSharedCategories(Integer userID, boolean isPrivileged);

    List<EdsContactCategory> getSharedPrivateCategories(Integer userID, boolean isPrivileged);

    List<EdsContactCategory> getDefaultCategoriesWithoutPrivateCategories();

    List<EdsContactCategory> getPrivateCategory(boolean isPrivileged);

    List<EdsContactCategory> getOwnCategories(Integer userID);

    String getCategoryIDsForUserForSOLR(String prefix, EdsUser user, String suffix, List<Integer> onlyTheseIDs);

    EdsContactCategory getDefaultCategoryByContactType(Integer contactType);

    List<EdsContactCategory> getDefaultCategoriesByContactType(Integer... contactTypes);

    Set<Integer> getSharedCategoryIDsForUser(boolean isPrivileged);

    EdsContactCategory getLeadCategory();

    List<Integer> getContactCategoryIDs(Integer contactID);

    List<EdsContactCategory> getList(ListingFilterParameter filterParameter);

    List<EdsContactCategory> getContactCategoryList(ListingFilterParameter filterParameter);

    Integer getContactCategoryCount(ListingFilterParameter filterParameter);

    ArrayList<ContactCategoryListItem> getContactCategories();

    List<EdsContactCategory> getAllSharedCategories(Integer userID);

}