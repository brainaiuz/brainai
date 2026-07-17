package com.edatasite.workforce.gwt.core.server.servlets;

import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.server.actions.BankAccountDocumentCommand;
import com.edatasite.workforce.gwt.core.server.actions.CreateDocumentCommand;
import com.edatasite.workforce.gwt.core.server.actions.ProductCategoryDocumentCommand;
import com.edatasite.workforce.gwt.core.server.actions.ProductDocumentCommand;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

public abstract class WfmCommandHandler implements HttpRequestHandler, CommandConstants {

    private static final Logger log = LoggerFactory.getLogger(WfmCommandHandler.class);
    private PropertyEditorRegistrar[] propertyEditorRegistrars;
    private String commandName = COMMAND_NAME;
    private String errorString = null;

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

    /**
     * @return the commandName
     */
    public String getCommandName() {
        return commandName;
    }

    /**
     * @param commandName the commandName to set
     */
    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    @SuppressWarnings("unchecked")
    private Class commandClass;

    @SuppressWarnings("unchecked")
    public Class getCommandClass() {
        return commandClass;
    }

    @SuppressWarnings("unchecked")
    public void setCommandClass(Class commandClass) {
        this.commandClass = commandClass;
    }

    protected final Object createCommand() throws Exception {
        if (this.commandClass == null) {
            throw new IllegalStateException(
                    "Cannot create command without commandClass being set - "
                            + "either set commandClass or (in a form controller) override formBackingObject");
        }
        return BeanUtils.instantiateClass(this.commandClass);
    }

    protected final ServletRequestDataBinder bindAndValidate(HttpServletRequest request, Object command) throws Exception {
        ServletRequestDataBinder binder = createBinder(command);
        binder.bind(request);
        return binder;
    }

    private ServletRequestDataBinder createBinder(Object command) {
        ServletRequestDataBinder binder = new ServletRequestDataBinder(command, getCommandName());
        prepareBinder(binder);
        return binder;
    }

    protected final void prepareBinder(ServletRequestDataBinder binder) {
        if (this.propertyEditorRegistrars != null) {
            for (PropertyEditorRegistrar propertyEditorRegistrar : this.propertyEditorRegistrars) {
                propertyEditorRegistrar.registerCustomEditors(binder);
            }
        }
    }

    //    @Transactional

