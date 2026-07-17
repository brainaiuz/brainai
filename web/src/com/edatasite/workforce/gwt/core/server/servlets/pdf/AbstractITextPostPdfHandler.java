package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsCompanySystemSettings;
import com.edatasite.workforce.core.domain.EdsCountry;
import com.edatasite.workforce.core.domain.EdsLocation;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.payrolluk.EdsCompanyPayrollSettings;
import com.edatasite.workforce.core.domain.pdf.EdsCompanyPdfTemplate;
import com.edatasite.workforce.core.domain.pdf.EdsPdfDynamicFooterHeader;
import com.edatasite.workforce.core.domain.pdf.EdsPdfFonts;
import com.edatasite.workforce.core.domain.pdf.EdsPdfTemplateSettings;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.enums.PdfGenerateTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DashboardChartsRequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.Utils;
import com.edatasite.workforce.gwt.core.server.db.DynamicFooterHeaderManager;
import com.edatasite.workforce.gwt.core.server.db.PropertManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextFontTypeEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextPdfTemplateEvent;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextTemplateFactory;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.RTLTextReplacedElementFactory;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextCompanyData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextSummaryView;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.PdfParams;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.core.server.utils.WfmJsonUtils;
import com.edatasite.workforce.gwt.profile.client.rpc.PdfFooteHederAttributeEnum;
import com.edatasite.workforce.gwt.profile.client.rpc.PdfFooterHeaderContentItem;
import com.edatasite.workforce.gwt.profile.server.app.PdfTemplateServiceLocal;
import com.edatasite.workforce.mail.EdsTemplate;
import com.edatasite.workforce.utils.EdsContextParams;
import com.finnetlimited.reportservice.core.client.gwtrpc.CoreService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lowagie.text.Anchor;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;
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
import org.apache.logging.log4j.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xhtmlrenderer.pdf.PDFEncryption;
import org.xml.sax.SAXException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Abstract pdf generate class
 * All pdf generate class parent this class
 */
public abstract class AbstractITextPostPdfHandler extends AbstractITextBasePdfHandler {

    Logger logger = LoggerFactory.getLogger(AbstractITextPostPdfHandler.class);

    private PropertyEditorRegistrar[] propertyEditorRegistrars;
    public HttpServletRequest request;
    @Autowired
    private PdfTemplateServiceLocal pdfTemplateServiceLocal;
    @Autowired
    protected CommonService commonService;
    @Autowired
    protected PropertManager propertManager;
    @Autowired
    private DynamicFooterHeaderManager dynamicFooterHeaderManager;
    @Autowired
    private CoreService coreService;


    private Integer currentReportID;

    public  Integer getCurrentReportID() {
        return currentReportID;
    }

    public  void setCurrentReportID(Integer currentReportID) {
        this.currentReportID = currentReportID;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) {
        Object dataClass = getDataClass(request);
        // facet filter setting set and list panel setting
        if (dataClass instanceof ListingFilterParameter) {
            ListingFilterParameter fp = (ListingFilterParameter) dataClass;
            fp.setFacetFilter(WfmJsonUtils.jsonConvertToFacetFilterRpc(fp.getFacetFilterJson()));
            fp.setListPanelTool(WfmJsonUtils.jsonConvertToListPanelToolRpc(fp.getListPanelToolJson()));
        }
        this.request = request;
        try {
            if (dataClass != null && prepareRequest(request)) {
                tryToBind(request, dataClass);
            }
            if (dataClass instanceof RequestObject && ((RequestObject) dataClass).getIds() != null && (!"".equals(((RequestObject) dataClass).getIds())) && ((RequestObject) dataClass).getIds() != null && (!"undefined".equals(((RequestObject) dataClass).getIds()))) {
                LinkedList<String> ids = new LinkedList<>(Arrays.asList(((RequestObject) dataClass).getIds().split(",")));
                ((RequestObject) dataClass).setObjectID(Integer.valueOf(ids.get(0)));
            }
            setFileName(uploadManager.getUser(), dataClass);
            buildPdfMetadataBefore(request, response);
            ByteArrayOutputStream baos = getPdfArrayOutputStream(dataClass);
            buildPdfMetadataAfter(response, baos.size());
            ServletOutputStream out = response.getOutputStream();
            baos.writeTo(out);
            try {
                baos.flush();
                baos.close();
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * Generate Pdf Output Stream
     *
     * @param dataClass
     * @return org.apache.commons.io.ByteArrayOutputStream
     */
    protected ByteArrayOutputStream getPdfArrayOutputStream(Object dataClass) {
        EdsCompany edsCompany;
        if (getUserId(dataClass) != null) {
            edsCompany = userManager.get(getUserId(dataClass)).getCompany();
        } else {
            edsCompany = userManager.getUser().getCompany();
        }
        String pdfType = getPdfCodeName(dataClass) != null ? getPdfCodeName(dataClass).name() : null;
        Integer selectedPdfId = getCustomisedPDFTemplateId(dataClass);

        EdsCompanyPdfTemplate pdfTemplate = companyPdfTemplateManager.getCompanyPdfTemplateByIDOrCode(edsCompany.getObjectID(), pdfType, selectedPdfId);
        if (pdfTemplate != null && !pdfTemplate.getClientPdf()) {
            if (PdfGenerateTypeEnum.PHANTOM_JS.equals(pdfTemplate.getGenerateType())) {
                return phantomGenerateCustomisePdfTemplate(dataClass, edsCompany, pdfTemplate); //customize pdf generate with phantomJS
            } else {
                return generateCustomisePdfTemplate(dataClass, edsCompany, pdfTemplate); //customize pdf generate with itext
            }
        } else if (getPdfCodeName(dataClass) != null && StringUtils.isNotEmpty(getPdfCodeName(dataClass).getUrl())) {
            return generatePDFFromHTML(dataClass, edsCompany, getPdfCodeName(dataClass).getUrl(), pdfTemplate); //default pdf generate with phantomJS
        } else {
            return generateDefaultPdf(dataClass, edsCompany); // default pdf generate with lowagie
        }
    }

    protected PdfParams.Orientation getOrientation(Object dataClass) {
        if (dataClass instanceof ListingFilterParameter) {
            return PdfParams.Orientation.getOrientation(((ListingFilterParameter) dataClass).isLandscape());
        } else if (dataClass instanceof DashboardChartsRequestObject) {
            return PdfParams.Orientation.getOrientation(((DashboardChartsRequestObject) dataClass).getIS_LANDSCAPE());
        }
        return null;
    }

    protected PdfParams getParams(Object dataClass) {
        PdfParams params = new PdfParams();
        params.setOrientation(getOrientation(dataClass));
        return params;
    }

    protected ByteArrayOutputStream generatePDFFromHTML(Object dataClass, EdsCompany edsCompany, String url, EdsCompanyPdfTemplate edsPdfTemplate) {
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

                pdfParams.setCompanyLogoEnabled(edsPdfSettings.getCompanyLogoEnabled()); //MUNIRUPDATED
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
        String pdfType = getPdfCodeName(dataClass) != null ? getPdfCodeName(dataClass).name() : null;
        pdfParams.setTableColumns(pdfTemplateServiceLocal.getPdfTableActiveColumns(pdfId, pdfType));
        pdfParams.setType(pdfType);

        if (dataClass instanceof RequestObject && ((RequestObject) dataClass).getIds() != null && !"".equals(((RequestObject) dataClass).getIds()) && ((RequestObject) dataClass).getIds() != null && !"undefined".equals(((RequestObject) dataClass).getIds())) {
            LinkedList<String> ids;
            RequestObject requestObject = (RequestObject) dataClass;
            ids = new LinkedList<>(Arrays.asList(requestObject.getIds().split(",")));
            int lastOne = 0;
            for (String id : ids) {
                lastOne++;
                requestObject.setObjectID(Integer.valueOf(id));
                pdfData = buildPdfDocumentCustomise(dataClass, edsCompany, true);
                Map<String, String> localizeMap = new HashMap<>();
                localizeMap.put("PAGE_LABEL", pdfWfmMessageSource.localize("page"));
                localizeMap.put("OF_LABEL", pdfWfmMessageSource.localize("of"));
                localizeMap.put("POWERED_BY_LABEL", pdfWfmMessageSource.localize("poweredBy"));
                localizeMap.put("REGISTRATED_OFFICE_LABEL", pdfWfmMessageSource.localize("registratedOffice"));
                localizeMap.put("FAX_LABEL", pdfWfmMessageSource.localize("fax"));
                localizeMap.put("PHONE_LABEL", pdfWfmMessageSource.localize("phone"));
                localizeMap.put("EMAIL_LABEL", pdfWfmMessageSource.localize("email"));
                // localizeMap.put("QR_LABEL", pdfWfmMessageSource.localize("phone"));
                pdfData.setLocalizeMap(localizeMap);
                pdfData.setCompanyData(getCompanyData(edsCompany, true, true));
                if (pdfData.getCurrentDate() == null) {
                    pdfData.setCurrentDate(ServerUtils.shortDateFormat(user.getUserDate(new Date()), user));
                }

                if (getOrientation(dataClass) != null) {
                    pdfParams.setOrientation(getOrientation(dataClass));
                } else if (pdfData.isLandscape()) {
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
                    pdfData.setTableName(getTableName(dataClass));
                }
                String body;
                if(coreService.getCustomHtmlCodeByReportId(currentReportID)!=null){
                    String   customHtmlCode= coreService.getCustomHtmlCodeByReportId(currentReportID);
                    body =generateHtmlContentWithGivenHtml(null,pdfData,customHtmlCode,true);
                }else {
                    body = generateHTMLContentWithUrl(null, pdfData, url, true);}                if (StringUtils.isNotEmpty(body)) {
                    params.add(new BasicNameValuePair("html", lastOne == ids.size() ? body : breakPage(body)));
                }
            }
            String header = generateHTMLContentWithUrl(edsPdfSettings, pdfData, "header.html", false);
            if (StringUtils.isNotEmpty(header)) {
                params.add(new BasicNameValuePair("header", header));
            }
            String footer = generateHTMLContentWithUrl(edsPdfSettings, pdfData, "footer.html", false);
            if (StringUtils.isNotEmpty(footer)) {
                params.add(new BasicNameValuePair("footer", footer));
            }
        } else {
            pdfData = buildPdfDocumentCustomise(dataClass, edsCompany, true);
            Map<String, String> localizeMap = new HashMap<>();
            localizeMap.put("PAGE_LABEL", pdfWfmMessageSource.localize("page"));
            localizeMap.put("OF_LABEL", pdfWfmMessageSource.localize("of"));
            localizeMap.put("POWERED_BY_LABEL", pdfWfmMessageSource.localize("poweredBy"));
            localizeMap.put("REGISTRATED_OFFICE_LABEL", pdfWfmMessageSource.localize("registratedOffice"));
            localizeMap.put("FAX_LABEL", pdfWfmMessageSource.localize("fax"));
            localizeMap.put("PHONE_LABEL", pdfWfmMessageSource.localize("phone"));
            localizeMap.put("EMAIL_LABEL", pdfWfmMessageSource.localize("email"));
            pdfData.setLocalizeMap(localizeMap);
            pdfData.setCompanyData(getCompanyData(edsCompany, true, true));
            if (pdfData.getCurrentDate() == null) {
                pdfData.setCurrentDate(ServerUtils.shortDateFormat(user.getUserDate(new Date()), user));
            }
            if (getOrientation(dataClass) != null) {
                pdfParams.setOrientation(getOrientation(dataClass));
            } else if (pdfData.isLandscape()) {
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
                pdfData.setTableName(getTableName(dataClass));
            }
            String header = generateHTMLContentWithUrl(edsPdfSettings, pdfData, "header.html", false);
            if (StringUtils.isNotEmpty(header)) {
                params.add(new BasicNameValuePair("header", header));
            }
            String footer = generateHTMLContentWithUrl(edsPdfSettings, pdfData, "footer.html", false);
            if (StringUtils.isNotEmpty(footer)) {
                params.add(new BasicNameValuePair("footer", footer));
            }
            String body;
            if(coreService.getCustomHtmlCodeByReportId(currentReportID)!=null){
                String   customHtmlCode= coreService.getCustomHtmlCodeByReportId(currentReportID);
                body =generateHtmlContentWithGivenHtml(null,pdfData,customHtmlCode,true);
            }else {
                body = generateHTMLContentWithUrl(null, pdfData, url, true);}            if (StringUtils.isNotEmpty(body)) {
                params.add(new BasicNameValuePair("html", body));
            }

        }
        return doRequest(pdfData, params);
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
                    item.setContent(item.getContent().replace(str, realValues.get(str) != null ? realValues.get(str) : ""));
                }
            }
            result.add(item);
        }
        return result;
    }

