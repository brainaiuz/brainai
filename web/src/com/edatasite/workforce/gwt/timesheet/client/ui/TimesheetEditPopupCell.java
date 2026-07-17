package com.edatasite.workforce.gwt.timesheet.client.ui;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.CoreMessages;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetDataItem;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetSettings;
import com.google.gwt.cell.client.AbstractEditableCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.text.shared.SafeHtmlRenderer;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * Created by Sherali Pirnafasov.
 * User: Sherali Pirnafasov
 * Email: sherali.pirnafasov@gmail.com
 * Date: 10/22/11
 * Time: 11:52 AM
 */
public class TimesheetEditPopupCell extends AbstractEditableCell<TimesheetDataItem, String> implements Constants {
    private static final int ESCAPE = 27;

    private TimesheetSettings timesheetSettings;
    private final TimesheetEditPanel editPanel;
    private int offsetX = -5;
    private int offsetY = -5;
    private Object lastKey;
    private Element lastParent;
    private int lastIndex;
    private int lastColumn;
    private TimesheetDataItem lastValue;
    private PopupPanel panel;
    private ValueUpdater<TimesheetDataItem> valueUpdater;
    private static final CoreMessages coreMessages = CoreMessages.App.get();

    /**
     * Constructs a new TimesheetEditPopupCell that uses the {@link SafeHtmlRenderer}.
     */
    public TimesheetEditPopupCell() {
        super("click", "keydown");

        this.editPanel = new TimesheetEditPanel();
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
        panel.addStyleName("timesheetEditCellPopup");
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
        editPanel.setCloseHandler(() -> panel.hide());

        panel.add(editPanel);

        // Hide the panel and call valueUpdater.update when a date is selected
        editPanel.addValueChangeHandler(event -> {
            // Remember the values before hiding the popup.
            Element cellParent = lastParent;
            TimesheetDataItem oldValue = lastValue;
            Object key = lastKey;
            int index = lastIndex;
            int column = lastColumn;
            panel.hide();

            // Update the cell and value updater.
            TimesheetDataItem eventValue = event.getValue();
            // Update the cell and value updater.
            setViewData(key, eventValue.getMinutes() + "");
            setValue(new Context(index, column, key), cellParent, oldValue);
            if (valueUpdater != null) {
                valueUpdater.update(event.getValue());
            }
        });
    }

    @Override
    public boolean isEditing(Context context, Element parent, TimesheetDataItem value) {
        return lastKey != null && lastKey.equals(context.getKey());
    }

    @Override
    public void onBrowserEvent(Context context, Element parent, TimesheetDataItem value,
                               NativeEvent event, ValueUpdater<TimesheetDataItem> valueUpdater) {
        super.onBrowserEvent(context, parent, value, event, valueUpdater);
        if ("true".equals(Utils.userSettings.get(VALIDATE_PAST_TIMSHEET))) {
            Integer pastDays = Integer.valueOf(Utils.userSettings.get(PAST_TIMSHEET_DAYS));
            if (value.getDate().getTime() < value.getCurrentServerDate().getTime()) {
                if (pastDays < Math.floor((value.getCurrentServerDate().getTime() - value.getDate().getTime()) / (double) (1000 * 60 * 60 * 24))) {
                    Info.show(coreMessages.validationPast(pastDays.toString()), Info.Type.WARNING);
                    return;
                }
            }
        }

        if ("true".equals(Utils.userSettings.get(VALIDATE_FUTURE_TIMESHEET))) {
            Integer futureDays = Integer.valueOf(Utils.userSettings.get(FUTURE_TIMESHEET_DAYS));
            if (value.getDate().getTime() > value.getCurrentServerDate().getTime()) {
                if (futureDays < Math.ceil(((value.getDate().getTime() - value.getCurrentServerDate().getTime()) / (double) (1000 * 60 * 60 * 24)))) {
                    Info.show(coreMessages.validationFuture(futureDays.toString()), Info.Type.WARNING);
                    return;
                }
            }
        }

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
    public void render(Context context, TimesheetDataItem value, SafeHtmlBuilder sb) {
        if (value != null) {
            value.setEditable(true);
            String minutes = Utils.formatMinutes(value.getMinutes());
            String val = "";
            if (value.getStatus() == TIMESHEET_ENTRY_APPROVED) {
                value.setEditable(false);
                if ("true".equals(Utils.userSettings.get(ISAUTOMATICAPPROVAL)) && value.isAutoApproved()) {
                    value.setEditable(true);
                }
                val = "<span class=\"mod--approved\">" + minutes + "</span>";
            } else if (value.getStatus() == TIMESHEET_ENTRY_WAITING) {
                value.setEditable(false);
                if ("true".equals(Utils.userSettings.get(ISAUTOMATICWAITINGFORAPPROVAL))) {
                    value.setEditable(true);
                }
                val = "<span class=\"mod--pending\">" + minutes + "</span>";
            } else if (value.getStatus() == TIMESHEET_ENTRY_REJECTED) {
                val = "<span class=\"mod--rejected\">" + minutes + "</span>";
            } else if (value.getStatus() == TIMESHEET_ENTRY_NOTSUBMITTED) {
                if (value.getMinutes() > 0) {
                    val = "<span class=\"mod--not-sent\">" + minutes + "</span>";
                } else {
                    val = "<span>" + minutes + "</span>";
                }
            } else if (value.getStatus() == TIMESHEET_ENTRY_APPLYING_UPDATE) {
                val = "<span>" + minutes + "</span>";
            }  else if (value.getStatus() == TIMESHEET_ENTRY_FAILED) {
                val = "<span class=\"mod--failed\">" + minutes + "</span>";
            } else {
                val = "<span>" + minutes + "</span>";
            }
            sb.append(SafeHtmlUtils.fromTrustedString(val));
        }
    }

    @Override
    protected void onEnterKeyDown(Context context, Element parent, TimesheetDataItem value,
                                  NativeEvent event, ValueUpdater<TimesheetDataItem> valueUpdater) {
        this.lastKey = context.getKey();
        this.lastParent = parent;
        this.lastValue = value;
        this.lastIndex = context.getIndex();
        this.lastColumn = context.getColumn();
        this.valueUpdater = valueUpdater;
        if (value.isEditable()) {
            editPanel.setTimesheetSettings(getTimesheetSettings());
            panel.setPopupPositionAndShow((offsetWidth, offsetHeight) -> {
                if (lastParent.getAbsoluteTop() + offsetHeight > Window.getClientHeight()) {
                    panel.setPopupPosition(lastParent.getAbsoluteLeft() + offsetX, lastParent.getAbsoluteTop() + offsetHeight - 10);
                } else {
                    panel.setPopupPosition(lastParent.getAbsoluteLeft() + offsetX, lastParent.getAbsoluteTop() + offsetY);
                }
            });
            editPanel.setValue(lastColumn, value);
            editPanel.setFocused(true);
        }
    }

    public TimesheetSettings getTimesheetSettings() {
        return timesheetSettings;
    }

    public void setTimesheetSettings(TimesheetSettings timesheetSettings) {
        this.timesheetSettings = timesheetSettings;
    }
}