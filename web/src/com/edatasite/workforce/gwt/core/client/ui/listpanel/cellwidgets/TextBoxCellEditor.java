package com.edatasite.workforce.gwt.core.client.ui.listpanel.cellwidgets;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.gen2.table.client.InlineCellEditor;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 27-Nov-2010
 * Time: 15:56:38
 * <p>
 * <E> Column Type In Listing Panel
 */
public abstract class TextBoxCellEditor<E> extends InlineCellEditor<E> {

    private String prevValue;
    private TextBox textBox;

    public TextBoxCellEditor() {
        super(new TextBox());
        this.textBox = (TextBox) getContentWidget();
        textBox.setWidth("150px");
        Widget accept = this.getAcceptWidget();
        accept.getParent().addStyleName("btn btn--icon btn--default ficon--check");
    }

    public TextBoxCellEditor(int width) {
        super(new TextBox());
        this.textBox = (TextBox) getContentWidget();
        textBox.setWidth(width + "px");
        Widget accept = this.getAcceptWidget();
        accept.getParent().addStyleName("btn btn--icon btn--default ficon--check");
    }

    public TextBox getTextBox() {
        return textBox;
    }

    @Override
    public void editCell(CellEditInfo cellEditInfo, E cellValue, Callback<E> callback) {
        super.editCell(cellEditInfo, cellValue, callback);
        textBox.setFocus(true);
    }

    public void setText(String text) {
        textBox.setText(text);
    }

    public String getText() {
        return textBox.getText();
    }

    public void addNumberValidation(boolean negativeAllowed) {
        textBox.addKeyPressHandler(event -> {
            char key = event.getCharCode();
            if (!Character.isDigit(key) && key != (char) KeyCodes.KEY_DELETE
                    && key != (char) KeyCodes.KEY_BACKSPACE && key != (char) KeyCodes.KEY_BACKSPACE
                    && key != (char) KeyCodes.KEY_LEFT && key != (char) KeyCodes.KEY_RIGHT
                    && key != (char) KeyCodes.KEY_HOME && key != (char) KeyCodes.KEY_END
                    && key != (char) KeyCodes.KEY_ENTER && key != (char) KeyCodes.KEY_DOWN
                    && key != (char) KeyCodes.KEY_UP && key != (char) KeyCodes.KEY_TAB
                    && (negativeAllowed && (!(key == '-' && getText().trim().length() == 0) || key != '-'))) {
                textBox.cancelKey();
            }
            //TaskNumber: T1001 - HRMS - Employee Listing validation
            if (!Character.isDigit(key) && key != (char) KeyCodes.KEY_DELETE
                    && key != (char) KeyCodes.KEY_BACKSPACE && key != (char) KeyCodes.KEY_BACKSPACE
                    && key != (char) KeyCodes.KEY_LEFT && key != (char) KeyCodes.KEY_RIGHT
                    && key != (char) KeyCodes.KEY_HOME && key != (char) KeyCodes.KEY_END
                    && key != (char) KeyCodes.KEY_ENTER && key != (char) KeyCodes.KEY_DOWN
                    && key != (char) KeyCodes.KEY_UP && key != (char) KeyCodes.KEY_TAB) {
                textBox.cancelKey();
            }

            if (textBox.getText() != null && textBox.getText().indexOf('.') != -1 && key == '.') {
                textBox.cancelKey();
            }
            if (textBox.getText() != null && key == '\'') {
                textBox.cancelKey();
            }
        });
    }

    public void addPercentageNumberValidation(int scale, double maxLength) {
        textBox.addKeyPressHandler(event -> {
            char key = event.getCharCode();
            if (Utils.isArabicLanguage()) {
                return;
            }

            if (key == (char) 0) {
                return;
            }
            if (key == '.' && scale <= 0) {
                ((TextBox) event.getSource()).cancelKey();
                return;
            }
            if (!Character.isDigit(key) && key != (char) KeyCodes.KEY_DELETE
                    && key != (char) KeyCodes.KEY_BACKSPACE && key != (char) KeyCodes.KEY_BACKSPACE
                    && key != (char) KeyCodes.KEY_LEFT && key != (char) KeyCodes.KEY_RIGHT
                    && key != (char) KeyCodes.KEY_HOME && key != (char) KeyCodes.KEY_END
                    && key != (char) KeyCodes.KEY_ENTER && key != (char) KeyCodes.KEY_DOWN
                    && key != (char) KeyCodes.KEY_UP && key != (char) KeyCodes.KEY_TAB) {
                ((TextBox) event.getSource()).cancelKey();
            }
            if (textBox.getText() != null && textBox.getText().indexOf('.') != -1 && key == '.') {
                ((TextBox) event.getSource()).cancelKey();
            }
            if (textBox.getText() != null && key == '\'') {
                ((TextBox) event.getSource()).cancelKey();
            }

            String validateString = textBox.getText().substring(textBox.getText().lastIndexOf('.') + 1, textBox.getText().length());
            if (textBox.getText().contains(".") && (key == '.' || ((key != (char) KeyCodes.KEY_BACKSPACE)
                    && (textBox.getCursorPos() > textBox.getText().lastIndexOf('.') && validateString.length() >= scale)))) {
                ((TextBox) event.getSource()).cancelKey();
                return;
            }
            if (Character.isDigit(key)) {
                boolean isTrue = Double.valueOf(textBox.getValue() + key).compareTo(maxLength) <= 0;
                if (!isTrue) {
                    ((TextBox) event.getSource()).cancelKey();
                }
            }
        });
    }

    public boolean validatePercentage() {
        return (Double.valueOf(textBox.getText()).compareTo((double) 100) <= 0);
    }

    public boolean validateField(String uiType) {
        if (Constants.UI_TYPE_URL.equals(uiType)) {
            return !Validation.validateUrl(textBox, null);
        }
        return !Validation.validateEmailRequired(textBox);
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
}
