package com.edatasite.workforce.gwt.core.client.rpc;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: 7/2/11
 * Time: 5:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class MergeItem extends SelectItem {
    private Integer itemObjectID;
    private CompanyCustomFieldItem customFieldItem;
    private boolean manyResults = false;
    private ArrayList<MergeItem> children;

    public MergeItem() {
    }

    public MergeItem(Integer itemObjectID) {
        setItemObjectID(itemObjectID);
    }

    public MergeItem(Integer itemObjectID, Integer valueID, String value) {
        super(valueID, value);
        setItemObjectID(itemObjectID);
    }

    public Integer getItemObjectID() {
        return itemObjectID;
    }

    public void setItemObjectID(Integer itemObjectID) {
        this.itemObjectID = itemObjectID;
    }

    public CompanyCustomFieldItem getCustomFieldItem() {
        return customFieldItem;
    }

    public void setCustomFieldItem(CompanyCustomFieldItem customFieldItem) {
        this.customFieldItem = customFieldItem;
    }

    public Integer getValueID() {
        return getId();
    }

    public void setValueID(Integer valueID) {
        setId(valueID);
    }

    public String getValue() {
        return getName() == null ? "N/A" : getName();
    }

    public void setValue(String value) {
        setName(value);
    }

    public boolean isManyResults() {
        return manyResults;
    }

    public void setManyResults(boolean manyResults) {
        this.manyResults = manyResults;
    }

    public ArrayList<MergeItem> getChildren() {
        if (children == null) {
            children = new ArrayList<>();
        }
        return children;
    }

    public void setChildren(ArrayList<MergeItem> children) {
        this.children = children;
    }

    public void addChild(MergeItem child) {
        getChildren().add(child);
    }
}
