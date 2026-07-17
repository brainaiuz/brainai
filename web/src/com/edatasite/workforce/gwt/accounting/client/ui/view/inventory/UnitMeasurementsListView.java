package com.edatasite.workforce.gwt.accounting.client.ui.view.inventory;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.UnitMeasurementItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
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
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
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

import java.util.HashMap;

import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_UNIT_MEASUREMENTS_ADD;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar
 * Date: Apr 16, 2010
 * Time: 11:53:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class UnitMeasurementsListView extends BaseListView implements Constants {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    private ListingPanel<UnitMeasurementItem> list;

    public UnitMeasurementsListView() {
        super("unitmeasurementsList", accountingStrings.measurements());
        if ((Utils.hasRole(PM) || Utils.hasRole(DR) || Utils.hasRole(ADMIN))) {
            setAddNew(() -> new AddUnitMeasurementView(null, null));
        }
    }

    protected Widget onInitialize() {
        list = new ListingPanel<>(getPanelType(), getColumnConfigs(), getListingRequestProvider(), getDisagn());
        list.setPDFListener(clickEvent -> {
            String pdfURL = CommandConstants.PDF_URL + "/unitMeasurementsListPDFHandler";
            ListingFilterParameter filterParametrs = list.getFilterParametrs();
            if (filterParametrs == null) {
                filterParametrs = new ListingFilterParameter();
            }
            list.callListPDF(pdfURL, filterParametrs);
        });
        list.setExcelListener(clickEvent -> {
            String excelURL = CommandConstants.COMMON_URL + "/unitMeasurementsListExcelHandler";
            ListingFilterParameter filterParametrs = list.getFilterParametrs();
            list.callListExcel(excelURL, filterParametrs);
        });

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_UNITMEASUREMENT_SAVED, UnitMeasurementsListView.this, (sender, args) -> list.reloadPage());
        add(list);
        list.reloadPage();
        return null;
    }

    private ListingPanelDesign getDisagn() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
            }

            @Override
            public ActionButton initTopToolBarNew() {
                ActionButton addNew = null;
                if (Utils.hasPermission(ACCOUNTING_UNIT_MEASUREMENTS_ADD)) {
                    addNew = getAddNewButton();
                    addNew.addClickHandler(clickEvent -> new AddUnitMeasurementView(null, null));
                }
                return addNew;
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
                exportOption.initExport(null, true);
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(accountingStrings.currentlyYouDontHaveAnyMeasurement());
                if ((Utils.hasRole(PM) || Utils.hasRole(DR) || Utils.hasRole(ADMIN))) {
                    message.setTextBeforeLink(accountingStrings.noMeasurementBeforeLinkMessage());
                    message.setHref(clickEvent -> new AddUnitMeasurementView(null, null));
                }
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    private ListPanelType getPanelType() {
        return ListPanelType.UnitMeasurementsListPanel;
    }

    private CustomColumnDefinitionConfig[] getColumnConfigs() {
        final ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[3];

        columns[0] = new ColumnDefinitionConfig<UnitMeasurementItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final UnitMeasurementItem item) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);

                MenuPopItem edit = new MenuPopItem(wfmStrings.edit(), "icon-employee-edit-profile");
                edit.getElement().setId("Measurement_edit_button");
                edit.setCommand(() -> new AddUnitMeasurementView(item.getObjectID(), null));
                actionItemCount++;
                menuBar.addItem(edit);

                if (Utils.hasRole(ADMIN)) {
                    MenuPopItem deleteMeasurement = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                    deleteMeasurement.getElement().setId("Measurement_delete_button");
                    deleteMeasurement.setCommand(() -> {
                        final WfmMessageBox message = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                        message.setTitle(wfmStrings.warning());
                        message.setMessage(wfmStrings.sureYouWantToDelete());
                        message.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                AccountingService.App.get().deleteUnitMeasurement(item.getObjectID(), new AbstractAsyncCallback<HashMap<String, Integer>>() {
                                    public void failure(Throwable throwable) {
                                        Info.show(accountingStrings.errorDeletingMeasurement(), Info.Type.WARNING);
                                    }

                                    public void success(HashMap<String, Integer> isUsed) {
                                        if (isUsed == null || (!isUsed.containsKey(AccountingConstants.PRODUCT) && !isUsed.containsKey(INV_ITEM) && !isUsed.containsKey(QUOTE_ITEM) && !isUsed.containsKey(OPPORTUNITY_ITEM))) {
                                            Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.measurement()), Info.Type.INFO);
                                            list.reloadPage();
                                        } else {
                                            StringBuilder message = new StringBuilder(accountingStrings.errorDeletingMeasurement()).append("</br>");
                                            if (isUsed.containsKey(AccountingConstants.PRODUCT)) {
                                                message.append(wfmMessages.unitMeasurementIsUsedInItemNTimes(wfmStrings.product(), String.valueOf(isUsed.get(AccountingConstants.PRODUCT)))).append("</br>");
                                            }
                                            if (isUsed.containsKey(INV_ITEM)) {
                                                message.append(wfmMessages.unitMeasurementIsUsedInItemNTimes(accountingStrings.invoiceItem(), String.valueOf(isUsed.get(INV_ITEM)))).append("</br>");
                                            }
                                            if (isUsed.containsKey(QUOTE_ITEM)) {
                                                message.append(wfmMessages.unitMeasurementIsUsedInItemNTimes(accountingStrings.quoteItem(), String.valueOf(isUsed.get(QUOTE_ITEM)))).append("</br>");
                                            }
                                            if (isUsed.containsKey(OPPORTUNITY_ITEM)) {
                                                message.append(wfmMessages.unitMeasurementIsUsedInItemNTimes(accountingStrings.opportunityItem(), String.valueOf(isUsed.get(OPPORTUNITY_ITEM)))).append("</br>");
                                            }
                                            Info.show(message.toString(), Info.Type.WARNING);
                                        }
                                    }
                                });
                            }
                        });
                        message.open();
                    });
                    menuBar.addItem(deleteMeasurement);
                }

                final com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem toolItem = new com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columns[0].setColumnSortable(false);
        columns[0].setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns[0].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);

        columns[1] = new ColumnDefinitionConfig<UnitMeasurementItem, String>(wfmStrings.name(), UnitMeasurementItem.NAME, 100) {
            @Override
            public String getCellValue(UnitMeasurementItem item) {
                return item.getName();
            }
        };

        columns[2] = new ColumnDefinitionConfig<UnitMeasurementItem, String>(wfmStrings.description(), UnitMeasurementItem.DESCRIPTION, 100) {
            @Override
            public String getCellValue(UnitMeasurementItem item) {
                return item.getDescription();
            }
        };
        return columns;
    }

    private ListingRequestProvider<UnitMeasurementItem> getListingRequestProvider() {
        return (filterParametrs, callback) -> AccountingService.App.get().getUnitMeasurementsList(filterParametrs, new AbstractAsyncCallback<ListResult<UnitMeasurementItem>>() {
            @Override
            public void failure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void success(ListResult<UnitMeasurementItem> result) {
                callback.onSuccess(result);
            }
        });
    }

    public String getIconStyle() {
        return "accountMark ac-type-num-settings";//return "icon-settings-invoice";
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
