package com.edatasite.workforce.gwt.assessment.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.WfmTreeItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.LinkedList;

public class TemplateItem implements IsSerializable {

    public TemplateItem() {
        super();

    }

    private String name;
    private LinkedList<WfmTreeItem> items;
    private ArrayList<SelectItem> department;
    private SelectItem owner;

    public TemplateItem(String name, LinkedList<WfmTreeItem> items, ArrayList<SelectItem> department, SelectItem owner) {
        super();
        this.name = name;
        this.items = items;
        this.department = department;
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LinkedList<WfmTreeItem> getItems() {
        return items;
    }

    public void setItems(LinkedList<WfmTreeItem> items) {
        this.items = items;
    }

    public ArrayList<SelectItem> getDepartment() {
        return department;
    }

    public void setDepartment(ArrayList<SelectItem> department) {
        this.department = department;
    }

    public SelectItem getOwner() {
        return owner;
    }

    public void setOwner(SelectItem owner) {
        this.owner = owner;
    }
}
