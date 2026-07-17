package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.ui.datePicker.PopupCalendar;
import com.edatasite.workforce.gwt.core.client.ui.hijri.HijriCalc;
import com.edatasite.workforce.gwt.core.client.ui.hijri.SimpleHijriDate;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Icon;

import java.util.Date;

/**
 * Main class of the DatePicker. It extends the TextBox widget and manages a
 * Date object. When it is clicked, it opens a PopupCalendar on which we can
 * select a new date. <br>
 * Example of use : <br>
 * <code>
 * DatePicker datePicker = new DatePicker();<br>
 * RootPanel.get().add(datePicker);<br>
 * </code>
 */
public class DatePicker extends Composite implements Clearable {


    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private PopupCalendar popup;
    private Date date;
    private DateTimeFormat dateFormatter;
    //	private Observable observable;
    private boolean defaultPosition = false;
    private Event event;

    private final String defaultValue = wfmStrings.pleaseSelect();
    private final String defaultFormat = DateUtils.getFormat().getPattern();
    private final TextBox textBox;
    private final boolean showImage;
    private final ImageResource clearButton;
    private Icon calendarImage;
    private String prevText;
    private Widget appender = null;
    private boolean isOnlyMonthFormat = false;

    /**
     * Default constructor. It creates s DatePicker which shows the current month.
     */
    public DatePicker() {
        this(null, null, false, null);
    }

    /**
     * Constructor with params. It creates a DatePicker which shows the current month.
     *
     * @param showImage - DatePicker with image
     */
    public DatePicker(boolean showImage) {
        this(null, null, showImage, null);
        if (getElement() != null && getElement().getFirstChildElement() != null) {
            getElement().getFirstChildElement().setAttribute("autocomplete", "off");
        }
    }

    public DatePicker(boolean showImage, Widget appender) {
        this(null, null, true, null, Constants.OVERALL_DATE_PICKER_WEEK_START, true, null, appender);
    }

    public DatePicker(boolean showImage, ImageResource clearButton) {
        this(null, null, showImage, null, Constants.OVERALL_DATE_PICKER_WEEK_START, true, clearButton);
    }

    /**
     * Create a DatePicker which show a specific Date.
     *
     * @param date - Date to show
     */
    public DatePicker(Date date) {
        this(date, null, false, null);
    }

    /**
     * Create Constructor which show a specific Date, Calendar Image
     *
     * @param date      - Date to show
     * @param showImage - DatePicker with image
     */
    public DatePicker(Date date, boolean showImage) {
        this(date, null, showImage, null);
    }

    /**
     * Create a DatePicker which uses a specific theme.
     *
     * @param theme - Theme name
     */
    public DatePicker(String theme) {
        this(null, null, false, theme);
    }

    /**
     * Create a DatePicker which specifics date and theme.
     *
     * @param date  - Date to show
     * @param theme - Theme name
     */
    public DatePicker(Date date, String theme) {
        this(date, null, false, theme);
    }

    /**
     * Create a DatePicker which specific dateFormatter.
     *
     * @param dateFormatter - Date to Formatter
     */
    public DatePicker(DateTimeFormat dateFormatter) {
        this(null, dateFormatter, false, null);
    }

    /**
     * Create a DatePicker which specifics date and dateFormatter
     *
     * @param date          - Date to show
     * @param dateFormatter - Date to Formatter
     */
    public DatePicker(Date date, DateTimeFormat dateFormatter) {
        this(date, dateFormatter, false, null);
    }

    /**
     * Create a Constructor which specific date, dateFormatter, showImage and theme
     *
     * @param date          - Date to show
     * @param dateFormatter - Date to Formatter
     * @param showImage     - DatePicker with Image
     * @param theme         - Calendar theme
     */
    public DatePicker(Date date, DateTimeFormat dateFormatter, boolean showImage, String theme) {
        this(date, dateFormatter, showImage, theme, Constants.OVERALL_DATE_PICKER_WEEK_START, true, null);
    }

    public DatePicker(Date date, DateTimeFormat dateFormatter, boolean showImage, String theme, String useCustomWeekStart, boolean showYearArrows, ImageResource clearButton) {
        this(date, dateFormatter, showImage, theme, useCustomWeekStart, showYearArrows, clearButton, null);
    }

