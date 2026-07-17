package com.edatasite.workforce.gwt.core.server.db.payroll;

import com.edatasite.workforce.core.domain.payrolluk.EdsCompanyPayrollSettings;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: 02.03.2009
 * Time: 15:06:48
 * To change this template use File | Settings | File Templates.
 */
public interface CompanyPayrollSettingsManager extends Manager<EdsCompanyPayrollSettings> {

    List<EdsCompanyPayrollSettings> getCompanySettings(Integer companyID);

    EdsCompanyPayrollSettings getCompanySettingValue(String key);

    List<EdsCompanyPayrollSettings> getCompanySettings(String... keys);

    default  Map<String, String> getCompanyPayrollSettingsMap(String... keys) {
        final List<EdsCompanyPayrollSettings> list = getCompanySettings(keys);

        return list.stream().collect(Collectors.toMap(EdsCompanyPayrollSettings::getKey, EdsCompanyPayrollSettings::getValue));
    }


}