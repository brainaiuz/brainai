package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsCompanyTrace;
import com.edatasite.workforce.gwt.core.server.db.CompanyTraceManager;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: Mar 27, 2009
 * Time: 4:25:19 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("companyTraceManager")
public class CompanyTraceManagerImpl extends BaseManager<EdsCompanyTrace> implements CompanyTraceManager {
    public CompanyTraceManagerImpl() {
        super(EdsCompanyTrace.class);
    }
}
