package com.edatasite.workforce.gwt.core.server.db.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsDiscount;
import com.edatasite.workforce.gwt.accounting.client.rpc.discount.DiscountItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: Dec 3, 2010
 * Time: 5:06:53 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DiscountManager extends Manager<EdsDiscount> {

    List<EdsDiscount> list(ListingFilterParameter filterParametrs, ListLoadConfig config);

    List<EdsDiscount> list(ListingFilterParameter filterParameter);

    EdsDiscount getDiscountByDiscountItem(DiscountItem discountItem);

	Integer listCount(ListingFilterParameter filterParametrs);
}
