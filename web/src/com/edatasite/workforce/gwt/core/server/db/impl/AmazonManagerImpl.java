package com.edatasite.workforce.gwt.core.server.db.impl;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.edatasite.workforce.core.domain.EdsUpload;
import com.edatasite.workforce.core.domain.EdsUploadAmazonSettings;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsProductPicture;
import com.edatasite.workforce.core.domain.documents.EdsFileBody;
import com.edatasite.workforce.core.domain.documents.EdsFolder;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.AmazonManager;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.JdbcSpringManager;
import com.edatasite.workforce.gwt.core.server.db.UploadAmazonSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.UploadManager;
import com.edatasite.workforce.gwt.core.server.db.settings.CompanySettingsManager;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.utils.AbstractComparator;
import com.edatasite.workforce.gwt.core.server.utils.ComparatorFactory;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.PermissionHolder;
import com.edatasite.workforce.gwt.documents.server.app.DocumentItem;
import com.edatasite.workforce.utils.EdsContextParams;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import jakarta.mail.internet.MimeUtility;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.stereotype.Repository;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Repository("amazonManager")
public class AmazonManagerImpl implements AmazonManager, Constants, CommandConstants {

    private static final Logger log = LoggerFactory.getLogger(AmazonManagerImpl.class);

    public static final int MAX_FILE_SIZE = 1024 * 1024 * 100; // 100 mb

    @Autowired
    private UploadAmazonSettingsManager uploadAmazonSettingsManager;
    @Autowired
    private CompanyManager companyManager;
    @Autowired
    private CompanySettingsManager companySettingsManager;
    @Autowired
    private JdbcSpringManager jdbcSpringManager;
    @Autowired
    public UploadManager uploadManager;

    private String getBucketName() {
        if (EdsContextParams.isLiveEnvironment()) {
            return "workforcetrack";
        } else {
            return "wfmtest";
        }
    }

