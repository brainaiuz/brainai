package com.edatasite.workforce.gwt.core.client.ui.listpanel.cellwidgets;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.google.gwt.gen2.table.client.InlineCellEditor;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by IntelliJ IDEA.
 * User: Virus
 * Date: 1/17/12
 * Time: 12:35 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class LookUpCellEditor<E> extends InlineCellEditor<E> {
    private String prevValue = null;
    private LookUp lookUp = null;
    private SelectItem[] items = null;

    protected LookUpCellEditor(Widget content) {
        super(content);
        this.lookUp = (LookUp) getContentWidget();
        lookUp.getSuggestBox().setWidth("150px");
        Widget accept = this.getAcceptWidget();
        accept.getParent().addStyleName("btn btn--icon btn--default ficon--check");
    }

    public LookUpCellEditor(Widget widget, int width) {
        super(widget);
        lookUp.getSuggestBox().setWidth(width + "px");
        Widget accept = this.getAcceptWidget();
        accept.getParent().addStyleName("btn btn--icon btn--default ficon--check");
    }

    public LookUp getLookUp() {
        return lookUp;
    }

    @Override
    public void editCell(CellEditInfo cellEditInfo, E cellValue, Callback<E> callback) {
        super.editCell(cellEditInfo, cellValue, callback);
        lookUp.getTextBox().setFocus(true);
//        lookUp.getSuggestBox().setFocus(true);
    }

    public void setText(String text) {
        lookUp.getTextBox().setText(text);
    }

    public String getText() {
        return lookUp.getText();
    }

    @Override
    protected boolean onAccept() {
        if ((prevValue != null && getText() != null && !prevValue.equals(getText()))
                || (prevValue == null && getText() != null)
                || (prevValue != null && getText() == null)) {
            return true;
        }
        super.cancel();
        return false;
    }

    @Override
    public void show() {
        prevValue = getText() != null && !"".equals(getText()) ? getText() : null;
        super.show();
        int offset = Window.getClientWidth() - getWidget().getOffsetWidth() - 32;
        if ((getPopupLeft() + getWidget().getOffsetWidth()) > Window.getClientWidth()) {
            setPopupPosition(offset, getPopupTop());
        }
    }

    public void setItems(String txt, SelectItem[] items) {
        this.items = items;
        lookUp.setItems(txt, items);
    }

    public SelectItem[] getItems() {
        return items;
    }

    public void setSelectItem(SelectItem selectItem) {
        lookUp.setSelected(selectItem);
    }

    public void setSelectId(Integer selectId) {
        lookUp.setSelected(selectId);
    }

    public void setSelectextText(String selectextText) {
        lookUp.setSelected(selectextText);
    }

    public SelectItem getSelectedItem() {
        return lookUp.getSelectedItem();
    }

    public SelectItem getSelectedItem(Integer itemId) {
        return lookUp.getSelectedItem(itemId);
    }

    public Integer getSelectedItemIDByValue(String value) {
        return lookUp.getSelectedItemIDByValue(value);
    }

    public Integer getSelectedItemID() {
        return lookUp.getSelectedItemID();
    }
}