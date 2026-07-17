package com.edatasite.workforce.gwt.core.client.ui.dynamicTable.headers;

import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.SortCommand;
import com.google.gwt.user.client.ui.Composite;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 20.10.2008
 * Time: 16:27:11
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractHeader extends Composite {

    public abstract void setSortCommand(SortCommand sortCommand);

    public abstract void setHeaderText(String text);

}
