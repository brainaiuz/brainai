package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsItemRating;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.ItemRatingManager;
import org.springframework.stereotype.Repository;

@Repository("itemRatingManager")
public class ItemRatingManagerImpl extends BaseManager<EdsItemRating> implements ItemRatingManager, Constants, AccountingConstants {

    public ItemRatingManagerImpl() {
        super(EdsItemRating.class);
    }

    @Override
    public Double calculateRating(Integer productID) {
        return (Double) findSingle("SELECT round(avg(r.rating)) FROM EdsItemRating r WHERE r.item.objectID = ?", productID);
    }

    @Override
    public EdsItemRating getRatingByUserAndProduct(Integer userID, Integer productID) {
        return (EdsItemRating) findSingle("from EdsItemRating r WHERE r.user.objectID = ? and r.item.objectID = ?", userID, productID);
    }
}