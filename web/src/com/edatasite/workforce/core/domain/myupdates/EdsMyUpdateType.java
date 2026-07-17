package com.edatasite.workforce.core.domain.myupdates;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.CascadeType;
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
 * User: Abdulaziz
 * Date: Jan 6, 2010
 * Time: 3:44:44 PM
 */
@Entity
@Table(schema = EdsScope.PUBLIC_SCHEMA, name = "myupdatetype")
public class EdsMyUpdateType extends EdsObject {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;


    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "parentId")
    private EdsMyUpdateType parent;

//    @ManyToOne(cascade = { CascadeType.PERSIST }, fetch = FetchType.LAZY)
//    @JoinColumn(name = "messageid")
//    private EdsMyUpdateMessage message;

//    private String type;
    private String code;
    private String description;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsMyUpdateType getParent() {
        return parent;
    }

    public void setParent(EdsMyUpdateType parent) {
        this.parent = parent;
    }

//    public EdsMyUpdateMessage getMessage() {
//        return message;
//    }
//
//    public void setMessage(EdsMyUpdateMessage message) {
//        this.message = message;
//    }
//
//    public String getType() {
//        return type;
//    }
//
//    public void setType(String type) {
//        this.type = type;
//    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
