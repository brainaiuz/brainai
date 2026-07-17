package com.edatasite.workforce.gwt.invoice.client.ui.view.itemserials;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductService;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductServiceAsync;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LookUpCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.listeners.EditableTableListener;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.CustomCellTextBox;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.invoice.client.rpc.ProductSerialItem;
import com.edatasite.workforce.gwt.invoice.client.ui.view.CustomCellLabel;
import com.edatasite.workforce.gwt.invoice.client.ui.view.CustomDatePickerCell;
import com.edatasite.workforce.gwt.invoice.client.ui.view.ProductSerialsLookUp;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLink;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static java.math.BigDecimal.ZERO;

public class ItemBatchSerialAssignPopup extends KpiModal {
    private static final String SERIAL_NUMBER = "SERIAL_NUMBER";
    private static final String LOT_NUMBER = "LOT_NUMBER";
    private static final String REF_NUMBER = "REF_NUMBER";
    private static final String EXPIRATION_DATE = "EXPIRATION_DATE";
    private static final String BALANCE_IN_BATCH = "BALANCE_IN_BATCH";
    private static final String QTY = "QTY";
    private static final String ID = "ID";

    private Integer productId;
    private Integer qty;
    private ItemBatchSerialAssignPopup.Link link;
    private TextBoxBase qtyBox;
    private String viewType;

    private EditableTable batchSerialTable;
    private List<ProductSerialItem> serialsList;
    private static final ProductServiceAsync productService = ProductService.App.get();

    public ItemBatchSerialAssignPopup(TextBoxBase qtyBox) {
        this.qtyBox = qtyBox;
        initialize(null);
    }

    public ItemBatchSerialAssignPopup(Integer productId, TextBoxBase qtyBox) {
        this.productId = productId;
        this.qtyBox = qtyBox;
        this.viewType = viewType;
        initialize(null);
    }

    public ItemBatchSerialAssignPopup(Integer productId, TextBoxBase qtyBox, List<String> items, String viewType) {
        this.productId = productId;
        this.qtyBox = qtyBox;
        this.viewType = viewType;
        initialize(items);
    }

    private void initialize(List<String> items) {
        setTitle("Assign Serial Number(s)");
        setScrollable(true);

        batchSerialTable = new EditableTable(getColumns(), true, true, false);
        batchSerialTable.setListener(new EditableTableListener() {
            @Override
            public void addRow() {
                batchSerialTable.addRow(getWidgets(null));
            }

            @Override
            public void removeRow() {
                //calculate();
            }
        });
        batchSerialTable.addRow(getWidgets(items));

        getContent().add(batchSerialTable);

        addButton(new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT, e -> {
            batchSerialTable.removeAllRows();
            batchSerialTable.addRow(getWidgets(null));
            serialsList = new ArrayList<>();
            close();
        }));
        addButton(new WfmButton2(wfmStrings.ok(), e -> assignSerials()));

