package com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.BillableExpenseItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewProduct;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductSelectItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.discount.DiscountItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.discount.DiscountMultiRangeItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.enums.ReceiveTypeEnum;
import com.edatasite.workforce.gwt.accounting.client.rpc.priceLevel.PriceLevelBBItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.priceLevel.PriceLevelItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.priceLevel.PriceLevelPPItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.priceLevel.PriceLevelService;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductService;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.client.ui.view.ProductLookUp;
import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.AddUnitMeasurementView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.quickadd.ProductQuickAddForm;
import com.edatasite.workforce.gwt.accounting.client.ui.view.quickadd.TaxQuickAddForm;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.CustomFieldLookUpTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.LoadingPanelCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TaxItem;
import com.edatasite.workforce.gwt.core.client.rpc.TestRPC;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableConstants;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.CurrencyWidget;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateTimeWidget;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.CustomFieldInterface;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ExtendedDatePicker;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LookUpCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.listeners.EditableTableListener;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.lookup.DropDownLookup;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.lookup.LookUp2;
import com.edatasite.workforce.gwt.core.client.ui.lookup.AccountsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.CrmAccountLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.DepartmentLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.MeasurementsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.ProjectLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.TaxLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.panel.HorizontalPanelDiv;
import com.edatasite.workforce.gwt.core.client.ui.upload.ExtendedItemUploadForm;
import com.edatasite.workforce.gwt.core.client.ui.view.ExtendedHTML;
import com.edatasite.workforce.gwt.core.client.ui.view.ObjectCommand;
import com.edatasite.workforce.gwt.core.client.ui.view.WarehouseLookUp;
import com.edatasite.workforce.gwt.core.client.ui.wfmDropdown.WfmDropdown;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseMarkupWidget;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoiceItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.Params;
import com.edatasite.workforce.gwt.invoice.client.rpc.PaymentItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.QuantityItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.ShippingDataItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.ShippingMethod;
import com.edatasite.workforce.gwt.invoice.client.rpc.TotalTaxItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.TypeItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.edatasite.workforce.gwt.invoice.client.ui.SmartMeasurementsLookUp;
import com.edatasite.workforce.gwt.invoice.client.ui.SmartProductLookUp;
import com.edatasite.workforce.gwt.invoice.client.ui.SmartTaxRateLookUp;
import com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingCustomFormConstants;
import com.edatasite.workforce.gwt.invoice.client.ui.view.CustomCellTextArea;
import com.edatasite.workforce.gwt.invoice.client.ui.view.InvoiceQuoteItemVariationPopup;
import com.edatasite.workforce.gwt.invoice.client.ui.view.components.ReceiptTable;
import com.edatasite.workforce.gwt.invoice.client.ui.view.components.ShippingMethodWidget;
import com.edatasite.workforce.gwt.submodule.paymentdeduction.client.SettingsData;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.FigCaption;
import gwt.material.design.client.ui.html.FigureWidget;
import gwt.material.design.client.ui.html.Small;
import gwt.material.design.client.ui.html.Span;
import gwt.material.design.jquery.client.api.JQuery;
import org.gwt.advanced.client.ui.widget.EditableGrid;
import org.gwt.advanced.client.ui.widget.cell.AbstractCell;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.edatasite.workforce.gwt.accounting.client.rpc.ProductItem.AVERAGE_COST;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.DATA_TYPE_NUMBER;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.ERROR_FORM_STYLE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.ONE_OFF_FIXED_AMOUNT;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.PAYABLE_CREDIT_NOTE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.PURCHASE_INVOICE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.PURCHASE_ORDER;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.RECEIVABLE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.RECEIVABLE_CREDIT_NOTE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SALE_INVOICE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.UI_TYPE_DATEPICKER;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.UI_TYPE_LOOKUP;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.UI_TYPE_TEXTAREA;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.UI_TYPE_TEXTBOX;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT_DELETE;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT_SUMMARY;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_BANK_ACCOUNT_RECEIVE_DELETE;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_BANK_ACCOUNT_RECEIVE_SUMMARY;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_RECEIVE_PAYMENT_LIST;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.RECEIVE_PAYMENT_SUMMARY;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 1/26/12
 * Time: 7:58 PM
 * To change this template use File | Settings | File Templates.
 */

public class ProductsTable extends Composite implements AccountingConstants, ItemTableConstants {

    public static final String CALCULATE = "calculate";
    public static final String PRODUCT = "PRODUCT";
    public static final String DESCRIPTION = "DESCRIPTION";
    public static final String QTY = "QTY";
    public static final String QTY_ON_HAND = "QTY_ON_HAND";
    public static final String MEASUREMENT = "MEASUREMENT";
    public static final String UNITPRICE = "UNITPRICE";
    public static final String COSTPRICE = "COSTPRICE";

    public static final String COMISSION = "COMISSION";
    public static final String DISCOUNT_LIST = "DISCOUNT_LIST";
    public static final String DISCOUNT_AMT = "DISCOUNT_AMT";
    public static final String DEPARTMENT = "DEPARTMENT";
    public static final String ACCOUNT = "ACCOUNT";
    public static final String DURATION = "DURATION";
    public static final String NET_AMT = "NET_AMT";
    public static final String TAX_LIST = "TAX_LIST";
    public static final String TAX_AMT = "TAX_AMT";
    public static final String TOTAL_AMT = "TOTAL_AMT";
    public static final String WAREHOUSE = "WAREHOUSE";
    public static final String PROJECT = "PROJECT";
    public static final String CLIENT = "CLIENT";
    public static final String SUPPLIER = "SUPPLIER";
    public static final String STATUS = "STATUS";
    public static final String ALLOCATION = "ALLOCATION";
    public static final String RECEIVED_QTY = "RECEIVED_QTY";
    public static final String DELIVERED_QTY = "DELIVERED_QTY";
    public static final String DOUBLE_TAX_LIST = "DOUBLE_TAX_LIST";
    public static final String DOUBLE_DISCOUNT_LIST = "DOUBLE_DISCOUNT_LIST";
    public static final String DOUBLE_DISCOUNT_AMT = "DOUBLE_DISCOUNT_AMT";
    public static final String REQUESTED_BEFORE = "REQUESTED_BEFORE";
    public static final String REQUEST_QTY = "REQUEST_QTY";
    public static final String CHECKBOX = "CHECKBOX";
    public static final String RECEIVE_TYPE = "RECEIVE_TYPE";
    public static final String RECEIPTS = "RECEIPTS";
    public static final String ATTACHMENT = "ATTACHMENT";
    public static final String REMARK = "REMARK";
    public static final String FROM_DATE = "FROM_DATE";
    public static final String TO_DATE = "TO_DATE";
    public static final String FAI_CATEGORY = "FAI_CATEGORY";

    public static final int DEFAULT_ROWS = 3;

    public static final String DEFAULT_DISCOUNT_TYPE_UNIT = "%";

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();

    private static final AccountingUtils utils = AccountingUtils.get();

    private EditableTable itemsTable;
    private EditableGrid grid;
    private ReceiptTable totalsTable;

    private DataListBox paymentTermsConditionsListBox;
    private TextArea2 paymentInstruction;

    private CurrencyWidget currencyWidget;
    private KpiCheckBox reverseChargeBox;
    private WfmDropdown placeOfSupplyBox;

    private ExtendedHTML subTotal,
            discountAmount,
            total,
            baseTotal,
            comissionAmount,
            billableExpenseAmount,
            billableExpenseTaxAmount,
            shippingTotal,
            netAmountTotal,
            shippingTaxValue;

    private HTML subTotalLabel,
            discountLabel,
            totalLabel,
            baseTotalLabel,
            comissionAmountLabel,
            billableExpenseAmountLabel,
            billableExpenseTaxLabel,
            shippingTotalLabel,
            netAmountTotalLabel,
            shippingTaxLabel;


    //Used in Credit Note Forms
    private ExtendedHTML creditedInvoice;
    private HTML creditedInvoiceLabel;
    private BigDecimal creditedInvoiceAmount;
    private BigDecimal creditableMaxInvoiceAmount;

    private PriceLevelItem priceLevel; //price level of clients. there are two types: FIXED PERCENTAGE, PER PRODUCT
    private DiscountItem clientDiscount; //discounts of client
    private final List<SelectItem> clientDiscountsSelectItem = new ArrayList<>(); //discounts of client
    private TaxItem clientOrSupplierTaxItem;
    private ShippingMethod shippingMethod;
    private ShippingMethodWidget shippingMethodWidget;
    private HashMap<Integer, Widget> taxWidgetMap;
    private final String type;//Type of invoice. There are two types: RECEIVABLE - sale and PAYABLE - purchase.
    private final String formType; //Constants.SALE_INVOICE, ....
    private boolean priceLavelChanged;

    private BigDecimal exchangeRateValue = BigDecimal.ONE;

    private final boolean isEditForm;
    private boolean isProjectBasedInvoice;
    private final boolean isPurchaseOrder;
    private final boolean isPurchaseInvoice;
    private final boolean isSupplierCreditNote;
    private final boolean isSaleQuote;

    private final boolean isSaleInvoice;
    private boolean isProgressInvoicing;
    private boolean isDeleteAndAddDsiabled;

    private Integer taxCalculationType = AccountingConstants.TAX_CALCULATION_EXCLUSIVE;
    private String currencyUnit;
    private AccountItem defaultAccount;
    private TaxItem defaultTax;
    private DiscountItem defaultDiscount;
    private Integer externalFormType;

    private HashMap<String, ColumnConfig> columnsMap;
    private HashMap<String, CompanyCustomFieldItem> customFieldsMap;

    private boolean hasBillableExp;
    private BigDecimal billableExpenseTotal;
    private BigDecimal billableExpenseTaxTotal;
    private NewInvoice newInvoice;
    private NewInvoiceItem[] invoiceItems;
    private boolean fromMultiQuoteConvert;
    private Boolean afterInc = false;
    private Boolean afterDec = false;
    private Boolean isDoubleTaxEnabled;
    private Boolean isDoubleDiscountEnabled;
    private Boolean isQuoteComissionEnabled;
    private Command comissionListener;
    private boolean isRoundingModeDisabled;

    private HashMap<Integer, BigDecimal> priceLevelMap;

    private ArrayList<Integer> convertedQuoteIds;
    private Integer currencyId;
    private LookUp crmAccountLookUp;
    private boolean isExpandProductGroup;
    private boolean isProductItem;
    private ArrayList<ExpenseMarkupWidget> markupWidgets;
    private Consumer<BigDecimal> totalUpdateFunction;
    //    private boolean reverseChargeApplicable;
    private final Params formParameters;
    private SelectItem defaultWarehouse;
    private SelectItem defaultDepartment;
    private TypeItem customerSupplierItem;
    private SelectItem firstSelectedWarehouse;

    public ProductsTable(final String type, final String formType, final Params formParameters) {
        this.type = type;
        this.formType = formType;
        isPurchaseOrder = Constants.PURCHASE_ORDER.equals(formType);
        isPurchaseInvoice = Constants.PURCHASE_INVOICE.equals(formType);
        isSupplierCreditNote = PAYABLE_CREDIT_NOTE.equals(formType);
        isSaleQuote = Constants.SALE_QUOTE.equals(formType);
        isSaleInvoice = Constants.SALE_INVOICE.equals(formType);
        isEditForm = formParameters.isEditForm();
        isProjectBasedInvoice = formParameters.isProjectBasedInvoice();
        this.formParameters = formParameters;
        this.initialize();
    }

    public void setClientDiscountsSelectItem(final List<SelectItem> clientDiscountsSelectItem) {
        if (clientDiscountsSelectItem != null && !clientDiscountsSelectItem.isEmpty()) {
            this.clientDiscountsSelectItem.clear();
            this.clientDiscountsSelectItem.addAll(clientDiscountsSelectItem);
        } else {
            this.clientDiscountsSelectItem.clear();
        }
    }

    private void initialize() {
        this.priceLevelMap = new HashMap<>();
        this.taxWidgetMap = new HashMap<>();
        this.initTotalTableWidgets();

        InvoiceService.App.get().getInvoiceSettings(new AsyncCallback<SettingsData>() {
            @Override
            public void onFailure(final Throwable throwable) {
            }

            @Override
            public void onSuccess(final SettingsData settingsData) {
                ProductsTable.this.isExpandProductGroup = settingsData.isExpandProductGroup();
            }
        });
    }

