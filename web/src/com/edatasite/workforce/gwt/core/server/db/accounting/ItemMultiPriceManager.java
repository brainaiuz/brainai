package com.edatasite.workforce.gwt.core.server.db.accounting;

import com.edatasite.workforce.core.domain.EdsItemMultiPrice;
import com.edatasite.workforce.gwt.core.server.db.Manager;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 2/27/16
 * Time: 12:26 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ItemMultiPriceManager extends Manager<EdsItemMultiPrice> {

    void deleteItemMultiPrices(Integer itemID);
}

