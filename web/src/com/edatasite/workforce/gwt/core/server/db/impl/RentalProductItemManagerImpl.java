package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsRentalProductItem;
import org.springframework.stereotype.Repository;


@Repository("rentalProductItemManager")
public class RentalProductItemManagerImpl extends BaseManager<EdsRentalProductItem> {

    public RentalProductItemManagerImpl() {
        super(EdsRentalProductItem.class);
    }
}