    private void initHandlers() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_DISCOUNT_SAVED, this, (sender, args) -> {
            final DiscountItem discountItem = (DiscountItem) args;
            for (int i = 0; i < this.grid.getRowCount(); i++) {

                final LookUpCell lookUpCell = (LookUpCell) this.grid.getWidget(i, this.itemsTable.getColumnId(ProductsTable.PRODUCT));
                final ProductLookUp productLookUp = (ProductLookUp) lookUpCell.getLookUp();
                final CustomCell comboBoxCell = (CustomCell) this.grid.getWidget(i, this.itemsTable.getColumnId(ProductsTable.DISCOUNT_LIST));
                final DataListBox discountDropdown = (DataListBox) comboBoxCell.getCustomWidget();
                final CustomCell comboBoxCell2;
                DataListBox discountDropdown2 = null;
                if (this.isDoubleDiscountEnabled && this.isSaleQuote) {
                    comboBoxCell2 = (CustomCell) this.grid.getWidget(i, this.itemsTable.getColumnId(ProductsTable.DOUBLE_DISCOUNT_LIST));
                    discountDropdown2 = (DataListBox) comboBoxCell2.getCustomWidget();
                }
                if (productLookUp.getSelectedItem() != null) {
                    for (final Integer productId : discountItem.getAppliedProductIDs()) {
                        if (productId.equals(productLookUp.getSelectedItemID())) {
                            if (discountDropdown.getItemsById().get(discountItem.getId()) == null) {
                                discountDropdown.addListItem(new SelectItem(discountItem.getId(), discountItem.getName()));
                            }

                            //add to double discount list but not select
                            if (discountDropdown2 != null && discountDropdown2.getItemsById().get(discountItem.getId()) == null) {
                                discountDropdown2.addListItem(new SelectItem(discountItem.getId(), discountItem.getName()));
                            }
                        }
                    }
                }
                this.onChangeDiscount(productLookUp.getItemWidgetsMap(), null);
            }
        });

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_DOUBLE_DISCOUNT_SAVED, this, (sender, args) -> {
            final DiscountItem discountItem = (DiscountItem) args;
            for (int i = 0; i < this.grid.getRowCount(); i++) {

                final LookUpCell lookUpCell = (LookUpCell) this.grid.getWidget(i, this.itemsTable.getColumnId(ProductsTable.PRODUCT));
                final ProductLookUp productLookUp = (ProductLookUp) lookUpCell.getLookUp();

                final CustomCell comboBoxCell = (CustomCell) this.grid.getWidget(i, this.itemsTable.getColumnId(ProductsTable.DISCOUNT_LIST));
                final DataListBox discountDropdown = (DataListBox) comboBoxCell.getCustomWidget();

                final CustomCell comboBoxCell2;
                DataListBox discountDropdown2 = null;
                if (this.isDoubleDiscountEnabled && this.isSaleQuote) {
                    comboBoxCell2 = (CustomCell) this.grid.getWidget(i, this.itemsTable.getColumnId(ProductsTable.DOUBLE_DISCOUNT_LIST));
                    discountDropdown2 = (DataListBox) comboBoxCell2.getCustomWidget();
                }
                if (productLookUp.getSelectedItem() != null) {
                    for (final Integer productId : discountItem.getAppliedProductIDs()) {
                        if (productId.equals(productLookUp.getSelectedItemID())) {
                            if (discountDropdown2 != null && discountDropdown2.getItemsById().get(discountItem.getId()) == null) {
                                discountDropdown2.addListItem(new SelectItem(discountItem.getId(), discountItem.getName()));

                            }

                            //add to discount list but not select
                            if (discountDropdown.getItemsById().get(discountItem.getId()) == null) {
                                discountDropdown.addListItem(new SelectItem(discountItem.getId(), discountItem.getName()));
                            }
                        }
                    }
                }
                this.onChangeDoubleDiscount(productLookUp.getItemWidgetsMap());
            }
        });

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_DISCOUNT_DELETED, this, (sender, args) -> {
            final Integer discountID = (Integer) args;
            for (int i = 0; i < this.grid.getRowCount(); i++) {

                final LookUpCell lookUpCell = (LookUpCell) this.grid.getWidget(i, this.itemsTable.getColumnId(ProductsTable.PRODUCT));
                final ProductLookUp productLookUp = (ProductLookUp) lookUpCell.getLookUp();
                final CustomCell comboBoxCell = (CustomCell) this.grid.getWidget(i, this.itemsTable.getColumnId(ProductsTable.DISCOUNT_LIST));
                final DataListBox discountDropdown = (DataListBox) comboBoxCell.getCustomWidget();

                final CustomCell comboBoxCell2;
                DataListBox discountDropdown2 = null;
                if (this.isDoubleDiscountEnabled && this.isSaleQuote) {
                    comboBoxCell2 = (CustomCell) this.grid.getWidget(i, this.itemsTable.getColumnId(ProductsTable.DOUBLE_DISCOUNT_LIST));
                    discountDropdown2 = (DataListBox) comboBoxCell2.getCustomWidget();
                }
                if (productLookUp.getSelectedItem() != null) {
                    discountDropdown.removeListItem(new SelectItem(discountID));
                    discountDropdown.setSelected(Constants.ONE_OFF_DISCOUNT);

                    if (this.isDoubleDiscountEnabled && this.isSaleQuote) {
                        discountDropdown2.removeListItem(new SelectItem(discountID));
                        discountDropdown2.setSelected(Constants.ONE_OFF_DISCOUNT);
                    }
                }
                this.onChangeDiscount(productLookUp.getItemWidgetsMap(), null);
                this.onChangeDoubleDiscount(productLookUp.getItemWidgetsMap());
            }
        });
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PRODUCT_QTY_CHANGE, this, (sender, args) -> calculate());


    }

    private void initItemTableHandlers() {
        JQuery.$(this.itemsTable).on(ProductsTable.CALCULATE, (event, params) -> {
            final boolean recalculate = params instanceof Boolean && (Boolean) params;
            this.calculate(recalculate);
            return null;
        });
        /*JQuery.$(itemsTable).on(UnitPrice.ON_CLIENT_PRICELEVEL_CHANGE, (event, params) -> {
            onClientPriceLevelChange(newInvoice);
            return null;
        });*/
    }

    private void initTotalTableWidgets() {
        this.subTotalLabel = new HTML(ProductsTable.wfmStrings.subtotal());
        this.discountLabel = new HTML(ProductsTable.wfmStrings.discount());
        this.totalLabel = new HTML(ProductsTable.wfmStrings.total());
        this.baseTotalLabel = new HTML(ProductsTable.wfmStrings.total());
        this.comissionAmountLabel = new HTML(ProductsTable.accountingStrings.comissionAmount());
        this.billableExpenseAmountLabel = new HTML(ProductsTable.accountingStrings.billableExpenseAmount());
        this.billableExpenseTaxLabel = new HTML(ProductsTable.accountingStrings.billableExpenseTaxAmount());
        this.shippingTotalLabel = new HTML(ProductsTable.accountingStrings.shipping());
        this.shippingTaxLabel = new HTML();
        this.netAmountTotalLabel = new HTML(ProductsTable.wfmStrings.netAmount());

        this.subTotalLabel.ensureDebugId(this.formType + "subTotalLabel");
        this.discountLabel.ensureDebugId(this.formType + "discountLabel");
        this.totalLabel.ensureDebugId(this.formType + "totalLabel");
        this.baseTotalLabel.ensureDebugId(this.formType + "baseTotalLabel");
        this.comissionAmountLabel.ensureDebugId(this.formType + "comissionAmountLabel");
        this.billableExpenseAmountLabel.ensureDebugId(this.formType + "billableExpenseAmountLabel");
        this.billableExpenseTaxLabel.ensureDebugId(this.formType + "billableExpenseTaxLabel");
        this.netAmountTotalLabel.ensureDebugId(this.formType + "netAmountTotalLabel");

        this.subTotal = this.getZeroAsHTML();
        this.discountAmount = this.getZeroAsHTML();
        this.total = this.getZeroAsHTML();
        this.baseTotal = this.getZeroAsHTML();
        this.comissionAmount = this.getZeroAsHTML();
        this.creditedInvoice = this.getZeroAsHTML();
        this.billableExpenseAmount = this.getZeroAsHTML();
        this.billableExpenseTaxAmount = this.getZeroAsHTML();
        this.shippingTotal = this.getZeroAsHTML();
        this.shippingTaxValue = this.getZeroAsHTML();
        this.netAmountTotal = this.getZeroAsHTML();

        this.subTotal.ensureDebugId(this.formType + "subTotal");
        this.discountAmount.ensureDebugId(this.formType + "discountAmount");
        this.total.ensureDebugId(this.formType + "total");
        this.baseTotal.ensureDebugId(this.formType + "baseTotal");
        this.comissionAmount.ensureDebugId(this.formType + "comissionAmount");
        this.creditedInvoice.ensureDebugId(this.formType + "creditedInvoice");
        this.billableExpenseAmount.ensureDebugId(this.formType + "billableExpenseAmount");
        this.billableExpenseTaxAmount.ensureDebugId(this.formType + "billableExpenseTaxAmount");
        this.netAmountTotal.ensureDebugId(this.formType + "netAmountTotal");

        this.subTotalLabel.setStyleName(AccountingCustomFormConstants.STYLE_TOTAL_LABEL);
        this.discountLabel.setStyleName(AccountingCustomFormConstants.STYLE_TOTAL_LABEL);
        this.totalLabel.setStyleName(AccountingCustomFormConstants.STYLE_TOTAL_LABEL);
        this.baseTotalLabel.setStyleName(AccountingCustomFormConstants.STYLE_TOTAL_LABEL);
        this.comissionAmountLabel.setStyleName(AccountingCustomFormConstants.STYLE_TOTAL_LABEL);
        this.billableExpenseAmountLabel.setStyleName(AccountingCustomFormConstants.STYLE_TOTAL_LABEL);
        this.billableExpenseTaxLabel.setStyleName(AccountingCustomFormConstants.STYLE_TOTAL_LABEL);
        this.shippingTotalLabel.setStyleName(AccountingCustomFormConstants.STYLE_TOTAL_LABEL);
        this.shippingTaxLabel.setStyleName(AccountingCustomFormConstants.STYLE_TOTAL_LABEL);
        this.netAmountTotalLabel.setStyleName(AccountingCustomFormConstants.STYLE_TOTAL_LABEL);

        this.subTotal.setStyleName(AccountingCustomFormConstants.STYLE_TOTAL_VALUE);
        this.discountAmount.setStyleName(AccountingCustomFormConstants.STYLE_TOTAL_VALUE);
        this.total.setStyleName(AccountingCustomFormConstants.STYLE_TOTAL_VALUE);
        this.baseTotal.setStyleName(AccountingCustomFormConstants.STYLE_TOTAL_VALUE);
        this.comissionAmount.setStyleName(AccountingCustomFormConstants.STYLE_TOTAL_VALUE);
        this.creditedInvoice.setStyleName(AccountingCustomFormConstants.STYLE_TOTAL_VALUE);
        this.billableExpenseAmount.setStyleName(AccountingCustomFormConstants.STYLE_TOTAL_VALUE);
        this.billableExpenseTaxAmount.setStyleName(AccountingCustomFormConstants.STYLE_TOTAL_VALUE);
        this.shippingTotal.setStyleName(AccountingCustomFormConstants.STYLE_TOTAL_VALUE);
        this.shippingTaxValue.setStyleName(AccountingCustomFormConstants.STYLE_TOTAL_VALUE);
        this.netAmountTotal.setStyleName(AccountingCustomFormConstants.STYLE_TOTAL_VALUE);
    }

    public void setMarkupWidgets(final ArrayList<ExpenseMarkupWidget> markupWidgets) {
        this.markupWidgets = markupWidgets;
    }

    private ArrayList<ExpenseMarkupWidget> getMarkupWidgets() {
        if (this.markupWidgets == null) {
            this.markupWidgets = new ArrayList<>();
        }
        return this.markupWidgets;
    }

    public BigDecimal getBillableExpenseTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (final ExpenseMarkupWidget markupWidget : this.getMarkupWidgets()) {
            final BillableExpenseItem exp = markupWidget.getBillableExpenseItem();

            if (this.currencyWidget.getCurrencyID().equals(exp.getCurrencyID())) {
                total = total.add(exp.getAmountInCurrency()).add(exp.getMarkupAmount());
            } else {
                total = total.add(exp.getAmountInBase().add(exp.getMarkupAmountInBase()).multiply(this.exchangeRateValue));
            }
        }
        return total;
    }

    public BigDecimal getBillableExpenseTotalInBase() {
        BigDecimal total = BigDecimal.ZERO;
        for (final ExpenseMarkupWidget markupWidget : this.getMarkupWidgets()) {
            total = total.add(markupWidget.getBillableExpenseItem().getAmountInBase().add(markupWidget.getBillableExpenseItem().getMarkupAmountInBase()));
        }
        return total;
    }

    private BigDecimal getBillableExpenseTaxTotal() {
        BigDecimal taxTotal = BigDecimal.ZERO;
        for (final ExpenseMarkupWidget markupWidget : this.getMarkupWidgets()) {
            BigDecimal tempTotal = BigDecimal.ZERO;
            final BillableExpenseItem exp = markupWidget.getBillableExpenseItem();

            if (this.currencyWidget.getCurrencyID().equals(exp.getCurrencyID())) {
                tempTotal = tempTotal.add(exp.getAmountInCurrency().add(exp.getMarkupAmount()));
            } else {
                tempTotal = tempTotal.add(exp.getAmountInBase().add(exp.getMarkupAmountInBase()).multiply(this.exchangeRateValue));
            }
            if (markupWidget.getEffectiveTaxPercent() != null) {
                taxTotal = taxTotal.add(tempTotal.multiply(markupWidget.getEffectiveTaxPercent()).divide(AccountingConstants.HUNDRED, AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP));
            }
        }
        return taxTotal;
    }

    private BigDecimal getBillableExpenseTaxTotalInBase() {
        BigDecimal taxTotal = BigDecimal.ZERO;
        for (final ExpenseMarkupWidget markupWidget : this.getMarkupWidgets()) {
            final BigDecimal tempTotal = markupWidget.getBillableExpenseItem().getAmountInBase().add(markupWidget.getBillableExpenseItem().getMarkupAmountInBase());

            if (markupWidget.getEffectiveTaxPercent() != null) {
                taxTotal = taxTotal.add(tempTotal.multiply(markupWidget.getEffectiveTaxPercent()).divide(AccountingConstants.HUNDRED, AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP));
            }
        }
        return taxTotal;
    }

    public void initItemTable(boolean editInvFromSQSOProgressInv) {
        if (this.formParameters.getExternalFormID() != null && (AccountingConstants.CONVERT_TO_INVOICE.equals(this.formParameters.getExternalFormID()) || AccountingConstants.CONVERT_TO_INVOICE_FROM_GRN.equals(this.formParameters.getExternalFormID()) || (AccountingConstants.PROGRESS_INVOICING.equals(this.formParameters.getExternalFormID()) || (AccountingConstants.CONVERT_TO_INVOICE_FROM_RENTAL_ORDER.equals(this.formParameters.getExternalFormID())) && !editInvFromSQSOProgressInv))) {
            this.itemsTable = new EditableTable(this.getColumns(), false, false);
        } else {
            boolean changeActions = true;

            if (formParameters.getOpportunityID() != null) {
                changeActions = Constants.SALE_QUOTE.equals(this.formType) && formParameters.getFormType().equals(Constants.SALE_ORDER) ? Utils.hasPermission(PermissionConstants.ACCOUNTING_SALES_ORDER_LINE_ITEM_ACTIONS) : Constants.SALE_QUOTE.equals(this.formType) && Utils.hasPermission(PermissionConstants.ACCOUNTING_SALES_QUOTE_LINE_ITEM_ACTIONS);
                changeActions = false;
            }

            this.itemsTable = new EditableTable(this.getColumns(), changeActions, changeActions);
            this.itemsTable.addStyleName("ProductTable-itemsTable");
            this.itemsTable.setDraggable(true);

            this.itemsTable.setListener(new EditableTableListener() {
                @Override
                public void addRow() {
                    // For Progress Invoicing and converted invoice we don't allow adding new rows
                    // When user click line item's last row, this listener generate new row
                    if (!isProgressInvoicing && !isDeleteAndAddDsiabled) {

                        ProductsTable.this.itemsTable.addRow(ProductsTable.this.getWidgets(null));
                    }
                }

                @Override
                public void removeRow() {
                    ProductsTable.this.updateTotal();
                    ProductsTable.this.checkForReverseCharge();
                }
            });
        }

        this.initItemTableHandlers();
        this.grid = this.itemsTable.getGrid();

        if (!this.isEditForm) {
            for (int i = 0; i < ProductsTable.DEFAULT_ROWS; i++) {
                this.itemsTable.addRow(this.getWidgets(null));
            }
            if (grid.getRowCount() > 0) {
                grid.setCurrentRow(0);
                grid.setFocus(true);
            }
        }

        this.totalsTable = new ReceiptTable(true);
        this.totalsTable.addStyleName("totalsTable java-ProductsTable");
        this.totalsTable.getOverallDiscount().setListener(() -> {

            this.discountAmount.setVisible(!Constants.ONE_OFF_FIXED_AMOUNT.equals(this.getOverallDiscount().getType()));
            this.calculate();
        });
        this.totalsTable.getOverallDiscount().setCurrencyListener(() -> {
            this.totalsTable.getOverallDiscount().setCurrencyId(this.currencyWidget.getCurrencyID());
            this.totalsTable.getOverallDiscount().setExchangeRate(this.currencyWidget.getExchangeRate());
        });

        if (!(this.isPurchaseInvoice || this.isPurchaseOrder || this.isSupplierCreditNote)) {
            checkAndInitializeShipping();
        } else {
            this.totalsTable.removeShippingBody();
        }


        /*
         * Drawing calculation table's body.
         */
        this.drawTotalsTable();

        /*
         * Putting of 'Payment Instructions/Notes' text area.
         */
        this.paymentTermsConditionsListBox = new DataListBox();
        this.paymentTermsConditionsListBox.setVisible(false);

        this.paymentInstruction = new TextArea2(30000);
        this.paymentInstruction.hideCharacterLimitPanel();
        this.paymentInstruction.getTextArea().getElement().getStyle().setHeight(100, Style.Unit.PX);

        if (Constants.PURCHASE_INVOICE.equals(this.formType) || Constants.SALE_INVOICE.equals(this.formType)) {
            this.paymentInstruction.setPlaceHolder(ProductsTable.accountingStrings.hereYouCanAddYourPaymentInstructionsOrNotes());
        } else {
            this.paymentInstruction.setPlaceHolder(ProductsTable.accountingStrings.hereYouCanAddYourTermsAndConditions());
        }
        this.paymentInstruction.setStyleName(AccountingCustomFormConstants.STYLE_TERMS_TXTAREA);
        this.paymentInstruction.ensureDebugId(this.formType + "paymentInstruction");
        this.paymentInstruction.getTextAreaPanel().setWidth("100%");
        this.initHandlers();
    }

    private void checkAndInitializeShipping() {
        totalsTable.initializeShipping(crmAccountLookUp, shippingTotal);
        totalsTable.getShippingMethodWidget().setOnChangeCommand(() -> {
            final ShippingMethod selectedMethod = totalsTable.getShippingMethodWidget().getSelectedMethod();

            if (selectedMethod != null && selectedMethod.isPriceChanged()) {
                selectedMethod.setCurrencyId(currencyWidget.getCurrencyID());
                selectedMethod.setExchangeRate(currencyWidget.getExchangeRate());
            }
            setShippingMethod(selectedMethod);
        });
        shippingMethodWidget = totalsTable.getShippingMethodWidget();

        InvoiceService.App.get().hasShippingMethod( new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(Boolean hasShippingMethod) {
                if (!hasShippingMethod) {
                    totalsTable.removeShippingBody();
                }
            }
        });
    }

    private ColumnConfig[] getColumns() {
        int index = 0;
        final ColumnConfig[] columns = new ColumnConfig[this.columnsMap.size()];

        for (String key : this.columnsMap.keySet()) {
            switch (key) {
                case ProductsTable.PRODUCT:
                    columns[index] = this.columnsMap.get(ProductsTable.PRODUCT);
                    columns[index].setTitle(this.columnsMap.get(key).isChanged() ? this.columnsMap.get(key).getTitle() : ProductsTable.accountingStrings.productOrService());
                    index++;
                    break;
                case ProductsTable.DESCRIPTION:
                    columns[index] = this.columnsMap.get(ProductsTable.DESCRIPTION);
                    columns[index].setTitle(this.columnsMap.get(key).isChanged() ? this.columnsMap.get(key).getTitle() : ProductsTable.wfmStrings.description());
                    columns[index].setCustomStyleName("product-description-cell");
                    index++;
                    break;
                case ProductsTable.QTY:
                    columns[index] = this.columnsMap.get(ProductsTable.QTY);
                    columns[index].setTitle(this.columnsMap.get(key).isChanged() ? this.columnsMap.get(key).getTitle() : ProductsTable.wfmStrings.qty());
                    if (this.isProjectBasedInvoice && Utils.isEmployeeAssignmentEnable()) {
                        columns[index].setWidth(0);
                        columns[index].setCustomStyleName("hide");
                    }
                    index++;
                    break;
                case ProductsTable.MEASUREMENT:
                    columns[index] = this.columnsMap.get(ProductsTable.MEASUREMENT);
                    columns[index].setTitle(this.columnsMap.get(key).isChanged() ? this.columnsMap.get(key).getTitle() : ProductsTable.wfmStrings.measurement());
                    index++;
                    break;
                case ProductsTable.UNITPRICE:
                    columns[index] = this.columnsMap.get(ProductsTable.UNITPRICE);
                    columns[index].setTitle(this.columnsMap.get(key).isChanged() ? this.columnsMap.get(key).getTitle() : ProductsTable.wfmStrings.price());
                    index++;
                    break;
                case ProductsTable.COMISSION:
                    columns[index] = this.columnsMap.get(ProductsTable.COMISSION);
                    columns[index].setTitle(this.columnsMap.get(key).isChanged() ? this.columnsMap.get(key).getTitle() : ProductsTable.wfmStrings.commission());
                    index++;
                    break;
                case ProductsTable.DISCOUNT_LIST:
                    columns[index] = this.columnsMap.get(ProductsTable.DISCOUNT_LIST);
                    columns[index].setTitle(this.columnsMap.get(key).isChanged() ? this.columnsMap.get(key).getTitle() : ProductsTable.accountingStrings.productDiscounts());
                    index++;
                    break;
                case ProductsTable.DISCOUNT_AMT:
                    columns[index] = this.columnsMap.get(ProductsTable.DISCOUNT_AMT);
                    columns[index].setTitle(this.columnsMap.get(key).isChanged() ? this.columnsMap.get(key).getTitle() : ProductsTable.wfmStrings.discount());
                    //columns[index].setWidth(100);
                    index++;
                    break;
                case ProductsTable.DOUBLE_DISCOUNT_LIST:
                    columns[index] = this.columnsMap.get(ProductsTable.DOUBLE_DISCOUNT_LIST);
                    columns[index].setTitle(this.columnsMap.get(key).isChanged() ? this.columnsMap.get(key).getTitle() : ProductsTable.accountingStrings.productDiscounts2());
                    index++;
                    break;
                case ProductsTable.DOUBLE_DISCOUNT_AMT:
                    columns[index] = this.columnsMap.get(ProductsTable.DOUBLE_DISCOUNT_AMT);
                    columns[index].setTitle(this.columnsMap.get(key).isChanged() ? this.columnsMap.get(key).getTitle() : ProductsTable.accountingStrings.discount2());
                    index++;
                    break;
                case ProductsTable.DEPARTMENT:
                    columns[index] = this.columnsMap.get(ProductsTable.DEPARTMENT);
                    columns[index].setTitle(Property.get(Constants.DEPARTMENT_LIST, ProductsTable.wfmStrings.department()));
                    //columns[index].setWidth(115);
                    index++;
                    break;
                case ProductsTable.ACCOUNT:
                    columns[index] = this.columnsMap.get(ProductsTable.ACCOUNT);
                    if (this.isPurchaseOrder || this.isPurchaseInvoice || this.isSupplierCreditNote) {
                        columns[index].setTitle(this.columnsMap.get(key).isChanged() ? this.columnsMap.get(key).getTitle() : ProductsTable.accountingStrings.salesTypeForPurchase());
                    } else {
                        columns[index].setTitle(this.columnsMap.get(key).isChanged() ? this.columnsMap.get(key).getTitle() : ProductsTable.wfmStrings.salesAccount());
                    }
                    index++;
                    break;
                case ProductsTable.NET_AMT:
                    columns[index] = this.columnsMap.get(ProductsTable.NET_AMT);
                    columns[index].setTitle(this.columnsMap.get(key).isChanged() ? this.columnsMap.get(key).getTitle() : ProductsTable.wfmStrings.netAmount());
                    //columns[index].setWidth(100);
                    index++;
                    break;
                case ProductsTable.TAX_LIST:
                    columns[index] = this.columnsMap.get(ProductsTable.TAX_LIST);
                    columns[index].setTitle(this.columnsMap.get(key).isChanged() ? this.columnsMap.get(key).getTitle() : ProductsTable.wfmStrings.taxRate());
                    index++;
                    break;
                case ProductsTable.DOUBLE_TAX_LIST:
                    columns[index] = this.columnsMap.get(ProductsTable.DOUBLE_TAX_LIST);
                    columns[index].setTitle(this.columnsMap.get(key).isChanged() ? this.columnsMap.get(key).getTitle() : ProductsTable.wfmStrings.taxRate());
                    index++;
                    break;
                case ProductsTable.WAREHOUSE:
                    columns[index] = this.columnsMap.get(ProductsTable.WAREHOUSE);
                    columns[index].setTitle(this.columnsMap.get(key).isChanged() ? this.columnsMap.get(key).getTitle() : ProductsTable.accountingStrings.warehouse());
                    index++;
                    break;
                case ProductsTable.TOTAL_AMT:
                    columns[index] = this.columnsMap.get(ProductsTable.TOTAL_AMT);
                    columns[index].setTitle(this.columnsMap.get(key).isChanged() ? this.columnsMap.get(key).getTitle() : ProductsTable.wfmStrings.totalAmount());
                    index++;
                    break;
                case ProductsTable.PROJECT:
                    columns[index] = this.columnsMap.get(ProductsTable.PROJECT);
                    columns[index].setTitle(this.columnsMap.get(key).isChanged() ? this.columnsMap.get(key).getTitle() : Property.get(Constants.PROJECT, ProductsTable.wfmStrings.project()));
                    index++;
                    break;
                case ProductsTable.CLIENT:
                    columns[index] = this.columnsMap.get(ProductsTable.CLIENT);
                    columns[index].setTitle(this.columnsMap.get(key).isChanged() ? this.columnsMap.get(key).getTitle() : ProductsTable.accountingStrings.billing());
                    index++;
                    break;
                case ProductsTable.ATTACHMENT:
                    columns[index] = this.columnsMap.get(ProductsTable.ATTACHMENT);
                    columns[index].setTitle(ProductsTable.wfmStrings.attachment());
                    index++;
                    break;
                case ProductsTable.FAI_CATEGORY:
                    columns[index] = this.columnsMap.get(ProductsTable.FAI_CATEGORY);
                    columns[index].setTitle(wfmStrings.category());
                    index++;
                    break;
                default:
                    columns[index] = this.columnsMap.get(key);
                    index++;
                    break;
            }
        }

        return columns;
    }

    public Object[] getWidgets(final Object object) {
        return this.getWidgetsMap(object).values().toArray(new Object[]{});
    }

    private LinkedHashMap<String, Object> getWidgetsMap(final Object object) {
        this.exchangeRateValue = Optional.ofNullable(this.exchangeRateValue).orElse(BigDecimal.ONE);

        NewProduct product = null;
        NewInvoiceItem invoiceItem;
        LinkedHashMap<String, Object> widgetsMap = new LinkedHashMap<>();
        LinkedHashMap<String, Widget> itemWidgetsMap = new LinkedHashMap<>();

        if (object instanceof NewProduct) {
            invoiceItem = null;
            product = (NewProduct) object;
            isProductItem = true;
        } else if (object instanceof NewInvoiceItem) {
            invoiceItem = (NewInvoiceItem) object;
            isProductItem = false;
        } else {
            invoiceItem = null;
        }

        for (final String key : columnsMap.keySet()) {
            switch (key) {
                case PRODUCT:
                    if (this.isProjectBasedInvoice) {
                        final CustomCellTextArea prod = new CustomCellTextArea();
                        widgetsMap.put(ProductsTable.PRODUCT, prod);
                        itemWidgetsMap.put(ProductsTable.PRODUCT, prod);

                        if (product != null) {
                            prod.setText(product.getItemName());
                        } else if (invoiceItem != null) {
                            prod.setText(invoiceItem.getItemName());
                            prod.setEntryIds(invoiceItem.getProjectBasedEntryIds());
                        }
                        prod.setEnabled(this.columnsMap.get(key) != null && !this.columnsMap.get(key).isDisabled());
                    } else {
                        SmartProductLookUp productLookUp = new SmartProductLookUp(this.type, formType);
                        productLookUp.setEnsureSuggestBox(this.formType + "productLookUp");
                        productLookUp.setAutocompleteOff();
                        productLookUp.setEnabled(this.columnsMap.get(key) != null && !this.columnsMap.get(key).isDisabled());
                        productLookUp.setLinkCommand(
                            () -> new ProductQuickAddForm(Constants.PAYABLE.equals(this.type), item -> {
                                productLookUp.addProductItem(item);
                                if (productLookUp.getOnSelectListener() != null) {
                                    productLookUp.getOnSelectListener().execute();
                                }
                            })
                        );

                        this.addDebugID(productLookUp, "productLookUp");
                        productLookUp.setOnSelectListener(() -> this.setItemValues(productLookUp, this.itemsTable.getGrid().getCurrentRow()));
                        productLookUp.setItemWidgetsMap(itemWidgetsMap);
                        widgetsMap.put(ProductsTable.PRODUCT, productLookUp);
                        itemWidgetsMap.put(ProductsTable.PRODUCT, productLookUp);

                        if (product != null) {
                            final String productName;
                            if (product.getNumberData() != null) {
                                productName = product.getNumberData().getNumberString() + " -> " + product.getItemName();
                            } else {
                                productName = product.getItemName();
                            }
                            productLookUp.addProductItem(new ProductSelectItem(product.getObjectId(), productName, product.getType(), product.isPurchasedFromSupplier(), product.isHasInventoryInProductKit(), product.getBrandID(), product.getBrandName(), product.getSellingPrice()));
                            productLookUp.setDiscountItems(product.getDiscountItems());
                            productLookUp.setItemDiscountID(product.getItemDiscountID());
                            productLookUp.setDiscountItemStaticType(product.getDiscountItemStaticType());
                        } else if (invoiceItem != null) {
                            ProductSelectItem productSelectItem = new ProductSelectItem(invoiceItem.getItemID() == null ? 0 : invoiceItem.getItemID(), invoiceItem.getFullItemName(), invoiceItem.getProductType(), invoiceItem.isProductPurchasedFromSupplier(), invoiceItem.isHasInventoryInProductKit(), invoiceItem.getProductBrandID(), invoiceItem.getProductBrand(), invoiceItem.getItemOriginalPrice());
                            productSelectItem.setExpItemId(invoiceItem.getExpanceItemId());
                            productLookUp.addProductItem(productSelectItem);
                            productLookUp.setAssignedSerials(invoiceItem.getItemID() == null ? 0 : invoiceItem.getItemID(), invoiceItem.getAssignedSerials());
                            productLookUp.setConvertedItemId(invoiceItem.getQuoteItemId());
                            productLookUp.setSaleInvoiceId(invoiceItem.getSaleInvoiceId());
                            productLookUp.setSoldOut(invoiceItem.isSoldOut());
                            productLookUp.setDiscountItems(invoiceItem.getItemDiscountList());
                            productLookUp.setItemDiscountID(invoiceItem.getItemDiscountID());
                            productLookUp.setDiscountItemStaticType(invoiceItem.getDiscountItemStaticType());
                            productLookUp.setItemID(invoiceItem.getID());
                            productLookUp.setUsedInGrn(invoiceItem.getUsedInGrn());
                            if (invoiceItem.isSoldOut() || invoiceItem.getUsedInGrn() != null) {
                                productLookUp.setEnabled(false);
                            }
                        }
                    }

                    break;
                case ProductsTable.DESCRIPTION:
                    final ProductDescriptionTextArea description = new ProductDescriptionTextArea(TextArea2.AREA_LENGTH_3);
                    description.hideCharacterLimitPanel();
                    description.setEnabled(this.columnsMap.get(key) != null && !this.columnsMap.get(key).isDisabled());
                    this.addDebugID(description, "description");
                    description.addStyleName("description-default-color product-table-description");
                    description.getTextAreaPanel().addStyleName("description-textareaPanel");
                    description.getTextArea().addStyleName("description-textarea");
                    widgetsMap.put(ProductsTable.DESCRIPTION, description);
                    itemWidgetsMap.put(ProductsTable.DESCRIPTION, description);

                    if (product != null) {
                        if (product.getCustomDescription() != null) {
                            description.setCustomDescription(product.getCustomDescription());
                        } else {
                            description.setText(product.getDescription(), true);
                        }
                    } else if (invoiceItem != null) {
                        if (invoiceItem.getCustomDescription() != null) {
                            description.setCustomDescription(invoiceItem.getCustomDescription(), true);
                        } else {
                            description.setText(invoiceItem.getDescription(), true);
                        }
                    }

                    break;
                case ProductsTable.QTY:
                    boolean qtyConvertedValidationTrackBatch = isProgressInvoicing || isDeleteAndAddDsiabled || isSaleInvoice;
                    final ItemQtyPanel qtyPanel = new ItemQtyPanel(itemWidgetsMap, this.isProjectBasedInvoice, qtyConvertedValidationTrackBatch);
                    qtyPanel.setOnDiscountChange(widgetsMap_ -> this.onChangeDiscount(widgetsMap_, null));
                    qtyPanel.setOnQuantityChange(this::calculate);
                    qtyPanel.setEnabled(this.columnsMap.get(key) != null && !this.columnsMap.get(key).isDisabled());
                    this.addDebugID(qtyPanel, "qtyPanel");
                    widgetsMap.put(ProductsTable.QTY, qtyPanel);
                    itemWidgetsMap.put(ProductsTable.QTY, qtyPanel);

                    if (product != null) {
                        BigDecimal quantity = product.getPkItemQty();
                        if (Utils.hasGenericAccess(GenericSettingsEnum.CONSIGNMENT_FUNCTION_ENABLE)) {
                            if (this.isPurchaseInvoice) {
                                qtyPanel.setQtyOnHandText(ProductsTable.utils.formatQty(product.getConsignedQty()));
                            } else {
                                qtyPanel.setQtyOnHandText(ProductsTable.utils.formatQty(product.getConsignedQtyToSell()));
                            }
                        } else {
                            qtyPanel.setQtyOnHandText(ProductsTable.utils.formatQty(product.getQuantity()));
                        }

                        if (product.getInventoryTrackingEnabled()) {
                            if (Constants.PURCHASE_INVOICE.equals(this.formType)) {
                                qtyPanel.initSerialsPanel(product.getObjectId(), null);
                            } else if (Constants.SALE_INVOICE.equals(this.formType)) {
                                qtyPanel.initAssignSerialsPanel(product.getObjectId(), null);
                                if (quantity == null) {
                                    quantity = BigDecimal.ZERO;
                                }
                            } else if (Constants.RECEIVABLE_CREDIT_NOTE.equals(this.formType) || PAYABLE_CREDIT_NOTE.equals(this.formType)) {
                                qtyPanel.initAssignSerialsPanel(product.getObjectId(), 0, this.formType, null);
                            }
                        }
                        if (product.getBatchTrackingEnabled()) {//Batch serials
                            if (Constants.SALE_INVOICE.equals(this.formType)) {
                                qtyPanel.initBatchSerialsPanel(product.getObjectId(), this.formType, null);
                                if (quantity == null) {
                                    quantity = BigDecimal.ZERO;
                                }
                            } else if (Constants.RECEIVABLE_CREDIT_NOTE.equals(this.formType) || PAYABLE_CREDIT_NOTE.equals(this.formType)) {
                                qtyPanel.initBatchSerialsPanel(product.getObjectId(), this.formType, null);
                            }
                        }
                        if (product.getTrackBatchesEnabled()) {
                            if (Constants.PURCHASE_INVOICE.equals(this.formType) || Constants.RECEIVABLE_CREDIT_NOTE.equals(this.formType)) {
                                qtyPanel.initTrackBatchPanel(product.getObjectId(), this.formType, product.getItemName());
                            } else if (Constants.SALE_INVOICE.equals(this.formType) || PAYABLE_CREDIT_NOTE.equals(this.formType)) {
                                qtyPanel.initAssignTrackBatchPanel(product, this.formType);
                                if (quantity == null) {
                                    quantity = BigDecimal.ZERO;
                                }
                            }
                        }
                        if (quantity == null) {
                            quantity = BigDecimal.ONE;
                        }
                        qtyPanel.setText(ProductsTable.utils.formatQty(quantity));
                    } else if (invoiceItem != null) {
                        final String qtyText;
                        if (this.isProjectBasedInvoice && invoiceItem.isFromTimesheet()) {
                            qtyText = Utils.formatMinutes(invoiceItem.getQuantity() != null ? invoiceItem.getQuantity()
                                    .multiply(new BigDecimal(60))
                                    .setScale(AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP).intValue() : 0);
                        } else {
                            if (invoiceItem.getQuantity() != null) {
                                qtyText = BigDecimal.ZERO.compareTo(invoiceItem.getQuantity()) == 0 ? AccountingUtils.getQtyZero() : ProductsTable.utils.formatQty(invoiceItem.getQuantity());
                            } else {
                                qtyText = AccountingUtils.getQtyZero();
                            }
                        }
                        qtyPanel.setQtyOnHandText(invoiceItem.getItemsInStockQty() != null ? ProductsTable.utils.formatQty(invoiceItem.getItemsInStockQty()) : AccountingUtils.getQtyZero());
                        qtyPanel.setText(qtyText);
//                      We need realy quantity in order to calculate correctly.
//                      it allows us to calculate prices correctly with hight scale (10)
//                      but if we remove it calulcation is not beeing correct
                        qtyPanel.setRealQuantity(invoiceItem.getQtyWithHighScale());
                        if (invoiceItem.isSoldOut()) {
                            qtyPanel.setEnabled(false);
                        }
                        qtyPanel.setFromTimesheet(invoiceItem.isFromTimesheet());

                        if (invoiceItem.getInventoryTrackingEnabled()) {
                            if (Constants.PURCHASE_INVOICE.equals(this.formType)) {
                                qtyPanel.initSerialsPanel(invoiceItem.getItemID(), invoiceItem.getSerials());
                            } else if (Constants.SALE_INVOICE.equals(this.formType) || Constants.RECEIVABLE_CREDIT_NOTE.equals(this.formType) || PAYABLE_CREDIT_NOTE.equals(this.formType)) {
                                if (AccountingConstants.COPY_INVOICE_TO_CREDITNOTE.equals(this.externalFormType)) {
                                    qtyPanel.initAssignSerialsPanel(invoiceItem.getItemID(), invoiceItem.getID(), this.formType, invoiceItem.getSerials());
                                } else {
                                    qtyPanel.initAssignSerialsPanel(invoiceItem.getItemID(), invoiceItem.getSerials());
                                }
                            }
                        }

                        if (invoiceItem.getBatchTrackingEnabled()) {
                            if (Constants.SALE_INVOICE.equals(this.formType) || Constants.RECEIVABLE_CREDIT_NOTE.equals(this.formType) || PAYABLE_CREDIT_NOTE.equals(this.formType)) {
                                qtyPanel.initBatchSerialsPanel(invoiceItem.getItemID(), this.formType, invoiceItem.getSerials());
                            }
                        }

                        if (invoiceItem.getTrackBatchesEnabled()) {
                            if (Constants.SALE_INVOICE.equals(this.formType) || PAYABLE_CREDIT_NOTE.equals(this.formType)) {
                                final String productLabel = invoiceItem.getItemName() != null ?
                                        (invoiceItem.getItemNumber() != null ? invoiceItem.getItemNumber() + " -> " + invoiceItem.getItemName() : invoiceItem.getItemName()) :
                                        ProductsTable.wfmStrings.notAvailable();
                                qtyPanel.initAssignTrackBatchPanel(invoiceItem.getItemID(), productLabel, invoiceItem.getID(), this.formType, invoiceItem.getBatchItems(), false);
                            } else if (AccountingConstants.CONVERT_TO_INVOICE.equals(this.formParameters.getExternalFormID())
                                    || AccountingConstants.CONVERT_TO_INVOICE_FROM_GRN.equals(this.formParameters.getExternalFormID())
                                    || Constants.PURCHASE_INVOICE.equals(this.formType)
                                    || Constants.RECEIVABLE_CREDIT_NOTE.equals(this.formType)) {
                                qtyPanel.initTrackBatchPanel(invoiceItem.getItemID(), invoiceItem.getID(), this.formType, invoiceItem.getBatchItems(), false, invoiceItem.getItemName());
                            }
                        }
                    }

                    break;
                case ProductsTable.MEASUREMENT:

                    SmartMeasurementsLookUp measurementsLookUp = new SmartMeasurementsLookUp();
                    measurementsLookUp.setLinkCommand(() -> {
                        final ObjectCommand command = item -> measurementsLookUp.addMeasurementUnit((SelectItem) item);
                        new AddUnitMeasurementView(null, command);
                    });
                    measurementsLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> measurementsLookUp.islink());
                    measurementsLookUp.setEnabled(this.columnsMap.get(key) != null && !this.columnsMap.get(key).isDisabled());
                    this.addDebugID(measurementsLookUp, "measurementsLookUp");
                    widgetsMap.put(ProductsTable.MEASUREMENT, measurementsLookUp);
                    itemWidgetsMap.put(ProductsTable.MEASUREMENT, measurementsLookUp);

                    if (product != null && product.getUnitMeasurement() != null) {
                        measurementsLookUp.setSelected(product.getUnitMeasurement());
                        measurementsLookUp.initMeasurementUnits(new SelectItem[]{product.getUnitMeasurement()});
                    } else if (invoiceItem != null && invoiceItem.getMeasurement() != null) {
                        measurementsLookUp.setSelected(invoiceItem.getMeasurement());
                        measurementsLookUp.initMeasurementUnits(new SelectItem[]{invoiceItem.getMeasurement()});
                    }

                    break;
                case ProductsTable.UNITPRICE:

                    final UnitPrice unitPrice = new UnitPrice(this.exchangeRateValue, currencyId);
                    unitPrice.setValuableText(AccountingUtils.getUnitPriceZero(), BigDecimal.ZERO);
                    unitPrice.setEnabled(this.columnsMap.get(key) != null && !this.columnsMap.get(key).isDisabled());
                    this.addDebugID(unitPrice, "unitPrice");
                    if ((invoiceItem != null && invoiceItem.isSoldOut()) || ((isSaleQuote || isPurchaseOrder) && newInvoice != null && newInvoice.getInvoicedItems() != null && newInvoice.getInvoicedItems().length > 0)) {
                        if (AccountingConstants.COPY_FROM_EXISTING_DATA.equals(this.formParameters.getExternalFormID()) || (newInvoice.getInvoicedAmount() == null || newInvoice.getInvoicedAmount().compareTo(BigDecimal.ZERO) == 0)) {

                            unitPrice.setEnabled(true);
                        } else {

                            unitPrice.setEnabled(false);
                        }
                    }
                    unitPrice.addKeyUpHandler(keyboard -> {
                        //  keyboard.isControlKeyDown() most of the computer users just click Ctrl
                        //  and paste it, but to handle Ctrl key must be pressed about a while that's why I just set key='V'
                        if (keyboard.getNativeKeyCode() == 'V' || keyboard.getNativeKeyCode() == 'v') {
                            String displayValue = unitPrice.getValue();
                            String trimmed = displayValue.replaceAll("\\s+", "");
                            unitPrice.setValue(trimmed);
                        }
                    });
                    Validation.checkToFocusTextBox(unitPrice, AccountingUtils.getUnitPriceZero());
                    Validation.addNumericKeyboardListener(unitPrice, AccountingUtils.getUnitPriceScale(), false);
                    widgetsMap.put(ProductsTable.UNITPRICE, unitPrice);
                    itemWidgetsMap.put(ProductsTable.UNITPRICE, unitPrice);

                    if (product != null) {
                        unitPrice.setMultiPricesMap(product.getMultiPricesMap());
                        BigDecimal price = BigDecimal.ZERO;
                        if (Constants.RECEIVABLE.equals(this.type) && product.getSellingPrice() != null) {
                            price = product.getSellingPrice();
                        } else if (Constants.PAYABLE.equals(this.type) && product.getUnitPrice() != null) {
                            price = product.getUnitPrice();
                        }
                        unitPrice.setValueInBaseCurrency(price);
                        price = price.multiply(this.exchangeRateValue);

                        if (Utils.isMultipleSalesPriceEnable() && this.currencyId != null && unitPrice.getMultiPricesMap().containsKey((Constants.RECEIVABLE.equals(this.type) ? Constants.RECEIVABLE : Constants.PAYABLE) + this.currencyId)) {
                            price = unitPrice.getMultiPricesMap().get((Constants.RECEIVABLE.equals(this.type) ? Constants.RECEIVABLE : Constants.PAYABLE) + this.currencyId);
                        }
                        unitPrice.setValuableText(ProductsTable.utils.formatUnitPrice(price), price);

                        if (this.priceLevel != null) {
                            this.setPriceLevelRate(product.getObjectId(), product.getBrandID(), this.priceLevel, unitPrice, null, this.grid.getCurrentRow());
                        }

                        if (Utils.hasPermission(PermissionConstants.ACCOUNTING_PRODUCT_AVARAGE_COST) && Constants.RECEIVABLE.equals(this.type)) {
                            unitPrice.setCostPriceTooltip(ProductsTable.utils.formatUnitPrice(product.getAverageCost() != null ? product.getAverageCost() : BigDecimal.ZERO));
                        }
                    } else if (invoiceItem != null) {
                        final BigDecimal unitPriceAmount = invoiceItem.getUnitPrice() != null ? invoiceItem.getUnitPrice() : BigDecimal.ZERO;
                        if (invoiceItem.getMultiPricesMap().containsKey((Constants.RECEIVABLE.equals(this.type) ? Constants.RECEIVABLE : Constants.PAYABLE) + this.currencyId)) {
                            invoiceItem.getMultiPricesMap().put((Constants.RECEIVABLE.equals(this.type) ? Constants.RECEIVABLE : Constants.PAYABLE) + this.currencyId, unitPriceAmount);
                        } else {
                            invoiceItem.getMultiPricesMap().put((Constants.RECEIVABLE.equals(this.type) ? Constants.RECEIVABLE : Constants.PAYABLE) + "-1", unitPriceAmount);
                        }
                        if (Constants.RECEIVABLE.equals(this.type) && invoiceItem.getItemAverageCost() != null) {
                            invoiceItem.getMultiPricesMap().put(AVERAGE_COST, invoiceItem.getItemAverageCost());
                        }

                        unitPrice.setMultiPricesMap(invoiceItem.getMultiPricesMap());
                        unitPrice.setValuableText(ProductsTable.utils.formatUnitPrice(unitPriceAmount), unitPriceAmount);
                        unitPrice.setValueInBaseCurrency(unitPriceAmount.divide(this.exchangeRateValue, AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP));
                    }

                    break;
                case ProductsTable.COMISSION:

                    if (AccountingUtils.get().enableComission() || this.isQuoteComissionEnabled()) {
                        ExtendedTextBox txtComission = new ExtendedTextBox();
                        txtComission.setText(AccountingUtils.getZero());
                        txtComission.setEnabled(this.columnsMap.get(key) != null && !this.columnsMap.get(key).isDisabled());
                        //txtComission.setWidth("100px");
                        this.addDebugID(txtComission, "unitPrice");
                        txtComission.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
                        Validation.addNumericKeyboardListener(txtComission, 2);
                        txtComission.addKeyUpHandler(event -> this.calculate());
                        widgetsMap.put(ProductsTable.COMISSION, txtComission);

                        if (product != null && product.getComission() != null) {
                            txtComission.setText(ProductsTable.utils.formatPrice(product.getComission()));
                        } else if (invoiceItem != null && invoiceItem.getComission() != null) {
                            txtComission.setText(ProductsTable.utils.formatPrice(invoiceItem.getComission()));
                            if (invoiceItem.isSoldOut()) {
                                txtComission.setEnabled(false);
                            }
                        }
                    }

                    break;
                case ProductsTable.DISCOUNT_LIST:
                    final DataListBox dwDiscountList = new DataListBox();
                    dwDiscountList.setEnabled(this.columnsMap.get(key) != null && !this.columnsMap.get(key).isDisabled());
                    this.addDebugID(dwDiscountList, "dwDiscountList");
                    dwDiscountList.addValueChangeHandler(ch -> this.onChangeDiscount(itemWidgetsMap, null));

                    if ((isSaleQuote || isPurchaseOrder) && newInvoice != null && newInvoice.getInvoicedItems() != null && newInvoice.getInvoicedItems().length > 0) {
                        dwDiscountList.setEnabled(false);
                    }

                    if (widgetsMap.get(ProductsTable.PRODUCT) instanceof ProductLookUp) {
                        ProductLookUp productLookUp = (ProductLookUp) widgetsMap.get(ProductsTable.PRODUCT);
                        productLookUp.getTextBox().addKeyUpHandler(keyUpEvent -> {

                            if (productLookUp.getSelectedItemID() == null) {

                                if (this.clientDiscount != null) {

                                    if (dwDiscountList.getItemsById().get(this.clientDiscount.getId()) == null) {
                                        dwDiscountList.addListItem(this.clientDiscount);
                                    }
                                    dwDiscountList.setSelected(this.clientDiscount.getId());
                                    dwDiscountList.setEnabled(false);
                                } else {
                                    dwDiscountList.setSelected(Constants.ONE_OFF_DISCOUNT);
                                    dwDiscountList.setEnabled(true);
                                }
                            }
                        });
                    }

                    widgetsMap.put(ProductsTable.DISCOUNT_LIST, dwDiscountList);
                    itemWidgetsMap.put(ProductsTable.DISCOUNT_LIST, dwDiscountList);

                    this.loadDiscountItems(itemWidgetsMap);

                    break;
                case ProductsTable.DISCOUNT_AMT:
                    NewProduct itemValues = product;
                    final Discount discount = new Discount(this.exchangeRateValue);
                    discount.setEnabled(this.columnsMap.get(key) != null && !this.columnsMap.get(key).isDisabled());
                    discount.setOnDiscountChange(() -> {
                        if (isProductItem) {
                            if (isSaleQuote || isSaleInvoice && itemValues != null) {
                                BigDecimal discAmount = itemValues.getDiscountAmount();
                                if (Objects.nonNull(discAmount) && discAmount.compareTo(BigDecimal.ZERO) != 0) {
                                    if (discount.getValue().doubleValue() > discAmount.doubleValue()) {
                                        discount.setValueText(ProductsTable.utils.formatDiscount(BigDecimal.ZERO), BigDecimal.ZERO);
                                        Info.warn(accountingStrings.discountAmountValidation());
                                    }
                                }
                            }
                        } else {
                            if (isSaleQuote || isSaleInvoice && invoiceItem != null) {
                                BigDecimal discAmount = invoiceItem.getCurrentProductDiscountAmount();
                                if (Objects.nonNull(discAmount) && discAmount.compareTo(BigDecimal.ZERO) != 0) {
                                    if (discount.getValue().doubleValue() > discAmount.doubleValue()) {
                                        discount.setValueText(ProductsTable.utils.formatDiscount(BigDecimal.ZERO), BigDecimal.ZERO);
                                        Info.warn(accountingStrings.discountAmountValidation());
                                    }
                                }
                            }
                        }
                        if (!this.getOverallDiscount().isEnabled()) {
                            this.calculate();
                        }
                    });
                    this.addDebugID(discount, "discount");
                    widgetsMap.put(ProductsTable.DISCOUNT_AMT, discount);
                    itemWidgetsMap.put(ProductsTable.DISCOUNT_AMT, discount);

                    if ((isSaleQuote || isPurchaseOrder) && newInvoice != null && newInvoice.getInvoicedItems() != null && newInvoice.getInvoicedItems().length > 0) {
                        if (AccountingConstants.COPY_FROM_EXISTING_DATA.equals(this.formParameters.getExternalFormID())) {
                            discount.setEnabled(true);
                        } else {
                            discount.setEnabled(false);
                        }
                    }

                    if (this.clientDiscount != null) {
                        discount.setEnabled(false);
                        this.onChangeDiscount(itemWidgetsMap, false, null);
                    } else if (invoiceItem != null) {
                        if (invoiceItem.getDiscountPercent() != null) {
                            if (AccountingConstants.COPY_FROM_SQ_SO_TO_PO.equals(this.externalFormType)
                                    || AccountingConstants.COPY_FROM_SI_TO_PO.equals(this.externalFormType)
                                    || AccountingConstants.COPY_FROM_SI_TO_PI.equals(this.externalFormType)
                                    || AccountingConstants.COPY_FROM_PI_TO_SI.equals(this.externalFormType)
                            ) {
                                discount.setValueText(ProductsTable.utils.formatDiscount(BigDecimal.ZERO), BigDecimal.ZERO);
                            } else {
                                discount.setValueText(ProductsTable.utils.formatDiscount(invoiceItem.getDiscountPercent()), invoiceItem.getDiscountPercent());
                            }
                            discount.setDiscountUnit(ProductsTable.DEFAULT_DISCOUNT_TYPE_UNIT);
                        } else if (invoiceItem.getDiscountAmount().compareTo(BigDecimal.ZERO) != 0) {
                            if (AccountingConstants.COPY_FROM_SQ_SO_TO_PO.equals(this.externalFormType)
                                    || AccountingConstants.COPY_FROM_SI_TO_PO.equals(this.externalFormType)
                                    || AccountingConstants.COPY_FROM_SI_TO_PI.equals(this.externalFormType)
                                    || AccountingConstants.COPY_FROM_PI_TO_SI.equals(this.externalFormType)
                            ) {
                                discount.setValueText(ProductsTable.utils.formatDiscount(BigDecimal.ZERO), BigDecimal.ZERO);
                            } else {
                                discount.setValueText(ProductsTable.utils.formatDiscount(invoiceItem.getDiscountAmount()), invoiceItem.getDiscountAmount());
                            }
                            discount.setValueInBaseCurrency(invoiceItem.getDiscountAmount().divide(this.exchangeRateValue, AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP));
                            discount.setDiscountUnit(this.getCurrencyUnit());
                        } else {
                            discount.setDiscountUnit(getCurrencyUnit());
                        }
                        if (invoiceItem.isSoldOut()) {
                            discount.setEnabled(false);
                        }
                    } else {
                        //TODO
                        DiscountItem defDiscount = this.defaultDiscount;
                        discount.setOnDiscountChange(this::calculate);
                        if (defDiscount != null && ONE_OFF_FIXED_AMOUNT.equals(defDiscount.getId())) {
                            discount.setDiscountUnit(this.getCurrencyUnit());
                        }
                    }
                    break;
                case ProductsTable.DOUBLE_DISCOUNT_LIST:

                    final DataListBox dwDiscountList2 = new DataListBox();
                    dwDiscountList2.setEnabled(this.columnsMap.get(key) != null && !this.columnsMap.get(key).isDisabled());
                    this.addDebugID(dwDiscountList2, "dwDoubleDiscountList");
                    dwDiscountList2.addValueChangeHandler(ch -> this.onChangeDiscount(itemWidgetsMap, null));

                    if (widgetsMap.get(ProductsTable.PRODUCT) instanceof ProductLookUp) {
                        ProductLookUp productLookUp = (ProductLookUp) widgetsMap.get(ProductsTable.PRODUCT);
                        productLookUp.getTextBox().addKeyUpHandler(keyUpEvent -> {

                            if (productLookUp.getSelectedItemID() == null) {
                                dwDiscountList2.removeListItems();
                                dwDiscountList2.addListItem(new DiscountItem(Constants.ONE_OFF_DISCOUNT, ProductsTable.wfmStrings.percentage()));
                                dwDiscountList2.addListItem(new DiscountItem(Constants.ONE_OFF_FIXED_AMOUNT, ProductsTable.wfmStrings.fixedAmount()));
                                dwDiscountList2.setSelected(Constants.ONE_OFF_DISCOUNT);
                            }
                        });
                    }

                    if (isPurchaseOrder && newInvoice != null && newInvoice.getInvoicedItems().length > 0) {
                        dwDiscountList2.setEnabled(false);
                    }

                    widgetsMap.put(ProductsTable.DOUBLE_DISCOUNT_LIST, dwDiscountList2);
                    itemWidgetsMap.put(ProductsTable.DOUBLE_DISCOUNT_LIST, dwDiscountList2);

                    final ArrayList<DiscountItem> dlist = new ArrayList<>();
                    dlist.add(new DiscountItem(Constants.ONE_OFF_DISCOUNT, ProductsTable.wfmStrings.percentage()));
                    dlist.add(new DiscountItem(Constants.ONE_OFF_FIXED_AMOUNT, ProductsTable.wfmStrings.fixedAmount()));

                    if (product != null) {

                        if (product.getDiscountItems() != null && product.getDiscountItems().length > 0) {
                            dlist.addAll(Arrays.asList(product.getDiscountItems()));
                            dwDiscountList2.setItems(dlist.toArray(new SelectItem[]{}));
                        }
                        dwDiscountList2.setSelected(Constants.ONE_OFF_DISCOUNT);

                    } else if (invoiceItem != null) {

                        if (invoiceItem.getItemDiscountList() != null && invoiceItem.getItemDiscountList().length > 0) {
                            dlist.addAll(Arrays.asList(invoiceItem.getItemDiscountList()));
                            dwDiscountList2.setItems(dlist.toArray(new SelectItem[]{}));

                            if (invoiceItem.getItemDoubleDiscountID() != null) {
                                dwDiscountList2.setSelected(invoiceItem.getItemDoubleDiscountID());
                            } else {
                                dwDiscountList2.setSelected(Constants.ONE_OFF_DISCOUNT);
                            }
                        }
                        if (invoiceItem.isSoldOut()) {
                            dwDiscountList2.setEnabled(false);
                        }
                    }

                    break;
                case ProductsTable.DOUBLE_DISCOUNT_AMT:

                    final Discount discount2 = new Discount(this.exchangeRateValue);
                    discount2.setEnabled(this.columnsMap.get(key) != null && !this.columnsMap.get(key).isDisabled());
                    this.addDebugID(discount2, "doubleDiscount");
                    widgetsMap.put(ProductsTable.DOUBLE_DISCOUNT_AMT, discount2);
                    itemWidgetsMap.put(ProductsTable.DOUBLE_DISCOUNT_AMT, discount2);

                    if (isPurchaseOrder && newInvoice != null && newInvoice.getInvoicedItems().length > 0) {
                        discount2.setEnabled(false);
                    }

                    if (invoiceItem != null) {

                        if (invoiceItem.getDoubleDiscountPercent() != null) {

                            if (AccountingConstants.COPY_FROM_SQ_SO_TO_PO.equals(this.externalFormType) || AccountingConstants.COPY_FROM_SI_TO_PO.equals(this.externalFormType)
                                    || AccountingConstants.COPY_FROM_SI_TO_PI.equals(this.externalFormType)
                                    || AccountingConstants.COPY_FROM_PI_TO_SI.equals(this.externalFormType)
                            ) {
                                discount2.setValueText(ProductsTable.utils.formatDiscount(BigDecimal.ZERO), BigDecimal.ZERO);
                            } else {
                                discount2.setValueText(ProductsTable.utils.formatDiscount(invoiceItem.getDoubleDiscountPercent()), invoiceItem.getDoubleDiscountPercent());
                            }
                            discount2.setDiscountUnit(ProductsTable.DEFAULT_DISCOUNT_TYPE_UNIT);
                        } else if (invoiceItem.getDoubleDiscountAmount().compareTo(BigDecimal.ZERO) != 0) {

                            if (AccountingConstants.COPY_FROM_SQ_SO_TO_PO.equals(this.externalFormType) || AccountingConstants.COPY_FROM_SI_TO_PO.equals(this.externalFormType)
                                    || AccountingConstants.COPY_FROM_SI_TO_PI.equals(this.externalFormType)
                                    || AccountingConstants.COPY_FROM_PI_TO_SI.equals(this.externalFormType)
                            ) {
                                discount2.setValueText(ProductsTable.utils.formatDiscount(BigDecimal.ZERO), BigDecimal.ZERO);
                            } else {
                                discount2.setValueText(ProductsTable.utils.formatDiscount(invoiceItem.getDoubleDiscountAmount()), invoiceItem.getDoubleDiscountAmount());
                            }
                            discount2.setValueInBaseCurrency(invoiceItem.getDoubleDiscountAmount().divide(this.exchangeRateValue, 6, RoundingMode.HALF_UP));
                            discount2.setDiscountUnit(this.getCurrencyUnit());
                        }
                        if (invoiceItem.isSoldOut()) {
                            discount2.setEnabled(false);
                        }
                    }

                    break;
                case ProductsTable.DEPARTMENT:
                    DepartmentLookUp departmentLookUp = new DepartmentLookUp();
                    departmentLookUp.setEnabled(this.columnsMap.get(key) != null && !this.columnsMap.get(key).isDisabled());
                    this.addDebugID(departmentLookUp, "departmentLookUp");
                    widgetsMap.put(ProductsTable.DEPARTMENT, departmentLookUp);
                    itemWidgetsMap.put(ProductsTable.DEPARTMENT, departmentLookUp);

                    if (product != null && product.getDefaultDepartment() != null) {
                        departmentLookUp.addItem(product.getDefaultDepartment());
                    }
                    if (invoiceItem != null && invoiceItem.getDepartmentItem() != null) {
                        departmentLookUp.addItem(invoiceItem.getDepartmentItem());
                    } else if (this.defaultDepartment != null) {
                        departmentLookUp.setSelected(this.defaultDepartment);
                    }

                    break;
                case ProductsTable.ACCOUNT:

                    AccountsLookUp accountsList = new AccountsLookUp(this.type);
                    accountsList.setEnabled(this.columnsMap.get(key) != null && !this.columnsMap.get(key).isDisabled());
                    this.addDebugID(accountsList, "accountsList");
                    widgetsMap.put(ProductsTable.ACCOUNT, accountsList);
                    itemWidgetsMap.put(ProductsTable.ACCOUNT, accountsList);
                    if (product != null) {
                        if (Constants.PAYABLE.equals(this.type)) {
                            if (product.getAssetAccount() != null) {
                                accountsList.addAccountItem(product.getAssetAccount());
                            }
                        } else if (product.getAccountId() != null) {
                            if (this.defaultAccount != null && this.defaultAccount.isDefault()) {
                                accountsList.addAccountItem(this.defaultAccount);
                            } else {
                                accountsList.addAccountItem(product.getAccountItem());
                            }
                        }
                        if ((this.isPurchaseOrder || this.isPurchaseInvoice) && (AccountingConstants.SERVICE.equals(product.getType()) || AccountingConstants.NON_INVENTORY_ITEM.equals(product.getType()) || AccountingConstants.OTHER_CHARGE.equals(product.getType()))) {
                            accountsList.addAccountItem(product.getCogsAccount());
                        }
                        if ((this.isPurchaseOrder || this.isPurchaseInvoice) && (AccountingConstants.INVENTORY_ITEM.equals(product.getType()) || AccountingConstants.ASSEMBLY_ITEM.equals(product.getType()))) {
                            accountsList.setEnabled(false);
                        }
                        accountsList.setProductGroup(AccountingConstants.PRODUCT_KIT.equals(product.getType()));
                    } else if (invoiceItem != null) {
                        if (invoiceItem.getAccountItem() != null) {
                            accountsList.addAccountItem(invoiceItem.getAccountItem());
                        }
                        accountsList.setProductGroup(AccountingConstants.PRODUCT_KIT.equals(invoiceItem.getProductType()));
                    }else {
                        if (this.defaultAccount != null) {
                            accountsList.setSelected(this.defaultAccount);
                        }
                    }

                    break;
                case ProductsTable.NET_AMT:

                    final ExtendedHTML netAmount = this.getZeroAsHTML();
                    this.addDebugID(netAmount, "netAmount");
                    widgetsMap.put(ProductsTable.NET_AMT, netAmount);
                    break;
                case ProductsTable.TAX_LIST:

                    final SmartTaxRateLookUp taxList = new SmartTaxRateLookUp(this.type);
                    taxList.setEnabled(this.columnsMap.get(key) != null && !this.columnsMap.get(key).isDisabled());
                    taxList.setLinkCommand(() -> new TaxQuickAddForm(o -> {
                        taxList.addTaxItem((TaxItem) o);
                        this.reDrawTaxesDropdown();
                    }));

                    if (isPurchaseOrder && newInvoice != null && newInvoice.getInvoicedItems() != null && newInvoice.getInvoicedItems().length > 0) {
                        taxList.setEnabled(false);
                    }

                    if (Constants.NON_VAT_REGISTERED_DESIGNATED_ZONE.equals(this.getSupplierTaxTreatment())) {
                        taxList.setExcludeExempt(true);
                    }
                    this.addDebugID(taxList, "taxList");
                    taxList.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
                        taxList.islink();
                        this.reDrawTaxesDropdown();
                        this.redrawFaiCategory(taxList);
                    });
                    taxList.getSuggestBox().addKeyUpHandler(keyUpEvent -> this.reDrawTaxesDropdown());
                    widgetsMap.put(ProductsTable.TAX_LIST, taxList);

                    /*
                      GCC countries: for doing bill of materials
                     */

                    if (Constants.NON_VAT_REGISTERED_DESIGNATED_ZONE.equals(this.getSupplierTaxTreatment()) && this.reverseChargeBox != null) {
                        taxList.setEnabled(this.reverseChargeBox.getValue());
                    } else if (Constants.NON_VAT_REGISTERED.equals(this.getSupplierTaxTreatment())) {
                        taxList.setEnabled(false);
                    } else if (Constants.NON_GCC.equals(this.getSupplierTaxTreatment()) && Utils.isSaudiCompany()) {
                        taxList.setEnabled(this.reverseChargeBox.getValue());
                    } else if (Constants.GCC_VAT_REGISTERED.equals(this.getSupplierTaxTreatment()) || Constants.GCC_NON_VAT_REGISTERED.equals(this.getSupplierTaxTreatment())) {

                        if (Utils.isSaudiCompany()) {
                            if (this.reverseChargeBox != null && this.reverseChargeBox.isAttached()) {
                                taxList.setEnabled(this.reverseChargeBox.getValue());
                            } else {
                                taxList.setEnabled(Constants.GCC_VAT_REGISTERED.equals(this.getSupplierTaxTreatment()));
                            }
                        }
                    }

                    if (!Objects.equals(this.taxCalculationType, AccountingConstants.NO_TAX_CALCULATION)) {
                        if (product != null) {
                            if (Constants.VAT_REGISTERED_DESIGNATED_ZONE.equals(this.getSupplierTaxTreatment())
                                    && (AccountingConstants.INVENTORY_ITEM.equals(product.getType()) || AccountingConstants.ASSEMBLY_ITEM.equals(product.getType()))) {
                                taxList.clear();
                                taxList.setEnabled(false);
                            } else if (this.clientOrSupplierTaxItem != null) {
                                taxList.addTaxItem(this.clientOrSupplierTaxItem);
                                this.reDrawTaxesDropdown();
                            } else if (product.getTaxItem() != null && !Utils.hasGenericAccess(GenericSettingsEnum.CANNOT_AUTO_SELECTED_PRODUCT_TAX_RATE)) {
                                taxList.addTaxItem(product.getTaxItem());
                                this.reDrawTaxesDropdown();
                            }
                        } else if (invoiceItem != null) {

                            if (invoiceItem.getTaxItem() != null) {
                                taxList.addTaxItem(invoiceItem.getTaxItem());
                                taxList.setSelected(invoiceItem.getTaxItem());
                            }

                            if (Constants.VAT_REGISTERED_DESIGNATED_ZONE.equals(this.getSupplierTaxTreatment())
                                    && (AccountingConstants.INVENTORY_ITEM.equals(invoiceItem.getItemType()) || AccountingConstants.ASSEMBLY_ITEM.equals(invoiceItem.getItemType()))) {
                                taxList.clear();
                                taxList.setEnabled(false);
                            } else if ((invoiceItem.isSoldOut() && AccountingConstants.COPY_FROM_FIXED_ASSET.equals(this.formParameters.getExternalFormID())) || AccountingConstants.PROGRESS_INVOICING.equals(this.formParameters.getExternalFormID())) {
                                taxList.setEnabled(false);
                            }
                        }else {
                            if (this.defaultTax != null) {
                                taxList.addTaxItem(this.defaultTax);
                                this.reDrawTaxesDropdown();
                            }
                        }
                    } else {
                        taxList.clear();
                        taxList.setEnabled(false);
                    }
