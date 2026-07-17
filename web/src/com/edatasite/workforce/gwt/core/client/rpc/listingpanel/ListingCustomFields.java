package com.edatasite.workforce.gwt.core.client.rpc.listingpanel;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 15-Nov-2010
 * Time: 18:20:54
 */
public interface ListingCustomFields {

    String KANBAN_ORDER = "kanban_order";

    Object getCustomFieldsValue(String columnCodeKey);

    void setCustomFieldsValue(String columnCodeKey, Object cellValue);
}
