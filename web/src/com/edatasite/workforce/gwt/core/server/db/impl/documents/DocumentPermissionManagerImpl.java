package com.edatasite.workforce.gwt.core.server.db.impl.documents;

import com.edatasite.workforce.core.domain.documents.EdsDocumentPermission;
import com.edatasite.workforce.gwt.core.server.db.documents.DocumentPermissionManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 15.05.2010
 * Time: 13:30:38
 * To change this template use File | Settings | File Templates.
 */
@Repository("documentPermissionManager")
public class DocumentPermissionManagerImpl extends BaseManager<EdsDocumentPermission> implements DocumentPermissionManager {

    public DocumentPermissionManagerImpl() {
        super(EdsDocumentPermission.class);
    }
}