    public InputStream getInputStream(EdsUpload upload) {
        EdsUploadAmazonSettings uploadAmazonSettings = uploadAmazonSettingsManager.getUploadAmazonSettings(upload);
        if (uploadAmazonSettings == null) {
            return null;
        }
        getLink(uploadAmazonSettings);

        S3Object object = null;
        try {
            object = AWSStorageUtil.getAWSClient().getObject(getBucketName(), uploadAmazonSettings.getAccessKey());
            InputStream content = getContentCopy(object.getObjectContent());
            object.close();
            return content;
        } catch (AmazonServiceException | IOException ex) {
            ex.printStackTrace();
            return null;
        } finally {
            if (object != null) {
                try {
                    object.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String putFile(EdsUpload upload, InputStream stream) throws IOException, NoSuchAlgorithmException {
        EdsUploadAmazonSettings uploadAmazonSettings = uploadAmazonSettingsManager.getUploadAmazonSettings(upload);
        EdsUser user = uploadAmazonSettingsManager.getUser();
        Integer userID = null, companyID = Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId());
        StringBuilder accessKey = new StringBuilder();
        if (user != null) {
            userID = user.getObjectID();
            companyID = uploadAmazonSettingsManager.getUser().getCompany().getObjectID();
        }
        if (companyID != null) {
            accessKey.append("c").append(companyID).append("/");
            if (userID != null) {
                accessKey.append("u").append(userID).append("/");
            }
        }
        Date expireDate = new Date();
        if (upload.getFileType() != null && ZIP_WITH_EML_FILE.equals(upload.getFileType())) {
            expireDate.setDate(expireDate.getDate() + 29);//expireDate 30 day or 1 month
        } else {
            expireDate.setDate(expireDate.getDate() + 1);
        }
        if (StringUtils.isNotBlank(upload.getFolderName())) {
            if (STATIC_FOLDER.equals(upload.getFolderName())) {
                accessKey = new StringBuilder("000000000000/public/" + companyID + "/" + STATIC_FOLDER + "/");
                expireDate = null;
            } else {
                accessKey.append(upload.getFolderName()).append("/");
            }
        }
        if (upload instanceof EdsFileBody && ((EdsFileBody) upload).getHeader().getFileType() == Constants.F_COMPANY_PUBLIC_ROOT) {
            accessKey = new StringBuilder("000000000000/public/" + companyID + "/");
            accessKey.append(UUID.randomUUID());
        } else {
            accessKey.append(UUID.randomUUID());
        }
        if (upload instanceof EdsProductPicture) {
            accessKey = new StringBuilder("000000000000/public/" + companyID + "/");
            accessKey.append(UUID.randomUUID());
            expireDate = null;
        }

        if (uploadAmazonSettings == null) {
            uploadAmazonSettings = new EdsUploadAmazonSettings();
            uploadAmazonSettings.setUpload(upload);
        }
        uploadAmazonSettings.setAccessKey(accessKey.toString());

        if (upload instanceof EdsFileBody) {
            if (((EdsFileBody) upload).getHeader().getFileType() == Constants.F_COMPANY_PUBLIC_ROOT || STATIC_FOLDER.equals(upload.getFolderName()) /*|| ((EdsFileBody) upload).getHeader().getFileType() == Constants.F_NOTE*/) {
                expireDate = null;
            }
            EdsFolder folder = ((EdsFileBody) upload).getHeader().getFolder();
            while (folder.getParent() != null) {
                if (folder.getParent().getFolderType() == Constants.F_COMPANY_PUBLIC_ROOT) {
                    expireDate = null;
                    break;
                } else if (folder.getParent().getFolderType() == Constants.F_DEFAULT && folder.getFolderType() == Constants.F_COMPANY_PUBLIC_ROOT) {
                    expireDate = null;
                    break;
                } /*else if (folder.getParent().getFolderType() == Constants.F_NOTE_ROOT && folder.getFolderType() == Constants.F_NOTE) {
                    expireDate = null;
                    break;
                }*/
                folder = folder.getParent();
            }
        }

        PutObjectRequest request = new PutObjectRequest(getBucketName(), accessKey.toString(), upload.getInputStream(), null);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(upload.getContentType());
        metadata.setContentEncoding("UTF-8");
        metadata.setContentLength(stream.available());
        metadata.setExpirationTime(expireDate);
        String originalName = upload.getOriginalName();
        if (originalName != null) {
            originalName = ServerUtils.normalizeFileNameT(originalName);
        }
        //This method is to determine if we should add Content-desposition meta data or not
        //if Content-desposition is set then browsers will not display but rather will download file
        if (upload.isDownloadable()) {
            metadata.setContentDisposition("attachment; filename=\"" + MimeUtility.encodeText(originalName) + "\"");
        }
        request.setMetadata(metadata);
        request.setInputStream(stream);
        if (expireDate == null) {
            request.setCannedAcl(CannedAccessControlList.PublicRead);
        }
        boolean uploadResult = false;
        try {
            AWSStorageUtil.getAWSClient().putObject(request);
            uploadResult = true;
        } catch (AmazonClientException e) {
            e.printStackTrace();
        }
        String fileLink = getFileLink(uploadAmazonSettings, (expireDate != null));
        if (EdsContextParams.getHost().contains("wiki.kpi") || expireDate == null) {
            if (fileLink.contains("?Expires")) {
                fileLink = fileLink.substring(0, fileLink.indexOf("?"));
            } else if (fileLink.contains("&Expires")) {
                fileLink = fileLink.substring(0, fileLink.indexOf("&"));
            } else if (fileLink.contains("X-Amz-Expires")) {
                fileLink = fileLink.substring(0, fileLink.indexOf("?"));
            }
        }
        if (uploadResult) {
            try {
                uploadAmazonSettings.setExpireDate(expireDate);
                uploadAmazonSettings.setFileLink(fileLink);
                if (uploadAmazonSettings.getObjectID() == null) {
                    uploadAmazonSettingsManager.create(uploadAmazonSettings);
                } else {
                    uploadAmazonSettingsManager.update(uploadAmazonSettings);
                }
            } catch (Exception ex) {
                deleteFile(uploadAmazonSettings.getAccessKey());
                return null;
            }
        } else {
            throw new IOException("Couldn't upload file to amazon");
        }
        return fileLink;
    }

    public void saveXmlBackup(InputStream stream, String fileName) throws IOException {

        String profile = System.getProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, Constants.DEFAULT_SPRING_PROFILES);
        if (profile.toLowerCase().contains("apps")) {
            profile = "app";
        }

        EdsUser user = uploadAmazonSettingsManager.getUser();
        Integer companyID = Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId());
        EdsCompanySettings companySettings = null;
        if (user != null) {
            companyID = user.getCompany().getObjectID();
            companySettings = user.getCompany().getCompanySettings();
        }

        String buckenName = companySettings != null && companySettings.getXmlBackupBuckedName() != null ? companySettings.getXmlBackupBuckedName() : Constants.BACKUP_BUCKED_NAME;
        PutObjectRequest request = new PutObjectRequest(buckenName, Constants.XML_BACKUP_ROOT_FOLDER + "/" + profile + "-schemabackup/" + companyID + "/" + MimeUtility.encodeText(fileName), stream, null);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("application/xml");
        metadata.setContentEncoding("UTF-8");
        metadata.setContentLength(stream.available());
        metadata.setExpirationTime(null);

        metadata.setContentDisposition("attachment; filename=\"" + MimeUtility.encodeText(fileName) + "\"");
        request.setMetadata(metadata);
        request.setInputStream(stream);
//        request.setCannedAcl(CannedAccessControlList.PublicRead);
        boolean uploadResult = false;
        try {
            if (companySettings != null && companySettings.getXmlBackupClientId() != null && companySettings.getXmlBackupClientSecret() != null) {
                AmazonS3 s3 = new AmazonS3Client(new BasicAWSCredentials(companySettings.getXmlBackupClientId(), companySettings.getXmlBackupClientSecret()));
                s3.putObject(request);
            } else {
                AWSStorageUtil.getAWSClient().putObject(request);
            }
            uploadResult = true;
        } catch (AmazonClientException e) {
            e.printStackTrace();
        }
        if (!uploadResult) {
            throw new IOException("Couldn't upload file to amazon");
        }
    }

    @Override
    public String putFile2(EdsUpload upload, DocumentItem item) throws IOException, NoSuchAlgorithmException {
        EdsUploadAmazonSettings uploadAmazonSettings = uploadAmazonSettingsManager.getUploadAmazonSettings(upload);
        EdsUser user = uploadAmazonSettingsManager.getUser();
        Integer userID = null, companyID = Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId());
        StringBuilder accessKey = new StringBuilder();
        if (user != null) {
            userID = user.getObjectID();
            companyID = uploadAmazonSettingsManager.getUser().getCompany().getObjectID();
        }
        if (companyID != null) {
            accessKey.append("c").append(companyID).append("/");
            if (userID != null) {
                accessKey.append("u").append(userID).append("/");
            }
        }
        Date expireDate = new Date();
        expireDate.setDate(expireDate.getDate() + 1);

        if (upload.getFolderName() != null && !"".equals(upload.getFolderName())) {
            accessKey.append(upload.getFolderName()).append("/");
        }
        accessKey.append(UUID.randomUUID());

        if (uploadAmazonSettings == null) {
            uploadAmazonSettings = new EdsUploadAmazonSettings();
            uploadAmazonSettings.setUpload(upload);
        }
        uploadAmazonSettings.setAccessKey(accessKey.toString());

        boolean uploadResult = false;
        try {
            CopyObjectRequest copyObjRequest = new CopyObjectRequest(
                    item.getSourceBucketName(), item.getSourceKey(), item.getDestinationBucketName(), accessKey.toString());

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(upload.getContentType());
            metadata.setContentEncoding("UTF-8");
            metadata.setContentLength(item.getSize());
            metadata.setExpirationTime(expireDate);
            String originalName = upload.getOriginalName();
            if (originalName != null) {
                originalName = ServerUtils.normalizeFileNameT(originalName);
            }
            metadata.setContentDisposition("attachment; filename=\"" + MimeUtility.encodeText(originalName) + "\"");
            copyObjRequest.setNewObjectMetadata(metadata);
            AWSStorageUtil.getAWSClient().copyObject(copyObjRequest);

            uploadResult = true;
        } catch (AmazonClientException e) {
            e.printStackTrace();
        }

        String fileLink = getFileLink(uploadAmazonSettings, (expireDate != null));
        if (EdsContextParams.getHost().contains("wiki.kpi") || expireDate == null) {
            if (fileLink.contains("?Expires")) {
                fileLink = fileLink.substring(0, fileLink.indexOf("?"));
            } else if (fileLink.contains("&Expires")) {
                fileLink = fileLink.substring(0, fileLink.indexOf("&"));
            }
        }
        if (uploadResult) {
            try {
                uploadAmazonSettings.setExpireDate(expireDate);
                if (uploadAmazonSettings.getObjectID() == null) {
                    uploadAmazonSettingsManager.create(uploadAmazonSettings);
                } else {
                    uploadAmazonSettingsManager.update(uploadAmazonSettings);
                }
            } catch (Exception ex) {
                deleteFile(uploadAmazonSettings.getAccessKey());
                return null;
            }
        } else {
            throw new IOException("Couldn't copy file to amazon");
        }
        return fileLink;
    }

