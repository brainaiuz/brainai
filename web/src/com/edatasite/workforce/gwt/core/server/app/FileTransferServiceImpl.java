package com.edatasite.workforce.gwt.core.server.app;

import com.edatasite.workforce.core.domain.EdsCompanySystemSettings;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsUpload;
import com.edatasite.workforce.core.domain.EdsUploadAmazonSettings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.CompanySystemSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.UploadAmazonSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.UploadManager;
import com.edatasite.workforce.utils.EdsContextParams;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

/**
 * User: Murad Satimov
 * Date: 8/27/17 5:12 PM
 */
@Service
public class FileTransferServiceImpl implements FileTransferService {

    private static final Logger log = LoggerFactory.getLogger(FileTransferServiceImpl.class);


    @Autowired
    private UploadManager uploadManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private UploadAmazonSettingsManager uploadAmazonSettingsManager;
    @Autowired
    private CompanySystemSettingsManager companySystemSettingsManager;

    @Override
    @Transactional
    public Integer transferFilesLimitedToLocalStorageFromAmazon(Integer start, int limit, String uploadPath) {
        if (ServerUtils.isNullOrEmpty(uploadPath)) {
            throw new RuntimeException("Local upload path doesn't exists!!!!");
        }
        if (limit <= 0) {
            limit = 100;
        }
        final List<EdsUploadAmazonSettings> list = uploadAmazonSettingsManager.getNotExpiredLimited(start, limit);

        if (list.isEmpty()) {
            return -1;
        }
        final EdsReference localUploadType = referenceManager.findReference(Constants._UPLOAD_TYPE, Constants.LOCAL);

        for (EdsUploadAmazonSettings amazonSettings : list) {
            String key = amazonSettings.getAccessKey();
            final EdsUpload upload = amazonSettings.getUpload();

            if (ServerUtils.isNullOrEmpty(key) || upload == null) {
                continue;
            }
            if (key.matches("^c[0-9]*/.*$")) {
                key = key.replaceFirst("^c[0-9]*/", "");
            }
            log.info("-----------------------Importing file: " + upload.getName() + ", with amazon key: " + key + "-------------------------------------");
            final Path copiedFile = Paths.get(uploadPath + key);

            if (!Files.exists(copiedFile)) {
                log.info("!!!!!!!!!!!!!!!File: " + uploadPath + key + " NOT FOUND!!!!!!!!!!!!!!!!!!1");
                continue;
            }
            String patToFile = uploadPath + key;

            if (key.lastIndexOf("/") != -1) {
                patToFile = uploadPath + key.substring(0, key.lastIndexOf("/") + 1);
            }
            try {
                Files.move(copiedFile,
                           copiedFile.resolveSibling(patToFile + upload.getObjectID()),
                           StandardCopyOption.REPLACE_EXISTING);

            } catch (IOException e) {
                log.error("Error while renaming file: " + e.getMessage());
                continue;
            }

            upload.setLocalPath(patToFile);
            upload.setType(localUploadType);
            uploadManager.update(upload);
        }
        return list.isEmpty() ? -1 : list.get(list.size() - 1).getObjectID();
    }

    @Override
    @Transactional
    public String getLocalUploadDirectory() {
        final String companyId = ServerSecurityContext.getInstance().getCompanyId();

        if (ServerUtils.isNullOrEmpty(companyId)) {
            throw new RuntimeException("CompanyId not found");
        }
        String uploadDir = EdsContextParams.getUploadDir();

        final EdsCompanySystemSettings settings = companySystemSettingsManager.findByCompanyID(Integer.valueOf(companyId));

        if (settings != null && !ServerUtils.isNullOrEmpty(settings.getUploadDir())) {
            uploadDir = settings.getUploadDir();
        }
        if (uploadDir != null) {
            return uploadDir + File.separator + companyId + File.separator;
        } else {
            throw new IllegalArgumentException("Upload Resource path is not given");
        }
    }
}
