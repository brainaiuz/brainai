package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsRentalOrderItem;
import com.edatasite.workforce.gwt.core.server.db.RentalOrderItemManager;
import org.springframework.stereotype.Repository;

@Repository("rentalOrderItemManager")
public class RentalOrderItemManagerImpl extends BaseManager<EdsRentalOrderItem> implements RentalOrderItemManager {

    public RentalOrderItemManagerImpl() {
        super(EdsRentalOrderItem.class);
    }
}