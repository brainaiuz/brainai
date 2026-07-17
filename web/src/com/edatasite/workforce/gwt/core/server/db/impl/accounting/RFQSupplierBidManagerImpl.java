package com.edatasite.workforce.gwt.core.server.db.impl.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsRFQSupplierBid;
import com.edatasite.workforce.gwt.core.server.db.accounting.RFQSupplierBidManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 8/10/12
 * Time: 3:44 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("rfqSupplierBidManager")
public class RFQSupplierBidManagerImpl extends BaseManager<EdsRFQSupplierBid> implements RFQSupplierBidManager {
    public RFQSupplierBidManagerImpl() {
        super(EdsRFQSupplierBid.class);
    }

    @Override
    public EdsRFQSupplierBid getSupplierBidByItem(Integer rfqItemID, Integer supplierID) {
        return (EdsRFQSupplierBid) findSingle("select bid from EdsRFQSupplierBid bid where bid.rfqItem.objectID = ? and bid.supplier.objectID = ?", rfqItemID, supplierID);
    }
}
