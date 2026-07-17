package com.edatasite.workforce.gwt.core.server.db.documents;

import com.edatasite.workforce.core.domain.documents.EdsCopiedFileHeader;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

/**
 * Created by : Faxriddin Taslimov  * Date: 30.09.2015
 */
public interface CopiedFileHeaderManager extends Manager<EdsCopiedFileHeader> {

    EdsCopiedFileHeader getFile(Integer folderId, String name);

    List<EdsCopiedFileHeader> list(ListingFilterParameter fp);

    boolean existsFile(Integer objectID, String name);

    List<Integer> getFileHeaders(ListingFilterParameter filter);

    EdsCopiedFileHeader getCopiedFile(Integer fileHeaderId, Integer folderId);

    List<EdsCopiedFileHeader> getCopiedFile(Integer fileHeaderId);

    List<EdsCopiedFileHeader> getListByFolderIdAndEntityId(ListingFilterParameter fp);
}