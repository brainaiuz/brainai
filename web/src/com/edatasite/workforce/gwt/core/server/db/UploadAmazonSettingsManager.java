package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsUpload;
import com.edatasite.workforce.core.domain.EdsUploadAmazonSettings;

import java.util.List;

public interface UploadAmazonSettingsManager extends Manager<EdsUploadAmazonSettings> {

    EdsUploadAmazonSettings getUploadAmazonSettings(EdsUpload upload);

    EdsUploadAmazonSettings getUploadAmazonSettingsByUploadId(Integer uploadId);

	List<EdsUploadAmazonSettings> getUploadAmazonSettingsList();

    List<EdsUploadAmazonSettings> getUploadAmazonSettingsListOnly(Integer companyID, Integer start, Integer limit);

    Long getUploadAmazonSettingsListSize(Integer companyID);

    String getUploadFileUrl(Integer uploadFileID);

    List<EdsUploadAmazonSettings> getNotExpiredLimited(Integer start, int limit);
}
