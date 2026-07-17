package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsUpload;
import com.edatasite.workforce.core.domain.EdsUploadAmazonSettings;
import com.edatasite.workforce.core.domain.EdsUploadMinIOSettings;
import com.edatasite.workforce.gwt.core.server.db.UploadMinIOSettingsManager;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository("uploadMinIOSettingsManager")
public class UploadMinIOSettingsManagerImpl  extends BaseManager<EdsUploadMinIOSettings> implements UploadMinIOSettingsManager {

    public UploadMinIOSettingsManagerImpl() {
        super(EdsUploadMinIOSettings.class);
    }

    public EdsUploadMinIOSettings getUploadMinIOSettings(EdsUpload upload) {
        return (EdsUploadMinIOSettings) findSingle("select ums from EdsUploadMinIOSettings" +
                " ums where ums.upload =?", upload);
    }

    @Override
    public EdsUploadMinIOSettings getUploadMinIOSettingsByUploadId(Integer uploadId) {
        return (EdsUploadMinIOSettings) findNativeSingle("select uas.* from " + getCompanyId() + ".uploadamazonsettings uas where uas.uploadid=" + uploadId, EdsUploadAmazonSettings.class);
    }

    public List<EdsUploadMinIOSettings> getUploadMinIOSettingsList() {
        return findNative("select * from " + getCompanyId() + ".uploadminiosettings order by id asc", EdsUploadAmazonSettings.class);
    }

    public List<EdsUploadMinIOSettings> getUploadMinIOSettingsListOnly(Integer companyID, Integer start, Integer limit){
        String companyId = getCompanyId();
        return findInterval("select ums from EdsUploadMinIOSettings ums", start, limit);
    }

    public Long getUploadMinIOSettingsListSize(Integer companyID) {
        String sql = "select count (id) from " + getCompanyId() + ".uploadminiosettings";
        return Long.getLong(findNativeSingle(sql).toString());
    }

    @Override
    public String getUploadFileUrl(Integer uploadFileID) {
        if(uploadFileID==null || uploadFileID.intValue()<=0) {
            return null;
        }
        EdsUploadMinIOSettings edsUploadMinIOSettings = (EdsUploadMinIOSettings) findSingle("select fa from EdsUploadMinIOSettings fa where fa.upload.objectID=?", uploadFileID);
        return edsUploadMinIOSettings == null ? null : edsUploadMinIOSettings.getFileLink();
    }

    @Override
    public List<EdsUploadMinIOSettings> getNotExpiredLimited(Integer start, int limit) {
        if (start == null || start < 0) {
            return Collections.emptyList();
        }
        if (limit <= 0) {
            limit = 100;
        }
        final String sql = "select ums from EdsUploadMinIOSettings ums" +
                "    join ums.upload u " +
                "    where ums.expireDate <= current_date " +
                "          and ums.upload is not null" +
                "          and ums.objectID > :startingPoint" +
                "    order by ums.objectID asc ";

        return slaveEntityManager.createQuery(sql, EdsUploadMinIOSettings.class)
                .setParameter("startingPoint", start)
                .setMaxResults(limit)
                .getResultList();
    }
}