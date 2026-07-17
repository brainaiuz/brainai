package com.workforcetrack.mobile.services;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.documents.client.exceptions.InsufficientPermissionsException;
import com.edatasite.workforce.gwt.documents.client.exceptions.ObjectNotFoundException;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.edatasite.workforce.gwt.documents.client.rpc.DocumentsService;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import com.workforcetrack.mobile.rpc.attachment.MFileResource;
import com.workforcetrack.mobile.rpc.attachment.MFileResourceList;
import com.workforcetrack.mobile.rpc.base.MStringList;
import com.workforcetrack.mobile.rpc.base.WebServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created with IntelliJ IDEA.
 * User: Sancho
 * Date: 20.04.12
 * Time: 16:14
 * To change this template use File | Settings | File Templates.
 */
@Service("uploadWebService")
public class UploadWebServiceImpl implements UploadWebService, CommandConstants {

    @Autowired
    private DocumentsService documentsService;
    @Autowired
    private CommonService commonService;

    @Override
    public String generateURL(MFileResource uploadResource) {
        String sessionID = ServerSecurityContext.getInstance().getSessionId();
        String upTo = AMAZON_PARAM_NAME;
        if (uploadResource.getUrl() != null && !"".equals(uploadResource.getUrl())) {
            upTo = uploadResource.getUrl();
        }

        Integer folderID = getFolderID(uploadResource);
        if (folderID == null) {
            return "";
        }
        Integer folderType = uploadResource.getFileType();
        String fileName = EncryptionHelper.encodeURL(uploadResource.getName());
        String description = EncryptionHelper.encodeURL(uploadResource.getDescription());
        Integer entityID = uploadResource.getObjectID();

        StringBuilder requestURL = new StringBuilder(FOLDER_ID).append("=").append(folderID);
        isNotNullParamValueAdd(requestURL, SESSION_ID_PARAM_NAME, sessionID);
        isNotNullParamValueAdd(requestURL, ATTACHMENT_NAME, fileName);
        isNotNullParamValueAdd(requestURL, DESCRIPTION_PARAM_NAME, description);
        isNotNullParamValueAdd(requestURL, UPLOAD_TYPE_PARAM_NAME, upTo);
        isNotNullParamValueAdd(requestURL, ENTITY_ID, entityID);
        isNotNullParamValueAdd(requestURL, FOLDER_TYPE_ID, folderType);

        return requestURL.toString();
    }

    @Override
    public MStringList generateURL(MFileResourceList uploadResourceList) {
        String sessionID = ServerSecurityContext.getInstance().getSessionId();
        MStringList resultList = new MStringList();

        if (uploadResourceList != null && uploadResourceList.getFile() != null && uploadResourceList.getFile().size() > 0) {
            StringBuilder requestURL = new StringBuilder();
            Integer folderID = null;
            for (MFileResource uploadResource : uploadResourceList.getFile()) {
                folderID = getFolderID(uploadResource);
                if (folderID == null) {
                    resultList.getResult().add("");
                    continue;
                }

                requestURL.setLength(0);
                requestURL.append(FOLDER_ID).append("=").append(folderID);
                isNotNullParamValueAdd(requestURL, SESSION_ID_PARAM_NAME, sessionID);
                isNotNullParamValueAdd(requestURL, ATTACHMENT_NAME, EncryptionHelper.encodeURL(uploadResource.getName()));
                isNotNullParamValueAdd(requestURL, DESCRIPTION_PARAM_NAME, EncryptionHelper.encodeURL(uploadResource.getDescription()));
                isNotNullParamValueAdd(requestURL, UPLOAD_TYPE_PARAM_NAME,
                        (uploadResource.getUrl() != null && !"".equals(uploadResource.getUrl())) ? uploadResource.getUrl() : AMAZON_PARAM_NAME);
                isNotNullParamValueAdd(requestURL, ENTITY_ID, uploadResource.getObjectID());
                isNotNullParamValueAdd(requestURL, FOLDER_TYPE_ID, uploadResource.getFileType());

                resultList.getResult().add(requestURL.toString());
            }
        }

        return resultList;
    }

    @Override
    public String deleteFile(Integer objectID) {
        Boolean result = Boolean.FALSE;
        try {
            documentsService.deleteFile(objectID);
            result = Boolean.TRUE;
        } catch (ObjectNotFoundException | InsufficientPermissionsException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return result.toString();
    }

    private void isNotNullParamValueAdd(StringBuilder stringBuilder, String paramName, Object paramValue) {
        if (!WebServiceUtils.isEmptyOrNull(paramValue)) {
            stringBuilder.append("&").append(paramName).append("=").append(paramValue);
        }
    }


    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Integer getFolderID(MFileResource fileResource) {
        if (WebServiceUtils.isEmptyOrNull(fileResource, fileResource.getFileType())) {
            return null;
        }
        Integer folderID = null;
        if (fileResource.getFileType().equals(Constants.F_TASK) || fileResource.getFileType().equals(Constants.F_PROJECT)) {
            if (!WebServiceUtils.isEmptyOrNull(fileResource.getObjectID())) {
                folderID = documentsService.getFolderID(fileResource.getFileType(), fileResource.getObjectID());
            }
        } else {
//            EdsUser user = userManager.getUser();
//            Integer companyID = user.getCompany().getObjectID();
//            Integer userID = user.getObjectID();
            FolderResource folderResource = commonService.getTempFolderByCompanyID(null, null);
            fileResource.setObjectID(null);
            folderID = folderResource.getObjectId();
        }

        return folderID;
    }


}
