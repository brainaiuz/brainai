package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsServerUploadHistory;
import com.edatasite.workforce.gwt.core.server.db.ServerUploadHistoryManager;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: Apr 25, 2011
 * Time: 4:23:49 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("serverUploadHistoryManager")
public class ServerUploadHistoryManagerImpl extends BaseManager<EdsServerUploadHistory> implements ServerUploadHistoryManager {

    public ServerUploadHistoryManagerImpl() {
        super(EdsServerUploadHistory.class);
    }

    public String getLatestUploadVersion() {
        EdsServerUploadHistory history =  (EdsServerUploadHistory) findSingle(
                                           "FROM EdsServerUploadHistory h ORDER BY h.date DESC");
        if (history == null) {
            return null;
        }
        
        return history.getVersion();
    }
}