    private HashMap<String, String> getAttributeMapByValue(ITextGenericPdfData pdfData) {
        EdsUser user = userManager.getUser();
        EdsCompany company = user.getCompany();
        ITextCompanyData companyData = pdfData.getCompanyData();

        String companyLogo = "<img alt=\"company logo\" src=\"" + companyData.getCompanyLogoUrl() + "\"" + " style=\"max-width: 100%; max-height: 100%;\"/>";
        String companyName = "<div style=\"vertical-align: top;height: 70px; padding-top: 19px; white-space: nowrap;font-size: $fontSize;color: $fontColor;\"\n" +
                "                width=\"34%\">" +
                "                <strong>" + (!ServerUtils.isNullOrEmpty(company.getName()) ? company.getName() : "") + "</strong> </div>";
        String companyWebsite = "<a href=\"http://" + companyData.getWebsite() + "\">" + companyData.getWebsite() + "</a>";
        String website = EdsContextParams.getWebsite() != null ? EdsContextParams.getWebsite() : "www.kpi.com";
        String poweredBy = "<div style=\"font-size: 7pt; margin-bottom:6px;\"> $poweredByLabel <a href=\"#\"> " + website + "</a></div>";
        String pagination = "<span>$pageLabel <b>{#pageNum}</b> $ofLabel <b>{#numPages}</b></span>";
        String qrCode = "<img alt=\"qr code images\"  src=\"https://workforcetrack.s3.amazonaws.com/000000000000/public/65159/a2113e82-397f-49fd-a58a-cdf4f4b93dff?AWSAccessKeyId=AKIAIROQMC77E5UKWBWQ\" style=\"width: 66px; height: 66px; vertical-align: bottom;\"/>";
        String phoneNumber = !ServerUtils.isNullOrEmpty(company.getPhone()) ? "$phoneLabel: " + company.getPhone() : "";
        String email = !ServerUtils.isNullOrEmpty(company.getEmail()) ? " <a href=\"mailto:" + company.getEmail() + "\">" + company.getEmail() + "</a>" : "";
        String faxNum = !ServerUtils.isNullOrEmpty(company.getFaxNumber()) ? "$faxLabel: " + company.getFaxNumber() : "";
        String address = getCompanyAdress(companyData);
        String locationEmail = !ServerUtils.isNullOrEmpty(user.getLocation() != null ? user.getLocation().getEmail() : "") ? " <a href=\"mailto:" + user.getLocation().getEmail() + "\">" + user.getLocation().getEmail() + "</a>" : "";

        System.out.println("====================== Company logo data ==========================");
        System.out.println(companyLogo);
        System.out.println(companyData.getCompanyLogoUrl());

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
            realValues.put(PdfFooteHederAttributeEnum.USER_LOCATION_EMAIL.getCode(), locationEmail);
            realValues.put(PdfFooteHederAttributeEnum.USER_LOCATION_ZIP_CODE.getCode(), !ServerUtils.isNullOrEmpty(user.getLocation().getZipCode()) ? user.getLocation().getZipCode() : "");
        } else {
            realValues.put(PdfFooteHederAttributeEnum.USER_LOCATION_ADRESS.getCode(), "");
            realValues.put(PdfFooteHederAttributeEnum.USER_LOCATION_PHONE.getCode(), "");
            realValues.put(PdfFooteHederAttributeEnum.USER_LOCATION_EMAIL.getCode(), "");
            realValues.put(PdfFooteHederAttributeEnum.USER_LOCATION_ZIP_CODE.getCode(), "");
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
        return address.toString();
    }

    private String getCompanyAdress(ITextCompanyData companyData) {
        StringBuilder address = new StringBuilder();
        StringBuilder ad = new StringBuilder();
        if (!ServerUtils.isNullOrEmpty(companyData.getAddress())) {
            address.append(getValueWithParagraphTeg(companyData.getAddress()));
        }
        if (!ServerUtils.isNullOrEmpty(companyData.getAddress2())) {
            address.append(companyData.getAddress2()).append(", ");
        }
        if (!ServerUtils.isNullOrEmpty(companyData.getCity())) {
            ad.append(companyData.getCity());
            ad.append(", ");
        }
        if (!ServerUtils.isNullOrEmpty(companyData.getState())) {
            ad.append(companyData.getState());
            ad.append(", ");
        }
//        if (!ServerUtils.isNullOrEmpty(companyData.getPostCode())) {
//            ad.append(companyData.getPostCode());
//            ad.append(", ");
//        }
        if (!ad.toString().isEmpty()) {
            address.append(ad);
        }
        if (!ServerUtils.isNullOrEmpty(companyData.getCountry())) {
            address.append(companyData.getCountry());
        }
        return address.toString();
    }

    private String getValueWithParagraphTeg(String val) {
        StringBuilder result = new StringBuilder();
        result.append(val);
        result.append("<br>");
        return result.toString();
    }


