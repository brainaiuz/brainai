package com.edatasite.workforce.gwt.crm.client.rpc;

import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 09-Jul-2009
 * Time: 18:52:19
 */
public class CrmAccountList extends ListResult<CrmAccountItem> {
    public CrmAccountList() {
    }

    public CrmAccountList(ArrayList<CrmAccountItem> crmAccountItems, Integer total) {
        super(crmAccountItems, total);
    }
}