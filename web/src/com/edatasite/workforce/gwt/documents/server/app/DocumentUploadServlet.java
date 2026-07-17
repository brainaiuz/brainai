package com.edatasite.workforce.gwt.documents.server.app;

import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.core.domain.documents.EdsFileHeader;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.documents.client.exceptions.InsufficientPermissionsException;
import com.edatasite.workforce.gwt.documents.client.exceptions.ObjectNotFoundException;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import gwtupload.server.UploadAction;
import gwtupload.server.UploadServlet;
import gwtupload.server.exceptions.UploadActionException;
import gwtupload.server.exceptions.UploadSizeLimitException;
import gwtupload.shared.UConsts;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serial;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 19.05.2010
 * Time: 13:39:05
 * To change this template use File | Settings | File Templates.
 */
public class DocumentUploadServlet extends UploadAction implements CommandConstants {

    @Serial
    private static final long serialVersionUID = 1L;

    final Hashtable<String, String> receivedContentTypes = new Hashtable<>();
    /**
     * Maintain a list with received files and their content types.
     */
    final Hashtable<String, Integer> receivedFiles = new Hashtable<>();

    /**
     * Override this method if you want to check the request before it is passed
     * to commons-fileupload parser.
     *
     * @param request
     * @throws RuntimeException
     */
    public void checkRequest(HttpServletRequest request) {
        final String sessionId = request.getParameter(CommandConstants.SESSION_ID_PARAM_NAME);
        if (sessionId != null && !sessionId.equals("undefined") && sessionId.matches(Constants.SESSION_REGEX)) {
            ServerSecurityContext.getInstance().setSessionId(sessionId);
        }
        Integer companyId = null;
        if (SecurityContext.getInstance().getSessionId() != null) {
            companyId = Integer.valueOf(SecurityContext.getInstance().getSessionId().split("\\$")[1]);
        } else if (request.getParameter(CommandConstants.COMPANY__ID) != null) {
            companyId = getCompanyID(request.getParameter(CommandConstants.COMPANY__ID));
        }
        if (companyId != null) {
            SecurityContext.setCompanyID(companyId);
        }
        String databaseName = SecurityContext.getInstance().getDatabase();
        if (SecurityContext.getCompanyID() != null && (databaseName == null || "".equals(databaseName))) {
            GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager = ApplicationContextProvider.applicationContext.getBean(GlobalAuthJdbcSpringManager.class);
            SecurityContext.getInstance().setDatabase(globalAuthJdbcSpringManager.getCompanyDatabaseName(SecurityContext.getCompanyID()));
        }
        final ServletContext context = getServletContext();
        final WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(context);
        final DocumentsServiceLocal documentsServiceLocal = (DocumentsServiceLocal) applicationContext.getBean("documentsService");
        Integer maxSize1 = documentsServiceLocal != null ? documentsServiceLocal.getCompanyFileUploadMaxSize(companyId) : null;
        if (maxSize1 != null) {
            maxSize = maxSize1;
        }

        if (request.getContentLength() > maxSize) {
            throw new UploadSizeLimitException(maxSize, request.getContentLength());
        }
    }

