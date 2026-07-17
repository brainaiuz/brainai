package com.edatasite.workforce.gwt.trainingcenter.client.rpc;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.enquiry.AddEditEnquiryItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.enquiry.EnquiryItem;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;

public interface EnquiryServiceAsync {
    void saveEnqueryItem(EnquiryItem enquiryItem, AsyncCallback<Void> callback);

    void getEnquiryItem(Integer enquiryId, AsyncCallback<EnquiryItem> callback);

    void getEnquiryItemForAddEdit(Integer enquiryId, AsyncCallback<AddEditEnquiryItem> callback);

    void geEnquiryList(ListingFilterParameter fp, AsyncCallback<ListResult<EnquiryItem>> callback);

    void getCustomerCurrency(Integer selectedItemID, AsyncCallback<SelectItem> callback);

    void getContactDetails(Integer contactID, AsyncCallback<ContactListItem> callback);

    void deleteEnquiry(Integer objectID,AsyncCallback<Void> callback);

    void getCourseByProductCategoryIds(ArrayList<Integer> productCategoryIds, AsyncCallback<SelectItem[]> callback);

    void getClientEnquiries(ListingFilterParameter fp, AsyncCallback<SelectItem[]> async);
}
