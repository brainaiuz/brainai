package com.edatasite.workforce.gwt.accounting.client.ui.view.inventory;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.BuildAssemblyService;
import com.edatasite.workforce.gwt.accounting.client.rpc.BuildAssemblyServiceAsync;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.AssemblyItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ExportImportOption;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.GuideListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.GuideListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BuildAssemblyItemListview extends BaseListView implements Constants, AccountingConstants, PermissionConstants {
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();
    private final BuildAssemblyServiceAsync buildAssemblyService = BuildAssemblyService.App.get();

    private ListingPanel<ProductItem> listingPanel;
    protected HashSet selectedItems = new HashSet();
    private final ActionButton deleteSelectedButton = null;

    public BuildAssemblyItemListview() {
        super(BUILD_ASSEMBLY_PRODUCTS);
        setDescription(property.getPlural(accountingStrings.buildAssembly()));
    }

    @Override
    protected Widget onInitialize() {
        listingPanel = new GuideListingPanel(ListPanelType.BuildAssembly, getColumns(), getListingRequestProvider(), getListingPanelDesign());
        listingPanel.setEnableDate();

        listingPanel.setPDFListener(clickEvent -> {
            if (listingPanel.getItemCount() > 1000) {
                Window.alert(wfmStrings.CurrentlyLimitedContactExport());
            }
            String pdfURL;
            pdfURL = CommandConstants.PDF_URL + "/productsServicesListPDFHandler";
            ListingFilterParameter listingFilterParameter = listingPanel.getFilterParametrs();
            listingFilterParameter.setPropertyCode(getPropertyCode());
            listingFilterParameter.setViewType(ViewName.BuildAssembly.name());
            listingPanel.callListPDF(pdfURL, listingFilterParameter);
        });

        listingPanel.setExcelListener(clickEvent -> {
            if (listingPanel.getItemCount() > 1000) {
                Window.alert(wfmStrings.CurrentlyLimitedContactExport());
            }
            String excelURL;
            excelURL = CommandConstants.COMMON_URL + "/downloadProductsServicesListExcel";
            ListingFilterParameter listingFilterParameter = listingPanel.getFilterParametrs();
            listingFilterParameter.setPropertyCode(getPropertyCode());
            listingFilterParameter.setViewType(ViewName.BuildAssembly.name());
            listingPanel.callListExcel(excelURL, listingFilterParameter);
        });

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SALEINVOICE_ADDED, BuildAssemblyItemListview.this, (sender, args) -> listingPanel.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PURCHASEINVOICE_ADDED, BuildAssemblyItemListview.this, (sender, args) -> listingPanel.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PRODUCTSERVICE_SAVED, BuildAssemblyItemListview.this, (sender, args) -> listingPanel.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SALESORDER_ADDED, BuildAssemblyItemListview.this, (sender, args) -> listingPanel.reloadPage());

        listingPanel.addSelectionRowHandler(selectedRows -> {
            if (!selectedRows.isEmpty()) {
                selectedItems = selectedRows;
                if (deleteSelectedButton != null) {
                    deleteSelectedButton.setVisible(true);
                }
            } else {
                if (deleteSelectedButton != null) {
                    deleteSelectedButton.setVisible(false);
                }
            }
        });
        add(listingPanel);
        return null;
    }

    private boolean hasPermissionToAdd() {
        return Utils.hasPermission(ACCOUNTING_BUILD_ASSEMBLY_ADD);
    }

    private boolean hasPermissionToDelete() {
        return Utils.hasPermission(ACCOUNTING_BUILD_ASSEMBLY_DELETE);
    }

    private GuideListingPanelDesign getListingPanelDesign() {
        return new GuideListingPanelDesign() {
            @Override
            public Command getAddNewItemCommand() {
                return hasPermissionToAdd() ? () -> SinksContainerFactory.entryPoint.onHistoryChanged("buildAssembly|add/add/") : null;
            }

            @Override
            public Command getUploadButtonCommand() {
//                return imp::open;
                return null;
            }

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
                if (hasPermissionToAdd()) {
                    ActionButton addnew = getAddNewButton();
                    addnew.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged("buildAssembly|add/add/"));
                    return addnew;
                }
                return null;
            }

            @Override
            public ActionButton initTopToolBarMore() {
                if (hasPermissionToDelete()) {
                    return getRemoveMoreButton(clickEvent -> deleteSelection());
                }
                return null;
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
//                DefaultNoItemsMessage message = new DefaultNoItemsMessage(property.getPlural(accountingStrings.messCurrentlyInventoryItems(), wfmStrings.assemblyItems()));
//                message.setHref("product|add/add/" + FROM_ASSEMBLY);
//                message.setTextBeforeLink(property.getPlural(accountingStrings.messAddingInventoryClicking(), wfmStrings.assemblyItems()));
//                emptyDataTable.initEmptyDataTable(message);
            }

            @Override
            public boolean isEditCustomFieldCell() {
                return false;
            }
        };
    }

    private ListingRequestProvider<AssemblyItem> getListingRequestProvider() {
        return (filterParametrs, callback) ->
                BuildAssemblyService.App.get().getBuildAssemblyList(filterParametrs, new AsyncCallback<ListResult<AssemblyItem>>() {
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess(ListResult<AssemblyItem> assemblyItemListResult) {
                callback.onSuccess(assemblyItemListResult);
            }
        });
    }

    private int actionItemCount;

    private ColumnDefinitionConfig[] getColumns() {
        List<ColumnDefinitionConfig> columnsList = new ArrayList<>();
        //Action
        ColumnDefinitionConfig column = new ColumnDefinitionConfig<AssemblyItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final AssemblyItem item) {
                actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);
                if (Utils.hasPermission(ACCOUNTING_BUILD_ASSEMBLY_SUMMARY)) {
                    MenuPopItem productSummary = new MenuPopItem(wfmStrings.summaryView(), "icon-puchase-invoise-small");
                    productSummary.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("buildAssembly|summary/" + item.getId(), item.getNumberData() != null ? item.getNumberData().getNumberString() : null));
                    actionItemCount++;
                    menuBar.addItem(productSummary);
                }

                if ((item.isBuilt() != null && item.isBuilt()) && hasPermissionToDelete()) {
                    MenuPopItem unBuildItem = new MenuPopItem(accountingStrings.unBuild(), "removeItemStyle-profile");
                    unBuildItem.setCommand(() -> {
                        LoadingPanel.loading(true);
                        if (Utils.isInventoryLocked() && DateUtils.getTransactionLockDate().after(item.getDate().getDate())) {
                            LoadingPanel.loading(false);
                            Info.show(accountingMessages.dateShouldBeAfterClosedBeforeDate("Built", Utils.getTransactionLockDate()), Info.Type.WARNING);
                            return;
                        }
                        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                        messageBox.setTitle(wfmStrings.warning());
                        messageBox.setMessage(wfmStrings.sureYouWantToUnbuild());
                        messageBox.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                buildAssemblyService.unBuildAssemblyItem(item.getId(), new AbstractAsyncCallback<SelectItem>() {
                                    public void failure(Throwable caught) {
                                        super.failure(caught);
                                        LoadingPanel.loading(false);
                                        Info.show(Property.get(Constants.PRODUCTS_OR_SERVICES, wfmStrings.sorrySomethingWentWrong()), Info.Type.WARNING);
                                    }

                                    public void success(SelectItem result) {
                                        if (result != null) {
                                            Info.warn(AccountingMessages.App.get().youDoNotHaveEnoughQuantity(result.getName()), 5000);
                                        } else {
                                            messageBox.close();
                                            Info.show(accountingStrings.succesfullyUnBuildAssembly(), Info.Type.INFO);
                                            listingPanel.reloadPage();
                                        }
                                        LoadingPanel.loading(false);
                                    }
                                });
                            }
                        });
                        messageBox.open();
                    });
                    menuBar.addItem(unBuildItem);
                } else if (hasPermissionToDelete()) {
                    MenuPopItem removeItem = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                    removeItem.setCommand(() -> {
                        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                        messageBox.setTitle(wfmStrings.warning());
                        messageBox.setMessage(wfmStrings.sureYouWantToDelete());
                        messageBox.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                buildAssemblyService.deleteSavedAssembly(item.getId(), new AbstractAsyncCallback<Void>() {
                                    public void failure(Throwable caught) {
                                        super.failure(caught);
                                        Info.show(Property.get(Constants.PRODUCTS_OR_SERVICES, wfmStrings.sorrySomethingWentWrong()), Info.Type.WARNING);
                                    }

                                    public void success(Void result) {
                                        Info.show(Property.get(Constants.PRODUCTS_OR_SERVICES, wfmStrings.messSuccessfulyyDeleted()), Info.Type.INFO);
                                        listingPanel.reloadPage();
                                    }
                                });
                            }
                        });
                        messageBox.open();
                    });
                    menuBar.addItem(removeItem);
                }

                final ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        column.setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        column.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        column.setColumnSortable(false);
        columnsList.add(column);

        //Number
        column = new ColumnDefinitionConfig<AssemblyItem, SimpleLink>(wfmStrings.number(), ProductItem.PRODUCT_NUMBER, 100) {
            @Override
            public SimpleLink getCellValue(final AssemblyItem item) {
                SimpleLink label = new SimpleLink(item.getNumberData() != null && item.getNumberData().getNumberString() != null ? item.getNumberData().getNumberString() : wfmStrings.na());
                // todo add "you don't have permission" warn
                if (Utils.hasPermission(ACCOUNTING_BUILD_ASSEMBLY_SUMMARY)) {
                    label.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("buildAssembly|summary/" + item.getId()));
                }
                return label;
            }
        };
        column.setMinimumColumnWidth(100);
        column.setColumnSortable(true);
        column.setShow(true);
        columnsList.add(column);

        //Assembly Item Name
        column = new ColumnDefinitionConfig<AssemblyItem, Widget>(wfmStrings.name(), ProductItem.NAME, 150) {
            @Override
            public Widget getCellValue(final AssemblyItem item) {
                StringBuilder str = new StringBuilder();
                str.append(item.getAssemblyItem() != null ? item.getAssemblyItem().getName() : wfmStrings.na());
                HTML label = new HTML(str.toString());
//                label.setStyleName("uploadLinkStyle2");
//                label.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("product|summary/" + item.getAssemblyItemId() + "/" + ASSEMBLY_ITEM_STR + "/" + (item.getWarehouseId())));
                return label;
            }
        };
        column.setMinimumColumnWidth(150);
        column.setColumnSortable(true);
        column.setShow(true);
        columnsList.add(column);

        column = new ColumnDefinitionConfig<AssemblyItem, String>(wfmStrings.date(), Constants.DATE, 110) {
            @Override
            public String getCellValue(AssemblyItem item) {
                return item.getDate() != null ? DateUtils.format(item.getDate().getDate()) : wfmStrings.na();
            }
        };
        column.setMinimumColumnWidth(100);
        column.setColumnSortable(true);
        column.setShow(true);
        columnsList.add(column);

        column = new ColumnDefinitionConfig<AssemblyItem, String>(wfmStrings.createdBy(), ProductItem.CREATOR, 150) {
            @Override
            public String getCellValue(AssemblyItem item) {
                return item.getCreator() != null ? item.getCreator().getName() : wfmStrings.na();
            }
        };
        column.setMinimumColumnWidth(100);
        column.setColumnSortable(true);
        column.setShow(true);
        columnsList.add(column);

        column = new ColumnDefinitionConfig<AssemblyItem, String>(wfmStrings.createdDate(), ProductItem.CREATED_DATE, 110) {
            @Override
            public String getCellValue(AssemblyItem item) {
                return item.getCreatedDate() != null ? DateUtils.format(item.getCreatedDate().getDate()) : wfmStrings.na();
            }
        };
        column.setMinimumColumnWidth(100);
        column.setColumnSortable(true);
        column.setShow(true);
        columnsList.add(column);

