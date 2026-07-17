package com.edatasite.workforce.gwt.accounting.client.ui.view.inventory;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewProduct;
import com.edatasite.workforce.gwt.accounting.client.rpc.VariationItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.VariationListItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductService;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductServiceAsync;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.KpiDataGrid;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.cell.client.TextInputCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Sanjar
 * Date: Apr 23, 2011
 * Time: 3:40:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class AddVariationsView implements Constants, AccountingConstants {

    private static final ProductServiceAsync productService = ProductService.App.get();
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private final LinkedHashMap<String, TreeItem> itemMap = new LinkedHashMap<>();
    private final LinkedHashMap<String, Set<TreeItem>> itemsMap = new LinkedHashMap<>();
    private final LinkedHashMap<String, CompanyCustomFieldItem> codeMap = new LinkedHashMap<>();

    private ArrayList<Integer> prepareToRemove = new ArrayList<>();

    private final LinkedHashMap<String, String> ids = new LinkedHashMap<>();
    private String[] cfCodes = null;
    private String[] cfValues = null;
    private LinkedHashMap<String, Integer> combinedIds = new LinkedHashMap<>();
    private LinkedHashMap<Integer, String> combinedNames = new LinkedHashMap<>();
    private final DialogBox dialogBox;
    private VerticalPanel dialogPanel;

    private Tree variationTreeList;

    private static final ProvidesKey<VariationListItem> KEY_PROVIDER_VARIATION = item -> item == null ? null : item.getObjectID();

    private KpiDataGrid<VariationListItem> variationDataGrid;
    private ListDataProvider<VariationListItem> variationDataProvider;

    private final String addVariationView = "addvariation_view_";

    private final Integer objectId;
    private NewProduct product = new NewProduct();

    private final ArrayList<VariationListItem> variationListItems = new ArrayList<>();

    public AddVariationsView(Integer objectId) {
        this.objectId = objectId;
        dialogBox = new DialogBox();
        dialogBox.setText(accountingStrings.addVariation());
        dialogBox.setStyleName("gwt-DialogBox workforce variation-create");
        init();
    }

    protected void init() {
        initInternal();
        loadData();
    }

    private void initInternal() {
        variationTreeList = new Tree();
        variationTreeList.ensureDebugId(addVariationView + "variation_tree");
        variationTreeList.setSize("225px", "250px");
        variationTreeList.setStyleName("scrollAuto");

        variationDataGrid = new KpiDataGrid<>(KEY_PROVIDER_VARIATION);
        variationDataGrid.setEmptyTableWidget(DefaultNoItemsMessage.getNoItemsMessage(wfmStrings.noDataAvailable(), null, null));
        variationDataGrid.setSize("375px", "250px");
        initVariationItemColumn();

        variationDataProvider = new ListDataProvider<>();
        variationDataProvider.addDataDisplay(variationDataGrid);


        dialogPanel = new VerticalPanel();

        HorizontalPanel horizontalPanel = new HorizontalPanel();
        horizontalPanel.add(variationTreeList);
        horizontalPanel.add(variationDataGrid);
        HorizontalPanel btnPanel = new HorizontalPanel();
        //btnSave
        WfmButton2 btnSave = new WfmButton2(wfmStrings.save(), sender -> save());

        WfmButton2 btnCancel = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT, sender -> dialogBox.hide());
        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);
        dialogPanel.add(horizontalPanel);
        dialogPanel.add(btnPanel);
        dialogBox.add(dialogPanel);
        dialogBox.center();

        registerListeners();
    }

    private void loadData() {
        LoadingPanel.loading(true);
        productService.getProductForVariation(objectId, new AbstractAsyncCallback<VariationItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                GWT.log(throwable.getMessage());
            }

            @Override
            public void success(VariationItem variationItem) {
                LoadingPanel.loading(false);

                product = variationItem.getProduct();
                combinedIds = new LinkedHashMap<>();
                combinedNames = new LinkedHashMap<>();

                if (product != null) {
                    if (product.getCategoryCustomFieldItems() != null) {
                        addCustomFields(product.getCategoryCustomFieldItems());

                        if (product.getVariationCombinate() != null && product.getVariationCombinate().size() > 0) {
                            fillExistVariations(product.getVariationCombinate());
                        }
                    }
                }

                openAllTree(true);
            }
        });
    }

    public void save() {
        ArrayList<NewProduct> _products = new ArrayList<>();
        ArrayList<CompanyCustomFieldItem> companyCustomFieldItems = product.getCategoryCustomFieldItems();
        for (VariationListItem item : variationListItems) {
            if (item.isSelected() && !item.isExisting()) {
                NewProduct _product = new NewProduct();

                String combinationItem = combinedNames.get(item.getObjectID());
                _product.getVariationCombinate().add(combinationItem);

                String[] sp = combinationItem.split("[|]");
                ArrayList<CompanyCustomFieldItem> ccf = new ArrayList<>();
                for (CompanyCustomFieldItem customField : companyCustomFieldItems) {
                    ccf.add(customField.cloneObject());
                }
                StringBuilder additionalTitle = new StringBuilder();
                for (String s : sp) {
                    String columnCode = ids.get(s);
                    for (CompanyCustomFieldItem cc : ccf) {
                        if (cc.getColumnCode().equals(columnCode)) {
                            cc.setFieldStringValue(itemMap.get(s).getText());
                            if (additionalTitle.toString().equals("")) {
                                additionalTitle.append(itemMap.get(s).getText());
                            } else {
                                additionalTitle.append(", ").append(itemMap.get(s).getText());
                            }

                            break;
                        }
                    }
                }

                _product.setCategoryCustomFieldItems(ccf);
                _product.setItemName(product.getItemName() + ": " + additionalTitle);
                _product.setSellingPrice(item.getPrice());
                _product.setQuantity(item.getQty());
                _product.setParentId(product.getObjectId());
                _products.add(_product);
            }
        }

        if (_products.size() > 0) {
            productService.saveVariationProducts(_products.toArray(new NewProduct[]{}), product.getObjectId(), new AbstractAsyncCallback<Integer[]>() {
                @Override
                public void failure(Throwable throwable) {
                    GWT.log(throwable.getMessage());
                }

                @Override
                public void success(Integer[] results) {
                    dialogBox.hide();
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PRODUCTSERVICE_SAVED, null, null);
                }
            });
        } else {
            dialogBox.hide();
        }
    }

    public void registerListeners() {
        variationTreeList.addSelectionHandler(treeItemSelectionEvent -> {
            Timer t = new Timer() {
                public void run() {
                    TreeItem item = treeItemSelectionEvent.getSelectedItem();
                    fireItem(item);
                }
            };

            // Schedule the timer to run once in 0.2 second.
            t.schedule(200);

        });
    }

    public void addCustomFields(ArrayList<CompanyCustomFieldItem> productCustomFields) {
        ArrayList<String> codeNames = new ArrayList<>();
        for (CompanyCustomFieldItem productCustomField : productCustomFields) {
            codeMap.put(productCustomField.getColumnCode(), productCustomField);
            codeNames.add(productCustomField.getColumnCode());
            addSection(productCustomField.getFieldName(), productCustomField);
        }

        int index = 1;
        cfCodes = new String[this.ids.size() + 1];
        cfValues = new String[this.ids.size() + 1];
        for (String key : this.ids.keySet()) {
            cfCodes[index] = this.ids.get(key);
            cfValues[index] = key;
            index++;
        }
    }

    public void addSection(String sectionName, CompanyCustomFieldItem fieldItem) {
        if (!UI_TYPE_DROPDOWN.equals(fieldItem.getUiType())
                && !UI_TYPE_CHECKBOX.equals(fieldItem.getUiType())
                && !UI_TYPE_RADIOBUTTON.equals(fieldItem.getUiType())) {
            return;
        }

        Set<TreeItem> items = new HashSet<>();

        if (fieldItem.getPredefinedValues() != null) {
            //section -> custom field name
            TreeItem section = addDisabledItem(sectionName);

            //custom field values
            for (String value : fieldItem.getPredefinedValues()) {
                String gv = fieldItem.getColumnCode().toLowerCase().replace("_", "") + value.toLowerCase().replaceAll("\"|'|#|/| ", "");
                ids.put(gv, fieldItem.getColumnCode());

                KpiCheckBox sItem = new KpiCheckBox(value);
                sItem.setName(gv);

                TreeItem item = section.addItem(sItem);
                itemMap.put(gv, item);
                items.add(item);
            }
        }

        itemsMap.put(sectionName, items);
    }

    private TreeItem addDisabledItem(String name) {
        final KpiCheckBox cBox = new KpiCheckBox(name);

        final TreeItem category = variationTreeList.addItem(cBox);
        category.addStyleName("gwt-Tree-wrapper");
        cBox.addClickHandler(clickEvent -> {
            Boolean itemValue = cBox.getValue();
            for (int i = 0; i < category.getChildCount(); i++) {
                TreeItem childItem = category.getChild(i);
                ((KpiCheckBox) childItem.getWidget()).setValue(itemValue);
            }
        });

        return category;
    }

    private void fireItem(TreeItem item) {
        if (item.getWidget() instanceof KpiCheckBox) {
            KpiCheckBox vcItemBox = (KpiCheckBox) item.getWidget();
            //vcItemBox.setValue(!vcItemBox.getValue());//tree ni selection event handler ishlagani uchun shunaqa logic quwilgan {Normurod}

            if (vcItemBox.getName() == null || vcItemBox.getName().isEmpty()) {
                if (vcItemBox.getValue()) {
                    for (TreeItem addField : itemsMap.get(item.getText())) {
                        String itemId = ((KpiCheckBox) addField.getWidget()).getName();
                        ((KpiCheckBox) (itemMap.get(itemId)).getWidget()).setValue(true);
                    }
                    addItems();
                } else {
                    for (TreeItem removeField : itemsMap.get(item.getText())) {
                        if (((KpiCheckBox) removeField.getWidget()).isEnabled()) {
                            String itemId = ((KpiCheckBox) removeField.getWidget()).getName();
                            ((KpiCheckBox) itemMap.get(itemId).getWidget()).setValue(false);
                        }
                    }
                    removeItems();
                }
            } else {
                if (((KpiCheckBox) item.getWidget()).getValue()) {
                    addItems();
                } else {
                    removeItems();
                }
            }

        }
        //initialList.fireEvent(Events.Add);
    }

    private ArrayList<String> getSelectedItems() {
        ArrayList<String> selectedIds = new ArrayList<>();
        for (TreeItem item : itemMap.values()) {
            if (((KpiCheckBox) item.getWidget()).getValue()) {
                selectedIds.add(((KpiCheckBox) item.getWidget()).getName());
            }
        }

        return selectedIds;
    }

    private void generateTableItems(Integer start, String id, String result) {
        for (int i = start; i < cfCodes.length; i++) {
            if (cfCodes[i] == null) {
                continue;
            }

            if (start.equals(0)) {
                result = "";
                id = "";
            }

            if (combinedIds.get(id + cfValues[i]) == null) {
                combinedIds.put(id + cfValues[i], combinedIds.size());
                combinedNames.put(combinedIds.get(id + cfValues[i]), id + cfValues[i]);
            }
            Integer _i = combinedIds.get(id + cfValues[i]);
            prepareToRemove.add(_i);
            addItemToTable(result + itemMap.get(cfValues[i]).getText(), _i);

            for (int j = i; j < cfCodes.length; j++) {
                if (cfCodes[j] == null || cfCodes[i].equals(cfCodes[j]) || cfCodes[j].equals("")) {
                    continue;
                }

                generateTableItems(j, id + cfValues[i] + "|", result + itemMap.get(cfValues[i]).getText() + "; ");
                break;
            }
        }
    }

    private void addItems() {
        cfCodes = new String[ids.size() + 1];
        for (String index : getSelectedItems()) {
            cfCodes[getCCIndex(index)] = ids.get(index);
        }
        prepareToRemove = new ArrayList<>();
        generateTableItems(0, "", "");
        reloadTableData();
    }

    private int getCCIndex(String value) {
        for (int i = 1; i < cfValues.length; i++) {
            if (value.equals(cfValues[i])) {
                return i;
            }
        }

        return 0;
    }

    private void removeItems() {
        addItems();

        for (int i = variationListItems.size() - 1; i >= 0; i--) {
            if (prepareToRemove.size() > 0) {
                if (!prepareToRemove.contains(variationListItems.get(i).getObjectID())) {
                    variationListItems.remove(variationListItems.get(i));
                }
            } else {
                variationListItems.clear();
                break;
            }
        }

        reloadTableData();
    }

    private void addItemToTable(final String name, Integer id) {
        VariationListItem item = new VariationListItem();
        item.setObjectID(id);
        item.setSelected(false);
        item.setPrice(product.getSellingPrice());
        item.setQty(product.getQuantity());
        item.setName(name);

        if (variationListItems.size() > 0) {
            ArrayList<Integer> items = new ArrayList<>();
            for (VariationListItem vlt : variationListItems) {
                items.add(vlt.getObjectID());
            }
            if (!items.contains(item.getObjectID())) {
                variationListItems.add(item);
            }
        } else {
            variationListItems.add(item);
        }
    }

    private void fillExistVariations(ArrayList<String> existingCombinates) {
        for (String existingCombinate : existingCombinates) {
            List<String> cfIds = Arrays.asList(existingCombinate.split("[|]"));
            TreeItem lsListItem = null;

            for (int i = 0; i < variationTreeList.getItemCount(); i++) {
                TreeItem cfTreeItem = variationTreeList.getItem(i);
                if (cfTreeItem.getChildCount() > 0) {
                    for (int k = 0; k < cfTreeItem.getChildCount(); k++) {
                        TreeItem cfvTreeItem = cfTreeItem.getChild(k);
                        KpiCheckBox cfvCheckBox = (KpiCheckBox) cfvTreeItem.getWidget();

                        if (cfIds.contains(cfvCheckBox.getName())) {
                            cfvCheckBox.setValue(true);
                            cfvCheckBox.setEnabled(false);
                            lsListItem = cfvTreeItem;
                        }
                    }
                }
            }

            fireItem(lsListItem);

            for (VariationListItem item : variationListItems) {
                if (item.getObjectID().equals(combinedIds.get(existingCombinate))) {
                    item.setSelected(true);
                    item.setExisting(true);
                }
            }
            reloadTableData();

        }
    }

    private void initVariationItemColumn() {

        CheckboxCell checkboxCell = new CheckboxCell();
        Column<VariationListItem, Boolean> checkBoxColumn = new Column<VariationListItem, Boolean>(checkboxCell) {
            @Override
            public Boolean getValue(VariationListItem item) {
                return item.isSelected();
            }
        };
        variationDataGrid.addColumn(checkBoxColumn, " ");
        variationDataGrid.setColumnWidth(variationDataGrid.getColumn(0), 20, com.google.gwt.dom.client.Style.Unit.PCT);

        variationDataGrid.addColumn(new Column<VariationListItem, String>(new TextCell()) {
                    @Override
                    public String getValue(VariationListItem item) {
                        return item.getName();
                    }
                }, accountingStrings.variation());
        variationDataGrid.setColumnWidth(variationDataGrid.getColumn(1), 100, com.google.gwt.dom.client.Style.Unit.PCT);

        TextInputCell priceCell = new TextInputCell();
        Column<VariationListItem, String> priceColumn = new Column<VariationListItem, String>(priceCell) {
            @Override
            public String getValue(VariationListItem item) {
                return AccountingUtils.get().formatUnitPrice(item.getPrice());
            }
        };
        variationDataGrid.addColumn(priceColumn, wfmStrings.price());
        variationDataGrid.setColumnWidth(variationDataGrid.getColumn(2), 100, com.google.gwt.dom.client.Style.Unit.PCT);

        TextInputCell qtyCell = new TextInputCell();
        Column<VariationListItem, String> qtyColumn = new Column<VariationListItem, String>(qtyCell) {
            @Override
            public String getValue(VariationListItem item) {
                return AccountingUtils.get().formatUnitPrice(item.getQty());
            }
        };
        variationDataGrid.addColumn(qtyColumn, wfmStrings.qty());
        variationDataGrid.setColumnWidth(variationDataGrid.getColumn(3), 100, com.google.gwt.dom.client.Style.Unit.PCT);

        checkBoxColumn.setFieldUpdater((i, variationListItem, aBoolean) -> variationListItem.setSelected(aBoolean != null ? aBoolean : false));

        priceColumn.setFieldUpdater((i, variationListItem, s) -> {
            if (s != null && !s.isEmpty()) {
                variationListItem.setPrice(AccountingUtils.get().parseToBigDecimal(s));
            }
        });

        qtyColumn.setFieldUpdater((i, variationListItem, s) -> {
            if (s != null && !s.isEmpty()) {
                variationListItem.setQty(AccountingUtils.get().parseToBigDecimal(s));
            }
        });

    }

    private void openAllTree(boolean open) {
        for (int i = 0; i < variationTreeList.getItemCount(); i++) {
            variationTreeList.getItem(i).setState(open, false);
        }
    }

    private void reloadTableData() {
        variationDataProvider.getList().clear();
        variationDataProvider.getList().addAll(variationListItems);
        variationDataProvider.refresh();
    }

    class OurClickEvent extends ClickEvent {
    }
}
