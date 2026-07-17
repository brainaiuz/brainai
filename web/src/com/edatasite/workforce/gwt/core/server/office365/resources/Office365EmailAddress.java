package com.edatasite.workforce.gwt.core.server.office365.resources;

import com.edatasite.workforce.gwt.core.server.office365.resources.base.Office365BaseItem;
import org.json.simple.JSONObject;

/**
 * Created by umakarimov on 9/30/15.
 */
public class Office365EmailAddress extends Office365BaseItem {
    private String name;
    private String address;

    /**
     * @see https://graph.microsoft.io/GraphDocuments/api-reference/v1.0/resources/emailaddress.htm
     */
    public Office365EmailAddress() {
    }

    public Office365EmailAddress(JSONObject data) {
        this.name = this.getString(data, "name");
        this.address = this.getString(data, "address");
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

    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("name", this.getName());
        json.put("address", this.getAddress());
        return json;
    }
}
