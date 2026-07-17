package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsUpload;
import com.edatasite.workforce.core.domain.EdsUploadMinIOSettings;

import java.util.List;

public interface UploadMinIOSettingsManager  extends Manager<EdsUploadMinIOSettings> {

    EdsUploadMinIOSettings getUploadMinIOSettings(EdsUpload upload);

    EdsUploadMinIOSettings getUploadMinIOSettingsByUploadId(Integer uploadId);

    List<EdsUploadMinIOSettings> getUploadMinIOSettingsList();

    List<EdsUploadMinIOSettings> getUploadMinIOSettingsListOnly(Integer companyID, Integer start, Integer limit);

    Long getUploadMinIOSettingsListSize(Integer companyID);

    String getUploadFileUrl(Integer uploadFileID);

    List<EdsUploadMinIOSettings> getNotExpiredLimited(Integer start, int limit);
}