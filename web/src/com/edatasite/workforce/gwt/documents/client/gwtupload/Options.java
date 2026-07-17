package com.edatasite.workforce.gwt.documents.client.gwtupload;

import java.util.HashMap;
import java.util.Map;

import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Cookies;

public class Options implements CommandConstants, Constants {
    private final String action;
    private String storage;
    private final int minSize = 1;
    private int maxSize = 1024 * 1024 * 100;  // 100 MB
    private final boolean multiple = true;
    private final boolean forceIframe = false;
    private final Map<String, String> params = new HashMap<>();

    public Options(FolderResource folder, String storage, String description) {
        this.storage = storage;
        this.action = GWT.getHostPageBaseURL() + "servlet.gupld?" + FOLDER_ID + "=" + folder.getObjectId()
                + "&" + SESSION_ID_PARAM_NAME + "=" + Cookies.getCookie(SESSION_ID_COOKIE)
                + "&" + UPLOAD_TYPE_PARAM_NAME + "=" + this.storage + "&" + ENTITY_ID + "=" + folder.getEntityId()
                + "&" + FOLDER_TYPE_ID + "=" + folder.getFileType()
                + "&" + CommandConstants.COMPANY__ID + "=" + Utils.getEncryptedCompanyID()
                + "&" + USER__ID + "=" + Utils.getUserID()
                + "&" + DESCRIPTION_PARAM_NAME + "=" + URL.encode(description);
    }

    public boolean useAdvancedUploader() {
        return UploadHandlerXhr.isSupported() && !isForceIframe();
    }

    public String getAction() {
        return action;
    }

    public boolean isForceIframe() {
        return forceIframe;
    }

    public boolean isMultiple() {
        return multiple;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(Integer maxSize) {
        if (maxSize != null && maxSize > 0) {
            this.maxSize = maxSize;
        }
    }

    public int getMinSize() {
        return minSize;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }
}
