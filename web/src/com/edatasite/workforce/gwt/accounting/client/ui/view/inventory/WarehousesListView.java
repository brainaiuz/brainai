package com.edatasite.workforce.gwt.accounting.client.ui.view.inventory;

import com.edatasite.workforce.gwt.accounting.client.bundles.AccountingWelcomeImageBundles;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.WarehouseItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityService;
import com.edatasite.workforce.gwt.availability.client.rpc.LeaveRequestLisItem;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
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
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.PDFTemplateSelector;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.gen2.table.client.SelectionGrid;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;

import java.util.HashMap;
import java.util.stream.Collectors;

public class WarehousesListView extends BaseListView implements Constants {
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private ListingPanel<WarehouseItem> list;
    private final boolean hasPermissionToAdd = Utils.hasPermission(PermissionConstants.ACCOUNTING_WAREHOUSES_ADD);
    private final boolean hasPermissionToEdit = Utils.hasPermission(PermissionConstants.ACCOUNTING_WAREHOUSES_EDIT);
    private final boolean hasPermissionToDelete = Utils.hasPermission(PermissionConstants.ACCOUNTING_WAREHOUSES_DELETE);
    private final boolean hasPermissionToExport = Utils.hasPermission(PermissionConstants.ACCOUNTING_WAREHOUSES_EXPORT);
    private final boolean hasPermissionToSummary = Utils.hasPermission(PermissionConstants.ACCOUNTING_WAREHOUSES_SUMMARY);
    private final boolean hasPermissionToProductsList = Utils.hasPermission(PermissionConstants.ACCOUNTING_PRODUCT_LIST);

    public WarehousesListView() {
        super(AccountingConstants.WAREHOUSE_LIST);
        setDescription(property.getSingular(accountingStrings.warehouses()));
        if (hasPermissionToAdd) {
            setAddNew("warehouse|add/add");
        }
    }
    protected Widget onInitialize() {
        list = new GuideListingPanel(ListPanelType.WarehousesPanel, getColumnConfigs(), getListData(), getDisagn(), SelectionGrid.SelectionPolicy.CHECKBOX);
        list.setCustomFieldsEditCellSaveChanges((rowValue, columnCodeName) -> saveWEditCellValue((WarehouseItem) rowValue, columnCodeName));

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_WAREHOUSE_SAVED, WarehousesListView.this, (sender, args) -> list.reloadPage());

        //Action--> download as .PDF
        list.setPDFListener(clickEvent -> {
            String pdfURL = CommandConstants.PDF_URL + "/wareHousesListPDFHandler";
            list.callListPDF(pdfURL, list.getFilterParametrs());
        });

