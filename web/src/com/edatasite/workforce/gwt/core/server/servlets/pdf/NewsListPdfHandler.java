package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.news.client.rpc.NewsListItem;
import com.edatasite.workforce.gwt.news.client.rpc.NewsService;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * User: Xushnud
 * Date: 23.12.2009
 * Time: 21:17:45
 */
public class NewsListPdfHandler extends AbstractITextPostPdfHandler {

    private NewsService newsService;

    public void setNewsService(NewsService newsService) {
        this.newsService = newsService;
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        EdsCompanySettings companySettings = company.getCompanySettings();
        filterParametrs.setLimit(StringUtils.isNotEmpty(companySettings.getPdfLimit()) ? Integer.parseInt(companySettings.getPdfLimit()) : LIMIT_PDF_ROWS);
        ListResult<NewsListItem> newslist = newsService.getNewsList(filterParametrs);

        Map<String, CellData> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(NewsListItem.SUBJECT, new CellData(commonLocalizer.localize(PdfLocalizationName.subject), Element.ALIGN_LEFT));
        mapColumnHeader.put(NewsListItem.DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.date), Element.ALIGN_LEFT));
        mapColumnHeader.put(NewsListItem.POSTED_BY, new CellData(commonLocalizer.localize(PdfLocalizationName.author), Element.ALIGN_LEFT));
        mapColumnHeader.put(NewsListItem.CATEGORY, new CellData(commonLocalizer.localize(PdfLocalizationName.category), Element.ALIGN_LEFT));
        mapColumnHeader.put(NewsListItem.LOCATION, new CellData(propertManager.findByCode(Constants.LOCATION_PROPERTY_OBJECTNAME) != null ? propertManager.findByCode("LocListView").getSingular() : commonLocalizer.localize(PdfLocalizationName.location), Element.ALIGN_LEFT));
        mapColumnHeader.put(NewsListItem.COMMENT, new CellData(commonLocalizer.localize(PdfLocalizationName.comments), Element.ALIGN_LEFT));
        mapColumnHeader.put(NewsListItem.VISIBILITY, new CellData(commonLocalizer.localize(PdfLocalizationName.visibility), Element.ALIGN_LEFT));
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        List<CellData> header = panelTools.getColumnCodeName().stream()
                .filter(columnCode -> mapColumnHeader.containsKey(columnCode))
                .map(columnCode -> mapColumnHeader.get(columnCode))
                .collect(Collectors.toList());
        ITextTableList tableList = new ITextTableList(header.size());
        tableList.addPdfTableHeader(header.toArray(new CellData[]{}));

        for (NewsListItem newsListItem : newslist.getList()) {
            Map<String, String> mapColumns = new HashMap<>();
            if (panelTools.getColumnCodeName().contains(NewsListItem.SUBJECT)) {
                mapColumns.put(NewsListItem.SUBJECT, getResultOrLongDash(newsListItem.getSubject()));
            }
            if (panelTools.getColumnCodeName().contains(NewsListItem.COMMENT)) {
                mapColumns.put(NewsListItem.COMMENT, String.valueOf(newsListItem.getComments() == null || newsListItem.getComments() == 0 ? 0 : newsListItem.getComments()));
            }
            if (panelTools.getColumnCodeName().contains(NewsListItem.DATE)) {
                mapColumns.put(NewsListItem.DATE, newsListItem.getDate() != null ? (ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(dateFormat(newsListItem.getDate())) : dateFormat(newsListItem.getDate())) : "newsListItem.getSubject()");
            }

            if (panelTools.getColumnCodeName().contains(NewsListItem.POSTED_BY)) {
                mapColumns.put(NewsListItem.POSTED_BY, getResultOrLongDash(newsListItem.getPostedBy()));
            }
            if (panelTools.getColumnCodeName().contains(NewsListItem.CATEGORY)) {
                mapColumns.put(NewsListItem.CATEGORY, newsListItem.getCategoryName() != null ? newsListItem.getCategoryName().replace("[", "").replace("]", "") : "—");
            }
            if (panelTools.getColumnCodeName().contains(NewsListItem.LOCATION)) {
                mapColumns.put(NewsListItem.LOCATION, getResultOrLongDash(newsListItem.getLocationName()));
            }
            if (panelTools.getColumnCodeName().contains(NewsListItem.VISIBILITY)) {
                mapColumns.put(NewsListItem.VISIBILITY, newsListItem.isVisibility() ? commonLocalizer.localize("public") : commonLocalizer.localize(PdfLocalizationName.internal));
            }
            List<String> columns = panelTools.getColumnCodeName().stream()
                    .filter(columnCode -> mapColumns.containsKey(columnCode))
                    .map(columnCode -> mapColumns.get(columnCode))
                    .collect(Collectors.toList());
            tableList.addPdfTableRows(columns.toArray(new String[]{}));
        }

        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        pdfData.setListTable(tableList);
        return pdfData;
    }

    @Override
    protected boolean isListingPDF() {
        return true;
    }

    @Override
    protected String getTableName(Object dataClass) {
        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        EdsProperty property = propertManager.findByCode(fp.getPropertyCode());
        return property != null ? property.getPlural() : pdfWfmMessageSource.localize("companyNews");
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(user.getFirstName() + "_" + user.getLastName() + "_NewsList_" + dateFormat(new Date()));
    }
}
