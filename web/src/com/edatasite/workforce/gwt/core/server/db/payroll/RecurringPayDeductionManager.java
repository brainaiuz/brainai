package com.edatasite.workforce.gwt.core.server.db.payroll;

import com.edatasite.workforce.core.domain.payrolluk.EdsRecurringPayDeduction;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

public interface RecurringPayDeductionManager extends Manager<EdsRecurringPayDeduction> {
    List<EdsRecurringPayDeduction> getAllItems(ListingFilterParameter fp);

    Integer getTotalCount();
}
