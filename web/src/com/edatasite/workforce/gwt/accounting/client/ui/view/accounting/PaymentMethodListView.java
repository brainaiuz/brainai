package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingServiceAsync;
import com.edatasite.workforce.gwt.accounting.client.rpc.PaymentMethodItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;

/**
 * Created by Omonullo Abdullaev on 08.04.16.
 */
public class PaymentMethodListView extends BaseListView implements Constants, AccountingConstants, PermissionConstants {

    private static final AccountingServiceAsync accountingService = AccountingService.App.get();

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    private ListingPanel<PaymentMethodItem> list;

    public PaymentMethodListView() {
        super("paymentmethodlist", accountingStrings.paymentMethods());
        if (Utils.hasPermission(ACCOUNTING_PAYMENT_METHOD_ADD)) {
            setAddNew(() -> new PaymentMethodPopup());
        }
    }

    @Override
    public String getIconStyle() {
        return "accountMark ac-type-num-settings";
    }

    protected Widget onInitialize() {
        list = new ListingPanel<>(ListPanelType.PaymentMethodListPanel, getColumns(), getListingRequestProvider(), getListingPanelDesign());

        list.setPDFListener(clickEvent -> {
            String pdfURL = CommandConstants.PDF_URL + "/paymentMethodListPDFHandler";
            ListingFilterParameter filterParametrs = list.getFilterParametrs();
            list.callListPDF(pdfURL, filterParametrs);
        });

        list.setExcelListener(clickEvent -> {
            String excelURL = CommandConstants.COMMON_URL + "/downloadPaymentMethodExcelHandler";
            ListingFilterParameter filterParametrs = list.getFilterParametrs();
            list.callListExcel(excelURL, filterParametrs);
        });

        /*WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PAYMENT_METHOD_ADD, {PaymentMethodListView.this, new WfmUiEvent() {
            public void onWfmUiEvent(Widget sender, Object args) {
                list.reloadPage();
            }
        });*/
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PAYMENT_METHOD_ADD, PaymentMethodListView.this, (sender, args) -> list.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PAYMENT_METHOD_DELETE, PaymentMethodListView.this, (sender, args) -> list.reloadPage());

        add(list);
        list.reloadPage();
        return null;
    }

    private ColumnDefinitionConfig[] getColumns() {
        ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[5];
        columns[0] = new ColumnDefinitionConfig<PaymentMethodItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {

            @Override
            public Anchor getCellValue(final PaymentMethodItem item) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);

                if (Utils.hasPermission(ACCOUNTING_PAYMENT_METHOD_EDIT)) {
                    MenuPopItem addPayMethod = new MenuPopItem(wfmStrings.edit(), "icon-employee-edit-profile");
                    addPayMethod.getElement().setId("Payment_edit_button");
                    addPayMethod.setCommand(() -> new PaymentMethodPopup(item.getObjectID()));
                    actionItemCount++;
                    menuBar.addItem(addPayMethod);
                }

                if (Utils.hasPermission(ACCOUNTING_PAYMENT_METHOD_DELETE)) {
                    MenuPopItem deletePaymentMethod = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                    deletePaymentMethod.getElement().setId("Payment_delete_button");
                    deletePaymentMethod.setCommand(() -> {
                        final WfmMessageBox message = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                        message.setTitle(wfmStrings.warning());
                        message.setMessage(wfmStrings.sureYouWantToDelete());
                        message.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                LoadingPanel.loading(true);
                                accountingService.deletePaymentMethod(item.getObjectID(), new AsyncCallback<Boolean>() {
                                    @Override
                                    public void onFailure(Throwable throwable) {
                                        LoadingPanel.loading(false);
                                        Info.warn(wfmStrings.sorrySomethingWentWrong());
                                    }

                                    @Override
                                    public void onSuccess(Boolean isDeleted) {
                                        LoadingPanel.loading(false);
                                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PAYMENT_METHOD_DELETE, isDeleted, PaymentMethodListView.this);
                                    }
                                });
                            }
                        });
                        message.open();
                    });
                    actionItemCount++;
                    menuBar.addItem(deletePaymentMethod);
                }

                final com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem toolItem = new com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columns[0].setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns[0].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns[0].setColumnSortable(false);

        columns[1] = new ColumnDefinitionConfig<PaymentMethodItem, String>(wfmStrings.name(), PaymentMethodItem.NAME, 100) {

            @Override
            public String getCellValue(PaymentMethodItem item) {
                return item.getName() != null ? item.getName() : "";
            }
        };
        columns[2] = new ColumnDefinitionConfig<PaymentMethodItem, String>(wfmStrings.code(), PaymentMethodItem.CODE, 100) {

            @Override
            public String getCellValue(PaymentMethodItem item) {
                return item.getCode() != null ? item.getCode() : "";
            }
        };
        columns[2].setShow(false);

        columns[3] = new ColumnDefinitionConfig<PaymentMethodItem, String>(wfmStrings.weight(), PaymentMethodItem.WEIGTH, 100) {

            @Override
            public String getCellValue(PaymentMethodItem item) {
                return String.valueOf(item.getWeigth() != null ? item.getWeigth() : "");
            }
        };
        columns[3].setShow(false);

        columns[4] = new ColumnDefinitionConfig<PaymentMethodItem, String>(wfmStrings.description(), PaymentMethodItem.DESCRIPTION, 100) {

            @Override
            public String getCellValue(PaymentMethodItem item) {
                return item.getDescription() != null ? item.getDescription() : "";
            }
        };
        columns[4].setShow(true);
        return columns;
    }

    private ListingRequestProvider<PaymentMethodItem> getListingRequestProvider() {
        return (filterParametrs, callback) -> accountingService.getAllPaymentMethods(filterParametrs, new AsyncCallback<ListResult<PaymentMethodItem>>() {
            @Override
            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }

            @Override
            public void onSuccess(ListResult<PaymentMethodItem> result) {
                callback.onSuccess(result);
            }
        });
    }

    private ListingPanelDesign getListingPanelDesign() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
            }

            @Override
            public ActionButton initTopToolBarNew() {
                if (Utils.hasPermission(ACCOUNTING_PAYMENT_METHOD_ADD)) {
                    ActionButton addPaymentMethod = getAddNewButton();
                    addPaymentMethod.addClickHandler(clickEvent -> new PaymentMethodPopup());
                    return addPaymentMethod;
                }
                return null;
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
                /*DefaultNoItemsMessage message = new DefaultNoItemsMessage(accountingStrings.currentlyThereAreNoReservations());*/
                /*if (Utils.hasPermission(ACCOUNTING_RESERVATION_ADD)(Utils.hasRole(PM) || Utils.hasRole(DR) || Utils.hasRole(ADMIN))) {
                    message.setTextBeforeLink(accountingStrings.youCanStartAddingReservation());
                    message.setHref("reservation|add/add");
                }
                emptyDataTable.initEmptyDataTable(message);*/
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
}
