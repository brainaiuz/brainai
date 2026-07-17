package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.emailfetching.EdsEmailFolder;

import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: Mar 19, 2010
 * Time: 4:53:29 PM
 * To change this template use File | Settings | File Templates.
 */
public interface EmailFolderManager extends Manager<EdsEmailFolder> {

    EdsEmailFolder getByURL(String folderURL, Integer emailSettingsID);

    void deleteFolders(Integer emailSettingID, Set<String> folders);

    List<EdsEmailFolder> getFoldersForFetch(Integer emailSettingID, boolean forListing);

    List<Integer> getFolderIdsForFetch(Integer emailSettingID, boolean forListing);
}