package com.google.gwt.cell.client;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.SafeHtmlRenderer;
import com.google.gwt.text.shared.SimpleSafeHtmlRenderer;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.datepicker.client.DatePicker;

import java.util.Date;

/**
 * Created by Djuraev on 10/05/15.
 */
public class DatePickerCellCustom extends AbstractEditableCell<Date, Date> {

    private static final int ESCAPE = 27;

    private final DatePicker datePicker;
    private final DateTimeFormat format;
    private Object lastKey;
    private Element lastParent;
    private int lastIndex;
    private int lastColumn;
    private Date lastValue;
    private PopupPanel panel;
    private ValueUpdater<Date> valueUpdater;
    private String className = "datePicker-cell";

    interface Template extends SafeHtmlTemplates {
        @Template ("<input type=\"text\" value=\"{0}\" class=\"{1}\"></input>")
        SafeHtml input(String value, String className);
    }

    private static Template template;


    public DatePickerCellCustom(DateTimeFormat format) {
        this(format, SimpleSafeHtmlRenderer.getInstance());
    }

    public DatePickerCellCustom(DateTimeFormat format, String className) {
        this(format, SimpleSafeHtmlRenderer.getInstance());
        this.className = className;
    }

    public DatePickerCellCustom(DateTimeFormat format, SafeHtmlRenderer<String> renderer) {
        super("click", "keydown");

        if (template == null) {
            template = GWT.create(DatePickerCellCustom.Template.class);
        }

        if (format == null) {
            throw new IllegalArgumentException("format == null");
        }
        if (renderer == null) {
            throw new IllegalArgumentException("renderer == null");
        }
        this.format = format;

        this.datePicker = new DatePicker();
        datePicker.getElement().addClassName("gwt-DatePicker-mod " + className);
        this.panel = new PopupPanel(true, true) {
            @Override
            protected void onPreviewNativeEvent(NativePreviewEvent event) {
                if (Event.ONKEYUP == event.getTypeInt()) {
                    if (event.getNativeEvent().getKeyCode() == ESCAPE) {
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
                lastParent.focus();
            }
            lastParent = null;
        });
        panel.add(datePicker);
        panel.getElement().setClassName("dateBoxPopup");

        datePicker.addValueChangeHandler(event -> {
            Element cellParent = lastParent;
            Date oldValue = lastValue;
            Object key = lastKey;
            int index = lastIndex;
            int column = lastColumn;
            panel.hide();

            Date date = event.getValue();
            setViewData(key, date);
            setValue(new Context(index, column, key), cellParent, oldValue);
            if (valueUpdater != null) {
                valueUpdater.update(date);
            }
        });
    }

    @Override
    public boolean isEditing(Context context, Element parent, Date value) {
        return lastKey != null && lastKey.equals(context.getKey());
    }

    @Override
    public void onBrowserEvent(Context context, Element parent, Date value,
                               NativeEvent event, ValueUpdater<Date> valueUpdater) {
        super.onBrowserEvent(context, parent, value, event, valueUpdater);
        if ("click".equals(event.getType())) {
            onEnterKeyDown(context, parent, value, event, valueUpdater);
        }
    }

    @Override
    public void render(Context context, Date value, SafeHtmlBuilder sb) {
        Object key = context.getKey();
        Date viewData = getViewData(key);
        if (viewData != null && viewData.equals(value)) {
            clearViewData(key);
            viewData = null;
        }

        String s = null;
        if (viewData != null) {
            s = format.format(viewData);
        } else if (value != null) {
            s = format.format(value);
        }
        if (s != null) {
            sb.append(template.input(s, className));
        } else {
            sb.append(template.input(DateUtils.getFormat().getPattern(), className));
        }
    }

    @Override
    protected void onEnterKeyDown(Context context, Element parent, Date value,
                                  NativeEvent event, ValueUpdater<Date> valueUpdater) {
        this.lastKey = context.getKey();
        this.lastParent = parent;
        this.lastValue = value;
        this.lastIndex = context.getIndex();
        this.lastColumn = context.getColumn();
        this.valueUpdater = valueUpdater;

        Date viewData = getViewData(lastKey);
        Date date = (viewData == null) ? lastValue : viewData;
        if (date != null) {
            datePicker.setCurrentMonth(date);
        }
        datePicker.setValue(date);
        panel.setPopupPositionAndShow((offsetWidth, offsetHeight) -> {
            if (offsetHeight + lastParent.getOffsetHeight() < Window.getClientHeight() - lastParent.getAbsoluteTop()) {
                panel.setPopupPosition(lastParent.getAbsoluteLeft(), lastParent.getAbsoluteTop() + lastParent.getOffsetHeight());
            } else {
                panel.setPopupPosition(lastParent.getAbsoluteLeft(), lastParent.getAbsoluteTop() - offsetHeight);
            }
        });
    }
}