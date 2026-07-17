package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsUpload;
import com.edatasite.workforce.core.domain.EdsUploadMinIOSettings;
import com.edatasite.workforce.core.domain.documents.EdsFolder;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.server.app.DocumentItem;

import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public interface MinIOManager {

    String putFile(EdsUpload upload, InputStream stream) throws IOException, NoSuchAlgorithmException;

    String putFile2(EdsUpload upload, DocumentItem item) throws IOException, NoSuchAlgorithmException;

    void deleteFile(EdsUpload upload) throws IOException;

    String getLink(EdsUploadMinIOSettings uploadMinIOSettings);

    InputStream getInputStream(EdsUpload upload);

    ArrayList<FileResource> getCompanyBackupFiles(EdsFolder folder, ListingFilterParameter parameter);

    String backupCompanyDocuments(Integer companyID);

    Integer copyCompanyDocumentsSizeToUploadTable(Integer companyID);

    void saveXmlBackup(InputStream stream, String fileName) throws IOException, NoSuchAlgorithmException;
}
