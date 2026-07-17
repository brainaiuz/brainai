package com.edatasite.workforce.gwt.dashboardwidget.client.view;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.MyUpdateItem;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.DashboardComponentItem;
import com.edatasite.workforce.gwt.core.client.rpc.historyNote.UpdateTypeStyle;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.DashboardBaseWidget;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.LoadingWidgets;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.dashboardwidget.client.rpc.DashboardWidgetService;
import com.google.gwt.core.client.GWT;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.ui.*;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Heading;
import gwt.material.design.client.ui.html.Icon;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: Abror Abdukadirov
 * Date: 02.05.2018 12:05
 */
public class DashboardMyUpdatesComponent extends DashboardBaseWidget {

    private MaterialPanel content;
    private MaterialPanel footerPanel;
    private Span loadingbar;
    private MaterialCollapsible collapsible;

    private MaterialCollapsibleItem todayItem;
    private MaterialCollapsibleItem yesterdayItem;

    private MaterialCollapsibleBody todayBody;
    private MaterialCollapsibleBody yesterdayBody;

    private MaterialCollapsibleItem dateItem;
    private MaterialCollapsibleBody dateBody;
    private List<MaterialCollapsibleItem> dateCollapsibleItems = new ArrayList<>();

    private DatePicker datePicker;

    private boolean hasMyUpdates = false;
    private boolean hasDateCollapse;
    private boolean hasDatePickerChange;
    private String sameDate;
    private Integer totalCount = 0;
    private Integer listCount = 0;
    private Integer start = 0;

    public DashboardMyUpdatesComponent(DashboardComponentItem gridItemConfig) {
        this.gridItemConfig = gridItemConfig;
    }

    public static Div drawRow(MyUpdateItem item, boolean fromContact) {
        Div row = new Div();
        row.add(drawTimeDiv(item, fromContact));
        row.add(drawTextDiv(item));

        if (item.getType() == null || item.getSubType() == null) {
            row.setStyleName("updates-row updates-cat--edited");
            return row;
        }
        row.setStyleName(UpdateTypeStyle.getStyleByUpdateSubType(item.getSubType()));
        return row;
    }

    private static Div drawTimeDiv(MyUpdateItem item, boolean fromContact) {
        Div timeDiv = new Div("updates-row__time");
        Span dateSpan = new Span();
        Span timeSpan = null;
        if (fromContact) {
            if (item.getStartDate() != null && item.getEndDate() != null) {
                if (DateUtil.isSameDay(item.getStartDate(), item.getEndDate())) {
                    String timeFormatted = DateUtils.getTimeFormatShort(item.getStartDate());
                    timeFormatted += " - " + DateUtils.getTimeFormatShort(item.getEndDate());

                    dateSpan.setText(DateUtils.getDateFormatShort(item.getStartDate()));
                    timeSpan = new Span(timeFormatted);
                } else {
                    dateSpan.setText(DateUtils.getDateFormatShort(item.getStartDate()));
                    timeSpan = new Span(DateUtils.getDateFormatShort(item.getEndDate()));
                }
            }
        } else {
            if (item.getEventDate() != null) {
                dateSpan.setText(DateUtils.getTimeFormatShort(item.getEventDate()));
            }
        }
        timeDiv.add(dateSpan);
        if (timeSpan != null) {
            timeDiv.add(timeSpan);
        }
        Span pointSpan = new Span();
        pointSpan.setStyleName("updates-row__time-point");
        timeDiv.add(pointSpan);
        return timeDiv;
    }

    private static Div drawTextDiv(MyUpdateItem item) {
        Div textDiv = new Div("updates-row__text");
        Div titleDiv = new Div("updates-row__title");
        Div infoDiv = new Div("updates-row__info");

        titleDiv.add(getIconStyle(item));

        Span titleSpan = new Span(item.getTitle() != null ? item.getTitle() : "N/A");
        titleDiv.add(titleSpan);
        textDiv.add(titleDiv);

        Span infoSpan = new Span();
        infoSpan.setText(item.getMessage() != null ? item.getMessage() : "N/A");
        infoDiv.add(infoSpan);
        textDiv.add(infoDiv);

        return textDiv;
    }

