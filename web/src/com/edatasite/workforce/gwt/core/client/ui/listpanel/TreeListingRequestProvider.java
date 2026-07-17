package com.edatasite.workforce.gwt.core.client.ui.listpanel;

import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.treetable.TreeItem;

/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 7/25/12
 * Time: 10:14 PM
 * To change this template use File | Settings | File Templates.
 */
public interface TreeListingRequestProvider<T> {
    void getRequest(Object treeItem, final TreeItem item, ListingFilterParameter filterParameter, TreeListingCallBack callback);
}
