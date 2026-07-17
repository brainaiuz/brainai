package com.finnetlimited.reportservice.core.server;

import com.edatasite.shared.log.KpiLog;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsCompanyAttachment;
import com.edatasite.workforce.core.domain.EdsLocale;
import com.edatasite.workforce.core.domain.EdsLocation;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.payrolluk.EdsCompanyPayrollSettings;
import com.edatasite.workforce.core.domain.pdf.EdsCompanyPdfTemplate;
import com.edatasite.workforce.core.domain.pdf.EdsPdfDynamicFooterHeader;
import com.edatasite.workforce.core.domain.pdf.EdsPdfTemplateSettings;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.core.tools.StringUtil;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.CompanyAttachmentManager;
import com.edatasite.workforce.gwt.core.server.db.DynamicFooterHeaderManager;
import com.edatasite.workforce.gwt.core.server.db.LocaleManager;
import com.edatasite.workforce.gwt.core.server.db.PdfTemplateSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.UploadManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.CompanyPayrollSettingsManager;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PDFConstants;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PdfReferenceCodeNameEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextPdfViewTypeEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextRowProperty;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextCompanyData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.PdfParams;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.profile.client.rpc.PdfFooteHederAttributeEnum;
import com.edatasite.workforce.gwt.profile.client.rpc.PdfFooterHeaderContentItem;
import com.edatasite.workforce.gwt.profile.server.app.PdfTemplateServiceLocal;
import com.edatasite.workforce.gwt.reportingsystem.client.enumerable.ReportType;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ColumnRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportRpc;
import com.edatasite.workforce.mail.EdsTemplate;
import com.edatasite.workforce.utils.EdsContextParams;
import com.finnetlimited.reportservice.core.client.gwtrpc.CoreService;
import com.finnetlimited.reportservice.core.client.gwtrpc.ViewRpc;
import com.finnetlimited.reportservice.core.server.domain.schema.EdsReport;
import com.finnetlimited.reportservice.core.server.handler.PdfReportHandler;
import com.finnetlimited.reportservice.core.server.utils.SqlQueryUtil;
import com.finnetlimited.reportservice.core.server.utils.StrUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.context.support.WfmResourceBundleMessageSource;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static com.edatasite.workforce.gwt.core.client.CommandConstants.FOR_APPROVE;
import static com.edatasite.workforce.gwt.core.client.CommandConstants.FOR_OVERDUE;
import static com.edatasite.workforce.gwt.core.client.CommandConstants.FOR_PAID;
import static com.edatasite.workforce.gwt.core.client.CommandConstants.FOR_PDF;
import static com.edatasite.workforce.gwt.core.client.CommandConstants.FOR_RECEIVED;

@Transactional
public class ReportingRecurrencePdfServiceImpl implements ReportingRecurrencePdfService {
    private static final Logger log = LoggerFactory.getLogger(CoreServiceImpl.class);
    private static final HashMap<String, Integer> typeMap = new HashMap<>();
    protected ApplicationContext applicationContext;
    @Autowired
    protected CommonService commonService;
    @Autowired
    @Qualifier("commonLocalizer")
    protected WfmMessageSource commonLocalizer;
    @Autowired
    protected UploadManager uploadManager;
    @Autowired
    protected PdfTemplateSettingsManager pdfTemplateSettingsManager;
    @Autowired
    protected WfmResourceBundleMessageSource pdfWfmMessageSource;
    @Autowired
    protected CompanyPayrollSettingsManager companyPayrollSettingsManager;
    private final SimpleDateFormat dateFormatForTitle = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    @Autowired
    private CompanyAttachmentManager companyAttachmentManager;
    @Autowired
    @Qualifier("reportingCoreService")
    private CoreService coreService;
    @Autowired
    @Qualifier("reportingCoreService")
    private CoreServiceLocal coreServiceLocal;
    @Autowired
    private LocaleManager localeManager;
    @Autowired
    private PdfTemplateServiceLocal pdfTemplateServiceLocal;
    @Autowired
    private DynamicFooterHeaderManager dynamicFooterHeaderManager;


