package com.edatasite.workforce.gwt.accounting.client.rpc.discount;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: Dec 3, 2010
 * Time: 4:42:52 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DiscountService extends RemoteService {

    DiscountList getDiscountList(ListingFilterParameter filterParametrs, ListLoadConfig config);

    DiscountItem getDiscountData(Integer objectID);

    Integer save(DiscountItem discountItem);

    Boolean deleteDiscount(Integer objectID);

    ListResult<DiscountItem> getDiscountList(ListingFilterParameter filterParameter);

    DiscountItem[] getDiscountListAsSelectItem();

    class App {
        public static DiscountServiceAsync get() {
            ServiceDefTarget target = GWT.create(DiscountService.class);
            target.setServiceEntryPoint(Utils.getRpcBaseUrl() + "/discount");
            return (DiscountServiceAsync) target;
        }
    }
}
