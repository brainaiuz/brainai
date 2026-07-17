package com.edatasite.workforce.gwt.core.server.db.documents;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.documents.EdsFolder;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: Jul 4, 2010
 * Time: 9:05:37 PM
 * To change this template use File | Settings | File Templates.
 */
public interface AttachmentUtilsManager {

    List<FileResource> getAttachments(int folderType, Integer folderID, Integer entityID);

    List<FileResource> getAttachments(int folderType, Integer folderID, Integer entityID, EdsUser user);

    void saveAttachments(int folderType, Integer folderID, Integer entityID, FileItem[] items);

    void saveAttachments(int folderType, Integer folderID, Integer entityID, FileItem[] items, EdsUser user);

    void copyFileWhenConvert(int folderType, int folderID, int fileID, int entityID, FileResource fileResource);

    EdsFolder getFolder(int folderType, Integer entityID, EdsCompany company);
}
