package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.workflow.EdsWebHookResponse;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

/**
 * User : Akhror
 * Date : 11.01.2022
 */
public interface WebHookResponseManager extends Manager<EdsWebHookResponse> {
    List<EdsWebHookResponse> list(ListingFilterParameter fp);

    List<EdsWebHookResponse> listByType(Integer typeId,String type);

    Integer getUnDeletedItemCount(Integer id);

}
