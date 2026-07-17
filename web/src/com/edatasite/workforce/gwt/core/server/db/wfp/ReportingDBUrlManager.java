package com.edatasite.workforce.gwt.core.server.db.wfp;

import com.edatasite.workforce.core.domain.reporting.EdsReportingDBUrl;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Virus
 * Date: 4/30/12
 * Time: 3:41 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ReportingDBUrlManager extends Manager<EdsReportingDBUrl> {
    List<EdsReportingDBUrl> list(ListingFilterParameter filterParameter);

    Integer listCount();

    EdsReportingDBUrl getByCompanyID(String companyId);
}
