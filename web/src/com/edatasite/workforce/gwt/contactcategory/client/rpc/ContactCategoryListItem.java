package com.edatasite.workforce.gwt.contactcategory.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TreeSelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.rbac.GroupMembersViewItem;
import com.edatasite.workforce.gwt.documents.client.rest.resource.PermissionHolder;
import com.edatasite.workforce.gwt.documents.client.rest.resource.UserResource;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: Jul 10, 2010
 * Time: 10:48:56 AM
 * To change this template use File | Settings | File Templates.
 */
public class ContactCategoryListItem implements IsSerializable {

    public static final String ID = "objectID";
    public static final String NAME = "contactCategoryName";
    public static final String DESCRIPTION = "categoryDescriprion";
    public static final String PARENT_NAME = "categoryParentName";
    public static final int MOVE = 2;
    public static final int COPY = 1;

    public final static int CUSTOM_CONTACT_CATEGORY = 0;
    public final static int CRM_CONTACT_CATEGORY = 1;
    public final static int CLIENT_CONTACT_CATEGORY = 2;
    public final static int SUPPLIER_CONTACT_CATEGORY = 3;
    public final static int EMPLOYEE_CONTACT_CATEGORY = 4;
    public final static int PRIVATE_CONTACT_CATEGORY = 5;

    private Integer objectID;
    private String name;
    private UserResource owner;
    private String description;
    private ContactCategoryListItem parent;
    private Integer parentID;
    private int type;
    private boolean doNotShow = false;
    private boolean selected = false;

    private ContactCategoryListItem[] parents;
    private ContactCategoryListItem[] children;
    private ArrayList<GroupMembersViewItem> groups;

    protected PermissionHolder permission = new PermissionHolder();
    protected HashSet<PermissionHolder> permissions = new HashSet<>();
    private DateNonConvertable modificationDate;

    private boolean shared = true;
    private int categoryType = CUSTOM_CONTACT_CATEGORY;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Retrieve the permissions.
     *
     * @return the permissions
     */
    public HashSet<PermissionHolder> getPermissions() {
        return permissions;
    }

    /**
     * Modify the permissions.
     *
     * @param newPermissions the permissions to set
     */
    public void setPermissions(HashSet<PermissionHolder> newPermissions) {
        permissions = newPermissions;
    }

    public PermissionHolder getPermission() {
        return permission;
    }

    public void setPermission(PermissionHolder permission) {
        this.permission = permission;
    }

    public ContactCategoryListItem getParent() {
        return parent;
    }

    public void setParent(ContactCategoryListItem parent) {
        this.parent = parent;
        if (parent != null) {
            setParentID(parent.getObjectID());
        }
    }

    public Integer getParentID() {
        return parentID;
    }

    public void setParentID(Integer parentID) {
        this.parentID = parentID;
    }

    public ContactCategoryListItem[] getParents() {
        return parents;
    }

    public void setParents(ContactCategoryListItem[] parents) {
        this.parents = parents;
    }

    public ContactCategoryListItem[] getChildren() {
        return children;
    }

    public void setChildren(ContactCategoryListItem[] children) {
        this.children = children;
    }

    public UserResource getOwner() {
        return owner;
    }

    public void setOwner(UserResource owner) {
        this.owner = owner;
    }

