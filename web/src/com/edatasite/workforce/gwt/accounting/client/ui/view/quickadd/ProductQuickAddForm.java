package com.edatasite.workforce.gwt.accounting.client.ui.view.quickadd;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewProduct;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductLocationItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductSelectItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductService;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.ProductExecuteCommand;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.lookup.AccountsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.WarehouseLookUp;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Label;

import java.math.BigDecimal;

public class ProductQuickAddForm extends KpiSideNavBox implements AccountingConstants {
    interface ProductQuickAddFormUiBinder extends UiBinder<Widget, ProductQuickAddForm> {
    }

    private static final ProductQuickAddFormUiBinder ourUiBinder = GWT.create(ProductQuickAddFormUiBinder.class);
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    protected static final WfmStrings wfmStrings = WfmStrings.App.get();

    SelectItem[] PRODUCT_TYPES = new SelectItem[]{
            new SelectItem(INVENTORY_ITEM, wfmStrings.inventoryItem()),
            new SelectItem(NON_INVENTORY_ITEM, wfmStrings.nonInventoryItem()),
            new SelectItem(SERVICE, wfmStrings.service()),
            new SelectItem(OTHER_CHARGE, wfmStrings.otherCharge())
    };

    @UiField
    HTMLPanel container;
    @UiField
    Label nameLabel;
    @UiField
    TextBox txtName;
    @UiField
    FormGroup typeInput;
    @UiField
    Label descriptionLabel;
    @UiField
    TextArea txtDescription;
    @UiField
    Label salesPriceLabel;
    @UiField
    TextBox txtsalesPrice;
    @UiField
    Label salesAccountLabel;
    @UiField(provided = true)
    AccountsLookUp salesAccountLookUp;
    @UiField
    FormGroup purchasePriceInput;
    @UiField
    FormGroup purchaseAccountInput;
    @UiField
    FormGroup qtyInput;
    @UiField
    FormGroup warehouseInput;
    @UiField
    FormGroup assetInput;
    @UiField
    FormGroup asOfInput;

    private DataListBox txtType;
    private TextBox txtPurchasePrice;
    private AccountsLookUp purchaseAccountLookUp;
    private WarehouseLookUp warehouseLookUp;
    private TextBox txtQty;
    private AccountsLookUp assetAccoutLookUp;
    private DatePicker asOfDate;
    private WfmButton2 btnSave;

    private NewProduct product;
    private final ProductExecuteCommand command;
    private final boolean isPurchase;
    private final boolean multiWarehouseEnabled = Utils.isMultiWarehouseEnabled();
    private Integer categoryId;

    public ProductQuickAddForm() {
        this(false, null);
    }

    public ProductQuickAddForm(ProductExecuteCommand command) {
        this(false, command);
    }

    public ProductQuickAddForm(Integer type) {
        this(false, null);
        applyType(type);
    }

    public ProductQuickAddForm(Integer categoryId, boolean fromCategory) {
        this(false, null);
        this.categoryId = categoryId;
    }

    public ProductQuickAddForm(boolean isPurchase, ProductExecuteCommand command) {
        super(DEFAULT_WIDTH);
        this.isPurchase = isPurchase;
        this.command = command;

        initProvided();
        ourUiBinder.createAndBindUi(this);

        addOpeningHandler(o -> loadData());

        show();
        if (!Utils.hasPermission(PermissionConstants.ACCOUNTING_PRODUCT_ADD) && Utils.hasPermission(PermissionConstants.ACCOUNTING_INVENTORY_ADD)) {
            txtType.setSelected(AccountingConstants.INVENTORY_ITEM);
            txtType.setEnabled(false);
            onTypeChangeEvent();
        } else if (!Utils.hasPermission(PermissionConstants.ACCOUNTING_PRODUCT_ADD) && Utils.hasPermission(PermissionConstants.ACCOUNTING_ASSEMBLY_ITEM_ADD)) {
            txtType.setSelected(AccountingConstants.ASSEMBLY_ITEM);
            txtType.setEnabled(false);
            onTypeChangeEvent();
        }
    }

