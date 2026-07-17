package com.edatasite.workforce.gwt.hrms.client.ui.cell;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Created by Sherali Pirnafasov.
 * User: Sherali Pirnafasov
 * Email: sherali.pirnafasov@gmail.com
 * Date: 10/22/11
 * Time: 12:14 PM
 */
public class EditPanel extends Composite implements HasValue<EditDataItem>, Constants {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private EditDataItem dataItem;

    private DisclosurePanel commentPanel = new DisclosurePanel();
    private TextArea2 commentText = new TextArea2(500);
    private FlexTable table = new FlexTable();
    private Label errorMessage = new Label();
    private TextBox textBox = new TextBox();

    private int column;

    public EditPanel() {
        commentText = new TextArea2(500);
        initWidget(table);
        DOM.setStyleAttribute(table.getElement(), "border", "2px black solid");
        DOM.setStyleAttribute(getElement(), "background", "white");
        textBox = new TextBox();
        textBox.setWidth("64px");
        Validation.addNumericKeyboardListener(textBox);

        textBox.addKeyDownHandler(event -> {
            if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                apply();
            }
        });

        table.setWidget(0, 1, textBox);
        table.getFlexCellFormatter().setColSpan(2, 0, 2);
        Button commentSave = new Button();
        commentSave.setText(wfmStrings.save());
        commentSave.addClickHandler(event -> apply());

        HorizontalPanel commentHP = new HorizontalPanel();
        commentHP.add(commentText);
        commentHP.add(commentSave);

        commentPanel.setHeader(new Label(wfmStrings.comment()));
        commentPanel.setContent(commentHP);
        Label previous = new Label();
        previous.setText("Press 'ENTER'");
        table.setWidget(1, 1, previous);
        table.setWidget(2, 0, errorMessage);
        table.setWidget(3, 1, commentPanel);
    }

    public void setText(String text) {
        table.setText(0, 1, text);
    }

    private boolean apply() {
        try {
            if (!textBox.getText().equals("")) {
                dataItem.setDescription(commentText.getText());
                dataItem.setValue(Double.valueOf(textBox.getValue()));
                setValue(dataItem, true);
            }
        } catch (NumberFormatException | StringIndexOutOfBoundsException exc) {
            errorPopup(errorMessage);
            return false;
        }

        return true;
    }

    private void errorPopup(Label errorMessage) {
        errorMessage.setText(wfmStrings.timeFormats());
    }

    @Override
    public EditDataItem getValue() {
        return dataItem;
    }

    public void setValue(int column, EditDataItem value) {
        this.column = column;
        this.commentPanel.setOpen(false);
        setValue(value, false);
    }

    @Override
    public void setValue(EditDataItem value) {
        setValue(value, false);
    }

    @Override
    public void setValue(EditDataItem value, boolean fireEvents) {
        this.dataItem = value;
        errorMessage.setText("");
        commentText.setText(value.getDescription() != null ? value.getDescription() : "");
        textBox.setText(Utils.formatDouble(value.getValue()));
        if (fireEvents) {
            ValueChangeEvent.fire(this, value);
        }
        textBox.setFocus(true);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<EditDataItem> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    public void setFocused(boolean b) {
        Utils.setFocus(textBox.getElement(), true);
    }
}