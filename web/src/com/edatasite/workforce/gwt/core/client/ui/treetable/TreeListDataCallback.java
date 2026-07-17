package com.edatasite.workforce.gwt.core.client.ui.treetable;

import java.util.ArrayList;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 27-Jul-2010
 * Time: 16:41:19
 */
public interface TreeListDataCallback {

    void onSuccess(Object[] data, TreeItem item, Integer itemID, ArrayList<Integer> childrenIds);

    void onFailure(Throwable t, TreeItem item);
}