    public ByteArrayOutputStream generatePDFFromHTML(ReportRpc reportRpc, EdsReport report, EdsCompany company, String url, EdsCompanyPdfTemplate edsPdfTemplate) {
        try {
            EdsUser user = uploadManager.getUser();
            ITextGenericPdfData pdfData = null;
            List<NameValuePair> params = Lists.newArrayList();

            PdfParams pdfParams = new PdfParams();
            EdsPdfTemplateSettings edsPdfSettings = pdfTemplateSettingsManager.getPdfSettings();
            if (edsPdfSettings != null) {
                if (edsPdfSettings.getCustomizedHeader()) {
                    pdfParams.setCompanyLogoEnabled(edsPdfSettings.getCompanyLogoEnabled());
                    pdfParams.setCompanyNameEnabled(edsPdfSettings.getCompanyNameEnabled());
                    pdfParams.setCompanyNameFontSize(StringUtils.defaultString(edsPdfSettings.getCompanyNameFontSize()));
                    pdfParams.setCompanyNameFontColor(StringUtils.defaultString(edsPdfSettings.getCompanyNameFontColor()));
                    pdfParams.setPaginationEnabled(edsPdfSettings.getPaginationEnabled());
                    pdfParams.setDocumentTitleEnabled(edsPdfSettings.getDocumentTitleEnabled());
                    pdfParams.setDocumentTitleFontSize(StringUtils.defaultString(edsPdfSettings.getDocumentTitleFontSize()));
                    pdfParams.setDocumentTitleFontColor(StringUtils.defaultString(edsPdfSettings.getDocumentTitleFontColor()));
                }
                if (edsPdfSettings.getCustomizedContent()) {
                    pdfParams.setTableBorderEnabled(edsPdfSettings.getTableBorderEnabled());
                    pdfParams.setTableBorderColor(edsPdfSettings.getTableBorderColor());
                    pdfParams.setItemRowEnabled(edsPdfSettings.getItemRowEnabled());
                    pdfParams.setItemRowFontSize(edsPdfSettings.getItemRowFontSize());
                    pdfParams.setItemRowBackgroundColorEnabled(edsPdfSettings.getItemRowBackgroundColorEnabled());
                    pdfParams.setItemRowBackgroundColor(edsPdfSettings.getItemRowBackgroundColor());
                    pdfParams.setItemRowFontColor(edsPdfSettings.getItemRowFontColor());
                    pdfParams.setTableHeaderFontSize(edsPdfSettings.getTableHeaderFontSize());
                    pdfParams.setTableHeaderBackgroundColorEnabled(edsPdfSettings.getTableHeaderBackgroundColorEnabled());
                    pdfParams.setTableHeaderBackgroundColor(edsPdfSettings.getTableHeaderBackgroundColor());
                    pdfParams.setTableHeaderFontColor(edsPdfSettings.getTableHeaderFontColor());
                }
                if (edsPdfSettings.getCustomizedFooter()) {
                    pdfParams.setQrCodeEnabled(edsPdfSettings.getQrCodeEnabled());
                    pdfParams.setPoweredByEnabled(edsPdfSettings.getPoweredByEnabled());
                    pdfParams.setCustomAddressEnabled(edsPdfSettings.getCustomAddressEnabled());
                    pdfParams.setCustomAddress(StringUtils.defaultString(edsPdfSettings.getCustomAddress()));
                    pdfParams.setCustomAddressFontSize(StringUtils.defaultString(edsPdfSettings.getCustomAddressFontSize()));
                    pdfParams.setCustomAddressFontColor(StringUtils.defaultString(edsPdfSettings.getCustomAddressFontColor()));
                    pdfParams.setFooterBackgroundColor(StringUtils.defaultString(edsPdfSettings.getFooterBackgroundColor()));
                }
            }
            if (edsPdfTemplate != null && edsPdfTemplate.getClientPdf()) {
                pdfParams.setOrientation(PdfParams.Orientation.getByCode(edsPdfTemplate.getOrientation()));
                pdfParams.setMarginTop(edsPdfTemplate.getMarginTop());
                pdfParams.setMarginLeft(edsPdfTemplate.getMarginLeft());
                pdfParams.setMarginBottom(edsPdfTemplate.getMarginBottom());
                pdfParams.setMarginRight(edsPdfTemplate.getMarginRight());
            } else if (edsPdfSettings != null) {
                pdfParams.setOrientation(PdfParams.Orientation.getByCode(edsPdfSettings.getOrientation()));
                pdfParams.setMarginTop(edsPdfSettings.getMarginTop());
                pdfParams.setMarginLeft(edsPdfSettings.getMarginLeft());
                pdfParams.setMarginBottom(edsPdfSettings.getMarginBottom());
                pdfParams.setMarginRight(edsPdfSettings.getMarginRight());
            }
            Integer pdfId = edsPdfTemplate != null ? edsPdfTemplate.getObjectID() : null;
            String pdfType = PdfReferenceCodeNameEnum.REPORTING_SYSTEM.name();
            pdfParams.setTableColumns(pdfTemplateServiceLocal.getPdfTableActiveColumns(pdfId, pdfType));
            pdfParams.setType(pdfType);


            pdfData = buildPdfDocumentCustomise(reportRpc, report, true, company);
            Map<String, String> localizeMap = new HashMap<>();
            localizeMap.put("PAGE_LABEL", pdfWfmMessageSource.localize("page"));
            localizeMap.put("OF_LABEL", pdfWfmMessageSource.localize("of"));
            localizeMap.put("POWERED_BY_LABEL", pdfWfmMessageSource.localize("poweredBy"));
            localizeMap.put("REGISTRATED_OFFICE_LABEL", pdfWfmMessageSource.localize("registratedOffice"));
            localizeMap.put("FAX_LABEL", pdfWfmMessageSource.localize("fax"));
            localizeMap.put("PHONE_LABEL", pdfWfmMessageSource.localize("phone"));
            pdfData.setLocalizeMap(localizeMap);
            pdfData.setCompanyData(getCompanyData(true, true, company));
            if (pdfData.getCurrentDate() == null) {
                pdfData.setCurrentDate(ServerUtils.shortDateFormat(user.getUserDate(new Date()), user));
            }
            if (pdfData.isLandscape()) {
                pdfParams.setOrientation(PdfParams.Orientation.landscape);
            } else if (edsPdfSettings != null) {
                pdfParams.setOrientation(PdfParams.Orientation.getByCode(edsPdfSettings.getOrientation()));
            } else {
                pdfParams.setOrientation(PdfParams.Orientation.portrait);
            }
            pdfData.setParams(pdfParams);
            if (edsPdfTemplate != null && StringUtils.isNotEmpty(edsPdfTemplate.getDocumentTitle())) {
                pdfData.setTableName(edsPdfTemplate.getDocumentTitle());
            } else {
                pdfData.setTableName(reportRpc.getName());
            }
            String header = generateHTMLContentWithUrl(edsPdfSettings, pdfData, "header.html");
            if (StringUtils.isNotEmpty(header)) {
                params.add(new BasicNameValuePair("header", header));
            }
            String footer = generateHTMLContentWithUrl(edsPdfSettings, pdfData, "footer.html");
            if (StringUtils.isNotEmpty(footer)) {
                params.add(new BasicNameValuePair("footer", footer));
            }
            String body = generateHTMLContentWithUrl(null, pdfData, url);
            if (StringUtils.isNotEmpty(body)) {
                params.add(new BasicNameValuePair("html", body));
            }

            return doRequest(pdfData, params);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERROR WHILE GENERATING PDF");
        }
        return null;
    }

