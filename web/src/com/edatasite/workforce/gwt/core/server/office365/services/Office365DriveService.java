package com.edatasite.workforce.gwt.core.server.office365.services;

import com.edatasite.workforce.core.domain.EdsSinxDocuments;
import com.edatasite.workforce.core.domain.EdsUpload;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365AccessTokenDTO;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365Drive;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365DriveItem;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365ResourceCollection;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by umakarimov on 10/6/15.
 */
public interface Office365DriveService {

    Office365Drive getDrive(Office365AccessTokenDTO token, String driveType);

    Office365ResourceCollection<Office365DriveItem> listRootChildren(Office365AccessTokenDTO token, String driveType);

    Office365ResourceCollection<Office365DriveItem> listFolderChildren(String itemId, Office365AccessTokenDTO token, String driveType, boolean getFile);

    Office365DriveItem getRoot(Office365AccessTokenDTO token, String driveType);

    Office365DriveItem getItem(String itemId, Office365AccessTokenDTO token, String driveType);

    Office365DriveItem createItem(String parentId, Office365DriveItem item, Office365AccessTokenDTO token, String driveType);

    Office365DriveItem createItemAtRoot(Office365DriveItem item, Office365AccessTokenDTO token, String driveType);

    Office365DriveItem updateItem(Office365DriveItem item, Office365AccessTokenDTO token, String driveType);

    void deleteItem(String itemId, Office365AccessTokenDTO token, String driveType);

    Office365DriveItem moveItem(Office365DriveItem item, String newParentId, Office365AccessTokenDTO token, String driveType);

    byte[] downloadItem(String itemId, int byteSize, Office365AccessTokenDTO token, String driveType);

    Office365DriveItem uploadItem(String parentId, String name, byte[] content, Office365AccessTokenDTO token, String driveType);

    Office365ResourceCollection<Office365DriveItem> itemSearch(String searchKey, Office365AccessTokenDTO token, String driveType);

    void uploadFile(EdsUpload upload) throws IOException;

    EdsSinxDocuments createSixDocumentData(EdsUser user, boolean active, Office365AccessTokenDTO tokenDTO);

    InputStream getInputStream(EdsUpload upload);

    void deleteDocument(EdsUpload upload);

}
