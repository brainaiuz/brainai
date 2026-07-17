package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsCompanyAttachment;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsUpload;
import com.edatasite.workforce.core.tools.StringUtil;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.server.db.CompanyAttachmentManager;
import com.edatasite.workforce.gwt.core.server.db.UploadManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("companyAttachmentManager")
public class CompanyAttachmentManagerImpl extends UploadManagerImpl<EdsCompanyAttachment>
        implements CompanyAttachmentManager {

    @Autowired
    private UploadManager uploadManager;

    public List<EdsCompanyAttachment> getCompanyAttachments(EdsCompany company, EdsReference logoType) {
        return find("select ca from EdsCompanyAttachment ca where ca.logoType=? and (ca.deleted is false or ca.deleted is null) order by ca.objectID desc", logoType);
    }

    public String getCompanyLogoUrl(EdsCompany company, String type, String imageSize) {
        EdsReference logoType = referenceManager.findReference(CommandConstants._LOGO_TYPE, type);
        EdsUpload attachment = (EdsCompanyAttachment) findSingle("select ca from EdsCompanyAttachment ca where ca.logoType=? and ca.parent is null and (ca.imageSize=? or ca.imageSize is null) order by ca.objectID desc ", logoType, imageSize);
        return uploadManager.getFileURL(attachment);
    }

    public String getCompanyLogoUrl(EdsCompany company, String type) {
        return getCompanyLogoUrl(company, type, IMAGE_SIZE_SMALL);
    }

    public SelectItem getCompanyLogo(EdsCompany company, String type) {
        EdsReference logoType = referenceManager.findReference(CommandConstants._LOGO_TYPE, type);
        EdsUpload attachment = (EdsCompanyAttachment) findSingle("select ca from EdsCompanyAttachment ca where ca.logoType=? and ca.parent is null and ca.imageSize=? order by ca.objectID desc", logoType, IMAGE_SIZE_SMALL);
        if (attachment == null) return null;
        String url = uploadManager.getFileURL(attachment);
        if (!StringUtil.isEmpty(url)) {
            return new SelectItem(attachment.getObjectID(), url);
        }
        return null;
    }

    @Override
    public String getCompanyStampUrl(EdsCompany company, String type) {
        EdsReference stampType = referenceManager.findReference(CommandConstants._LOGO_TYPE, type);
        EdsUpload attachment = (EdsCompanyAttachment) findSingle("select ca from EdsCompanyAttachment ca where ca.logoType=? and ca.parent is null and (ca.imageSize=? or ca.imageSize is null) order by ca.objectID desc", stampType, IMAGE_SIZE_SMALL);
        String url = null;
        if (attachment == null) return url;
        return uploadManager.getFileURL(attachment);
    }

    public CompanyAttachmentManagerImpl() {
        super(EdsCompanyAttachment.class);
    }

}
