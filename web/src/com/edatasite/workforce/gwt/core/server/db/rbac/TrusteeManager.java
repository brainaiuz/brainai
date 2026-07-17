package com.edatasite.workforce.gwt.core.server.db.rbac;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.rbac.EdsGroup;
import com.edatasite.workforce.core.domain.rbac.EdsTrustee;
import com.edatasite.workforce.gwt.core.server.db.Manager;

/**
 * User: Abdulaziz
 * Date: Jan 7, 2010
 * Time: 12:27:19 PM
 */
public interface TrusteeManager extends Manager<EdsTrustee> {
    EdsTrustee getTrustee(Integer trusteeID, Integer trusteeType);

    EdsTrustee getTrustee(EdsUser user);

    EdsTrustee getTrustee(EdsGroup group);

//    EdsTrustee createTrustee(Integer trusteeID, Integer trusteeType);
//
//    EdsTrustee createUserTrustee(EdsUser user);
//
//    EdsTrustee createGroupTrustee(EdsGroup group);
}
