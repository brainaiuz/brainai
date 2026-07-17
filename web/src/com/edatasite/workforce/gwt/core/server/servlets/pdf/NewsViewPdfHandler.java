package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.NewsComment;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.lucene.parser.HTMLParser;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextPdfViewTypeEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextSummaryView;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.PdfParams;
import com.edatasite.workforce.gwt.news.client.rpc.NewsData;
import com.edatasite.workforce.gwt.news.server.NewsServiceLocal;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * User: Xushnud
 * Date: 24.12.2009
 * Time: 16:54:35
 */
public class NewsViewPdfHandler extends AbstractITextPostPdfHandler implements PDFConstants, Constants {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd , yyyy HH:mm", Locale.ENGLISH);

    @Autowired
    private NewsServiceLocal newsServiceLocal;

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) {
        ITextGenericPdfData pdf = new ITextGenericPdfData();
        ITextSummaryView pdfData = new ITextSummaryView();
        pdf.setSummaryView(pdfData);
        pdf.setPdfViewType(ITextPdfViewTypeEnum.SUMMARYVIEW);
        ITextTableList table = new ITextTableList(6);
        pdfData.addTable(table);
        EdsUser user = uploadManager.getUser();
        pdf.setTableName("News");
        RequestObject requestObject = (RequestObject) dataClass;
        Integer newsID = requestObject.getObjectID();
        NewsData newsItem = newsServiceLocal.getNews(newsID);
        NewsComment[] comments = newsServiceLocal.getNewsComments(newsID);
        String nophotoImage = null;
        try {
            nophotoImage = getRealPath("/pdfimages/no-photo.gif");
        } catch (IOException e) {
            e.printStackTrace();
        }
        setFileName("News" + "_" + newsItem.getSubject() + "_" + dateFormat(user.getUserDate()));

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd , yyyy HH:mm", Locale.ENGLISH);

        table.addPdfTableHeader("Subject", "Date", "Posted By", "Location", "Comments", "Visibility");
        String subject = newsItem.getSubject();
        String commentsNumber = String.valueOf(newsItem.getComments().size());
        String date = ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(dateFormat.format(newsItem.getPublishedDate())) : dateFormat.format(newsItem.getPublishedDate());
        String postedBy = newsItem.getCreatorName();
        String location = newsItem.getLocation();
        String visibility = newsItem.isVisibility() ? "Public" : "Internal";
        table.addPdfTableRows(subject, date, postedBy, location, commentsNumber, visibility);
        ITextTableList newsFullTextTable = new ITextTableList(1);
        newsFullTextTable.setTotalWidth(560);
        newsFullTextTable.setBorderWidth(0);
        newsFullTextTable.setName("Full Text:");
        HTMLParser htmlParser = new HTMLParser();

        try {
            String parse = htmlParser.performParse(newsItem.getFullDescription());
            newsFullTextTable.addPdfTableRows(parse);
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }
        pdfData.addTable(newsFullTextTable);

        ITextTableList commentsTable = new ITextTableList(2);
        commentsTable.setTotalWidth(200);
        commentsTable.setBorderWidth(0);
        commentsTable.setName("Comments:");

        pdfData.addTable(commentsTable);
        for (NewsComment comment1 : comments) {
            String name = comment1.getUsername();
            String commentDate = ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(dateFormat.format(user.getUserDate(comment1.getDate()))) : dateFormat.format(user.getUserDate(comment1.getDate()));
            String imageUrl = comment1.getEmployeeImageUrl() != null ? comment1.getEmployeeImageUrl() : nophotoImage;
            String comment = comment1.getComment() != null ? String.valueOf(comment1.getComment()) : "";
            commentsTable.addPdfTableRows(name, commentDate);
            CellData imageCell = new CellData(ITextTableList.CELL_IMAGE);
            imageCell.setLink(imageUrl);
            commentsTable.addPdfTableRows(imageCell, new CellData(comment));
        }
        return pdf;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ITextGenericPdfData pdf = new ITextGenericPdfData();
        EdsUser edsUser = uploadManager.getUser();

        RequestObject requestObject = (RequestObject) dataClass;
        Integer newsId = requestObject.getObjectID();

        NewsData newsItem = newsServiceLocal.getNews(newsId);
        NewsComment[] comments = newsServiceLocal.getNewsComments(newsId);

        String fullDescription = null;
        HTMLParser htmlParser = new HTMLParser();
        try {
            fullDescription = htmlParser.performParse(newsItem.getFullDescription());
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }
        HashMap<String, CustomisedITextTable> newsMap = Maps.newHashMap();
        CustomisedITextTable newsTable = new CustomisedITextTable();
        newsTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE, TYPE);
        newsTable.addRowWithCode(SUBJECT, commonLocalizer.localize(PdfLocalizationName.subject), escapeHtml(newsItem.getSubject()), SUBJECT);
        newsTable.addRowWithCode(COMMENT_SIZE, commonLocalizer.localize(PdfLocalizationName.comments), String.valueOf(newsItem.getComments().size()), COMMENT_SIZE);
        newsTable.addRowWithCode(PUBLISHED_DATE, commonLocalizer.localize(PdfLocalizationName.date), dateFormat.format(newsItem.getPublishedDate()), PUBLISHED_DATE);
        newsTable.addRowWithCode(USER_NAME, commonLocalizer.localize(PdfLocalizationName.postedby), escapeHtml(newsItem.getCreatorName()), USER_NAME);
        newsTable.addRowWithCode(LOCATION, propertManager.findByCode(Constants.LOCATION_PROPERTY_OBJECTNAME) != null ? propertManager.findByCode("LocListView").getSingular() : commonLocalizer.localize(PdfLocalizationName.location), escapeHtml(newsItem.getLocation()), LOCATION);
        newsTable.addRowWithCode(VISIBILITY, commonLocalizer.localize(PdfLocalizationName.visibility), newsItem.isVisibility() ? commonLocalizer.localize(PdfLocalizationName.publicValue) : commonLocalizer.localize(PdfLocalizationName.internalValue), VISIBILITY);
        newsTable.addRowWithCode(DESCRIPTION, commonLocalizer.localize(PdfLocalizationName.fullText), fullDescription, DESCRIPTION);
        newsMap.put("NEWS", newsTable);

        CustomisedITextTable commentTable = new CustomisedITextTable();
        commentTable.setName(pdfWfmMessageSource.localize(PdfLocalizationName.comments));
        commentTable.addColumn(USER_NAME, "");
        commentTable.addColumn(COMMENT_DATE, "");
        commentTable.addColumn(USER_IMAGE, "");
        commentTable.addColumn(COMMENT, "");
        List<String> columnsValue = Lists.newArrayList();

        if (comments != null) {
            for (NewsComment item : comments) {
                columnsValue.clear();
                columnsValue.add(escapeHtml(item.getUsername()));
                columnsValue.add(ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(dateFormat.format(edsUser.getUserDate(item.getDate()))) : dateFormat.format(edsUser.getUserDate(item.getDate())));
                columnsValue.add(item.getEmployeeImageUrl() != null ? item.getEmployeeImageUrl() : "");
                columnsValue.add(escapeHtml(item.getComment()));
                commentTable.addRow(columnsValue.toArray(new String[]{}));
            }
        }
        newsMap.put("NEWS_COMMENTS", commentTable);

        pdf.setCustomData(newsMap);
        return pdf;
    }

    @Override
    protected PdfParams.Orientation getOrientation(Object dataClass) {
        return ((RequestObject) dataClass).getIS_LANDSCAPE() ? PdfParams.Orientation.landscape : null;
    }

    @Override
    protected Object getDataClass(HttpServletRequest request) {
        return new RequestObject();
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        RequestObject requestObject = (RequestObject) dataClass;
        Integer newsID = requestObject.getObjectID();
        NewsData newsItem = newsServiceLocal.getNews(newsID);
        setFileName("News" + "_" + newsItem.getSubject() + "_" + dateFormat(user.getUserDate()));
    }

    @Override
    protected String getTableName(Object dataClass) {
        return pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.news);
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.NEWS;
    }
}