    public ProductQuickAddForm(boolean isPurchase, Integer type, ProductExecuteCommand command) {
        super(DEFAULT_WIDTH);
        this.isPurchase = isPurchase;
        this.command = command;

        initProvided();
        ourUiBinder.createAndBindUi(this);

        addOpeningHandler(o -> loadData());
        show();
        txtType.setSelected(type);
        onTypeChangeEvent();
    }

    private void initProvided() {
        salesAccountLookUp = new AccountsLookUp(Constants.RECEIVABLE);
    }

    @Override
    public void show() {
        super.show();
        onInitialize();
    }

    private void onInitialize() {
        clear();

        //header
        addHeader(new HTML(accountingStrings.addProduct()));

        txtType = new DataListBox();
        purchaseAccountLookUp = new AccountsLookUp(Constants.PAYABLE);
        txtQty = new TextBox();
        txtPurchasePrice = new TextBox();
        warehouseLookUp = new WarehouseLookUp();
        assetAccoutLookUp = new AccountsLookUp(Constants.ASSETS);
        asOfDate = new DatePicker();

        Validation.addNumericKeyboardListener(txtQty, 0);
        Validation.addNumericKeyboardListener(txtsalesPrice, AccountingUtils.calculationScale);
        Validation.addNumericKeyboardListener(txtPurchasePrice, AccountingUtils.calculationScale);

        nameLabel.setText(wfmStrings.name());
        descriptionLabel.setText(wfmStrings.description());
        salesPriceLabel.setText(wfmStrings.sellingPrice());
        salesAccountLabel.setText(wfmStrings.salesAccount());

        typeInput.setLabel(wfmStrings.type());
        typeInput.addToContent(txtType);
        purchasePriceInput.setLabel(wfmStrings.purchasePrice());
        purchasePriceInput.addToContent(txtPurchasePrice);
        purchaseAccountInput.setLabel(wfmStrings.purchaseAccount());
        purchaseAccountInput.addToContent(purchaseAccountLookUp);
        qtyInput.setLabel(wfmStrings.qty());
        qtyInput.addToContent(txtQty);
        warehouseInput.setLabel(accountingStrings.warehouse());
        warehouseInput.addToContent(warehouseLookUp);
        assetInput.setLabel(wfmStrings.assetAccount());
        assetInput.addToContent(assetAccoutLookUp);
        asOfInput.setLabel(wfmStrings.asOF());
        asOfInput.addToContent(asOfDate);

        initTypes();
        onTypeChangeEvent();

        //body
        addBody(container);
        onTypeChangeEvent();


        btnSave = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        btnSave.addClickHandler(ch -> save());
        btnSave.ensureDebugId("product_quickadd_save");
        //footer
        addFooter(btnSave);
    }

