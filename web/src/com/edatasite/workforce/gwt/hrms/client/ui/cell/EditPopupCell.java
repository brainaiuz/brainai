package com.edatasite.workforce.gwt.hrms.client.ui.cell;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.google.gwt.cell.client.AbstractEditableCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * Created by Sherali Pirnafasov.
 * User: Sherali Pirnafasov
 * Email: sherali.pirnafasov@gmail.com
 * Date: 10/22/11
 * Time: 11:52 AM
 */
public class EditPopupCell extends AbstractEditableCell<EditDataItem, String> implements Constants {
    private static final int ESCAPE = 27;

    private final EditPanel editPanel;
    private int offsetX = -5;
    private int offsetY = -5;
    private Object lastKey;
    private Element lastParent;
    private int lastIndex;
    private int lastColumn;
    private EditDataItem lastValue;
    private PopupPanel panel;
    private ValueUpdater<EditDataItem> valueUpdater;

    /**
     * Constructs a new EditPopupCell that uses the {@link com.google.gwt.text.shared.SafeHtmlRenderer}.
     */
    public EditPopupCell() {
        super("click", "keydown");

        this.editPanel = new EditPanel();
        this.panel = new PopupPanel(true, true) {
            @Override
            protected void onPreviewNativeEvent(Event.NativePreviewEvent event) {
                if (Event.ONKEYUP == event.getTypeInt()) {
                    if (event.getNativeEvent().getKeyCode() == ESCAPE) {
                        // Dismiss when escape is pressed
                        panel.hide();
                    }
                }
            }
        };
        panel.addCloseHandler(event -> {
            lastKey = null;
            lastValue = null;
            lastIndex = -1;
            lastColumn = -1;
            if (lastParent != null && !event.isAutoClosed()) {
                // Refocus on the containing cell after the user selects a value, but
                // not if the popup is auto closed.
                lastParent.focus();
            }
            lastParent = null;
        });
        panel.add(editPanel);

        // Hide the panel and call valueUpdater.update when a date is selected
        editPanel.addValueChangeHandler(event -> {
            // Remember the values before hiding the popup.
            Element cellParent = lastParent;
            EditDataItem oldValue = lastValue;
            Object key = lastKey;
            int index = lastIndex;
            int column = lastColumn;
            panel.hide();

            // Update the cell and value updater.
            EditDataItem eventValue = event.getValue();
            // Update the cell and value updater.
            setViewData(key, Utils.formatDouble(eventValue.getValue()));
            setValue(new Context(index, column, key), cellParent, oldValue);
            if (valueUpdater != null) {
                valueUpdater.update(event.getValue());
            }
        });
    }

    @Override
    public boolean isEditing(Context context, Element parent, EditDataItem value) {
        return lastKey != null && lastKey.equals(context.getKey());
    }

    @Override
    public void onBrowserEvent(Context context, Element parent, EditDataItem value,
                               NativeEvent event, ValueUpdater<EditDataItem> valueUpdater) {
        super.onBrowserEvent(context, parent, value, event, valueUpdater);
        if ("click".equals(event.getType())) {
            onEnterKeyDown(context, parent, value, event, valueUpdater);

        } else if ("keydown".equals(event.getType())) {
            int keyCode = event.getKeyCode();

            // allow special keys
            if (Utils.isNumberKey(keyCode)) {
                onEnterKeyDown(context, parent, value, event, valueUpdater);
            }
        }
    }

    @Override
    public void render(Context context, EditDataItem value, SafeHtmlBuilder sb) {
        if (value != null) {
            value.setEditable(true);
            String val = Utils.formatDouble(value.getValue());
            sb.append(SafeHtmlUtils.fromTrustedString(val));
        }
    }

    @Override
    protected void onEnterKeyDown(Context context, Element parent, EditDataItem value,
                                  NativeEvent event, ValueUpdater<EditDataItem> valueUpdater) {
        this.lastKey = context.getKey();
        this.lastParent = parent;
        this.lastValue = value;
        this.lastIndex = context.getIndex();
        this.lastColumn = context.getColumn();
        this.valueUpdater = valueUpdater;
        if (value.isEditable()) {
            panel.setPopupPositionAndShow((offsetWidth, offsetHeight) -> panel.setPopupPosition(lastParent.getAbsoluteLeft() + offsetX,
                    lastParent.getAbsoluteTop() + offsetY));
            editPanel.setValue(lastColumn, value);
            editPanel.setFocused(true);
        }
    }
}