package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.shared.xml.EdsErrorListener;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUsagePlan;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.CompanyAttachmentManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.UploadManager;
import com.edatasite.workforce.gwt.core.server.db.UsagePlanManager;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.web.HttpRequestHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 03.10.2008
 * Time: 17:10:45
 * To change this template use File | Settings | File Templates.
 */


public abstract class BasePDFHandler implements HttpRequestHandler, Constants, CommandConstants, ApplicationContextAware {

    protected static final String PAYPAL_LOGO_URL = "/pdfimages/paypal.gif";
    protected static final String GOOGLE_CHECKOUT_LOGO_URL = "/pdfimages/google-checkout.gif";

    private TransformerHandler handler;
    private String fileName = "wfm";

    protected ApplicationContext applicationContext;
    protected ByteArrayOutputStream baos;

    protected CompanyAttachmentManager companyAttachmentManager;
    protected UploadManager uploadManager;
    protected ReferenceManager referenceManager;
    private UsagePlanManager usagePlanManager;
    @Autowired
    @Qualifier("commonLocalizer")
    protected WfmMessageSource commonLocalizer;

    @Autowired
    @Qualifier("countryLocalizer")
    protected WfmMessageSource countryLocalizer;


    //    protected String dateFormat(Date date) {
//        return ServerUtils.shortDateFormat(date, uploadManager.getUser());
//    }
    protected String dateFormat(Date date, boolean... isServerTime) {
        return ServerUtils.shortDateFormat(date, uploadManager.getUser(), isServerTime == null || isServerTime.length <= 0 || !isServerTime[0]);
    }

    protected String dateFormat(Date date, EdsCompany company) {
        return ServerUtils.shortDateFormat(date, company);
    }


    protected String longDateFormat(Date date) {
        return ServerUtils.longDateFormat(date, uploadManager.getUser());
    }

    protected String longDateFormat(Date date, EdsCompany company) {
        return ServerUtils.longDateFormat(date, company);
    }

    public void setFileName(String fileName) {

        if (fileName != null && !"".equals(fileName)) {
            this.fileName = fileName;
        }
    }

    protected void setHandler() throws FOPException, IOException, TransformerConfigurationException, SAXException {

        baos = new ByteArrayOutputStream();
        FopFactory fopFactory = FopFactory.newInstance(applicationContext.getResource(getUserConfigPath()).getFile());

//        fopFactory.setUserConfig(applicationContext.getResource(getUserConfigPath()).getFile());
//        fopFactory.setFontBaseURL(applicationContext.getResource(getFontBase()).getURL().toString());

        Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, baos);
        SAXTransformerFactory factory = (SAXTransformerFactory) TransformerFactory.newInstance();
        factory.setErrorListener(new EdsErrorListener());
        handler = factory.newTransformerHandler(new StreamSource(applicationContext.getResource(getFileRepository()).getInputStream()));
        handler.setResult(new SAXResult(fop.getDefaultHandler()));
    }

    /**
     * Path of your xslt file.
     *
     * @return path.
     */
    protected abstract String getFileRepository();

    /**
     * Function returns real URL path of the given file.
     * Analogue of getServletContext().getRealPath() function.
     *
     * @param path - path
     * @return real path
     * @throws IOException - IOException
     */
    protected String getRealPath(String path) throws IOException {
        return applicationContext.getResource(path).getURL().toString();
    }

    private String getUserConfigPath() {
        return "/WEB-INF/fop-cfg.xml";
    }

    private String getFontBase() {
        return "/fonts";
    }

    protected void returnResponse(HttpServletResponse response) {
        try {
            byte[] data = baos.toByteArray();
            if (fileName.contains(" ")) {
                fileName = fileName.replace(" ", "");
            }
            if (fileName.contains("/")) {
                fileName = fileName.replace("\\/", "_");
            }
            fileName = ServerUtils.normalizeFileNameT(fileName);

            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + ".pdf\"");
            response.setHeader("Content-Type", "text/html; charset=utf-8");
            response.setContentType("application/pdf");
            response.setCharacterEncoding("UTF-8");
            response.setContentLength(data.length);
            response.getOutputStream().write(data);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    protected void closeStream() {
        try {
            baos.flush();
            baos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String getCompanyLogoUrl(EdsCompany company) {
        String url = null;
//  checking company usage plan for isPaid
        EdsUsagePlan usagePlan = usagePlanManager.getCurrentUsagePlan(company);
//        SettingsData getCompanySettings()
        if (usagePlan == null || !usagePlan.getPaid()) {
            return null;
        }

        if (company != null) {
            url = companyAttachmentManager.getCompanyLogoUrl(company, FOR_PDF);
//            List<EdsCompanyAttachment> attachments = companyAttachmentManager.getCompanyAttachments(company, referenceManager.findReference(_LOGO_TYPE, FOR_PDF));
//            if (attachments.size() > 0) {
//                EdsUpload upload = attachments.get(0);
//                if (uploadManager.isUploadTypeAmazon(upload)) {
//                    EdsUploadAmazonSettings uploadAmazonSettings = uploadAmazonSettingsManager.getUploadAmazonSettings(upload);
//                    if (uploadAmazonSettings != null) {
//                        try {
//                            url = amazonManager.getLink(uploadAmazonSettings);
//                        } catch (Exception e) {
//
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
        }
        return url;
    }


    protected void startDocument() throws SAXException {

        if (handler == null) {
            throw new NullPointerException(
                    "handler is not setted it can't be null");
        }

        handler.startDocument();
    }

    protected void endDocument() throws SAXException {

        if (handler == null) {
            throw new NullPointerException(
                    "handler is not setted it can't be null");
        }

        handler.endDocument();
    }

    protected void startElement(String name) throws SAXException {

        if (handler == null) {
            throw new NullPointerException(
                    "handler is not setted it can't be null");
        }

        handler.startElement(/* URI */"", name, /* PREFIX + ":" + */name,
                new AttributesImpl());
    }

    protected void endElement(String name) throws SAXException {

        if (handler == null) {
            throw new NullPointerException(
                    "handler is not setted it can't be null");
        }

        handler.endElement(/* URI */"", name, /* PREFIX + ":" + */name);
    }

    protected void writeElement(String name, String value) throws SAXException {

        if (handler == null) {
            throw new NullPointerException(
                    "handler is not setted it can't be null");
        }

        handler.startElement(/* URI */"", name, /* PREFIX + ":" + */name,
                new AttributesImpl());
        if (value != null && !"".equals(value)) {
            handler.characters(value.toCharArray(), 0, value.length());
        }
        handler.endElement(/* URI */"", name, /* PREFIX + ":" + */name);
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void setCompanyAttachmentManager(CompanyAttachmentManager companyAttachmentManager) {
        this.companyAttachmentManager = companyAttachmentManager;
    }

    public void setUploadManager(UploadManager uploadManager) {
        this.uploadManager = uploadManager;
    }

    public void setReferenceManager(ReferenceManager referenceManager) {
        this.referenceManager = referenceManager;
    }

    public void setUsagePlanManager(UsagePlanManager usagePlanManager) {
        this.usagePlanManager = usagePlanManager;
    }
}
