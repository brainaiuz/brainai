package com.edatasite.workforce.gwt.core.server.db.payroll;

import com.edatasite.workforce.core.domain.payrolluk.EdsPayrollGlobalSettings;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 9/21/15
 * Time: 6:47 PM
 * To change this template use File | Settings | File Templates.
 */
public interface PayrollGlobalSettingsManager extends Manager<EdsPayrollGlobalSettings> {

    List<EdsPayrollGlobalSettings> getSettingsList(ListingFilterParameter lfp);

    Integer getSettingsSize();
}
