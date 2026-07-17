package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.pricelevel;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.enums.PriceLevelOperationTypeEnum;
import com.edatasite.workforce.gwt.accounting.client.rpc.priceLevel.PriceLevelItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.priceLevel.PriceLevelPPItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.priceLevel.PriceLevelService;
import com.edatasite.workforce.gwt.accounting.client.rpc.priceLevel.PriceLevelServiceAsync;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.CustomerLookUpItem;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.ExtendedCommand;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.edatasite.workforce.gwt.core.client.ui.customlist.CustomList;
import com.edatasite.workforce.gwt.core.client.ui.customlist.CustomListItem;
import com.edatasite.workforce.gwt.core.client.ui.customlist.Design;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.HasObjectPermissionCustomForm;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: Jan 25, 2011
 * Time: 12:12:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class EditPriceLevelView extends HasObjectPermissionCustomForm implements Constants, AccountingConstants, Colapse {

    private PriceLevelServiceAsync priceLevelService = PriceLevelService.App.get();
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private Integer calculationScale = Utils.getAccountingCalculationScale() != null ? Utils.getAccountingCalculationScale() : 2;

    //Form fields
    FormGroup operationType;
    FormGroup currency;
    FormGroup name;
    FormGroup type;
    FormGroup percentage;
    FormGroup clientField;
    FormGroup clientTypes;
    FormGroup productsTable;
    FormGroup brandsTable;
    private TextBox txtName;
    private TextBox txtPercentage;
    private DataListBox dwType;
    private DataListBox dwCase;
    private DataListBox dwCurrency;
    private DataListBox dwOperationType;

    private WPLPerProduct wPerProduct;
    private WPLPerBrand wPerBrand;

    private VerticalPanel customerSupplierPanel;
    private CustomerLookUpItem customerLookUpItem;
    private KpiCheckBox checkAll;
    private CustomList clientTypeList;

    private HorizontalPanel pnlWrap;

    private Command closeCommand;
    private ExtendedCommand provider;
    private Integer clientID;
    private Integer clientCurrencyID;
    private Integer objectID;

    private PriceLevelItem priceLevelItem = new PriceLevelItem();
    private SelectItem[] appliedClients;
    private String editPriceLevelView = "edit_price_level_view_";
    boolean isCopy;

    /**
     * add price level case
     * @param integer
     * @param copy
     */
    public EditPriceLevelView() {
        super("addpriceLevel", wfmStrings.add() + " " + wfmStrings.priceLevel());
    }

    /**
     * edit price level case by object id
     *
     * @param objectID
     */
    public EditPriceLevelView(Integer objectID, boolean isCopy) {
        super("edit", wfmStrings.edit() + " " + wfmStrings.priceLevel());
        this.isCopy = isCopy;
        this.objectID = objectID;
    }

    @Override
    protected PriceLevelItem getObjectPermissionForEdit() {
        return priceLevelItem;
    }

    @Override
    protected void getDataToFillFields() {
        loadData();
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.PRICE_LEVEL_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
    }

    @Override
    protected void addButtons() {

  WfmButton2 printBarcode = new WfmButton2(wfmStrings.printBarcode(), WfmButton2.BTN_PRIMARY);
        printBarcode.addClickHandler(clickEvent -> {
            LinkedHashMap<String, String> params = new LinkedHashMap<>();


            JSONArray products = new JSONArray();
            WPLPerProductLarge productLarge = new WPLPerProductLarge(priceLevelItem.getId(), true);
            Timer t = new Timer() {
                @Override
                public void run() {
                    for (int i = 0; i< productLarge.getProductList().size(); i++){
                        PriceLevelPPItem ppItem = productLarge.getProductList().get(i);
                        JSONObject product = new JSONObject();
                        product.put("productNumber", new JSONString(ppItem.getProductID().toString()));
                        product.put("name", new JSONString(ppItem.getProductName()));
                        product.put("unitpPrice", new JSONString(ppItem.getCustomPrice() != null ? ppItem.getCustomPrice().toString() : ""));
                        products.set(i, product);
                    }
                    params.put("products", products.toString());
                    String pdfUrl = CommandConstants.PDF_URL + "/productsBarcodePDFHandler";
                    Utils.sendPDFOrExcelRequest(panel, pdfUrl, params, "_blank");
                }
            };
            t.schedule(1000);
        });
        addButton(printBarcode);

        btnSavePriceLevel = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        btnSavePriceLevel.ensureDebugId(editPriceLevelView + "btnSavePriceLevel");

        btnSavePriceLevel.addClickHandler(clickEvent -> {
            if (validate()) {
                setEnabledButtons(false);
                LoadingPanel.loading(true);
                save();
            }
        });
        addButton(btnSavePriceLevel);
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        initInternal();

        return this;
    }

    protected void initInternal() {
        super.initInternal();
        addTitleField(PRICE_LEVEL_TITLE, accountingStrings.addEditPriceLevel());

        txtName = new TextBox();
        txtName.ensureDebugId(editPriceLevelView + "txtName");

        txtPercentage = new TextBox();
        txtPercentage.ensureDebugId(editPriceLevelView + "txtPercentage");

        txtPercentage.setText(AccountingUtils.get().format(ZERO));
        txtPercentage.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        Validation.addNumericKeyboardListener(txtPercentage, 2);
        Validation.checkToFocusTextBox(txtPercentage, AccountingUtils.get().format(ZERO));
        txtPercentage.addKeyUpHandler(event -> {
            BigDecimal percentage = ZERO;
            try {
                percentage = AccountingUtils.get().parseToBigDecimal(txtPercentage.getText());
            } catch (NumberFormatException ex) {
                txtPercentage.setText(AccountingUtils.get().format(ZERO));
            } finally {
                if (percentage.compareTo(HUNDRED) > 0) {
                    txtPercentage.setText(txtPercentage.getText().substring(0, 2));
                }
            }
        });

        customerLookUpItem = new CustomerLookUpItem(Design.CHECK, true);
        customerLookUpItem.ensureDebugId(editPriceLevelView + "customerLookUpItem");
        customerLookUpItem.setSearchText(wfmStrings.search());
        customerLookUpItem.setHeight(280);
        checkAll = new KpiCheckBox("<b>" + wfmStrings.selectAll() + "</b>");
        checkAll.addValueChangeHandler(booleanValueChangeEvent -> customerLookUpItem.setCheckAllItems(booleanValueChangeEvent.getValue()));

        dwType = new DataListBox();
        dwType.ensureDebugId(editPriceLevelView + "dwType");
        dwType.setItems(new SelectItem[]{new SelectItem(FIXED_PERCENTAGE, accountingStrings.fixedPercentage()), new SelectItem(PER_PRODUCT, accountingStrings.perProduct()), new SelectItem(BY_BRAND, accountingStrings.byBrand())});
        dwType.addValueChangeHandler(changeEvent -> {
            onChageTypes();
        });

        dwCase = new DataListBox();
        dwCase.ensureDebugId(editPriceLevelView + "dwCase");
        dwCase.setWithoutNullLabel(true);
        dwCase.setWidth("100px");
        dwCase.setItems(new SelectItem[]{new SelectItem(DECREASE, accountingStrings.decrease()), new SelectItem(INCREASE, accountingStrings.increase())});

        dwCurrency = new DataListBox();
        dwCurrency.ensureDebugId(editPriceLevelView + "dwCurrency");
        dwCurrency.addValueChangeHandler(event -> {

            if (dwCurrency.getSelectedId() != null) {
                customerLookUpItem.setCurrencyID(dwCurrency.getSelectedId());
                customerLookUpItem.setSpecialOffer(dwOperationType.getSelectedId() != null && PriceLevelOperationTypeEnum.FOR_SUPPLIER.getId() == dwOperationType.getSelectedId());
                customerLookUpItem.setReload(true);
                customerLookUpItem.setClientID(clientID);
                customerLookUpItem.initialize();

                wPerProduct.setCurrency((CurrencyItem) dwCurrency.getSelectedItem());
            }
        });

        dwOperationType = new DataListBox();
        dwOperationType.ensureDebugId(editPriceLevelView + "dwOperationType");
        dwOperationType.addValueChangeHandler(changeEvent -> {
            if (dwOperationType.getSelectedId() != null && dwOperationType.getSelectedId().equals(PriceLevelOperationTypeEnum.FOR_SUPPLIER.getId())) {
                clientField.setLabel(wfmStrings.suppliers());
                customerLookUpItem.setSpecialOffer(true);
                customerLookUpItem.setClientID(clientID);
                customerLookUpItem.initialize();
            } else {
                clientField.setLabel(wfmStrings.customers());
                customerLookUpItem.setSpecialOffer(false);
                customerLookUpItem.setClientID(clientID);
                customerLookUpItem.initialize();
            }
        });
        dwOperationType.setItems(new SelectItem[]{new SelectItem(PriceLevelOperationTypeEnum.FOR_CLIENT.getId(), accountingStrings.forClient()),
                new SelectItem(PriceLevelOperationTypeEnum.FOR_SUPPLIER.getId(), accountingStrings.forSupplier())});
        dwOperationType.setWithoutNullLabel(true);

        operationType = new FormGroup(wfmStrings.operationType(), dwOperationType);
        currency = new FormGroup(wfmStrings.currency(), dwCurrency);
        name = new FormGroup(accountingStrings.priceLevelName(), txtName);
        type = new FormGroup(accountingStrings.priceLevelType(), dwType);
        percentage = new FormGroup(accountingStrings.itemPolisBy(), new InputGroup(dwCase, txtPercentage));

        wPerProduct = new WPLPerProduct();
        wPerBrand = new WPLPerBrand();

        productsTable = new FormGroup(wPerProduct);
        productsTable.setVisible(false);
        brandsTable = new FormGroup(wPerBrand);
        brandsTable.setVisible(false);
        customerSupplierPanel = new VerticalPanel();

        clientTypeList = new CustomList(Design.CHECK, true);
        clientTypeList.ensureDebugId(editPriceLevelView + "brandList");
        clientTypeList.setSearchText(accountingStrings.searchBrands());
        clientTypeList.setHeight(200);

        clientField = new FormGroup(customerSupplierPanel);
        clientTypes = new FormGroup(Property.get(Constants.CLIENT_LIST, accountingStrings.clientTypes(), wfmStrings.customer()), clientTypeList);

        GColumn leftColumn = new GColumn(name, operationType, currency, type, percentage);
        GColumn rightColumn = new GColumn(clientField, clientTypes);

        Div container = new Div();
        container.add(new GRow(leftColumn, rightColumn));
        container.add(new GRow(new GColumn(productsTable, brandsTable)));
        addField(PRICE_LEVEL_PANEL, container);
        show();
    }

    private WfmButton2 btnSavePriceLevel;

    private void setEnabledButtons(boolean b) {
        if (btnSavePriceLevel != null) {
            btnSavePriceLevel.setEnabled(b);
        }
    }

    private void loadData() {
        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setObjectId(objectID);
        filterParameter.setCurrencyID(clientCurrencyID);
        filterParameter.setClientId(clientID);
        priceLevelService.getPriceLevelData(filterParameter, new AsyncCallback<PriceLevelItem>() {
            @Override
            public void onFailure(Throwable throwable) {
                GWT.log(throwable.getMessage());
            }

            @Override
            public void onSuccess(PriceLevelItem levelItem) {
                priceLevelItem = levelItem;
                if (isCopy) {
                    objectID = null;
                }
                setData();
            }
        });
    }

    protected void setData() {
        super.setData();
        dwOperationType.setSelected(priceLevelItem.getOperationType().getId());
        clientField.setLabel(PriceLevelOperationTypeEnum.FOR_CLIENT.getId() == priceLevelItem.getOperationType().getId() ? wfmStrings.customers() : wfmStrings.suppliers());
        txtName.setText(priceLevelItem.getName());
        if (priceLevelItem.getPercent() != null) {
            txtPercentage.setValue(priceLevelItem.getPercent().toString());
        }

        if (priceLevelItem.getType() != null) {
            dwType.setSelected(priceLevelItem.getType());
        } else {
            dwType.setSelected(FIXED_PERCENTAGE);
        }
        onChageTypes();

        if (priceLevelItem.getPLCase() != null) {
            dwCase.setSelected(priceLevelItem.getPLCase());
        } else {
            dwCase.setSelected(DECREASE);
        }

        appliedClients = priceLevelItem.getAppliedClients();
        dwCurrency.setItems(priceLevelItem.getCurrencyList());
        dwCurrency.setSelected(priceLevelItem.getCurrency());

        if (priceLevelItem.getCurrency() != null) {
            wPerProduct.setCurrency(priceLevelItem.getCurrency());
        }

        initClientList(appliedClients, priceLevelItem.getCurrency().getId());
        initClientTypes(priceLevelItem);
    }

    private void initClientTypes(PriceLevelItem levelItem) {
        if (levelItem.getClientTypeList() != null && levelItem.getClientTypeList().length > 0) {
            final CustomListItem checkAll = new CustomListItem(new SelectItem(0, "<b>" + wfmStrings.selectAll() + "</b>"));
            clientTypeList.add(checkAll);
            checkAll.addValueChangeHandler(booleanValueChangeEvent -> clientTypeList.setCheckAllItems(booleanValueChangeEvent.getValue()));

            for (SelectItem clientType : levelItem.getClientTypeList()) {
                CustomListItem item = new CustomListItem(clientType);
                clientTypeList.add(item);

                if (levelItem.getAppliedClientTypes() != null && levelItem.getAppliedClientTypes().length > 0) {
                    for (SelectItem appliedClientType : levelItem.getAppliedClientTypes()) {
                        if (appliedClientType.getId().equals(clientType.getId())) {
                            item.setCheck(true);
                        }
                    }
                }
            }
        }
    }

    private void initClientList(SelectItem[] appliedClients, Integer currencyID) {

        if (appliedClients != null && appliedClients.length > 0) {
            customerLookUpItem = new CustomerLookUpItem(Design.CHECK, true, appliedClients, currencyID);
            customerLookUpItem.setHeight(280);
            customerLookUpItem.setSpecialOffer(dwOperationType.getSelectedId() != null && PriceLevelOperationTypeEnum.FOR_SUPPLIER.getId() == dwOperationType.getSelectedId());
            customerLookUpItem.setClientID(clientID);
            customerLookUpItem.initialize();
        } else {
            customerLookUpItem.removeItems();
            customerLookUpItem.setCurrencyID(currencyID);
            customerLookUpItem.setSpecialOffer(dwOperationType.getSelectedId() != null && PriceLevelOperationTypeEnum.FOR_SUPPLIER.getId() == dwOperationType.getSelectedId());
            customerLookUpItem.setClientID(clientID);
            customerLookUpItem.initialize();
        }
        customerLookUpItem.getTopPanel().add(checkAll);
        customerSupplierPanel.clear();
        customerSupplierPanel.add(customerLookUpItem);
    }

    protected PriceLevelItem fillRPCWithValues(PriceLevelItem priceLevelItem) {
        super.fillRPCWithValues(priceLevelItem);
        priceLevelItem.setId(objectID);
        priceLevelItem.setOperationType(PriceLevelOperationTypeEnum.FOR_SUPPLIER.getId() == dwOperationType.getSelectedId() ? PriceLevelOperationTypeEnum.FOR_SUPPLIER : PriceLevelOperationTypeEnum.FOR_CLIENT);
        priceLevelItem.setName(txtName.getText());
        priceLevelItem.setType(dwType.getSelectedId());
        priceLevelItem.setPLCase(dwCase.getSelectedId());
        priceLevelItem.setPercent(txtPercentage.getText() != null ? Double.valueOf(txtPercentage.getText().replace(",", ".")) : null);
        priceLevelItem.setPriceLevelPPItems(wPerProduct.getData());
        priceLevelItem.setPriceLevelBBItems(wPerBrand.getData());
        priceLevelItem.setCurrency(new CurrencyItem(dwCurrency.getSelectedId(), null, null));
        if (priceLevelItem.getType() != BY_BRAND && customerLookUpItem.getItems() != null && customerLookUpItem.getItems().size() > 0) {
            ArrayList<SelectItem> appliedClients = new ArrayList<>();
            for (CustomListItem client : customerLookUpItem.getItems()) {
                if (client.getValue()) {
                    appliedClients.add(client.getItem());
                }
            }
            priceLevelItem.setAppliedClients(appliedClients.toArray(new SelectItem[]{}));
        } else if (clientTypeList != null && clientTypeList.getItems().size() > 0) {
            List<SelectItem> appliedClientTypes = new ArrayList<>();
            for (CustomListItem brand : clientTypeList.getItems()) {
                if (brand.getValue()) {
                    appliedClientTypes.add(brand.getItem());
                }
            }
            priceLevelItem.setAppliedClientTypes(appliedClientTypes.toArray(new SelectItem[]{}));
        }
        return priceLevelItem;
    }

    private Boolean validate() {
        int errors = 0;

        if (!Validation.validateTextBoxRequired(txtName)) {
            errors++;
        }
        if (!Validation.validateListBoxRequired(dwType, accountingStrings.typeIsRequired(), new StringBuffer())) {
            errors++;
        }
        if (!Validation.validateListBoxRequired(dwCurrency, accountingStrings.currencyIsRequired(), new StringBuffer())) {
            errors++;
        }
        if (dwType.getSelectedId() != null && dwType.getSelectedId().equals(PER_PRODUCT) && !Validation.validateListBoxRequired(dwCase, accountingStrings.caseIsRequired(), new StringBuffer())) {
            errors++;
        }

        return errors <= 0;
    }

    private void save() {
        priceLevelItem = (PriceLevelItem) fillRPCWithValues(priceLevelItem);
        priceLevelService.save(priceLevelItem, new AsyncCallback<Integer>() {
            @Override
            public void onFailure(Throwable throwable) {
                setEnabledButtons(true);
                GWT.log(throwable.getMessage());
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(Integer result) {
                setEnabledButtons(true);
                LoadingPanel.loading(false);

                if (result != null && result > 0) {

                    if (priceLevelItem.getTotalCountPerProductItems().compareTo(MAX_LIMIT_PRICE_LEVEL_PER_PRODUCT) <= 0 && PER_PRODUCT.equals(priceLevelItem.getType())) {
                        priceLevelService.savePriceLevelPPItems(result, priceLevelItem.getPriceLevelPPItems(), getAsyncCallback());
                    } else if (BY_BRAND.equals(priceLevelItem.getType())) {
                        priceLevelService.savePriceLevelPBItems(result, priceLevelItem.getPriceLevelBBItems(), getAsyncCallback());
                    }

                    priceLevelItem.setId(result);
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.priceLevel()), Info.Type.INFO);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PRICE_LEVEL_SAVED, null, null);
                    closeTab();
                } else if (result.equals(-1)) {
                    Info.show(accountingStrings.priceLevelErrorMessage(), Info.Type.WARNING);
                } else {
                    Info.show(accountingStrings.priceLevelNoSaved(), Info.Type.INFO);
                }
            }
        });
    }

    private AsyncCallback getAsyncCallback() {
        return new AsyncCallback<Void>() {

            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(Void aVoid) {

            }
        };
    }

    private void onChageTypes() {

        if (dwType.getSelectedId() != null && dwType.getSelectedId().equals(PER_PRODUCT)) {
            percentage.setVisible(false);
            brandsTable.setVisible(false);
            clientTypes.setVisible(false);
            productsTable.setVisible(true);
            clientField.setVisible(true);
        } else if (dwType.getSelectedId() != null && dwType.getSelectedId().equals(BY_BRAND)) {
            percentage.setVisible(false);
            productsTable.setVisible(false);
            clientField.setVisible(false);
            brandsTable.setVisible(true);
            clientTypes.setVisible(true);
        } else {
            percentage.setVisible(true);
            clientField.setVisible(true);
            productsTable.setVisible(false);
            brandsTable.setVisible(false);
            clientTypes.setVisible(false);
        }

        wPerProduct.removeItems();
        if (priceLevelItem != null && priceLevelItem.getPriceLevelPPItems() != null && priceLevelItem.getPriceLevelPPItems().length > 0) {
            wPerProduct.setItems(priceLevelItem.getPriceLevelPPItems());
        } else if (priceLevelItem.getTotalCountPerProductItems() > 0) {
            productsTable.setContent(new WPLPerProductLarge(priceLevelItem.getId(), false));
        } else {
            wPerProduct.addEmptyRow();
        }
        wPerBrand.removeItems();
        if (priceLevelItem != null && priceLevelItem.getPriceLevelBBItems() != null && priceLevelItem.getPriceLevelBBItems().length > 0) {
            wPerBrand.setItems(priceLevelItem.getPriceLevelBBItems());
        } else {
            wPerBrand.addEmptyRow();
        }
    }

    @Override
    public String getIconStyle() {
        return "accountMark ac-edit";  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void asyncOnInitialize() {
        asyncOnInitialize(new AbstractAsyncCallback<Widget>() {
            public void failure(Throwable reason) {
                Info.show(wfmStrings.failedToDownloadCodeForThisWidget() + " (" + reason + ")", Info.Type.WARNING);
            }
        });
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