    private String replaceCustomPdfDatas(List<EdsPdfDynamicFooterHeader> footerHeaders, String content, ITextGenericPdfData pdfData) {
        if (footerHeaders != null && footerHeaders.size() > 0) {
            ArrayList<PdfFooterHeaderContentItem> replacedValuMap = getReplacedValueMap(new ArrayList<>(footerHeaders), pdfData);
            for (PdfFooterHeaderContentItem v : replacedValuMap) {
                if (v.getEnable() && v.getContent() != null && !v.getPosition().contains(Constants.DEFAULT_)) {
                    content = content.replace(v.getPosition(), v.getContent());
                } else if (v.getPosition().contains(Constants.DEFAULT_)) {
                    String key = v.getPosition().substring(Constants.DEFAULT_.length());
                    content = content.replace(key, v.getContent());
                } else {
                    content = content.replace(v.getPosition(), "");
                }
            }
        } else {
            content = content.replace(Constants.FOOTER_RIGHT, "");
            content = content.replace(Constants.FOOTER_CENTER, "");
            content = content.replace(Constants.FOOTER_LEFT, "");
            content = content.replace(Constants.HEADER_LEFT, "");
            content = content.replace(Constants.HEADER_CENTER, "");
            content = content.replace(Constants.HEADER_RIGHT, "");
        }
        return content;
    }

    private ArrayList<PdfFooterHeaderContentItem> getReplacedValueMap(ArrayList<EdsPdfDynamicFooterHeader> contentItems, ITextGenericPdfData pdfData) {
        String[] valueCodes = PdfFooteHederAttributeEnum.getCodesAsArray();
        HashMap<String, String> realValues = getAttributeMapByValue(pdfData);
        ArrayList<PdfFooterHeaderContentItem> result = new ArrayList<>();

        for (EdsPdfDynamicFooterHeader fh : contentItems) {
            PdfFooterHeaderContentItem item = new PdfFooterHeaderContentItem(fh.getKey(), ServerUtils.isNullOrEmpty(fh.getValue()) ? "" : fh.getValue(), fh.getEnable() != null && fh.getEnable());
            for (String str : valueCodes) {
                if ((item.getContent().contains(str))) {
                    item.setContent(item.getContent().replace(str, realValues.get(str)));
                }
            }
            result.add(item);
        }
        return result;
    }

