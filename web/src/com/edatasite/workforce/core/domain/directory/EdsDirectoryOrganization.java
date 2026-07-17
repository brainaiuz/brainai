package com.edatasite.workforce.core.domain.directory;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;

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
 * User: Sanjar
 * Date: Oct 6, 2010
 * Time: 1:51:05 PM
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "directoryorganization")
public class EdsDirectoryOrganization extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "directoryid")
    private EdsDirectory directory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountid")
    private EdsCrmAccount account;

    private Boolean isFeatured;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsDirectory getDirectory() {
        return directory;
    }

    public void setDirectory(EdsDirectory directory) {
        this.directory = directory;
    }

    public EdsCrmAccount getAccount() {
        return account;
    }

    public void setAccount(EdsCrmAccount account) {
        this.account = account;
    }

    public Boolean getFeatured() {
        return isFeatured;
    }

    public void setFeatured(Boolean featured) {
        isFeatured = featured;
    }
}
