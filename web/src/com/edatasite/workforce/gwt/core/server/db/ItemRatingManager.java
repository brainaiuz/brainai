package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsItemRating;

public interface ItemRatingManager extends Manager<EdsItemRating> {

    Double calculateRating(Integer productID);

    EdsItemRating getRatingByUserAndProduct(Integer userID, Integer productID);
}