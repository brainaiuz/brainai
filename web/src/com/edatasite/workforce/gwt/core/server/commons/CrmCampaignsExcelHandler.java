package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.CampaignItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.PropertManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmResourceBundleMessageSource;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: unni
 * Date: Aug 10, 2009
 * Time: 8:08:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class CrmCampaignsExcelHandler extends BaseExcelHandler {

    private static final Logger log = LoggerFactory.getLogger(CrmCampaignsExcelHandler.class);

    @Autowired
    private CRMService crmService;
    @Autowired
    private UserManager userManager;
    @Autowired
    @Qualifier("allReferenceWfmMessageSource")
    protected WfmResourceBundleMessageSource excelReferenceMessageSource;
    @Autowired
    private PropertManager propertManager;
    private String sheetName;

    @Override
    protected void setFileName() {
        filename = "Crm Campaigns";
    }

    protected HSSFWorkbook getWorkBook(Object object) {
        String shortDateFormat = "MM/dd/yyyy";
        EdsCompanySettings companySettings = userManager.getUser().getCompany().getCompanySettings();
        if (companySettings != null) {
            shortDateFormat = companySettings.getShortDateFormat();
        }
        ListingFilterParameter filterParametrs = (ListingFilterParameter) object;
        if (companySettings.getExcelLimit() != null && !"".equals(companySettings.getExcelLimit())) {
            filterParametrs.setLimit(Integer.parseInt(companySettings.getExcelLimit()));
        } else {
            filterParametrs.setLimit(LIMIT_EXCEL_ROW);
        }
        filterParametrs.setSearchType(1);
        EdsUser user = userManager.getUser();
        EdsCompany edsCompany = user.getCompany();
        ListResult<CampaignItem> campaignList = crmService.getCampaigns(filterParametrs);
        ExcelData[] cellDatas;
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        List<String> header = panelTools.getColumnCodeName();
        header.remove(CampaignItem.ACTION);
        HashMap<String, String> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(CampaignItem.OWNER, commonLocalizer.localize(PdfLocalizationName.owner));
        mapColumnHeader.put(CampaignItem.NAME, commonLocalizer.localize(PdfLocalizationName.name));
        mapColumnHeader.put(CampaignItem.TYPE, commonLocalizer.localize(PdfLocalizationName.type));
        mapColumnHeader.put(CampaignItem.STATUS, commonLocalizer.localize(PdfLocalizationName.status));
        mapColumnHeader.put(CampaignItem.START_DATE, commonLocalizer.localize(PdfLocalizationName.startDateField));
        mapColumnHeader.put(CampaignItem.END_DATE, commonLocalizer.localize(PdfLocalizationName.endDateField));
        try {
            WorkBook workBook = new WorkBook(true, 0, 1, 0, 1);
            workBook.setSheetName(filename);
            EdsProperty property = propertManager.findByCode(filterParametrs.getPropertyCode());
            sheetName = property != null ? property.getPlural() : commonLocalizer.localize(PdfLocalizationName.campaigns);
            List<ExcelData[]> list = new LinkedList<>();

            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size() + 1, edsCompany.getName(), workBook.getSheet(), 0));
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size() + 1, sheetName, workBook.getSheet(), 1));
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size() + 1, ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(ServerUtils.shortDateFormat(user.getUserDate(new Date()), user)) + " Xolatiga ko'ra" : excelReferenceMessageSource.localize("EPAsOf", " As Of") + "  " + ServerUtils.shortDateFormat(user.getUserDate(new Date()), user), workBook.getSheet(), 2));

            cellDatas = new ExcelData[header.size()];
            for (int i = 0; i < header.size(); i++) {
                cellDatas[i] = getExcelDataHeader(new ExcelData(mapColumnHeader.get(header.get(i)), ExcelData.STRING, header.get(i).equals(CampaignItem.NAME) ? 50 : 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            }
            list.add(cellDatas);
            List<String> cell = new ArrayList<>();
            for (CampaignItem item : campaignList.getList()) {
                String temp = "";
                for (int i = 0; i < header.size(); i++) {
                    temp = header.get(i);
                    if (CampaignItem.OWNER.equals(temp)) {
                        cell.add(header.indexOf(CampaignItem.OWNER), item.getAssignee());
                    } else if (CampaignItem.NAME.equals(temp)) {
                        cell.add(header.indexOf(CampaignItem.NAME), item.getName());
                    } else if (CampaignItem.TYPE.equals(temp)) {
                        cell.add(header.indexOf(CampaignItem.TYPE), item.getType());
                    } else if (CampaignItem.STATUS.equals(temp)) {
                        cell.add(header.indexOf(CampaignItem.STATUS), item.getStatus());
                    } else if (CampaignItem.START_DATE.equals(temp)) {
                        if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                            cell.add(header.indexOf(CampaignItem.START_DATE), ServerUtils.convertToUzbDateFormat(shortDateFormat2(userManager.getUser().getUserDate(item.getStartDate()))));
                        } else {
                            cell.add(header.indexOf(CampaignItem.START_DATE), shortDateFormat2(userManager.getUser().getUserDate(item.getStartDate())));
                        }
                    } else if (CampaignItem.END_DATE.equals(temp)) {
                        if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                            cell.add(header.indexOf(CampaignItem.END_DATE), ServerUtils.convertToUzbDateFormat(shortDateFormat2(userManager.getUser().getUserDate(item.getEndDate()))));

                        }
                        cell.add(header.indexOf(CampaignItem.END_DATE), shortDateFormat2(userManager.getUser().getUserDate(item.getEndDate())));
                    }
                }
                cellDatas = new ExcelData[header.size()];
                for (int k = 0; k < header.size(); k++) {
                    cellDatas[k] = getExcelRows(new ExcelData(cell.get(k), ExcelData.STRING, header.get(k).equals(CampaignItem.NAME) ? 50 : 20, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                list.add(cellDatas);
            }
            workBook.setList(list);
            return workBook.getWorkBook(filename, 0, 0, 0, header.size());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Cannot generate Crm Campaigns list excel report, exception: " + e);
        }
        return null;
    }

    protected String shortDateFormat2(Date date) {
        return ServerUtils.shortDateFormat(date, userManager.getUser());
    }
}
