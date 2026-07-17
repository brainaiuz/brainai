package com.edatasite.workforce.gwt.core.server.office365.resources;

import com.edatasite.workforce.gwt.core.server.office365.resources.base.Office365BaseItem;
import org.json.simple.JSONObject;

import java.util.ArrayList;

/**
 * Created by umakarimov on 9/29/15.
 */
public class Office365ContactFolder extends Office365BaseItem {
    private String id;
    private String displayName;
    private String parentFolderId;

    private ArrayList<Office365ContactFolder> childFolders;

    public Office365ContactFolder() {
    }

    /**
     * @param data
     * @see https://msdn.microsoft.com/office/office365/APi/complex-types-for-mail-contacts-calendar#ContactFolderResource
     */
    public Office365ContactFolder(JSONObject data) {
        this.id = this.getString(data, "id");
        this.displayName = this.getString(data, "displayName");
        this.parentFolderId = this.getString(data, "parentFolderId");

        this.childFolders = this.getArrayList(data, "childFolders", new FieldMapper<Office365ContactFolder>() {
            @Override
            public Office365ContactFolder map(Object item) {
                return new Office365ContactFolder((JSONObject) item);
            }
        });
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();

        json.put("id", this.getId());
        json.put("displayName", this.getDisplayName());
        json.put("parentFolderId", this.getParentFolderId());
        json.put("childFolders", this.getJSONArray(this.getChildFolders()));

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

    public String getParentFolderId() {
        return parentFolderId;
    }

    public void setParentFolderId(String parentFolderId) {
        this.parentFolderId = parentFolderId;
    }

    public ArrayList<Office365ContactFolder> getChildFolders() {
        return childFolders;
    }

    public void setChildFolders(ArrayList<Office365ContactFolder> childFolders) {
        this.childFolders = childFolders;
    }
}
