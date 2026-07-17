package com.edatasite.workforce.gwt.core.server.office365.services.impl;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.workforce.core.domain.EdsSinxDocuments;
import com.edatasite.workforce.core.domain.EdsSinxDocumentsSettings;
import com.edatasite.workforce.core.domain.EdsUpload;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.UserCompanyDTO;
import com.edatasite.workforce.gwt.core.server.db.GoogleDocumentsManager;
import com.edatasite.workforce.gwt.core.server.db.SinxDocumentsSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.office365.constants.Office365Constants;
import com.edatasite.workforce.gwt.core.server.office365.managers.Office365AuthManager;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365AccessTokenDTO;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365Drive;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365DriveItem;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365File;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365Folder;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365HttpResponse;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365ItemReference;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365ResourceCollection;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365SharePointCollectionItem;
import com.edatasite.workforce.gwt.core.server.office365.services.Office365AuthService;
import com.edatasite.workforce.gwt.core.server.office365.services.Office365DriveService;
import com.edatasite.workforce.gwt.core.server.office365.utils.Office365Fetcher;
import com.edatasite.workforce.gwt.core.server.office365.utils.Office365HttpClient;
import com.edatasite.workforce.utils.EdsContextParams;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.api.client.http.InputStreamContent;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by umakarimov on 10/6/15.
 */
@Service("office365DriveService")
public class Office365DriveServiceImpl extends Office365Fetcher implements Office365DriveService, Office365Constants {
    @Autowired
    private UserManager userManager;
    @Autowired
    private Office365AuthService office365AuthService;
    @Autowired
    private Office365AuthManager office365AuthManager;
    @Autowired
    private GoogleDocumentsManager googleDocumentsManager;
    @Autowired
    private SinxDocumentsSettingsManager sinxDocumentsSettingsManager;

    private final static TypeReference driveItemListType = new TypeReference<Office365ResourceCollection<Office365DriveItem>>() {
    };

    /**
     * @param token
     * @return
     * @see http://graph.microsoft.io/docs/api-reference/v1.0/api/drive_get
     */
    @Override
    public Office365Drive getDrive(Office365AccessTokenDTO token, String driveType) {
        return new Request<Office365Drive>(driveType, driveType.equals(OFFICE_ONE_DRIVE) ? DRIVE : SHAREPOINT, token)
                .setClass(Office365Drive.class)
                .sendGet()
                .getResource();
    }

    /**
     * @param token
     * @return
     * @see http://graph.microsoft.io/docs/api-reference/v1.0/api/item_list_children
     */
    @Override
    public Office365ResourceCollection<Office365DriveItem> listRootChildren(Office365AccessTokenDTO token, String driveType) {
        if (driveType.equals(OFFICE_ONE_DRIVE)) {
            return new Request<Office365ResourceCollection<Office365DriveItem>>(driveType, DRIVE_ROOT_CHILDREN, token)
                    .setTypeReference(driveItemListType)
                    .sendGet()
                    .getResource();
        } else {
            return listSharePointRootChildren(token);
        }
    }

