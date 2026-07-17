package com.edatasite.workforce.rest.base.to;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.workforce.gwt.contactcategory.client.rpc.ContactCategoryListItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dilshod Madrahimov on 5/4/15 1:54 PM
 */
public class ContactCategoryTO implements IsSerializable {
    Integer id;
    String name;
    String description;
    ContactCategoryTO parent;
    Integer type;
    Integer categoryType = ContactCategoryListItem.CUSTOM_CONTACT_CATEGORY;
    UserTO owner;
    List<ContactCategoryTO> children;
    //Boolean system;
    Boolean selected;

    public ContactCategoryTO() {
    }

    public ContactCategoryTO(Integer id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ContactCategoryTO getParent() {
        return parent;
    }

    public void setParent(ContactCategoryTO parent) {
        this.parent = parent;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(Integer categoryType) {
        this.categoryType = categoryType;
    }

    public UserTO getOwner() {
        return owner;
    }

    public void setOwner(UserTO owner) {
        this.owner = owner;
    }

    public List<ContactCategoryTO> getChildren() {
        if (children == null) {
            children = new ArrayList<>();
        }
        return children;
    }

    public void setChildren(List<ContactCategoryTO> children) {
        this.children = children;
    }

    /*public boolean isSystem() {
        return getType() == 1;
    }*/

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public ContactCategoryListItem wrap(ContactCategoryTO contactCategoryTO) {
        ContactCategoryListItem item = new ContactCategoryListItem();
        item.setObjectID(contactCategoryTO.getId());
        item.setName(contactCategoryTO.getName());
        item.setDescription(contactCategoryTO.getDescription());
        item.setType(contactCategoryTO.getType() == null ? EdsObject.CUSTOM : contactCategoryTO.getType());
        item.setCategoryType(contactCategoryTO.getCategoryType());
        if (contactCategoryTO.getParent() != null) {
            item.setParentID(contactCategoryTO.getParent().getId());
        } else {
            item.setParent(null);
        }
        return item;
    }


    public static List<ContactCategoryTO> getAsTree(ArrayList<ContactCategoryListItem> categoryListItems, List<Integer> selectedList) {
        List<ContactCategoryTO> contactCategoryTOs = new ArrayList<>();
        if (categoryListItems.size() > 0) {
            for (ContactCategoryListItem categoryItem : categoryListItems) {
                ContactCategoryTO categoryTO = asTree(categoryItem, selectedList);
                contactCategoryTOs.add(categoryTO);
            }
        }
        return contactCategoryTOs;
    }

    public static ContactCategoryTO asTree(ContactCategoryListItem categoryItem, List<Integer> selectedList) {
        ContactCategoryTO categoryTO = new ContactCategoryTO(categoryItem.getObjectID(), categoryItem.getName(), categoryItem.getDescription());
        if (selectedList != null && selectedList.size() > 0) {
            categoryTO.setSelected(selectedList.contains(categoryItem.getObjectID()));
        }
        if (categoryItem.getParent() != null) {
            categoryTO.setParent(asTree(categoryItem.getParent(), selectedList));
        }
        if (categoryItem.getChildren() != null && categoryItem.getChildren().length > 0) {
            for (ContactCategoryListItem child : categoryItem.getChildren()) {
                if (child != null) {
                    ContactCategoryTO childCategoryTO = asTree(child, selectedList);
                    if (childCategoryTO != null) {
                        categoryTO.getChildren().add(childCategoryTO);
                    }
                }
            }
        }
        return categoryTO;
    }

}
