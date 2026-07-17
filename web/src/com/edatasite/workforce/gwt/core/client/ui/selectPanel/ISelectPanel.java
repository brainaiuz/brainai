package com.edatasite.workforce.gwt.core.client.ui.selectPanel;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

import java.util.Set;

/**
 * Created by Hurshid on 7/3/2017.
 */
public interface ISelectPanel {

    void removeItem(SelectItem item);

    void addItem(Set<SelectItem> selectedItems, boolean isSelected);
}
