package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 15.09.2010
 * Time: 15:47:56
 * To change this template use File | Settings | File Templates.
 */
public class Address extends AbstractRpcMap implements IsSerializable, Serializable {
    public enum EntityType {
        CrmAccount,
        Company
    }
    public static final int STREET = 0;
    public static final int CITY = 1;
    public static final int COUNTRY_NAME = 2;
    public static final int STATE = 3;
    public static final int POSTCODE = 4;
    public static final Integer BILLING_ADDRESS = 0;
    public static final Integer MAILING_ADDRESS = 1;

    private static final String IS_LINKED_ADDRESS = "isLinkedAddress";
    private static final String IS_PRIMARY = "isPrimary";
    private static final String LINKED_ADDRESS_ID = "linkedAddressID";
    private static final String OBJECT_ID = "objectID";
    private static final String NAME = "name";
    private static final String ADDRESS = "address";
    private static final String ADDRESSB = "addressb";
    private static final String CITY_NAME = "city";
    private static final String ZIP_CODE = "zipCode";
    private static final String COUNTRY_ID = "countryId";
    private static final String STATE_ID = "stateId";
    private static final String COUNTRY = "country";
    private static final String COUNTRY_CODE = "countryCode";
    private static final String STATE_NAME = "state";
    private static final String NAME_ID = "nameId";
    private static final String ADDRESS_ID = "addressId";
    private static final String ADDRESS_B_ID = "addressBId";
    private static final String CITY_ID = "cityId";
    private static final String ZIP_CODE_ID = "zipCodeId";
    private static final String ENTITY_ID = "entityID";
    private static final String ENTITY_TYPE = "entityType";
    private static final String LONGITUDE = "longitude";
    private static final String LATITUDE = "latitude";
    private static final String RELATION_TYPE = "relationType";
    private static final String IS_NEW = "isNew";
    private static final String CITY_SUBDIVISION_NAME = "citySubdivisionName";
    private static final String PLOT_IDENTIFICATION = "plotIdentification";
    private static final String BUILDING_NUMBER = "buildingNumber";

    private HashMap<String, String> valueMap = null;

    protected HashMap<String, String> getInstance() {
        return valueMap = valueMap == null ? new HashMap<>() : valueMap;
    }

    public boolean isLinkedAddress() {
        return getBool(IS_LINKED_ADDRESS);
    }

    public void setLinkedAddress(boolean linkedAddress) {
        addBool(IS_LINKED_ADDRESS, linkedAddress);
    }

    public Integer getLinkedAddressID() {
        return getInteger(LINKED_ADDRESS_ID);
    }

    public void setLinkedAddressID(Integer linkedAddressID) {
        addInteger(LINKED_ADDRESS_ID, linkedAddressID);
    }

    public Address() {
        setRelationType(Constants.G_HOME);
    }

    public Address(Integer objectID) {
        this();
        setObjectID(objectID);
    }

    public Boolean getIsNew() {
        return getBool(IS_NEW);
    }

    public void setIsNew(Boolean isNew) {
        addBool(IS_NEW, isNew);
    }

    public Address(Integer relationType, String address, String addressb, String city, Integer countryID, String countryName, Integer stateID, String state, String postCode) {
        this();
        setRelationType(relationType);
        setAddress(address);
        setAddressb(addressb);
        setCity(city);
        setCountryId(countryID);
        setCountry(countryName);
        setStateId(stateID);
        setState(state);
        setZipCode(postCode);
    }

    public Address clone() {
        Address clone = new Address();
        clone.setName(getName());
        clone.setAddress(getAddress());
        clone.setAddressb(getAddressb());
        clone.setCity(getCity());
        clone.setZipCode(getZipCode());
        clone.setCountryId(getCountryId());
        clone.setStateId(getStateId());
        clone.setPrimary(isPrimary());
        clone.setCountry(getCountry());
        clone.setState(getState());
        clone.setNameId(getNameId());
        clone.setAddressId(getAddressId());
        clone.setAddressBId(getAddressBId());
        clone.setCityId(getCityId());
        clone.setZipCodeId(getZipCodeId());
        clone.setRelationType(getRelationType());
        clone.setEntityID(getEntityID());
        clone.setEntityType(getEntityType());
        clone.setCitySubdivisionName(getCitySubdivisionName());
        clone.setPlotIdentification(getPlotIdentification());
        clone.setBuildingNumber(getBuildingNumber());
        return clone;
    }

