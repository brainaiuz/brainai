package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.xml.sax.SAXException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.TransformerConfigurationException;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 07.10.2008
 * Time: 13:18:19
 * To change this template use File | Settings | File Templates.
 */
public abstract class PostPDFHandler extends BasePDFHandler {

    private PropertyEditorRegistrar[] propertyEditorRegistrars;
    private Object dataClass;

    @Autowired
    @Qualifier("commonLocalizer")
    protected WfmMessageSource commonLocalizer;


    /**
     * Handles request, when service is called.
     * Generates PDF and returns is as response to client.
     *
     * @param request
     * @param response
     * @throws javax.servlet.ServletException
     */

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        dataClass = getDataClass();

        if (dataClass != null && prepareRequest(request)) {
            tryToBind(request, dataClass);
        }

        prepareOutputStream(dataClass);
        returnResponse(response);
        closeStream();
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
            prepareOutputStream(object);
            outputStream.write(baos.toByteArray());
            closeStream();
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
     * All PDF forming logic should be here.
     *
     * @param object object that you're pass from client as a request parameter
     */
//    @Transactional
    public abstract void writePDF(Object object);

    /**
     * You should define object for autobinding.
     * All request parameters will be set to this object.
     *
     * @return your object
     */
    protected abstract Object getDataClass();

    private void prepareOutputStream(Object object) {
        try {
            setHandler();
            writePDF(object);
        } catch (TransformerConfigurationException | IOException ex) {
            ex.printStackTrace();
        } catch (SAXException ex) {
            ex.printStackTrace();
        }
    }


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

    private final ServletRequestDataBinder tryToBind(HttpServletRequest request, Object command) {
        ServletRequestDataBinder binder = createBinder(request, command);
        binder.bind(request);
        return binder;
    }

    private ServletRequestDataBinder createBinder(HttpServletRequest request, Object command) {
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
     * to separate objects, as an alternative to {@link #initBinder}.
     *
     * @see #initBinder
     */
    public final void setPropertyEditorRegistrars(PropertyEditorRegistrar propertyEditorRegistrar) {
        this.propertyEditorRegistrars = new PropertyEditorRegistrar[]{propertyEditorRegistrar};
    }

    /**
     * Specify multiple PropertyEditorRegistrars to be applied
     * to every DataBinder that this controller uses.
     * <p>Allows for factoring out the registration of PropertyEditors
     * to separate objects, as an alternative to {@link #initBinder}.
     *
     * @see #initBinder
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

    public String velocityReplaceContentAttributes(Object object) {
        return null;
    }
}
