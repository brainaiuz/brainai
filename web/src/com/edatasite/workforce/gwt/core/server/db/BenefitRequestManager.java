package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsBenefitRequest;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.gwt.availability.client.rpc.BenefitRequestItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.Date;
import java.util.List;

/**
 * Created by Djuraev on 8/7/15.
 */
public interface BenefitRequestManager extends Manager<EdsBenefitRequest> {

    ListResult<BenefitRequestItem> getBenefitRequestList(ListingFilterParameter fp);

    List<EdsBenefitRequest> getBenefitRequestList(EdsEmployee employee);

    double getEmployeeUsedBenefitAllowance(Date startYearDate, Date endYearDate, Integer employeeID, Integer benefitID);

    List<EdsBenefitRequest> getBenefitRequestForPayment(ListingFilterParameter lfp);

    List<SelectItem> getBenefitRequestTypeList();
}

