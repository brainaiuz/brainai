package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityService;
import com.edatasite.workforce.gwt.availability.client.rpc.HolidayItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.PropertManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 23.07.2009
 * Time: 15:20:58
 * To change this template use File | Settings | File Templates.
 */
public class HolidaysExcelHandler extends BaseExcelHandler {

    @Autowired
    private AvailabilityService availabilityService;
    @Autowired
    private UserManager userManager;

    @Autowired
    private PropertManager propertManager;
    private static final Logger log = LoggerFactory.getLogger(TimeSlotListExcelHandler.class);

    @Override
    protected void setFileName() {
        filename = "Holidays";
    }

    protected HSSFWorkbook getWorkBook(Object ob) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) ob;
        filterParametrs.setLimit(LIMIT_EXCEL_ROW);
        ListResult<HolidayItem> holidayList = availabilityService.getHolidays(filterParametrs);
        List<HolidayItem> holListItems = holidayList.getList();
        EdsUser user = userManager.getUser();
        ExcelData[] cellDatas;
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        List<String> header = panelTools.getColumnCodeName();
        if (header.contains("Action")) {
            header.remove("Action");
        } else {
            header.remove("action");
        }
        header.remove(HolidayItem.ACTION);
        Map<String, String> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(HolidayItem.NAME, commonLocalizer.localize(PdfLocalizationName.name));
        mapColumnHeader.put(HolidayItem.DESCRIPTION, commonLocalizer.localize(PdfLocalizationName.description));
        mapColumnHeader.put(HolidayItem.FROM, commonLocalizer.localize(PdfLocalizationName.from));
        mapColumnHeader.put(HolidayItem.TO, commonLocalizer.localize(PdfLocalizationName.to));
        mapColumnHeader.put(HolidayItem.LOCATION, propertManager.findByCode(Constants.LOCATION_PROPERTY_OBJECTNAME) != null ? propertManager.findByCode("LocListView").getSingular() : commonLocalizer.localize(PdfLocalizationName.location));
        try {
            List<ExcelData[]> list = new LinkedList<>();
            cellDatas = new ExcelData[header.size()];

            for (int i = 0; i < header.size(); i++) {
                cellDatas[i] = new ExcelData(mapColumnHeader.get(header.get(i)), ExcelData.STRING, header.get(i).equals(HolidayItem.NAME) || header.get(i).equals(HolidayItem.DESCRIPTION) ? 50 : 20, false, header.get(i).equals(HolidayItem.NAME) || header.get(i).equals(HolidayItem.DESCRIPTION) || header.get(i).equals(HolidayItem.FROM), ExcelData.NO_BORDER, ExcelData.HEADER);
            }

            list.add(cellDatas);

            for (HolidayItem holidays : holListItems) {
                String temp = "";
                cellDatas = new ExcelData[header.size()];
                for (int j = 0; j < header.size(); j++) {
                    temp = "";
                    if (HolidayItem.NAME.equals(header.get(j))) {
                        temp = holidays.getName() == null ? "" : holidays.getName();
                    }
                    if (HolidayItem.DESCRIPTION.equals(header.get(j))) {
                        temp = holidays.getDescription() == null ? "" : holidays.getDescription();
                    }
                    if (HolidayItem.FROM.equals(header.get(j))) {
                        String fromDate = dateFormat(holidays.getFrom().getDate(), true);
                        temp = holidays.getFrom() == null ? "" : ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(fromDate) : fromDate;
                    }
                    if (HolidayItem.TO.equals(header.get(j))) {
                        String toDate = dateFormat(holidays.getTo().getDate(), true);
                        temp = holidays.getTo() == null ? "" : ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(toDate) : toDate;
                    }
                    if (HolidayItem.LOCATION.equals(header.get(j))) {
                        temp = holidays.getName() == null ? "" : holidays.getLocationName();
                    }
                    cellDatas[j] = new ExcelData(temp, ExcelData.STRING, header.get(j).equals(HolidayItem.NAME) || header.get(j).equals(HolidayItem.DESCRIPTION) ? 50 : 20, false, !header.get(j).equals(HolidayItem.TAKEN_FROM_ANNUAL_LEAVE_ALLOWANCE), ExcelData.NO_BORDER, ExcelData.NORMAL);
                }
                list.add(cellDatas);
            }
            return new WorkBook(list).getWorkBook(filename, 0, 0, 0, header.size());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Cannot generate holidays list excel report, exception: " + e);
        }
        return null;
    }

}
