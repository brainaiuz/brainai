package com.workforcetrack.mobile.rpc.client;

import com.edatasite.workforce.gwt.core.client.rpc.Address;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: HAveANiceDay
 * Date: 22.06.11
 * Time: 21:58
 * To change this template use File | Settings | File Templates.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "adressData")
public class MAdressData {
    @XmlElement
    private Integer objectID;
    @XmlElement
    private String name;
    @XmlElement
    private String address;
    @XmlElement
    private String addressb;
    @XmlElement
    private String country;
    @XmlElement
    private Integer nameId;
    @XmlElement
    private Integer addressId;
    @XmlElement
    private Integer addressBId;
    @XmlElement
    private Integer countryID;
    @XmlElement
    private boolean primary;
    @XmlElement
    private String city;
    @XmlElement
    private Integer stateID;
    @XmlElement
    private String state;
    @XmlElement
    private String zipCode;

    public MAdressData() {
    }

    public MAdressData(Address address) {
        if (address != null) {
            this.objectID = address.getObjectID();
            this.name = address.getName();
            this.address = address.getAddress();
            this.addressb = address.getAddressb();
            this.country = address.getCountry();
            this.countryID = address.getCountryId();
            this.city = address.getCity();
            this.state = address.getState();
            this.stateID = address.getStateId();
            this.primary = address.isPrimary();
            this.zipCode = address.getZipCode();

        }

    }

    public MAdressData(Address[] addressDatas) {
        for (Address addressData : addressDatas) {
            if (addressData.isPrimary()) {
                this.objectID = addressData.getObjectID();
                this.name = addressData.getName();
                this.address = addressData.getAddress();
                this.addressb = addressData.getAddressb();
                this.country = addressData.getCountry();
                this.countryID = addressData.getCountryId();
                this.city = addressData.getCity();
                this.state = addressData.getState();
                this.stateID = addressData.getStateId();
                this.primary = addressData.isPrimary();
                this.zipCode = addressData.getZipCode();
            }
        }
    }

    public Address[] convertToArray(Address[] array) {
        List<Address> list = new ArrayList<>();
        for (Address addressData : array) {
            if (addressData.getObjectID() == this.objectID) {
                addressData.setAddress(this.address);
                addressData.setAddressb(this.addressb);
                addressData.setCity(this.city);
                addressData.setCountry(this.country);
                addressData.setCountryId(this.countryID);
                addressData.setName(this.name);
                addressData.setState(this.state);
                addressData.setStateId(this.stateID);
                addressData.setObjectID(this.objectID);
                addressData.setPrimary(this.primary);
                addressData.setZipCode(this.zipCode);
                list.add(addressData);
            }

        }
        return list.toArray(new Address[]{});

    }

    public Address convertToAD(Address addressData) {
        addressData.setAddress(this.address);
        addressData.setAddressb(this.addressb);
        addressData.setCity(this.city);
        addressData.setCountry(this.country);
        addressData.setCountryId(this.countryID);
        addressData.setName(this.name);
        addressData.setState(this.state);
        addressData.setStateId(this.stateID);
        addressData.setObjectID(this.objectID);
        addressData.setPrimary(true);
        addressData.setZipCode(this.zipCode);
        return addressData;
    }

    public boolean isPrimary() {
        return primary;
    }

    public void setPrimary(boolean primary) {
        primary = primary;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getStateID() {
        return stateID;
    }

    public void setStateID(Integer stateID) {
        this.stateID = stateID;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAddressb() {
        return addressb;
    }

    public void setAddressb(String addressb) {
        this.addressb = addressb;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getNameId() {
        return nameId;
    }

    public void setNameId(Integer nameId) {
        this.nameId = nameId;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public Integer getAddressBId() {
        return addressBId;
    }

    public void setAddressBId(Integer addressBId) {
        this.addressBId = addressBId;
    }

    public Integer getCountryID() {
        return countryID;
    }

    public void setCountryID(Integer countryID) {
        this.countryID = countryID;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}