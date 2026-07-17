package com.edatasite.workforce.gwt.invoice.client.ui.view.shippingData.form;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductSelectItem;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.FooteredView;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.WftHTMLPanel;
import com.edatasite.workforce.gwt.core.client.enums.ShippingDataStatus;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.LeaveRequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.PdfTemplateTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTable;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableColumn;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTextBox;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButton;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButtonItem;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.FooterInformer;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.IFooteredView;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.ViewFooter;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceFormFields;
import com.edatasite.workforce.gwt.invoice.client.rpc.ShippingData;
import com.edatasite.workforce.gwt.invoice.client.rpc.ShippingDataItem;
import com.edatasite.workforce.gwt.invoice.client.ui.view.itemTrackBatches.ItemAddTrackBatchPopup;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.ProductsTable;
import com.edatasite.workforce.gwt.invoice.client.ui.view.purchaseorder.AllocationTextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants.INVENTORY_ITEM;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.BTN_DEFAULT_OUTLINE;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_JOURNAL_REPORT;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.CUSTOMER_CLICKABLE;
import static com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingCustomFormConstants.INPUT_CRM_ACCOUNT;
import static com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingCustomFormConstants.INPUT_CURRENCY;
import static com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingCustomFormConstants.INPUT_CUSTOMER;
import static com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingCustomFormConstants.INPUT_DATE;
import static com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingCustomFormConstants.INPUT_ITEM_TABLE;
import static com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingCustomFormConstants.INPUT_NUMBER;
import static com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingCustomFormConstants.INPUT_PDF_TEMPLATE;
import static com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingCustomFormConstants.INPUT_SALE_ORDER;
import static com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingCustomFormConstants.INPUT_SHIPPING_LABEL;
import static com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingCustomFormConstants.PDF_VERSION;

public abstract class AbstractGdnGrnSummaryView extends FooteredView implements FittedContent {
    protected static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    protected static final WfmStrings wfmStrings = WfmStrings.App.get();
    protected DataListBox templates;
    protected DynamicTable itemTable;
    protected ShippingData shippingData;
    protected String viewType;
    protected boolean isGrn;
    protected WfmButton2 convertToInvoice, excel, delete;
    protected HashMap<String, Widget> widgetsMap;
    protected HTMLPanel htmlPanel;
    List<Widget> rightWidgets = new ArrayList<>();
    private SplitButton printPdfSplitButton;
    protected static final AccountingUtils utils = AccountingUtils.get();


    public AbstractGdnGrnSummaryView(String name, String viewType, boolean isGrn) {
        super(name, viewType);
        this.isGrn = isGrn;
    }

    public AbstractGdnGrnSummaryView() {

    }

    protected Widget onInitialize() {

        delete = new WfmButton2(wfmStrings.delete(), BTN_DEFAULT_OUTLINE);

        printPdfSplitButton = new SplitButton(100, WfmButton2.BTN_WHITE_OUTLINE);

        convertToInvoice = new WfmButton2(Property.getShortName(Constants.SALE_INVOICE, wfmStrings.convert(), accountingStrings.invoice()), WfmButton2.BTN_PRIMARY);

        excel = new WfmButton2(wfmStrings.excel(), BTN_DEFAULT_OUTLINE);

        if (isGrn) {
            excel.setVisible(false);
            printPdfSplitButton.setVisible(Utils.hasPermission(PermissionConstants.ACCOUNTING_PURCHASE_ORDER_GRN_EXPORT));
        } else {
            excel.setVisible(Utils.hasPermission(PermissionConstants.ACCOUNTING_GDN_EXPORT));
            printPdfSplitButton.setVisible(Utils.hasPermission(PermissionConstants.ACCOUNTING_GDN_EXPORT));
        }


        widgetsMap = new HashMap<>();

        initializeData();

        return null;
    }

    protected abstract void initializeData();

    protected abstract String pdfUrl();

    protected abstract String excelUrl();

    protected abstract String convertToInvoiceLink();

    protected abstract void deleteGdnGrn(Integer id);


