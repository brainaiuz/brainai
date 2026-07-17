package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.PropertManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TCService;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.coursebooking.CourseBookingItem;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Murad
 * Date: 11/19/12
 * Time: 9:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class CourseBookingListExcelHandler extends BaseExcelHandler {

    @Autowired
    private TCService tcService;

    @Autowired
    private PropertManager propertManager;

    @Override
    protected void setFileName() {
        filename = "CourseBookingList";
    }

    @Override
    protected boolean prepareRequest(HttpServletRequest request) {
        return false;
    }

    protected HSSFWorkbook getWorkBook(Object object) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) object;

        EdsUser user = userManager.getUser();
        EdsCompany edsCompany = user.getCompany();
        EdsCompanySettings companySettings = edsCompany.getCompanySettings();
        String shortDateFormat = "MMM dd, yyyy";
        if (companySettings != null) {
            shortDateFormat = companySettings.getShortDateFormat();
        }
        if (companySettings != null && companySettings.getExcelLimit() != null && !"".equals(companySettings.getExcelLimit())) {
            filterParametrs.setLimit(Integer.parseInt(companySettings.getExcelLimit()));
        } else {
            filterParametrs.setLimit(LIMIT_EXCEL_ROW);
        }

        int start = 0, limit = 200;
        filterParametrs.setStart(start);

        ListResult<CourseBookingItem> courseList = tcService.getCourseBookingListFromSolr(filterParametrs);
        List<CourseBookingItem> courseItems = new ArrayList<>(courseList.getList());
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();

        ExcelData[] cellDatas;
        Map<String, ExcelData> mapColumnHeader = new HashMap<>();
        try {
            List<ExcelData[]> list = new LinkedList<>();
            mapColumnHeader.put(CourseBookingItem.NUMBER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.number), ExcelData.STRING, 10, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(CourseBookingItem.CUSTOMER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.customer), ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(CourseBookingItem.CONTACT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.contact), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(CourseBookingItem.LOCATION, new ExcelData(propertManager.findByCode(Constants.LOCATION_PROPERTY_OBJECTNAME) != null ? propertManager.findByCode("LocListView").getSingular() : commonLocalizer.localize(PdfLocalizationName.location), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(CourseBookingItem.STATUS, new ExcelData(commonLocalizer.localize(PdfLocalizationName.status), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(CourseBookingItem.TYPE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.type), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(CourseBookingItem.CREATIONDATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.creationDate), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));

            List<ExcelData> excellDatasList = new ArrayList<>();
            for (int i = 0; i < panelTools.getColumnCodeName().size(); i++) {
                if (mapColumnHeader.containsKey(panelTools.getColumnCodeName().get(i))) {
                    excellDatasList.add(getExcelDataHeader(mapColumnHeader.get(panelTools.getColumnCodeName().get(i))));
                }
            }
            cellDatas = new ExcelData[excellDatasList.size()];
            excellDatasList.toArray(cellDatas);
            list.add(cellDatas);

            for (CourseBookingItem courseBooking : courseItems) {
                Map<String, ExcelData> mapColumn = new HashMap<>();
                if (panelTools.getColumnCodeName().contains(CourseBookingItem.NUMBER)) {
                    mapColumn.put(CourseBookingItem.NUMBER, new ExcelData(courseBooking.getNumber() != null ? courseBooking.getNumber() : "", ExcelData.STRING, 10, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CourseBookingItem.CUSTOMER)) {
                    mapColumn.put(CourseBookingItem.CUSTOMER, new ExcelData(courseBooking.getCustomer() != null ? courseBooking.getCustomer().getName() : "", ExcelData.STRING, 30, true, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CourseBookingItem.CONTACT)) {
                    mapColumn.put(CourseBookingItem.CONTACT, new ExcelData(courseBooking.getContact() != null ? courseBooking.getContact().getName() : "", ExcelData.STRING, 20, true, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CourseBookingItem.LOCATION)) {
                    mapColumn.put(CourseBookingItem.LOCATION, new ExcelData(courseBooking.getLocation() != null ? courseBooking.getLocation().getName() : "", ExcelData.STRING, 20, true, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CourseBookingItem.STATUS)) {
                    mapColumn.put(CourseBookingItem.STATUS, new ExcelData(courseBooking.getStatus() != null ? courseBooking.getStatus() : "", ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CourseBookingItem.TYPE)) {
                    mapColumn.put(CourseBookingItem.TYPE, new ExcelData(courseBooking.getType() != null ? courseBooking.getType() : "", ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CourseBookingItem.CREATIONDATE)) {
                    mapColumn.put(CourseBookingItem.CREATIONDATE, new ExcelData(ServerUtils.dateFormat(courseBooking.getCreationDate() != null ? user.getUserDate(courseBooking.getCreationDate()) : null, shortDateFormat),
                            ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                excellDatasList = new ArrayList<>();
                for (int i = 0; i < panelTools.getColumnCodeName().size(); i++) {
                    if (mapColumn.containsKey(panelTools.getColumnCodeName().get(i))) {
                        excellDatasList.add(getExcelRows(mapColumn.get(panelTools.getColumnCodeName().get(i))));
                    }
                }
                cellDatas = new ExcelData[excellDatasList.size()];
                excellDatasList.toArray(cellDatas);
                list.add(cellDatas);
            }
            WorkBook workBook = new WorkBook(list, true, 0, 1, 0, 1);

            return workBook.getWorkBook(filename, 0, 0, 0, 7);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
