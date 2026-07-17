package com.edatasite.workforce.gwt.accounting.client.ui.view.inventory;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewProduct;
import com.edatasite.workforce.gwt.accounting.client.rpc.RentalProductItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductService;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductServiceAsync;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableConstants;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.ProfileImage;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTable;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableColumn;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButton;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButtonItem;
import com.edatasite.workforce.gwt.core.client.ui.upload.GeneralFileUpload;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceQuoteRequestObject;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import static com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingCustomFormConstants.PURCHASE_ACCOUNT;
import static com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingCustomFormConstants.PURCHASE_PRICE;
import static com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingCustomFormConstants.SALES_ACCOUNT;
import static com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingCustomFormConstants.SALES_PRICE;

public class RentalProductSummaryView extends CustomForm2 implements Constants {
    public static final SelectItem[] RENTAL_UNITS = new SelectItem[]{
//            new SelectItem(1, wfmStrings.minutes(), Constants.TIME_GRANULARITY.MINUTES),
            new SelectItem(2, wfmStrings.hours(), Constants.TIME_GRANULARITY.HOURS),
            new SelectItem(3, wfmStrings.days(), Constants.TIME_GRANULARITY.DAYS),
            new SelectItem(4, wfmStrings.weeks(), TIME_GRANULARITY.WEEKS),
            new SelectItem(5, wfmStrings.months(), TIME_GRANULARITY.MONTHS)
    };
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final ProductServiceAsync productService = ProductService.App.get();
    public BarcodeGenerator barcodeGenerator;
    private NewProduct product;
    private final Integer objectID;
    private LinkedHashMap<String, FormProperty> formPropertyMap;
    private DynamicTable itemsTable;
    private LinkedList<String> itemColumns;
    private SplitButton printPdfSplitButton;
    private FormHasCustomField customFieldUtil;

    public RentalProductSummaryView(Integer objectID) {
        super("summary");
        setDescription(property.getSingular(wfmStrings.summaryView(), accountingStrings.rentalItem()));
        this.objectID = objectID;
    }

