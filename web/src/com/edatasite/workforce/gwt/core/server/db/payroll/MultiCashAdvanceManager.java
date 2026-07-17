package com.edatasite.workforce.gwt.core.server.db.payroll;

import com.edatasite.workforce.core.domain.payrolluk.EdsMultiCashAdvance;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;


public interface MultiCashAdvanceManager extends Manager<EdsMultiCashAdvance> {

    Integer getCashAdvanceIntNumber();

    boolean numberExists(String numberString, Integer objectId);

    List<EdsMultiCashAdvance> getMultiCashAdvanceList(ListingFilterParameter fp);

    Integer getMultiCashAdvanceCount(ListingFilterParameter fp);
}