    public static Icon getIconStyle(MyUpdateItem item) {
        Icon icon = new Icon();
        if (item.getSubType() == null) {
            icon.setStyleName("ficon--edited");
            return icon;
        }
        switch (item.getSubType()) {
            case MyUpdateItem.ADD:
                icon.setStyleName("ficon--plus");
                break;
            case MyUpdateItem.DELETE:
                icon.setStyleName("ficon--trash");
                break;
            case MyUpdateItem.EDIT:
                icon.setStyleName("ficon--edited");
                break;
            case MyUpdateItem.FILE_UPLOAD:
                icon.setStyleName("ficon--uploaded");
                break;
            case MyUpdateItem.STATUS_PAID:
                icon.setStyleName("ficon--paid");
                break;
            case MyUpdateItem.STATUS_APPROVED:
            case MyUpdateItem.STATUS_COMPELETED:
            case MyUpdateItem.STATUS_RECEIVED:
                icon.setStyleName("ficon--completed");
                break;
            case MyUpdateItem.STATUS_WAITING:
                icon.setStyleName("ficon--edited");
                break;
            case MyUpdateItem.STATUS_REJECT:
            case MyUpdateItem.STATUS_TERMINATED:
                icon.setStyleName("ficon--rejected");
                break;
            case MyUpdateItem.ASSIGN:
                icon.setStyleName("ficon--assigned");
                break;
            case MyUpdateItem.IMPORTED:
                icon.setStyleName("ficon--import");
                break;
            case MyUpdateItem.CONVERTED:
                icon.setStyleName("ficon--converted");
                break;
            case MyUpdateItem.STATUS_REFUNDED:
                icon.setStyleName("ficon--arrow-left");
                break;
            case MyUpdateItem.STATUS_SUBMITED:
                icon.setStyleName("ficon--submited");
                break;
            case MyUpdateItem.STATUS_SENT:
                icon.setStyleName("ficon--sent");
                break;
            case MyUpdateItem.STATUS_CANCELLED:
            case MyUpdateItem.STATUS_CLOSED:
                icon.setStyleName("ficon--cancel");
                break;
            default:
                icon.setStyleName("ficon--edited");
        }
        return icon;
    }

