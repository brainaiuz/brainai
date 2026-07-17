package com.edatasite.workforce.gwt.core.server.db.impl.payroll;

import com.edatasite.workforce.core.domain.payrolluk.EdsRuleEosSettings;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.EndOfServiceRuleManager;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 12.05.14
 * Time: 15:01
 * To change this template use File | Settings | File Templates.
 */
@Repository("endOfServiceRuleManager")
public class EndOfServiceRuleManagerImpl extends BaseManager<EdsRuleEosSettings> implements EndOfServiceRuleManager {

    public EndOfServiceRuleManagerImpl() {
        super(EdsRuleEosSettings.class);
    }

    @Override
    public void deleteRuleSettings(Integer objectID) {
        update("delete from EdsRuleEosSettings  rs where rs.settings.objectID=?", objectID);
    }
}