    private Office365ResourceCollection<Office365DriveItem> listSharePointRootChildren(Office365AccessTokenDTO token) {
        Office365ResourceCollection<Office365DriveItem> allItem = new Office365ResourceCollection<>();
        ArrayList<Office365DriveItem> listItem = new ArrayList<>();
        Office365HttpResponse currentSite = Office365HttpClient.doGet(token.getSiteUrl() + "/_api/web", null, token);
        Office365DriveItem defaultSiteitem = new Office365DriveItem();
        defaultSiteitem.setId("");
        if (currentSite.get("Title") != null) {
            defaultSiteitem.setName(currentSite.get("Title").toString());
        }
        defaultSiteitem.setFolder(new Office365Folder());
        if (hasThisUrlInSettings(defaultSiteitem.getId()) != null) {
            defaultSiteitem.setName(hasThisUrlInSettings(defaultSiteitem.getId()));
            listItem.add(defaultSiteitem);
        }

        Office365HttpResponse data = Office365HttpClient.doGet(token.getSiteUrl() + "/_api/web/webinfos", null, token);
        if (data.hasError()) {
            return null;
        }
        ArrayList siteCollections = (ArrayList) data.get("value");
        for (Object obj : siteCollections) {
            Office365DriveItem item = new Office365DriveItem();
            if (((JSONObject) obj).get("ServerRelativeUrl") != null) {
                item.setId(((JSONObject) obj).get("ServerRelativeUrl").toString());
            }
            if (((JSONObject) obj).get("Title") != null) {
                item.setName(((JSONObject) obj).get("Title").toString());
            }
            item.setFolder(new Office365Folder());
            if (hasThisUrlInSettings(item.getId()) != null) {
                item.setName(hasThisUrlInSettings(item.getId()));
                listItem.add(item);
            }
        }
        allItem.setValue(listItem);
        return allItem;
    }

    private String hasThisUrlInSettings(String itemId) {
        EdsCompanySettings comS = userManager.getUser().getCompany().getCompanySettings();
        if (comS != null && comS.getSharePointSiteUrls() != null && !"".equals(comS.getSharePointSiteUrls())) {
            String[] savedURLs = comS.getSharePointSiteUrls().split("_&_");
            for (String tileWithUrl : savedURLs) {
                String[] titleAndUrl = tileWithUrl.split("_@_");
                if (titleAndUrl[1].contains(itemId)) {
                    return titleAndUrl[0];
                }
            }
        }
        return null;
    }

    /**
     * @param itemId
     * @param token
     * @return
     * @see http://graph.microsoft.io/docs/api-reference/v1.0/api/item_list_children
     */
    @Override
    public Office365ResourceCollection<Office365DriveItem> listFolderChildren(String itemId, Office365AccessTokenDTO token, String driveType, boolean getFile) {

        if (driveType.equals(OFFICE_ONE_DRIVE)) {
            String url = String.format(DRIVE_ITEM_CHILDREN, itemId);
            return new Request<Office365ResourceCollection<Office365DriveItem>>(driveType, url, token)
                    .setTypeReference(driveItemListType)
                    .sendGet()
                    .getResource();
        } else {
            return listSharePointFolderChildren(itemId, token, getFile);
        }

    }

    private Office365ResourceCollection<Office365DriveItem> listSharePointFolderChildren(String itemId, Office365AccessTokenDTO token, boolean getFile) {
        return getOffice365DriveItemOffice365ResourceCollection(itemId, token, getFile, false);
    }

    private Office365ResourceCollection<Office365DriveItem> getOffice365DriveItemOffice365ResourceCollection(String itemId, Office365AccessTokenDTO token, boolean getFile, boolean oneItem) {
        Office365ResourceCollection<Office365DriveItem> allItem = new Office365ResourceCollection<>();
        ArrayList<Office365DriveItem> listItem = new ArrayList<>();
        Integer sharedFolderIndex = itemId.indexOf("Shared Documents");
        String subsite_url = itemId;
        if (sharedFolderIndex != -1) {
            subsite_url = itemId.substring(0, sharedFolderIndex - 1);
        } else {
            itemId = itemId + "/" + "Shared Documents";
        }
        Office365HttpResponse data;
        if (oneItem) {
            data = Office365HttpClient.doGet(token.getSiteUrl() + subsite_url.replace(" ", "%20") + "/_api/Web/GetFileByServerRelativeUrl('" + itemId.replace(" ", "%20") + "')", null, token);
            if (data.hasError()) {
                return null;
            }
            parsJsonToTransferObject(token, getFile, listItem, data);
        } else {
            data = Office365HttpClient.doGet(token.getSiteUrl() + subsite_url.replace(" ", "%20") + "/_api/Web/GetFolderByServerRelativeUrl('" + itemId.replace(" ", "%20") + "')/" + (getFile ? "Files" : "Folders"), null, token);
            ArrayList siteCollections = (ArrayList) data.get("value");
            if (siteCollections != null && siteCollections.size() > 0) {
                for (Object obj : siteCollections) {
                    parsJsonToTransferObject(token, getFile, listItem, (JSONObject) obj);
                }
            }
        }
        allItem.setValue(listItem);
        return allItem;
    }