    public void deleteFile(EdsUpload upload) throws IOException {
        Map<String, Object> edsUploadAmazonSettings = jdbcSpringManager.getUploadAmazonSettingsList(upload.getObjectID());
        Integer edsUploadAmazonSettingsId = Integer.valueOf(String.valueOf(edsUploadAmazonSettings.get("id")));
        String accessKey = String.valueOf(edsUploadAmazonSettings.get("accessKey"));
        if (edsUploadAmazonSettingsId != null) {
            deleteFileLink(accessKey);
            deleteFile(accessKey);
            if (uploadAmazonSettingsManager.get(edsUploadAmazonSettingsId) != null) {
                uploadAmazonSettingsManager.delete(uploadAmazonSettingsManager.get(edsUploadAmazonSettingsId));
            }
        }
    }

    private void deleteFile(String fileAccessKey) throws IOException {
        AWSStorageUtil.getAWSClient().deleteObject(getBucketName(), fileAccessKey);
    }

    private String getFileLink(EdsUploadAmazonSettings uploadAmazonSettings, boolean expirable) {
        String fileAccessKey = uploadAmazonSettings.getAccessKey();
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(getBucketName(), fileAccessKey);
        generatePresignedUrlRequest.setMethod(HttpMethod.GET);
        if (expirable) {
            Date expiration = new Date();
            long milliSeconds = expiration.getTime();
            milliSeconds += 1000 * 60 * 60 * 24 * 3; // Add 3 day.
            expiration.setTime(milliSeconds);
            generatePresignedUrlRequest.setExpiration(expiration);
        }
        URL url = AWSStorageUtil.getAWSClient().generatePresignedUrl(generatePresignedUrlRequest);
        return url.toString();
    }