        MaterialIcon icon = new MaterialIcon();
        icon.addStyleName("ficon--plus");
        link = new Link(this, "", icon);
        link.setTitle("Assign Serial(s)");
        link.addClickHandler(e -> {
            if (Validation.validateTextBoxRequired(qtyBox) && AccountingUtils.get().parseToBigDecimal(qtyBox.getText()).compareTo(ZERO) > 0) {
                setQty(AccountingUtils.get().parseToBigDecimal(qtyBox.getText()).intValue());
                open();
            }
        });
    }

    private ColumnConfig[] getColumns() {
        ColumnConfig[] columns = new ColumnConfig[6];
        columns[0] = new ColumnConfig(LookUpCell.class, SERIAL_NUMBER, "Serial Number", 120, true, "center-align-Cell");
        columns[1] = new ColumnConfig(CustomCell.class, LOT_NUMBER, "Lot Number", 100, true, "center-align-Cell");
        columns[2] = new ColumnConfig(CustomCell.class, REF_NUMBER, "Ref Number", 100, true, "center-align-Cell");
        columns[3] = new ColumnConfig(CustomCell.class, EXPIRATION_DATE, "Expiry Date", 100, true, "center-align-Cell");
        columns[4] = new ColumnConfig(CustomCell.class, BALANCE_IN_BATCH, "Balance in Batch", 70);
        columns[5] = new ColumnConfig(CustomCell.class, QTY, "Quantity", 70);

        return columns;
    }

    private Widget[] getWidgets(List<String> items) {
        int index = 0;
        final Widget[] widgets = new Widget[5];

        final ProductSerialsLookUp serialsLookUp = new ProductSerialsLookUp(productId, null, viewType);
        serialsLookUp.getSuggestBox().setWidth("140px");

        final CustomCellLabel lotNumber = new CustomCellLabel();
        final CustomCellLabel refNumber = new CustomCellLabel();
        final CustomDatePickerCell expiryDate = new CustomDatePickerCell();
        final CustomCellLabel balanceInBatch = new CustomCellLabel();

        CustomCellTextBox qtyCell = new CustomCellTextBox();
        qtyCell.setAlignment(ValueBoxBase.TextAlignment.CENTER);
        qtyCell.addChangeHandler(e -> qtyCell.setStyleName(""));
        Validation.addNumericKeyboardListener(qtyCell, 0);

        serialsLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> setSerialValues(widgets, serialsLookUp.getSelectedData()));

        widgets[index++] = serialsLookUp;
        widgets[index++] = lotNumber;
        widgets[index++] = refNumber;
        widgets[index++] = expiryDate;
        widgets[index++] = balanceInBatch;
        widgets[index++] = qtyCell;

        return widgets;
    }

    private void setSerialValues(Widget[] widgets, ProductSerialItem selectedSerial) {

        productService.getSerialsQty(productId, selectedSerial.getSerial(), selectedSerial.getExpirationDate(), new AsyncCallback<Integer>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.warn(wfmStrings.sorrySomethingWentWrong());
            }

            @Override
            public void onSuccess(Integer serialsQty) {
                LoadingPanel.loading(false);

                ((CustomCellLabel) widgets[1]).setText(selectedSerial.getLotNumber());

                ((CustomCellLabel) widgets[2]).setText(selectedSerial.getRefNumber() != null ? selectedSerial.getRefNumber() : "");

                String expirationDate = selectedSerial.getExpirationDate() != null ? DateUtils.format(selectedSerial.getExpirationDate(), DateUtils.format) : "";
                ((CustomDatePickerCell) widgets[3]).setItemValue(selectedSerial.getExpirationDate());

                ((CustomCellLabel) widgets[4]).setText(serialsQty.toString());

                calculate();
            }
        });
    }
    private void calculate() {
        for (int i = 0; i < batchSerialTable.getRowCount(); i++) {
            LookUpCell serialCell = (LookUpCell) batchSerialTable.getColumnCellWidgetById(i, SERIAL_NUMBER);
            serialCell.InActive();

            CustomCell lotCell = (CustomCell) batchSerialTable.getColumnCellWidgetById(i, LOT_NUMBER);
            lotCell.InActive();

            CustomCell refNumberCell = (CustomCell) batchSerialTable.getColumnCellWidgetById(i, REF_NUMBER);
            refNumberCell.InActive();

            CustomCell expiryDateCell = (CustomCell) batchSerialTable.getColumnCellWidgetById(i, EXPIRATION_DATE);
            expiryDateCell.InActive();

            CustomCell balanceInBatchCell = (CustomCell) batchSerialTable.getColumnCellWidgetById(i, BALANCE_IN_BATCH);
            balanceInBatchCell.InActive();

            CustomCell qtyCell = (CustomCell) batchSerialTable.getColumnCellWidgetById(i, QTY);
            qtyCell.InActive();
        }
    }

    private void assignSerials() {
        int errors = 0;
        Integer totalQty = 0;
        serialsList = new ArrayList<>();
        for (int i = 0; i < batchSerialTable.getRowCount(); i++) {
            CustomCellLabel qtyBatchCell = (CustomCellLabel) batchSerialTable.getColumnById(i, BALANCE_IN_BATCH);
            CustomCellTextBox qtyCell = (CustomCellTextBox) batchSerialTable.getColumnById(i, QTY);
            ProductSerialsLookUp serailNumberCell = (ProductSerialsLookUp) batchSerialTable.getColumnById(i, SERIAL_NUMBER);

            Integer qty = qtyCell.getText().isEmpty() ? 0 : Integer.parseInt(qtyCell.getText());
            Integer qtyBatch = qtyBatchCell.getText().isEmpty() ? 0 : Integer.parseInt(qtyBatchCell.getText());
            if (qty > qtyBatch) {
                errors ++;
                qtyCell.setStyleName("x-form-invalid");
                Info.warn("You can not enter more than available quantity");
                break;
            }
            totalQty += qty;

            if (serailNumberCell.getSelectedData() != null) {
                ProductSerialItem productSerialItem = serailNumberCell.getSelectedData();
                productSerialItem.setQty(BigDecimal.valueOf(qty));
                serialsList.add(productSerialItem);
            }
        }
        if (errors == 0) {
            if (qty < totalQty) {
                Info.warn("Quantity doesn't match");
            } else if (totalQty == 0) {
                Info.warn("Please select serial(s)");
            } else if (productId != null) {
                qtyBox.setValue(totalQty.toString());
                close();
            } else {
                close();
            }
        }
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public void onProductChangeEvent(Integer productId) {
        this.productId = productId;
    }

    public com.edatasite.workforce.gwt.invoice.client.ui.view.itemserials.ItemBatchSerialAssignPopup.Link getLink() {
        return link;
    }

    public ProductSerialItem[] getSerials() {
        ProductSerialItem[] serialItems = new ProductSerialItem[]{};
        if (serialsList != null) {
            serialItems = serialsList.toArray(new ProductSerialItem[serialsList.size()]);
        }
        return  serialItems;
    }

    public class Link extends MaterialLink {
        com.edatasite.workforce.gwt.invoice.client.ui.view.itemserials.ItemBatchSerialAssignPopup popup;

        public Link(com.edatasite.workforce.gwt.invoice.client.ui.view.itemserials.ItemBatchSerialAssignPopup popup, String text, MaterialIcon icon) {
            super(text, icon);
            this.popup = popup;
        }

        public ProductSerialItem[] getSerials() {
            return popup.getSerials();
        }

        public void setProductId(Integer productId) {
            popup.onProductChangeEvent(productId);
        }
    }
}