    public Address(Integer objectID, String name, String address, String addressb, String city, String zipCode) {
        this();
        setObjectID(objectID);
        setName(name);
        setAddress(address);
        setAddressb(addressb);
        setCity(city);
        setZipCode(zipCode);
    }

    public Integer getObjectID() {
        return getInteger(OBJECT_ID);
    }

    public void setObjectID(Integer objectID) {
        addInteger(OBJECT_ID, objectID);
    }

    public String getName() {
        return getString(NAME);
    }

    public void setName(String name) {
        addString(NAME, name);
    }

    public String getAddress() {
        return getString(ADDRESS);
    }

    public void setAddress(String address) {
        addString(ADDRESS, address);
    }

    public String getAddressb() {
        return getString(ADDRESSB);
    }

    public void setAddressb(String addressb) {
        addString(ADDRESSB, addressb);
    }

    public String getCity() {
        return getString(CITY_NAME);
    }

    public void setCity(String city) {
        addString(CITY_NAME, city);
    }

    public String getZipCode() {
        return getString(ZIP_CODE);
    }

    public void setZipCode(String zipCode) {
        addString(ZIP_CODE, zipCode);
    }

    public Integer getCountryId() {
        return getInteger(COUNTRY_ID);
    }

    public void setCountryId(Integer countryId) {
        addInteger(COUNTRY_ID, countryId);
    }

    public Integer getStateId() {
        return getInteger(STATE_ID);
    }

    public void setStateId(Integer stateId) {
        addInteger(STATE_ID, stateId);
    }

    public boolean isPrimary() {
        return getBool(IS_PRIMARY);
    }

    public void setPrimary(boolean primary) {
        addBool(IS_PRIMARY, primary);
    }

    public String getCountry() {
        return getString(COUNTRY);
    }

    public void setCountry(String country) {
        addString(COUNTRY, country);
    }

    public String getCountryCode() {
        return getString(COUNTRY_CODE);
    }

    public void setCountryCode(String countryCode) {
        addString(COUNTRY_CODE, countryCode);
    }

    public String getState() {
        return getString(STATE_NAME);
    }

    public void setState(String state) {
        addString(STATE_NAME, state);
    }

    public Integer getNameId() {
        return getInteger(NAME_ID);
    }

    public void setNameId(Integer nameId) {
        addInteger(NAME_ID, nameId);
    }

    public Integer getAddressId() {
        return getInteger(ADDRESS_ID);
    }

    public void setAddressId(Integer addressId) {
        addInteger(ADDRESS_ID, addressId);
    }

    public Integer getAddressBId() {
        return getInteger(ADDRESS_B_ID);
    }

    public void setAddressBId(Integer addressBId) {
        addInteger(ADDRESS_B_ID, addressBId);
    }

    public Integer getCityId() {
        return getInteger(CITY_ID);
    }

    public void setCityId(Integer cityId) {
        addInteger(CITY_ID, cityId);
    }

    public Integer getZipCodeId() {
        return getInteger(ZIP_CODE_ID);
    }

    public void setZipCodeId(Integer zipCodeId) {
        addInteger(ZIP_CODE_ID, zipCodeId);
    }

    public Integer getRelationType() {
        if (getInteger(RELATION_TYPE) == null) {
            setRelationType(Constants.G_HOME);
        }
        return getInteger(RELATION_TYPE);
    }

    public void setRelationType(Integer relationType) {
        if (relationType != null) {
            addInteger(RELATION_TYPE, relationType);
        }
    }

    public Integer getEntityID() {
        return getInteger(ENTITY_ID);
    }

    public void setEntityID(Integer entityID) {
        addInteger(ENTITY_ID, entityID);
    }

    public String getEntityType() {
        return getString(ENTITY_TYPE);
    }

    public void setEntityType(String entityType) {
        addString(ENTITY_TYPE, entityType);
    }

    public Double getLongitude() {
        return getDouble(LONGITUDE);
    }

    public void setLongitude(Double longitude) {
        addDouble(LONGITUDE, longitude);
    }

    public Double getLatitude() {
        return getDouble(LATITUDE);
    }

    public void setLatitude(Double latitude) {
        addDouble(LATITUDE, latitude);
    }

