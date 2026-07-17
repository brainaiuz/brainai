package com.finnetlimited.reportservice.core.server.db.schema;

import com.edatasite.workforce.gwt.core.server.db.Manager;
import com.finnetlimited.reportservice.core.server.domain.schema.EdsCustomizeReport;

/**
 * Created with IntelliJ IDEA.
 * User: Virus
 * Date: 3/22/13
 * Time: 4:50 PM
 * To change this template use File | Settings | File Templates.
 */
public interface CustomizeReportManager extends Manager<EdsCustomizeReport> {
    EdsCustomizeReport getByCode(String type, String reportCode);

    void create(EdsCustomizeReport customizeReport);
}
