package com.edatasite.workforce.core.domain.accounting;


import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "fai_events")
public class EdsFaiEvents extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private String integrationId;

    private Integer companyId;

    private String database;

    private String zatca_status;

    private String status;


    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getIntegrationId() {
        return integrationId;
    }

    public void setIntegrationId(String integrationId) {
        this.integrationId = integrationId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public String getZatca_status() {
        return zatca_status;
    }

    public void setZatca_status(String zatca_status) {
        this.zatca_status = zatca_status;
    }

}
