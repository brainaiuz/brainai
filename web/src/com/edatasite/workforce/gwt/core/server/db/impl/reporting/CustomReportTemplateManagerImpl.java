package com.edatasite.workforce.gwt.core.server.db.impl.reporting;

import com.edatasite.workforce.core.domain.reporting.EdsCustomReportTemplate;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.wfp.CustomReportTemplateManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Virus on 3/25/2014.
 */
@Repository("customReportTemplate")
public class CustomReportTemplateManagerImpl extends BaseManager<EdsCustomReportTemplate> implements CustomReportTemplateManager {
    public CustomReportTemplateManagerImpl() {
        super(EdsCustomReportTemplate.class);
    }

    @Override
    public List<EdsCustomReportTemplate> getByCode(String code) {
        return (List<EdsCustomReportTemplate>) find("SELECT t FROM EdsCustomReportTemplate t WHERE t.reportTemplate=?", code);
    }
}
