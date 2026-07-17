package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsUpload;
import com.edatasite.workforce.core.domain.EdsUploadSettings;
import com.edatasite.workforce.core.domain.documents.EdsFolder;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.rpc.FindEncodeInputStream;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.server.app.DocumentItem;

import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public interface UploadManager<E extends EdsUpload> extends Manager<E> {

    String COMPONENT_NAME = "uploadManager";

    InputStream getInputStream(E upload);

    void deleteFile(EdsUpload upload) throws Exception;

    FindEncodeInputStream getFindEncodeInputStream(E upload);

    String getResourceDirectory();

    void createBlank(EdsUpload edsUpload);

    void createAndCommit(E obj);

    void createUpload(E obj);

    EdsUpload getImageWithParentAndSize(EdsUpload parent, String size);

    Double getUploadStorage();

    String getFileURL(Integer uploadId);

    String getFileURL(Integer uploadId, boolean needHostForLocalFile);

    String getFileURL(EdsUpload upload);

    String getFileURL(EdsUpload upload, boolean needHostForLocalFile);

    EdsUpload getUploadByContentTypeAndName(String contentType, String name);

    EdsUploadSettings getUploadSettings(EdsUpload upload);

    EdsUploadSettings getUploadSettingsByUploadId(Integer uploadId);

    String getUploadFileUrl(Integer uploadFileID);

    void createCopy(Integer uploadId, EdsUpload newUpload);

    String backupCompanyDocuments(Integer companyID);

    void persistFile(EdsUploadSettings uploadSettings);

    String putFileToPublicFolder(E obj, InputStream stream) throws IOException, NoSuchAlgorithmException;

    String putFile2(EdsUpload upload, DocumentItem item) throws IOException, NoSuchAlgorithmException;

    ArrayList<FileResource> getCompanyBackupFiles(EdsFolder folder, ListingFilterParameter parameter);

    Integer copyCompanyDocumentsSizeToUploadTable(Integer companyID);

    void saveXmlBackup(InputStream stream, String fileName) throws IOException, NoSuchAlgorithmException;
}
