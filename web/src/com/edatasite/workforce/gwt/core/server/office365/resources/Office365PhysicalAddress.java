package com.edatasite.workforce.gwt.core.server.office365.resources;

import com.edatasite.workforce.gwt.core.server.office365.resources.base.Office365BaseItem;
import org.json.simple.JSONObject;

/**
 * Created by umakarimov on 10/5/15.
 */
public class Office365PhysicalAddress extends Office365BaseItem {
    private String street;
    private String city;
    private String state;
    private String countryOrRegion;
    private String postalCode;

    public Office365PhysicalAddress() {
    }

    /**
     * @param data
     * @see https://msdn.microsoft.com/office/office365/APi/complex-types-for-mail-contacts-calendar#PhysicalAddress
     */
    public Office365PhysicalAddress(JSONObject data) {
        this.street = this.getString(data, "street");
        this.city = this.getString(data, "city");
        this.state = this.getString(data, "state");
        this.countryOrRegion = this.getString(data, "countryOrRegion");
        this.postalCode = this.getString(data, "postalCode");
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("street", this.getStreet());
        json.put("city", this.getCity());
        json.put("state", this.getState());
        json.put("countryOrRegion", this.getCountryOrRegion());
        json.put("postalCode", this.getPostalCode());

        return json;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountryOrRegion() {
        return countryOrRegion;
    }

    public void setCountryOrRegion(String countryOrRegion) {
        this.countryOrRegion = countryOrRegion;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}