    private void loadData() {
        LoadingPanel.loading(true, getBody());
        ProductService.App.get().getProductEditData(null, false, new AsyncCallback<NewProduct>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(NewProduct result) {
                LoadingPanel.loading(false);
                product = result;

                if (product.getAccountItem() != null) {
                    salesAccountLookUp.addAccountItem(product.getAccountItem());
                } else if (product.getDefaultReceivableAccount() != null) {
                    salesAccountLookUp.addAccountItem(product.getDefaultReceivableAccount());
                }

                if (product.getCogsAccount() != null) {
                    purchaseAccountLookUp.addAccountItem(product.getCogsAccount());
                } else if (product.getDefaultPayableAccount() != null) {
                    purchaseAccountLookUp.addAccountItem(product.getDefaultPayableAccount());
                }

                if (product.getAssetAccount() != null) {
                    assetAccoutLookUp.addAccountItem(product.getAssetAccount());
                }
            }
        });
    }

    private void save() {
        enableButtons(false);

        if (validateForm()) {
            LoadingPanel.loading(true, getBody());
            ProductService.App.get().saveProduct(getFormData(), new AsyncCallback<ProductSelectItem>() {
                @Override
                public void onFailure(Throwable throwable) {
                    LoadingPanel.loading(false);
                    Info.warn(wfmStrings.errorOccurredSavingChanges());
                    enableButtons(true);
                }

                @Override
                public void onSuccess(ProductSelectItem result) {
                    LoadingPanel.loading(false);
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.product()));
                    if (command != null) {
                        command.execute(result);
                    }
                    remove();

                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PRODUCTSERVICE_SAVED, null, null);
                }
            });
            //save
        } else {
            enableButtons(true);
        }
    }

    private NewProduct getFormData() {
        boolean isInventory = INVENTORY_ITEM.equals(txtType.getSelectedId());
        product.setType(txtType.getSelectedId());
        product.setItemName(txtName.getText());
        product.setDescription(txtDescription.getText());
        product.setActive(true);

        //sales
        product.setSellingPrice(AccountingUtils.get().parseToBigDecimal(txtsalesPrice.getText()));
        product.setAccountId(salesAccountLookUp.getSelectedItemID());

        if (isInventory || isPurchase) {
            //purchase
            product.setUnitPrice(AccountingUtils.get().parseToBigDecimal(txtPurchasePrice.getText()));
            product.setCogsAccountID(purchaseAccountLookUp.getSelectedItemID());
        }

        if (isInventory) {
            BigDecimal qty = AccountingUtils.get().parseToBigDecimal(txtQty.getText());
            product.setAssetAccountID(assetAccoutLookUp.getSelectedItemID());
            product.setAsOf(new DateNonConvertable(asOfDate.getDate()));

            ProductLocationItem[] productLocations = new ProductLocationItem[1];
            productLocations[0] = new ProductLocationItem();
            productLocations[0].setQty(qty);
            productLocations[0].setMinReorderPoint(BigDecimal.ONE);

            if (multiWarehouseEnabled) {
                productLocations[0].setWarehouseID(warehouseLookUp.getSelectedItemID());
            }

            product.setProductLocations(productLocations);
            product.setTotalQtyOnHand(qty);
            product.setQuantity(qty);
            product.setTotalValue(product.getUnitPrice().multiply(qty));
        }

        product.setPurchasedFromSupplier(isPurchase);
        if (categoryId != null) {
            product.setCategoryID(categoryId);
        }

        return product;
    }

    private boolean validateForm() {
        boolean isInventory = INVENTORY_ITEM.equals(txtType.getSelectedId());
        int errors = 0;

        if (!Validation.validateTextBoxRequired(txtName)) {
            errors++;
        }
        if (!Validation.validateTextBoxRequired(txtsalesPrice)) {
            errors++;
        }
        if (!Validation.validateLookUpRequired(salesAccountLookUp)) {
            errors++;
        }

        if (isInventory || isPurchase) {
            if (!Validation.validateTextBoxRequired(txtPurchasePrice)) {
                errors++;
            }
            if (!Validation.validateLookUpRequired(purchaseAccountLookUp)) {
                errors++;
            }
        }

        if (isInventory) {
//            if (!Validation.validateTextBoxRequired(txtQty)) {
//                errors++;
//            }
            if (!Validation.validateLookUpRequired(assetAccoutLookUp)) {
                errors++;
            }
            if (txtQty.getText() != null && !txtQty.getText().isEmpty()) {
                if (!Validation.validateDate(asOfDate)) {
                    errors++;
                }
                if (multiWarehouseEnabled && !Validation.validateLookUpRequired(warehouseLookUp)) {
                    errors++;
                }
            }
        }

        if (errors > 0) {
            Info.show(wfmStrings.fillRequiredField(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private void applyType(Integer type) {
        if (type != null) {
            txtType.setSelected(type);
            typeInput.setVisible(false);
            onTypeChangeEvent();
        }
    }

    private void onTypeChangeEvent() {
        boolean isInventory = INVENTORY_ITEM.equals(txtType.getSelectedId());

        purchasePriceInput.setVisible(isInventory || isPurchase);
        purchaseAccountInput.setVisible(isInventory || isPurchase);
        qtyInput.setVisible(isInventory);
        assetInput.setVisible(isInventory);
        asOfInput.setVisible(isInventory);
        warehouseInput.setVisible(isInventory && multiWarehouseEnabled);

        initBodyScrollContent();
    }

    private void initTypes() {
        txtType.clear();
        txtType.setWithoutNullLabel(true);
        txtType.setItems(PRODUCT_TYPES);
        txtType.setSelected(INVENTORY_ITEM);

        txtType.addValueChangeHandler(ch -> onTypeChangeEvent());
    }

    private void enableButtons(boolean enable) {
        btnSave.setEnabled(enable);
    }
}