    private void parsJsonToTransferObject(Office365AccessTokenDTO token, boolean getFile, ArrayList<Office365DriveItem> listItem, JSONObject obj) {
        Office365DriveItem item = new Office365DriveItem();
        if (obj.get("ServerRelativeUrl") != null) {
            item.setId(obj.get("ServerRelativeUrl").toString());
        }
        if (obj.get("Name") != null) {
            item.setName(obj.get("Name").toString());
        }
        if (getFile) {
            item.setFile(new Office365File());
            item.setDownloadUrl(token.getSiteUrl() + item.getId());
            item.seteTag(obj.get("ETag").toString());
            item.setSize(Long.valueOf(obj.get("Length").toString()));
        } else {
            item.setFolder(new Office365Folder());
        }
        listItem.add(item);
    }

    /**
     * @param token
     * @return
     * @see http://graph.microsoft.io/docs/api-reference/v1.0/api/item_get
     */
    @Override
    public Office365DriveItem getRoot(Office365AccessTokenDTO token, String driveType) {
        return new Request<Office365DriveItem>(driveType, driveType.equals(OFFICE_ONE_DRIVE) ? DRIVE_ROOT : SHAREPOINT_ROOT, token)
                .setClass(Office365DriveItem.class)
                .sendGet()
                .getResource();
    }

    /**
     * @param itemId
     * @param token
     * @return
     * @see http://graph.microsoft.io/docs/api-reference/v1.0/api/item_get
     */
    @Override
    public Office365DriveItem getItem(String itemId, Office365AccessTokenDTO token, String driveType) {
        if (driveType.equals(OFFICE_ONE_DRIVE)) {
            String url = String.format(DRIVE_ITEM, itemId);

            return new Request<Office365DriveItem>(driveType, url, token)
                    .setClass(Office365DriveItem.class)
                    .sendGet()
                    .getResource();
        } else {
            Office365ResourceCollection<Office365DriveItem> collectionItem = getOffice365DriveItemOffice365ResourceCollection(itemId, token, true, true);
            if (collectionItem != null && (collectionItem.getValue() != null && collectionItem.getValue().size() > 0)) {
                return collectionItem.getValue().get(0);
            }
            return null;
        }

    }

    /**
     * @param parentId
     * @param item
     * @param token
     * @return
     * @see http://graph.microsoft.io/docs/api-reference/v1.0/api/item_post_children
     */
    @Override
    public Office365DriveItem createItem(String parentId, Office365DriveItem item, Office365AccessTokenDTO token, String driveType) {
        String url = String.format(driveType.equals(OFFICE_ONE_DRIVE) ? DRIVE_ITEM_CHILDREN : SHAREPOINT_ITEM_CHILDREN, parentId);

        item.setParentReference(new Office365ItemReference(parentId));
        return new Request<Office365DriveItem>(driveType, url, token)
                .setClass(Office365DriveItem.class)
                .setResource(item)
                .sendPost()
                .getResource();
    }

    /**
     * @param item
     * @param token
     * @return
     * @see http://graph.microsoft.io/docs/api-reference/v1.0/api/item_post_children
     */
    @Override
    public Office365DriveItem createItemAtRoot(Office365DriveItem item, Office365AccessTokenDTO token, String driveType) {
        return new Request<Office365DriveItem>(driveType, driveType.equals(OFFICE_ONE_DRIVE) ? DRIVE_ROOT_CHILDREN : SHAREPOINT_ROOT_CHILDREN, token)
                .setClass(Office365DriveItem.class)
                .setResource(item)
                .sendPost()
                .getResource();
    }

