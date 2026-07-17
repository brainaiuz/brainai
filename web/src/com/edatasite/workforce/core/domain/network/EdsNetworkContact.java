package com.edatasite.workforce.core.domain.network;

import com.edatasite.shared.db.EdsScope;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: Aug 7, 2010
 * Time: 4:22:25 PM
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "networkcontact")
public class EdsNetworkContact extends EdsCooContact {

    public static final String COMPANY_USER = "COMPANY_USER";
    public static final String CRM_CONTACT = "CRM_CONTACT";

    @Column(name = "deleted")
    private boolean deleted;

    /**
     * We have two types of contacts:
     * 1. Contacts whom sent invitation to join to the network. (In this case invited value will return true.)
     * 2. Contacts who want to join to the network and wait for network creator's approval. (In this case it will return false.)
     *
     * In the first case it will return true, otherwise false.
     */
    @Column(name = "invited")
    private boolean invited;

    @Column(name = "rejectconfirmed")
    private Boolean rejectconfirmed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "network_id")
    private EdsNetwork network;

    @Column(name = "type")
    private String type;//Type of the contact, either EdsUser or EdsCrmContact.

    /**
     * Here maybe EdsUser objectID or EdsCrmContact objectID.  If there is EdsUser objectID,  it will be ok,
     * because the EdsUser is physical object and we are working only with EdsUser type contacts (there will
     * be no problem to be peer to peer with him/her). But if the userContactID is the id of the crmContact,
     * it means that in order  to become accessible  to be peer contact,  it has to be registered to the COO
     * system that means he/she will be created as EdsUser for current crmContact.After registering, we have
     * to update the data.  The id of the EdsCrmContact will be replaced with EdsUser's id.  Thus this is an 
     * integer value, in order to update the id easily.
     */
    @Column(name = "usercontact_id")
    private Integer userContactID;

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isInvited() {
        return invited;
    }

    public void setInvited(boolean invited) {
        this.invited = invited;
    }

    public EdsNetwork getNetwork() {
        return network;
    }

    public void setNetwork(EdsNetwork network) {
        this.network = network;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getUserContactID() {
        return userContactID;
    }

    public void setUserContactID(Integer userContactID) {
        this.userContactID = userContactID;
    }

    public Boolean isRejectconfirmed() {
        return rejectconfirmed;
    }

    public void setRejectconfirmed(Boolean rejectconfirmed) {
        this.rejectconfirmed = rejectconfirmed;
    }
}
