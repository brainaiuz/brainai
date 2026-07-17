package com.finnetlimited.reportservice.core.server.handler;

import com.edatasite.shared.log.KpiLog;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsLocale;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.pdf.EdsCompanyPdfTemplate;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.LocaleManager;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.AbstractITextPostPdfHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.IPostPDFHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PdfReferenceCodeNameEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextPdfViewTypeEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextRowProperty;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.RTLTextReplacedElementFactory;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.*;
import com.edatasite.workforce.gwt.reportingsystem.client.enumerable.ReportType;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ColumnRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.xml.RpcConvertToXmlLocal;
import com.edatasite.workforce.mail.EdsTemplate;
import com.finnetlimited.reportservice.core.client.gwtrpc.CoreService;
import com.finnetlimited.reportservice.core.client.gwtrpc.ReportRequestObject;
import com.finnetlimited.reportservice.core.client.gwtrpc.ViewRpc;
import com.finnetlimited.reportservice.core.server.CoreServiceLocal;
import com.finnetlimited.reportservice.core.server.parser.XmlParser;
import com.finnetlimited.reportservice.core.server.utils.SqlQueryUtil;
import com.finnetlimited.reportservice.core.server.utils.StrUtils;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xhtmlrenderer.pdf.PDFEncryption;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: ${Dilsh0d}
 * Date: 14-Apr-2010
 * Time: 17:10:11
 */
public class PdfReportHandler extends AbstractITextPostPdfHandler implements IPostPDFHandler {

    protected SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM_dd_yyyy_HH_mm");
    private SimpleDateFormat dateFormatForTitle = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private static Logger log = LoggerFactory.getLogger(PdfReportHandler.class);
    @Autowired
    @Qualifier("reportingCoreService")
    private CoreService coreService;
    @Autowired
    @Qualifier("reportingCoreService")
    private CoreServiceLocal coreServiceLocal;
    private static HashMap<String, Integer> typeMap = new HashMap<>();
    @Autowired
    private LocaleManager localeManager;

    static {
        typeMap.put("string", 0);
        typeMap.put("number", 1);
        typeMap.put("money", 2);
        typeMap.put("percent", 3);
        typeMap.put("time", 4);
        typeMap.put("date", 5);
    }

    protected ByteArrayOutputStream getPdfArrayOutputStream(Object dataClass) {
        EdsCompany edsCompany;
        if (getUserId(dataClass) != null) {
            edsCompany = userManager.get(getUserId(dataClass)).getCompany();
        } else {
            edsCompany = userManager.getUser().getCompany();
        }
        PdfParams pdfParams = getParams(dataClass);
        String templateName = pdfParams.getTemplateName();

        if (!ServerUtils.isNullOrEmpty(templateName) && ("End of the day (This month)".equalsIgnoreCase(templateName) || "Kunlik savdo xolati".equalsIgnoreCase(templateName))) {
            return generateCustomisePdfTemplate(dataClass, edsCompany, null);
        } else if (getPdfCodeName(dataClass) != null && StringUtils.isNotEmpty(getPdfCodeName(dataClass).getUrl())) {
            return generatePDFFromHTML(dataClass, edsCompany, getPdfCodeName(dataClass).getUrl(), null);
        } else {
            return generateCustomisePdfTemplate(dataClass, edsCompany, null);
        }
    }

    protected ByteArrayOutputStream generateCustomisePdfTemplate(Object dataClass, EdsCompany edsCompany, EdsCompanyPdfTemplate edsCompanyPdfTemplate) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream is = null;
        PdfParams pdfParams = getParams(dataClass);
        String templateName = pdfParams.getTemplateName();

