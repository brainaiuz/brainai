package com.edatasite.workforce.gwt.core.server.office365.resources;

import com.edatasite.workforce.gwt.core.server.office365.resources.base.Office365BaseItem;
import org.json.simple.JSONObject;

/**
 * Created by faxriddin.taslimov on 19/11/2016.
 */
public class Office365ContactGroups extends Office365BaseItem {
    private String id;
    private String displayName;

    public Office365ContactGroups() {
    }

    /**
     * @param data
     * @see https://msdn.microsoft.com/office/office365/APi/complex-types-for-mail-contacts-calendar#ContactFolderResource
     */
    public Office365ContactGroups(JSONObject data) {
        this.id = this.getString(data, "id");
        this.displayName = this.getString(data, "displayName");

    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();

        json.put("id", this.getId());
        json.put("displayName", this.getDisplayName());

        return json;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

}
