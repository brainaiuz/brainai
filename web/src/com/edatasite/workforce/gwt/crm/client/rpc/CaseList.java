package com.edatasite.workforce.gwt.crm.client.rpc;

import com.edatasite.workforce.gwt.crmcase.client.rpc.CaseItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 09-Jul-2009
 * Time: 18:52:19
 * To change this template use File | Settings | File Templates.
 */
public class CaseList extends ListResult<CaseItem> {

    private Boolean isTrash = false;

    public CaseList() {
    }

    public CaseList(ArrayList<CaseItem> item, int total) {
        super(item, total);
    }

    public Boolean isTrash() {
        return isTrash;
    }

    public void setTrash(Boolean trash) {
        isTrash = trash;
    }
}