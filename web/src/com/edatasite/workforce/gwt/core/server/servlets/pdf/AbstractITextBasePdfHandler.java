package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsCompanyAttachment;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.tools.StringUtil;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.Utils;
import com.edatasite.workforce.gwt.core.server.db.*;
import com.edatasite.workforce.gwt.core.server.db.payroll.CompanyPayrollSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.settings.CompanySettingsManager;
import com.edatasite.workforce.gwt.documents.client.rpc.DocumentsService;
import com.edatasite.workforce.gwt.documents.server.app.DocumentsServiceLocal;
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceCircularResolver;
import com.edatasite.workforce.utils.EdsContextParams;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.context.support.WfmResourceBundleMessageSource;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Date;

/**
 * This is Abstract Base generic PDF class
 * Uses IText pdf generate libriry
 */
public abstract class AbstractITextBasePdfHandler implements HttpRequestHandler, Constants, CommandConstants, ApplicationContextAware {

    protected static final String PAYPAL_LOGO_URL = "/pdfimages/paypal.gif";
    protected static final String GOOGLE_CHECKOUT_LOGO_URL = "/pdfimages/google-checkout.gif";
    protected static final String MASTERCARD_LOGO_URL = "/pdfimages/mastercard.gif";
    public static final int DEFAULT_FONT_SIZE = 8;


    protected ApplicationContext applicationContext;
    @Autowired
    protected CompanyAttachmentManager companyAttachmentManager;
    @Autowired
    protected UploadManager uploadManager;
    @Autowired
    protected ReferenceManager referenceManager;
    @Autowired
    protected UserManager userManager;
    @Autowired
    protected WfmResourceBundleMessageSource pdfWfmMessageSource;
    @Autowired
    protected CompanyPdfTemplateManager companyPdfTemplateManager;
    @Autowired
    protected CompanyPdfFontsManager companyPdfFontsManager;
    @Autowired
    protected CompanySystemSettingsManager companySystemSettingsManager;
    @Autowired
    protected CompanySettingsManager companySettingsManager;
    @Autowired
    protected InvoiceCircularResolver invoiceCircularResolver;
    @Autowired
    protected DocumentsService documentsService;
    @Autowired
    protected DocumentsServiceLocal documentsServiceLocal;


    protected String fileName = "wfm";
    private final String downloadType = PDFDownloadType.INLINE;
    protected static final int LIMIT_PDF_ROWS = 1000;
    protected Property property;
    @Autowired
    @Qualifier("commonLocalizer")
    protected WfmMessageSource commonLocalizer;
    @Autowired
    @Qualifier("crmLocalizer")
    protected WfmMessageSource crmLocalizer;
    @Autowired
    @Qualifier("hrmsLocalizer")
    protected WfmMessageSource hrmsLocalizer;
    @Autowired
    @Qualifier("availabilityLocalizer")
    protected WfmMessageSource availabilityLocalizer;
    @Autowired
    @Qualifier("countryLocalizer")
    protected WfmMessageSource countryLocalizer;
    @Autowired
    @Qualifier("accountingLocalizer")
    protected WfmMessageSource accountingLocalizer;
    @Autowired
    @Qualifier("dashboardLocalizer")
    protected WfmMessageSource dashboardLocalizer;

    @Autowired
    @Qualifier("referenceWfmMessageSource")
    protected WfmMessageSource referenceWfmMessageSource;
    @Autowired
    @Qualifier("pmLocalizer")
    protected WfmMessageSource pmLocalizer;
    @Autowired
    @Qualifier("payrollLocalizer")
    protected WfmMessageSource payrollLocalizer;
    @Autowired
    protected FinancialSettingsManager financialSettingsManager;
    @Autowired
    protected CompanyPayrollSettingsManager companyPayrollSettingsManager;
    @Autowired
    protected PdfTemplateSettingsManager pdfTemplateSettingsManager;

    protected String dateFormat(Date date, boolean... isServerTime) {
        return ServerUtils.shortDateFormat(date, uploadManager.getUser(), isServerTime == null || isServerTime.length <= 0 || !isServerTime[0]);
    }