    private HashMap<String, String> getAttributeMapByValue(ITextGenericPdfData pdfData) {
        EdsUser user = uploadManager.getUser();
        EdsCompany company = user.getCompany();
        ITextCompanyData companyData = pdfData.getCompanyData();

        String companyLogo = "<img alt=\"company logo\" src=\"" + companyData.getCompanyLogoUrl() + "\"" + " style=\"width: 66px; height: 66px\"/>";
        String companyName = "<div style=\"vertical-align: top;height: 70px; padding-top: 19px; text-align: left;white-space: nowrap;font-size: $fontSize;color: $fontColor;\"\n" +
                "                width=\"34%\">" +
                "                <strong>" + (!ServerUtils.isNullOrEmpty(company.getName()) ? company.getName() : "") + "</strong> </div>";
        String companyWebsite = "<a href=\"http://" + companyData.getWebsite() + "\">" + companyData.getWebsite() + "</a>";
        String poweredBy = "<div style=\"font-size: 7pt; margin-bottom:6px;\"> $poweredByLabel <a href=\"#\">www.kpi.com</a></div>";
        String pagination = "<span>$pageLabel <b>{#pageNum}</b> $ofLabel <b>{#numPages}</b></span>";
        String qrCode = "<img alt=\"images\"  src=\"https://workforcetrack.s3.amazonaws.com/000000000000/public/65159/a2113e82-397f-49fd-a58a-cdf4f4b93dff?AWSAccessKeyId=AKIAIROQMC77E5UKWBWQ\" style=\"width: 60px; height: 60px; margin-right:15px;\"/>";
        String phoneNumber = !ServerUtils.isNullOrEmpty(company.getPhone()) ? "$phoneLabel: " + company.getPhone() : "";
        String email = !ServerUtils.isNullOrEmpty(company.getEmail()) ? "$emailLabel: " + "<a href=\"mailto:" + company.getEmail() + "\">" + company.getEmail() + "</a>" : "";
        String faxNum = !ServerUtils.isNullOrEmpty(company.getFaxNumber()) ? "$faxLabel: " + company.getFaxNumber() : "";
        String address = getCompanyAdress(companyData);

        HashMap<String, String> realValues = new HashMap<>();
        realValues.put(PdfFooteHederAttributeEnum.COMPANY_LOGO.getCode(), companyLogo);
        realValues.put(PdfFooteHederAttributeEnum.COMPANY_NAME.getCode(), companyName);
        realValues.put(PdfFooteHederAttributeEnum.COMPANY_MAIN_ADDRESS.getCode(), address);
        realValues.put(PdfFooteHederAttributeEnum.COMPANY_WEBSITE.getCode(), companyWebsite);
        realValues.put(PdfFooteHederAttributeEnum.POWERED_BY.getCode(), poweredBy);
        realValues.put(PdfFooteHederAttributeEnum.PAGINATION.getCode(), pagination);
        realValues.put(PdfFooteHederAttributeEnum.QR_CODE.getCode(), qrCode);
        realValues.put(PdfFooteHederAttributeEnum.PHONE_NUMBER.getCode(), phoneNumber);
        realValues.put(PdfFooteHederAttributeEnum.EMAIL_ID.getCode(), email);
        realValues.put(PdfFooteHederAttributeEnum.FAX_NUM.getCode(), faxNum);
        realValues.put(PdfFooteHederAttributeEnum.DOCUMENT_TITLE.getCode(), !ServerUtils.isNullOrEmpty(pdfData.getTableName()) ? pdfData.getTableName() : "");
        if (user != null && user.getLocation() != null) {
            realValues.put(PdfFooteHederAttributeEnum.USER_LOCATION_ADRESS.getCode(), getUserLocationAddress(user.getLocation()));
            realValues.put(PdfFooteHederAttributeEnum.USER_LOCATION_PHONE.getCode(), !ServerUtils.isNullOrEmpty(user.getLocation().getPhone()) ? user.getLocation().getPhone() : "");
            realValues.put(PdfFooteHederAttributeEnum.USER_LOCATION_EMAIL.getCode(), !ServerUtils.isNullOrEmpty(user.getLocation().getEmail()) ? user.getLocation().getEmail() : "");
            realValues.put(PdfFooteHederAttributeEnum.USER_LOCATION_ZIP_CODE.getCode(), !ServerUtils.isNullOrEmpty(user.getLocation().getZipCode()) ? user.getLocation().getZipCode() : "");
        }
        return realValues;
    }

    private String getUserLocationAddress(EdsLocation location) {
        StringBuilder address = new StringBuilder();
        if (location.getState() != null && !ServerUtils.isNullOrEmpty(location.getState().getName())) {
            address.append(location.getState().getName());
            address.append(", ");
        }
        if (location.getCityDistrict() != null && !ServerUtils.isNullOrEmpty(location.getCityDistrict().getName())) {
            address.append(location.getCityDistrict().getName());
            address.append(", ");
        }
        if (!ServerUtils.isNullOrEmpty(location.getCity())) {
            address.append(location.getCity());
            address.append(", ");
        }
        if (location.getCountry() != null && !ServerUtils.isNullOrEmpty(location.getCountry().getName())) {
            address.append(location.getCountry().getName());
        }
        return getValueWithParagraphTeg(address.toString());
    }

    private String getCompanyAdress(ITextCompanyData companyData) {
        StringBuilder address = new StringBuilder();
        StringBuilder ad = new StringBuilder();
        if (!ServerUtils.isNullOrEmpty(companyData.getAddress())) {
            address.append(getValueWithParagraphTeg(companyData.getAddress()));
        }
        if (!ServerUtils.isNullOrEmpty(companyData.getAddress2())) {
            address.append(getValueWithParagraphTeg(companyData.getAddress2()));
        }
        if (!ServerUtils.isNullOrEmpty(companyData.getCity())) {
            ad.append(companyData.getCity());
            ad.append(", ");
        }
        if (!ServerUtils.isNullOrEmpty(companyData.getState())) {
            ad.append(companyData.getState());
            ad.append(", ");
        }
        if (!ServerUtils.isNullOrEmpty(companyData.getPostCode())) {
            ad.append(companyData.getPostCode());
            ad.append(", ");
        }
        if (!ad.toString().isEmpty()) {
            address.append(getValueWithParagraphTeg(ad.toString()));
        }
        if (!ServerUtils.isNullOrEmpty(companyData.getCountry())) {
            address.append(getValueWithParagraphTeg(companyData.getCountry()));
        }
        return address.toString();
    }