    private void deleteFileLink(String fileAccessKey) {

    }

    public String getLink(EdsUploadAmazonSettings uploadAmazonSettings) {
        Date expire = new Date();
        if (uploadAmazonSettings.getExpireDate() != null) {
            DateTime start = new DateTime(uploadAmazonSettings.getExpireDate());
            DateTime end = new DateTime(System.currentTimeMillis());
            int d = Minutes.minutesBetween(end, start).getMinutes();
            if (d < 120 || uploadAmazonSettings.getUpload() instanceof EdsProductPicture) {
                expire.setDate(expire.getDate() + 3);
                uploadAmazonSettings.setExpireDate(expire);
                deleteFileLink(uploadAmazonSettings.getAccessKey());
                uploadAmazonSettings.setFileLink(getFileLink(uploadAmazonSettings, true));
            } else {
                return uploadAmazonSettings.getFileLink();
            }
        } else if (uploadAmazonSettings.getUpload() instanceof EdsProductPicture) {
            var fileLink = getFileLink(uploadAmazonSettings, false);
            if (fileLink.contains("X-Amz-Expires")) {
                fileLink = fileLink.substring(0, fileLink.indexOf("?"));
            }
            uploadAmazonSettings.setFileLink(fileLink);
        } else {
            expire.setDate(expire.getDate() + 3);
            uploadAmazonSettings.setExpireDate(expire);
            deleteFileLink(uploadAmazonSettings.getAccessKey());
            uploadAmazonSettings.setFileLink(getFileLink(uploadAmazonSettings, true));
        }
        uploadAmazonSettingsManager.createOrUpdate(uploadAmazonSettings);
        return uploadAmazonSettings.getFileLink();
    }

