package com.edatasite.workforce.gwt.core.server.db.impl.reporting;

import com.edatasite.workforce.core.domain.reporting.EdsReportTemplateCategory;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.wfp.ReportTemplateCategoryManager;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 20.10.11
 * Time: 15:44
 * To change this template use File | Settings | File Templates.
 */

@Repository("reportTemplateCategoryManager")
public class ReportTemplateCategoryManagerImpl extends BaseManager<EdsReportTemplateCategory> implements ReportTemplateCategoryManager {

    public ReportTemplateCategoryManagerImpl() {
        super(EdsReportTemplateCategory.class);
    }

    @Override
    public ArrayList<EdsReportTemplateCategory> getReportTemplateCategoryList() {
        return (ArrayList<EdsReportTemplateCategory>) find("select cat from EdsReportTemplateCategory cat ");
    }

    @Override
    public Map<String, EdsReportTemplateCategory> getReportTemplateCategoryListMap() {
        ArrayList<EdsReportTemplateCategory> reportTemplateCategoryLis = (ArrayList<EdsReportTemplateCategory>) find("select cat from EdsReportTemplateCategory cat ");
        return reportTemplateCategoryLis.stream().collect(Collectors.toMap(EdsReportTemplateCategory::getCode, reportTemplate -> reportTemplate, (p1, p2) -> p1));
    }

    @Override
    public EdsReportTemplateCategory getReportTemplateCategory(String name) {
        return (EdsReportTemplateCategory) findSingle("select cat from EdsReportTemplateCategory cat where cat.name =?", name);
    }

    @Override
    public EdsReportTemplateCategory getReportTemplateCategoryByCode(String code) {
        return (EdsReportTemplateCategory) findSingle("select cat from EdsReportTemplateCategory cat where cat.code =?", code);
    }
}
