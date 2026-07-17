package com.edatasite.workforce.gwt.core.server.db.impl.rbac.contact;

import com.edatasite.workforce.core.domain.crm.contact.EdsContactPolicy;
import com.edatasite.workforce.gwt.core.server.db.Manager;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: Oct 25, 2010
 * Time: 4:23:13 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ContactPolicyManager extends Manager<EdsContactPolicy> {

    EdsContactPolicy getCompanyRelationPolicy(String relationshipCode);
}