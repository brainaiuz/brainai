package com.edatasite.workforce.gwt.project.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.BookingReservationItem;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.Relational;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.UserGrant;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingCustomFields;
import com.edatasite.workforce.gwt.core.client.ui.Markedable;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: developer
 * Date: 5/18/12
 * Time: 4:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class BookingItemsItem extends Relational implements IsSerializable, UserGrant, Markedable, ListingCustomFields {

    public static final String ACTION = "action";
    public static final String ITEM_NUMBER = "itemNumber";
    public static final String ITEM_NAME = "itemName";
    public static final String STATUS = "status";
    public static final String AVAILABLE_ON = "availableon";
    public static final String CATEGORY = "category";
    public static final String DESCRIPTION = "description";
    public static final String LOCATION = "location";

    private Integer objectID;
    private String itemNumber;
    private String itemName;
    private SelectItem category;
    private String description;

    private Integer locationID;
    private String location;

    private String status;
    private String availableOn;
    private Date startDate;
    private Date endDate;
    private Integer intNumber;
    private NumberData numberData;
    private String layoutHTML;

    private SelectItem[] categories = new SelectItem[]{};
    private SelectItem[] locations;

    private ArrayList<CompanyCustomFieldItem> customFieldItems;
    private HashMap<String, Object> customFieldValues;

    public NumberData getNumberData() {
        return numberData;
    }

    public void setNumberData(NumberData numberData) {
        this.numberData = numberData;
    }

    public Integer getIntNumber() {
        return intNumber;
    }

    public void setIntNumber(Integer intNumber) {
        this.intNumber = intNumber;
    }

    public ArrayList<BookingReservationItem> getBookingReservationItemList() {
        return bookingReservationItemList;
    }

    public void setBookingReservationItemList(ArrayList<BookingReservationItem> bookingReservationItemList) {
        this.bookingReservationItemList = bookingReservationItemList;
    }

    private ArrayList<BookingReservationItem> bookingReservationItemList = new ArrayList<>();

    public SelectItem[] getCategories() {
        return categories;
    }

    public void setCategories(SelectItem[] categories) {
        this.categories = categories;
    }

    public SelectItem getCategory() {
        return category;
    }

    public void setCategory(SelectItem category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLayoutHTML() {
        return layoutHTML;
    }

    public void setLayoutHTML(String layoutHTML) {
        this.layoutHTML = layoutHTML;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAvailableOn() {
        return availableOn;
    }

    public void setAvailableOn(String availableOn) {
        this.availableOn = availableOn;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
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

    @Override
    public Boolean isMarked() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setMarked(Boolean marked) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setPermission(int permission) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getPermission() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Integer getLocationID() {
        return locationID;
    }

    public void setLocationID(Integer locationID) {
        this.locationID = locationID;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public SelectItem[] getLocations() {
        return locations;
    }

    public void setLocations(SelectItem[] locations) {
        this.locations = locations;
    }

    @Override
    public Integer getRelationID() {
        return getObjectID();
    }

    @Override
    public String getRelationType() {
        return RelationItem.TYPE_BOOKING;
    }

    @Override
    public String getRelationName() {
        return getItemName();
    }
}
