package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsSinxDocuments;
import com.edatasite.workforce.core.domain.EdsSinxDocumentsSettings;
import com.edatasite.workforce.core.domain.EdsUpload;
import com.edatasite.workforce.gwt.core.server.db.SinxDocumentsSettingsManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 15.01.2009
 * Time: 12:02:29
 * To change this template use File | Settings | File Templates.
 */
@Repository("sinxDocumentsSettingsManager")
public class GoogleDocumentsSettingsManagerImpl extends BaseManager<EdsSinxDocumentsSettings> implements SinxDocumentsSettingsManager {

    public GoogleDocumentsSettingsManagerImpl() {
        super(EdsSinxDocumentsSettings.class);
    }

    public EdsSinxDocumentsSettings getSinxDocsSettings(EdsUpload upload) {
        return (EdsSinxDocumentsSettings) findSingle("select gds from EdsSinxDocumentsSettings gds where gds.upload=?", upload);
    }

    public List<EdsSinxDocumentsSettings> getGoogleDocSettings(EdsSinxDocuments googleDocuments) {
        return find("select googDoc from EdsSinxDocumentsSettings googDoc where googDoc.sinxDocuments=?", googleDocuments);
    }
}
