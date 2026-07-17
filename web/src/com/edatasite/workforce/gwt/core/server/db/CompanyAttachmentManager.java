package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsCompanyAttachment;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

import java.util.List;

public interface CompanyAttachmentManager extends Manager<EdsCompanyAttachment> {

    List<EdsCompanyAttachment> getCompanyAttachments(EdsCompany company, EdsReference logoType);

    String getCompanyLogoUrl(EdsCompany company, String type);

    String getCompanyLogoUrl(EdsCompany company, String type, String imageSize);

    EdsUser getUser();

    SelectItem getCompanyLogo(EdsCompany company, String logoType);

    String getCompanyStampUrl(EdsCompany company, String stampType);
}
