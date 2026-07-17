package com.edatasite.workforce.gwt.core.server.db.impl.customfields;

import com.edatasite.workforce.core.domain.customfields.EdsBookingItemCustomFields;
import com.edatasite.workforce.gwt.core.server.db.customfields.BookingItemCFManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

@Repository("bookingItemCFManager")
public class BookingItemCFManagerImpl extends BaseManager<EdsBookingItemCustomFields> implements BookingItemCFManager {
    public BookingItemCFManagerImpl() {
        super(EdsBookingItemCustomFields.class);
    }
}
