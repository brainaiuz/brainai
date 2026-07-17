package com.edatasite.workforce.gwt.invoice.client.ui.view;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.client.ui.view.quickadd.ShippingMethodQuickAddForm;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
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
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.invoice.client.rpc.ShippingMethod;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Jun 16, 2010
 * Time: 5:38:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class ShippingMethodsListView extends BaseListView implements Constants, AccountingConstants {

    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private ListingPanel<ShippingMethod> list;

    public ShippingMethodsListView() {
        super("shippintMethod", accountingStrings.shippingMethods());
        setAddNew(() -> new ShippingMethodQuickAddForm());
    }

    protected Widget onInitialize() {
        list = new ListingPanel<>(getPanelType(), getColumnConfigs(), getListingRequestProvider(), getDisagn());
        list.setPDFListener(clickEvent -> {
            String pdfURL = CommandConstants.PDF_URL + "/shippingMethodsListPDFHandler";
            ListingFilterParameter filterParametrs = list.getFilterParametrs();
            if (filterParametrs == null) {
                filterParametrs = new ListingFilterParameter();
            }
            list.callListPDF(pdfURL, filterParametrs);
        });

        list.setExcelListener(clickEvent -> {
            String excelURL = CommandConstants.COMMON_URL + "/downloadShippingMethodsListExcel";
            list.callListExcel(excelURL, list.getFilterParametrs());
        });
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SHIPPINGMETHOD_SAVED, ShippingMethodsListView.this, (sender, args) -> list.reloadPage());

        add(list);
        return null;
    }

    private ListingRequestProvider<ShippingMethod> getListingRequestProvider() {
        return (filterParametrs, callback) -> InvoiceService.App.get().getShippingMethodData(filterParametrs, new AbstractAsyncCallback<ListResult<ShippingMethod>>() {

            @Override
            public void failure(Throwable throwable) {
                callback.onFailure(throwable);
            }

            @Override
            public void success(ListResult<ShippingMethod> result) {
                callback.onSuccess(result);
            }
        });
    }

    private ListingPanelDesign getDisagn() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
            }

            @Override
            public ActionButton initTopToolBarNew() {
                ActionButton addNew = getAddNewButton();
                addNew.addClickHandler(ch -> new ShippingMethodQuickAddForm());
                return addNew;
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
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(accountingMessages.currentlyYouDontHaveAnyShippingMethods());
                message.setTextBeforeLink(accountingStrings.youCanStartAddingShippingByClicking());
                message.setHref(c -> new ShippingMethodQuickAddForm());
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    private ListPanelType getPanelType() {
        return ListPanelType.ShippingMethodsPanel;
    }


    private ColumnDefinitionConfig[] getColumnConfigs() {

        final ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[5];

        columns[0] = new ColumnDefinitionConfig<ShippingMethod, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {

            @Override
            public Anchor getCellValue(final ShippingMethod item) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);

                MenuPopItem shippingMethodSummary = new MenuPopItem(wfmStrings.edit(), "icon-sales-quote-small");
                shippingMethodSummary.getElement().setId("ShippingMethod_edit_button");
                shippingMethodSummary.setCommand(() -> new ShippingMethodQuickAddForm(item.getId()));
                actionItemCount++;

                menuBar.addItem(shippingMethodSummary);
                MenuPopItem deleteShippingMedthod = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                deleteShippingMedthod.getElement().setId("Shipping_method_delete_button");
                deleteShippingMedthod.setCommand(() -> {
                    final WfmMessageBox message = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                    message.setTitle(wfmStrings.warning());
                    message.setMessage(wfmStrings.sureYouWantToDelete());
                    message.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onCancel() {
                        }

                        @Override
                        public void onSubmit() {
                            AccountingService.App.get().deleteShippingMedthod(item.getId(), new AsyncCallback<Boolean>() {

                                @Override
                                public void onFailure(Throwable caught) {
                                    Info.show(wfmStrings.error(), Info.Type.WARNING);
                                }

                                @Override
                                public void onSuccess(Boolean result) {
                                    if (result) {
                                        Info.show(accountingMessages.shippingMethodHasBeenSuccessfullyDeleted(), Info.Type.INFO);
                                        list.reloadPage();
                                    } else {
                                        Info.show(accountingMessages.thisShippingMethodIsInUse(), Info.Type.WARNING);
                                    }
                                }
                            });
                        }
                    });
                    message.open();
                });
                actionItemCount++;
                menuBar.addItem(deleteShippingMedthod);

                MenuPopItem pdfVersion = new MenuPopItem(wfmStrings.pdf(), "icon-pdf-profile");
                pdfVersion.getElement().setId("Shipping_method_pdf_button");
                pdfVersion.setCommand(() -> {
                    String pdfURL = CommandConstants.PDF_URL + "/pickViewPDFHandler";
                    RequestObject requestObject = new RequestObject(item.getId());
                    list.callItemPDF(pdfURL, requestObject);
                });
                actionItemCount++;
                menuBar.addItem(pdfVersion);
                ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };

        columns[0].setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns[0].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns[0].setColumnSortable(false);

        columns[1] = new ColumnDefinitionConfig<ShippingMethod, String>(wfmStrings.name(), ShippingMethod.NAME, 140) {

            @Override
            public String getCellValue(ShippingMethod item) {
                return item.getName();
            }
        };

        columns[2] = new ColumnDefinitionConfig<ShippingMethod, String>(wfmStrings.description(), ShippingMethod.DESCRIPTION, 100) {

            @Override
            public String getCellValue(ShippingMethod item) {
                return item.getDescription();
            }
        };

        columns[3] = new ColumnDefinitionConfig<ShippingMethod, BigDecimal>(wfmStrings.price(), ShippingMethod.PRICE, 100) {

            @Override
            public BigDecimal getCellValue(ShippingMethod item) {
                return item.getPrice();
            }
        };

        columns[4] = new ColumnDefinitionConfig<ShippingMethod, String>(wfmStrings.taxRate(), ShippingMethod.TAXRATE, 100) {

            @Override
            public String getCellValue(ShippingMethod item) {
                return item.getTaxItem() != null ? item.getTaxItem().getName() : null;
            }
        };

        return columns;
    }

    @Override
    public String getIconStyle() {
        return "accountMark shipping-method-list";
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
