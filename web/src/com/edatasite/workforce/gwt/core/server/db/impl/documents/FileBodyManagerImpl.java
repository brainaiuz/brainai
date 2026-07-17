package com.edatasite.workforce.gwt.core.server.db.impl.documents;

import com.edatasite.workforce.core.domain.documents.EdsFileBody;
import com.edatasite.workforce.gwt.core.server.db.documents.FileBodyManager;
import com.edatasite.workforce.gwt.core.server.db.impl.UploadManagerImpl;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 15.05.2010
 * Time: 13:30:38
 * To change this template use File | Settings | File Templates.
 */
@Repository("fileBodyManager")
public class FileBodyManagerImpl extends UploadManagerImpl<EdsFileBody> implements FileBodyManager {

    public FileBodyManagerImpl() {
        super(EdsFileBody.class);


    }

    @Override
    public EdsFileBody getFileVersion(Integer fileId, int version) {
        Map params = new HashMap();
        params.put("version", version);
        params.put("fileId", fileId);
        return (EdsFileBody) findSingleByNamedParams("select f from EdsFileBody f " +
                "where f.header.objectID=:fileId and f.version=:version", params);
    }

    @Override
    public Long getFileSize(Integer userId) {
        Map params = new HashMap();
        params.put("ownerId", userId);
        Long singleResult = (Long) findSingleByNamedParams("select sum(f.fileSize) from EdsFileBody f where f.header.owner.objectID=:ownerId", params);
        if (singleResult == null) {
            singleResult = 0L;
        }
        return singleResult;

    }

    public void createUpload(EdsFileBody body) {
        super.createUpload(body);
    }
}