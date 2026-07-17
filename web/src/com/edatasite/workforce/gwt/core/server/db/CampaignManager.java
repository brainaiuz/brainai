package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.crm.EdsCampaign;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 08-Jul-2009
 * Time: 19:09:00
 * To change this template use File | Settings | File Templates.
 */
public interface CampaignManager extends Manager<EdsCampaign> {
    List<EdsCampaign> getCampaignList(ListingFilterParameter fp);

    EdsCampaign getCampaignByName(String name);

    Integer getCampaignListCount(ListingFilterParameter fp);

    List<String> getCampaignNames();

    List<Object[]> getList();

    void setCampaignsDeletedTrue(String ids);
}
