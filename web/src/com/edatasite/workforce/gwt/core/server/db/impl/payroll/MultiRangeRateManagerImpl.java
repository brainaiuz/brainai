package com.edatasite.workforce.gwt.core.server.db.impl.payroll;

import com.edatasite.workforce.core.domain.payrolluk.EdsMultiRangeRate;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.MultiRangeRateManager;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 2011-07-19
 * Time: 8:05 AM
 * To change this template use File | Settings | File Templates.
 */
@Repository("multiRangeRateManager")
public class MultiRangeRateManagerImpl extends BaseManager<EdsMultiRangeRate> implements MultiRangeRateManager {

    public MultiRangeRateManagerImpl() {
        super(EdsMultiRangeRate.class);
    }
}