//        column = new ColumnDefinitionConfig<AssemblyItem, String>(wfmStrings.modifiedBy(), ProductItem.UPDATER, 150) {
//            @Override
//            public String getCellValue(AssemblyItem item) {
//                return item.getUpdater() != null ? item.getUpdater().getName() : wfmStrings.na();
//            }
//        };
//        column.setMinimumColumnWidth(100);
//        column.setColumnSortable(true);
//        column.setShow(true);
//        columnsList.add(column);

//        column = new ColumnDefinitionConfig<AssemblyItem, String>(wfmStrings.modifiedDate(), ProductItem.UPDATED_DATE, 110) {
//            @Override
//            public String getCellValue(AssemblyItem item) {
//                return item.getUpdatedDate() != null ? DateUtils.format(item.getUpdatedDate().getDate()) : wfmStrings.na();
//            }
//        };
//        column.setMinimumColumnWidth(100);
//        column.setColumnSortable(true);
//        column.setShow(true);
//        columnsList.add(column);

        column = new ColumnDefinitionConfig<AssemblyItem, String>(wfmStrings.status(), ProductItem.STATUS, 150) {
            @Override
            public String getCellValue(AssemblyItem item) {
                return item.getStatus() != null ? item.getStatus().getName() : wfmStrings.na();
            }
        };
        column.setMinimumColumnWidth(100);
        column.setColumnSortable(true);
        column.setShow(true);
        columnsList.add(column);

        return columnsList.toArray(new ColumnDefinitionConfig[0]);
    }

    private void deleteSelection() {
        if (selectedItems.isEmpty()) {
            Info.show(accountingMessages.pleaseSelectOneRow(property.getPlural(wfmStrings.assemblyItems())), Info.Type.WARNING);
        } else {
            showDeleteMessage();
        }
    }

    private void showDeleteMessage() {
        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
        messageBox.setTitle(wfmStrings.warning());
        String message = wfmStrings.areYouSureYouWantToDeleteTheSelectedRecords();

        ArrayList<Integer> ids = getIDsOnly(selectedItems);
        if (!ids.isEmpty()) {
            Info.show(accountingStrings.youCannotDeleteBuildAssembly(), Info.Type.WARNING);
            return;
        }

        messageBox.setMessage(message);
        messageBox.addCloseHandler(new CloseHandler() {
            @Override
            public void onSubmit() {
                if (!ids.isEmpty()) {
                    LoadingPanel.loading(true);
                    buildAssemblyService.deleteSelectedSavedAssemblyList(ids, new AbstractAsyncCallback<Void>() {
                        @Override
                        public void failure(Throwable throwable) {
                            LoadingPanel.loading(false);
                            Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                        }

                        @Override
                        public void success(Void result) {
                            LoadingPanel.loading(false);
                            listingPanel.reloadPage();
                            Info.show(property.getPlural(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.assemblyItems()), Info.Type.INFO);
                        }
                    });
                }
            }
        });
        messageBox.open();
    }

    private static ArrayList<Integer> getIDsOnly(Set<ProductItem> selectedItems) {
        ArrayList<Integer> ids = new ArrayList<>();
        for (ProductItem item : selectedItems) {
            if (item.isBuilt()) {
                break;
            }
            ids.add(item.getObjectId());
        }
        return ids;
    }

    @Override
    public String getIconStyle() {
        return "accountMark purchase-order-list";
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
    public String getPropertyCode() {
        return Constants.BUILD_ASSEMBLY_PRODUCTS;
    }
}
