package com.edatasite.workforce.gwt.core.server.db.impl.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsPriceLevelBB;
import com.edatasite.workforce.gwt.core.server.db.accounting.PriceLevelBBManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 2/16/16
 * Time: 3:12 AM
 * To change this template use File | Settings | File Templates.
 */
@Repository("priceLevelBBManager")
public class PriceLevelBBManagerImpl extends BaseManager<EdsPriceLevelBB> implements PriceLevelBBManager {

    public PriceLevelBBManagerImpl() {
        super(EdsPriceLevelBB.class);
    }

    @Override
    public void deleteByPriceLevel(Integer objectID) {

        if (objectID == null) {
            return;
        }
        update("delete FROM EdsPriceLevelBB bbpl WHERE bbpl.priceLevel.objectID = ?", objectID);
    }
}
