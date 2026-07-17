package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.crm.EdsSmsSendItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Virus
 * Date: 7/20/11
 * Time: 7:00 PM
 * To change this template use File | Settings | File Templates.
 */
public interface SmsSendItemManager extends  Manager<EdsSmsSendItem>{
    List<EdsSmsSendItem> getSmsList(Integer id);

    List<EdsSmsSendItem> getSMSBy(ListingFilterParameter fp);
}
