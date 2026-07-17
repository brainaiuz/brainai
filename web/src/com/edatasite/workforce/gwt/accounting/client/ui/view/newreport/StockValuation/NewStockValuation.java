package com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.StockValuation;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewProduct;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.InventoryStockData;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.InventoryStockValuation;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.InventoryStockValuationItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductService;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductServiceAsync;
import com.edatasite.workforce.gwt.accounting.client.ui.view.ProductLookUp;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBox;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxDatePeriodItem;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxItem;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.SectionBoxPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.paging.PagingWidget;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import gwt.material.design.addins.client.menubar.MaterialMenuBar;
import gwt.material.design.client.constants.Position;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Icon;
import gwt.material.design.client.ui.html.Span;

import java.math.BigDecimal;
import java.util.Date;

import static com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.ProfitAndLoss.NewProfitAndLoss.accountingMessages;


/**
 * Created by admin on 20.09.2014.
 */
public class NewStockValuation extends Composite implements Constants {
    interface NewStockValuationUiBinder extends UiBinder<HTMLPanel, NewStockValuation> {
    }

    public static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final ProductServiceAsync productService = ProductService.App.get();
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    private static final NewStockValuationUiBinder ourUiBinder = GWT.create(NewStockValuationUiBinder.class);

    private ProductLookUp productValues;
    private boolean isProductView = false;
    private Integer productId;
    private DateNonConvertable date;
    private Integer warehouseId;
    private final String currencySymbol = "";
    private DatePicker fromValue;
    private DatePicker toValue;
    private DataListBox wareHouseValues;
    private PagingWidget pagingWidget;
    private KpiModal filterDialog;
    private KpiCheckBox hideZeroAmountCheckBox;
    private MaterialLink pdfVersion;
    private MaterialLink portrait;
    private MaterialLink landscape;

    @UiField
    SectionBoxPanel headerPanel;
    @UiField
    HTMLPanel exportPanel;
    @UiField
    HeadingElement infoText;
    @UiField
    DivElement thType;
    @UiField
    DivElement thEntryDate;
    @UiField
    DivElement thTransactionDate;
    @UiField
    DivElement thName;
    @UiField
    DivElement thNo;
    @UiField
    DivElement thQty;
    @UiField
    DivElement thTransactionValue;
    @UiField
    DivElement thCostPerQty;
    @UiField
    DivElement thQuantityOnHand;
    @UiField
    DivElement thBalanceValue;
    @UiField
    Element tableBody;


    public NewStockValuation() {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
        onInitialize();
    }

    public NewStockValuation(Integer productId) {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
        this.productId = productId;
        onInitialize();
    }

    public NewStockValuation(Integer productId, Integer warehouseId, boolean isProductView) {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
        this.isProductView = isProductView;
        this.productId = productId;
        this.warehouseId = warehouseId;
        onInitialize();
    }

    public NewStockValuation(Integer productId, Integer warehouseId, boolean isProductView, DateNonConvertable date) {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
        this.isProductView = isProductView;
        this.productId = productId;
        this.warehouseId = warehouseId;
        this.date = date;
        onInitialize();
    }

