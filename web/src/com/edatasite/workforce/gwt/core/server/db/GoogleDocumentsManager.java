package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsSinxDocuments;
import com.edatasite.workforce.core.domain.EdsSinxDocumentsSettings;
import com.edatasite.workforce.core.domain.EdsUpload;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.documents.EdsFileBody;
import com.edatasite.workforce.gwt.core.client.rpc.TreeSelectItem;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.google.api.services.drive.Drive;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 17.11.2008
 * Time: 18:13:25
 * To change this template use File | Settings | File Templates.
 */
public interface GoogleDocumentsManager extends Manager<EdsSinxDocuments> {

    EdsSinxDocuments getGoogleDocuments(EdsUser user, boolean withCheck);

    //String getGoogleId(EdsSinxDocuments googleDocuments, String sessionToken) throws GeneralSecurityException, IOException, ServiceException;

    Drive getService(EdsUser user);

    Boolean validateUser();

    void uploadFile(EdsUpload upload) throws GeneralSecurityException, IOException, ServiceException;

    Boolean checkExistingFoldersIntoGoogleDrive(Drive service, List<TreeSelectItem> folders, String parentId) throws IOException;

    void createFoldersIntoGoogleDrive(Drive service, List<TreeSelectItem> folders, String parentId) throws IOException;

    ArrayList<TreeSelectItem> getAllSubFoldersInKpiRoot(String root) throws IOException;

    FolderResource[] getAllGoogleFolders() throws GeneralSecurityException, IOException, ServiceException;

    ArrayList<FolderResource> getGoogleFolders(Drive service, String parentId) throws GeneralSecurityException, IOException, ServiceException;

    ArrayList<FileResource> getGoogleFiles(Drive service, String folderId) throws GeneralSecurityException, IOException, ServiceException;

    FolderResource[] getAllGoogleDocuments() throws GeneralSecurityException, IOException, ServiceException;

    void deleteDocument(EdsUpload upload) throws GeneralSecurityException, IOException, ServiceException;

    void updateFile(EdsFileBody currentBody) throws GeneralSecurityException, IOException, ServiceException;

	String[] getDocumentParameters(String googleDocResourceId);

    InputStream getFileInputStream(EdsUpload upload);

    String getDocumentID(EdsSinxDocumentsSettings documentLink);
}
