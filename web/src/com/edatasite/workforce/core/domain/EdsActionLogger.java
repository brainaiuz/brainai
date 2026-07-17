package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Abdulaziz
 * Date: Aug 28, 2009
 * Time: 4:27:14 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "actionlogger")
public class EdsActionLogger extends EdsObject {
   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    @Column
    private Integer creatorid;

    @Column
    private Integer companyid;

    @Column(length = 1000)
    private String methodName;

    @Column(length = 1000000)
    private String actionParams;

    @Column(length = 10000000)
    private String payload;

    @Column(length = 1000)
    private String serializationPolicyProvider;

    @Column(length = 100)
    private String session;

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public Integer getCreatorid() {
        return creatorid;
    }

    public void setCreatorid(Integer creatorid) {
        this.creatorid = creatorid;
    }

    public Integer getCompanyid() {
        return companyid;
    }

    public void setCompanyid(Integer companyid) {
        this.companyid = companyid;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getActionParams() {
        return actionParams;
    }

    public void setActionParams(String actionParams) {
        this.actionParams = actionParams;
    }

    public String getSerializationPolicyProvider() {
        return serializationPolicyProvider;
    }

    public void setSerializationPolicyProvider(String serializationPolicyProvider) {
        this.serializationPolicyProvider = serializationPolicyProvider;
    }
}