    public void setData(ShippingData shippingData) {

        this.shippingData = shippingData;

        registerEventHandlers(shippingData);

        itemTable = new DynamicTable(getColumns(), false);

        Widget orderNumber = null;
        FormGroup orderNumberField = null;
        if (isGrn) {
            orderNumber = new SimpleLink(shippingData.getOrderNumber(), "purchaseorder|summary/" + shippingData.getQuoteId());
            orderNumberField = new FormGroup(orderNumber);
            orderNumberField.ensureDebugId(Constants.PURCHASE_ORDER);
            orderNumberField.getGroupContent().addStyleName("form-control");
            Div purchaseOrderFieldLabel = orderNumberField.getGroupLabel();
            purchaseOrderFieldLabel.addStyleName("label-group");
            purchaseOrderFieldLabel.add(new Span(Property.get(Constants.PURCHASE_ORDER, wfmStrings.orderNumber())));
        } else {
            orderNumber = new SimpleLink(shippingData.getOrderNumber(), "saleorder|summary/" + shippingData.getQuoteId());
            orderNumberField = new FormGroup(orderNumber);
            orderNumberField.ensureDebugId(Constants.SALE_ORDER);
            orderNumberField.getGroupContent().addStyleName("form-control");
            Div orderFieldLabel = orderNumberField.getGroupLabel();
            orderFieldLabel.addStyleName("label-group");
            orderFieldLabel.add(new Span(Property.get(Constants.SALE_ORDER, wfmStrings.orderNumber())));
        }
        widgetsMap.put(INPUT_SALE_ORDER, orderNumberField);

        FormGroup numberField = new FormGroup(wfmStrings.number(), getWidgetAsFormControl(shippingData.getNumber()));
        widgetsMap.put(INPUT_NUMBER, numberField);

        FormGroup shippingLabelField = new FormGroup(accountingStrings.shippingLabel(), getWidgetAsFormControl(shippingData.getShippingLabel()));
        widgetsMap.put(INPUT_SHIPPING_LABEL, shippingLabelField);

        FormGroup customerField;
        String clientName = shippingData.getClientName();
        CrmAccountItem customer = shippingData.getCustomer();
        Double customerBalance = customer.getClientBalance();

        if (isGrn) {
            customerField = new FormGroup(
                    Property.get(Constants.SUPPLIER_LIST, wfmStrings.supplier()),
                    getWidgetAsFormControl(clientName)
            );
            widgetsMap.put(INPUT_CUSTOMER, customerField);
        } else {
            Widget customerName;

            if (Utils.hasPermission(CUSTOMER_CLICKABLE)) {
                customerName = new SimpleLink(clientName, "client|summary/" + customer.getObjectId(), clientName, customer.getNumber());
            } else {
                customerName = new HTML(clientName);
            }

            FormGroup clientField = new FormGroup(customerName);
            clientField.ensureDebugId(InvoiceFormFields.CUSTOMER);
            clientField.getGroupContent().addStyleName("form-control");

            Div clientFieldLabel = clientField.getGroupLabel();
            clientFieldLabel.addStyleName("label-group");
            clientFieldLabel.add(new Span(Property.get(Constants.CLIENT_LIST, wfmStrings.customer())));

            if (Utils.hasPermission(CUSTOMER_CLICKABLE)) {
                Span balance = new Span(wfmStrings.balance() + ": ");

                 balance.add(new SimpleLink(customerBalance >= 0
                         ? utils.formatPrice(customerBalance)
                         : "(" + utils.formatPrice((-1) * customerBalance) + ")",
                     "customerBalance|customerBalance/" +customer.getObjectId() + "/" + CrmAccountItem.CUSTOMER,
                     "", wfmStrings.balance() + ":" + clientName
                 ));

                clientFieldLabel.add(balance);
            } else {
                Span balance = new Span(wfmStrings.balance() + ": " + (customerBalance >= 0 ? utils.formatPrice(customerBalance) : "(" + utils.formatPrice((-1) * customerBalance) + ")"));
                clientFieldLabel.add(balance);
            }
            widgetsMap.put(INPUT_CUSTOMER, clientField);
        }

        FormGroup currencyField = new FormGroup(wfmStrings.currency(), getWidgetAsFormControl(shippingData.getCurrencyName()));
        widgetsMap.put(INPUT_CURRENCY, currencyField);

        FormGroup dateField = new FormGroup(wfmStrings.date(), getWidgetAsFormControl(DateUtils.format(shippingData.getShippingDate())));
        widgetsMap.put(INPUT_DATE, dateField);

        FormGroup creatorField = new FormGroup(wfmStrings.createdBy(), getWidgetAsFormControl(shippingData.getCreatorName()));
        widgetsMap.put(INPUT_CRM_ACCOUNT, creatorField);

        if (shippingData.getTemplates() != null && shippingData.getTemplates().length > 0) {
            templates = new DataListBox();
            templates.setItems(shippingData.getTemplates());
            if (shippingData.getSelectedTemplateId() != null) {
                templates.setSelected(shippingData.getSelectedTemplateId());
            }
            FormGroup templateField = new FormGroup(accountingStrings.choosePdfTemplate(), templates);
            widgetsMap.put(INPUT_PDF_TEMPLATE, templateField);
        }
        final Function<String, EditableTextBox> binaryFunc = (value) -> {
            final EditableTextBox valueBox = new EditableTextBox();

            valueBox.setEnabled(false);
            valueBox.setValue(Optional.ofNullable(value).orElse(""));
            return valueBox;
        };

        for (ShippingDataItem item : shippingData.getItems()) {
            Label product = new Label();
            Label article = new Label();
            Label warehouse = new Label();
            Label recieveType = new Label();
            Label netAmount = new Label();
            TextBox quantity = new TextBox();
            quantity.getElement().setAttribute("style", "cursor: pointer; color: #8299ad !important;");

            AllocationTextBox allocate = new AllocationTextBox(item.getReceivedAllocation());

            product.setWordWrap(true);
            product.getElement().getStyle().setProperty("whiteSpace", "normal");
            product.getElement().getStyle().setProperty("overflowWrap", "break-word");
            product.getElement().getStyle().setProperty("cursor", "pointer");
            product.setText(item.getItem() != null ? item.getItem().getName() : "");
            if (((ProductSelectItem) item.getItem()).isActive()) {
                product.addClickHandler(event -> {
                    String trackLink = "";
                    if (INVENTORY_ITEM.equals(((ProductSelectItem) item.getItem()).getProductType())) {
                        if (((ProductSelectItem) item.getItem()).getInventoryTrackingEnabled()) {
                            trackLink = ((ProductSelectItem) item.getItem()).getInventoryTrackingEnabled() ? "/INVENTORY_TRACKING" : "";
                        } else if (((ProductSelectItem) item.getItem()).getBatchTrackingEnabled()) {
                            trackLink = item.getTrackBatchesEnabled() ? "/BATCH_TRACKING" : "";
                        }
                    }
                    SinksContainerFactory.entryPoint.onHistoryChanged("product|summary/" + item.getItem().getId() + "/" + ((ProductSelectItem) item.getItem()).getProductType() + trackLink
                            , item.getItem().getNumber(), item.getItem().getName());
                });
            }

            warehouse.setText(item.getWarehouse() != null ? item.getWarehouse().getName() : "");
            recieveType.setText(item.getReceiveType().getTitle());
            netAmount.setText(AccountingUtils.get().formatPrice(Optional.ofNullable(item.getNet()).orElse(BigDecimal.ZERO)));

            quantity.setText(AccountingUtils.get().formatPrice(Optional.ofNullable(item.getAmount()).orElse(BigDecimal.ZERO)));
            quantity.addMouseOverHandler(event -> {
                quantity.getElement().setAttribute("style", "cursor: pointer; color: #1071e3 !important;");
            });
            quantity.addMouseOutHandler(event -> {
                quantity.getElement().setAttribute("style", "cursor: pointer; color: #8299ad !important;");
            });
            quantity.addClickHandler(event -> {
                String trackLink = "";
                if (INVENTORY_ITEM.equals(((ProductSelectItem) item.getItem()).getProductType())) {
                    if (((ProductSelectItem) item.getItem()).getInventoryTrackingEnabled()) {
                        trackLink = ((ProductSelectItem) item.getItem()).getInventoryTrackingEnabled() ? "/INVENTORY_TRACKING" : "";
                    } else if (((ProductSelectItem) item.getItem()).getBatchTrackingEnabled()) {
                        trackLink = item.getTrackBatchesEnabled() ? "/BATCH_TRACKING" : "";
                    }
                }
                SinksContainerFactory.entryPoint.onHistoryChanged("stockvaluation|summary/" + item.getItem().getId() + "/" + ((ProductSelectItem) item.getItem()).getProductType() + trackLink +
                                "/" + item.getWarehouse().getId() + "/" + shippingData.getId()
                        , item.getItem().getNumber(), item.getItem().getName());
            });
            ItemAddTrackBatchPopup viewTrackBatchPopup = new ItemAddTrackBatchPopup(item.getItem() != null ? item.getItem().getId() : null, quantity, true);
            ItemAddTrackBatchPopup.Link viewTrackBatchLink = viewTrackBatchPopup.getLink();
            viewTrackBatchLink.setVisible(false);
            Div qtyPanel = new Div();
            qtyPanel.addStyleName("input-group input-group--plus-off");
            qtyPanel.add(quantity);
            Div divAssign = new Div();
            divAssign.add(viewTrackBatchLink);
            qtyPanel.add(divAssign);
            if (item.getBatchItems() != null && item.getBatchItems().size() > 0) {
                viewTrackBatchPopup.setTrackBatchItems(item.getBatchItems());
                viewTrackBatchLink.setProductName(item.getItem() != null ? item.getItem().getName() : "");
                viewTrackBatchLink.setProductId(item.getItem() != null ? item.getItem().getId() : null);
                viewTrackBatchLink.setVisible(true);
                qtyPanel.removeStyleName("input-group--plus-off");
                qtyPanel.addStyleName("input-group--plus-on");
            }

            LinkedHashMap<String, Widget> itemWidgetsMap = new LinkedHashMap<>();
            itemWidgetsMap.put("product", product);
            itemWidgetsMap.put("warehouse", warehouse);
            if (isGrn) {
                itemWidgetsMap.put("receiveType", recieveType);
            }
            itemWidgetsMap.put("net", netAmount);
            if (isGrn) {
                itemWidgetsMap.put(ProductsTable.ALLOCATION, allocate);
            }
            itemWidgetsMap.put(ProductsTable.RECEIVED_QTY, qtyPanel);

            itemTable.addRow(item.getItem().getId(), itemWidgetsMap.values().toArray(new Widget[]{}));
        }
        widgetsMap.put(INPUT_ITEM_TABLE, itemTable);

        htmlPanel = new WftHTMLPanel(shippingData.getLayoutHtml(), widgetsMap).getContainer();
        htmlPanel.setStyleName("add-form");
        htmlPanel.add(createFooter());
        add(htmlPanel);
    }

