package com.edatasite.workforce.gwt.core.server.office365.services;

import com.edatasite.workforce.gwt.core.server.office365.resources.Office365AccessTokenDTO;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365BaseList;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365MessageFolder;

/**
 * Created by umakarimov on 9/29/15.
 */
public interface Office365MessageFolderService {
    Office365BaseList<Office365MessageFolder> getEmailFolders(Office365AccessTokenDTO token);

    Office365MessageFolder createEmailFolder(Office365AccessTokenDTO token, Office365MessageFolder folder);

    Office365MessageFolder updateEmailFolder(Office365AccessTokenDTO token, Office365MessageFolder folder);

    void deleteEmailFolder(Office365AccessTokenDTO token, Office365MessageFolder folder);

    Office365MessageFolder moveEmailFolder(
            Office365AccessTokenDTO token, Office365MessageFolder folder, String destinationId
    );

    Office365MessageFolder copyEmailFolder(
            Office365AccessTokenDTO token, Office365MessageFolder folder, String destinationId
    );
}
