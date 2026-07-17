package com.edatasite.workforce.gwt.trainingcenter.client.rpc.coursebooking;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingCustomFields;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.student.StudentItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created with IntelliJ IDEA.
 * User: dilsh0d
 * Date: 11/08/12
 * Time: 15:06
 * To change this template use File | Settings | File Templates.
 */
public class CourseBookingItem implements IsSerializable, ListingCustomFields {
    public static final String STUDENT_ACTION = "action";
    public static final String NUMBER = "number";
    public static final String CUSTOMER = "customer";
    public static final String CONTACT = "contact";
    public static final String LOCATION = "location";
    public static final String STATUS = "status";
    public static final String TYPE = "type";
    public static final String CREATIONDATE = "creationdate";
    public static final String CREATOR = "creator";
    public static final String UPDATER = "updater";

    private Integer objectID;
    private String number;
    private String invoiceNumber;
    private NumberData numberData;
    private SelectItem location;
    private SelectItem customer;
    private SelectItem contact;
    private ArrayList<StudentItem> studentItems;
    private CrmAccountItem customerItems;
    private ContactListItem contactItems;
    private Date creationDate;


    private Integer statusID;
    private String statusCode;
    private SelectItem status;

    private Integer typeID;
    private SelectItem type;
    private String typeCode;

    private String masterCardPaymentURL;

    private boolean isKeyClient = false;
    private boolean isPrePaid = false;

    private boolean isPDOCustomer = false;

    private String userDefinedUrl;

    private CourseListItem[] courseListItemList;

    private Integer invoiceID;

    private Integer creatorID;
    private SelectItem creator;

    private Integer updaterID;
    private SelectItem updater;

    private ArrayList<CompanyCustomFieldItem> customFieldItems;
    private HashMap<String, Object> customFieldValues;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public NumberData getNumberData() {
        return numberData;
    }

    public void setNumberData(NumberData numberData) {
        this.numberData = numberData;
    }

    public SelectItem getLocation() {
        if (location == null) {
            location = new SelectItem();
        }
        return location;
    }

    public void setLocation(SelectItem location) {
        this.location = location;
    }

    public SelectItem getCustomer() {
        return customer;
    }

    public void setCustomer(SelectItem customer) {
        this.customer = customer;
    }

    public SelectItem getContact() {
        return contact;
    }

    public void setContact(SelectItem contact) {
        this.contact = contact;
    }

    public ArrayList<StudentItem> getStudentItems() {
        if (studentItems == null) {
            studentItems = new ArrayList<>();
        }
        return studentItems;
    }

    public void setStudentItems(ArrayList<StudentItem> studentItems) {
        this.studentItems = studentItems;
    }

    public Integer getStatusID() {
        return statusID;
    }

    public void setStatusID(Integer statusID) {
        this.statusID = statusID;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public Integer getTypeID() {
        return typeID;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
    }

    public SelectItem getType() {
        return type;
    }

    public void setType(SelectItem type) {
        this.type = type;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public CrmAccountItem getCustomerItems() {
        return customerItems;
    }

    public void setCustomerItems(CrmAccountItem customerItems) {
        this.customerItems = customerItems;
    }

    public ContactListItem getContactItems() {
        return contactItems;
    }

    public void setContactItems(ContactListItem contactItems) {
        this.contactItems = contactItems;
    }

    public String getMasterCardPaymentURL() {
        if ("".equals(masterCardPaymentURL)) {
            masterCardPaymentURL = null;
        }
        return masterCardPaymentURL;
    }

    public void setMasterCardPaymentURL(String masterCardPaymentURL) {
        this.masterCardPaymentURL = masterCardPaymentURL;
    }

    public boolean isKeyClient() {
        return isKeyClient;
    }

    public void setKeyClient(boolean keyClient) {
        isKeyClient = keyClient;
    }

    public boolean isPrePaid() {
        return isPrePaid;
    }

    public void setPrePaid(boolean prePaid) {
        isPrePaid = prePaid;
    }

    public CourseListItem[] getCourseListItemList() {
        return courseListItemList;
    }

    public void setCourseListItemList(CourseListItem[] courseListItemList) {
        this.courseListItemList = courseListItemList;
    }

    public String getUserDefinedUrl() {
        return userDefinedUrl;
    }

    public void setUserDefinedUrl(String userDefinedUrl) {
        this.userDefinedUrl = userDefinedUrl;
    }

    public Integer getInvoiceID() {
        return invoiceID;
    }

    public void setInvoiceID(Integer invoiceID) {
        this.invoiceID = invoiceID;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public boolean isPDOCustomer() {
        return isPDOCustomer;
    }

    public void setPDOCustomer(boolean PDOCustomer) {
        isPDOCustomer = PDOCustomer;
    }

    public Integer getCreatorID() {
        return creatorID;
    }

    public void setCreatorID(Integer creatorID) {
        this.creatorID = creatorID;
    }

    public SelectItem getCreator() {
        return creator;
    }

    public void setCreator(SelectItem creator) {
        this.creator = creator;
    }

    public Integer getUpdaterID() {
        return updaterID;
    }

    public void setUpdaterID(Integer updaterID) {
        this.updaterID = updaterID;
    }

    public SelectItem getUpdater() {
        return updater;
    }

    public void setUpdater(SelectItem updater) {
        this.updater = updater;
    }

    public static ArrayList<Integer> getIDsOnly(HashSet<CourseBookingItem> selectedItems) {
        ArrayList<Integer> ids = new ArrayList<>();
        for (CourseBookingItem item : selectedItems) {
            ids.add(item.getObjectID());
        }
        return ids;
    }

    public ArrayList<CompanyCustomFieldItem> getCustomFieldItems() {
        return customFieldItems;
    }

    public void setCustomFieldItems(ArrayList<CompanyCustomFieldItem> customFieldItems) {
        this.customFieldItems = customFieldItems;
    }

    public HashMap<String, Object> getCustomFieldValuesItems() {
        return customFieldValues;
    }

    public void setCustomFieldValuesItems(HashMap<String, Object> customFieldValues) {
        this.customFieldValues = customFieldValues;
    }

    @Override
    public Object getCustomFieldsValue(String columnCodeKey) {
        return customFieldValues.get(columnCodeKey);
    }

    @Override
    public void setCustomFieldsValue(String columnCodeKey, Object cellValue) {
        customFieldValues.put(columnCodeKey, cellValue);
    }

    public SelectItem getStatus() {
        return status;
    }

    public void setStatus(SelectItem status) {
        this.status = status;
    }
}
