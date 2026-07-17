package com.edatasite.workforce.core.domain.rbac.email;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.rbac.EdsGroup;
import com.edatasite.workforce.core.domain.settings.EdsEmailSetting;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Sancho
 * Date: 04.01.12
 * Time: 17:25
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "useremailrbac")
public class EdsUserEmailRbac extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    private EdsEmailSetting emailSetting;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    private EdsUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "groupid")
    private EdsGroup group;

    private Integer trusteeType;

    private String relationship;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsEmailSetting getEmailSetting() {
        return emailSetting;
    }

    public void setEmailSetting(EdsEmailSetting emailSetting) {
        this.emailSetting = emailSetting;
    }

    public EdsUser getUser() {
        return user;
    }

    public void setUser(EdsUser user) {
        this.user = user;
    }

    public EdsGroup getGroup() {
        return group;
    }

    public void setGroup(EdsGroup group) {
        this.group = group;
    }

    public Integer getTrusteeType() {
        return trusteeType;
    }

    public void setTrusteeType(Integer trusteeType) {
        this.trusteeType = trusteeType;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }
}
