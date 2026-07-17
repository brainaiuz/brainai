package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsSocialNetworks;
import com.edatasite.workforce.core.domain.EdsUser;

/**
 * Created by IntelliJ IDEA.
 * User: HRS
 * Date: 21.12.2009
 * Time: 11:36:39
 * To change this template use File | Settings | File Templates.
 */
public interface SocialNetworksManager extends Manager<EdsSocialNetworks> {
    EdsSocialNetworks getTwitterAccount(EdsUser user);

    EdsSocialNetworks getFacebookAccount(EdsUser user);

    EdsSocialNetworks getLinkedinAccount(EdsUser user);

    void deleteSocialNetworks(EdsUser user, EdsSocialNetworks sn);

    Integer createTwitter(EdsUser user, EdsSocialNetworks sn);

    Integer createFacebook(EdsUser user, EdsSocialNetworks sn);

    Integer createLinkedin(EdsUser user, EdsSocialNetworks sn);

}
