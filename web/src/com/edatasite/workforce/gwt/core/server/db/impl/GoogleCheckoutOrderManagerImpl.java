package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsGoogleCheckoutOrder;
import com.edatasite.workforce.gwt.core.server.db.GoogleCheckoutOrderManager;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Apr 21, 2011
 * Time: 2:06:40 AM
 * To change this template use File | Settings | File Templates.
 */
@Repository("googleCheckoutOrderManager")
public class GoogleCheckoutOrderManagerImpl extends BaseManager<EdsGoogleCheckoutOrder> implements GoogleCheckoutOrderManager {

    public GoogleCheckoutOrderManagerImpl() {
        super(EdsGoogleCheckoutOrder.class);
    }

    @Override
    public EdsGoogleCheckoutOrder getByOrderNumber(String orderNumber) {
        EdsGoogleCheckoutOrder order = (EdsGoogleCheckoutOrder) findSingle(" FROM EdsGoogleCheckoutOrder o WHERE o.orderNumber='" + orderNumber + "'");
        if (order != null){
            return order;
        } else {
            return null;
        }
    }
}
