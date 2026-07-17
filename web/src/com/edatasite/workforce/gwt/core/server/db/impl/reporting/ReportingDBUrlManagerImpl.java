package com.edatasite.workforce.gwt.core.server.db.impl.reporting;

import com.edatasite.workforce.core.domain.reporting.EdsReportingDBUrl;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.wfp.ReportingDBUrlManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Virus
 * Date: 4/30/12
 * Time: 3:42 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("reportingDBUrlManager")
public class ReportingDBUrlManagerImpl extends BaseManager<EdsReportingDBUrl> implements ReportingDBUrlManager {
    public ReportingDBUrlManagerImpl() {
        super(EdsReportingDBUrl.class);
    }

    @Override
    public List<EdsReportingDBUrl> list(ListingFilterParameter filterParameter) {
        return (List<EdsReportingDBUrl>) findInterval("select t from EdsReportingDBUrl t ", filterParameter.getStart(), filterParameter.getLimit());
    }

    @Override
    public Integer listCount() {
        return Integer.parseInt(findSingle("select count(t.objectID) from EdsReportingDBUrl t ").toString());
    }

    @Override
    public EdsReportingDBUrl getByCompanyID(String companyId) {
        StringBuilder sql = new StringBuilder("select t.* from " +
                        getPublic() + ".reportingDBUrl t left join " +
                        getPublic() + ".companyReportingDBUrl r on t.id=reportingDBUrlID where coalesce(r.companyId," + companyId + ")=" + companyId + " order by companyid");

        return (EdsReportingDBUrl) findNativeSingle(sql.toString(), EdsReportingDBUrl.class);

    }
}
