package com.workforcetrack.mobile.services;

import com.workforcetrack.mobile.rpc.accounting.MProductList;
import com.workforcetrack.mobile.rpc.accounting.MProductListItem;
import com.workforcetrack.mobile.rpc.client.MFilterParametrs;

/**
 * Created by IntelliJ IDEA.
 * User: Aziz
 * Date: 5/23/11
 * Time: 3:16 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ProductWebService {

    MProductListItem getBaseData(Integer objectID);
    //CRUD

    MProductListItem getBaseData();

    MProductList getList (MFilterParametrs mFilterParametrs);

    MProductListItem get(Integer objectID);

    MProductListItem edit(Integer objectID);

    MProductListItem get();

    Integer save(MProductListItem mProductListItem);

    Boolean delete(Integer objectID);

}
