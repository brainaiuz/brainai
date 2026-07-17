package com.edatasite.workforce.gwt.core.server.db;


import com.edatasite.workforce.gwt.core.server.rpc.GoogleGadgetDTO;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: User
 * Date: 26.05.12
 * Time: 18:09
 * To change this template use File | Settings | File Templates.
 */
public interface GoogleGadgetManager {
    ArrayList<GoogleGadgetDTO> authByOpenSocialId(final String openSocialId);

    ArrayList<GoogleGadgetDTO> findToken(final String token);

    Integer getUserAuthIdByUsername(String domainName, String username);

    void updateUserAuthId(GoogleGadgetDTO googleGadgetDTO);

    void insertTokenAndOpenSocialID(GoogleGadgetDTO googleGadgetDTO);

    void deleteGoogleGadgetUser(final Integer userid, final Integer companyid);
}
