package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.settings.EdsEmailSetting;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.EmailSettingsManager;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: Mar 19, 2010
 * Time: 4:54:22 PM
 * To change this template use File | Settings | File Templates.
 */

@SuppressWarnings("unchecked")
@Repository("emailSettingsManager")
public class EmailSettingsManagerImpl extends BaseManager<EdsEmailSetting> implements EmailSettingsManager, Constants {

    public EmailSettingsManagerImpl() {
        super(EdsEmailSetting.class);
    }

    @Override
    public EdsEmailSetting getCompanyEmailSetting(Integer companyID) {
        companyID = companyID == null ? SecurityContext.getCompanyID() : companyID;
        if (companyID == null) return null;
        return (EdsEmailSetting) findNativeSingle("select es.* from \"" + companyID + "\".autoresponse es where es.deleted is not true and es.active is true " +
                "and es.companyEmail is true", EdsEmailSetting.class);
    }

    public EdsEmailSetting getEmailSetting(String email) {
        if (StringUtils.isEmpty(email)) {
            return null;
        } else if (email.contains("<") && email.contains(">")) {
            email = email.substring(email.lastIndexOf("<") + 1, email.lastIndexOf(">"));
        }
        try {
            return slaveEntityManager.createQuery("select es from EdsEmailSetting es where (es.deleted is null or es.deleted<>true) and es.email=:email"
                    , EdsEmailSetting.class).setParameter("email", email).setMaxResults(1).getSingleResult();
        } catch (NoResultException | EmptyResultDataAccessException ex) {
            return null;
        }
    }

    public EdsEmailSetting getActiveEmailSetting(String email) {
        if (StringUtils.isEmpty(email)) {
            return null;
        } else if (email.contains("<") && email.contains(">")) {
            email = email.substring(email.lastIndexOf("<") + 1, email.lastIndexOf(">"));
        }
        try {
            return slaveEntityManager.createQuery("select es from EdsEmailSetting es where es.active=true and (es.deleted is null or es.deleted<>true) and es.email=:email"
                    , EdsEmailSetting.class).setParameter("email", email).setMaxResults(1).getSingleResult();
        } catch (NoResultException | EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override
    public List<EdsEmailSetting> getAllActiveEmailSettings() {
        try {
            return slaveEntityManager.createQuery("select es from EdsEmailSetting es where es.active=true and (es.deleted is null or es.deleted<>true) and es.provider='DEFAULT'",
                    EdsEmailSetting.class).getResultList();
        } catch (Exception e) {
            System.out.println("CompanyId - " + ServerSecurityContext.getInstance().getCompanyId());
            return new ArrayList<>();
        }
    }

    @Override
    public List<String> getAllActiveEmails() {
        try {
            return slaveEntityManager.createQuery("select es.email from EdsEmailSetting es where es.active=true and (es.deleted is null or es.deleted<>true) and es.provider='DEFAULT'",
                    String.class).getResultList();
        } catch (Exception e) {
            System.out.println("CompanyId - " + ServerSecurityContext.getInstance().getCompanyId());
            return new ArrayList<>();
        }
    }

    @Override
    public void undefaultAccounts(Integer objectID, Integer userID) {
        masterEntityManager.createQuery("update EdsEmailSetting set defaultEmail = false where user.objectID=:userID and objectID!=:id").setParameter("userID", userID)
                .setParameter("id", objectID).executeUpdate();
    }

    @Override
    public void undoCompanyEmails(Integer objectID) {
        masterEntityManager.createQuery("update EdsEmailSetting set companyEmail = false where objectID!=:id").setParameter("id", objectID).executeUpdate();
    }

    @Override
    public EdsEmailSetting getUserDefaultEmailAccount() {
        try {
            return slaveEntityManager.createQuery("select es from EdsEmailSetting es where es.active=true and (es.deleted is null or es.deleted<>true) and es.defaultEmail=true and es.user=:owner"
                    , EdsEmailSetting.class).setParameter("owner", getUser()).setMaxResults(1).getSingleResult();
        } catch (NoResultException | EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Transactional
    @Override
    public void updateFetchingTimes(Integer emailSettingId, Date fetchingStartDate, Date date) {
        EdsEmailSetting settings = get(emailSettingId);
        settings.setLastFetchingStarted(fetchingStartDate);
        settings.setLastFetchingEnd(date);
        update(settings);
    }

    @Override
    public EdsEmailSetting getUserEmailAccount() {
        List<EdsEmailSetting> emailSettings = slaveEntityManager.createQuery("select e from EdsEmailSetting e where (e.deleted is null or e.deleted<>true) and e.user=:user " +
                "order by e.defaultEmail desc", EdsEmailSetting.class).setParameter("user", getUser()).setMaxResults(1).getResultList();
        return emailSettings.size() > 0 ? emailSettings.get(0) : null;
    }
}
