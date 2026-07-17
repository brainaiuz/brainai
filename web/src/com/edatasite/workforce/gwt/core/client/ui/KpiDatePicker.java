package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.interfaces.CustomCellInterface;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.calendardatepicker.CalendarDatePicker;
import com.edatasite.workforce.gwt.core.client.ui.hijri.HijriCalc;
import com.edatasite.workforce.gwt.core.client.ui.hijri.SimpleHijriDate;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import gwt.material.design.client.ui.html.Icon;

import java.util.Date;

/**
 * User: Abror Abdukadirov
 * Date: 16.03.2018 17:11
 */
public class KpiDatePicker extends Composite implements CustomCellInterface {


    private static final WfmStrings wfmStrings = WfmStrings.App.get();


    private TextBox textbox;
    private PopupPanel popup;
    private CalendarDatePicker datePicker;
    private DateTimeFormat dateTimeFormat;

    private final boolean showImage;
    private boolean enable = true;//If this returns false, it will not show popup.
    private Date date;
    private final String defaultValue = wfmStrings.pleaseSelect();
    private final String defaultFormat = DateUtils.getFormat().getPattern();

    private final String CUSTOM_STYLE;

    public KpiDatePicker() {
        this(null, null, false, null);
    }

    public KpiDatePicker(String customStyle) {
        this(null, null, false, customStyle);
    }

    public KpiDatePicker(boolean showImage) {
        this(null, null, showImage, null);
    }

    public KpiDatePicker(Date date) {
        this(date, null, false, null);
    }

    public KpiDatePicker(DateTimeFormat dateTimeFormat) {
        this(null, dateTimeFormat, false, null);
    }

    public KpiDatePicker(Date date, DateTimeFormat dateTimeFormat) {
        this(date, dateTimeFormat, false, null);
    }

    public KpiDatePicker(Date date, DateTimeFormat dateTimeFormat, boolean showImage, String customStyle) {
        this.date = date;
        this.showImage = showImage;
        this.dateTimeFormat = dateTimeFormat;
        this.CUSTOM_STYLE = customStyle;
        show();
    }

    private void show() {
        initialize();
    }

    private void initialize() {
        textbox = new TextBox();
        textbox.setText(defaultFormat);
        if (dateTimeFormat == null) {
            dateTimeFormat = DateUtils.getFormat();
        }
        if (CUSTOM_STYLE != null) {
            textbox.addStyleName(CUSTOM_STYLE);
        } else {
            textbox.addStyleName("input-default-color");
        }
        textbox.addClickHandler(event -> popupPosition());

        textbox.getElement().getStyle().setColor("#999");
        textbox.addFocusHandler(event -> {
            if (defaultFormat.equals(textbox.getValue())) {
                textbox.getElement().getStyle().setColor("#000");
            }
        });
        textbox.addBlurHandler(event -> {
            if ("".equals(textbox.getValue())) {
                textbox.getElement().getStyle().setColor("#999");
                textbox.setText(defaultFormat);
            }
        });
        textbox.addValueChangeHandler(stringValueChangeEvent -> {
            try {
                if (!"".equals(stringValueChangeEvent.getValue())) {
                    textbox.getElement().getStyle().setColor("#000");
                    date = dateTimeFormat.parse(stringValueChangeEvent.getValue());
                    setDate(date);
                    datePicker.setCurrentMonth(date);
                    datePicker.setValue(date);
                } else {
                    setDate(null);
                }
            } catch (IllegalArgumentException e) {
                invalidDate();
            }
        });

        textbox.addKeyDownHandler(keyDownEvent -> {
            try {
                if (keyDownEvent.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    if (!"".equals(textbox.getValue())) {
                        textbox.getElement().getStyle().setColor("#000");
                        setDate(dateTimeFormat.parse(textbox.getValue()));
                    }
                }
            } catch (IllegalArgumentException e) {
                invalidDate();
            } finally {
                if (keyDownEvent.getNativeKeyCode() == KeyCodes.KEY_ENTER
                    || keyDownEvent.getNativeKeyCode() == KeyCodes.KEY_TAB) {
                    popup.hide(false);
                }
            }
        });

        datePicker = new CalendarDatePicker(false);
        datePicker.setValue(date);
        datePicker.addValueChangeHandler(dateValueChangeEvent -> {
            date = dateValueChangeEvent.getValue();
            synchronizeDate(date);
        });

        popup = new PopupPanel(true);
        popup.setStyleName("dateBoxPopup");
        popup.setWidget(datePicker);

        /**
         * If there is default zIndex, shell will cover the datepicker and it will not be shown.
         * Therefore we are setting custom zIndex value in order to be shown the datepicker.
         */
        DOM.setIntStyleAttribute(popup.getElement(), "zIndex", 7000);


        if (showImage) {
            HorizontalPanel panel = new HorizontalPanel();
            panel.setSpacing(1);
            panel.add(textbox);
            Icon image = new Icon();

            image.setStyleName("ficon--calendar pointer");
            image.addClickHandler(event -> popupPosition());

            panel.add(image);
            initWidget(panel);
        } else {
            initWidget(textbox);
        }
    }

