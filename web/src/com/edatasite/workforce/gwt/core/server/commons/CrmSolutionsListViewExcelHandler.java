package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.PropertManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.rpc.SolutionItem;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmResourceBundleMessageSource;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: unni
 * Date: Aug 10, 2009
 * Time: 8:08:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class CrmSolutionsListViewExcelHandler extends BaseExcelHandler {

    private static final Logger log = LoggerFactory.getLogger(CrmSolutionsListViewExcelHandler.class);
    @Autowired
    private PropertManager propertManager;
    private String sheetName;

    @Autowired
    private CRMService crmService;
    @Autowired
    @Qualifier("allReferenceWfmMessageSource")
    protected WfmResourceBundleMessageSource excelReferenceMessageSource;
    @Autowired
    private UserManager userManager;

    @Override
    protected void setFileName() {
        filename = "Crm Solutions";
    }

    protected HSSFWorkbook getWorkBook(Object object) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) object;
        EdsCompany edsCompany = userManager.getUser().getCompany();
        EdsCompanySettings companySettings = edsCompany.getCompanySettings();

        if (companySettings.getExcelLimit() != null && !"".equals(companySettings.getExcelLimit())) {
            filterParametrs.setLimit(Integer.parseInt(companySettings.getExcelLimit()));
        } else {
            filterParametrs.setLimit(LIMIT_EXCEL_ROW);
        }
        ListResult<SolutionItem> solutionList = crmService.getSolutionList(filterParametrs);
        List<SolutionItem> solutionListItems = solutionList.getList();
        EdsUser user = userManager.getUser();
        ExcelData[] cellDatas;
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        List<String> header = panelTools.getColumnCodeName();
        header.remove("action");

        Map<String, String> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(SolutionItem.ASSIGNEE, crmLocalizer.localize(PdfLocalizationName.leadAssignee));
        mapColumnHeader.put(SolutionItem.TITLE, crmLocalizer.localize(PdfLocalizationName.solutionTitle));
        mapColumnHeader.put(SolutionItem.STATUS, commonLocalizer.localize(PdfLocalizationName.status));
        mapColumnHeader.put(SolutionItem.QUESTION, crmLocalizer.localize(PdfLocalizationName.question));
        mapColumnHeader.put(SolutionItem.ANSWER, crmLocalizer.localize(PdfLocalizationName.answer));
        try {
            WorkBook workBook = new WorkBook(true, 0, 1, 0, 1);
            workBook.setSheetName(filename);
            EdsProperty property = propertManager.findByCode(filterParametrs.getPropertyCode());
            sheetName = property != null ? property.getPlural() : commonLocalizer.localize(PdfLocalizationName.solutions);
            List<ExcelData[]> list = new LinkedList<>();
            cellDatas = new ExcelData[header.size()];

            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size() + 1, edsCompany.getName(), workBook.getSheet(), 0));
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size() + 1, sheetName, workBook.getSheet(), 1));
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size() + 1, excelReferenceMessageSource.localize("EPAsOf", " As Of") + "  " + ServerUtils.shortDateFormat(user.getUserDate(new Date()), user), workBook.getSheet(), 2));

            for (int i = 0; i < header.size(); i++) {
                cellDatas[i] = getExcelDataHeader(new ExcelData(mapColumnHeader.get(header.get(i)), ExcelData.STRING, header.get(i).equals(SolutionItem.TITLE) ? 50 : 20, true, header.get(i).equals(SolutionItem.TITLE) || header.get(i).equals(SolutionItem.STATUS), ExcelData.NO_BORDER, ExcelData.HEADER));
            }

            list.add(cellDatas);

            for (SolutionItem solutions : solutionListItems) {
                String temp = "";
                cellDatas = new ExcelData[header.size()];
                for (int j = 0; j < header.size(); j++) {
                    temp = "";
                    if (SolutionItem.ASSIGNEE.equals(header.get(j))) {
                        temp = solutions.getAssignee() == null ? "" : solutions.getAssignee();
                    } else if (SolutionItem.TITLE.equals(header.get(j))) {
                        temp = solutions.getTitle() == null ? "" : solutions.getTitle();
                    } else if (SolutionItem.STATUS.equals(header.get(j))) {
                        temp = solutions.getStatus() == null ? "" : solutions.getStatus();
                    } else if (SolutionItem.QUESTION.equals(header.get(j))) {
                        temp = solutions.getQuestion() == null ? "" : solutions.getQuestion();
                    } else if (SolutionItem.ANSWER.equals(header.get(j))) {
                        temp = solutions.getAnswer() == null ? "" : solutions.getAnswer();
                    }
                    if (temp.length() > 255) {
                        temp = temp.substring(0, 250) + " ...";
                    }
                    cellDatas[j] = getExcelRows(new ExcelData(temp, ExcelData.STRING, header.get(j).equals(SolutionItem.ASSIGNEE) || header.get(j).equals(SolutionItem.TITLE) ? 50 : 20, true, !header.get(j).equals(SolutionItem.STATUS), ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                list.add(cellDatas);

            }
            workBook.setList(list);
            return workBook.getWorkBook(filename, 0, 0, 0, header.size());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Cannot generate solution list excel report, exception: " + e);
        }
        return null;
    }
}