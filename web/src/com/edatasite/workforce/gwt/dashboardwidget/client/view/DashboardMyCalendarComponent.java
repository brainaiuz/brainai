package com.edatasite.workforce.gwt.dashboardwidget.client.view;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.DashboardComponentItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DashboardBaseWidget;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.LoadingWidgets;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiTimePicker;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.dashboardwidget.client.rpc.DashboardMyCalendarCarouselItem;
import com.edatasite.workforce.gwt.dashboardwidget.client.rpc.DashboardMyCalendarDetailItem;
import com.edatasite.workforce.gwt.dashboardwidget.client.rpc.DashboardWidgetService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Heading;
import gwt.material.design.client.ui.html.Icon;
import gwt.material.design.client.ui.html.Span;
import gwt.material.design.jquery.client.api.JQueryElement;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static gwt.material.design.jquery.client.api.JQuery.$;

/**
 * User: Abror Abdukadirov
 * Date: 12.05.2018 17:23
 */
public class DashboardMyCalendarComponent extends DashboardBaseWidget {

    private Div wrapperDiv;
    private MaterialPanel carousel;
    private MaterialPanel content;
    private Div activeDayDiv = new Div();
    private Div daysDiv;
    private Div daysNavigationDiv;
    private Div timerDiv;
    private DataListBox priorityListBox;
    private DatePicker datePicker;
    private KpiTimePicker startTime;
    private KpiTimePicker endTime;
    private boolean hasCarouselInitialized = false;
    private final DateTimeFormat timeFormat = DateTimeFormat.getFormat("HH:mm");
    private Date todayDate;
    private int selectedMonth;
    protected static final WfmStrings wfmStrings = WfmStrings.App.get();
//    protected static final MeetingMinutesString meetingMinutesString = MeetingMinutesString.App.get();
    private int selectedDay;

    public DashboardMyCalendarComponent(DashboardComponentItem gridItemConfig) {
        this.gridItemConfig = gridItemConfig;
    }

