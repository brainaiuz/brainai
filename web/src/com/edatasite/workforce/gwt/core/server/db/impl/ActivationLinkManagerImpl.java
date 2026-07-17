package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.workforce.core.domain.ActivationLinkType;
import com.edatasite.workforce.core.domain.EdsActivationLink;
import com.edatasite.workforce.core.tools.StringUtil;
import com.edatasite.workforce.gwt.core.server.db.ActivationLinkManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * User: Murad
 * Date: 7/24/2018 5:55 PM
 */
@Repository
public class ActivationLinkManagerImpl extends BaseManager<EdsActivationLink> implements ActivationLinkManager {

    public ActivationLinkManagerImpl() {
        super(EdsActivationLink.class);
    }

    @Override
    public EdsActivationLink getByKey(String key) {
        if (StringUtil.isEmpty(key)) {
            return null;
        }
        /*TODO we should give the access to user to click one more time activation link
        (a.deleted is null or a.deleted <> true)" +
                           "        and*/
        final String sql = "select a from EdsActivationLink a " +
                           "    where a.companyId is not null" +
                           "        and a.userId is not null " +
                           "        and a.key = :activationKey";
        final List<EdsActivationLink> list = this.slaveEntityManager.createQuery(sql)
                                                               .setParameter("activationKey", key)
                                                               .setMaxResults(1)
                                                               .getResultList();
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public String getOrCreate(Integer companyId, Integer userId, ActivationLinkType linkType) {
        if (companyId == null || userId == null) {
            return null;
        }
        if (linkType == null) {
            linkType = ActivationLinkType.USER_ACTIVATION;
        }
        final String sql = "select al from EdsActivationLink al " +
                           "    where (al.deleted is null or al.deleted <> true)" +
                           "        and al.companyId = :companyId" +
                           "        and al.userId = :userId" +
                           "        and al.linkType = :linkType";
        final List<EdsActivationLink> list = this.slaveEntityManager.createQuery(sql)
                                                               .setParameter("companyId", companyId)
                                                               .setParameter("userId", userId)
                                                               .setParameter("linkType", linkType)
                                                               .setMaxResults(1)
                                                               .getResultList();
        return list.isEmpty() ? saveActivationLink(companyId, userId, linkType) : EncryptionHelper.encodeURL(list.get(0).getKey());
    }
}
