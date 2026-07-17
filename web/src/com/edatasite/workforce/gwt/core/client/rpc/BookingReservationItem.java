package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: developer
 * Date: 5/19/12
 * Time: 12:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class BookingReservationItem extends Relational implements IsSerializable, Key {

    public static final String ACTION = "action";
    public static final String RESERVED_BY = "reservedBy";
    public static final String CATEGORY = "category";
    public static final String BOOKING_ITEM = "bookingItem";
    public static final String FROM_DATE = "fromDate";
    public static final String TO_DATE = "toDate";
    private Date fromDate;
    private Date toDate;
    private Integer objectID;
    private Integer calendarEventID;
    private String layoutHTML;
    private SelectItem[] categories = new SelectItem[]{};
    private SelectItem[] bookingItems = new SelectItem[]{};
    private SelectItem[] reservedByIds = new SelectItem[]{};
    private SelectItem selectedBookingItemId;
    private SelectItem selectedReservedById;
    private SelectItem selectedCategoryId;
    private String bookingItemName;
    private ArrayList<RelationItem> relationItems;

    public ArrayList<RelationItem> getRelationItems() {
        return relationItems;
    }

    public void setRelationItems(ArrayList<RelationItem> relationItems) {
        this.relationItems = relationItems;
    }

    public SelectItem[] getCategories() {
        return categories;
    }

    public void setCategories(SelectItem[] categories) {
        this.categories = categories;
    }

    public SelectItem[] getBookingItems() {
        return bookingItems;
    }

    public void setBookingItems(SelectItem[] bookingItems) {
        this.bookingItems = bookingItems;
    }

    public SelectItem[] getReservedByIds() {
        return reservedByIds;
    }

    public void setReservedByIds(SelectItem[] reservedByIds) {
        this.reservedByIds = reservedByIds;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public String getLayoutHTML() {
        return layoutHTML;
    }

    public void setLayoutHTML(String layoutHTML) {
        this.layoutHTML = layoutHTML;
    }

    public SelectItem getSelectedReservedById() {
        return selectedReservedById;
    }

    public void setSelectedReservedById(SelectItem selectedReservedById) {
        this.selectedReservedById = selectedReservedById;
    }

    public SelectItem getSelectedCategoryId() {
        return selectedCategoryId;
    }

    public void setSelectedCategoryId(SelectItem selectedCategoryId) {
        this.selectedCategoryId = selectedCategoryId;
    }

    public SelectItem getSelectedBookingItemId() {
        return selectedBookingItemId;
    }

    public void setSelectedBookingItemId(SelectItem selectedBookingItemId) {
        this.selectedBookingItemId = selectedBookingItemId;
    }

    public Integer getCalendarEventID() {
        return calendarEventID;
    }

    public void setCalendarEventID(Integer calendarEventID) {
        this.calendarEventID = calendarEventID;
    }

    public String getBookingItemName() {
        return bookingItemName;
    }

    public void setBookingItemName(String bookingItemName) {
        this.bookingItemName = bookingItemName;
    }

    @Override
    public String getKey() {
        return "" + objectID;  //To change body of implemented methods use File | Settings | File Templates.
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
        return getRelationName();
    }
}
