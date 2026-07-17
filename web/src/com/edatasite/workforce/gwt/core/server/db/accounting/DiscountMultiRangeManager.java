package com.edatasite.workforce.gwt.core.server.db.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsDiscountMultiRangeValue;
import com.edatasite.workforce.gwt.core.server.db.Manager;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: Dec 7, 2010
 * Time: 12:35:16 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DiscountMultiRangeManager extends Manager<EdsDiscountMultiRangeValue> {

    void deleteMultiRangeValuesByDiscount(Integer discountID);
}
