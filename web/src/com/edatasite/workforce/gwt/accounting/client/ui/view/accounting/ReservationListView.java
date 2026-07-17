package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingServiceAsync;
import com.edatasite.workforce.gwt.accounting.client.rpc.ReservationItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.*;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Sanjar
 * Date: Feb 1, 2011
 * Time: 4:19:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReservationListView extends BaseListView implements Constants, AccountingConstants, PermissionConstants {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final AccountingServiceAsync accountingService = AccountingService.App.get();

    private ListingPanel<ReservationItem> list;
    private ListingFilterParameter fpParametrs;

    public ReservationListView() {
        super("reservations", wfmStrings.reservations());
    }

    public void refresh() {
        list.reloadPage();
    }

    protected Widget onInitialize() {
        list = new ListingPanel<>(ListPanelType.ReservationPanel, getColumns(), getListingRequestProvider(), getListingPanelDesign());

        list.setPDFListener(clickEvent -> {
            String pdfURL = CommandConstants.PDF_URL + "/downloadReservationPDFHandler";
            ListingFilterParameter filterParametrs = list.getFilterParametrs();
            list.callListPDF(pdfURL, filterParametrs);
        });

        list.setExcelListener(clickEvent -> {
            String excelURL = CommandConstants.COMMON_URL + "/downloadReservationExelHandler";
            ListingFilterParameter filterParametrs = list.getFilterParametrs();
            list.callListExcel(excelURL, filterParametrs);
        });

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_RESERVATION_SAVED, ReservationListView.this, (sender, args) -> list.reloadPage());


        add(list);
        list.reloadPage();
        return null;
    }

    private ListingPanelDesign getListingPanelDesign() {
        return new ListingPanelDesign() {

            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
            }

            @Override
            public ActionButton initTopToolBarNew() {
                if (Utils.hasPermission(ACCOUNTING_RESERVATION_ADD)) {
                    ActionButton addNew = getAddNewButton();
                    addNew.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("reservation|add/add"));
                    return addNew;
                }
                return null;
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(accountingStrings.currentlyThereAreNoReservations());
                if (Utils.hasPermission(ACCOUNTING_RESERVATION_ADD)/*(Utils.hasRole(PM) || Utils.hasRole(DR) || Utils.hasRole(ADMIN))*/) {
                    message.setTextBeforeLink(accountingStrings.youCanStartAddingReservation());
                    message.setHref("reservation|add/add");
                }
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    private ListingRequestProvider<ReservationItem> getListingRequestProvider() {
        return (filterParametrs, callback) -> accountingService.getReservationList(filterParametrs, new AsyncCallback<ListResult<ReservationItem>>() {
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess(ListResult<ReservationItem> list) {
                callback.onSuccess(list);
            }
        });
    }

    private int actionItemCount;

    private ColumnDefinitionConfig[] getColumns() {

        ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[9];

        columns[0] = new ColumnDefinitionConfig<ReservationItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {

            @Override
            public Anchor getCellValue(final ReservationItem item) {
                actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);

                if (Utils.hasPermission(ACCOUNTING_RESERVATION_EDIT)) {
                    if (!item.getStatus().equals(RESERVATION_STATUS_CANCELED) && !item.getStatus().equals(RESERVATION_STATUS_CLOSED)) {
                        MenuPopItem editReservation = new MenuPopItem(wfmStrings.edit(), "icon-edit");
                        editReservation.setCommand(() -> {
                            if (RESERVATION_TYPE_PRODUCT.equals(item.getType())) {
                                SinksContainerFactory.entryPoint.onHistoryChanged("reservation|edit/" + item.getId());
                            } else {
                                SinksContainerFactory.entryPoint.onHistoryChanged("reservation|edit/" + item.getId() + "/type/event");
                            }
                        });
                        menuBar.addItem(editReservation);
                    }
                }

                final com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem toolItem = new com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }

        };
        columns[0].setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns[0].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns[0].setColumnSortable(false);

        columns[1] = new ColumnDefinitionConfig<ReservationItem, SimpleLink>(wfmStrings.id(), ReservationItem.ID, 50) {

            @Override
            public SimpleLink getCellValue(ReservationItem item) {
                return getLink("#" + item.getId(), "reservation|summary/" + item.getId());
            }
        };
        columns[1].setMinimumColumnWidth(50);
        columns[1].addStyleAttribute("padding-left", "5px");
        columns[2] = new ColumnDefinitionConfig<ReservationItem, String>(wfmStrings.type(), ReservationItem.TYPE, 150) {

            @Override
            public String getCellValue(ReservationItem item) {
                return item.getType().equals(RESERVATION_TYPE_PRODUCT) ? wfmStrings.product() : Property.get(Constants.EVENT_LIST, wfmStrings.event());
            }
        };
        columns[2].setMinimumColumnWidth(50);
        columns[2].addStyleAttribute("padding-left", "5px");
        columns[2].setColumnSortable(false);

        columns[3] = new ColumnDefinitionConfig<ReservationItem, String>(wfmStrings.fromDate(), ReservationItem.FROM_DATE, 150) {

            @Override
            public String getCellValue(ReservationItem item) {
                return DateUtils.formatInternalShort(item.getFromDate());
            }
        };

        columns[3].setMinimumColumnWidth(50);
        columns[3].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columns[4] = new ColumnDefinitionConfig<ReservationItem, String>(wfmStrings.toDate(), ReservationItem.TO_DATE, 150) {

            @Override
            public String getCellValue(ReservationItem item) {
                return DateUtils.formatInternalShort(item.getToDate());
            }
        };
        columns[4].setMinimumColumnWidth(50);
        columns[4].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columns[5] = new ColumnDefinitionConfig<ReservationItem, String>(wfmStrings.status(), ReservationItem.STATUS, 150) {

            @Override
            public String getCellValue(ReservationItem item) {
                String status = "";
                if (item.getStatus().equals(RESERVATION_STATUS_PENDING)) {
                    status = wfmStrings.pending();
                } else if (item.getStatus().equals(RESERVATION_STATUS_RESERVED)) {
                    status = "<span style=\"color:blue\">" + accountingStrings.reserved() + "</span>";
                } else if (item.getStatus().equals(RESERVATION_STATUS_STARTED)) {
                    status = "<span style=\"color:green\">" + accountingStrings.started() + "</span>";
                } else if (item.getStatus().equals(RESERVATION_STATUS_CLOSED)) {
                    status = wfmStrings.closed();
                } else if (item.getStatus().equals(RESERVATION_STATUS_CANCELED)) {
                    status = "<i>" + accountingStrings.canceled() + "</i>";
                }

                if (item.getToDate() != null) {
                    if (item.getToDate().compareTo(new Date()) < 0 && (item.getStatus().equals(RESERVATION_STATUS_STARTED) || item.getStatus().equals(RESERVATION_STATUS_RESERVED))) {
                        status = "<span style=\"color:red\">" + accountingStrings.overdue() + "</span>";
                    }
                }

                return status;
            }
        };
        columns[5].setMinimumColumnWidth(50);
        columns[5].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columns[6] = new ColumnDefinitionConfig<ReservationItem, String>(wfmStrings.name(), ReservationItem.NAME, 200) {

            @Override
            public String getCellValue(ReservationItem item) {
                String name = "";
                if (item.getItemId() != null) {
                    name = item.getItemName() != null ? item.getItemName() + (item.getItemCode() != null ? " [" + item.getItemCode() + "]" : "") : "";
                } else {
                    name = item.getEventName();
                }
                return name;
            }
        };
        columns[6].setMinimumColumnWidth(50);
        columns[6].addStyleAttribute("padding-left", "5px");
        columns[7] = new ColumnDefinitionConfig<ReservationItem, String>(accountingStrings.shippingMethodLocation(), ReservationItem.SHIPPING_METOD, 200) {

            @Override
            public String getCellValue(ReservationItem item) {
                return item.getShippingMethodName() != null ? item.getShippingMethodName() : "";
            }
        };
        columns[7].addStyleAttribute("padding-left", "5px");
        columns[7].setMinimumColumnWidth(50);
        columns[8] = new ColumnDefinitionConfig<ReservationItem, Comparable>(accountingStrings.qtyAttendants(), ReservationItem.QYT, 100) {

            @Override
            public Comparable getCellValue(ReservationItem item) {
                return item.getQty() != null ? item.getQty() : "0";
            }
        };
        columns[8].setMinimumColumnWidth(100);
        columns[8].addStyleAttribute("padding-left", "5px");
        return columns;
    }

    public String getIconStyle() {
        return "accountMark  reservation-list";//return "icon-accounting-bank-accounts";
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
