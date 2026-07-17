package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.CompLocationRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.location.client.rpc.LocationService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmResourceBundleMessageSource;

import java.util.*;

/**
 * User: Ilhombek
 * Date: Mar 30, 2010
 * Time: 16:10:35 PM
 */
public class LocationListExcelHandler extends BaseExcelHandler {

    private static final Logger log = LoggerFactory.getLogger(LocationListExcelHandler.class);

    @Autowired
    private LocationService locationService;
    @Autowired
    @Qualifier("allReferenceWfmMessageSource")
    protected WfmResourceBundleMessageSource excelReferenceMessageSource;
    @Autowired
    private UserManager userManager;

    @Override
    protected void setFileName() {
        filename = "Locations List";
    }

    protected HSSFWorkbook getWorkBook(Object object) {
        ListingFilterParameter fp = (ListingFilterParameter) object;
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        fp.setLimit(LIMIT_EXCEL_ROW);
        ListResult<CompLocationRpc> locationList = locationService.getLocations(fp);
        ListPanelToolRpc panelTools = fp.getListPanelTool();

        EdsUser user = userManager.getUser();
        EdsCompany edsCompany = user.getCompany();
        ExcelData[] cellDatas;
        Map<String, ExcelData> mapColumnData = new HashMap<>();
        try {
            WorkBook workBook = new WorkBook(true, 0, 1, 0, 1);
            workBook.setSheetName(filename);

            List<ExcelData[]> list = new LinkedList<>();
            mapColumnData.put(CompLocationRpc.CODE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.number), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(CompLocationRpc.NAME, new ExcelData(commonLocalizer.localize(PdfLocalizationName.name), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(CompLocationRpc.COUNTRY_NAME, new ExcelData(commonLocalizer.localize(PdfLocalizationName.country), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(CompLocationRpc.STATE_NAME, new ExcelData(commonLocalizer.localize(PdfLocalizationName.state), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(CompLocationRpc.CITY_DISTRICT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.cityOrDistrict), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(CompLocationRpc.CITY_NAME, new ExcelData(commonLocalizer.localize(PdfLocalizationName.city), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(CompLocationRpc.FAX, new ExcelData(commonLocalizer.localize(PdfLocalizationName.fax), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(CompLocationRpc.ZIP_CODE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.postCode), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(CompLocationRpc.PARENT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.parent), ExcelData.STRING, 75, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            CustomFieldsUtils.setCustomFieldsExcelHeaderMapWithoutBorder(panelTools.getListViewCustomFields(), mapColumnData);

            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), edsCompany.getName(), workBook.getSheet(), 0));
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), commonLocalizer.localize(PdfLocalizationName.location), workBook.getSheet(), 1));
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertDateFormatFromEngToUzb(ServerUtils.shortDateFormat(user.getUserDate(new Date()), user)) + " Xolatiga ko'ra" : commonLocalizer.localize(PdfLocalizationName.asOF) +" " +ServerUtils.shortDateFormat(user.getUserDate(new Date()), user), workBook.getSheet(), 2));


            List<ExcelData> excelDataList = new ArrayList<>();
            for (int i = 0; i < panelTools.getColumnCodeName().size(); i++) {
                if (mapColumnData.containsKey(panelTools.getColumnCodeName().get(i))) {
                    excelDataList.add(mapColumnData.get(panelTools.getColumnCodeName().get(i)));
                }
            }
            cellDatas = new ExcelData[excelDataList.size()];
            excelDataList.toArray(cellDatas);
            list.add(cellDatas);
            for (CompLocationRpc location : locationList.getList()) {
                Map<String, ExcelData> mapColumn = new HashMap<>();
                if (panelTools.getColumnCodeName().contains(CompLocationRpc.CODE)) {
                    mapColumn.put(CompLocationRpc.CODE, new ExcelData(location.getNumberData() != null ? location.getNumberData().getNumberString() : "", ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CompLocationRpc.NAME)) {
                    mapColumn.put(CompLocationRpc.NAME, new ExcelData(location.getName() != null ? location.getName() : "", ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CompLocationRpc.COUNTRY_NAME)) {
                    mapColumn.put(CompLocationRpc.COUNTRY_NAME, new ExcelData(location.getCountryName() != null ? location.getCountryName() : "", ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CompLocationRpc.STATE_NAME)) {
                    mapColumn.put(CompLocationRpc.STATE_NAME, new ExcelData(location.getStateName() != null ? location.getStateName() : "", ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CompLocationRpc.CITY_DISTRICT)) {
                    mapColumn.put(CompLocationRpc.CITY_DISTRICT, new ExcelData(location.getCityOrDestrictName(), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CompLocationRpc.CITY_NAME)) {
                    mapColumn.put(CompLocationRpc.CITY_NAME, new ExcelData(location.getCityName() != null ? location.getCityName() : "", ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CompLocationRpc.FAX)) {
                    mapColumn.put(CompLocationRpc.FAX, new ExcelData(location.getFax() != null ? location.getFax() : "", ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CompLocationRpc.ZIP_CODE)) {
                    mapColumn.put(CompLocationRpc.ZIP_CODE, new ExcelData(location.getZipCode() != null ? location.getZipCode() : "", ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CompLocationRpc.PARENT)) {
                    mapColumn.put(CompLocationRpc.PARENT, new ExcelData(location.getParent() != null ? location.getParent().getName() : "", ExcelData.STRING, 75, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                CustomFieldsUtils.setCustomFieldsExcelTableRowsWithoutBorder(panelTools.getListViewCustomFields(), mapColumn, panelTools.getColumnCodeName(), location, edsCompany);
                excelDataList = new ArrayList<>();
                for (int j = 0; j < panelTools.getColumnCodeName().size(); j++) {
                    if (mapColumn.containsKey(panelTools.getColumnCodeName().get(j))) {
                        excelDataList.add(mapColumn.get(panelTools.getColumnCodeName().get(j)));
                    }
                }
                cellDatas = new ExcelData[excelDataList.size()];
                excelDataList.toArray(cellDatas);
                list.add(cellDatas);
            }
            workBook.setList(list);
            return workBook.getWorkBook(filename, 0, 0, 0, 3);
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("Cannot generate location list excel report, exception: " + ex);
        }
        return null;
    }
}