package com.edatasite.workforce.gwt.invoice.client.ui.view;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductSelectItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.client.ui.view.ProductLookUp;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmWindow;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.lookup.ProjectLookUp;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoiceItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.ProductSerialItem;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.ItemQtyPanel;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.ProductsTable;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.gwt.advanced.client.ui.widget.EditableGrid;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 5/7/12
 * Time: 12:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProductSerialsAssignView {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    private final String viewType;
    private NewInvoice invoiceData;
    private FlexTable serialsTable;
    private Button assignSerialsButton;

    private ProjectLookUp relatedProjectLookUp;
    private ProductsTable relatedProductsTable;

    public ProductSerialsAssignView(ProjectLookUp relatedProjectLookUp, String viewType) {
        this.viewType = viewType;
        this.relatedProjectLookUp = relatedProjectLookUp;
        initializeAssignSerialsButton();
    }

    public ProductSerialsAssignView(NewInvoice invoiceData, String viewType) {
        this.invoiceData = invoiceData;
        this.viewType = viewType;
        initializeAssignSerialsButton();
    }

    private void initializeAssignSerialsButton() {
        assignSerialsButton = new Button(accountingStrings.assignSerials());
        assignSerialsButton.addClickHandler(event -> showAssignSerialsDialogBox());
    }

    private void showAssignSerialsDialogBox() {

        serialsTable = new FlexTable();
        serialsTable.setCellSpacing(10);

        if (Constants.PURCHASE_ORDER.equals(viewType)) {
            NewInvoiceItem[] items = invoiceData.getItems();
            for (int i = 0; i < items.length; i++) {
                serialsTable.setWidget(0, i, new ExtendedHTML("<b>" + items[i].getItemName() + "</b>", i, items[i].getQuantity()));

                HorizontalPanel headerPanel = new HorizontalPanel();
                headerPanel.setSpacing(2);
                headerPanel.add(new HTML("<b>" + wfmStrings.serial() + "</b>"));
                headerPanel.add(new HTML("<b>" + wfmStrings.expiryDate() + "</b>"));
                headerPanel.setCellWidth(headerPanel.getWidget(0), "145px");
                serialsTable.setWidget(1, i, headerPanel);

                serialsTable.getColumnFormatter().setWidth(i, "150px");
                int row = 2;
                for (int k = 0; k < items[i].getQuantity().intValue(); k++) {
                    SerialItemPanel itemPanel = new SerialItemPanel();
                    if (items[i].getAssignedSerials() != null && k < items[i].getAssignedSerials().length && items[i].getAssignedSerials()[k] != null) {
                        itemPanel.setItem(items[i].getAssignedSerials()[k]);
                    }
                    serialsTable.setWidget(row, i, itemPanel);
                    row++;
                }
            }
        } else if (Constants.SALE_INVOICE.equals(viewType)) {
            boolean containsValidProduct = false;
            EditableTable itemsTable = relatedProductsTable.getItemsTable();
            EditableGrid editableGrid = itemsTable.getGrid();
            relatedProductsTable.validation();

            Integer relatedProjectID = relatedProjectLookUp.getSelectedItemID();

            for (int i = 0; i < editableGrid.getRowCount(); i++) {
                if (itemsTable.isItemValid(i)) {
                    Widget widget = itemsTable.getColumnById(i, ProductsTable.PRODUCT);
                    if (widget instanceof ProductLookUp) {
                        ProductLookUp product = (ProductLookUp) widget;
                        ItemQtyPanel qtyPanel = (ItemQtyPanel) itemsTable.getColumnById(i, ProductsTable.QTY);
                        if (product.getSelectedItemID() != null && product.getSelectedItemID() > 0 && qtyPanel.isIntegerQty() && product.getSelectedData() != null && product.getSelectedData() instanceof ProductSelectItem) {
                            ProductSelectItem selectedData = (ProductSelectItem) product.getSelectedData();
                            if (AccountingConstants.INVENTORY_ITEM.equals(selectedData.getProductType()) || selectedData.isPurchasedFromSupplier()) {
                                ExtendedHTML extendedHTML = new ExtendedHTML("<b>" + product.getSelectedItem().getName() + "</b>", i, qtyPanel.getQty());
                                extendedHTML.setProductLookUp(product);
                                serialsTable.setWidget(0, i, extendedHTML);
                                serialsTable.getColumnFormatter().setWidth(i, "150px");
                                int row = 1;
                                for (int k = 0; k < qtyPanel.getQty().intValue(); k++) {
                                    containsValidProduct = true;
                                    ProductSerialsLookUp serialsLookUp = new ProductSerialsLookUp(product.getSelectedItemID(), relatedProjectID,viewType);
                                    serialsLookUp.getSuggestBox().setWidth("140px");
                                    if (product.getAssignedSerials() != null && k < product.getAssignedSerials().length && product.getAssignedSerials()[k] != null) {
                                        serialsLookUp.addProductSerialItem(product.getAssignedSerials()[k]);
                                    }
                                    serialsTable.setWidget(row, i, serialsLookUp);
                                    row++;
                                }
                            }
                        }
                    }
                }
            }
            if (!containsValidProduct) {
                serialsTable.setWidget(0, 0, new HTML(accountingStrings.noneOfThisInvoiceItemsAreAssignedSN()));
                serialsTable.getFlexCellFormatter().setWidth(0, 0, "480px");
                serialsTable.getFlexCellFormatter().setHeight(0, 0, "300px");
                serialsTable.getFlexCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
                serialsTable.getFlexCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_MIDDLE);
            }
        }

        final KpiModal dialogBox = new KpiModal();
        dialogBox.setTitle(accountingStrings.assignSerialNumbers());
        ScrollPanel scrollPanel = new ScrollPanel();
        scrollPanel.add(serialsTable);
        scrollPanel.setSize("700px", "350px");

        HorizontalPanel buttonPanel = new HorizontalPanel();
        Button okButton = new Button(wfmStrings.ok());
        Button cancelButton = new Button(wfmStrings.cancel());
        okButton.addClickHandler(event -> {
            if (!validateAndSetAssignedSerials()) {
                WfmWindow.alert(accountingStrings.duplicateSerialNumber());
                return;
            }
            dialogBox.close();
        });
        cancelButton.addClickHandler(event -> dialogBox.close());
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        buttonPanel.setStyleName("workforce");
        buttonPanel.setSpacing(10);

        VerticalPanel verticalPanel = new VerticalPanel();
        verticalPanel.add(scrollPanel);
        verticalPanel.add(buttonPanel);
        verticalPanel.setCellHorizontalAlignment(buttonPanel, HasHorizontalAlignment.ALIGN_RIGHT);
        dialogBox.add(verticalPanel);
        dialogBox.open();
    }

    private boolean validateAndSetAssignedSerials() {
        HashMap<String, String> assignedSerialsMap = new HashMap<>();
        boolean isDuplicateSerial = false;
        if (Constants.PURCHASE_ORDER.equals(viewType)) {
            List<ProductSerialItem> serialsList = new LinkedList<>();
            int columnsCount = serialsTable.getCellCount(0);
            for (int i = 0; i < columnsCount; i++) {
                ExtendedHTML productHTML = (ExtendedHTML) serialsTable.getWidget(0, i);
                BigDecimal qty = invoiceData.getItems()[productHTML.index].getQuantity();
                int rowCount = qty.intValue() + 2;
                for (int j = 2; j < rowCount; j++) {
                    SerialItemPanel itemPanel = (SerialItemPanel) serialsTable.getWidget(j, i);
                    String serialTxt = itemPanel.getSerialTextBox().getText().trim();
                    if (!"".equals(serialTxt.trim())) {
                        if (assignedSerialsMap.containsKey(serialTxt)) {
                            isDuplicateSerial = true;
                        } else {
                            assignedSerialsMap.put(serialTxt, serialTxt);
                        }
                        serialsList.add(itemPanel.getItem());
                    }
                }

                invoiceData.getItems()[productHTML.index].setAssignedSerials(serialsList.toArray(new ProductSerialItem[]{}));
                serialsList.clear();
            }
        } else if (Constants.SALE_INVOICE.equals(viewType)) {
            if (serialsTable.getRowCount() > 1) {
                List<ProductSerialItem> serialsList = new LinkedList<>();
                int columnsCount = serialsTable.getCellCount(0);
                for (int i = 0; i < columnsCount; i++) {
                    ExtendedHTML productHTML = (ExtendedHTML) serialsTable.getWidget(0, i);
                    int rowCount = productHTML.quantity.intValue() + 1;
                    for (int j = 1; j < rowCount; j++) {
                        ProductSerialsLookUp serialsLookUp = (ProductSerialsLookUp) serialsTable.getWidget(j, i);
                        ProductSerialItem selectedData = serialsLookUp.getSelectedData();
                        if (selectedData != null) {
                            String serialTxt = serialsLookUp.getSelectedItem().getName();
                            if (assignedSerialsMap.containsKey(serialTxt)) {
                                isDuplicateSerial = true;
                            } else {
                                assignedSerialsMap.put(serialTxt, serialTxt);
                            }
                            serialsList.add(selectedData);
                        }
                    }

                    productHTML.getProductLookUp().setAssignedSerials(productHTML.getProductLookUp().getSelectedItemID(), serialsList.toArray(new ProductSerialItem[]{}));
                    serialsList.clear();
                }
            }
        }
        return !isDuplicateSerial;
    }

    public void validateAssignSerialsButton() {
        NewInvoiceItem[] items = invoiceData.getItems();
        if (items != null) {
            for (NewInvoiceItem item : items) {
                if (item.getItemID() != null && item.getItemID() != 0 && (AccountingConstants.INVENTORY_ITEM.equals(item.getProductType()) || item.isProductPurchasedFromSupplier()) && item.isIntegerQuantity()) {
                    assignSerialsButton.setVisible(true);
                }
            }
        }
    }

    public void setRelatedProductsTable(ProductsTable relatedProductsTable) {
        this.relatedProductsTable = relatedProductsTable;
    }

    public Button getAssignSerialsButton() {
        return assignSerialsButton;
    }

    public class ExtendedHTML extends HTML {
        public int index;
        public BigDecimal quantity;

        private ProductLookUp productLookUp;

        public ExtendedHTML(String text, int index, BigDecimal quantity) {
            super(text);
            this.index = index;
            this.quantity = quantity;
        }

        public ProductLookUp getProductLookUp() {
            return productLookUp;
        }

        public void setProductLookUp(ProductLookUp productLookUp) {
            this.productLookUp = productLookUp;
        }
    }

    public class SerialItemPanel extends HorizontalPanel {
        private final TextBox serialTextBox;
        private final DatePicker expirationDate;
        private ProductSerialItem item;

        public SerialItemPanel() {
            super();
            setSpacing(2);
            serialTextBox = new TextBox();
            serialTextBox.setWidth("140px");
            expirationDate = new DatePicker();
            expirationDate.setWidth("100px");
            add(serialTextBox);
            add(expirationDate);
        }

        public ProductSerialItem getItem() {
            if (item == null)
                item = new ProductSerialItem();
            item.setSerial(serialTextBox.getText().trim());
            item.setExpirationDate(expirationDate.getDate());
            return item;
        }

        public void setItem(ProductSerialItem item) {
            serialTextBox.setText(item.getSerial());
            if (item.getExpirationDate() != null) {
                expirationDate.setDate(item.getExpirationDate());
            }
            this.item = item;
        }

        public TextBox getSerialTextBox() {
            return serialTextBox;
        }

        public DatePicker getExpirationDate() {
            return expirationDate;
        }
    }
}
