package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.emailfetching.EdsEmailFolder;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.EmailFolderManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: Mar 19, 2010
 * Time: 4:54:22 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("emailFolderManager")
public class EmailFolderManagerImpl extends BaseManager<EdsEmailFolder> implements EmailFolderManager, Constants {

    public EmailFolderManagerImpl() {
        super(EdsEmailFolder.class);
    }

    @Override
    public EdsEmailFolder getByURL(String folderURL, Integer emailSettingsID) {
        if (StringUtils.isEmpty(folderURL)) {
            return null;
        }
        try {
            return slaveEntityManager.createQuery("select f from EdsEmailFolder f where (f.deleted<>true or f.deleted is null) and f.emailSetting.objectID=:settingID " +
                    "and url=:folderURL", EdsEmailFolder.class).setParameter("settingID", emailSettingsID)
                    .setParameter("folderURL", folderURL).getSingleResult();
        } catch (NoResultException | EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override
    public void deleteFolders(Integer emailSettingID, Set<String> folders) {
        Query query = masterEntityManager.createQuery("update EdsEmailFolder set deleted = true, fetchable = false where emailSetting.objectID=:settingID " +
                (folders != null ? "and fullName not in (:folders)" : ""));
        query.setParameter("settingID", emailSettingID);
        if (folders != null) {
            query.setParameter("folders", folders);
        }
        query.executeUpdate();
    }

    @Override
    public List<EdsEmailFolder> getFoldersForFetch(Integer emailSettingID, boolean forListing) {
        return slaveEntityManager.createQuery("select f from EdsEmailFolder f where (f.deleted<>true or f.deleted is null) and f.fetchable = true " +
                /*and (f.type is null or f.type!=:folderType) */" and f.emailSetting.objectID =:settingID order by " + (forListing ? "f.type, f.name" : "f.lastFetchedDate"), EdsEmailFolder.class)
                /*.setParameter("folderType", MCFolderType.DRAFT)*/.setParameter("settingID", emailSettingID).getResultList();
    }

    @Override
    public List<Integer> getFolderIdsForFetch(Integer emailSettingID, boolean forListing) {
        return slaveEntityManager.createQuery("select f.objectID from EdsEmailFolder f where (f.deleted<>true or f.deleted is null) and f.fetchable = true " +
                " and f.emailSetting.objectID =:settingID order by " + (forListing ? "f.type, f.name" : "f.objectID"/*"f.lastFetchedDate"*/), Integer.class)
                .setParameter("settingID", emailSettingID).getResultList();
    }
}