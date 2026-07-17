package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingCustomFields;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Apr 15, 2010
 * Time: 4:45:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class WarehouseItem implements IsSerializable, ListingCustomFields {


    public static final String ACTION = "action";
    public static final String NAME = "name";
    public static final String NOTES = "notes";
    public static final String PRIMARY_CONTACT = "primary_contact";
    public static final String PHONE = "phone";
    public static final String EMAIL = "email";
    public static final String ADDRESS = "address";
    public static final String ASSIGNEE = "assignee";
    public static final String WAREHOUSE_CODE = "code";


    public NumberData getNumberData() {
        return numberData;
    }

    public void setNumberData(NumberData numberData) {
        this.numberData = numberData;
    }

    private NumberData numberData;
    private Integer objectID;
    private String name;
    private String notes;
    private String contactname; //will be removed
    private Integer primaryContactID;
    private String assignee;
    private ContactListItem contactNameLookUp;
    private String phone; //will be removed
    private String email; //will be removed
    private String address;
    private Integer companyID;
    private ArrayList<SelectItem> selectedOwners;
    private SelectItem[] ownerItems, templates;
    private ArrayList<CompanyCustomFieldItem> customFieldItems;
    private HashMap<String, Object> customFieldValues;

    public WarehouseItem() {
    }
    public Integer getObjectID() {
        return objectID;
    }
    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }
    public String getContactname() {
        return contactname;
    }
    public void setContactname(String contactname) {
        this.contactname = contactname;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public Integer getCompanyID() {
        return companyID;
    }
    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
    }
    public ArrayList<SelectItem> getSelectedOwners() {
        return selectedOwners;
    }
    public void setSelectedOwners(ArrayList<SelectItem> selectedOwners) {
        this.selectedOwners = selectedOwners;
    }
    public SelectItem[] getOwnerItems() {
        return ownerItems;
    }
    public void setOwnerItems(SelectItem[] ownerItems) {
        this.ownerItems = ownerItems;
    }
    public ContactListItem getContactNameLookUp() {
        return contactNameLookUp;
    }
    public void setContactNameLookUp(ContactListItem contactNameLookUp) {
        this.contactNameLookUp = contactNameLookUp;
    }
    public Integer getPrimaryContactID() {
        return primaryContactID;
    }
    public void setPrimaryContactID(Integer primaryContactID) {
        this.primaryContactID = primaryContactID;
    }
    public String getAssignee() {
        return assignee;
    }
    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public SelectItem[] getTemplates() {
        return templates;
    }

    public void setTemplates(SelectItem[] templates) {
        this.templates = templates;
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
}
