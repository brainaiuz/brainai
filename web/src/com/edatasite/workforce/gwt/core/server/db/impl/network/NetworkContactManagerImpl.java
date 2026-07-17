package com.edatasite.workforce.gwt.core.server.db.impl.network;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.network.EdsNetwork;
import com.edatasite.workforce.core.domain.network.EdsNetworkContact;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.network.NetworkContactManager;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: Aug 7, 2010
 * Time: 5:01:14 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("networkContactManager")
public class NetworkContactManagerImpl extends BaseManager<EdsNetworkContact> implements NetworkContactManager {

    public NetworkContactManagerImpl() {
        super(EdsNetworkContact.class);
    }

    /**
     * If in EdsNetworkContact pending = false and confirmed = true, it means that the usercontact
     * has been confirmed and he/she is  an instance of EdsUser (thecontact type is COMPANY_USER).
     * Therefore  all  returned   EdsNetworkContact's usercontacts  are instances of EdsUser class
     * but not an instances of EdsCrmContact.
     *
     * @param network
     * @return
     */
    public List<EdsNetworkContact> getNetworkContacts(EdsNetwork network) {
        final Integer companyId= SecurityContext.getCompanyID();
        return findNative("SELECT distinct on(n.usercontact_id) n.usercontact_id,n.* from \"" +
                companyId + "\".networkcontact n where n.deleted<>true and n.pending=false and n.confirmed=true and n.network_id=" + network.getObjectID(),EdsNetworkContact.class);
    }

    public EdsNetworkContact getNetworkContact(Integer networkId, Integer userContactID) {
        return (EdsNetworkContact) findSingle("FROM EdsNetworkContact contact WHERE contact.network.objectID = ? AND " +
                "contact.deleted <> true AND contact.userContactID = ?", networkId, userContactID);
    }

    public List<EdsNetworkContact> getWaitingForApprovalContacts(EdsUser user) {
        return find("FROM EdsNetworkContact nc WHERE nc.userContactID = ? AND " +
                "nc.type = ? AND nc.pending = true AND nc.confirmed = false AND nc.invited = true AND nc.network.isDeleted <> true " +
                "ORDER BY nc.network.objectID DESC", getUser().getObjectID(), EdsNetworkContact.COMPANY_USER);
    }
    public List<EdsNetworkContact> getNetworkPendingContacts(EdsNetwork network) {
        final Integer companyId= SecurityContext.getCompanyID();
        return findNative("SELECT distinct on(n.usercontact_id) n.usercontact_id,n.* from \"" +
                companyId + "\".networkcontact n where n.deleted<>true and n.pending=true and n.network_id=" + network.getObjectID(),EdsNetworkContact.class);
    }

    public List<EdsNetworkContact> getMyWantsToJoinContacts(EdsUser user) {
        return find("SELECT nc FROM EdsNetworkContact nc " +
                "WHERE (nc.user.objectID = ? OR nc.user=?) AND nc.deleted <> true AND nc.pending<>false AND nc.invited=false", user.getObjectID(), user);
    }

     public List<EdsNetworkContact> getMyRejectedContacts(EdsUser user) {
        return find("SELECT nc FROM EdsNetworkContact nc " +
                "WHERE (nc.userContactID=?) AND nc.rejectconfirmed=true AND nc.invited<>true ORDER BY nc.objectID", user.getObjectID());
    }

}
