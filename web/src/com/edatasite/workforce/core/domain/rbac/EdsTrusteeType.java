package com.edatasite.workforce.core.domain.rbac;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * User: Abdulaziz
 * Date: Jan 6, 2010
 * Time: 10:46:26 PM
 */
@Entity
@Table(schema = EdsScope.PUBLIC_SCHEMA, name = "trusteetype")
public class EdsTrusteeType extends EdsObject {
    public static final Integer ENTITY = 1;
    public static final Integer USER = 2;
    public static final Integer GROUP = 3;
    public static final Integer SERVICE = 4;
    public static final Integer CLIENT = 5;
    public static final Integer CONTACT = 6;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;
    private String name;
    @Column(name = "description")
    private String description;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
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
}
