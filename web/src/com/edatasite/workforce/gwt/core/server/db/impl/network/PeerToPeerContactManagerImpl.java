package com.edatasite.workforce.gwt.core.server.db.impl.network;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.network.EdsPeerToPeerContact;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.network.PeerToPeerContactManager;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: Aug 7, 2010
 * Time: 4:50:52 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("peerToPeerContactManager")
public class PeerToPeerContactManagerImpl extends BaseManager<EdsPeerToPeerContact> implements PeerToPeerContactManager {

    public PeerToPeerContactManagerImpl() {
        super(EdsPeerToPeerContact.class);
    }

    public EdsPeerToPeerContact getPeerContact(String userEmail, String peerEmail) {
        return (EdsPeerToPeerContact) findSingle("from EdsPeerToPeerContact contact where contact.user.email = ? " +
                                                              "and contact.peer.email = ?", userEmail, peerEmail);
    }

    public boolean isUserContact(EdsUser user, EdsUser peer) {
        if (user.getEmail() == null || peer.getEmail() == null) {
            return false;
        }

        return getPeerContact(user.getEmail(), peer.getEmail()) != null;
    }
}
