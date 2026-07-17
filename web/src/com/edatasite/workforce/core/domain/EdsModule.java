package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Aziz
 * Date: Apr 24, 2014
 */

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "mymodule")
public class EdsModule extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "code", unique = true)
    private String code;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "section", unique = true)
    private String section;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "sorder", columnDefinition = "int default 0")
    private Integer sorder = 0;


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

    public String getSection() {
        return section;
    }

    public void setSection(String module) {
        this.section = module;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getSorder() {
        return sorder;
    }

    public void setSorder(Integer sorder) {
        this.sorder = sorder;
    }
}
