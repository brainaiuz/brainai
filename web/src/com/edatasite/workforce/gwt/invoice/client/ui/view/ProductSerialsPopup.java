package com.edatasite.workforce.gwt.invoice.client.ui.view;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewProduct;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductSelectItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductService;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductServiceAsync;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.client.ui.view.ProductLookUp;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTable;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableItem;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LookUpCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.listeners.EditableTableListener;
import com.edatasite.workforce.gwt.core.client.ui.lookup.ProjectLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.CustomCellTextBox;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoiceItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.ProductSerialItem;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.ItemQtyPanel;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.ProductsTable;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialPanel;
import org.gwt.advanced.client.ui.widget.EditableGrid;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by Dilshod Madrahimov on 7/23/15 2:59 PM
 */
public class ProductSerialsPopup extends KpiModal {

    public static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    public static final String IMPORT = "IMPORT";
    private static final String ID = "ID";
    private static final String PRODUCT_ID = "PRODUCT_ID";
    private static final String PRODUCT_NUMBER = "PRODUCT_NUMBER";
    private static final String PRODUCT_NAME = "PRODUCT_NAME";
    private static final String ARTICLE_NUMBER = "ARTICLE_NUMBER";
    private static final String QTY = "QTY";
    private static final String SERIAL_NUMBER = "SERIAL_NUMBER";
    private static final String LOT_NUMBER = "LOT_NUMBER";
    private static final String REF_NUMBER = "REF_NUMBER";
    private static final String EXPIRATION_DATE = "EXPIRATION_DATE";
    protected String viewType;
    private NewInvoice invoiceData;
    private EditableTable serialTable;
    private ProjectLookUp relatedProjectLookUp;
    private ProductsTable relatedProductsTable;
    private DynamicTable dynamicTable;
    private NewProduct product;
    private LinkedHashMap<String, Widget> widgetMap;
    private ArrayList<ProductSerialItem> serialsList;
    boolean isAlmadarSerials = Utils.hasGenericAccess(GenericSettingsEnum.ALMADAR_PRODUCT_SERIAL_ENABLED);
    private static final ProductServiceAsync productService = ProductService.App.get();

    public ProductSerialsPopup() {

    }

    public ProductSerialsPopup(NewProduct product, String viewType) {
        this.product = product;
        this.viewType = viewType;
        initialize();
    }

    public ProductSerialsPopup(NewInvoice invoiceData, String viewType) {
        this.invoiceData = invoiceData;
        this.viewType = viewType;
        initialize();
    }

    public ProductSerialsPopup(ProjectLookUp relatedProjectLookUp, ProductsTable relatedProductsTable, String viewType) {
        this.viewType = viewType;
        this.relatedProjectLookUp = relatedProjectLookUp;
        this.relatedProductsTable = relatedProductsTable;
        initialize();
    }

    public ProductSerialsPopup(ProjectLookUp relatedProjectLookUp, DynamicTable dynamicTable, String viewType) {
        this.viewType = viewType;
        this.relatedProjectLookUp = relatedProjectLookUp;
        this.dynamicTable = dynamicTable;
        initialize();
    }

    private void initialize() {
        setTitle("Assign Serial Numbers");
        setScrollable(true);

        if (Constants.PURCHASE_ORDER.equals(viewType)) {
            initPurchaseOrderForm();
        } else if (Constants.SHIPPING_DATA.equals(viewType)) {
            initGDNForm();
        } else if (Constants.SALE_INVOICE.equals(viewType)) {
            initSaleInvoiceForm();
        } else {
            initProductForm();
        }

        serialTable.setListener(new EditableTableListener() {
            @Override
            public void addRow() {
            }

            @Override
            public void removeRow() {
            }
        });
        getContent().add(serialTable);
        WfmButton2 okBtn = new WfmButton2(wfmStrings.ok());
        WfmButton2 cancelBtn = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT);