    @Override
    protected void initInternal() {
//        if (gridItemConfig != null && gridItemConfig.getName() != null) {
//            setTitle(gridItemConfig.getName());
//        } else {
//        }
        setTitle(wfmStrings.myUpdates());
        mainPanel.addStyleName("widget--updates");
        headerRow.remove(actionPanel);

        title.removeFromParent();
        filterPanel.removeFromParent();

        MaterialCollapsible tabPanel = new MaterialCollapsible();
        MaterialCollapsibleItem myUpdateTab = new MaterialCollapsibleItem();
        MaterialCollapsibleItem perUpdatesTab = new MaterialCollapsibleItem();

        Heading myUpdateH3 = new Heading(HeadingSize.H3);
        MaterialLink myUpdateLink = new MaterialLink(wfmStrings.myUpdates());
        myUpdateLink.addClickHandler(event -> {
            if (!hasMyUpdates) {
                mainPanel.addStyleName("widget--updates-links");
                tabPanel.setActive(0);
                hasMyUpdates = true;
                loadComponentData();
            }
        });
        myUpdateH3.add(myUpdateLink);
        myUpdateTab.add(myUpdateH3);

        tabPanel.add(myUpdateTab);
        boolean hasEssUser = Utils.hasEitherRole(ESS_USER_CODE);
        boolean hasSalesPerson = Utils.hasEitherRole(SALESPERSON_CODE) && !Utils.hasEitherRole(ADMIN_CODE);
        if (!hasEssUser && !hasSalesPerson) {
            Heading perUpdateH3 = new Heading(HeadingSize.H3);
            MaterialLink perUpdateLink = new MaterialLink(wfmStrings.peerUpdates());
            perUpdateLink.addClickHandler(event -> {
                if (hasMyUpdates) {
                    mainPanel.removeStyleName("widget--updates-links");
                    tabPanel.setActive(2);
                    hasMyUpdates = false;
                    loadComponentData();
                }
            });
            perUpdateH3.add(perUpdateLink);
            perUpdatesTab.add(perUpdateH3);
            tabPanel.add(perUpdatesTab);
        }
        tabPanel.setStyleName("widget-heading__tabs");
        headerRow.add(tabPanel);
        headerRow.add(actionPanel);
        if (hasEssUser || hasSalesPerson) {
            tabPanel.setActive(0);
            hasMyUpdates = true;
        } else {
            tabPanel.setActive(2);
            hasMyUpdates = false;
        }
        Div calendarActionDiv = new Div("widget-heading__action");
        Div calendarBoxDiv = new Div("calendar-box");
        Div calendarBoxInputDiv = new Div("calendar-box__input");
        datePicker = new DatePicker();
        calendarBoxInputDiv.add(datePicker);
        datePicker.addChangeHandler(changeEvent -> {
            hasDatePickerChange = true;
            loadComponentData();
            hasDatePickerChange = false;
        });
        calendarBoxDiv.add(calendarBoxInputDiv);

        Div calendarBoxIconDiv = new Div("calendar-box__icon");
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
        moreButton.addClickHandler(clickEvent -> {
            this.start = start + 20;
            getUpdates();
        });
        footerPanel.add(loadingbar);
        footerPanel.add(moreButton);

        collapsible = new MaterialCollapsible();
        collapsible.addStyleName("collapsible--arrows-left updates-list");
        collapsible.setAccordion(false);

        todayItem = new MaterialCollapsibleItem();
        MaterialCollapsibleHeader todayHeader = new MaterialCollapsibleHeader();
        todayBody = new MaterialCollapsibleBody();

        Heading todayH3 = new Heading(HeadingSize.H3);
        Span todaySpan = new Span(wfmStrings.today());
        todayH3.add(todaySpan);
        todayHeader.add(todayH3);

        todayItem.add(todayHeader);
        todayItem.add(todayBody);

        yesterdayItem = new MaterialCollapsibleItem();
        MaterialCollapsibleHeader yesterdayHeader = new MaterialCollapsibleHeader();
        yesterdayBody = new MaterialCollapsibleBody();

        Heading yesterdayH3 = new Heading(HeadingSize.H3);
        Span yesterdaySpan = new Span(wfmStrings.yesterday());
        yesterdayH3.add(yesterdaySpan);
        yesterdayHeader.add(yesterdayH3);

        yesterdayItem.add(yesterdayHeader);
        yesterdayItem.add(yesterdayBody);

        dateBody = new MaterialCollapsibleBody();
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CALENDAR_EVENT_ADD, DashboardMyUpdatesComponent.this, (sender, args) -> loadComponentData());
    }

    @Override
    protected void getData() {
        clearPanelAndFilters(false);

        getUpdates();
    }

    @Override
    protected void getSampleData(boolean nodata) {
        clearPanelAndFilters(nodata);

        ArrayList<MyUpdateItem> items = new ArrayList<>();
        MyUpdateItem item = new MyUpdateItem();
        item.setType(MyUpdateItem.ADD);
        item.setTitle("Product Added");
        item.setMessage("You have added Sony earphones");
        item.setSubType(MyUpdateItem.ADD);
        item.setEventDate(new Date());
        items.add(item);

        item = new MyUpdateItem();
        item.setType(MyUpdateItem.ADD);
        item.setTitle("Purchase Order Added");
        item.setMessage("You have added Purchase Order PO0002");
        item.setSubType(MyUpdateItem.ADD);
        item.setEventDate(new Date());
        items.add(item);

        item = new MyUpdateItem();
        item.setType(MyUpdateItem.STATUS_RECEIVED);
        item.setTitle("Purchase Order Received");
        item.setMessage("You have received for Purchase Order PO0002");
        item.setSubType(MyUpdateItem.STATUS_RECEIVED);
        item.setEventDate(new Date(System.currentTimeMillis() - (24 * 60 * 60 * 1000)));
        items.add(item);

        item = new MyUpdateItem();
        item.setType(MyUpdateItem.ASSIGN);
        item.setTitle("Task Assigned");
        item.setMessage("You have been assigned to New Project task by Beko(admin)");
        item.setSubType(MyUpdateItem.ASSIGN);
        item.setEventDate(new Date(System.currentTimeMillis() - (24 * 60 * 60 * 1000)));
        items.add(item);

        item = new MyUpdateItem();
        item.setType(MyUpdateItem.EDIT);
        item.setTitle("Task Edited");
        item.setMessage("You have edited \"New Project\" task");
        item.setSubType(MyUpdateItem.EDIT);
        item.setEventDate(new Date(System.currentTimeMillis() - (24 * 60 * 60 * 1000)));
        items.add(item);

        item = new MyUpdateItem();
        item.setType(MyUpdateItem.DELETE);
        item.setTitle("Task Deleted");
        item.setMessage("You have deleted New Project task");
        item.setSubType(MyUpdateItem.DELETE);
        item.setEventDate(new Date(System.currentTimeMillis() - ((24 * 60 * 60 * 1000) * 2)));
        items.add(item);

        item = new MyUpdateItem();
        item.setType(MyUpdateItem.FILE_UPLOAD);
        item.setTitle("Project File Uploaded");
        item.setMessage("You have added task.png document to Moon Architect project");
        item.setSubType(MyUpdateItem.FILE_UPLOAD);
        item.setEventDate(new Date(System.currentTimeMillis() - ((24 * 60 * 60 * 1000) * 2)));
        items.add(item);

        item = new MyUpdateItem();
        item.setType(MyUpdateItem.STATUS_REJECT);
        item.setTitle("Timesheet Rejected");
        item.setMessage("You have rejected Sara Mandela's timesheet entries");
        item.setSubType(MyUpdateItem.STATUS_REJECT);
        item.setEventDate(new Date(System.currentTimeMillis() - ((24 * 60 * 60 * 1000) * 2)));
        items.add(item);

        item = new MyUpdateItem();
        item.setType(MyUpdateItem.STATUS_SENT);
        item.setTitle("Invoice Sent");
        item.setMessage("You have sent Sales Invoice INV003 to client James Carter");
        item.setSubType(MyUpdateItem.STATUS_SENT);
        item.setEventDate(new Date(System.currentTimeMillis() - ((24 * 60 * 60 * 1000) * 2)));
        items.add(item);

        item = new MyUpdateItem();
        item.setType(MyUpdateItem.STATUS_PAID);
        item.setTitle("Invoice Paid");
        item.setMessage("You have received Sales Invoice payment INV0003");
        item.setSubType(MyUpdateItem.STATUS_PAID);
        item.setEventDate(new Date(System.currentTimeMillis() - ((24 * 60 * 60 * 1000) * 2)));
        items.add(item);

        setData(items);
    }

    private void getUpdates() {
        loadingbar.setVisible(true);
        if (totalCount < (listCount + start)) {
            LoadingWidgets.get(getCode()).show();
        }
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setStart(start);
        fp.setLimit(20);
        AbstractAsyncCallback callback = new AbstractAsyncCallback<ListResult<MyUpdateItem>>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingWidgets.get(getCode()).hide();
            }

            @Override
            public void onSuccess(ListResult<MyUpdateItem> result) {
                if (!result.getList().isEmpty()) {
                    LoadingWidgets.get(getCode()).hide();
                    totalCount = result.getTotal();
                    listCount = result.getList().size();
                    setData(result.getList());
                } else {
                    noData();
                }
            }
        };
        DateNonConvertable dateNonConvertable = null;
        if (datePicker.getDate() != null) {
            dateNonConvertable = new DateNonConvertable(datePicker.getDate());
        }
        if (hasMyUpdates) {
            DashboardWidgetService.App.get().getMyLatestUpdates(fp, dateNonConvertable, callback);
        } else {
            DashboardWidgetService.App.get().getMyPeersUpdates(fp, dateNonConvertable, callback);
        }
    }

    @Override
    public String getCode() {
        return DASHBOARD_WIDGET_CODE.MY_UPDATES;
    }

    @Override
    protected String getEmptyText() {
        return null;
    }

    private void setData(ArrayList<MyUpdateItem> result) {
        if (result != null && result.size() > 0) {
            for (MyUpdateItem item : result) {
                Div row = drawRow(item, false);
                if (hasMyUpdates && item.getLink() != null && !"".equals(item.getLink())) {
                    row.addClickHandler(event -> {
                        Utils.redirect(GWT.getHostPageBaseURL() + item.getSectionURL() + "?link=" + item.getLink());
                    });
                }
                if (DateUtil.isToday(item.getEventDate())) {
                    todayBody.add(row);
                } else if (DateUtil.isYesterday(item.getEventDate())) {
                    yesterdayBody.add(row);
                } else {
                    String formattedDate = DateUtils.getDateFormatShort(item.getEventDate());
                    if (sameDate == null || !formattedDate.equals(sameDate)) {
                        sameDate = formattedDate;
                        hasDateCollapse = true;

                        drawInnerCollapsible(formattedDate);

                        dateBody.add(row);
                        dateCollapsibleItems.add(dateItem);
                    } else {
                        dateBody.add(row);
                    }
                }
            }
            if (todayBody.getWidgetCount() > 0 && collapsible.getWidgetIndex(todayItem) < 0) {
                collapsible.add(todayItem);
            }
            if (yesterdayBody.getWidgetCount() > 0 && collapsible.getWidgetIndex(yesterdayItem) < 0) {
                collapsible.add(yesterdayItem);
            }
            if (dateCollapsibleItems.size() > 0 && hasDateCollapse) {
                for (MaterialCollapsibleItem dateCollapseItem : dateCollapsibleItems) {
                    if (dateCollapseItem == null || dateCollapseItem.getBody() == null) {
                        continue;
                    }
                    if (dateCollapseItem.getBody().getWidgetCount() > 0 && collapsible.getWidgetIndex(dateCollapseItem) < 0) {
                        collapsible.add(dateCollapseItem);
                    }
                    dateCollapseItem.addStyleName("active");
                    dateCollapseItem.getHeader().addStyleName("active");
                    dateCollapseItem.getBody().setDisplay(Display.BLOCK);
                }
            }
            hasDateCollapse = false;

            content.add(collapsible);
            contentPanel.add(content);

            todayItem.addStyleName("active");
            todayItem.getHeader().addStyleName("active");
            todayItem.getBody().setDisplay(Display.BLOCK);

            yesterdayItem.addStyleName("active");
            yesterdayItem.getHeader().addStyleName("active");
            yesterdayItem.getBody().setDisplay(Display.BLOCK);

            if (dateItem != null) {
                dateItem.addStyleName("active");
                dateItem.getHeader().addStyleName("active");
                dateItem.getBody().setDisplay(Display.BLOCK);
            }

        }
        loadingbar.setVisible(false);
        if (totalCount > (listCount + start)) {
            mainPanel.add(footerPanel);
        } else {
            mainPanel.remove(footerPanel);
        }
    }

    private void drawInnerCollapsible(String formattedDate) {
        dateItem = new MaterialCollapsibleItem();
        MaterialCollapsibleHeader dateHeader = new MaterialCollapsibleHeader();
        dateBody = new MaterialCollapsibleBody();

        Heading dateH3 = new Heading(HeadingSize.H3);
        Span dateSpan = new Span(formattedDate);
        dateH3.add(dateSpan);
        dateHeader.add(dateH3);

        dateItem.add(dateHeader);
        dateItem.add(dateBody);
    }

    private void clearPanelAndFilters(boolean hasNoData) {
        content.clear();
        if (!hasNoData) {
            contentPanel.clear();
        }
        collapsible.clear();
        todayBody.clear();
        yesterdayBody.clear();
        dateBody.clear();
        dateCollapsibleItems.clear();

        if (!hasDatePickerChange) {
            datePicker.setDate(new Date());
        }
        totalCount = 0;
        listCount = 0;
        start = 0;
        sameDate = null;
    }
}
