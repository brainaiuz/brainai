package com.edatasite.workforce.core.domain.myactivity;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.*;

/**
 * User: Ilhombek
 * Date: Dec 2, 2009
 * Time: 11:52:16 AM
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "myactivitytype")
public class EdsMyActivityType extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "parentid")
    private EdsMyActivityType parent;

    @Column(name = "type")
    private String type;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    public EdsMyActivityType getParent() {
        return parent;
    }

    public void setParent(EdsMyActivityType parent) {
        this.parent = parent;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }
}
