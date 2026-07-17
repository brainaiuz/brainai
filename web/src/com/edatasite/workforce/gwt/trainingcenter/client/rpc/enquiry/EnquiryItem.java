package com.edatasite.workforce.gwt.trainingcenter.client.rpc.enquiry;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: dilsh0d
 * Date: 18/07/12
 * Time: 21:02
 * To change this template use File | Settings | File Templates.
 */
public class EnquiryItem implements IsSerializable {
    public static String ENQUIRY_ACTION = "action";
    public static String ENQUIRY_NUMBER = "number";
    public static String ENQUIRY_DATE = "date";
    public static String ENQUIRY_MODE = "mode";
    public static String REF_INFO = "refinfo";
    public static String ENQUIRY_CUSTOMER = "customer";
    public static String CUSTOMER_CURRENCY = "currency";
    public static String CONTACT_NAME = "contactname";
    public static String CONTACT_EMAIL = "contactemail";
    public static String CONTACT_PHONE = "contactphone";
    public static String CONTACT_MOBILE = "contactmobile";

    private Integer objectID;
    private NumberData numberData;
    private String refInfo;
    private SelectItem customer;
    private SelectItem enquiryMode;
    private SelectItem currency;
    private Date enquiryDate;
    private Date lastUpdateTime;
    private ContactListItem contactDetails;
    private ArrayList<SelectItem> productCategories;
    private ArrayList<EnquiryCourseItem> courseItemList;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public NumberData getNumberData() {
        return numberData;
    }

    public void setNumberData(NumberData numberData) {
        this.numberData = numberData;
    }

    public String getRefInfo() {
        return refInfo;
    }

    public void setRefInfo(String refInfo) {
        this.refInfo = refInfo;
    }

    public SelectItem getCustomer() {
        if (customer == null) {
            customer = new SelectItem();
        }
        return customer;
    }

    public void setCustomer(SelectItem customer) {
        this.customer = customer;
    }

    public SelectItem getEnquiryMode() {
        if (enquiryMode == null) {
            enquiryMode = new SelectItem();
        }
        return enquiryMode;
    }

    public void setEnquiryMode(SelectItem enquiryMode) {
        this.enquiryMode = enquiryMode;
    }

    public SelectItem getCurrency() {
        if(currency == null){
            currency = new SelectItem();
        }
        return currency;
    }

    public void setCurrency(SelectItem currency) {
        this.currency = currency;
    }

    public Date getEnquiryDate() {
        return enquiryDate;
    }

    public void setEnquiryDate(Date enquiryDate) {
        this.enquiryDate = enquiryDate;
    }

    public ContactListItem getContactDetails() {
        if(contactDetails == null){
            contactDetails = new ContactListItem();
        }
        return contactDetails;
    }

    public void setContactDetails(ContactListItem contactDetails) {
        this.contactDetails = contactDetails;
    }

    public ArrayList<SelectItem> getProductCategories() {
        if (productCategories == null) {
            productCategories = new ArrayList<>();
        }
        return productCategories;
    }

    public void setProductCategories(ArrayList<SelectItem> productCategories) {
        this.productCategories = productCategories;
    }

    public ArrayList<EnquiryCourseItem> getCourseItemList() {
        if(courseItemList == null){
            courseItemList = new ArrayList<>();
        }
        return courseItemList;
    }

    public void setCourseItemList(ArrayList<EnquiryCourseItem> courseItemList) {
        this.courseItemList = courseItemList;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}
