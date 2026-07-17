package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsServerUploadHistory;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: Apr 25, 2011
 * Time: 4:26:33 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ServerUploadHistoryManager extends Manager<EdsServerUploadHistory> {

    String getLatestUploadVersion();
}
