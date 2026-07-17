package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsGoogleCheckoutOrder;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Apr 21, 2011
 * Time: 2:05:15 AM
 * To change this template use File | Settings | File Templates.
 */
public interface GoogleCheckoutOrderManager extends Manager<EdsGoogleCheckoutOrder> {

    EdsGoogleCheckoutOrder getByOrderNumber(String orderNumber);
}
