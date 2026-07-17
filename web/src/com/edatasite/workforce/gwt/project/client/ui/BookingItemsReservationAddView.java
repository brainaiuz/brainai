package com.edatasite.workforce.gwt.project.client.ui;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.BookingReservationItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DateTimePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.KpiDataGrid;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.StartEndTime;
import com.edatasite.workforce.gwt.core.client.ui.cell.SimpleLinkCell;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
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
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ProvidesKey;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created with IntelliJ IDEA.
 * User: developer
 * Date: 5/19/12
 * Time: 12:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class BookingItemsReservationAddView extends CustomForm2 implements HasLinksInterface, Constants, Colapse {

    private static final ProjectServiceAsync projectService = ProjectService.App.get();
    private DataListBox reservedBy;
    private DataListBox bookingItem;
    private DataListBox category;
    private DateTimePicker dateAndTime;
    private WfmButton2 saveButton;
    private HasLinks linkingUtil;
    private SimpleLink addLink;
    private BookingReservationItem items;
    private final AtomicBoolean firstClick = new AtomicBoolean(true);


    private Integer objectID;
    private boolean isValid = true;
    private BookingReservationItem bookingReservationItems = new BookingReservationItem();
    final DateTimeFormat timeFormat = DateTimeFormat.getShortTimeFormat();
    final DateTimeFormat dateFormat = DateUtils.getDateFormatShort();
    private Date startDate;
    private Date endDate;
    private boolean isAllDay;
    DateTimeFormat timeFormatHour = DateUtils.getTimeFormatInternal()/*DateTimeFormat.getFormat("HH:mm")*/;
    private KpiDataGrid<BookingReservationItem> dataGrid;

    public BookingItemsReservationAddView(String[] params) {
        super("bookingitemsreservationadd", wfmStrings.addReservation());

        if (params.length == 2) {
            this.objectID = params[1] != null && !"null".equals(params[1]) ? Integer.valueOf(params[1]) : null;
        }
        if (params.length == 6) {
            this.startDate = params[3] != null && !"null".equals(params[3]) ? DateUtils.getDateAndTimeFormatFull().parse(params[3]) : null;
            this.endDate = params[4] != null && !"null".equals(params[4]) ? DateUtils.getDateAndTimeFormatFull().parse(params[4]) : null;
            this.isAllDay = params[5].equals("true");
        }
    }

    public BookingItemsReservationAddView(Integer eventID) {
        super("bookingitemsreservationadd", wfmStrings.addReservation());
    }

    public static final ProvidesKey<BookingReservationItem> KEY_PROVIDER = item -> item != null ? item.getObjectID() : null;

    protected Widget onInitialize() {
        super.onInitialize();
        return null;
    }


    @Override
    protected void registerFields() {
        dataGrid = new KpiDataGrid<>(KEY_PROVIDER);
        initTableColumn();

        reservedBy = new DataListBox();
        category = new DataListBox();
        bookingItem = new DataListBox();
        reservedBy.addStyleName(DEFAULT_WIDTH);
        category.addStyleName(DEFAULT_WIDTH);
        bookingItem.addStyleName(DEFAULT_WIDTH);

        dateAndTime = new DateTimePicker();
        dateAndTime.setAllDay(isAllDay);
        dateAndTime.setStartDate(this.startDate != null ? this.startDate : new Date());
        dateAndTime.setDueDate(this.endDate != null ? this.endDate : new Date());
        dateAndTime.allDay.setValue(isAllDay);
        if (!isAllDay) {
            dateAndTime.setStartTime(new StartEndTime(timeFormatHour.format(this.startDate != null ? this.startDate : DateUtil.getDateWithZeroMinutes(DateUtil.addHours(new Date(), 0)))).time);
            dateAndTime.setEndTime(new StartEndTime(timeFormatHour.format(this.endDate != null ? this.endDate : DateUtil.getDateWithZeroMinutes(DateUtil.addHours(new Date(), 1)))).time);
            if (dateAndTime.getEndTime() != null && dateAndTime.getEndTime().time.equals("23:59")) {
                dateAndTime.setEndTime("23:45");
            }
        } else {
            dateAndTime.setStartTime("00:00");
            dateAndTime.setEndTime("23:45");
        }


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
    protected void getDataToFillFields() {
        projectService.getBookingItemsReservationData(objectID, new AbstractAsyncCallback<BookingReservationItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                GWT.log(throwable.getMessage());
            }

            @Override
            public void success(BookingReservationItem reservationItem) {
                LoadingPanel.loading(false);
                items = reservationItem;

                getReservationHistoryList(reservationItem.getObjectID() != null ? reservationItem.getObjectID() : objectID);

                category.setItems(reservationItem.getCategories());
                category.setSelected(reservationItem.getSelectedCategoryId());

                bookingItem.setItems(reservationItem.getBookingItems());
                bookingItem.setSelected(objectID);


                reservedBy.setItems(reservationItem.getReservedByIds());
                reservedBy.setSelected(reservationItem.getSelectedReservedById());


            }
        });
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.BOOKING_ITEM_RESERVATION_VIEW;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }


    @Override
    protected void initPredefinedValues() {

    }


    private void getSelectCategoryItems(Integer categoryId) {
        ProjectService.App.get().getBookingItemsByCategoryId(categoryId, new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void success(SelectItem[] result) {
                bookingItem.setItems(result);
            }
        });
    }

    @Override
    protected void addButtons() {

        FooterInformer link = new FooterInformer(SvgEnum.link, wfmStrings.links(), null);
        footer.addToLeftSide(link);

        link.addClickHandler(event -> {
            if (firstClick.get()) {
                getLinkingUtil().getAddLinkSideNavBox();
                ArrayList<RelationItem> relationItems = new ArrayList<>();
                getLinkingUtil().getAddLinkSideNavBox().setSelectedRelations(relationItems, true);
                firstClick.set(false);
            } else {
                getLinkingUtil().getAddLinkSideNavBox().show();
            }
        });

        saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> {
            if (validate()) {
                bookingReservationItems = getBookingItemObject();
                save(bookingReservationItems);
            }
        });
        addButton(saveButton);
    }

    private boolean validate() {
        int errors = 0;

        if (!Validation.validateListBoxRequired(reservedBy, new HTML(), wfmStrings.pleaseSelect())) {
            errors++;
        }

        if (!Validation.validateListBoxRequired(category, new HTML(), wfmStrings.pleaseSelect())) {
            errors++;
        }

        if (!Validation.validateListBoxRequired(bookingItem, new HTML(), wfmStrings.pleaseSelect())) {
            errors++;
        }


        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
        }

        return errors == 0;
    }

    private BookingReservationItem getBookingItemObject() {
        bookingReservationItems.setObjectID(objectID);
        bookingReservationItems.setSelectedBookingItemId(bookingItem.getSelectedItem());
        bookingReservationItems.setSelectedCategoryId(category.getSelectedItem());
        bookingReservationItems.setSelectedReservedById(reservedBy.getSelectedItem());
        bookingReservationItems.setFromDate(dateAndTime.getStartDate());
        if (firstClick.get()) {
            bookingReservationItems.setRelations(bookingReservationItems != null ? bookingReservationItems.getRelations() : null);
        } else {
            bookingReservationItems.setRelations(getLinkingUtil().getAddLinkSideNavBox().getSelectedRelations());
        }
        if (dateAndTime.isAllDay()) {
            Date dueDate = dateAndTime.getDueDate();
            if (dueDate != null) {
                dueDate.setHours(23);
                dueDate.setMinutes(59);
                dueDate.setSeconds(59);
            }
            bookingReservationItems.setToDate(dueDate);
        } else {
            bookingReservationItems.setToDate(dateAndTime.getDueDate());
        }

        return bookingReservationItems;
    }


    private void save(final BookingReservationItem item) {
        projectService.validateBookingItemReservation(item, new AbstractAsyncCallback<Integer>() {
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
                isValid = false;
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


    private void getReservationHistoryList(Integer bookingItemId) {
        projectService.getBookingItemsReservationHistoryList(bookingItemId, new AbstractAsyncCallback<ArrayList<BookingReservationItem>>() {
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            public void success(ArrayList<BookingReservationItem> result) {
                dataGrid.supplyProvider(result);
                dataGrid.refresh();
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

        //Edit
        final Column<BookingReservationItem, String> edit = new Column<BookingReservationItem, String>(new SimpleLinkCell()) {

            @Override
            public String getValue(final BookingReservationItem object) {
                return wfmStrings.edit();
            }
        };
        edit.setFieldUpdater((index, object, value) -> SinksContainerFactory.entryPoint.onHistoryChanged("bookingitemsreservation|edit/" + object.getObjectID()));
        dataGrid.addColumn(edit, wfmStrings.edit());
        dataGrid.setColumnWidth(edit, 10, com.google.gwt.dom.client.Style.Unit.PCT);

        //View
        final Column<BookingReservationItem, String> view = new Column<BookingReservationItem, String>(new SimpleLinkCell()) {

            @Override
            public String getValue(final BookingReservationItem object) {
                return wfmStrings.summaryView();
            }
        };
        view.setFieldUpdater((index, object, value) -> {
            Integer selectedItemId = objectID != null ? objectID : bookingItem.getSelectedId();
            SinksContainerFactory.entryPoint.onHistoryChanged("bookingitemsreservation|summary/" + selectedItemId);
        });
        dataGrid.addColumn(view, wfmStrings.summaryView());
        dataGrid.setColumnWidth(view, 10, com.google.gwt.dom.client.Style.Unit.PCT);

    }

    @Override
    public String getIconStyle() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


    @Override
    public HasLinks getLinkingUtil() {
        if (linkingUtil == null) {
            linkingUtil = new HasLinks(BookingItemsReservationAddView.this) {
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
                    return items != null ? items.getBookingItemName() : null;
                }

            };
        }
        return linkingUtil;
    }

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
