package com.edatasite.workforce.gwt.core.client.rpc;

/**
 * Created by IntelliJ IDEA. User: iskan Date: Jan 16, 2008 Time: 5:32:18 PM To
 * change this template use File | Settings | File Templates.
 */

public class WfmTreeItem extends SelectItem {

    private int type;
    private WfmTreeItem parent;
    private boolean children;
    private boolean checked;
    private Double doubleValue;
    private Double givenScore = 0d;
    private String color;

    public static final WfmTreeItem ROOT = new WfmTreeItem();

    static {
        ROOT.setType(0);
    }

    public WfmTreeItem() {
        super();

    }

    public WfmTreeItem(Integer id, String name) {
        super(id, name);
    }

    public WfmTreeItem(Integer id, String name, boolean isChecked) {
        super(id, name);
        this.checked = isChecked;
    }

    public WfmTreeItem(Integer id, String name, String description) {
        super(id, name, description);
    }

    public WfmTreeItem(WfmTreeItem parent, Integer id, String name) {
        this(id, name);
        this.parent = parent;
    }

    public WfmTreeItem(WfmTreeItem parent, Integer id, String name, String description) {
        this(id, name, description);
        this.parent = parent;
    }

    public WfmTreeItem(WfmTreeItem parent, Integer id, String name, String description, Double doubleValue) {
        this(parent, id, name, description);
        this.doubleValue = doubleValue;
    }

    public WfmTreeItem(WfmTreeItem parent, Integer id, String name, boolean checked) {
        this(parent, id, name);
        this.checked = checked;
    }

    public WfmTreeItem(WfmTreeItem parent, Integer id, String name, boolean checked, String description) {
        this(parent, id, name, description);
        this.checked = checked;
    }

    public WfmTreeItem(WfmTreeItem parent, Integer id, String name, boolean checked, String description, Double doubleValue) {
        this(parent, id, name, checked, description);
        this.doubleValue = doubleValue;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public WfmTreeItem getParent() {
        return parent;
    }

    public void setParent(WfmTreeItem parent) {
        this.parent = parent;
    }

    public boolean hasChildren() {
        return children;
    }

    public void setChildren(boolean children) {
        this.children = children;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public Double getDoubleValue() {
        return doubleValue;
    }

    public void setDoubleValue(Double doubleValue) {
        this.doubleValue = doubleValue;
    }

    public Double getGivenScore() {
        return givenScore;
    }

    public void setGivenScore(Double givenScore) {
        this.givenScore = givenScore;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}