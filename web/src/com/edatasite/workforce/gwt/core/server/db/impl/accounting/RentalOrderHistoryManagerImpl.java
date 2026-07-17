package com.edatasite.workforce.gwt.core.server.db.impl.accounting;

import com.edatasite.workforce.core.domain.EdsRentalOrderHistory;
import com.edatasite.workforce.gwt.core.server.db.RentalOrderHistoryManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("rentalOrderHistoryManager")
public class RentalOrderHistoryManagerImpl extends BaseManager<EdsRentalOrderHistory> implements RentalOrderHistoryManager {

    public RentalOrderHistoryManagerImpl() {
        super(EdsRentalOrderHistory.class);
    }

    @Override
    public List<EdsRentalOrderHistory> getComments(Integer rentalOrderId) {
        return find("select roh from EdsRentalOrderHistory roh where roh.rentalOrder.objectID=" + rentalOrderId);
    }
}
