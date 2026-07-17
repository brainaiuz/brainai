package com.edatasite.workforce.gwt.core.server.db.payroll;

import com.edatasite.workforce.core.domain.payrolluk.EdsEosCalculation;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Admin
 * Date: 14.05.14
 * Time: 17:52
 * To change this template use File | Settings | File Templates.
 */
public interface EndOfServiceGratuityManager extends Manager<EdsEosCalculation> {

    List<EdsEosCalculation> getEosCalculationList(ListingFilterParameter filterParameter);

    Integer getEosCalculationCount();

    Integer getLastIntNumber();
}
