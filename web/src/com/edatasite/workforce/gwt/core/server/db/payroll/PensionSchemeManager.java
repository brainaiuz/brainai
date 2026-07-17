package com.edatasite.workforce.gwt.core.server.db.payroll;

import com.edatasite.workforce.core.domain.payrolluk.EdsPensionScheme;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Oct 10, 2009
 * Time: 7:06:59 PM
 * To change this template use File | Settings | File Templates.
 */
public interface PensionSchemeManager extends Manager<EdsPensionScheme> {
    List<EdsPensionScheme> getCompanyPensionSchemes();

    List<EdsPensionScheme> getPensionSchemes(ListingFilterParameter fp);

    EdsPensionScheme getPensionSchema(String countryCode);

    void deletePensionScheme(Integer id);

    boolean isDeductedFromGrossPay(String pensionSchemes);

    boolean isDeductedFromNetPay(String pensionSchemes);
}