    public ArrayList<FileResource> getCompanyBackupFiles(EdsFolder folder, ListingFilterParameter parameter) {
        EdsUser admin = uploadManager.getUser();
        String uploadType = EdsContextParams.getUploadType();
        ArrayList<FileResource> result = new ArrayList<>();
        if (Constants.AMAZON.equals(uploadType)) {
            result = getBackupFilesFromAmazon(folder, admin);
            parameter.setSource("backup");
        } else if (Constants.LOCAL.equals(uploadType)) {
            parameter.setSource(uploadManager.getResourceDirectory());
            result = getBackupFilesFromLocalStorage(folder, parameter, admin);
        }

        if (parameter.getSortField() == null) {
            parameter.setSortField(FileResource.NAME);
            parameter.setSortDir(2);//descending order
        }
        if (FileResource.NAME.equals(parameter.getSortField())) {
            result.sort(getComparatorFactoryForFileName().createComparator(parameter.getSortDir()));
        } else if (FileResource.DATE.equals(parameter.getSortField())) {
            result.sort(getComparatorFactoryForFileDate().createComparator(parameter.getSortDir()));
        } else if (FileResource.SIZE.equals(parameter.getSortField())) {
            result.sort(getComparatorFactoryForFileSize().createComparator(parameter.getSortDir()));
        }
        parameter.setStatusID(result.size());
        if (result.size() >= (parameter.getStart() + parameter.getLimit())) {
            FileResource[] fileResources = result.subList(parameter.getStart(), parameter.getStart() + parameter.getLimit()).toArray(new FileResource[]{});
            result.clear();
            Collections.addAll(result, fileResources);
        } else {
            FileResource[] fileResources = result.subList(parameter.getStart(), result.size()).toArray(new FileResource[]{});
            result.clear();
            Collections.addAll(result, fileResources);
        }

        return result;
    }

