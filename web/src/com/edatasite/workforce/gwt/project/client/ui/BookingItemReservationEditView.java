package com.edatasite.workforce.gwt.project.client.ui;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.BookingReservationItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinks;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinksInterface;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.FooterInformer;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectService;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectServiceAsync;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ProvidesKey;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created with IntelliJ IDEA.
 * User: WFT01
 * Date: 27.08.12
 * Time: 17:59
 * To change this template use File | Settings | File Templates.
 */
public class BookingItemReservationEditView extends CustomForm2 implements HasLinksInterface, Constants, Colapse {
    private static final ProjectServiceAsync projectService = ProjectService.App.get();

    private final Integer objectID;
    private WfmButton2 saveButton;
    HTML reservedByLabel;
    private DataListBox reservedBy;
    private DataListBox bookingItem;
    private DataListBox category;
    private DateTimePicker dateAndTime;
    private boolean saveAndClose = false;
    private boolean isAllDay;
    private Date startDate;
    private Date endDate;
    private HTML getReservedByLabel;
    private HashMap<String, Widget> widgetsMap;
    private HTMLPanel htmlPanel;
    DateTimeFormat timeFormatHour = DateUtils.getTimeFormatInternal()/*DateTimeFormat.getFormat("HH:mm")*/;
    private VerticalPanel linksPanel;
    private SimpleLink addLink;
    private RelationItem[] predefinedLinks;
    private FooterInformer link;
    private Integer calendarEventID;
    private BookingReservationItem bookingReservationItems = new BookingReservationItem();
    private BookingReservationItem item;
    private KpiDataGrid<BookingReservationItem> dataGrid;
    private final AtomicBoolean firstClick = new AtomicBoolean(true);

    public BookingItemReservationEditView(Integer objectID) {
        super("edit", wfmStrings.editReservation());
        this.objectID = objectID;
    }

    public static final ProvidesKey<BookingReservationItem> KEY_PROVIDER = item -> item != null ? item.getObjectID() : null;

    protected Widget onInitialize() {
        super.onInitialize();
        return null;
    }


    @Override
    protected void registerFields() {
        dataGrid = new KpiDataGrid<>(KEY_PROVIDER);
        dataGrid.setSize("540px", "342px");
        initTableColumn();

        reservedBy = new DataListBox();
        reservedBy.addStyleName(DEFAULT_WIDTH);

        category = new DataListBox();
        category.addStyleName(DEFAULT_WIDTH);

        bookingItem = new DataListBox();
        bookingItem.addStyleName(DEFAULT_WIDTH);

        dateAndTime = new DateTimePicker();


        category.addValueChangeHandler(event -> getSelectCategoryItems(category.getSelectedId()));
        bookingItem.addValueChangeHandler(event -> getReservationHistoryList(bookingItem.getSelectedId()));


        addTitleField(INFORMATION, wfmStrings.information());
        addField(RESERVED_BY, reservedBy, wfmStrings.reservedBy());
        addField(CATEGORY, category, wfmStrings.category());
        addField(ITEMS, bookingItem, wfmStrings.item());
        addField(START_DATE, Utils.getInHorizontalPanel(0, 0, true, dateAndTime.startDate, dateAndTime.startTime), wfmStrings.startDate());
        addField(END_DATE, Utils.getInHorizontalPanel(0, 0, true, dateAndTime.dueDate, dateAndTime.endTime), wfmStrings.endDate());
        addField(ALL_DATE, dateAndTime.allDay, null);

        addField(RESERVATION_HISTORY, dataGrid, wfmStrings.reservationHistory());
        show();

    }

    @Override
    protected void initPredefinedValues() {

    }