    private Integer getCompanyID(String embedCompanyID) {
        Integer companyId = null;
        try {
            companyId = Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId());
        } catch (NumberFormatException e) {

            try {
                companyId = Integer.valueOf(embedCompanyID);
            } catch (NumberFormatException e1) {
                e1.printStackTrace();
            }
        }
        return companyId;
    }

    /**
     * Override executeAction to save the received files in a custom place
     * and delete this items from session.
     */
    @Override
    public String executeAction(HttpServletRequest request, List<FileItem> sessionFiles) throws UploadActionException {
        String response;
        final String sessionId = request.getParameter(SESSION_ID_PARAM_NAME);
        if (sessionId != null && sessionId.matches(Constants.SESSION_REGEX)) {
            ServerSecurityContext.getInstance().setSessionId(sessionId);
        }
        Integer companyId = null;
        if (SecurityContext.getInstance().getSessionId() != null) {
            try {
                companyId = Integer.valueOf(SecurityContext.getInstance().getCompanyId());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        } else if (request.getParameter(CommandConstants.COMPANY__ID) != null) {
            companyId = getCompanyID(request.getParameter(CommandConstants.COMPANY__ID));
        }
        if (companyId != null) {
            SecurityContext.setCompanyID(companyId);
        }
        final ServletContext context = getServletContext();
        final WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(context);
        final DocumentsServiceLocal documentsServiceLocal = (DocumentsServiceLocal) applicationContext.getBean("documentsService");

        int cont = 0;
        final int size = sessionFiles.size();
        StringBuilder responseBuilder = new StringBuilder();
        for (FileItem item : sessionFiles) {
            if (!item.isFormField()) {
                cont++;
                try {
                    /// Create a new file based on the remote file name in the client                    
                    final String id = request.getParameter(FOLDER_ID);
                    Integer folderId = null;
                    if (StringUtils.isNumeric(id)) {
                        folderId = Integer.valueOf(id);
                    }
                    String storage = request.getParameter(UPLOAD_TYPE_PARAM_NAME);
                    if (StringUtils.equals(storage, AMAZON_PARAM_NAME)) {
                        storage = Constants.AMAZON;
                    } else if (StringUtils.equals(storage, GOOGLE_DOCS_PARAM_NAME)) {
                        storage = Constants.GOOGLE;
                    } else if (StringUtils.equals(storage, OFFICE_365_DOCS_PARAM_NAME)) {
                        storage = Constants.OFFICE_365;
                    } else if (StringUtils.equals(storage, OFFICE_365_DOCS_SHARE_POINT_PARAM_NAME)) {
                        storage = Constants.OFFICE_365_SHARE_POINT;
                    } else if (StringUtils.equals(storage, LOCAL_PARAM_NAME)) {
                        storage = Constants.LOCAL;
                    } else if (StringUtils.equals(storage, MINIO_PARAM_NAME)) {
                        storage = Constants.MINIO;
                    } else {
                        storage = Constants.AMAZON;
                    }
                    final String entity = request.getParameter(ENTITY_ID);
                    Integer entityId = null;
                    if (StringUtils.isNumeric(entity)) {
                        entityId = Integer.valueOf(entity);
                    }

                    int folderTypeId = EdsFileHeader.F_DEFAULT;
                    if (StringUtils.isNumeric(request.getParameter(FOLDER_TYPE_ID))) {
                        folderTypeId = Integer.valueOf(request.getParameter(FOLDER_TYPE_ID));
                    }
                    String description = "";
                    if (request.getParameter(DESCRIPTION_PARAM_NAME) != null) {
                        description = URLDecoder.decode(request.getParameter(DESCRIPTION_PARAM_NAME), StandardCharsets.UTF_8);
                        if (StringUtils.isEmpty(description)) {
                            description = "";
                        }
                    }
                    final DocumentItem fileBody = new DocumentItem();
                    fileBody.setInputStream(item.getInputStream());
                    fileBody.setContentType(getContentType(item.getContentType()));
                    final String filename = getFilename(item.getName());
                    final String[] ext = filename.split("\\.");
                    fileBody.setName(StringUtils.isNotEmpty(request.getParameter(ATTACHMENT_NAME)) && size == 1
                            ? URLDecoder.decode(request.getParameter(ATTACHMENT_NAME), StandardCharsets.UTF_8) + "." + ext[ext.length - 1] : getFilename(item.getName()));
                    fileBody.setFolderId(folderId);
                    fileBody.setDescription(description);
                    Integer userID = request.getParameter(CommandConstants.USER__ID) != null ? Integer.valueOf(request.getParameter(CommandConstants.USER__ID)) : null;
                    final FileResource fileResource = userID != null ?
                            documentsServiceLocal.createFile(fileBody, storage, folderTypeId, entityId, userID)
                            : documentsServiceLocal.createFile(fileBody, storage, folderTypeId, entityId);
                    /// Save a list with the received files
                    receivedFiles.put(item.getFieldName(), fileResource.getObjectId());
                    receivedContentTypes.put(item.getFieldName(), getContentType(item.getContentType()));

                    /// Compose a xml message with the full file information
                    responseBuilder.append("<file-").append(cont).append("-field>").append(item.getFieldName()).append("</file-").append(cont).append("-field>\n");
                    responseBuilder.append("<file-").append(cont).append("-name>").append(fileResource.getEncodedName()).append("</file-").append(cont).append("-name>\n");
                    responseBuilder.append("<file-").append(cont).append("-size>").append(item.getSize()).append("</file-").append(cont).append("-size>\n");
                    responseBuilder.append("<file-").append(cont).append("-type>").append(getContentType(item.getContentType())).append("</file-").append(cont).append("-type>\n");
                    responseBuilder.append("<file-").append(cont).append("-id>").append(fileResource.getObjectId()).append("</file-").append(cont).append("-id>\n");
                    responseBuilder.append("<file-").append(cont).append("-amazonlink>").append(fileResource.getAmazonLink()).append("</file-").append(cont).append("-amazonlink>\n");
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new UploadActionException(e.getMessage());
                }
            }
        }
        response = responseBuilder.toString();

        /// Remove files from session because we have a copy of them
        removeSessionFileItems(request);

        /// Send information of the received files to the client.
        //ServerSecurityContext.getInstance().setSessionId(null);
        System.out.println(response);
        return "<response>\n" + response + "</response>\n";
    }
    //this method used because of bug in chrome... 

    private String getContentType(String contentType) {
        return contentType == null ? "xtype" : contentType;
    }

    /**
     * Get the content of an uploaded file.
     */
    @Override
    public void getUploadedFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String fieldName = request.getParameter(UConsts.PARAM_SHOW);
        final Integer f = receivedFiles.get(fieldName);
        if (f != null) {

        } else {
            renderXmlResponse(request, response, UploadServlet.XML_ERROR_ITEM_NOT_FOUND);
        }
    }

    /**
     * Remove a file when the user sends a delete request.
     */
    @Override
    public void removeItem(HttpServletRequest request, String fieldName) throws UploadActionException {
        final String sessionId = request.getParameter(SESSION_ID_PARAM_NAME);
        ServerSecurityContext.getInstance().setSessionId(sessionId);
        final Integer file = receivedFiles.get(fieldName);
        receivedFiles.remove(fieldName);
        receivedContentTypes.remove(fieldName);
        if (file != null) {
            try {
                ServletContext context = getServletContext();
                WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(context);
                DocumentsServiceLocal documentsServiceLocal = (DocumentsServiceLocal) applicationContext.getBean("documentsService");
                documentsServiceLocal.deleteFile(file);
            } catch (ObjectNotFoundException | InsufficientPermissionsException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Returns the file name from a potential full path argument. Apparently IE
     * insists on sending the full path name of a file when uploading, forcing
     * us to trim the extra path info. Since this is only observed on Windows we
     * get to check for a single path separator value.
     *
     * @param name the potentially full path name of a file
     * @return the file name without extra path information
     */
    protected String getFilename(String name) {
        int pathSepIndex = name.lastIndexOf("\\");
        if (pathSepIndex == -1) {
            pathSepIndex = name.lastIndexOf("/");
            if (pathSepIndex == -1) {
                return name;
            }
        }
        return name.substring(pathSepIndex + 1);
    }
}