    public DatePicker(Date date, DateTimeFormat dateFormatter, boolean showImage, String theme, String useCustomWeekStart, boolean showYearArrows, ImageResource clearButton, Widget appender) {
        popup = new PopupCalendar(this, useCustomWeekStart, showYearArrows, isOnlyMonthFormat);
        textBox = new TextBox();
        if (Constants.TIMESHEET_WEEK_START.equals(useCustomWeekStart)) {
            textBox.setWidth("110px");
        }
//        observable = new Observable();
        this.date = date;
        this.dateFormatter = dateFormatter != null ? dateFormatter : DateUtils.getFormat();
        this.showImage = showImage;
        this.clearButton = clearButton;
        this.appender = appender;
        if (theme != null) {
            setTheme(theme);
        }
        drawInitialize();
        if (date != null) {
            synchronizeFromDate();
        }
        if (getElement() != null && getElement().getFirstChildElement() != null) {
            getElement().getFirstChildElement().setAttribute("autocomplete", "off");
        }
    }

/*
    public void addBeforePopupHide(int eventType, Listener listener) {
		popup.addListener(eventType, listener);
	}
    */
/*
    public void addListener(int eventType, Listener listener) {
		observable.addListener(eventType, listener);
	}
*/

    public void clearSelected() {
        setText(wfmStrings.pleaseSelect());
    }

    public void setDefaultFormatText() {
        if (defaultFormat != null && !"".equals(defaultFormat)) {
            setText(defaultFormat);
        }
    }

//	public boolean fireEvent(int type) {
//		BaseEvent be = new BaseEvent();
//		be.widget = this;
//		return fireEvent(type, be);
//	}

//	public boolean fireEvent(int type, BaseEvent be) {
//		return observable.fireEvent(type, be);
//	}

    /**
     * Return the Date contained in the DatePicker.
     *
     * @return The Date
     */
    public Date getDate() {
        if (getText() != null && !"".equals(getText()) && !defaultFormat.equals(getText())) {
            if (date == null) {
                try {
                    if (dateFormatter == null) {
                        dateFormatter = DateUtils.getFormat();
                    }
                    if (wfmStrings.pleaseSelect().equals(getText())) {
                        return null;
                    }
                    date = dateFormatter.parse(getText());
                } catch (IllegalArgumentException e) {
//                    e.printStackTrace();
                }
            }
            return date;
        }
        return null;
    }

    public DateNonConvertable getDateAsNonConvertable(){
        return new DateNonConvertable(getDate());
    }

    /**
     * Set the Date of the datePicker and synchronize it with the display.
     *
     * @param value - value
     */
    public void setDate(Date value) {
        this.date = value;
        synchronizeFromDate();
    }

    public void setDateTimeFormat(DateTimeFormat dateFormatter) {
        this.dateFormatter = dateFormatter;
    }

    public void setOnlyMonthFormat(boolean onlyMonthFormat) {
        this.isOnlyMonthFormat = onlyMonthFormat;
        popup = new PopupCalendar(this, Constants.OVERALL_DATE_PICKER_WEEK_START, true, isOnlyMonthFormat);

    }

    public void setOnlyYearFormat(boolean onlyYearFormat) {
        popup = new PopupCalendar(this, Constants.OVERALL_DATE_PICKER_WEEK_START, true, false, onlyYearFormat);
    }

    /**
     * Return the theme name.
     *
     * @return Theme name
     */
    public String getTheme() {
        return popup.getTheme();
    }

    /**
     * Set the theme name.
     *
     * @param theme Theme name
     */
    public void setTheme(String theme) {
        popup.setTheme(theme);
    }

//    public void onBrowserEvent(Event event) {
//        this.event = event;
//        showPopupCalendar();
//    }

    public void showPopupCalendar() {
        if (event != null) {
            switch (DOM.eventGetType(event)) {
                case Event.ONCLICK:
                    showPopup();
                    break;
                case Event.ONBLUR:
                    popup.hidePopupCalendar();
                    break;
                case Event.ONCHANGE:
                    parseDate();
                    break;
                case Event.ONKEYPRESS:
                    showPopup();
                    if (DOM.eventGetKeyCode(event) == 13 ||
                            DOM.eventGetKeyCode(event) == KeyCodes.KEY_ENTER) {
                        parseDate();
                        showPopup();
                        break;
                    }
            }
        } else {
            showPopup();
        }/*Please, decide this code*/
    }