    /**
     * @param item
     * @param token
     * @return
     * @see http://graph.microsoft.io/docs/api-reference/v1.0/api/item_update
     */
    @Override
    public Office365DriveItem updateItem(Office365DriveItem item, Office365AccessTokenDTO token, String driveType) {
        String url = String.format(driveType.equals(OFFICE_ONE_DRIVE) ? DRIVE_ITEM : SHAREPOINT_ITEM, item.getId());

        return new Request<Office365DriveItem>(driveType, url, token)
                .setClass(Office365DriveItem.class)
                .setResource(item)
                .sendPatch()
                .getResource();
    }

    /**
     * @param itemId
     * @param token
     * @see http://graph.microsoft.io/docs/api-reference/v1.0/api/item_delete
     */
    @Override
    public void deleteItem(String itemId, Office365AccessTokenDTO token, String driveType) {
        String url = String.format(driveType.equals(OFFICE_ONE_DRIVE) ? DRIVE_ITEM : SHAREPOINT_ITEM, itemId);

        new Request<Office365DriveItem>(driveType, url, token)
                .sendDelete();
    }


    /**
     * @param item
     * @param newParentId
     * @param token
     * @return
     * @see http://graph.microsoft.io/docs/api-reference/v1.0/api/item_move
     */
    @Override
    public Office365DriveItem moveItem(Office365DriveItem item, String newParentId, Office365AccessTokenDTO token, String driveType) {
        item.getParentReference().setId(newParentId);

        return this.updateItem(item, token, driveType);
    }

    /**
     * @param itemId
     * @param bytesSize
     * @param token
     * @return
     * @see http://graph.microsoft.io/docs/api-reference/v1.0/api/item_downloadcontent
     */
    @Override
    public byte[] downloadItem(String itemId, int bytesSize, Office365AccessTokenDTO token, String driveType) {
        String url = String.format(driveType.equals(OFFICE_ONE_DRIVE) ? DRIVE_ITEM_CONTENT : SHAREPOINT_ITEM_CONTENT, itemId);

        return new Request(driveType, url, token).loadFile(bytesSize);
    }

    /**
     * @param parentId
     * @param name
     * @param content
     * @param token
     * @return
     * @see http://graph.microsoft.io/docs/api-reference/v1.0/api/item_uploadcontent
     */
    @Override
    public Office365DriveItem uploadItem(String parentId, String name, byte[] content, Office365AccessTokenDTO token, String driveType) {
        if (driveType.equals(OFFICE_ONE_DRIVE)) {
            String url = String.format(DRIVE_ITEM_CHILDREN_CONTENT, parentId, name);

            return new Request<Office365DriveItem>(driveType, url, token)
                    .setBytes(content)
                    .setClass(Office365DriveItem.class)
                    .sendPut()
                    .getResource();
        } else {
            return creteItemToSharePoint(parentId, name, content, token, driveType);
        }
    }

    private Office365DriveItem creteItemToSharePoint(String parentId, String name, byte[] content, Office365AccessTokenDTO token, String driveType) {
        Integer sharedFolderIndex = parentId.indexOf("Shared Documents");
        String subsite_url = parentId;
        if (sharedFolderIndex != -1) {
            subsite_url = parentId.substring(0, sharedFolderIndex - 1);
        } else {
            parentId = parentId + "/" + "Shared Documents";
        }

        String url = token.getSiteUrl() + subsite_url.replace(" ", "%20") + "/_api/Web/GetFolderByServerRelativeUrl('" + parentId.replace(" ", "%20") + "')/files/add(overwrite=true,url='" + name + "')?@target='{3}'";
        Office365SharePointCollectionItem drItem = new Request<Office365SharePointCollectionItem>(driveType, url, token)
                .setBytes(content)
                .setClass(Office365SharePointCollectionItem.class)
                .sendPost()
                .getResource();

        Office365DriveItem item = new Office365DriveItem();
        if (drItem != null && drItem.getShrItem() != null) {
            item.setId(drItem.getShrItem().getId());
            item.seteTag(drItem.getShrItem().geteTag());
            item.setWebUrl((token.getSiteUrl() + drItem.getShrItem().getId()).replace(" ", "%20"));
            item.setDownloadUrl((token.getSiteUrl() + drItem.getShrItem().getId()).replace(" ", "%20"));

        }
        return item;
    }