    protected ByteArrayOutputStream phantomGenerateCustomisePdfTemplate(Object dataClass,
                                                                        EdsCompany edsCompany,
                                                                        EdsCompanyPdfTemplate companyPdfTEmplate) {
        if (companyPdfTEmplate.getTemplate() == null) {
            return new ByteArrayOutputStream();
        }
        ITextGenericPdfData pdfData = null;
        List<NameValuePair> params = Lists.newArrayList();
        PdfParams pdfParams = new PdfParams();
        pdfParams.setFormat(companyPdfTEmplate.getPageFormat());
        pdfParams.setOrientation(PdfParams.Orientation.getByCode(companyPdfTEmplate.getOrientation()));
        pdfParams.setHeaderHeight(companyPdfTEmplate.getHeaderHeight());
        pdfParams.setFooterHeight(companyPdfTEmplate.getFooterHeight());
        pdfParams.setMarginTop(companyPdfTEmplate.getMarginTop());
        pdfParams.setMarginLeft(companyPdfTEmplate.getMarginLeft());
        pdfParams.setMarginBottom(companyPdfTEmplate.getMarginBottom());
        pdfParams.setMarginRight(companyPdfTEmplate.getMarginRight());

        if (dataClass instanceof RequestObject && ((RequestObject) dataClass).getIds() != null && !Objects.equals("undefined", ((RequestObject) dataClass).getIds()) && !"".equals(((RequestObject) dataClass).getIds()) && ((RequestObject) dataClass).getIds() != null) {
            LinkedList<String> ids = null;
            RequestObject quoteRequestObject = (RequestObject) dataClass;
            ids = new LinkedList<>(Arrays.asList(quoteRequestObject.getIds().split(",")));

            int lastOne = 0;
            StringBuilder bodyBuilder = new StringBuilder();
            for (String id : ids) {
                lastOne++;
                quoteRequestObject.setObjectID(Integer.valueOf(id));
                pdfData = buildPdfDocumentCustomise(dataClass, edsCompany, true);
                pdfData.setCompanyData(getCompanyData(edsCompany, true, true));
                EdsUser user = uploadManager.getUser();
                if (pdfData.getCurrentDate() == null) {
                    pdfData.setCurrentDate(ServerUtils.shortDateFormat(user.getUserDate(new Date()), user));
                }
                pdfData.setTableName(getTableName(dataClass));
                pdfData.setParams(pdfParams);

                String body = generateHTMLContent(pdfData, companyPdfTEmplate.getTemplate().getContent());
                if (StringUtils.isNotEmpty(body)) {
                    bodyBuilder.append(lastOne == ids.size() ? body : breakPage(body));
                }
            }
            if (StringUtils.isNotEmpty(bodyBuilder)) {
                params.add(new BasicNameValuePair("html", bodyBuilder.toString()));
            }
            String header = generateHTMLContent(pdfData, companyPdfTEmplate.getTemplate().getHeader());
            if (StringUtils.isNotEmpty(header)) {
                params.add(new BasicNameValuePair("header", header));
            }
            String footer = generateHTMLContent(pdfData, companyPdfTEmplate.getTemplate().getFooter());
            if (StringUtils.isNotEmpty(footer)) {
                params.add(new BasicNameValuePair("footer", footer));
            }
        } else {
            pdfData = buildPdfDocumentCustomise(dataClass, edsCompany, true);
            pdfData.setCompanyData(getCompanyData(edsCompany, true, true));
            EdsUser user = uploadManager.getUser();
            if (pdfData.getCurrentDate() == null) {
                pdfData.setCurrentDate(ServerUtils.shortDateFormat(user.getUserDate(new Date()), user));
            }
            pdfData.setTableName(getTableName(dataClass));
            pdfData.setParams(pdfParams);

            String header = generateHTMLContent(pdfData, companyPdfTEmplate.getTemplate().getHeader());
            if (StringUtils.isNotEmpty(header)) {
                params.add(new BasicNameValuePair("header", header));
            }
            String footer = generateHTMLContent(pdfData, companyPdfTEmplate.getTemplate().getFooter());
            if (StringUtils.isNotEmpty(footer)) {
                params.add(new BasicNameValuePair("footer", footer));
            }
            String body = generateHTMLContent(pdfData, companyPdfTEmplate.getTemplate().getContent());
            if (StringUtils.isNotEmpty(body)) {
                params.add(new BasicNameValuePair("html", body));
            }
        }
        return doRequest(pdfData, params);
    }

