package com.edatasite.workforce.gwt.core.server.db.wfp;

import com.edatasite.workforce.core.domain.reporting.EdsCustomReportTemplate;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

/**
 * Created by Virus on 3/25/2014.
 */
public interface CustomReportTemplateManager extends Manager<EdsCustomReportTemplate> {
    List<EdsCustomReportTemplate> getByCode(String code);
}
