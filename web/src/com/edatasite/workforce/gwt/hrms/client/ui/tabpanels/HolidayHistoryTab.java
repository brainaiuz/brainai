package com.edatasite.workforce.gwt.hrms.client.ui.tabpanels;

import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityService;
import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityServiceAsync;
import com.edatasite.workforce.gwt.availability.client.rpc.HolidayHistoryList;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.*;

/**
 * User: ASUS
 * Date: 26.02.2016 11:44
 */
public class HolidayHistoryTab extends Composite {
    private Integer holidayId;
    private FlexTable historyTable;
    private ScrollPanel scrollPanel;
    private final AvailabilityServiceAsync holidayService = AvailabilityService.App.get();
    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    FlowPanel container = new FlowPanel();

    public HolidayHistoryTab(Integer objectID) {
        super();
        this.holidayId = objectID;
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_HOLIDAY_ADD, HolidayHistoryTab.this, (sender, args) -> refresh());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_HOLIDAY_EDIT, HolidayHistoryTab.this, (sender, args) -> refresh());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_HOLIDAY_DELETED, HolidayHistoryTab.this, (sender, args) -> refresh());
        scrollPanel = new ScrollPanel(container);
        historyTable = new FlexTable();
        historyTable.setWidth("100%");
        historyTable.setCellSpacing(5);
        historyTable.setCellPadding(5);
        container.setWidth("100%");
        scrollPanel.setWidth("100%");
        scrollPanel.setHeight("100%");
        scrollPanel.setHorizontalScrollPosition(0);
        initWidget(scrollPanel);
        setWidth("100%");
        refresh();
    }
    public void refresh() {
        holidayService.getHolidayHistories(holidayId, new AbstractAsyncCallback<HolidayHistoryList[]>() {
            @Override
            public void failure(Throwable caught) {
            }

            @Override
            public void success(HolidayHistoryList[] result) {
                historyTable.clear();
                getHistoryList(result);
            }
        });

    }

    private void getHistoryList(HolidayHistoryList[] historyLists) {
        if (historyLists != null && historyLists.length > 0) {
            for (int i = 0; i < historyLists.length; i++) {
                Image userImage = new Image();
                userImage.setWidth("29px");
                if (historyLists[i].getUpdaterImageURL() != null) {
                    userImage.setUrl(historyLists[i].getUpdaterImageURL());
                } else {
                    userImage = new Image();
                }

                Grid grid = new Grid(3, 1);
                grid.setWidget(0, 0, new HTML("<b>" + historyLists[i].getUpdater() + "</b>"));
                grid.setWidget(1, 0, new HTML("<span style='color:#13649b;font-size:12px;width:100%;'>" + historyLists[i].getMessage() + "</span>"));
                grid.setHTML(2, 0, "<span style='color:gray;hight:10px;'>" + DateUtils.formatInternal(historyLists[i].getCreationTime()) + "</span>");

                FlexTable flexTable = new FlexTable();
                flexTable.getElement().setAttribute("style", "width:100%; border-bottom:1px dotted #CCCCCC;height:3px;padding-bottom:3px;");
                flexTable.setHTML(0, 0, "<div class=not-image-smalls>" + userImage + "</div>");
                flexTable.getFlexCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
                flexTable.getFlexCellFormatter().setStyleName(0, 0, "paddingTop4");
                flexTable.setWidget(0, 1, grid);

                flexTable.getFlexCellFormatter().setWidth(0, 0, "7%");
                flexTable.getFlexCellFormatter().setWidth(0, 1, "93%");

                historyTable.setWidget(i, 0, flexTable);
                historyTable.getFlexCellFormatter().setVerticalAlignment(i, 0, HasVerticalAlignment.ALIGN_TOP);
//                container.clear();
            }
            container.add(historyTable);
        } else {
            getEmptyPanel(hrmsStrings.noHolidayHistory(), null, null);
        }
        if (historyLists.length > 4) {
            setHeight("330px");
        }
    }

    public void getEmptyPanel(String message, String textBeforeLink, final String link) {
        historyTable.clear();
        final VerticalPanel vpanel = new VerticalPanel();
        vpanel.setSize("100%", "100%");
        final HorizontalPanel centerPanel = new HorizontalPanel();
        final HorizontalPanel horz = new HorizontalPanel();
        final HTML noNotes = new HTML(message);
        horz.add(noNotes);

        if (textBeforeLink != null) {
            final SimpleLink mylink = new SimpleLink(textBeforeLink);
            horz.add(mylink);
            mylink.addClickHandler(event -> {
                if (link != null) {
                    SinksContainerFactory.entryPoint.onHistoryChanged(link);
                }
            });
        }

        vpanel.add(horz);
        vpanel.setCellHorizontalAlignment(horz, HasHorizontalAlignment.ALIGN_CENTER);
        vpanel.setCellVerticalAlignment(horz, HasVerticalAlignment.ALIGN_MIDDLE);
        DOM.setStyleAttribute(this.getElement(), "overflow", "hidden");
        container.clear();
        container.add(vpanel);
    }
}
