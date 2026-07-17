package com.edatasite.workforce.rest.base.to;

import com.edatasite.workforce.core.domain.EdsAddress;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.rest.base.enums.ContactParamEnum;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dilshod Madrahimov on 4/7/15 7:14 PM
 */
public class AddressTO implements IsSerializable {
    Integer id;
    String name;
    SelectItemTO type;
    String address1;
    String address2;
    String city;
    SelectItemTO country;
    SelectItemTO state;
    String postCode;
    Boolean isPrimary = Boolean.TRUE;
    Integer entityId;
    String entityType;
    Integer relationType;
    Double latitude;
    Double longitude;

    public AddressTO() {

    }

    public AddressTO(Address address) {
        if (address != null) {
            this.id = address.getObjectID();
            this.name = address.getName();
            this.address1 = address.getAddress();
            this.address2 = address.getAddressb();
            this.city = address.getCity();
            this.country = address.getCountryId() == null ? null : new SelectItemTO(address.getCountryId(), address.getCountry(), address.getCountryCode(), "");
            this.state = address.getStateId() == null ? null : new SelectItemTO(address.getStateId(), address.getState());
            this.postCode = address.getZipCode();
            this.entityId = address.getEntityID();
            this.entityType = address.getEntityType();
            this.relationType = address.getRelationType();
            this.latitude = address.getLatitude();
            this.longitude = address.getLongitude();
            this.isPrimary = address.isPrimary();
            this.type = ContactParamEnum.getParamAsSelectItemTO(address.getRelationType());
        }
    }

    public AddressTO(EdsAddress address) {
        if (address != null) {
            this.id = address.getObjectID();
            this.name = address.getName();
            this.address1 = address.getAddress();
            this.address2 = address.getAddressb();
            this.city = address.getCity();
            this.country = address.getCountry() != null ? new SelectItemTO(address.getCountry().getObjectID(), address.getCountry().getName(), address.getCountry().getCode(), "") : null;
            this.state = address.getState() != null ? new SelectItemTO(address.getState().getObjectID(), address.getState().getName(), address.getState().getCode(), "") : null;
            this.postCode = address.getZipCode();
            this.entityId = address.getEntityID();
            this.entityType = address.getEntityType();
            this.relationType = address.getRelationType();
            this.latitude = address.getLatitude();
            this.longitude = address.getLongitude();
            this.isPrimary = address.isPrimary();
            this.type = ContactParamEnum.getParamAsSelectItemTO(address.getRelationType());
        }
    }

    public Address wrap() {
        Address address = new Address();
        address.setObjectID(getId());
        address.setName(getName());
        address.setAddress(getAddress1());
        address.setAddressb(getAddress2());
        address.setCity(getCity());
        if (getCountry() != null) {
            address.setCountry(getCountry().getName());
        }
        if (getState() != null) {
            address.setState(getState().getName());
        }
        address.setZipCode(getPostCode());
        address.setEntityType(getEntityType());
        address.setEntityID(getEntityId());
        address.setRelationType(getRelationType());
        address.setLatitude(getLatitude());
        address.setLongitude(getLongitude());
        address.setPrimary(getIsPrimary() != null ? getIsPrimary() : false);

        return address;
    }

    public static ArrayList<Address> getAddresses(List<AddressTO> addressTOList) {
        ArrayList<Address> result = new ArrayList<>();
        if (addressTOList == null || addressTOList.isEmpty()) {
            return result;
        }
        for (AddressTO addressTO : addressTOList) {
            Address address = new Address();
            address.setObjectID(addressTO.getId());
            address.setName(addressTO.getName());
            address.setAddress(addressTO.getAddress1());
            address.setAddressb(addressTO.getAddress2());
            address.setCity(addressTO.getCity());
            if (addressTO.getCountry() != null) {
                address.setCountry(addressTO.getCountry().getName());
            }
            if (addressTO.getState() != null) {
                address.setState(addressTO.getState().getName());
            }
            address.setZipCode(addressTO.getPostCode());
            address.setEntityType(addressTO.getEntityType());
            address.setEntityID(addressTO.getEntityId());
            address.setRelationType(addressTO.getRelationType());
            address.setLatitude(addressTO.getLatitude());
            address.setLongitude(addressTO.getLongitude());
            address.setPrimary(addressTO.getIsPrimary() != null ? addressTO.getIsPrimary() : false);

            result.add(address);
        }

        return result;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SelectItemTO getType() {
        return type;
    }

    public void setType(SelectItemTO type) {
        this.type = type;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public SelectItemTO getCountry() {
        return country;
    }

    public void setCountry(SelectItemTO country) {
        this.country = country;
    }

    public SelectItemTO getState() {
        return state;
    }

    public void setState(SelectItemTO state) {
        this.state = state;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public Boolean getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(Boolean isPrimary) {
        this.isPrimary = isPrimary;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Integer getRelationType() {
        return relationType;
    }

    public void setRelationType(Integer relationType) {
        this.relationType = relationType;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

}
