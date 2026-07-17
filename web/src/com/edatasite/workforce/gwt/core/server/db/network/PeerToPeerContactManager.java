package com.edatasite.workforce.gwt.core.server.db.network;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.network.EdsPeerToPeerContact;
import com.edatasite.workforce.gwt.core.server.db.Manager;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: Aug 7, 2010
 * Time: 4:45:46 PM
 * To change this template use File | Settings | File Templates.
 */
public interface PeerToPeerContactManager extends Manager<EdsPeerToPeerContact> {

    EdsPeerToPeerContact getPeerContact(String userEmail, String peerEmail);
    boolean isUserContact(EdsUser user, EdsUser peer);
}
