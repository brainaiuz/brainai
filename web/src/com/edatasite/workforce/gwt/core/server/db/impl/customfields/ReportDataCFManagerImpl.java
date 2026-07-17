package com.edatasite.workforce.gwt.core.server.db.impl.customfields;

import com.edatasite.workforce.core.domain.customfields.EdsReportDataCustomFields;
import com.edatasite.workforce.gwt.core.server.db.customfields.ReportDataCFManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

/**
 * User: Faxriddin Taslimov on 14.08.19.
 */
@Repository("reportDataCFManager")
public class ReportDataCFManagerImpl extends BaseManager<EdsReportDataCustomFields> implements ReportDataCFManager {
    public ReportDataCFManagerImpl() {
        super(EdsReportDataCustomFields.class);
    }
}
