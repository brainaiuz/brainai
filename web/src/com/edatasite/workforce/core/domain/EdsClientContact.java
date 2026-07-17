/**********************************************************************************************************************
 * LAST CHANGE                                                                                                        *
 * User: Hayot                                                                                                        *
 * Time: 2010/4/27 2:8:35                                                                                             *
 **********************************************************************************************************************/

package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.lucene.Indexable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: asomiddin
 * Date: 23.06.2007
 * Time: 15:14:39
 * To change this template use File | Settings | File Templates.
 */
@Entity
//@Indexed
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "clientcontact")
public class EdsClientContact extends EdsUser implements Indexable {

    private Boolean access;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crmcontactid")
    private EdsCrmContact crmContact;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clientid")
    private EdsCrmAccount client;

    public EdsCrmContact getCrmContact() {
        return crmContact;
    }

    public void setCrmContact(EdsCrmContact crmContact) {
        this.crmContact = crmContact;
    }

    public Boolean getAccess() {
        return access;
    }

    public void setAccess(Boolean access) {
        this.access = access;
    }

    public Integer getClientID() {
        if (crmContact != null && crmContact.getCrmAccount() != null && crmContact.getCrmAccount().getObjectID() != null) {
            return crmContact.getCrmAccount().getObjectID();
        } else {
            return client != null ? client.getObjectID() : null;
        }
    }

    public String getClientName() {
        if (crmContact != null && crmContact.getCrmAccount() != null && crmContact.getCrmAccount().getObjectID() != null) {
            return crmContact.getCrmAccount().getName();
        } else {
            return client != null ? client.getName() : null;
        }
    }

    public String getAccessMailSubject(EdsCompany company) {
        return getAccessType() + " access to " + company.getName();
    }

    public String getAccessType() {
        EdsCrmAccount crmAccount = getCrmContact().getCrmAccount();
        StringBuilder accessType = new StringBuilder();
        if (crmAccount.isClient() && crmAccount.isSupplier()) {
            accessType.append("Client/Supplier");
        } else if (crmAccount.isClient()) {
            accessType.append("Client");
        } else if (crmAccount.isSupplier()) {
            accessType.append("Supplier");
        }

        return accessType.toString();
    }
}