        okBtn.addClickHandler(clickEvent -> {
            if (!validateAndSetAssignedSerials()) {
                return;
            }
            close();
        });
        cancelBtn.addClickHandler(clickEvent -> close());
        addButton(cancelBtn);
        addButton(okBtn);
        open();
    }

    private void initGDNForm() {
        serialTable = new EditableTable(getColumns());

        ArrayList<Object> objects;
        widgetMap = new LinkedHashMap<>();
        int row = 0;
        for (int i = 0; i < dynamicTable.getRowNumber(); i++) {
            DynamicTableItem tableItem = dynamicTable.getItem(i);
            Label productWidget = (Label) tableItem.getColumnById("product");
            TextBox shipping = (TextBox) ((MaterialPanel) tableItem.getColumnById("shipping")).getWidget(0);

            String productText = productWidget.getText();
            Integer productId = Integer.valueOf(productWidget.getStylePrimaryName());

            Label description = (Label) tableItem.getColumnById("description");
            Integer productType = 1;
            if (description.getStylePrimaryName() != null && !"".equals(description.getStylePrimaryName())) {
                productType = Integer.valueOf(description.getStylePrimaryName());
            }
            ProductLookUp productLookUp = new ProductLookUp(Constants.RECEIVABLE);
            productLookUp.addProductItem(new SelectItem(productId, productText));
            productLookUp.setSelected(productId, productText);
            BigDecimal shipped = AccountingUtils.get().parseToBigDecimal(getQuantityReceived(shipping.getText()));
            if (productId != null && isIntegerQty(shipped) && productLookUp.getSelectedItem() != null) {
                if (AccountingConstants.INVENTORY_ITEM.equals(productType)) {
                    if (isAlmadarSerials) {
                        int k = 0;
                        k++;
                        objects = new ArrayList<>(6);

                        CustomCellLabel idCell = new CustomCellLabel();

                        CustomCellLabel productIdCell = new CustomCellLabel();
                        productIdCell.setText(productLookUp.getSelectedItem().getId() + "");

                        CustomCellLabel productNameCell = new CustomCellLabel();
                        productNameCell.setText(productLookUp.getSelectedItem().getName());

                        CustomCellLabel productNumberCell = new CustomCellLabel();
                        productNumberCell.setText(productLookUp.getSelectedItem().getName().split("->")[0]);

                        final ProductSerialsLookUp serialsLookUp = new ProductSerialsLookUp(productLookUp.getSelectedItemID(), null, viewType);
                        serialsLookUp.getSuggestBox().setWidth("140px");

                        final CustomCellLabel lotNumber = new CustomCellLabel();
                        final CustomCellLabel refNumber = new CustomCellLabel();
                        final CustomCellLabel qty = new CustomCellLabel();
                        final int finalK = row;
                        serialsLookUp.getSuggestBox().addSelectionHandler(selectionEvent -> {
                            serialTable.getColumnCellWidgetById(finalK, SERIAL_NUMBER).setStyleName("");
                            if (serialsLookUp.getSelectedData() != null) {
                                ProductSerialItem selectedSerial = serialsLookUp.getSelectedData();
                                validateSerialsAndItemQty(productLookUp.getSelectedItemID(), selectedSerial, lotNumber, refNumber, finalK, shipped.intValue());
                            }
                        });
                        if (productLookUp.getAssignedSerials() != null && k < productLookUp.getAssignedSerials().length && productLookUp.getAssignedSerials()[k] != null) {
                            serialsLookUp.addProductSerialItem(productLookUp.getAssignedSerials()[k]);
                            idCell.setText(productLookUp.getAssignedSerials()[k].getObjectID() + "");
                            if (productLookUp.getAssignedSerials()[k].getLotNumber() != null) {
                                lotNumber.setText(productLookUp.getAssignedSerials()[k].getLotNumber());
                            }
                            if (productLookUp.getAssignedSerials()[k].getRefNumber() != null) {
                                refNumber.setText(productLookUp.getAssignedSerials()[k].getRefNumber());
                            }
                        }
                        qty.setText(Utils.getCalculationNumberFormat().format(shipped));

                        objects.add(idCell);
                        objects.add(productIdCell);
                        objects.add(productNameCell);
                        objects.add(productNumberCell);
                        objects.add(serialsLookUp);
                        objects.add(lotNumber);
                        objects.add(refNumber);
                        objects.add(qty);

                        widgetMap.put(productNumberCell.getText(), productLookUp);
                        serialTable.addRow(objects.toArray(new Object[objects.size()]));
                        row++;
                    } else {
                        for (int k = 0; k < shipped.intValue(); k++) {
                            objects = new ArrayList<>(5);

                            CustomCellLabel idCell = new CustomCellLabel();

                            CustomCellLabel productIdCell = new CustomCellLabel();
                            productIdCell.setText(productLookUp.getSelectedItem().getId() + "");

                            CustomCellLabel productNameCell = new CustomCellLabel();
                            productNameCell.setText(productLookUp.getSelectedItem().getName());

                            CustomCellLabel productNumberCell = new CustomCellLabel();
                            productNumberCell.setText(productLookUp.getSelectedItem().getName().split("->")[0]);

                            final ProductSerialsLookUp serialsLookUp = new ProductSerialsLookUp(productLookUp.getSelectedItemID(), null, viewType);
                            serialsLookUp.getSuggestBox().setWidth("140px");

                            final CustomCellLabel lotNumber = new CustomCellLabel();
                            final CustomCellLabel refNumber = new CustomCellLabel();
                            final int finalK = row;
                            serialsLookUp.getSuggestBox().addSelectionHandler(selectionEvent -> {
                                if (serialsLookUp.getSelectedData() != null) {
                                    lotNumber.setText(serialsLookUp.getSelectedData().getLotNumber()); //widget value
                                    refNumber.setText(serialsLookUp.getSelectedData().getRefNumber()); //widget value
                                    serialTable.getColumnCellWidgetById(finalK, LOT_NUMBER).getElement().getFirstChildElement().setInnerHTML(serialsLookUp.getSelectedData().getLotNumber()); //render value
                                    serialTable.getColumnCellWidgetById(finalK, REF_NUMBER).getElement().getFirstChildElement().setInnerHTML(serialsLookUp.getSelectedData().getRefNumber()); //render value
                                }
                            });
                            if (productLookUp.getAssignedSerials() != null && k < productLookUp.getAssignedSerials().length && productLookUp.getAssignedSerials()[k] != null) {
                                serialsLookUp.addProductSerialItem(productLookUp.getAssignedSerials()[k]);
                                idCell.setText(productLookUp.getAssignedSerials()[k].getObjectID() + "");
                                if (productLookUp.getAssignedSerials()[k].getLotNumber() != null) {
                                    lotNumber.setText(productLookUp.getAssignedSerials()[k].getLotNumber());
                                }
                                if (productLookUp.getAssignedSerials()[k].getRefNumber() != null) {
                                    refNumber.setText(productLookUp.getAssignedSerials()[k].getRefNumber());
                                }
                            }

                            objects.add(idCell);
                            objects.add(productIdCell);
                            objects.add(productNameCell);
                            objects.add(productNumberCell);
                            objects.add(serialsLookUp);
                            objects.add(lotNumber);
                            objects.add(refNumber);

                            widgetMap.put(productNumberCell.getText(), productLookUp);
                            serialTable.addRow(objects.toArray(new Object[objects.size()]));
                            row++;
                        }
                    }
                }
            }
        }
    }

    private boolean isIntegerQty(BigDecimal shipped) {
        return shipped.subtract(new BigDecimal(shipped.intValue())).compareTo(BigDecimal.ZERO) == 0;
    }

    private String getQuantityReceived(String text) {
        return text.equals(wfmStrings.notAvailable()) || text.equals("") ? "0" : (text.indexOf(':') == -1 ? text : getHourValue(text));
    }

    private String getHourValue(String text) {
        String[] time = text.split(":");
        return (Double.parseDouble(time[0]) + Double.parseDouble(time[1]) / 60) + "";
    }


    private void initPurchaseOrderForm() {
        serialTable = new EditableTable(getColumns());
        fillPOItemsSerialTable();
    }

    private void initSaleInvoiceForm() {
        EditableTable itemsTable = relatedProductsTable.getItemsTable();
        EditableGrid editableGrid = itemsTable.getGrid();
        relatedProductsTable.validation();
        serialTable = new EditableTable(getColumns());

        Integer relatedProjectID = relatedProjectLookUp.getSelectedItemID();
        ArrayList<Object> objects;
        widgetMap = new LinkedHashMap<>();
        int row = 0;
        for (int i = 0; i < editableGrid.getRowCount(); i++) {
            if (itemsTable.isItemValid(i)) {
                Widget widget = itemsTable.getColumnById(i, ProductsTable.PRODUCT);
                if (widget instanceof ProductLookUp) {
                    ProductLookUp productLookUp = (ProductLookUp) widget;
                    ItemQtyPanel qtyPanel = (ItemQtyPanel) itemsTable.getColumnById(i, ProductsTable.QTY);
                    if (productLookUp.getSelectedItemID() != null && productLookUp.getSelectedItemID() > 0 && qtyPanel.isIntegerQty() && productLookUp.getSelectedData() != null && productLookUp.getSelectedData() instanceof ProductSelectItem) {
                        ProductSelectItem selectedData = (ProductSelectItem) productLookUp.getSelectedData();
                        if (AccountingConstants.INVENTORY_ITEM.equals(selectedData.getProductType()) || selectedData.isPurchasedFromSupplier()) {
                            if (isAlmadarSerials) {
                                int k = 0;
                                k++;
                                objects = new ArrayList<>(6);
                                CustomCellLabel idCell = new CustomCellLabel();

                                CustomCellLabel productIdCell = new CustomCellLabel();
                                productIdCell.setText(selectedData.getId() + "");

                                CustomCellLabel productNameCell = new CustomCellLabel();
                                productNameCell.setText(productLookUp.getSelectedItem().getName());

                                CustomCellLabel productNumberCell = new CustomCellLabel();
                                productNumberCell.setText(productLookUp.getSelectedItem().getName().split("->")[0]);

                                final ProductSerialsLookUp serialsLookUp = new ProductSerialsLookUp(productLookUp.getSelectedItemID(), relatedProjectID, viewType);
                                serialsLookUp.getSuggestBox().setWidth("140px");

                                final CustomCellLabel lotNumber = new CustomCellLabel();
                                final CustomCellLabel refNumber = new CustomCellLabel();
                                final CustomCellLabel qty = new CustomCellLabel();
                                final int finalK = row;
                                serialsLookUp.getSuggestBox().addSelectionHandler(selectionEvent -> {
                                    serialTable.getColumnCellWidgetById(finalK, SERIAL_NUMBER).setStyleName("");
                                    if (serialsLookUp.getSelectedData() != null) {
                                        ProductSerialItem selectedSerial = serialsLookUp.getSelectedData();
                                        validateSerialsAndItemQty(productLookUp.getSelectedItemID(), selectedSerial, lotNumber, refNumber, finalK, qtyPanel.getQty().intValue());
                                    }
                                });
                                if (productLookUp.getAssignedSerials() != null && k < productLookUp.getAssignedSerials().length && productLookUp.getAssignedSerials()[k] != null) {
                                    serialsLookUp.addProductSerialItem(productLookUp.getAssignedSerials()[k]);
                                    idCell.setText(productLookUp.getAssignedSerials()[k].getObjectID() + "");
                                    if (productLookUp.getAssignedSerials()[k].getLotNumber() != null) {
                                        lotNumber.setText(productLookUp.getAssignedSerials()[k].getLotNumber());
                                    }
                                    if (productLookUp.getAssignedSerials()[k].getRefNumber() != null) {
                                        refNumber.setText(productLookUp.getAssignedSerials()[k].getRefNumber());
                                    }
                                }
                                qty.setText(Utils.getCalculationNumberFormat().format(qtyPanel.getQty()));

                                objects.add(idCell);
                                objects.add(productIdCell);
                                objects.add(productNameCell);
                                objects.add(productNumberCell);
                                objects.add(serialsLookUp);
                                objects.add(lotNumber);
                                objects.add(refNumber);
                                objects.add(qty);

                                widgetMap.put(productNumberCell.getText(), productLookUp);

                                addRowToSerialTable(objects.toArray(new Object[objects.size()]));
                                row++;
                            } else {
                                for (int k = 0; k < qtyPanel.getQty().intValue(); k++) {
                                    objects = new ArrayList<>(5);

                                    CustomCellLabel idCell = new CustomCellLabel();

                                    CustomCellLabel productIdCell = new CustomCellLabel();
                                    productIdCell.setText(selectedData.getId() + "");

                                    CustomCellLabel productNameCell = new CustomCellLabel();
                                    productNameCell.setText(productLookUp.getSelectedItem().getName());

                                    CustomCellLabel productNumberCell = new CustomCellLabel();
                                    productNumberCell.setText(productLookUp.getSelectedItem().getName().split("->")[0]);

                                    final ProductSerialsLookUp serialsLookUp = new ProductSerialsLookUp(productLookUp.getSelectedItemID(), relatedProjectID, viewType);
                                    serialsLookUp.getSuggestBox().setWidth("140px");

                                    final CustomCellLabel lotNumber = new CustomCellLabel();
                                    final CustomCellLabel refNumber = new CustomCellLabel();
                                    final int finalK = row;
                                    serialsLookUp.getSuggestBox().addSelectionHandler(selectionEvent -> {
                                        serialTable.getColumnCellWidgetById(finalK, SERIAL_NUMBER).removeStyleName("x-form-invalid");
                                        if (serialsLookUp.getSelectedData() != null) {
                                            lotNumber.setText(serialsLookUp.getSelectedData().getLotNumber()); //widget value
                                            refNumber.setText(serialsLookUp.getSelectedData().getRefNumber()); //widget value
                                            serialTable.getColumnCellWidgetById(finalK, LOT_NUMBER).getElement().getFirstChildElement().setInnerHTML(serialsLookUp.getSelectedData().getLotNumber()); //render value
                                            serialTable.getColumnCellWidgetById(finalK, REF_NUMBER).getElement().getFirstChildElement().setInnerHTML(serialsLookUp.getSelectedData().getRefNumber()); //render value
                                        }
                                    });
                                    if (productLookUp.getAssignedSerials() != null && k < productLookUp.getAssignedSerials().length && productLookUp.getAssignedSerials()[k] != null) {
                                        serialsLookUp.addProductSerialItem(productLookUp.getAssignedSerials()[k]);
                                        idCell.setText(productLookUp.getAssignedSerials()[k].getObjectID() + "");
                                        if (productLookUp.getAssignedSerials()[k].getLotNumber() != null) {
                                            lotNumber.setText(productLookUp.getAssignedSerials()[k].getLotNumber());
                                        }
                                        if (productLookUp.getAssignedSerials()[k].getRefNumber() != null) {
                                            refNumber.setText(productLookUp.getAssignedSerials()[k].getRefNumber());
                                        }
                                    }

                                    objects.add(idCell);
                                    objects.add(productIdCell);
                                    objects.add(productNameCell);
                                    objects.add(productNumberCell);
                                    objects.add(serialsLookUp);
                                    objects.add(lotNumber);
                                    objects.add(refNumber);

                                    widgetMap.put(productNumberCell.getText(), productLookUp);

                                    addRowToSerialTable(objects.toArray(new Object[objects.size()]));
                                    row++;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void validateSerialsAndItemQty(Integer selectedItem, ProductSerialItem selectedSerial, CustomCellLabel lotNumber, CustomCellLabel refNumber, Integer finalK, Integer itemQty) {

        productService.getSerialsQty(selectedItem, selectedSerial.getSerial(), selectedSerial.getExpirationDate(), new AsyncCallback<Integer>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.warn(wfmStrings.sorrySomethingWentWrong());
            }

            @Override
            public void onSuccess(Integer serialsQys) {
                if (itemQty > serialsQys ){
                    serialTable.getColumnCellWidgetById(finalK, SERIAL_NUMBER).setStyleName("x-form-invalid");
                    Info.warn(accountingStrings.serialsQtyExceeded().replace("{SerialNumber}", selectedSerial.getSerial()));
                } else {
                    lotNumber.setText(selectedSerial.getLotNumber());
                    refNumber.setText(selectedSerial.getRefNumber());
                    serialTable.getColumnCellWidgetById(finalK, LOT_NUMBER).getElement().getFirstChildElement().setInnerHTML(selectedSerial.getLotNumber());
                    serialTable.getColumnCellWidgetById(finalK, REF_NUMBER).getElement().getFirstChildElement().setInnerHTML(selectedSerial.getRefNumber());
                }
            }
        });
    }

    private void addRowToSerialTable(Object[] objects) {
        serialTable.addRow(objects);
    }

    private void initProductForm() {
        serialTable = new EditableTable(getColumns());
        ArrayList<Object> objects;
        if (Constants.PRODUCT_ADD.equals(viewType)) {
            int count = 0;
            if (product.getProductSerialItems() != null) {
                for (ProductSerialItem productSerialItem : product.getProductSerialItems()) {
                    CustomCellLabel productNumberCell = new CustomCellLabel();
                    if (product.getNumberData() != null) {
                        productNumberCell.setText(product.getNumberData().getNumberString());
                    }
                    CustomCellTextBox serialNumberCell = new CustomCellTextBox();
                    if (productSerialItem.getSerial() != null) {
                        serialNumberCell.setText(productSerialItem.getSerial());
                    }
                    CustomCellTextBox lotNumber = new CustomCellTextBox();
                    if (productSerialItem.getLotNumber() != null) {
                        lotNumber.setText(productSerialItem.getLotNumber());
                    }
                    CustomCellTextBox refNumber = new CustomCellTextBox();
                    if (productSerialItem.getRefNumber() != null) {
                        refNumber.setText(productSerialItem.getRefNumber());
                    }
                    CustomDatePickerCell expirationDateCell = new CustomDatePickerCell();
                    if (productSerialItem.getExpirationDate() != null) {
                        expirationDateCell.setItemValue(productSerialItem.getExpirationDate());
                    }
                    objects = new ArrayList<>();
                    objects.add(productNumberCell);
                    objects.add(serialNumberCell);
                    objects.add(lotNumber);
                    objects.add(refNumber);
                    objects.add(expirationDateCell);
                    addRowToSerialTable(objects.toArray(new Object[objects.size()]));
                    count++;
                }
            }
            for (int i = 0; i < product.getTotalQtyOnHand().intValue() - count; i++) {
                CustomCellLabel productNumberCell = new CustomCellLabel();
                productNumberCell.setText(product.getNumberData().getNumberString());

                CustomCellTextBox serialNumberCell = new CustomCellTextBox();
                CustomDatePickerCell expirationDateCell = new CustomDatePickerCell();
                CustomCellTextBox lotNumber = new CustomCellTextBox();
                CustomCellTextBox refNumber = new CustomCellTextBox();
                objects = new ArrayList<>();
                objects.add(productNumberCell);
                objects.add(serialNumberCell);
                objects.add(lotNumber);
                objects.add(refNumber);
                objects.add(expirationDateCell);
                addRowToSerialTable(objects.toArray(new Object[objects.size()]));
            }
        } else {
            if (product.getProductSerialItems() != null) {
                for (ProductSerialItem productSerialItem : product.getProductSerialItems()) {

                    CustomCellLabel idCell = new CustomCellLabel();
                    if (productSerialItem.getObjectID() != null) {
                        idCell.setText(productSerialItem.getObjectID() + "");
                    }

                    CustomCellLabel productIdCell = new CustomCellLabel();
                    if (productSerialItem.getItemID() != null) {
                        productIdCell.setText(productSerialItem.getItemID() + "");
                    }else{
                        productIdCell.setText(product.getObjectId() + "");
                    }

                    CustomCellLabel productNumberCell = new CustomCellLabel();
                    productNumberCell.setText(product.getNumberData().getNumberString());

                    CustomCellTextBox serialNumberCell = new CustomCellTextBox();
                    if (productSerialItem.getSerial() != null) {
                        serialNumberCell.setText(productSerialItem.getSerial());
                    }
                    CustomCellTextBox lotNumber = new CustomCellTextBox();
                    if (productSerialItem.getLotNumber() != null) {
                        lotNumber.setText(productSerialItem.getLotNumber());
                    }
                    CustomCellTextBox refNumber = new CustomCellTextBox();
                    if (productSerialItem.getRefNumber() != null) {
                        refNumber.setText(productSerialItem.getRefNumber());
                    }
                    CustomDatePickerCell expirationDateCell = new CustomDatePickerCell();
                    if (productSerialItem.getExpirationDate() != null) {
                        expirationDateCell.setItemValue(productSerialItem.getExpirationDate());
                    }

                    objects = new ArrayList<>(5);
                    objects.add(idCell);
                    objects.add(productIdCell);
                    objects.add(productNumberCell);
                    objects.add(serialNumberCell);
                    objects.add(lotNumber);
                    objects.add(refNumber);
                    objects.add(expirationDateCell);

                    addRowToSerialTable(objects.toArray(new Object[objects.size()]));
                }
            }
        }

    }

    protected ColumnConfig[] getColumns() {
        if (Constants.PURCHASE_ORDER.equals(viewType)) {
            int i = 0;
            ColumnConfig[] columns = new ColumnConfig[isAlmadarSerials ? 9 : 8];
            columns[i++] = new ColumnConfig(CustomCell.class, ID, "", 0, false, "hide");
            columns[i++] = new ColumnConfig(CustomCell.class, PRODUCT_ID, "", 0, false, "hide");
            columns[i] = new ColumnConfig(CustomCell.class, PRODUCT_NAME, "Product Name", 120, true, "center-align-Cell");
            columns[i++].setTitle("Product Name");
            columns[i] = new ColumnConfig(CustomCell.class, PRODUCT_NUMBER, "Product Number", 120, true, "center-align-Cell");
            columns[i++].setTitle("Product Number");
            if (isAlmadarSerials) {
                columns[i] = new ColumnConfig(CustomCell.class, ARTICLE_NUMBER, "Article Number", 120, true, "center-align-Cell");
                columns[i++].setTitle("Article Number");
            }
            columns[i] = new ColumnConfig(CustomCell.class, SERIAL_NUMBER, "Serial Number", 120, true, "center-align-Cell");
            columns[i++].setTitle("Serial Number");
            columns[i] = new ColumnConfig(CustomCell.class, LOT_NUMBER, "Lot Number", 120, true, "center-align-Cell");
            columns[i++].setTitle("Lot Number");
            columns[i] = new ColumnConfig(CustomCell.class, REF_NUMBER, "Ref Number", 120, true, "center-align-Cell");
            columns[i++].setTitle("Ref Number");
            columns[i] = new ColumnConfig(CustomCell.class, EXPIRATION_DATE, "Expiry Date", 120, true, "center-align-Cell");
            columns[i].setTitle("Expiry Date");
            return columns;
        }
        if (Constants.SALE_INVOICE.equals(viewType) || Constants.SHIPPING_DATA.equals(viewType)) {
            int i = 0;
            ColumnConfig[] columns = new ColumnConfig[isAlmadarSerials ? 8 : 7];
            columns[i++] = new ColumnConfig(CustomCell.class, ID, "", 0, false, "hide");
            columns[i++] = new ColumnConfig(CustomCell.class, PRODUCT_ID, "", 0, false, "hide");
            columns[i] = new ColumnConfig(CustomCell.class, PRODUCT_NAME, "Product Name", 120, true, "center-align-Cell");
            columns[i++].setTitle("Product Name");
            columns[i] = new ColumnConfig(CustomCell.class, PRODUCT_NUMBER, "Product Number", 120, true, "center-align-Cell");
            columns[i++].setTitle("Product Number");
            columns[i] = new ColumnConfig(LookUpCell.class, SERIAL_NUMBER, "Serial Number", 120, true, "center-align-Cell");
            columns[i++].setTitle("Serial Number");
            columns[i] = new ColumnConfig(CustomCell.class, LOT_NUMBER, "Lot Number", 120, true, "center-align-Cell");
            columns[i++].setTitle("Lot Number");
            columns[i] = new ColumnConfig(CustomCell.class, REF_NUMBER, "Ref Number", 120, true, "center-align-Cell");
            columns[i++].setTitle("Ref Number");
            if (isAlmadarSerials) {
                columns[i] = new ColumnConfig(CustomCell.class, QTY, "Quantity", 120, true, "center-align-Cell");
                columns[i++].setTitle("Quantity");
            }
            return columns;
        }
        if (Constants.PRODUCT_ADD.equals(viewType)) {
            ColumnConfig[] columns = new ColumnConfig[5];
            columns[0] = new ColumnConfig(CustomCell.class, PRODUCT_NUMBER, "Product Number", 120, true, "center-align-Cell");
            columns[0].setTitle("Product Number");
            columns[1] = new ColumnConfig(CustomCell.class, SERIAL_NUMBER, "Serial Number", 120, true, "center-align-Cell");
            columns[1].setTitle("Serial Number");
            columns[2] = new ColumnConfig(CustomCell.class, LOT_NUMBER, "Lot Number", 120, true, "center-align-Cell");
            columns[2].setTitle("Lot Number");
            columns[3] = new ColumnConfig(CustomCell.class, REF_NUMBER, "Ref Number", 120, true, "center-align-Cell");
            columns[3].setTitle("Ref Number");
            columns[4] = new ColumnConfig(CustomCell.class, EXPIRATION_DATE, "Expiry Date", 120, true, "center-align-Cell");
            columns[4].setTitle("Expiry Date");
            return columns;
        }
        if (Constants.PRODUCT_EDIT.equals(viewType)) {
            ColumnConfig[] columns = new ColumnConfig[7];
            columns[0] = new ColumnConfig(CustomCell.class, ID, "", 0, false, "hide");
            columns[1] = new ColumnConfig(CustomCell.class, PRODUCT_ID, "", 0, false, "hide");
            columns[2] = new ColumnConfig(CustomCell.class, PRODUCT_NUMBER, "Product Number", 100, true, "center-align-Cell");
            columns[2].setTitle("Product Number");
            columns[3] = new ColumnConfig(CustomCell.class, SERIAL_NUMBER, "Serial Number", 100, true, "center-align-Cell");
            columns[3].setTitle("Serial Number");
            columns[4] = new ColumnConfig(CustomCell.class, LOT_NUMBER, "Lot Number", 100, true, "center-align-Cell");
            columns[4].setTitle("Lot Number");
            columns[5] = new ColumnConfig(CustomCell.class, REF_NUMBER, "Ref Number", 100, true, "center-align-Cell");
            columns[5].setTitle("Ref Number");
            columns[6] = new ColumnConfig(CustomCell.class, EXPIRATION_DATE, "Expiry Date", 100, true, "center-align-Cell");
            columns[6].setTitle("Expiry Date");
            return columns;
        }
        return null;
    }


    private void fillPOItemsSerialTable() {
        ArrayList<Object> objects;
        for (NewInvoiceItem item : invoiceData.getItems()) {
            for (int i = 0; i < item.getReceive().intValue(); i++) {
                objects = new ArrayList<>(6);
                CustomCellLabel idCell = new CustomCellLabel();

                CustomCellLabel productIdCell = new CustomCellLabel();
                productIdCell.setText(item.getItemID() + "");

                CustomCellLabel productNameCell = new CustomCellLabel();
                productNameCell.setText(item.getItemName());

                CustomCellLabel productNumberCell = new CustomCellLabel();
                productNumberCell.setText(item.getItemNumber());

                CustomCellLabel articleNUmberCell = new CustomCellLabel();

                CustomCellTextBox serialNumberCell = new CustomCellTextBox();
                CustomDatePickerCell expirationDateCell = new CustomDatePickerCell();

                final CustomCellTextBox lotNumber = new CustomCellTextBox();
                final CustomCellTextBox refNumber = new CustomCellTextBox();
                if (item.getAssignedSerials() != null && i < item.getAssignedSerials().length && item.getAssignedSerials()[i] != null) {
                    if (item.getAssignedSerials()[i].getObjectID() == null) {
                        if (isAlmadarSerials) {
                            if (item.getAssignedSerials()[i].getArticle() != null) {
                                articleNUmberCell.setText(item.getAssignedSerials()[i].getArticle());
                            }
                        }
                        if (item.getAssignedSerials()[i].getSerial() != null) {
                            serialNumberCell.setText(item.getAssignedSerials()[i].getSerial());
                        }
                        if (item.getAssignedSerials()[i].getExpirationDate() != null) {
                            expirationDateCell.setItemValue(item.getAssignedSerials()[i].getExpirationDate());
                        }
                        if (item.getAssignedSerials()[i].getObjectID() != null) {
                            idCell.setText(item.getAssignedSerials()[i].getObjectID() + "");
                        }
                        if (item.getAssignedSerials()[i].getLotNumber() != null) {
                            lotNumber.setText(item.getAssignedSerials()[i].getLotNumber());
                        }
                        if (item.getAssignedSerials()[i].getRefNumber() != null) {
                            refNumber.setText(item.getAssignedSerials()[i].getRefNumber());
                        }
                    }
                }
                objects.add(idCell);
                objects.add(productIdCell);
                objects.add(productNameCell);
                objects.add(productNumberCell);
                if (isAlmadarSerials) {
                    objects.add(articleNUmberCell);
                }
                objects.add(serialNumberCell);
                objects.add(lotNumber);
                objects.add(refNumber);
                objects.add(expirationDateCell);

                addRowToSerialTable(objects.toArray(new Object[objects.size()]));
            }
        }
    }


    private boolean validateAndSetAssignedSerials() {
        HashMap<String, String> validationMap = new HashMap<>();
        HashMap<String, String> productNumberMap = new HashMap<>();
        serialsList = new ArrayList<>();
        if (Constants.PURCHASE_ORDER.equals(viewType)) {
            int row = 0;
            for (int i = 0; i < serialTable.getRowCount(); i++) {
                CustomCellLabel idCell = (CustomCellLabel) serialTable.getColumnById(i, ID);
                CustomCellLabel productIdCell = (CustomCellLabel) serialTable.getColumnById(i, PRODUCT_ID);
                CustomCellLabel productNameCell = (CustomCellLabel) serialTable.getColumnById(i, PRODUCT_NAME);
                CustomCellLabel productNumberCell = (CustomCellLabel) serialTable.getColumnById(i, PRODUCT_NUMBER);
                CustomCellTextBox serailNumberCell = (CustomCellTextBox) serialTable.getColumnById(i, SERIAL_NUMBER);
                CustomCellTextBox lotNumber = (CustomCellTextBox) serialTable.getColumnById(i, LOT_NUMBER);
                CustomCellTextBox refNumber = (CustomCellTextBox) serialTable.getColumnById(i, REF_NUMBER);
                CustomDatePickerCell expirationDateCell = (CustomDatePickerCell) serialTable.getColumnById(i, EXPIRATION_DATE);
                if (serailNumberCell.getValue() == null || "".equals(serailNumberCell.getValue().trim())) {
                    Info.warn(accountingStrings.serialNumbersAreMandatory());
                    return false;
                }
                if (validationMap.containsKey(serailNumberCell.getValue())) {
                    Info.show(accountingStrings.duplicateSerialNumber(), Info.Type.INFO);
                    return false;
                }
                validationMap.put(serailNumberCell.getValue(), serailNumberCell.getValue());
                if (!"".equals(idCell.getText())) {
                    serialsList.add(new ProductSerialItem(Integer.valueOf(idCell.getText()), serailNumberCell.getValue(), expirationDateCell.getDate(), lotNumber.getValue(), refNumber.getValue()));
                } else {
                    serialsList.add(new ProductSerialItem(serailNumberCell.getValue(), expirationDateCell.getDate(), lotNumber.getValue(), refNumber.getValue()));
                }
                if (!productNumberMap.containsKey(productNumberCell.getText()) && i != 0) {
                    serialsList.remove(serialsList.size() - 1);
                    invoiceData.getItems()[row].setAssignedSerials(serialsList.toArray(new ProductSerialItem[serialsList.size()]));
                    serialsList.clear();
                    if (!"".equals(idCell.getText())) {
                        serialsList.add(new ProductSerialItem(Integer.valueOf(idCell.getText()), serailNumberCell.getValue(), expirationDateCell.getDate(), lotNumber.getValue(), refNumber.getValue()));
                    } else {
                        serialsList.add(new ProductSerialItem(serailNumberCell.getValue(), expirationDateCell.getDate(), lotNumber.getValue(), refNumber.getValue()));
                    }
                    row++;
                }
                if (!productIdCell.getText().isEmpty()) {
                    Integer productId = Integer.valueOf(productIdCell.getText());
                    if (invoiceData.getItems()[row] != null && invoiceData.getItems().length > row+1) {
                        for (NewInvoiceItem item : invoiceData.getItems()) {
                            if (!item.getItemID().equals(productId)) {
                                row++;
                            } else {
                                break;
                            }
                        }
                    }
                }
                productNumberMap.put(productNumberCell.getText(), productNumberCell.getText());
            }
            for (ProductSerialItem productSerialItem : serialsList.toArray(new ProductSerialItem[serialsList.size()])) {
            }

            NewInvoiceItem[] invoiceItems = invoiceData.getItems();
            invoiceData.getItems()[row].setAssignedSerials(serialsList.toArray(new ProductSerialItem[serialsList.size()]));
            serialsList.clear();

        } else if (Constants.SALE_INVOICE.equals(viewType) || Constants.SHIPPING_DATA.equals(viewType)) {
            ArrayList<ProductSerialItem> productSerialItems = new ArrayList<>();
            productNumberMap.clear();
            validationMap.clear();
            serialsList.clear();
            for (int i = 0; i < serialTable.getRowCount(); i++) {
                CustomCellLabel idCell = (CustomCellLabel) serialTable.getColumnById(i, ID);
                CustomCellLabel productIdCell = (CustomCellLabel) serialTable.getColumnById(i, PRODUCT_ID);
                CustomCellLabel productNumberCell = (CustomCellLabel) serialTable.getColumnById(i, PRODUCT_NUMBER);
                ProductSerialsLookUp serailNumberLookUpCell = (ProductSerialsLookUp) serialTable.getColumnById(i, SERIAL_NUMBER);
                ProductSerialItem selectedData = serailNumberLookUpCell.getSelectedData();
                if (selectedData != null) {
                    if (validationMap.containsKey(selectedData.getSerial())) {
                        return false;
                    }
                    validationMap.put(selectedData.getSerial(), selectedData.getSerial());
                    selectedData.setItemID(Integer.valueOf(productIdCell.getText()));
                    serialsList.add(selectedData);

                    if (!productNumberMap.containsKey(productNumberCell.getText())) {
                        productSerialItems = new ArrayList<>();
                    }
                    productNumberMap.put(productNumberCell.getText(), productNumberCell.getText());
                    productSerialItems.add(selectedData);
                    ProductLookUp productLookUp = (ProductLookUp) widgetMap.get(productNumberCell.getText());
                    productLookUp.setAssignedSerials(Integer.valueOf(productIdCell.getText()), productSerialItems.toArray(new ProductSerialItem[productSerialItems.size()]));
                }
            }
            if (Constants.SALE_INVOICE.equals(viewType)) {
                serialsList.clear();
                widgetMap.clear();
            }
        } else if (Constants.PRODUCT_ADD.equals(viewType)) {
            validationMap.clear();
            serialsList.clear();
            for (int i = 0; i < serialTable.getRowCount(); i++) {
                CustomCellTextBox serailNumberCell = (CustomCellTextBox) serialTable.getColumnById(i, SERIAL_NUMBER);
                CustomCellTextBox lotNumber = (CustomCellTextBox) serialTable.getColumnById(i, LOT_NUMBER);
                CustomCellTextBox refNumber = (CustomCellTextBox) serialTable.getColumnById(i, REF_NUMBER);
                if (serailNumberCell.getValue() != null && !"".equals(serailNumberCell.getValue())) {
                    CustomDatePickerCell expirationDateCell = (CustomDatePickerCell) serialTable.getColumnById(i, EXPIRATION_DATE);
                    if (validationMap.containsKey(serailNumberCell)) {
                        return false;
                    }
                    validationMap.put(serailNumberCell.getValue(), serailNumberCell.getValue());

                    ProductSerialItem serialItem = new ProductSerialItem();
                    serialItem.setSerial(serailNumberCell.getText());
                    serialItem.setExpirationDate(expirationDateCell.getDate());
                    if (lotNumber != null && lotNumber.getValue() != null && !"".equals(lotNumber.getValue())) {
                        serialItem.setLotNumber(lotNumber.getText());
                    }
                    if (refNumber != null && refNumber.getValue() != null && !"".equals(refNumber.getValue())) {
                        serialItem.setRefNumber(refNumber.getText());
                    }
                    serialsList.add(serialItem);
                }
            }
            if (product.getProductSerialItems() != null) {
                product.getProductSerialItems().addAll(serialsList);
            } else {
                product.setProductSerialItems(serialsList);
            }
        } else if (Constants.PRODUCT_EDIT.equals(viewType)) {
            validationMap.clear();
            serialsList.clear();
            for (int i = 0; i < serialTable.getRowCount(); i++) {
                CustomCellTextBox serailNumberCell = (CustomCellTextBox) serialTable.getColumnById(i, SERIAL_NUMBER);
                CustomCellTextBox lotNumber = (CustomCellTextBox) serialTable.getColumnById(i, LOT_NUMBER);
                CustomCellTextBox refNumber = (CustomCellTextBox) serialTable.getColumnById(i, REF_NUMBER);
                if (serailNumberCell.getValue() != null && !"".equals(serailNumberCell.getValue())) {
                    CustomCellLabel idCell = (CustomCellLabel) serialTable.getColumnById(i, ID);
                    CustomCellLabel productIdCell = (CustomCellLabel) serialTable.getColumnById(i, PRODUCT_ID);
                    CustomDatePickerCell expirationDateCell = (CustomDatePickerCell) serialTable.getColumnById(i, EXPIRATION_DATE);
                    if (validationMap.containsKey(serailNumberCell)) {
                        return false;
                    }
                    validationMap.put(serailNumberCell.getValue(), serailNumberCell.getValue());

                    ProductSerialItem serialItem = new ProductSerialItem();
                    serialItem.setObjectID(!"".equals(idCell.getText()) ? Integer.valueOf(idCell.getText()) : null);
                    serialItem.setItemID(Integer.valueOf(productIdCell.getText()));
                    serialItem.setSerial(serailNumberCell.getText());
                    if (lotNumber != null && lotNumber.getValue() != null && !"".equals(lotNumber.getValue())) {
                        serialItem.setLotNumber(lotNumber.getValue());
                    }
                    if (refNumber != null && refNumber.getValue() != null && !"".equals(refNumber.getValue())) {
                        serialItem.setRefNumber(refNumber.getValue());
                    }
                    serialItem.setExpirationDate(expirationDateCell.getDate());

                    serialsList.add(serialItem);
                }
            }
        }
        return true;
    }

    public ArrayList<ProductSerialItem> getProductSerialItems() {
        return serialsList;
    }

}