    /**
     * @param token
     * @return
     * @see http://graph.microsoft.io/docs/api-reference/v1.0/api/item_search
     */
    @Override
    public Office365ResourceCollection<Office365DriveItem> itemSearch(String searchKey, Office365AccessTokenDTO token, String driveType) {
        String url = String.format(driveType.equals(OFFICE_ONE_DRIVE) ? DRIVE_ROOT_SEARCH : SHAREPOINT_ROOT_SEARCH, EncryptionHelper.encodeURL(searchKey));

        return new Request<Office365ResourceCollection<Office365DriveItem>>(driveType, url, token)
                .setTypeReference(driveItemListType)
                .sendGet()
                .getResource();
    }

    public void uploadFile(EdsUpload upload) throws IOException {
        if (upload.getInputStream().available() == 0) {
            copyAttachment(upload);
            return;
        }

        String folderId = null;
        Office365AccessTokenDTO tokenDTO;
        UserCompanyDTO userCompany = office365AuthManager.getUserCompany();
        EdsUser user = userManager.getUser();
        if (user != null && user.getCompany().getCompanySettings().getCustomAuthId() != null) {
            userCompany.setAuthId(user.getCompany().getCompanySettings().getCustomAuthId());
            tokenDTO = office365AuthService.getUserAccessToken(EdsContextParams.getHost(), userCompany, upload.getType().getCode());
        } else {
            tokenDTO = office365AuthService.getUserAccessToken(EdsContextParams.getHost(), upload.getType().getCode());
        }
        if (tokenDTO != null) {
            if (upload.getDriveFolderName() != null && !upload.getDriveFolderName().isEmpty()) {
                Office365Folder office365Folder = new Office365Folder();
                office365Folder.setChildCount(0L);
                Office365DriveItem office365DriveItem = new Office365DriveItem();
                office365DriveItem.setName(upload.getDriveFolderName());
                office365DriveItem.setFolder(office365Folder);
                Office365DriveItem officefolder = createItem(upload.getDriveFolderId(), office365DriveItem, tokenDTO, upload.getType().getCode());
                folderId = officefolder.getId();
            } else if (upload.getDriveFolderId() != null && !upload.getDriveFolderId().isEmpty()) {
                folderId = upload.getDriveFolderId();
            }

            if (folderId == null) {
                folderId = getRoot(tokenDTO, upload.getType().getCode()).getId();
            }

            //if the folder's gevin, then upload the file into folder

            Office365DriveItem officeUploadItem = new Office365DriveItem();
            if (folderId != null && !folderId.isEmpty()) {
                byte[] bytes = IOUtils.toByteArray(new InputStreamContent(upload.getContentType(), upload.getInputStream()).getInputStream());
                String fileOfficeName = EncryptionHelper.encodeURL(upload.getOriginalName());
                officeUploadItem = uploadItem(folderId, fileOfficeName, bytes, tokenDTO, upload.getType().getCode());
            }

            System.out.println("File ID: " + officeUploadItem.getId());

            EdsSinxDocuments googleDocuments = googleDocumentsManager.getGoogleDocuments(user, true);
            if (googleDocuments == null) {
                googleDocuments = createSixDocumentData(user, true, tokenDTO);
            }
            EdsSinxDocumentsSettings googleDocsSettings = new EdsSinxDocumentsSettings();
            googleDocsSettings.setDocumentLink(officeUploadItem.getWebUrl());
            googleDocsSettings.setSinxDocuments(googleDocuments);
            googleDocsSettings.setUpload(upload);
            googleDocsSettings.setDocumentID(officeUploadItem.getId());
            if (officeUploadItem.geteTag().contains("{") && officeUploadItem.geteTag().contains("}")) {
                googleDocsSettings.setDocumentOpenID(officeUploadItem.geteTag().substring(officeUploadItem.geteTag().indexOf("{") + 1, officeUploadItem.geteTag().indexOf("}")));
            } else {
                googleDocsSettings.setDocumentOpenID(officeUploadItem.geteTag());
            }
            googleDocsSettings.setDownloadLink(officeUploadItem.getDownloadUrl());
            sinxDocumentsSettingsManager.create(googleDocsSettings);
        }
    }

