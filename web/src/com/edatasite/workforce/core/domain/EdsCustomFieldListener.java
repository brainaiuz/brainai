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
 * Created with IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: 6/14/12
 * Time: 2:11 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "customFieldListener")
public class EdsCustomFieldListener extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customfield_id")
    private EdsCompanyCustomFieldsSettings customfield;

    private String listenerCode;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "joinedfield_id")
    private EdsCompanyCustomFieldsSettings joinedfield;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsCompanyCustomFieldsSettings getCustomfield() {
        return customfield;
    }

    public void setCustomfield(EdsCompanyCustomFieldsSettings customfield) {
        this.customfield = customfield;
    }

    public String getListenerCode() {
        return listenerCode;
    }

    public void setListenerCode(String listenerCode) {
        this.listenerCode = listenerCode;
    }

    public EdsCompanyCustomFieldsSettings getJoinedfield() {
        return joinedfield;
    }

    public void setJoinedfield(EdsCompanyCustomFieldsSettings joinedfield) {
        this.joinedfield = joinedfield;
    }
}
