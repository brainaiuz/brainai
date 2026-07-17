package com.edatasite.workforce.gwt.core.server.db.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsBrand;
import com.edatasite.workforce.gwt.accounting.client.rpc.BrandItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Jun 17, 2010
 * Time: 2:35:57 AM
 * To change this template use File | Settings | File Templates.
 */
public interface BrandManager extends Manager<EdsBrand> {
    List<EdsBrand> getBrandList(ListingFilterParameter fp);

    int getBrandListCount(ListingFilterParameter fp);

    EdsBrand getBrandByName(String brandName);

    Boolean checkIfBrandExists(BrandItem brandItem);

}