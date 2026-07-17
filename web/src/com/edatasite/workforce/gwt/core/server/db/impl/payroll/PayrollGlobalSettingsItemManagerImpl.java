package com.edatasite.workforce.gwt.core.server.db.impl.payroll;

import com.edatasite.workforce.core.domain.payrolluk.EdsPayrollGlobalSettingsItem;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PayrollGlobalSettingsItemManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 9/21/15
 * Time: 6:49 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("paymentGlobalSettingsItemManager")
public class PayrollGlobalSettingsItemManagerImpl extends BaseManager<EdsPayrollGlobalSettingsItem> implements PayrollGlobalSettingsItemManager {

    public PayrollGlobalSettingsItemManagerImpl() {
        super(EdsPayrollGlobalSettingsItem.class);
    }

    @Override
    public void deleteItemsBySettingId(Integer globalPayrollSettingsId) {
        updateNative("update " + getCompanyId() + ".paymentdeductionsettingsitem set deleted = true where pds_id = " + globalPayrollSettingsId);
    }

    @Override
    public void deleteItemsByID(List<Integer> idsList) {
        updateNative("update  " + getCompanyId() + ".paymentdeductionsettingsitem set deleted=true where id in (" + ServerUtils.getAsCommoDelimited(idsList, "0", ",") + ")");
    }
}
