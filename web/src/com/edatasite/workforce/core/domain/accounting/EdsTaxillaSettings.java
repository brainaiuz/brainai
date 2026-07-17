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
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "taxilla_settings")
public class EdsTaxillaSettings extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "grant_type")
    private String grandType;

    @Column(name = "client_id")
    private String clientID;

    @Column(name = "client_secret")
    private String clientSecret;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public String getGrandType() {
        return grandType;
    }

    public String getClientID() {
        return clientID;
    }

    public String getClientSecret() {
        return clientSecret;
    }
}
