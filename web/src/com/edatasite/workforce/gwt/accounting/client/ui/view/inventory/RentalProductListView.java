package com.edatasite.workforce.gwt.accounting.client.ui.view.inventory;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductService;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductServiceAsync;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.RbacService;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFieldConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrProductServiceRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
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
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Iftixor
 * Date: 08.10.21
 * Time: 21:40:53
 */
public class RentalProductListView extends BaseListView implements Constants, AccountingConstants, PermissionConstants {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    private final ProductServiceAsync productService = ProductService.App.get();

    private ListingPanel<ProductItem> listingPanel;
    private int actionItemCount;

    public RentalProductListView() {
        super(RENTAL_PRODUCTS);
        setDescription(property.getPlural(wfmStrings.rentalProducts()));
        if (hasPermissionToCreateRentalProduct()) {
            setAddNew(() -> SinksContainerFactory.entryPoint.onHistoryChanged("product-rental|add/add"));
        }
    }

    @Override
    protected Widget onInitialize() {
        listingPanel = new GuideListingPanel(ListPanelType.RentalProductsListPanel, getColumns(), getListingRequestProvider(), getListingPanelDesign());

//        listingPanel.setPDFListener(clickEvent -> {
//            if (listingPanel.getItemCount() > 1000) {
//                Window.alert(wfmStrings.CurrentlyLimitedContactExport());
//            }
//            String pdfURL;
//            pdfURL = CommandConstants.PDF_URL + "/productsServicesListPDFHandler";
//            ListingFilterParameter listingFilterParameter = listingPanel.getFilterParametrs() == null ? new ListingFilterParameter() : listingPanel.getFilterParametrs();
//            listingFilterParameter.setViewType(ViewName.InventoryItemsView.name());
//            listingPanel.callListPDF(pdfURL, listingFilterParameter);
//        });

//        listingPanel.setExcelListener(clickEvent -> {
//            if (listingPanel.getItemCount() > 1000) {
//                Window.alert(wfmStrings.CurrentlyLimitedContactExport());
//            }
//            String excelURL;
//            excelURL = CommandConstants.COMMON_URL + "/downloadProductsServicesListExcel";
//            ListingFilterParameter listingFilterParameter = listingPanel.getFilterParametrs() == null ? new ListingFilterParameter() : listingPanel.getFilterParametrs();
//            listingFilterParameter.setViewType(ViewName.InventoryItemsView.name());
//            listingPanel.callListExcel(excelURL, listingFilterParameter);
//        });
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_RENTAL_PRODUCT_ADDED, RentalProductListView.this, (sender, args) -> listingPanel.reloadPage());