    public ArrayList<GroupMembersViewItem> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<GroupMembersViewItem> groups) {
        this.groups = groups;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public DateNonConvertable getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(DateNonConvertable modificationDate) {
        this.modificationDate = modificationDate;
    }

    public void addChild(ContactCategoryListItem child) {
        ArrayList<ContactCategoryListItem> children = this.children != null && this.children.length > 0 ? new ArrayList(Arrays.asList(this.children)) : new ArrayList<>();
        if (!children.contains(child)) {
            children.add(child);
        }
        this.children = children.toArray(new ContactCategoryListItem[]{});

    }

    public SelectItem getAsSelectItem() {
        return new SelectItem(this.getObjectID(), this.name, this.description);
    }

    public boolean isShared() {
        return shared;
    }

    public void setShared(boolean shared) {
        this.shared = shared;
    }

    public int getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(int categoryType) {
        this.categoryType = categoryType;
    }

    public boolean isDoNotShow() {
        return doNotShow;
    }

    public void setDoNotShow(boolean doNotShow) {
        this.doNotShow = doNotShow;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public static TreeSelectItem[] getAsTreeSelectItem(ArrayList<ContactCategoryListItem> contactCategoryListItems, ContactCategoryListItem... ignoredCategories) {
        ArrayList<TreeSelectItem> treeSelectItems = new ArrayList<>();
        if (contactCategoryListItems != null && contactCategoryListItems.size() > 0) {
            for (ContactCategoryListItem category : contactCategoryListItems) {
                if (category != null) {
                    TreeSelectItem item = category.asTreeSelectItem(ignoredCategories);
                    if (item != null) {
                        treeSelectItems.add(item);
                    }
                }
            }
            return treeSelectItems.toArray(new TreeSelectItem[]{});
        }
        return null;
    }

    public TreeSelectItem asTreeSelectItem(ContactCategoryListItem... ignoredCategories) {
        if (ignoredCategories != null && ignoredCategories.length > 0 && Arrays.asList(ignoredCategories).contains(this)) {
            return null;
        }
        TreeSelectItem item = new TreeSelectItem(getObjectID(), getName(), getDescription(), isSystemCategory());
        if (getCategoryType() == ContactCategoryListItem.PRIVATE_CONTACT_CATEGORY && isSystemCategory()) {
            item.setSelected(true);
        }
        if (getParent() != null) {
            item.setParent(getParent().asTreeSelectItem());
        }
        item.setShowInDropDown(isShared());
        if (getChildren() != null && getChildren().length > 0) {
            for (ContactCategoryListItem child : getChildren()) {
                if (child != null) {
                    TreeSelectItem childItem = child.asTreeSelectItem(ignoredCategories);
                    if (childItem != null) {
                        item.addChild(childItem);
                    }
                }
            }
        }
        return item;
    }

    public static ArrayList asList(ArrayList<ContactCategoryListItem> contactCategories) {
        ArrayList<ContactCategoryListItem> items = new ArrayList<>();
        if (contactCategories != null && contactCategories.size() > 0) {
            for (ContactCategoryListItem contactCategory : contactCategories) {
                if (contactCategory != null) {
                    if (contactCategory.isShared() || contactCategory.isSystemCategory()) {
                        items.add(contactCategory);
                    }
                    if (contactCategory.hasChildren()) {
                        items.addAll(asList(new ArrayList<>(Arrays.asList(contactCategory.getChildren()))));
                    }
                }
            }
        }
        return items;
    }

    private boolean hasChildren() {
        return getChildren() != null && getChildren().length > 0;
    }

    public boolean isSystemCategory() {
        return getType() == 1;
    }

    @Override
    public String toString() {
        return name != null ? name : "";
    }

    @Override
    public int hashCode() {
        String string = "" + (this.toString());
        string += getObjectID() != null ? getObjectID().toString() : "";
        return string.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this.getObjectID() != null && obj != null && ((ContactCategoryListItem) obj).getObjectID() != null && this.getObjectID().equals(((ContactCategoryListItem) obj).getObjectID());
    }

    public static ArrayList<Integer> getIDs(ArrayList<ContactCategoryListItem> contactCategories) {
        ArrayList<Integer> iDs = new ArrayList<>();
        if (contactCategories != null && contactCategories.size() > 0) {
            for (ContactCategoryListItem contactCategory : contactCategories) {
                if (contactCategory != null && (contactCategory.isShared() || contactCategory.isSystemCategory()) && !contactCategory.isDoNotShow()) {
                    if (!iDs.contains(contactCategory.getObjectID())) {
                        iDs.add(contactCategory.getObjectID());
                    }
                }
            }
        }
        return iDs;
    }

    public static HashMap<Integer, ContactCategoryListItem> asMap(ContactCategoryListItem[] contactCategoryListItem) {
        HashMap<Integer, ContactCategoryListItem> map = new HashMap<>();
        if (contactCategoryListItem != null && contactCategoryListItem.length > 0) {
            for (ContactCategoryListItem category : contactCategoryListItem) {

                if (category != null && !category.isDoNotShow()) {
                    map.put(category.getObjectID(), category);
                    if (category.hasChildren()) {
                        map.putAll(asMap(category.getChildren()));
                    }
                }
            }
        }
        return map;
    }
}