    private ArrayList<FileResource> getBackupFilesFromLocalStorage(EdsFolder folder, ListingFilterParameter parameter, EdsUser admin) {
        String dirName = parameter.getSource() != null ? parameter.getSource() : "";
        ArrayList<FileResource> result = new ArrayList<>();
        File[] files = new File(dirName).listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    FileResource fileItem = new FileResource();
                    fileItem.setObjectId(-1); //THIS IS LOCAL FILE FROM HARD DRIVE DISC
                    fileItem.setBodyId(0);
                    fileItem.setName(file.getName());
                    fileItem.setDescription(file.getName());
                    fileItem.setContentLength(file.length());
                    fileItem.setFolderId(folder.getObjectID());
                    fileItem.setFolderName(folder.getName());
                    fileItem.setVersioned(false);
                    fileItem.setOwner(admin.getDTO());
                    fileItem.setCreatedBy(admin.getFullName());
                    fileItem.setCreationDate(new Date(file.lastModified()));
                    fileItem.setContentType("application/zip");
                    fileItem.setDeleted(false);
                    fileItem.setReadForAll(true);
                    fileItem.setUploadType(LOCAL);
                    fileItem.setModificationDate(new Date(file.lastModified()));
                    fileItem.setCreatedBy(admin.getFullName());
                    fileItem.setPath(file.getAbsolutePath());
                    PermissionHolder p = new PermissionHolder();
                    p.setDelete(true);
                    p.setRead(true);
                    p.setWrite(true);
                    p.setModifyACL(false);
                    fileItem.setPermission(p);
                    result.add(fileItem);
                }
            }
        }
        return result;
    }

    private ArrayList<FileResource> getBackupFilesFromAmazon(EdsFolder folder, EdsUser admin) {
        EdsCompanySettings companySettings = admin.getCompany().getCompanySettings();
        ArrayList<FileResource> result = new ArrayList<>();
        String profile = System.getProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, Constants.DEFAULT_SPRING_PROFILES);
        if (profile.toLowerCase().contains("apps")) {
            profile = "app";
        }
        try {
            String buckenName = companySettings != null && companySettings.getXmlBackupBuckedName() != null ? companySettings.getXmlBackupBuckedName() : Constants.BACKUP_BUCKED_NAME;
            String folderUrl = folder.getFolderType() == EdsFolder.F_XML_BACKUPS_ROOT ? Constants.XML_BACKUP_ROOT_FOLDER : Constants.AMAZON_BACKUP_ROOT_FOLDER;
            ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
                    .withBucketName(buckenName)
                    .withPrefix(folderUrl + "/" + profile + "-schemabackup/" + admin.getCompany().getObjectID() + "/");
            ObjectListing objectListing;
            do {
                AmazonS3 s3;
                if (companySettings != null && companySettings.getXmlBackupClientId() != null && companySettings.getXmlBackupClientSecret() != null) {
                    s3 = new AmazonS3Client(new BasicAWSCredentials(companySettings.getXmlBackupClientId(), companySettings.getXmlBackupClientSecret()));
                } else {
                    s3 = AWSStorageUtil.getAWSClient();
                }
                objectListing = s3.listObjects(listObjectsRequest);
                for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                    FileResource file = new FileResource();
                    file.setBodyId(0);
                    file.setFolderId(folder.getObjectID());
                    file.setFolderName(folder.getName());
                    file.setVersioned(false);
                    file.setName(objectSummary.getKey().substring(objectSummary.getKey().lastIndexOf("/") + 1));
                    URL url = s3.generatePresignedUrl(new GeneratePresignedUrlRequest(buckenName, objectSummary.getKey(), HttpMethod.GET));
                    file.setPath(url.toString());
                    file.setAmazonLink(url.toString());
                    file.setOwner(admin.getDTO());
                    file.setCreatedBy(admin.getFullName());
                    file.setContentLength(objectSummary.getSize());
                    file.setCreationDate(objectSummary.getLastModified());
                    if (folder.getFolderType() == EdsFolder.F_XML_BACKUPS_ROOT) {
                        file.setContentType("application/xml");
                    } else {
                        file.setContentType("application/zip");
                    }
                    file.setDeleted(false);
                    file.setReadForAll(true);
                    file.setUploadType(AMAZON);
                    file.setModificationDate(objectSummary.getLastModified());
                    file.setCreatedBy(admin.getFullName());

                    PermissionHolder p = new PermissionHolder();
                    p.setDelete(false);
                    p.setRead(true);
                    p.setWrite(true);
                    p.setModifyACL(false);
                    file.setPermission(p);
                    result.add(file);
                }
                listObjectsRequest.setMarker(objectListing.getNextMarker());
            } while (objectListing.isTruncated());
        } catch (AmazonClientException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String backupCompanyDocuments(Integer companyID) {
        String filePath = "";
        try {
            int BUFFER = 2048;
            ArrayList<String> fileNames = new ArrayList<>();
            File folder = new File(System.getProperty("java.io.tmpdir") + "/" + companyID.toString() + "_" + "AmazonFiles/");
            if (!folder.exists()) {
                folder.mkdir();
            }
            S3Object s3Object;
            BufferedInputStream origin;
            FileOutputStream dest = new FileOutputStream(folder.getAbsolutePath() + "/" + "AmazonFiles.zip");
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
            out.setMethod(ZipEntry.DEFLATED);
            out.setLevel(9);
            byte[] data = new byte[BUFFER];
            List<EdsUploadAmazonSettings> settings = uploadAmazonSettingsManager.getUploadAmazonSettingsList();
            if (settings != null) {
                int filesCount = 0;
                log.info("Starting backup company amazon files for company: " + companyID);
                for (EdsUploadAmazonSettings setting : settings) {
                    String key = setting.getAccessKey().replace("%2F", "/");
                    try {
                        s3Object = AWSStorageUtil.getAWSClient().getObject(getBucketName(), key);
                        if (s3Object != null) {
                            String fileName = String.valueOf(System.currentTimeMillis());
                            if (s3Object.getObjectMetadata().getContentDisposition() != null && !"".equals(s3Object.getObjectMetadata().getContentDisposition())) {
                                String[] strings = s3Object.getObjectMetadata().getContentDisposition().split("=");
                                if (strings.length == 2) {
                                    fileName = strings[1].replace("\"", "");
                                }
                            }

                            if (fileNames.contains(fileName)) {
                                fileName = setting.getObjectID().toString() + "_" + fileName;
                            }
                            fileNames.add(fileName);
                            filesCount++;
                            try {
                                origin = new BufferedInputStream(s3Object.getObjectContent(), BUFFER);
                                ZipEntry entry = new ZipEntry(fileName);
                                out.putNextEntry(entry);
                                int count;
                                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                                    out.write(data, 0, count);
                                }
                                origin.close();
                            } catch (Exception e) {
                                e.getMessage();
                            } finally {
                                if (s3Object != null) {
                                    s3Object.close();
                                }
                            }
                        }
                    } catch (AmazonClientException e) {
                        e.printStackTrace();
                    }
                }
                out.flush();
                out.close();

                if (filesCount > 0) {
                    EdsUpload upload = new EdsUpload();
                    upload.setContentType("application/zip");
                    upload.setFolderName(STATIC_FOLDER);
                    upload.setOriginalName("AmazonFiles.zip");
                    File file = new File(folder.getAbsolutePath() + "/" + "AmazonFiles.zip");
                    FileInputStream inputStream = new FileInputStream(file);
                    upload.setInputStream(inputStream);
                    uploadManager.createUpload(upload);
                    filePath = putFile(upload, upload.getInputStream());
                    inputStream.close();
                    file.delete();
                }
                log.info("Ending backup company amazon files for company: " + companyID);
                return filePath;
            }
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Integer copyCompanyDocumentsSizeToUploadTable(Integer companyID) {
        SecurityContext.getInstance().setCompanyId(companyID);

        long totalSize = 0;
        int limit = 100;
        String myBucketName = "";
        Long uploadListSize = uploadAmazonSettingsManager.getUploadAmazonSettingsListSize(companyID);
        List<EdsUploadAmazonSettings> uploadAmazonSettingses;
        for (int i = 0; i <= uploadListSize; i = i + limit) {
            uploadAmazonSettingses = uploadAmazonSettingsManager.getUploadAmazonSettingsListOnly(companyID, i, limit);
            for (EdsUploadAmazonSettings item : uploadAmazonSettingses) {
                try {
                    String key = item.getAccessKey().replace("%2F", "/");
                    if (item.getFileLink().contains("workforcetrack")) {
                        myBucketName = "workforcetrack";
                    } else if (item.getFileLink().contains("wfmtest")) {
                        myBucketName = "wfmtest";
                    } else {
                        myBucketName = getBucketName();
                    }
                    try {
                        ObjectMetadata metadata = AWSStorageUtil.getAWSClient().getObjectMetadata(myBucketName, key);
                        if (null != metadata) {
                            item.getUpload().setSize(metadata.getContentLength());
                            uploadManager.update(item.getUpload());
                            totalSize = totalSize + metadata.getContentLength();
                        }
                    } catch (AmazonClientException e) {
                        System.out.println("ResponseCode=404, ResponseMessage= Not Found Document in Amazon S3 " + myBucketName + "/" + key);
                    }
                } catch (AmazonClientException e) {
                    e.printStackTrace();
                }
            }
        }

        EdsCompanySettings css = companyManager.getUser().getCompany().getCompanySettings();
        css.setIndexedDocumentUpload(true);
        companySettingsManager.createOrUpdate(css);

        return Integer.valueOf(String.valueOf(totalSize));
    }

    private ComparatorFactory<FileResource> getComparatorFactoryForFileName() {
        return sortOrder -> new AbstractComparator<FileResource>() {
            public int compare(FileResource o1, FileResource o2) {
                return internalCompare(o1.getEncodedName() != null ? o1.getEncodedName() : "", o2.getEncodedName() != null ? o2.getEncodedName() : "", sortOrder);
            }
        };
    }

    private ComparatorFactory<FileResource> getComparatorFactoryForFileDate() {
        return sortOrder -> new AbstractComparator<FileResource>() {
            public int compare(FileResource o1, FileResource o2) {
                return internalCompare(o1.getCreationDate() != null ? o1.getCreationDate() : "", o2.getCreationDate() != null ? o2.getCreationDate() : "", sortOrder);
            }
        };
    }

    private ComparatorFactory<FileResource> getComparatorFactoryForFileSize() {
        return sortOrder -> new AbstractComparator<FileResource>() {
            public int compare(FileResource o1, FileResource o2) {
                return internalCompare(o1.getContentLength() != null ? o1.getContentLength() : "", o2.getContentLength() != null ? o2.getContentLength() : "", sortOrder);
            }
        };
    }

    private InputStream getContentCopy(InputStream input) throws IOException {
        int BUFFER_SIZE = 1024 * 2;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[BUFFER_SIZE];
        int len;
        while ((len = input.read(buffer)) > -1) {
            baos.write(buffer, 0, len);
        }
        baos.flush();
        return new ByteArrayInputStream(baos.toByteArray());
    }
}
