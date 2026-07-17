package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsSinxDocuments;
import com.edatasite.workforce.core.domain.EdsSinxDocumentsSettings;
import com.edatasite.workforce.core.domain.EdsUpload;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 15.01.2009
 * Time: 12:00:29
 * To change this template use File | Settings | File Templates.
 */
public interface SinxDocumentsSettingsManager extends Manager<EdsSinxDocumentsSettings> {

    EdsSinxDocumentsSettings getSinxDocsSettings(EdsUpload upload);

    List<EdsSinxDocumentsSettings> getGoogleDocSettings(EdsSinxDocuments googleDocuments);
}
