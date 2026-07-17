package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.List;

public class ChartNode implements IsSerializable {

    private Integer id;
    private String name;
    private String desc;
    private Integer leaderId;
    private Integer depth;
    private Integer sorder;
    private ChartNode parent;
    private List<ChartNode> children;

    public ChartNode() {
    }

    public ChartNode(Integer id, String name, String desc, Integer leaderId, Integer depth, ChartNode parent, Integer sorder) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.leaderId = leaderId;
        this.depth = depth;
        this.parent = parent;
        this.sorder = sorder;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(Integer leaderId) {
        this.leaderId = leaderId;
    }

    public Integer getSorder() {
        return sorder;
    }

    public void setSorder(Integer sorder) {
        this.sorder = sorder;
    }

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public ChartNode getParent() {
        return parent;
    }

    public void setParent(ChartNode parent) {
        this.parent = parent;
    }

    public List<ChartNode> getChildren() {
        if (children == null) {
            children = new ArrayList<>();
        }
        return children;
    }

    public void addChild(ChartNode child) {
        getChildren().add(child);
    }

    public boolean isRoot() {
        return (this.parent == null);
    }

    @Override
    public String toString() {
        return "id=" + id + ", name=" + name + ", parent=" + parent;
    }
}