        if (edsCompany.getObjectID().equals(74682) && !ServerUtils.isNullOrEmpty(templateName) && "End of the day (This month)".equalsIgnoreCase(templateName)) {
            is = this.getClass().getClassLoader().getResourceAsStream("/template/reporting_system_mobio_check.html");
        } else if (!ServerUtils.isNullOrEmpty(templateName) && "Kunlik savdo xolati".equalsIgnoreCase(templateName)) {
            is = this.getClass().getClassLoader().getResourceAsStream("/template/reporting_system_mobio_kunlik_savdo_check.html");
        } else {
            is = this.getClass().getClassLoader().getResourceAsStream("/template/reporting_system_itext.html");
        }
        String content = "";
        try {
            content = IOUtils.toString(is, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        ITextGenericPdfData itextGenericPdfData = buildPdfDocumentCustomise(dataClass, edsCompany, false);

        EdsTemplate template = new EdsTemplate(content);
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            org.w3c.dom.Document doc = builder.parse(new ByteArrayInputStream(template.process(itextGenericPdfData).getBytes(StandardCharsets.UTF_8)));
            ITextRenderer renderer = new ITextRenderer();
            RTLTextReplacedElementFactory rtlTextReplacedElementFactory = new RTLTextReplacedElementFactory(renderer.getOutputDevice(), "rtldir-arabic;rtldirheader-arabic");
            renderer.getSharedContext().setReplacedElementFactory(rtlTextReplacedElementFactory);
            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/Montserrat-Regular.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/Montserrat-Bold.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/Montserrat-BoldItalic.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/Montserrat-Italic.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            renderer.setDocument(doc, null);
            renderer.layout();
            if (itextGenericPdfData.getUserPassword() != null) {
                renderer.setPDFEncryption(new PDFEncryption(itextGenericPdfData.getUserPassword(), itextGenericPdfData.getOwnerPassword() != null ? itextGenericPdfData.getOwnerPassword() : itextGenericPdfData.getUserPassword(), PdfWriter.ALLOW_PRINTING, PdfWriter.ENCRYPTION_AES_128 | PdfWriter.DO_NOT_ENCRYPT_METADATA));
            }
            renderer.createPDF(baos);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baos;
    }

    protected Document newDocument(EdsCompany edsCompany, Object dataClass) {
        if ("55462".equals(ServerSecurityContext.getInstance().getCompanyId()))
            return new Document(PageSize.A4.rotate(), 20, 20, 120, 50);
        else
            return new Document(PageSize.A4, 20, 20, 120, 50);
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        Integer index = 0;
        ReportRequestObject reportRequestObject = (ReportRequestObject) dataClass;
        ReportRpc reportRpc = wrapReport(reportRequestObject);
        setFileName(uploadManager.getUser(), dataClass);

        reportRpc.setLimit(getRowCount(reportRpc.getId()));

        ITextGenericPdfData pdfData = new ITextGenericPdfData();

        ITextTableList tableList = generate(reportRpc, index);

        EdsUser user = uploadManager.getUser();
        EdsCompany edsCompany = user.getCompany();

        pdfData.setPdfViewType(ITextPdfViewTypeEnum.LISTTABLE);
        String tableName = edsCompany.getName() + "\n\n";
        tableName = tableName + (!ServerUtils.isNullOrEmpty(reportRpc.getName()) ? reportRpc.getName() : reportRpc.getViewName()) + "\n\n";
        tableName = tableName + "Created on: " + dateFormatForTitle.format(user.getUserDate());
        pdfData.setTableName(tableName);
        pdfData.setListTable(tableList);
        ITextCompanyData companyData = new ITextCompanyData();
        companyData.setCompanyName(userManager.getUser().getCompany().getName());
        try {
            String imageUrl = getPdfLogoUrl(edsCompany, false);
            if (imageUrl != null) {
                companyData.setCompanyLogoUrl(imageUrl.replaceAll("[&]", "&amp;"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        pdfData.setCompanyData(companyData);

        pdfData.setTableName(reportRpc.getName());
        return pdfData;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        Integer index = 0;
        ReportRequestObject reportRequestObject = (ReportRequestObject) dataClass;
        ReportRpc reportRpc = wrapReport(reportRequestObject);
        setFileName(uploadManager.getUser(), dataClass);

        reportRpc.setLimit(getRowCount(reportRpc.getId()));

        ITextGenericPdfData pdfData = new ITextGenericPdfData();

        ITextTableList tableList = generate(reportRpc, index);

        PdfParams pdfParams = getParams(dataClass);
        String templateName = pdfParams.getTemplateName();

        CustomisedITextTable iTextTable = new CustomisedITextTable();
        CustomisedITextTable kunlikSavdoCheck = new CustomisedITextTable();
        if (!ServerUtils.isNullOrEmpty(templateName)) {
            if ("End of the day (This month)".equalsIgnoreCase(templateName)) {
                iTextTable = generatReportCheckMobio(reportRpc, index);
            } else if ("Kunlik savdo xolati".equalsIgnoreCase(templateName)) {
                kunlikSavdoCheck = generateKunlikSavdoReportCheckMobio(reportRpc, index);
            }
        }

        EdsUser user = uploadManager.getUser();

        SimpleDateFormat format = new SimpleDateFormat("MMM dd yyyy", Locale.ENGLISH);
        pdfData.setCurrentDate(format.format(new Date()));
        pdfData.setExtraData(format.format(new Date()));
        pdfData.setPdfViewType(ITextPdfViewTypeEnum.LISTTABLE);
        pdfData.setListTable(tableList);
        ITextCompanyData companyData = getCompanyData(company, true, hasPhantom);
        pdfData.setExtraData(commonLocalizer.localize("createdOn") + ": " + dateFormatForTitle.format(user.getUserDate()));
        pdfData.setCompanyData(companyData);
        pdfData.setLandscape(reportRpc.isLandscape());
        HashMap<String, CustomisedITextTable> customData = new HashMap<>();
        customData.put("MOBIO_DATA", iTextTable);
        customData.put("KUNLIK_SAVDO_DATA", kunlikSavdoCheck);
        pdfData.setCustomData(customData);

        return pdfData;
    }

    @Override
    protected PdfParams getParams(Object dataClass) {
        ReportRequestObject reportRequestObject = (ReportRequestObject) dataClass;
        ReportRpc reportRpc = wrapReport(reportRequestObject);
        PdfParams params = new PdfParams();
        params.setOrientation(PdfParams.Orientation.getOrientation(reportRpc.isLandscape()));
        params.setTemplateName(reportRpc.getViewName());
        params.setHeaderHeight("300px");
        params.setFooterHeight("240px");
        return params;
    }

    @Override
    protected Integer getCustomisedPDFTemplateId(Object object) {
        return 1;
    }

    @Override
    public void initFooterParams(EdsCompany edsCompany) {
        setShownWFTFooter(false);
    }

    private ITextTableList generate(ReportRpc reportRpc, Integer index) {
        ITextTableList tableList = getHeader(reportRpc);

        try {
            ResultSet resultSet;
            ViewRpc viewRpc = SqlQueryUtil.getViewParser(reportRpc.getViewCode());
            reportRpc.setNoTimeZone(viewRpc.isNoTimezone());
            viewRpc = ReportType.TABULAR.name().equals(reportRpc.getTableType()) ? viewRpc : null;
            if (reportRpc.getTableType().equals(ReportType.TABULAR.name()) || reportRpc.getGroupColumns().isEmpty()) {
                resultSet = coreServiceLocal.getTabularReportResult(reportRpc, null);
                generateTabularReport(resultSet, tableList, reportRpc, index, viewRpc);
            } else {
                resultSet = coreServiceLocal.getSummaryReportResult(reportRpc, null);
                ArrayList<String> selectedColumnNames = new ArrayList<>();
                for (int i = 0; i < reportRpc.getSelectedColumns().size(); i++) {
                    selectedColumnNames.add(reportRpc.getSelectedColumns().get(i).getName());
                }
                ArrayList<String> summaryColumns = new ArrayList<>();
                for (int i = 0; i < reportRpc.getSumaries().size(); i++) {
                    summaryColumns.add(reportRpc.getSumaries().get(i).getName());
                }
                generateSummaryReport(resultSet, tableList, selectedColumnNames, summaryColumns, reportRpc.getGroupColumns().size() + 1, reportRpc.getSelectedColumns().size() + 1, index, reportRpc);
            }
            getFooter(tableList, resultSet, reportRpc, index, viewRpc);

            KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
            kpiLog.setActionType(KpiLog.ActionType.EXPORT);
            if (reportRpc.getId() == null) {
                kpiLog.setEntityType(reportRpc.getViewName());
                kpiLog.setEntityId(reportRpc.getXmlTemplateId());
            } else {
                kpiLog.setEntityId(reportRpc.getId());
                kpiLog.setEntityType(reportRpc.getViewName() + "/" + reportRpc.getName());
            }
            kpiLog.setEntityName(PdfReportHandler.class.getSimpleName());
            ServerUtils.kpiLog(log, kpiLog, "Export Excel Report");
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return tableList;
    }

    private CustomisedITextTable generatReportCheckMobio(ReportRpc reportRpc, Integer index) {
        CustomisedITextTable iTextTable = new CustomisedITextTable();

        try {
            ResultSet resultSet;
            ViewRpc viewRpc = SqlQueryUtil.getViewParser(reportRpc.getViewCode());
            reportRpc.setNoTimeZone(viewRpc.isNoTimezone());
            resultSet = coreServiceLocal.getSummaryReportResult(reportRpc, null);
            ArrayList<String> selectedColumnNames = new ArrayList<>();
            for (int i = 0; i < reportRpc.getSelectedColumns().size(); i++) {
                selectedColumnNames.add(reportRpc.getSelectedColumns().get(i).getName());
            }
            ArrayList<String> summaryColumns = new ArrayList<>();
            for (int i = 0; i < reportRpc.getSumaries().size(); i++) {
                summaryColumns.add(reportRpc.getSumaries().get(i).getName());
            }
            iTextTable = generateSummaryReportMobioCheckPDF(resultSet, selectedColumnNames, summaryColumns, reportRpc.getGroupColumns().size() + 1, reportRpc.getSelectedColumns().size() + 1, index, reportRpc);

        } catch (SQLException e) {
            log.error(e.getMessage());
        }

        return iTextTable;
    }

    private CustomisedITextTable generateKunlikSavdoReportCheckMobio(ReportRpc reportRpc, Integer index) {
        CustomisedITextTable kunlikSavdoTable = new CustomisedITextTable();

        try {
            ResultSet resultSet;
            ViewRpc viewRpc = SqlQueryUtil.getViewParser(reportRpc.getViewCode());
            reportRpc.setNoTimeZone(viewRpc.isNoTimezone());
            resultSet = coreServiceLocal.getSummaryReportResult(reportRpc, null);
            ArrayList<String> selectedColumnNames = new ArrayList<>();
            for (int i = 0; i < reportRpc.getSelectedColumns().size(); i++) {
                selectedColumnNames.add(reportRpc.getSelectedColumns().get(i).getName());
            }
            ArrayList<String> summaryColumns = new ArrayList<>();
            for (int i = 0; i < reportRpc.getSumaries().size(); i++) {
                summaryColumns.add(reportRpc.getSumaries().get(i).getName());
            }
            kunlikSavdoTable = generateKunlikSavdoCheckPDF(resultSet, selectedColumnNames, summaryColumns, reportRpc.getGroupColumns().size() + 1, reportRpc.getSelectedColumns().size() + 1, index, reportRpc);

        } catch (SQLException e) {
            log.error(e.getMessage());
        }

        return kunlikSavdoTable;
    }

    private void getFooter(ITextTableList tableList, ResultSet resultSet, ReportRpc reportRpc, Integer index, ViewRpc viewRpc) throws SQLException {
        //footer row...
        int id = (ReportType.TABULAR.name().equals(reportRpc.getTableType()) && viewRpc != null && viewRpc.getHiddenColumnCount() > 0) ? viewRpc.getHiddenColumnCount() : 0;
        if (resultSet.first()) {
            Map<Integer, ITextRowProperty> properties = new HashMap<>();
            CellData[] cells = new CellData[reportRpc.getSelectedColumns().size()];
            {
                ITextRowProperty property = new ITextRowProperty();
                property.setBackgroundColor("#FAC743");
                String totalGrand = "";
                String local = ServerUtils.getUserLocale().getLanguage();
                if (!ReportType.TABULAR.name().equals(reportRpc.getTableType())) {
                    if ("ru".equals(local)) {
                        totalGrand = "ОБЩИЙ ИТОГ";
                    } else if ("uz".equals(local)) {
                        totalGrand = "JAMI";
                    } else {
                        totalGrand = "GRAND TOTAL";
                    }
                }
                String bgColor = "#f5f7f9";
                CellData cellData = new CellData(totalGrand, Element.ALIGN_LEFT, bgColor);
                cells[0] = cellData;
            }

            for (int i = 2 + id; i <= reportRpc.getSelectedColumns().size() + id; i++) {
                String value = "";
                if (reportRpc.getSumaries().contains(reportRpc.getSelectedColumns().get(i - 1 - id))) {
                    value = resultSet.getString(i);
                    if (value != null) {
                        value = value.replaceAll("(?s)<!--.*?-->", "").replaceAll("<[^>]*>", "").replaceAll("&nbsp;", "\n").replaceAll("&quot;", "\"").replaceAll("- {2}#", "");
                    }
                }
                ITextRowProperty property = new ITextRowProperty();
                property.setBackgroundColor("#FAC743");
                CellData cellData = new CellData(value, Element.ALIGN_RIGHT);
                cells[i - (2 + id) + 1] = cellData;
            }
            tableList.addRowProperty(index, properties);
            tableList.addPdfTableRows(cells);
        }
    }

    private void generateSummaryReport(ResultSet resultSet, ITextTableList tableList, ArrayList<String> selectedColumns, ArrayList<String> summaryColumns, Integer maxDepth, Integer columnCount, Integer index, ReportRpc reportRpc) throws SQLException {
        resultSet.next();
        String sorderColumn = getColumnByName(reportRpc.getSortTableByColumn(), selectedColumns);
        EdsCompany company = userManager.getUser().getCompany();
        EdsCompanySettings companySettings = companySettingsManager.getCompanySettings(company.getObjectID());
        SimpleDateFormat dateFormat = getCompanyLongDateFormat(company);
        SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(company);
        EdsUser user = (EdsUser) ServerSecurityContext.getInstance().getUser();
        Locale locale = Locale.ENGLISH;
        if (user != null && user.getCompany() != null && user.getCompany().getLocale() != null) {
            EdsLocale userLocale = localeManager.getLocaleBylanguageCode(user.getCompany().getLocale());
            if (userLocale != null && userLocale.getLanguageCode() != null && userLocale.getCountry() != null) {
                locale = new Locale(userLocale.getLanguageCode(), userLocale.getCountry());
            } else {
                locale = new Locale(user.getCompany().getLocale());
            }
        }
        NumberFormat numberFormat = NumberFormat.getInstance(locale);
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);

        String local = ServerUtils.getUserLocale().getLanguage();
        String totalStr;
        if ("ru".equals(local)) {
            totalStr = "Итог";
        } else if ("uz".equals(local)) {
            totalStr = "Jami";
        } else {
            totalStr = "Total";
        }

        int lastDepth = 1;
        while (resultSet.next()) {
            List<CellData> cells = new ArrayList<>();
            Map<Integer, ITextRowProperty> properties = new HashMap<>();

            int depth;
            boolean isTotal = false;
            if (sorderColumn != null) {
                depth = resultSet.getInt(columnCount + 1) + 1;
            } else {
                depth = resultSet.getInt(columnCount) + 1;
            }

            if (!Objects.equals(depth, maxDepth) || lastDepth != 1) {
                for (int i = 0; i < depth - 1; i++) {
                    if (i < lastDepth - 1) {
                        String bgColor = "#ececf1";
                        if (i == 1) {
                            bgColor = "#fbfbfc";
                        } else if (i == 2) {
                            bgColor = "#f2f2f5";
                        }
                        cells.add(new CellData("", Element.ALIGN_LEFT, bgColor));
                    } else {
                        String value = resultSet.getString(i + 1);
                        if (value == null) {
                            value = companySettings.getReportingEmptyValueString();
                        }
                        String bgColor = "#fff";
                        if (i == 2) {
                            bgColor = "#f2f2f5";
                        }
                        CellData cellData = new CellData(value);
                        cellData.setBold("normal");
                        cellData.setBackgroundColor(bgColor);
                        cells.add(cellData);
                    }
                }
            } else {
                for (int i = 0; i < depth - 1; i++) {
                    String value = resultSet.getString(i + 1);
                    ColumnRpc columnRpc = reportRpc.getSelectedColumns().get(i);
                    if (value == null) {
                        value = companySettings.getReportingEmptyValueString();
                    }
                    String bgColor = "#ececf1";
                    if (i == 1) {
                        bgColor = "#fbfbfc";
                    } else if (i == 2) {
                        bgColor = "#f2f2f5";
                    }
                    CellData cellData = new CellData(value, getAlignment(columnRpc), bgColor);
                    cellData.setBold("normal");
                    cells.add(cellData);
                }
            }
            int j = 1;

            for (int i = depth; i < columnCount; i++) {
                ITextRowProperty property = new ITextRowProperty();
                String value = resultSet.getString(i);
                ColumnRpc columnRpc = reportRpc.getSelectedColumns().get(i - 1);

                if (j == 1 && !Objects.equals(depth, maxDepth)) {
                    value = totalStr;
                    isTotal = true;
                }
                j++;

                if (value == null || "".equals(value)) {
                    value = companySettings.getReportingEmptyValueString();
                } else {
                    value = Jsoup.parse(value).text();
                    value = value.replace(",", "").replaceAll("(?s)<!--.*?-->", "").replaceAll("<[^>]*>", "").replace("&nbsp;", "\n").replace("&quot;", "\"").replace("&", "&amp;");
                    switch (columnRpc.getColumnFormat()) {
                        case SqlQueryUtil.ColumnFormat_MONEY -> {
                            try {
                                value = numberFormat.format(Double.valueOf(value));
                            } catch (Exception ignored) {
                                value = value;
                            }
                        }
                        case SqlQueryUtil.ColumnFormat_DATE, SqlQueryUtil.ColumnFormat_DATE_WITHOUT_TIME_ZONE, SqlQueryUtil.ColumnFormat_WITHOUT_TIME_ZONE -> {
                            try {
                                if ("short".equals(columnRpc.getCustomDateFormat()) || columnRpc.getCustomDateFormat() == null) {
                                    DateFormat shortDateFormatter = new SimpleDateFormat("yyyy-MM-dd");
                                    Date shortDate = shortDateFormatter.parse(value);
                                    value = shortDateFormat.format(shortDate);
                                } else if ("long".equals(columnRpc.getCustomDateFormat())) {
                                    DateFormat longDateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                    Date longDate = longDateFormatter.parse(value);
                                    value = dateFormat.format(ServerUtils.convertServerDateToUserDate(longDate, user.getUserTimezone()));
                                }
                            } catch (Exception e) {
                                log.error(e.getMessage());
                                value = value;
                            }
                        }
                    }
                }
                CellData cellData;

                if (Objects.equals(depth, maxDepth)) {
                    String bold = isTotal ? "bold" : "normal";
                    String bgColor = isTotal ? "#ececf1" : "#fff";
                    cellData = new CellData(value, getAlignment(columnRpc), bgColor);
                    cellData.setBold(bold);
                } else {
                    if (i == depth) {
                        cellData = new CellData(value.replaceAll("\\$\\{.+}", ""));
                    } else if (summaryColumns.contains(selectedColumns.get(i - 1))) {
                        cellData = new CellData(value);
                    } else {
                        cellData = new CellData("");
                    }

                    String bold = isTotal ? "bold" : "normal";
                    String bgColor = "";
                    if (isTotal) {
                        if (depth == 3) {
                            bgColor = "#f2f2f5";
                        } else if (depth == 2) {
                            bgColor = "fbfbfc";
                        } else if (depth == 1) {
                            bgColor = "#ececf1";
                        }
                    }

                    cellData.setAlignment(getAlignment(columnRpc));
                    cellData.setBold(bold);
                    cellData.setBackgroundColor(bgColor);
                }
                cellData.setType(ITextTableList.CELL_HTML_TEXT);
                cells.add(cellData);
                properties.put(i, property);
            }
            lastDepth = depth;
            tableList.addRowProperty(index, properties);
            CellData[] colArray = new CellData[cells.size()];
            cells.toArray(colArray);
            tableList.addPdfTableRows(colArray);
        }
    }

    private CustomisedITextTable generateSummaryReportMobioCheckPDF(ResultSet
                                                                            resultSet, ArrayList<String> selectedColumns, ArrayList<String> summaryColumns, Integer maxDepth, Integer
                                                                            columnCount, Integer index, ReportRpc reportRpc) throws SQLException {
        resultSet.next();
        CustomisedITextTable table = new CustomisedITextTable();

        String sorderColumn = getColumnByName(reportRpc.getSortTableByColumn(), selectedColumns);
        SimpleDateFormat dateFormat = getCompanyLongDateFormat(userManager.getUser().getCompany());
        SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(userManager.getUser().getCompany());
        EdsUser user = (EdsUser) ServerSecurityContext.getInstance().getUser();
        Locale locale = Locale.ENGLISH;
        if (user != null && user.getCompany() != null && user.getCompany().getLocale() != null) {
            EdsLocale userLocale = localeManager.getLocaleBylanguageCode(user.getCompany().getLocale());
            if (userLocale != null && userLocale.getLanguageCode() != null && userLocale.getCountry() != null) {
                locale = new Locale(userLocale.getLanguageCode(), userLocale.getCountry());
            } else {
                locale = new Locale(user.getCompany().getLocale());
            }
        }
        table.addColumnOrder("DATE", "USER", "UMUMIY_SAVDO", "CASH_OR_TERMINAL", "PUL", "OVERDUE");
        NumberFormat numberFormat = NumberFormat.getInstance(locale);
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);

        while (resultSet.next()) {
            int depth;
            if (sorderColumn != null) {
                depth = resultSet.getInt(columnCount + 1) + 1;
            } else {
                depth = resultSet.getInt(columnCount) + 1;
            }
            String invoicedate = "";
            String creator = "";
            String umumiySavdo = "";
            String cashOrTerminal = "";
            String pul = "";
            String overDue = "";
            boolean isCreator = false;
            boolean isDate = false;

            for (int i = depth; i < columnCount; i++) {
                String value = resultSet.getString(i);
                ColumnRpc columnRpc = reportRpc.getSelectedColumns().get(i - 1);

                if (value == null)
                    value = "";
                value = value.replace(",", "").replaceAll("(?s)<!--.*?-->", "").replaceAll("<[^>]*>", "").replace("&nbsp;", "\n").replace("&quot;", "\"").replace("&", "&amp;");

                if ("invoicedate".equalsIgnoreCase(columnRpc.getName())) {
                    try {
                        if ("short".equals(columnRpc.getCustomDateFormat()) || columnRpc.getCustomDateFormat() == null) {
                            DateFormat shortDateFormatter = new SimpleDateFormat("yyyy-MM-dd");
                            Date shortDate = shortDateFormatter.parse(value);
                            invoicedate = shortDateFormat.format(shortDate);
                        } else if ("long".equals(columnRpc.getCustomDateFormat())) {
                            DateFormat longDateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                            Date longDate = longDateFormatter.parse(value);
                            invoicedate = dateFormat.format(ServerUtils.convertServerDateToUserDate(longDate, user.getUserTimezone()));
                        }
                    } catch (Exception ignored) {
                    }
                    isDate = true;
                }

                if ("creator".equalsIgnoreCase(columnRpc.getName())) {
                    creator = value;
                    isCreator = true;
                }
                if (isCreator) {
                    if ("paymenttotal".equalsIgnoreCase(columnRpc.getName())) {
                        umumiySavdo = numberFormat.format(Double.valueOf(value));
                    }
                }
                if (StrUtils.isEmpty(value) || isDate || isCreator) {
                    continue;
                }
                if ("paymentmethod".equalsIgnoreCase(columnRpc.getName())) {
                    cashOrTerminal = value;
                }
                if ("paymenttotal".equalsIgnoreCase(columnRpc.getName())) {
                    pul = numberFormat.format(Double.valueOf(value));
                }
                if ("duetotal".equalsIgnoreCase(columnRpc.getName())) {
                    overDue = numberFormat.format(Double.valueOf(value));
                }
            }
            table.addRow(invoicedate, creator, umumiySavdo, cashOrTerminal, pul, overDue);
        }
        return table;
    }

    private CustomisedITextTable generateKunlikSavdoCheckPDF(ResultSet
                                                                     resultSet, ArrayList<String> selectedColumns, ArrayList<String> summaryColumns, Integer maxDepth, Integer
                                                                     columnCount, Integer index, ReportRpc reportRpc) throws SQLException {
        resultSet.next();
        CustomisedITextTable table = new CustomisedITextTable();

        String sorderColumn = getColumnByName(reportRpc.getSortTableByColumn(), selectedColumns);
        SimpleDateFormat dateFormat = getCompanyLongDateFormat(userManager.getUser().getCompany());
        SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(userManager.getUser().getCompany());
        EdsUser user = (EdsUser) ServerSecurityContext.getInstance().getUser();
        Locale locale = Locale.ENGLISH;
        if (user != null && user.getCompany() != null && user.getCompany().getLocale() != null) {
            EdsLocale userLocale = localeManager.getLocaleBylanguageCode(user.getCompany().getLocale());
            if (userLocale != null && userLocale.getLanguageCode() != null && userLocale.getCountry() != null) {
                locale = new Locale(userLocale.getLanguageCode(), userLocale.getCountry());
            } else {
                locale = new Locale(user.getCompany().getLocale());
            }
        }
        table.addColumnOrder("USER", "DATE", "TYPE", "PAYMENT_METHOD", "PUL_TC");
        NumberFormat numberFormat = NumberFormat.getInstance(locale);
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);

        int depth, lastGorder = 1;
        while (resultSet.next()) {
            if (sorderColumn != null) {
                depth = resultSet.getInt(columnCount + 1) + 1;
            } else {
                depth = resultSet.getInt(columnCount) + 1;
            }
            String creator = "";
            String invoicedate = "";
            String type = "";
            String cashOrTerminal = "";
            String pulTc = "";
            boolean isNumberTotal = false;

            for (int i = lastGorder; i < columnCount; i++) {
                String value = resultSet.getString(i);
                ColumnRpc columnRpc = reportRpc.getSelectedColumns().get(i - 1);

                if (value == null)
                    value = "";
                value = value.replace(",", "").replaceAll("(?s)<!--.*?-->", "").replaceAll("<[^>]*>", "").replace("&nbsp;", "\n").replace("&quot;", "\"").replace("&", "&amp;");

                if ("invoicedate".equalsIgnoreCase(columnRpc.getName())) {
                    try {
                        if ("short".equals(columnRpc.getCustomDateFormat()) || columnRpc.getCustomDateFormat() == null) {
                            DateFormat shortDateFormatter = new SimpleDateFormat("yyyy-MM-dd");
                            Date shortDate = shortDateFormatter.parse(value);
                            invoicedate = shortDateFormat.format(shortDate);
                        } else if ("long".equals(columnRpc.getCustomDateFormat())) {
                            DateFormat longDateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                            Date longDate = longDateFormatter.parse(value);
                            invoicedate = dateFormat.format(ServerUtils.convertServerDateToUserDate(longDate, user.getUserTimezone()));
                        }
                    } catch (Exception ignored) {
                    }
                }

                if ("creator".equalsIgnoreCase(columnRpc.getName())) {
                    creator = value;
                }

                if ("xtype".equalsIgnoreCase(columnRpc.getName()) && !NumberUtils.isNumber(value.trim())) {
                    type = value;
                }

                if ("paymentmethod".equalsIgnoreCase(columnRpc.getName())) {
                    cashOrTerminal = value;
                    if (NumberUtils.isNumber(value.trim())) {
                        cashOrTerminal = "&#160;";
                        isNumberTotal = true;
                    }
                }

                if ("transactionamount".equalsIgnoreCase(columnRpc.getName())) {
                    ITextGenericPdfData iTextGenericPdfData = new ITextGenericPdfData();
                    pulTc = iTextGenericPdfData.getFormattedAsInt(Double.valueOf(value));
                    if (isNumberTotal) {
                        pulTc = "<b>" + pulTc + "</b>";
                    }
                }
            }
            lastGorder = depth;
            table.addRow(creator, invoicedate, type, cashOrTerminal, pulTc);
        }

        LinkedHashMap<String, HashMap<String, String>> hashMap = table.getRows();
        if (Objects.equals(hashMap.get("2").get("PAYMENT_METHOD"), "&#160;")) {
            hashMap.remove("2");
        } else if (Objects.equals(hashMap.get("3").get("PAYMENT_METHOD"), "&#160;")) {
            hashMap.remove("3");
        }

        int size = hashMap.size() - 1;

        HashMap<String, String> keyToRemove = hashMap.remove(String.valueOf(size));
        table.getRows().values().remove(keyToRemove);

        boolean isBreak = false;
        HashMap<String, String> zeroElement = hashMap.get("0");
        for (Map.Entry<String, HashMap<String, String>> row : table.getRows().entrySet()) {

            for (String rowValue : row.getValue().values()) {
                if (Objects.equals(rowValue.trim(), "Kunlik savdo")) {
                    table.getRows().put("0", hashMap.get(String.valueOf(row.getKey())));
                    table.getRows().put(String.valueOf(row.getKey()), zeroElement);
                    isBreak = true;

                }
            }
            if (isBreak) {
                break;
            }
        }
        return table;
    }

    private String getColumnByName(String columnName, ArrayList<String> columns) {
        if (!StrUtils.isEmpty(columnName)) {
            for (String column : columns) {
                if (columnName.equals(column) || columnName.replace("_", ".").equals(column.replace("_", "."))) {
                    return column;
                }
            }
        }
        return null;
    }

    private int getAlignment(ColumnRpc columnRpc) {
        Integer typeId = typeMap.get(columnRpc.getType());
        typeId = typeId == null ? 0 : typeId;
        switch (typeId) {
            case 1, 2, 3 -> {
                return Element.ALIGN_RIGHT;
            }
            case 4 -> {
                return Element.ALIGN_CENTER;
            }
            default -> {
                return Element.ALIGN_LEFT;
            }
        }
    }

    private void generateTabularReport(ResultSet resultSet, ITextTableList tableList, ReportRpc reportRpc, Integer
            index, ViewRpc viewRpc) throws SQLException {
        int x = 1 + ((viewRpc != null && viewRpc.getHiddenColumnCount() > 0) ? (reportRpc.getSelectedColumns().size() + viewRpc.getHiddenColumnCount()) : reportRpc.getSelectedColumns().size());
        int id = 1 + ((viewRpc != null && viewRpc.getHiddenColumnCount() > 0) ? (viewRpc.getHiddenColumnCount()) : 0);
        resultSet.next();
        EdsCompany company = userManager.getUser().getCompany();
        EdsCompanySettings companySettings = companySettingsManager.getCompanySettings(company.getObjectID());
        SimpleDateFormat dateFormat = getCompanyLongDateFormat(company);
        SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(company);
        EdsUser user = (EdsUser) ServerSecurityContext.getInstance().getUser();
        Locale locale = Locale.ENGLISH;
        if (user != null && user.getCompany() != null && user.getCompany().getLocale() != null) {
            EdsLocale userLocale = localeManager.getLocaleBylanguageCode(user.getCompany().getLocale());
            if (userLocale != null && userLocale.getLanguageCode() != null && userLocale.getCountry() != null) {
                locale = new Locale(userLocale.getLanguageCode(), userLocale.getCountry());
            } else {
                locale = new Locale(user.getCompany().getLocale());
            }
        }
        NumberFormat numberFormat = NumberFormat.getInstance(locale);
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);

        while (resultSet.next()) {
            Map<Integer, ITextRowProperty> properties = new HashMap<>();
            CellData[] colArray = new CellData[x - id];

            ITextRowProperty property = new ITextRowProperty();
            for (int i = id; i < x; i++) {
                String value = resultSet.getString(i);
                if (value == null || value.equals("")) {
                    value = companySettings.getReportingEmptyValueString();
                } else {
                    switch (reportRpc.getSelectedColumns().get(i - id).getColumnFormat()) {
                        case SqlQueryUtil.ColumnFormat_MONEY -> {
                            try {
                                value = numberFormat.format(Double.valueOf(value));
                            } catch (Exception ignored) {
                            }
                        }
                        case SqlQueryUtil.ColumnFormat_DATE, SqlQueryUtil.ColumnFormat_DATE_WITHOUT_TIME_ZONE, SqlQueryUtil.ColumnFormat_WITHOUT_TIME_ZONE -> {
                            try {
                                ColumnRpc columnRpc = reportRpc.getSelectedColumns().get(i - id);

                                if ("short".equals(columnRpc.getCustomDateFormat()) || columnRpc.getCustomDateFormat() == null) {
                                    DateFormat shortDateFormatter = new SimpleDateFormat("yyyy-MM-dd");
                                    Date shortDate = shortDateFormatter.parse(value);
                                    value = shortDateFormat.format(shortDate);
                                } else if ("long".equals(columnRpc.getCustomDateFormat())) {
                                    DateFormat longDateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                    Date longDate = longDateFormatter.parse(value);
                                    value = dateFormat.format(ServerUtils.convertServerDateToUserDate(longDate, user.getUserTimezone()));
                                }
                            } catch (Exception e) {
                                value = value;
                            }
                        }
                    }
                }
                value = Jsoup.parse(value).text();
                CellData cellData = new CellData(value.replace("&", "&amp;"));
                cellData.setType(ITextTableList.CELL_HTML_TEXT);

                cellData.setAlignment(getAlignment(reportRpc.getSelectedColumns().get(i - id)));
                colArray[i - id] = cellData;
                properties.put(i, property);
            }

            tableList.addRowProperty(index, properties);
            tableList.addPdfTableRows(colArray);
        }
    }

    private ITextTableList getHeader(ReportRpc reportRpc) {
        CellData[] headers = new CellData[reportRpc.getSelectedColumns().size()];
        int i = 0;
        for (ColumnRpc columnRpc : reportRpc.getSelectedColumns()) {
            headers[i++] = new CellData(columnRpc.getTitle(), getAlignment(columnRpc));
        }
        ITextTableList tableList = new ITextTableList(headers.length);
        tableList.addPdfTableHeader(headers);
        if (tableList.getNumColumns() > 0) {
            tableList.setTotalWidth(100 / tableList.getNumColumns());
        }
        return tableList;
    }

    ReportRpc wrapReport(ReportRequestObject request) {
        String xmlText = request.getXml();
        if (xmlText == null && request.getObjectID() != null) {
            ReportRpc reportRpc = coreService.getReport(request.getObjectID(), false);
            if (reportRpc != null) {
                RpcConvertToXmlLocal rpcToXml = new RpcConvertToXmlLocal(reportRpc);
                xmlText = rpcToXml.generate();
            }
        }

        XmlParser parser = new XmlParser();
        ReportRpc report = parser.getReportStructure(xmlText);
        report.setForPrint(true);
        return report;
    }

    @Override
    protected Object getDataClass(HttpServletRequest request) {
        return new ReportRequestObject();
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        /*  if (dataClass != null) {
            ReportRequestObject reportRequestObject = (ReportRequestObject) dataClass;
            ReportRpc reportRpc = wrapReport(reportRequestObject);

            setFileName(getFileName() + "_" + clearSpaces(user.getCompany().getName()) + "_" + clearSpaces(reportRpc.getName()) + dateFormat.format(user.getUserDate()));
        }*/
    }

    protected void setFileName(EdsUser user, ReportRpc reportRpc) {
        setFileName(getFileName() + "_" + clearSpaces(user.getCompany().getName()) + "_" + clearSpaces(reportRpc.getName()) + dateFormat.format(user.getUserDate()));

    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.REPORTING_SYSTEM;
    }

    @Override
    protected Integer getUserId(Object object) {
        if (object instanceof ReportRequestObject) {
            return ((ReportRequestObject) object).getUserID();
        }
        return null;
    }

    public String getFileName() {
        return "Report";
    }

    @Override
    protected boolean prepareRequest(HttpServletRequest request) {
        return false;
    }

    String clearSpaces(String s) {
        if ("".equals(s)) {
            return "";
        }
        return s.replace(" ", "_").replace("&", "_").replace("<", "_").replace(">", "_").replace("!", "_").replace("+", "_");
    }

    private Integer getRowCount(Integer reportId) {
        if (reportId != null) {
            ReportRpc report = coreService.getReport(reportId);
            if ((report != null && report.getMaxExcelRowCount() != null && report.getMaxExcelRowCount() > 0)) {
                return report.getMaxExcelRowCount();
            }
        }

        return 64000;
    }

    @Override
    protected String getTableName(Object dataClass) {
        ReportRequestObject reportRequestObject = (ReportRequestObject) dataClass;
        ReportRpc reportRpc = wrapReport(reportRequestObject);
        return (!ServerUtils.isNullOrEmpty(reportRpc.getName()) ? reportRpc.getName() : reportRpc.getViewName()) + "\n\n";
    }
}