    private void registerEventHandlers(ShippingData shippingData) {

        if (isGrn && Utils.hasPermission(PermissionConstants.ACCOUNTING_GRN_DELETE) || !isGrn && Utils.hasPermission(PermissionConstants.ACCOUNTING_GDN_DELETE)) {
            Div deleteWrapper = new Div();
            deleteWrapper.add(delete);
            delete.addClickHandler((clickEvent) -> {
                final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);

                messageBox.setTitle(wfmStrings.confirmation());
                messageBox.setMessage(wfmStrings.areYouSureWantToDeleteThe() + " " + shippingData.getNumber() + "?");
                messageBox.addCloseHandler(new CloseHandler() {
                    @Override
                    public void onSubmit() {
                        deleteGdnGrn(shippingData.getId());
                    }
                });
                messageBox.open();
            });
            rightWidgets.add(deleteWrapper);
        }

        initPdfButton(shippingData);
        Div pdfwrapper = new Div();
        pdfwrapper.add(printPdfSplitButton);

        rightWidgets.add(pdfwrapper);

        excel.addClickHandler(sender -> {
            RequestObject requestObject = new RequestObject(shippingData.getId());
            HashMap<String, String> parametrs = requestObject.getRequestParams();
            Utils.sendPDFOrExcelRequest(htmlPanel, excelUrl(), parametrs, "_blank");
        });