    @Override
    public String toString() {
        String searchKey = "";
        if (!getNotNull(getAddress()).isEmpty()) {
            searchKey = addToSearchKey(searchKey, getAddress());
        }
        if (!getNotNull(getAddressb()).isEmpty()) {
            searchKey = addToSearchKey(searchKey, getAddressb());
        }
        if (!getNotNull(getCity()).isEmpty()) {
            searchKey = addToSearchKey(searchKey, getCity());
        }
        if (getCountryId() != null && !getNotNull(getCountry()).isEmpty()) {
            searchKey = addToSearchKey(searchKey, getCountry());
        }
        if (getStateId() != null && !getNotNull(getState()).isEmpty()) {
            searchKey = addToSearchKey(searchKey, getState());
        }
        if (!getNotNull(getZipCode()).isEmpty()) {
            searchKey = addToSearchKey(searchKey, getZipCode());
        }
        return "".equals(searchKey) ? "N/A" : searchKey;
    }

    private String addToSearchKey(String searchKey, String text) {
        if (text == null) {
            return searchKey;
        }
        searchKey = searchKey != null && !searchKey.isEmpty() ? searchKey + "," + text : text;
        return searchKey;
    }

    private String getNotNull(String text) {
        return text == null ? "" : text;
    }

    public static HashMap<Integer, Address> asMap(Address[] addresses) {
        HashMap<Integer, Address> map = new HashMap<>();
        if (addresses == null) {
            return map;
        }
        for (Address address : addresses) {
            if (address == null) {
                continue;
            }
            map.put(address.getObjectID(), address);
        }
        return map;
    }

    public boolean isNotEmpty() {
        return getAddress() != null || getCity() != null || getCountry() != null || getState() != null || getZipCode() != null || getAddressId() != null
                || getCountryId() != null || getStateId() != null || getCityId() != null || getZipCodeId() != null;
    }

    public String asString(){
        String s = "";
        if (getCountryId() != null) {
            s += (!s.isEmpty() && getCountry() != null && !"".equalsIgnoreCase(getCountry()) ? ", " : "") + getOrEmpty(getCountry());
        }
        if (getCountryId() != null) {
            s += (!s.isEmpty() && getState() != null && !"".equalsIgnoreCase(getState()) ? ", " : "") + getOrEmpty(getState());
        }
        s += (!s.isEmpty() && getCity() != null && !"".equalsIgnoreCase(getCity()) ? ", " : "") + getOrEmpty(getCity());
        s += (!s.isEmpty() && getAddress() != null && !"".equalsIgnoreCase(getAddress()) ? ", " : "") + getOrEmpty(getAddress());
        s += (!s.isEmpty() && getAddressb() != null && !"".equalsIgnoreCase(getAddressb()) ? ", " : "") + getOrEmpty(getAddressb());
        s += (!s.isEmpty() && getZipCode() != null && !"".equalsIgnoreCase(getZipCode()) ? ", " : "") + getOrEmpty(getZipCode());
        s += (!s.isEmpty() && getCitySubdivisionName() != null && !"".equalsIgnoreCase(getCitySubdivisionName()) ? ", " : "") + getOrEmpty(getCitySubdivisionName());
        s += (!s.isEmpty() && getPlotIdentification() != null && !"".equalsIgnoreCase(getPlotIdentification()) ? ", " : "") + getOrEmpty(getPlotIdentification());
        s += (!s.isEmpty() && getBuildingNumber() != null && !"".equalsIgnoreCase(getBuildingNumber()) ? ", " : "") + getOrEmpty(getBuildingNumber());
        return s;
    }

    private String getOrEmpty(String value) {
        return value == null ? "" : value;
    }

    public String getCitySubdivisionName() {
        return getString(CITY_SUBDIVISION_NAME);
    }

    public void setCitySubdivisionName(String subdivision) {
        addString(CITY_SUBDIVISION_NAME, subdivision);
    }

    public String getPlotIdentification() {
        return getString(PLOT_IDENTIFICATION);
    }

    public void setPlotIdentification(String plotOption) {
        addString(PLOT_IDENTIFICATION, plotOption);
    }

    public String getBuildingNumber() {
        return getString(BUILDING_NUMBER);
    }

    public void setBuildingNumber(String buildingNumber) {
        addString(BUILDING_NUMBER, buildingNumber);
    }
}
