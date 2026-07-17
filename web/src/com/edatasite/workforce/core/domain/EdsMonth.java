package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.*;


@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "month")
public class EdsMonth extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    public EdsMonth(Integer objectID, String name) {
        this.objectID = objectID;
        this.name = name;
    }

    public EdsMonth(Integer objectID) {
        super();
        this.objectID = objectID;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsMonth() {

    }


    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
