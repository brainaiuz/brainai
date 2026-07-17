package com.edatasite.workforce.gwt.core.server.db.impl.payroll;

import com.edatasite.workforce.core.domain.payrolluk.EdsPayrollGlobalSettings;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PayrollGlobalSettingsManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 9/21/15
 * Time: 6:46 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("paymentGlobalSettingsManager")
public class PayrollGlobalSettingsManagerImpl extends BaseManager<EdsPayrollGlobalSettings> implements PayrollGlobalSettingsManager {
    public PayrollGlobalSettingsManagerImpl() {
        super(EdsPayrollGlobalSettings.class);
    }

    @Override
    public List<EdsPayrollGlobalSettings> getSettingsList(ListingFilterParameter lfp) {
        return findInterval("select pds from EdsPayrollGlobalSettings pds where " + ServerUtils.checkForDeleted("pds.deleted") + " order by lastUpdate desc", lfp.getStart(), lfp.getLimit());
    }

    @Override
    public Integer getSettingsSize() {
        return find("select pds from EdsPayrollGlobalSettings pds where " + ServerUtils.checkForDeleted("pds.deleted")).size();
    }
}