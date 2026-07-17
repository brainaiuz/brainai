package com.edatasite.workforce.gwt.core.server.db.impl.payroll;

import com.edatasite.workforce.core.domain.payrolluk.EndOfServiceSettings;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.EndOfServiceManager;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 12.05.14
 * Time: 12:11
 * To change this template use File | Settings | File Templates.
 */
@Repository("endOfServiceManager")
public class EndOfServiceManagerImpl extends BaseManager<EndOfServiceSettings> implements EndOfServiceManager {

    public EndOfServiceManagerImpl() {
        super(EndOfServiceSettings.class);
    }


    @Override
    public EndOfServiceSettings getEndOfServiceSettings(String countryCode) {
        return (EndOfServiceSettings) findSingle("select eos from EndOfServiceSettings eos where eos.countryCode=?", countryCode);
    }

}
