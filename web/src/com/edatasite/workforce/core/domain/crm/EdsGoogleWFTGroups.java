package com.edatasite.workforce.core.domain.crm;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsUser;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 29.06.11
 * Time: 15:41
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "crmgooglewftgroups")
public class EdsGoogleWFTGroups extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "googlegroupid")
    private String googleGroupID;

    @Column(name = "wftidname")
    private Integer wftGroupID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    private EdsUser user;

    @Column(name = "deleted")
    private Boolean deleted;

    @Column(name = "isOfficeGroup")
    private Boolean isOfficeGroup = false;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public String getGoogleGroupID() {
        return googleGroupID;
    }

    public void setGoogleGroupID(String googleGroupID) {
        this.googleGroupID = googleGroupID;
    }

    public Integer getWftGroupID() {
        return wftGroupID;
    }

    public void setWftGroupID(Integer wftGroupID) {
        this.wftGroupID = wftGroupID;
    }

    public EdsUser getUser() {
        return user;
    }

    public void setUser(EdsUser user) {
        this.user = user;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Boolean getOfficeGroup() {
        return isOfficeGroup;
    }

    public void setOfficeGroup(Boolean officeGroup) {
        isOfficeGroup = officeGroup;
    }
}
