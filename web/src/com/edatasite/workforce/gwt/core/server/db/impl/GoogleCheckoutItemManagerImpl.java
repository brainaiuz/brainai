package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsGoogleCheckoutItem;
import com.edatasite.workforce.gwt.core.server.db.GoogleCheckoutItemManager;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Aug 3, 2010
 * Time: 12:15:07 AM
 * To change this template use File | Settings | File Templates.
 */
@Repository("googleCheckoutItemManager")
public class GoogleCheckoutItemManagerImpl extends BaseManager<EdsGoogleCheckoutItem> implements GoogleCheckoutItemManager {
    public GoogleCheckoutItemManagerImpl() {
        super(EdsGoogleCheckoutItem.class);
    }
}