    private void invalidDate() {
        Info.show(wfmStrings.pleaseEnterTheDateValueInCorrectFormat() + " " + defaultFormat, Info.Type.WARNING);
        textbox.setText("");
        setDate(null);
    }


    private void popupPosition() {
        if (enable) {
            popup.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
                final int textboxAbsolTop = textbox.getAbsoluteTop();

                public void setPosition(int offsetWidth, int offsetHeight) {
                    int totalWidth = textbox.getAbsoluteLeft() + popup.getOffsetWidth();
                    if (offsetHeight + textbox.getOffsetHeight() < Window.getClientHeight() - textboxAbsolTop) {
                        if (totalWidth > Window.getClientWidth()) {
                            popup.setPopupPosition(textbox.getAbsoluteLeft() - (totalWidth - Window.getClientWidth()), textboxAbsolTop + textbox.getOffsetHeight());
                        } else {
                            popup.setPopupPosition(textbox.getAbsoluteLeft(), textboxAbsolTop + textbox.getOffsetHeight());
                        }
                    } else {
                        if (totalWidth > Window.getClientWidth()) {
                            popup.setPopupPosition(textbox.getAbsoluteLeft() - (totalWidth - Window.getClientWidth()), textboxAbsolTop - offsetHeight);
                        } else {
                            popup.setPopupPosition(textbox.getAbsoluteLeft(), textboxAbsolTop - offsetHeight);
                        }
                    }
                }
            });
        }
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;

        datePicker.setValue(date);
        synchronizeDate(date);
    }

    public void setMonth(Date date) {
        datePicker.setCurrentMonth(date);
    }

    public void setDefaultValue() {
        textbox.setText(defaultValue);
        setDate(null);
    }

    public void setDefaultFormat() {
        textbox.setText(defaultFormat);
        setDate(null);
    }

    public String getText() {
        if (Utils.isAlternativeCalendar() && textbox.getText().contains(" (")) {
            return textbox.getText().substring(0, textbox.getText().indexOf(" ("));
        }
        return textbox.getText();
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    private void synchronizeDate(Date date) {
        if (date != null) {
            String dateFormat;
            if (dateTimeFormat != null) {
                dateFormat = dateTimeFormat.format(date);
            } else {
                dateFormat = DateUtils.getFormat().format(date);
            }
            if (Utils.isAlternativeCalendar() && date != null) {
                SimpleHijriDate simpleHijriDate = HijriCalc.toHijri(date);
                dateFormat = dateFormat + simpleHijriDate.getDatePickerCurrentDateShortFormat();
            }
            textbox.setText(dateFormat);
            if (!defaultFormat.equals(getText())) {
                textbox.getElement().getStyle().setColor("#000");
            }
            popup.hide();
        }
    }

    public void setDateTimeFormat(DateTimeFormat dateTimeFormat) {
        this.dateTimeFormat = dateTimeFormat;
    }

    public void addValueChangeHandler(ValueChangeHandler<Date> handler) {
        datePicker.addValueChangeHandler(handler);
    }

    public void setEnabled(boolean enable) {
        this.enable = enable;

        if (enable) {
            textbox.removeStyleName("search-textbox");
        } else {
            textbox.addStyleName("search-textbox");
        }

    }

    public boolean isEnable() {
        return enable;
    }

    public void setTextBoxEnabled(boolean enable) {
        textbox.setEnabled(enable);
    }
    @Override
    public void setWidth(String width) {
        textbox.setWidth(width);
    }

    public void setWidth(int width) {
        textbox.setWidth(width - 16 + "px");
    }

    @Override
    public void setHeight(String height) {
        textbox.setHeight(height);
    }

    @Override
    public void setSize(String width, String height) {
        textbox.setSize(width, height);
    }

    public void setFocus(boolean focus) {
        textbox.setFocus(focus);
    }

    @Override
    public String getDisplayValue() {
        return DateUtils.format(getDate());
    }

    @Override
    public void setItemValue(Object value) {
        setDate(value != null ? (Date) value : new Date());
    }

    @Override
    public void setItemFocus(boolean focused) {
        setFocus(focused);
    }
}
