package com.edatasite.workforce.gwt.core.server.db.impl.rbac.email;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.rbac.EdsRelationship;
import com.edatasite.workforce.core.domain.rbac.EdsTrusteeType;
import com.edatasite.workforce.core.domain.rbac.email.EdsUserEmailRbac;
import com.edatasite.workforce.core.domain.settings.EdsEmailSetting;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Azazello
 * Date: 05.03.18
 * Time: 00:00
 * To change this template use File | Settings | File Templates.
 */
@Repository("userEmailRbacManager")
public class UserEmailRbacManagerImpl extends BaseManager<EdsUserEmailRbac> implements UserEmailRbacManager {

    public UserEmailRbacManagerImpl() {
        super(UserEmailRbacManagerImpl.class);
    }

    @Override
    public List<EdsUserEmailRbac> getEmailRbacEntries(Integer settingID) {
        return slaveEntityManager.createQuery("select distinct ti from EdsUserEmailRbac ti where ti.emailSetting.objectID=:settingID", EdsUserEmailRbac.class)
                .setParameter("settingID", settingID).getResultList();
    }

    @Override
    public List<EdsEmailSetting> getSharedEmailAccounts(boolean onlyActive) {
        EdsUser user = getUser();

        if (user == null) {
            return new ArrayList<>();
        }

        return slaveEntityManager.createQuery("select distinct rbac.emailSetting from EdsUserEmailRbac rbac where (rbac.user.objectID=:userID or rbac.group.objectID in " +
                "(select distinct gg.objectID from EdsGroup gg join gg.members memb where memb.objectID in " +
                "(select distinct t.objectID from EdsTrustee t where t.trusteeID=:userID and t.type.objectID=:trusteeType))) " +
                "and rbac.emailSetting.deleted is not true " + (onlyActive ? " and rbac.emailSetting.active is true" : ""), EdsEmailSetting.class)
                .setParameter("userID", user.getObjectID())
                .setParameter("trusteeType", EdsTrusteeType.USER)
                .getResultList();
    }

    @Transactional
    public void createEmailOwnerRbacEntry(EdsEmailSetting emailSetting) {
        removeEmailEntries(emailSetting.getObjectID());
        EdsUserEmailRbac emailRbac = new EdsUserEmailRbac();
        emailRbac.setEmailSetting(emailSetting);
        emailRbac.setUser(emailSetting.getUser());
        emailRbac.setTrusteeType(EdsTrusteeType.USER);
        emailRbac.setRelationship(EdsRelationship.EMAIL_OWNER);
        create(emailRbac);
    }

    @Transactional
    public void removeEmailEntries(Integer settingID) {
        masterEntityManager.createQuery("delete from EdsUserEmailRbac emailRbac where emailRbac.emailSetting.objectID=:settingID")
                .setParameter("settingID", settingID).executeUpdate();
    }

    @Override
    public List<EdsEmailSetting> getSharedUserEmailsForUser(EdsUser user) {
        Map params = new HashMap();
        if (user == null) {
            user = getUser();
        }
        params.put("userID", user.getObjectID());
        params.put("trusteeType", EdsTrusteeType.USER);
        String checkActiveAndDelete = new StringBuffer(" and ").append(ServerUtils.checkForDeleted("rbac.emailSetting.deleted")).append(" and ").append("rbac.emailSetting.active IS TRUE").toString();

        return (List<EdsEmailSetting>) findByNamedParams("select distinct rbac.emailSetting from EdsUserEmailRbac rbac where (rbac.user.objectID=:userID or rbac.group.objectID in " +
                "(select distinct gg.objectID from EdsGroup gg join gg.members memb where memb.objectID in (select distinct t.objectID from EdsTrustee t where t.trusteeID=:userID and t.type.objectID=:trusteeType)))" +
                checkActiveAndDelete, params);
    }
}
