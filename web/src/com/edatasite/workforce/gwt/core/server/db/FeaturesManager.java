package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsFeatures;

/**
 * Created by IntelliJ IDEA.
 * User: Marat
 * Date: 13.03.12
 * Time: 12:44
 * To change this template use File | Settings | File Templates.
 */
public interface FeaturesManager extends Manager<EdsFeatures>{
    Boolean isFeatureShown(String message_code, Integer userId);
}
