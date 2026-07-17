package com.edatasite.workforce.gwt.core.client.ui.treeselect;

import com.google.gwt.user.client.Command;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: Nov 28, 2009
 * Time: 6:50:55 PM
 * To change this template use File | Settings | File Templates.
 */
public interface TreeSelectCallback {

    void addChildren(NTreeSelectItem parent, Command command);
}