        rightWidgets.add(excel);

        if (isGrn && Utils.hasPermission(PermissionConstants.ACCOUNTING_GRN_CONVERT_TO_INVOICE) || !isGrn && Utils.hasPermission(PermissionConstants.ACCOUNTING_GDN_CONVERT_TO_INVOICE)) {
            Div convertToInvoiceWrap = new Div();
            convertToInvoiceWrap.add(convertToInvoice);
            convertToInvoice.addClickHandler(clickEvent -> {
                closeTab();
                SinksContainerFactory.entryPoint.onHistoryChanged(convertToInvoiceLink() + shippingData.getId());

            });
            if (shippingData.getStatus() == null || (shippingData.getStatus() != null && shippingData.getStatus() != ShippingDataStatus.CONVERTED)) {
                rightWidgets.add(convertToInvoiceWrap);
            }
        }
    }

    private void initPdfButton(ShippingData shippingData) {
        List<SplitButtonItem> pdfCommandSubItems = new ArrayList<>();
        Integer defaultTemplateId = null;
        if (shippingData.getTemplates() != null) {
            shippingData.getTemplates();
            for (SelectItem pdfItem : shippingData.getTemplates()) {
                if (pdfItem.isDefaultSelected()) {
                    defaultTemplateId = pdfItem.getId();
                }
                pdfCommandSubItems.add(new SplitButtonItem("PDF_TEMPLATE_" + pdfItem.getId(),
                        pdfItem.getName(),
                        () -> generatePDF(shippingData.getId(), pdfItem.getId())));
            }
        }
        Integer finalDefaultTemplateId = defaultTemplateId;
        SplitButtonItem pdfVersion = new SplitButtonItem(PDF_VERSION,
                wfmStrings.pdfVersion(),
                () -> generatePDF(shippingData.getId(), finalDefaultTemplateId),
                true);
        pdfVersion.ensureDebugId("pdfVersionItem");
        pdfCommandSubItems.add(pdfVersion);

        if (Utils.hasRoles(Constants.ADMIN)) {
            pdfCommandSubItems.add(new SplitButtonItem("PDF_CUSTOMIZATION", wfmStrings.customize(), () -> {
                String pdfType = "";
                if (isGrn) {
                    pdfType = PdfTemplateTypeEnum.GOODS_RECEIVED_NOTES.name();
                } else {
                    pdfType = PdfTemplateTypeEnum.GOODS_DELIVERED_NOTES.name();
                }
                Utils.openURL(GWT.getHostPageBaseURL() + "Settings.html#pdftemplate|summary/null/" + pdfType);
            }));
        }
        printPdfSplitButton.addItemList(pdfCommandSubItems);
    }

    protected DynamicTableColumn[] getColumns() {
        ArrayList<DynamicTableColumn> headers = new ArrayList<>();
        headers.add(new DynamicTableColumn(wfmStrings.itemName(), "product", isGrn ? 350 : 380, false));
        headers.add(new DynamicTableColumn(accountingStrings.warehouse(), "warehouse", isGrn ?  160 : 175 , false));
        if (isGrn) {
            headers.add(new DynamicTableColumn(accountingStrings.receiveType(), "receiveType", 160, false));
        }
        headers.add(new DynamicTableColumn(wfmStrings.netAmount(), "net", isGrn ? 140 : 175, false));
        if (isGrn) {
            headers.add(new DynamicTableColumn(accountingStrings.allocate(), ProductsTable.ALLOCATION, 100, false));
        }
        headers.add(new DynamicTableColumn(!isGrn ? accountingStrings.shipped() : accountingStrings.received(), ProductsTable.RECEIVED_QTY, isGrn ? 100 : 70, false));
        return headers.toArray(new DynamicTableColumn[]{});
    }

    public ViewFooter createFooter() {
        return new ViewFooter(new IFooteredView() {
            @Override
            public List<Widget> getFooterLeftSideWidgets() {
                return AbstractGdnGrnSummaryView.this.getFooterLeftSideWidgets();
            }

            @Override
            public List<Widget> getFooterRightSideWidgets() {
                return AbstractGdnGrnSummaryView.this.getFooterRightSideWidgets();
            }
        });
    }

    public List<Widget> getFooterLeftSideWidgets() {
        List<Widget> leftSideWidgets = new ArrayList<>();

        if (shippingData != null && shippingData.getJournalId() != null && Utils.hasPermission(ACCOUNTING_JOURNAL_REPORT)) {
            FooterInformer showJournal = new FooterInformer(SvgEnum.wallet, wfmStrings.showJournal(), null);
            showJournal.addClickHandler(clickEvent -> {
                SinksContainerFactory.entryPoint.onHistoryChanged("clickedreport|journalReport/" + shippingData.getJournalId(), accountingStrings.reportView() + ": " + shippingData.getNumber(), accountingStrings.reportView() + ": " + shippingData.getNumber());
            });
            showJournal.setBadgeCount(1);

            leftSideWidgets.add(showJournal);
        }
        return leftSideWidgets;
    }

    protected List<Widget> getFooterRightSideWidgets() {
        return rightWidgets;
    }

    private void generatePDF(Integer objectId, Integer pdfTemplateId) {
        LeaveRequestObject requestObject = new LeaveRequestObject(objectId, null, pdfTemplateId);
        HashMap<String, String> parametrs = requestObject.getRequestParams();
        Utils.sendPDFOrExcelRequest(htmlPanel, pdfUrl(), parametrs, "_blank");
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    public void asyncOnInitialize(AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}
