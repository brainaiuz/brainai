package com.edatasite.workforce.gwt.core.server.db.payroll;

import com.edatasite.workforce.core.domain.payrolluk.EdsPayrollGlobalSettingsItem;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 9/21/15
 * Time: 6:48 PM
 * To change this template use File | Settings | File Templates.
 */
public interface PayrollGlobalSettingsItemManager extends Manager<EdsPayrollGlobalSettingsItem> {

    void deleteItemsBySettingId(Integer globalPayrollSettingsId);

    void deleteItemsByID(List<Integer> idsList);
}
