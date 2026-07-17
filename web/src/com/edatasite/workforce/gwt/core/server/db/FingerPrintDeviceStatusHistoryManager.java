package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsFingerPrintDeviceStatusHistory;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

/**
 * Created by dilsh0d on 21.04.2016.
 */
public interface FingerPrintDeviceStatusHistoryManager extends Manager<EdsFingerPrintDeviceStatusHistory> {

    List<EdsFingerPrintDeviceStatusHistory> getList(ListingFilterParameter fp);

    Integer getListTotal(ListingFilterParameter fp);

}
