package com.edatasite.workforce.gwt.core.server.db.impl.rbac;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.rbac.EdsGroup;
import com.edatasite.workforce.core.domain.rbac.EdsTrustee;
import com.edatasite.workforce.core.domain.rbac.EdsTrusteeType;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.TrusteeManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.TrusteeTypeManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * User: Abdulaziz
 * Date: Jan 7, 2010
 * Time: 12:41:53 PM
 */
@Repository("trusteeManager")
public class TrusteeManagerImpl extends BaseManager<EdsTrustee> implements TrusteeManager {
    public TrusteeManagerImpl() {
        super(EdsTrustee.class);
    }

    @Autowired
    private TrusteeTypeManager trusteeTypeManager;

    public EdsTrustee getTrustee(Integer trusteeID, Integer trusteeType) {
        return (EdsTrustee) findSingle("SELECT tr FROM EdsTrustee tr WHERE tr.trusteeID = ? AND tr.type.objectID = ?", trusteeID, trusteeType);
    }

    public EdsTrustee getTrustee(EdsUser user) {
        return getTrustee(user.getObjectID(), EdsTrusteeType.USER) != null ? getTrustee(user.getObjectID(), EdsTrusteeType.USER) : createUserTrustee(user);
    }

    public EdsTrustee getTrustee(EdsGroup group) {
        return getTrustee(group.getObjectID(), EdsTrusteeType.GROUP) != null ? getTrustee(group.getObjectID(), EdsTrusteeType.GROUP) : createGroupTrustee(group);
    }

    private EdsTrustee createTrustee(Integer trusteeID, Integer trusteeType) {
        EdsTrustee trustee = new EdsTrustee();
        trustee.setTrusteeID(trusteeID);
        trustee.setType(trusteeTypeManager.getTrusteeType(trusteeType));
        create(trustee);
        return trustee;
    }

    private EdsTrustee createUserTrustee(EdsUser user) {
        return createTrustee(user.getObjectID(), EdsTrusteeType.USER);
    }

    private EdsTrustee createGroupTrustee(EdsGroup group) {
        return createTrustee(group.getObjectID(), EdsTrusteeType.GROUP);
    }


}
