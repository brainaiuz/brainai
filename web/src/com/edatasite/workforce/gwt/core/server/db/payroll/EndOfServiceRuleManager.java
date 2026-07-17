package com.edatasite.workforce.gwt.core.server.db.payroll;

import com.edatasite.workforce.core.domain.payrolluk.EdsRuleEosSettings;
import com.edatasite.workforce.gwt.core.server.db.Manager;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 12.05.14
 * Time: 14:59
 * To change this template use File | Settings | File Templates.
 */
public interface EndOfServiceRuleManager extends Manager<EdsRuleEosSettings> {

    void deleteRuleSettings(Integer objectID);

}