        //Action--> download as .XLS
        list.setExcelListener(clickEvent -> {
            String excelHandler = CommandConstants.COMMON_URL + "/downloadWarehousesListExcel";
            list.callListExcel(excelHandler, list.getFilterParametrs());
        });
        add(list);
        return null;
    }
    private ListingRequestProvider<WarehouseItem> getListData() {
        return (filterParametrs, callback) -> {

            filterParametrs.setViewType(AccountingConstants.WAREHOUSE_LIST);
            AccountingService.App.get().getWarehousesList(filterParametrs, new AsyncCallback<ListResult<WarehouseItem>>() {
                @Override
                public void onFailure(Throwable throwable) {
                    callback.onFailure(throwable);
                }
                @Override
                public void onSuccess(ListResult<WarehouseItem> warehouseItemListResult) {
                    callback.onSuccess(warehouseItemListResult);
                }
            });
        };
    }

    private GuideListingPanelDesign getDisagn() {
        return new GuideListingPanelDesign() {
            @Override
            public Command getAddNewItemCommand() {
                return null;
            }

            @Override
            public Command getUploadButtonCommand() {
                return null;
            }

            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
            }

            @Override
            public ActionButton initTopToolBarNew() {
                ActionButton addNewWarehouse = null;
                if (hasPermissionToAdd) {
                    addNewWarehouse = getAddNewButton();
                    addNewWarehouse.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged("warehouse|add/add"));
                }
                return addNewWarehouse;
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
                exportOption.initExport(null, hasPermissionToExport);
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                String text = wfmStrings.noDataAvailableInTableList();
                DefaultNoItemsMessage message;
                message = new DefaultNoItemsMessage(text);
                message.setTextBeforeLink(wfmStrings.youCanStartAddingItemByClick());
                message.setHref("warehouse" + "|add/add");
                emptyDataTable.initEmptyDataTable(message);
            }

            @Override
            public boolean isEditCustomFieldCell() {
                return true;
            }
        };
    }

    //List columns, configuration
    private ColumnDefinitionConfig[] getColumnConfigs() {
        final ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[5];

        //Action
        columns[0] = new ColumnDefinitionConfig<WarehouseItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, 70) {
            @Override
            public Anchor getCellValue(final WarehouseItem item) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);

                //Summary
                if (hasPermissionToSummary) {
                    MenuPopItem viewMenuPopItem = new MenuPopItem(wfmStrings.summaryView(), "icon-warehouse-view");
                    viewMenuPopItem.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("warehouse|summary/" + item.getObjectID(), item.getName()));
                    actionItemCount++;
                    menuBar.addItem(viewMenuPopItem);
                }

                //PDF Version from Action menu
                if (Utils.hasPermission(PermissionConstants.ACCOUNTING_WAREHOUSES_EXPORT)) {
                    MenuPopItem pdfVersion = new MenuPopItem(wfmStrings.pdf(), "icon-pdf-profile");
                    pdfVersion.ensureDebugId("exportToPDF");
                    pdfVersion.setCommand(() -> new PDFTemplateSelector(Constants.WAREHOUSE_ID, new ExtendedCommand() {
                        @Override
                        public void execute(Integer id) {

                            RequestObject  requestObject = new RequestObject(item.getObjectID()); //employee id yoq
                            String pdfUrl = CommandConstants.PDF_URL + "/warehouseProductsViewPDFHandler";
                            HashMap<String, String> requestParams = requestObject.getRequestParams();
                            list.callListPDF(pdfUrl, requestParams);
                        }
                    }));
                    actionItemCount++;
                    menuBar.addItem(pdfVersion);
                }

                //Edit
                if (hasPermissionToEdit) {
                    MenuPopItem warehouseEdit = new MenuPopItem(wfmStrings.edit(), "icon-edit");
                    warehouseEdit.getElement().setId("warehouse_setting_edit_id");
                    warehouseEdit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("warehouse|edit/" + item.getObjectID(), item.getName()));
                    actionItemCount++;
                    menuBar.addItem(warehouseEdit);
                }
                //Related Products for the selected warehouse
                if (hasPermissionToProductsList) {
                    MenuPopItem productsMenuPopItem = new MenuPopItem(wfmStrings.products(), "icon-warehouse-product-list");
                    productsMenuPopItem.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("warehouseproductlist|summary/" + item.getObjectID(), wfmStrings.products() + "-" + item.getName()));
                    actionItemCount++;
                    menuBar.addItem(productsMenuPopItem);
                }
                //Delete
                if (hasPermissionToDelete) {
                    MenuPopItem deleteMenuPopItem = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                    deleteMenuPopItem.setCommand(() -> {
                        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                        messageBox.setTitle(wfmStrings.deleting());
                        messageBox.setMessage(accountingStrings.wantToDelete() + "?");
                        messageBox.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                AccountingService.App.get().deleteWarehouse(item.getObjectID(), new AbstractAsyncCallback<Boolean>() {

                                    public void failure(Throwable caught) {
                                        Info.show(wfmStrings.errorOccurred(), Info.Type.WARNING);
                                    }
                                    public void success(Boolean deleted) {
                                        if (deleted) {
                                            Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), accountingStrings.warehouse()), Info.Type.INFO);
                                            list.reloadPage();
                                        } else {
                                            Info.show(wfmStrings.errorOccurred(), Info.Type.WARNING);
                                        }
                                    }
                                });
                            }
                        });
                        messageBox.open();
                    });

                    menuBar.addItem(deleteMenuPopItem);
                }

                ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);

                return toolItem.getAction();
            }
        };
        columns[0].setMinimumColumnWidth(70);
        columns[0].setMaximumColumnWidth(80);
        columns[0].setShow(true);
        columns[0].setColumnSortable(false); //disabled for ACTION

        //Number
        columns[1] = new ColumnDefinitionConfig<WarehouseItem, String>(wfmStrings.number(), WarehouseItem.WAREHOUSE_CODE, 60) {

            @Override
            public String getCellValue(WarehouseItem item) {
                return item.getObjectID() != null ? String.valueOf(item.getObjectID()) : "";

            }
        };
        columns[1].setMinimumColumnWidth(60);
        columns[1].setMaximumColumnWidth(70);
        columns[1].setShow(true);
        columns[1].setColumnSortable(true);

        //Warehouse Name
        columns[2] = new ColumnDefinitionConfig<WarehouseItem, SimpleLink>(wfmStrings.name(), WarehouseItem.NAME, 150) {
            @Override
            public SimpleLink getCellValue(WarehouseItem item) {
                SimpleLink label = new SimpleLink(item.getName() != null ? item.getName() : "");
                if (item.getName() != null) {
                    label.addClickHandler(clickEvent -> {
                        if (hasPermissionToSummary) {
                            SinksContainerFactory.entryPoint.onHistoryChanged("warehousesummary|summary/" + item.getObjectID(), item.getName());
                        } else {
                            Info.show(wfmStrings.youDontHavePermission(), Info.Type.WARNING);
                        }
                    });
                }
                return label;

            }
        };
        columns[2].setMinimumColumnWidth(150);
        columns[2].setMaximumColumnWidth(200);
        columns[2].setShow(true);
        columns[2].setColumnSortable(true);

        //Assignee
        columns[3] = new ColumnDefinitionConfig<WarehouseItem, String>(wfmStrings.assignee(), WarehouseItem.ASSIGNEE, 150) {

            @Override
            public String getCellValue(WarehouseItem item) {
                return item.getSelectedOwners() != null ? item.getSelectedOwners().stream().map(SelectItem::getName).collect(Collectors.joining(", ")) : "";
            }
        };
        columns[3].setMinimumColumnWidth(150);
        columns[3].setMaximumColumnWidth(200);
        columns[3].setShow(true);
        columns[3].setColumnSortable(false);

        //Description
        columns[4] = new ColumnDefinitionConfig<WarehouseItem, String>(wfmStrings.description(), WarehouseItem.NOTES, 150) {

            @Override
            public String getCellValue(WarehouseItem item) {
                return item.getNotes() != null ? item.getNotes() : "";
            }
        };
        columns[4].setMinimumColumnWidth(150);
        columns[4].setMaximumColumnWidth(250);
        columns[4].setShow(false);
        columns[4].setColumnSortable(false);

        return columns;
    }

    public String getIconStyle() {
        return null;
    }

    public ImageResource getIconImage() {
        return AccountingWelcomeImageBundles.App.get().warehouse();
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

    @Override
    public String getPropertyCode() {
        return AccountingConstants.WAREHOUSE_LIST;
    }

    private void saveWEditCellValue(WarehouseItem rowValue, String columnCodeName) {
        AccountingService.App.get().saveWEditCellValue(rowValue, columnCodeName, new AbstractAsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable caught) {
                super.onFailure(caught);
            }

            @Override
            public void onSuccess(Boolean result) {
                list.reloadPage();
            }
        });
    }

}