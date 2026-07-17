package com.edatasite.workforce.gwt.core.server.db.webforms;

import com.edatasite.workforce.core.domain.webforms.EdsWebForm;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: Jul 29, 2010
 * Time: 7:02:17 PM
 * To change this template use File | Settings | File Templates.
 */
public interface WebFormManager extends Manager<EdsWebForm> {
    List<EdsWebForm> list(ListingFilterParameter filterParametrs);

//    EdsWebForm getByEncreptedLink(String url);

    List<EdsWebForm> getCompanyWebFormsIncludeDeleteds();

    void updateUrl(EdsWebForm webForm);

    Integer getListCount(ListingFilterParameter filterParametr);

    EdsWebForm getWebFormByCustomLayout(Integer customLayoutID);
}
