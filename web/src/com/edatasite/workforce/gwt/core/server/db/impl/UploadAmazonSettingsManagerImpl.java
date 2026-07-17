package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsUpload;
import com.edatasite.workforce.core.domain.EdsUploadAmazonSettings;
import com.edatasite.workforce.gwt.core.server.db.UploadAmazonSettingsManager;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository("uploadAmazonSettingsManager")
public class UploadAmazonSettingsManagerImpl extends BaseManager<EdsUploadAmazonSettings> implements UploadAmazonSettingsManager {

    public UploadAmazonSettingsManagerImpl() {
        super(EdsUploadAmazonSettings.class);
    }

    public EdsUploadAmazonSettings getUploadAmazonSettings(EdsUpload upload) {
        return (EdsUploadAmazonSettings) findSingle("select uas from EdsUploadAmazonSettings" +
                " uas where uas.upload =?", upload);
    }

    @Override
    public EdsUploadAmazonSettings getUploadAmazonSettingsByUploadId(Integer uploadId) {
        return (EdsUploadAmazonSettings) findNativeSingle("select uas.* from " + getCompanyId() + ".uploadamazonsettings uas where uas.uploadid=" + uploadId, EdsUploadAmazonSettings.class);
    }

	public List<EdsUploadAmazonSettings> getUploadAmazonSettingsList() {
		return findNative("select * from " + getCompanyId() + ".uploadamazonsettings order by id asc", EdsUploadAmazonSettings.class);
	}

    public List<EdsUploadAmazonSettings> getUploadAmazonSettingsListOnly(Integer companyID, Integer start, Integer limit){
        String companyId = getCompanyId();
        return findInterval("select uas from EdsUploadAmazonSettings uas", start, limit);
    }

    public Long getUploadAmazonSettingsListSize(Integer companyID) {
        String companyId = getCompanyId();
        String sql = "select count (id) from " + getCompanyId() + ".uploadamazonsettings";
        return Long.getLong(findNativeSingle(sql).toString());
    }

    @Override
    public String getUploadFileUrl(Integer uploadFileID) {
        if(uploadFileID==null || uploadFileID.intValue()<=0) {
            return null;
        }
        EdsUploadAmazonSettings edsUploadAmazonSettings = (EdsUploadAmazonSettings) findSingle("select fa from EdsUploadAmazonSettings fa where fa.upload.objectID=?", uploadFileID);
        return edsUploadAmazonSettings == null ? null : edsUploadAmazonSettings.getFileLink();
    }

    @Override
    public List<EdsUploadAmazonSettings> getNotExpiredLimited(Integer start, int limit) {
        if (start == null || start < 0) {
            return Collections.emptyList();
        }
        if (limit <= 0) {
            limit = 100;
        }
        final String sql = "select uas from EdsUploadAmazonSettings uas" +
                           "    join uas.upload u " +
                           "    where uas.expireDate <= current_date " +
                           "          and uas.upload is not null" +
                           "          and uas.objectID > :startingPoint" +
                           "    order by uas.objectID asc ";

        return slaveEntityManager.createQuery(sql, EdsUploadAmazonSettings.class)
                            .setParameter("startingPoint", start)
                            .setMaxResults(limit)
                            .getResultList();
    }
}
