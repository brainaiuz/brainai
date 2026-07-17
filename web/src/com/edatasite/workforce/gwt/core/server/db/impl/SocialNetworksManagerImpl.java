package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsSocialNetworks;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.server.db.SocialNetworksManager;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User: HRS
 * Date: 21.12.2009
 * Time: 11:35:33
 * To change this template use File | Settings | File Templates.
 */
@Repository("socialNetworksManager")
public class SocialNetworksManagerImpl extends BaseManager<EdsSocialNetworks> implements SocialNetworksManager {
    private static final int TWITTER = 1;
    private static final int FACEBOOK = 2;
    private static final int LINKEDIN = 3;
    private EdsUser user;


    public SocialNetworksManagerImpl() {
        super(EdsSocialNetworks.class);
    }

    public EdsSocialNetworks getTwitterAccount(EdsUser user) {
        this.user = user;
        return getAccount(TWITTER);
    }

    public EdsSocialNetworks getFacebookAccount(EdsUser user) {
        this.user = user;
        return getAccount(FACEBOOK);
    }

    public EdsSocialNetworks getLinkedinAccount(EdsUser user) {
        this.user = user;
        return getAccount(LINKEDIN);
    }

    public EdsSocialNetworks getAccount(int type) {
        return (EdsSocialNetworks) findSingle("from EdsSocialNetworks sn where sn.user = ? and sn.deleted = false and type = ? order by objectID desc", this.user, type);
    }

    public void deleteSocialNetworks(EdsUser user, EdsSocialNetworks sn) {
        this.user = user;
        if (sn != null) {
            update("update EdsSocialNetworks sn set sn.deleted= true " +
                    "where sn.user = ? and sn = ? and sn.deleted<>true ", this.user, sn);
        }
    }

    public Integer createTwitter(EdsUser user, EdsSocialNetworks sn) {
        sn.setType(TWITTER);
        sn.setUser(user);
        create(sn);
        return sn.getObjectID();
    }

    public Integer createFacebook(EdsUser user, EdsSocialNetworks sn) {
        sn.setType(FACEBOOK);
        sn.setUser(user);
        create(sn);
        return sn.getObjectID();
    }

    public Integer createLinkedin(EdsUser user, EdsSocialNetworks sn) {
        sn.setType(LINKEDIN);
        sn.setUser(user);
        create(sn);
        return sn.getObjectID();
    }

}
