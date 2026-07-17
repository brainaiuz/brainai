package com.edatasite.workforce.gwt.trainingcenter.client.rpc.enquiry;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created with IntelliJ IDEA.
 * User: dilsh0d
 * Date: 19/07/12
 * Time: 19:52
 * To change this template use File | Settings | File Templates.
 */
public class AddEditEnquiryItem implements IsSerializable {
    private SelectItem[] enquiryModes;
    private SelectItem[] productCategories;
    private SelectItem[] sessionItems;
    private EnquiryItem enquiryItem;

    public SelectItem[] getEnquiryModes() {
        if(enquiryModes ==null){
            enquiryModes = new SelectItem[0];
        }
        return enquiryModes;
    }

    public void setEnquiryModes(SelectItem[] enquiryModes) {
        this.enquiryModes = enquiryModes;
    }

    public SelectItem[] getProductCategories() {
        if (productCategories == null) {
            productCategories = new SelectItem[0];
        }
        return productCategories;
    }

    public void setProductCategories(SelectItem[] productCategories) {
        this.productCategories = productCategories;
    }

    public SelectItem[] getSessionItems() {
        if (sessionItems == null) {
            sessionItems = new SelectItem[0];
        }
        return sessionItems;
    }

    public void setSessionItems(SelectItem[] sessionItems) {
        this.sessionItems = sessionItems;
    }

    public EnquiryItem getEnquiryItem() {
        if (enquiryItem == null) {
            enquiryItem = new EnquiryItem();
        }
        return enquiryItem;
    }

    public void setEnquiryItem(EnquiryItem enquiryItem) {
        this.enquiryItem = enquiryItem;
    }
}
