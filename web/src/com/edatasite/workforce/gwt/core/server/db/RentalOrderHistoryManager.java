package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsRentalOrderHistory;

import java.util.List;

public interface RentalOrderHistoryManager extends Manager<EdsRentalOrderHistory> {

    List<EdsRentalOrderHistory> getComments(Integer rentalOrderId);

}
