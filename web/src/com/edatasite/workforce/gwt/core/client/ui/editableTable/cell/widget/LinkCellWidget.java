package com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.widget;

import com.edatasite.workforce.gwt.core.client.interfaces.LinkableCellInterface;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.Command;
import gwt.material.design.client.ui.MaterialLink;

/**
 * User: Abror Abdukadirov
 * Date: 04.04.2019 19:27
 */
public class LinkCellWidget extends MaterialLink implements LinkableCellInterface {

    private Command clickHandler;
    private SelectItem item;

    public LinkCellWidget(String text, Command clickHandler) {
        super(text);
        this.clickHandler = clickHandler;
    }

    @Override
    public String getDisplayValue() {
        return this.getText();
    }

    @Override
    public void setItemValue(Object value) {
    }

    @Override
    public void setItemFocus(boolean focused) {
    }

    @Override
    public Command getClickHandler() {
        return this.clickHandler;
    }

    public void setClickHandler(Command clickHandler) {
        this.clickHandler = clickHandler;
    }

    public SelectItem getItem() {
        return item;
    }

    public void setItem(SelectItem item) {
        this.item = item;
    }
}