    @Override
    protected void initInternal() {
//        if (gridItemConfig != null && gridItemConfig.getName() != null) {
//            setTitle(gridItemConfig.getName());
//        } else {
//        }
        setTitle(accountingStrings.myAgenda());

        mainPanel.addStyleName("widget--agenda");
        carousel = new MaterialPanel("agenda-nav");
        content = new MaterialPanel("widget-content widget-list");

        Div calendarActionDiv = new Div("widget-heading__action");
        Div calendarBoxDiv = new Div("calendar-box");
        Div calendarBoxInputDiv = new Div("calendar-box__input");
        datePicker = new DatePicker(DateTimeFormat.getFormat("dd/MM/yyyy"));
        datePicker.setDate(new Date());
        datePicker.addChangeHandler(changeEvent -> {
            if (datePicker.getDate() != null) {
                selectedDay = DateUtil.getDay(datePicker.getDate());
                if (selectedMonth != DateUtil.getMonth(datePicker.getDate())) {
                    getMyCalendarDays();
                } else {
                    goToSlide();
                    selectecDayActive();
                    getDayDetailData(DateUtil.getDate(DateUtil.getYear(new Date()), selectedMonth, selectedDay));
                }
            }
        });
        calendarBoxInputDiv.add(datePicker);
        calendarBoxDiv.add(calendarBoxInputDiv);

        Div calendarBoxIconDiv = new Div("calendar-box__icon");
        Icon calendarIcon = new Icon();
        calendarIcon.setStyleName("ficon--calendar2");
        calendarIcon.addClickHandler(event -> datePicker.showPopupCalendar());
        calendarBoxIconDiv.add(calendarIcon);
        calendarBoxDiv.add(calendarBoxIconDiv);
        calendarActionDiv.add(calendarBoxDiv);
        actionPanel.add(calendarActionDiv);

        daysNavigationDiv = new Div("agenda-nav__actions");
        MaterialLink prevLink = new MaterialLink();
        prevLink.setStyleName("agenda-nav__action-prev");
        Icon prevIcon = new Icon();
        prevIcon.setStyleName("ficon--chevron-left");
        prevLink.add(prevIcon);
        daysNavigationDiv.add(prevLink);

        MaterialLink nextLink = new MaterialLink();
        nextLink.setStyleName("agenda-nav__action-next");
        Icon nextIcon = new Icon();
        nextIcon.setStyleName("ficon--chevron-right");
        nextLink.add(nextIcon);
        daysNavigationDiv.add(nextLink);

        wrapperDiv = new Div("gwt-wrapper");
        wrapperDiv.add(drawAddNewPanel());
        wrapperDiv.add(content);

        contentPanel.removeFromParent();
        mainPanel.add(carousel);
        mainPanel.add(contentPanel);
        contentPanel.add(wrapperDiv);
        startTime.initialize();
        endTime.initialize();

        daysDiv = new Div("agenda-nav__days");
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CALENDAR_EVENT_ADD, DashboardMyCalendarComponent.this, (sender, args) -> loadComponentData());
    }

    @Override
    protected void getData() {
        clearPanel();
        getMyCalendarDays();
    }

    @Override
    protected void getSampleData(boolean nodata) {
        clearPanel();
        getMyCalendarSampleDays();
    }

    private void getMyCalendarDays() {
        selectedMonth = DateUtil.getMonth(datePicker.getDate());
        LoadingWidgets.get(getCode()).show();
        DashboardWidgetService.App.get().getMyCalendarDays(new DateNonConvertable(datePicker.getDate()), new AbstractAsyncCallback<DashboardMyCalendarCarouselItem>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingWidgets.get(getCode()).hide();
            }

            @Override
            public void onSuccess(DashboardMyCalendarCarouselItem result) {
                LoadingWidgets.get(getCode()).hide();
                drawCarouselPanel(result);
            }
        });
    }

    private void getMyCalendarSampleDays() {
        selectedMonth = DateUtil.getMonth(datePicker.getDate());
        LoadingWidgets.get(getCode()).show();
        DashboardWidgetService.App.get().getMyCalendarSampleDays(new DateNonConvertable(datePicker.getDate()), new AbstractAsyncCallback<DashboardMyCalendarCarouselItem>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingWidgets.get(getCode()).hide();
            }

            @Override
            public void onSuccess(DashboardMyCalendarCarouselItem result) {
                LoadingWidgets.get(getCode()).hide();
                drawCarouselPanel(result);
            }
        });
    }

    private void getDayDetailSampleData() {
        List<DashboardMyCalendarDetailItem> result = new ArrayList<>();

        Date startDate = new Date();
        int hour = startDate.getHours();
        int minutes = startDate.getMinutes();
        if (minutes < 30) {
            minutes += 30;
        } else {
            minutes = 30 - (60 - minutes);
            hour++;
        }
        Date endDate = new Date(startDate.getYear(), startDate.getMonth(), startDate.getDate(), hour, minutes, 59);

        DashboardMyCalendarDetailItem item = new DashboardMyCalendarDetailItem();
        item.setName("Alex Polay");
        item.setDescription("Incoming call");
        item.setStartDate(startDate);
        item.setEndDate(endDate);
        item.setType(DashboardMyCalendarCarouselItem.CALL);
        result.add(item);

        item = new DashboardMyCalendarDetailItem();
        item.setName("Invite colleagues to your kpi.com");
        item.setDescription("Meeting Hall," + Utils.getUserCity());
        item.setStartDate(startDate);
        item.setEndDate(endDate);
        item.setType(DashboardMyCalendarCarouselItem.MEETING);
        result.add(item);

        item = new DashboardMyCalendarDetailItem();
        item.setName("Fill out profile -Provide more information for your profile");
        item.setDescription("Download a copy of the Users Manual for product (https://www.kpi.com/presentations/)");
        item.setType(DashboardMyCalendarCarouselItem.TASK);
        result.add(item);

        drawDetailPanel(result);
    }

    private void drawCarouselPanel(DashboardMyCalendarCarouselItem data) {
        Div daysHolder = drawDays(data);
        carousel.clear();
        carousel.add(daysHolder);
        carousel.add(daysNavigationDiv);

        if (!hasCarouselInitialized) {
            reinitialize(true, 0);
            hasCarouselInitialized = true;
        } else {
            destroy();
            reinitialize(true, 0);
        }
        if (enableToShowSample) {
            getDayDetailSampleData();
        } else {
            if (todayDate != null) {
                getDayDetailData(todayDate);
            }
        }
    }

    private Div drawAddNewPanel() {
        Div finderPanel = new Div();
        finderPanel.addStyleName("widget-row widget-finder");

        TextBox nameBox = new TextBox();
        nameBox.setStyleName("form-control");
        nameBox.setPlaceHolder(Property.get(Constants.EVENT_LIST, wfmStrings.meetingAnEventOrPhoneCall(), wfmStrings.event().toLowerCase()));
        nameBox.addKeyPressHandler(keyPressEvent -> {
            if (keyPressEvent.getNativeEvent().getKeyCode() == (char) KeyCodes.KEY_ENTER) {
                if (nameBox.getText() != null && !"".equals(nameBox.getText().trim()) && priorityListBox.getSelectedId() != null) {
                    addNewItem(nameBox);
                }
            }
        });
        nameBox.addClickHandler(clickEvent -> nameBox.removeStyleName(Constants.ERROR_FORM_STYLE));
        WfmButton2 addRow = new WfmButton2(null, "btn--circle btn--success");
        addRow.add(new SvgIcon(SvgEnum.plus));
        addRow.removeHasiconLeftStyle();
        addRow.addClickHandler(clickEvent -> {
            nameBox.removeStyleName(Constants.ERROR_FORM_STYLE);
            if (nameBox.getText() != null && !"".equals(nameBox.getText().trim()) && priorityListBox.getSelectedId() != null) {
                addNewItem(nameBox);
            } else {
                nameBox.addStyleName(Constants.ERROR_FORM_STYLE);
            }
        });

        Div iconDiv = new Div("widget-row__icon");
        iconDiv.add(addRow);
        finderPanel.add(iconDiv);

        Div nameDiv = new Div("widget-finder-search");
        nameDiv.add(nameBox);
        finderPanel.add(nameDiv);

        timerDiv = new Div("widget-finder-timer");
        Element timeDl = Document.get().createElement("dl");
        timeDl.setClassName("event-date--2");

        Element startTimeDd = Document.get().createElement("dd");
        startTimeDd.setClassName("input-group bootstrap-timepicker timepicker");
        startTime = new KpiTimePicker(false);
        startTime.setValue(KpiTimePicker.getHoursAndMinutes(new Date()));

        startTimeDd.appendChild(startTime.getElement());
        Element spanElement = Document.get().createElement("span");
        spanElement.setClassName("input-group-addon");
        Element iconElement = Document.get().createElement("i");
        iconElement.setClassName("ficon--waiting");
        spanElement.appendChild(iconElement);
        startTimeDd.appendChild(spanElement);
        startTimeDd.appendChild(Document.get().createElement("span"));
        timeDl.appendChild(startTimeDd);

        Element middleDd = Document.get().createElement("dd");
        Span middleSpan = new Span();
        middleSpan.getElement().setInnerHTML("-&nbsp;");
        middleDd.appendChild(middleSpan.getElement());
        timeDl.appendChild(middleDd);

        Element endTimeDd = Document.get().createElement("dd");
        endTimeDd.setClassName("input-group bootstrap-timepicker timepicker");
        endTime = new KpiTimePicker(false);
        int hour = new Date().getHours();
        int minutes = new Date().getMinutes();
        if (minutes < 30) {
            minutes += 30;
        } else {
            minutes = 30 - (60 - minutes);
            hour++;
        }
        endTime.setValue(new int[]{hour, minutes});

        endTimeDd.appendChild(endTime.getElement());
        Element spanElement2 = Document.get().createElement("span");
        spanElement2.setClassName("input-group-addon");
        Element iconElement2 = Document.get().createElement("i");
        iconElement2.setClassName("ficon--waiting");
        spanElement2.appendChild(iconElement2);
        endTimeDd.appendChild(spanElement2);
        endTimeDd.appendChild(Document.get().createElement("span"));
        timeDl.appendChild(endTimeDd);
        timerDiv.getElement().appendChild(timeDl);

        Div priorityDiv = new Div("widget-row__end");
        priorityListBox = new DataListBox();
        priorityListBox.setStyleName("form-control listBox-small listBox-cat todo-cat--2");
        priorityListBox.setWithoutNullLabel(true);
        priorityListBox.setItems(getPriorites());
        priorityListBox.setSelected(new SelectItem(1, wfmStrings.call(), DashboardMyCalendarCarouselItem.CALL));
        priorityListBox.addValueChangeHandler(event -> {
            if (priorityListBox.getSelectedItem() != null) {
                String type = priorityListBox.getSelectedItem().getDescription();
                if (DashboardMyCalendarCarouselItem.TASK.equals(type)) {
                    timerDiv.setDisplay(Display.NONE);
                } else {
                    timerDiv.setDisplay(Display.BLOCK);
                }
                switch (type) {
                    case DashboardMyCalendarCarouselItem.CALL:
                        priorityListBox.setStyleName("form-control listBox-small listBox-cat todo-cat--2");
                        break;
                    case DashboardMyCalendarCarouselItem.MEETING:
                        priorityListBox.setStyleName("form-control listBox-small listBox-cat todo-cat--3");
                        break;
                    case DashboardMyCalendarCarouselItem.TASK:
                        priorityListBox.setStyleName("form-control listBox-small listBox-cat todo-cat--1");
                        break;
                }
            }
        });
        priorityDiv.add(timerDiv);
        priorityDiv.add(priorityListBox);

        finderPanel.add(priorityDiv);

        return finderPanel;
    }

    private Div drawDays(DashboardMyCalendarCarouselItem data) {
        Div dayHolderDiv = new Div("agenda-nav__days-holder");

        todayDate = null;
        daysDiv.clear();
        if (hasCarouselInitialized) {
            removeAllDaysNative(daysDiv.getElement());
        }

        for (DashboardMyCalendarCarouselItem item : data.getDays()) {
            daysDiv.add(drawDay(item));
        }
        dayHolderDiv.add(daysDiv);
        return dayHolderDiv;
    }

    private Div drawDay(DashboardMyCalendarCarouselItem day) {
        Div dayDiv = new Div("agenda-day");

        if (day.getDate() != null && DateUtil.isToday(day.getDate().getNonConvertedDate())) {
            dayDiv.addStyleName("agenda-day--heading");
            Heading header = new Heading(HeadingSize.H6);
            header.setText(wfmStrings.today());
            header.getElement().setAttribute("style", "margin: 0!important;    " +
                    "    width: 100%;\n" +
                    "    position: absolute;\n" +
                    "    font-size: 12px;\n" +
                    "    margin: 0!important;\n" +
                    "    color: white;\n" +
                    "    background: grey;\n" +
                    "    top: 0;");
            dayDiv.add(header);

            todayDate = DateUtil.getDate(day.getYear(), day.getMonth(), day.getDay());
            if (selectedDay == DateUtil.getDay(day.getDate().getNonConvertedDate())) {
                activeDayDiv.removeStyleName("active");
                dayDiv.addStyleName("active");
                activeDayDiv = dayDiv;
            }
        } else {
            if (day.getDate() != null && selectedDay == DateUtil.getDay(day.getDate().getNonConvertedDate())) {
                activeDayDiv.removeStyleName("active");
                dayDiv.addStyleName("active");
                activeDayDiv = dayDiv;
            }
        }
        dayDiv.addClickHandler(event -> {
            activeDayDiv.removeStyleName("active");
            dayDiv.addStyleName("active");
            activeDayDiv = dayDiv;
            if (day.getDate() != null) {
                selectedDay = DateUtil.getDay(day.getDate().getNonConvertedDate());
            }
            if (!enableToShowSample) {
                getDayDetailData(DateUtil.getDate(day.getYear(), day.getMonth(), day.getDay()));
            }
        });
        Div markDiv = new Div("agenda-day__marks");
        if (day.isMeeting()) {
            Icon meetingIcon = new Icon();
            meetingIcon.setStyleName("todo-mark--meeting");
            markDiv.add(meetingIcon);
        }

        if (day.isCall()) {
            Icon callIcon = new Icon();
            callIcon.setStyleName("todo-mark--call");
            markDiv.add(callIcon);
        }

        if (day.isTask()) {
            Icon taskIcon = new Icon();
            taskIcon.setStyleName("todo-mark--task");
            markDiv.add(taskIcon);
        }

        Div signDiv = new Div("agenda-day__sign");
        signDiv.getElement().setInnerText(getWeekDayTitle(day.getWeekDay()));

        Div numberDiv = new Div("agenda-day__number");
        numberDiv.getElement().setInnerText(String.valueOf(day.getDay()));

        dayDiv.add(markDiv);
        dayDiv.add(signDiv);
        dayDiv.add(numberDiv);

        return dayDiv;
    }

    private void getDayDetailData(Date date) {
        LoadingWidgets.get(getCode()).show();
        DashboardWidgetService.App.get().getMyCalendarDetailData(new DateNonConvertable(date), new AbstractAsyncCallback<ArrayList<DashboardMyCalendarDetailItem>>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingWidgets.get(getCode()).hide();
            }

            @Override
            public void onSuccess(ArrayList<DashboardMyCalendarDetailItem> result) {
                LoadingWidgets.get(getCode()).hide();
                drawDetailPanel(result);
            }
        });
    }

    private void drawDetailPanel(List<DashboardMyCalendarDetailItem> items) {
        content.clear();
        if (items.isEmpty()) {
            getEmptyMessagePanel().getElement().setInnerText(getEmptyText());
            wrapperDiv.add(getEmptyMessagePanel());
            return;
        }
        hideEmptyMessage();
        for (DashboardMyCalendarDetailItem item : items) {
            Div rowDiv = new Div("widget-row");

            Div iconDiv = new Div("widget-row__icon");
            Div textDiv = new Div("widget-row__text");
            Div endDiv = new Div("widget-row__end");

            Icon icon = new Icon();
            iconDiv.add(icon);

            Element dl = Document.get().createElement("dl");
            Element dt = Document.get().createElement("dt");
            Element dd = Document.get().createElement("dd");
            textDiv.getElement().appendChild(dl);

            Span labelSpan = new Span();
            labelSpan.setStyleName("cat-label");
            dt.appendChild(labelSpan.getElement());
            Span labelSpan2 = new Span();
            labelSpan2.getElement().setInnerText(" " + item.getName());
            dt.appendChild(labelSpan2.getElement());

            Span descriptionSpan = new Span();
            descriptionSpan.getElement().setInnerText(item.getDescription());
            dd.appendChild(descriptionSpan.getElement());

            Div actionDiv = new Div("todo-action");
            endDiv.add(actionDiv);
            Span typeSpan = new Span();
            typeSpan.setStyleName("todo-indicator");

            Element em = Document.get().createElement("em");
            typeSpan.getElement().appendChild(em);

            if (!DashboardMyCalendarCarouselItem.TASK.equals(item.getType())) {
                Element dateDl = Document.get().createElement("dl");
                dateDl.setClassName("event-date");
                Element startDt = Document.get().createElement("dt");
                startDt.setInnerText(wfmStrings.start());
                Element startDd = Document.get().createElement("dd");
                Span startTimeSpan = new Span(timeFormat.format(item.getStartDate()));
                startDd.appendChild(startTimeSpan.getElement());

                Element endDt = Document.get().createElement("dt");
                endDt.setInnerText(wfmStrings.end());
                Element endDd = Document.get().createElement("dd");
                Span endTimeSpan = new Span(timeFormat.format(item.getEndDate()));
                endDd.appendChild(endTimeSpan.getElement());

                dateDl.appendChild(startDt);
                dateDl.appendChild(startDd);
                dateDl.appendChild(endDt);
                dateDl.appendChild(endDd);

                endDiv.getElement().appendChild(dateDl);
            }
            switch (item.getType()) {
                case DashboardMyCalendarCarouselItem.CALL:
                    rowDiv.addStyleName("widget-row--call");
                    icon.setStyleName("ficon--phone2");

                    labelSpan.getElement().setInnerText(wfmStrings.call());

                    actionDiv.addStyleName("todo-cat--2");
                    em.setInnerText(wfmStrings.call());

                    rowDiv.addClickHandler(event -> {
                        String url = GWT.getHostPageBaseURL() + "/Crm.html#event|summary/" + item.getObjectId();
                        Window.open(url, "_blank", null);
                    });
                    break;
                case DashboardMyCalendarCarouselItem.MEETING:
                    rowDiv.addStyleName("widget-row--meeting");
                    icon.setStyleName("ficon--users");

                    labelSpan.getElement().setInnerText(wfmStrings.meetingMinutes());

                    actionDiv.addStyleName("todo-cat--3");
                    em.setInnerText(wfmStrings.meetingMinutes());

                    rowDiv.addClickHandler(event -> {
                        String url = GWT.getHostPageBaseURL() + "/Crm.html#event|summary/" + item.getObjectId();
                        Window.open(url, "_blank", null);
                    });
                    break;
                case DashboardMyCalendarCarouselItem.TASK:
                    rowDiv.addStyleName("widget-row--task");
                    icon.setStyleName("ficon--check-circle");

                    labelSpan.getElement().setInnerText(wfmStrings.task().toUpperCase());

                    actionDiv.addStyleName("todo-cat--1");
                    em.setInnerText(wfmStrings.task().toUpperCase());

                    rowDiv.addClickHandler(event -> {
                        String url = GWT.getHostPageBaseURL() + "/ProjectManagement.html#task|summary/" + item.getObjectId();
                        Window.open(url, "_blank", null);
                    });
                    break;
            }
            dl.appendChild(dt);
            dl.appendChild(dd);

            actionDiv.add(typeSpan);

            rowDiv.add(iconDiv);
            rowDiv.add(textDiv);
            rowDiv.add(endDiv);

            content.add(rowDiv);
        }
    }

    private void addNewItem(TextBox subjectTextBox) {
        LoadingWidgets.get(getCode()).show();
        String type = priorityListBox.getSelectedItem().getDescription();
        Date selectedDate = DateUtil.getDate(DateUtil.getYear(new Date()), selectedMonth, selectedDay);
        Date startDate = new Date(selectedDate.getYear(), selectedDate.getMonth(), selectedDate.getDate(), startTime.getValue()[0], startTime.getValue()[1], 0);
        Date endDate = new Date(selectedDate.getYear(), selectedDate.getMonth(), selectedDate.getDate(), endTime.getValue()[0], endTime.getValue()[1], 59);

        if (!DashboardMyCalendarCarouselItem.TASK.equals(type)) {
            if (!validate(startDate, endDate)) {
                LoadingWidgets.get(getCode()).hide();
                return;
            }
        }
        DashboardMyCalendarDetailItem item = new DashboardMyCalendarDetailItem();
        item.setName(subjectTextBox.getText());
        item.setType(type);
        item.setStartDate(startDate);
        if (DashboardMyCalendarCarouselItem.TASK.equals(type)) {
            item.setEndDate(startDate);
        } else {
            item.setEndDate(endDate);
        }
        DashboardWidgetService.App.get().saveMyCalendarItem(item, new AbstractAsyncCallback<Integer>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingWidgets.get(getCode()).hide();
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void onSuccess(Integer result) {
                if (result == 0) {
                    LoadingWidgets.get(getCode()).hide();
                    Info.warn(wfmStrings.sureEnteredAllData());
                } else {
                    subjectTextBox.setText("");
                    activeDayChangeColors(type);
                    LoadingWidgets.get(getCode()).hide();
                    getDayDetailData(DateUtil.getDate(DateUtil.getYear(new Date()), selectedMonth, selectedDay));
                }
            }
        });
    }

    private boolean validate(Date startDate, Date endDate) {
        endTime.removeStyleName(Constants.ERROR_FORM_STYLE);
        if (!Validation.validateDateOrder(startDate, endDate, null, false)) {
            endTime.addStyleName(Constants.ERROR_FORM_STYLE);
            return false;
        }
        return true;
    }

    private void activeDayChangeColors(String type) {
        if (activeDayDiv.getWidgetCount() <= 0) {
            return;
        }
        for (int i = 0; i < activeDayDiv.getWidgetCount(); i++) {
            Widget divWidget = activeDayDiv.getWidget(i);
            if (!(divWidget instanceof Div)) {
                continue;
            }
            Div markDiv = (Div) divWidget;
            if (!"agenda-day__marks".equals(markDiv.getStyleName())) {
                continue;
            }
            if (markDiv.getWidgetCount() == 3) {
                break;
            }
            if (markDiv.getWidgetCount() == 0) {
                Icon icon = new Icon();
                icon.setStyleName(getIconStyleName(type));
                markDiv.add(icon);
            } else {
                String iconStyleName = getIconStyleName(type);
                Map<String, Icon> icons = new HashMap<>();
                for (int j = 0; j < markDiv.getWidgetCount(); j++) {
                    Widget iconWidget = markDiv.getWidget(j);
                    if (!(iconWidget instanceof Icon)) {
                        continue;
                    }
                    Icon icon = (Icon) iconWidget;
                    icons.put(icon.getStyleName(), icon);
                }
                if (icons.size() > 0 && icons.get(iconStyleName) == null) {
                    Icon newIcon = new Icon();
                    newIcon.setStyleName(iconStyleName);
                    icons.put(iconStyleName, newIcon);

                    markDiv.clear();
                    if (icons.get("todo-mark--meeting") != null) {
                        markDiv.add(icons.get("todo-mark--meeting"));
                    }
                    if (icons.get("todo-mark--call") != null) {
                        markDiv.add(icons.get("todo-mark--call"));
                    }
                    if (icons.get("todo-mark--task") != null) {
                        markDiv.add(icons.get("todo-mark--task"));
                    }
                }
            }
            break;
        }
    }

    private String getWeekDayTitle(int weekDay) {
        switch (weekDay) {
            case 1:
                return wfmStrings.shortSunday();
            case 2:
                return wfmStrings.shortMonday();
            case 3:
                return wfmStrings.shortTuesday();
            case 4:
                return wfmStrings.shortWedn();
            case 5:
                return wfmStrings.shortThurs();
            case 6:
                return wfmStrings.shortFriday();
            case 7:
                return wfmStrings.shortSaturday();
            default:
                return "-";
        }
    }

    private String getIconStyleName(String type) {
        switch (type) {
            case DashboardMyCalendarCarouselItem.CALL:
                return "todo-mark--call";
            case DashboardMyCalendarCarouselItem.MEETING:
                return "todo-mark--meeting";
            case DashboardMyCalendarCarouselItem.TASK:
                return "todo-mark--task";
            default:
                return "";
        }
    }

    private SelectItem[] getPriorites() {
        List<SelectItem> priorites = new ArrayList<>();
        priorites.add(new SelectItem(1, wfmStrings.call(), DashboardMyCalendarCarouselItem.CALL));
        priorites.add(new SelectItem(2, wfmStrings.meetingMinutes(), DashboardMyCalendarCarouselItem.MEETING));
        priorites.add(new SelectItem(3, wfmStrings.task(), DashboardMyCalendarCarouselItem.TASK));

        for (SelectItem priority : priorites) {
            switch (priority.getDescription()) {
                case DashboardMyCalendarCarouselItem.CALL:
                    priority.setStyleName("todo-cat--2");
                    break;
                case DashboardMyCalendarCarouselItem.MEETING:
                    priority.setStyleName("todo-cat--3");
                    break;
                case DashboardMyCalendarCarouselItem.TASK:
                    priority.setStyleName("todo-cat--1");
                    break;
            }

        }
        return priorites.toArray(new SelectItem[]{});
    }

    private void goToSlide() {
        if (datePicker.getDate() != null) {
            int index = DateUtil.getDay(datePicker.getDate());
            if (index > 0) {
                if (index <= 10) {
                    index = 0;
                } else if (index <= 20) {
                    index = 10;
                } else if (index <= 30) {
                    index = 20;
                } else {
                    index = 30;
                }
            }
            goToNative(index);
        }
    }

    private void goToSlideByIndex(int index) {
        this.goToByIndexNative(index);
    }

    private void selectecDayActive() {
        JQueryElement jElement = $(".agenda-day[data-slick-index=" + (selectedDay - 1) + "]");

        if (jElement == null || jElement.asElement() == null) {
            return;
        }
        activeDayDiv.removeStyleName("active");
        jElement.asElement().addClassName("active");

        if (daysDiv.getWidgetCount() <= 0) {
            return;
        }
        for (int i = 0; i < daysDiv.getWidgetCount(); i++) {
            Widget widget = daysDiv.getWidget(i);
            if (widget != null && jElement.asElement().equals(widget.getElement())) {
                activeDayDiv = (Div) widget;
                return;
            }
        }
    }

    private void destroy() {
        this.destroyNative(daysDiv.getElement());
    }

    private void reinitialize(Boolean goTo, int index) {
        this.reinitializeNative(daysDiv.getElement(), goTo, index);
    }

    private native void goToByIndexNative(int index) /*-{
        var slide = $wnd.$('.agenda-nav__days');
        if (slide) {
            slide.slick('slickGoTo', index, false);
        }
    }-*/;

    private native void goToNative(int index) /*-{
        var that = this;
        var slide = $wnd.$('.agenda-nav__days');
        if (slide) {
            var width = $wnd.$('.agenda-nav__days').find('.slick-track').width();
            if (typeof $wnd.$.fn.slick !== 'undefined' && width > 0) {
                slide.slick('slickGoTo', index, false);
            } else {
                setTimeout(function () {
                    that.@com.edatasite.workforce.gwt.dashboardwidget.client.view.DashboardMyCalendarComponent::destroy()();
                    that.@com.edatasite.workforce.gwt.dashboardwidget.client.view.DashboardMyCalendarComponent::reinitialize(Ljava/lang/Boolean;I)(false, index);
                }, 4000);
            }
        }
    }-*/;

    private native void removeAllDaysNative(Element element) /*-{
        $wnd.$(element).slick('slickRemove');
    }-*/;

    private native void destroyNative(Element element) /*-{
        $wnd.$(element).slick('unslick');
    }-*/;

    private native void reinitializeNative(Element element, boolean goTo, int index) /*-{
        var that = this;
        $wnd.$(function () {
            $wnd.$(element).slick({
                infinite: false,
                draggable: false,
                speed: 600,
                slidesToShow: 10,
                slidesToScroll: 10,
                prevArrow: $wnd.$('.agenda-nav__action-prev'),
                nextArrow: $wnd.$('.agenda-nav__action-next')
            });
            if (goTo && index === 0) {
                that.@com.edatasite.workforce.gwt.dashboardwidget.client.view.DashboardMyCalendarComponent::goToSlide()();
            } else {
                that.@com.edatasite.workforce.gwt.dashboardwidget.client.view.DashboardMyCalendarComponent::goToSlideByIndex(I)(index);
            }
        });
    }-*/;

    @Override
    protected String getEmptyText() {
        return Property.getPluralWithObjectCodeWithReplace(Constants.EVENT_LIST, wfmStrings.currentlyYouDoNotHaveAnyActivities(), wfmStrings.activities());
    }

    @Override
    public String getCode() {
        return DASHBOARD_WIDGET_CODE.MY_CALENDAR;
    }

    @Override
    protected void clearPanel() {
        datePicker.setDate(new Date());
        selectedMonth = DateUtil.getMonth(datePicker.getDate());
        selectedDay = DateUtil.getDay(datePicker.getDate());
        Date currentDate = new Date();
        startTime.setValue(KpiTimePicker.getHoursAndMinutes(currentDate));
        int hour = currentDate.getHours();
        int minutes = currentDate.getMinutes();
        if (minutes < 30) {
            minutes += 30;
        } else {
            minutes = 30 - (60 - minutes);
            hour++;
        }
        endTime.setValue(new int[]{hour, minutes});
        priorityListBox.setSelected(new SelectItem(1, wfmStrings.call(), DashboardMyCalendarCarouselItem.CALL));
        priorityListBox.setStyleName("form-control listBox-small listBox-cat todo-cat--2");
        timerDiv.setDisplay(Display.BLOCK);
    }
}
