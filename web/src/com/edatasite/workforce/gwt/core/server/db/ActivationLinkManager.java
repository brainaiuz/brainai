package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.workforce.appContext.SpringPropertiesUtil;
import com.edatasite.workforce.core.domain.ActivationLinkType;
import com.edatasite.workforce.core.domain.EdsActivationLink;

import java.util.Date;

/**
 * User: Murad
 * Date: 7/24/2018 5:54 PM
 */
public interface ActivationLinkManager extends Manager<EdsActivationLink> {

    EdsActivationLink getByKey(String key);

    default String saveActivationLink(Integer companyId, Integer userId, ActivationLinkType type) {
        if (companyId == null || userId == null) {
            return null;
        }
        if (type == null) {
            type = ActivationLinkType.USER_ACTIVATION;
        }
        final EdsActivationLink domain = new EdsActivationLink();

        String serviceId = SpringPropertiesUtil.getProperty("kpi.discovery.service-id");

        domain.setCompanyId(companyId);
        domain.setUserId(userId);
        domain.setLinkType(type);
        domain.setCreatedDate(new Date());
        domain.setKey(EncryptionHelper.encrypt("type=" + ActivationLinkType.USER_ACTIVATION +
                                               "&user_id=" + userId +
                                               "&company_id=" + companyId +
                                               "&service_id=" + serviceId +
                                               "&time=" + System.currentTimeMillis()));
//        domain.setKey(UUID.randomUUID().toString());

        create(domain);
        return EncryptionHelper.encodeURL(domain.getKey());
//        return domain.getKey();
    }

    String getOrCreate(Integer companyId, Integer userId, ActivationLinkType linkType);
}
