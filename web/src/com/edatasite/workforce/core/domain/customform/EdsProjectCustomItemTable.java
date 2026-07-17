package com.edatasite.workforce.core.domain.customform;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.customfields.EdsProjectItemTableCF;
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
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "project_item_table")
public class EdsProjectCustomItemTable extends EdsObject {

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
    @JoinColumn(name = "project_id")
    private EdsProject project;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customfieldsid")
    private EdsProjectItemTableCF customFields;

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

    public EdsProject getProject() {
        return project;
    }

    public void setProject(EdsProject project) {
        this.project = project;
    }

    public EdsProjectItemTableCF getCustomFields() {
        return customFields;
    }

    public void setCustomFields(EdsProjectItemTableCF customFields) {
        this.customFields = customFields;
    }
}
