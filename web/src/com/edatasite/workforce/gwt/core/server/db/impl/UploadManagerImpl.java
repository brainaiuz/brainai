package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsCompanySystemSettings;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsUpload;
import com.edatasite.workforce.core.domain.EdsUploadAmazonSettings;
import com.edatasite.workforce.core.domain.EdsUploadMinIOSettings;
import com.edatasite.workforce.core.domain.EdsUploadSettings;
import com.edatasite.workforce.core.domain.documents.EdsFolder;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.AmazonManager;
import com.edatasite.workforce.gwt.core.server.db.CompanySystemSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.GoogleDocumentsManager;
import com.edatasite.workforce.gwt.core.server.db.MinIOManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.UploadAmazonSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.UploadManager;
import com.edatasite.workforce.gwt.core.server.db.UploadMinIOSettingsManager;
import com.edatasite.workforce.gwt.core.server.office365.services.Office365DriveService;
import com.edatasite.workforce.gwt.core.server.rpc.FindEncodeInputStream;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.server.app.DocumentItem;
import com.edatasite.workforce.utils.EdsContextParams;
import com.google.gdata.util.ServiceException;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

@Repository("uploadManager")
public class UploadManagerImpl<E extends EdsUpload> extends BaseManager<E> implements UploadManager<E>, Constants {

    @Autowired
    protected AmazonManager amazonManager;
    @Autowired
    protected MinIOManager minIOManager;
    @Autowired
    protected ReferenceManager referenceManager;
    @Autowired
    private GoogleDocumentsManager googleDocumentsManager;
    @Autowired
    private CompanySystemSettingsManager companySystemSettingsManager;
    @Autowired
    private Office365DriveService office365DriveService;
    @Autowired
    private UploadAmazonSettingsManager uploadAmazonSettingsManager;
    @Autowired
    private UploadMinIOSettingsManager uploadMinIOSettingsManager;


    private static final int BUFFER_SIZE = 1024 * 100; // 100K buffer

    public UploadManagerImpl() {
        super(EdsUpload.class);
    }

    public UploadManagerImpl(Class objectClass) {
        super(objectClass);
    }

    @Override
    public void createBlank(EdsUpload obj) {
        super.create((E) obj);
    }

