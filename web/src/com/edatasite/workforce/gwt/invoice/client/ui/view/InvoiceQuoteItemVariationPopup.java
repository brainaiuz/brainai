package com.edatasite.workforce.gwt.invoice.client.ui.view;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewProduct;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.CheckboxSelector;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.KpiDataGrid;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.view.ObjectCommand;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ProvidesKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Sanjar
 * Date: Jun 2, 2011
 * Time: 4:41:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class InvoiceQuoteItemVariationPopup extends KpiModal {
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final AccountingUtils utils = AccountingUtils.get();
    private final NewProduct[] products;
    private final Map<Integer, NewProduct> productMap = new HashMap<>();
//    private Map<String, TableItem> tableItems = new HashMap<String, TableItem>();
    private final ObjectCommand provider;
    private final String parentName;

    private DataListBox assignee;
    private KpiDataGrid<SelectItem> valueTable;

//    private Table table;
    private CheckboxSelector selector;
    private FlexTable itemsSelector;
    private final LinkedHashMap<Integer, SelectItem> selectedIDs = new LinkedHashMap<>();

   public static final ProvidesKey<NewProduct> KEY_PROVIDER = item -> item != null ? item.getObjectId() : null;


    public InvoiceQuoteItemVariationPopup(String parentName, NewProduct[] products, ObjectCommand provider) {
        this.products = products;
        this.provider = provider;
        this.parentName = parentName;
        for (NewProduct product : products) {
            productMap.put(product.getObjectId(), product);
        }
        setMaxHeight("400px");
        setWidth(550);
        init();
    }

    private void init() {

        itemsSelector = new FlexTable();
        itemsSelector.getElement().addClassName("dataTable");
        itemsSelector.setCellPadding(10);
        itemsSelector.setCellSpacing(10);
        KpiCheckBox selectAll = new KpiCheckBox("<b>" + parentName + "</b>");
        selectAll.addClickHandler(clickEvent -> {
            int i = 1;
            for (NewProduct product : products) {
                Widget widget = itemsSelector.getWidget(i++, 0);
                if (widget instanceof KpiCheckBox) {
                    KpiCheckBox checkBox2 = (KpiCheckBox) widget;
                    checkBox2.setValue(selectAll.getValue());
                }
                if (selectAll.getValue()) {
                    String productName;
                    if (product.getNumberData() != null) {
                        productName = product.getNumberData().getNumberString() + " -> " + product.getItemName();
                    } else {
                        productName = product.getItemName();
                    }
                    selectedIDs.put(product.getObjectId(), new SelectItem(product.getObjectId(), productName));
                } else {
                    selectedIDs.clear();
                }
            }

        });
        itemsSelector.setWidget(0, 0, selectAll);
        itemsSelector.setHTML(0, 1, "<b>" + wfmStrings.qty() + "</b>");
        itemsSelector.setHTML(0, 2, "<b>" + wfmStrings.price() + "</b>");
        itemsSelector.getRowFormatter().setStyleName(0, "theader");

        SelectItem[] items = new SelectItem[products.length];
        int i = 1;
        for (NewProduct product : products) {
            KpiCheckBox select = new KpiCheckBox(product.getItemName());
            select.addClickHandler(clickEvent -> {
                if (select.getValue()) {
                    selectedIDs.put(product.getObjectId(), new SelectItem(product.getObjectId(), product.getItemName()));
                } else {
                    selectedIDs.remove(product.getObjectId());
                }
            });
            itemsSelector.setWidget(i, 0, select);
            itemsSelector.setHTML(i, 1, utils.formatQty(product.getQuantity()));
            itemsSelector.setHTML(i++, 2, utils.formatQty(product.getSellingPrice()));
        }

        add(itemsSelector);

        //btnSave
        WfmButton2 btnSave = new WfmButton2(wfmStrings.save(), sender -> save());
        WfmButton2 btnCancel = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT, sender -> close());
        addButton(btnCancel);
        addButton(btnSave);
        open();
    }

    private void save() {
        List<NewProduct> products = new ArrayList<>();
        for (Integer itemID : getSelectItemIDs()) {
            products.add(productMap.get(itemID));
        }
        provider.execute(products);
        close();
    }

    public ArrayList<Integer> getSelectItemIDs() {
        return new ArrayList<>(selectedIDs.keySet());
    }
}