    private void onInitialize() {
        GBox groupBox = headerPanel.drawNewGroupBox();
        groupBox.setStyleUnited(true);
        groupBox.setStyleWidthFree(true);

        productValues = new ProductLookUp(PAYABLE, STOCK_VALUATION_REPORT);
        productValues.showClearButton();
        productValues.ensureDebugId("stockValuation-product-lookUp");
        productValues.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
        });
        productValues.setClearCommand(() -> initInternal());

        GBoxItem gBoxProduct = new GBoxItem();
        gBoxProduct.setLabel(wfmStrings.product());
        gBoxProduct.addComponent(productValues);
        headerPanel.addGroupBoxItem(0, gBoxProduct);

        GBoxDatePeriodItem datePeriodItem = new GBoxDatePeriodItem();
        Date currentDate = new Date();
        fromValue = new DatePicker();
        fromValue.ensureDebugId("stockValuation-from-datePicker");
        datePeriodItem.setStartBoxItem(wfmStrings.from(), fromValue);
        toValue = new DatePicker();
        toValue.ensureDebugId("stockValuation-toDatePicker");
        datePeriodItem.setDueBoxItem(wfmStrings.to(), toValue);
        if (this.date != null) {
            fromValue.setDate(DateUtil.resetTime(this.date.getDate()));
            toValue.setDate(DateUtil.getDayLastTime(this.date.getDate()));
        } else {
            fromValue.setDate(DateUtil.getMonthFirstDay(currentDate));
            toValue.setDate(DateUtil.getMonthLastDate(currentDate));
        }
        headerPanel.addGroupBoxItem(datePeriodItem);

        //Filter
        initFilterPopup();

        WfmButton2 filterButton = new WfmButton2(null, WfmButton2.BTN_WHITE, "ficon--filter");
        filterButton.removeHasiconLeftStyle();
        filterButton.addClickHandler(event -> filterDialog.open());
        GBoxItem filterItem = headerPanel.addGroupBoxItem(0, null, filterButton);
        filterItem.setStyleSplitRight(true);
        filterItem.setStyleWidthFree(true);

        wareHouseValues = new DataListBox();
        wareHouseValues.ensureDebugId("stockValuation-wareHouseList");
        GBoxItem warehouseSection = headerPanel.addGroupBoxItem(accountingStrings.warehouse(), wareHouseValues);
        warehouseSection.setStyleSplitRight(true);

        WfmButton2 updateButton = new WfmButton2(wfmStrings.update(), WfmButton2.BTN_PRIMARY);
        updateButton.addClickHandler(event -> initInternal());
        GBoxItem updateItem = headerPanel.addGroupBoxItem(null, updateButton);
        updateItem.setStyleSplitRight(true);
        updateItem.setStyleWidthFree(true);
        exportSection();

        pagingWidget = new PagingWidget();
        pagingWidget.setLimit(20);
        pagingWidget.setPaging(getPagingLoader());

        GBoxItem paging = headerPanel.addGroupBoxItem(null, pagingWidget);
        paging.addStyleToComponent("paging-group__wrapper");
        paging.setStyleSplitRight(true);
        paging.addStyleName("ml-auto");


        thType.setInnerHTML(wfmStrings.type());
        thEntryDate.setInnerHTML(wfmStrings.entryDate());
        thTransactionDate.setInnerHTML(accountingStrings.transactionDate());
        thName.setInnerHTML(wfmStrings.name());
        thNo.setInnerHTML(wfmStrings.number());
        thQty.setInnerHTML(wfmStrings.qty());
        thTransactionValue.setInnerHTML(accountingStrings.transactionValue());
        thCostPerQty.setInnerHTML(wfmStrings.costPerQty());
        thQuantityOnHand.setInnerHTML(wfmStrings.qtyOnHand());
        infoText.setInnerHTML(accountingStrings.updateToSeeResults());

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PRODUCTSERVICE_SAVED, NewStockValuation.this, (sender, args) -> initInternal());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_INVOICE_VOID, NewStockValuation.this, (sender, args) -> initInternal());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SALEINVOICE_ADDED, NewStockValuation.this, (sender, args) -> initInternal());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PURCHASEINVOICE_ADDED, NewStockValuation.this, (sender, args) -> initInternal());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_INVOICEPAYMENT_CHANGE, NewStockValuation.this, (sender, args) -> initInternal());

        if (Utils.isMultiWarehouseEnabled()) {
            AccountingService.App.get().getWarehousesForLookUp(null, new AsyncCallback<SelectItem[]>() {
                @Override
                public void onFailure(Throwable caught) {
                }

                @Override
                public void onSuccess(SelectItem[] result) {
                    if (result != null) {
                        wareHouseValues.setItems(result);
                    }

                    if (productId != null) {
                        getProductForFilter();
                    } else {
                        initInternal();
                    }
                }
            });
        } else {
            warehouseSection.removeFromParent();
            if (productId != null) {
                getProductForFilter();
            } else {
                initInternal();
            }
        }
    }

    private void getProductForFilter() {
        productService.getProduct(productId, new AbstractAsyncCallback<NewProduct>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(NewProduct result) {
                productValues.addItem(new SelectItem(productId, result.getItemName()));
                initInternal();
            }
        });
    }


    private void initInternal() {
        infoText.setInnerHTML(" ");
        if (validate()) {
            LoadingPanel.loading(true);
            pagingWidget.resetAndReload();
        }
    }


    private PagingWidget.Paging getPagingLoader() {
        return (start, limit) -> {
            ListingFilterParameter filter = getFilterData();
            filter.setStart(start - 1);
            productService.getStockValuations(filter, Utils.getStartDateNC(fromValue.getDate()), Utils.getEndDateNC(toValue.getDate()), new AbstractAsyncCallback<InventoryStockData>() {
                @Override
                public void failure(Throwable throwable) {
                    LoadingPanel.loading(false);
                    throwable.printStackTrace();
                }

                @Override
                public void success(InventoryStockData stockData) {
                    LoadingPanel.loading(false);
                    tableBody.removeAllChildren();
                    InventoryStockValuation[] inventoryStockValuations = stockData.getStockValuations();
                    pagingWidget.setTotalCount(stockData.getTotalCount());
                    if (inventoryStockValuations != null && inventoryStockValuations.length > 0) {

                        thBalanceValue.setInnerHTML(accountingMessages.balanceValue("(" + stockData.getCurrency().getName() + ")"));
                        createBalanceHeader(tableBody, wfmStrings.beginningBalance(), stockData.getBeginningBalance());

                        for (InventoryStockValuation inventoryStockValuation : inventoryStockValuations) {
                            createGroupHeader(tableBody, inventoryStockValuation.getProductCode(), inventoryStockValuation.getName());
                            //inventory stock beginning balance and quantity
                            BigDecimal balanceValue = inventoryStockValuation.getBeginningBalance();
                            BigDecimal qtyOnHande = inventoryStockValuation.getBeginningQty();
                            createSubGroupHeader(tableBody, wfmStrings.beginningBalance(), qtyOnHande, balanceValue);

                            InventoryStockValuationItem[] stockValuationItems = inventoryStockValuation.getStockValuationItems();
                            if (stockValuationItems != null) {
                                for (final InventoryStockValuationItem item : stockValuationItems) {
                                    Boolean isNegative = false;
                                    Element tr = DOM.createTR();
                                    tr.addClassName("set_unit_2_row");
                                    Element td = DOM.createTD();
                                    tr.appendChild(td);
                                    Element link = DOM.createAnchor();
                                    DOM.sinkEvents(link.cast(), Event.ONCLICK);
                                    EventListener eventListener = null;
                                    if (item.getTransactionType() != null && item.getTransactionType().equals(TT_STOCK_ADJUSTMENT)) {
                                        link.setInnerHTML(TT_STOCK_ADJUSTMENT_STR);
                                        eventListener = event -> SinksContainerFactory.entryPoint.onHistoryChanged("stockadjustment|summary/" + item.getItemId());
                                    } else if (TT_STOCK_TRANSFER.equals(item.getTransactionType())) {
                                        link.setInnerHTML(accountingStrings.stockTransfer());
                                        eventListener = event -> SinksContainerFactory.entryPoint.onHistoryChanged("stocktransfer|summary/" + item.getItemId());
                                    } else if (item.getTransactionType() != null && item.getTransactionType().equals(TT_OPENING_BALANCE)) {
                                        td.setInnerHTML(TT_OPENING_BALANCE_STR);
                                    } else if (item.getTransactionType() != null && item.getTransactionType().equals(TT_BUILD_ASSEMBLY)) {
                                        if (item.getItemId() != null) {
                                            eventListener = event -> SinksContainerFactory.entryPoint.onHistoryChanged("buildAssembly|summary/" + item.getItemId(), item.getNumber());
                                            link.setInnerHTML(accountingStrings.buildAssembly());
                                        } else {
                                            td.setInnerHTML(TT_BUILD_ASSEMBLY_STR);
                                        }                                    } else if (item.getTransactionType() != null && item.getTransactionType().equals(TT_GOODS_RECEIVED)) {
                                        eventListener = event -> {
                                            if (item.getShippingDataId() != null) {
                                                SinksContainerFactory.entryPoint.onHistoryChanged("grn|summary/" + item.getShippingDataId(), item.getNumber());
                                            } else if (item.getItemId() != null) {
                                                SinksContainerFactory.entryPoint.onHistoryChanged("purchaseorder/" + item.getItemId(), item.getNumber());
                                            }
                                        };
                                        link.setInnerHTML(TT_GOODS_RECEIVED_STR + " -> " + item.getShippingDataNumber());
                                    } else if (TT_GOODS_DELIVERED.equals(item.getTransactionType())) {
                                        eventListener = event -> {
                                            if (item.getShippingDataId() != null) {
                                                SinksContainerFactory.entryPoint.onHistoryChanged("gdn|summary/" + item.getShippingDataId(), item.getNumber());
                                            } else {
                                                SinksContainerFactory.entryPoint.onHistoryChanged("saleorder/" + item.getItemId(), item.getNumber());
                                            }
                                        };
                                        link.setInnerHTML(TT_GOODS_DELIVERED_STR + " -> " + item.getShippingDataNumber());
                                    } else if (item.getTransactionType() != null && item.getTransactionType().equals(TT_PURCHASE)) {
                                        eventListener = event -> SinksContainerFactory.entryPoint.onHistoryChanged("purchaseinvoice/" + item.getItemId(), item.getNumber());
                                        link.setInnerHTML(accountingStrings.purchase());
                                    } else if (item.getTransactionType() != null && item.getTransactionType().equals(TT_INVOICE)) {
                                        isNegative = true;
                                        eventListener = event -> SinksContainerFactory.entryPoint.onHistoryChanged("saleinvoice/" + item.getItemId(), item.getNumber());
                                        link.setInnerHTML(accountingStrings.invoice());
                                    } else if (item.getTransactionType() != null && item.getTransactionType().equals(TT_CUSTOMER_CREDIT_NOTE)) {
                                        eventListener = event -> SinksContainerFactory.entryPoint.onHistoryChanged("receivablecreditnote/" + item.getItemId(), item.getNumber());
                                        link.setInnerHTML(TT_CUSTOMER_CREDIT_NOTE_STR);
                                    } else if (item.getTransactionType() != null && item.getTransactionType().equals(TT_SUPPLIER_CREDIT_NOTE)) {
                                        isNegative = true;
                                        eventListener = event -> SinksContainerFactory.entryPoint.onHistoryChanged("receivablecreditnote/" + item.getItemId(), item.getNumber());
                                        link.setInnerHTML(TT_SUPPLIER_CREDIT_NOTE_STR);
                                    }
                                    if (item.getTransactionType() != null) {
                                        if (eventListener != null)
                                            DOM.setEventListener(link.cast(), eventListener);
                                        td.appendChild(link);
                                    }
                                    Element td1 = DOM.createTD();
                                    td1.setInnerHTML(DateUtils.format(item.getEntryDate()));
                                    Element td2 = DOM.createTD();
                                    td2.setInnerHTML(DateUtils.format(item.getTransactionDate()));
                                    Element td3 = DOM.createTD();
                                    td3.setInnerHTML(item.getName() != null ? item.getName() : "-");
                                    Element td4 = DOM.createTD();
                                    td4.setInnerHTML(item.getNumber());

                                    tr.appendChild(td1);
                                    tr.appendChild(td2);
                                    tr.appendChild(td3);
                                    tr.appendChild(td4);

                                    qtyOnHande = qtyOnHande.add(item.getQty());
                                    balanceValue = balanceValue.add(item.getTransactionValue());
                                    Element td5 = DOM.createTD();
                                    Element td6 = DOM.createTD();
                                    if (isNegative || item.getQty().compareTo(BigDecimal.ZERO) < 0) {
                                        td5.setInnerHTML(currencySymbol + "(" + AccountingUtils.get().formatQty(item.getQty().multiply(new BigDecimal(-1))) + ")");
                                        td6.setInnerHTML(currencySymbol + "(" + AccountingUtils.get().formatPrice(item.getTransactionValue().multiply(new BigDecimal(-1))) + ")");
                                    } else {
                                        td5.setInnerHTML(currencySymbol + " " + AccountingUtils.get().formatQty(item.getQty()));
                                        td6.setInnerHTML(currencySymbol + " " + AccountingUtils.get().formatPrice(item.getTransactionValue()));
                                    }
                                    Element td61 = DOM.createTD();
                                    Span priceList = new Span(item.getQuantityPerPriceList());
                                    priceList.getElement().setTitle(item.getPriceListWithoutScaling());
                                    DOM.appendChild(td61, priceList.getElement());

                                    Element td7 = DOM.createTD();
                                    Element td8 = DOM.createTD();
                                    if (qtyOnHande.doubleValue() < 0) {
                                        td7.setInnerHTML(currencySymbol + "(" + AccountingUtils.get().formatQty(qtyOnHande.multiply(new BigDecimal(-1))) + ")");
                                    } else {
                                        td7.setInnerHTML(currencySymbol + " " + AccountingUtils.get().formatQty(qtyOnHande));
                                    }

                                    if (balanceValue.doubleValue() < 0) {
                                        td8.setInnerHTML(currencySymbol + "(" + AccountingUtils.get().formatPrice(balanceValue.multiply(new BigDecimal(-1))) + ")");
                                    } else {
                                        td8.setInnerHTML(currencySymbol + " " + AccountingUtils.get().formatPrice(balanceValue));
                                    }
                                    td5.addClassName("text-right");
                                    td6.addClassName("text-right");
                                    td61.addClassName("text-right");
                                    td7.addClassName("text-right");
                                    td8.addClassName("text-right");

                                    tr.appendChild(td5);
                                    tr.appendChild(td6);
                                    tr.appendChild(td61);
                                    tr.appendChild(td7);
                                    tr.appendChild(td8);

                                    tableBody.appendChild(tr);
                                }
                            }
                            createSubGroupHeader(tableBody, wfmStrings.endingBalance(), qtyOnHande, balanceValue);
                        }
                        createBalanceHeader(tableBody, wfmStrings.endingBalance(), stockData.getEndingBalance());

                    }
                    Utils.table__frame_affix_init();
                }
            });
        };
    }

    private void createGroupHeader(Element element, String code, String name) {
        Element tr = DOM.createTR();
        tr.addClassName("set_head_row");
        Element td = DOM.createTD();
        td.setInnerHTML(code + " -> " + name);
        td.setAttribute("colspan", "10");
        tr.appendChild(td);
        element.appendChild(tr);
    }

    private void createBalanceHeader(Element element, String title, BigDecimal balanceValue) {
        Element tr = DOM.createTR();
        tr.addClassName("set_head_row");
        Element td = DOM.createTD();
        td.setInnerHTML(title);
        td.setAttribute("colspan", "9");
        tr.appendChild(td);
        Element td1 = DOM.createTD();
        td1.setInnerHTML(getPriceAsString(balanceValue));
        td1.setClassName("text-right");
        tr.appendChild(td1);
        element.appendChild(tr);
    }

    private void createSubGroupHeader(Element element, String title, BigDecimal qtyOnHand, BigDecimal balanceValue) {
        Element tr = DOM.createTR();
        tr.addClassName("set_head_2_row");
        Element td = DOM.createTD();
        td.setInnerHTML(title);
        td.setAttribute("colspan", "8");
        tr.appendChild(td);
        Element td1 = DOM.createTD();
        td1.setInnerHTML(getQtyAsString(qtyOnHand));
        td1.setClassName("text-right");
        tr.appendChild(td1);
        Element td2 = DOM.createTD();
        td2.setInnerHTML(getPriceAsString(balanceValue));
        td2.setClassName("text-right");
        tr.appendChild(td2);
        element.appendChild(tr);
    }

    private String getPriceAsString(BigDecimal value) {
        if (value.compareTo(BigDecimal.ZERO) >= 0) {
            return AccountingUtils.get().formatPrice(value);
        } else {
            return "(" + AccountingUtils.get().formatPrice(value.abs()) + ")";
        }
    }

    private String getQtyAsString(BigDecimal value) {
        if (value.compareTo(BigDecimal.ZERO) >= 0) {
            return AccountingUtils.get().formatQty(value);
        } else {
            return "(" + AccountingUtils.get().formatQty(value.abs()) + ")";
        }
    }

    private boolean validate() {
        int errors = 0;
        fromValue.removeStyleName(ERROR_FORM_STYLE);
        toValue.removeStyleName(ERROR_FORM_STYLE);

        if (!Validation.validateDate(fromValue)) {
            errors++;
        }
        if (!Validation.validateDate(toValue)) {
            errors++;
        }

        if (!Validation.validateDateOrder(fromValue, toValue)) {
            return false;
        }

        return errors == 0;
    }

    private void initFilterPopup() {
        filterDialog = new KpiModal();
        filterDialog.setWidth(400);
        filterDialog.setCloseButton(true);
        filterDialog.setDismissible(false);

        MaterialPanel contentPanel = new MaterialPanel();

        hideZeroAmountCheckBox = new KpiCheckBox();
        hideZeroAmountCheckBox.setText(accountingStrings.hideZeroAmounts());
        hideZeroAmountCheckBox.ensureDebugId("hideZeroAmountCheckBox");

        FormGroup excludeZeroFormGroup = new FormGroup(hideZeroAmountCheckBox);
        contentPanel.add(excludeZeroFormGroup);

        WfmButton2 resetButton = new WfmButton2(wfmStrings.reset(), WfmButton2.BTN_DEFAULT);

        resetButton.addClickHandler(clickEvent -> hideZeroAmountCheckBox.setValue(false));

        WfmButton2 applyFilterButton = new WfmButton2(wfmStrings.apply(), WfmButton2.BTN_SUCCESS);
        applyFilterButton.addClickHandler(clickEvent -> {
            filterDialog.close();
            initInternal();
        });

        filterDialog.add(contentPanel);
        filterDialog.addButton(resetButton);
        filterDialog.addButton(applyFilterButton);
    }

    private ListingFilterParameter getFilterData() {
        ListingFilterParameter filter = new ListingFilterParameter();
        filter.setLimit(20);
        filter.setStartDateNC(Utils.getStartDateNCForFilter(fromValue.getDate()));
        filter.setEndDateNC(Utils.getEndDateNCForFilter(toValue.getDate()));
        if (productValues.getSelectedItemID() != null && productValues.getSelectedItemID() > 0) {
            filter.setCaseID(productValues.getSelectedItemID());
        } else if (!isProductView && productId != null) {
            filter.setCaseID(productId);
        } else {
            filter.setCaseID(null);
        }
        filter.setWarehouseID(wareHouseValues.getSelectedId());
        filter.setShortList(hideZeroAmountCheckBox.getValue());
        return filter;
    }

    private void exportSection() {
        MaterialMenuBar showMenuBar = new MaterialMenuBar();
        showMenuBar.setClass("dropdown-kit--arrow--below");

        MaterialLink showLink = new MaterialLink();
        showLink.addStyleName("btn btn--white btn--icon");

        Icon ieIcon = new Icon();//import/export icon for listing top panel
        ieIcon.setClass("ficon--download-cloud");
        showLink.add(ieIcon);

        MaterialDropDown showMenuContainer = new MaterialDropDown(showLink);
        showMenuContainer.setClass("dropdown-content--2 dropdown-content--export");
        showMenuContainer.setBelowOrigin(true);
        showLink.add(showMenuContainer);

        pdfVersion = getPdfVersion();
        pdfVersion.ensureDebugId("pdf_button");

        Div wrapper = new Div("java-wrap");
        showMenuContainer.add(wrapper);

        MaterialLink pdfVersion = getPdfVersion();
        wrapper.add(pdfVersion);

        MaterialDropDown mdp = new MaterialDropDown(pdfVersion);
        mdp.setHover(true);
        mdp.setHoverable(true);

        mdp.add(NewStockValuation.this::getPortraitLink);
        mdp.add(NewStockValuation.this::getLandscapeLink);

        wrapper.add(mdp);

        setPDFListener();

        MaterialLink exportExl = new MaterialLink();
        exportExl.addStyleName("hasicon--left");
        Icon exlIcon = new Icon();
        exlIcon.setClass("ficon--file-excel");
        exportExl.add(exlIcon);
        exportExl.setText(wfmStrings.excel());
        exportExl.addClickHandler(ch -> {
            if (validate()) {
                String URL = (CommandConstants.COMMON_URL + "/stockValuationExcelHandler");
                ListingFilterParameter filter = new ListingFilterParameter();
                filter.setPropertyCode("stockValuation");
                filter.setStartDateNC(Utils.getStartDateNCForFilter(fromValue.getDate()));
                filter.setEndDateNC(Utils.getEndDateNCForFilter(toValue.getDate()));
                if (productValues.getSelectedItemID() != null && productValues.getSelectedItemID() > 0) {
                    filter.setCaseID(productValues.getSelectedItemID());
                } else if (!isProductView && productId != null) {
                    filter.setCaseID(productId);
                } else {
                    filter.setCaseID(null);
                }
                filter.setWarehouseID(wareHouseValues.getSelectedId());
                filter.setShortList(hideZeroAmountCheckBox.getValue());
                Utils.sendPDFOrExcelRequest(exportPanel, URL, filter.getRequestParams(), "_blank");
            }
        });
        showMenuContainer.add(exportExl);


        showMenuBar.add(showLink);

        Div div = new Div();
        new KpiToolTip(showMenuBar, wfmStrings.export(), Position.TOP);
        div.add(showMenuBar);
        headerPanel.addGroupBoxItem(0, null, div);
    }

    public MaterialLink getPdfVersion() {

        if (pdfVersion == null) {
            pdfVersion = new MaterialLink();
            MaterialIcon pdfIcon = new MaterialIcon();
            pdfIcon.setStylePrimaryName("ficon--file-pdf hasicon--left");
            pdfVersion.add(pdfIcon);
            pdfVersion.setText(wfmStrings.pdf());
        }
        return pdfVersion;
    }

    private MaterialLink getPortraitLink() {
        if (portrait == null) {
            portrait = new MaterialLink();
            portrait.setText(wfmStrings.portrait());
        }
        return portrait;
    }

    private MaterialLink getLandscapeLink() {
        if (landscape == null) {
            landscape = new MaterialLink();
            landscape.setText(wfmStrings.landscape());
        }
        return landscape;
    }

    public void setPDFListener() {
        getPortraitLink().addClickHandler((event) -> {
            sendPdfRequest(false);
        });
        getLandscapeLink().addClickHandler((event) -> {
            sendPdfRequest(true);
        });
    }

    private void sendPdfRequest(boolean landscape) {
        if (validate()) {
            String URL = (CommandConstants.PDF_URL + "/stockValuationPDFHandler");
            ListingFilterParameter filter = new ListingFilterParameter();
            filter.setLandscape(landscape);
            filter.setPropertyCode("stockValuation");
            filter.setStartDateNC(Utils.getStartDateNCForFilter(fromValue.getDate()));
            filter.setEndDateNC(Utils.getEndDateNCForFilter(toValue.getDate()));
            if (productValues.getSelectedItemID() != null && productValues.getSelectedItemID() > 0) {
                filter.setCaseID(productValues.getSelectedItemID());
            } else if (!isProductView && productId != null) {
                filter.setCaseID(productId);
            } else {
                filter.setCaseID(null);
            }
            filter.setWarehouseID(wareHouseValues.getSelectedId());
            filter.setShortList(hideZeroAmountCheckBox.getValue());
            Utils.sendPDFOrExcelRequest(exportPanel, URL, filter.getRequestParams(), "_blank");
        }
    }
}