    @Override
    public void create(E obj) {
        if (obj.getInputStream() == null && !Constants.NO_DATA.equals(obj.getType().getCode())) {
            throw new RuntimeException("Creation of blank attachments\n are not supported!");
        }

        try {
            long streamLength = obj.getInputStream() != null ? obj.getInputStream().available() : 0;
            obj.setSize(streamLength);
        } catch (IOException e) {
            e.printStackTrace();
        }

        super.create(obj);

        try {
            writeData(obj, obj.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
            super.delete(obj);
            throw new RuntimeException(e.getMessage());
        }
        obj.setSize(obj.getObjectID() != null && obj.getSize() != null ? obj.getSize() : 0);
    }

    public void createAndCommit(E obj) {
        if (obj.getInputStream() == null && !Constants.NO_DATA.equals(obj.getType().getCode())) {
            throw new RuntimeException("Creation of blank attachments\n are not supported!");
        }

        try {
            long streamLength = obj.getInputStream() != null ? obj.getInputStream().available() : 0;
            obj.setSize(streamLength);
        } catch (IOException e) {
            e.printStackTrace();
        }

        EntityManager em = jpaTemplate.createHibernateEntityManager();
        Transaction tx = null;
        try (Session session = em.unwrap(Session.class)) {
            tx = session.getTransaction();
            tx.begin();

            session.persist(obj);
            session.flush();

            writeData(obj, obj.getInputStream());
            session.update(obj);
            session.flush();

            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException(e.getMessage());
        } finally {
            em.close();
        }
    }

    @Override
    public void createUpload(E obj) {
        super.create(obj);
    }

    @Override
    public void update(E obj) {
        super.update(obj);
        if (obj.getInputStream() != null) {
            writeData(obj, obj.getInputStream());
        }
    }

    /*@Override
    public void delete(E obj) {
        try {
            deleteFile(obj);
            super.delete(obj);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }*/

    private void writeData(EdsUpload upload, InputStream stream) {
        String uploadTypeCode = upload.getType() != null ? upload.getType().getCode() : null;
        if (!(Constants.GOOGLE.equals(uploadTypeCode) || Constants.OFFICE_365.equals(uploadTypeCode) || Constants.OFFICE_365_SHARE_POINT.equals(uploadTypeCode))) {
            //we get default storage type from hostbasedsettings file upload type. It may be Amazon or local storage.
            EdsReference ref = referenceManager.findReference(_UPLOAD_TYPE, EdsContextParams.getUploadType());
            upload.setType(ref);
        }

        uploadTypeCode = upload.getType() != null ? upload.getType().getCode() : null;
        try {
            if (Constants.AMAZON.equals(uploadTypeCode)) {
                amazonManager.putFile(upload, stream);
            } else if (Constants.GOOGLE.equals(uploadTypeCode)) {
                googleDocumentsManager.uploadFile(upload);
            } else if (Constants.OFFICE_365.equals(uploadTypeCode) || Constants.OFFICE_365_SHARE_POINT.equals(uploadTypeCode)) {
                office365DriveService.uploadFile(upload);
            } else if (Constants.MINIO.equals(uploadTypeCode)) {
                minIOManager.putFile(upload, stream);
            } else {
                String rd = getResourceDirectory();
                new File(rd).mkdirs();
                String fileName = rd + upload.getObjectID().toString();
                try (OutputStream ois = Files.newOutputStream(Paths.get(fileName))) {
                    byte[] buffer = new byte[BUFFER_SIZE];
                    int count;

                    while ((count = stream.read(buffer)) > 0) {
                        ois.write(buffer, 0, count);
                    }
                    upload.setLocalPath(rd);
                }
            }
        } catch (ServiceException | GeneralSecurityException | IOException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public FindEncodeInputStream getFindEncodeInputStream(EdsUpload upload) {
        InputStream is = getInputStream(upload);
        if (is != null) {
            FindEncodeInputStream encodeInputStream = new FindEncodeInputStream(is);
            encodeInputStream.setIs(getInputStream(upload));
            return encodeInputStream;
        }
        return null;
    }

    @Override
    public InputStream getInputStream(EdsUpload upload) {
        String uploadTypeCode = upload.getType() != null ? upload.getType().getCode() : null;
        if (AMAZON.equals(uploadTypeCode)) {
            return amazonManager.getInputStream(upload);
        } else if (GOOGLE.equals(uploadTypeCode)) {
            return googleDocumentsManager.getFileInputStream(upload);
        } else if (OFFICE_365.equals(uploadTypeCode) || OFFICE_365_SHARE_POINT.equals(uploadTypeCode)) {
            return office365DriveService.getInputStream(upload);
        } else if (MINIO.equals(uploadTypeCode)) {
            return minIOManager.getInputStream(upload);
        } else if (uploadTypeCode == null || LOCAL.equals(uploadTypeCode)) {
            try {
                String fileName = upload.getLocalPath() + upload.getObjectID().toString();
                return Files.newInputStream(Paths.get(fileName));
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public void deleteFile(EdsUpload upload) throws Exception {
        if (upload != null) {
            String uploadTypeCode = upload.getType() != null ? upload.getType().getCode() : null;
            if (AMAZON.equals(uploadTypeCode)) {
                amazonManager.deleteFile(upload);
            } else if (GOOGLE.equals(uploadTypeCode)) {
                googleDocumentsManager.deleteDocument(upload);
            } else if (OFFICE_365.equals(uploadTypeCode) || OFFICE_365_SHARE_POINT.equals(uploadTypeCode)) {
                office365DriveService.deleteDocument(upload);
            } else if (MINIO.equals(uploadTypeCode)) {
                minIOManager.deleteFile(upload);
            } else if (uploadTypeCode == null || LOCAL.equals(uploadTypeCode)) {
                String fileName = upload.getLocalPath() + upload.getObjectID().toString();
                File file = new File(fileName);
                if (!file.exists()) {
                    throw new IllegalArgumentException("File not found: " + fileName);
                }
                Files.delete(Path.of(fileName));
            }
        }
    }

    private boolean isUploadTypeAmazon(EdsUpload upload) {
        return upload.getType() != null && AMAZON.equals(upload.getType().getCode());
    }

    private boolean isUploadTypeMinIO(EdsUpload upload) {
        return upload.getType() != null && MINIO.equals(upload.getType().getCode());
    }

    @Override
    public String getResourceDirectory() {
        String userPath = "";
        String uploadDir = EdsContextParams.getUploadDir();

        EdsCompanySystemSettings settings = companySystemSettingsManager.findByCompanyID(Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId()));
        if (settings != null && settings.getUploadDir() != null) {
            uploadDir = settings.getUploadDir();
        }

        if (getUser() != null) {
            userPath = getUser().getObjectID().toString() + File.separator;
        }
        if (uploadDir != null) {
            return uploadDir + File.separator + ServerSecurityContext.getInstance().getCompanyId() + File.separator + userPath;
        } else {
            throw new IllegalArgumentException("Upload Resource path is not given");
        }
    }

    //Bu metod amazondagi static folderdan image larni parent iga qarab boshqa razmerdagilarini olib chiqadi
    @Override
    public EdsUpload getImageWithParentAndSize(EdsUpload parent, String size) {
        return (EdsUpload) findSingle("select u from EdsUpload u where u.parent is not null and u.parent = ? and u.imageSize = ?", parent, size);
    }

    @Override
    public Double getUploadStorage() {
        Long result = (Long) findSingle("select sum (upload.size) from EdsUpload upload");
        if (result != null) {
            return result / (1024d * 1024d * 1024d);
        } else {
            return Double.valueOf(0);
        }
    }

    public String getFileURL(Integer uploadId) {
        EdsUpload upload = get(uploadId);
        return getFileURL(upload, true);
    }

    public String getFileURL(Integer uploadId, boolean needHostForLocalFile) {
        EdsUpload upload = get(uploadId);
        return getFileURL(upload, needHostForLocalFile);
    }

    public String getFileURL(EdsUpload upload) {
        return getFileURL(upload, true);
    }

    public String getFileURL(EdsUpload upload, boolean needHostForLocalFile) {
        if (upload != null) {
            if (isUploadTypeAmazon(upload)) {
                EdsUploadAmazonSettings uploadAmazonSettings = uploadAmazonSettingsManager.getUploadAmazonSettings(upload);
                if (uploadAmazonSettings != null) {
                    try {
                        return amazonManager.getLink(uploadAmazonSettings);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (isUploadTypeMinIO(upload)) {
                EdsUploadMinIOSettings uploadMinIOSettings = uploadMinIOSettingsManager.getUploadMinIOSettings(upload);
                if (uploadMinIOSettings != null) {
                    try {
                        return minIOManager.getLink(uploadMinIOSettings);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                if (needHostForLocalFile) {
                    return EdsContextParams.getFullHost() + "common/downloadFile?id=" + upload.getObjectID() + "&" + C_ID + "=" + ServerSecurityContext.getInstance().getCompanyId() + "&dbname=" + ServerSecurityContext.getInstance().getDatabase();
                } else {
                    return "/common/downloadFile?id=" + upload.getObjectID() + "&" + C_ID + "=" + ServerSecurityContext.getInstance().getCompanyId() + "&dbname=" + ServerSecurityContext.getInstance().getDatabase();
                }
            }
        }
        return "";
    }

    @Override
    public EdsUpload getUploadByContentTypeAndName(String contentType, String name) {
        String searchContent = "%" + contentType + "%";
        String searchName = "%" + name + "%";
        return (EdsUpload) findSingle("SELECT u FROM EdsUpload u WHERE u.contentType LIKE ? AND u.originalName like ? order by id desc", searchContent, searchName);
    }

    public EdsUploadSettings getUploadSettings(EdsUpload upload) {
        if (isUploadTypeMinIO(upload)) {
            return uploadMinIOSettingsManager.getUploadMinIOSettings(upload);
        } else {
            return uploadAmazonSettingsManager.getUploadAmazonSettings(upload);
        }
    }

    public EdsUploadSettings getUploadSettingsByUploadId(Integer uploadId) {
        EdsUpload upload = get(uploadId);
        if (isUploadTypeMinIO(upload)) {
            return uploadMinIOSettingsManager.getUploadMinIOSettings(upload);
        } else {
            return uploadAmazonSettingsManager.getUploadAmazonSettings(upload);
        }
    }

    public String getUploadFileUrl(Integer uploadFileID) {
        EdsUpload upload = get(uploadFileID);
        if (isUploadTypeMinIO(upload)) {
            return uploadMinIOSettingsManager.getUploadFileUrl(uploadFileID);
        } else {
            return uploadAmazonSettingsManager.getUploadFileUrl(uploadFileID);
        }
    }

    public void createCopy(Integer uploadId, EdsUpload newUpload) {
        if (isUploadTypeMinIO(newUpload)) {
            EdsUploadMinIOSettings uploadSettings = uploadMinIOSettingsManager.getUploadMinIOSettingsByUploadId(uploadId);
            EdsUploadMinIOSettings eUMS = uploadSettings.cloneShallow();
            eUMS.setUpload(newUpload);
            uploadMinIOSettingsManager.create(eUMS);
        } else {
            EdsUploadAmazonSettings uploadSettings = uploadAmazonSettingsManager.getUploadAmazonSettingsByUploadId(uploadId);
            EdsUploadAmazonSettings eUAS = uploadSettings.cloneShallow();
            eUAS.setUpload(newUpload);
            uploadAmazonSettingsManager.create(eUAS);
        }
    }

    public String backupCompanyDocuments(Integer companyID) {
        if (Constants.MINIO.equals(EdsContextParams.getUploadType())) {
            return minIOManager.backupCompanyDocuments(companyID);
        } else {
            return amazonManager.backupCompanyDocuments(companyID);
        }
    }

    public void persistFile(EdsUploadSettings uploadSettings) {
        if (Constants.MINIO.equals(EdsContextParams.getUploadType())) {
            uploadMinIOSettingsManager.persist(uploadSettings);
        } else {
            uploadAmazonSettingsManager.persist(uploadSettings);
        }
    }

    public String putFileToPublicFolder(E obj, InputStream stream) throws IOException, NoSuchAlgorithmException {
        obj.setFolderName(CommandConstants.STATIC_FOLDER);
        createUpload(obj);
        if (isUploadTypeMinIO(obj)) {
            return minIOManager.putFile(obj, stream);
        } else {
            return amazonManager.putFile(obj, stream);
        }
    }

    public String putFile2(EdsUpload upload, DocumentItem item) throws IOException, NoSuchAlgorithmException {
        if (isUploadTypeMinIO(upload)) {
            return minIOManager.putFile2(upload, item);
        } else {
            return amazonManager.putFile2(upload, item);
        }
    }

    @Override
    public ArrayList<FileResource> getCompanyBackupFiles(EdsFolder folder, ListingFilterParameter parameter) {
        if (MINIO.equals(EdsContextParams.getUploadType())) {
            return minIOManager.getCompanyBackupFiles(folder, parameter);
        } else {
            return amazonManager.getCompanyBackupFiles(folder, parameter);
        }
    }
    @Override
    public Integer copyCompanyDocumentsSizeToUploadTable(Integer companyID) {
        if (MINIO.equals(EdsContextParams.getUploadType())) {
            return minIOManager.copyCompanyDocumentsSizeToUploadTable(companyID);
        } else {
            return amazonManager.copyCompanyDocumentsSizeToUploadTable(companyID);
        }
    }
    @Override
    public void saveXmlBackup(InputStream stream, String fileName) throws IOException, NoSuchAlgorithmException {
        if (MINIO.equals(EdsContextParams.getUploadType())) {
            minIOManager.saveXmlBackup(stream, fileName);
        } else {
            amazonManager.saveXmlBackup(stream, fileName);
        }
    }
}
