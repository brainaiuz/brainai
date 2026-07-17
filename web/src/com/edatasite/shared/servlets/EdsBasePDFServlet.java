package com.edatasite.shared.servlets;

import com.edatasite.shared.xml.EdsErrorListener;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamSource;
import java.io.File;

/**
 * User: mansur
 * Date: 31.08.2007
 * Time: 19:07:30
 */

public abstract class EdsBasePDFServlet extends HttpServlet {

    private TransformerHandler handler;

    protected TransformerHandler getHandler(HttpServletResponse response, HttpServletRequest request, String fileRepository) throws Exception {

        if (fileRepository == null || "".equals(fileRepository)) {
            throw new Exception("File repository cann't be null or empty.");
        }
        // currently class doesn't actually

        String baseURL = "";

        TransformerHandler handler;
        response.setContentType("application/pdf");

        //FOPFactory
        FopFactory fopFactory = FopFactory.newInstance(new File(getServletContext().getRealPath("fonts/WEB-INF/fop-cfg.xml")));
//        fopFactory.setFontBaseURL(baseURL);
//        fopFactory.setFontBaseURL(baseURL + "fonts/");
//        fopFactory.setUserConfig(new File(getServletContext().getRealPath("/WEB-INF/fop-cfg.xml")));

        //FOPUserAgent
        FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
//        foUserAgent.setBaseURL(baseURL);

        Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, response.getOutputStream());
        SAXTransformerFactory factory = (SAXTransformerFactory) TransformerFactory.newInstance();
        factory.setErrorListener(new EdsErrorListener());
        handler = factory.newTransformerHandler(new StreamSource(getServletContext().getResourceAsStream(fileRepository)));
        handler.setResult(new SAXResult(fop.getDefaultHandler()));

        return handler;
    }

    protected void startDocument() throws SAXException {

        if (handler == null) {
            throw new NullPointerException("handler was not setted it cann't be null");
        }

        handler.startDocument();
    }

    protected void endDocument() throws SAXException {

        if (handler == null) {
            throw new NullPointerException("handler was not setted it cann't be null");
        }

        handler.endDocument();
    }

    protected void startElement(String name) throws SAXException {

        if (handler == null) {
            throw new NullPointerException("handler was not setted it cann't be null");
        }

        handler.startElement(/*URI*/"", name, /*PREFIX + ":" +*/ name, new AttributesImpl());
    }

    protected void endElement(String name) throws SAXException {

        if (handler == null) {
            throw new NullPointerException("handler was not setted it cann't be null");
        }

        handler.endElement(/*URI*/"", name, /*PREFIX + ":" + */name);
    }

    protected void writeElement(String name, String value) throws SAXException {

        if (handler == null) {
            throw new NullPointerException("handler was not setted it cann't be null");
        }

        handler.startElement("", name, name, new AttributesImpl());
        if (value != null && !"".equals(value)) {
            handler.characters(value.toCharArray(), 0, value.length());
        }
        handler.endElement("", name, name);
    }

    protected void setHandler(TransformerHandler handler) {
        this.handler = handler;
    }

}
