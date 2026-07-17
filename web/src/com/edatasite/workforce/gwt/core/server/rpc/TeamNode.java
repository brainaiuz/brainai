package com.edatasite.workforce.gwt.core.server.rpc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dilsh0d on 21.03.16.
 */
public class TeamNode implements Serializable {
    private Integer id;
    private String name;
    private String desc;
    private Integer leaderId;
    private Integer depth;
    private Integer sorder;
    private TeamNode parent;
    private List<TeamNode> children;

    public TeamNode() {
    }

    public TeamNode(Integer id, String name, String desc, Integer leaderId, Integer depth, TeamNode parent, Integer sorder) {
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

    public TeamNode getParent() {
        return parent;
    }

    public void setParent(TeamNode parent) {
        this.parent = parent;
    }

    public List<TeamNode> getChildren() {
        if (children == null) {
            children = new ArrayList<>();
        }
        return children;
    }

    public void addChild(TeamNode child) {
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
