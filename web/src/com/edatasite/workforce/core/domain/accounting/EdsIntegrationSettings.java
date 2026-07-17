package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.core.client.ui.Constants;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Shohruh on 02-Feb-17.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "integrationSettings")
public class EdsIntegrationSettings extends EdsObject {

    public static final String TARGET_URL = Constants.TARGET_URL;
    public static final String TARGET_USERNAME = Constants.TARGET_USERNAME;
    public static final String TARGET_PASSWORD = Constants.TARGET_PASSWORD;
    public static final String TARGET_CONTROLLER = Constants.TARGET_CONTROLLER;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "key")
    private String key;

    @Column(name = "value")
    private String value;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
