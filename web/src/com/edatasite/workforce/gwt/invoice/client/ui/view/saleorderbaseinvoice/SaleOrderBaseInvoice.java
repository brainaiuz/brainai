package com.edatasite.workforce.gwt.invoice.client.ui.view.saleorderbaseinvoice;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.cell.CheckBoxCell;
import com.edatasite.workforce.gwt.core.client.ui.cell.SimpleLinkCell;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.lookup.CrmAccountLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.panel.HorizontalPanelDiv;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.invoice.client.rpc.TypeItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.enums.QIGroupingField;
import com.edatasite.workforce.gwt.invoice.client.rpc.saleorderbaseinvoice.SaleOrderBaseInvoiceItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceServiceAsync;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.QuoteService;
import com.edatasite.workforce.gwt.invoice.client.ui.view.components.InvoiceAdvancedOptions;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Heading;
import gwt.material.design.client.ui.html.Span;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.UI_TYPE_DATEPICKER;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.UI_TYPE_DATEPICKER_TIME;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.CUSTOMER_CLICKABLE;

public class SaleOrderBaseInvoice extends Composite {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final ProvidesKey<SaleOrderBaseInvoiceItem> KEY_PROVIDER = item -> item == null ? null : item.getObjectId();
    private static final SaleOrderBasedInvoiceUiBinder ourUiBinder = GWT.create(SaleOrderBasedInvoiceUiBinder.class);
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private static final InvoiceServiceAsync invoiceService = InvoiceService.App.get();

    private static final int PAGE_SIZE = 30;
    private static final int ROW_HEIGHT = 24;

    @UiField
    FormGroup fgCustomer;
    @UiField
    FormGroup fgInvoiceType;
    @UiField
    FormGroup fgType;
    @UiField
    FormGroup fgStatus;
    @UiField
    FormGroup fgTaxCalcType;
    @UiField
    FormGroup fgDateType;
    @UiField
    FormGroup fgFromDate;
    @UiField
    FormGroup fgToDate;
    @UiField
    Div pnlDataGrid;
    @UiField
    SOBaseInvoiceGroups soBaseInvoiceGroups;
    @UiField
    HTMLPanel pnlGroupByItem;
    @UiField
    Div groupByItemContainer;
    @UiField
    HorizontalPanelDiv groupingFieldsContainer;
    @UiField
    FormGroup showMoreLink;
    private final int MAX_HEIGHT = 400;

    CrmAccountLookUp customerLookUp;
    DataListBox invoiceTypeList;
    DatePicker fromDateBox;
    DatePicker toDateBox;
    DataListBox taxCalcTypeList;
    DataListBox dateTypeList;
    DataListBox typeList; //contains {BOTH, ONLY_SALE_ORDER, ONLY_SALE_QUOTE}
    DataListBox statusList;
    KpiCheckBox showOnlyGdnsBox;
    KpiCheckBox groupByItemBox;
    KpiCheckBox itemBox;
    KpiCheckBox priceBox;
    KpiCheckBox accountBox;
    KpiCheckBox taxBox;
    KpiCheckBox departmentBox;
    Command cmdInvoiceTypeHandler;
    Command cmdStatusChangeHandler;
    Command cmdBasedOnChangeHandler;
    InvoiceAdvancedOptions advancedOptions;
    MaterialLink showCustomFields;
    ArrayList<CompanyCustomFieldItem> saleOrderCustomFields;
    ArrayList<CompanyCustomFieldItem> saleQuoteCustomFields;
    ListingFilterParameter fp;
    HashMap<String, String> customFieldsMap;
    private final int MIN_HEIGHT = 250;
    private final String type;

    private KpiDataGrid<SaleOrderBaseInvoiceItem> dataGrid;
    private ListDataProvider<SaleOrderBaseInvoiceItem> dataProvider;
    private MaterialLink customerBalanceLink;

    public SaleOrderBaseInvoice(String type) {
        this.type = type;
        initWidget(ourUiBinder.createAndBindUi(this));
        init();
    }