    private void copyAttachment(EdsUpload upload) {
        EdsSinxDocumentsSettings googleDocsSettings = new EdsSinxDocumentsSettings();
        googleDocsSettings.setUpload(upload);
        sinxDocumentsSettingsManager.create(googleDocsSettings);
    }

    public EdsSinxDocuments createSixDocumentData(EdsUser user, boolean active, Office365AccessTokenDTO tokenDTO) {
        EdsSinxDocuments sinxDocuments = new EdsSinxDocuments();
        sinxDocuments.setUser(user);
        if (tokenDTO != null) {
            sinxDocuments.setOfficeToken(tokenDTO.getAccessToken());
            sinxDocuments.setOfficeID(tokenDTO.getObjectId());
        }
        sinxDocuments.setActive(active);
        sinxDocuments.setAttempts(0);
        sinxDocuments.setReason(null);
        googleDocumentsManager.create(sinxDocuments);
        return sinxDocuments;
    }

    public InputStream getInputStream(EdsUpload upload) {
        InputStream inputStream = null;
        boolean userIsNull = false;
        EdsUser user = userManager.getUser();
        EdsSinxDocumentsSettings documentLink = sinxDocumentsSettingsManager.getSinxDocsSettings(upload);
        String storageType = upload.getType().getCode();
        if (user == null) {
            //this logic need for get google document's inputStream in background services
            ServerSecurityContext.getInstance().setStaticUserID(documentLink.getSinxDocuments().getUser().getObjectID());
            userIsNull = true;
        }
        Office365AccessTokenDTO dto;
        UserCompanyDTO userCompany = office365AuthManager.getUserCompany();
        user = userManager.getUser();
        if (user != null && user.getCompany().getCompanySettings().getCustomAuthId() != null) {
            userCompany.setAuthId(user.getCompany().getCompanySettings().getCustomAuthId());
            dto = office365AuthService.getUserAccessToken(EdsContextParams.getHost(), userCompany, storageType);
        } else {
            dto = office365AuthService.getUserAccessToken(EdsContextParams.getHost(), storageType);
        }
        if (dto != null) {
            byte[] myBytes = downloadItem(googleDocumentsManager.getDocumentID(documentLink), 5000, dto, storageType);
            inputStream = new ByteArrayInputStream(myBytes);
        }

        if (userIsNull) {
            ServerSecurityContext.getInstance().setStaticUserID(null);
        }
        return inputStream;
    }

    public void deleteDocument(EdsUpload upload) {
        EdsSinxDocumentsSettings docSettings = sinxDocumentsSettingsManager.getSinxDocsSettings(upload);
        if (docSettings != null) {
            String documentID = googleDocumentsManager.getDocumentID(docSettings);
            if (documentID != null) {
                if (docSettings.getDocumentID() != null) {
                    Office365AccessTokenDTO dto = office365AuthService.getUserAccessToken(EdsContextParams.getHost(), upload.getType().getCode());
                    deleteItem(documentID, dto, upload.getType().getCode());
                }
                sinxDocumentsSettingsManager.delete(docSettings);
            }
        }
    }
}
