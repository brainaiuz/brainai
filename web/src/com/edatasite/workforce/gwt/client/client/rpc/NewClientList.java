package com.edatasite.workforce.gwt.client.client.rpc;

import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: Jun 2, 2011
 * Time: 3:34:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class NewClientList extends ListResult<CrmAccountItem> {

    public NewClientList() {
    }

    public NewClientList(ArrayList<CrmAccountItem> crmAccountItems, Integer total) {
        super(crmAccountItems, total);
    }

}