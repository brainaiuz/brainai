package com.edatasite.workforce.gwt.core.server.db.impl.rbac.contact;

import com.edatasite.workforce.core.domain.crm.contact.EdsContactPolicy;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: Oct 25, 2010
 * Time: 4:22:21 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("contactPolicyManager")
public class ContactPolicyManagerImpl extends BaseManager<EdsContactPolicy> implements ContactPolicyManager {
    public ContactPolicyManagerImpl() {
        super(EdsContactPolicy.class);
    }

    public EdsContactPolicy getCompanyRelationPolicy(String relationshipCode) {
        return (EdsContactPolicy) findSingle("SELECT cP FROM EdsContactPolicy cP WHERE cP.entryType = ? AND cP.relation.code = ?", EdsContactPolicy.BUILT_IN, relationshipCode);
    }
}