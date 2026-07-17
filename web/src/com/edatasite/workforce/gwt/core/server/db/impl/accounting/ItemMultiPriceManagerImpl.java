package com.edatasite.workforce.gwt.core.server.db.impl.accounting;

import com.edatasite.workforce.core.domain.EdsItemMultiPrice;
import com.edatasite.workforce.gwt.core.server.db.accounting.ItemMultiPriceManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 2/27/16
 * Time: 12:28 AM
 * To change this template use File | Settings | File Templates.
 */
@Repository("itemMultiPriceManager")
public class ItemMultiPriceManagerImpl extends BaseManager<EdsItemMultiPrice> implements ItemMultiPriceManager {

    public ItemMultiPriceManagerImpl(){
        super(EdsItemMultiPrice.class);
    }

    @Override
    public void deleteItemMultiPrices(Integer itemID) {
        update("DELETE FROM EdsItemMultiPrice imp WHERE imp.item.objectID = ?", itemID);
    }
}
