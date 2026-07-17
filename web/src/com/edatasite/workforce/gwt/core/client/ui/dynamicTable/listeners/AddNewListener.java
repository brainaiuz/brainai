package com.edatasite.workforce.gwt.core.client.ui.dynamicTable.listeners;

/**
 * Created with IntelliJ IDEA.
 * User: Hasan Xo'janazarov
 * Date: 29.05.12
 * Time: 17:18
 * To change this template use File | Settings | File Templates.
 */
public interface AddNewListener {

    void plusClicked(int rowId);

    void minusClicked(int rowId, Integer objectId);

}
