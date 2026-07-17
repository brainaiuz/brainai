package com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.widget;

import com.edatasite.workforce.gwt.core.client.interfaces.LinkableCellInterface;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.lookup.CrmAccountLookUp;
import com.google.gwt.user.client.Command;

public class LinkableCrmAccountLookUp extends CrmAccountLookUp implements LinkableCellInterface {

    private Command clickHandler;
    private SelectItem item;

    public LinkableCrmAccountLookUp(String typeCode, boolean searchByParent) {
        super(typeCode, searchByParent);
    }

    public LinkableCrmAccountLookUp(String typeCode, boolean searchByParent, boolean codeAlso) {
        super(typeCode, searchByParent, codeAlso);
    }

    @Override
    public String getDisplayValue() {
        return null;
    }

    @Override
    public void setItemValue(Object value) {

    }

    @Override
    public void setItemFocus(boolean focused) {

    }

    @Override
    public Command getClickHandler() {
        return clickHandler;
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
