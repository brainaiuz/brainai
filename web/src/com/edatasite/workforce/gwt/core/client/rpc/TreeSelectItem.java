package com.edatasite.workforce.gwt.core.client.rpc;

import java.util.ArrayList;

/**
 * User: hayot  * Date: Dec 7, 2010
 */

public class TreeSelectItem extends SelectItem {
    private TreeSelectItem parent;
    private ArrayList<TreeSelectItem> children;
    private boolean showInDropDown;
    private boolean systematic;

    public TreeSelectItem() {
        super();
    }

    public TreeSelectItem(Integer id, String name) {
        super(id, name);
    }

    public TreeSelectItem(Integer id, String name, String description) {
        super(id, name, description);
    }

    public TreeSelectItem(Integer id, String name, String description, boolean systematic) {
        super(id, name, description);
        this.systematic = systematic;
    }

    public TreeSelectItem(Integer id) {
        super(id);
    }

    public TreeSelectItem getParent() {
        return parent;
    }

    public void setParent(TreeSelectItem parent) {
        this.parent = parent;
    }

    public ArrayList<TreeSelectItem> getChildren() {
        if (children == null) {
            children = new ArrayList<>();
        }
        return children;
    }

    public void setChildren(ArrayList<TreeSelectItem> children) {
        this.children = children;
    }

    public void addChild(TreeSelectItem child) {
        if (!getChildren().contains(child)) {
            child.setParent(this);
            getChildren().add(child);
        }
    }

    private int getStepOfHierarchy() {
        return getParent() != null ? getParent().getStepOfHierarchy() + 1 : 1;
    }

    public boolean hasChildren() {
        return getChildren().size() > 0;
    }

    public SelectItem asSelectItem(boolean forTreeDropDown) {
        SelectItem item = new SelectItem(getId());
        item.setName(forTreeDropDown ? getNameForDropDown() : getName());
        item.setDescription(getDescription());
        return item;
    }

    private String getNameForDropDown() {
        int step = getStepOfHierarchy();
        if (step == 1) {
            return getName();
        } else {
            StringBuilder prefix = new StringBuilder();
            for (int i = 0; i < step - 1; i++) {
                prefix.append("....");
            }
            return prefix + getName();
        }
    }

    public boolean isShowInDropDown() {
        return showInDropDown;
    }

    public void setShowInDropDown(boolean showInDropDown) {
        this.showInDropDown = showInDropDown;
    }

    public boolean isSystematic() {
        return systematic;
    }

    /**
     * please use this only when you need treeSelectItem's children seperately in List(or array)
     *
     * @param list
     * @return
     */
    public static ArrayList<TreeSelectItem> withoutTreeCapability(ArrayList<TreeSelectItem> list) {
        if (list != null && list.size() > 0) {
            ArrayList<TreeSelectItem> newList = new ArrayList<>();
            for (TreeSelectItem item : list) {
                ArrayList<TreeSelectItem> children = new ArrayList<>(item.getChildren());
                item.setChildren(null);
                if (item.isShowInDropDown()) {
                    newList.add(item);
                }
                newList.addAll(withoutTreeCapability(children));
            }
            return newList;
        }
        return new ArrayList<>();
    }
}
