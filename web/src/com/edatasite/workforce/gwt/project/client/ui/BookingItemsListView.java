package com.edatasite.workforce.gwt.project.client.ui;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;
import com.edatasite.workforce.gwt.project.client.rpc.BookingItemsItem;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectService;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: developer
 * Date: 5/18/12
 * Time: 4:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class BookingItemsListView extends BaseListView implements Constants {

    private static final ProjectServiceAsync projectService = ProjectService.App.get();
    private static final ProjectStrings projectString = ProjectStrings.App.get();
    private ListingPanel<BookingItemsItem> listPanel;
    protected Set selectedItems = new HashSet();
    private final String context;

    public BookingItemsListView() {
        super(BOOKINGITEMS_LIST);
        setDescription(property.getPlural(wfmStrings.bookingItems()));
        this.context = PermissionConstants.TRAININGCENTER_CONTEXT;
    }

    public BookingItemsListView(String context) {
        super(BOOKINGITEMS_LIST);
        setDescription(property.getPlural(wfmStrings.bookingItems()));
        this.context = context;
    }

    protected Widget onInitialize() {
        listPanel = new ListingPanel<>(ListPanelType.BookingItemsListPanel, getColumnConfig(), getListProvider(), getListDesign());


        listPanel.setPDFListener(event -> {
            String pdfURL = CommandConstants.PDF_URL + "/downloadBookingItemsListViewPDF";
            ListingFilterParameter filterParametrs = listPanel.getFilterParametrs();
            listPanel.callListPDF(pdfURL, filterParametrs);
        });

        listPanel.setExcelListener(event -> {
            String excelURL = CommandConstants.COMMON_URL + "/downloadBookingItemsListExcelHandler";
            listPanel.callListExcel(excelURL, listPanel.getFilterParametrs());
        });

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_BOOKING_ITEMS_ADD, BookingItemsListView.this, (sender, args) -> listPanel.reloadPage());

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_BOOKING_ITEMS_EDIT, BookingItemsListView.this, (sender, args) -> listPanel.reloadPage());

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_BOOKING_ITEMS_SAVED, BookingItemsListView.this, (sender, args) -> listPanel.reloadPage());


        listPanel.addSelectionRowHandler(selectedRows -> selectedItems = selectedRows);

        add(listPanel);

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_BOOKING_ITEM_DELETE, BookingItemsListView.this, (sender, args) -> listPanel.reloadPage());
        return null;
    }

    private CustomColumnDefinitionConfig[] getColumnConfig() {
        ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[7];

        columns[0] = new ColumnDefinitionConfig<BookingItemsItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {

            @Override
            public Anchor getCellValue(final BookingItemsItem item) {

                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);
                menuBar.setAutoOpen(true);


                final MenuPopItem bookingItemSummary = new MenuPopItem(wfmStrings.summaryView(), "icon-team-small");
                bookingItemSummary.ensureDebugId("view");
                bookingItemSummary.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("bookingitems|summary/" + item.getObjectID()));
                actionItemCount++;
                menuBar.addItem(bookingItemSummary);

                MenuPopItem bookingItemsEdit = new MenuPopItem(wfmStrings.edit(), "icon-issue-edit-small");
                bookingItemsEdit.ensureDebugId("edit");
                bookingItemsEdit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("bookingitems|edit/" + item.getObjectID()));
                actionItemCount++;
                menuBar.addItem(bookingItemsEdit);
                bookingItemsEdit.setVisible(Utils.hasPermission(PermissionConstants.PM_CONTEXT.equals(context) ? PermissionConstants.PM_BOOKING_EDIT : PermissionConstants.TC_BOOKING_EDIT));

                MenuPopItem addReservation = new MenuPopItem(wfmStrings.addReservation(), "icon-issue-edit-small");
                addReservation.ensureDebugId("addReservation");
                addReservation.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("bookingitemsreservation|add/add/" + item.getObjectID()));
                actionItemCount++;
                menuBar.addItem(addReservation);
                bookingItemsEdit.setVisible(Utils.hasPermission(PermissionConstants.PM_CONTEXT.equals(context) ? PermissionConstants.PM_ADD_RESERVATION : PermissionConstants.TC_ADD_RESERVATION));

                MenuPopItem deleteBookingItem = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                deleteBookingItem.setCommand(() -> {
                    final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                    messageBox.setTitle(wfmStrings.confirmation());
                    messageBox.setMessage(wfmStrings.sureYouWantToDelete());
                    messageBox.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            LoadingPanel.loading(true);
                            ProjectService.App.get().deleteBookingItem(item.getObjectID(), new AbstractAsyncCallback<Void>() {
                                @Override
                                public void failure(Throwable caught) {
                                    LoadingPanel.loading(false);
                                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                }

                                @Override
                                public void success(Void result) {
                                    LoadingPanel.loading(false);
                                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_BOOKING_ITEM_DELETE, result, BookingItemsListView.this);
                                    Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.bookingItem()), Info.Type.INFO);
                                }
                            });
                        }
                    });
                    messageBox.open();
                });
                actionItemCount++;
                menuBar.addItem(deleteBookingItem);

                ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);

                return toolItem.getAction();
            }
        };
        columns[0].setColumnSortable(false);
        columns[0].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns[0].setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);

        columns[1] = new ColumnDefinitionConfig<BookingItemsItem, SimpleLink>(wfmStrings.number(), BookingItemsItem.ITEM_NUMBER, 50) {
            @Override
            public SimpleLink getCellValue(BookingItemsItem item) {
                return new SimpleLink((item.getItemNumber() != null ? item.getItemNumber() : ""), "bookingitems|summary/" + item.getObjectID());
            }
        };
        columns[1].setColumnSortable(true);
        columns[1].setMinimumColumnWidth(50);

        columns[2] = new ColumnDefinitionConfig<BookingItemsItem, SimpleLink>(wfmStrings.name(), BookingItemsItem.ITEM_NAME, 120) {
            @Override
            public SimpleLink getCellValue(final BookingItemsItem item) {
                return new SimpleLink((item.getItemName() != null ? item.getItemName() : ""), "bookingitems|summary/" + item.getObjectID());
            }
        };
        columns[2].setMinimumColumnWidth(70);
        columns[2].setColumnSortable(true);


        columns[3] = new ColumnDefinitionConfig<BookingItemsItem, String>(wfmStrings.category(), BookingItemsItem.CATEGORY, 120) {
            @Override
            public String getCellValue(BookingItemsItem item) {
                return item.getCategory() != null ? item.getCategory().getName() : wfmStrings.notAvailable();
            }
        };
        columns[3].setColumnSortable(true);
        columns[3].setMinimumColumnWidth(70);

        columns[4] = new ColumnDefinitionConfig<BookingItemsItem, String>(wfmStrings.status(), BookingItemsItem.STATUS, 150) {
            @Override
            public String getCellValue(BookingItemsItem item) {
                return item.getStatus();
            }
        };
        columns[4].setColumnSortable(false);
        columns[4].setMinimumColumnWidth(120);

        columns[5] = new ColumnDefinitionConfig<BookingItemsItem, String>(Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location()), BookingItemsItem.LOCATION, 150) {
            @Override
            public String getCellValue(BookingItemsItem item) {
                return item.getLocation();
            }
        };
        columns[5].setColumnSortable(false);
        columns[5].setMinimumColumnWidth(120);

        columns[6] = new ColumnDefinitionConfig<BookingItemsItem, String>(wfmStrings.description(), BookingItemsItem.DESCRIPTION, 150) {
            @Override
            public String getCellValue(BookingItemsItem item) {
                return item.getDescription();
            }
        };
        columns[6].setColumnSortable(false);
        columns[6].setMinimumColumnWidth(120);

        return columns;
    }

    private ListingRequestProvider<BookingItemsItem> getListProvider() {
        return (filterParametrs, callback) -> {
            projectService.getBookingItems(filterParametrs, new AbstractAsyncCallback<ListResult<BookingItemsItem>>() {
                public void failure(Throwable throwable) {
                    callback.onFailure(throwable);
                }

                public void success(ListResult<BookingItemsItem> itemList) {
                    callback.onSuccess(itemList);
                }
            });
        };
    }

    private ListingPanelDesign getListDesign() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public ActionButton initTopToolBarNew() {

                ActionButton newItem = getAddNewButton(ActionButton.Type.TOOLMENU);
                MenuBar menu = new MenuBar(true);

                MenuPopItem addNewCategory = new MenuPopItem(wfmStrings.addCategory());
                addNewCategory.ensureDebugId(wfmStrings.addCategory());
                addNewCategory.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("bookingcategory|add/add"));
                MenuPopItem addNewReservation = new MenuPopItem(projectString.reservation());
                addNewReservation.ensureDebugId(projectString.reservation());
                addNewReservation.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("bookingitemsreservation|add/add"));
                if (Utils.hasPermission(PermissionConstants.PM_CONTEXT.equals(context) ? PermissionConstants.PM_BOOKING_ITEMS_ADD : PermissionConstants.TC_BOOKING_ITEMS_ADD)) {
                    MenuPopItem addNewBookingItem = new MenuPopItem(wfmStrings.item());
                    addNewBookingItem.ensureDebugId(wfmStrings.item());
                    addNewBookingItem.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("bookingitems|add/add"));
                    menu.addItem(addNewBookingItem);
                }
                menu.addItem(addNewReservation);

                newItem.setMenu(menu);
                return newItem;
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
                exportOption.initExport(null, true);
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message;
                message = new DefaultNoItemsMessage(wfmStrings.currentlyNoBookingItems());
                message.setTextBeforeLink(wfmStrings.youCanRegistrYourBookingItemsByClicking());
                message.setHref("bookingitems|add/add");
                emptyDataTable.initEmptyDataTable(message);
            }

            public void initDataEmptyTable(HTML emptyTable) {
                emptyTable.setText("");
                emptyTable.setStyleName("drawColumns");
                emptyTable.addClickHandler(clickEvent -> {
                });
            }
        };
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

    @Override
    public String getIconStyle() {
        return "bgMark booking-list";
    }
    @Override
    public String getPropertyCode() {
        return BOOKINGITEMS_LIST;
    }
}
