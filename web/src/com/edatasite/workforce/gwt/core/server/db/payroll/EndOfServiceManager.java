package com.edatasite.workforce.gwt.core.server.db.payroll;

import com.edatasite.workforce.core.domain.payrolluk.EndOfServiceSettings;
import com.edatasite.workforce.gwt.core.server.db.Manager;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 12.05.14
 * Time: 12:09
 * To change this template use File | Settings | File Templates.
 */
public interface EndOfServiceManager extends Manager<EndOfServiceSettings> {

    EndOfServiceSettings getEndOfServiceSettings(String countryCode);

}