    private String getValueWithParagraphTeg(String val) {
        StringBuilder result = new StringBuilder();
        result.append(val);
        result.append("<br>");
        return result.toString();
    }

    public ByteArrayOutputStream doRequest(ITextGenericPdfData pdfData, List<NameValuePair> params) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        StringBuilder apiUrl = getGenerateAPIUrl(pdfData);

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(apiUrl.toString());

        try {
            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                InputStream inputStream = entity.getContent();
                try {
                    IOUtils.copy(inputStream, baos);
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                }
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
        return baos;
    }

    public ITextGenericPdfData buildPdfDocumentCustomise(ReportRpc reportRpc, EdsReport report, boolean hasPhantom, EdsCompany company) {
        Integer index = 0;

        reportRpc.setLimit(getRowCount(report));

        ITextGenericPdfData pdfData = new ITextGenericPdfData();

        ITextTableList tableList = generate(reportRpc, index, company);

        PdfParams pdfParams = getParams(reportRpc);
        String templateName = pdfParams.getTemplateName();

        EdsUser user = uploadManager.getUser();

        SimpleDateFormat format = new SimpleDateFormat("MMM dd yyyy", Locale.ENGLISH);
        pdfData.setCurrentDate(format.format(new Date()));
        pdfData.setExtraData(format.format(new Date()));
        pdfData.setPdfViewType(ITextPdfViewTypeEnum.LISTTABLE);
        pdfData.setListTable(tableList);
        ITextCompanyData companyData = getCompanyData(true, hasPhantom, company);
        pdfData.setExtraData(commonLocalizer.localize("createdOn") + ": " + dateFormatForTitle.format(report.getAuditInfo().getCreationDate()));
        pdfData.setCompanyData(companyData);
        pdfData.setLandscape(true);
        HashMap<String, CustomisedITextTable> customData = new HashMap<>();
        pdfData.setCustomData(customData);

        return pdfData;
    }

    public Integer getRowCount(EdsReport report) {
        if ((report != null && report.getMaxExcelRowCount() != null && report.getMaxExcelRowCount() > 0)) {
            return report.getMaxExcelRowCount();
        }
        return 64000;
    }

