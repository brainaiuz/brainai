package com.edatasite.workforce.gwt.core.server.db.impl.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsDiscountMultiRangeValue;
import com.edatasite.workforce.gwt.core.server.db.accounting.DiscountMultiRangeManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: Dec 7, 2010
 * Time: 12:36:17 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("discountMultiRangeManager")
public class DiscountMultiRangeManagerImpl extends BaseManager<EdsDiscountMultiRangeValue> implements DiscountMultiRangeManager {

    public DiscountMultiRangeManagerImpl() {
        super(EdsDiscountMultiRangeValue.class);
    }

    @Override
    public void deleteMultiRangeValuesByDiscount(Integer discountID) {
        String schema = SecurityContext.getInstance().getCompanyId();
        updateNative("DELETE FROM \"" + schema +"\".discount_multirange_values dmv WHERE dmv.discount_id = '" + discountID + "' ");
    }
}
