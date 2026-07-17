package com.edatasite.workforce.gwt.core.client.ui.editableTable;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.interfaces.CustomCellInterface;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;

import java.math.BigDecimal;
import java.util.function.Consumer;

/**
 * User: Murad
 * Date: 4/10/2018 9:41 PM
 */
public class EditableSaveTextBox extends Composite implements CustomCellInterface {
    private static NumberFormat numberFormat = Utils.getCalculationNumberFormat();

    private WfmButton2 saveButton;
    private TextBox textBox;
    private String emptyValue;
    private boolean numericField;
    private Consumer<BigDecimal> saveHandler;

    public EditableSaveTextBox(String emptyValue, boolean numericField) {
        this(emptyValue, numericField, null);
    }

    public EditableSaveTextBox(String emptyValue, boolean numericField, Consumer<BigDecimal> saveHandler) {
        this.emptyValue = emptyValue;
        this.numericField = numericField;
        this.saveHandler = saveHandler;
        this.createButton();
        this.createEventHandlers();
    }

    private void createButton() {
        final HTMLPanel panel = new HTMLPanel("");

        panel.setStyleName("saveable-cell");
        this.textBox = new TextBox();
        panel.add(this.textBox);

        this.saveButton = new WfmButton2("<i class='" + WfmButton2.ICON_CHECK + "'></i>");
        this.saveButton.setStyleName("btn btn-sm btn-default");
        this.saveButton.addClickHandler(clickEvent -> {
            if (this.saveHandler != null) {
                if (textBox.getText() == null || textBox.getText().isEmpty()) {
                    return;
                }
                this.saveHandler.accept(new BigDecimal(textBox.getText().replace(",", "")));
            }
        });
        panel.add(this.saveButton);
        initWidget(panel);
    }

    private void createEventHandlers() {
        blurClickHandler();
    }

    private native void blurClickHandler() /*-{
        var that = this;
        $wnd.document.onclick = function (event) {
            var elementNode = event.target;
            var elementParentNode = elementNode.parentNode;
            if ((elementNode && elementParentNode == null && elementNode.className && elementNode.className.search('right-align-Cell') === -1) ||
                (elementParentNode && elementParentNode.className && elementParentNode.className.search('saveable-cell') === -1)) {
                that.@com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableSaveTextBox::closePopup()();
            }
        };
    }-*/;

    @Override
    public String getDisplayValue() {
        if (numericField) {
            return this.textBox.getText() == null || this.textBox.getText().isEmpty()
                   ? emptyValue
                   : numberFormat.format(numberFormat.parse(this.textBox.getText()));
        }
        return this.textBox.getText();
    }

    @Override
    public void setItemValue(Object value) {
        this.textBox.setText((String) value);
    }

    @Override
    public void setItemFocus(boolean focused) {
        this.textBox.setFocus(focused);
    }

    public TextBox getTextBox() {
        return textBox;
    }

    public void setText(String format) {
        this.textBox.setText(format);
    }

    public void setEnabled(boolean enabled) {
        this.textBox.setEnabled(enabled);
        this.saveButton.setEnabled(enabled);
    }

    public Consumer<BigDecimal> getSaveHandler() {
        return saveHandler;
    }

    public void setSaveHandler(Consumer<BigDecimal> saveHandler) {
        this.saveHandler = saveHandler;
    }

    private Consumer<Boolean> closeHandler;

    public void addCloseHandler(Consumer<Boolean> closeHandler) {
        this.closeHandler = closeHandler;
    }

    public void closePopup() {
        if (this.closeHandler == null) {
            return;
        }
        closeHandler.accept(true);
    }
}