    /**
     * Display the date in the DatePicker.
     */
    public void synchronizeFromDate() {
        if (date != null && ("".equals(prevText) || !"".equals(getText()))) {
            if (dateFormatter == null) {
                dateFormatter = DateUtils.getFormat();
            }
            this.setText(dateFormatter.format(this.date));
            if (!defaultFormat.equals(getText())) {
                textBox.getElement().getStyle().setColor("#536677");
            }
        } else {
            this.setText("");
        }
        prevText = this.getText();
        fireEvent(new BlurEvent() {
            @Override
            protected void dispatch(BlurHandler handler) {
                super.dispatch(handler);
            }
        });
//		observable.fireEvent(Event.ONBLUR);
    }

    public Date getCurrentDate() {
        return new Date();
    }


    public PopupCalendar getPopup() {
        return popup;
    }

    public void setPopup(PopupCalendar popup) {
        this.popup = popup;
    }

    public boolean isDefaultPosition() {
        return defaultPosition;
    }

    public void setDefaultPosition(boolean defaultPosition) {
        this.defaultPosition = defaultPosition;
    }

    public void setText(String text) {
        if (Utils.isAlternativeCalendar() && date != null && !text.equals(defaultValue)) {
            SimpleHijriDate simpleHijriDate = HijriCalc.toHijri(date);
            text = text + simpleHijriDate.getDatePickerCurrentDateShortFormat();
        }
        textBox.setText(text);
    }

    public boolean isEnabled() {
        return textBox.isEnabled();
    }

    public void setEnabled(boolean enabled) {
        textBox.setEnabled(enabled);
        if (calendarImage != null) {
            if (enabled) {
                calendarImage.addStyleName("pointer");
            } else {
                calendarImage.removeStyleName("pointer");
            }
        }
    }

    public void setMaxLength(int length) {
        textBox.setMaxLength(length);
    }

    public HandlerRegistration addBlurHandler(BlurHandler handler) {
        return textBox.addBlurHandler(handler);
    }

    public HandlerRegistration addChangeHandler(ChangeHandler handler) {
        popup.addChangeHandler(handler);
        return textBox.addChangeHandler(handler);
    }

    @Override
    public void setHeight(String height) {
        textBox.setHeight(height);
    }

    public void setFocus(boolean focused) {
        textBox.setFocus(focused);
    }

    public String getText() {
        if (Utils.isAlternativeCalendar() && textBox.getText().contains(" (")) {
            return textBox.getText().substring(0, textBox.getText().indexOf(" ("));
        }
        return textBox.getText();
    }

    @Override
    public void setSize(String width, String height) {
        textBox.setSize(width, height);
    }

    @Override
    public void setWidth(String width) {
        textBox.setWidth(width);
    }

    public void setWidth(int width) {
        textBox.setWidth(width - 16 + "px");
    }

    /**
     * initialize textBox and calendar image
     */
    private void drawInitialize() {
        textBox.sinkEvents(Event.ONCLICK | Event.ONCHANGE | Event.ONKEYPRESS | Event.ONBLUR);

//        setText(defaultValue);
        setText(defaultFormat);
        textBox.getElement().getStyle().setColor("rgba(0, 0, 0, 0.26)");
        textBox.addFocusHandler(event -> {
            if (defaultFormat.equals(textBox.getValue())) {
                setText("");
                textBox.getElement().getStyle().setColor("#536677");
            }
        });
        textBox.addBlurHandler(event -> {
            try {
                if ("".equals(textBox.getValue()) || defaultValue.equals(textBox.getValue())) {
                    textBox.getElement().getStyle().setColor("rgba(0, 0, 0, 0.26)");
                    setText(defaultFormat);
                } else {
                    textBox.getElement().getStyle().setColor("#536677");
                    setDate(dateFormatter.parse(getText()));
                }
            } catch (Exception e) {
                invalidDate();
            }
        });

        textBox.addValueChangeHandler(stringValueChangeEvent -> {
            try {
                if (!"".equals(stringValueChangeEvent.getValue())) {
                    textBox.getElement().getStyle().setColor("#536677");
                    setDate(dateFormatter.parse(stringValueChangeEvent.getValue()));
                }
            } catch (IllegalArgumentException e) {
                invalidDate();
            }
        });

        textBox.addKeyDownHandler(keyDownEvent -> {
            try {
                if (keyDownEvent.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    if (!"".equals(textBox.getValue())) {
                        textBox.getElement().getStyle().setColor("#536677");
                        setDate(dateFormatter.parse(getText()));
                    }
                }
            } catch (IllegalArgumentException e) {
                invalidDate();
            } finally {
                if (keyDownEvent.getNativeKeyCode() == KeyCodes.KEY_ENTER
                        || keyDownEvent.getNativeKeyCode() == KeyCodes.KEY_TAB) {
                    popup.hidePopupCalendar();
                }
            }
        });

        textBox.addClickHandler(event -> showPopupCalendar());
        if (showImage) {
            MaterialPanel hPanel = new MaterialPanel("datepicker-field input-group");
            calendarImage = new Icon();
            calendarImage.setStyleName("ficon--calendar pointer");
            calendarImage.addClickHandler(event -> {
                if (textBox.isEnabled()) {
                    showPopupCalendar();
                }
            });

            Div iconDiv = new Div("input-group-append");
            Div iconChildDiv = new Div("input-group-text");
            iconChildDiv.add(calendarImage);
            iconDiv.add(iconChildDiv);

            if (appender != null) {
                Div apender = new Div("input-group-text");
                apender.add(appender);
                iconDiv.add(apender);
            }

            hPanel.add(textBox);
            hPanel.add(iconDiv);
            if (clearButton != null) {
                Image clearImage = new Image(clearButton);
                clearImage.setStyleName("pointer");
                clearImage.addClickHandler(sender -> setText(defaultValue));
                hPanel.add(clearImage);
            }
            initWidget(hPanel);
        } else {
            initWidget(textBox);
        }
    }

