package com.edatasite.workforce.gwt.accounting.client.ui.view.inventory;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewProduct;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.AssemblyItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.MultiPriceItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductService;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.client.ui.view.ProductLookUp;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.LoadingPanelCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.FlexPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTable;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableColumn;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableItem;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.TotalTable;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.listeners.AddListener;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductAssemblyView extends Composite implements AccountingConstants {
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    private FlexPanel pnlContainer;
    private DynamicTable assemblyItemsTable;
    private Label totalLabel;

    private Integer productId;
    private ArrayList<Integer> locationIds;
    private final HashMap<Integer, NewProduct> assemblyItems = new HashMap<>();
    private final HashMap<Integer, List<MultiPriceItem>> multiPriceItems = new HashMap<>();
    private final WfmStrings wfmStrings = WfmStrings.App.get();

    public ProductAssemblyView() {
        initialize();
    }

    public ProductAssemblyView(Integer productId) {
        this.productId = productId;
        initialize();
    }

    private void initialize() {
        pnlContainer = new FlexPanel();

        totalLabel = new Label(AccountingUtils.getZero());
        TotalTable totalTable = new TotalTable();
        totalTable.addItem("<b>" + wfmStrings.costPrice() + "</b>", totalLabel);

        assemblyItemsTable = new DynamicTable(getColumns(), true);
        assemblyItemsTable.addRow(getWidgets(null));
        assemblyItemsTable.addListener(new AddListener() {
            public void plusClicked(int rowId) {
                assemblyItemsTable.insertRow(rowId + 1, getWidgets(null));
            }

            @Override
            public void minusClicked(int rowId, Integer objectId) {
                updateTotal();
                calculate();
            }
        });

        GColumn assemblyCol = new GColumn(GColumnEnum.COL_12);
        assemblyCol.add(new FormGroup(assemblyItemsTable));
        assemblyCol.add(new FormGroup("&nbsp;", totalTable));

        pnlContainer.add(assemblyCol);
        initWidget(pnlContainer);
    }

    private DynamicTableColumn[] getColumns() {
        DynamicTableColumn[] columns = new DynamicTableColumn[6];
        columns[0] = new DynamicTableColumn(wfmStrings.category(), "category", 250);
        columns[1] = new DynamicTableColumn(wfmStrings.item(), "product", 250);
        columns[2] = new DynamicTableColumn(wfmStrings.description(), "description", 320);
        columns[3] = new DynamicTableColumn(wfmStrings.cost(), "cost", 250);
        columns[4] = new DynamicTableColumn(wfmStrings.qty(), "qty", 250);
        columns[5] = new DynamicTableColumn(wfmStrings.total(), "total", 250);
        return columns;
    }

    private Widget[] getWidgets(AssemblyItem item) {
        Integer index = 0;
        final Widget[] widgets = new Widget[6];

        TextBox category = new TextBox();
        category.setEnabled(false);
        category.ensureDebugId(PRODUCT + "category");

        final ProductLookUp productLookUp = new ProductLookUp(ASSEMBLY_ITEMS);
        productLookUp.ensureDebugId(PRODUCT + "productLookUp");
        productLookUp.setProductID(productId);
        productLookUp.setLocationIds(locationIds);
        productLookUp.getSuggestBox().addKeyDownHandler(event -> productLookUp.setLocationIds(locationIds));
//        productLookUp.setWithoutType(ASSEMBLY_ITEM);
        productLookUp.getSuggestBox().addSelectionHandler(event -> setItemValues(productLookUp.getSelectedItem(), widgets));

        TextBox description = new TextBox();
        description.setPlaceHolder(wfmStrings.description());
        description.ensureDebugId(PRODUCT + DESCRIPTION_COLUMN);

        TextBox cost = new TextBox();
        cost.setEnabled(false);
        cost.setText(AccountingUtils.getUnitPriceZero());
        cost.setAlignment(ValueBoxBase.TextAlignment.RIGHT);

        TextBox qty = new TextBox();
        qty.ensureDebugId(PRODUCT + QTY_COLUMN);
        qty.setText(AccountingUtils.getQtyZero());
        qty.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        Validation.checkToFocusTextBox(qty, AccountingUtils.getQtyZero());
        Validation.addNumericKeyboardListener(qty, AccountingUtils.customQtyScale, true);
        qty.addKeyUpHandler(event -> calculate());

        TextBox total = new TextBox();
        total.setEnabled(false);
        total.setText(AccountingUtils.getZero());
        total.setAlignment(ValueBoxBase.TextAlignment.RIGHT);

        if (item != null) {
            category.setText(item.getCategory() != null ? item.getCategory() : "");
            productLookUp.addProductItem(item.getProduct());
            productLookUp.setLocationIds(item.getLocationIds());
            description.setText(item.getDescription());
            cost.setText(AccountingUtils.get().formatUnitPrice(item.getCostPrice()));
            qty.setText(AccountingUtils.get().formatQty(item.getQuantity()));
            if (item.getProductPrice() != null && item.getProductSellingPrice() != null) {
                NewProduct newProduct = new NewProduct();
                newProduct.setSellingPrice(item.getProductSellingPrice());
                newProduct.setUnitCost(item.getProductPrice());
                assemblyItems.put(item.getProduct().getId(), newProduct);
            }
            multiPriceItems.put(item.getProduct().getId(), item.getMultiPriceItems());
        }

        widgets[index] = category;
        widgets[++index] = productLookUp;
        widgets[++index] = description;
        widgets[++index] = cost;
        widgets[++index] = qty;
        widgets[++index] = total;

        return widgets;
    }

    private void setItemValues(SelectItem item, final Widget[] widgets) {
        if (item != null && item.getId() != null) {
            final Integer productId = item.getId();
            ProductService.App.get().getProductBaseData(productId, new LoadingPanelCallback<NewProduct>(assemblyItemsTable) {
                @Override
                public void success(NewProduct product) {
                    generateWidgets(product, widgets);
                }
            });
        }
    }

    private void generateWidgets(NewProduct product, Widget[] widgets) {
        assemblyItems.put(product.getObjectId(), product);
        multiPriceItems.put(product.getObjectId(), product.getMultiPrices());
        ((TextBox) widgets[0]).setText(product.getCategoryName() != null ? product.getCategoryName() : "");
        ((ProductLookUp) widgets[1]).setLocationIds(product.getLocationIds());
        ((TextBox) widgets[2]).setText(product.getDescription());
        ((TextBox) widgets[3]).setText(AccountingUtils.get().formatUnitPrice(product.getUnitPrice() != null ? product.getUnitPrice() : product.getSellingPrice()));
        ((TextBox) widgets[4]).setText(AccountingUtils.get().formatQty(BigDecimal.ONE));
        calculate();
    }

    private BigDecimal calculate() {
        BigDecimal totalItem = ZERO;
        BigDecimal totalSellingPriceItem = ZERO;
        BigDecimal totals = ZERO;
        BigDecimal totalSellingPrice = ZERO;
        for (int i = 0; i < assemblyItemsTable.getRowNumber(); i++) {
            DynamicTableItem item = assemblyItemsTable.getItem(i);
            ProductLookUp productLookUp = (ProductLookUp) item.getColumnById("product");
            BigDecimal qty = AccountingUtils.get().parseToBigDecimal(((TextBox) item.getColumnById("qty")).getText());
            BigDecimal cost = AccountingUtils.get().parseToBigDecimal(((TextBox) item.getColumnById("cost")).getText());
            BigDecimal sellingPrice = null;
            BigDecimal multiPrice = getProductMultiPrice(productLookUp);
            if (multiPrice == null) {
                NewProduct productCost = getProductCostPrice(productLookUp);
                if (productCost != null) {
                    cost = productCost.getUnitCost();
                    sellingPrice = productCost.getSellingPrice();
                }
            } else {
                cost = multiPrice;
            }
            ((TextBox) item.getColumnById("cost")).setText(AccountingUtils.get().formatPrice(cost));
            totalItem = qty.multiply(cost);
            if (qty != null && sellingPrice != null) {
                totalSellingPriceItem = qty.multiply(sellingPrice);
                totalSellingPrice = totalSellingPrice.add(totalSellingPriceItem).setScale(AccountingUtils.calculationScale, RoundingMode.HALF_UP);
            }
            totals = totals.add(totalItem).setScale(AccountingUtils.calculationScale, RoundingMode.HALF_UP);

            ((TextBox) item.getColumnById("total")).setText(AccountingUtils.get().formatPrice(totalItem));
        }
        totalLabel.setText(AccountingUtils.get().formatPrice(totals));


        BigDecimal[] totalProduct = new BigDecimal[2];
        totalProduct[0] = totals;
        totalProduct[1] = totalSellingPrice;
        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_ASSAMBLY_ITEM_TOTAL_CHANGE, totalProduct, this);
        return totals;
    }

    private BigDecimal getProductMultiPrice(ProductLookUp productLookUp) {
        if (productLookUp != null && productLookUp.getSelectedItemID() != null && assemblyItems.get(productLookUp.getSelectedItemID()) != null) {
            Integer productId = productLookUp.getSelectedItemID();
            List<MultiPriceItem> multiPriceItemsList = multiPriceItems.get(productId);
            if (multiPriceItemsList != null) {
                for (MultiPriceItem mpi : multiPriceItemsList) {
                    if (mpi != null && mpi.getCurrency() != null) {
                        return mpi.getPrice();
                    }
                }
            }
        }
        return null;
    }

    private NewProduct getProductCostPrice(ProductLookUp productLookUp) {
        if (productLookUp != null && productLookUp.getSelectedItemID() != null && assemblyItems.get(productLookUp.getSelectedItemID()) != null) {
            Integer productId = productLookUp.getSelectedItemID();
            return assemblyItems.get(productId);
        }
        return null;
    }

    private void updateTotal() {
        BigDecimal total = new BigDecimal(0);
        for (int i = 0; i < assemblyItemsTable.getRowNumber(); i++) {
            DynamicTableItem item = assemblyItemsTable.getItem(i);
            TextBox label = (TextBox) item.getColumnById("total");
            BigDecimal value = AccountingUtils.get().parseToBigDecimal(label.getText());
            total = total.add(value);
        }
        totalLabel.setText(AccountingUtils.get().formatPrice(total));
    }

    public void setAssemblyItems(NewProduct product) {
        assemblyItemsTable.clear();
        if (product.getAssemblyItems() != null && product.getAssemblyItems().size() > 0) {
            for (AssemblyItem item : product.getAssemblyItems()) {
                assemblyItemsTable.addRow(getWidgets(item));
                DynamicTableItem tableItem = assemblyItemsTable.getItem(assemblyItemsTable.getRowNumber() - 1);
                if (item.getAssemblyItemId() != null) {
                    tableItem.setObjectId(item.getAssemblyItemId());
                }
            }
        } else {
            assemblyItemsTable.addRow(getWidgets(null));
        }
        calculate();
    }

    public ArrayList<AssemblyItem> getAssemblyItems() {
        ArrayList<AssemblyItem> assemblyItems = new ArrayList<>();
        for (int i = 0; i < assemblyItemsTable.getRowNumber(); i++) {
            DynamicTableItem item = assemblyItemsTable.getItem(i);
            ProductLookUp itemLookUp = (ProductLookUp) item.getColumnById("product");
            if (itemLookUp.getSelectedItemID() != null) {
                AssemblyItem assemblyItem = new AssemblyItem();
                if (item.getObjectId() != null) {
                    assemblyItem.setAssemblyItemId(item.getObjectId());
                }
                assemblyItem.setProduct(itemLookUp.getSelectedItem());
                assemblyItem.setLocationIds(itemLookUp.getLocationIds());
                TextBox desc = (TextBox) item.getColumnById("description");
                assemblyItem.setDescription(desc.getText());
                BigDecimal qty = AccountingUtils.get().parseToBigDecimal(((TextBox) item.getColumnById("qty")).getText());
                assemblyItem.setQuantity(qty);
                BigDecimal cost = AccountingUtils.get().parseToBigDecimal(((TextBox) item.getColumnById("cost")).getText());
                assemblyItem.setCostPrice(cost);
                BigDecimal total = AccountingUtils.get().parseToBigDecimal(((TextBox) item.getColumnById("total")).getText());
                assemblyItem.setTotal(total);
                assemblyItem.setCategory(((TextBox) item.getColumnById("category")).getText());
                assemblyItems.add(assemblyItem);
            }
        }

        return assemblyItems;
    }

    public boolean validate() {
        int errors = 0;
        for (int i = 0; i < assemblyItemsTable.getRowNumber(); i++) {
            DynamicTableItem item = assemblyItemsTable.getItem(i);
            ProductLookUp productLookUp = (ProductLookUp) item.getColumnById("product");
            if (!Validation.validateLookUpRequired(productLookUp)) {
                errors++;
            }
        }
        return errors == 0;
    }

    public void reRender(ArrayList<Integer> locationIds) {
        ArrayList<AssemblyItem> items = getAssemblyItems();

        if (items != null && !items.isEmpty()) {
            ArrayList<AssemblyItem> reRenderedItems = new ArrayList<>();

            for (AssemblyItem item : items) {
                if (item.getLocationIds() != null && locationIds != null) {
                    GWT.log("itemIds: " + item.getLocationIds());
                    GWT.log("LoationIds: " + locationIds);
                    GWT.log("==========");

                    for (Integer locationId : item.getLocationIds()) {
                        if (locationIds.contains(locationId)) {
                            reRenderedItems.add(item);
//                            break;
                        }
                    }
                }
            }
            if (reRenderedItems.isEmpty()) {
                assemblyItemsTable.clear();
                assemblyItemsTable.addRow(getWidgets(null));
            } else {
                assemblyItemsTable.clear();
                for (AssemblyItem item : reRenderedItems) {
                    assemblyItemsTable.addRow(getWidgets(item));
                    DynamicTableItem tableItem = assemblyItemsTable.getItem(assemblyItemsTable.getRowNumber() - 1);
                    if (item.getAssemblyItemId() != null) {
                        tableItem.setObjectId(item.getAssemblyItemId());
                    }
                }
            }
        } else {
            if (assemblyItemsTable.getRowCount() == 1)
                assemblyItemsTable.addRow(getWidgets(null));
        }
        calculate();
    }

    public void clearData() {
        assemblyItemsTable.clear();
        assemblyItemsTable.addRow(getWidgets(null));
        calculate();
    }




    private class ProductTypeLabel extends Label {
        private Integer productType;

        private ProductTypeLabel() {
        }

        public Integer getProductType() {
            return productType;
        }

        public void setProductType(Integer productType) {
            this.productType = productType;
            if (INVENTORY_ITEM.equals(productType)) {
                setText(INVENTORY_ITEM_STR);
            } else if (NON_INVENTORY_ITEM.equals(productType)) {
                setText(NON_INVENTORY_ITEM_STR);
            } else if (PRODUCT_KIT.equals(productType)) {
                setText(PRODUCT_KIT_STR);
            } else if (ASSEMBLY_ITEM.equals(productType)) {
                setText(ASSEMBLY_ITEM_STR);
            }
        }
    }

    public ArrayList<Integer> getLocationIds() {
        return locationIds;
    }

    public void setLocationIds(ArrayList<Integer> locationIds) {
        this.locationIds = locationIds;
    }
}
