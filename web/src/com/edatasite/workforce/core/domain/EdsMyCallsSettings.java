package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.contact.client.rpc.MyCallsSettings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(schema = EdsScope.PUBLIC_SCHEMA, name = "mycalls_settings")
public class EdsMyCallsSettings extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

   @Column(name = "user_login")
   private String userLogin;

    @Column(name = "secret_key")
    private String secretKey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operator_id")
    private EdsUser operator;

    @Column(name = "deleted", columnDefinition = " boolean default false")
    private boolean deleted = false;

    private Integer companyId;


    public MyCallsSettings getRPC(){
        MyCallsSettings settings = new MyCallsSettings();
        settings.setObjectID(getObjectID());
        settings.setUserLogin(getUserLogin());
        settings.setOperator(getOperator() != null ? new SelectItem(getOperator().getObjectID(),getOperator().getName()) : null);
        settings.setSecretKey(getSecretKey());

        return settings;
    }



    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }


    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public EdsUser getOperator() {
        return operator;
    }

    public void setOperator(EdsUser operator) {
        this.operator = operator;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }
}
