package com.edatasite.workforce.gwt.core.client.ui.listpanel.cellwidgets;

import com.edatasite.workforce.gwt.core.client.ui.KpiEditor;
import com.google.gwt.gen2.table.client.InlineCellEditor;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

/**
 * User: Azamjon
 * Date: 10-Nov-2019
 */
public abstract class HTMLTextAreaCellEditor<E> extends InlineCellEditor<E> {

    private String prevValue;
    private KpiEditor htmlTextarea;

    public HTMLTextAreaCellEditor() {
        super(new KpiEditor());
        this.htmlTextarea = (KpiEditor) getContentWidget();
        htmlTextarea.setWidth("150px");
        Widget accept = this.getAcceptWidget();
        accept.getParent().addStyleName("btn btn--icon btn--default ficon--check");
    }

    public HTMLTextAreaCellEditor(int width) {
        super(new TextArea());
        this.htmlTextarea = (KpiEditor) getContentWidget();
        htmlTextarea.setWidth(width + "px");
        Widget accept = this.getAcceptWidget();
        accept.getParent().addStyleName("btn btn--icon btn--default ficon--check");
    }

    public KpiEditor getTextArea() {
        return htmlTextarea;
    }

    @Override
    public void editCell(CellEditInfo cellEditInfo, E cellValue, Callback<E> callback) {
        super.editCell(cellEditInfo, cellValue, callback);
    }

    public void setText(String text) {
        htmlTextarea.setData(text);
    }

    public String getText() {
        return htmlTextarea.getData();
    }

    @Override
    protected boolean onAccept() {
        if ((prevValue != null && !prevValue.equals(getText())) || (prevValue == null || prevValue == "")) {
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
}