    private void invalidDate() {
        Info.show(wfmStrings.pleaseEnterTheDateValueInCorrectFormat() + " " + defaultFormat, Info.Type.WARNING);
        setDate(null);
        setText(defaultFormat);
    }

    /**
     * Display the PopupCalendar.
     */
    private void showPopup() {
        if (this.date != null) {
            popup.setDisplayedMonth(this.date);
        }
        popup.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
            final int textBoxAbsoluteTop = textBox.getAbsoluteTop();

            @Override
            public void setPosition(int offsetWidth, int offsetHeight) {
                int leftPosition = textBox.getAbsoluteLeft();
                int popupWidth = popup.getOffsetWidth();
                if (popupWidth + leftPosition > Window.getClientWidth()) {
                    leftPosition = Window.getClientWidth() - popupWidth - 10;
                }
                if (offsetHeight + textBox.getOffsetHeight() < Window.getClientHeight() - textBoxAbsoluteTop) {
                    popup.setPopupPosition(leftPosition, textBoxAbsoluteTop + textBox.getOffsetHeight(), isDefaultPosition());
                } else {
                    popup.setPopupPosition(leftPosition, textBoxAbsoluteTop - offsetHeight, isDefaultPosition());
                }

            }
        });
//		popup.setPopupPosition(this.textBox.getAbsoluteLeft(), this.textBox.getAbsoluteTop() + 20, isDefaultPosition());
        popup.displayMonth();
    }


    public void showPopupForCellWidget(final Integer left, final Integer top, final Integer height) {
        if (left != null && left != 0 && top != null && top != 0) {
            if (this.date != null) {
                popup.setDisplayedMonth(this.date);
            }
            popup.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
                final int textBoxAbsoluteTop = top;

                @Override
                public void setPosition(int offsetWidth, int offsetHeight) {
                    if (offsetHeight + textBox.getOffsetHeight() < Window.getClientHeight() - textBoxAbsoluteTop) {
                        popup.setPopupPosition(left, textBoxAbsoluteTop + height, isDefaultPosition());
                    } else {
                        popup.setPopupPosition(left, textBoxAbsoluteTop - offsetHeight, isDefaultPosition());
                    }
                }
            });
            popup.displayMonth();
        }
    }

    @Override
    public void addStyleName(String style) {
        textBox.addStyleName(style);
    }

    @Override
    public void removeStyleName(String style) {
        textBox.removeStyleName(style);
    }

    /**
     * Parse the date entered in the DatePicker.
     */
    private void parseDate() {
        if (this.date != null) {
            synchronizeFromDate();
            fireEvent(new ChangeEvent() {
                @Override
                protected void dispatch(ChangeHandler handler) {
                    super.dispatch(handler);
                }
            });
//			fireEvent(Events.SelectionChange);
        }
    }

    public void setDefaultValue() {
        setText(defaultValue);
    }
}