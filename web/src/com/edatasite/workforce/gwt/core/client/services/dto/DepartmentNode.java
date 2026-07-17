package com.edatasite.workforce.gwt.core.client.services.dto;

import com.edatasite.workforce.gwt.core.client.enums.ChildOrientation;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

import java.util.ArrayList;
import java.util.List;

public class DepartmentNode {
    private Integer id;
    private String name;
    private String description;
    private String shortDescription;
    private String numberData;
    private String head;
    private String color;
    private boolean root;
    private boolean changeSubDepColor;
    private boolean manager;
    private int depth;
    private int order;
    private ChildOrientation childOrientation = ChildOrientation.HORIZONTAL;
    private Integer parentId;
    private boolean parentHasMoreThanOneChild;
    private Integer locationId;
    private List<DepartmentNode> children = new ArrayList<>();
    private List<EmployeeItem> employees = new ArrayList<>();
    private List<SelectItem> metrics = new ArrayList<>();

    public boolean hasChildren() {
        return children != null && !children.isEmpty();
    }

    public boolean hasChildrenMoreThanOne() {
        return children != null && !children.isEmpty() && children.size() > 1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ChildOrientation getChildOrientation() {
        return childOrientation;
    }

    public void setChildOrientation(ChildOrientation childOrientation) {
        this.childOrientation = childOrientation;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public List<DepartmentNode> getChildren() {
        return children;
    }

    public void setChildren(List<DepartmentNode> children) {
        this.children = children;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public List<EmployeeItem> getEmployees() {
        return employees;
    }

    public boolean hasEmployee() {
        return employees != null && !employees.isEmpty();
    }

    public void setEmployees(List<EmployeeItem> employees) {
        this.employees = employees;
    }

    public boolean isRoot() {
        return root;
    }

    public void setRoot(boolean root) {
        this.root = root;
    }

    public List<SelectItem> getMetrics() {
        return metrics;
    }

    public void setMetrics(List<SelectItem> metrics) {
        this.metrics = metrics;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getNumberData() {
        return numberData;
    }

    public void setNumberData(String numberData) {
        this.numberData = numberData;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean hasManager() {
        return manager;
    }

    public void setManager(boolean manager) {
        this.manager = manager;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public boolean isParentHasMoreThanOneChild() {
        return parentHasMoreThanOneChild;
    }

    public void setParentHasMoreThanOneChild(boolean parentHasMoreThanOneChild) {
        this.parentHasMoreThanOneChild = parentHasMoreThanOneChild;
    }

    public boolean isChangeSubDepColor() {
        return changeSubDepColor;
    }

    public void setChangeSubDepColor(boolean changeSubDepColor) {
        this.changeSubDepColor = changeSubDepColor;
    }
}
