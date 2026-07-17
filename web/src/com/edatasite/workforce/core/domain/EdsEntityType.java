package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Created by Omonullo on 5/15/2017.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "entitytype", uniqueConstraints = @UniqueConstraint(columnNames = {"code"}))
public class EdsEntityType extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private String code;

    @Column(name="name", nullable = false)
    private String name;

    private Integer selectedObjectId;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSelectedObjectId() {
        return selectedObjectId;
    }

    public void setSelectedObjectId(Integer selectedObjectId) {
        this.selectedObjectId = selectedObjectId;
    }
}
