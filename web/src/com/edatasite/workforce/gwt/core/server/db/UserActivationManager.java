package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.EdsUserActivation;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: SherzodMuratov
 * Date: 25.02.2009
 * Time: 12:20:11
 * To change this template use File | Settings | File Templates.
 */
public interface UserActivationManager extends Manager<EdsUserActivation> {
    List<EdsUser> getPandingUsers();

    List<EdsUserActivation> getNonMailSentUsers(Date date);
}