    public ITextTableList generate(ReportRpc reportRpc, Integer index, EdsCompany company) {
        ITextTableList tableList = getHeader(reportRpc);

        try {
            ResultSet resultSet;
            ViewRpc viewRpc = SqlQueryUtil.getViewParser(reportRpc.getViewCode());
            reportRpc.setNoTimeZone(viewRpc.isNoTimezone());
            viewRpc = ReportType.TABULAR.name().equals(reportRpc.getTableType()) ? viewRpc : null;
            if (reportRpc.getTableType().equals(ReportType.TABULAR.name())) {
                resultSet = coreServiceLocal.getTabularReportResult(reportRpc, null);
                generateTabularReport(resultSet, tableList, index, viewRpc, reportRpc, company);
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
                generateSummaryReport(resultSet, tableList, selectedColumnNames, summaryColumns, reportRpc.getGroupColumns().size() + 1, reportRpc.getSelectedColumns().size() + 1, index, reportRpc, company);
            }
            getFooter(tableList, resultSet, index, viewRpc, reportRpc);

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

    public ITextTableList getHeader(ReportRpc reportRpc) {
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

    public Integer getAlignment(ColumnRpc columnRpc) {
        Integer typeId = typeMap.get(columnRpc.getType());
        typeId = typeId == null ? 0 : typeId;
        switch (typeId) {
            case 1, 2, 3 -> {
                return com.lowagie.text.Element.ALIGN_RIGHT;
            }
            case 4 -> {
                return com.lowagie.text.Element.ALIGN_CENTER;
            }
            default -> {
                return com.lowagie.text.Element.ALIGN_LEFT;
            }
        }
    }

    public void generateTabularReport(ResultSet resultSet, ITextTableList tableList, Integer index, ViewRpc viewRpc, ReportRpc reportRpc, EdsCompany company) throws SQLException {
        int x = 1 + ((viewRpc != null && viewRpc.getHiddenColumnCount() > 0) ? (reportRpc.getSelectedColumns().size() + viewRpc.getHiddenColumnCount()) : reportRpc.getSelectedColumns().size());
        int id = 1 + ((viewRpc != null && viewRpc.getHiddenColumnCount() > 0) ? (viewRpc.getHiddenColumnCount()) : 0);
        resultSet.next();

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
                    value = "n/a";
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
                                e.printStackTrace();
                                value = "";
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

    public SimpleDateFormat getCompanyShortDateFormat(EdsCompany company) {
        SimpleDateFormat shortDateFormat;
        if (company.getCompanySettings() != null && company.getCompanySettings().getShortDateFormat() != null) {
            shortDateFormat = new SimpleDateFormat(company.getCompanySettings().getShortDateFormat());
        } else {
            shortDateFormat = new SimpleDateFormat("MMMM dd, yyyy");
        }
        return shortDateFormat;
    }

    public SimpleDateFormat getCompanyLongDateFormat(EdsCompany company) {
        SimpleDateFormat shortDateFormat;
        if (company.getCompanySettings() != null && company.getCompanySettings().getLongDateFormat() != null) {
            shortDateFormat = new SimpleDateFormat(company.getCompanySettings().getLongDateFormat());
        } else {
            shortDateFormat = new SimpleDateFormat("MMMM dd, yyyy");
        }
        return shortDateFormat;
    }

    public void generateSummaryReport(ResultSet resultSet, ITextTableList tableList, ArrayList<String> selectedColumns, ArrayList<String> summaryColumns, Integer maxDepth, Integer columnCount, Integer index, ReportRpc reportRpc, EdsCompany company) throws SQLException {
        resultSet.next();
        String sorderColumn = getColumnByName(reportRpc.getSortTableByColumn(), selectedColumns);
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
            totalStr = "Итого";
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
                        cells.add(new CellData("", com.lowagie.text.Element.ALIGN_LEFT, bgColor));
                    } else {
                        String value = resultSet.getString(i + 1);
                        if (value == null) {
                            value = "n/a";
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
                        value = "n/a";
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
                if (value == null) {
                    value = "n/a";
                }

                if (j == 1 && !Objects.equals(depth, maxDepth)) {
                    value = totalStr;
                    isTotal = true;
                }
                j++;

                value = Jsoup.parse(value).text();
                value = value.replace(",", "").replaceAll("(?s)<!--.*?-->", "").replaceAll("<[^>]*>", "").replace("&nbsp;", "\n").replace("&quot;", "\"").replace("&", "&amp;");
                switch (columnRpc.getColumnFormat()) {
                    case SqlQueryUtil.ColumnFormat_MONEY -> {
                        try {
                            value = numberFormat.format(Double.valueOf(value));
                        } catch (Exception ignored) {

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
                        } catch (Exception ignored) {
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

    public String getColumnByName(String columnName, ArrayList<String> columns) {
        if (!StrUtils.isEmpty(columnName)) {
            for (String column : columns) {
                if (columnName.equals(column) || columnName.replace("_", ".").equals(column.replace("_", "."))) {
                    return column;
                }
            }
        }
        return null;
    }

    public void getFooter(ITextTableList tableList, ResultSet resultSet, Integer index, ViewRpc viewRpc, ReportRpc reportRpc) throws SQLException {
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
                CellData cellData = new CellData(totalGrand, com.lowagie.text.Element.ALIGN_LEFT, bgColor);
                cells[0] = cellData;
            }

            for (int i = 2 + id; i <= reportRpc.getSelectedColumns().size() + id; i++) {
                String value = "";
                if (reportRpc.getSumaries().contains(reportRpc.getSelectedColumns().get(i - 1 - id))) {
                    value = resultSet.getString(i);
                    if (value != null) {
                        value = value.replaceAll("(?s)<!--.*?-->", "").replaceAll("<[^>]*>", "").replace("&nbsp;", "\n").replace("&quot;", "\"").replaceAll("- {2}#", "");
                    }
                }
                ITextRowProperty property = new ITextRowProperty();
                property.setBackgroundColor("#FAC743");
                CellData cellData = new CellData(value, com.lowagie.text.Element.ALIGN_RIGHT);
                cells[i - (2 + id) + 1] = cellData;
            }
            tableList.addRowProperty(index, properties);
            tableList.addPdfTableRows(cells);
        }
    }

    public PdfParams getParams(ReportRpc reportRpc) {
        PdfParams params = new PdfParams();
        params.setOrientation(PdfParams.Orientation.getOrientation(reportRpc.isLandscape() != null ? reportRpc.isLandscape() : true));
        params.setTemplateName(reportRpc.getViewName());
        params.setHeaderHeight("300px");
        params.setFooterHeight("240px");
        return params;
    }

    public ITextCompanyData getCompanyData(boolean customised, boolean hasPhantom, EdsCompany company) {
        ITextCompanyData companyData = new ITextCompanyData();
        EdsCompanySettings companySettings = company.getCompanySettings();
        if (customised) {
            EdsCompanyPayrollSettings companyWebsite = companyPayrollSettingsManager.getCompanySettingValue(Constants.WEBSITE);
            HashMap<String, CustomisedITextTable> customData = new HashMap<>();
            CustomisedITextTable customFieldTable = new CustomisedITextTable();
            companyData.setCompanyName(escapeHtml(company.getName()));
            companyData.setAddress(company.getAddress1() != null ? escapeHtml(company.getAddress1()) : "");
            companyData.setAddress2(company.getBillAddress2() != null ? escapeHtml(company.getBillAddress2()) : "");
            companyData.setCity(company.getCity() != null ? escapeHtml(company.getCity()) : "");
            companyData.setPostCode((company.getPostCode() != null && !"".equals(company.getPostCode())) ? escapeHtml(company.getPostCode()) : "");
            companyData.setCountry((company.getCountryZone() != null && company.getCountryZone().getCountry() != null) ?
                    escapeHtml(company.getCountryZone().getCountry().getName()) : "");
            companyData.setState(company.getCountryRegion() != null ? company.getCountryRegion().getName() : "");
            companyData.setCompanyEmail((company.getEmail() != null && company.getEmail().length() > 1 ?
                    (escapeHtml(company.getEmail())) : ""));
            companyData.setCompanyFax((company.getFaxNumber() != null && company.getFaxNumber().length() > 1 ?
                    (escapeHtml(company.getFaxNumber())) : ""));
            companyData.setCompanyPhone((company.getPhone() != null && company.getPhone().length() > 1 ?
                    (escapeHtml(company.getPhone())) : ""));
            companyData.setWebsite(companyWebsite != null && companyWebsite.getValue() != null ? companyWebsite.getValue() : "");
            customFieldTable.setCustomFields(getCustomFields(companySettings, company));
            customData.put("CUSTOM_FIELD", customFieldTable);
            companyData.setCustomData(customData);
        } else {
            companyData.setCompanyName(company.getName());
            companyData.setAddress(company.getAddress1() != null ? company.getAddress1() : "");
            companyData.setCity(company.getCity() != null ? company.getCity() : "");
            companyData.setPostCode((company.getPostCode() != null && !"".equals(company.getPostCode())) ? company.getPostCode() : "");
            companyData.setCountry((company.getCountryZone() != null && company.getCountryZone().getCountry() != null) ? company.getCountryZone().getCountry().getName() : "");
            companyData.setCompanyEmail((company.getEmail() != null && company.getEmail().length() > 1 ? (company.getEmail()) : ""));
            companyData.setCompanyFax((company.getFaxNumber() != null && company.getFaxNumber().length() > 1 ? (company.getFaxNumber()) : ""));
            companyData.setCompanyPhone((company.getPhone() != null && company.getPhone().length() > 1 ? (company.getPhone()) : ""));
        }

        try {
            String imageUrl = getPdfLogoUrl(hasPhantom, company);
            if (imageUrl != null) {
                companyData.setCompanyLogoUrl(imageUrl.replaceAll("[&]", "&amp;"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String url = null;
        try {
            url = getPdfStampUrl(FOR_APPROVE, company);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (url != null) {
            companyData.setApproveStampUrl(url.replaceAll("[&]", "&amp;"));
        }
        try {
            url = getPdfStampUrl(FOR_RECEIVED, company);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (url != null) {
            companyData.setReceivedStampUrl(url.replaceAll("[&]", "&amp;"));
        }
        try {
            url = getPdfStampUrl(FOR_OVERDUE, company);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (url != null) {
            companyData.setOverdueStampUrl(url.replaceAll("[&]", "&amp;"));
        }
        try {
            url = getPdfStampUrl(FOR_PAID, company);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (url != null) {
            companyData.setPaidStampUrl(url.replaceAll("[&]", "&amp;"));
        }
        return companyData;
    }

    public String escapeHtml(String value) {
        if (ServerUtils.isNullOrEmpty(value)) {
            return "";
        }
        return value
                .replace("\u001F", "")
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }

    public Map<String, LinkedHashMap<String, Map<String, String>>> getCustomFields(EdsCompanySettings companySettings, EdsCompany company) {
        Map<String, LinkedHashMap<String, Map<String, String>>> customFields = new HashMap<>();
        if (companySettings != null && companySettings.getCompanySettingsCustomFields() != null) {
            List<CompanyCustomFieldItem> customFieldItems = CustomFieldsUtils.setRPCCustomFieldItems(companySettings.getCompanySettingsCustomFields(),
                    commonService.getCompanyCustomFields(ViewName.CompanySettings));
            if (customFieldItems != null && customFieldItems.size() > 0) {
                LinkedHashMap<String, Map<String, String>> itemCusFields = new LinkedHashMap<>();
                for (CompanyCustomFieldItem item : customFieldItems) {
                    if (item != null) {
                        Map<String, String> cols = new HashMap<>();
                        cols.put(PDFConstants.COLUMN_NAME, item.getFieldName() != null ? escapeHtml(item.getFieldName()) : "");
                        if (CompanyCustomFieldItem.DATE.equals(item.getDataType())) {
                            SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(company);
                            cols.put(PDFConstants.COLUMN_VALUE, item.getFieldDateNonConvertedValue() != null ? escapeHtml(shortDateFormat.format(item.getFieldDateNonConvertedValue().getNonConvertedDate())) : "");
                        } else {
                            cols.put(PDFConstants.COLUMN_VALUE, item.getFieldStringValue() != null ? escapeHtml(item.getFieldStringValue()) : "");
                        }
                        if (item.getFieldName() != null) {
                            itemCusFields.put(escapeHtml(item.getFieldName()), cols);
                        }
                    }
                }
                customFields.put("SETTINGS", itemCusFields);
            }
        }
        return customFields;
    }

    public String getPdfLogoUrl(boolean hasPhantom, EdsCompany company) throws IOException {
        String companyLogoUrl = getCompanyLogoUrl(company);
        if ((companyLogoUrl == null || "".equals(companyLogoUrl)) && company.getShowWorkforceLogoOnPDF()) {
            if (hasPhantom) {
                String pdfLogoUrl = EdsContextParams.getPdfLogo();
                if (pdfLogoUrl.startsWith("/")) {
                    pdfLogoUrl = pdfLogoUrl.substring(1);
                }
                String fullHost = EdsContextParams.getFullHost();
                if (fullHost.contains("localhost")) {
                    fullHost = "https://apps.kpi.com/";
                }
                companyLogoUrl = fullHost + pdfLogoUrl;
            } else {
                companyLogoUrl = getRealPath(EdsContextParams.getPdfLogo());
            }
        }
        return companyLogoUrl;
    }

    public String getCompanyLogoUrl(EdsCompany company) {
        String url = null;

        if (company != null) {
            url = companyAttachmentManager.getCompanyLogoUrl(company, FOR_PDF);
            if (StringUtil.isEmpty(url) && Constants.LOCAL.equals(EdsContextParams.getUploadType())) {
                SelectItem item = companyAttachmentManager.getCompanyLogo(company, FOR_PDF);
                if (item != null) {
                    EdsCompanyAttachment logo = companyAttachmentManager.get(item.getId());
                    url = logo.getLocalPath() + logo.getObjectID();
                }
            }

        }
        return url;
    }

    public String getRealPath(String path) throws IOException {
        if (applicationContext != null) {
            return applicationContext.getResource(path).getURL().toString();
        }
        return "";
    }

    public String getPdfStampUrl(String type, EdsCompany company) throws IOException {
        String stampUrl = getStampUrl(type, company);
        if (stampUrl == null || "".equals(stampUrl)) {
            stampUrl = switch (type) {
                case FOR_APPROVE -> getRealPath("/pdfimages/approved.png");
                case FOR_RECEIVED -> getRealPath("/pdfimages/received.png");
                case FOR_OVERDUE -> getRealPath("/pdfimages/overdue.png");
                default -> getRealPath("/pdfimages/paid.png");
            };
        }

        return stampUrl;
    }

    public String getStampUrl(String type, EdsCompany company) throws IOException {
        String url = null;

        if (company != null) {
            url = companyAttachmentManager.getCompanyStampUrl(company, type);
        }
        return url;
    }

    public String generateHTMLContentWithUrl(EdsPdfTemplateSettings pdfTemplate, ITextGenericPdfData pdfData, String url) {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("/template/" + url);
        String result = null;
        String htmlContent = "";
        try {
            htmlContent = IOUtils.toString(is, StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
        }
        if (pdfTemplate != null) {
            htmlContent = replaceCustomPdfDatas(pdfTemplate.getDynamicFooterHeaders(), htmlContent, pdfData);
        } else {
            htmlContent = replaceCustomPdfDatas(dynamicFooterHeaderManager.getDefaultFooterHeaderValues(), htmlContent, pdfData);
        }
        EdsTemplate template = new EdsTemplate(htmlContent);
        try {
            result = template.process(pdfData);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return result;
    }

    public StringBuilder getGenerateAPIUrl(ITextGenericPdfData pdfData) {
        Map<String, String> queryParams = Maps.newHashMap();
        if (pdfData.getParams() != null) {
            PdfParams params = pdfData.getParams();
            if (StringUtils.isNotEmpty(params.getFormat())) {
                queryParams.put("format", params.getFormat());
            }
            if (params.getOrientation() != null) {
                queryParams.put("orientation", params.getOrientation().name());
            }
            if (StringUtils.isNotEmpty(params.getHeaderHeight())) {
                queryParams.put("headerHeight", params.getHeaderHeight());
            }
            if (StringUtils.isNotEmpty(params.getFooterHeight())) {
                queryParams.put("footerHeight", params.getFooterHeight());
            }
            if (StringUtils.isNotEmpty(params.getMarginTop())) {
                queryParams.put("marginTop", params.getMarginTop());
            }
            if (StringUtils.isNotEmpty(params.getMarginRight())) {
                queryParams.put("marginRight", params.getMarginRight());
            }
            if (StringUtils.isNotEmpty(params.getMarginBottom())) {
                queryParams.put("marginBottom", params.getMarginBottom());
            }
            if (StringUtils.isNotEmpty(params.getMarginLeft())) {
                queryParams.put("marginLeft", params.getMarginLeft());
            }
        }

        StringBuilder apiUrl = new StringBuilder("https://awspdf.kpi.com/convert/pdf");
        if (!queryParams.isEmpty()) {
            boolean hasFirstMark = true;
            for (Map.Entry<String, String> entry : queryParams.entrySet()) {
                if (StringUtils.isEmpty(entry.getValue())) {
                    continue;
                }
                if (hasFirstMark) {
                    apiUrl.append("?");
                    hasFirstMark = false;
                } else {
                    apiUrl.append("&");
                }
                apiUrl.append(entry.getKey());
                apiUrl.append("=");
                apiUrl.append(entry.getValue());
            }
        }
        return apiUrl;
    }
}
