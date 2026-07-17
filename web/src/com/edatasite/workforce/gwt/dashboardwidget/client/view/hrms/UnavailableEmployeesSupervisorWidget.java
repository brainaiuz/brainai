package com.edatasite.workforce.gwt.dashboardwidget.client.view.hrms;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.DashboardComponentItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DashboardBaseWidget;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.LoadingWidgets;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.dashboardwidget.client.rpc.DashboardWidgetService;
import com.edatasite.workforce.gwt.dashboardwidget.client.rpc.LRPC;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.ui.MaterialCollapsible;
import gwt.material.design.client.ui.MaterialCollapsibleBody;
import gwt.material.design.client.ui.MaterialCollapsibleHeader;
import gwt.material.design.client.ui.MaterialCollapsibleItem;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Anchor;
import gwt.material.design.client.ui.html.DD;
import gwt.material.design.client.ui.html.DL;
import gwt.material.design.client.ui.html.DT;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.FigCaption;
import gwt.material.design.client.ui.html.FigureWidget;
import gwt.material.design.client.ui.html.Heading;
import gwt.material.design.client.ui.html.Icon;
import gwt.material.design.client.ui.html.Label;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by Dilsh0d Madrahimov on 9/21/16 6:31 PM
 */
public class UnavailableEmployeesSupervisorWidget extends DashboardBaseWidget {

    private Integer selectedYear = Integer.parseInt(DateUtils.yearFormat.format(new Date()));
    private static final WfmMessages wfmMessages = WfmMessages.App.get();
    private static final DateTimeFormat dateFormat = DateTimeFormat.getFormat("dd-MMM");
    private DataListBox reasonListBox;
    private TextBox nameBox;
    private DatePicker datePicker;
    private static final MaterialCollapsible collapsible = new MaterialCollapsible();
    private MaterialPanel content;
    private MaterialPanel footerPanel;
    private int step = 0;
    private int limit = 20;
    private int monthcount = 0;
    private MaterialCollapsibleItem[] citems = new MaterialCollapsibleItem[12];
    private Span loadingbar;
    private Div noDataContent;
    private boolean nodata;

    public UnavailableEmployeesSupervisorWidget(DashboardComponentItem componentConf) {
        this.gridItemConfig = componentConf;
    }