    protected String longDateFormat(Date date, boolean... isServerTime) {
        return ServerUtils.longDateFormat(date, uploadManager.getUser(), isServerTime == null || isServerTime.length <= 0 || !isServerTime[0]);
    }

    protected String getResultOrNA(Object object) {
        if (object == null || "".equals(object.toString())) {
            return "N/A";
        } else {
            return object.toString();
        }
    }

    public int getCalculationScale() {
        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        if (fs != null) {
            return fs.getCalculationScale() != null ? fs.getCalculationScale() : 2;
        }
        return 2;
    }

    public static String getMoneyFormat(BigDecimal bigDecimal, Integer scale) {
        return Utils.formatDecimal(bigDecimal.setScale(scale, BigDecimal.ROUND_HALF_UP));
    }

    public static String getMoneyFormat(BigDecimal bigDecimal) {
        return getMoneyFormat(bigDecimal, 2);
    }

    public static String getExtendedMoneyFormat(BigDecimal bigDecimal) {
        return Utils.formatExtendedDecimal(bigDecimal.setScale(4, BigDecimal.ROUND_HALF_UP));
    }

    public static String getMoneyFormat(double doubleValue) {
        return Utils.formatDouble(doubleValue);
    }

    public void setFileName(String fileName) {
        if (fileName != null && !"".equals(fileName.trim())) {
            this.fileName = ServerUtils.normalizeFileName(fileName);
        }
    }

    /**
     * Function returns real URL path of the given file.
     * Analogue of getServletContext().getRealPath() function.
     *
     * @param path
     * @return real path
     * @throws java.io.IOException
     */
    protected String getRealPath(String path) throws IOException {
        return applicationContext.getResource(path).getURL().toString();
    }

    /**
     * @param path
     * @return
     * @throws IOException
     */
    protected String getAbsaloutPath(String path) throws IOException {
        return applicationContext.getResource(path).getFile().getAbsolutePath();
    }

    public String localize(String message) {
        return pdfWfmMessageSource.localize(message);
    }

    protected String getFontBase() {
        return "/fonts";
    }

    /**
     * Set Pdf Meta Data
     *
     * @param response
     */
    protected void buildPdfMetadataBefore(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        try {
            if (fileName.contains(" ")) {
                fileName = fileName.replace(" ", "");
            }
            if (fileName.contains("/")) {
                fileName = fileName.replace("\\/", "_");
            }

            String userAgent = request.getHeader("USER-AGENT").toLowerCase();
            fileName = URLEncoder.encode(fileName, "UTF8");
            if (userAgent != null && (userAgent.contains("chrome") || userAgent.contains("msie") || userAgent.contains("safari"))) {
                response.setHeader("Content-Disposition", getDownloadType() + " filename=\"" + fileName + ".pdf\"");
            } else {
                response.setHeader("Content-Disposition", getDownloadType() + " filename*=\"utf-8'" + fileName + ".pdf\"");
            }

// 			response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + ".pdf\"");
            response.setHeader("Content-Type", "text/html; charset=utf-8");
            response.setContentType("application/pdf");
            response.setCharacterEncoding("UTF-8");
            response.setContentLength(100);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Set Pdf Meta Data
     *
     * @param response
     */
    protected void buildPdfMetadataAfter(HttpServletResponse response, int contentLength) {
        try {
            response.setContentLength(contentLength);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param company
     * @return Company logo Url else null
     */
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

    public String getStampUrl(EdsCompany company, String type) throws IOException {
        String url = null;

        if (company != null) {
            url = companyAttachmentManager.getCompanyStampUrl(company, type);
        }
        return url;
    }

    /**
     * @param object
     */
    protected void prepareOutputStream(Object object) {

    }

    /**
     * create Pdf Document A4 format
     * Padinng left,right,top,bottom 10 point
     *
     * @return Document
     */
    protected Document newDocument(EdsCompany edsCompany, Object dataClass) {
        return new Document(PageSize.A4, 20, 20, 120, 50);
    }

    /**
     * create PdfWriter object for given IText document
     *
     * @param document,outputstream
     * @return PdfWriter
     * @throws DocumentException
     */
    protected PdfWriter newWriter(Document document, OutputStream os) throws DocumentException {
        return PdfWriter.getInstance(document, os);
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public String getDownloadType() {
        return downloadType;
    }
}
