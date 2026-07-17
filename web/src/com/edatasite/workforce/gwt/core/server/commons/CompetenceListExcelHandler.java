package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.assessment.client.rpc.AssessmentService;
import com.edatasite.workforce.gwt.assessment.client.rpc.SkillItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * Created by Farrukh on 09-Jun-17.
 */
public class CompetenceListExcelHandler extends BaseExcelHandler implements Constants {

    private AssessmentService assessmentService;

    private static final Logger log = LoggerFactory.getLogger(CompetenceListExcelHandler.class);

    protected HSSFWorkbook getWorkBook(Object object) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) object;
        filterParametrs.setLimit(LIMIT_EXCEL_ROW);
        EdsUser user = userManager.getUser();
        ListResult<SkillItem> competenciesList = assessmentService.getCompetencies(filterParametrs);

        ExcelData[] cellDatas;
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        List<String> header = panelTools.getColumnCodeName();
        header.remove(SkillItem.ACTION);
        Map<String, String> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(SkillItem.COMPETENCY_GROUP_NAME, commonLocalizer.localize(PdfLocalizationName.skillGroup));
        mapColumnHeader.put(SkillItem.COMPETENCY_NAME, commonLocalizer.localize("competencyName"));
        mapColumnHeader.put(SkillItem.COMPETENCY_DESCRIPTION, commonLocalizer.localize(PdfLocalizationName.description));

        try {
            List<ExcelData[]> list = new LinkedList<>();
            cellDatas = new ExcelData[header.size()];

            for (int i = 0; i < header.size(); i++) {
                cellDatas[i] = new ExcelData(mapColumnHeader.get(header.get(i)), ExcelData.STRING,
                        header.get(i).equals(SkillItem.COMPETENCY_DESCRIPTION) ? 60 : 40, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
            }

            list.add(cellDatas);

            for (SkillItem competence : competenciesList.getList()) {
                List<String> cell = new ArrayList<>();
                for (int ii = 0; ii < header.size(); ii++) {
                    if (SkillItem.COMPETENCY_GROUP_NAME.equals(header.get(ii))) {
                        cell.add(header.indexOf(SkillItem.COMPETENCY_GROUP_NAME), (competence.getGroupName() != null ? competence.getGroupName() : ""));
                    }
                    if (SkillItem.COMPETENCY_NAME.equals(header.get(ii))) {
                        cell.add(header.indexOf(SkillItem.COMPETENCY_NAME), (competence.getName() != null ? competence.getName() : ""));
                    }
                    if (SkillItem.COMPETENCY_DESCRIPTION.equals(header.get(ii))) {
                        cell.add(header.indexOf(SkillItem.COMPETENCY_DESCRIPTION), (competence.getDescription() != null ? competence.getDescription() : ""));
                    }
                }
                cellDatas = new ExcelData[header.size()];
                for (int k = 0; k < header.size(); k++) {
                    cellDatas[k] = new ExcelData(cell.get(k), ExcelData.STRING,
                            header.get(k).equals(SkillItem.COMPETENCY_DESCRIPTION) ? 60 : 40, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                }
                list.add(cellDatas);
            }

            return new WorkBook(list).getWorkBook(filename, 0, 0, 0, 6);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Cannot generate competece list excel report, exception: " + e);
        }
        return null;
    }

    @Override
    protected void setFileName() {
        filename = "Competece List_" + dateFormat(new Date());
    }

    public void setAssessmentService(AssessmentService assessmentService) {
        this.assessmentService = assessmentService;
    }
}