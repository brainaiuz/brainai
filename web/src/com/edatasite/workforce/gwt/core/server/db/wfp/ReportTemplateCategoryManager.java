package com.edatasite.workforce.gwt.core.server.db.wfp;

import com.edatasite.workforce.core.domain.reporting.EdsReportTemplateCategory;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 20.10.11
 * Time: 15:43
 * To change this template use File | Settings | File Templates.
 */

public interface ReportTemplateCategoryManager extends Manager<EdsReportTemplateCategory> {

    ArrayList<EdsReportTemplateCategory> getReportTemplateCategoryList();

    Map<String, EdsReportTemplateCategory> getReportTemplateCategoryListMap();

    EdsReportTemplateCategory getReportTemplateCategory(String name);

    EdsReportTemplateCategory getReportTemplateCategoryByCode(String code);
}
