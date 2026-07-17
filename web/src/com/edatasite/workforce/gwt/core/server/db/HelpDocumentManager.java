package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsHelpDocument;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

/**
 * User: Dilshod Madrahimov
 * Date: 2/28/13
 * Time: 5:23 PM
 */
public interface HelpDocumentManager extends Manager<EdsHelpDocument> {

    List<EdsHelpDocument> getHelpDocumentList(ListingFilterParameter param);

    Integer getHelpDocumentTotalCount(ListingFilterParameter filterParameter);

    List<EdsHelpDocument> getHelpDocumentBySectionForm(String section, String view);

    EdsHelpDocument getHelpDocumentByFormCode(String formName);

    void deleteHelpDocument(Integer objectId);

    Boolean getExistHelpDocument(Integer objectID ,String form, String block);
}
