package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.MeetingManager;
import com.edatasite.workforce.gwt.core.server.db.PropertManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.meetingMinutes.client.rpc.MeetingMinutesItem;
import com.edatasite.workforce.gwt.meetingMinutes.client.rpc.MeetingMinutesService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmResourceBundleMessageSource;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Hasan Xo'janazarov
 * Date: 02.05.12
 * Time: 11:31
 * To change this template use File | Settings | File Templates.
 */
public class MeetingMinutesListExcelHandler extends BaseExcelHandler {

    @Autowired
    @Qualifier("allReferenceWfmMessageSource")
    protected WfmResourceBundleMessageSource excelReferenceMessageSource;

    @Autowired
    private MeetingMinutesService meetingMinutesService;

    @Autowired
    private MeetingManager meetingManager;

    @Autowired
    private PropertManager propertManager;

    @Override
    protected void setFileName() {
        filename = "Meeting Minutes";
    }

    protected HSSFWorkbook getWorkBook(Object object) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) object;
        filterParametrs.setLimit(1000);

        ListResult<MeetingMinutesItem> meetingList = meetingMinutesService.getMeetingMinutes(filterParametrs);
        List<MeetingMinutesItem> meetingListItems = meetingList.getList();
        ExcelData[] cellDatas;
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        List<String> header = panelTools.getColumnCodeName();
        if (header.contains("action")) {
            header.remove("action");
        } else {
            header.remove("action");
        }
        header.remove(MeetingMinutesItem.ACTION);
        Map<String, String> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(MeetingMinutesItem.NAME, commonLocalizer.localize(PdfLocalizationName.name));
        mapColumnHeader.put(MeetingMinutesItem.LOCATION, propertManager.findByCode(Constants.LOCATION_PROPERTY_OBJECTNAME) != null ? propertManager.findByCode("LocListView").getSingular() : commonLocalizer.localize(PdfLocalizationName.location));
        mapColumnHeader.put(MeetingMinutesItem.TYPE, commonLocalizer.localize(PdfLocalizationName.type));
        mapColumnHeader.put(MeetingMinutesItem.MEETING_ID, commonLocalizer.localize(PdfLocalizationName.number));
        mapColumnHeader.put(MeetingMinutesItem.CALLED_BY, commonLocalizer.localize(PdfLocalizationName.calledBy));
        mapColumnHeader.put(MeetingMinutesItem.DATE, commonLocalizer.localize(PdfLocalizationName.startDate));
        mapColumnHeader.put(MeetingMinutesItem.END_DATE, commonLocalizer.localize(PdfLocalizationName.endDate));
        mapColumnHeader.put(MeetingMinutesItem.PREPARED_BY, commonLocalizer.localize(PdfLocalizationName.preparedBy));

        try {
            EdsUser user = meetingManager.getUser();
            List<ExcelData[]> list = new LinkedList<>();
            cellDatas = new ExcelData[header.size()];

            for (int i = 0; i < header.size(); i++) {
                cellDatas[i] = new ExcelData(mapColumnHeader.get(header.get(i)), ExcelData.STRING, 20, false, false, ExcelData.NO_BORDER, ExcelData.HEADER);
            }
            list.add(cellDatas);
            for (MeetingMinutesItem meetingItem : meetingListItems) {
                String temp = "";
                cellDatas = new ExcelData[header.size()];
                for (int j = 0; j < header.size(); j++) {
                    if (MeetingMinutesItem.NAME.equals(header.get(j))) {
                        temp = meetingItem.getName() != null ? meetingItem.getName() : "";
                    } else if (MeetingMinutesItem.LOCATION.equals(header.get(j))) {
                        temp = meetingItem.getLocation() != null ? meetingItem.getLocation() : "";
                    } else if (MeetingMinutesItem.TYPE.equals(header.get(j))) {
                        temp = meetingItem.getType() != null ? meetingItem.getType().getName() : "";
                    } else if (MeetingMinutesItem.MEETING_ID.equals(header.get(j))) {
                        temp = meetingItem.getMeetingNumber() != null ? meetingItem.getMeetingNumber() : "";
                    } else if (MeetingMinutesItem.CALLED_BY.equals(header.get(j))) {
                        temp = meetingItem.getCalledBy() != null ? meetingItem.getCalledBy().getName() : "";
                    } else if (MeetingMinutesItem.DATE.equals(header.get(j))) {
                        temp = meetingItem.getStartdate() != null ? (ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(ServerUtils.longDateFormat(user.getUserDate(meetingItem.getStartdate()), user)) : ServerUtils.longDateFormat(user.getUserDate(meetingItem.getStartdate()), user)) : "";
                    } else if (MeetingMinutesItem.END_DATE.equals(header.get(j))) {
                        temp = meetingItem.getEnddate() != null ? (ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(ServerUtils.longDateFormat(user.getUserDate(meetingItem.getEnddate()), user)) : ServerUtils.longDateFormat(user.getUserDate(meetingItem.getEnddate()), user)) : "";
                    } else if (MeetingMinutesItem.PREPARED_BY.equals(header.get(j))) {
                        temp = meetingItem.getPreparedBy() != null ? meetingItem.getPreparedBy().getName() : "";
                    }

                    cellDatas[j] = new ExcelData(temp, ExcelData.STRING, 20, false, !header.get(j).equals(MeetingMinutesItem.TYPE), ExcelData.NO_BORDER, ExcelData.NORMAL);
                }
                list.add(cellDatas);
            }
            return new WorkBook(list).getWorkBook(filename, 0, 0, 0, header.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
