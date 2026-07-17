package com.edatasite.workforce.gwt.core.client.ui.listpanel.cellwidgets;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.google.gwt.gen2.table.client.InlineCellEditor;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 27-Nov-2010
 * Time: 15:54:06
 *
 * <E> Column Type In Listing Panel
 */

public abstract class DropDownCellEditor<E> extends InlineCellEditor<E> {

    private String prevValue = null;
    private DataListBox listBox;

    public DropDownCellEditor(String width, boolean withAcceptWidget) {
        super(new DataListBox(false, true));
        this.listBox = (DataListBox) getContentWidget();
        listBox.setWidth(width);
        Widget accept = this.getAcceptWidget();
        accept.getParent().removeFromParent();
    }

    public DropDownCellEditor() {
        super(new DataListBox(false, true));
        this.listBox = (DataListBox) getContentWidget();
        listBox.setWidth("150px");
        Widget accept = this.getAcceptWidget();
        accept.getParent().addStyleName("btn btn--icon btn--default ficon--check");
    }

    public DropDownCellEditor(boolean withoutNullLabel) {
        super(new DataListBox(false, true, true));
        this.listBox = (DataListBox) getContentWidget();
        listBox.setWidth("150px");
        Widget accept = this.getAcceptWidget();
        accept.getParent().addStyleName("btn btn--icon btn--default ficon--check");
    }

    public DropDownCellEditor(int width) {
        super(new DataListBox(false, true));
        this.listBox = (DataListBox) getContentWidget();
        listBox.setWidth(width + "px");
        Widget accept = this.getAcceptWidget();
        accept.getParent().addStyleName("btn btn--icon btn--default ficon--check");
    }

    public DataListBox getListBox() {
        return listBox;
    }

    @Override
    public void editCell(CellEditInfo cellEditInfo, E cellValue, Callback<E> eCallback) {
        super.editCell(cellEditInfo, cellValue, eCallback);
        listBox.setFocus(true);
    }

    public void setItems(SelectItem[] items) {
        listBox.setItems(items);
    }

    public SelectItem getSelectItem() {
        return listBox.getSelectedItem();
    }

    public void setSelectItem(SelectItem item) {
        listBox.setSelectedItem(item);
    }

    public String getSelectName() {
        if (listBox.getSelectedItem() != null) {
            return listBox.getSelectedItem().getName();
        }
        return null;
    }

    public void setSelectName(String name) {
        setDefaultValue();
        listBox.setSelectedByValue(name);
    }

    public void setSelectDoubleValueName(String name) {
        setDefaultValue();
        Double dv = Double.parseDouble(name.replace(";", ""));
        for (SelectItem item : listBox.getItems()) {
            if (dv.equals(Double.parseDouble(item.getName()))) {
                setSelectName(item.getName());
                break;
            }
        }
    }

    public void setDefaultValue() {
        prevValue = null;
        listBox.setSelectedNullLabel();
    }

    @Override
    protected boolean onAccept() {
        if ((prevValue != null && getSelectName() != null && !prevValue.equals(getSelectName()))
                || (prevValue == null && getSelectName() != null)
                || (prevValue != null && getSelectName() == null)) {
            return true;
        }
        super.cancel();
        return false;
    }

    @Override
    public void show() {
        prevValue = null;
        if (listBox.getSelectedItem() != null) {
            prevValue = listBox.getSelectedItem().getName();
        }
        super.show();
        int offset = Window.getClientWidth() - getWidget().getOffsetWidth() - 32;
        if ((getPopupLeft() + getWidget().getOffsetWidth()) > Window.getClientWidth()) {
            setPopupPosition(offset, getPopupTop());
        }
    }
}