    @Override
    protected void initInternal() {
        title.removeFromParent();
        filterPanel.removeFromParent();
        headerRow.remove(actionPanel);
        reasonListBox = new DataListBox();
        reasonListBox.setNullLabel(accountingStrings.allTypes());
        mainPanel.addStyleName("widget--updates widget--updates-links");
        Div titleDiv = new Div("widget-heading__action");
        headerRow.add(titleDiv);
        titleDiv.getElement().setInnerHTML(wfmStrings.unavailableEmployees());
        Div typeDiv = new Div("widget-heading__dropdown");
        typeDiv.add(reasonListBox);
        headerRow.add(typeDiv);
        headerRow.add(actionPanel);


        Div calendarActionDiv = new Div("widget-heading__action");
        Div calendarBoxDiv = new Div("calendar-box");
        Div calendarBoxInputDiv = new Div("calendar-box__input");
        datePicker = new DatePicker(DateTimeFormat.getFormat("MMMM yyyy"));
        datePicker.getElement().getStyle().setOpacity(0);
        calendarBoxInputDiv.add(datePicker);
        datePicker.addChangeHandler(changeEvent -> {
            step = 0;
            if (datePicker.getDate() == null) {
                datePicker.getElement().getStyle().setOpacity(0);
                selectedYear = Integer.parseInt(DateUtils.yearFormat.format(new Date()));
            } else {
                selectedYear = Integer.parseInt(DateUtils.yearFormat.format(datePicker.getDate()));
                datePicker.getElement().getStyle().setOpacity(1);
            }

            loadData(false);
        });
        calendarBoxDiv.add(calendarBoxInputDiv);

        Div calendarBoxIconDiv = new Div("btn btn--icon calendar-box__icon");
        Icon calendarIcon = new Icon();
        calendarIcon.setStyleName("ficon--calendar2");
        calendarIcon.addClickHandler(event -> datePicker.showPopupCalendar());
        calendarBoxIconDiv.add(calendarIcon);
        calendarBoxDiv.add(calendarBoxIconDiv);
        calendarActionDiv.add(calendarBoxDiv);
        actionPanel.add(calendarActionDiv);

        content = new MaterialPanel();
        content.setStyleName("widget-content");

        footerPanel = new MaterialPanel("widget-footer");
        loadingbar = new Span();
        loadingbar.setStyleName("blue widget-loading--svg widget-loading");
        loadingbar.setVisible(false);

        WfmButton2 moreButton = new WfmButton2(null, "btn btn-lg btn-block text-center");
        moreButton.getElement().setInnerText(wfmStrings.loadMore());
        if (!enableToShowSample) {
            moreButton.addClickHandler(clickEvent -> {
                step++;
                loadData(true);
            });
        }
        footerPanel.add(loadingbar);
        footerPanel.add(moreButton);
        mainPanel.add(footerPanel);

        createSearchPanel();

        collapsible.addStyleName("collapsible--arrows-left updates-list");
        collapsible.setAccordion(false);
        content.add(collapsible);

        contentPanel.add(content);


        DashboardWidgetService.App.get().getCustomReasons(new AsyncCallback<SelectItem[]>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingWidgets.get(getCode()).hide();
            }

            @Override
            public void onSuccess(SelectItem[] items) {
                reasonListBox.setItems(items);
                LoadingWidgets.get(getCode()).hide();
            }
        });
        noDataContent = new Div("chart-no-data");
        noDataContent.getElement().setInnerHTML(wfmStrings.noDataAvailable());
        initHandler();
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_LEAVER_REQUEST_DELETE, UnavailableEmployeesSupervisorWidget.this, (sender, args) -> loadComponentData());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_LEAVER_REQUEST_REJECTED, UnavailableEmployeesSupervisorWidget.this, (sender, args) -> loadComponentData());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_LEAVER_REQUEST_APPROVED, UnavailableEmployeesSupervisorWidget.this, (sender, args) -> loadComponentData());
    }

    private void createSearchPanel() {
        MaterialPanel header = new MaterialPanel("widget-row widget-finder");
        Div nameDiv = new Div("widget-finder-search");
        Label nameLabel = new Label(wfmStrings.searchEmployee());
        nameLabel.getElement().setAttribute("for", "widget-finder-employee-search");
        nameLabel.setStyleName("widget-finder-search-1");

        nameBox = new TextBox();
        nameBox.setStyleName("widget-finder-search-1  form-control");
        nameBox.getElement().setId("widget-finder-employee-search");
        nameBox.addKeyPressHandler(keyPressEvent -> {
            if (keyPressEvent.getNativeEvent().getKeyCode() == (char) KeyCodes.KEY_ENTER) {
                loadData(false);
            }
        });
        nameDiv.add(nameBox);
        nameDiv.add(nameLabel);
        header.add(nameDiv);
        Div endDiv = new Div("widget-row__end");
        Anchor searchBtn = new Anchor();
        searchBtn.addStyleName("widget-finder-search__button");
        searchBtn.getElement().getStyle().setCursor(Style.Cursor.POINTER);
        searchBtn.addClickHandler((event) -> loadData(false));
        MaterialIcon searchIcon = new MaterialIcon();
        searchIcon.addStyleName("ficon--search");
        searchBtn.add(searchIcon);
        endDiv.add(searchBtn);
        header.add(endDiv);
        contentPanel.add(header);
    }

    private void initHandler() {
        reasonListBox.addValueChangeHandler(changeEvent -> {
            step = 0;
            loadData(false);
        });
    }

    @Override
    protected void getData() {
        step = 0;
        reasonListBox.setSelectedNullLabel();
        selectedYear = Integer.parseInt(DateUtils.yearFormat.format(new Date()));
        nameBox.setValue("");
        datePicker.setDate(DateUtil.getMonthLastDateWithTime(new Date()));
        loadData(false);
    }

    @Override
    protected void getSampleData(boolean nodata) {
        collapsible.clear();
        ArrayList<LRPC> result = new ArrayList<>();
        LRPC sampleData = new LRPC();
        sampleData.setReasonColor("cc0000");
        sampleData.setReason(wfmStrings.annualLeave());
        sampleData.setEmployeeName(Utils.getUserFullName());
        sampleData.setEndDate(new DateNonConvertable(dateFormat.parse("12-May")));
        sampleData.setStartDate(new DateNonConvertable(dateFormat.parse("20-Apr")));
        result.add(sampleData);
        citems = new MaterialCollapsibleItem[12];
        enrichCItems(result, false, nodata);

        if (citems != null) {
            for (int i = 11; i >= 0; i--) {
                if (citems[i] != null) {
                    collapsible.add(citems[i]);
                }
            }
        }
    }

    private void loadData(boolean increment) {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setLimit(20);
        fp.setReasonID(reasonListBox.getSelectedId());
        fp.setStatusCode(Constants.LR_STATUS_SS_APPROVED);
        fp.setDate(datePicker.getDate());
        fp.setSearchKey(nameBox.getValue() != null && nameBox.getValue().length() > 0 ? nameBox.getValue().trim() : "");
        if (increment) {
            step++;
        }
        fp.setStart(step * 20);
        loadingbar.setVisible(true);
        DashboardWidgetService.App.get().getLeaveRequestList(fp, new AsyncCallback<ArrayList<LRPC>>() {
            @Override
            public void onFailure(Throwable throwable) {
                loadingbar.setVisible(false);
                GWT.log(throwable.getMessage());
                noData();
            }

            @Override
            public void onSuccess(ArrayList<LRPC> result) {
                noDataContent.removeFromParent();
                contentPanel.removeStyleName(noDataClass);
                loadingbar.setVisible(false);
                if (collapsible != null) {
                    collapsible.clear();
                }
                if (step > 0) { // loadMore ?
                    if (increment && (result == null || result.size() == 0)) {
                        step--;
                        if (nodata) {
                            noData();
                        }
                    } else {
                        nodata = false;
                        enrichCItems(result, true, false);
                    }

                } else { // new
                    if (result == null || result.size() == 0) { //empty
                        noData();
                    } else {
                        nodata = false;
                        citems = new MaterialCollapsibleItem[12];
                        enrichCItems(result, false, false);
                    }
                }
                if (citems != null) {
                    for (int i = 11; i >= 0; i--) {
                        MaterialCollapsibleItem citem = citems[i];
                        if (citem != null) {
                            collapsible.add(citem);
                            citem.setParent(collapsible);
                            citem.expand();
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void noData() {
        nodata = true;
        contentPanel.add(noDataContent);
        contentPanel.addStyleName(noDataClass);
        getSampleData(true);
    }

    private void enrichCItems(List<LRPC> result, boolean more, boolean nodata) {
        ArrayList<LRPC>[] mData = createData(result);
        if (datePicker.getDate() == null || nodata) {
            for (int i = 11; i >= 0; i--) {
                attachDataWidgets(mData, more, i);
            }
        } else {
            attachDataWidgets(mData, more, datePicker.getDate().getMonth());
        }
        if (citems != null) {
            for (int i = 11; i >= 0; i--) {
                if (citems[i] != null) {
                    collapsible.add(citems[i]);
                }
            }
        }
    }

    private void attachDataWidgets(ArrayList<LRPC>[] mData, boolean more, int i) {
        if (mData[i] != null && mData[i].size() > 0) {
            if ((more && citems[i] == null) || !more) {
                citems[i] = getNewCollapsibleItem(i);
            }
            for (LRPC request : mData[i]) {
                citems[i].getBody().add(drawRow(request, i));
            }
        }
    }

    private ArrayList<LRPC>[] createData(List<LRPC> result) {
        ArrayList<LRPC>[] mData = new ArrayList[12];
        for (int i = 0; i < 12; i++) {
            mData[i] = new ArrayList<>();
        }
        for (LRPC request : result) {
            Date startDate = request.getStartDate().getNonConvertedDate();
            int start = startDate.getMonth();
            Date endDate = request.getEndDate().getNonConvertedDate();
            int end = endDate.getMonth();
            int diff = 0;
            if (startDate.getYear() < endDate.getYear()) {
                if (DateUtil.getYear(startDate).equals(selectedYear)) {
                    end = 11;
                } else {
                    start = 0;
                }
            }
            diff = end - start;
            while (diff >= 0) {
                mData[start + diff--].add(request);
            }
        }
        for (ArrayList<LRPC> month : mData) {
            if (month != null && month.size() > 0) {
                month.sort(new Comparator<LRPC>() {
                    @Override
                    public int compare(LRPC o1, LRPC o2) {
                        return o2.getStartDate().getNonConvertedDate().compareTo(o1.getStartDate().getNonConvertedDate());
                    }
                });
            }
        }
        return mData;
    }

    private MaterialCollapsibleItem getNewCollapsibleItem(int monthOrder) {
        String[] monthNames = new String[]{wfmStrings.january(), wfmStrings.february(),
                wfmStrings.march(), wfmStrings.april(), wfmStrings.may(),
                wfmStrings.june(), wfmStrings.july(), wfmStrings.august(),
                wfmStrings.september(), wfmStrings.october(), wfmStrings.november(),
                wfmStrings.december()};
        MaterialCollapsibleItem collapsibleItem = new MaterialCollapsibleItem();
        MaterialCollapsibleHeader header = new MaterialCollapsibleHeader();
        MaterialCollapsibleBody body_ = new MaterialCollapsibleBody();
        Heading h3 = new Heading(HeadingSize.H3);
        Span span = new Span(monthNames[monthOrder]);
        h3.add(span);
        header.add(h3);
        collapsibleItem.add(header);
        collapsibleItem.add(body_);
        return collapsibleItem;
    }

    @Override
    public String getCode() {
        return DASHBOARD_WIDGET_CODE.UNAVAILABLE_EMPLOYEES_SUPERVISION;
    }

    @Override
    protected String getEmptyText() {
        return accountingStrings.currentlyThereAreNoUpcomingEmployees();
    }

    /**
     * <div class="updates-row">
     * drawTimeDiv(request);
     * drawImageDiv(request);
     * drawTextDiv(request);
     * </div>
     */

    public Div drawRow(LRPC request, int forMonth) {

//        Date startDate = request.getStartDate().getNonConvertedDate();
//        Date endate = request.getEndDate().getNonConvertedDate();
        /*if (startDate.getMonth() != forMonth) {
            Date mod = startDate;
            mod.setMonth(forMonth);
            mod = DateUtil.getMonthFirstDay(mod);
            mod.setYear(endate.getYear());
            request.setStartDate(new DateNonConvertable(mod));
        }
        if (endate.getMonth() != forMonth) {
            Date mod = endate;
            mod.setMonth(forMonth);
            mod.setYear(startDate.getYear());
            mod = DateUtil.getMonthLastDate(mod);
            request.setEndDate(new DateNonConvertable(mod));
        }*/
        Div row = new Div();
        if (request.getEmployeeId() != null) {
            row.addClickHandler(event -> {
                if (Utils.isHRMS()) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("employeeProfile|hrmsleaveRequests/" + request.getEmployeeId());
                } else {
                    Utils.openURL(GWT.getHostPageBaseURL() + "Hrms.html#employeeProfile|hrmsleaveRequests/" + request.getEmployeeId());
                }
            });
        }
        row.add(drawTimeDiv(request));
        row.add(drawImageDiv(request));
        row.add(drawTextDiv(request));
        row.addStyleName("updates-row");
        return row;
    }


    /**
     * <div class="updates-row__time">
     * <dl>
     * <dt>1-MAY - 10-MAY</dt>
     * <dd>9 DAYS</dd>
     * </dl>
     * <i class="sort-mark" style="background: green"></i>
     * </div>
     */

    private Div drawTimeDiv(LRPC request) {
        Div timeDiv = new Div("updates-row__time");
        DL dl = new DL();
        DT dt = new DT(dateFormat.format(request.getStartDate().getNonConvertedDate()) + " - " + dateFormat.format(request.getEndDate().getNonConvertedDate()));
        DD dd = new DD();
        dd.setText(wfmMessages.days(request.getDuration()));
        dl.add(dt);
        dl.add(dd);
        timeDiv.add(dl);
        MaterialIcon icon = new MaterialIcon();
        icon.addStyleName("sort-mark");
        icon.getElement().getStyle().setCursor(Style.Cursor.POINTER);
        if (request.getReasonColor() != null) {
            if (!request.getReasonColor().contains("#")) {
                request.setReasonColor("#" + request.getReasonColor());
            }
            icon.getElement().getStyle().setBackgroundColor(request.getReasonColor());
        }
        timeDiv.add(icon);
        return timeDiv;
    }

    /**
     * <div class="updates-row__img">
     * <figure class="img-group img-group--circle">
     * <div class="img-group__img">
     * <img src="http://placekitten.com/g/40/40" alt="image">
     * </div>
     * <figcaption>A. J.</figcaption>
     * </figure>
     * </div>
     */

    private Div drawImageDiv(LRPC request) {
        Div imgDiv = new Div("updates-row__img");
        FigureWidget figure = new FigureWidget();
        figure.addStyleName("img-group img-group--circle");
        imgDiv.add(figure);
        Div imgGrp = new Div("img-group__img");
        if (request.getEmployeePhotoUrl() != null) {
            MaterialImage img = new MaterialImage(request.getEmployeePhotoUrl());
            imgGrp.add(img);
            figure.add(imgGrp);
        } else {
            FigCaption figCaption = new FigCaption();
            figCaption.setText(Utils.getFirstTwoLetters(request.getEmployeeName()));

            figure.add(figCaption);
        }
        return imgDiv;
    }

    /**
     * <div class="updates-row__text">
     * <div class="updates-row__title">
     * <span>Absent Leave</span>
     * </div>
     * <div class="updates-row__info">
     * <span>Alex Farguson</span>
     * <span class="updates-row__action-name">
     * Unavailable from 1-May to 10-May
     * </span>
     * </div>
     * </div>
     */

    private Div drawTextDiv(LRPC request) {
        Div textDiv = new Div("updates-row__text");
        Div titleDiv = new Div("updates-row__title");
        Span titleSpan = new Span(request.getReason());
        titleDiv.add(titleSpan);
        textDiv.add(titleDiv);

        Div infoDiv = new Div("updates-row__info");
        Span infoSpan = new Span();
        infoSpan.getElement().setInnerHTML(wfmMessages.smbdyUnavavailableFromTo("<span>" + request.getEmployeeName() + "</span>",
                dateFormat.format(request.getStartDate().getNonConvertedDate()), dateFormat.format(request.getEndDate().getNonConvertedDate())));
        infoDiv.add(infoSpan);
        textDiv.add(infoDiv);

        return textDiv;
    }

}