//                    if (!taxList.getSuggestBox().getTextBox().isEnabled()) {
//                        taxList.clear();
//                    }Todo

                    break;
                case ProductsTable.TAX_AMT:
                    final ExtendedHTML taxAmount = this.getZeroAsHTML();
                    this.addDebugID(taxAmount, "taxAmount");
                    widgetsMap.put(ProductsTable.TAX_AMT, taxAmount);
                    break;
                case ProductsTable.DOUBLE_TAX_LIST:

                    TaxLookUp doubleTaxList = new TaxLookUp(this.type);
                    doubleTaxList.setEnabled(this.columnsMap.get(key) != null && !this.columnsMap.get(key).isDisabled());
                    this.addDebugID(doubleTaxList, "doubleTaxList");
                    doubleTaxList.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> this.reDrawTaxesDropdown());
                    doubleTaxList.getSuggestBox().addKeyUpHandler(keyUpEvent -> this.reDrawTaxesDropdown());
                    widgetsMap.put(ProductsTable.DOUBLE_TAX_LIST, doubleTaxList);

                    if (isPurchaseOrder && newInvoice != null && newInvoice.getInvoicedItems().length > 0) {
                        doubleTaxList.setEnabled(false);
                    }

                    if (!Objects.equals(this.taxCalculationType, AccountingConstants.NO_TAX_CALCULATION)) {
                        if (product != null) {
                            if (product.getDoubleTaxItem() != null) {
                                doubleTaxList.addTaxItem(product.getDoubleTaxItem());
                                this.reDrawTaxesDropdown();
                            }
                        } else if (invoiceItem != null && invoiceItem.getDoubleTaxItem() != null) {

                            if (AccountingConstants.COPY_FROM_SQ_SO_TO_PO.equals(this.externalFormType) || AccountingConstants.COPY_FROM_SI_TO_PO.equals(this.externalFormType)
                                    || AccountingConstants.COPY_FROM_SI_TO_PI.equals(this.externalFormType)
                                    || AccountingConstants.COPY_FROM_PI_TO_SI.equals(this.externalFormType)
                            ) {
                                doubleTaxList.addTaxItem(invoiceItem.getDoubleTaxItem());
                            } else {
                                doubleTaxList.addTaxItem(invoiceItem.getDoubleTaxItem());
                            }
                            if (invoiceItem.isSoldOut()) {
                                doubleTaxList.setEnabled(false);
                            }
                        }
                    } else {
                        doubleTaxList.clear();
                        doubleTaxList.setEnabled(false);
                    }


                    break;
                case ProductsTable.WAREHOUSE:
                    //hozircha Sales Invoice va Purchase Invoicelar uchun
                    final String checkOwnPermissionType = (Constants.SALE_INVOICE.equals(this.formType) || Constants.PURCHASE_INVOICE.equals(this.formType) || Constants.RECEIVABLE_CREDIT_NOTE.equals(this.formType)) ? this.formType : null;
                    final WarehouseLookUp warehouseLookUp = new WarehouseLookUp(checkOwnPermissionType);
                    warehouseLookUp.setEnabled(this.columnsMap.get(key) != null && !this.columnsMap.get(key).isDisabled());
                    this.addDebugID(warehouseLookUp, "warehouseLookUp");
                    widgetsMap.put(ProductsTable.WAREHOUSE, warehouseLookUp);
                    if (AccountingConstants.CONVERT_TO_INVOICE_FROM_GRN.equals(this.formParameters.getExternalFormID())) {
                        warehouseLookUp.setEnabled(false);
                    }
                    if (product != null) {
                        if (Constants.SALE_INVOICE.equals(this.formType) || Constants.PURCHASE_INVOICE.equals(this.formType)) {
                            warehouseLookUp.setProductID(product.getObjectId());
                        }
                        if (Utils.hasGenericAccess(GenericSettingsEnum.WAREHOUSE_AUTOSELECT_BY_OWNER)) {
                            if (product != null && product.getWarehouseByOwner() != null) {
                                warehouseLookUp.addItem(product.getWarehouseByOwner());
                            }
                        } else {
                            if (product.getWarehouse() != null) {
                                warehouseLookUp.addItem(product.getWarehouse());
                            } else if (this.defaultWarehouse != null) {
                                warehouseLookUp.addItem(this.defaultWarehouse);
                            } else if (product.getDefaultItemWarehouse() != null) {
                                // set product default warehouse
                                warehouseLookUp.addItem(product.getDefaultItemWarehouse());
                            } else if (firstSelectedWarehouse != null && grid.getCurrentRow() > 0) {
                                warehouseLookUp.addItem(firstSelectedWarehouse);
                            } else {
                                final ListingFilterParameter filterParametrs = new ListingFilterParameter();
                                filterParametrs.setProductId(product.getObjectId());
                                filterParametrs.setViewType(checkOwnPermissionType);
                                filterParametrs.setLookUp(true);
                                filterParametrs.setLimit(20);
                                AccountingService.App.get().getWarehousesForLookUp(filterParametrs, new AsyncCallback<SelectItem[]>() {
                                    @Override
                                    public void onFailure(final Throwable caught) {
                                    }

                                    @Override
                                    public void onSuccess(final SelectItem[] result) {
                                        if (result != null && result.length == 1) {
                                            warehouseLookUp.addItem(result[0]);
                                        }
                                    }
                                });
                            }
                        }

                        if (product.getInventoryTrackingEnabled()) {
                            final ItemQtyPanel itemQtyPanel = (ItemQtyPanel) widgetsMap.get(ProductsTable.QTY);
                            warehouseLookUp.getSuggestBox().addSelectionHandler(e -> itemQtyPanel.onWarehouseChangeEvent(warehouseLookUp.getSelectedItemID()));
                            if (warehouseLookUp.getSelectedItemID() != null) {
                                AccountingService.App.get().getItemQtyByWarehouse(product.getObjectId(), warehouseLookUp.getSelectedItemID(), new AsyncCallback<BigDecimal>() {
                                    @Override
                                    public void onFailure(final Throwable caught) {
                                    }

                                    @Override
                                    public void onSuccess(final BigDecimal result) {
                                        itemQtyPanel.setQtyOnHandText(ProductsTable.utils.formatQty(result));
                                    }
                                });
                            }
                        }

                        if (product.getBatchTrackingEnabled()) {
                            final ItemQtyPanel itemQtyPanel = (ItemQtyPanel) widgetsMap.get(ProductsTable.QTY);
                            warehouseLookUp.getSuggestBox().addSelectionHandler(e -> itemQtyPanel.onWarehouseChangeEvent(warehouseLookUp.getSelectedItemID()));
                        }
                        if (product.getTrackBatchesEnabled()) {
                            final ItemQtyPanel itemQtyPanel = (ItemQtyPanel) widgetsMap.get(ProductsTable.QTY);
                            warehouseLookUp.getSuggestBox().addSelectionHandler(e -> itemQtyPanel.onWarehouseChangeEvent(warehouseLookUp.getSelectedItemID()));
                            // Invoice line item, set product warehouse to trackbatch widget
                            if (!itemQtyPanel.hasWarehouse() && warehouseLookUp.getSelectedItemID() != null && product.getDefaultItemWarehouse() != null) {
                                itemQtyPanel.onWarehouseChangeEvent(product.getDefaultItemWarehouse().getId());
                            }
                        }
                        final NewProduct finalProduct = product;
                        warehouseLookUp.getSuggestBox().addSelectionHandler(e -> {
                            if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_WAREHOUSE_AUTO_SELECT) && this.grid.getCurrentRow() == 0 && (Constants.PURCHASE_INVOICE.equals(this.formType) || Constants.SALE_INVOICE.equals(this.formType))) {
                                firstSelectedWarehouse = warehouseLookUp.getSelectedItem();
                            }
                            final ItemQtyPanel itemQtyPanel = (ItemQtyPanel) widgetsMap.get(ProductsTable.QTY);
                            if (warehouseLookUp.getSelectedItemID() != null) {
                                AccountingService.App.get().getItemQtyByWarehouse(finalProduct.getObjectId(), warehouseLookUp.getSelectedItemID(), new AsyncCallback<BigDecimal>() {
                                    @Override
                                    public void onFailure(final Throwable caught) {
                                    }

                                    @Override
                                    public void onSuccess(final BigDecimal result) {
                                        itemQtyPanel.setQtyOnHandText(ProductsTable.utils.formatQty(result));
                                    }
                                });
                            }
                        });
                    } else if (invoiceItem != null) {
                        if (Constants.SALE_INVOICE.equals(this.formType) && Constants.PURCHASE_INVOICE.equals(this.formType)) {
                            warehouseLookUp.setProductID(invoiceItem.getItemID());
                        }
                        if (invoiceItem.getWarehouse() != null) {
                            warehouseLookUp.addItem(invoiceItem.getWarehouse());
                        } else if (this.defaultWarehouse != null) {
                            warehouseLookUp.addItem(this.defaultWarehouse);
                        }

                        if (invoiceItem.getInventoryTrackingEnabled()) {
                            final ItemQtyPanel itemQtyPanel = (ItemQtyPanel) widgetsMap.get(ProductsTable.QTY);
                            warehouseLookUp.getSuggestBox().addSelectionHandler(e -> itemQtyPanel.onWarehouseChangeEvent(warehouseLookUp.getSelectedItemID()));
                        }

                        if (invoiceItem.getBatchTrackingEnabled()) {
                            final ItemQtyPanel itemQtyPanel = (ItemQtyPanel) widgetsMap.get(ProductsTable.QTY);
                            warehouseLookUp.getSuggestBox().addSelectionHandler(e -> itemQtyPanel.onWarehouseChangeEvent(warehouseLookUp.getSelectedItemID()));
                        }
                        if (invoiceItem.getTrackBatchesEnabled()) {
                            final ItemQtyPanel itemQtyPanel = (ItemQtyPanel) widgetsMap.get(ProductsTable.QTY);
                            warehouseLookUp.getSuggestBox().addSelectionHandler(e -> itemQtyPanel.onWarehouseChangeEvent(warehouseLookUp.getSelectedItemID()));
                            // If track batch warehouse is null and Invoice line item has warehouse, that warehouse needs to set to trackbatch
                            if (!itemQtyPanel.hasWarehouse() && warehouseLookUp.getSelectedItemID() != null && invoiceItem.getWarehouse() != null) {
                                itemQtyPanel.onWarehouseChangeEvent(invoiceItem.getWarehouse().getId());
                            }
                        }
                    }

                    break;
                case ProductsTable.TOTAL_AMT:

                    final ExtendedHTML totalAmount = this.getZeroAsHTML();
                    //totalAmount.setWidth("98px");
                    this.addDebugID(totalAmount, "totalAmount");
                    widgetsMap.put(ProductsTable.TOTAL_AMT, totalAmount);

                    break;
                case ProductsTable.PROJECT:

                    ProjectLookUp projectLookUp = new ProjectLookUp(this.type, this.crmAccountLookUp);
                    //projectLookUp.getSuggestBox().setWidth("110px");
                    widgetsMap.put(ProductsTable.PROJECT, projectLookUp);

                    if (invoiceItem != null && invoiceItem.getProject() != null) {
                        projectLookUp.addItem(invoiceItem.getProject());
                    }

                    break;
                case ProductsTable.ATTACHMENT:
                    final ExtendedItemUploadForm uploadForm = new ExtendedItemUploadForm(Constants.F_SALE_QUOTE, null);
                    widgetsMap.put(ProductsTable.ATTACHMENT, uploadForm);
                    itemWidgetsMap.put(ProductsTable.ATTACHMENT, uploadForm);
                    if (invoiceItem != null && invoiceItem.getAttachments() != null) {
                        FileResource fileResource = null;
                        final ArrayList<FileResource> attachments = new ArrayList<>();
                        for (final FileItem attachment : invoiceItem.getAttachments()) {
                            fileResource = new FileResource();
                            fileResource.setObjectId(attachment.getId());
                            fileResource.setName(attachment.getFileName());
                            fileResource.setDescription(attachment.getDescription());
                            fileResource.setCreationDate(attachment.getDate());
                            fileResource.setContentLength(attachment.getSize());
                            fileResource.setContentType(attachment.getContentType());
                            fileResource.setGoogleDownloadLink(attachment.getGoogleDocumentLink());
                            fileResource.setAmazonLink(attachment.getAmazonLink());
                            fileResource.setOfficeDownloadLink(attachment.getOfficeDocumentLink());
                            fileResource.setUploadType(attachment.getUploadType());
                            attachments.add(fileResource);
                        }
                        uploadForm.setFiles(attachments);
                    }
                    break;
                case ProductsTable.CLIENT:
                    final CrmAccountLookUp clientLookUp = new CrmAccountLookUp(CrmConstants.CUSTOMER, true);
                    clientLookUp.setEnabled(this.columnsMap.get(key) != null && !this.columnsMap.get(key).isDisabled());
                    //clientLookUp.getSuggestBox().setWidth("110px");
                    widgetsMap.put(ProductsTable.CLIENT, clientLookUp);

                    if (product != null) {
                        if (AccountingConstants.INVENTORY_ITEM.equals(product.getType()) || AccountingConstants.ASSEMBLY_ITEM.equals(product.getType()) || AccountingConstants.PRODUCT_KIT.equals(product.getType())) {
                            clientLookUp.clear();
                            clientLookUp.setEnabled(false);
                        }
                    } else if (invoiceItem != null) {

                        if (invoiceItem.getClient() != null) {
                            clientLookUp.addItem(invoiceItem.getClient());
                        }
                        if (invoiceItem.getItemType() != null && (AccountingConstants.INVENTORY_ITEM.equals(invoiceItem.getItemType()) || AccountingConstants.ASSEMBLY_ITEM.equals(invoiceItem.getItemType()) || AccountingConstants.PRODUCT_KIT.equals(invoiceItem.getItemType()))) {
                            clientLookUp.clear();
                            clientLookUp.setEnabled(false);
                        }
                    }

                    break;
                case ProductsTable.FROM_DATE:
                    ExtendedDatePicker fromDatePicker = new ExtendedDatePicker();
                    widgetsMap.put(ProductsTable.FROM_DATE, fromDatePicker);
                    fromDatePicker.setDate(invoiceItem != null && invoiceItem.getFromDate() != null ? invoiceItem.getFromDate().getNonConvertedDate() : null);
                    break;
                case ProductsTable.TO_DATE:
                    ExtendedDatePicker toDatePicker = new ExtendedDatePicker();
                    widgetsMap.put(ProductsTable.TO_DATE, toDatePicker);
                    toDatePicker.setDate(invoiceItem != null && invoiceItem.getToDate() != null ? invoiceItem.getToDate().getNonConvertedDate() : null);
                    break;
                case ProductsTable.FAI_CATEGORY:
                    LookUp lookUp = getLookUp(invoiceItem);
                    widgetsMap.put(ProductsTable.FAI_CATEGORY, lookUp);
                    itemWidgetsMap.put(ProductsTable.FAI_CATEGORY, lookUp);
                    break;
                default:
                    if (this.customFieldsMap != null && this.customFieldsMap.get(key) != null) {
                        final CompanyCustomFieldItem fieldItem = this.customFieldsMap.get(key).cloneObject();

                        if (Constants.UI_TYPE_TEXTBOX.equals(fieldItem.getUiType()) || Constants.UI_TYPE_TEXTBOX_EMAIL.equals(fieldItem.getUiType()) || Constants.UI_TYPE_URL.equals(fieldItem.getUiType())) {
                            widgetsMap.put(key, new CustomTextBoxField(fieldItem));
                        } else if (Constants.UI_TYPE_TEXTAREA.equals(fieldItem.getUiType())) {
                            final CustomTextAreaField customTextAreaField = new CustomTextAreaField(fieldItem);
                            customTextAreaField.hideCharacterLimitPanel();
                            Validation.addAutoResizeListenerToTextArea(customTextAreaField.getTextArea());
                            widgetsMap.put(key, customTextAreaField);
                        } else if (Constants.UI_TYPE_PERCENTAGE.equals(fieldItem.getUiType())) {
                            widgetsMap.put(key, new CustomPercentageField(fieldItem));
                        } else if (Constants.UI_TYPE_DROPDOWN.equals(fieldItem.getUiType())) {
                            widgetsMap.put(key, new CustomDropDownField(fieldItem));
                        } else if (Constants.UI_TYPE_DATEPICKER.equals(fieldItem.getUiType())) {
                            widgetsMap.put(key, new CustomDatePicker(fieldItem));
                        } else if (Constants.UI_TYPE_DATEPICKER_TIME.equals(fieldItem.getUiType())) {
                            widgetsMap.put(key, new CustomDateTime(fieldItem));
                        } else if (Constants.UI_TYPE_LOOKUP.equals(fieldItem.getUiType())) {
                            widgetsMap.put(key, new CustomFieldLookUpField(fieldItem));
                        } else if (Constants.UI_TYPE_MULTI_LOOKUP.equals(fieldItem.getUiType())) {
                            widgetsMap.put(key, new CustomFieldMultiLookUpField(fieldItem));
                        }

                        if (product != null) {
                            final CompanyCustomFieldItem fitem = product.getProductCustomFieldItems() != null && !product.getProductCustomFieldItems().isEmpty() ? product.getCustomFieldByAlias(fieldItem.getAliasName()) : null;

                            if (fitem != null) {
                                fitem.setObjectId(null);
                                ((CustomFieldInterface) widgetsMap.get(key)).setFieldItem(fitem);
                            } else {
                                if (!Utils.isNullOrEmpty(product.getItemName()) && CustomFormConstants.NAME.equals(fieldItem.getAliasName()) && (UI_TYPE_TEXTBOX.equals(fieldItem.getUiType()) || UI_TYPE_TEXTAREA.equals(fieldItem.getUiType()))) {
                                    fieldItem.setFieldStringValue(product.getItemName());
                                } else if (!Utils.isNullOrEmpty(product.getDescription()) && CustomFormConstants.DESCRIPTION.equals(fieldItem.getAliasName()) && (UI_TYPE_TEXTBOX.equals(fieldItem.getUiType()) || UI_TYPE_TEXTAREA.equals(fieldItem.getUiType()))) {
                                    fieldItem.setFieldStringValue(product.getDescription());
                                } else if (product.getUnitPrice() != null && AccountingCustomFormConstants.PURCHASE_PRICE.equals(fieldItem.getAliasName()) && UI_TYPE_TEXTBOX.equals(fieldItem.getUiType()) && DATA_TYPE_NUMBER.equals(fieldItem.getDataType())) {
                                    fieldItem.setFieldStringValue(utils.formatUnitPrice(product.getUnitPrice()));
                                } else if (product.getSellingPrice() != null && AccountingCustomFormConstants.SALES_PRICE.equals(fieldItem.getAliasName()) && UI_TYPE_TEXTBOX.equals(fieldItem.getUiType()) && DATA_TYPE_NUMBER.equals(fieldItem.getDataType())) {
                                    fieldItem.setFieldStringValue(utils.formatUnitPrice(product.getSellingPrice()));
                                } else if (product.getCategoryID() != null && CustomFormConstants.CATEGORY.equals(fieldItem.getAliasName()) && UI_TYPE_LOOKUP.equals(fieldItem.getUiType()) && CustomFieldLookUpTypeEnum.PRODUCT_CATEGORY.equals(fieldItem.getLookUpTypeEnum())) {
                                    fieldItem.setFieldStringValue(product.getCategoryName());
                                    fieldItem.setSelectedId(product.getCategoryID());
                                } else if (!Utils.isNullOrEmpty(product.getInternalSKUNumber()) && CustomFormConstants.SKU_NUMBER.equals(fieldItem.getAliasName()) && (UI_TYPE_TEXTBOX.equals(fieldItem.getUiType()) || UI_TYPE_TEXTAREA.equals(fieldItem.getUiType()))) {
                                    fieldItem.setFieldStringValue(product.getInternalSKUNumber());
                                } else if (!Utils.isNullOrEmpty(product.getUpcNumber()) && CustomFormConstants.UPC_NUMBER.equals(fieldItem.getAliasName()) && (UI_TYPE_TEXTBOX.equals(fieldItem.getUiType()) || UI_TYPE_TEXTAREA.equals(fieldItem.getUiType()))) {
                                    fieldItem.setFieldStringValue(product.getUpcNumber());
                                } else if (product.getSuppliers() != null && product.getSuppliers().length > 0 && CustomFormConstants.SUPPLIERS.equals(fieldItem.getAliasName()) && UI_TYPE_LOOKUP.equals(fieldItem.getUiType()) && CustomFieldLookUpTypeEnum.SUPPLIER.equals(fieldItem.getLookUpTypeEnum())) {
                                    fieldItem.setFieldStringValue(product.getSuppliers()[0].getName());
                                    fieldItem.setSelectedId(product.getSuppliers()[0].getId());
                                } else if (!Utils.isNullOrEmpty(product.getManufacturer()) && CustomFormConstants.MANUFACTURER.equals(fieldItem.getAliasName()) && (UI_TYPE_TEXTBOX.equals(fieldItem.getUiType()) || UI_TYPE_TEXTAREA.equals(fieldItem.getUiType()))) {
                                    fieldItem.setFieldStringValue(product.getManufacturer());
                                } else if (product.getUnitMeasurement() != null && CustomFormConstants.UNIT_MEASUREMENT.equals(fieldItem.getAliasName()) && UI_TYPE_LOOKUP.equals(fieldItem.getUiType()) && CustomFieldLookUpTypeEnum.UNIT_MEASUREMENT.equals(fieldItem.getLookUpTypeEnum())) {
                                    fieldItem.setFieldStringValue(product.getUnitMeasurement().getName());
                                    fieldItem.setSelectedId(product.getUnitMeasurement().getId());
                                } else if (!Utils.isNullOrEmpty(product.getPartNumber()) && CustomFormConstants.PART_NUMBER.equals(fieldItem.getAliasName()) && (UI_TYPE_TEXTBOX.equals(fieldItem.getUiType()) || UI_TYPE_TEXTAREA.equals(fieldItem.getUiType()))) {
                                    fieldItem.setFieldStringValue(product.getPartNumber());
                                } else if (product.getAsOf() != null && "AS_OF".equals(fieldItem.getAliasName()) && UI_TYPE_DATEPICKER.equals(fieldItem.getUiType())) {
                                    fieldItem.setFieldDateNonConvertedValue(product.getAsOf());
                                }
                                ((CustomFieldInterface) widgetsMap.get(key)).setFieldItem(fieldItem);
                            }

                        } else if (invoiceItem != null && invoiceItem.getCustomFieldItems() != null && !invoiceItem.getCustomFieldItems().isEmpty()) {

                            if (invoiceItem.getID() != null) {
                                ((CustomFieldInterface) widgetsMap.get(key)).setFieldItem(invoiceItem.getCustomFieldByCode(key));
                            } else {
                                final CompanyCustomFieldItem fitem = invoiceItem.getCustomFieldByAlias(fieldItem.getAliasName());

                                if (fitem != null) {
                                    fitem.setObjectId(null);
                                    ((CustomFieldInterface) widgetsMap.get(key)).setFieldItem(fitem);
                                }
                            }
                        }
                    }

                    break;
            }
        }

        if (widgetsMap.get(ProductsTable.PROJECT) != null && widgetsMap.get(ProductsTable.CLIENT) != null) {
            ProjectLookUp projectLookUp = (ProjectLookUp) widgetsMap.get(ProductsTable.PROJECT);
            final CrmAccountLookUp clientLookUp = (CrmAccountLookUp) widgetsMap.get(ProductsTable.CLIENT);
            projectLookUp.setClientSupplierLookUp(clientLookUp);
            clientLookUp.getSuggestBox().addSelectionHandler(selectionEvent -> projectLookUp.clear());
        }

        return widgetsMap;
    }

    private LookUp getLookUp(NewInvoiceItem invoiceItem) {
        LookUp lookUp = new LookUp2();
        if (invoiceItem != null && invoiceItem.getTaxItem() != null) {
            if (Constants.SALE_INVOICE.equals(formType) || Constants.CREDIT_NOTE.equals(formType) || Constants.RECEIVABLE_CREDIT_NOTE.equals(formType)) {
                lookUp.setItems(invoiceItem.getTaxItem().getFaiCategories());
            } else if (Constants.PURCHASE_INVOICE.equals(formType) || PAYABLE_CREDIT_NOTE.equals(formType)) {
                lookUp.setItems(invoiceItem.getTaxItem().getFaiPurchaseCategories());
            }
            lookUp.setSelected(invoiceItem.getFaiCategory());
        }
        return lookUp;
    }

    private void redrawFaiCategory(TaxLookUp taxList) {
        if (taxList != null && taxList.getSelectedItemID() != null && itemsTable.getColumnId(ProductsTable.FAI_CATEGORY) != null) {
            LookUp2 value = new LookUp2();
            SelectItem[] faiCategories;
            if (Constants.SALE_INVOICE.equals(formType) || Constants.CREDIT_NOTE.equals(formType) || Constants.RECEIVABLE_CREDIT_NOTE.equals(formType)) {
                faiCategories = taxList.getData(taxList.getSelectedItemID()).getFaiCategories();
            } else if (Constants.PURCHASE_INVOICE.equals(formType) || PAYABLE_CREDIT_NOTE.equals(formType)) {
                faiCategories = taxList.getData(taxList.getSelectedItemID()).getFaiPurchaseCategories();
            } else {
                faiCategories = new SelectItem[]{};
            }
            value.setItems(faiCategories);
            LookUpCell faiCategory = (LookUpCell) grid.getWidget(itemsTable.getGrid().getCurrentRow(), itemsTable.getColumnId(ProductsTable.FAI_CATEGORY));
            if (faiCategory !=null && faiCategory.getSelectedItem()!= null && Arrays.asList(faiCategories).contains(faiCategory.getSelectedItem())) {
                value.setSelected(faiCategory.getSelectedItem());
            }
            LookUpCell updatedWidget = new LookUpCell(faiCategory.getStyleName());
            updatedWidget.setValue(value);
            updatedWidget.displayActive(true);
            grid.setWidget(itemsTable.getGrid().getCurrentRow(), itemsTable.getColumnId(ProductsTable.FAI_CATEGORY), updatedWidget);
        }
    }

    private void loadDiscountItems(final LinkedHashMap<String, Widget> itemWidgetsMap) {
        ProductLookUp productLookUp = (ProductLookUp) itemWidgetsMap.get(ProductsTable.PRODUCT);
        DataListBox dwDiscountList = (DataListBox) itemWidgetsMap.get(ProductsTable.DISCOUNT_LIST);

        if (dwDiscountList == null) {
            return;
        }

        dwDiscountList.clear();
        dwDiscountList.setEnabled(true);
        Map<String, DiscountItem> list = new HashMap<>();
        list.put(ProductsTable.wfmStrings.percentage(), new DiscountItem(Constants.ONE_OFF_DISCOUNT, ProductsTable.wfmStrings.percentage()));
        list.put(ProductsTable.wfmStrings.fixedAmount(), new DiscountItem(Constants.ONE_OFF_FIXED_AMOUNT, ProductsTable.wfmStrings.fixedAmount()));

        if (!clientDiscountsSelectItem.isEmpty()) {
            clientDiscountsSelectItem.forEach(i -> {
                DiscountItem item = new DiscountItem();
                item.setId(i.getId());
                item.setName(i.getName());
                item.setDescription(i.getDescription());
                list.put(i.getName(), item);
            });
        } else {
            if (productLookUp != null && productLookUp.getDiscountItems() != null) {
                productLookUp.getDiscountItems();
                for (DiscountItem discount : productLookUp.getDiscountItems()) {
                    if (discount != null) {
                        list.put(discount.getName(), discount);
                    }
                }
            }
        }

        dwDiscountList.setWithoutNullLabel(true);
        dwDiscountList.setItems(list.values().toArray(new DiscountItem[0]));

        if (this.clientDiscount != null) {
            dwDiscountList.addListItem(this.clientDiscount);
            dwDiscountList.setSelected(this.clientDiscount.getId());
            dwDiscountList.setEnabled(false);
        } else if (productLookUp != null && productLookUp.getItemDiscountID() != null) {
            dwDiscountList.setSelected(productLookUp.getItemDiscountID());
        } else if (productLookUp != null && Constants.ONE_OFF_FIXED_AMOUNT.equals(productLookUp.getDiscountItemStaticType())) {
/*
Temporary workaround explanation:

- When a discount is a fixed amount, it is assigned ID "1" (ONE_OFF_FIXED_AMOUNT).
- If a product has its own discount (e.g., 5%, 10%), these discounts are also assigned IDs like 1 and 2 in the database.
- This situation causes a conflict: the fixed amount discount gets overridden by the product-specific discount due to the shared ID.

To prevent this override, I reset the fixed amount discount.
This approach serves as a fast and optimal temporary solution.
A complete fix would involve reworking the entire discount logic, which could be time-consuming.
*/
            dwDiscountList.setSelected(new SelectItem(Constants.ONE_OFF_FIXED_AMOUNT, wfmStrings.fixedAmount()));
        } else {
            dwDiscountList.setSelected(defaultDiscount);
        }
    }

    private void addDebugID(final Widget widget, final String widgetName) {
        if (widget != null) {
            widget.ensureDebugId(this.formType + widgetName);
            if (widget instanceof DropDownLookup) {
                final DropDownLookup lookup = (DropDownLookup) widget;
                lookup.setEnsureDebugId(this.formType + widgetName);
            }
        }

    }

    public void setDefaultAccount(final AccountItem defaultAccount) {
        this.defaultAccount = defaultAccount;
        if (defaultAccount != null) {
            for (int i = 0; i < this.grid.getRowCount(); i++) {
                final AccountsLookUp account = (AccountsLookUp) this.itemsTable.getColumnById(i, ProductsTable.ACCOUNT);
                if (account != null) {
                    account.addAccountItem(defaultAccount);
                }
                final LookUpCell accountCell = (LookUpCell) this.itemsTable.getColumnCellWidgetById(i, ProductsTable.ACCOUNT);
                if (accountCell != null) {
                    accountCell.InActive();
                }
            }
        }
    }

    public void setDefaultTax(final TaxItem defaultTax) {
        this.defaultTax = defaultTax;
        if (defaultTax != null) {
            for (int i = 0; i < this.grid.getRowCount(); i++) {
                final TaxLookUp account = (TaxLookUp) this.itemsTable.getColumnById(i, ProductsTable.TAX_LIST);
                if (account != null) {
                    account.addTaxItem(defaultTax);
                }
                final LookUpCell accountCell = (LookUpCell) this.itemsTable.getColumnCellWidgetById(i, ProductsTable.TAX_LIST);
                if (accountCell != null) {
                    accountCell.InActive();
                }
            }
        }
    }

    public void setDefaultDiscount(final DiscountItem defaultDiscount) {
        this.defaultDiscount = defaultDiscount;
        if (defaultDiscount != null) {
            for (int i = 0; i < this.grid.getRowCount(); i++) {
                LinkedHashMap<String, Widget> itemWidgetsMap = new LinkedHashMap<>();
                final DataListBox pnlDiscountList = (DataListBox) this.itemsTable.getColumnById(i, ProductsTable.DISCOUNT_LIST);
                final ItemQtyPanel qtyPanel = (ItemQtyPanel) this.itemsTable.getColumnById(i, ProductsTable.QTY);
                final UnitPrice txtUnitPrice = (UnitPrice) this.itemsTable.getColumnById(i, ProductsTable.UNITPRICE);
                final Discount discount = (Discount) this.itemsTable.getColumnById(i, ProductsTable.DISCOUNT_AMT);
                if (pnlDiscountList != null) {
                    pnlDiscountList.setSelected(new DiscountItem(defaultDiscount.getId(), defaultDiscount.getName()));
                }
                itemWidgetsMap.put(ProductsTable.DISCOUNT_LIST, pnlDiscountList);
                itemWidgetsMap.put(ProductsTable.QTY, qtyPanel);
                itemWidgetsMap.put(ProductsTable.UNITPRICE, txtUnitPrice);
                itemWidgetsMap.put(ProductsTable.DISCOUNT_AMT, discount);
                this.onChangeDiscount(itemWidgetsMap, defaultDiscount);

                CustomCell discountListCell = (CustomCell) this.itemsTable.getColumnCellWidgetById(i, ProductsTable.DISCOUNT_LIST);
                if (discountListCell != null) {
                    discountListCell.InActive();
                }
            }
        }
    }


    public void setCustomerOrSupplierTaxItem(final TaxItem taxItem) {
        clientOrSupplierTaxItem = taxItem;
        for (int i = 0; i < this.grid.getRowCount(); i++) {
            TaxLookUp taxLookUp = (TaxLookUp) this.itemsTable.getColumnById(i, ProductsTable.TAX_LIST);
            if (taxLookUp != null && taxItem != null) {
                taxLookUp.addTaxItem(taxItem);
                taxLookUp.setSelected(taxItem);
                final LookUpCell taxCell = (LookUpCell) this.itemsTable.getColumnCellWidgetById(i, ProductsTable.TAX_LIST);
                taxCell.InActive();
            }
        }

        this.reDrawTaxesDropdown();
    }

    public void setCustomerDefaultWarehouseAndDepartment(final SelectItem department, final SelectItem warehouse) {
        defaultDepartment = department;
        defaultWarehouse = warehouse;
        for (int i = 0; i < this.grid.getRowCount(); i++) {
            final ProductLookUp productLookUp = (ProductLookUp) this.itemsTable.getColumnById(i, ProductsTable.PRODUCT);
            if (productLookUp != null && productLookUp.getSelectedItem() == null) {
                return;
            }
            final WarehouseLookUp wareHouseLookUp = (WarehouseLookUp) this.itemsTable.getColumnById(i, ProductsTable.WAREHOUSE);
            if (wareHouseLookUp != null) {
                if (this.defaultWarehouse != null) {
                    wareHouseLookUp.setSelected(this.defaultWarehouse);
                } /*else {
                    wareHouseLookUp.clear();
                }*/
                final LookUpCell wLookUp = (LookUpCell) this.itemsTable.getColumnCellWidgetById(i, ProductsTable.WAREHOUSE);
                wLookUp.InActive();
            }
            final DepartmentLookUp departmentLookUp = (DepartmentLookUp) this.itemsTable.getColumnById(i, ProductsTable.DEPARTMENT);
            if (departmentLookUp != null) {
                if (this.defaultDepartment != null) {
                    departmentLookUp.setSelected(this.defaultDepartment);
                } /*else {
                    departmentLookUp.clear();
                }*/
                final LookUpCell dCell = (LookUpCell) this.itemsTable.getColumnCellWidgetById(i, ProductsTable.DEPARTMENT);
                dCell.InActive();
            }
        }
    }


    public void resetCustomerOrSupplierTaxItem() {
        if (this.clientOrSupplierTaxItem != null) {
            for (int i = 0; i < this.grid.getRowCount(); i++) {
                final TaxLookUp taxLookUp = (TaxLookUp) this.itemsTable.getColumnById(i, ProductsTable.TAX_LIST);

                if (taxLookUp != null) {
                    taxLookUp.clear();
                    final LookUpCell taxCell = (LookUpCell) this.itemsTable.getColumnCellWidgetById(i, ProductsTable.TAX_LIST);
                    taxCell.InActive();
                }
            }
            this.reDrawTaxesDropdown();
            this.clientOrSupplierTaxItem = null;
        }
    }

    public AccountItem getDefaultAccount() {
        return this.defaultAccount;
    }

    public void setValues(final NewInvoice data) {
        this.newInvoice = data;
        this.customerSupplierItem = data.getTypeItem();

        invoiceItems = data.getItems();
        this.hasBillableExp = data.isHasBillableExpense();
        this.isProgressInvoicing = this.formType.equals(Constants.SALE_INVOICE) && data.isProgressInvoicing();

        if (!Constants.SALE_QUOTE.equals(this.formType)) {
            this.isDeleteAndAddDsiabled = (Constants.SALE_INVOICE.equals(this.formType) || Constants.PURCHASE_ORDER.equals(this.formType)) && data.isDeleteAndAddDsiabled();
        }
        if (data.getBillableExpenseAmount() != null) {
            this.billableExpenseTotal = data.getBillableExpenseAmount();
            this.billableExpenseAmount.setHTML(ProductsTable.utils.formatPrice(this.billableExpenseTotal));
        }
        if (data.getBillableExpenseTaxAmount() != null) {
            this.billableExpenseTaxTotal = data.getBillableExpenseTaxAmount();
            this.billableExpenseTaxAmount.setHTML(ProductsTable.utils.formatPrice(this.billableExpenseTaxTotal));
        }

        int length = invoiceItems.length;
        this.itemsTable.removeAllRows();
        if (this.isProgressInvoicing || this.isDeleteAndAddDsiabled) {
            this.itemsTable.setShowAddCell(false);
            // disable trash button, but when clicks it shows warning message.
            this.itemsTable.setDisableRemoveCell(true);
            this.itemsTable.setFormtType(this.formType);
        }
        for (int i = 0; i < length; i++) {
            this.itemsTable.addRow(this.getWidgets(invoiceItems[i]));
        }
        if (this.reverseChargeBox != null && this.reverseChargeBox.isAttached() && this.reverseChargeBox.isVisible()) {
            this.checkForReverseCharge();
        }

        while (length < ProductsTable.DEFAULT_ROWS && !this.isProgressInvoicing && !this.isDeleteAndAddDsiabled) {
            this.itemsTable.addRow(this.getWidgets(null));
            length++;
            if (grid.getRowCount() > 0) {
                grid.setCurrentRow(0);
                grid.setFocus(true);
            }
        }

        /*
          apply selected overall discount to total
         */
        {
            if (data.getDiscountType() != null) {
                this.totalsTable.setOverallDiscount(data.getDiscountAmount(), data.getDiscountType());
            }
        }

        /*
          apply selected shipping method to total
         */
        if (this.totalsTable.getShippingMethodWidget() != null) {
            if (data.getTypeItem() != null) {
                this.totalsTable.getShippingMethodWidget().setAppliedCrmAccountId(data.getTypeItem().getId());
            }
            if (data.getShippingMethodID() != null && data.getShippingMethodName() != null) {
                shippingMethod = data.getShippingMethod();
                this.totalsTable.getShippingMethodWidget().setSelectedMethod(data.getShippingMethod());
            }
        }

        this.calculate();
        this.updateTotal();
    }

    private void setItemValues(SmartProductLookUp productLoockup, Integer position) {
        //TODO
        final ProductSelectItem item = productLoockup.getSelectedItem();
        if (this.isExpandProductGroup && item instanceof ProductSelectItem && AccountingConstants.PRODUCT_KIT.equals(item.getProductType())) {
            ProductService.App.get().getProductKitProducts(item.getId(), this.isPurchaseOrder || this.isPurchaseInvoice, new LoadingPanelCallback<NewProduct[]>(this.itemsTable, ProductsTable.wfmStrings.pleaseWait()) {
                @Override
                public void success(final NewProduct[] items) {
                    ProductsTable.this.itemsTable.shiftRows(position, items.length - 1);
                    for (int i = 0; i < items.length; i++) {
                        ProductsTable.this.setRowData(items[i], position + i);
                    }
                }
            });
        } else {
            if (item != null && item.getId() != null && item.getId() != 0 && !CONVERT_TO_INVOICE_FROM_RENTAL_ORDER.equals(formParameters.getExternalFormID())) {
                //final Integer productId = item.getId();

                ProductService.App.get().getProductBaseData(item.getId(), new LoadingPanelCallback<NewProduct>(this.itemsTable, ProductsTable.wfmStrings.pleaseWait()) {
                    public void success(final NewProduct product) {
                        if (product.getHasVariations() != null && product.getHasVariations() && product.getChildProducts() != null) {
                            new InvoiceQuoteItemVariationPopup(product.getItemName(), product.getChildProducts(), o -> {
                                Integer pos = position;
                                final List<NewProduct> products = (List<NewProduct>) o;
                                for (final NewProduct p : products) {
                                    if (position.equals(pos)) {
                                        productLoockup.setSelected(p.getObjectId(), p.getItemName());
                                    }
                                    if (pos >= itemsTable.getGrid().getRowCount()) {
                                        final LinkedHashMap<String, Object> _widgetsMap = getWidgetsMap(p);
                                        itemsTable.addRow(_widgetsMap.values().toArray(new Widget[]{}));
                                    }
                                    setRowData(p, pos);
                                    pos++;
                                }
                            });
                        } else {
                            setRowData(product, position);
                        }
                    }
                });
            }
        }
    }


    private void setRowData(final Object object, final Integer position) {
        this.itemsTable.addRow(position, this.getWidgets(object));
        this.reDrawTaxesDropdown();
        this.checkForReverseCharge();
    }

    private void updateTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (int i = 0; i < this.grid.getRowCount(); i++) {
            final UnitPrice unitPriceTxtBox = (UnitPrice) this.itemsTable.getColumnById(i, ProductsTable.UNITPRICE);
            total = total.add(unitPriceTxtBox.getTotalAmount() != null ? unitPriceTxtBox.getTotalAmount() : BigDecimal.ZERO);
        }
        this.subTotal.setValue(total);
        this.reDrawTaxesDropdown();
    }

    private void checkForReverseCharge() {

        if (!(this.isPurchaseOrder || this.isPurchaseInvoice || this.isSupplierCreditNote)) {
            return;
        }
        boolean hasInventoryItem = false;

        for (int i = 0; i < this.grid.getRowCount(); i++) {
            final ProductLookUp productLookUp = (ProductLookUp) this.itemsTable.getColumnById(i, ProductsTable.PRODUCT);
            if (productLookUp.getSelectedItemID() != null && AccountingConstants.INVENTORY_ITEM.equals(((ProductSelectItem) productLookUp.getSelectedData()).getProductType())) {
                hasInventoryItem = true;
            }
        }

        if (this.isReverseChargeApplicable() && this.reverseChargeBox != null && this.reverseChargeBox.getParent().isVisible()) {
            if (hasInventoryItem) {
                this.reverseChargeBox.setValue(false);
                this.reverseChargeBox.setEnabled(false);
                this.clearSelectedTaxFromItems(true);
            } else {
                this.reverseChargeBox.setEnabled(true);
            }
        }
    }


    public void reDrawTaxesDropdown() {
        this.taxWidgetMap.clear();
        for (int i = 0; i < this.grid.getRowCount(); i++) {
            LookUpCell lookUpCell = null;
            LookUpCell doubleLookUpCell = null;
            TaxLookUp taxLookUp = null;
            TaxLookUp doubleTaxLookUp = null;

            if (this.itemsTable.getColumnId(ProductsTable.TAX_LIST) != null) {
                lookUpCell = (LookUpCell) this.grid.getWidget(i, this.itemsTable.getColumnId(ProductsTable.TAX_LIST));
                taxLookUp = (TaxLookUp) lookUpCell.getLookUp();
            }
            if (this.itemsTable.getColumnId(ProductsTable.DOUBLE_TAX_LIST) != null) {
                doubleLookUpCell = (LookUpCell) this.grid.getWidget(i, this.itemsTable.getColumnId(ProductsTable.DOUBLE_TAX_LIST));
                doubleTaxLookUp = (TaxLookUp) doubleLookUpCell.getLookUp();
            }
            if (lookUpCell != null && taxLookUp.getSelectedItemID() != null && !this.taxWidgetMap.containsKey(taxLookUp.getSelectedItemID())) {
                final TaxView tax = new TaxView(this.getZeroAsString());
                tax.setItem(taxLookUp.getData(taxLookUp.getSelectedItemID()));
                this.taxWidgetMap.put(taxLookUp.getSelectedItemID(), tax);
                lookUpCell.InActive();
            }
            if (doubleLookUpCell != null && doubleTaxLookUp.getSelectedItemID() != null && !this.taxWidgetMap.containsKey(doubleTaxLookUp.getSelectedItemID())) {
                final TaxView doubleTax = new TaxView(this.getZeroAsString());
                doubleTax.setItem(doubleTaxLookUp.getData(doubleTaxLookUp.getSelectedItemID()));
                this.taxWidgetMap.put(doubleTaxLookUp.getSelectedItemID(), doubleTax);
                doubleLookUpCell.InActive();
            }

            if (i == this.grid.getCurrentRow()) {
                this.redrawFaiCategory(taxLookUp);
            }
        }
        this.calculate();
        this.drawTotalsTable();
    }

    private void checkProductMultiCurrencySellingPrice() {
        BigDecimal unitPrice;
        if (Constants.RECEIVABLE.equals(this.type)) {
            for (int i = 0; i < this.grid.getRowCount(); i++) {
                final UnitPrice unitPriceTextBox = (UnitPrice) this.itemsTable.getColumnById(i, ProductsTable.UNITPRICE);
                final HashMap<String, BigDecimal> multiPricesMap = unitPriceTextBox.getMultiPricesMap();
                if (Utils.isMultipleSalesPriceEnable() && multiPricesMap != null && !multiPricesMap.isEmpty() && this.currencyId != null) {
                    if (multiPricesMap.containsKey(Constants.RECEIVABLE + this.currencyId)) {
                        unitPrice = multiPricesMap.get(Constants.RECEIVABLE + this.currencyId);
                        unitPriceTextBox.setValuableText(ProductsTable.utils.formatUnitPrice(unitPrice), unitPrice);
                        unitPriceTextBox.setExchangeRate(BigDecimal.ONE);
                        unitPriceTextBox.setIgnoreMultiPrice(false);
                    } else if (multiPricesMap.containsKey(Constants.RECEIVABLE + "-1")) {
                        unitPrice = multiPricesMap.get(Constants.RECEIVABLE + "-1");
                        unitPriceTextBox.setValuableText(ProductsTable.utils.formatUnitPrice(unitPrice), unitPrice);
                        unitPriceTextBox.setExchangeRate(BigDecimal.ONE);
                        unitPriceTextBox.setIgnoreMultiPrice(true);
                    }
                    //Cost Price Tooltip
                    if (Utils.hasPermission(PermissionConstants.ACCOUNTING_PRODUCT_COST)) {
                        BigDecimal costPrice;
                        if (multiPricesMap.containsKey(Constants.PAYABLE + this.currencyId)) {
                            costPrice = multiPricesMap.get(Constants.PAYABLE + this.currencyId);
                        } else {
                            costPrice = multiPricesMap.get(Constants.PAYABLE + "-1");
                        }
                        if (multiPricesMap.get(AVERAGE_COST) != null) {
                            costPrice = multiPricesMap.get(AVERAGE_COST);
                        }
                        costPrice = costPrice == null ? BigDecimal.ZERO : costPrice;
                        unitPriceTextBox.setCostPriceTooltip(ProductsTable.utils.formatUnitPrice(costPrice));
                    }
                }
            }
        } else if (Constants.PAYABLE.equals(this.type)) {
            for (int i = 0; i < this.grid.getRowCount(); i++) {
                final UnitPrice unitPriceTextBox = (UnitPrice) this.itemsTable.getColumnById(i, ProductsTable.UNITPRICE);
                final HashMap<String, BigDecimal> multiPricesMap = unitPriceTextBox.getMultiPricesMap();
                if (Utils.isMultipleSalesPriceEnable() && multiPricesMap != null && !multiPricesMap.isEmpty() && this.currencyId != null) {
                    if (multiPricesMap.containsKey(Constants.PAYABLE + this.currencyId)) {
                        unitPrice = multiPricesMap.get(Constants.PAYABLE + this.currencyId);
                        unitPriceTextBox.setValuableText(ProductsTable.utils.formatUnitPrice(unitPrice), unitPrice);
                        unitPriceTextBox.setExchangeRate(BigDecimal.ONE);
                        unitPriceTextBox.setIgnoreMultiPrice(false);
                    } else {
                        unitPrice = multiPricesMap.get(Constants.PAYABLE + "-1");
                        unitPrice = unitPrice == null ? BigDecimal.ZERO : unitPrice;
                        unitPriceTextBox.setValuableText(ProductsTable.utils.formatUnitPrice(unitPrice), unitPrice);
                        unitPriceTextBox.setExchangeRate(BigDecimal.ONE);
                        unitPriceTextBox.setIgnoreMultiPrice(true);
                    }
                }
            }
        }
    }

    public void calculate() {
        this.calculate(false);
    }

    private Map calculateOverallFixedAmountPerItem() {
        if (this.getOverallDiscount() == null || !this.getOverallDiscount().isEnabled() || Constants.ONE_OFF_DISCOUNT.equals(this.getOverallDiscount().getType())) {
            return Collections.emptyMap();
        }
        BigDecimal totalOverallDiscountValue = this.getOverallDiscount().getAmountInCurrency();

        if (totalOverallDiscountValue == null || totalOverallDiscountValue.compareTo(BigDecimal.ZERO) <= 0) {
            return Collections.emptyMap();
        }
        Map<Integer, BigDecimal> columnMap = new HashMap();
        BigDecimal subtotalValue = BigDecimal.ZERO;

        /*
          Overall discount with foreign currency
         */
        {
            if (this.getOverallDiscount().getCurrencyId() == null) {
                this.getOverallDiscount().setCurrencyId(this.currencyId);
            }

            if (this.currencyId == null || this.getOverallDiscount().getCurrencyId().equals(this.currencyId)) {
                totalOverallDiscountValue = totalOverallDiscountValue;
            } else if (this.currencyWidget.getBaseCurrency().getId().equals(this.currencyId)) {
                totalOverallDiscountValue = totalOverallDiscountValue.divide(this.getOverallDiscount().getExchangeRate(), AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP);
            } else if (this.getOverallDiscount().getCurrencyId().equals(this.currencyWidget.getBaseCurrency().getId())) {
                totalOverallDiscountValue = totalOverallDiscountValue.multiply(this.exchangeRateValue);
            } else {
                totalOverallDiscountValue = totalOverallDiscountValue.divide(this.getOverallDiscount().getExchangeRate(), AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP).multiply(this.exchangeRateValue);
            }
            this.getOverallDiscount().getOverallDiscountBox().setText(ProductsTable.utils.formatPrice(totalOverallDiscountValue));
        }

        for (int i = 0; i < this.grid.getRowCount(); i++) {
            BigDecimal rowQuantity = BigDecimal.ZERO;
            BigDecimal rowUnitPrice = BigDecimal.ZERO;

            Widget qtyColumn = this.itemsTable.getColumnById(i, ProductsTable.QTY);
            UnitPrice unitPriceTextBox = (UnitPrice) this.itemsTable.getColumnById(i, ProductsTable.UNITPRICE);

            if (qtyColumn != null) {
                rowQuantity = ((ItemQtyPanel) qtyColumn).getQty();
            }
            if (unitPriceTextBox != null) {
                rowUnitPrice = AccountingUtils.get().parseToBigDecimal(unitPriceTextBox.getText()).setScale(AccountingUtils.customUnitPriceScale, RoundingMode.HALF_UP);
            }

            BigDecimal rowTotalPrice = rowQuantity.multiply(rowUnitPrice).setScale(AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP);
            subtotalValue = subtotalValue.add(rowTotalPrice);
            columnMap.put(i, rowTotalPrice);
        }
        if (subtotalValue.compareTo(BigDecimal.ZERO) == 0 || columnMap.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<Integer, BigDecimal> sortedMap = columnMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));
        Map<Integer, BigDecimal> resultMap = new HashMap<>();
        BigDecimal calculatedDicountTotal = BigDecimal.ZERO;

        int j = 0;
        for (final Map.Entry<Integer, BigDecimal> sortedEntry : sortedMap.entrySet()) {
            j++;
            BigDecimal rowSubtotal = sortedEntry.getValue();

            if (rowSubtotal == null || rowSubtotal.compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }
            BigDecimal rowDiscountPercent = subtotalValue.divide(rowSubtotal, 2, RoundingMode.HALF_UP);
            BigDecimal rowDiscountValue = totalOverallDiscountValue.divide(rowDiscountPercent, 2, RoundingMode.HALF_UP);

            if (j == sortedMap.size()) {
                rowDiscountValue = totalOverallDiscountValue.subtract(calculatedDicountTotal);
            }
            resultMap.put(sortedEntry.getKey(), rowDiscountValue);
            calculatedDicountTotal = calculatedDicountTotal.add(rowDiscountValue);

        }
        return resultMap;
    }

    public void calculate(final boolean recalculate) {
        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal totalDiscount = BigDecimal.ZERO;
        BigDecimal comissionAmountTotal = BigDecimal.ZERO;
        BigDecimal totalValue;
        final BigDecimal netTotal;
        Map<Integer, BigDecimal> taxTotal = new HashMap<>();
        Map<Integer, BigDecimal> fixedAmountMap = calculateOverallFixedAmountPerItem();
        this.exchangeRateValue = Optional.ofNullable(this.exchangeRateValue).orElse(BigDecimal.ONE);

        for (int i = 0; i < this.grid.getRowCount(); i++) {
            BigDecimal quantity = BigDecimal.ZERO;
            if (AccountingConstants.PROGRESS_INVOICING.equals(this.formParameters.getExternalFormID()) && !Utils.hasGenericAccess(GenericSettingsEnum.EDIT_SALE_INVOICE_CONVERTED_FROM_SQ_SO)) {
                if (invoiceItems != null) {
                    quantity = invoiceItems[i] != null ? invoiceItems[i].getQtyWithHighScale() : BigDecimal.ZERO;
                }
            } else if (this.itemsTable.getColumnById(i, ProductsTable.QTY) != null) {
                quantity = ((ItemQtyPanel) this.itemsTable.getColumnById(i, ProductsTable.QTY)).getQty();
            }

            if (!recalculate) {
                final ProductDescriptionTextArea descriptionTextArea = (ProductDescriptionTextArea) this.itemsTable.getColumnById(i, ProductsTable.DESCRIPTION);
                if (descriptionTextArea != null && descriptionTextArea.getCustomDescription() != null) {
                    descriptionTextArea.applyCustomDescriptionToShow(quantity);
                    final CustomCell unitPriceCell = (CustomCell) this.itemsTable.getColumnCellWidgetById(i, ProductsTable.DESCRIPTION);
                    unitPriceCell.InActive();
                }
            }

            final TextBox txtComission = (TextBox) this.itemsTable.getColumnById(i, ProductsTable.COMISSION);
            final BigDecimal comission = txtComission != null ? AccountingUtils.get().parseToBigDecimal(txtComission.getText()) : BigDecimal.ZERO;

            final Discount pnlDiscount = (Discount) this.itemsTable.getColumnById(i, ProductsTable.DISCOUNT_AMT);
            final DataListBox pnlDiscountList = (DataListBox) this.itemsTable.getColumnById(i, ProductsTable.DISCOUNT_LIST);
            BigDecimal discount = BigDecimal.ZERO;
            String discountUnit = ProductsTable.DEFAULT_DISCOUNT_TYPE_UNIT;
            //Double discount
            final Discount pnlDiscount2 = (Discount) this.itemsTable.getColumnById(i, ProductsTable.DOUBLE_DISCOUNT_AMT);
            BigDecimal discount2 = BigDecimal.ZERO;
            String discountUnit2 = ProductsTable.DEFAULT_DISCOUNT_TYPE_UNIT;

            BigDecimal unitPrice = BigDecimal.ZERO;
            final UnitPrice unitPriceTextBox = (UnitPrice) this.itemsTable.getColumnById(i, ProductsTable.UNITPRICE);
            if (unitPriceTextBox != null) {
                if (unitPriceTextBox.isIgnoreMultiPrice() && (this.currencyId != null && (unitPriceTextBox.getCurrencyId() == null || !this.currencyId.equals(unitPriceTextBox.getCurrencyId())) && !this.fromMultiQuoteConvert) || recalculate) {
                    unitPriceTextBox.setExchangeRate(this.exchangeRateValue);
                    unitPriceTextBox.setCurrencyId(this.currencyId);
                    if (recalculate) {
                        unitPriceTextBox.recalculate(unitPriceTextBox);
                    }
                    unitPrice = unitPriceTextBox.getValueInBaseCurrency().multiply(this.exchangeRateValue).setScale(AccountingUtils.customUnitPriceScale, RoundingMode.HALF_UP);
                    unitPriceTextBox.setValuableText(ProductsTable.utils.formatUnitPrice(unitPrice), unitPrice);

                    if (pnlDiscount2 != null && !pnlDiscount2.getDiscountUnit().equals(ProductsTable.DEFAULT_DISCOUNT_TYPE_UNIT)) {
                        final BigDecimal discountRate2 = pnlDiscount2.getValueInBaseCurrency().multiply(this.exchangeRateValue).setScale(AccountingUtils.customUnitPriceScale, RoundingMode.HALF_UP);
                        pnlDiscount2.setValueText(ProductsTable.utils.formatDiscount(discountRate2), discountRate2);
                    }
                } else {
                    unitPrice = AccountingUtils.get().parseToBigDecimal(unitPriceTextBox.getText());
                }
            }

            if (pnlDiscount != null) {
                discount = pnlDiscount.getValue();

                if (pnlDiscount.getDiscountUnit() != null && !pnlDiscount.getDiscountUnit().equals(ProductsTable.DEFAULT_DISCOUNT_TYPE_UNIT)) {
                    pnlDiscount.setDiscountUnit(this.getCurrencyUnit());
                    discountUnit = pnlDiscount.getDiscountUnit();
                }
            } else if (pnlDiscountList != null && pnlDiscountList.getSelectedItem() != null) {
                final DiscountItem discountItem = (DiscountItem) pnlDiscountList.getSelectedItem();

                if (discountItem.getPercentage() != null && discountItem.getPercentage().compareTo(BigDecimal.ZERO) != 0) {
                    discountUnit = ProductsTable.DEFAULT_DISCOUNT_TYPE_UNIT;
                    discount = discountItem.getPercentage();
                } else if (discountItem.getFixedAmount() != null && discountItem.getFixedAmount().compareTo(BigDecimal.ZERO) != 0) {
                    discountUnit = this.getCurrencyUnit();
                    discount = discountItem.getFixedAmount();
                }
            }

            if (pnlDiscount2 != null) {
                discount2 = pnlDiscount2.getValue();

                if (pnlDiscount2.getDiscountUnit() != null && !pnlDiscount2.getDiscountUnit().equals(ProductsTable.DEFAULT_DISCOUNT_TYPE_UNIT)) {
                    pnlDiscount2.setDiscountUnit(this.getCurrencyUnit());
                    discountUnit2 = pnlDiscount2.getDiscountUnit();
                }
            }

            final BigDecimal itemTotalPrice = quantity != null ? quantity.multiply(unitPrice).setScale(AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP) : BigDecimal.ZERO;
            BigDecimal discountedNet = itemTotalPrice;
            DataListBox dwDiscountList = (DataListBox) this.itemsTable.getColumnById(i, ProductsTable.DISCOUNT_LIST);

            if (!this.getOverallDiscount().isEnabled()) {
                discountedNet = discountUnit.equals(ProductsTable.DEFAULT_DISCOUNT_TYPE_UNIT) ? itemTotalPrice.subtract(itemTotalPrice.multiply(discount).divide(AccountingConstants.HUNDRED, AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP)) : itemTotalPrice.subtract(discount);

                //Discount
                discountedNet = discountUnit2.equals(ProductsTable.DEFAULT_DISCOUNT_TYPE_UNIT) ? discountedNet.subtract(itemTotalPrice.multiply(discount2).divide(AccountingConstants.HUNDRED, AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP)) : discountedNet.subtract(discount2);
                totalDiscount = totalDiscount.add(discountUnit.equals(ProductsTable.DEFAULT_DISCOUNT_TYPE_UNIT)
                        ? itemTotalPrice.multiply(discount).divide(AccountingConstants.HUNDRED, AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP)
                        : discount);

                //Double discount
                totalDiscount = totalDiscount.add(discountUnit2.equals(ProductsTable.DEFAULT_DISCOUNT_TYPE_UNIT)
                        ? itemTotalPrice.multiply(discount2).divide(AccountingConstants.HUNDRED, AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP)
                        : discount2);

                if (dwDiscountList != null) {
                    dwDiscountList.setEnabled(true);
                }
            } else if (Constants.ONE_OFF_FIXED_AMOUNT.equals(this.getOverallDiscount().getType())) {

                if (dwDiscountList != null) {
                    dwDiscountList.setSelected(Constants.ONE_OFF_FIXED_AMOUNT);
                    dwDiscountList.setEnabled(false);
                }
                BigDecimal itemDiscount = fixedAmountMap.getOrDefault(i, BigDecimal.ZERO);
                totalDiscount = totalDiscount.add(itemDiscount);

                if (pnlDiscount != null) {
                    pnlDiscount.setDiscountUnit(this.getCurrencyUnit());
                    pnlDiscount.setValueText(ProductsTable.utils.formatDiscount(itemDiscount), itemDiscount);
                    pnlDiscount.setEnabled(false);
                }
                discountedNet = itemTotalPrice.subtract(itemDiscount);

            } else if (Constants.ONE_OFF_DISCOUNT.equals(this.getOverallDiscount().getType())) {
                BigDecimal overallDiscountValue = this.getOverallDiscount().getValue();
                BigDecimal itemDiscount = itemTotalPrice.multiply(overallDiscountValue)
                        .divide(AccountingConstants.HUNDRED, AccountingUtils.customUnitPriceScale, RoundingMode.HALF_UP);
                totalDiscount = totalDiscount.add(itemDiscount);

                if (pnlDiscount != null) {
                    pnlDiscount.setDiscountUnit(ProductsTable.DEFAULT_DISCOUNT_TYPE_UNIT);
                    pnlDiscount.setValueText(ProductsTable.utils.formatDiscount(overallDiscountValue), overallDiscountValue);
                    pnlDiscount.setEnabled(false);
                }
                discountedNet = discountUnit.equals(ProductsTable.DEFAULT_DISCOUNT_TYPE_UNIT)
                        ? itemTotalPrice.subtract(itemTotalPrice.multiply(overallDiscountValue).divide(AccountingConstants.HUNDRED, AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP))
                        : itemTotalPrice.subtract(overallDiscountValue);

                if (dwDiscountList != null) {
                    dwDiscountList.setSelected(Constants.ONE_OFF_DISCOUNT);
                    dwDiscountList.setEnabled(false);
                }
            }

            TaxLookUp taxLookUp = (TaxLookUp) this.itemsTable.getColumnById(i, ProductsTable.TAX_LIST);
            TaxLookUp doubleTaxLookUp = (TaxLookUp) this.itemsTable.getColumnById(i, ProductsTable.DOUBLE_TAX_LIST);

            BigDecimal itemTaxAmount = BigDecimal.ZERO;
            BigDecimal doubleTaxAmount = BigDecimal.ZERO;

            //calculate tax amount
            if (taxLookUp != null && taxLookUp.getSelectedItemID() != null) {
                itemTaxAmount = this.calculateTaxAmount(taxLookUp, taxTotal, discountedNet);
            } else if (taxLookUp != null) {
                taxLookUp.setItemTaxAmount(BigDecimal.ZERO);
            }

            //calculate double tax amount
            if (doubleTaxLookUp != null && doubleTaxLookUp.getSelectedItemID() != null) {
                doubleTaxAmount = this.calculateTaxAmount(doubleTaxLookUp, taxTotal, discountedNet);
            } else if (doubleTaxLookUp != null) {
                doubleTaxLookUp.setItemTaxAmount(BigDecimal.ZERO);
            }
            if (unitPriceTextBox != null) {
                unitPriceTextBox.setNetAmount(discountedNet.setScale(AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP));
                unitPriceTextBox.setTotalAmount((AccountingConstants.TAX_CALCULATION_INCLUSIVE.equals(this.taxCalculationType) || AccountingConstants.NO_TAX_CALCULATION.equals(this.taxCalculationType))
                        ? discountedNet
                        : this.isDoubleTaxEnabled
                        ? discountedNet.add(itemTaxAmount).add(doubleTaxAmount)
                        : discountedNet.add(itemTaxAmount));
                if (this.isReverseChargeApplicable() && this.reverseChargeBox.getValue()) {
                    unitPriceTextBox.setTotalAmount(unitPriceTextBox.getTotalAmount().subtract(itemTaxAmount));
                }
            }

            final CustomCell unitPriceCell = (CustomCell) this.itemsTable.getColumnCellWidgetById(i, ProductsTable.UNITPRICE);

            if (unitPriceCell != null) {
                unitPriceCell.InActive();
            }

            final CustomCell discountListCell = (CustomCell) this.itemsTable.getColumnCellWidgetById(i, ProductsTable.DISCOUNT_LIST);
            if (discountListCell != null) {
                discountListCell.InActive();
            }

            final CustomCell discountCell = (CustomCell) this.itemsTable.getColumnCellWidgetById(i, ProductsTable.DISCOUNT_AMT);
            if (discountCell != null) {
                discountCell.InActive();
            }

            final CustomCell discountCell2 = (CustomCell) this.itemsTable.getColumnCellWidgetById(i, ProductsTable.DOUBLE_DISCOUNT_AMT);
            if (discountCell2 != null) {
                discountCell2.InActive();
            }

            final ExtendedHTML netAmount = (ExtendedHTML) this.itemsTable.getColumnById(i, ProductsTable.NET_AMT);
            if (netAmount != null) {
                netAmount.setText(ProductsTable.utils.formatPrice(discountedNet));
                final CustomCell netAmountCell = (CustomCell) this.itemsTable.getColumnCellWidgetById(i, ProductsTable.NET_AMT);
                netAmountCell.InActive();
            }

            final ExtendedHTML taxAmount = (ExtendedHTML) this.itemsTable.getColumnById(i, ProductsTable.TAX_AMT);
            if (taxAmount != null) {
                taxAmount.setText(ProductsTable.utils.formatPrice(itemTaxAmount));
                final CustomCell taxAmountCell = (CustomCell) this.itemsTable.getColumnCellWidgetById(i, ProductsTable.TAX_AMT);
                taxAmountCell.InActive();
            }

            final Label totalAmount = (Label) this.itemsTable.getColumnById(i, ProductsTable.TOTAL_AMT);
            if (totalAmount != null && unitPriceTextBox.getTotalAmount() != null) {
                totalAmount.setText(ProductsTable.utils.formatPrice(unitPriceTextBox.getTotalAmount()));
                final CustomCell totalAmountCell = (CustomCell) this.itemsTable.getColumnCellWidgetById(i, ProductsTable.TOTAL_AMT);
                totalAmountCell.InActive();
            }

            subtotal = subtotal.add(itemTotalPrice);
            comissionAmountTotal = comissionAmountTotal.add(itemTotalPrice.multiply(comission).divide(AccountingConstants.HUNDRED, AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP));
        }
        totalDiscount = totalDiscount.setScale(AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP);
        netTotal = subtotal.subtract(totalDiscount);
        totalValue = subtotal.subtract(totalDiscount).add(!(AccountingConstants.TAX_CALCULATION_INCLUSIVE.equals(this.taxCalculationType) || AccountingConstants.NO_TAX_CALCULATION.equals(this.taxCalculationType)) ? this.getTotalTaxAmount(taxTotal) : BigDecimal.ZERO);

        if (this.isReverseChargeApplicable() && this.reverseChargeBox.getValue()) {
            totalValue = totalValue.subtract(this.getTotalTaxAmount(taxTotal));
        }
        BigDecimal shipViaTaxAmount = BigDecimal.ZERO;

        if (this.shippingMethod != null && this.shippingMethod.getPrice() != null) {
            BigDecimal price;

            if (this.shippingMethod.getCurrencyId().equals(this.currencyId)) {
                price = this.shippingMethod.getPrice();
            } else if (this.currencyWidget.getBaseCurrency().getId().equals(this.currencyId)) {
                price = this.shippingMethod.getPrice().divide(this.shippingMethod.getExchangeRate(), AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP);
            } else if (this.shippingMethod.getCurrencyId().equals(this.currencyWidget.getBaseCurrency().getId())) {
                price = this.shippingMethod.getPrice().multiply(this.exchangeRateValue);
            } else {
                price = this.shippingMethod.getPrice().divide(this.shippingMethod.getExchangeRate(), AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP).multiply(this.exchangeRateValue);
            }
            if (this.shippingMethodWidget != null) {
                this.shippingMethodWidget.getShippingAmountBox().setText(ProductsTable.utils.formatPrice(price));
            }

            if (this.shippingMethod.getTaxItem() != null) {
                final BigDecimal taxPercent = this.shippingMethod.getTaxItem().getEffectiveTaxPercent();
                if (AccountingConstants.TAX_CALCULATION_INCLUSIVE.equals(this.taxCalculationType)) {
                    shipViaTaxAmount = price.multiply(taxPercent).divide(AccountingConstants.HUNDRED.add(taxPercent), AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP);
                } else if (AccountingConstants.TAX_CALCULATION_EXCLUSIVE.equals(this.taxCalculationType)) {
                    shipViaTaxAmount = price.multiply(taxPercent.divide(AccountingConstants.HUNDRED, AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP));
                    price = price.add(shipViaTaxAmount);
                }
                this.shippingTaxLabel.setHTML(this.shippingMethod.getTaxItem().getName());
                this.shippingTaxValue.setValue(shipViaTaxAmount);
            }
            if (!this.isRoundingModeDisabled) {
                price = price.setScale(AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP);
            }
            totalValue = totalValue.add(price);
        }
        /*if (exchangeRateValue == null) {
            exchangeRateValue = new BigDecimal(1);
        }*/
        totalValue = totalValue.setScale(AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP);
        BigDecimal totalValueInBase = totalValue.divide(this.exchangeRateValue, AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP);
        if (!isSaleInvoice) {//  Bilable expense need to be only for view for SI, no need to add to total
            /*--------Billab Expense logic started from here*/
            totalValue = totalValue.add(this.getBillableExpenseTotal());
            totalValue = totalValue.add(this.getBillableExpenseTaxTotal());

            totalValueInBase = totalValueInBase.add(this.getBillableExpenseTotalInBase());
            totalValueInBase = totalValueInBase.add(this.getBillableExpenseTaxTotalInBase());
            /*--------Billab Expense logic end*/
        }
        this.subTotal.setValue(subtotal);
        this.total.setValue(totalValue);
        this.discountAmount.setValue(totalDiscount);
        this.comissionAmount.setValue(comissionAmountTotal);
        this.netAmountTotal.setValue(netTotal);
        this.baseTotal.setValue(totalValueInBase);

        if (this.creditableMaxInvoiceAmount != null && this.creditableMaxInvoiceAmount.compareTo(BigDecimal.ZERO) > 0) {
            if (totalValue.compareTo(this.creditableMaxInvoiceAmount) < 0) {
                this.creditedInvoiceAmount = totalValue;
            } else {
                this.creditedInvoiceAmount = this.creditableMaxInvoiceAmount;
            }
            this.creditedInvoice.setValue(this.creditedInvoiceAmount);
        }
        // for Maurico's split customisation
        if (this.comissionListener != null) {
            this.comissionListener.execute();
        }

        if (this.totalUpdateFunction != null) {
            this.totalUpdateFunction.accept(totalValue);
        }
        this.drawTotalsTable();
    }


    private BigDecimal calculateTaxAmount(final TaxLookUp taxLookUp, final Map<Integer, BigDecimal> taxTotal, final BigDecimal discountedNet) {
        TaxView taxHTML = (TaxView) this.taxWidgetMap.get(taxLookUp.getSelectedItemID());
        BigDecimal itemTaxAmount = BigDecimal.ZERO;

        if (taxHTML == null) {
            taxHTML = new TaxView();
            taxHTML.setItem(taxLookUp.getSelectedData());
            this.taxWidgetMap.put(taxLookUp.getSelectedItemID(), taxHTML);
        }

        final BigDecimal taxPercent = taxHTML.getItem().getEffectiveTaxPercent();
        if (AccountingConstants.TAX_CALCULATION_INCLUSIVE.equals(this.taxCalculationType)) {
            itemTaxAmount = discountedNet.multiply(taxPercent).divide(AccountingConstants.HUNDRED.add(taxPercent), AccountingUtils.systemCalculationScale);
        } else if (AccountingConstants.TAX_CALCULATION_EXCLUSIVE.equals(this.taxCalculationType)) {
            itemTaxAmount = discountedNet.multiply(taxPercent).divide(AccountingConstants.HUNDRED, AccountingUtils.systemCalculationScale);
        }
        final BigDecimal totalTax = taxTotal.get(taxLookUp.getSelectedItemID());
        final BigDecimal currentTaxTotal = (totalTax != null ? totalTax : BigDecimal.ZERO).add(itemTaxAmount);

        taxHTML.setHTML(ProductsTable.utils.formatPrice(currentTaxTotal));
        taxTotal.put(taxLookUp.getSelectedItemID(), currentTaxTotal);
        taxLookUp.setItemTaxAmount(itemTaxAmount);

        return itemTaxAmount;
    }

    private BigDecimal getTotalTaxAmount(final Map<Integer, BigDecimal> taxTotal) {
        BigDecimal total = BigDecimal.ZERO;

        for (final BigDecimal taxAmount : taxTotal.values()) {
            total = total.add(taxAmount.setScale(AccountingUtils.calculationScale, RoundingMode.HALF_UP));
        }

        return total;
    }

    private void onChangeDiscount(final LinkedHashMap<String, Widget> widgetsMap, DiscountItem defaultDiscountItem) {
        this.onChangeDiscount(widgetsMap, true, defaultDiscountItem);
    }

    private void onChangeDiscount(final LinkedHashMap<String, Widget> widgetsMap, final boolean calculate, DiscountItem defaultDiscountItem) {
        if (this.columnsMap.containsKey(ProductsTable.DISCOUNT_AMT)) {
            final ItemQtyPanel qtyPanel = (ItemQtyPanel) widgetsMap.get(ProductsTable.QTY);
            final UnitPrice txtUnitPrice = (UnitPrice) widgetsMap.get(ProductsTable.UNITPRICE);
            final DataListBox dwDiscountList = (DataListBox) widgetsMap.get(ProductsTable.DISCOUNT_LIST);
            final Discount discount = (Discount) widgetsMap.get(ProductsTable.DISCOUNT_AMT);

            final BigDecimal itemQty = qtyPanel.getQty();
            final BigDecimal unitPrice = txtUnitPrice.getDoubleValue().setScale(AccountingUtils.customUnitPriceScale, RoundingMode.HALF_UP);
            final BigDecimal netAmount = unitPrice.multiply(itemQty);

            DiscountItem discountItem = dwDiscountList != null ? (DiscountItem) dwDiscountList.getSelectedItem() : null;
            if (discountItem == null && defaultDiscountItem != null) {
                discountItem = defaultDiscountItem;
            }

            if (discountItem == null) {
                if (calculate) {
                    this.calculate();
                }
                return;
            }

            if ((discountItem.getPercentage() != null) || (discountItem.getId() != null && discountItem.getId().equals(Constants.ONE_OFF_DISCOUNT) || Constants.ONE_OFF_DISCOUNT_STR.equals(discountItem.getName()))) {

                discount.setDiscountUnit(ProductsTable.DEFAULT_DISCOUNT_TYPE_UNIT);
                if (discountItem.getPercentage() != null && discountItem.getPercentage().compareTo(BigDecimal.valueOf(100)) <= 0) {
                    discount.setValueText(ProductsTable.utils.formatDiscount(discountItem.getPercentage()), discountItem.getPercentage());
                } else if (discount != null && discount.getValue().compareTo(BigDecimal.valueOf(100)) > 0) {
                    discount.setValueText(BigDecimal.ZERO.toString(), BigDecimal.ZERO);
                }

                if (calculate) {
                    this.calculate();
                }
                return;
            }

            if ((discountItem.getFixedAmount() != null) || (discountItem.getId() != null && discountItem.getId().equals(Constants.SIMPLE_DISCOUNT))) {
                if (discountItem.getPercentage() != null) {
                    discount.setValueText(ProductsTable.utils.formatDiscount(discountItem.getPercentage()), discountItem.getPercentage());
                    discount.setDiscountUnit(ProductsTable.DEFAULT_DISCOUNT_TYPE_UNIT);
                } else {
                    discount.setDiscountUnit(this.getCurrencyUnit());
                    if (discountItem.getFixedAmount() != null) {
                        discount.setValueInBaseCurrency(discountItem.getFixedAmount());
                        discount.setValueText(ProductsTable.utils.formatPrice(discountItem.getFixedAmount().multiply(this.exchangeRateValue)), discountItem.getFixedAmount().multiply(this.exchangeRateValue));
                        discount.setDiscountUnit(this.getCurrencyUnit());
                    }
                }
            } else {
                boolean isInRange = false; //is Discount in Range
                if (Constants.QUANTITY.equals(discountItem.getMultiRangeDiscountType())) {
                    for (final DiscountMultiRangeItem multiRangeItem : discountItem.getMultiRangeItems()) {
                        if (multiRangeItem.getFromQty() <= itemQty.intValue() && itemQty.intValue() <= multiRangeItem.getToQty()) {
                            if (multiRangeItem.getPercentage() != null) {
                                discount.setValueText(ProductsTable.utils.format(multiRangeItem.getPercentage()), multiRangeItem.getPercentage());
                                discount.setDiscountUnit(ProductsTable.DEFAULT_DISCOUNT_TYPE_UNIT);
                            } else {
                                discount.setDiscountUnit(this.getCurrencyUnit());
                                if (multiRangeItem.getFixedAmount() != null) {
                                    discount.setValueInBaseCurrency(multiRangeItem.getFixedAmount());
                                    discount.setValueText(ProductsTable.utils.formatPrice(multiRangeItem.getFixedAmount()), multiRangeItem.getFixedAmount());
                                    discount.setDiscountUnit(this.getCurrencyUnit());
                                }
                            }

                            isInRange = true;
                        }
                    }
                } else {
                    if (discountItem.getMultiRangeItems() != null) {
                        for (final DiscountMultiRangeItem multiRangeItem : discountItem.getMultiRangeItems()) {
                            if (multiRangeItem.getFromAmount().compareTo(netAmount) <= 0 && netAmount.compareTo(multiRangeItem.getToAmount()) <= 0) {
                                if (multiRangeItem.getPercentage() != null) {
                                    discount.setValueText(ProductsTable.utils.format(multiRangeItem.getPercentage()), multiRangeItem.getPercentage());
                                    discount.setDiscountUnit(ProductsTable.DEFAULT_DISCOUNT_TYPE_UNIT);
                                } else {
                                    discount.setDiscountUnit(this.getCurrencyUnit());
                                    if (multiRangeItem.getFixedAmount() != null) {
                                        discount.setValueInBaseCurrency(multiRangeItem.getFixedAmount());
                                        discount.setValueText(ProductsTable.utils.formatPrice(multiRangeItem.getFixedAmount()), multiRangeItem.getFixedAmount());
                                        discount.setDiscountUnit(this.getCurrencyUnit());
                                    }
                                }

                                isInRange = true;
                            }
                        }
                    }
                }

                if (!isInRange) {
                    if (dwDiscountList != null) {
                        dwDiscountList.setSelected(Constants.ONE_OFF_DISCOUNT);
                    }
                    discount.setValueText(ProductsTable.utils.formatPrice(BigDecimal.ZERO), BigDecimal.ZERO);
                    discount.setDiscountUnit(ProductsTable.DEFAULT_DISCOUNT_TYPE_UNIT);
                    Info.show(ProductsTable.wfmStrings.thereAreNoDiscountRange(), Info.Type.WARNING);
                }
            }
        }

        if (calculate) {
            this.calculate();
        }
    }

    private void onChangeDoubleDiscount(final LinkedHashMap<String, Widget> widgetsMap) {
        if (this.columnsMap.containsKey(ProductsTable.DOUBLE_DISCOUNT_AMT)) {
            final ItemQtyPanel qtyPanel = (ItemQtyPanel) widgetsMap.get(ProductsTable.QTY);
            final UnitPrice txtUnitPrice = (UnitPrice) widgetsMap.get(ProductsTable.UNITPRICE);
            final DataListBox dwDiscountList2 = (DataListBox) widgetsMap.get(ProductsTable.DOUBLE_DISCOUNT_LIST);
            final Discount discount2 = (Discount) widgetsMap.get(ProductsTable.DOUBLE_DISCOUNT_AMT);
//            ExtendedHTML dwNetAmount = (ExtendedHTML) widgetsMap.get(NET_AMT);


            final BigDecimal itemQty = qtyPanel.getQty();
            final BigDecimal unitPrice = txtUnitPrice.getDoubleValue().setScale(AccountingUtils.customUnitPriceScale, RoundingMode.HALF_UP);
            final BigDecimal netAmount = unitPrice.multiply(itemQty);

            final DiscountItem discountItem2 = dwDiscountList2 != null ? (DiscountItem) dwDiscountList2.getSelectedItem() : null;

            if (discountItem2 == null) {
                discount2.setValueText(BigDecimal.ZERO.toString(), BigDecimal.ZERO);
                discount2.setDiscountUnit(ProductsTable.DEFAULT_DISCOUNT_TYPE_UNIT);
                this.calculate();
                return;
            }

            if (discountItem2.getId() != null && discountItem2.getId().equals(Constants.ONE_OFF_DISCOUNT)) {
                this.calculate();
                return;
            }

            if (discountItem2.getId() != null && discountItem2.getType().equals(Constants.SIMPLE_DISCOUNT)) {
                if (discountItem2.getPercentage() != null) {
                    discount2.setValueText(ProductsTable.utils.format(discountItem2.getPercentage()), discountItem2.getPercentage());
                    discount2.setDiscountUnit(ProductsTable.DEFAULT_DISCOUNT_TYPE_UNIT);
                } else {
                    discount2.setDiscountUnit(this.getCurrencyUnit());
                    if (discountItem2.getFixedAmount() != null) {
                        discount2.setValueInBaseCurrency(discountItem2.getFixedAmount());
                        discount2.setValueText(ProductsTable.utils.formatPrice(discountItem2.getFixedAmount().multiply(this.exchangeRateValue)), discountItem2.getFixedAmount().multiply(this.exchangeRateValue));
                        discount2.setDiscountUnit(this.getCurrencyUnit());
                    }
                }
            } else {
                boolean isInRange = false; //is Discount in Range
                if (discountItem2.getMultiRangeDiscountType().equals(Constants.QUANTITY)) {
                    for (final DiscountMultiRangeItem multiRangeItem : discountItem2.getMultiRangeItems()) {
                        if (multiRangeItem.getFromQty() <= itemQty.intValue() && itemQty.intValue() <= multiRangeItem.getToQty()) {
                            if (multiRangeItem.getPercentage() != null) {
                                discount2.setValueText(ProductsTable.utils.format(multiRangeItem.getPercentage()), multiRangeItem.getPercentage());
                                discount2.setDiscountUnit(ProductsTable.DEFAULT_DISCOUNT_TYPE_UNIT);
                            } else {
                                discount2.setDiscountUnit(this.getCurrencyUnit());
                                if (multiRangeItem.getFixedAmount() != null) {
                                    discount2.setValueInBaseCurrency(multiRangeItem.getFixedAmount());
                                    discount2.setValueText(ProductsTable.utils.formatPrice(multiRangeItem.getFixedAmount()), multiRangeItem.getFixedAmount());
                                }
                            }

                            isInRange = true;
                        }
                    }
                } else {
                    for (final DiscountMultiRangeItem multiRangeItem : discountItem2.getMultiRangeItems()) {
                        if (multiRangeItem.getFromAmount().compareTo(netAmount) <= 0 && netAmount.compareTo(multiRangeItem.getToAmount()) <= 0) {
                            if (multiRangeItem.getPercentage() != null) {
                                discount2.setValueText(ProductsTable.utils.format(multiRangeItem.getPercentage()), multiRangeItem.getPercentage());
                                discount2.setDiscountUnit(ProductsTable.DEFAULT_DISCOUNT_TYPE_UNIT);
                            } else {
                                discount2.setDiscountUnit(this.getCurrencyUnit());
                                if (multiRangeItem.getFixedAmount() != null) {
                                    discount2.setValueInBaseCurrency(multiRangeItem.getFixedAmount());
                                    discount2.setValueText(ProductsTable.utils.formatPrice(multiRangeItem.getFixedAmount()), multiRangeItem.getFixedAmount());
                                }
                            }

                            isInRange = true;
                        }
                    }
                }

                if (!isInRange) {
                    dwDiscountList2.setSelected(Constants.ONE_OFF_DISCOUNT);
                    discount2.setValueText(ProductsTable.utils.formatPrice(BigDecimal.ZERO), BigDecimal.ZERO);
                    discount2.setDiscountUnit(ProductsTable.DEFAULT_DISCOUNT_TYPE_UNIT);
                    Info.show(ProductsTable.wfmStrings.thereAreNoDiscountRange(), Info.Type.WARNING);
                }
            }
        }
        this.calculate();
    }

    public void onClientPriceLevelChange(final NewInvoice invoice) {
        final boolean fromConvert = this.formParameters.getExternalFormID() != null
                && (AccountingConstants.CONVERT_TO_INVOICE.equals(this.formParameters.getExternalFormID())
                || AccountingConstants.CONVERT_TO_INVOICE_FROM_GRN.equals(this.formParameters.getExternalFormID())
                || AccountingConstants.CONVERT_TO_INVOICE_FROM_RENTAL_ORDER.equals(this.formParameters.getExternalFormID())
                || AccountingConstants.PROGRESS_INVOICING.equals(this.formParameters.getExternalFormID()));
        final boolean fromCopy = this.formParameters.getExternalFormID() != null &&
                (AccountingConstants.COPY_FROM_EXISTING_DATA.equals(this.formParameters.getExternalFormID())
                        || AccountingConstants.COPY_INVOICE_TO_CREDITNOTE.equals(this.formParameters.getExternalFormID())
                        || AccountingConstants.COPY_FROM_SI_TO_PO.equals(this.formParameters.getExternalFormID())
                        || AccountingConstants.COPY_FROM_SI_TO_PI.equals(this.formParameters.getExternalFormID())
                        || AccountingConstants.COPY_FROM_SQ_SO_TO_PO.equals(this.formParameters.getExternalFormID())
                        || AccountingConstants.COPY_FROM_SO_TO_SQ.equals(this.formParameters.getExternalFormID())
                        || AccountingConstants.COPY_PO_TO_PI.equals(this.formParameters.getExternalFormID())
                        || AccountingConstants.COPY_FROM_PI_TO_SI.equals(this.formParameters.getExternalFormID())
                        || AccountingConstants.COPY_PO_TO_SQ.equals(this.formParameters.getExternalFormID()));
        if (fromConvert || (fromCopy && !this.isPriceLavelChanged())) {
            return;
        }
        ProductSelectItem productSelectItem = null;
        NewInvoiceItem[] items = null;
        if (invoice != null && (invoice.getItems() != null && invoice.getItems().length > 0)) {
            items = invoice.getItems();
        }
        if (this.priceLevel != null) {
            for (int i = 0; i < this.grid.getRowCount(); i++) {
                final Widget product = this.itemsTable.getColumnById(i, ProductsTable.PRODUCT);
                if (product instanceof ProductLookUp) {
                    final ProductLookUp productLookUp = (ProductLookUp) product;
                    if (productLookUp.getSelectedItemID() != null && Utils.normalize(productLookUp.getSuggestBox().getText()).equals(Utils.normalize(productLookUp.getSelectedItem().getName()))) {
                        final CustomCell unitPriceCell = (CustomCell) this.itemsTable.getColumnCellWidgetById(i, ProductsTable.UNITPRICE);
                        if (productLookUp.getSelectedItem() instanceof ProductSelectItem) {
                            productSelectItem = (ProductSelectItem) productLookUp.getSelectedItem();
                        } else {
                            productSelectItem = null;
                        }
                        unitPriceCell.InActive();
                    } else {
                        final CustomCell unitPriceCell = (CustomCell) this.itemsTable.getColumnCellWidgetById(i, ProductsTable.UNITPRICE);
                        this.setPriceLevelRate(productLookUp.getSelectedItemID(), productSelectItem != null ? productSelectItem.getBrandId() : null, this.priceLevel, (UnitPrice) unitPriceCell.getCustomWidget(), null, i);
                        unitPriceCell.InActive();
                    }
                } else {
                    final CustomCell unitPriceCell = (CustomCell) this.itemsTable.getColumnCellWidgetById(i, ProductsTable.UNITPRICE);
                    if (items != null && items.length > i) {
                        this.setPriceLevelRate(null, null, this.priceLevel, (UnitPrice) unitPriceCell.getCustomWidget(), items[i], i);
                    } else {
                        this.setPriceLevelRate(null, null, this.priceLevel, (UnitPrice) unitPriceCell.getCustomWidget(), null, i);
                    }
                }
            }
            this.calculate();
        } else {
            for (int i = 0; i < this.grid.getRowCount(); i++) {
                final CustomCell unitPriceCell = (CustomCell) this.itemsTable.getColumnCellWidgetById(i, ProductsTable.UNITPRICE);
                if (items != null && items.length > i) {
                    this.setPriceLevelRate(null, null, null, (UnitPrice) unitPriceCell.getCustomWidget(), items[i], i);
                } else {
                    this.setPriceLevelRate(null, null, null, (UnitPrice) unitPriceCell.getCustomWidget(), null, i);
                }
                unitPriceCell.InActive();
            }
            this.calculate();
        }
    }

    public void onClientDiscountChange() {
        for (int i = 0; i < this.grid.getRowCount(); i++) {
            final LookUpCell lookUpCell = (LookUpCell) this.grid.getWidget(i, this.itemsTable.getColumnId(ProductsTable.PRODUCT));
            final ProductLookUp productLookUp = (ProductLookUp) lookUpCell.getLookUp();

            this.loadDiscountItems(productLookUp.getItemWidgetsMap());

            final CustomCell comboBoxCell = (CustomCell) this.itemsTable.getColumnCellWidgetById(i, ProductsTable.DISCOUNT_LIST);

            if (comboBoxCell == null) {
                continue;
            }

            final DataListBox dwDiscountList = (DataListBox) comboBoxCell.getCustomWidget();
            final Discount discount = (Discount) this.itemsTable.getColumnById(i, ProductsTable.DISCOUNT_AMT);

            if (discount == null) {
                continue;
            }

            dwDiscountList.setEnabled(true);
            discount.setEnabled(true);
            if (this.clientDiscount != null) {
                if (dwDiscountList.getItemsById().get(this.clientDiscount.getId()) == null) {
                    dwDiscountList.addListItem(this.clientDiscount);
                }
                dwDiscountList.setSelected(this.clientDiscount.getId());
                dwDiscountList.setEnabled(false);
                discount.setEnabled(false);
            }
//            else if (!this.isEditForm) {
//                dwDiscountList.setSelected(Constants.ONE_OFF_DISCOUNT);
////                When user converts SI to new SI, discount should be copied from old SI to new SI, this line code also effect convert to invocie from SQ
////                discount.setValueText(ProductsTable.utils.formatDiscount(BigDecimal.ZERO), BigDecimal.ZERO);
////                discount.setValueInBaseCurrency(BigDecimal.ZERO);
//                discount.setDiscountUnit(ProductsTable.DEFAULT_DISCOUNT_TYPE_UNIT);
//            }

            comboBoxCell.InActive();
            this.onChangeDiscount(productLookUp.getItemWidgetsMap(), false, null);
        }
        this.calculate();
    }

    public void drawTotalsTable() {
        this.totalsTable.clear();
        this.totalsTable.setSubtotalItem(this.subTotalLabel, this.subTotal);

        if (this.columnsMap.containsKey(ProductsTable.DISCOUNT_AMT)) {
            this.totalsTable.setDiscountItem(this.discountLabel, this.discountAmount);
            this.totalsTable.getOverallDiscount().setEnableDiscountSwitcher(!columnsMap.get(ProductsTable.DISCOUNT_AMT).isDisabled());
        }

        for (final Integer key : this.taxWidgetMap.keySet()) {
            final TaxView tax = (TaxView) this.taxWidgetMap.get(key);
            final HTML taxLabel = new HTML(tax.getItem().getName());
            taxLabel.setStyleName(AccountingCustomFormConstants.STYLE_TOTAL_LABEL);
            tax.setStyleName(AccountingCustomFormConstants.STYLE_TOTAL_VALUE);

            this.totalsTable.addItem(taxLabel, tax);
        }
        if (this.hasBillableExp) {
            this.totalsTable.addItem(this.billableExpenseAmountLabel, this.billableExpenseAmount);
            this.totalsTable.addItem(this.billableExpenseTaxLabel, this.billableExpenseTaxAmount);
        }
        if (this.shippingMethod != null && this.shippingMethod.getPrice() != null) {

            if (this.shippingMethod.getTaxItem() != null) {
                this.totalsTable.addShippingTax(this.shippingTaxLabel, this.shippingTaxValue);
            }
        }
        this.totalsTable.addGrossItem(this.totalLabel, this.total);

        if (this.currencyId != null
                && this.currencyWidget.getBaseCurrency() != null
                && !this.currencyId.equals(this.currencyWidget.getBaseCurrency().getId())) {
            this.totalsTable.addGrossItem(this.baseTotalLabel, this.baseTotal);
        }

        //payments & due initialization
        if (newInvoice != null && newInvoice.getPaymentItems() != null) {
            if (newInvoice.getPaymentItems().length > 0) {
                PaymentItem[] paymentItems = newInvoice.getPaymentItems();

                if (paymentItems != null) {
                    for (PaymentItem paymentItem : paymentItems) {
                        setPaymentInfoToTable(paymentItem);
                    }
                }

                /*DUE AMOUNT*/
                HTML label = new HTML(wfmStrings.dueAmount());
                HTML value = new HTML(utils.formatPrice(newInvoice.getDueAmount()));
                new KpiToolTip(value, String.valueOf(newInvoice.getDueAmount()));
                totalsTable.setDueAmount(label, value);
            }
        }
    }

    protected void setPaymentInfoToTable(PaymentItem item) {
        boolean isPaymentEnabledUser = Utils.hasPermission(ACCOUNTING_RECEIVE_PAYMENT_LIST);
        boolean receivePaymentView = Utils.hasPermission(RECEIVE_PAYMENT_SUMMARY);
        PaymentInformation paymentInformation = null;

        boolean isCreditNote = RECEIVABLE_CREDIT_NOTE.equals(formType) || PAYABLE_CREDIT_NOTE.equals(formType);

        if (item.isInvoiceCreditNoteAllocation()) {

            if (isCreditNote) {

                if (isPaymentEnabledUser) {
                    paymentInformation = new PaymentInformation(item, PAYABLE_CREDIT_NOTE.equals(formType) || PURCHASE_INVOICE.equals(formType) ? accountingStrings.lessDebitToInvoice() : accountingStrings.lessCreditToInvoice(), (RECEIVABLE.equals(newInvoice.getType()) ? "saleinvoice|summary/" : "purchaseinvoice|summary/") + item.getInvoice().getId());
                } else {
                    paymentInformation = new PaymentInformation(item, PAYABLE_CREDIT_NOTE.equals(formType) || PURCHASE_INVOICE.equals(formType) ? accountingStrings.lessDebitToInvoice() : accountingStrings.lessCreditToInvoice());
                }
            } else {

                if (isPaymentEnabledUser) {
                    paymentInformation = new PaymentInformation(item, PAYABLE_CREDIT_NOTE.equals(formType) || PURCHASE_INVOICE.equals(formType) ? accountingStrings.lessDebitToInvoice() : accountingStrings.lessCreditNote(), (RECEIVABLE.equals(newInvoice.getType()) ? "receivablecreditnote|summary/" : "payablecreditnote|summary/") + item.getCreditNote().getId());
                } else {
                    paymentInformation = new PaymentInformation(item, PAYABLE_CREDIT_NOTE.equals(formType) || PURCHASE_INVOICE.equals(formType) ? accountingStrings.lessDebitToInvoice() : accountingStrings.lessCreditNote());
                }
            }
        } else {

            if (receivePaymentView) {

                if (isCreditNote) {
                    paymentInformation = new PaymentInformation(item, accountingStrings.lessCashRefund(), "invoicepayment|paymentView/" + item.getObjectId() + "/cashRefund");
                } else {
                    if (item.getBatchPaymentID() != null) {
                        paymentInformation = new PaymentInformation(item, accountingStrings.lessPayment(), "receivepayment|summary/" + item.getBatchPaymentID() + "/" + newInvoice.getType());
                    } else {
                        paymentInformation = new PaymentInformation(item, accountingStrings.lessPayment(), "invoicepayment|paymentView/" + item.getObjectId());
                    }
                }
            } else {
                paymentInformation = new PaymentInformation(item, isCreditNote ? accountingStrings.lessCashRefund() : accountingStrings.lessPayment());
            }
        }
        totalsTable.addPaidItem(paymentInformation, (paymentInformation.getAction() != null) ? new MaterialLink(item.getAmount().toString(), paymentInformation.getAction()) : new HTML(item.getAmount().toString()));
    }

    /**
     * Implement selected Price Level for the items
     *
     * @param productID
     * @param brandID
     * @param priceLevel
     * @param unitPrice
     * @param item
     */
    private void setPriceLevelRate(final Integer productID, final Integer brandID, final PriceLevelItem priceLevel, final UnitPrice unitPrice, final NewInvoiceItem item, final Integer index) {
        boolean hasProduct = false;
//        unitPrice.setEnabled(!isProgressInvoicing);

        if (item != null && item.isSoldOut()) {
            unitPrice.setEnabled(false);
        }

        if (priceLevel != null) {
            if (priceLevel.getType().equals(Constants.PER_PRODUCT)) {
                if (productID != null && !AccountingConstants.CONVERT_TO_INVOICE.equals(this.formParameters.getExternalFormID()) && !AccountingConstants.CONVERT_TO_INVOICE_FROM_GRN.equals(this.formParameters.getExternalFormID())) {
                    PriceLevelService.App.get().getPriceLevelPPItem(productID, priceLevel.getId(), new AsyncCallback<PriceLevelPPItem>() {
                        @Override
                        public void onFailure(final Throwable throwable) {
                            BigDecimal customPrice = unitPrice.getDoubleValue();
                            if (item != null && item.getPriceLevelAmount() != null && item.getUnitPrice().compareTo(customPrice) == 0) {
                                customPrice = customPrice.subtract(item.getPriceLevelAmount());
                            }
                            ProductsTable.this.priceLevelMap.put(index, null);
                            unitPrice.setValuableText(ProductsTable.utils.formatUnitPrice(customPrice), unitPrice.getDoubleValue());
                            ProductsTable.this.afterInc = true;
                            ProductsTable.this.calculate(true);
                        }

                        @Override
                        public void onSuccess(final PriceLevelPPItem priceLevelPPItem) {
                            if (priceLevelPPItem != null) {
                                final BigDecimal customPrice = priceLevelPPItem.getCustomPrice() != null ? BigDecimal.valueOf(priceLevelPPItem.getCustomPrice()) : new BigDecimal("0");
                                //BigDecimal standartPrice = priceLevelPPItem.getStandarPrice() != null ? new BigDecimal(priceLevelPPItem.getStandarPrice()) : new BigDecimal(0d);

                                final BigDecimal priceInBaseCurrency;

                                if (ProductsTable.this.currencyId.equals(priceLevel.getCurrency().getId())) {
                                    priceInBaseCurrency = customPrice.divide(ProductsTable.this.exchangeRateValue, AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP);
                                } else {
                                    priceInBaseCurrency = customPrice;
                                }
                                ProductsTable.this.priceLevelMap.put(index, priceInBaseCurrency);
                                unitPrice.setValueInBaseCurrency(priceInBaseCurrency);

                                if (ProductsTable.this.currencyId.equals(priceLevel.getCurrency().getId())) {
                                    unitPrice.setValuableText(ProductsTable.utils.formatUnitPrice(customPrice), customPrice);
                                } else {
                                    unitPrice.setValuableText(ProductsTable.utils.formatUnitPrice(priceInBaseCurrency.multiply(ProductsTable.this.exchangeRateValue)), priceInBaseCurrency.multiply(ProductsTable.this.exchangeRateValue));
                                }

                                if (Utils.hasGenericAccess(GenericSettingsEnum.LOCK_UNIT_PRICE_WHEN_PRICE_LEVEL_APPLIED) && (ProductsTable.this.isPurchaseInvoice || ProductsTable.this.isPurchaseOrder)) {
                                    unitPrice.setEnabled(false);
                                }
                            } else {
                                BigDecimal customPrice = unitPrice.getDoubleValue();
                                if (item != null && item.getPriceLevelAmount() != null && item.getUnitPrice().compareTo(customPrice) == 0) {
                                    customPrice = customPrice.subtract(item.getPriceLevelAmount());
                                }
                                ProductsTable.this.priceLevelMap.put(index, null);
                                unitPrice.setValuableText(ProductsTable.utils.formatUnitPrice(customPrice), unitPrice.getDoubleValue());
                                ProductsTable.this.afterInc = true;
                            }
                            ProductsTable.this.calculate(true);
                        }
                    });
                }
            } else if (priceLevel.getType().equals(Constants.BY_BRAND)) {
                if (brandID != null) {
                    for (final PriceLevelBBItem priceLevelBBItem : priceLevel.getPriceLevelBBItems()) {
                        if (brandID.equals(priceLevelBBItem.getBrand().getId())) {
                            hasProduct = true;
                            BigDecimal customPrice = unitPrice.getDoubleValue();
                            final BigDecimal originalPrice;
                            final BigDecimal priceLevelAmount;
                            final BigDecimal priceInBaseCurrency;
                            if (priceLevelBBItem.getEffectType().equals(Constants.DECREASE)) {
                                if (item != null && item.getPriceLevelAmount() != null && item.getUnitPrice().compareTo(customPrice) == 0) {
                                    if (this.afterInc) {
                                        customPrice = customPrice.subtract(item.getPriceLevelAmount());
                                    } else {
                                        customPrice = customPrice.add(item.getPriceLevelAmount());
                                    }
                                }
                                if (this.exchangeRateValue.compareTo(BigDecimal.ZERO) != 0) {
                                    priceInBaseCurrency = customPrice.divide(this.exchangeRateValue, AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP);
                                } else {
                                    priceInBaseCurrency = customPrice;
                                }
                                originalPrice = customPrice;
                                priceLevelAmount = customPrice.multiply(BigDecimal.valueOf(priceLevelBBItem.getPercentage())).divide(AccountingConstants.HUNDRED, AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP);
                                customPrice = customPrice.subtract(priceLevelAmount);
                                if (index != null) {
                                    this.priceLevelMap.put(index, priceLevelAmount);
                                }
                                this.afterDec = true;
                                this.afterInc = false;
                            } else {
                                if (item != null && item.getPriceLevelAmount() != null && item.getUnitPrice().compareTo(customPrice) == 0) {
                                    if (this.afterDec) {
                                        customPrice = customPrice.add(item.getPriceLevelAmount());
                                    } else {
                                        customPrice = customPrice.subtract(item.getPriceLevelAmount());
                                    }
                                }
                                if (this.exchangeRateValue.compareTo(BigDecimal.ZERO) != 0) {
                                    priceInBaseCurrency = customPrice.divide(this.exchangeRateValue, AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP);
                                } else {
                                    priceInBaseCurrency = customPrice;
                                }
                                originalPrice = customPrice;
                                priceLevelAmount = customPrice.multiply(BigDecimal.valueOf(priceLevelBBItem.getPercentage())).divide(AccountingConstants.HUNDRED, AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP);
                                customPrice = customPrice.add(priceLevelAmount);
                                if (index != null) {
                                    this.priceLevelMap.put(index, priceLevelAmount);
                                }
                                this.afterInc = true;
                                this.afterDec = false;
                            }
                            unitPrice.setValueInBaseCurrency(priceInBaseCurrency);
                            unitPrice.setValuableText(ProductsTable.utils.formatUnitPrice(customPrice), originalPrice);

                            if (Utils.hasGenericAccess(GenericSettingsEnum.LOCK_UNIT_PRICE_WHEN_PRICE_LEVEL_APPLIED) && (this.isPurchaseInvoice || this.isPurchaseOrder) && priceInBaseCurrency.compareTo(BigDecimal.ZERO) > 0) {
                                unitPrice.setEnabled(false);
                            }
                        }
                    }
                    if (!hasProduct) {
                        BigDecimal customPrice = unitPrice.getDoubleValue();
                        if (item != null && item.getPriceLevelAmount() != null && item.getUnitPrice().compareTo(customPrice) == 0) {
                            customPrice = customPrice.subtract(item.getPriceLevelAmount());
                        }
                        this.priceLevelMap.put(index, null);
                        unitPrice.setValuableText(ProductsTable.utils.formatUnitPrice(customPrice), unitPrice.getDoubleValue());
                        this.afterInc = true;
                    }

                }
            } else if (priceLevel.getType().equals(Constants.FIXED_PERCENTAGE)) {
                BigDecimal customPrice = unitPrice.getDoubleValue();
                final BigDecimal originalPrice;
                final BigDecimal priceLevelAmount;
                final BigDecimal priceInBaseCurrency;
                if (priceLevel.getPLCase().equals(Constants.DECREASE)) {
                    if (item != null && item.getPriceLevelAmount() != null && item.getUnitPrice().compareTo(customPrice) == 0) {
                        if (this.afterInc) {
                            customPrice = customPrice.subtract(item.getPriceLevelAmount());
                        } else {
                            customPrice = customPrice.add(item.getPriceLevelAmount());
                        }
                    }
                    if (this.exchangeRateValue.compareTo(BigDecimal.ZERO) != 0) {
                        priceInBaseCurrency = customPrice.divide(this.exchangeRateValue, AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP);
                    } else {
                        priceInBaseCurrency = customPrice;
                    }
                    originalPrice = customPrice;
                    priceLevelAmount = customPrice.multiply(BigDecimal.valueOf(priceLevel.getPercent())).divide(AccountingConstants.HUNDRED, AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP);
                    customPrice = customPrice.subtract(priceLevelAmount);
                    if (index != null) {
                        this.priceLevelMap.put(index, priceLevelAmount);
                    }
                    this.afterDec = true;
                    this.afterInc = false;
                } else {
                    if (item != null && item.getPriceLevelAmount() != null && item.getUnitPrice().compareTo(customPrice) == 0) {
                        if (this.afterDec) {
                            customPrice = customPrice.add(item.getPriceLevelAmount());
                        } else {
                            customPrice = customPrice.subtract(item.getPriceLevelAmount());
                        }
                    }
                    if (this.exchangeRateValue.compareTo(BigDecimal.ZERO) != 0) {
                        priceInBaseCurrency = customPrice.divide(this.exchangeRateValue, AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP);
                    } else {
                        priceInBaseCurrency = customPrice;
                    }
                    originalPrice = customPrice;
                    priceLevelAmount = customPrice.multiply(BigDecimal.valueOf(priceLevel.getPercent())).divide(AccountingConstants.HUNDRED, AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP);
                    customPrice = customPrice.add(priceLevelAmount);
                    if (index != null) {
                        this.priceLevelMap.put(index, priceLevelAmount);
                    }
                    this.afterInc = true;
                    this.afterDec = false;
                }
                unitPrice.setValueInBaseCurrency(priceInBaseCurrency);
                unitPrice.setValuableText(ProductsTable.utils.formatUnitPrice(customPrice), originalPrice);

                if (Utils.hasGenericAccess(GenericSettingsEnum.LOCK_UNIT_PRICE_WHEN_PRICE_LEVEL_APPLIED) && (this.isPurchaseInvoice || this.isPurchaseOrder) && priceInBaseCurrency.compareTo(BigDecimal.ZERO) > 0) {
                    unitPrice.setEnabled(false);
                }
            }
        } /*else {
            BigDecimal customPrice = unitPrice.getDoubleValue();

            if (item != null && item.getPriceLevelAmount() != null && item.getUnitPrice().compareTo(customPrice) == 0) {
                customPrice = customPrice.subtract(item.getPriceLevelAmount());
            }

            priceLevelMap.put(index, null);
            unitPrice.setValuableText(utils.formatUnitPrice(customPrice), unitPrice.getDoubleValue());
        }*/

    }

    public ShippingMethod getShippingMethod() {
        return this.shippingMethod;
    }

    private void setShippingMethod(final ShippingMethod shippingAmount) {
        shippingMethod = shippingAmount;
        this.calculate();
        this.drawTotalsTable();
    }

    public BigDecimal getExchangeRateValue() {
        return this.exchangeRateValue;
    }

    public void setExchangeRateValue(final BigDecimal exchangeRateValue) {
        this.exchangeRateValue = exchangeRateValue;
    }

    public BigDecimal getSubTotal() {
        return AccountingUtils.get().parseToBigDecimal(this.subTotal.getText()).setScale(AccountingUtils.calculationScale, RoundingMode.HALF_UP);
    }

    public BigDecimal getShippingTotal() {

        if (this.shippingMethodWidget != null) {
            return ProductsTable.utils.parseToBigDecimal(this.shippingMethodWidget.getShippingAmountBox().getText()).setScale(AccountingUtils.calculationScale, RoundingMode.HALF_UP);
        } else {
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal getTotalInBaseCurrency() {
        return AccountingUtils.get().parseToBigDecimal(this.baseTotal.getText()).setScale(AccountingUtils.calculationScale, RoundingMode.HALF_UP);
    }

    public BigDecimal getTotalInInvoiceCurrency() {
        return AccountingUtils.get().parseToBigDecimal(this.total.getText()).setScale(AccountingUtils.calculationScale, RoundingMode.HALF_UP);
    }

    public BigDecimal getComissionAmount() {
        return this.comissionAmount.getValue() != null ? this.comissionAmount.getValue() : AccountingUtils.get().parseToBigDecimal(this.comissionAmount.getText()).setScale(AccountingUtils.calculationScale, RoundingMode.HALF_UP);
    }

    public BigDecimal getTotalDiscount() {
        return AccountingUtils.get().parseToBigDecimal(this.discountAmount.getText()).setScale(AccountingUtils.calculationScale, RoundingMode.HALF_UP);
    }

    public BigDecimal getNetAmountTotal() {
        return this.netAmountTotal.getValue() != null ? this.netAmountTotal.getValue() : AccountingUtils.get().parseToBigDecimal(this.netAmountTotal.getText()).setScale(AccountingUtils.calculationScale, RoundingMode.HALF_UP);
    }

    public HTML getTotalLabel() {
        return this.totalLabel;
    }

    public HTML getBaseTotalLabel() {
        return this.baseTotalLabel;
    }

    public TextArea2 getPaymentInstruction() {
        return this.paymentInstruction;
    }

    public UnitPrice getUnitPriceInstance() {
        return new UnitPrice(this.exchangeRateValue, currencyId);
    }

    public ProductDescriptionTextArea getDescriptionTextAreaInstance() {
        return new ProductDescriptionTextArea(TextArea2.AREA_LENGTH_3);
    }

    public Discount getDiscountInstance() {
        return new Discount(this.exchangeRateValue);
    }

    public EditableTable getItemsTable() {
        return this.itemsTable;
    }

    public ReceiptTable getTotalsTable() {
        return this.totalsTable;
    }

    public ReceiptTable.OverallDiscount getOverallDiscount() {
        return this.totalsTable.getOverallDiscount();
    }


    public BigDecimal getSubTotalHighScale() {
        return AccountingUtils.get().parseToBigDecimal(this.subTotal.getValueString()).setScale(AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP);
    }

    public BigDecimal getTotalInBaseCurrencyHighScale() {
        return AccountingUtils.get().parseToBigDecimal(this.baseTotal.getValueString()).setScale(AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP);
    }

    public BigDecimal getTotalInInvoiceCurrencyHighScale() {
        return AccountingUtils.get().parseToBigDecimal(this.total.getValueString()).setScale(AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP);
    }

    public BigDecimal getTotalDiscountHighScale() {
        return AccountingUtils.get().parseToBigDecimal(this.discountAmount.getValueString()).setScale(AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP);
    }

    public BigDecimal getShippingTaxAmount() {
        BigDecimal shipViaTaxAmount = AccountingUtils.get().parseToBigDecimal(this.shippingTaxValue.getText()).setScale(AccountingUtils.calculationScale, RoundingMode.HALF_UP);
        shipViaTaxAmount = shipViaTaxAmount.divide(this.exchangeRateValue, AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP);
        return shipViaTaxAmount;
    }

    public BigDecimal getTotalTaxAmountByCurrency() {
        BigDecimal totalTaxAmount = BigDecimal.ZERO;
        boolean isBaseCurrency = currencyWidget.getBaseCurrency().getId().equals(this.currencyId);
        for (int i = 0; i < this.grid.getRowCount(); i++) {
            final TaxLookUp taxLookUp = (TaxLookUp) this.itemsTable.getColumnById(i, ProductsTable.TAX_LIST);
            final TaxLookUp doubleTaxLookUp = (TaxLookUp) this.itemsTable.getColumnById(i, ProductsTable.DOUBLE_TAX_LIST);

            if (taxLookUp != null && taxLookUp.getSelectedItemID() != null) {
                totalTaxAmount = totalTaxAmount.add(taxLookUp.getItemTaxAmount());
            }
            if (doubleTaxLookUp != null && doubleTaxLookUp.getSelectedItemID() != null) {
                totalTaxAmount = totalTaxAmount.add(doubleTaxLookUp.getItemTaxAmount());
            }
        }

        return isBaseCurrency ? totalTaxAmount.setScale(AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP) : totalTaxAmount.divide(this.exchangeRateValue, AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP);
    }

    public TotalTaxItem[] getTotalTaxItems() {
        final TotalTaxItem[] totalTaxItems = new TotalTaxItem[this.taxWidgetMap.size()];
        int i = 0;
        for (final Integer key : this.taxWidgetMap.keySet()) {
            final TaxView tax = (TaxView) this.taxWidgetMap.get(key);
            totalTaxItems[i] = new TotalTaxItem();
            totalTaxItems[i].setTaxItem(tax.getItem());
            totalTaxItems[i].setTaxAmount(AccountingUtils.get().parseToBigDecimal(tax.getText()));
            i++;
        }
        return totalTaxItems;
    }

    public NewInvoiceItem[] getDataItems() {
        return this.getDataItems(null);
    }

    public NewInvoiceItem[] getDataItems(final String status) {

        final NewInvoiceItem[] items = new NewInvoiceItem[this.itemsTable.getValidRows()];
        int j = -1;
        for (int i = 0; i < this.grid.getRowCount(); i++) {

            if (!this.itemsTable.isItemValid(i) && !Constants.DRAFT.equals(status)) {
                continue;
            }
            NewInvoiceItem item = new NewInvoiceItem();
            Map<String, CompanyCustomFieldItem> itemCFsValues = new LinkedHashMap<>();

            final Widget widget = this.itemsTable.getColumnById(i, ProductsTable.PRODUCT);
            final ProductDescriptionTextArea description = (ProductDescriptionTextArea) this.itemsTable.getColumnById(i, ProductsTable.DESCRIPTION);
            final ItemQtyPanel qtyPanel = (ItemQtyPanel) this.itemsTable.getColumnById(i, ProductsTable.QTY);
            final MeasurementsLookUp mu = (MeasurementsLookUp) this.itemsTable.getColumnById(i, ProductsTable.MEASUREMENT);
            final UnitPrice unitPrice = (UnitPrice) this.itemsTable.getColumnById(i, ProductsTable.UNITPRICE); //todo convert to BigDecimal
            final TextBox txtComission = (TextBox) this.itemsTable.getColumnById(i, ProductsTable.COMISSION);
            final DataListBox discountList = (DataListBox) this.itemsTable.getColumnById(i, ProductsTable.DISCOUNT_LIST);
            final Discount pnlDiscount = (Discount) this.itemsTable.getColumnById(i, ProductsTable.DISCOUNT_AMT);
            final DataListBox discountList2 = (DataListBox) this.itemsTable.getColumnById(i, ProductsTable.DOUBLE_DISCOUNT_LIST);
            final Discount pnlDiscount2 = (Discount) this.itemsTable.getColumnById(i, ProductsTable.DOUBLE_DISCOUNT_AMT);
            final DepartmentLookUp departmentLookUp = (DepartmentLookUp) this.itemsTable.getColumnById(i, ProductsTable.DEPARTMENT);
            final AccountsLookUp account = (AccountsLookUp) this.itemsTable.getColumnById(i, ProductsTable.ACCOUNT);
            final TaxLookUp taxLookUp = (TaxLookUp) this.itemsTable.getColumnById(i, ProductsTable.TAX_LIST);
            final TaxLookUp doubleTaxLookUp = (TaxLookUp) this.itemsTable.getColumnById(i, ProductsTable.DOUBLE_TAX_LIST);
            final WarehouseLookUp warehouseLookUp = (WarehouseLookUp) this.itemsTable.getColumnById(i, ProductsTable.WAREHOUSE);
            final ProjectLookUp projectLookUp = (ProjectLookUp) this.itemsTable.getColumnById(i, ProductsTable.PROJECT);
            final CrmAccountLookUp clientLookUp = (CrmAccountLookUp) this.itemsTable.getColumnById(i, ProductsTable.CLIENT);
            final ExtendedItemUploadForm uploadForm = (ExtendedItemUploadForm) this.itemsTable.getColumnById(i, ProductsTable.ATTACHMENT);
            final ExtendedDatePicker fromDate = (ExtendedDatePicker) this.itemsTable.getColumnById(i, ProductsTable.FROM_DATE);
            final ExtendedDatePicker toDate = (ExtendedDatePicker) this.itemsTable.getColumnById(i, ProductsTable.TO_DATE);
            LookUp faiCategory = (LookUp) this.itemsTable.getColumnById(i, ProductsTable.FAI_CATEGORY);

            final BigDecimal discountedNet = unitPrice.getNetAmount();
            final BigDecimal discount;

            if (widget instanceof ProductLookUp) {
                final ProductLookUp product = (ProductLookUp) widget;
                if (product.getSelectedItemID() != null && Utils.normalize(product.getSuggestBox().getText()).equals(Utils.normalize(product.getSelectedItem().getName()))) {
                    final ProductSelectItem productSelectItem = (ProductSelectItem) product.getSelectedData();
                    item.setProductType(productSelectItem.getProductType());
                    item.setItemID(product.getSelectedItemID());
                    item.setExpanceItemId(productSelectItem.getExpItemId());
                    final String[] split = product.getSuggestBox().getText().split(" -> ");
                    item.setItemName(split.length > 1 ? split[1] : split[0]);
//                        GWT.log("item name " + item.getItemName() +" expenxe id " +item.getExpanceItemId());
                    item.setItemCategory((product.getSelectedItemID() != null && product.getSelectedData() != null) ? ((SelectItem) product.getSelectedData()).getDescription() : null);
                    item.setAssignedSerials(qtyPanel.getBatchSerials());
                    item.setQuoteItemId(product.getConvertedItemId());
                    item.setSaleInvoiceId(product.getSaleInvoiceId());
                    item.setItemOriginalPrice(productSelectItem.getOriginalPrice());
                    item.setProductBrand(productSelectItem.getBrandName());
                    item.setID(product.getItemID());
                    item.setSerials(qtyPanel.getSerials());
                    item.setBatchItems(qtyPanel.getTtrackBatches());

                    if (product.getItemID() != null) {
                        itemCFsValues = setHideValues(item, product.getItemID(), i);
                    }
                } else {
                    item.setItemName(product.getSuggestBox().getText());
                }
                if (product instanceof SmartProductLookUp && ((SmartProductLookUp) product).getUsedInGrn() != null) {
                    item.setReceiveType(((SmartProductLookUp) product).getUsedInGrn().getReceiveType());
                    if (item.getReceiveType().equals(ReceiveTypeEnum.RECEIVE_BY_QTY)) {
                        item.setReceivedQty(((SmartProductLookUp) product).getUsedInGrn().getAmount());
                    } else {
                        item.setReceivedAmount(((SmartProductLookUp) product).getUsedInGrn().getAmount());
                    }
                }
            } else {
                final CustomCellTextArea area = (CustomCellTextArea) widget;
                item.setItemName(area.getText());
                item.setProjectBasedInvoiceDesc(area.getText());
                item.setProjectBasedEntryIds(area.getEntryIds());
                item.setMeasurement(new SelectItem(null, "hours"));
            }

            if (!this.getOverallDiscount().isEnabled()) {
                if (pnlDiscount != null) {
                    discount = pnlDiscount.getValue();
                    if (pnlDiscount.getDiscountUnit().equals(ProductsTable.DEFAULT_DISCOUNT_TYPE_UNIT)) {
                        item.setDiscountPercent(discount);
                        item.setDiscountAmount(null);
                    } else {
                        item.setDiscountAmount(discount);
                        item.setDiscountPercent(null);
                    }
                }

                //set selected item discount
                if (discountList != null && discountList.getSelectedItem() != null) {
                    if (Constants.ONE_OFF_FIXED_AMOUNT.equals(discountList.getSelectedItem().getId()) && Constants.ONE_OFF_FIXED_AMOUNT_STR.equals(discountList.getSelectedItem().getName())) {
                        item.setDiscountItemStaticType(Constants.ONE_OFF_FIXED_AMOUNT);
                    } else if (Constants.ONE_OFF_DISCOUNT.equals(discountList.getSelectedItem().getId()) && Constants.ONE_OFF_DISCOUNT_STR.equals(discountList.getSelectedItem().getName())) {
                        item.setDiscountItemStaticType(Constants.ONE_OFF_DISCOUNT);
                    } else {
                        item.setItemDiscountID(discountList.getSelectedId());
                    }
                }

                //Double discount
                final BigDecimal discount2;
                if (pnlDiscount2 != null) {
                    discount2 = pnlDiscount2.getValue();
                    if (pnlDiscount2.getDiscountUnit().equals(ProductsTable.DEFAULT_DISCOUNT_TYPE_UNIT)) {
                        item.setDoubleDiscountPercent(discount2);
                        item.setDoubleDiscountAmount(null);
                    } else {
                        item.setDoubleDiscountAmount(discount2);
                        item.setDoubleDiscountPercent(null);
                    }
                }
                //set selected item double discount
                if (discountList2 != null) {
                    item.setItemDoubleDiscountID(discountList2.getSelectedId());
                }
            } else {
                discount = pnlDiscount != null ? pnlDiscount.getValue() : BigDecimal.ZERO;

                if (Constants.ONE_OFF_DISCOUNT.equals(this.getOverallDiscount().getType())) {
                    item.setDiscountPercent(discount);
                    item.setDiscountAmount(null);
                } else if (Constants.ONE_OFF_FIXED_AMOUNT.equals(this.getOverallDiscount().getType())) {
                    item.setDiscountAmount(discount);
                    item.setDiscountPercent(null);
                }
                item.setDiscountItemStaticType(this.getOverallDiscount().getType());
            }
            if (txtComission != null) {
                item.setComission(AccountingUtils.get().parseToBigDecimal(txtComission.getText()));
            }
            if (departmentLookUp != null) {
                item.setDepartmentItem(departmentLookUp.getSelectedItem());
            }

            if (account != null) {
                item.setAccountID(account.getSelectedItemID());
            } else if (defaultAccount != null) {
                item.setAccountID(defaultAccount.getId());
            }
            item.setDescription(description != null ? description.getText() : null);
            item.setQuantity(qtyPanel.getQty());
            item.setFromTimesheet(qtyPanel.isFromTimesheet());

            if (mu != null) {
                item.setMeasurement((SelectItem) mu.getSelectData());
            }
            item.setUnitPrice(AccountingUtils.get().parseToBigDecimal(unitPrice.getText()));
            item.setPriceLevelAmount(this.priceLevelMap.get(i));
            if (discountedNet != null) {
                item.setNet(discountedNet.divide(this.exchangeRateValue, AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP));//sending net with discount to the server in base currency.
            }
            if (faiCategory != null) {
                item.setFaiCategoryId(faiCategory.getSelectedItemID());
            }

            if (taxLookUp != null) {
                item.setTaxItem(taxLookUp.getSelectedData());
                item.setTaxAmount(taxLookUp.getItemTaxAmount());
            }
            if (doubleTaxLookUp != null) {
                item.setDoubleTaxItem(doubleTaxLookUp.getSelectedData());
                item.setDoubleTaxAmount(doubleTaxLookUp.getItemTaxAmount());
            }

            item.setTotalAmount(unitPrice.getTotalAmount());

            if (warehouseLookUp != null) {
                item.setWarehouse(new SelectItem(warehouseLookUp.getSelectedItemID()));
            }

            if (projectLookUp != null) {
                item.setProject(projectLookUp.getSelectedItem());
            }
            if (clientLookUp != null) {
                item.setClient(clientLookUp.getSelectedItem());
            }
            if (fromDate != null && fromDate.getDate() != null) {
                item.setFromDate(new DateNonConvertable(DateUtil.resetTime(fromDate.getDate())));
            }
            if (toDate != null && toDate.getDate() != null) {
                item.setToDate(new DateNonConvertable(DateUtil.resetTime(toDate.getDate())));
            }

            if (this.itemsTable.getColumnById(i, ProductsTable.RECEIVED_QTY) != null) {
                final TextBox receive = (TextBox) this.itemsTable.getColumnById(i, ProductsTable.RECEIVED_QTY);
                item.setReceivedQty(ProductsTableUtils.getQuantity(receive.getText()));
            }

            if (uploadForm != null && uploadForm.getAttachedFiles() != null) {
                uploadForm.getAttachedFiles();
                for (final FileResource resource : uploadForm.getAttachedFiles()) {
                    final FileItem attachment = new FileItem();
                    attachment.setId(resource.getObjectId());
                    attachment.setFileName(resource.getName());
                    attachment.setDescription(resource.getDescription());
                    attachment.setDate(new Date());
                    attachment.setSize(resource.getContentLength());
                    attachment.setUploadType(resource.getUploadType());
                    attachment.setAmazonLink(resource.getAmazonLink());
                    attachment.setGoogleDocumentLink(resource.getGoogleDownloadLink());
                    attachment.setOfficeDocumentLink(resource.getOfficeDownloadLink());
                    item.getAttachments().add(attachment);
                }
            }

            //initialize custom fields data
            if (this.customFieldsMap != null && !this.customFieldsMap.isEmpty()) {
                final ArrayList<CompanyCustomFieldItem> fieldItems = new ArrayList<>();

                for (final String key : this.customFieldsMap.keySet()) {
                    final CustomFieldInterface customField = (CustomFieldInterface) this.itemsTable.getColumnById(i, key);

                    if (customField != null) {
                        fieldItems.add(customField.getFieldItem());
                    } else if (itemCFsValues != null && !itemCFsValues.isEmpty() && itemCFsValues.get(key + "__" + i) != null) {
                        fieldItems.add(itemCFsValues.get(key + "_###_" + i));
                    }
                }

                if (!fieldItems.isEmpty()) {
                    item.setCustomFieldItems(fieldItems);
                }
            }

            //when saving object with DRAFT status
            if (Constants.DRAFT.equals(status)) {

                if (!Utils.isNullOrEmpty(item.getItemName()) && !LookUp.wfmStrings.searchTypeMessage().equals(item.getItemName().trim())
                        || item.getItemID() != null) {
                    items[++j] = item;
                }
            } else {
                items[++j] = item;
            }
        }
        return items;
    }

    public QuantityItem[] getQuantityItemsForValidate() {
        List<QuantityItem> quantityItems = Stream.of(getDataItems())
                .filter(invItem -> invItem.getItemID() != null && invItem.getQuantity().compareTo(BigDecimal.ZERO) > 0)
                .map(invItem -> {
                    QuantityItem qtyItem = new QuantityItem();
                    qtyItem.setId(invItem.getItemID());
                    qtyItem.setQuantity(invItem.getQuantity());
                    qtyItem.setWarehouseID(invItem.getWarehouse() != null ? invItem.getWarehouse().getId() : null);
                    return qtyItem;
                }).collect(Collectors.toList());
        return quantityItems.toArray(new QuantityItem[]{});
    }

    private Map<String, CompanyCustomFieldItem> setHideValues(NewInvoiceItem result, Integer lineItemId, Integer rowId) {
        Map<String, CompanyCustomFieldItem> cFsValues = new LinkedHashMap<>();
        if (invoiceItems != null) {
            for (NewInvoiceItem invoiceItem : invoiceItems) {
                if (lineItemId.equals(invoiceItem.getID())) {
                    if (invoiceItem.getDescription() != null) {
                        result.setDescription(invoiceItem.getDescription());
                    }
                    if (invoiceItem.getComission() != null) {
                        result.setComission(invoiceItem.getComission());
                    }
                    if (invoiceItem.getDepartmentItem() != null) {
                        result.setDepartmentItem(invoiceItem.getDepartmentItem());
                    }
                    if (invoiceItem.getAccountID() != null) {
                        result.setAccountID(invoiceItem.getAccountID());
                    }
                    if (invoiceItem.getQuantity() != null) {
                        result.setQuantity(invoiceItem.getQuantity());
                    }
                    if (invoiceItem.getMeasurement() != null) {
                        result.setMeasurement(invoiceItem.getMeasurement());
                    }
                    if (invoiceItem.getUnitPrice() != null) {
                        result.setUnitPrice(invoiceItem.getUnitPrice());
                    }
                    if (invoiceItem.getPriceLevelAmount() != null) {
                        result.setPriceLevelAmount(invoiceItem.getPriceLevelAmount());
                    }
                    if (invoiceItem.getNet() != null) {
                        result.setNet(invoiceItem.getNet());
                    }
                    if (invoiceItem.getDoubleTaxItem() != null) {
                        result.setDoubleTaxItem(invoiceItem.getDoubleTaxItem());
                    }
                    if (invoiceItem.getDoubleTaxAmount() != null) {
                        result.setDoubleTaxAmount(invoiceItem.getDoubleTaxAmount());
                    }
                    if (invoiceItem.getTotalAmount() != null) {
                        result.setTotalAmount(invoiceItem.getTotalAmount());
                    }
                    if (invoiceItem.getWarehouse() != null) {
                        result.setWarehouse(invoiceItem.getWarehouse());
                    }
                    if (invoiceItem.getProject() != null) {
                        result.setProject(invoiceItem.getProject());
                    }
                    if (invoiceItem.getClient() != null) {
                        result.setClient(invoiceItem.getClient());
                    }
                    if (invoiceItem.getDiscountPercent() != null) {
                        result.setDiscountPercent(invoiceItem.getDiscountPercent());
                    }
                    if (invoiceItem.getDiscountAmount() != null) {
                        result.setDiscountAmount(invoiceItem.getDiscountAmount());
                    }
                    if (invoiceItem.getSupplierID() != null) {
                        result.setSupplierID(invoiceItem.getSupplierID());
                    }
                    if (invoiceItem.getUuid() != null) {
                        result.setUuid(invoiceItem.getUuid());
                    }


                    if (customFieldsMap != null && invoiceItem.getCustomFieldItems() != null) {
                        for (String columnCode : customFieldsMap.keySet()) {
                            if (invoiceItem.getCustomFieldByCode(columnCode) != null) {
                                cFsValues.put(columnCode + "_###_" + rowId, invoiceItem.getCustomFieldByCode(columnCode));
                            }
                        }
                    }

                    break;
                }
            }
        }
        return cFsValues;
    }

    public QuantityItem[] getQuantityItemsForValidateStockInconsistency() {
        Map<String, QuantityItem> oldItemsMap = mapInvoiceItems(invoiceItems);
        Map<String, QuantityItem> adjustedItemsMap = mapInvoiceItems(getDataItems());

        List<QuantityItem> list = new ArrayList<>();
        oldItemsMap.keySet().forEach(key -> {
            if (adjustedItemsMap.get(key) == null) {
                QuantityItem qitem = oldItemsMap.get(key);
                qitem.setQuantity(BigDecimal.ZERO.subtract(qitem.getQuantity()));
                list.add(qitem);
            } else if (oldItemsMap.get(key).getQuantity().compareTo(adjustedItemsMap.get(key).getQuantity()) > 0) {
                list.add(adjustedItemsMap.get(key));
            }
        });
        return list.toArray(new QuantityItem[]{});
    }

    public void markAsError(SelectItem[] items) {
        for (SelectItem item : items) {
            final Widget qtyWidget = grid.getWidget(item.getRowId(), this.itemsTable.getColumnId(ProductsTable.QTY));
            qtyWidget.setStyleName("x-form-invalid");
            ((AbstractCell) qtyWidget).displayActive(true);
        }
    }

    public boolean validation() {
        return this.validation(null, false);
    }

    public boolean validation(final String status) {
        return this.validation(status, false);
    }

    public boolean validation(final String status, final boolean ignoreAccountValidation) {
        //TODO need to review this are and think well to optimize it
        int errors = 0;
        this.itemsTable.setValidRows(0);
        final boolean testDepartment = AccountingUtils.get().isEnableAccountingDepartmentRelation() && !Utils.hasPermission(PermissionConstants.SKIP_DEPARTMENT_ITEM_VALIDATION);
        boolean descriptionRequired = false;
        boolean unitMeasurementRequired = false;
        boolean txtCommissionRequired = false;
        boolean discountListRequired = false;
        boolean pnlDiscountRequired = false;
        boolean discountList2Required = false;
        boolean pnlDiscount2Required = false;
        boolean faiCategoryInvalid = false;
        final Set<CompanyCustomFieldItem> requiredCFs = new HashSet<>();
        final Set<String> invalidCFs = new HashSet<>();
        for (final String columnCode : this.columnsMap.keySet()) {
            if (this.customFieldsMap != null && this.customFieldsMap.containsKey(columnCode) && (this.customFieldsMap.get(columnCode).isRequired() || Constants.UI_TYPE_TEXTBOX_EMAIL.equals(this.customFieldsMap.get(columnCode).getUiType()) || Constants.UI_TYPE_URL.equals(this.customFieldsMap.get(columnCode).getUiType()) || Constants.UI_TYPE_PERCENTAGE.equals(this.customFieldsMap.get(columnCode).getUiType()))) {
                requiredCFs.add(this.customFieldsMap.get(columnCode));
            }
        }
        for (int rowID = 0; rowID < this.grid.getRowCount(); rowID++) {
            this.itemsTable.resetValidation(rowID);
            boolean isAccountSelected = true;
            boolean hasSelectedItem = true, isZeroQty = false, isZeroUnitPrice = false;

            final Widget item = this.itemsTable.getColumnById(rowID, ProductsTable.PRODUCT);
            final ProductDescriptionTextArea description = (ProductDescriptionTextArea) this.itemsTable.getColumnById(rowID, ProductsTable.DESCRIPTION);
            final ItemQtyPanel qtyPanel = (ItemQtyPanel) this.itemsTable.getColumnById(rowID, ProductsTable.QTY);
            final MeasurementsLookUp mu = (MeasurementsLookUp) this.itemsTable.getColumnById(rowID, ProductsTable.MEASUREMENT);
            final UnitPrice unitPrice = (UnitPrice) this.itemsTable.getColumnById(rowID, ProductsTable.UNITPRICE);
            final TextBox txtComission = (TextBox) this.itemsTable.getColumnById(rowID, ProductsTable.COMISSION);
            final DataListBox discountList = (DataListBox) this.itemsTable.getColumnById(rowID, ProductsTable.DISCOUNT_LIST);
            final Discount pnlDiscount = (Discount) this.itemsTable.getColumnById(rowID, ProductsTable.DISCOUNT_AMT);
            final DataListBox discountList2 = (DataListBox) this.itemsTable.getColumnById(rowID, ProductsTable.DOUBLE_DISCOUNT_LIST);
            final Discount pnlDiscount2 = (Discount) this.itemsTable.getColumnById(rowID, ProductsTable.DOUBLE_DISCOUNT_AMT);
            final DepartmentLookUp departmentLookUp = (DepartmentLookUp) this.itemsTable.getColumnById(rowID, ProductsTable.DEPARTMENT);
            final AccountsLookUp account = (AccountsLookUp) this.itemsTable.getColumnById(rowID, ProductsTable.ACCOUNT);
            final TaxLookUp taxLookUp = (TaxLookUp) this.itemsTable.getColumnById(rowID, ProductsTable.TAX_LIST);
            final TaxLookUp doubleTaxLookUp = (TaxLookUp) this.itemsTable.getColumnById(rowID, ProductsTable.DOUBLE_TAX_LIST);
            final WarehouseLookUp warehouseLookUp = (WarehouseLookUp) this.itemsTable.getColumnById(rowID, ProductsTable.WAREHOUSE);
            final ProjectLookUp projectLookUp = (ProjectLookUp) this.itemsTable.getColumnById(rowID, ProductsTable.PROJECT);
            final CrmAccountLookUp clientLookUp = (CrmAccountLookUp) this.itemsTable.getColumnById(rowID, ProductsTable.CLIENT);
            LookUp faiCategory = (LookUp) this.itemsTable.getColumnById(rowID, ProductsTable.FAI_CATEGORY);

            if (item instanceof ProductLookUp) {

                if (LookUp.wfmStrings.searchTypeMessage().equals(((ProductLookUp) item).getTextBox().getText().trim())) {
                    this.itemsTable.setColumnValid(ProductsTable.PRODUCT);
                    errors++;
                    hasSelectedItem = false;
                }
            } else if (item instanceof CustomCellTextArea) {

                if ("".equals(((CustomCellTextArea) item).getText())) {
                    this.itemsTable.setColumnValid(ProductsTable.PRODUCT);
                    errors++;
                    hasSelectedItem = false;
                }
            }

            if (this.columnsMap.get(ProductsTable.DESCRIPTION) != null && this.columnsMap.get(ProductsTable.DESCRIPTION).isRequired() && description.getTextArea() != null && !Validation.validateTextAreaRequired(description.getTextArea())) {
                descriptionRequired = true;
                this.itemsTable.setColumnValid(ProductsTable.DESCRIPTION);
                errors++;

            }
            if (this.columnsMap.get(ProductsTable.MEASUREMENT) != null && this.columnsMap.get(ProductsTable.MEASUREMENT).isRequired() && !Validation.validateLookUpRequired(mu)) {
                unitMeasurementRequired = true;
                this.itemsTable.setColumnValid(ProductsTable.MEASUREMENT);
                errors++;
            }
            if (this.columnsMap.get(ProductsTable.FAI_CATEGORY) != null && this.columnsMap.get(ProductsTable.FAI_CATEGORY).isRequired() &&  faiCategory!=null && !Validation.validateLookUpRequired(faiCategory)) {
                faiCategoryInvalid = true;
                this.itemsTable.setColumnValid(ProductsTable.FAI_CATEGORY);
                errors++;
            }
            if (this.columnsMap.get(ProductsTable.COMISSION) != null && this.columnsMap.get(ProductsTable.COMISSION).isRequired() && !Validation.validateTextBoxRequired(txtComission)) {
                txtCommissionRequired = true;
                this.itemsTable.setColumnValid(ProductsTable.COMISSION);
                errors++;
            }
            if (this.columnsMap.get(ProductsTable.DISCOUNT_LIST) != null && this.columnsMap.get(ProductsTable.DISCOUNT_LIST).isRequired() && !Validation.validateDataListBoxRequired(discountList)) {
                discountListRequired = true;
                this.itemsTable.setColumnValid(ProductsTable.DISCOUNT_LIST);
                errors++;
            }
            if (this.columnsMap.get(ProductsTable.DOUBLE_DISCOUNT_LIST) != null && this.columnsMap.get(ProductsTable.DOUBLE_DISCOUNT_LIST).isRequired() && !Validation.validateDataListBoxRequired(discountList2)) {
                discountList2Required = true;
                this.itemsTable.setColumnValid(ProductsTable.DOUBLE_DISCOUNT_LIST);
                errors++;
            }
            if (this.columnsMap.get(ProductsTable.DISCOUNT_AMT) != null && this.columnsMap.get(ProductsTable.DISCOUNT_AMT).isRequired() && pnlDiscount.getTxtDiscount() != null && !Validation.validateTextBoxRequired(pnlDiscount.getTxtDiscount())) {
                pnlDiscountRequired = true;
                this.itemsTable.setColumnValid(ProductsTable.DISCOUNT_AMT);
                errors++;
            }
            if (this.columnsMap.get(ProductsTable.DOUBLE_DISCOUNT_AMT) != null && this.columnsMap.get(ProductsTable.DOUBLE_DISCOUNT_AMT).isRequired() && pnlDiscount2.getTxtDiscount() != null && !Validation.validateTextBoxRequired(pnlDiscount2.getTxtDiscount())) {
                pnlDiscount2Required = true;
                this.itemsTable.setColumnValid(ProductsTable.DOUBLE_DISCOUNT_AMT);
                errors++;
            }
            if (this.columnsMap.get(ProductsTable.DOUBLE_TAX_LIST) != null && this.columnsMap.get(ProductsTable.DOUBLE_TAX_LIST).isRequired() && !Validation.validateLookUpRequired(doubleTaxLookUp)) {
                this.itemsTable.setColumnValid(ProductsTable.DOUBLE_TAX_LIST);
                errors++;
            }
            if (this.columnsMap.get(ProductsTable.PROJECT) != null && this.columnsMap.get(ProductsTable.PROJECT).isRequired() && !Validation.validateLookUpRequired(projectLookUp)) {
                this.itemsTable.setColumnValid(ProductsTable.PROJECT);
                errors++;
            }
            if (this.columnsMap.get(ProductsTable.CLIENT) != null && this.columnsMap.get(ProductsTable.CLIENT).isRequired() && !Validation.validateLookUpRequired(clientLookUp)) {
                this.itemsTable.setColumnValid(ProductsTable.CLIENT);
                errors++;
            }

            if (!qtyPanel.validate(status)) {
                this.itemsTable.setColumnValid(ProductsTable.QTY);
                isZeroQty = true;
                errors++;
                qtyPanel.addStyleName(ERROR_FORM_STYLE);
            } else if (columnsMap.get(ItemTableConstants.QTY) != null && columnsMap.get(ItemTableConstants.QTY).getMinValue() != null && qtyPanel.getQty().compareTo(columnsMap.get(ItemTableConstants.QTY).getMinValue()) < 0) {
                qtyPanel.addStyleName(ERROR_FORM_STYLE);
                errors++;
                this.itemsTable.setColumnValid(ProductsTable.QTY);
                Info.warn(ProductsTable.accountingMessages.minValue(String.valueOf(columnsMap.get(ItemTableConstants.QTY).getMinValue())), 5000);
            } else {
                qtyPanel.removeStyleName(ERROR_FORM_STYLE);
            }

            if (!this.validateCalculatable(unitPrice.getText(), false)) {
                this.itemsTable.setColumnValid(ProductsTable.UNITPRICE);
                errors++;
            } else if (AccountingUtils.get().parseToBigDecimal(unitPrice.getText()).compareTo(BigDecimal.ZERO) == 0) {
                isZeroUnitPrice = true;
            }
            if (Utils.hasGenericAccess(GenericSettingsEnum.CHECK_NET_AND_TOTAL_WHEN_SAVE)) {
                if (!checkNetAndSubTotalcalculate(rowID)) {
                    errors++;
                }
            }
            if (!ignoreAccountValidation && account != null) {
                if (!(account.getSelectedItemID() != null && account.getSelectedItemID() > 0)) {
                    this.itemsTable.setColumnValid(ProductsTable.ACCOUNT);
                    errors++;
                    isAccountSelected = false;
                }
            }

            if (Constants.VAT_COUNTRIES.contains(Utils.getCompanyrCountryCode()) && Utils.isVatRegistered() || (this.columnsMap.get(ProductsTable.TAX_LIST) != null && this.columnsMap.get(ProductsTable.TAX_LIST).isRequired())) {

                if (taxLookUp != null && taxLookUp.getSuggestBox().getTextBox().isEnabled() && !(taxLookUp.getSelectedItemID() != null && taxLookUp.getSelectedItemID() > 0)) {
                    this.itemsTable.setColumnValid(ProductsTable.TAX_LIST);
                    errors++;
                }
            }

            if (Utils.isMultiWarehouseEnabled() && warehouseLookUp != null) {
                final Widget productWidget = this.itemsTable.getColumnById(rowID, ProductsTable.PRODUCT);
                if (productWidget instanceof ProductLookUp) {
                    final ProductLookUp productLookUp = (ProductLookUp) productWidget;
                    if (productLookUp.getSelectedData() != null && productLookUp.getSelectedData() instanceof ProductSelectItem) {
                        final ProductSelectItem productSelectItem = (ProductSelectItem) productLookUp.getSelectedData();
                        if ((AccountingConstants.INVENTORY_ITEM.equals(productSelectItem.getProductType()) || AccountingConstants.ASSEMBLY_ITEM.equals(productSelectItem.getProductType()) || AccountingConstants.RENTAL_ITEM.equals(productSelectItem.getProductType()) || productSelectItem.isHasInventoryInProductKit())
                                && !(warehouseLookUp.getSelectedItemID() != null && warehouseLookUp.getSelectedItemID() > 0)) {
                            if (!Constants.DRAFT.equals(status)) {
                                this.itemsTable.setColumnValid(ProductsTable.WAREHOUSE);
                                errors++;
                            }
                        }
                    } else if (!this.itemsTable.isValid(ProductsTable.PRODUCT)) {
                        this.itemsTable.setColumnValid(ProductsTable.WAREHOUSE);
                        errors++;
                    }
                }
            }

            if (AccountingUtils.get().isEnableAccountingDepartmentRelation() && columnsMap.get(DEPARTMENT) != null && columnsMap.get(DEPARTMENT).isRequired()) {
                if (departmentLookUp != null && (departmentLookUp.getSelectedItem() == null || departmentLookUp.getSelectedItemID() == null)) {
                    this.itemsTable.setColumnValid(ProductsTable.DEPARTMENT);
                    errors++;
                }
            }
            for (final CompanyCustomFieldItem cfItem : requiredCFs) {
                if (Constants.UI_TYPE_TEXTBOX.equals(cfItem.getUiType())) {
                    final TextBox t = (TextBox) this.itemsTable.getColumnById(rowID, cfItem.getColumnCode());
                    if (!Validation.validateTextBoxRequired(t)) {
                        this.itemsTable.setColumnValid(cfItem.getColumnCode());
                        invalidCFs.add(cfItem.getColumnCode());
                        errors++;
                    }
                } else if (Constants.UI_TYPE_TEXTBOX_EMAIL.equals(cfItem.getUiType())) {
                    final TextBox t = (TextBox) this.itemsTable.getColumnById(rowID, cfItem.getColumnCode());
                    if (cfItem.isRequired() || (!cfItem.isRequired() && !t.getText().isEmpty())) {
                        if (!Validation.validateEmailRequired(t)) {
                            this.itemsTable.setColumnValid(cfItem.getColumnCode());
                            invalidCFs.add(cfItem.getColumnCode());
                            errors++;
                        }
                    }
                } else if (Constants.UI_TYPE_URL.equals(cfItem.getUiType())) {
                    final TextBox t = (TextBox) this.itemsTable.getColumnById(rowID, cfItem.getColumnCode());
                    if (cfItem.isRequired() || (!cfItem.isRequired() && !t.getText().isEmpty())) {
                        if (!Validation.validateUrl(t, null)) {
                            this.itemsTable.setColumnValid(cfItem.getColumnCode());
                            invalidCFs.add(cfItem.getColumnCode());
                            errors++;
                        }
                    }
                } else if (Constants.UI_TYPE_PERCENTAGE.equals(cfItem.getUiType())) {
                    final TextBox t = (TextBox) this.itemsTable.getColumnById(rowID, cfItem.getColumnCode());
                    if (cfItem.isRequired()) {
                        if (!Validation.validateIntegerTextBoxRequired(t)) {
                            this.itemsTable.setColumnValid(cfItem.getColumnCode());
                            invalidCFs.add(cfItem.getColumnCode());
                            errors++;
                        }/* else {
                            if (t.getText() != null && t.getText().trim().length() > 0 && Double.valueOf(t.getText()).compareTo((double) 100) > 0) {
                                itemsTable.setColumnValid(cfItem.getColumnCode());
                                invalidCFs.add(cfItem.getColumnCode());
                                errors++;
                            }
                        }*/
                    } else {
                        if (t.getText() != null && !t.getText().isEmpty() && Double.valueOf(t.getText()).compareTo((double) 100) > 0) {
                            this.itemsTable.setColumnValid(cfItem.getColumnCode());
                            invalidCFs.add(cfItem.getColumnCode());
                            errors++;
                        }
                    }
                } else if (Constants.UI_TYPE_DROPDOWN.equals(cfItem.getUiType())) {
                    final DataListBox t = (DataListBox) this.itemsTable.getColumnById(rowID, cfItem.getColumnCode());
                    if (t.getSelectedItem() == null) {
                        this.itemsTable.setColumnValid(cfItem.getColumnCode());
                        invalidCFs.add(cfItem.getColumnCode());
                        errors++;
                    }
                } else if (Constants.UI_TYPE_DATEPICKER.equals(cfItem.getUiType())) {
                    final DatePicker t = (DatePicker) this.itemsTable.getColumnById(rowID, cfItem.getColumnCode());
                    if (!Validation.validateDate(t)) {
                        this.itemsTable.setColumnValid(cfItem.getColumnCode());
                        invalidCFs.add(cfItem.getColumnCode());
                        errors++;
                    }
                } else if (Constants.UI_TYPE_DATEPICKER_TIME.equals(cfItem.getUiType())) {
                    final DateTimeWidget t = (DateTimeWidget) this.itemsTable.getColumnById(rowID, cfItem.getColumnCode());
                    if (!Validation.validateDateTime(t)) {
                        this.itemsTable.setColumnValid(cfItem.getColumnCode());
                        invalidCFs.add(cfItem.getColumnCode());
                        errors++;
                    }
                } else if (Constants.UI_TYPE_TEXTAREA.equals(cfItem.getUiType())) {
                    final CustomTextAreaField t = (CustomTextAreaField) this.itemsTable.getColumnById(rowID, cfItem.getColumnCode());
                    if (t != null && (t.getText() == null || t.getText().isEmpty())) {
                        this.itemsTable.setColumnValid(cfItem.getColumnCode());
                        invalidCFs.add(cfItem.getColumnCode());
                        errors++;
                    }
                } else if (Constants.UI_TYPE_LOOKUP.equals(cfItem.getUiType())) {
                    final CustomFieldLookUpField t = (CustomFieldLookUpField) this.itemsTable.getColumnById(rowID, cfItem.getColumnCode());
                    if (t != null && !Validation.validateLookUpRequired(t)) {
                        this.itemsTable.setColumnValid(cfItem.getColumnCode());
                        invalidCFs.add(cfItem.getColumnCode());
                        errors++;
                    }
                } else if (Constants.UI_TYPE_MULTI_LOOKUP.equals(cfItem.getUiType())) {
                    final CustomFieldMultiLookUpField t = (CustomFieldMultiLookUpField) this.itemsTable.getColumnById(rowID, cfItem.getColumnCode());
                    if (t.getSelectedItems() == null || (t.getSelectedItems() != null && t.getSelectedItems().isEmpty())) {
                        t.addStyleName(ERROR_FORM_STYLE);
                        Utils.scrollIntoView(t.getElement());
                        this.itemsTable.setColumnValid(cfItem.getColumnCode());
                        invalidCFs.add(cfItem.getColumnCode());
                        errors++;
                    }
                }
            }
            if (errors > 0) {

                if (errors == this.itemsTable.getRequiredFieldCount()) {
                    this.itemsTable.setItemValid(rowID, false);
                    errors = 0;
                } else if (errors == this.itemsTable.getRequiredFieldCount() - 2 && isAccountSelected && isZeroUnitPrice) {
                    this.itemsTable.setItemValid(rowID, false);
                    errors = 0;
                } else if (errors == this.itemsTable.getRequiredFieldCount() - 1 && (isAccountSelected || isZeroUnitPrice)) {
                    this.itemsTable.setItemValid(rowID, false);
                    errors = 0;
                } else if (!hasSelectedItem && isZeroQty) {
                    this.itemsTable.setItemValid(rowID, false);
                    errors = 0;
                } else if (errors == this.itemsTable.getRequiredFieldCount() - 2 && (isAccountSelected || isZeroUnitPrice)
                        && taxLookUp != null && !taxLookUp.getSuggestBox().getTextBox().isEnabled()) {
                    this.itemsTable.setItemValid(rowID, false);
                    errors = 0;
                } else if (this.itemsTable.validateFields(rowID)) {
                    this.itemsTable.setItemValid(rowID, true);
                    this.itemsTable.incValidRow();
                    errors = 0;
                } else {
                    this.itemsTable.setItemValid(rowID, false);
                    return false;
                }
            } else {
                this.itemsTable.setItemValid(rowID, true);
                this.itemsTable.incValidRow();
            }
        }
        if (this.itemsTable.getValidRows() == 0) {
            this.itemsTable.notValid(0, ProductsTable.PRODUCT);
            if (descriptionRequired) {
                this.itemsTable.notValid(0, ProductsTable.DESCRIPTION);
            }
            if (unitMeasurementRequired) {
                this.itemsTable.notValid(0, ProductsTable.MEASUREMENT);
            }
            if (faiCategoryInvalid) {
                this.itemsTable.notValid(0, ProductsTable.FAI_CATEGORY);
            }
            if (txtCommissionRequired) {
                this.itemsTable.notValid(0, ProductsTable.COMISSION);
            }
            if (discountListRequired) {
                this.itemsTable.notValid(0, ProductsTable.DISCOUNT_LIST);
            }
            if (pnlDiscountRequired) {
                this.itemsTable.notValid(0, ProductsTable.DISCOUNT_AMT);
            }
            if (pnlDiscount2Required) {
                this.itemsTable.notValid(0, ProductsTable.DOUBLE_DISCOUNT_AMT);
            }
            if (discountList2Required) {
                this.itemsTable.notValid(0, ProductsTable.DOUBLE_DISCOUNT_LIST);
            }
            this.itemsTable.notValid(0, ProductsTable.QTY);
            this.itemsTable.notValid(0, ProductsTable.UNITPRICE);
            this.itemsTable.notValid(0, ProductsTable.WAREHOUSE);
            if (testDepartment) {
                this.itemsTable.notValid(0, ProductsTable.DEPARTMENT);
            }
            for (final String code : invalidCFs) {
                this.itemsTable.notValid(0, code);
            }
            return false;
        }
        if (customFieldsMap != null && !customFieldsMap.isEmpty()) {
            return Validation.itemTableNumericCFMinValueValidate(itemsTable, customFieldsMap.values());
        } else {
            return true;
        }
    }

    public boolean checkNetAndSubTotalcalculate(int i) {
        boolean isValidNetAndSubTotal = true;
        Map<Integer, BigDecimal> taxTotal = new HashMap<>();
        Map<Integer, BigDecimal> fixedAmountMap = calculateOverallFixedAmountPerItem();
        this.exchangeRateValue = Optional.ofNullable(this.exchangeRateValue).orElse(BigDecimal.ONE);

        BigDecimal quantity = BigDecimal.ZERO;
        if (AccountingConstants.PROGRESS_INVOICING.equals(this.formParameters.getExternalFormID()) && !Utils.hasGenericAccess(GenericSettingsEnum.EDIT_SALE_INVOICE_CONVERTED_FROM_SQ_SO)) {
            if (invoiceItems != null) {
                quantity = invoiceItems[i] != null ? invoiceItems[i].getQtyWithHighScale() : BigDecimal.ZERO;
            }
        } else if (this.itemsTable.getColumnById(i, ProductsTable.QTY) != null) {
            quantity = ((ItemQtyPanel) this.itemsTable.getColumnById(i, ProductsTable.QTY)).getQty();
        }

        final Discount pnlDiscount = (Discount) this.itemsTable.getColumnById(i, ProductsTable.DISCOUNT_AMT);
        final DataListBox pnlDiscountList = (DataListBox) this.itemsTable.getColumnById(i, ProductsTable.DISCOUNT_LIST);
        BigDecimal discount = BigDecimal.ZERO;
        String discountUnit = ProductsTable.DEFAULT_DISCOUNT_TYPE_UNIT;
        //Double discount
        final Discount pnlDiscount2 = (Discount) this.itemsTable.getColumnById(i, ProductsTable.DOUBLE_DISCOUNT_AMT);
        BigDecimal discount2 = BigDecimal.ZERO;
        String discountUnit2 = ProductsTable.DEFAULT_DISCOUNT_TYPE_UNIT;

        BigDecimal unitPrice = BigDecimal.ZERO;
        final UnitPrice unitPriceTextBox = (UnitPrice) this.itemsTable.getColumnById(i, ProductsTable.UNITPRICE);
        if (unitPriceTextBox != null) {
            if (unitPriceTextBox.isIgnoreMultiPrice() && (this.exchangeRateValue.compareTo(unitPriceTextBox.getExchangeRate()) != 0 && !this.fromMultiQuoteConvert)) {
                unitPrice = unitPriceTextBox.getValueInBaseCurrency().multiply(this.exchangeRateValue).setScale(AccountingUtils.customUnitPriceScale, RoundingMode.HALF_UP);
            } else {
                unitPrice = unitPriceTextBox.getDoubleValue();
            }
        }

        if (pnlDiscount != null) {
            discount = pnlDiscount.getValue();

            if (pnlDiscount.getDiscountUnit() != null && !pnlDiscount.getDiscountUnit().equals(ProductsTable.DEFAULT_DISCOUNT_TYPE_UNIT)) {
                discountUnit = pnlDiscount.getDiscountUnit();
            }
        } else if (pnlDiscountList != null && pnlDiscountList.getSelectedItem() != null) {
            final DiscountItem discountItem = (DiscountItem) pnlDiscountList.getSelectedItem();
            if (discountItem.getPercentage() != null && discountItem.getPercentage().compareTo(BigDecimal.ZERO) != 0) {
                discountUnit = ProductsTable.DEFAULT_DISCOUNT_TYPE_UNIT;
                discount = discountItem.getPercentage();
            } else if (discountItem.getFixedAmount() != null && discountItem.getFixedAmount().compareTo(BigDecimal.ZERO) != 0) {
                discountUnit = this.getCurrencyUnit();
                discount = discountItem.getFixedAmount();
            }
        }

        if (pnlDiscount2 != null) {
            discount2 = pnlDiscount2.getValue();

            if (pnlDiscount2.getDiscountUnit() != null && !pnlDiscount2.getDiscountUnit().equals(ProductsTable.DEFAULT_DISCOUNT_TYPE_UNIT)) {
                discountUnit2 = pnlDiscount2.getDiscountUnit();
            }
        }

        final BigDecimal itemTotalPrice = quantity != null ? quantity.multiply(unitPrice).setScale(AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP) : BigDecimal.ZERO;
        BigDecimal discountedNet = itemTotalPrice;

        if (!this.getOverallDiscount().isEnabled()) {
            discountedNet = discountUnit.equals(ProductsTable.DEFAULT_DISCOUNT_TYPE_UNIT) ? itemTotalPrice.subtract(itemTotalPrice.multiply(discount).divide(AccountingConstants.HUNDRED, AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP)) : itemTotalPrice.subtract(discount);
            discountedNet = discountUnit2.equals(ProductsTable.DEFAULT_DISCOUNT_TYPE_UNIT) ? discountedNet.subtract(itemTotalPrice.multiply(discount2).divide(AccountingConstants.HUNDRED, AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP)) : discountedNet.subtract(discount2);
        } else if (Constants.ONE_OFF_FIXED_AMOUNT.equals(this.getOverallDiscount().getType())) {
            BigDecimal itemDiscount = fixedAmountMap.getOrDefault(i, BigDecimal.ZERO);
            discountedNet = itemTotalPrice.subtract(itemDiscount);

        } else if (Constants.ONE_OFF_DISCOUNT.equals(this.getOverallDiscount().getType())) {
            BigDecimal overallDiscountValue = this.getOverallDiscount().getValue();

            discountedNet = discountUnit.equals(ProductsTable.DEFAULT_DISCOUNT_TYPE_UNIT)
                    ? itemTotalPrice.subtract(itemTotalPrice.multiply(overallDiscountValue).divide(AccountingConstants.HUNDRED, AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP))
                    : itemTotalPrice.subtract(overallDiscountValue);
        }

        final TaxLookUp taxLookUp = (TaxLookUp) this.itemsTable.getColumnById(i, ProductsTable.TAX_LIST);
        final TaxLookUp doubleTaxLookUp = (TaxLookUp) this.itemsTable.getColumnById(i, ProductsTable.DOUBLE_TAX_LIST);

        BigDecimal itemTaxAmount = BigDecimal.ZERO, doubleTaxAmount = BigDecimal.ZERO;

        if (taxLookUp != null && taxLookUp.getSelectedItemID() != null) {
            itemTaxAmount = this.calculateTaxAmount(taxLookUp, taxTotal, discountedNet);
        }
        if (doubleTaxLookUp != null && doubleTaxLookUp.getSelectedItemID() != null) {
            doubleTaxAmount = this.calculateTaxAmount(doubleTaxLookUp, taxTotal, discountedNet);
        }
        BigDecimal taxedTotal = (AccountingConstants.TAX_CALCULATION_INCLUSIVE.equals(this.taxCalculationType) || AccountingConstants.NO_TAX_CALCULATION.equals(this.taxCalculationType))
                ? discountedNet
                : this.isDoubleTaxEnabled
                ? discountedNet.add(itemTaxAmount).add(doubleTaxAmount)
                : discountedNet.add(itemTaxAmount);
        Double currentValue, needValue;
        final ExtendedHTML netAmount = (ExtendedHTML) this.itemsTable.getColumnById(i, ProductsTable.NET_AMT);
        if (netAmount != null) {
            currentValue = AccountingUtils.parsePriceToBigDecimal(netAmount.getText()).setScale(AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP).doubleValue();
            needValue = discountedNet.setScale(AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP).doubleValue();
            if (currentValue.equals(needValue)) {
                CustomCell netAmountCell = (CustomCell) this.itemsTable.getColumnCellWidgetById(i, ProductsTable.NET_AMT);
                netAmountCell.addStyleName(ERROR_FORM_STYLE);
                netAmountCell.InActive();
                this.itemsTable.notValid(0, ProductsTable.NET_AMT);
                isValidNetAndSubTotal = false;
            }
        }

        final Label totalAmount = (Label) this.itemsTable.getColumnById(i, ProductsTable.TOTAL_AMT);
        if (totalAmount != null) {
            currentValue = AccountingUtils.parsePriceToBigDecimal(totalAmount.getText()).setScale(AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP).doubleValue();
            needValue = taxedTotal.setScale(AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP).doubleValue();
            if (currentValue.equals(needValue)) {
                CustomCell totalAmountCell = (CustomCell) this.itemsTable.getColumnCellWidgetById(i, ProductsTable.TOTAL_AMT);
                totalAmountCell.addStyleName(ERROR_FORM_STYLE);
                totalAmountCell.InActive();
                this.itemsTable.notValid(0, ProductsTable.TOTAL_AMT);
                isValidNetAndSubTotal = false;
            }
        }

        return isValidNetAndSubTotal;
    }

    private boolean validateCalculatable(final String text, final boolean validateZero) {
        if (text == null || text.isEmpty()) {
            return false;
        } else return !validateZero || AccountingUtils.get().parseToBigDecimal(text).compareTo(BigDecimal.ZERO) != 0;
    }

    public void onTaxCalculationTypeChange(final Integer type, final boolean calculate) {
        if (type != null) {
            switch (type) {
                case 0:
                    this.setTaxCalculationType(AccountingConstants.NO_TAX_CALCULATION, calculate);
                    break;
                case 1:
                    this.setTaxCalculationType(AccountingConstants.TAX_CALCULATION_INCLUSIVE, calculate);
                    break;
                case 2:
                    this.setTaxCalculationType(AccountingConstants.TAX_CALCULATION_EXCLUSIVE, calculate);
                    break;
            }
        } else {
            this.setTaxCalculationType(AccountingConstants.TAX_CALCULATION_EXCLUSIVE, calculate);
        }
    }


    public void clearSelectedTaxFromItems(final boolean disableTaxField) {
        for (int i = 0; i < this.grid.getRowCount(); i++) {
            final LookUpCell lookUpCell = (LookUpCell) this.itemsTable.getColumnCellWidgetById(i, ProductsTable.TAX_LIST);
            if (lookUpCell != null) {
                final TaxLookUp tax = (TaxLookUp) lookUpCell.getLookUp();
                tax.clear();
                tax.setEnabled(!disableTaxField);

                tax.setExcludeExempt(this.reverseChargeBox != null && this.reverseChargeBox.getValue());
                lookUpCell.InActive();
            }

            final LookUpCell doubleLookUpCell = (LookUpCell) this.itemsTable.getColumnCellWidgetById(i, ProductsTable.DOUBLE_TAX_LIST);

            if (doubleLookUpCell != null) {
                final TaxLookUp doubleTax = (TaxLookUp) doubleLookUpCell.getLookUp();
                doubleTax.clear();
                doubleTax.setEnabled(!disableTaxField);
                doubleLookUpCell.InActive();
            }
        }
        this.reDrawTaxesDropdown();
    }

    public void onReverseChargeChange() {
        if (this.reverseChargeBox != null) {
            if (Utils.isUKVATRegistered()) {
                this.clearSelectedTaxFromItems(reverseChargeBox.getValue());
                return;
            }
            if (this.reverseChargeBox.getValue()) {
                this.clearSelectedTaxFromItems(false);
            } else if (Utils.isSaudiCompany()) {
                this.clearSelectedTaxFromItems(true);
            } else {
                this.clearSelectedTaxFromItems(Constants.NON_VAT_REGISTERED_DESIGNATED_ZONE.equalsIgnoreCase(this.getSupplierTaxTreatment()));
            }
        }
    }

    private void setTaxCalculationType(final Integer taxCalculationType, final boolean calculate) {
        this.taxCalculationType = taxCalculationType;

        //This is for enabling/disabling tax according to tax calculation type
        if (AccountingConstants.NO_TAX_CALCULATION.equals(taxCalculationType)) {
            this.clearSelectedTaxFromItems(true);
        } else {
            for (int i = 0; i < this.grid.getRowCount(); i++) {
                final LookUpCell lookUpCell = (LookUpCell) this.itemsTable.getColumnCellWidgetById(i, ProductsTable.TAX_LIST);

                if (lookUpCell == null) {
                    break;
                }

                final TaxLookUp tax = (TaxLookUp) lookUpCell.getLookUp();
                lookUpCell.InActive();

                if (!(Constants.VAT_COUNTRIES.contains(Utils.getCompanyrCountryCode()) && Utils.isVatRegistered()) || !(this.isPurchaseOrder || this.isPurchaseInvoice || this.isSupplierCreditNote)) {
                    tax.setEnabled(true);
                }
                final LookUpCell doubleLookUpCell = (LookUpCell) this.itemsTable.getColumnCellWidgetById(i, ProductsTable.DOUBLE_TAX_LIST);

                if (doubleLookUpCell != null) {
                    doubleLookUpCell.InActive();
                }
            }
            if (calculate) {
                this.calculate();
            }
        }
    }

    public void setCreditPaymentParams(final String name, final BigDecimal amount) {
        this.creditedInvoiceLabel = new HTML(name);
        this.creditedInvoiceLabel.setStyleName(AccountingCustomFormConstants.STYLE_TOTAL_LABEL);
        creditedInvoiceAmount = amount;
        creditableMaxInvoiceAmount = amount;
        this.creditedInvoice.setHTML(ProductsTable.utils.formatPrice(this.creditedInvoiceAmount));
    }

    public void setEnabled(final boolean enabled) {
        this.setEnabled(enabled, false, null);
    }

    public void setEnabled(final boolean enabled, final boolean enableforDescription, final Integer fromType) {
        for (int i = 0; i < this.grid.getRowCount(); i++) {
            final Widget product = this.itemsTable.getColumnById(i, ProductsTable.PRODUCT);
            final ProductDescriptionTextArea description = (ProductDescriptionTextArea) this.itemsTable.getColumnById(i, ProductsTable.DESCRIPTION);
            final ItemQtyPanel qtyPanel = (ItemQtyPanel) this.itemsTable.getColumnById(i, ProductsTable.QTY);
            final MeasurementsLookUp unitMeasure = (MeasurementsLookUp) this.itemsTable.getColumnById(i, ProductsTable.MEASUREMENT);
            final UnitPrice unitPrice = (UnitPrice) this.itemsTable.getColumnById(i, ProductsTable.UNITPRICE);
            final DataListBox discountList = (DataListBox) this.itemsTable.getColumnById(i, ProductsTable.DISCOUNT_LIST);
            final Discount pnlDiscount = (Discount) this.itemsTable.getColumnById(i, ProductsTable.DISCOUNT_AMT);
            final DataListBox discountList2 = (DataListBox) this.itemsTable.getColumnById(i, ProductsTable.DOUBLE_DISCOUNT_LIST);
            final Discount pnlDiscount2 = (Discount) this.itemsTable.getColumnById(i, ProductsTable.DOUBLE_DISCOUNT_AMT);
            final Widget accounts = this.itemsTable.getColumnById(i, ProductsTable.ACCOUNT);
            final Widget taxColum = this.itemsTable.getColumnById(i, ProductsTable.TAX_LIST);

            boolean saleInvoiceOrPurchaseOrder = SALE_INVOICE.equals(formType) || PURCHASE_ORDER.equals(formType);
            if (product instanceof ProductLookUp) {
                ProductLookUp productLookUp = (ProductLookUp) product;
                productLookUp.setEnabled(enabled);
                if (saleInvoiceOrPurchaseOrder) {
                    productLookUp.getSuggestBox().getTextBox().addMouseOutHandler(fucus -> WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_INVOICE_LINE_ITEM_FOCUS, null, ProductsTable.this));
                }
            } else if (product instanceof CustomCellTextArea) {
                ((CustomCellTextArea) product).setEnabled(enabled);
            }
            if (accounts instanceof AccountsLookUp) {
                if (AccountingConstants.COPY_FROM_FIXED_ASSET.equals(fromType) || Constants.PURCHASE_ORDER.equals(formType)) {
                    ((AccountsLookUp) accounts).setEnabled(enabled);
                }
            }
            if (taxColum instanceof SmartTaxRateLookUp) {
                SmartTaxRateLookUp taxRateLookUp = (SmartTaxRateLookUp) taxColum;
                taxRateLookUp.setEnabled(enabled);
                if (saleInvoiceOrPurchaseOrder) {
                    taxRateLookUp.getSuggestBox().getTextBox().addMouseOutHandler(fucus -> WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_INVOICE_LINE_ITEM_FOCUS, null, ProductsTable.this));
                }
            }
            if (description != null) {
                description.setEnabled(enableforDescription);
            }
            if (qtyPanel != null) {
                qtyPanel.setEnabled(enabled);
            }
            if (unitMeasure != null) {
                unitMeasure.setEnabled(enabled);
                if (saleInvoiceOrPurchaseOrder) {
                    unitMeasure.getTextBox().addMouseOverHandler(fucus -> WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_INVOICE_LINE_ITEM_FOCUS, null, ProductsTable.this));
                }
            }
            if (unitPrice != null) {
                unitPrice.setEnabled(enabled);
                if (saleInvoiceOrPurchaseOrder) {
                    unitPrice.addMouseOutHandler(fucus -> WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_INVOICE_LINE_ITEM_FOCUS, null, ProductsTable.this));
                }

            }
            if (discountList != null) {
                discountList.setEnabled(enabled);
            }
            if (pnlDiscount != null) {
                pnlDiscount.setEnabled(enabled);
            }
            if (discountList2 != null) {
                discountList2.setEnabled(enabled);
            }
            if (pnlDiscount2 != null) {
                pnlDiscount2.setEnabled(enabled);
            }
        }
    }

    public BigDecimal getCreditedInvoiceAmount() {
        return this.creditedInvoiceAmount;
    }

    private String getZeroAsString() {
        return AccountingUtils.getZero();
    }

    public ExtendedHTML getZeroAsHTML() {
        final ExtendedHTML zeroValue = new ExtendedHTML(AccountingUtils.getZero());
        zeroValue.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        return zeroValue;
    }

    public void setCurrencyId(final Integer currencyId) {
        this.currencyId = currencyId;
    }

    public Integer getCurrencyId() {
        return this.currencyId;
    }

    public void setCustomerSupplierItem(final TypeItem customerSupplierItem) {
        this.customerSupplierItem = customerSupplierItem;
    }

    /*public void setReverseChargeApplicable(boolean reverseChargeApplicable) {
        this.reverseChargeApplicable = Utils.hasGenericAccess(GenericSettingsEnum.ACCOUNTING_IS_REVERSE_CHARGE) && reverseChargeApplicable;
    }*/

    public ItemQtyPanel getQtyPanelInstance(final LinkedHashMap<String, Widget> widgetsMap) {
        return new ItemQtyPanel(widgetsMap, this.isProjectBasedInvoice, false);
    }

    public DataListBox getPaymentTermsConditionsListBox() {
        return this.paymentTermsConditionsListBox;
    }

    public String getPaymentTermsConditionsTemplate() {
        return this.paymentTermsConditionsListBox.isSomethingSelected() ? this.paymentTermsConditionsListBox.getSelectedItem().getDescription() : null;
    }

    public PriceLevelItem getPriceLevel() {
        return this.priceLevel;
    }

    public void setPriceLevel(final PriceLevelItem priceLevel) {
        this.priceLevel = priceLevel;
    }

    public boolean isPriceLavelChanged() {
        return this.priceLavelChanged;
    }

    public DiscountItem getClientDiscount() {
        return this.clientDiscount;
    }

    public void setClientDiscount(final DiscountItem clientDiscount) {
        this.clientDiscount = clientDiscount;
    }

    private String getCurrencyUnit() {
        return this.currencyUnit;
    }

    private void setCurrencyUnit(final String currencyUnit) {
        this.currencyUnit = currencyUnit;
    }

    public void setExternalFormType(final Integer externalFormType) {
        this.externalFormType = externalFormType;
    }

    public HashMap<String, ColumnConfig> getColumnsMap() {
        return this.columnsMap;
    }

    public HTML getBillableExpenseAmount() {
        return this.billableExpenseAmount;
    }

    public HTML getBillableExpenseTaxAmount() {
        return this.billableExpenseTaxAmount;
    }

    public HTML getBillableExpenseAmountLabel() {
        return this.billableExpenseAmountLabel;
    }

    public HTML getBillableExpenseTaxLabel() {
        return this.billableExpenseTaxLabel;
    }

    public void setHasBillableExp(final boolean hasBillableExp) {
        this.hasBillableExp = hasBillableExp;
    }

    public boolean hasBillableExp() {
        return this.hasBillableExp;
    }

    public void setBillableExpenseTotal(final BigDecimal billableExpenseTotal) {
        this.billableExpenseTotal = billableExpenseTotal;
    }

    public void setBillableExpenseTaxTotal(final BigDecimal billableExpenseTaxTotal) {
        this.billableExpenseTaxTotal = billableExpenseTaxTotal;
    }

    public void setNewInvoice(final NewInvoice newInvoice) {
        this.newInvoice = newInvoice;
    }

    public NewInvoice getNewInvoice() {
        return this.newInvoice;
    }

    public void setFromMultiQuoteConvert(final Boolean fromMultiQuoteConvert) {
        this.fromMultiQuoteConvert = fromMultiQuoteConvert;
    }

    public void setRoundingModeDisabled(final boolean roundingModeDisabled) {
        this.isRoundingModeDisabled = roundingModeDisabled;
    }

    public void setColumnsMap(final LinkedHashMap<String, ColumnConfig> columns) {
        this.columnsMap = columns;
    }

    public void setItemCustomFields(final List<CompanyCustomFieldItem> customFields) {
        if (customFields != null && !customFields.isEmpty()) {
            this.customFieldsMap = new HashMap<>();
            for (final CompanyCustomFieldItem field : customFields) {
                this.customFieldsMap.put(field.getColumnCode(), field);
            }
        }
    }

    public Boolean isDoubleTaxEnabled() {
        return this.isDoubleTaxEnabled;
    }

    public void setDoubleTaxEnabled(final Boolean enabled) {
        this.isDoubleTaxEnabled = enabled;
    }

    public void setDoubleDiscountEnabled(final Boolean enabled) {
        isDoubleDiscountEnabled = enabled;
    }

    public Boolean isQuoteComissionEnabled() {
        return this.isQuoteComissionEnabled;
    }

    public void setQuoteComissionEnabled(final Boolean quoteComissionEnabled) {
        this.isQuoteComissionEnabled = quoteComissionEnabled;
    }


    public void setComissionListener(final Command comissionListener) {
        this.comissionListener = comissionListener;
    }

    public ArrayList<Integer> getConvertedQuoteIds() {
        return this.convertedQuoteIds;
    }

    public void setConvertedQuoteIds(final ArrayList<Integer> convertedQuoteIds) {
        this.convertedQuoteIds = convertedQuoteIds;
    }

    public void setCrmAccountLookUp(final LookUp crmAccountLookUp) {
        this.crmAccountLookUp = crmAccountLookUp;
    }

    public void clearProjectFromLineItems() {

        if (Utils.isProjectInLineItemEnable() && (Constants.SALE_INVOICE.equals(this.formType) || Constants.SALE_QUOTE.equals(this.formType) || Constants.SALE_ORDER.equals(this.formType))) {
            for (int i = 0; i < this.grid.getRowCount(); i++) {
                final ProjectLookUp projectLookUp = (ProjectLookUp) this.itemsTable.getColumnById(i, ProductsTable.PROJECT);

                if (projectLookUp != null) {
                    projectLookUp.clear();
                    final LookUpCell projectCell = (LookUpCell) this.itemsTable.getColumnCellWidgetById(i, ProductsTable.PROJECT);
                    projectCell.InActive();
                }
            }
        }
    }

    public void setCurrencyWidget(CurrencyWidget currencyWidget) {

        if (currencyWidget != null) {
            this.currencyWidget = currencyWidget;
            this.currencyWidget.addListener(() -> {
                this.getTotalLabel().setHTML(ProductsTable.accountingMessages.dynamicTotal(currencyWidget.getCurrencyName()));
                //this is for the discount unit
                boolean recalculate = false;//true if rate has changed only (same currency)

                if (currencyWidget.getCurrencyID() != null) {
                    final CurrencyItem currencyItem = currencyWidget.getCurrency();
                    if (currencyItem.getSymbol() != null && !currencyItem.getSymbol().isEmpty()) {
                        this.setCurrencyUnit(currencyItem.getSymbol());
                    } else {
                        this.setCurrencyUnit(currencyItem.getName());
                    }
                    if (currencyItem.getId() != null) {
                        recalculate = currencyItem.getId().equals(this.getCurrencyId());
                        this.setCurrencyId(currencyItem.getId());
                    }
                }
                this.setExchangeRateValue(currencyWidget.getExchangeRate());

                if ((this.formParameters.getExternalFormID() == null
                        || AccountingConstants.COPY_FROM_CLIENT_SUPPLIER.equals(this.formParameters.getExternalFormID())
                        || AccountingConstants.COPY_FROM_CRM_ACCOUNT.equals(this.formParameters.getExternalFormID()))
                        && Utils.isMultipleSalesPriceEnable()) {
                    this.checkProductMultiCurrencySellingPrice();
                }
                this.calculate(recalculate);

                if (this.getPriceLevel() != null) {
                    this.onClientPriceLevelChange(this.getNewInvoice());
                }
            });
        }
    }

    public void setProjectBasedInvoice(final boolean projectBasedInvoice) {
        this.isProjectBasedInvoice = projectBasedInvoice;
    }

    public void setReverseChargeBox(final KpiCheckBox reverseChargeBox) {
        this.reverseChargeBox = reverseChargeBox;
    }

    public KpiCheckBox getReverseChargeBox() {
        return this.reverseChargeBox;
    }

    public void setPlaceOfSupplyBox(final WfmDropdown placeOfSupplyBox) {
        this.placeOfSupplyBox = placeOfSupplyBox;

        if (this.placeOfSupplyBox != null && (this.isPurchaseOrder || this.isPurchaseInvoice || this.isSupplierCreditNote)) {
            this.placeOfSupplyBox.addValueChangeHandler(ch -> this.checkForReverseCharge());
        }
    }

    /**
     * This method verifies that Customer/Supplier is configured reverse-charge applicable transaciton
     *
     * @return
     */
    public boolean isReverseChargeApplicable() {
        if (!(this.isPurchaseOrder || this.isPurchaseInvoice || this.isSupplierCreditNote)) {
            return false;
        }
        if (this.customerSupplierItem == null) {
            return false;
        }
        if (this.reverseChargeBox == null || !this.reverseChargeBox.isAttached()) {
            return false;
        }
        if (Utils.isUKVATRegistered()) {
            return false;
        }
        return this.reverseChargeBox.isAttached();
    }

    private String getSupplierTaxTreatment() {

        if (this.customerSupplierItem == null || !(this.isPurchaseOrder || this.isPurchaseInvoice || this.isSupplierCreditNote)) {
            return null;
        }
        if (this.customerSupplierItem.getTaxTreatment() != null) {
            return this.customerSupplierItem.getTaxTreatment().getCode();
        }
        return null;
    }


    Map<String, QuantityItem> mapInvoiceItems(NewInvoiceItem[] items) {
        List<QuantityItem> itemList = Stream.of(items)
                .filter(item -> item.getItemID() != null && Arrays.asList(INVENTORY_ITEM, ASSEMBLY_ITEM, PRODUCT_KIT).contains(item.getProductType()))
                .map(item -> {
                    QuantityItem qitem = new QuantityItem();
                    qitem.setId(item.getItemID());
                    qitem.setWarehouseID(item.getWarehouse() != null ? item.getWarehouse().getId() : null);
                    qitem.setQuantity(item.getQuantity());
                    return qitem;
                }).collect(Collectors.toList());
        Map<String, QuantityItem> map = new HashMap<>();
        itemList.forEach(item -> {
            String key = item.getId() + (item.getWarehouseID() != null ? "_" + item.getWarehouseID() : "");
            if (map.get(key) != null) {
                map.get(key).setQuantity(map.get(key).getQuantity().add(item.getQuantity()));
            } else {
                map.put(key, item);
            }
        });

        return map;
    }

    public boolean validateUseInGrn() {
        for (int rowID = 0; rowID < grid.getRowCount(); rowID++) {
            itemsTable.resetValidation(rowID);
            Widget item = itemsTable.getColumnById(rowID, ProductsTable.PRODUCT);
            ItemQtyPanel qtyPanel = (ItemQtyPanel) itemsTable.getColumnById(rowID, ProductsTable.QTY);
            ExtendedHTML total = (ExtendedHTML) itemsTable.getColumnById(rowID, ProductsTable.TOTAL_AMT);
            if (item instanceof SmartProductLookUp && ((SmartProductLookUp) item).getSelectedItem() != null && ((SmartProductLookUp) item).getUsedInGrn() != null) {
                ShippingDataItem dataItem = ((SmartProductLookUp) item).getUsedInGrn();
                if (dataItem.getReceiveType().equals(ReceiveTypeEnum.RECEIVE_BY_VALUE)) {
                    if (ProductsTable.utils.parseToBigDecimal(total.getText()).compareTo(dataItem.getAmount()) < 0) {
                        total.addStyleName(ERROR_FORM_STYLE);
                        Info.warn(wfmStrings.quantityCannotBeLessThanReceived());
                        return false;
                    }
                } else {
                    if (qtyPanel.getQty().compareTo(dataItem.getAmount()) < 0) {
                        qtyPanel.addStyleToTextbox();
                        Info.warn(wfmStrings.quantityCannotBeLessThanReceived());
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void setIsDeleteAndAddDsiabled(boolean isDisabled) {
        this.isDeleteAndAddDsiabled = isDisabled;
    }

    protected class PaymentInformation extends FigureWidget {

        private final String action;
        boolean isBankReceiptDelete = Utils.hasPermission(ACCOUNTING_BANK_ACCOUNT_RECEIVE_DELETE);
        boolean isCashReceiptDelete = Utils.hasPermission(ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT_DELETE);
        boolean isBankReceiptSummary = Utils.hasPermission(ACCOUNTING_BANK_ACCOUNT_RECEIVE_SUMMARY);
        boolean isCashReceiptSummary = Utils.hasPermission(ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT_SUMMARY);

        public PaymentInformation(PaymentItem paymentItem, String title) {
            this(paymentItem, title, null);
        }

        public PaymentInformation(PaymentItem paymentItem, String title, String action) {
            this.action = action;
            addStyleName("right-label");

            FigCaption figCaption = new FigCaption();
            add(figCaption);

            Div container = new Div();
            figCaption.add(container);

            if (action != null && !action.isEmpty()) {
                SvgIcon trashIcon = new SvgIcon((SvgEnum.trash2));
                MaterialLink removePaymentLink = new MaterialLink();
                removePaymentLink.setClass("btn--icon");
                if (isBankReceiptDelete || isCashReceiptDelete) {
                    removePaymentLink.add(trashIcon);
                    removePaymentLink.addClickHandler(ch -> deletePaymentItem(paymentItem));
                }
                Span span = new Span(title);
                MaterialLink detailsLink = new MaterialLink(title, action);

                HorizontalPanelDiv pnlCont = new HorizontalPanelDiv();
                pnlCont.add(removePaymentLink);
                pnlCont.add(isBankReceiptSummary || isCashReceiptSummary ? detailsLink : span);
                container.add(pnlCont);
            } else {
                container.add(new Span(title));
            }
            figCaption.add(new Small(DateUtils.format(paymentItem.getDate())));

            SvgIcon svgIcon = new SvgIcon(SvgEnum.check);
            Div iconWrapper = new Div();
            iconWrapper.setClass("icon-wrapp--circle");
            iconWrapper.add(svgIcon);
            add(iconWrapper);
        }

        private void deletePaymentItem(PaymentItem paymentItem) {
            final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.INFO, Action.YesNo);
            messageBox.setTitle(wfmStrings.confirmation());
            messageBox.setMessage(accountingMessages.areYouSureYouWantToDelete(wfmStrings.payment()));
            messageBox.addCloseHandler(new CloseHandler() {
                @Override
                public void onSubmit() {
                    LoadingPanel.loading(true);
                    InvoiceService.App.get().deletePayment(paymentItem.getObjectId(), new AbstractAsyncCallback<TestRPC>() {
                        public void failure(Throwable caught) {
                            LoadingPanel.loading(false);
                        }

                        public void success(TestRPC result) {
                            initialize();
                        }
                    });
                }

                @Override
                public void onCancel() {

                }
            });
            messageBox.open();
        }

        public String getAction() {
            return action;
        }
    }
}