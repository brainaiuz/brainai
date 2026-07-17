package com.edatasite.workforce.gwt.core.server.db.payroll;

import com.edatasite.workforce.core.domain.payrolluk.EdsOvertimeObject;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

public interface OvertimeManager extends Manager<EdsOvertimeObject> {
    List<EdsOvertimeObject> getAllItems(ListingFilterParameter filterParametrs);

    Integer getTotalUndeletedItemCount();

    Integer getOvertimeLastIntNumber();
}
