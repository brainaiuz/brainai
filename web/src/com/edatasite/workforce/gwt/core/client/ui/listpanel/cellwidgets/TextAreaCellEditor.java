package com.edatasite.workforce.gwt.core.client.ui.listpanel.cellwidgets;

import com.google.gwt.gen2.table.client.InlineCellEditor;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 27-Nov-2010
 * Time: 15:56:38
 *
 * <E> Column Type In Listing Panel
 */
public abstract class TextAreaCellEditor<E> extends InlineCellEditor<E> {

    private String prevValue;
    private TextArea textarea;

    public TextAreaCellEditor() {
        super(new TextArea());
        this.textarea = (TextArea) getContentWidget();
        textarea.setWidth("150px");
        Widget accept = this.getAcceptWidget();
        accept.getParent().addStyleName("btn btn--icon btn--default ficon--check");
    }

    public TextAreaCellEditor(int width) {
        super(new TextArea());
        this.textarea = (TextArea) getContentWidget();
        textarea.setWidth(width + "px");
        Widget accept = this.getAcceptWidget();
        accept.getParent().addStyleName("btn btn--icon btn--default ficon--check");
    }

    public TextArea getTextArea() {
        return textarea;
    }

    @Override
    public void editCell(CellEditInfo cellEditInfo, E cellValue, Callback<E> callback) {
        super.editCell(cellEditInfo, cellValue, callback);
        textarea.setFocus(true);
    }

    public void setText(String text) {
        textarea.setText(text);
    }

    public String getText() {
         return textarea.getText();
    }

    @Override
    protected boolean onAccept() {
        if ((prevValue != null && !prevValue.equals(getText())) || (prevValue==null || prevValue=="")){
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
