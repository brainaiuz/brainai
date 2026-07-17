package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsFeatures;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.ui.FeatureConstants;
import com.edatasite.workforce.gwt.core.server.db.FeaturesManager;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User: Marat
 * Date: 13.03.12
 * Time: 12:45
 */
@Repository
public class FeaturesManagerImpl extends BaseManager<EdsFeatures> implements FeaturesManager, FeatureConstants {
    public FeaturesManagerImpl() {
        super(EdsFeatures.class);
    }

    public Boolean isFeatureShown(String message_code, Integer userID) {
        boolean isShown = false;
        String companyId = ServerSecurityContext.getInstance().getCompanyId();
        if (!"".equals(companyId) || companyId != null) {
            EdsFeatures feature = (EdsFeatures) findSingle("SELECT f FROM EdsFeatures f WHERE f.message_code=? AND f.user.objectID=?",message_code,userID);
            if (feature!= null && feature.getObjectID()!=null) {
                isShown = true;
            } else {
                EdsFeatures newFeature = new EdsFeatures();
                newFeature.setMessage_code(message_code);
                EdsUser user = (EdsUser) findSingle("SELECT u FROM EdsUser u where u.objectID=?",userID);
                newFeature.setUser(user);
                create(newFeature);
                isShown = false;
            }
        } else {
            isShown = true;
        }
        return isShown;
    }
}
