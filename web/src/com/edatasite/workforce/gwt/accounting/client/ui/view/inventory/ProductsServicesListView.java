package com.edatasite.workforce.gwt.accounting.client.ui.view.inventory;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewProductCustomDescription;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductCustomSettings;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductService;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductServiceAsync;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.AccountingImageBundle;
import com.edatasite.workforce.gwt.accounting.client.ui.view.quickadd.ProductQuickAddForm;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.RbacService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TreeSelectItem;
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
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
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
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingCallback;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.cellwidgets.DropDownCellEditor;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CellChange;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.WarehouseLookUp;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.gen2.table.client.SelectionGrid;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.ListItem;
import gwt.material.design.client.ui.html.Span;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Aug 13, 2009
 * Time: 6:00:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProductsServicesListView extends BaseListView implements Constants, AccountingConstants, PermissionConstants {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();
    private static final ProductServiceAsync productService = ProductService.App.get();
    public final static Images images = GWT.create(Images.class);
    private final ImportFilePopUp imp = new ImportFilePopUp(ImportTypeEnum.PRODUCT, null);
    private ListingPanel<ProductItem> list;
    protected HashSet<ProductItem> selectedItems = new HashSet<>();

    private Integer categoryID = null;

    private HashMap<String, String> customSettings;

    private TreeSelectItem[] categoryItems;

    private TreeSelectItem[] unitMeasurementItems;

    private WarehouseLookUp warehouseLookUp;

    private ArrayList<String> companyCurrencies;

    private final ActionButton deleteSelectedButton = null;

    protected int totalCount;
    private Integer parentId;
    private Integer crmAccountId;
    private Integer rentalItemId;

    public ProductsServicesListView() {
        super("productList");
        setDescription(property.getPlural(wfmStrings.productsOrServices()));
        if (hasPermissionToCreeateProduct()) {
            setAddNew("product|add/add");
        } else if (hasPermissionToQuickAddProduct()) {
            setAddNew(() -> new ProductQuickAddForm());
        }
    }

    public ProductsServicesListView(Integer crmAccountId, boolean isSupplier) {
        this();
        this.crmAccountId = crmAccountId;
    }

    public ProductsServicesListView(Integer categoryID) {
        this(categoryID, null);
    }

    public ProductsServicesListView(ListingFilterParameter filterParameter) {
        this();
        this.rentalItemId = filterParameter.getProductId();
    }

    private final AccountingImageBundle imageBundle = GWT.create(AccountingImageBundle.class);

    protected Widget onInitialize() {
        productService.getProductServiceListCustomSettings(new AsyncCallback<ProductCustomSettings>() {
            @Override
            public void onFailure(Throwable caught) {
                customSettings = new HashMap<>();
                drawForm();
            }

            @Override
            public void onSuccess(ProductCustomSettings result) {
                customSettings = result.getCustomSettings();
                categoryItems = result.getProductCategory();
                unitMeasurementItems = result.getUnitMeasurementItems();
                companyCurrencies = result.getCompanyCurrencyList();
                drawForm();
            }
        });
        return null;
    }

    private void drawForm() {

        if (!Utils.hasRole(CLIENT)) {
            list = new GuideListingPanel(
                    ListPanelType.ProductServiceListPanel, getColumns(), getListingRequestProvider(),
                    getListingPanelDesign(), SelectionGrid.SelectionPolicy.CHECKBOX, false,
                    Utils.hasPermission(PermissionConstants.ACCOUNTING_PRODUCT_LIST_CUSTOMIZE), true,
                    Utils.hasPermission(PermissionConstants.ACCOUNTING_PRODUCT_LIST_FILTER));
        } else {
            list = new GuideListingPanel(ListPanelType.ProductServiceListPanel, getColumns(), getListingRequestProvider(), getListingPanelDesign());
        }

        list.setCustomFieldsEditCellSaveChanges((rowValue, columnCodeName) -> saveProductItemCellValue((ProductItem) rowValue, columnCodeName));

        list.setEnableDate();

        if (Utils.isMultiWarehouseEnabled()) {
            warehouseLookUp = new WarehouseLookUp();
            warehouseLookUp.getSuggestBox().setWidth("175px");
            warehouseLookUp.getElement().getStyle().setFloat(Style.Float.LEFT);
            warehouseLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> list.reloadPage());
            list.getAdvancedFilterPanel().add(warehouseLookUp);
            Scheduler.get().scheduleFixedDelay(() -> {
                list.getAdvancedFilterPanel().setVisible(true);
                return true;
            }, 1000);
        }

        list.setOnReset(() -> {
            if (warehouseLookUp != null) {
                warehouseLookUp.clear();
            }
        });

        list.setPDFListener(clickEvent -> {
            if (list.getItemCount() > 1000) {
                Window.alert(wfmStrings.CurrentlyLimitedContactExport());
            }
            String pdfURL;
            pdfURL = CommandConstants.PDF_URL + "/productsServicesListPDFHandler";
            ListingFilterParameter listingFilterParameter = list.getFilterParametrs();
            listingFilterParameter.setPropertyCode(getPropertyCode());
            list.callListPDF(pdfURL, listingFilterParameter);
        });

        list.setExcelListener(clickEvent -> {
            if (list.getItemCount() > 1000) {
                Window.alert(wfmStrings.CurrentlyLimitedContactExport());
            }
            String excelURL;
            excelURL = CommandConstants.COMMON_URL + "/downloadProductsServicesListExcel";
            ListingFilterParameter listingFilterParameter = list.getFilterParametrs() == null ? new ListingFilterParameter() : list.getFilterParametrs();
            listingFilterParameter.setPropertyCode(getPropertyCode());
            listingFilterParameter.setFeatured(Utils.isMultipleSalesPriceEnable());
            list.callListExcel(excelURL, listingFilterParameter);
        });

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SALEINVOICE_ADDED, ProductsServicesListView.this, (sender, args) -> list.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PURCHASEINVOICE_ADDED, ProductsServicesListView.this, (sender, args) -> list.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PRODUCTSERVICE_SAVED, ProductsServicesListView.this, (sender, args) -> list.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SALESORDER_ADDED, ProductsServicesListView.this, (sender, args) -> list.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PRODUCT_IMPORT_RELOAD_PAGE, ProductsServicesListView.this, (sender, args) -> list.reloadPage());
        list.addSelectionRowHandler(selectedRows -> {
            if (selectedRows.size() > 0) {
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

        add(list);
    }

    private void saveProductItemCellValue(ProductItem rowValue, String columnCodeName) {
        ProductService.App.get().saveProductItemCellValue(rowValue, columnCodeName, new AbstractAsyncCallback<Boolean>() {
        });
    }

    private boolean hasPermissionToCreeateProduct() {
        return Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_PRODUCT_ADD : ACCOUNTING_PRODUCT_ADD);
    }

    private boolean hasPermissionToQuickAddProduct() {
        return Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_PRODUCT_QUICK_ADD : ACCOUNTING_PRODUCT_QUICK_ADD);
    }

    private GuideListingPanelDesign getListingPanelDesign() {
        return new GuideListingPanelDesign() {
            @Override
            public Command getAddNewItemCommand() {
                if (hasPermissionToCreeateProduct()) {
                    return () -> goTo(categoryID != null ? "product|add/add/" + categoryID + "/" + BY_CATEGORY : "product|add/add");
                } else if (hasPermissionToQuickAddProduct()) {
                    return () -> showQuickAddForm();
                }
                return null;
            }

            @Override
            public Command getUploadButtonCommand() {
                return categoryID == null ? imp::open : null;
            }

            @Override
            public ListingFacetFilter initFacetFilter() {
                return new ListingFacetFilter() {
                    @Override
                    public FacetCallbackProvider getFacetCallbackProvider() {
                        return (data, callback) -> {
                            if (warehouseLookUp != null && warehouseLookUp.getSelectedItemID() != null) {
                                data.getCustomData().put(WAREHOUSE_ID, warehouseLookUp.getSelectedItemID().toString());
                            }
                            RbacService.App.get().getProductsServicesFacetFilterData(data, new AbstractAsyncCallback<FacetFilterRpc>() {
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
                final AddNewMenus addNewMenus = getActions();
                if (addNewMenus.isHasItem()) {
                    ActionButton more = getAddNewButton(ActionButton.Type.TOOLMENU);
                    final ActionButton finalMore = more;
                    more.addClickHandler(clickEvent -> finalMore.setMenu(addNewMenus));
                    return more;
                }
                return null;
            }

            @Override
            public ActionButton initTopToolBarMore() {
                if (Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_PRODUCT_DELETE : ACCOUNTING_PRODUCT_DELETE)) {
                    return getRemoveMoreButton(clickEvent -> deleteSelection());
                }
                return null;
            }

            @Override
            public ActionButton initTopToolBarMoreActions() {
                ActionButton printBarcode = new ActionButton(wfmStrings.printBarcode(), ActionButton.Type.BUTTON);
                printBarcode.addClickHandler(clickEvent -> {

                    LinkedHashMap<String, String> params = new LinkedHashMap<>();

                    String products = getProductItemsAsJsonString();
                    params.put("products", products);
                    String pdfUrl = CommandConstants.PDF_URL + "/productsBarcodePDFHandler";
                    Utils.sendPDFOrExcelRequest(list.getExportPanel(), pdfUrl, params, "_blank");
                });
                return printBarcode;
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
                if (categoryID == null) {
                    imp.setSubmitCompleted(() -> {

                        if (imp.getObjectId() != null) {
                            goTo("importproducts|add/add/" + imp.getObjectId() + "/" + FROM_CSV);
                        }
                    });


                    if (Utils.isMultiCompanySubsidiary()) {
                        ListItem li = new ListItem();

                        MaterialLink importItem = new MaterialLink(wfmStrings.importString());

                        MaterialDropDown items = new MaterialDropDown(importItem);
                        items.setHover(true);
                        items.setBelowOrigin(true);
                        ImportFileActionLink csvLink = new ImportFileActionLink();
                        csvLink.setText(wfmStrings.csv());
                        csvLink.addClickHandler(ch -> imp.open());
                        items.add(csvLink);

                        ImportFileActionLink parentLink = new ImportFileActionLink();
                        parentLink.setText(wfmStrings.from() + " " + wfmStrings.parent());
                        parentLink.addClickHandler(ch -> goTo("importproducts|add/add/null/" + FROM_PARENT));
                        items.add(parentLink);
                        li.add(importItem);
                        li.add(items);

                        menuContainer.add(li);
                    } else {
                        ImportFileActionLink csvLink = new ImportFileActionLink();
                        csvLink.setText(wfmStrings.csv());
                        csvLink.addClickHandler(ch -> imp.open());
                        if (Utils.hasPermission(ACCOUNTING_PRODUCT_LIST_IMPORT)) {
                            menuContainer.add(csvLink);
                        }
                    }
                }
                exportOption.initExport(null, Utils.hasPermission(ACCOUNTING_PRODUCT_LIST_PDF_EXCEL_EXPORT));
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(property.getPlural(accountingStrings.messCurrentlyProducts(), wfmStrings.productsOrServices()));
                message.setHref(c -> new ProductQuickAddForm());
                message.setTextBeforeLink(property.getPlural(accountingStrings.messAddingProductsClicking(), wfmStrings.productsOrServices()));
                emptyDataTable.initEmptyDataTable(message);
            }

            @Override
            public boolean isEditCustomFieldCell() {
                return Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_PRODUCT_EDIT : ACCOUNTING_PRODUCT_EDIT);
            }
        };
    }

    private AddNewMenus getActions() {
        final AddNewMenus addNewMenus = new AddNewMenus(images);
        //ACTIONS
        com.google.gwt.user.client.ui.MenuItem actionItem = new com.google.gwt.user.client.ui.MenuItem(
                Utils.getButtonText(AbstractImagePrototype.create(images.actions()).getHTML(),
                        wfmStrings.actions(), 0, AbstractImagePrototype.create(images.action()).getHTML()), true, new MenuBar(true)) {
            @Override
            public MenuBar getSubMenu() {
                return addNewMenus.createMenu();
            }
        };
        return addNewMenus;
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

        contentConfigure.addContentConfigure(FacetContentType.ProductsServicesFacetFilter.getContentCode()[4], Property.get(Constants.SUPPLIER_LIST, wfmStrings.supplier()), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrProductServiceRepresenter.FIELD_MULTI_SUPPLIER_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrProductServiceRepresenter.FIELD_MULTI_SUPPLIER_ID_NAME;
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

        contentConfigure.addContentConfigure(FacetContentType.ProductsServicesFacetFilter.getContentCode()[8], wfmStrings.status(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrProductServiceRepresenter.FIELD_PRODUCT_ACTIVE;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrProductServiceRepresenter.FIELD_PRODUCT_ACTIVE;
            }

//            @Override
//            public boolean isShowFacetConttentFilter() {
//                return false;
//            }
        });

//        contentConfigure.addContentConfigure(FacetContentType.ProductsServicesFacetFilter.getContentCode()[2], wfmStrings.price(), new FacetFieldConfigure() {
//            @Override
//            public String getSolrFieldCriteriaName() {
//                return SolrProductServiceRepresenter.FIELD_UNITPRICE;
//            }
//
//            @Override
//            public String getSolrFacetFieldName() {
//                return SolrProductServiceRepresenter.FIELD_UNITPRICE;
//            }
//
//            @Override
//            public boolean isConditionItemId() {
//                return false;
//            }
//        });


        contentConfigure.addContentConfigure(FacetContentType.ProductsServicesFacetFilter.getContentCode()[5], wfmStrings.unitMeasurement(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrProductServiceRepresenter.FIELD_UNIT_MEASUREMENT_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrProductServiceRepresenter.FIELD_UNIT_MEASUREMENT_ID_NAME;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
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

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }

        });

        contentConfigure.addContentConfigure(FacetContentType.ProductsServicesFacetFilter.getContentCode()[9], wfmStrings.purchaseAccount(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrProductServiceRepresenter.FIELD_COGS_ACCOUNT_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrProductServiceRepresenter.FIELD_COGS_ACCOUNT_ID_NAME;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }
        });

        contentConfigure.addContentConfigure(FacetContentType.ProductsServicesFacetFilter.getContentCode()[10], wfmStrings.assetAccount(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrProductServiceRepresenter.FIELD_ASSET_ACCOUNT_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrProductServiceRepresenter.FIELD_ASSET_ACCOUNT_ID_NAME;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }
        });

        return contentConfigure;
    }

    private ListingRequestProvider<ProductItem> getListingRequestProvider() {
        return (filterParametrs, callback) -> {
            loadProductsList(filterParametrs, callback, null);
        };
    }

    public ProductsServicesListView(Integer categoryID, Integer parentId) {
        super("productList");
        setDescription(parentId != null ? "Variants" : Property.getPluralWithObjectCode(Constants.PRODUCTS_OR_SERVICES, wfmStrings.productsOrServices()));
        this.categoryID = categoryID;
        this.parentId = parentId;
        if (hasPermissionToCreeateProduct()) {
            if (categoryID != null) {
                setAddNew("product|add/add/" + categoryID + "/" + BY_CATEGORY);
            } else {
                setAddNew("product|add/add");
            }
        } else if (hasPermissionToQuickAddProduct()) {
            setAddNew(() -> showQuickAddForm());
        }
    }

    private String getProductItemsAsJsonString() {
        JSONArray products = new JSONArray();
        int i = 0;
        for (Object item : selectedItems) {
            ProductItem productItem = (ProductItem) item;
            JSONObject product = new JSONObject();
            product.put("productNumber", new JSONString(productItem.getProductNumber()));
            product.put("name", new JSONString(productItem.getName()));
            product.put("description", new JSONString(productItem.getDescription() != null ? productItem.getDescription() : ""));
            product.put("unitpPrice", new JSONString(productItem.getUnitpPrice() != null ? productItem.getUnitpPrice().toString() : ""));
            products.set(i, product);
            i++;
        }

        return products.toString();
    }

    private int actionItemCount;

    protected void loadProductsList(ListingFilterParameter filterParametrs, ListingCallback<ProductItem> callback, Span container) {
        if (filterParametrs == null) {
            filterParametrs = new ListingFilterParameter();
        }
        filterParametrs.setCategoryID(categoryID);
        if (warehouseLookUp != null) {
            filterParametrs.setWarehouseID(warehouseLookUp.getSelectedItemID());
        }
        filterParametrs.setFeatured(Utils.isMultipleSalesPriceEnable());
        if (parentId != null) {
            filterParametrs.setParentID(parentId);
        }
        filterParametrs.setSupplierId(crmAccountId);
        filterParametrs.setProductId(rentalItemId);
        ProductService.App.get().getProductsListFromSolr(filterParametrs, new AsyncCallback<ListResult<ProductItem>>() {
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess(ListResult<ProductItem> list) {
                AccountingUtils.loadData();
                if (callback != null) {
                    callback.onSuccess(list);
                }
                totalCount = list.getTotal();
                statisticShortcut = statisticShortcut != null ? statisticShortcut : container;
                if (statisticShortcut != null) {
                    if (totalCount > 0) {
                        statisticShortcut.setText(countFormat(totalCount));
                        statisticShortcut.setClass("tab-label");
                    } else {
                        statisticShortcut.setText("");
                        statisticShortcut.removeStyleName("tab-label");
                    }
                }
            }
        });
    }

    public String getIconStyle() {
        return "accountMark purchase-order-list";
    }


    private void updateProductCategory(ProductItem item) {
        ProductService.App.get().updateProductCategory(item.getObjectId(), item.getCategoryId(), new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(Boolean result) {

            }
        });
    }

    private void updateProductMeasurement(ProductItem item) {
        ProductService.App.get().updateProductMeasurement(item.getObjectId(), item.getUnitMeasurementId(), new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(Boolean result) {

            }
        });
    }

    private void showDeleteMessage() {
        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
        messageBox.setTitle(wfmStrings.warning());
        ProductItem item = selectedItems.iterator().next();
        String message = wfmStrings.areYouSureYouWantToDeleteTheSelectedRecords();

        messageBox.setMessage(message);
        messageBox.addCloseHandler(new CloseHandler() {
            @Override
            public void onSubmit() {
                ArrayList<Integer> ids = getIDsOnly(selectedItems);
                if (ids.size() > 0) {
                    LoadingPanel.loading(true);
                    ProductService.App.get().deleteSelectedProductServices(ids, new AbstractAsyncCallback<ArrayList<Integer>>() {
                        @Override
                        public void failure(Throwable throwable) {
                            LoadingPanel.loading(false);
                            Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                        }

                        @Override
                        public void success(ArrayList<Integer> result) {
                            LoadingPanel.loading(false);
                            list.reloadPage();
                            if (result == null || result.size() == 0) {
                                Info.show(property.getPlural(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.productsOrServices()), Info.Type.INFO);
                            } else if (result.size() > 0) {
                                VerticalPanel msg = new VerticalPanel();
                                msg.add(new HTML(property.getSingular(accountingStrings.errorDeletingProduct(), accountingStrings.productOrService())));
                                msg.add(new HTML("&nbsp"));
                                msg.add(new HTML(property.getPlural(accountingStrings.msgForDeleteProductServices(), wfmStrings.productsOrServices())));
                                WfmMessageBox inActiveMessageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                                inActiveMessageBox.setTitle(wfmStrings.confirmationMessage());
                                inActiveMessageBox.setMessage(msg.toString());
                                //inActiveMessageBox.setWidth(515);
                                inActiveMessageBox.addCloseHandler(new CloseHandler() {
                                    @Override
                                    public void onSubmit() {
                                        productService.inActiveProducts(result, new AbstractAsyncCallback<Boolean>() {
                                            @Override
                                            public void failure(Throwable throwable) {
                                                Info.show(accountingStrings.inActiveProductErrorMessage(), Info.Type.WARNING);
                                            }

                                            @Override
                                            public void success(Boolean result) {
                                                Info.show(accountingStrings.inActiveProductMessage(), Info.Type.INFO);
                                                list.reloadPage();
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

    protected void deleteSelection() {
        if (selectedItems.size() == 0) {
            Info.show(accountingMessages.pleaseSelectOneRow(property.getPlural(wfmStrings.productsOrServices())), Info.Type.WARNING);
        } else {
            showDeleteMessage();
        }
    }

    public static ArrayList<Integer> getIDsOnly(HashSet<ProductItem> selectedItems) {
        ArrayList<Integer> ids = new ArrayList<>();
        for (ProductItem item : selectedItems) {
            ids.add(item.getObjectId());
        }
        return ids;
    }


    public class AddNewMenus extends PopupPanel implements ClickHandler {
        private final MenuBar contextMenu = new MenuBar(true);
        private final Images images;
        private boolean hasItem = false;

        public AddNewMenus(Images images) {
            super(true);
            setAnimationEnabled(true);
            this.images = images;
            createMenu();
            add(contextMenu);
        }

        private MenuBar createMenu() {
            contextMenu.clearItems();
            contextMenu.setAutoOpen(false);

            if (hasPermissionToCreeateProduct()) {
                this.hasItem = true;
                contextMenu.addItem("<span>" + property.getSingular(wfmStrings.product()) + "</span>", true, () -> goTo(categoryID != null ? "product|add/add/" + categoryID + "/" + BY_CATEGORY : "product|add/add"));
            }

            if (hasPermissionToQuickAddProduct()) {
                this.hasItem = true;
                MenuPopItem quickProduct = new MenuPopItem(wfmStrings.quickAdd());
                quickProduct.ensureDebugId("new_task");
                quickProduct.setCommand(() -> showQuickAddForm());
                contextMenu.addItem(quickProduct);
            }

            if (Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_STOCK_ADJUSTMENT_ADD : ACCOUNTING_STOCK_ADJUSTMENT_ADD)) {
                this.hasItem = true;
                contextMenu.addItem("<span>" + accountingStrings.stockAdjustment() + "</span>", true, (Command) () -> goTo("stockadjustment|add/add")).ensureDebugId(PRODUCT + accountingStrings.stockAdjustment());
            }
            if (Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_STOCK_TRANSFER_LIST : ACCOUNTING_STOCK_TRANSFER_LIST) && Utils.isMultiWarehouseEnabled()) {
                this.hasItem = true;
                contextMenu.addItem("<span>" + accountingStrings.stockTransfer() + "</span>", true, (Command) () -> goTo("stocktransfer|add/add")).ensureDebugId(PRODUCT + "StockTransfer");
            }
//            if (Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_BUILD_ASSEMBLY : ACCOUNTING_BUILD_ASSEMBLY)) {
//                this.hasItem = true;
//                contextMenu.addItem("<span>" + accountingStrings.buildAssembly() + "</span>", true, (Command) () -> goTo("buildAssembly|add/add")).ensureDebugId(PRODUCT + accountingStrings.buildAssembly());
//            }

            return contextMenu;
        }

        @Override
        public void onClick(ClickEvent clickEvent) {
            final int left = clickEvent.getRelativeElement().getAbsoluteLeft();
            final int top = clickEvent.getRelativeElement().getAbsoluteTop() + clickEvent.getRelativeElement().getOffsetHeight();
            this.setPopupPosition(left, top);
            this.show();
        }

        public boolean isHasItem() {
            return hasItem;
        }
    }

    private void showQuickAddForm() {
        if (categoryID != null) {
            new ProductQuickAddForm(categoryID, true);
        } else {
            new ProductQuickAddForm();
        }
    }

    @Override
    public void initStatistics(Integer parentId, Span container) {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setLimit(1);
        loadProductsList(fp, null, container);
    }

    private ColumnDefinitionConfig[] getColumns() {
        ArrayList<ColumnDefinitionConfig> columnsList = new ArrayList<>();

        ColumnDefinitionConfig column = new ColumnDefinitionConfig<ProductItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH - 10) {
            @Override
            public Anchor getCellValue(final ProductItem item) {
                actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);
                if (Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_PRODUCT_SUMMARY : ACCOUNTING_PRODUCT_SUMMARY)) {
                    MenuPopItem productSummary = new MenuPopItem(wfmStrings.summaryView(), "icon-puchase-invoise-small");
                    productSummary.setCommand(() -> {
                        String trackLink = "";
                        if (INVENTORY_ITEM.equals(item.getType())) {
                            if (item.getInventoryTrackingEnabled()) {
                                trackLink = item.getInventoryTrackingEnabled() ? "/INVENTORY_TRACKING" : "";
                            } else if (item.getTrackBatchesEnabled()) {
                                trackLink = item.getTrackBatchesEnabled() ? "/BATCH_TRACKING" : "";
                            }
                        }
                        SinksContainerFactory.entryPoint.onHistoryChanged("product|summary/" + item.getObjectId() + "/"
                                + item.getTypeName() + trackLink, item.getProductNumber(), item.getName());
//                        }
                    });
                    actionItemCount++;
                    menuBar.addItem(productSummary);
                }

                if (Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_PRODUCT_EDIT : ACCOUNTING_PRODUCT_EDIT)) {
                    MenuPopItem productEdit = new MenuPopItem(wfmStrings.edit(), "icon-employee-edit-profile");
                    productEdit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("product|productadd/" + item.getObjectId(), item.getProductNumber(), item.getName()));
                    actionItemCount++;
                    menuBar.addItem(productEdit);
                }

                if (Utils.hasPermission(PermissionConstants.ACCOUNTING_PRODUCT_LIST_ITEM_COPY)) {
                    MenuPopItem copyTo = new MenuPopItem(wfmStrings.copy(), "icon-copy");
                    copyTo.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("product|add/add/" + COPY_FROM_EXISTING + "/" + item.getObjectId()));
                    actionItemCount++;
                    menuBar.addItem(copyTo);
                }

                if (Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_VARIATION_ADD : ACCOUNTING_VARIATION_ADD) && AccountingUtils.get().enableVariation() && item.getParentId() == null && item.isActive()) {
                    MenuPopItem addVariation = new MenuPopItem(accountingStrings.addVariation(), "icon-employee-edit-profile");
                    addVariation.setCommand(() -> new AddVariationsView(item.getObjectId()));
                    actionItemCount++;
                    menuBar.addItem(addVariation);
                }

                if (Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_VARIATION_DELETE : ACCOUNTING_VARIATION_DELETE) && AccountingUtils.get().enableVariation() && item.getParentId() == null && item.isActive()) {
                    MenuPopItem removeVariationItem = new MenuPopItem(wfmStrings.removeVariations(), "removeItemStyle-profile");
                    removeVariationItem.setCommand(() -> {
                        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                        messageBox.setTitle(wfmStrings.warning());
                        messageBox.setMessage(wfmStrings.sureYouWantToDelete());
                        messageBox.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                productService.deleteProductChilds(item.getObjectId(), new AbstractAsyncCallback<Boolean>() {

                                    public void failure(Throwable caught) {
                                        Info.show(property.getSingular(accountingStrings.errorDeletingProduct(), accountingStrings.productOrService()), Info.Type.WARNING);
                                    }

                                    public void success(Boolean deleted) {
                                        if (deleted) {
                                            Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), accountingStrings.variation()), Info.Type.INFO);
                                            list.reloadPage();
                                        } else {
                                            Info.show(property.getSingular(accountingStrings.errorDeletingProduct(), accountingStrings.productOrService()), Info.Type.WARNING);
                                        }
                                    }
                                });
                            }
                        });
                        messageBox.open();
                    });
                    actionItemCount++;
                    menuBar.addItem(removeVariationItem);
                }

                if (Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_PRODUCT_DELETE : ACCOUNTING_PRODUCT_DELETE)) {

//                    if (ASSEMBLY_ITEM_STR.equals(item.getTypeName()) && (item.isBuilt() != null && item.isBuilt())) {
//                        MenuPopItem unBuildItem = new MenuPopItem(accountingStrings.unBuild(), "removeItemStyle-profile");
//                        unBuildItem.setCommand(() -> new ProductUnBuildView(item.getObjectId()));
//                        menuBar.addItem(unBuildItem);
//                    }

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
                                        Info.show(property.getSingular(accountingStrings.errorDeletingProduct(), accountingStrings.productOrService()), Info.Type.WARNING);
                                    }

                                    public void success(Boolean deleted) {
                                        if (deleted) {
                                            Info.show(property.getSingular(wfmStrings.messSuccessfulyyDeleted(), accountingStrings.productOrService()), Info.Type.INFO);
                                            list.reloadPage();
                                        } else {
                                            VerticalPanel msg = new VerticalPanel();
                                            msg.add(new HTML(property.getSingular(accountingStrings.errorDeletingProduct(), accountingStrings.productOrService())));
                                            msg.add(new HTML("&nbsp"));
                                            msg.add(new HTML(property.getPlural(accountingStrings.msgForDeleteProductServices(), wfmStrings.productsOrServices())));
                                            WfmMessageBox inActiveMessageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                                            inActiveMessageBox.setTitle(wfmStrings.confirmationMessage());
                                            inActiveMessageBox.setMessage(msg.toString());
                                            //inActiveMessageBox.setWidth(515);
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
                                                            list.reloadPage();
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
                    actionItemCount++;
                }
                if (Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_STOCK_ADJUSTMENT_ADD : ACCOUNTING_STOCK_ADJUSTMENT_ADD) && !SERVICE_STR.equals(item.getTypeName()) && !OTHER_CHARGE_STR.equals(item.getTypeName())) {
                    MenuPopItem addStockAdj = new MenuPopItem(accountingStrings.addStockAdjustment());
                    addStockAdj.setCommand(() -> goTo("stockadjustment|add/add/" + item.getObjectId()));
                    actionItemCount++;
                    menuBar.addItem(addStockAdj);
                }
                if (Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_STOCK_TRANSFER_ADD : ACCOUNTING_STOCK_TRANSFER_ADD) && Utils.isMultiWarehouseEnabled()) {
                    MenuPopItem addStockTransfer = new MenuPopItem(wfmStrings.add() + " " + accountingStrings.stockTransfer());
                    addStockTransfer.setCommand(() -> goTo("stocktransfer|add/add/" + item.getObjectId()));
                    actionItemCount++;
                    menuBar.addItem(addStockTransfer);
                }

                final ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        column.setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH - 10);
        column.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH - 10);
        column.setColumnSortable(false);
        columnsList.add(column);

        column = new ColumnDefinitionConfig<ProductItem, SimpleLink>(property.getShortForNumber(wfmStrings.number()), ProductItem.PRODUCT_NUMBER, 80) {

            @Override
            public SimpleLink getCellValue(final ProductItem item) {
                SimpleLink anchor = new SimpleLink(item.getProductNumber());
                anchor.addClickHandler(clickEvent -> {
                    String trackLink = "";
                    if (INVENTORY_ITEM.equals(item.getType())) {
                        if (item.getInventoryTrackingEnabled()) {
                            trackLink = item.getInventoryTrackingEnabled() ? "/INVENTORY_TRACKING" : "";
                        } else if (item.getTrackBatchesEnabled()) {
                            trackLink = item.getTrackBatchesEnabled() ? "/BATCH_TRACKING" : "";
                        }
                    }
                    SinksContainerFactory.entryPoint.onHistoryChanged("product|summary/" + item.getObjectId() + "/" + item.getTypeName() + trackLink, item.getProductNumber(), item.getName());
                });
                return anchor;
            }
        };
        columnsList.add(column);

        column = new ColumnDefinitionConfig<ProductItem, Widget>(wfmStrings.name(), ProductItem.NAME, 250) {

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
                    label.addClickHandler(clickEvent -> {
                        String trackLink = "";
                        if (INVENTORY_ITEM.equals(item.getType())) {
                            if (item.getInventoryTrackingEnabled()) {
                                trackLink = item.getInventoryTrackingEnabled() ? "/INVENTORY_TRACKING" : "";
                            } else if (item.getTrackBatchesEnabled()) {
                                trackLink = item.getTrackBatchesEnabled() ? "/BATCH_TRACKING" : "";
                            }
                        }
                        SinksContainerFactory.entryPoint.onHistoryChanged("product|summary/" + item.getObjectId() + "/" + item.getTypeName() + trackLink, item.getProductNumber(), item.getName());
                    });
                }
                return label;
            }
        };
        columnsList.add(column);


        column = new ColumnDefinitionConfig<ProductItem, String>(wfmStrings.status(), ProductItem.STATUS, 100) {
            @Override
            public String getCellValue(ProductItem item) {
                return item.isActive() ? wfmStrings.active() : wfmStrings.inactive();
            }
        };
        columnsList.add(column);

        column = new ColumnDefinitionConfig<ProductItem, String>(wfmStrings.description(), ProductItem.DISCRIPTION, 150) {
            @Override
            public String getCellValue(ProductItem item) {
                return item.getDescription() != null ? item.getDescription() : "";
            }
        };
        columnsList.add(column);

        column = new ColumnDefinitionConfig<ProductItem, String>(wfmStrings.type(), ProductItem.TYPE, 100) {
            @Override
            public String getCellValue(ProductItem item) {
                return item.getTypeName();
            }
        };
        column.setColumnSortable(Boolean.FALSE);
        columnsList.add(column);

        column = new ColumnDefinitionConfig<ProductItem, String>(accountingStrings.maximumDiscount(), ProductItem.DISCOUNT_TYPE, 100) {
            @Override
            public String getCellValue(ProductItem item) {
                return item.getDiscountTypeName();
            }
        };
        columnsList.add(column);



        column = new ColumnDefinitionConfig<ProductItem, String>(wfmStrings.assetAccount(), ProductItem.ASSET_ACCOUND, 100) {
            @Override
            public String getCellValue(ProductItem item) {
                if (ASSEMBLY_ITEM_STR.equals(item.getTypeName()) || INVENTORY_ITEM_STR.equals(item.getTypeName()))
                    return item.getAssetAccount();
                return "N/A";
            }
        };
        columnsList.add(column);

        column = new ColumnDefinitionConfig<ProductItem, String>(wfmStrings.purchaseAccount(), ProductItem.COGS_ACCOUND, 100) {
            @Override
            public String getCellValue(ProductItem item) {
                return item.getCogsAccount();
            }
        };
        columnsList.add(column);

        column = new ColumnDefinitionConfig<ProductItem, String>(wfmStrings.salesAccount(), ProductItem.ACCOUND, 100) {
            @Override
            public String getCellValue(ProductItem item) {
                return item.getAccount();
            }
        };
        columnsList.add(column);

        column = new ColumnDefinitionConfig<ProductItem, String>(accountingStrings.inventoryType(), ProductItem.INVENTORY_TYPE, 100) {
            @Override
            public String getCellValue(ProductItem item) {
                if (item.getParentId() != null && item.getParentId() > 0) {
                    return accountingStrings.variation();
                } else {
                    return property.getSingular(wfmStrings.product());
                }
            }
        };
        column.setShow(false);
        columnsList.add(column);

        if (Utils.hasPermission(Utils.isLogistics() ? PermissionConstants.LOGISTICS_PRODUCT_SELLING : PermissionConstants.ACCOUNTING_PRODUCT_SELLING)) {
            column = new ColumnDefinitionConfig<ProductItem, String>(wfmStrings.sellingPrice(), ProductItem.SELING_PRICE, 100) {

                @Override
                public String getCellValue(ProductItem item) {
                    return item.getUnitpPrice() != null ? AccountingUtils.get().formatUnitPrice(item.getUnitpPrice()) : "";
                }
            };
            column.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
            columnsList.add(column);
        }

        if (Utils.hasPermission(Utils.isLogistics() ? PermissionConstants.LOGISTICS_PRODUCT_COST : PermissionConstants.ACCOUNTING_PRODUCT_COST)) {
            column = new ColumnDefinitionConfig<ProductItem, String>(wfmStrings.purchasePrice(), ProductItem.COST_PRICE, 100) {

                @Override
                public String getCellValue(ProductItem item) {
                    return item.getCostPrice() != null ? AccountingUtils.get().formatUnitPrice(item.getCostPrice()) : "";
                }
            };
            column.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
            columnsList.add(column);
        }

        column = new ColumnDefinitionConfig<ProductItem, Widget>(accountingStrings.onHand(), ProductItem.ITEMS_IN_STOCK, 100) {

            @Override
            public Widget getCellValue(final ProductItem item) {
                SimpleLink a = null;
                if (warehouseLookUp != null && warehouseLookUp.getSelectedItemID() != null) {
                    a = new SimpleLink(item.getItemsInWarehouse().compareTo(BigDecimal.ZERO) < 0 ?
                            "(" + AccountingUtils.get().formatQty(new BigDecimal(-1).multiply(item.getItemsInWarehouse())) + ")" :
                            AccountingUtils.get().formatQty(item.getItemsInWarehouse()));
                } else {
                    a = new SimpleLink(item.getItemsInStock().compareTo(BigDecimal.ZERO) < 0 ?
                            "(" + AccountingUtils.get().formatQty(new BigDecimal(-1).multiply(item.getItemsInStock())) + ")" :
                            AccountingUtils.get().formatQty(item.getItemsInStock()));
                }
                a.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("clickedreport|stockValuation/" + item.getObjectId()));
                return a;
            }
        };
        column.setColumnSortable(false);
        column.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
        column.setColumnSortable(true);
        columnsList.add(column);

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
        column.setShow(false);
        column.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
        column.setColumnSortable(false);
        columnsList.add(column);

        column = new ColumnDefinitionConfig<ProductItem, Widget>(accountingStrings.onPurchaseOrder(), ProductItem.ON_PURCHASE_ORDER, 100) {

            @Override
            public Widget getCellValue(ProductItem item) {
                SimpleLink onPurchase = new SimpleLink(AccountingUtils.get().formatQty(item.getOnPurchaseOrder() != null ? item.getOnPurchaseOrder() : BigDecimal.ZERO));
                if (item.getOnPurchaseOrder() != null) {
                    onPurchase.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("product|purchaseorder/" + item.getObjectId()));
                }
                return onPurchase;
            }
        };
        column.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
        column.addStyleAttribute("paddingRight", "5px");
        column.setColumnSortable(false);
        column.setShow(false);
        columnsList.add(column);

        column = new ColumnDefinitionConfig<ProductItem, Widget>(accountingStrings.onSaleOrder(), ProductItem.SALE_ORDER_QTY, 100) {

            @Override
            public Widget getCellValue(ProductItem item) {
                SimpleLink onSales = new SimpleLink(AccountingUtils.get().formatQty(item.getOnSaleOrderQty()));
                if (item.getOnSaleOrderQty() != null) {
                    onSales.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("product|saleorder/" + item.getObjectId()));
                }
                return onSales;
            }
        };
        column.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
        column.addStyleAttribute("paddingRight", "5px");
        column.setColumnSortable(false);
        column.setShow(false);
        columnsList.add(column);

        column = new ColumnDefinitionConfig<ProductItem, Widget>(Property.get(Constants.SUPPLIER_LIST, wfmStrings.supplier()), ProductItem.Vendor, 150) {
            @Override
            public Widget getCellValue(ProductItem item) {
                VerticalPanel simpleLinks = new VerticalPanel();
                if (item.getSuppliers() != null) {
                    item.getSuppliers();
                    for (SelectItem supplier : item.getSuppliers()) {
                        SimpleLink simpleLink = new SimpleLink(supplier.getName());
                        simpleLink.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("suppliersummary|summary/" + supplier.getId(), supplier.getDescription()));
                        simpleLinks.add(simpleLink);
                    }
                }
                return simpleLinks;
            }
        };
        column.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        column.addStyleAttribute("paddingRight", "5px");
        column.setColumnSortable(true);
        column.setShow(false);
        columnsList.add(column);

        /*---------------------------------------------------------------------------------------------*/

        final DropDownCellEditor<SelectItem> cellEditor = new DropDownCellEditor<SelectItem>(150) {
            @Override
            protected SelectItem getValue() {
                return getListBox().getSelectedItem();
            }

            @Override
            protected void setValue(SelectItem cellValue) {
                getListBox().setAllowFirstItem(true);
                if (getListBox().getItems() == null || getListBox().getItems().length < 1) {
                    getListBox().setItems(categoryItems);
                }
                getListBox().setSelectedIndex(0);
                if (cellValue == null || cellValue.getId() == null) {
                    if (cellValue != null && cellValue.getName() != null) {
                        getListBox().setSelectedByValue(cellValue.getName());
                    } else {
                        getListBox().setSelectedNullLabel();
                    }
                } else {
                    getListBox().setSelected(cellValue.getId());
                }
            }
        };

        column = new ColumnDefinitionConfig<ProductItem, SelectItem>(wfmStrings.category(), ProductItem.Category, 100) {
            @Override
            public SelectItem getCellValue(ProductItem item) {
                return new SelectItem(item.getCategoryId(), item.getCategory());
            }

            @Override
            public void setCellValue(ProductItem rowValue, SelectItem cellValue) {
                rowValue.setCategoryId(cellValue != null ? cellValue.getId() : null);
                rowValue.setCategory(cellValue != null ? cellValue.getName() : null);
                saveCellValue(rowValue);
            }
        };

        cellEditor.getListBox().setItems(categoryItems);
        column.setCellChangesSave((CellChange<ProductItem>) (rowValue, columnCodeName) -> updateProductCategory(rowValue));
        column.setCellEditor(cellEditor);

        column.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        column.addStyleAttribute("paddingRight", "5px");
        column.setColumnSortable(true);
        column.setShow(false);
        columnsList.add(column);

        /*---------------------------------------------------------------------------------------------*/

        final DropDownCellEditor<SelectItem> unitMeasurementCellEditor = new DropDownCellEditor<SelectItem>(150) {
            @Override
            protected SelectItem getValue() {
                return getListBox().getSelectedItem();
            }

            @Override
            protected void setValue(SelectItem cellValue) {
                getListBox().setAllowFirstItem(true);
                if (getListBox().getItems() == null || getListBox().getItems().length < 1) {
                    getListBox().setItems(unitMeasurementItems);
                }
                getListBox().setSelectedIndex(0);
                if (cellValue == null || cellValue.getId() == null) {
                    if (cellValue != null && cellValue.getName() != null) {
                        getListBox().setSelectedByValue(cellValue.getName());
                    } else {
                        getListBox().setSelectedNullLabel();
                    }
                } else {
                    getListBox().setSelected(cellValue.getId());
                }
            }
        };

        column = new ColumnDefinitionConfig<ProductItem, SelectItem>(wfmStrings.unitMeasurement(), ProductItem.UNIT_MEASUREMENT_NAME, 100) {
            @Override
            public SelectItem getCellValue(ProductItem item) {

                unitMeasurementCellEditor.setVisible(!OTHER_CHARGE_STR.equals(item.getTypeName()) && !PRODUCT_KIT_STR.equals(item.getTypeName()));
                return new SelectItem(item.getUnitMeasurementId(), item.getUnitMeasurementName());
            }

            @Override
            public void setCellValue(ProductItem rowValue, SelectItem cellValue) {
                rowValue.setUnitMeasurementId(cellValue != null ? cellValue.getId() : null);
                rowValue.setUnitMeasurementName(cellValue != null ? cellValue.getName() : null);
                saveCellValue(rowValue);
            }
        };
        column.setCellChangesSave((CellChange<ProductItem>) (rowValue, columnCodeName) -> updateProductMeasurement(rowValue));
        column.setCellEditor(unitMeasurementCellEditor);

        column.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        column.addStyleAttribute("paddingRight", "5px");
        column.setColumnSortable(true);
        column.setShow(false);
        columnsList.add(column);

        if (customSettings != null && customSettings.containsKey(CUSTOM_ASSEMBLY_ITEMS_DESCRIPTION) && "YES".equals(customSettings.get(CUSTOM_ASSEMBLY_ITEMS_DESCRIPTION))) {
            column = new ColumnDefinitionConfig<ProductItem, String>(wfmStrings.inbox(), ProductItem.IN_BOX, 100) {
                @Override
                public String getCellValue(ProductItem item) {
                    NewProductCustomDescription subItem = (item.getSubItemsData() != null && item.getSubItemsData().size() > 0 && item.getSubItemsData().get(0) != null) ? item.getSubItemsData().get(0) : null;
                    return AccountingUtils.get().formatQty((subItem != null && subItem.getQty() != null) ? subItem.getQty() : BigDecimal.ZERO);
                }
            };
            column.addStyleAttribute("paddingRight", "5px");
            column.setColumnSortable(false);
            column.setShow(false);
            columnsList.add(column);

            column = new ColumnDefinitionConfig<ProductItem, String>(wfmStrings.total(), ProductItem.TOTAL_SUB_QTY, 100) {
                @Override
                public String getCellValue(ProductItem item) {
                    NewProductCustomDescription subItem = (item.getSubItemsData() != null && item.getSubItemsData().size() > 0 && item.getSubItemsData().get(0) != null) ? item.getSubItemsData().get(0) : null;
                    return AccountingUtils.get().formatQty(item.getItemsInStock().multiply((subItem != null && subItem.getQty() != null) ? subItem.getQty() : BigDecimal.ZERO));
                }
            };
            column.addStyleAttribute("paddingRight", "5px");
            column.setColumnSortable(false);
            column.setShow(false);
            columnsList.add(column);
        }
        column = new ColumnDefinitionConfig<ProductItem, Widget>(wfmStrings.picture(), ProductItem.PICTURE, 60) {
            @Override
            public Widget getCellValue(final ProductItem item) {
                Image attachment = item.getDefaultPictureMiniUrl() != null ? new Image(item.getDefaultPictureMiniUrl()) : new Image(imageBundle.attachment());
                attachment.getElement().setAttribute("height", "auto");
                attachment.getElement().setAttribute("width", "80%");
                attachment.getElement().setAttribute("max-height", "40px");
                if (item.getDefaultPictureUrl() != null) {
                    attachment.addClickHandler(clickEvent -> {
                        Image productImage = new Image(item.getDefaultPictureUrl());
                        final KpiModal dialogBox = new KpiModal();
                        dialogBox.setCloseButton(true);
                        VerticalPanel imagePanel = new VerticalPanel();
                        imagePanel.setStyleName("imagePanel");
                        imagePanel.setSpacing(2);
                        imagePanel.add(productImage);
                        dialogBox.add(imagePanel);
                        dialogBox.center();
                    });
                    return attachment;
                } else {
                    return new HTML("");
                }
            }
        };
        column.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        column.addStyleAttribute("paddingRight", "5px");
        column.setColumnSortable(false);
        column.setShow(false);
        columnsList.add(column);

        column = new ColumnDefinitionConfig<ProductItem, String>(wfmStrings.barcode(), ProductItem.BARCODE, 100) {
            @Override
            public String getCellValue(ProductItem item) {
                return item.getBarCodeString() != null ? item.getBarCodeString() : "N/A";
            }
        };
        column.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        column.addStyleAttribute("paddingRight", "5px");
        column.setColumnSortable(false);
        column.setShow(false);
        columnsList.add(column);

        column = new ColumnDefinitionConfig<ProductItem, String>(wfmStrings.partNumber(), ProductItem.PART_NUMBER, 100) {
            @Override
            public String getCellValue(ProductItem item) {
                return item.getPartNumber();
            }
        };
        column.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        column.addStyleAttribute("paddingRight", "5px");
        column.setColumnSortable(false);
        column.setShow(false);
        columnsList.add(column);

        if (Utils.hasPermission(Utils.isLogistics() ? PermissionConstants.LOGISTICS_PRODUCT_AVARAGE_COST : PermissionConstants.ACCOUNTING_PRODUCT_AVARAGE_COST)) {
            column = new ColumnDefinitionConfig<ProductItem, String>(accountingStrings.averageCost(), ProductItem.AVERAGE_COST + "_", 100) {

                @Override
                public String getCellValue(ProductItem item) {
                    return item.getAverageCost() != null ? AccountingUtils.get().formatUnitPrice(item.getAverageCost()) : "N/A";
                }
            };
            column.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
            column.addStyleAttribute("paddingRight", "5px");
            column.setShow(false);
            columnsList.add(column);
        }

        column = new ColumnDefinitionConfig<ProductItem, String>(wfmStrings.brand(), ProductItem.BRAND, 100) {
            @Override
            public String getCellValue(ProductItem item) {
                return item.getBrand();
            }
        };
        column.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        column.addStyleAttribute("paddingRight", "5px");
        column.setColumnSortable(false);
        column.setShow(false);
        columnsList.add(column);

        column = new ColumnDefinitionConfig<ProductItem, String>(wfmStrings.createdBy(), ProductItem.CREATOR, 100) {
            @Override
            public String getCellValue(ProductItem item) {
                return item.getCreator() != null && !Utils.isNullOrEmpty(item.getCreator().getName()) ? item.getCreator().getName() : "";
            }
        };
        column.addStyleAttribute("paddingRight", "5px");
        column.setShow(false);
        columnsList.add(column);

        column = new ColumnDefinitionConfig<ProductItem, String>(wfmStrings.modifiedBy(), ProductItem.UPDATER, 100) {
            @Override
            public String getCellValue(ProductItem item) {
                return item.getUpdater() != null && !Utils.isNullOrEmpty(item.getUpdater().getName()) ? item.getUpdater().getName() : "";
            }
        };
        column.addStyleAttribute("paddingRight", "5px");
        column.setColumnSortable(false);
        column.setShow(false);
        columnsList.add(column);


        column = new ColumnDefinitionConfig<ProductItem, String>(wfmStrings.modifiedDate(), ProductItem.UPDATED_DATE, 100) {
            @Override
            public String getCellValue(ProductItem item) {
                return item.getUpdatedDate() != null ? DateUtils.format(item.getUpdatedDate()) : "";
            }
        };
        column.addStyleAttribute("paddingRight", "5px");
        column.setShow(false);
        columnsList.add(column);


        column = new ColumnDefinitionConfig<ProductItem, String>(wfmStrings.rentStatus(), ProductItem.RENT_STATUS, 100) {
            @Override
            public String getCellValue(ProductItem item) {
                return item.getRentStatus() != null ? item.getRentStatus() : wfmStrings.na();
            }
        };
        column.addStyleAttribute("paddingRight", "5px");
        column.setColumnSortable(false);
        column.setShow(false);
        columnsList.add(column);


        return columnsList.toArray(new ColumnDefinitionConfig[0]);
    }

    public interface Images extends ClientBundle {
        @Source("com/edatasite/workforce/gwt/messagecenter/public/images/addInvoice.png")
        ImageResource addNew();

        @Source("com/edatasite/workforce/gwt/core/resource/icons/botton-arrow.gif")
        ImageResource action();

        @Source("com/edatasite/workforce/gwt/core/resource/icons/action.png")
        ImageResource actions();
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
        return Constants.PRODUCTS_OR_SERVICES;
    }
}
