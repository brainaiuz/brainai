package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Sanjar
 * Date: Feb 1, 2011
 * Time: 4:36:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReservationItem implements IsSerializable {

    public static final String ACTION = "action";
    public static final String ID = "id";
    public static final String TYPE = "type";
    public static final String FROM_DATE = "fromDate";
    public static final String TO_DATE = "toDate";
    public static final String STATUS = "status";
    public static final String NAME = "name";
    public static final String SHIPPING_METOD = "shippingMetod";
    public static final String QYT = "qyt";

    private Integer id;
    private String code;
    private Date fromDate;
    private Date toDate;
    private Integer locationId;
    private Integer categoryId;
    private Integer qty = 1;
    private Integer status;
    private Integer itemId;
    private Integer eventId;
    private Integer shippingMethodId;
    private String clientName;
    private String clientCode;
    private Integer clientID;
    private Integer type;

    //Additional params
    private String locationName;
    private String itemName;
    private String itemCode;
    private String eventName;
    private String shippingMethodName;

    private ContactItem contact;

    public ReservationItem() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public ContactItem getContact() {
        return contact;
    }

    public void setContact(ContactItem contact) {
        this.contact = contact;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Integer getShippingMethodId() {
        return shippingMethodId;
    }

    public void setShippingMethodId(Integer shippingMethodId) {
        this.shippingMethodId = shippingMethodId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public String getShippingMethodName() {
        return shippingMethodName;
    }

    public void setShippingMethodName(String shippingMethodName) {
        this.shippingMethodName = shippingMethodName;
    }

    public Integer getClientID() {
        return clientID;
    }

    public void setClientID(Integer clientID) {
        this.clientID = clientID;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
