package com.edatasite.workforce.gwt.trainingcenter.client.rpc;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.enquiry.AddEditEnquiryItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.enquiry.EnquiryItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Normurod
 * Date: 7/18/12
 * Time: 1:42 PM
 * To change this template use File | Settings | File Templates.
 */
public interface EnquiryService extends RemoteService {

    void saveEnqueryItem(EnquiryItem enquiryItem);

    EnquiryItem getEnquiryItem(Integer enquiryId);

    AddEditEnquiryItem getEnquiryItemForAddEdit(Integer enquiryId);

    ListResult<EnquiryItem> geEnquiryList(ListingFilterParameter fp);

    SelectItem getCustomerCurrency(Integer selectedItemID);

    ContactListItem getContactDetails(Integer contactID);

    void deleteEnquiry(Integer objectID);

    SelectItem[] getCourseByProductCategoryIds(ArrayList<Integer> productCategoryIds);

    SelectItem[] getClientEnquiries(ListingFilterParameter fp);

    class App {
        public static EnquiryServiceAsync get() {
            ServiceDefTarget target = GWT.create(EnquiryService.class);
            target.setServiceEntryPoint(Utils.getHostNameURL() + "rpc/enquiry");
            return (EnquiryServiceAsync) target;
        }
    }
}
