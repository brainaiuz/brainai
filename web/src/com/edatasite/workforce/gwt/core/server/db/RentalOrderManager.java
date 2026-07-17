package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsRentalOrder;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

public interface RentalOrderManager extends Manager<EdsRentalOrder> {
    Integer getOrderLastIntNumber();

    List<EdsRentalOrder> getRentalOrderList(ListingFilterParameter fp);

    Integer getRentalOrderCount(ListingFilterParameter fp);

    void deleteRentalOrderItems(Integer objectID);

    boolean isRentOrderNumberExist(String numberString, Integer objectID);
}
