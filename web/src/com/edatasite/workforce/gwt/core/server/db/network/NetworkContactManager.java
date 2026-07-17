package com.edatasite.workforce.gwt.core.server.db.network;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.network.EdsNetwork;
import com.edatasite.workforce.core.domain.network.EdsNetworkContact;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: Aug 7, 2010
 * Time: 4:44:39 PM
 * To change this template use File | Settings | File Templates.
 */
public interface NetworkContactManager extends Manager<EdsNetworkContact> {

    List<EdsNetworkContact> getNetworkContacts(EdsNetwork network);
    EdsNetworkContact getNetworkContact(Integer networkId, Integer userContactID);
    List<EdsNetworkContact> getWaitingForApprovalContacts(EdsUser user);
    List<EdsNetworkContact> getNetworkPendingContacts(EdsNetwork network);
    List<EdsNetworkContact> getMyWantsToJoinContacts(EdsUser user);
    List<EdsNetworkContact> getMyRejectedContacts(EdsUser user);
}
