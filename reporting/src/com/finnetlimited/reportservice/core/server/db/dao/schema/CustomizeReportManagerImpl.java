package com.finnetlimited.reportservice.core.server.db.dao.schema;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.finnetlimited.reportservice.core.server.db.schema.CustomizeReportManager;
import com.finnetlimited.reportservice.core.server.domain.schema.EdsCustomizeReport;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: Virus
 * Date: 3/22/13
 * Time: 4:52 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("customizeReportManager")
public class CustomizeReportManagerImpl extends BaseManager<EdsCustomizeReport> implements CustomizeReportManager {
    public CustomizeReportManagerImpl() {
        super(EdsCustomizeReport.class);
    }

    @Override
    public EdsCustomizeReport getByCode(String type, String reportCode) {
        EdsUser edsUser = getUser();
        if (edsUser == null || edsUser.getObjectID() == null) {
            return null;
        }
        return (EdsCustomizeReport) findNativeSingle("SELECT  t.* from " + getCompanyId() + ".reportingCustomize t WHERE t.dtype='" + type + "' and " + (!ServerUtils.isNullOrEmpty(reportCode) ? (" t.reportcode='" + reportCode + "' and ") : "") + "t.userid=" + edsUser.getObjectID(), EdsCustomizeReport.class);
    }

    @Override
    public void create(EdsCustomizeReport customizeReport) {
        EdsUser edsUser = getUser();
        if (edsUser != null) {
            customizeReport.setUserid(edsUser);
            super.create(customizeReport);
        }
    }
}
