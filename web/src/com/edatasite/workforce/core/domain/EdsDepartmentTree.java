package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by dilsh0d on 18.03.16.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "team_tree")
public class EdsDepartmentTree extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "parent_id")
    private Integer parentId;

    @Column(name = "child_id")
    private Integer childId;

    @Column(name = "depth", columnDefinition = "int default 0")
    private Integer depth = 0;

    @Column(name = "sorder", columnDefinition = "int default 1024 ")
    private Integer sorder = 1024;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id",updatable = false,insertable = false)
    private EdsDepartment parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id",updatable = false,insertable = false)
    private EdsDepartment child;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getChildId() {
        return childId;
    }

    public void setChildId(Integer childId) {
        this.childId = childId;
    }

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public EdsDepartment getParent() {
        return parent;
    }

    public EdsDepartment getChild() {
        return child;
    }

    public Integer getSorder() {
        return sorder;
    }

    public void setSorder(Integer sorder) {
        this.sorder = sorder;
    }
}