    protected Widget onInitialize() {
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.RentalProductsView, getFormID(), new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {
                RentalProductSummaryView.super.onInitialize();
            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                if (result != null) {
                    getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                    formPropertyMap = result.getFormPropertyMap();
                }
                RentalProductSummaryView.super.onInitialize();
            }
        });
        return null;
    }

    @Override
    protected void registerFields() {
        show();
    }

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }

    @Override
    protected void initPredefinedValues() {

    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        productService.getRentalProductEditData(objectID, false, new AsyncCallback<NewProduct>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                GWT.log(throwable.getMessage());
            }

            @Override
            public void onSuccess(NewProduct result) {
                LoadingPanel.loading(false);
                product = result;
                initialize();
                pdfTool(product);
            }
        });
    }

    private void initialize() {

        drawMainSection();
        drawFinanceSection();
        drawRentalItems();
        drawProductAttachments();
        addTitleField(CustomFormConstants.ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());
        getCustomFieldUtil().drawCustomFields(this, objectID, true);

        getCustomFieldUtil().fillCustomFieldsWithData(product.getProductCustomFieldItems(), true);


    }

    private void drawMainSection() {
        HTML productName = new HTML();
        HTML productNumber = new HTML();
        HTML activeProductCheckBox = new HTML();

        productName.setText(product.getItemName());
        productName.getElement().setId("product_name");
        productNumber.setText(product.getNumberData().getNumberString());
        productNumber.getElement().setId("product_number");
        if (product.isActive()) {
            activeProductCheckBox.setText("Yes");
        } else {
            activeProductCheckBox.setText("No");
        }

        HTML categoryField = new HTML();
        categoryField.setText(product.getCategoryName());
        categoryField.getElement().setId("product_category");

        HTML brandField = new HTML();
        brandField.setText(product.getBrandName());

        VerticalPanel pnlSuppliers = new VerticalPanel();
        pnlSuppliers.setSpacing(5);
        pnlSuppliers.add(new HTML("N/A"));

        if (product.getSuppliers() != null && product.getSuppliers().length > 0) {
            pnlSuppliers.clear();
            for (final SelectItem supplier : product.getSuppliers()) {
                if (Utils.hasPermission(PermissionConstants.CLIENT_NAME_CLICKABLE)) {
                    MaterialLink clientLink = new MaterialLink(supplier.getName());
                    clientLink.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged("client|summary/" + supplier.getId(), supplier.getName()));
                    pnlSuppliers.add(clientLink);
                } else {
                    pnlSuppliers.add(new HTML(supplier.getName()));
                }
            }
        }

        barcodeGenerator = new BarcodeGenerator(BARCODE);
        barcodeGenerator.setData(product.getBarCodeText(), product.getQRCodeSizeID());


        addTitleField(CustomFormConstants.PRODUCT_INFORMATION, property.getSingular(wfmStrings.rentalInformation(), accountingStrings.rentalItem()));

        if (formPropertyMap != null && formPropertyMap.get(NAME) != null) {
            addField(NAME, productName, getTitle(formPropertyMap.get(NAME).isChanged() ? formPropertyMap.get(NAME).getTitle() : wfmStrings.name()));
        } else {
            addField(NAME, productName, wfmStrings.name());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NUMBER) != null) {
            addField(CustomFormConstants.NUMBER, productNumber, getTitle(formPropertyMap.get(CustomFormConstants.NUMBER).isChanged() ? formPropertyMap.get(CustomFormConstants.NUMBER).getTitle() : wfmStrings.number()));
        } else {
            addField(CustomFormConstants.NUMBER, productNumber, property.getShortForNumber(wfmStrings.number()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.ACTIVE) != null) {
            addField(CustomFormConstants.ACTIVE, activeProductCheckBox, getTitle(formPropertyMap.get(CustomFormConstants.ACTIVE).isChanged() ? formPropertyMap.get(CustomFormConstants.ACTIVE).getTitle() : wfmStrings.active()));
        } else {
            addField(CustomFormConstants.ACTIVE, activeProductCheckBox, wfmStrings.active());
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CATEGORY) != null) {
            addField(CATEGORY, categoryField, getTitle(formPropertyMap.get(CATEGORY).isChanged() ? formPropertyMap.get(CATEGORY).getTitle() : wfmStrings.category()));
        } else {
            addField(CATEGORY, categoryField, wfmStrings.category());
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.BRAND) != null) {
            addField(BRAND, brandField, getTitle(formPropertyMap.get(BRAND).isChanged() ? formPropertyMap.get(BRAND).getTitle() : wfmStrings.brand()));
        } else {
            addField(BRAND, brandField, wfmStrings.brand());
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SUPPLIERS) != null) {
            addField(CustomFormConstants.SUPPLIERS, pnlSuppliers, getTitle(formPropertyMap.get(CustomFormConstants.SUPPLIERS).isChanged() ? formPropertyMap.get(CustomFormConstants.SUPPLIERS).getTitle() : Property.getPluralWithObjectCode(Constants.SUPPLIER_LIST, wfmStrings.suppliers())));
        } else {
            addField(CustomFormConstants.SUPPLIERS, pnlSuppliers, Property.getPluralWithObjectCode(Constants.SUPPLIER_LIST, wfmStrings.suppliers()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.BARCODE) != null) {
            addField(CustomFormConstants.BARCODE, barcodeGenerator.createWidget(false), getTitle(formPropertyMap.get(CustomFormConstants.BARCODE).isChanged() ? formPropertyMap.get(CustomFormConstants.BARCODE).getTitle() : wfmStrings.barcode()));
        } else {
            addField(CustomFormConstants.BARCODE, barcodeGenerator.createWidget(false), wfmStrings.barcode());
        }
    }

    private void drawFinanceSection() {
        HTML purchasePriceTextBox = new HTML();
        HTML salesPriceTextBox = new HTML();
        HTML purchaseAccountTextBox = new HTML();
        HTML salesAccountTextBox = new HTML();
        HTML taxField = new HTML();

        purchasePriceTextBox.setText(product.getUnitPrice() != null ? AccountingUtils.get().formatUnitPrice(product.getUnitPrice()) : wfmStrings.notAvailable());
        salesPriceTextBox.setText(product.getSellingPrice() != null ? AccountingUtils.get().formatUnitPrice(product.getSellingPrice()) : wfmStrings.notAvailable());
        salesPriceTextBox.getElement().setId("product_sales_price");
        purchaseAccountTextBox.setText(product.getCogsAccount() != null ? product.getCogsAccount().getName() : wfmStrings.notAvailable());
        salesAccountTextBox.setText(product.getAccountItem() != null ? product.getAccountItem().getName() : wfmStrings.notAvailable());
        taxField.setText(product.getTaxItem() != null ? product.getTaxItem().getName() : "");

        addTitleField(FINANCIAL_INFORMATION, wfmStrings.financialInformation());

        if (formPropertyMap != null && formPropertyMap.get(PURCHASE_PRICE) != null) {
            addField(PURCHASE_PRICE, purchasePriceTextBox, getTitle(formPropertyMap.get(PURCHASE_PRICE).isChanged() ? formPropertyMap.get(PURCHASE_PRICE).getTitle() : wfmStrings.purchasePrice()));
        } else {
            addField(PURCHASE_PRICE, purchasePriceTextBox, wfmStrings.purchasePrice());
        }

        if (formPropertyMap != null && formPropertyMap.get(PURCHASE_ACCOUNT) != null) {
            addField(PURCHASE_ACCOUNT, purchaseAccountTextBox, getTitle(formPropertyMap.get(PURCHASE_ACCOUNT).isChanged() ? formPropertyMap.get(PURCHASE_ACCOUNT).getTitle() : wfmStrings.purchaseAccount()));
        } else {
            addField(PURCHASE_ACCOUNT, purchaseAccountTextBox, wfmStrings.purchaseAccount());
        }

        if (formPropertyMap != null && formPropertyMap.get(SALES_PRICE) != null) {
            addField(SALES_PRICE, salesPriceTextBox, getTitle(formPropertyMap.get(SALES_PRICE).isChanged() ? formPropertyMap.get(SALES_PRICE).getTitle() : wfmStrings.sellingPrice()));
        } else {
            addField(SALES_PRICE, salesPriceTextBox, wfmStrings.sellingPrice());
        }

        if (formPropertyMap != null && formPropertyMap.get(SALES_ACCOUNT) != null) {
            addField(SALES_ACCOUNT, salesAccountTextBox, getTitle(formPropertyMap.get(SALES_ACCOUNT).isChanged() ? formPropertyMap.get(SALES_ACCOUNT).getTitle() : wfmStrings.salesAccount()));
        } else {
            addField(SALES_ACCOUNT, salesAccountTextBox, wfmStrings.salesAccount());
        }

        if (formPropertyMap != null && formPropertyMap.get(TAX) != null) {
            addField(TAX, taxField, getTitle(formPropertyMap.get(TAX).isChanged() ? formPropertyMap.get(TAX).getTitle() : wfmStrings.taxRate()));
        } else {
            addField(TAX, taxField, wfmStrings.taxRate());
        }
    }

    private void drawProductAttachments() {
        GeneralFileUpload fileUploadPanel = new GeneralFileUpload(F_PRODUCTS_SERVICES, objectID, objectID);
        ProfileImage fileUploadForm = new ProfileImage(objectID, getFormID());

        addField(ATTACHMENTS, fileUploadPanel, null);
        addField(IMAGE_UPLOAD, fileUploadForm, null);

        CommonService.App.get().getRentalProductImageURL(objectID, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(String s) {
                fileUploadForm.initialize(s, product.getItemName(), null, true);
            }
        });
    }

    private void drawRentalItems() {
        itemsTable = new DynamicTable(getColumns(), false);
        itemsTable.setStyleName("invoice__summery-table");
        addField(CustomFormConstants.ITEMS, itemsTable, null, true);

        if (product.getRentalProductItems() != null && product.getRentalProductItems().size() > 0) {
            for (RentalProductItem rentalProductItem : product.getRentalProductItems()) {
                itemsTable.addRow(getWidgets(rentalProductItem));
            }
        }
        HTML extraDay = new HTML();
        HTML extraHour = new HTML();
        HTML securityTime = new HTML();
        HTML rentalType = new HTML();
        extraDay.setText(product.getExtraDay() != null ? AccountingUtils.get().formatUnitPrice(product.getExtraDay()) : wfmStrings.notAvailable());
        extraHour.setText(product.getExtraHour() != null ? AccountingUtils.get().formatUnitPrice(product.getExtraHour()) : wfmStrings.notAvailable());
        securityTime.setText(product.getSecurityTime() != null ? AccountingUtils.get().formatUnitPrice(product.getSecurityTime()) : wfmStrings.notAvailable());
        rentalType.setHTML("Consumable");

        if (formPropertyMap != null && formPropertyMap.get(OVERTIME_HOURS) != null) {
            addField(OVERTIME_HOURS, extraHour, getTitle(formPropertyMap.get(OVERTIME_HOURS).isChanged() ? formPropertyMap.get(OVERTIME_HOURS).getTitle() : wfmStrings.extraHour()));
        } else {
            addField(OVERTIME_HOURS, extraHour, wfmStrings.extraHour());
        }
        if (formPropertyMap != null && formPropertyMap.get(OVERTIME_END_DATE) != null) {
            addField(OVERTIME_END_DATE, extraDay, getTitle(formPropertyMap.get(OVERTIME_END_DATE).isChanged() ? formPropertyMap.get(OVERTIME_END_DATE).getTitle() : wfmStrings.extraDay()));
        } else {
            addField(OVERTIME_END_DATE, extraDay, wfmStrings.extraDay());
        }

        if (formPropertyMap != null && formPropertyMap.get(SECURITY_TIME) != null) {
            addField(SECURITY_TIME, securityTime, getTitle(formPropertyMap.get(SECURITY_TIME).isChanged() ? formPropertyMap.get(SECURITY_TIME).getTitle() : wfmStrings.securityTime()));
        } else {
            addField(SECURITY_TIME, securityTime, wfmStrings.securityTime());
        }

        if (formPropertyMap != null && formPropertyMap.get(TYPE) != null) {
            addField(TYPE, rentalType, getTitle(formPropertyMap.get(TYPE).isChanged() ? formPropertyMap.get(TYPE).getTitle() : wfmStrings.type()));
        } else {
            addField(TYPE, rentalType, wfmStrings.type());
        }
    }

    private DynamicTableColumn[] getColumns() {
        itemColumns = new LinkedList<>();
        LinkedList<DynamicTableColumn> columnsList = new LinkedList<>();
        if (product != null && product.getCustomItemColumns() != null && product.getCustomItemColumns().length > 0) {
            DynamicTableColumn columnConfig;
            for (ColumnConfigs column : product.getCustomItemColumns()) {
                boolean isPixel = (column.getWidth() == null || column.getWidth() == 0);
                switch (column.getCode()) {
                    case ItemTableConstants.UNITS:
                        columnConfig = new DynamicTableColumn(column.isChanged() ? column.getTitle() : wfmStrings.units(), ItemTableConstants.UNITS, Utils.getColumnWidth(column.getWidth(), 250));
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columnsList.add(columnConfig);
                        itemColumns.add(ItemTableConstants.UNITS);
                        break;
                    case ItemTableConstants.UNITPRICE:
                        columnConfig = new DynamicTableColumn(column.isChanged() ? column.getTitle() : wfmStrings.price(), ItemTableConstants.UNITPRICE, Utils.getColumnWidth(column.getWidth(), 250));
                        columnConfig.setPixel(isPixel);
                        columnsList.add(columnConfig);
                        itemColumns.add(ItemTableConstants.UNITPRICE);
                        break;
                    case ItemTableConstants.DESCRIPTION:
                        columnConfig = new DynamicTableColumn(column.isChanged() ? column.getTitle() : wfmStrings.description(), ItemTableConstants.DESCRIPTION, Utils.getColumnWidth(column.getWidth(), 250));
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columnsList.add(columnConfig);
                        itemColumns.add(ItemTableConstants.DESCRIPTION);
                        break;
                }
            }
        } else {
            columnsList.add(new DynamicTableColumn(wfmStrings.units(), ItemTableConstants.UNITS, 150));
            itemColumns.add(ItemTableConstants.UNITS);

            columnsList.add(new DynamicTableColumn(wfmStrings.price(), ItemTableConstants.UNITPRICE, 150));
            itemColumns.add(ItemTableConstants.UNITPRICE);

            columnsList.add(new DynamicTableColumn(wfmStrings.description(), ItemTableConstants.DESCRIPTION, 250));
            itemColumns.add(ItemTableConstants.DESCRIPTION);
        }
        return columnsList.toArray(new DynamicTableColumn[]{});
    }

    private Widget[] getWidgets(final RentalProductItem data) {

        Label units = new Label();

        Label price = new Label();

        Label description = new Label();


        description.setText(data.getDescription());

        if (data.getUnitCode() != null) {
            units.setText(unitName(data.getUnitCode()));
        }
        if (data.getPrice() != null) {
            price.setText(AccountingUtils.get().formatPrice(data.getPrice()));
        }

        return new Widget[]{units, price, description};
    }

    private String unitName(String unitCode) {
        for (SelectItem unit : RENTAL_UNITS) {
            if (unitCode.equals(unit.getDescription()))
                return unit.getName();
        }
        return "";
    }


    @Override
    protected void addButtons() {

        if (Utils.hasPermission(ACCOUNTING_RENTAL_EDIT)) {
            WfmButton2 editButton = new WfmButton2(wfmStrings.edit(), BTN_PRIMARY);
            editButton.addClickHandler(clickEvent -> {
                closeTab();
                SinksContainerFactory.entryPoint.onHistoryChanged("product-rental|add/add/" + product.getObjectId(), product.getNumberData().getNumberString(), product.getItemName());
            });
            addButton(editButton);
        }

        printPdfSplitButton = new SplitButton(100, WfmButton2.BTN_WHITE_OUTLINE);
        addRightButton(printPdfSplitButton);
    }

    public void pdfTool(NewProduct result) {
        if (printPdfSplitButton == null) {
            return;
        }
        List<SplitButtonItem> pdfTemplatesList = new ArrayList<>();
        Integer defaultTemplateId = null;
        if (result != null && result.getTemplates() != null && result.getTemplates().length > 0) {
            for (SelectItem pdfItem : result.getTemplates()) {
                if (pdfItem.isDefaultSelected()) {
                    defaultTemplateId = pdfItem.getId();
                }

                pdfTemplatesList.add(new SplitButtonItem("PDF_TEMPLATE_" + pdfItem.getId(), pdfItem.getName(), () -> generatePDF(panel, pdfItem.getId(), false)));
            }
        } else {
            pdfTemplatesList.add(new SplitButtonItem("PDF_TEMPLATE_", wfmStrings.landscape(), () -> generatePDF(panel, null, true)));
        }
        Integer finalDefaultTemplateId = defaultTemplateId;

        SplitButtonItem pdfVersion = new SplitButtonItem(PDF_VERSION, wfmStrings.pdfVersion(), () -> generatePDF(panel, finalDefaultTemplateId, false), true);
        pdfTemplatesList.add(pdfVersion);
        printPdfSplitButton.addItemList(pdfTemplatesList);
    }

    private void generatePDF(HTMLPanel panel, Integer templateID, boolean landscape) {
        InvoiceQuoteRequestObject requestObject = new InvoiceQuoteRequestObject(objectID);
        HashMap<String, String> parameters = requestObject.getRequestParams();

        if (templateID != null) {
            parameters.put("templateID", String.valueOf(templateID));
        }
        if (landscape) {
            parameters.put("IS_LANDSCAPE", "true");
        }
        String pdfURL = CommandConstants.PDF_URL + "/productRentalViewPdfHandler";
        Utils.sendPDFOrExcelRequest(panel, pdfURL, parameters, "_blank");
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.RENTAL_PRODUCT_FORM;
    }

    @Override
    protected String getFormType() {
        return null;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    public String getIconStyle() {
        return "accountMark purchase-order-list";
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
        return Constants.RENTAL_PRODUCTS;
    }
}