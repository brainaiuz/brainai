package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsLayout;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 1/25/12
 * Time: 5:39 PM
 * To change this template use File | Settings | File Templates.
 */
public interface LayoutManager extends Manager<EdsLayout> {
    String getLayoutHTML(String type);

    String getCustomLayout(String type);

    String getDefaultLayout(String type);

    EdsLayout getLayout(String formID, String formType);

    EdsLayout getCustomLayout(String formID, String formType);

    EdsLayout getDefaultLayout(String formID, String formType);

    EdsLayout get(Integer companyID, Integer layoutID);

    List<EdsLayout> list(ListingFilterParameter param);

    Integer listCount(ListingFilterParameter filterParameter);
}
