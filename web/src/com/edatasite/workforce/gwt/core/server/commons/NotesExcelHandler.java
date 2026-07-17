package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.note.client.rpc.NoteService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.WfmResourceBundleMessageSource;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 10.09.2009
 * Time: 14:47:39
 * To change this template use File | Settings | File Templates.
 */
public class NotesExcelHandler extends BaseExcelHandler {
    @Autowired
    private NoteService noteService;
    @Autowired
    private UserManager userManager;

    private static final Logger log = LoggerFactory.getLogger(NotesExcelHandler.class);
    protected WfmResourceBundleMessageSource excelReferenceMessageSource;
   /* protected Object getDataClass(HttpServletRequest request) {
        return new ListingFilterParameter();
    }*/

    @Override
    protected void setFileName() {
        filename = excelReferenceMessageSource.localize("wokrspacenotes", "Notes");
    }

    protected HSSFWorkbook getWorkBook(Object object) {

        String longDateFormat = "MM/dd/yyyy HH:mm";
        EdsCompanySettings companySettings = userManager.getUser().getCompany().getCompanySettings();
        if (companySettings != null) {
            longDateFormat = companySettings.getLongDateFormat();
        }

        ListingFilterParameter filterParametrs = (ListingFilterParameter) object;
        filterParametrs.setLimit(1000);
        ListResult<HistoryListItem> historyList = noteService.noteList(filterParametrs);
        List<HistoryListItem> historyListItems = historyList.getList();
        ExcelData[] cellDatas;
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        List<String> header = panelTools.getColumnCodeName();
        if (header.contains("Action")) {
            header.remove("Action");
        } else {
            header.remove("action");
        }
        header.remove(HistoryListItem.action);
        Map<String, String> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(HistoryListItem.SUBJECT,excelReferenceMessageSource.localize("wokrspacesubject", "Subject"));
        mapColumnHeader.put(HistoryListItem.NOTE, excelReferenceMessageSource.localize("wokrspacedescriptionField", "Note"));
        mapColumnHeader.put(HistoryListItem.relatedTo, excelReferenceMessageSource.localize("wokrspacerelatedTo", "Related to"));
        mapColumnHeader.put(HistoryListItem.modified, excelReferenceMessageSource.localize("wokrspacemodified", "Modified"));
        mapColumnHeader.put(HistoryListItem.visibilit, excelReferenceMessageSource.localize("wokrspacevisibility", "Visibility"));
        mapColumnHeader.put(HistoryListItem.owner, excelReferenceMessageSource.localize("wokrspacepostedBy", "Posted by"));

         try {
            List<ExcelData[]> list = new LinkedList<>();
            cellDatas = new ExcelData[header.size()];

            for (int i = 0; i < header.size(); i++) {
                cellDatas[i] = new ExcelData(mapColumnHeader.get(header.get(i)), ExcelData.STRING, header.get(i).equals(HistoryListItem.SUBJECT) || header.get(i).equals(HistoryListItem.modified) ? 50 : 20, false, header.get(i).equals(HistoryListItem.owner) || header.get(i).equals(HistoryListItem.NOTE) || header.get(i).equals(HistoryListItem.visibilit) || header.get(i).equals(HistoryListItem.relatedTo), ExcelData.NO_BORDER, ExcelData.HEADER);
            }

            list.add(cellDatas);

            for (HistoryListItem grades : historyListItems) {
                String temp = "";

                cellDatas = new ExcelData[header.size()];
                for (int j = 0; j < header.size(); j++) {
                    temp = "";
                    if (HistoryListItem.SUBJECT.equals(header.get(j))) {
                    temp = grades.getSubject() != null ? grades.getSubject(): "";
                } else if (HistoryListItem.NOTE.equals(header.get(j))) {
                    temp = (grades.getComment()!= null ? grades.getComment(): "");
                } else if (HistoryListItem.relatedTo.equals(header.get(j))) {
                    temp = grades.getRelatedName() != null ? grades.getRelatedName():"";
                } else if (HistoryListItem.modified.equals(header.get(j))) {
                    temp = grades.getEventDate() != null ? ServerUtils.dateFormat(grades.getEventDate(),longDateFormat):"";
                } else if (HistoryListItem.visibilit.equals(header.get(j))) {
                    temp = grades.isVisibility()? excelReferenceMessageSource.localize("wokrspaceprivate", "Private") :excelReferenceMessageSource.localize("wokrspacepublic", "Public");
                } else if (HistoryListItem.owner.equals(header.get(j))) {
                    temp = grades.getEmployee()!=null? grades.getEmployee():"";
                }
                   // cellDatas[j] = new ExcelData(temp, ExcelData.STRING, header.get(j).equals(NewsListItem.SUBJECT) || header.get(j).equals(NewsListItem.DATE) ? 50 : 20, false, header.get(j).equals(NewsListItem.POSTED_BY)|| header.get(j).equals(NewsListItem.COMMENT) || header.get(j).equals(NewsListItem.VISIBILITY) ? true : false, ExcelData.NO_BORDER, ExcelData.NORMAL);
                    cellDatas[j] = new ExcelData(temp, ExcelData.STRING, header.get(j).equals(HistoryListItem.SUBJECT) || header.get(j).equals(HistoryListItem.modified) ? 50 : 20, false, header.get(j).equals(HistoryListItem.owner) || header.get(j).equals(HistoryListItem.NOTE) || header.get(j).equals(HistoryListItem.visibilit) || header.get(j).equals(HistoryListItem.relatedTo), ExcelData.NO_BORDER, ExcelData.NORMAL);

                }
                list.add(cellDatas);

            }
            return new WorkBook(list).getWorkBook(filename, 0, 0, 0, header.size());
        }
       catch (Exception ex) {
            ex.printStackTrace();
            log.error("Cannot generate Notes excel report, exception: " + ex);
        }
        return null;
    }

    public void setExcelReferenceMessageSource(WfmResourceBundleMessageSource excelReferenceMessageSource) {
        this.excelReferenceMessageSource = excelReferenceMessageSource;
    }
}
