package com.edatasite.workforce.gwt.client.server.app;

import com.edatasite.workforce.core.domain.EdsClientContact;
import com.edatasite.workforce.core.domain.EdsUser;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 8/24/12
 * Time: 11:51 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ClientSupplierAccessService {
    Integer enableAccess(Integer contactID, Boolean fromSubscriptionForm, boolean sendActivationEmail);

    Integer disableAccess(Integer contactID);

    Boolean sendActivationLinkToClientContact(EdsClientContact clientContact, boolean userNameExist);

    void initClientGroups(EdsUser client);
}
