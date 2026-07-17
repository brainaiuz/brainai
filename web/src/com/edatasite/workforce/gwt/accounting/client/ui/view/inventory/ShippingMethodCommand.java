package com.edatasite.workforce.gwt.accounting.client.ui.view.inventory;

import com.edatasite.workforce.gwt.invoice.client.rpc.ShippingMethod;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 03.08.2010
 * Time: 15:41:58
 * To change this template use File | Settings | File Templates.
 */
public interface ShippingMethodCommand {
    void execute(ShippingMethod selectedID);
}
