package com.edatasite.workforce.core.domain.crm.contact;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.core.client.rpc.CustomTableRpc;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "crmContactCustomItemTable")
public class EdsCrmContactCustomItemTable extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;


    @Column(name = "name", length = 1000)
    @Type(type = "text")
    private String name;

    @Type(type = "text")
    private String description;

    private String uuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crmContact_id")
    private EdsCrmContact crmContact;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customfieldsid")
    private EdsCrmContactItemTableCF customFields;

    public CustomTableRpc getRpc() {
        CustomTableRpc rpc = new CustomTableRpc();
        rpc.setId(getObjectID());
        rpc.setUuid(getUuid());
        rpc.setItemName(getName());
        rpc.setDescription(getDescription());
        return rpc;
    }

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    @Override
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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public EdsCrmContact getCrmContact() {
        return crmContact;
    }

    public void setCrmContact(EdsCrmContact crmContact) {
        this.crmContact = crmContact;
    }

    public EdsCrmContactItemTableCF getCustomFields() {
        return customFields;
    }

    public void setCustomFields(EdsCrmContactItemTableCF customFields) {
        this.customFields = customFields;
    }
}
