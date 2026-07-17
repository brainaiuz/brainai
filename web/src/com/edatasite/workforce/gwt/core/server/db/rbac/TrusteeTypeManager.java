package com.edatasite.workforce.gwt.core.server.db.rbac;

import com.edatasite.workforce.core.domain.rbac.EdsTrusteeType;
import com.edatasite.workforce.gwt.core.server.db.Manager;

/**
 * User: Abdulaziz
 * Date: Jan 7, 2010
 * Time: 12:27:55 PM
 */
public interface TrusteeTypeManager extends Manager<EdsTrusteeType> {
    EdsTrusteeType getTrusteeType(Integer typeID);

    EdsTrusteeType creageTrusteeType(String name, String description);

}
