package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingCustomFields;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * User: Dilshod
 * Date: 01.12.2009
 * Time: 22:15:17
 */
public class CompLocationRpc extends BaseListItem implements IsSerializable, ListingCustomFields {


    public static final String ACTION = "action";
    public static final String NAME = "name";
    public static final String CODE = "code";
    public static final String COUNTRY_NAME = "countryName";
    public static final String STATE_NAME = "stateName";
    public static final String CITY_NAME = "cityName";
    public static final String EMAIL = "email";
    public static final String PHONE_NUMBER = "phoneNumber";
    public static final String FAX = "fax";
    public static final String ZIP_CODE = "ZIP_CODE";
    public static final String CITY_DISTRICT = "cityOrDistrict";
    public static final String PARENT = "PARENT";


    private NumberData numberData;
    private Integer countryId;
    private Integer stateId;
    private String name;
    private String cityName;
    private Double latitude;
    private Double longitude;
    private Integer radius;
    private String countryName;
    private String stateName;
    private Integer locationEmployeesSize;
    private String email;
    private String fax;
    private String phoneNumber;
    private String zipCode;
    private HashSet<Integer> locationMembers;
    private HashSet<Integer> updatedEmployees;
    private HashSet<Integer> teams;
    private Integer memberCount;
    private ArrayList<CompanyCustomFieldItem> customFieldItems;
    private HashMap<String, Object> customFieldValues;
    private ReferenceLocale localeItem;
    private Integer cityOrDestrictId;
    private String cityOrDestrictName;
    private SelectItem parent;
    private SelectItem cityOrDistrict;
    private String ownersId;
    private ArrayList<SelectItem> owners = new ArrayList<>();


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public Integer getStateId() {
        return stateId;
    }

    public void setStateId(Integer stateId) {
        this.stateId = stateId;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Double getLatitude() {
        return latitude;
    }

    public String getLatitudeValue() {
        return latitude != null ? latitude.toString() : "";
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getLongitudeValue() {
        return longitude != null ? longitude.toString() : "";
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getRadius() {
        return radius;
    }

    public String getRadiusValue() {
        return radius != null ? radius.toString() : "";
    }

    public void setRadius(Integer radius) {
        this.radius = radius;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public Integer getLocationEmployeesSize() {
        return locationEmployeesSize;
    }

    public void setLocationEmployeesSize(Integer locationEmployeesSize) {
        this.locationEmployeesSize = locationEmployeesSize;
    }

    public HashSet<Integer> getLocationMembers() {
        if (locationMembers == null) {
            locationMembers = new HashSet<>();
        }
        return locationMembers;
    }

    public void setLocationMembers(HashSet<Integer> locationMembers) {
        this.locationMembers = locationMembers;
    }

    @Override
    public Integer getRelationID() {
        return getObjectID();
    }

    @Override
    public String getRelationType() {
        return null;
    }

    @Override
    public String getRelationName() {
        return getCityName();
    }

    public Integer getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(Integer memberCount) {
        this.memberCount = memberCount;
    }

    public HashSet<Integer> getUpdatedEmployees() {
        if (updatedEmployees == null) {
            updatedEmployees = new HashSet<>();
        }
        return updatedEmployees;
    }

    public void setUpdatedEmployees(HashSet<Integer> updatedEmployees) {
        this.updatedEmployees = updatedEmployees;
    }


    public HashSet<Integer> getTeams() {
        if (teams == null) {
            teams = new HashSet<>();
        }
        return teams;
    }

    public void setTeams(HashSet<Integer> updatedTeams) {
        this.teams = updatedTeams;
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

    public ReferenceLocale getLocaleItem() {
        return localeItem;
    }

    public void setLocaleItem(ReferenceLocale localeItem) {
        this.localeItem = localeItem;
    }

    public NumberData getNumberData() {
        return numberData;
    }

    public void setNumberData(NumberData numberData) {
        this.numberData = numberData;
    }

    public Integer getCityOrDestrictId() {
        return cityOrDestrictId;
    }

    public void setCityOrDestrictId(Integer cityOrDestrictId) {
        this.cityOrDestrictId = cityOrDestrictId;
    }

    public String getCityOrDestrictName() {
        return cityOrDestrictName;
    }

    public void setCityOrDestrictName(String cityOrDestrictName) {
        this.cityOrDestrictName = cityOrDestrictName;
    }

    public SelectItem getParent() {
        return parent;
    }

    public void setParent(SelectItem parent) {
        this.parent = parent;
    }

    public SelectItem getCityOrDistrict() {
        return cityOrDistrict;
    }

    public void setCityOrDistrict(SelectItem cityOrDistrict) {
        this.cityOrDistrict = cityOrDistrict;
    }

    public String getOwnersId() {
        return ownersId;
    }

    public void setOwnersId(String ownersId) {
        this.ownersId = ownersId;
    }

    public ArrayList<SelectItem> getOwners() {
        return owners;
    }

    public void setOwners(ArrayList<SelectItem> owners) {
        this.owners = owners;
    }
}