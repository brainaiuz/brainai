package com.edatasite.workforce.gwt.core.server.office365.resources;

import com.edatasite.workforce.gwt.core.server.office365.resources.base.Office365BaseItem;
import org.json.simple.JSONObject;

import java.util.ArrayList;

/**
 * Created by umakarimov on 9/29/15.
 */
public class Office365MessageFolder extends Office365BaseItem {
    private String id;
    private String displayName;
    private String parentFolderId;
    private Long childFolderCount;
    private Long unreadItemCount;
    private Long totalItemCount;

    private ArrayList<Office365MessageFolder> childFolders = new ArrayList<>();

    public Office365MessageFolder() {
    }

    public Office365MessageFolder(String displayName) {
        this.displayName = displayName;
    }

    /**
     * @param data
     * @see https://msdn.microsoft.com/office/office365/APi/complex-types-for-mail-contacts-calendar#RESTAPIResourcesFolder
     */
    public Office365MessageFolder(JSONObject data) {
        this.id = this.getString(data, "Id");
        this.displayName = this.getString(data, "DisplayName");
        this.parentFolderId = this.getString(data, "ParentFolderId");
        this.childFolderCount = this.getLong(data, "ChildFolderCount");
        this.unreadItemCount = this.getLong(data, "UnreadItemCount");
        this.totalItemCount = this.getLong(data, "TotalItemCount");

        this.childFolders = this.getArrayList(data, "ChildFolders", new FieldMapper<Office365MessageFolder>() {
            @Override
            public Office365MessageFolder map(Object item) {
                return new Office365MessageFolder((JSONObject) item);
            }
        });
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();

        json.put("Id", this.getId());
        json.put("DisplayName", this.getDisplayName());
        json.put("ParentFolderId", this.getParentFolderId());
        json.put("ChildFolderCount", this.getChildFolderCount());
        json.put("UnreadItemCount", this.getUnreadItemCount());
        json.put("TotalItemCount", this.getTotalItemCount());
        json.put("ChildFolders", this.getJSONArray(this.getChildFolders()));

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

    public Long getChildFolderCount() {
        return childFolderCount;
    }

    public void setChildFolderCount(Long childFolderCount) {
        this.childFolderCount = childFolderCount;
    }

    public Long getUnreadItemCount() {
        return unreadItemCount;
    }

    public void setUnreadItemCount(Long unreadItemCount) {
        this.unreadItemCount = unreadItemCount;
    }

    public Long getTotalItemCount() {
        return totalItemCount;
    }

    public void setTotalItemCount(Long totalItemCount) {
        this.totalItemCount = totalItemCount;
    }

    public ArrayList<Office365MessageFolder> getChildFolders() {
        return childFolders;
    }

    public void setChildFolders(ArrayList<Office365MessageFolder> childFolders) {
        this.childFolders = childFolders;
    }
}
