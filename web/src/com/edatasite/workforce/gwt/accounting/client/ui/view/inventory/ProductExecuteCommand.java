package com.edatasite.workforce.gwt.accounting.client.ui.view.inventory;

import com.edatasite.workforce.gwt.accounting.client.rpc.ProductSelectItem;

/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 3/9/13
 * Time: 4:15 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ProductExecuteCommand {
    void execute(ProductSelectItem selectedID);
}
