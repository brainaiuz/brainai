package com.edatasite.workforce.gwt.core.server.db.documents;

import com.edatasite.workforce.core.domain.documents.EdsFileBody;
import com.edatasite.workforce.gwt.core.server.db.Manager;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 15.05.2010
 * Time: 13:29:32
 * To change this template use File | Settings | File Templates.
 */
public interface FileBodyManager extends Manager<EdsFileBody> {

    /**
     * Fetch the file body with the specified version number.
     *
     * @param fileId  the ID of the file header
     * @param version the version number
     * @return the file body
     */
    EdsFileBody getFileVersion(Integer fileId, int version);

    Long getFileSize(Integer userId);

    void createUpload(EdsFileBody body);
}