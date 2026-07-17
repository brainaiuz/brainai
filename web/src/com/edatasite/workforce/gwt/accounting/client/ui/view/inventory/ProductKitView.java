package com.edatasite.workforce.gwt.accounting.client.ui.view.inventory;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewProduct;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductKitItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductSelectItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductService;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.client.ui.view.ProductLookUp;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.FlexPanel;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LookUpCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.listeners.EditableTableListener;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.CustomCellTextBox;
import com.edatasite.workforce.gwt.invoice.client.ui.view.CustomCellLabel;
import com.edatasite.workforce.gwt.invoice.client.ui.view.ExtendedTextArea;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: Mar 3, 2011
 * Time: 1:54:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProductKitView extends Composite implements AccountingConstants, Constants {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    private TextBox txtStandartPrice;
    private TextBox txtSellingPrice;
    private TextBox txtCostPrice;

    private EditableTable tblKitItem;

    private FlexPanel pnlContainer;

    private BigDecimal standartPrice = ZERO;

    private final Integer productID;
    private boolean isView;

    public ProductKitView(Integer productID) {
        this.productID = productID;
        initialize();
    }

    public ProductKitView(Integer productID, boolean isView) {
        this.productID = productID;
        this.isView = isView;
        initialize();
    }

    public NewProduct getProductKitData(NewProduct product) {
        ProductKitItem[] productKitItems = new ProductKitItem[tblKitItem.getRowCount()];
        for (int i = 0; i < tblKitItem.getRowCount(); i++) {
            ProductLookUp productLookUp = (ProductLookUp) tblKitItem.getColumnById(i, "product");
            CustomCellTextBox qty = (CustomCellTextBox) tblKitItem.getColumnById(i, "qty");
            ExtendedTextArea description = (ExtendedTextArea) tblKitItem.getColumnById(i, "description");

            productKitItems[i] = new ProductKitItem();
            productKitItems[i].setProductItem((ProductSelectItem) productLookUp.getSelectedData());
            if (qty.getValue() != null && !qty.getValue().isEmpty()) {
                productKitItems[i].setQuantity(AccountingUtils.get().parseToBigDecimal(qty.getText()));
            }
            productKitItems[i].setDescription(description.getValue());
        }

        product.setProductKitItems(productKitItems);
        product.setSellingPrice(AccountingUtils.get().parseToBigDecimal(txtSellingPrice.getValue()));
        product.setUnitPrice(AccountingUtils.get().parseToBigDecimal(txtCostPrice.getValue()));

        return product;
    }

    public void setProductKitData(NewProduct product) {
        if (product.getProductKitItems() != null && product.getProductKitItems().length > 0) {
            tblKitItem.removeAllRows();
            for (ProductKitItem kitItem : product.getProductKitItems()) {
                tblKitItem.addRow(getWidgets(kitItem));
            }
        }
        txtStandartPrice.setText(AccountingUtils.get().formatPrice(standartPrice));
        txtSellingPrice.setText(AccountingUtils.get().formatPrice(product.getSellingPrice()));
        txtCostPrice.setText(AccountingUtils.get().formatPrice(product.getUnitPrice()));
    }

    public boolean validateKitItems() {
        int errors = 0;


        for (int rowID = 0; rowID < tblKitItem.getRowCount(); rowID++) {
            tblKitItem.resetValidation(rowID);
            ProductLookUp product = (ProductLookUp) tblKitItem.getColumnById(rowID, "product");
            if (!Validation.validateLookUpRequired(product)) {
                tblKitItem.notValid(rowID, "product");
                errors++;
            }

            CustomCellTextBox qty = (CustomCellTextBox) tblKitItem.getColumnById(rowID, "qty");
            if (qty.getValue() == null || qty.getValue().isEmpty()) {
                tblKitItem.notValid(rowID, "qty");
                errors++;
            }
        }

        return errors == 0;
    }

    private void initialize() {
        pnlContainer = new FlexPanel();

        tblKitItem = new EditableTable(getColumns(), true, true, false);
        tblKitItem.setListener(new EditableTableListener() {
            @Override
            public void addRow() {
                tblKitItem.addRow(getWidgets(new ProductKitItem()));
            }

            @Override
            public void removeRow() {
                calculate();
            }
        });
        tblKitItem.setRemoveRowListener(() -> {
            if (tblKitItem.getRowCount() < 2) {
                Info.show(accountingStrings.atLeastOneItemRequired(), Info.Type.WARNING);
            } else {
                tblKitItem.getGrid().getModel().removeRow(tblKitItem.getGrid().getCurrentRow());
            }
        });
        tblKitItem.addRow(getWidgets(new ProductKitItem()));
        GColumn column = new GColumn(GColumnEnum.COL_12);
        column.add(new FormGroup(tblKitItem));
        pnlContainer.add(new GRow(column));

        txtStandartPrice = new TextBox();
        txtStandartPrice.setEnabled(false);
        txtSellingPrice = new TextBox();
        txtSellingPrice.setEnabled(!isView);
        txtCostPrice = new TextBox();
        txtCostPrice.setEnabled(!isView);
        txtStandartPrice.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        txtSellingPrice.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        txtCostPrice.setAlignment(ValueBoxBase.TextAlignment.RIGHT);

        FormGroup standardPriceField = new FormGroup(wfmStrings.standardPrice(), txtStandartPrice);
        FormGroup sellingPriceField = new FormGroup(wfmStrings.sellingPrice(), txtSellingPrice);
        FormGroup costPriceField = new FormGroup(wfmStrings.costPrice(), txtCostPrice);

        GColumn col1 = new GColumn(GColumnEnum.COL_4, standardPriceField);
        GColumn col2 = new GColumn(GColumnEnum.COL_4, sellingPriceField);
        GColumn col3 = new GColumn(GColumnEnum.COL_4, costPriceField);

        pnlContainer.add(new GRow(col1, col2, col3));

        initWidget(pnlContainer);
    }

    private ColumnConfig[] getColumns() {

        ColumnConfig[] columns = new ColumnConfig[6];
        columns[0] = new ColumnConfig(LookUpCell.class, "product", wfmStrings.product(), 240);
        columns[1] = new ColumnConfig(CustomCell.class, "description", wfmStrings.description(), 240);
        columns[2] = new ColumnConfig(CustomCell.class, "qty", wfmStrings.qty(), 100);
        columns[3] = new ColumnConfig(CustomCell.class, "price", wfmStrings.price(), 100);
        columns[4] = new ColumnConfig(CustomCell.class, "cost", wfmStrings.cost(), 100);
        columns[5] = new ColumnConfig(CustomCell.class, "subtotal", wfmStrings.subtotal(), 100);

        return columns;
    }

    private Widget[] getWidgets(final ProductKitItem item) {
        int index = 0;
        final Widget[] widgets = new Widget[6];


        final CustomCellLabel lblPrice = new CustomCellLabel();
        lblPrice.setStyleName("totalBold");
        lblPrice.setText(AccountingUtils.get().getZero()); //set default price
        if (item.getPrice() != null)
            lblPrice.setText(item.getPrice());

        final CustomCellLabel lblSubtotal = new CustomCellLabel();
        lblSubtotal.setStyleName("totalBold");
        lblSubtotal.setText(AccountingUtils.get().getZero()); //set defualt subtotal
        if (item.getSubtotal() != null) {
            lblSubtotal.setText(item.getSubtotal());
            standartPrice = standartPrice.add(new BigDecimal(item.getSubtotal()));
        }

        CustomCellLabel lblCost = new CustomCellLabel();
        lblCost.setStyleName("totalBold");
        lblCost.setText(AccountingUtils.get().getZero());
        if (item.getCost() != null) {
            lblCost.setText(item.getCost());
        }

        final ProductLookUp productLookUp = new ProductLookUp(PRODUCT_GROUP);
        productLookUp.setProductID(productID);
        productLookUp.setWithoutType(PRODUCT_KIT);
        productLookUp.setEnabled(!isView);

        final ExtendedTextArea txtDescription = new ExtendedTextArea();
        txtDescription.addStyleName(DEFAULT_WIDTH);
        txtDescription.setEnabled(!isView);
        if (item.getDescription() != null) {
            txtDescription.setValue(item.getDescription());
        }

        final CustomCellTextBox txtQuantity = new CustomCellTextBox();
        txtQuantity.setEnabled(!isView);
        txtQuantity.setWidth("140px");
        txtQuantity.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        Validation.addNumericKeyboardListener(txtQuantity, 4);
        txtQuantity.addKeyUpHandler(event -> {
            if (txtQuantity.getValue() != null && !txtQuantity.getValue().equals("")) {
                BigDecimal net = AccountingUtils.get().parseToBigDecimal(lblPrice.getText()).multiply(AccountingUtils.get().parseToBigDecimal(txtQuantity.getValue())).setScale(2, RoundingMode.HALF_UP);
                lblSubtotal.setText(AccountingUtils.get().formatPrice(net));
                calculate();
            }
        });
        if (item.getQuantity() != null)
            txtQuantity.setValue(AccountingUtils.get().formatPrice(item.getQuantity()));

        if (item.getProductItem() != null) {
            productLookUp.addProductItem(item.getProductItem());
        }
        productLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> setItemValues(productLookUp.getSelectedItemID(), widgets));

        widgets[index++] = productLookUp;
        widgets[index++] = txtDescription;
        widgets[index++] = txtQuantity;
        widgets[index++] = lblPrice;
        widgets[index++] = lblCost;
        widgets[index++] = lblSubtotal;

        return widgets;
    }

    private void setItemValues(Integer productID, final Widget[] widgets) {
        ProductService.App.get().getProductBaseData(productID, new AbstractAsyncCallback<NewProduct>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                GWT.log(throwable.getMessage());
            }

            @Override
            public void success(NewProduct product) {
                LoadingPanel.loading(false);

                ((ExtendedTextArea) widgets[1]).setValue(product.getDescription());

                ((CustomCellTextBox) widgets[2]).setValue("1"); //default value will be 1

                BigDecimal sellingPrice = product.getSellingPrice() != null ? product.getSellingPrice().setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
                BigDecimal unitPrice = product.getUnitPrice() != null ? product.getUnitPrice().setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO;

                ((CustomCellLabel) widgets[3]).setText(AccountingUtils.get().format(sellingPrice));
                ((CustomCellLabel) widgets[4]).setText(AccountingUtils.get().format(unitPrice));

                ((CustomCellLabel) widgets[5]).setText(AccountingUtils.get().format(sellingPrice));

                calculate();
            }
        });
    }

    private void calculate() {
        BigDecimal standPrice = ZERO;
        BigDecimal costPrice = ZERO;

        for (int i = 0; i < tblKitItem.getRowCount(); i++) {
            CustomCellLabel subtotal = (CustomCellLabel) tblKitItem.getColumnById(i, "subtotal");
            CustomCellLabel cost = (CustomCellLabel) tblKitItem.getColumnById(i, "cost");
            CustomCellTextBox qty = (CustomCellTextBox) tblKitItem.getColumnById(i, "qty");

            LookUpCell productCell = (LookUpCell) tblKitItem.getColumnCellWidgetById(i, "product");
            productCell.InActive();
            CustomCell descriptionCell = (CustomCell) tblKitItem.getColumnCellWidgetById(i, "description");
            descriptionCell.InActive();
            CustomCell qtyCell = (CustomCell) tblKitItem.getColumnCellWidgetById(i, "qty");
            qtyCell.InActive();
            CustomCell costAllocateCell = (CustomCell) tblKitItem.getColumnCellWidgetById(i, "costAllocate");
            CustomCell priceCell = (CustomCell) tblKitItem.getColumnCellWidgetById(i, "price");
            priceCell.InActive();
            CustomCell costCell = (CustomCell) tblKitItem.getColumnCellWidgetById(i, "cost");
            costCell.InActive();
            CustomCell subtotalCell = (CustomCell) tblKitItem.getColumnCellWidgetById(i, "subtotal");
            subtotalCell.InActive();


            standPrice = standPrice.add(AccountingUtils.get().parseToBigDecimal(subtotal.getText()));
            costPrice = costPrice.add(AccountingUtils.get().parseToBigDecimal(cost.getText()).multiply(AccountingUtils.get().parseToBigDecimal(qty.getText())));
        }

        txtStandartPrice.setText(AccountingUtils.get().format(standPrice));
        txtSellingPrice.setText(AccountingUtils.get().format(standPrice));
        txtCostPrice.setText(AccountingUtils.get().formatPrice(costPrice));
    }

}
