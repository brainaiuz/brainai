package com.edatasite.workforce.core.domain.magento;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsUser;
import com.google.code.magja.soap.SoapConfig;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Created by Shohruh on 06 Dec 2016.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "magentoApiSettings")
public class EdsMagentoApiSettings extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;
    private String apiUrl;
    private String apiUser;
    private String apiKey;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private EdsUser kpiUser;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getApiUser() {
        return apiUser;
    }

    public void setApiUser(String apiUser) {
        this.apiUser = apiUser;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public SoapConfig getSoapConfig() {
        return new SoapConfig(apiUser, apiKey, apiUrl);
    }

    public EdsUser getKpiUser() {
        return kpiUser;
    }

    public void setKpiUser(EdsUser kpiUser) {
        this.kpiUser = kpiUser;
    }
}
