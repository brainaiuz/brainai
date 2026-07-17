package com.edatasite.workforce.gwt.core.server.db.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsRFQSupplierBid;
import com.edatasite.workforce.gwt.core.server.db.Manager;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 8/10/12
 * Time: 3:43 PM
 * To change this template use File | Settings | File Templates.
 */
public interface RFQSupplierBidManager extends Manager<EdsRFQSupplierBid>{
    EdsRFQSupplierBid getSupplierBidByItem(Integer rfqItemID, Integer objectID);
}
