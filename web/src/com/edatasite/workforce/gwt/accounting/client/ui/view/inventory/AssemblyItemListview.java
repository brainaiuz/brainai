package com.edatasite.workforce.gwt.accounting.client.ui.view.inventory;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductService;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductServiceAsync;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.client.ui.view.ProductUnBuildView;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.RbacService;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFieldConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrProductServiceRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.components.ImportFileActionLink;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listTable.ImportFilePopUp;
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
import com.edatasite.workforce.gwt.core.client.ui.view.WarehouseLookUp;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.gen2.table.client.SelectionGrid;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AssemblyItemListview extends BaseListView implements Constants, AccountingConstants, PermissionConstants {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();

    private final ProductServiceAsync productService = ProductService.App.get();
    private final ImportFilePopUp imp = new ImportFilePopUp(ImportTypeEnum.ASSEMBLY_ITEMS, null);

    private ListingPanel<ProductItem> listingPanel;
    private WarehouseLookUp warehouseLookUp;
    protected HashSet<ProductItem> selectedItems = new HashSet<>();
    private final ActionButton deleteSelectedButton = null;

    public AssemblyItemListview() {
        super(ASSEMBLY_PRODUCTS);
        setDescription(property.getPlural(wfmStrings.assemblyItems()));
        if (hasPermissionToAdd()) {
            setAddNew(() -> SinksContainerFactory.entryPoint.onHistoryChanged("product|add/add/" + FROM_ASSEMBLY));
        }
    }

    @Override
    protected Widget onInitialize() {
        if (!Utils.hasRole(CLIENT)) {
            listingPanel = new GuideListingPanel(ListPanelType.AssemblyItemsListPanel, getColumns(), getListingRequestProvider(), getListingPanelDesign(), SelectionGrid.SelectionPolicy.CHECKBOX);
        } else {
            listingPanel = new GuideListingPanel(ListPanelType.AssemblyItemsListPanel, getColumns(), getListingRequestProvider(), getListingPanelDesign());
        }
        listingPanel.setEnableDate();

        if (Utils.isMultiWarehouseEnabled()) {
            warehouseLookUp = new WarehouseLookUp();
            warehouseLookUp.getSuggestBox().setWidth("200px");
            warehouseLookUp.setStyleName("showAllCheckBox");
            warehouseLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> listingPanel.reloadPage());
            listingPanel.getAdvancedFilterPanel().add(warehouseLookUp);
            listingPanel.getAdvancedFilterPanel().setVisible(true);
        }

        listingPanel.setOnReset(() -> {
            if (warehouseLookUp != null) {
                warehouseLookUp.clear();
            }
        });

        listingPanel.setCustomFieldsEditCellSaveChanges((rowValue, columnCodeName) -> saveProductItemCellValue((ProductItem) rowValue, columnCodeName));

        listingPanel.setPDFListener(clickEvent -> {
            if (listingPanel.getItemCount() > 1000) {
                Window.alert(wfmStrings.CurrentlyLimitedContactExport());
            }
            String pdfURL;
            pdfURL = CommandConstants.PDF_URL + "/productsServicesListPDFHandler";
            ListingFilterParameter listingFilterParameter = listingPanel.getFilterParametrs();
            listingFilterParameter.setPropertyCode(getPropertyCode());
            listingFilterParameter.setViewType(ViewName.AssemblyItemsView.name());
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
            listingFilterParameter.setViewType(ViewName.AssemblyItemsView.name());
            listingPanel.callListExcel(excelURL, listingFilterParameter);
        });

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SALEINVOICE_ADDED, AssemblyItemListview.this, (sender, args) -> listingPanel.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PURCHASEINVOICE_ADDED, AssemblyItemListview.this, (sender, args) -> listingPanel.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PRODUCTSERVICE_SAVED, AssemblyItemListview.this, (sender, args) -> listingPanel.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SALESORDER_ADDED, AssemblyItemListview.this, (sender, args) -> listingPanel.reloadPage());
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

    private void saveProductItemCellValue(ProductItem rowValue, String columnCodeName) {
        ProductService.App.get().saveProductItemCellValue(rowValue, columnCodeName, new AbstractAsyncCallback<Boolean>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
            }

            @Override
            public void success(Boolean result) {
                super.success(result);
                listingPanel.reloadPage();
            }
        });
    }

    private boolean hasPermissionToAdd() {
        return Utils.hasPermission(ACCOUNTING_ASSEMBLY_ITEM_ADD);
    }

    private GuideListingPanelDesign getListingPanelDesign() {
        return new GuideListingPanelDesign() {
            @Override
            public Command getAddNewItemCommand() {
                return hasPermissionToAdd() ? () -> SinksContainerFactory.entryPoint.onHistoryChanged("product|add/add/" + FROM_ASSEMBLY) : null;
            }

            @Override
            public Command getUploadButtonCommand() {
                return imp::open;
            }

            @Override
            public ListingFacetFilter initFacetFilter() {
                return new ListingFacetFilter() {
                    @Override
                    public FacetCallbackProvider getFacetCallbackProvider() {
                        return (data, callback) -> {
                            data.getCustomData().put(PRODUCT_TYPE, ASSEMBLY_ITEM.toString());
                            if (warehouseLookUp != null && warehouseLookUp.getSelectedItemID() != null) {
                                data.getCustomData().put(WAREHOUSE_ID, warehouseLookUp.getSelectedItemID().toString());
                            }
                            RbacService.App.get().getProductsServicesFacetFilterData(data, new AbstractAsyncCallback<FacetFilterRpc>() {// add product type parameter to solr query
                                public void failure(Throwable caught) {
                                    super.failure(caught);
                                    callback.onFailure(caught);
                                }

                                public void success(FacetFilterRpc data) {
                                    super.success(data);
                                    callback.onSuccess(data);
                                }
                            });
                        };
                    }

                    @Override
                    public FacetContentConfigure getFacetFilterContentconfigure() {
                        return getFacetContentConfigure();
                    }
                };
            }

            @Override
            public ActionButton initTopToolBarNew() {
                if (hasPermissionToAdd()) {
                    ActionButton newItem = getAddNewButton(ActionButton.Type.TOOLMENU);
                    newItem.addClickHandler((clickEvent) -> SinksContainerFactory.entryPoint.onHistoryChanged("product|add/add/" + FROM_ASSEMBLY));
                    return newItem;
                }
                return null;
            }

            @Override
            public ActionButton initTopToolBarMore() {
                if (Utils.hasPermission(ACCOUNTING_ASSEMBLY_ITEM_DELETE)) {
                    return getRemoveMoreButton(clickEvent -> deleteSelection());
                }
                return null;
            }

            @Override
            public HorizontalPanel initTopToolBarWidgets() {
                return null;
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
                imp.setSubmitCompleted(() -> {
                    if (imp.getObjectId() != null) {
                        goTo("importproducts|add/add/" + imp.getObjectId() + "/" + FROM_CSV + "/" + ASSEMBLY_PRODUCTS);
                    }
                });
                ImportFileActionLink csvLink = new ImportFileActionLink();
                csvLink.setText(wfmStrings.csv());
                csvLink.addClickHandler(ch -> imp.open());
                menuContainer.add(csvLink);

                exportOption.initExport(null, true);
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(property.getPlural(accountingStrings.messCurrentlyInventoryItems(), wfmStrings.assemblyItems()));
                message.setHref("product|add/add/" + FROM_ASSEMBLY);
                message.setTextBeforeLink(property.getPlural(accountingStrings.messAddingInventoryClicking(), wfmStrings.assemblyItems()));
                emptyDataTable.initEmptyDataTable(message);
            }

            @Override
            public boolean isEditCustomFieldCell() {
                return Utils.hasPermission(ACCOUNTING_ASSEMBLY_ITEM_EDIT);
            }
        };
    }

    private FacetContentConfigure getFacetContentConfigure() {
        FacetContentConfigure contentConfigure = new FacetContentConfigure(3, wfmStrings.filter());
        contentConfigure.addContentConfigure(FacetContentType.ProductsServicesFacetFilter.getContentCode()[0], wfmStrings.account(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrProductServiceRepresenter.FIELD_ACCOUNT_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrProductServiceRepresenter.FIELD_ACCOUNT_ID_NAME;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }

        });

        contentConfigure.addContentConfigure(FacetContentType.ProductsServicesFacetFilter.getContentCode()[4], Property.get(Constants.SUPPLIER_LIST, wfmStrings.supplier()), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrProductServiceRepresenter.FIELD_MULTI_SUPPLIER_NAME;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrProductServiceRepresenter.FIELD_MULTI_SUPPLIER_NAME;
            }

            @Override
            public boolean isConditionItemId() {
                return false;
            }
        });

        contentConfigure.addContentConfigure(FacetContentType.ProductsServicesFacetFilter.getContentCode()[3], wfmStrings.category(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrProductServiceRepresenter.FIELD_CATEGORY;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrProductServiceRepresenter.FIELD_CATEGORY;
            }

            @Override
            public boolean isConditionItemId() {
                return false;
            }
        });

        contentConfigure.addContentConfigure(FacetContentType.ProductsServicesFacetFilter.getContentCode()[1], wfmStrings.type(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrProductServiceRepresenter.FIELD_PRODUCT_TYPE_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrProductServiceRepresenter.FIELD_PRODUCT_TYPE_ID_NAME;
            }
        });
        contentConfigure.addContentConfigure(FacetContentType.ProductsServicesFacetFilter.getContentCode()[2], wfmStrings.unitPrice(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrProductServiceRepresenter.FIELD_UNITPRICE;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrProductServiceRepresenter.FIELD_UNITPRICE;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }

            @Override
            public boolean isConditionItemId() {
                return false;
            }
        });

        contentConfigure.addContentConfigure(FacetContentType.ProductsServicesFacetFilter.getContentCode()[8], wfmStrings.status(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrProductServiceRepresenter.FIELD_PRODUCT_ACTIVE;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrProductServiceRepresenter.FIELD_PRODUCT_ACTIVE;
            }
        });


        return contentConfigure;
    }

    private ListingRequestProvider<ProductItem> getListingRequestProvider() {
        return (filterParametrs, callback) -> {
            filterParametrs.setProductType(ASSEMBLY_ITEM);
            if (warehouseLookUp != null) {
                filterParametrs.setWarehouseID(warehouseLookUp.getSelectedItemID());
            }
            ProductService.App.get().getProductsListFromSolr(filterParametrs, new AsyncCallback<ListResult<ProductItem>>() {
                public void onFailure(Throwable caught) {
                    callback.onFailure(caught);
                }

                public void onSuccess(ListResult<ProductItem> list) {
                    AccountingUtils.loadData();
                    callback.onSuccess(list);
                }
            });
        };
    }

    private int actionItemCount;

    private ColumnDefinitionConfig[] getColumns() {
        List<ColumnDefinitionConfig> columnsList = new ArrayList<>();
        //Action
        ColumnDefinitionConfig column = new ColumnDefinitionConfig<ProductItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final ProductItem item) {
                actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);
                if (Utils.hasPermission(ACCOUNTING_ASSEMBLY_ITEM_SUMMARY) && item.isActive()) {
                    MenuPopItem productSummary = new MenuPopItem(wfmStrings.summaryView(), "icon-puchase-invoise-small");
                    productSummary.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("product|summary/" + item.getObjectId() + "/" + item.getTypeName() + "/" + ((warehouseLookUp != null && warehouseLookUp.getSelectedItemID() != null) ? warehouseLookUp.getSelectedItemID() : item.getWarehouseId()), item.getProductNumber(), item.getName()));
                    actionItemCount++;
                    menuBar.addItem(productSummary);
                }

                if (Utils.hasPermission(ACCOUNTING_ASSEMBLY_ITEM_EDIT) && Utils.hasPermission(PermissionConstants.ACCOUNTING_PRODUCT_COST)) {
                    MenuPopItem productEdit = new MenuPopItem(wfmStrings.edit(), "icon-employee-edit-profile");
                    productEdit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("product|productadd/" + item.getObjectId(), item.getProductNumber(), item.getName()));
                    actionItemCount++;
                    menuBar.addItem(productEdit);
                }

                if (Utils.hasPermission(ACCOUNTING_ASSEMBLY_ITEM_ADD)) {
                    MenuPopItem copyTo = new MenuPopItem(wfmStrings.copy(), "icon-copy");
                    copyTo.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("product|add/add/" + COPY_FROM_EXISTING + "/" + FROM_ASSEMBLY + "/" + item.getObjectId()));
                    actionItemCount++;
                    menuBar.addItem(copyTo);
                }



                if (Utils.hasPermission(ACCOUNTING_ASSEMBLY_ITEM_DELETE) && item.isActive()) {
                    if (item.getTypeName().equals(ASSEMBLY_ITEM_STR) && (item.isBuilt() != null && item.isBuilt())) {
                        MenuPopItem unBuildItem = new MenuPopItem(accountingStrings.unBuild(), "removeItemStyle-profile");
                        unBuildItem.setCommand(() -> new ProductUnBuildView(item.getObjectId()));
                        menuBar.addItem(unBuildItem);
                    }

                    MenuPopItem removeItem = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                    removeItem.setCommand(() -> {
                        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                        messageBox.setTitle(wfmStrings.warning());
                        messageBox.setMessage(wfmStrings.sureYouWantToDelete());
                        messageBox.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                productService.deleteProduct(item.getObjectId(), new AbstractAsyncCallback<Boolean>() {

                                    public void failure(Throwable caught) {
                                        super.failure(caught);
                                        Info.show(Property.get(Constants.PRODUCTS_OR_SERVICES, accountingStrings.errorDeletingProduct(), accountingStrings.productOrService()), Info.Type.WARNING);
                                    }

                                    public void success(Boolean deleted) {
                                        if (deleted) {
                                            Info.show(Property.get(Constants.PRODUCTS_OR_SERVICES, wfmStrings.messSuccessfulyyDeleted(), accountingStrings.productOrService()), Info.Type.INFO);
                                            listingPanel.reloadPage();
                                        } else {
                                            VerticalPanel msg = new VerticalPanel();
                                            msg.add(new HTML(Property.get(Constants.PRODUCTS_OR_SERVICES, accountingStrings.errorDeletingProduct(), accountingStrings.productOrService())));
                                            msg.add(new HTML("&nbsp"));
                                            msg.add(new HTML(Property.getPluralWithObjectCodeWithReplace(Constants.PRODUCTS_OR_SERVICES, accountingStrings.msgForDeleteProductServices(), wfmStrings.productsOrServices())));
                                            WfmMessageBox inActiveMessageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                                            inActiveMessageBox.setTitle(wfmStrings.confirmationMessage());
                                            inActiveMessageBox.setMessage(msg.toString());
                                            inActiveMessageBox.setWidth(515);
                                            inActiveMessageBox.addCloseHandler(new CloseHandler() {
                                                @Override
                                                public void onSubmit() {
                                                    productService.inActiveProduct(item.getObjectId(), new AbstractAsyncCallback<Boolean>() {
                                                        @Override
                                                        public void failure(Throwable throwable) {
                                                            super.failure(throwable);
                                                            Info.show(accountingStrings.inActiveProductErrorMessage(), Info.Type.WARNING);
                                                        }

                                                        @Override
                                                        public void success(Boolean result) {
                                                            super.success(result);
                                                            Info.show(accountingStrings.inActiveProductMessage(), Info.Type.INFO);
                                                            listingPanel.reloadPage();
                                                        }
                                                    });
                                                }
                                            });
                                            inActiveMessageBox.open();
                                        }
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
        column = new ColumnDefinitionConfig<ProductItem, SimpleLink>(wfmStrings.number(), ProductItem.PRODUCT_NUMBER, 150) {
            @Override
            public SimpleLink getCellValue(final ProductItem item) {
                SimpleLink label = new SimpleLink(item.getProductNumber());
                if (item.isActive()) {
                    label.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("product|summary/" + item.getObjectId() + "/" + item.getTypeName() + "/" + ((warehouseLookUp != null && warehouseLookUp.getSelectedItemID() != null) ? warehouseLookUp.getSelectedItemID() : item.getWarehouseId()), item.getProductNumber(), item.getName()));
                }
                return label;
            }
        };
        columnsList.add(column);
        //Name
        column = new ColumnDefinitionConfig<ProductItem, Widget>(wfmStrings.name(), ProductItem.NAME, 150) {

            @Override
            public Widget getCellValue(final ProductItem item) {
                StringBuilder str = new StringBuilder();
                if (!item.isActive()) {
                    str.append("<b style='margin:0 5px'>X</b>");
                }
                str.append(item.getName());
                HTML label = new HTML(str.toString());
                if (item.isActive()) {
                    label.setStyleName("uploadLinkStyle2");
                    label.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("product|summary/" + item.getObjectId() + "/" + item.getTypeName() + "/" + ((warehouseLookUp != null && warehouseLookUp.getSelectedItemID() != null) ? warehouseLookUp.getSelectedItemID() : item.getWarehouseId()), item.getProductNumber(), item.getName()));
                }
                return label;
            }
        };
        column.addStyleAttribute("padding-left", "5px");
        columnsList.add(column);

        if (Utils.hasPermission(PermissionConstants.ACCOUNTING_PRODUCT_SELLING)) {
            //Sale Price
            column = new ColumnDefinitionConfig<ProductItem, String>(accountingStrings.salePrice(), ProductItem.SELING_PRICE, 100) {

                @Override
                public String getCellValue(ProductItem item) {
                    return item.getUnitpPrice() != null ? AccountingUtils.get().formatUnitPrice(item.getUnitpPrice()) : "";
                }
            };
            column.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
            column.addStyleAttribute("padding-right", "5px");
            columnsList.add(column);
        }
        //Cost Price
        if (Utils.hasPermission(PermissionConstants.ACCOUNTING_PRODUCT_COST)) {
            column = new ColumnDefinitionConfig<ProductItem, String>(wfmStrings.costPrice(), ProductItem.COST_PRICE, 100) {

                @Override
                public String getCellValue(ProductItem item) {
                    return item.getCostPrice() != null ? AccountingUtils.get().formatUnitPrice(item.getCostPrice()) : "";
                }
            };
            column.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
            column.addStyleAttribute("padding-right", "5px");
            columnsList.add(column);
        }

        if (Utils.hasPermission(PermissionConstants.ACCOUNTING_PRODUCT_AVARAGE_COST)) {
            column = new ColumnDefinitionConfig<ProductItem, String>(accountingStrings.averageCost() + " ", ProductItem.AVERAGE_COST + "_", 100) {

                @Override
                public String getCellValue(ProductItem item) {
                    return item.getAverageCost() != null ? AccountingUtils.get().formatUnitPrice(item.getAverageCost()) : "N/A";
                }
            };
            column.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
            column.addStyleAttribute("padding-right", "5px");
            column.setShow(false);
            columnsList.add(column);
        }

        //On Hand
        column = new ColumnDefinitionConfig<ProductItem, Widget>(accountingStrings.onHand(), ProductItem.ITEMS_IN_STOCK, 100) {

            @Override
            public Widget getCellValue(final ProductItem item) {
                SimpleLink a = new SimpleLink(item.getItemsInStock().compareTo(BigDecimal.ZERO) < 0 ? "(" + AccountingUtils.get().formatQty(new BigDecimal(-1).multiply(item.getItemsInStock())) + ")" : AccountingUtils.get().formatQty(item.getItemsInStock()));
                a.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("clickedreport|stockValuation/" + item.getObjectId()));
                return a;
            }
        };
        column.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
        column.addStyleAttribute("padding-right", "5px");
        columnsList.add(column);


        //Part number
        column = new ColumnDefinitionConfig<ProductItem, String>(wfmStrings.partNumber(), ProductItem.PART_NUMBER, 150) {
            @Override
            public String getCellValue(ProductItem item) {
                return item.getPartNumber() != null ? item.getPartNumber() : "";
            }
        };
        column.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
        column.addStyleAttribute("padding-right", "5px");
        column.setColumnSortable(false);
        column.setShow(false);
        columnsList.add(column);

        //Avalibility in Stock
        column = new ColumnDefinitionConfig<ProductItem, String>(accountingStrings.availableStock(), ProductItem.AVAILABLE_STOCK, 110) {
            @Override
            public String getCellValue(ProductItem item) {
                BigDecimal onHand = item.getItemsInStock().compareTo(BigDecimal.ZERO) < 0 ? new BigDecimal(-1).multiply(item.getItemsInStock()) : item.getItemsInStock();
                BigDecimal order = item.getOnSaleOrderQty();

                if (onHand.compareTo(order) > 0) {
                    return AccountingUtils.get().formatQty(onHand.subtract(order));
                }
                return String.valueOf(0d);
            }
        };
        column.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
        column.addStyleAttribute("padding-right", "5px");
        column.setColumnSortable(false);
        column.setShow(false);
        columnsList.add(column);

        //On Supplier
        column = new ColumnDefinitionConfig<ProductItem, String>(Property.get(Constants.SUPPLIER_LIST, wfmStrings.supplier()), ProductItem.Vendor, 150) {
            @Override
            public String getCellValue(ProductItem item) {
                return item.getVendor() != null ? item.getVendor() : "";
            }
        };
        column.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
        column.addStyleAttribute("padding-right", "5px");
        column.setColumnSortable(false);
        column.setShow(false);
        columnsList.add(column);

        column = new ColumnDefinitionConfig<ProductItem, String>(wfmStrings.status(), ProductItem.STATUS, 100) {
            @Override
            public String getCellValue(ProductItem item) {
                return item.isActive() ? wfmStrings.active() : wfmStrings.inactive();
            }
        };
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

        messageBox.setMessage(message);
        messageBox.addCloseHandler(new CloseHandler() {
            @Override
            public void onSubmit() {
                ArrayList<Integer> ids = getIDsOnly(selectedItems);
                if (!ids.isEmpty()) {
                    LoadingPanel.loading(true);
                    productService.deleteSelectedProductServices(ids, new AbstractAsyncCallback<ArrayList<Integer>>() {
                        @Override
                        public void failure(Throwable throwable) {
                            super.failure(throwable);
                            LoadingPanel.loading(false);
                            Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                        }

                        @Override
                        public void success(ArrayList<Integer> result) {
                            LoadingPanel.loading(false);
                            listingPanel.reloadPage();
                            if (result == null || result.isEmpty()) {
                                Info.show(property.getPlural(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.assemblyItems()), Info.Type.INFO);
                            } else if (!result.isEmpty()) {
                                VerticalPanel msg = new VerticalPanel();
                                msg.add(new HTML(property.getSingular(accountingStrings.errorDeletingProduct(), wfmStrings.assemblyItems())));
                                msg.add(new HTML("&nbsp"));
                                msg.add(new HTML(property.getPlural(accountingStrings.msgForDeleteProductServices(), wfmStrings.assemblyItems())));
                                WfmMessageBox inActiveMessageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                                inActiveMessageBox.setTitle(wfmStrings.confirmationMessage());
                                inActiveMessageBox.setMessage(msg.toString());
                                inActiveMessageBox.addCloseHandler(new CloseHandler() {
                                    @Override
                                    public void onSubmit() {
                                        productService.inActiveProducts(result, new AbstractAsyncCallback<Boolean>() {
                                            @Override
                                            public void failure(Throwable throwable) {
                                                super.failure(throwable);
                                                Info.show(accountingStrings.inActiveProductErrorMessage(), Info.Type.WARNING);
                                            }

                                            @Override
                                            public void success(Boolean result) {
                                                super.success(result);
                                                Info.show(accountingStrings.inActiveProductMessage(), Info.Type.INFO);
                                                listingPanel.reloadPage();
                                            }
                                        });
                                    }
                                });
                                inActiveMessageBox.open();
                            }
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
        return Constants.ASSEMBLY_PRODUCTS;
    }
}