    public void handleRequest(HttpServletRequest request, HttpServletResponse response) {
        String sessionId = request.getParameter(SESSION_ID_PARAM_NAME);
        ServerSecurityContext.getInstance().setSessionId(sessionId);
        response.setContentType("text/plain");
        OutputStream stream = null;
        try {
            stream = response.getOutputStream();
            Object command = createCommand();
            if (request instanceof DefaultMultipartHttpServletRequest && command instanceof WfmCommand) {
                DefaultMultipartHttpServletRequest multipartRequest = (DefaultMultipartHttpServletRequest) request;
                WfmCommand wfmCommand;
                if (command instanceof CreateDocumentCommand) {
                    wfmCommand = (CreateDocumentCommand) command;
                    String logoType = multipartRequest.getParameter(LOGO_TYPE);
                    String value = multipartRequest.getParameter(ATTACHMENT_ID);
                    String hostId = multipartRequest.getParameter(HOST_ID);
                    String imageWidth = multipartRequest.getParameter(IMAGE_WIDTH);
                    String imageHeight = multipartRequest.getParameter(IMAGE_HEIGHT);
                    String imgType = multipartRequest.getParameter(IMAGE_TYPE);
                    String fromSection = multipartRequest.getParameter(FROM_SECTION);
                    String folderName = multipartRequest.getParameter(ATTACHMENT_FOLDER);
                    String notdownloadable = multipartRequest.getParameter(NOTDOWNLOADABLE);
                    Integer companyID = null;
                    try {
                        companyID = Integer.parseInt(multipartRequest.getParameter(COMPANY__ID));
                        if (companyID == null) {
                            companyID = Integer.parseInt(ServerSecurityContext.getInstance().getCompanyId());
                        }
                    } catch (Exception ignored) {

                    }
                    ((CreateDocumentCommand) wfmCommand).setNotdownloadable(notdownloadable);
                    ((CreateDocumentCommand) wfmCommand).setHostID(hostId != null ? Integer.parseInt(hostId) : null);
                    ((CreateDocumentCommand) wfmCommand).setAttachmentID(value != null ? Integer.parseInt(value) : null);
                    ((CreateDocumentCommand) wfmCommand).setLogoType(logoType);
                    ((CreateDocumentCommand) wfmCommand).setImageWidth(imageWidth != null ? Integer.parseInt(imageWidth) : null);
                    ((CreateDocumentCommand) wfmCommand).setImageHeight(imageHeight != null ? Integer.parseInt(imageHeight) : null);
                    ((CreateDocumentCommand) wfmCommand).setImgType(imgType);
                    ((CreateDocumentCommand) wfmCommand).setFolderName(folderName);
                    if (companyID != null) {
                        ((CreateDocumentCommand) wfmCommand).setCompanyID(companyID);
                    } else {
                        ((CreateDocumentCommand) wfmCommand).setCompanyID(Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId()));
                    }
                    if (BMT_SURVEY_LOGO.equals(logoType)) {
                        MultipartFile file = multipartRequest.getFile(BMT_SURVEY_LOGO);

                        ((CreateDocumentCommand) wfmCommand).setImgType(file.getContentType());
                        ((CreateDocumentCommand) wfmCommand).setLogoType(logoType);
                        wfmCommand.addFile(new WfmMultipartFile(logoType, file));
                    } else if ("COO_IMAGE".equals(logoType)) {
                        MultipartFile file = multipartRequest.getFile("COO_IMAGE");

                        ((CreateDocumentCommand) wfmCommand).setImgType(file.getContentType());
                        ((CreateDocumentCommand) wfmCommand).setLogoType(logoType);
                        wfmCommand.addFile(new WfmMultipartFile(logoType, file));
                    }
                } else if (command instanceof BankAccountDocumentCommand) {
                    wfmCommand = (BankAccountDocumentCommand) command;
                    String bankAccountType = multipartRequest.getParameter(BANK_ACCOUNT_TYPE);
                    String value = multipartRequest.getParameter(ATTACHMENT_ID);
                    String bankAccountId = multipartRequest.getParameter(BANK_ACCOUNT_ID);
                    ((BankAccountDocumentCommand) wfmCommand).setBankAccountID(bankAccountId != null ? Integer.parseInt(bankAccountId) : null);
                    ((BankAccountDocumentCommand) wfmCommand).setAttachmentID(value != null ? Integer.parseInt(value) : null);
                    ((BankAccountDocumentCommand) wfmCommand).setBankAccAttchType(bankAccountType);
                } else if (command instanceof ProductDocumentCommand) {
                    wfmCommand = (ProductDocumentCommand) command;
                    String pictureId = multipartRequest.getParameter(PICTURE_ID);
                    String productId = multipartRequest.getParameter(PRODUCT_ID);
                    String imgType = multipartRequest.getParameter(IMAGE_TYPE);
                    ((ProductDocumentCommand) wfmCommand).setProductID(productId != null ? Integer.parseInt(productId) : null);
                    ((ProductDocumentCommand) wfmCommand).setPictureID(pictureId != null ? Integer.parseInt(pictureId) : null);
                    ((ProductDocumentCommand) wfmCommand).setImgType(imgType);
                } else if (command instanceof ProductCategoryDocumentCommand) {
                    wfmCommand = (ProductCategoryDocumentCommand) command;
                    String pictureId = multipartRequest.getParameter(PICTURE_ID);
                    String categoryId = multipartRequest.getParameter(PRODUCT_CATEGORY_ID);
                    String imgType = multipartRequest.getParameter(IMAGE_TYPE);
                    ((ProductCategoryDocumentCommand) wfmCommand).setCategoryID(categoryId != null ? Integer.parseInt(categoryId) : null);
                    ((ProductCategoryDocumentCommand) wfmCommand).setPictureID(pictureId != null ? Integer.parseInt(pictureId) : null);
                    ((ProductCategoryDocumentCommand) wfmCommand).setImgType(imgType);
                } else {
                    wfmCommand = (WfmCommand) command;
                    wfmCommand.setParameters(multipartRequest.getParameterMap() != null ? (HashMap) multipartRequest.getParameterMap() : null);
                    MultipartFile multipartF = multipartRequest.getFile((ATTACHMENT_PARAM_BASE + 0));
                    if (multipartF != null) {
                        WfmMultipartFile multipartFile = new WfmMultipartFile((ATTACHMENT_PARAM_BASE + 0), multipartF);
                        wfmCommand.addFile(multipartFile);
                    }

                    MultipartFile excelReportTemplate = multipartRequest.getFile(EXCEL_TEMPLATE_REPORT);
                    if (excelReportTemplate != null) {
                        wfmCommand.addFile(new WfmMultipartFile(EXCEL_TEMPLATE_REPORT, excelReportTemplate));
                        wfmCommand.setCompanyId(multipartRequest.getParameter("COMPANY_ID"));
                    }
                }

                if(request.getParameter(MULTIPLE_FILES) != null && request.getParameter(MULTIPLE_FILES).equals("true")){
                    List<MultipartFile> files = ((DefaultMultipartHttpServletRequest) request).getFiles(ATTACHMENT_PARAM_BASE + 0);
                    if(files != null && files.size()>0){
                        for(MultipartFile file : files){
                            if(file != null){
                                wfmCommand.addFile(new WfmMultipartFile("", file, ""));
                                wfmCommand.getParameters().put(MULTIPLE_FILES, "true");
                            }
                        }
                    }
                } else {
                    String[] descriptions = multipartRequest.getParameterValues(DESCRIPTION_PARAM_NAME);
                    String[] uploadTypes = multipartRequest.getParameterValues(UPLOAD_TYPE_PARAM_NAME);
                    if (descriptions != null && uploadTypes != null) {
                        for (int i = 0; i < descriptions.length; i++) {
                            MultipartFile file = multipartRequest.getFile(ATTACHMENT_PARAM_BASE + Integer.toString(i));
                            if (file != null) {
                                WfmMultipartFile multipartFile = new WfmMultipartFile(descriptions[i], file, uploadTypes[i]);
                                wfmCommand.addFile(multipartFile);
                            }
                        }
                    }
                }
            }
            bindAndValidate(request, command);
            try {
                execute(command);
            } catch (Throwable t) {
                throw t;
            }
            if (errorString == null) {
                if (returnValue != null) {
                    returnValue = "[" + returnValue + "]";
                    stream.write(returnValue.getBytes());
                } else {
                    stream.write(SUCCESS.getBytes());
                }
            } else {
                stream.write(errorString.getBytes());
            }
            response.flushBuffer();
        } catch (Throwable t) {
            try {
                log.error("Error executing handler:", t);
                stream.write(FAIL.getBytes());
                response.flushBuffer();
            } catch (IOException e) {
            }
        }
    }

    public abstract void execute(Object command) throws Throwable;

    public String getErrorString() {
        return errorString;
    }

    public void setErrorString(String errorString) {
        this.errorString = errorString;
    }

    private String returnValue;

    public void setReturnValues(String value) {
        returnValue = value;
    }
}