    @Override
    protected void getDataToFillFields() {
        ProjectService.App.get().getBookingItemsReservationData(objectID, new AbstractAsyncCallback<BookingReservationItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                GWT.log(throwable.getMessage());
            }

            @Override
            public void success(BookingReservationItem bookingItems) {
                item = bookingItems;
                fillFieldWithValue(bookingItems);
                LoadingPanel.loading(false);
            }
        });
    }

    private void initTableColumn() {
        //Reserved By
        Column<BookingReservationItem, String> reservedBy = new Column<BookingReservationItem, String>(new TextCell()) {
            @Override
            public String getValue(BookingReservationItem object) {
                return object.getSelectedReservedById() != null ? object.getSelectedReservedById().getName() : wfmStrings.notAvailable();
            }
        };
        dataGrid.addColumn(reservedBy, wfmStrings.reservedBy());
        dataGrid.setColumnWidth(reservedBy, 25, com.google.gwt.dom.client.Style.Unit.PCT);

        //from
        Column<BookingReservationItem, String> fromdate = new Column<BookingReservationItem, String>(new TextCell()) {
            @Override
            public String getValue(BookingReservationItem object) {
                return object.getFromDate() != null ? DateUtils.formatInternalShort1(object.getFromDate()) : wfmStrings.notAvailable();
            }
        };
        dataGrid.addColumn(fromdate, wfmStrings.from());
        dataGrid.setColumnWidth(fromdate, 20, com.google.gwt.dom.client.Style.Unit.PCT);

        //to
        Column<BookingReservationItem, String> toDate = new Column<BookingReservationItem, String>(new TextCell()) {
            @Override
            public String getValue(BookingReservationItem object) {
                return object.getToDate() != null ? DateUtils.formatInternalShort1(object.getToDate()) : wfmStrings.notAvailable();
            }
        };
        dataGrid.addColumn(toDate, wfmStrings.to());
        dataGrid.setColumnWidth(toDate, 20, com.google.gwt.dom.client.Style.Unit.PCT);

    }

    private void fillFormWithData(List<BookingReservationItem> result) {
        ArrayList<BookingReservationItem> test = new ArrayList<>();
        dataGrid.supplyProvider(result);
        dataGrid.refresh();
    }


    private void drawReservationHistoryList(Integer itemId) {
        if (itemId != null) {
            getReservationHistoryList(itemId);
        }

    }

    private void getReservationHistoryList(Integer bookingItemId) {
        projectService.getBookingItemsReservationHistoryList(bookingItemId, new AbstractAsyncCallback<ArrayList<BookingReservationItem>>() {
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            public void success(ArrayList<BookingReservationItem> result) {
                fillFormWithData(result);
            }
        });
    }

    @Override
    protected void addButtons() {

        link = new FooterInformer(SvgEnum.link, wfmStrings.links(), null);
        footer.addToLeftSide(link);

        link.addClickHandler(event -> {
            if (firstClick.get()) {
                getLinkingUtil().getAddLinkSideNavBox();
                ArrayList<RelationItem> relationItems = new ArrayList<>();
                getLinkingUtil().getAddLinkSideNavBox().setSelectedRelations(item.getRelations(), false);
                firstClick.set(false);
            } else {
                getLinkingUtil().getAddLinkSideNavBox().show();
            }
        });


        saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> {
            saveAndClose = true;
            bookingReservationItems = getBookingItemObject();
            save(bookingReservationItems);
        });
        addButton(saveButton);
    }

    private BookingReservationItem getBookingItemObject() {
        bookingReservationItems.setObjectID(objectID);
        bookingReservationItems.setBookingItems(bookingItem.getItems());
        bookingReservationItems.setSelectedBookingItemId(bookingItem.getSelectedItem());

        bookingReservationItems.setCategories(category.getItems());
        bookingReservationItems.setSelectedCategoryId(category.getSelectedItem());

        bookingReservationItems.setReservedByIds(reservedBy.getItems());
        bookingReservationItems.setSelectedReservedById(reservedBy.getSelectedItem());

        bookingReservationItems.setFromDate(dateAndTime.getStartDate());
        bookingReservationItems.setToDate(dateAndTime.getDueDate());
        bookingReservationItems.setCalendarEventID(calendarEventID);

        if (firstClick.get()) {
            bookingReservationItems.setRelations(item != null ? item.getRelations() : null);
        } else {
            bookingReservationItems.setRelations(getLinkingUtil().getAddLinkSideNavBox().getSelectedRelations());
        }
        return bookingReservationItems;
    }


    protected void save(final BookingReservationItem item) {
        projectService.validateBookingItemReservation(item, new AbstractAsyncCallback<Integer>() {
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            public void success(Integer result) {
                if (result != null && result == 0) {
                    projectService.saveBookingItemReservation(item, new AbstractAsyncCallback<Integer>() {
                        public void failure(Throwable caught) {
                            LoadingPanel.loading(false);
                            Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                        }

                        public void success(Integer result) {
                            LoadingPanel.loading(false);
                            Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.reservations()), Info.Type.INFO);
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_BOOKING_ITEMS_RESERVATION_SAVED, result, null);
                            closeTab();
                        }
                    });
                } else {
                    Info.show(wfmStrings.itemsIsNotAvailableAtThisTime(), Info.Type.WARNING);
                }
            }
        });
    }


    private void fillFieldWithValue(BookingReservationItem reservationItem) {

        bookingItem.setItems(reservationItem.getBookingItems());
        bookingItem.setSelected(reservationItem.getSelectedBookingItemId());
        bookingItem.ensureDebugId("booking_item");

        category.setItems(reservationItem.getCategories());
        category.setSelected(reservationItem.getSelectedCategoryId());
        category.ensureDebugId("booking_category");

        reservedBy.setItems(reservationItem.getReservedByIds());
        reservedBy.setSelected(reservationItem.getSelectedReservedById());
        reservedBy.ensureDebugId("booking_reserved");

        dateAndTime.setStartDate(reservationItem.getFromDate() != null ? reservationItem.getFromDate() : new Date());
        dateAndTime.setDueDate(reservationItem.getToDate() != null ? reservationItem.getToDate() : new Date());
        dateAndTime.allDay.setValue(isAllDay);

        dateAndTime.startDate.ensureDebugId("reservation_start_date");
        dateAndTime.dueDate.ensureDebugId("reservation_end_date");
        dateAndTime.startTime.ensureDebugId("reservation_start_time");
        dateAndTime.endTime.ensureDebugId("reservation_end_time");

        link.setBadgeCount(reservationItem.getRelations().size());
        if (!isAllDay) {
            dateAndTime.setStartTime(new StartEndTime(timeFormatHour.format(reservationItem.getFromDate() != null ? reservationItem.getFromDate() : DateUtil.getDateWithZeroMinutes(DateUtil.addHours(new Date(), 0)))).time);
            dateAndTime.setEndTime(new StartEndTime(timeFormatHour.format(reservationItem.getToDate() != null ? reservationItem.getToDate() : DateUtil.getDateWithZeroMinutes(DateUtil.addHours(new Date(), 1)))).time);
            if (dateAndTime.getEndTime() != null && dateAndTime.getEndTime().time.equals("23:59")) {
                dateAndTime.setEndTime("23:45");
            }
        } else {
            dateAndTime.setStartTime("00:00");
            dateAndTime.setEndTime("23:45");
        }


        Integer itemId = reservationItem.getSelectedBookingItemId() != null ? reservationItem.getSelectedBookingItemId().getId() : bookingItem.getSelectedId();
        drawReservationHistoryList(reservationItem.getSelectedBookingItemId() != null ? reservationItem.getSelectedBookingItemId().getId() : objectID);

    }


    private void getSelectCategoryItems(Integer categoryId) {
        projectService.getBookingItemsByCategoryId(categoryId, new AsyncCallback<SelectItem[]>() {

            @Override
            public void onFailure(Throwable caught) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onSuccess(SelectItem[] result) {
                bookingItem.setItems(result);
            }
        });
    }


    @Override
    protected String getFormID() {
        return LayoutRPC.BOOKING_ITEM_RESERVATION_VIEW;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.EDIT;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    public String getIconStyle() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    HasLinks linkingUtil;

    @Override
    public HasLinks getLinkingUtil() {
        if (linkingUtil == null) {
            linkingUtil = new HasLinks(BookingItemReservationEditView.this) {
                @Override
                protected boolean isActionEditing() {
                    return true;
                }

                @Override
                protected Integer getRelationID() {
                    return objectID;
                }

                @Override
                protected String getRelationType() {
                    return RelationItem.TYPE_BOOKING;
                }

                @Override
                protected String getRelationName() {
                    return item != null ? item.getBookingItemName() : null;
                }

            };
        }
        return linkingUtil;
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {

            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }

}
