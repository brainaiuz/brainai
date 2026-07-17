package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.accounting.EdsShippingDataItem;

import java.util.List;
import java.util.Map;

/**
 * User: Murad Satimov
 * Date: 1/9/18 8:26 PM
 */
public interface ShippingDataItemManager extends Manager<EdsShippingDataItem> {

    List<EdsShippingDataItem> findByShippingDataId(Integer shippingDataId);

    List<String> getGDNShippingLabels(EdsShippingDataItem sdi);

    Map<Integer, String> getGDNShippingLabelsBySdiIds(List<Integer> sdiIds);
}
