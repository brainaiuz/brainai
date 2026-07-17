package com.edatasite.workforce.gwt.core.server.office365.services.impl;

import com.edatasite.workforce.gwt.core.server.office365.constants.Office365Constants;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365AccessTokenDTO;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365BaseList;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365HttpResponse;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365MessageFolder;
import com.edatasite.workforce.gwt.core.server.office365.resources.base.Office365BaseItem;
import com.edatasite.workforce.gwt.core.server.office365.services.Office365MessageFolderService;
import com.edatasite.workforce.gwt.core.server.office365.utils.Office365HttpClient;
import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

/**
 * Created by umakarimov on 9/29/15.
 */
@Service("office365MessageFolderService")
public class Office365MessageFolderServiceImpl implements Office365MessageFolderService, Office365Constants {
    /**
     * @param token
     * @return
     * @see https://msdn.microsoft.com/office/office365/APi/mail-rest-operations#Getfolders
     */
    @Override
    public Office365BaseList<Office365MessageFolder> getEmailFolders(Office365AccessTokenDTO token) {
        Office365HttpResponse data = Office365HttpClient.doGet(OUTLOOK_MESSAGE_FOLDER_LIST_URL, null, token);

        if (data.hasError()) {
            return null;
        }

        return new Office365BaseList<>(data, new Office365BaseItem.FieldMapper<Office365MessageFolder>() {
            @Override
            public Office365MessageFolder map(Object item) {
                return new Office365MessageFolder((JSONObject) item);
            }
        });
    }

    /**
     * @param token
     * @param folder
     * @return
     * @see https://msdn.microsoft.com/office/office365/APi/mail-rest-operations#CreateAFolder
     */
    @Override
    public Office365MessageFolder createEmailFolder(Office365AccessTokenDTO token, final Office365MessageFolder folder) {
        if (StringUtils.isBlank(folder.getDisplayName())) {
            return null;
        }

        Office365HttpResponse data = Office365HttpClient.doPost(OUTLOOK_MESSAGE_FOLDER_LIST_URL, new JSONObject() {{
            this.put("DisplayName", folder.getDisplayName());
        }}, token);

        if (data.hasError()) {
            return null;
        }

        return new Office365MessageFolder(data);
    }

    /**
     * @param token
     * @param folder
     * @return
     * @see https://msdn.microsoft.com/office/office365/APi/mail-rest-operations#Updatefolders
     */
    @Override
    public Office365MessageFolder updateEmailFolder(Office365AccessTokenDTO token, final Office365MessageFolder folder) {
        if (StringUtils.isBlank(folder.getId()) || StringUtils.isBlank(folder.getDisplayName())) {
            return null;
        }

        String folderUrl = String.format(OUTLOOK_MESSAGE_FOLDER_ITEM_URL, folder.getId());

        Office365HttpResponse data = Office365HttpClient.doPatch(folderUrl, new JSONObject() {{
            this.put("DisplayName", folder.getDisplayName());
        }}, token);

        if (data.hasError()) {
            return null;
        }

        return new Office365MessageFolder(data);
    }

    /**
     * @param token
     * @param folder
     * @see https://msdn.microsoft.com/office/office365/APi/mail-rest-operations#Deletefolders
     */
    @Override
    public void deleteEmailFolder(Office365AccessTokenDTO token, Office365MessageFolder folder) {
        if (StringUtils.isBlank(folder.getId()) || StringUtils.isBlank(folder.getDisplayName())) {
            return;
        }

        String folderUrl = String.format(OUTLOOK_MESSAGE_FOLDER_ITEM_URL, folder.getId());

        Office365HttpClient.doDelete(folderUrl, null, token);
    }

    @Override
    public Office365MessageFolder moveEmailFolder(
            Office365AccessTokenDTO token, Office365MessageFolder folder, String destinationId
    ) {
        return this.moveOrCopyEmailFolder(true, token, folder, destinationId);
    }

    @Override
    public Office365MessageFolder copyEmailFolder(
            Office365AccessTokenDTO token, Office365MessageFolder folder, String destinationId
    ) {
        return this.moveOrCopyEmailFolder(false, token, folder, destinationId);
    }

    /**
     * @param move
     * @param token
     * @param folder
     * @param destinationId
     * @return
     * @see https://msdn.microsoft.com/office/office365/APi/mail-rest-operations#Moveorcopyfolders
     */
    private Office365MessageFolder moveOrCopyEmailFolder(
            boolean move, Office365AccessTokenDTO token,
            Office365MessageFolder folder, final String destinationId
    ) {
        if (StringUtils.isBlank(folder.getId()) ||
                StringUtils.isBlank(folder.getDisplayName()) ||
                StringUtils.isBlank(destinationId)) {
            return null;
        }

        String actionUrl = move ? OUTLOOK_MESSAGE_FOLDER_ITEM_MOVE_URL : OUTLOOK_MESSAGE_FOLDER_ITEM_COPY_URL;
        String folderUrl = String.format(actionUrl, folder.getId());

        Office365HttpResponse data = Office365HttpClient.doPost(folderUrl, new JSONObject() {{
            this.put("DestinationId", destinationId);
        }}, token);

        if (data.hasError()) {
            return null;
        }

        return new Office365MessageFolder(data);
    }
}