        add(listingPanel);
        return null;
    }

    private boolean hasPermissionToCreateRentalProduct() {
        return Utils.hasPermission(ACCOUNTING_RENTAL_ADD);
    }

    private GuideListingPanelDesign getListingPanelDesign() {
        return new GuideListingPanelDesign() {
            @Override
            public Command getAddNewItemCommand() {
                return hasPermissionToCreateRentalProduct() ? () -> SinksContainerFactory.entryPoint.onHistoryChanged("product-rental|add/add") : null;
            }

            @Override
            public Command getUploadButtonCommand() {
                return null;
            }

            @Override
            public ListingFacetFilter initFacetFilter() {
                return new ListingFacetFilter() {
                    @Override
                    public FacetCallbackProvider getFacetCallbackProvider() {
                        return (data, callback) -> {
                            data.getCustomData().put(PRODUCT_TYPE, RENTAL_ITEM.toString());
                            RbacService.App.get().getProductsServicesFacetFilterData(data, new AbstractAsyncCallback<FacetFilterRpc>() {// add product type parameter to solr query
                                public void failure(Throwable caught) {
                                    callback.onFailure(caught);
                                }

                                public void success(FacetFilterRpc data) {
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
                if (hasPermissionToCreateRentalProduct()) {
                    ActionButton newItem = getAddNewButton(ActionButton.Type.TOOLMENU);

                    newItem.addClickHandler((clickEvent) -> SinksContainerFactory.entryPoint.onHistoryChanged("product-rental|add/add"));
                    return newItem;
                }
                return null;
            }

            @Override
            public ActionButton initTopToolBarMore() {
                return null;
            }

            @Override
            public HorizontalPanel initTopToolBarWidgets() {
                return null;
            }


            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(property.getPlural(accountingStrings.messCurrentlyInventoryItems(), accountingStrings.rentalItem()));
                message.setHref("product|add/add/");
                message.setTextBeforeLink(property.getPlural(accountingStrings.messAddingInventoryClicking(), accountingStrings.rentalItem()));
                emptyDataTable.initEmptyDataTable(message);
            }

            @Override
            public boolean isEditCustomFieldCell() {
                return Utils.hasPermission(ACCOUNTING_RENTAL_EDIT);
            }
        };
    }

    private FacetContentConfigure getFacetContentConfigure() {
        FacetContentConfigure contentConfigure = new FacetContentConfigure(3, wfmStrings.filter());
        contentConfigure.setRangePanelEnabled(true);
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

        contentConfigure.addContentConfigure(FacetContentType.ProductsServicesFacetFilter.getContentCode()[6], wfmStrings.brand(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrProductServiceRepresenter.FIELD_BRAND_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrProductServiceRepresenter.FIELD_BRAND_ID_NAME;
            }
        });
        contentConfigure.addContentConfigure(FacetContentType.ProductsServicesFacetFilter.getContentCode()[2], wfmStrings.price(), new FacetFieldConfigure() {
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


        return contentConfigure;
    }

    private ListingRequestProvider<ProductItem> getListingRequestProvider() {
        return (filterParametrs, callback) -> {
            filterParametrs.setProductType(RENTAL_ITEM);
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

    private ColumnDefinitionConfig[] getColumns() {
        List<ColumnDefinitionConfig> columnsList = new ArrayList<>();
        //Action
        ColumnDefinitionConfig column = new ColumnDefinitionConfig<ProductItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final ProductItem item) {
                actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);
                if (Utils.hasPermission(ACCOUNTING_RENTAL_SUMMARY) && item.isActive()) {
                    MenuPopItem productSummary = new MenuPopItem(wfmStrings.summaryView(), "icon-puchase-invoise-small");
                    productSummary.setCommand(() ->
                            SinksContainerFactory.entryPoint.onHistoryChanged("product-rental|summary/" + item.getObjectId(), item.getProductNumber(), item.getName()));
                    actionItemCount++;
                    menuBar.addItem(productSummary);
                }

                if (Utils.hasPermission(ACCOUNTING_RENTAL_EDIT)) {
                    MenuPopItem productEdit = new MenuPopItem(wfmStrings.edit(), "icon-employee-edit-profile");
                    productEdit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("product-rental|add/add/" + item.getObjectId(), item.getProductNumber(), item.getName()));
                    actionItemCount++;
                    menuBar.addItem(productEdit);
                }

                if (Utils.hasPermission(ACCOUNTING_RENTAL_DELETE) && item.isActive()) {
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
                                        Info.show(Property.get(Constants.RENTAL_PRODUCTS, accountingStrings.errorDeletingProduct(), accountingStrings.rentalItem()), Info.Type.WARNING);
                                    }

                                    public void success(Boolean deleted) {
                                        if (deleted) {
                                            Info.show(Property.get(Constants.RENTAL_PRODUCTS, wfmStrings.messSuccessfulyyDeleted(), accountingStrings.rentalItem()), Info.Type.INFO);
                                            listingPanel.reloadPage();
                                        } else {
                                            VerticalPanel msg = new VerticalPanel();
                                            msg.add(new HTML(Property.get(Constants.RENTAL_PRODUCTS, accountingStrings.errorDeletingProduct(), accountingStrings.rentalItem())));
                                            msg.add(new HTML("&nbsp"));
                                            msg.add(new HTML(Property.getPluralWithObjectCodeWithReplace(Constants.RENTAL_PRODUCTS, accountingStrings.msgForDeleteProductServices(), accountingStrings.rentalItem())));
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
                                                            Info.show(accountingStrings.inActiveProductErrorMessage(), Info.Type.WARNING);
                                                        }

                                                        @Override
                                                        public void success(Boolean result) {
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
                    label.addClickHandler(clickEvent ->
                            SinksContainerFactory.entryPoint.onHistoryChanged("product-rental|summary/" + item.getObjectId(), item.getProductNumber(), item.getName()));
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
                /*if (item.getParentId() != null) {
                    str.append(item.getName()).append(" ").append(accountingStrings.child());
                } else {*/
                str.append(item.getName());
//                }
                HTML label = new HTML(str.toString());
                if (item.isActive()) {
                    label.addClickHandler(clickEvent ->
                            SinksContainerFactory.entryPoint.onHistoryChanged("product-rental|summary/" + item.getObjectId(), item.getProductNumber(), item.getName()));
                }
                return label;
            }
        };
        column.addStyleAttribute("padding-left", "5px");
        columnsList.add(column);


        //Brand
        column = new ColumnDefinitionConfig<ProductItem, String>(wfmStrings.brand(), ProductItem.BRAND, 150) {

            @Override
            public String getCellValue(final ProductItem item) {
                return item.getBrand() != null ? item.getBrand() : "";
            }
        };
        column.addStyleAttribute("padding-left", "5px");
        columnsList.add(column);


        //Category
        column = new ColumnDefinitionConfig<ProductItem, String>(wfmStrings.category(), ProductItem.Category, 150) {

            @Override
            public String getCellValue(final ProductItem item) {
                return item.getCategory() != null ? item.getCategory() : "";
            }
        };
        column.addStyleAttribute("padding-left", "5px");
        columnsList.add(column);

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

        //Cost Price
        column = new ColumnDefinitionConfig<ProductItem, String>(wfmStrings.costPrice(), ProductItem.COST_PRICE, 100) {

            @Override
            public String getCellValue(ProductItem item) {
                return item.getCostPrice() != null ? AccountingUtils.get().formatUnitPrice(item.getCostPrice()) : "";
            }
        };
        column.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
        column.addStyleAttribute("padding-right", "5px");
        columnsList.add(column);

        //Qty
        column = new ColumnDefinitionConfig<ProductItem, String>(wfmStrings.qty(), ProductItem.SALE_ORDER_QTY, 100) {

            @Override
            public String getCellValue(ProductItem item) {
                return AccountingUtils.get().formatUnitPrice(item.getQty());
            }
        };
        column.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
        column.addStyleAttribute("padding-right", "5px");
        columnsList.add(column);

        return columnsList.toArray(new ColumnDefinitionConfig[0]);
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
        return Constants.RENTAL_PRODUCTS;
    }
}
