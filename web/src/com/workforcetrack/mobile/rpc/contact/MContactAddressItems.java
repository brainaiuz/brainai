package com.workforcetrack.mobile.rpc.contact;

import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.workforcetrack.mobile.rpc.base.WebServiceUtils;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 5/11/11
 * Time: 11:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class MContactAddressItems implements Serializable {

    private String street;
    private String city;
    private String country;
    private String state;
    private String postCode;

    private Integer stateID;
    private Integer countryID;
    private Integer parentID;
    private Boolean isNew;
    private Integer objectID;

    public MContactAddressItems() {
        this.parentID = 1; //1 - HOME
    }

    public MContactAddressItems(Address contactAddressItems) {

        if (contactAddressItems != null) {
            this.parentID = contactAddressItems.getRelationType();
            this.street = contactAddressItems.getAddress();
            this.city = contactAddressItems.getCity();
            this.country = contactAddressItems.getCountry();
            this.state = contactAddressItems.getState();
            this.postCode = contactAddressItems.getZipCode();

            this.stateID = contactAddressItems.getStateId();
            this.countryID = contactAddressItems.getCountryId();
            this.isNew = contactAddressItems.getIsNew();
            this.objectID = contactAddressItems.getObjectID();
        }
    }

    public Address convertToAddress(Address address) {
        if (address == null) {
            address = new Address();
        }
        address.setRelationType(WebServiceUtils.getNotZeroValue(getParentID()));
        address.setAddress(getStreet());
        address.setCity(getCity());
        address.setCountry(getCountry());
        address.setState(getState());
        address.setCountryId(WebServiceUtils.getNotZeroValue(getCountryID()));
        address.setStateId(WebServiceUtils.getNotZeroValue(getStateID()));
        address.setZipCode(getPostCode());
        address.setIsNew(getIsNew());
        address.setObjectID(getObjectID());
        return address;
    }

    public static boolean convert(Address contactAddressItems, MContactAddressItems mContactAddressItems, boolean fromContactAddressItem) {
        if (contactAddressItems == null || mContactAddressItems == null) {
            return false;
        }

        try {

            if (fromContactAddressItem) {
                mContactAddressItems.setParentID(contactAddressItems.getRelationType());
                mContactAddressItems.setStreet(contactAddressItems.getAddress());
                mContactAddressItems.setCity(contactAddressItems.getCity());
                mContactAddressItems.setCountry(contactAddressItems.getCountry());
                mContactAddressItems.setState(contactAddressItems.getState());
                mContactAddressItems.setPostCode(contactAddressItems.getZipCode());
                mContactAddressItems.setStateID(contactAddressItems.getStateId());
                mContactAddressItems.setCountryID(contactAddressItems.getCountryId());
                mContactAddressItems.setObjectID(contactAddressItems.getObjectID());
            } else {
                contactAddressItems.setRelationType(mContactAddressItems.getParentID() == null
                        || mContactAddressItems.getParentID() == 0 ? null : mContactAddressItems.getParentID());
                contactAddressItems.setAddress(mContactAddressItems.getStreet());
                contactAddressItems.setCity(mContactAddressItems.getCity());
                contactAddressItems.setCountry(mContactAddressItems.getCountry());
                contactAddressItems.setState(mContactAddressItems.getState());
                contactAddressItems.setZipCode(mContactAddressItems.getPostCode() == null || mContactAddressItems.getPostCode().equals(0) ? null : mContactAddressItems.getPostCode());
                contactAddressItems.setStateId(mContactAddressItems.getStateID() == null ||
                        mContactAddressItems.getStateID() == 0 ? null : mContactAddressItems.getStateID());
                contactAddressItems.setCountryId(mContactAddressItems.getCountryID() == null ||
                        mContactAddressItems.getCountryID() == 0 ? null : mContactAddressItems.getCountryID());
                contactAddressItems.setIsNew(mContactAddressItems.getIsNew());
                contactAddressItems.setObjectID(mContactAddressItems.getObjectID() == null ||
                        mContactAddressItems.getObjectID() == 0 ? null : mContactAddressItems.getObjectID());
            }

            return true;
        } catch (Exception ex) {
            ex.printStackTrace();

            return false;
        }


    }

    public Integer getStateID() {
        return stateID;
    }

    public void setStateID(Integer stateID) {
        this.stateID = stateID;
    }

    public Integer getCountryID() {
        return countryID;
    }

    public void setCountryID(Integer countryID) {
        this.countryID = countryID;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public Integer getParentID() {
        return parentID;
    }

    public void setParentID(Integer parentID) {
        this.parentID = parentID;
    }

    public Boolean getIsNew() {
        return isNew;
    }

    public void setIsNew(Boolean isNew) {
        this.isNew = isNew;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }
}
