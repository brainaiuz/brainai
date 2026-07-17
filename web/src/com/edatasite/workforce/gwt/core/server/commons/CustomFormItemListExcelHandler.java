package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.customform.EdsCustomForm;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.FormItems;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.CustomFormManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
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
import java.util.Map;

/**
 * Created by Azam on 09/27/2019
 * Created date: 04:57 am
 */
public class CustomFormItemListExcelHandler extends BaseExcelHandler implements Constants {

    private static final Logger log = LoggerFactory.getLogger(CustomFormItemListExcelHandler.class);

    @Autowired
    private CustomFormManager customFormManager;
    @Autowired
    protected CommonService commonService;

    @Autowired
    @Qualifier("allReferenceWfmMessageSource")
    protected WfmResourceBundleMessageSource excelReferenceMessageSource;

    @Override
    protected void setFileName() {
//        filename = "Custom Form Item";
    }

    @Override
    protected void setFileName(Object dataClass) {
        ListingFilterParameter filterParameters = (ListingFilterParameter) dataClass;
        EdsCustomForm customForm = customFormManager.get(filterParameters.getFacetFilter().getTypeId());
        filename = customForm != null ? customForm.getName() : "List";
    }

    protected HSSFWorkbook getWorkBook(Object object) {
        ListingFilterParameter filterParameters = (ListingFilterParameter) object;
        filterParameters.setAllByFilter(true);
        filterParameters.setForExportOnly(true);

        EdsUser user = userManager.getUser();
        EdsCompany company = user.getCompany();
        EdsCompanySettings companySettings = company.getCompanySettings();

        if (companySettings.getExcelLimit() != null && !"".equals(companySettings.getExcelLimit())) {
            filterParameters.setLimit(Integer.parseInt(companySettings.getExcelLimit()));
        } else {
            filterParameters.setLimit(LIMIT_EXCEL_ROW);
        }
        filterParameters.setParentID(filterParameters.getFacetFilter().getTypeId());

        EdsCustomForm customForm = customFormManager.get(filterParameters.getFacetFilter().getTypeId());
        ListResult<FormItems> customFormItems = commonService.getCustomFormItems(filterParameters);
        List<FormItems> itemsList = customFormItems.getList();

        ListPanelToolRpc panelTools = filterParameters.getListPanelTool();
        ExcelData[] cellDatas;
        Map<String, ExcelData> mapColumnHeader = new HashMap<>();

        try {
            WorkBook workBook = new WorkBook(true, 0, 1, 0, 1);
            workBook.setSheetName(filename);

            List<ExcelData[]> list = new LinkedList<>();
            mapColumnHeader.put(FormItems.CREATER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.createdBy), ExcelData.STRING, 25, true, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(FormItems.CREATED_DATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.createdDate), ExcelData.STRING, 25, true, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(FormItems.UPDATER, new ExcelData(commonLocalizer.localize("modifiedBy"), ExcelData.STRING, 25, true, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(FormItems.UPDATED_DATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.modifiedDate), ExcelData.STRING, 30, true, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(FormItems.STATUS, new ExcelData(commonLocalizer.localize(PdfLocalizationName.status), ExcelData.STRING, 30, true, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(FormItems.APPROVER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.approver), ExcelData.STRING, 30, true, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            CustomFieldsUtils.setCustomFieldsExcelHeaderMap(panelTools.getListViewCustomFields(), mapColumnHeader);

            // Set excell header
            List<ExcelData> excellDatasList = new ArrayList<>();
            for (String columnCode : panelTools.getColumnCodeName()) {
                if (mapColumnHeader.containsKey(columnCode)) {
                    excellDatasList.add(mapColumnHeader.get(columnCode));
                }
            }

            String excelName = customForm != null ? customForm.getName() : "List";
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), company.getName(), workBook.getSheet(), 0));
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), excelName, workBook.getSheet(), 1));
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), excelReferenceMessageSource.localize("EPAsOf", " As Of") + "  " + ServerUtils.shortDateFormat(user.getUserDate(new Date()), user), workBook.getSheet(), 2));

            cellDatas = new ExcelData[excellDatasList.size()];
            excellDatasList.toArray(cellDatas);
            list.add(cellDatas);

            for (FormItems item : itemsList) {
                Map<String, ExcelData> mapColumn = new HashMap<>();
                if (panelTools.getColumnCodeName().contains(FormItems.CREATER)) {
                    if (item.isAnonymous()) {
                        mapColumn.put(FormItems.CREATER, new ExcelData(getResultOrNA(commonLocalizer.localize(PdfLocalizationName.anonymous)), ExcelData.STRING, 50, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    } else {
                        mapColumn.put(FormItems.CREATER, new ExcelData(getResultOrNA(item.getCreator()), ExcelData.STRING, 50, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    }
                }
                if (panelTools.getColumnCodeName().contains(FormItems.CREATED_DATE)) {
                    mapColumn.put(FormItems.CREATED_DATE, new ExcelData(item.getCreatedDate() != null ? ServerUtils.shortDateFormat(item.getCreatedDate(), company) : "N/A", ExcelData.STRING, 25, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(FormItems.UPDATER)) {
                    if (item.isAnonymous()) {
                        mapColumn.put(FormItems.UPDATER, new ExcelData(getResultOrNA(commonLocalizer.localize(PdfLocalizationName.anonymous)), ExcelData.STRING, 50, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    } else {
                        mapColumn.put(FormItems.UPDATER, new ExcelData(getResultOrNA(item.getUpdater()), ExcelData.STRING, 50, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    }
                }
                if (panelTools.getColumnCodeName().contains(FormItems.UPDATED_DATE)) {
                    mapColumn.put(FormItems.UPDATED_DATE, new ExcelData(item.getModifiedData() != null ? ServerUtils.longDateFormat(item.getModifiedData(), company) : "N/A", ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(FormItems.STATUS)) {
                    mapColumn.put(FormItems.STATUS, new ExcelData(getResultOrNA(item.getStatus()), ExcelData.STRING, 50, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(FormItems.APPROVER)) {
                    mapColumn.put(FormItems.APPROVER, new ExcelData(getResultOrNA(item.getCurrentApproverName()), ExcelData.STRING, 50, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                CustomFieldsUtils.setCustomFieldsExcelTableRows(panelTools.getListViewCustomFields(), mapColumn, panelTools.getColumnCodeName(), item, company);
                excellDatasList = new ArrayList<>();
                for (String columnCode : panelTools.getColumnCodeName()) {
                    if (mapColumn.containsKey(columnCode)) {
                        excellDatasList.add(mapColumn.get(columnCode));
                    }
                }

                cellDatas = new ExcelData[excellDatasList.size()];
                excellDatasList.toArray(cellDatas);
                list.add(cellDatas);
            }
            workBook.setList(list);
            return workBook.getWorkBook(filename, 0, 0, 0, 6);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Cannot generate Form Item List excel report, exception: " + e);
        }
        return null;
    }

    public String getResultOrNA(String value) {
        if (ServerUtils.isNullOrEmpty(value)) {
            return "N/A";
        }
        return value
                .replace("\u001F", "")
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }

    protected String dateFormat(Date date, boolean... isServerTime) {
        return ServerUtils.longDateFormat(date, userManager.getUser(), isServerTime == null || isServerTime.length <= 0 || !isServerTime[0]);
    }

}
