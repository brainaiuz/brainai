package com.edatasite.workforce.core.domain.webhook;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * User : Akhror
 * Date : 04.03.2025
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "webhook_attribute")
public class EdsPublicWebhookAttribute extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "webhookId")
    private Integer webhookId;

    @ManyToOne
    @JoinColumn(name = "webhookId", updatable = false, insertable = false, foreignKey = @ForeignKey(name = "none"))
    private EdsPublicWebHook webHook;

    private String key;

    @Column(name = "value")
    private String value;

    @Column(name = "query_value")
    private String queryValue;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getWebhookId() {
        return webhookId;
    }

    public void setWebhookId(Integer webhookId) {
        this.webhookId = webhookId;
    }

    public EdsPublicWebHook getWebHook() {
        return webHook;
    }

    public void setWebHook(EdsPublicWebHook webHook) {
        this.webHook = webHook;
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

    public String getQueryValue() {
        return queryValue;
    }

    public void setQueryValue(String queryValue) {
        this.queryValue = queryValue;
    }
}