    void init() {
        customerLookUp = new CrmAccountLookUp(CrmAccountLookUp.CUSTOMER, true);
        fgCustomer.setLabel(Property.get(Constants.CLIENT_LIST, wfmStrings.customer()));
        fgCustomer.addToContent(customerLookUp);
        fgCustomer.setId("fgcustomer");
        customerLookUp.getSuggestBox().addSelectionHandler(sh -> loadData());

        invoiceTypeList = new DataListBox();
        invoiceTypeList.setWithoutNullLabel(true);
        invoiceTypeList.setItems(new SelectItem[]{
                new SelectItem(INVOICE_TYPE.DETAILED_INVOICE, accountingStrings.detailedInvoice()),
                new SelectItem(INVOICE_TYPE.GROUPED_BY_OBJECT, accountingStrings.groupByOrder())
        });
        invoiceTypeList.addValueChangeHandler(ch -> cmdInvoiceTypeHandler.execute());
        invoiceTypeList.setSelected(INVOICE_TYPE.DETAILED_INVOICE);
        fgInvoiceType.setLabel(wfmStrings.invoiceType());
        fgInvoiceType.addToContent(invoiceTypeList);

        customerBalanceLink = new MaterialLink(AccountingUtils.get().formatPrice(BigDecimal.ZERO));
        customerBalanceLink.setHref("javaScript:void(0)");
        customerBalanceLink.getElement().getStyle().setProperty("pointerEvents", "none");

        Div supplierFieldLabel = fgCustomer.getGroupLabel();
        supplierFieldLabel.addStyleName("label-group");


        Span balance = new Span(wfmStrings.balance() + ": ");
        balance.add(customerBalanceLink);
        supplierFieldLabel.add(balance);

        typeList = new DataListBox();
        typeList.setWithoutNullLabel(true);
        typeList.setItems(new SelectItem[]{
                new SelectItem(BASED_ON.BOTH, "Both"),
                new SelectItem(BASED_ON.SALE_ORDER, Property.get(Constants.SALE_ORDER_CODE, accountingStrings.salesOrder())),
                new SelectItem(BASED_ON.SALE_QUOTE, Property.get(Constants.SALE_QUOTE, wfmStrings.salesQuote())),
        });
        typeList.setSelected(BASED_ON.BOTH);
        typeList.addValueChangeHandler(ch -> cmdBasedOnChangeHandler.execute());
        fgType.setLabel(accountingStrings.basedOn());
        fgType.addToContent(typeList);

        statusList = new DataListBox();
        statusList.setWithoutNullLabel(true);
        statusList.setItems(new SelectItem[]{
                new SelectItem(STATUS_TYPE.APPROVED, wfmStrings.approved()),
                new SelectItem(STATUS_TYPE.SHIPPED_OR_PARTIAL_SHIPPED, accountingStrings.shipped() + "/" + accountingStrings.partialShipped())
        });
        statusList.setSelected(STATUS_TYPE.APPROVED);
        statusList.setEnabled(false);
        statusList.addValueChangeHandler(vch -> cmdStatusChangeHandler.execute());
        fgStatus.setLabel(wfmStrings.status());
        fgStatus.addToContent(statusList);
        customerLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
            onChangeLookUp();
        });

        String dateLabel = Property.get(Constants.SALE_ORDER_CODE, accountingStrings.salesOrder()) + "/" + Property.get(Constants.SALE_QUOTE, wfmStrings.salesQuote()) + " " + wfmStrings.date();
        if (SaleOrderBaseInvoiceItem.SALE_ORDER.equals(type)) {
            dateLabel = Property.get(Constants.SALE_ORDER_CODE, accountingStrings.salesOrder()) + " " + wfmStrings.date();
        } else if (SaleOrderBaseInvoiceItem.SALE_QUOTE.equals(type)) {
            dateLabel = Property.get(Constants.SALE_QUOTE, wfmStrings.salesQuote()) + " " + wfmStrings.date();
        } else if (SaleOrderBaseInvoiceItem.GDN.equals(type)) {
            dateLabel = WfmStrings.App.get().shipDate();
        }
        dateTypeList = new DataListBox();
        dateTypeList.setWithoutNullLabel(true);
        dateTypeList.setItems(new SelectItem[]{
                new SelectItem(DATE_TYPE.ORDER_DATE, dateLabel),
                new SelectItem(DATE_TYPE.DUE_DATE, wfmStrings.dueDate()),
        });
        dateTypeList.setSelected(DATE_TYPE.ORDER_DATE);
        dateTypeList.addValueChangeHandler(ch -> loadData());
        dateTypeList.setEnabled(!SaleOrderBaseInvoiceItem.GDN.equals(type));
        fgDateType.setLabel(WfmStrings.App.get().filter());
        fgDateType.addToContent(dateTypeList);

        //date period fields
        fromDateBox = new DatePicker(false);
        fromDateBox.addChangeHandler(ch -> loadData());
        fgFromDate.setLabel(wfmStrings.from());
        fgFromDate.addToContent(fromDateBox);

        toDateBox = new DatePicker(false);
        toDateBox.addChangeHandler(ch -> loadData());
        fgToDate.setLabel(wfmStrings.to());
        fgToDate.addToContent(toDateBox);

        //tax fields
        taxCalcTypeList = new DataListBox();
        taxCalcTypeList.setWithoutNullLabel(true);
        taxCalcTypeList.setItems(new SelectItem[]{
                new SelectItem(0, wfmStrings.noTax()),//NO TAX
                new SelectItem(1, wfmStrings.taxInclusive()),//TAX INCLUSIVE
                new SelectItem(2, wfmStrings.taxExclusive())});//TAX EXCLUSIVE
        getDefaultTaxCalc();
        taxCalcTypeList.addValueChangeHandler(ch -> loadData());

        fgTaxCalcType.setLabel(accountingStrings.amounts());
        fgTaxCalcType.addToContent(taxCalcTypeList);

        showOnlyGdnsBox = new KpiCheckBox();
        showOnlyGdnsBox.setText("Show GDNs only");
        showOnlyGdnsBox.addValueChangeHandler(ch -> loadData());

        advancedOptions = createAdvancedOptions();
        showCustomFields = new MaterialLink(wfmStrings.showAdditionalFields());
        showCustomFields.addStyleName("btn-flat SalesQuoteView");
        showCustomFields.addClickHandler(ch -> {
            showAdvancedOptions(wfmStrings.customFields(), advancedOptions);
        });
        showMoreLink.addToContent(showCustomFields);
        showMoreLink.setVisible(false);

        initGroupByItemPanel();

        soBaseInvoiceGroups.setObjectType(type != null ? type : SaleOrderBaseInvoiceItem.SALE_ORDER);

        initItemsTable();
        initEventHandlers();
    }

    private void onChangeLookUp() {
        if (!Validation.validateLookUpRequired(customerLookUp)) {
            return;
        }
        invoiceService.getClientOrSupplier(customerLookUp.getSelectedItemID(), Constants.RECEIVABLE, new AsyncCallback<TypeItem>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(TypeItem typeItem) {
                if (typeItem.getSupplierCustomerBalance() >= 0) {
                    customerBalanceLink.setText(AccountingUtils.get().formatPrice(typeItem.getSupplierCustomerBalance()));
                } else {
                    customerBalanceLink.setText("(" + AccountingUtils.get().formatPrice((-1) * typeItem.getSupplierCustomerBalance()) + ")");
                }
                customerBalanceLink.getElement().getStyle().setProperty("pointerEvents", "visible");
                if (Utils.hasPermission(CUSTOMER_CLICKABLE)) {
                    customerBalanceLink.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("customerBalance|customerBalance/" + customerLookUp.getSelectedItemID() + "/" + CrmAccountItem.CUSTOMER));
                }
            }
        });
    }


    public void showAdvancedOptions(String title, InvoiceAdvancedOptions advancedOptions) {
        KpiSideNavBox popUp = new KpiSideNavBox();

        Heading h1 = new Heading(HeadingSize.H1);
        h1.setClass("hasicon--left");
        h1.add(new Span(title));
        WfmButton2 applyFilter = new WfmButton2(wfmStrings.apply(), WfmButton2.BTN_PRIMARY);
        applyFilter.addClickHandler(event -> {
            customFieldsMap = new HashMap<>();
            List<CompanyCustomFieldItem> cfValues = advancedOptions.getCustomFieldsData();
            if (cfValues != null) {
                for (CompanyCustomFieldItem cf : cfValues) {
                    String value;
                    if (cf.getFieldDateNonConvertedValue() != null && (cf.getUiType().equals(UI_TYPE_DATEPICKER_TIME) || cf.getUiType().equals(UI_TYPE_DATEPICKER))) {
                        value = DateUtils.formatInternalShort1(cf.getFieldDateNonConvertedValue().getNonConvertedDate());
                    } else {
                        value = cf.getFieldStringValue();
                    }
                    if (value != null && !"".equals(value)) {
                        customFieldsMap.put(cf.getColumnCode(), value);
                    }
                }
                loadData();
            }
            popUp.remove();
        });
        popUp.addHeader(h1);
        popUp.addBody(advancedOptions);
        popUp.addFooter(applyFilter);
        popUp.show();
    }

    void initEventHandlers() {
        cmdInvoiceTypeHandler = () -> {
            Integer invTypeId = invoiceTypeList.getSelectedId();
            if (invTypeId == INVOICE_TYPE.DETAILED_INVOICE) {
                soBaseInvoiceGroups.onClickInvoiceType(SOBaseInvoiceGroups.DETAILED_INVOICE);
                pnlGroupByItem.setVisible(true);
            } else if (invTypeId == INVOICE_TYPE.GROUPED_BY_OBJECT) {
                soBaseInvoiceGroups.onClickInvoiceType(SOBaseInvoiceGroups.GROUPED_BY_OBJECT);
                pnlGroupByItem.setVisible(false);
            }
        };
        cmdBasedOnChangeHandler = () -> {
            Integer basedOn = typeList.getSelectedId();

            if (basedOn != BASED_ON.SALE_ORDER) {
                statusList.setSelected(STATUS_TYPE.APPROVED);
                statusList.setEnabled(Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_SALES_QUOTE_PICKLIST));
                soBaseInvoiceGroups.setObjectType(SaleOrderBaseInvoiceItem.SALE_QUOTE);
                if (basedOn == BASED_ON.SALE_QUOTE) {
                    if (saleQuoteCustomFields == null) {
                        getCustomFields();
                    }
                    advancedOptions.createAndAppendCustomFieldsView(null, saleQuoteCustomFields);
                }
            } else {
                statusList.setEnabled(true);
                if (saleOrderCustomFields == null) {
                    getCustomFields();
                }
                advancedOptions.createAndAppendCustomFieldsView(null, saleOrderCustomFields);
            }
            cmdStatusChangeHandler.execute();
        };
        cmdStatusChangeHandler = () -> {
            Integer selectedStatus = statusList.getSelectedId();

            if (selectedStatus == STATUS_TYPE.SHIPPED_OR_PARTIAL_SHIPPED) {
                showOnlyGdnsBox.setValue(true);
                soBaseInvoiceGroups.setVisible(false);
                invoiceTypeList.setSelected(INVOICE_TYPE.DETAILED_INVOICE);
                invoiceTypeList.setEnabled(false);
                cmdInvoiceTypeHandler.execute();
                soBaseInvoiceGroups.setObjectType(SaleOrderBaseInvoiceItem.GDN);
            } else {
                showOnlyGdnsBox.setValue(false);
                invoiceTypeList.setEnabled(true);
                soBaseInvoiceGroups.setVisible(true);

                if (typeList.getSelectedId() == BASED_ON.SALE_ORDER) {
                    soBaseInvoiceGroups.setObjectType(SaleOrderBaseInvoiceItem.SALE_ORDER);
                }
            }
            loadData();
        };

        if (SaleOrderBaseInvoiceItem.SALE_ORDER.equals(type) || SaleOrderBaseInvoiceItem.GDN.equals(type)) {
            typeList.setSelected(BASED_ON.SALE_ORDER);
            typeList.setEnabled(false);

            if (SaleOrderBaseInvoiceItem.GDN.equals(type)) {
                statusList.setSelected(STATUS_TYPE.SHIPPED_OR_PARTIAL_SHIPPED);
            }
            cmdBasedOnChangeHandler.execute();
            if (SaleOrderBaseInvoiceItem.GDN.equals(type)) {
                statusList.setEnabled(false);
            }
        } else if (SaleOrderBaseInvoiceItem.SALE_QUOTE.equals(type)) {
            typeList.setSelected(BASED_ON.SALE_QUOTE);
            typeList.setEnabled(false);
            cmdBasedOnChangeHandler.execute();
        }
        getCustomFields();
    }

    void initGroupByItemPanel() {
        String defaultConfig = Utils.userSettings.get(Constants.MULTI_QUOTE_CONVERTER_GROUP_BY_ITEM_CONFIG);
        defaultConfig = defaultConfig != null && !"null".equals(defaultConfig.trim()) ? defaultConfig.trim() : null;
        groupingFieldsContainer.setVisible(defaultConfig != null && defaultConfig.length() > 0);

        groupByItemBox = new KpiCheckBox("Group By Item");
        groupByItemBox.addValueChangeHandler(ch -> {
            groupingFieldsContainer.setVisible(groupByItemBox.getValue());
        });
        groupByItemBox.setValue(groupingFieldsContainer.isVisible());
        groupByItemContainer.add(groupByItemBox);

        itemBox = new KpiCheckBox(QIGroupingField.ITEM.name());
        itemBox.setValue(true);
        itemBox.setEnabled(false);

        priceBox = new KpiCheckBox(QIGroupingField.PRICE.name());
        priceBox.setValue(defaultConfig != null && defaultConfig.contains(QIGroupingField.PRICE.name()));

        accountBox = new KpiCheckBox(QIGroupingField.ACCOUNT.name());
        accountBox.setValue(defaultConfig != null && defaultConfig.contains(QIGroupingField.ACCOUNT.name()));
        taxBox = new KpiCheckBox(QIGroupingField.TAX.name());
        taxBox.setValue(defaultConfig != null && defaultConfig.contains(QIGroupingField.TAX.name()));
        departmentBox = new KpiCheckBox(QIGroupingField.DEPARTMENT.name());
        departmentBox.setValue(defaultConfig != null && defaultConfig.contains(QIGroupingField.DEPARTMENT.name()));

        groupingFieldsContainer.add(10, itemBox, priceBox, accountBox, taxBox, departmentBox);
    }

    void initItemsTable() {
        dataProvider = new ListDataProvider<>();
        dataGrid = new KpiDataGrid<>(KEY_PROVIDER);
        dataGrid.setPageSize(PAGE_SIZE);
        dataGrid.removeStyleName("cellBasedWidget-mod cellBasedWidget-attachment box-radius--top cellBasedWidget-mod--static-body cellBasedWidget-mod--cell-not-overflow");
        dataGrid.setEmptyTableWidget(DefaultNoItemsMessage.getNoItemsMessage(WfmStrings.App.get().thereAreNoAnyInOutDataYet(), "", null));
        dataGrid.setHeight(MIN_HEIGHT + "px");
        dataProvider.addDataDisplay(dataGrid);

        if (showOnlyGdnsBox.getValue()) {
            initGdnColumns();
        } else {
            initColumns();
        }

        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        SimplePager pager = new SimplePager(SimplePager.TextLocation.CENTER, pagerResources, false, PAGE_SIZE, true);
        pager.getElement().getStyle().setVerticalAlign(Style.VerticalAlign.MIDDLE);
        pager.setDisplay(dataGrid);
        pager.setStyleName("pg_fingetprintlist_pager right");

        pnlDataGrid.clear();
        pnlDataGrid.add(dataGrid);
        pnlDataGrid.add(pager);
    }

    private void initColumns() {
        int index = 0;

        Header<Boolean> header = new Header(new CheckBoxCell()) {
            @Override
            public Boolean getValue() {
                return Boolean.FALSE;
            }
        };
        header.setUpdater(value -> {
            List<SaleOrderBaseInvoiceItem> list = dataProvider.getList();

            for (SaleOrderBaseInvoiceItem item : list) {
                item.setSelected(value);
            }
            dataProvider.refresh();
        });

        Column<SaleOrderBaseInvoiceItem, Boolean> checkBoxField = new Column<SaleOrderBaseInvoiceItem, Boolean>(new CheckBoxCell()) {
            @Override
            public Boolean getValue(SaleOrderBaseInvoiceItem item) {
                return item.isSelected();
            }
        };
        checkBoxField.setFieldUpdater((idx, item, value) -> {
            item.setSelected(value);
        });
        dataGrid.addColumn(checkBoxField, header);
        dataGrid.setColumnWidth(dataGrid.getColumn(index++), 50, Style.Unit.PX);
        dataGrid.getHeader(0).setHeaderStyleNames("pbinv-cell-header");
        dataGrid.removeColumnStyleName(0, "com-google-gwt-user-cellview-client-DataGrid-Style-dataGridHeader");

        SimpleLinkCell numberLink = new SimpleLinkCell();
        numberLink.setStyleName("txt-elem--ellipsis");
        Column<SaleOrderBaseInvoiceItem, String> numberField = new Column<SaleOrderBaseInvoiceItem, String>(numberLink) {
            @Override
            public String getValue(SaleOrderBaseInvoiceItem item) {
                return item.getNumber();
            }
        };
        numberField.setFieldUpdater((idx, item, value) -> {
            if (SaleOrderBaseInvoiceItem.SALE_QUOTE.equals(item.getType())) {
                SinksContainerFactory.entryPoint.onHistoryChanged(Constants.SALE_QUOTE + "|summary/" + item.getObjectId(), item.getNumber());
            } else {
                SinksContainerFactory.entryPoint.onHistoryChanged(Constants.SALE_ORDER_CODE + "|summary/" + item.getObjectId(), item.getNumber());
            }
        });
        dataGrid.addColumn(numberField, wfmStrings.orderNumber());
        dataGrid.setColumnWidth(dataGrid.getColumn(index++), 300, Style.Unit.PX);
        dataGrid.getHeader(1).setHeaderStyleNames("pbinv-cell-header");
        dataGrid.removeColumnStyleName(1, "com-google-gwt-user-cellview-client-DataGrid-Style-dataGridHeader");

        dataGrid.addColumn(new TextColumn<SaleOrderBaseInvoiceItem>() {
            @Override
            public String getValue(SaleOrderBaseInvoiceItem object) {
                return object.getReference();
            }
        }, wfmStrings.reference());
        dataGrid.setColumnWidth(dataGrid.getColumn(index++), 300, Style.Unit.PX);
        dataGrid.getHeader(1).setHeaderStyleNames("pbinv-cell-header");
        dataGrid.removeColumnStyleName(1, "com-google-gwt-user-cellview-client-DataGrid-Style-dataGridHeader");

        dataGrid.addColumn(new TextColumn<SaleOrderBaseInvoiceItem>() {
            @Override
            public String getValue(SaleOrderBaseInvoiceItem object) {
                return object.getOrderDate() != null ? DateUtils.format(object.getOrderDate().getNonConvertedDate()) : "N/A";
            }
        }, wfmStrings.date());
        dataGrid.setColumnWidth(dataGrid.getColumn(index++), 200, Style.Unit.PX);
        dataGrid.getHeader(2).setHeaderStyleNames("pbinv-cell-header");
        dataGrid.removeColumnStyleName(2, "com-google-gwt-user-cellview-client-DataGrid-Style-dataGridHeader");

        dataGrid.addColumn(new TextColumn<SaleOrderBaseInvoiceItem>() {
            @Override
            public String getValue(SaleOrderBaseInvoiceItem object) {
                return object.getDueDate() != null ? DateUtils.format(object.getDueDate().getNonConvertedDate()) : "N/A";
            }
        }, wfmStrings.dueDate());
        dataGrid.setColumnWidth(dataGrid.getColumn(index++), 200, Style.Unit.PX);
        dataGrid.getHeader(2).setHeaderStyleNames("pbinv-cell-header");
        dataGrid.removeColumnStyleName(2, "com-google-gwt-user-cellview-client-DataGrid-Style-dataGridHeader");
    }

    private void initGdnColumns() {
        int index = 0;

        Header<Boolean> header = new Header(new CheckBoxCell()) {
            @Override
            public Boolean getValue() {
                return Boolean.FALSE;
            }
        };
        header.setUpdater(value -> {
            List<SaleOrderBaseInvoiceItem> list = dataProvider.getList();

            for (SaleOrderBaseInvoiceItem item : list) {
                item.setSelected(value);
            }
            dataProvider.refresh();
        });
        Column<SaleOrderBaseInvoiceItem, Boolean> checkBoxField = new Column<SaleOrderBaseInvoiceItem, Boolean>(new CheckBoxCell()) {
            @Override
            public Boolean getValue(SaleOrderBaseInvoiceItem item) {
                return item.isSelected();
            }
        };
        checkBoxField.setFieldUpdater((idx, item, value) -> {
            item.setSelected(value);
        });
        dataGrid.addColumn(checkBoxField, header);
        dataGrid.setColumnWidth(dataGrid.getColumn(index++), 50, Style.Unit.PX);
        dataGrid.getHeader(0).setHeaderStyleNames("pbinv-cell-header");
        dataGrid.removeColumnStyleName(0, "com-google-gwt-user-cellview-client-DataGrid-Style-dataGridHeader");

        SimpleLinkCell numberLink = new SimpleLinkCell();
        numberLink.setStyleName("txt-elem--ellipsis");
        Column<SaleOrderBaseInvoiceItem, String> numberField = new Column<SaleOrderBaseInvoiceItem, String>(numberLink) {
            @Override
            public String getValue(SaleOrderBaseInvoiceItem item) {
                return item.getNumber();
            }
        };
        numberField.setFieldUpdater((idx, item, value) -> {
            SinksContainerFactory.entryPoint.onHistoryChanged("gdn|summary/" + item.getObjectId(), item.getNumber());
        });
        dataGrid.addColumn(numberField, accountingStrings.gdnNumber());
        dataGrid.setColumnWidth(dataGrid.getColumn(index++), 150, Style.Unit.PX);
        dataGrid.getHeader(1).setHeaderStyleNames("pbinv-cell-header");
        dataGrid.removeColumnStyleName(1, "com-google-gwt-user-cellview-client-DataGrid-Style-dataGridHeader");

        dataGrid.addColumn(new TextColumn<SaleOrderBaseInvoiceItem>() {
            @Override
            public String getValue(SaleOrderBaseInvoiceItem object) {
                return object.getShippingLabel();
            }
        }, accountingStrings.shippingLabel());
        dataGrid.setColumnWidth(dataGrid.getColumn(index++), 300, Style.Unit.PX);
        dataGrid.getHeader(1).setHeaderStyleNames("pbinv-cell-header");
        dataGrid.removeColumnStyleName(1, "com-google-gwt-user-cellview-client-DataGrid-Style-dataGridHeader");

        dataGrid.addColumn(new TextColumn<SaleOrderBaseInvoiceItem>() {
            @Override
            public String getValue(SaleOrderBaseInvoiceItem object) {
                return object.getShipDate() != null ? DateUtils.format(object.getShipDate().getNonConvertedDate()) : "N/A";
            }
        }, WfmStrings.App.get().shipDate());
        dataGrid.setColumnWidth(dataGrid.getColumn(index++), 200, Style.Unit.PX);
        dataGrid.getHeader(2).setHeaderStyleNames("pbinv-cell-header");
        dataGrid.removeColumnStyleName(2, "com-google-gwt-user-cellview-client-DataGrid-Style-dataGridHeader");

        String numberLabel = Property.get(Constants.SALE_QUOTE, accountingStrings.salesOrder()) + " " + WfmStrings.App.get().number();
        String dateLabel = Property.get(Constants.SALE_ORDER_CODE, accountingStrings.salesOrder()) + " " + wfmStrings.date();

        SimpleLinkCell orderNumberLink = new SimpleLinkCell();
        numberLink.setStyleName("txt-elem--ellipsis");
        Column<SaleOrderBaseInvoiceItem, String> orderNumberField = new Column<SaleOrderBaseInvoiceItem, String>(orderNumberLink) {
            @Override
            public String getValue(SaleOrderBaseInvoiceItem item) {
                return item.getReference();
            }
        };
        orderNumberField.setFieldUpdater((idx, item, value) -> {
            SinksContainerFactory.entryPoint.onHistoryChanged(Constants.SALE_ORDER_CODE + "|summary/" + item.getQuoteId(), item.getReference());
        });
        dataGrid.addColumn(orderNumberField, numberLabel);
        dataGrid.setColumnWidth(dataGrid.getColumn(index++), 150, Style.Unit.PX);
        dataGrid.getHeader(1).setHeaderStyleNames("pbinv-cell-header");
        dataGrid.removeColumnStyleName(1, "com-google-gwt-user-cellview-client-DataGrid-Style-dataGridHeader");

        dataGrid.addColumn(new TextColumn<SaleOrderBaseInvoiceItem>() {
            @Override
            public String getValue(SaleOrderBaseInvoiceItem object) {
                return object.getOrderDate() != null ? DateUtils.format(object.getOrderDate().getNonConvertedDate()) : "N/A";
            }
        }, dateLabel);
        dataGrid.setColumnWidth(dataGrid.getColumn(index++), 200, Style.Unit.PX);
        dataGrid.getHeader(2).setHeaderStyleNames("pbinv-cell-header");
        dataGrid.removeColumnStyleName(2, "com-google-gwt-user-cellview-client-DataGrid-Style-dataGridHeader");
    }

    void loadData() {
        if (!validate()) {
            return;
        }

        fp = fp != null ? fp : new ListingFilterParameter();
        fp.setClientId(customerLookUp.getSelectedItemID());
        fp.setType(typeList.getSelectedId());
        fp.setStatusID(statusList.getSelectedId());
        fp.setIsGdn(showOnlyGdnsBox.getValue());
        fp.setRelationID(taxCalcTypeList.getSelectedId());
        fp.setCategoryID(dateTypeList.getSelectedId());
        fp.setStartDateNC(Utils.getStartDateNCForFilter(fromDateBox.getDate()));
        fp.setEndDateNC(Utils.getStartDateNCForFilter(toDateBox.getDate()));
        fp.setCustomFields(customFieldsMap);

        LoadingPanel.loading(true);
        QuoteService.App.get().getConvertingItems(fp, new AsyncCallback<ArrayList<SaleOrderBaseInvoiceItem>>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(ArrayList<SaleOrderBaseInvoiceItem> items) {
                LoadingPanel.loading(false);
                initItemsTable();

                if (items != null && items.size() > 0) {
                    int height = items.size() * 45 + 50;

                    if (height > MAX_HEIGHT) {
                        height = MAX_HEIGHT;
                    }
                    dataGrid.setHeight(height + "px");

                    if (height == MAX_HEIGHT) {
                        dataGrid.getElement().getStyle().setOverflow(Style.Overflow.AUTO);
                    }
                }

                dataProvider.getList().clear();
                dataProvider.getList().addAll(items);
                dataProvider.refresh();
            }
        });
    }

    boolean validate() {
        if (!Validation.validateLookUpRequired(customerLookUp)) {
            return false;
        }
        if (!Validation.validateDate(fromDateBox)) {
            return false;
        }
        if (!Validation.validateDate(toDateBox)) {
            return false;
        }
        if (toDateBox.getDate().compareTo(fromDateBox.getDate()) < 0) {
            Info.warn("To date cannot be less than From date!");
            return false;
        }
        return true;
    }

    public String getObjectType() {
        return soBaseInvoiceGroups.getObjectType();
    }

    public String getInvoiceType() {
        return soBaseInvoiceGroups.getInvoiceType();
    }

    public List<SaleOrderBaseInvoiceItem> getSelectedItems() {
        return dataProvider.getList().stream().filter(item -> item.isSelected()).collect(Collectors.toList());
    }

    public long getSelectedCount() {
        return dataProvider.getList().stream().filter(item -> item.isSelected()).count();
    }

    public Date getFromDate() {
        return fromDateBox.getDate();
    }

    public Date getToDate() {
        return toDateBox.getDate();
    }

    public Integer getClientId() {
        return customerLookUp.getSelectedItemID();
    }

    public HashMap<String, Boolean> getSelectedNameFields() {
        return soBaseInvoiceGroups.getSelectedNameFields();
    }

    public HashMap<String, Boolean> getSelectedDescFields() {
        return soBaseInvoiceGroups.getSelectedDescFields();
    }

    public boolean isGroupedByItem() {
        return groupByItemBox.getValue();
    }

    public ArrayList<QIGroupingField> getGrouppingFields() {
        ArrayList<QIGroupingField> fields = new ArrayList<>();

        if (itemBox.getValue()) {
            fields.add(QIGroupingField.ITEM);
        }
        if (priceBox.getValue()) {
            fields.add(QIGroupingField.PRICE);
        }
        if (accountBox.getValue()) {
            fields.add(QIGroupingField.ACCOUNT);
        }
        if (taxBox.getValue()) {
            fields.add(QIGroupingField.TAX);
        }
        if (departmentBox.getValue()) {
            fields.add(QIGroupingField.DEPARTMENT);
        }
        return fields;
    }

    private void getDefaultTaxCalc() {
        AllInOneService.App.get().getTaxCalcTypeForInvoice(new AsyncCallback<Integer>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(Integer integer) {
                taxCalcTypeList.setSelected(integer);
            }
        });
    }

    private InvoiceAdvancedOptions createAdvancedOptions() {
        return new InvoiceAdvancedOptions(ArrayList::new, false);
    }

    private void getCustomFields() {
        Integer selectedType = typeList.getSelectedId();
        if (!selectedType.equals(BASED_ON.BOTH)) {
            CommonService.App.get().getCompanyCustomFieldsForBaseInvoices(selectedType.equals(BASED_ON.SALE_ORDER) ? ViewName.SaleOrder : ViewName.SaleQuote, new AsyncCallback<ArrayList<CompanyCustomFieldItem>>() {
                @Override
                public void onFailure(Throwable throwable) {

                }

                @Override
                public void onSuccess(ArrayList<CompanyCustomFieldItem> companyCustomFieldItems) {
                    if (selectedType.equals(BASED_ON.SALE_ORDER)) {
                        saleOrderCustomFields = companyCustomFieldItems;
                    } else {
                        saleQuoteCustomFields = companyCustomFieldItems;
                    }
                    initCustomFields();
                }
            });
        }
    }

    private void initCustomFields() {
        Integer selectedType = typeList.getSelectedId();
        if (selectedType.equals(BASED_ON.SALE_ORDER)) {
            advancedOptions.createAndAppendCustomFieldsView(null, saleOrderCustomFields);
            showMoreLink.setVisible(true);
        } else if (selectedType.equals(BASED_ON.SALE_QUOTE)) {
            advancedOptions.createAndAppendCustomFieldsView(null, saleQuoteCustomFields);
            showMoreLink.setVisible(true);
        }
    }

    interface SaleOrderBasedInvoiceUiBinder extends UiBinder<HTMLPanel, SaleOrderBaseInvoice> {
    }

    interface INVOICE_TYPE {
        int DETAILED_INVOICE = 1;
        int GROUPED_BY_OBJECT = 2;
    }

    interface BASED_ON {
        int BOTH = 0;
        int SALE_ORDER = 1;
        int SALE_QUOTE = 2;
    }

    interface STATUS_TYPE {
        int APPROVED = 0;
        int SHIPPED_OR_PARTIAL_SHIPPED = 1;
    }

    interface DATE_TYPE {
        int ORDER_DATE = 0;
        int DUE_DATE = 1;
    }
}