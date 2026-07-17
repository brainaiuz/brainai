package com.edatasite.workforce.gwt.core.client.ui.dynamicTable.listeners;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 21.10.2008
 * Time: 18:33:33
 * To change this template use File | Settings | File Templates.
 */
public interface AddListener {

    void plusClicked(int rowId);

    void minusClicked(int rowId, Integer objectId);

}
