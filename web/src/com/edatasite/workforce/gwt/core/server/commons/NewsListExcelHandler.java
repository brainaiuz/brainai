package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.PropertManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.news.client.rpc.NewsListItem;
import com.edatasite.workforce.gwt.news.client.rpc.NewsService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmResourceBundleMessageSource;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * User: Ilhombek
 * Date: 23.05.2010
 * Time: 21:28:19
 */
public class NewsListExcelHandler extends BaseExcelHandler {

    private static final Logger log = LoggerFactory.getLogger(NewsListExcelHandler.class);

    @Autowired
    private NewsService newsService;
    @Autowired
    private UserManager userManager;
    @Autowired
    @Qualifier("allReferenceWfmMessageSource")
    protected WfmResourceBundleMessageSource excelReferenceMessageSource;
    @Autowired
    private PropertManager propertManager;

    @Override
    protected void setFileName() {
        filename = excelReferenceMessageSource.localize("wokrspaceNewsList", "News List");
    }

    @Override
    protected HSSFWorkbook getWorkBook(Object object) {
        String shortDateFormat = "MM/dd/yyyy";
        EdsCompanySettings companySettings = userManager.getUser().getCompany().getCompanySettings();
        if (companySettings != null) {
            shortDateFormat = companySettings.getShortDateFormat();
        }
//        ListingFilterParameter fp = new ListingFilterParameter();
        ListingFilterParameter fp = (ListingFilterParameter) object;
        fp.setLimit(1000);
        ListResult<NewsListItem> newsList = newsService.getNewsList(fp);
        List<NewsListItem> newsListItem = newsList.getList();

        ExcelData[] cellExcelDatas;
        ListPanelToolRpc panelTools = fp.getListPanelTool();
        List<String> header = panelTools.getColumnCodeName();
        if (header.contains("Action")) {
            header.remove("Action");
        } else {
            header.remove("action");
        }
        header.remove(NewsListItem.ACTION);
        Map<String, String> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(NewsListItem.SUBJECT, commonLocalizer.localize(PdfLocalizationName.subject));
        mapColumnHeader.put(NewsListItem.DATE, commonLocalizer.localize(PdfLocalizationName.date));
        mapColumnHeader.put(NewsListItem.POSTED_BY, commonLocalizer.localize(PdfLocalizationName.author));
        mapColumnHeader.put(NewsListItem.CATEGORY, commonLocalizer.localize(PdfLocalizationName.category));
        mapColumnHeader.put(NewsListItem.COMMENT, commonLocalizer.localize(PdfLocalizationName.comments));
        mapColumnHeader.put(NewsListItem.VISIBILITY, commonLocalizer.localize(PdfLocalizationName.visibility));
        mapColumnHeader.put(NewsListItem.LOCATION, propertManager.findByCode(Constants.LOCATION_PROPERTY_OBJECTNAME) != null ? propertManager.findByCode("LocListView").getSingular() : commonLocalizer.localize(PdfLocalizationName.location));
        try {
            List<ExcelData[]> list = new LinkedList<>();
            cellExcelDatas = new ExcelData[header.size()];

            for (int i = 0; i < header.size(); i++) {
                cellExcelDatas[i] = new ExcelData(mapColumnHeader.get(header.get(i)), ExcelData.STRING, header.get(i).equals(NewsListItem.SUBJECT) || header.get(i).equals(NewsListItem.DATE) ? 50 : 20, false, header.get(i).equals(NewsListItem.POSTED_BY) || header.get(i).equals(NewsListItem.COMMENT) || header.get(i).equals(NewsListItem.VISIBILITY), ExcelData.NO_BORDER, ExcelData.HEADER);
            }

            list.add(cellExcelDatas);

            for (NewsListItem grades : newsListItem) {
                String temp = "";

                cellExcelDatas = new ExcelData[header.size()];
                for (int j = 0; j < header.size(); j++) {
                    temp = "";
                    if (NewsListItem.SUBJECT.equals(header.get(j))) {
                        temp = grades.getSubject() != null ? grades.getSubject() : "";
                    } else if (NewsListItem.DATE.equals(header.get(j))) {
                        temp = ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(ServerUtils.dateFormat(grades.getDate(), shortDateFormat)) : ServerUtils.dateFormat(grades.getDate(), shortDateFormat);
                    } else if (NewsListItem.POSTED_BY.equals(header.get(j))) {
                        temp = grades.getPostedBy() != null ? grades.getPostedBy() : "";
                    } else if (NewsListItem.CATEGORY.equals(header.get(j))) {
                        temp = grades.getCategoryName() != null ? grades.getCategoryName() : "";
                    } else if (NewsListItem.LOCATION.equals(header.get(j))) {
                        temp = grades.getLocationName() != null ? grades.getLocationName() : "";
                    } else if (NewsListItem.COMMENT.equals(header.get(j))) {
                        temp = String.valueOf(grades.getComments() == null || grades.getComments() == 0 ? 0 : grades.getComments());

                    } else if (NewsListItem.VISIBILITY.equals(header.get(j))) {
                        temp = grades.isVisibility() ? excelReferenceMessageSource.localize("wokrspacepublic", "Public") : commonLocalizer.localize("internal", "Internal");
                    }
                    cellExcelDatas[j] = new ExcelData(temp, ExcelData.STRING, header.get(j).equals(NewsListItem.SUBJECT) || header.get(j).equals(NewsListItem.DATE) ? 50 : 20, false, header.get(j).equals(NewsListItem.POSTED_BY) || header.get(j).equals(NewsListItem.COMMENT) || header.get(j).equals(NewsListItem.VISIBILITY), ExcelData.NO_BORDER, ExcelData.NORMAL);
                }
                list.add(cellExcelDatas);

            }
            return new WorkBook(list).getWorkBook(filename, 0, 0, 0, header.size());
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("Cannot generate news list excel report, exception: " + ex);
        }
        return null;
    }
}