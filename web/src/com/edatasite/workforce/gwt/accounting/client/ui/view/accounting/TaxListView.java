/**********************************************************************************************************************
 * LAST CHANGE                                                                                                        *
 * User: Hayot                                                                                                        *
 * Time: 2010/4/8 6:20:43                                                                                             *
 **********************************************************************************************************************/

package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.TaxListData;
import com.edatasite.workforce.gwt.accounting.client.rpc.TaxListItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.client.ui.view.quickadd.TaxQuickAddForm;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;

/**
 * Created by IntelliJ IDEA.
 * User: Sher
 * Date: Jun 19, 2009
 * Time: 4:06:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class TaxListView extends BaseListView implements Constants, AccountingConstants {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private ListingPanel<TaxListItem> listingTable;
    private final InvoiceServiceAsync service = InvoiceService.App.get();

    public TaxListView() {
        super("texes", accountingStrings.taxRates());
        if (Utils.hasRole(PM) || Utils.hasRole(DR) || Utils.hasRole(ADMIN) || Utils.hasRole(ACCOUNTANT)) {
            setAddNew(() -> new TaxQuickAddForm());
        }
    }

    protected Widget onInitialize() {
        listingTable = new ListingPanel<>(getPanelType(), getColumnConfigs(), getListingRequestProvider(), getListingPanelDesign());

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_TAX_SAVED, TaxListView.this, (sender, args) -> listingTable.reloadPage());

        listingTable.setPDFListener(clickEvent -> {
            String pdfURL = CommandConstants.PDF_URL + "/taxRatesListPDFHandler";
            ListingFilterParameter filterParametrs = listingTable.getFilterParametrs();
            listingTable.callListPDF(pdfURL, filterParametrs);
        });

        listingTable.setExcelListener(clickEvent -> {
            String excelURL = CommandConstants.COMMON_URL + "/downloadTaxRatesSupplierListExcel";
            ListingFilterParameter filterParametrs = listingTable.getFilterParametrs();
            listingTable.callListExcel(excelURL, filterParametrs);
        });

        add(listingTable);
        return null;
    }

    private ListingPanelDesign getListingPanelDesign() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return new ListingFacetFilter() {
                    @Override
                    public FacetCallbackProvider getFacetCallbackProvider() {
                        return null;
                    }

                    @Override
                    public FacetContentConfigure getFacetFilterContentconfigure() {
                        return null;
                    }
                };
            }

            @Override
            public ActionButton initTopToolBarNew() {
                ActionButton newTaxRate = null;
                if (Utils.hasRole(PM) || Utils.hasRole(DR) || Utils.hasRole(ADMIN) || Utils.hasRole(ACCOUNTANT)) {
                    newTaxRate = getAddNewButton();
                    newTaxRate.addClickHandler(c -> new TaxQuickAddForm());
                }

                return newTaxRate;
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
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(accountingStrings.currentlyYouDoNotHaveAnyTaxRate());
                if (Utils.hasRole(PM) || Utils.hasRole(DR) || Utils.hasRole(ADMIN) || Utils.hasRole(ACCOUNTANT)) {
                    message.setTextBeforeLink(accountingStrings.noTaxRatetMessageBeforeLink());
                    message.setHref(c -> new TaxQuickAddForm());
                }
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    private ListingRequestProvider<TaxListItem> getListingRequestProvider() {
        return (filterParametrs, callback) -> service.getAccountingTaxList(filterParametrs, new AsyncCallback<TaxListData>() {

            @Override
            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }

            @Override
            public void onSuccess(TaxListData data) {
                callback.onSuccess(data.getTaxList());
            }
        });
    }

    private CustomColumnDefinitionConfig[] getColumnConfigs() {
        ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[3];
        columns[0] = new ColumnDefinitionConfig<TaxListItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {

            @Override
            public Anchor getCellValue(final TaxListItem taxListItem) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);
                boolean noneditable = taxListItem.getPermissionType() != null && taxListItem.getPermissionType().equals(NON_EDITABLE);
                boolean nondeletable = taxListItem.getPermissionType() != null && (taxListItem.getPermissionType().equals(NON_DELETABLE)
                        || taxListItem.getPermissionType().equals(NON_EDITABLE));

                if (!noneditable) {
                    MenuPopItem taxEdit = new MenuPopItem(wfmStrings.edit(), "icon-edit");
                    taxEdit.setCommand(() -> new TaxQuickAddForm(taxListItem.getObjectID()));
                    taxEdit.getElement().setId("tax_rates_edit_button");
                    actionItemCount++;
                    menuBar.addItem(taxEdit);

                }

                if (!nondeletable) {
                    MenuPopItem taxDelete = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                    taxDelete.getElement().setId("Tax_rates_delete_button");
                    taxDelete.setCommand(() -> {
                        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
                        messageBox.setTitle(wfmStrings.warning());
                        messageBox.setMessage(wfmStrings.sureYouWantToDelete());
                        messageBox.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                InvoiceService.App.get().deleteTax(taxListItem.getObjectID(), new AbstractAsyncCallback<Boolean>() {
                                    public void failure(Throwable caught) {
                                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                    }

                                    public void success(Boolean deleted) {
                                        if (deleted) {
                                            Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.tax()), Info.Type.INFO);
                                            listingTable.reloadPage();
                                        } else {
                                            Info.show(accountingStrings.infoMessage15(), Info.Type.WARNING);
                                        }
                                    }
                                });
                            }
                        });
                        messageBox.open();
                    });
                    menuBar.addItem(taxDelete);


                }

                ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columns[0].setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns[0].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns[0].setColumnSortable(false);
        columns[1] = new ColumnDefinitionConfig<TaxListItem, String>(wfmStrings.taxName(), TaxListItem.NAME, 200) {

            @Override
            public String getCellValue(TaxListItem taxListItem) {
                return taxListItem.getName();
            }
        };
        columns[1].setMinimumColumnWidth(180);
        columns[2] = new ColumnDefinitionConfig<TaxListItem, String>(wfmStrings.taxRate(), TaxListItem.TAXRATE, 100) {

            @Override
            public String getCellValue(TaxListItem taxListItem) {
                return AccountingUtils.get().formatTaxRate(taxListItem.getPercent());
            }
        };
        columns[2].setMinimumColumnWidth(80);
        return columns;
    }

    private ListPanelType getPanelType() {
        return ListPanelType.TaxPanel;
    }

    public String getIconStyle() {
        return "accountMark tax-list";
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