    private String generateHTMLContent(ITextGenericPdfData pdfData, String html) {
        String result = null;
        EdsTemplate template = new EdsTemplate(html);
        try {
            result = template.process(pdfData);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return result;
    }
    private String generateHtmlContentWithGivenHtml(EdsPdfTemplateSettings pdfTemplate, ITextGenericPdfData pdfData, String customHtmlCode, boolean isBody) {
        String result = null;
        String htmlContent = customHtmlCode;
        if (!isBody) {
            if (pdfTemplate != null) {
                htmlContent = replaceCustomPdfDatas(pdfTemplate.getDynamicFooterHeaders(), htmlContent, pdfData);
            } else {
                htmlContent = replaceCustomPdfDatas(dynamicFooterHeaderManager.getDefaultFooterHeaderValues(), htmlContent, pdfData);
            }
        }
//        System.out.println(url);
//        System.out.println(htmlContent);
        EdsTemplate template = new EdsTemplate(htmlContent);
        try {
            result = template.process(pdfData);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return result;
    }
    private String generateHTMLContentWithUrl(EdsPdfTemplateSettings pdfTemplate, ITextGenericPdfData pdfData, String url, boolean isBody) {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("/template/" + url);
        String result = null;
        String htmlContent = "";
        try {
            htmlContent = IOUtils.toString(is, StandardCharsets.UTF_8);
        } catch (IOException e) {
            logger.error(e.getMessage());
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }
        }
        if (!isBody) {
            if (pdfTemplate != null) {
                htmlContent = replaceCustomPdfDatas(pdfTemplate.getDynamicFooterHeaders(), htmlContent, pdfData);
            } else {
                htmlContent = replaceCustomPdfDatas(dynamicFooterHeaderManager.getDefaultFooterHeaderValues(), htmlContent, pdfData);
            }
        }
//        System.out.println(url);
//        System.out.println(htmlContent);
        EdsTemplate template = new EdsTemplate(htmlContent);
        try {
            result = template.process(pdfData);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return result;
    }

    private ByteArrayOutputStream doRequest(ITextGenericPdfData pdfData, List<NameValuePair> params) {
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
            logger.error(e.getMessage());
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
        return baos;
    }

    private StringBuilder getGenerateAPIUrl(ITextGenericPdfData pdfData) {
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

    /**
     * This method generate default Pdf
     *
     * @param dataClass
     * @param edsCompany
     * @return org.apache.commons.io.ByteArrayOutputStream
     */
    private ByteArrayOutputStream generateDefaultPdf(Object dataClass, EdsCompany edsCompany) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = newDocument(edsCompany, dataClass);
        try {
            PdfWriter pdfWriter = newWriter(document, baos);

            initFooterParams(edsCompany);

            String fontName = getDefaultFont(edsCompany);

            ITextPdfTemplateEvent iTextPdfTemplateEvent;

            boolean fullHeaderAndFooter = edsCompany.getObjectID().equals(56895) || edsCompany.getObjectID().equals(56952);//KENDAH OR AUDING (56952)
            boolean isExtremeCompany = edsCompany.getObjectID().equals(62442);
            if (fullHeaderAndFooter) {
                setShownPaging(false);
                iTextPdfTemplateEvent = new ITextPdfTemplateEvent(getFullHeader(dataClass, edsCompany, pdfWriter, document, fontName),
                        getFullFooter(dataClass, edsCompany, pdfWriter, document, fontName, edsCompany.getObjectID().equals(56895) ? "/pdfimages/kendah_footer.png" : "/pdfimages/auding_footer.png"), getOnEveryPageFooterAndHeader(), false, false, true);
            } else if (isExtremeCompany) {
                document.setMargins(20, 20, 95, 65);
                iTextPdfTemplateEvent = new ITextPdfTemplateEvent(getFullHeader(dataClass, edsCompany, pdfWriter, document, fontName),
                        getFullFooter(dataClass, edsCompany, pdfWriter, document, fontName, "/pdfimages/extreme_footer2.png"), true, false, false, true);
            } else {
                iTextPdfTemplateEvent = new ITextPdfTemplateEvent(getPageHeader(dataClass, edsCompany, pdfWriter, document, fontName),
                        getPageFooter(dataClass, edsCompany, pdfWriter, document, fontName), getOnEveryPageFooterAndHeader(), isShownPaging, getPagingOnTop(), false);
            }
            pdfWriter.setPageEvent(iTextPdfTemplateEvent);

            document.open();

            ITextGenericPdfData pdfDataIText = buildPdfDocument(dataClass, document, pdfWriter);

            setDefaultFont(fontName, pdfDataIText);

            if (pdfDataIText != null) {
                if (pdfDataIText.getTableName() != null) {
                    PdfPTable themaTable = new PdfPTable(1);
                    themaTable.setTotalWidth(document.getPageSize().getWidth());
                    themaTable.getDefaultCell().setBorder(0);
                    themaTable.getDefaultCell().setPaddingTop(15);
                    themaTable.getDefaultCell().setPaddingBottom(pdfDataIText.getExtraData() != null ? 5 : 15);
//                    if (fullHeaderAndFooter)
//                        themaTable.getDefaultCell().setPaddingBottom(30);

                    themaTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                    themaTable.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
                    themaTable.addCell(new Phrase(pdfDataIText.getTableName(), FontFactory.getFont(fontName, BaseFont.IDENTITY_H, pdfDataIText.getNameFontSize(), Font.BOLD)));
                    document.add(themaTable);
                }
                if (pdfDataIText.getExtraData() != null) {
                    PdfPTable themaTable = new PdfPTable(1);
                    themaTable.setTotalWidth(document.getPageSize().getWidth());
                    themaTable.getDefaultCell().setBorder(0);
                    themaTable.getDefaultCell().setPaddingBottom(15);
//                    if (fullHeaderAndFooter)
//                        themaTable.getDefaultCell().setPaddingBottom(30);

                    themaTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                    themaTable.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
                    themaTable.addCell(new Phrase(pdfDataIText.getExtraData(), FontFactory.getFont(fontName, BaseFont.IDENTITY_H, pdfDataIText.getNameFontSize(), Font.BOLD)));
                    document.add(themaTable);
                }
                document.add(ITextTemplateFactory.getPdfElement(pdfDataIText, document));
            }
            document.close();

            // Create a reader
            PdfReader reader = new PdfReader(baos.toByteArray());
            // Create a stamper
            PdfStamper stamper = new PdfStamper(reader, baos);
            if (pdfDataIText.getUserPassword() != null) {
                stamper.setEncryption(pdfDataIText.getUserPassword(), pdfDataIText.getOwnerPassword() != null ? pdfDataIText.getOwnerPassword() : pdfDataIText.getUserPassword(), PdfWriter.ALLOW_PRINTING, PdfWriter.ENCRYPTION_AES_128 | PdfWriter.DO_NOT_ENCRYPT_METADATA);
            }
            // Loop over the pages and add a header to each page
            initPagingAndStamper(reader, stamper, document, iTextPdfTemplateEvent, dataClass);
            // Close the stamper
            stamper.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baos;
    }

    protected void initPagingAndStamper(PdfReader pdfReader, PdfStamper pdfStamper, Document document, ITextPdfTemplateEvent iTextPdfTemplateEvent, Object dataClass) throws DocumentException {
        int n = pdfReader.getNumberOfPages();
        if (n > 1) {
            for (int i = 1; i <= n; i++) {
                getFooterNumberOfPagingTable(i, n, pdfStamper, document, iTextPdfTemplateEvent.getFooter());
            }
        }
    }

    /**
     * <h1>... This is method write to pdf page number of paging ...</h1>
     * <br/>
     * <h2>... Write bi developer {Dilshod.T} ...</h2>
     * <br/>
     * <h3>... Create date {19:40 05/08/2011} ...</h3>
     *
     * @param number
     * @param n
     * @param stamper
     * @param document
     * @param footer
     */
    private void getFooterNumberOfPagingTable(int number, int n, PdfStamper stamper, Document document, PdfPTable footer) {
        if (isShownPaging) {
            Phrase pageCounter = new Phrase(commonLocalizer.localizeWithParam(PdfLocalizationName.pdfPagination, number, n), FontFactory.getFont(ITextFontTypeEnum.TIMES_NEW_ROMAN.getName(), 10, Font.BOLD));
            PdfPTable pageCounterTable = new PdfPTable(1);
            pageCounterTable.setTotalWidth(150);
            pageCounterTable.getDefaultCell().setBorder(0);
            pageCounterTable.setHorizontalAlignment(PdfPTable.ALIGN_RIGHT);
            pageCounterTable.getDefaultCell().setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
            pageCounterTable.addCell(pageCounter);
            int height = 5;
            for (int i = 0; i < footer.size() - 1; i++) {
                height += footer.getRowHeight(i);
            }
            /*if (footer.size() != 0) {
                height += footer.getRow(footer.size() - 1).getCells()[0].getPaddingTop();
            }*/
            pageCounterTable.writeSelectedRows(0, -1, document.right() - 160, document.bottom() - height, stamper.getOverContent(number));
        }
    }

    /**
     * This method generate customize client html template convert to Pdf
     *
     * @param dataClass             Client user interface set data objects
     * @param edsCompany
     * @param edsCompanyPdfTemplate
     * @return org.apache.commons.io.ByteArrayOutputStream
     */
    protected ByteArrayOutputStream generateCustomisePdfTemplate(Object dataClass, EdsCompany edsCompany, EdsCompanyPdfTemplate edsCompanyPdfTemplate) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (dataClass instanceof RequestObject && ((RequestObject) dataClass).getIds() != null && (!"".equals(((RequestObject) dataClass).getIds()))
                && (!"undefined".equals(((RequestObject) dataClass).getIds())) && ((RequestObject) dataClass).getIds() != null) {
            LinkedList<String> ids;
            RequestObject requestObject = (RequestObject) dataClass;
            ids = new LinkedList<>(Arrays.asList(requestObject.getIds().split(",")));
            List<InputStream> inputStreams = new ArrayList<>();
            ITextGenericPdfData itextGenericPdfData = null;
            int lastOne = 0;
            for (String id : ids) {
                lastOne++;
                requestObject.setObjectID(Integer.valueOf(id));
                itextGenericPdfData = buildPdfDocumentCustomise(dataClass, edsCompany, false);
                itextGenericPdfData.setCompanyData(getCompanyData(edsCompany, true, false));
                EdsTemplate template = new EdsTemplate(lastOne == ids.size() ? edsCompanyPdfTemplate.getTemplate().getContent() : breakPage(edsCompanyPdfTemplate.getTemplate().getContent()));
                try {
                    inputStreams.add(new ByteArrayInputStream(template.process(itextGenericPdfData).getBytes(StandardCharsets.UTF_8)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                org.w3c.dom.Document doc = concatDocuments("root", inputStreams.stream().toArray(InputStream[]::new));
                ITextRenderer renderer = new ITextRenderer();
                RTLTextReplacedElementFactory rtlTextReplacedElementFactory = new RTLTextReplacedElementFactory(renderer.getOutputDevice(), "rtldir-arabic;rtldirheader-arabic");
                renderer.getSharedContext().setReplacedElementFactory(rtlTextReplacedElementFactory);
                if (edsCompanyPdfTemplate.getFontFamily() != null && !"".equals(edsCompanyPdfTemplate.getFontFamily())) {
                    renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/" + edsCompanyPdfTemplate.getFontFamily()), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                    if (edsCompanyPdfTemplate.getFontFamily() != null) {
                        if ("calibri.ttf".equals(edsCompanyPdfTemplate.getFontFamily())) {
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/calibrib.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/calibri_italic.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/calibri_bold_italic.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        }
                        if ("arial.ttf".equals(edsCompanyPdfTemplate.getFontFamily())) {
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/arialbd.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/ariali.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/barcode-font.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        }
                        if ("arial_narrow.ttf".equals(edsCompanyPdfTemplate.getFontFamily())) {
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/arial_narrowb.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        }
                        if ("tahoma.ttf".equals(edsCompanyPdfTemplate.getFontFamily())) {
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/thomabd.ttf"), BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/tahoma.ttf"), BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
                        }
                        if ("avant_garde.ttf".equals(edsCompanyPdfTemplate.getFontFamily())) {
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/avant_garde_bold.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/avant_garde_medium.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/avant_garde_italic.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        }
                        if ("arialuni.ttf".equals(edsCompanyPdfTemplate.getFontFamily())) {
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/arialuni.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        }
                        if ("verdana.ttf".equals(edsCompanyPdfTemplate.getFontFamily())) {
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/verdanab.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/verdanai.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/verdanaz.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        }
                        if ("trebuchet_ms.ttf".equals(edsCompanyPdfTemplate.getFontFamily())) {
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/trebucbd.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/trebucbi.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/trebucit.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        }
                        if ("times.ttf".equals(edsCompanyPdfTemplate.getFontFamily())) {
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/timesbd.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/timesi.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/timesbi.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        }
                        if ("dejavusans.ttf".equals(edsCompanyPdfTemplate.getFontFamily())) {
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/dejavusans-bold.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        }
                        if ("source_sans_pro.ttf".equals(edsCompanyPdfTemplate.getFontFamily())) {
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/source_sans_pro_black.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/source_sans_pro_black_italic.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/source_sans_pro_bold.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/source_sans_pro_bold_italic.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/source_sans_pro_extra_light.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/source_sans_pro_extra_light_italic.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/source_sans_pro_italic.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/source_sans_pro_light.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/source_sans_pro_light_italic.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/source_sans_pro_light_semi_bold.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/source_sans_pro_light_semi_bold_italic.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        }
                        if ("arial-rounded-mt-bold.ttf".equals(edsCompanyPdfTemplate.getFontFamily())) {
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/arial-rounded-mt-bold.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        }
                        if ("helvetica.ttf".equals(edsCompanyPdfTemplate.getFontFamily())) {
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/helvetica.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        }
                        if ("GARA.TTF".equals(edsCompanyPdfTemplate.getFontFamily())) {
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/GARA.TTF"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/GARABD.TTF"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/GARAIT.TTF"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        }
                        if ("GOTHIC.TTF".equals(edsCompanyPdfTemplate.getFontFamily())) {
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/GOTHIC.TTF"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/GOTHICB.TTF"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/GOTHICBI.TTF"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/GOTHICI.TTF"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        }
                        if ("Montserrat-Regular.ttf".equals(edsCompanyPdfTemplate.getFontFamily())) {
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/Montserrat-Regular.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/Montserrat-Bold.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/Montserrat-BoldItalic.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/Montserrat-Italic.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        }
                        if ("ManilaSansReg.ttf".equals(edsCompanyPdfTemplate.getFontFamily())) {
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/ManilaSansReg.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/ManilaSansBld.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        }
                        if ("HelveticaNeueLTArabic-Light.ttf".equals(edsCompanyPdfTemplate.getFontFamily())) {
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/HelveticaNeueLTArabic-Light.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/HelveticaNeueLTArabic-Bold.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/HelveticaNeueLTArabic-Roman.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        }
                        if ("GothamLight.ttf".equals(edsCompanyPdfTemplate.getFontFamily())) {
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/GothamLight.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/GothamBold.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/GothamMedium.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/GothamBook.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        }
                        if ("OpenSans-Regular.ttf".equals(edsCompanyPdfTemplate.getFontFamily())) {
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/OpenSans-Regular.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/OpenSans-Italic.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/OpenSans-Bold.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/OpenSans-BoldItalic.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/OpenSans-Semibold.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        }
                        if ("ALSAgrofont-Regular.ttf".equals(edsCompanyPdfTemplate.getFontFamily())) {
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/ALSAgrofont-Regular.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/ALSAgrofont-Bold.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/ALSAgrofont-Medium.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/ALSAgrofont-BoldExpanded.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        }
                    }
                } else {
                    renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/times.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                }
                renderer.setDocument(doc, null);
                renderer.layout();
                if (itextGenericPdfData.getUserPassword() != null) {
                    renderer.setPDFEncryption(new PDFEncryption(itextGenericPdfData.getUserPassword(), itextGenericPdfData.getOwnerPassword() != null ? itextGenericPdfData.getOwnerPassword() : itextGenericPdfData.getUserPassword(), PdfWriter.ALLOW_PRINTING, PdfWriter.ENCRYPTION_AES_128 | PdfWriter.DO_NOT_ENCRYPT_METADATA));
                }
                renderer.createPDF(baos);
            } catch (SAXException | ParserConfigurationException e) {
                logger.error(e.getMessage());
            } catch (Exception e) {
                logger.error(e.getMessage());
            }

        } else {

            ITextGenericPdfData itextGenericPdfData = buildPdfDocumentCustomise(dataClass, edsCompany, false);
            itextGenericPdfData.setCompanyData(getCompanyData(edsCompany, true, false));
            EdsTemplate template = new EdsTemplate(edsCompanyPdfTemplate.getTemplate().getContent());
            try {
                DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                org.w3c.dom.Document doc = builder.parse(new ByteArrayInputStream(template.process(itextGenericPdfData).getBytes(StandardCharsets.UTF_8)));
                ITextRenderer renderer = new ITextRenderer();
                RTLTextReplacedElementFactory rtlTextReplacedElementFactory = new RTLTextReplacedElementFactory(renderer.getOutputDevice(), "rtldir-arabic;rtldirheader-arabic");
                renderer.getSharedContext().setReplacedElementFactory(rtlTextReplacedElementFactory);
                if (edsCompanyPdfTemplate.getFontFamily() != null && !"".equals(edsCompanyPdfTemplate.getFontFamily())) {
                    renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/" + edsCompanyPdfTemplate.getFontFamily()), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                    if (edsCompanyPdfTemplate.getFontFamily() != null) {
                        if ("calibri.ttf".equals(edsCompanyPdfTemplate.getFontFamily())) {
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/calibrib.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/calibri_italic.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/calibri_bold_italic.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        }
                        if ("arial.ttf".equals(edsCompanyPdfTemplate.getFontFamily())) {
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/arialbd.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/ariali.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/barcode-font.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        }
                        if ("arial_narrow.ttf".equals(edsCompanyPdfTemplate.getFontFamily())) {
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/arial_narrowb.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        }
                        if ("tahoma.ttf".equals(edsCompanyPdfTemplate.getFontFamily())) {
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/thomabd.ttf"), BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/tahoma.ttf"), BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
                        }
                        if ("avant_garde.ttf".equals(edsCompanyPdfTemplate.getFontFamily())) {
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/avant_garde_bold.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/avant_garde_medium.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/avant_garde_italic.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        }
                        if ("arialuni.ttf".equals(edsCompanyPdfTemplate.getFontFamily())) {
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/arialuni.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        }
                        if ("verdana.ttf".equals(edsCompanyPdfTemplate.getFontFamily())) {
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/verdanab.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/verdanai.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/verdanaz.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        }
                        if ("trebuchet_ms.ttf".equals(edsCompanyPdfTemplate.getFontFamily())) {
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/trebucbd.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/trebucbi.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/trebucit.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        }
                        if ("times.ttf".equals(edsCompanyPdfTemplate.getFontFamily())) {
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/timesbd.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/timesi.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/timesbi.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        }
                        if ("dejavusans.ttf".equals(edsCompanyPdfTemplate.getFontFamily())) {
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/dejavusans-bold.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        }
                        if ("source_sans_pro.ttf".equals(edsCompanyPdfTemplate.getFontFamily())) {
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/source_sans_pro_black.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/source_sans_pro_black_italic.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/source_sans_pro_bold.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/source_sans_pro_bold_italic.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/source_sans_pro_extra_light.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/source_sans_pro_extra_light_italic.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/source_sans_pro_italic.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/source_sans_pro_light.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/source_sans_pro_light_italic.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/source_sans_pro_light_semi_bold.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/source_sans_pro_light_semi_bold_italic.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        }
                        if ("arial-rounded-mt-bold.ttf".equals(edsCompanyPdfTemplate.getFontFamily())) {
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/arial-rounded-mt-bold.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        }
                        if ("helvetica.ttf".equals(edsCompanyPdfTemplate.getFontFamily())) {
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/helvetica.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        }
                        if ("GARA.TTF".equals(edsCompanyPdfTemplate.getFontFamily())) {
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/GARA.TTF"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/GARABD.TTF"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/GARAIT.TTF"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        }
                        if ("GOTHIC.TTF".equals(edsCompanyPdfTemplate.getFontFamily())) {
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/GOTHIC.TTF"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/GOTHICB.TTF"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/GOTHICBI.TTF"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/GOTHICI.TTF"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        }
                        if ("Montserrat-Regular.ttf".equals(edsCompanyPdfTemplate.getFontFamily())) {
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/Montserrat-Regular.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/Montserrat-Bold.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/Montserrat-BoldItalic.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/Montserrat-Italic.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        }
                        if ("ManilaSansReg.ttf".equals(edsCompanyPdfTemplate.getFontFamily())) {
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/ManilaSansReg.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/ManilaSansBld.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        }
                        if ("HelveticaNeueLTArabic-Light.ttf".equals(edsCompanyPdfTemplate.getFontFamily())) {
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/HelveticaNeueLTArabic-Light.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/HelveticaNeueLTArabic-Bold.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/HelveticaNeueLTArabic-Roman.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        }
                        if ("GothamLight.ttf".equals(edsCompanyPdfTemplate.getFontFamily())) {
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/GothamLight.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/GothamBold.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/GothamMedium.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/GothamBook.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        }
                        if ("OpenSans-Regular.ttf".equals(edsCompanyPdfTemplate.getFontFamily())) {
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/OpenSans-Regular.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/OpenSans-Italic.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/OpenSans-Bold.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/OpenSans-BoldItalic.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/OpenSans-Semibold.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        }
                        if ("ALSAgrofont-Regular.ttf".equals(edsCompanyPdfTemplate.getFontFamily())) {
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/ALSAgrofont-Regular.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/ALSAgrofont-Bold.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/ALSAgrofont-Medium.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                            renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/ALSAgrofont-BoldExpanded.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        }
                    }
                } else {
                    renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/times.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                }
                renderer.setDocument(doc, null);
                renderer.layout();
                if (itextGenericPdfData.getUserPassword() != null) {
                    renderer.setPDFEncryption(new PDFEncryption(itextGenericPdfData.getUserPassword(), itextGenericPdfData.getOwnerPassword() != null ? itextGenericPdfData.getOwnerPassword() : itextGenericPdfData.getUserPassword(), PdfWriter.ALLOW_PRINTING, PdfWriter.ENCRYPTION_AES_128 | PdfWriter.DO_NOT_ENCRYPT_METADATA));
                }
                renderer.createPDF(baos);
            } catch (DocumentException | SAXException | ParserConfigurationException e) {
                logger.error(e.getMessage());
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
        return baos;
    }

    /**
     * return content with break page
     *
     * @param content
     * @return content
     */
    private String breakPage(String content) {
        String stringContent = "";
        String lastLine = "";
        String breakPage = "<p style=\"page-break-after: always;\"></p>\n";
        if (!content.equals("")) {
            stringContent = content.substring(0, content.indexOf("</body>"));
            lastLine = content.substring(content.indexOf("</body>"));
        }
        return stringContent + breakPage + lastLine;
    }

    /**
     * return document for some inputstreams
     *
     * @param rootElementName
     * @param inputStreams
     * @return org.w3c.dom.Document
     */
    public org.w3c.dom.Document concatDocuments(String rootElementName, InputStream... inputStreams) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        org.w3c.dom.Document result = builder.newDocument();
        org.w3c.dom.Element rootElement = result.createElement(rootElementName);
        result.appendChild(rootElement);
        for (InputStream inputStream : inputStreams) {
            org.w3c.dom.Document document = builder.parse(inputStream);
            org.w3c.dom.Element root = document.getDocumentElement();
            NodeList childNodes = root.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node importNode = result.importNode(childNodes.item(i), true);
                rootElement.appendChild(importNode);
            }
        }
        return result;
    }

    /**
     * This is null that handler generate Default Pdf <br/>
     * else not null generate customize velocity Pdf Template
     *
     * @param dataClass
     * @return Pdf Reference Code Name
     */
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return isListingPDF() ? PdfReferenceCodeNameEnum.REPORTING_SYSTEM : null;
    }

    /**
     * If are you want customise pdf that <br/>
     * uses this is method Okey
     *
     * @return com.edatasite.workforce.gwt.core.server.servlets.pdf.ITextGenericPdfData
     */
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        return null;
    }

    /**
     * This is false that handler generate Default Pdf <br/>
     * else true generate customize velocity Pdf Template for Listings
     *
     * @return Pdf Reference Code Name
     */
    protected boolean isListingPDF() {
        return false;
    }

    protected String getTableName(Object dataClass) {
        return "List";
    }


    /**
     * Are you want uses himself header,<br/>
     * that override this method
     * <p/>
     * Create default Pdf page header
     *
     * @return Header
     */
    protected PdfPTable getPageHeader(Object object, EdsCompany edsCompany, PdfWriter pdfWriter, Document document, String fontName) throws DocumentException {
        String companyName = edsCompany.getName();
        String address = edsCompany.getAddress1() != null ? edsCompany.getAddress1() : "";
        String city = edsCompany.getCity() != null ? edsCompany.getCity() : "";
        String postCode = (edsCompany.getPostCode() != null && !"".equals(edsCompany.getPostCode())) ? edsCompany.getPostCode() : "";
        String state = (edsCompany.getCountryRegion() != null) ? edsCompany.getCountryRegion().getName() : "";
        EdsCountry edsCountry = (edsCompany.getCountryZone() != null && edsCompany.getCountryZone().getCountry() != null) ? edsCompany.getCountryZone().getCountry() : null;
        String country = "";
        if (edsCountry != null) {
            country = countryLocalizer.localize(edsCountry.getCode(), edsCountry.getName());
        }
        String cityPostCode = (!"".equals(city) && !"".equals(postCode) ? (city + ", " + postCode) : (!"".equals(city) ? city : postCode));
        Integer pdfFontID = edsCompany.getCompanySettings().getPdfFontID();

        EdsPdfFonts pdfFonts = pdfFontID != null ? companyPdfFontsManager.getPdfFontByID(pdfFontID) : null;
        String default_font = pdfFonts != null ? pdfFonts.getFontName() : ITextFontTypeEnum.ARIAL.getName();

        PdfPTable header = new PdfPTable(2);
        header.getDefaultCell().setBorder(0);
        float width = document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin();
        header.setWidthPercentage(50);
        header.setTotalWidth(width);
        PdfPTable leftHeader = new PdfPTable(1);
        leftHeader.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
        leftHeader.getDefaultCell().setBorder(0);
        leftHeader.getDefaultCell().setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        leftHeader.setTotalWidth((document.getPageSize().getWidth() / 2) - document.leftMargin() - 10);

        Color textColor = null;
        String color = edsCompany.getCompanySettings().getPdfStyleColor();
        if (color != null && !"".equals(color) && color.length() == 6) {
            textColor = Utils.hexToRGB(color);
        } else {
            textColor = Utils.hexToRGB(DEFAULT_FONT_COLOR);
        }

        String imageUrl = null;
        try {
            imageUrl = getPdfLogoUrl(edsCompany, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        PdfPTable rightTable = new PdfPTable(1);
        rightTable.getDefaultCell().setBorder(1);
        rightTable.setTotalWidth(width / 2);
        rightTable.setHorizontalAlignment(PdfPTable.ALIGN_RIGHT);
        rightTable.getDefaultCell().setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        rightTable.getDefaultCell().setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        Image image = null;
        if (imageUrl != null) {
            try {
                image = Image.getInstance(imageUrl);
            } catch (IOException e) {
                image = null;
                logger.error(e.getMessage(), Level.ERROR);
            }
        }
        if (image != null) {

            EdsCompanySettings cs = edsCompany.getCompanySettings();

            if (cs != null && cs.getPdfLogoHeight() != null && cs.getPdfLogoWidth() != null) {
                image.scaleAbsolute(cs.getPdfLogoWidth(), cs.getPdfLogoHeight());
            } else {
                if (image.getWidth() > 240 && image.getHeight() > 60) {
                    float widthScale = image.getWidth() / 240;
                    float heightScale = image.getHeight() / 60;
                    if (widthScale > heightScale) {
                        image.scaleAbsoluteWidth(240);
                        image.scaleAbsoluteHeight(image.getHeight() / widthScale);
                    } else {
                        image.scaleAbsoluteHeight(60);
                        image.scaleAbsoluteWidth(image.getWidth() / heightScale);
                    }
                } else if (image.getWidth() > 240) {
                    image.scaleAbsoluteWidth(240);
                    image.scaleAbsoluteHeight(image.getHeight() * 240 / image.getWidth());
                } else if (image.getHeight() > 60) {
                    image.scaleAbsoluteHeight(60);
                    image.scaleAbsoluteWidth(image.getWidth() * 60 / image.getHeight());
                } else {
                    image.scaleAbsolute((int) (image.getWidth() * 0.8), (int) (image.getHeight() * 0.8));
                }
            }
            Chunk a = new Chunk(image, 0, 0);
            leftHeader.addCell(new Phrase(a));
            header.addCell(leftHeader);
            rightTable.addCell(new Phrase(getTableName(object), FontFactory.getFont(ITextFontTypeEnum.ARIAL.getName(), BaseFont.IDENTITY_H, 14)));
            header.addCell(rightTable);
        }
        PdfPTable headerWithUnderLine = new PdfPTable(1);
        headerWithUnderLine.setTotalWidth(width);
        headerWithUnderLine.getDefaultCell().setBorder(Rectangle.BOTTOM);
        headerWithUnderLine.setSpacingAfter(0);
        headerWithUnderLine.addCell(header);

        return headerWithUnderLine;
    }

    protected PdfPTable getFullHeader(Object object, EdsCompany edsCompany, PdfWriter pdfWriter, Document document, String fontName) throws DocumentException {

        boolean isExtremeExpress = EXTREME_EXPRESS.equals(edsCompany.getObjectID());

        String imageUrl = companyAttachmentManager.getCompanyLogoUrl(edsCompany, FOR_INVOICEPDF, IMAGE_SIZE_SMALL);
        PdfPTable logo = new PdfPTable(1);
        logo.setTotalWidth(document.getPageSize().getWidth());
        logo.setWidthPercentage(95);
        if (edsCompany.getObjectID().equals(56952)) {
            logo.setWidthPercentage(100);
        }
        logo.getDefaultCell().setBorder(0);
        if (isExtremeExpress)
            logo.getDefaultCell().setPadding(0);
        logo.setHorizontalAlignment(PdfPTable.ALIGN_CENTER);
        logo.getDefaultCell().setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        logo.getDefaultCell().setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        if (imageUrl != null) {
            try {
                Image image = Image.getInstance(imageUrl);
                if (isExtremeExpress) {
                    image.scaleAbsoluteWidth(document.getPageSize().getWidth() - 20);
                } else {
                    image.scaleAbsoluteWidth(document.getPageSize().getWidth() - 100);
                }
                image.scaleAbsoluteHeight(image.getHeight() * (document.getPageSize().getWidth() - 100) / image.getWidth());

                Chunk a = new Chunk(image, 0, 0);
                logo.addCell(new Phrase(a));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return logo;
    }

    /**
     * If uses himself logo that <br>
     * Overrride this is method
     *
     * @return Image url
     */
    protected String getPdfLogoUrl(EdsCompany edsCompany, boolean hasPhantom) throws IOException {
        String companyLogoUrl = getCompanyLogoUrl(edsCompany);
        if ((companyLogoUrl == null || "".equals(companyLogoUrl)) && edsCompany.getShowWorkforceLogoOnPDF()) {
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

    protected String getPdfAccountingLogoUrl(EdsCompany edsCompany) throws IOException {
        String invoiceImgUrl = invoiceCircularResolver.getInvoiceLogoUrl(edsCompany);
        if (invoiceImgUrl == null) {
            return null;
        }
//        System.out.println(invoiceImgUrl);
        if (Constants.LOCAL.equals(EdsContextParams.getUploadType())) {
            return invoiceImgUrl;
        }
//        System.out.println(getRealPath(invoiceImgUrl));
        return getRealPath(invoiceImgUrl);
    }


    protected String getPdfStampUrl(EdsCompany edsCompany, String type) throws IOException {
        String stampUrl = getStampUrl(edsCompany, type);
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

    /**
     * Are you want uses himself footer,<br/>
     * that override this method
     * <p/>
     * Create default Pdf page footer
     *
     * @return Footer
     */
    protected PdfPTable getPageFooter(Object object, EdsCompany edsCompany, PdfWriter pdfWriter, Document document, String fontName) throws DocumentException {
        PdfPTable footer;
        if (edsCompany.getShowCertificatePdfFooter()) {
            footer = new PdfPTable(3);
            footer.setWidths(new float[]{commonLocalizer.initializeUserLocale().getLanguage().contains("ru") ? 1.0f : 0.35f, 0.35f, 0.30f});
        } else {
            footer = new PdfPTable(2);
            footer.setWidths(new float[]{commonLocalizer.initializeUserLocale().getLanguage().contains("ru") ? 1.0f : 0.35f, 0.65f});
        }
        footer.getDefaultCell().setNoWrap(true);
        footer.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
        footer.getDefaultCell().setPadding(3);
        footer.getDefaultCell().setBorder(0);
        footer.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        footer.setTotalWidth(235);


        if (isShownWFTFooter) {
            String url = "http://www." + EdsContextParams.getHelpHost();

            Phrase poweredBy = new Phrase(commonLocalizer.localize(PdfLocalizationName.poweredBy), FontFactory.getFont(ITextFontTypeEnum.ARIAL.getName(), BaseFont.IDENTITY_H, 10, Font.BOLD));
            Anchor anchor = new Anchor(url, FontFactory.getFont(ITextFontTypeEnum.ARIAL.getName(), BaseFont.IDENTITY_H, false, 10, Font.NORMAL, Color.BLUE));
            anchor.setReference(url);
            footer.addCell(poweredBy);
            footer.addCell(anchor);
            String imageUrl = "/pdfimages/ISO_9001_logo.jpg";
            if (edsCompany.getShowCertificatePdfFooter()) {
                try {
                    imageUrl = getRealPath(imageUrl);
                    Image image = Image.getInstance(imageUrl);
                    image.scaleAbsolute(30f, 20f);
                    footer.addCell(image);
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }
        return footer;
    }

    /**
     * Are you want uses himself footer,<br/>
     * that override this method
     * <p/>
     * Create default Pdf page footer
     *
     * @return Footer
     */
    protected PdfPTable getFullFooter(Object object, EdsCompany edsCompany, PdfWriter pdfWriter, Document document, String fontName, String imageUrl) throws DocumentException {
        PdfPTable footer;
        footer = new PdfPTable(1);

        footer.getDefaultCell().setNoWrap(true);
        footer.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
        footer.getDefaultCell().setPadding(0);
        footer.getDefaultCell().setBorder(0);
        footer.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        footer.setTotalWidth(document.getPageSize().getWidth());

        try {
            imageUrl = getRealPath(imageUrl);
            Image image = Image.getInstance(imageUrl);

            footer.addCell(image);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return footer;
    }

    public void initFooterParams(EdsCompany edsCompany) {
        if (edsCompany != null) {
            EdsCompanySystemSettings systemSettings = companySystemSettingsManager.findByCompanyID(edsCompany.getObjectID());
            if (systemSettings != null && systemSettings.getShownWFTFooter() != null) {
                setShownWFTFooter(systemSettings.getShownWFTFooter());
            }
        }
    }

    /**
     * WFT footer link shown
     */
    public boolean isShownWFTFooter = true;
    public boolean isShownEmployeeFooter = true;
    /**
     * Paging shown
     */
    public boolean isShownPaging = true;

    public boolean isShownWFTFooter() {
        return isShownWFTFooter;
    }

    public void setShownWFTFooter(boolean shownWFTFooter) {
        isShownWFTFooter = shownWFTFooter;
    }

    public boolean isShownEmployeeFooter() {
        return isShownEmployeeFooter;
    }

    public void setShownEmployeeFooter(boolean isShownEmployeeFooter) {
        this.isShownEmployeeFooter = isShownEmployeeFooter;
    }

    public boolean isShownPaging() {
        return isShownPaging;
    }

    public void setShownPaging(boolean shownPaging) {
        isShownPaging = shownPaging;
    }

    protected boolean getPagingOnTop() {
        return false;
    }

    /**
     * If true pdf pages view header & footer <br/>
     * else View first page header & view footer last page
     *
     * @return
     */
    protected boolean getOnEveryPageFooterAndHeader() {
        return true;
    }

    /**
     * Generates and returns PDF as stream.
     *
     * @param object information that you need to generate pdf.
     * @return outputStream pdf as output stream
     */
    public ByteArrayOutputStream getPDFStream(Object object) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            ByteArrayOutputStream baos = getPdfArrayOutputStream(object);
            outputStream.write(baos.toByteArray());
            try {
                baos.flush();
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return outputStream;
    }

    /**
     * Pdf File Name
     *
     * @return
     */
    public String getFileName() {
        return null;
    }

    /**
     * @param dataClass
     * @param document
     * @param writer
     */
    public abstract ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException;

    /**
     * You should define object for autobinding.
     * All request parameters will be set to this object.
     *
     * @param request
     * @return your object
     */
    protected Object getDataClass(HttpServletRequest request) {
        Map filterMap = request.getParameterMap();
        ListingFilterParameter fp = new ListingFilterParameter();
        HashMap<String, String> paramsMap = fp.getRequestParams();
        Iterator<Map> entries = filterMap.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry entry = (Map.Entry) entries.next();
            if (paramsMap.containsKey(entry.getKey())) {
                String[] value = (String[]) entry.getValue();
                paramsMap.put((String) entry.getKey(), value[0]);
            }
        }
        fp.setRequestParams(paramsMap);
        return fp;
    }

    /**
     * @void setFileName
     */
    protected abstract void setFileName(EdsUser user, Object dataClass);

    /**
     * You can rewrite this method
     * if you want to parse request to take some parametrs.
     *
     * @param request
     * @return return true if you want PostPDFHandler to parse and bind your request.
     */
    protected boolean prepareRequest(HttpServletRequest request) {
        return true;
    }

    /**
     * Get userId by request
     *
     * @param object
     * @return userId or null
     */
    protected Integer getUserId(Object object) {
        return null;
    }

    protected Integer getCustomisedPDFTemplateId(Object object) {
        return null;
    }

    private final ServletRequestDataBinder tryToBind(HttpServletRequest request, Object command) {
        ServletRequestDataBinder binder = createBinder(command);
        binder.bind(request);
        return binder;
    }

    private ServletRequestDataBinder createBinder(Object command) {
        ServletRequestDataBinder binder = new ServletRequestDataBinder(command);
        prepareBinder(binder);
        return binder;
    }

    private final void prepareBinder(ServletRequestDataBinder binder) {
        if (this.propertyEditorRegistrars != null) {
            for (PropertyEditorRegistrar propertyEditorRegistrar : this.propertyEditorRegistrars) {
                propertyEditorRegistrar.registerCustomEditors(binder);
            }
        }
    }

    /**
     * Specify a single PropertyEditorRegistrar to be applied
     * to every DataBinder that this controller uses.
     * <p>Allows for factoring out the registration of PropertyEditors
     * to separate objects, as an alternative to {@link #}.
     *
     * @see #
     */
    public final void setPropertyEditorRegistrars(PropertyEditorRegistrar propertyEditorRegistrar) {
        this.propertyEditorRegistrars = new PropertyEditorRegistrar[]{propertyEditorRegistrar};
    }

    /**
     * Specify multiple PropertyEditorRegistrars to be applied
     * to every DataBinder that this controller uses.
     * <p>Allows for factoring out the registration of PropertyEditors
     * to separate objects, as an alternative to {@link #}.
     *
     * @see #
     */
    public final void setPropertyEditorRegistrars(PropertyEditorRegistrar[] propertyEditorRegistrars) {
        this.propertyEditorRegistrars = propertyEditorRegistrars;
    }

    /**
     * Return the PropertyEditorRegistrars (if any) to be applied
     * to every DataBinder that this controller uses.
     */
    public final PropertyEditorRegistrar[] getPropertyEditorRegistrars() {
        return this.propertyEditorRegistrars;
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

    protected ITextCompanyData getCompanyData(EdsCompany edsCompany, boolean customised, boolean hasPhantom) {
        ITextCompanyData companyData = new ITextCompanyData();
        EdsCompanySettings companySettings = edsCompany.getCompanySettings();
        if (customised) {
            EdsCompanyPayrollSettings companyWebsite = companyPayrollSettingsManager.getCompanySettingValue(Constants.WEBSITE);
            HashMap<String, CustomisedITextTable> customData = new HashMap<>();
            CustomisedITextTable customFieldTable = new CustomisedITextTable();
            companyData.setCompanyName(escapeHtml(edsCompany.getName()));
            companyData.setAddress(edsCompany.getAddress1() != null ? escapeHtml(edsCompany.getAddress1()) : "");
            companyData.setAddress2(edsCompany.getBillAddress2() != null ? escapeHtml(edsCompany.getBillAddress2()) : "");
            companyData.setCity(edsCompany.getCity() != null ? escapeHtml(edsCompany.getCity()) : "");
            companyData.setPostCode((edsCompany.getPostCode() != null && !"".equals(edsCompany.getPostCode())) ? escapeHtml(edsCompany.getPostCode()) : "");
            companyData.setCountry((edsCompany.getCountryZone() != null && edsCompany.getCountryZone().getCountry() != null) ?
                    escapeHtml(edsCompany.getCountryZone().getCountry().getName()) : "");
            companyData.setState(edsCompany.getCountryRegion() != null ? edsCompany.getCountryRegion().getName() : "");
            companyData.setCompanyEmail((edsCompany.getEmail() != null && edsCompany.getEmail().length() > 1 ?
                    (escapeHtml(edsCompany.getEmail())) : ""));
            companyData.setCompanyFax((edsCompany.getFaxNumber() != null && edsCompany.getFaxNumber().length() > 1 ?
                    (escapeHtml(edsCompany.getFaxNumber())) : ""));
            companyData.setCompanyPhone((edsCompany.getPhone() != null && edsCompany.getPhone().length() > 1 ?
                    (escapeHtml(edsCompany.getPhone())) : ""));
            companyData.setWebsite(companyWebsite != null && companyWebsite.getValue() != null ? companyWebsite.getValue() : "");
            customFieldTable.setCustomFields(getCustomFields(companySettings));
            customData.put("CUSTOM_FIELD", customFieldTable);
            companyData.setCustomData(customData);
            companyData.setBaseCurrency(edsCompany.getCurrency() != null ? edsCompany.getCurrency().getName() : "");
            companyData.setBuildingNumber(edsCompany.getBillingAddress() != null ?  edsCompany.getBillingAddress().getBuildingNumber() : "");
            companyData.setPlotIdentification(edsCompany.getBillingAddress() != null ? edsCompany.getBillingAddress().getPlotIdentification() : "");
        } else {
            companyData.setCompanyName(edsCompany.getName());
            companyData.setAddress(edsCompany.getAddress1() != null ? edsCompany.getAddress1() : "");
            companyData.setCity(edsCompany.getCity() != null ? edsCompany.getCity() : "");
            companyData.setPostCode((edsCompany.getPostCode() != null && !"".equals(edsCompany.getPostCode())) ? edsCompany.getPostCode() : "");
            companyData.setCountry((edsCompany.getCountryZone() != null && edsCompany.getCountryZone().getCountry() != null) ? edsCompany.getCountryZone().getCountry().getName() : "");
            companyData.setCompanyEmail((edsCompany.getEmail() != null && edsCompany.getEmail().length() > 1 ? (edsCompany.getEmail()) : ""));
            companyData.setCompanyFax((edsCompany.getFaxNumber() != null && edsCompany.getFaxNumber().length() > 1 ? (edsCompany.getFaxNumber()) : ""));
            companyData.setCompanyPhone((edsCompany.getPhone() != null && edsCompany.getPhone().length() > 1 ? (edsCompany.getPhone()) : ""));
            companyData.setBaseCurrency(edsCompany.getCurrency() != null ? edsCompany.getCurrency().getName() : "");
        }

        try {
            String imageUrl = getPdfLogoUrl(edsCompany, hasPhantom);
            if (imageUrl != null) {
                companyData.setCompanyLogoUrl(imageUrl.replaceAll("[&]", "&amp;"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String url = null;
        try {
            url = getPdfStampUrl(edsCompany, FOR_APPROVE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (url != null) {
            companyData.setApproveStampUrl(url.replaceAll("[&]", "&amp;"));
        }
        try {
            url = getPdfStampUrl(edsCompany, FOR_RECEIVED);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (url != null) {
            companyData.setReceivedStampUrl(url.replaceAll("[&]", "&amp;"));
        }
        try {
            url = getPdfStampUrl(edsCompany, FOR_OVERDUE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (url != null) {
            companyData.setOverdueStampUrl(url.replaceAll("[&]", "&amp;"));
        }
        try {
            url = getPdfStampUrl(edsCompany, FOR_PAID);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (url != null) {
            companyData.setPaidStampUrl(url.replaceAll("[&]", "&amp;"));
        }

        return companyData;
    }

    private Map<String, LinkedHashMap<String, Map<String, String>>> getCustomFields(EdsCompanySettings companySettings) {
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
                            SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(userManager.getUser().getCompany());
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

    public String escapeHtml(String value) {
        if (ServerUtils.isNullOrEmpty(value)) {
            return "";
        }
        return value
                .replace("\u001F", "")
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", " &quot;");
    }

    public String getResultOrLongDash(String value) {
        if (ServerUtils.isNullOrEmpty(value)) {
            return "—";
        }
        return value
                .replace("\u001F", "")
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }

    private void setDefaultFont(String fontName, ITextGenericPdfData pdfDataIText) {
        if (pdfDataIText.getBaseInvoice() != null) {
            pdfDataIText.getBaseInvoice().setFontName(fontName);
        }
        if (pdfDataIText.getListTable() != null) {
            pdfDataIText.getListTable().setFontName(fontName);
        }
        if (pdfDataIText.getSummaryView() != null) {
            pdfDataIText.getSummaryView().setFontName(fontName);
        }
        ITextSummaryView[] summaries = pdfDataIText.getSummaryViewArray();
        if (summaries != null && summaries.length > 0) {
            for (ITextSummaryView summary : summaries) {
                if (summary != null) {
                    summary.setFontName(fontName);
                }
            }
        }
    }

    public String getDefaultFont(EdsCompany company) {
        if (company != null && company.getCompanySettings() != null && company.getCompanySettings().getPdfFontID() != null) {
            EdsPdfFonts font = companyPdfFontsManager.get(company.getCompanySettings().getPdfFontID());
            if (font != null && font.getFontName() != null) {
                return font.getFontName();
            }
        }
        return ITextFontTypeEnum.ARIAL.getName();
    }

    public DecimalFormat getPriceScaleNumberFormat(EdsFinancialSettings fs) {
        if (fs != null && fs.getCalculationScale() != null) {
            return getNewDecimalPoint(fs.getCalculationScale());
        } else {
            return new DecimalFormat(",##0.00");
        }
    }

    public DecimalFormat getDefaultScaleNumberFormat(EdsCompany company, Integer companyPDFTemplateID) {
        EdsCompanyPdfTemplate edsCompanyPdfTemplate = companyPdfTemplateManager.getCompanyPdfTemplateByIDOrCode(company.getObjectID(), getPdfCodeName(null) != null ? getPdfCodeName(null).name() : null, companyPDFTemplateID);
        DecimalFormatSymbols symbols = null;
        if (edsCompanyPdfTemplate != null) {
            symbols = new DecimalFormatSymbols();
            symbols.setDecimalSeparator(parseSeparator(edsCompanyPdfTemplate.getExtendedNumberFormatDecimalSeparator(), '.'));
            symbols.setGroupingSeparator(parseSeparator(edsCompanyPdfTemplate.getExtendedNumberFormatGroupSeparator(), ','));
        }
        return symbols != null ? new DecimalFormat(",##0.00", symbols) : new DecimalFormat(",##0.00");
    }

    public DecimalFormat getPriceScaleNumberFormat(EdsCompany company, Integer companyPDFTemplateID) {

        EdsCompanyPdfTemplate edsCompanyPdfTemplate = companyPdfTemplateManager.getCompanyPdfTemplateByIDOrCode(company.getObjectID(), getPdfCodeName(null) != null ? getPdfCodeName(null).name() : null, companyPDFTemplateID);
        DecimalFormatSymbols symbols = null;
        if (edsCompanyPdfTemplate != null) {
            symbols = new DecimalFormatSymbols();
            symbols.setDecimalSeparator(parseSeparator(edsCompanyPdfTemplate.getExtendedNumberFormatDecimalSeparator(), '.'));
            symbols.setGroupingSeparator(parseSeparator(edsCompanyPdfTemplate.getExtendedNumberFormatGroupSeparator(), ','));
        }

        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        DecimalFormat numberFormat;
        if (fs != null && fs.getCalculationScale() != null) {
            numberFormat = getNewDecimalPoint(fs.getCalculationScale(), symbols);
        } else {
            numberFormat = symbols != null ? new DecimalFormat(",##0.00", symbols) : new DecimalFormat(",##0.00");
        }

        return numberFormat;
    }

    public DecimalFormat getUnitPriceNumberFormat(EdsCompany company, Integer companyPDFTemplateID) {
        DecimalFormat exNumFormat;
        EdsCompanyPdfTemplate edsCompanyPdfTemplate = companyPdfTemplateManager.getCompanyPdfTemplateByIDOrCode(company.getObjectID(), getPdfCodeName(null) != null ? getPdfCodeName(null).name() : null, companyPDFTemplateID);
        if (edsCompanyPdfTemplate != null && edsCompanyPdfTemplate.getExtendedNumberFormat() != null && !"".equals(edsCompanyPdfTemplate.getExtendedNumberFormat().trim())) {
            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setDecimalSeparator(parseSeparator(edsCompanyPdfTemplate.getExtendedNumberFormatDecimalSeparator(), '.'));
            symbols.setGroupingSeparator(parseSeparator(edsCompanyPdfTemplate.getExtendedNumberFormatGroupSeparator(), ','));
            exNumFormat = new DecimalFormat(edsCompanyPdfTemplate.getExtendedNumberFormat(), symbols);
        } else {
            EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
            exNumFormat = new DecimalFormat(",##0.00");
            if (fs != null && fs.getProductPriceScale() != null) {
                exNumFormat = getNewDecimalPoint(fs.getProductPriceScale());
            }
        }
        return exNumFormat;
    }

    public DecimalFormat getQtyNumberFormat(EdsCompany company, Integer companyPDFTemplateID) {
        DecimalFormat numFormat = null;
        EdsCompanyPdfTemplate edsCompanyPdfTemplate = companyPdfTemplateManager.getCompanyPdfTemplateByIDOrCode(company.getObjectID(), getPdfCodeName(null) != null ? getPdfCodeName(null).name() : null, companyPDFTemplateID);
        if (edsCompanyPdfTemplate != null && edsCompanyPdfTemplate.getNumberFormat() != null && !"".equals(edsCompanyPdfTemplate.getNumberFormat().trim())) {
            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setDecimalSeparator(parseSeparator(edsCompanyPdfTemplate.getNumberFormatDecimalSeparator(), '.'));
            symbols.setGroupingSeparator(parseSeparator(edsCompanyPdfTemplate.getNumberFormatGroupSeparator(), ','));
            numFormat = new DecimalFormat(edsCompanyPdfTemplate.getNumberFormat(), symbols);
        } else {
            EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
            numFormat = new DecimalFormat(",##0.00");
            if (fs != null && fs.getProductQuantity() != null) {
                numFormat = getNewDecimalPoint(fs.getProductQuantity());
            }
        }
        return numFormat;
    }


    public DecimalFormat getNewDecimalPoint(Integer productPrice) {
        if (productPrice == 0) {
            return new DecimalFormat(",##0");
        } else {
            String s = ".";
            for (int i = 0; i < productPrice; i++) {
                s = s.concat("0");
            }
            return new DecimalFormat(",##0" + s);
        }
    }

    public DecimalFormat getNewDecimalPoint(Integer productPrice, DecimalFormatSymbols symbols) {
        if (productPrice == 0) {
            return symbols != null ? new DecimalFormat(",##0", symbols) : new DecimalFormat(",##0");
        } else {
            String s = ".";
            for (int i = 0; i < productPrice; i++) {
                s = s.concat("0");
            }
            return symbols != null ? new DecimalFormat(",##0" + s, symbols) : new DecimalFormat(",##0" + s);
        }
    }

    private char parseSeparator(String separator, char def) {
        return (separator != null && !"".equals(separator.trim())) ? separator.trim().charAt(0) : def;
    }

    private static final SimpleDateFormat fpDateParseFormat = new SimpleDateFormat("ddMMyyyy HH:mm:ss");

    public static Date parseFilterParameterDate(String dateAsString) {
        try {
            return (dateAsString != null && !dateAsString.trim().isEmpty()) ? fpDateParseFormat.parse(dateAsString) : null;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void audingPdfFooterSignature(PdfReader pdfReader, PdfStamper pdfStamper, Document document) throws DocumentException {
        EdsUser user = userManager.getUser();
        if (user.getCompany().getObjectID().equals(56952)) {

            int n = pdfReader.getNumberOfPages();
            float width = document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin();
            PdfPTable table = new PdfPTable(5);
            table.setTotalWidth(width);
            PdfPCell cell;
            cell = new PdfPCell(new Phrase(" "));
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            cell.setFixedHeight(70);
            table.addCell(cell);
            table.addCell(new Phrase("  C.E.O. "));
            table.addCell(new Phrase("  Managing Director   "));
            table.addCell(new Phrase("  Accounts  "));
            table.addCell(new Phrase("  Recipient   "));
            float[] columnWidths = new float[]{10f, 30f, 30f, 30f, 30f};
            table.setWidths(columnWidths);
            table.writeSelectedRows(0, -1, 0, document.bottom() + 77, pdfStamper.getOverContent(n));
        }
    }

    public String velocityReplaceContentAttributes(Object object) {
        EdsCompany edsCompany;
        if (getUserId(object) != null) {
            edsCompany = userManager.get(getUserId(object)).getCompany();
        } else {
            edsCompany = userManager.getUser().getCompany();
        }
        EdsCompanyPdfTemplate edsPdfTEmplate = companyPdfTemplateManager.getCompanyPdfTemplateByIDOrCode(edsCompany.getObjectID(),
                getPdfCodeName(null) != null ? getPdfCodeName(null).name() : null,
                getCustomisedPDFTemplateId(object),
                true);
        String html = null;
        if (edsPdfTEmplate != null) {
            try {
                EdsTemplate template = new EdsTemplate(edsPdfTEmplate.getTemplate().getContent());
                ITextGenericPdfData itextGenericPdfData = buildPdfDocumentCustomise(object, edsCompany, false);
                html = template.process(itextGenericPdfData);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return html;
    }
}
