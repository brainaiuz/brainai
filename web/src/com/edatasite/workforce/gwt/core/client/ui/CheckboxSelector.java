package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.cell.CheckBoxCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.view.client.ProvidesKey;
import gwt.material.design.client.ui.MaterialPanel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * User: Ilhombek
 * Date: 31.08.2009
 * Time: 18:28:22
 */
public class CheckboxSelector extends Composite implements Clearable {
    private LinkedHashMap<Integer, SelectItem> selectedIDs = new LinkedHashMap<>();
    private KpiDataGrid<SelectItem> mainGrid;

    public CheckboxSelector() {
        init("250px");
    }

    public CheckboxSelector(String height) {
        init(height);
    }

    public static final ProvidesKey<SelectItem> KEY_PROVIDER = item -> item == null ? null : item.getId() != null ? item.getId() : item.getName();

    private void init(String height) {
        mainGrid = new KpiDataGrid<>(KEY_PROVIDER);
        mainGrid.addStyleName("cellBasedWidget-selector-table cellBasedWidget-mod cellBasedWidget-mod--static-body");
        mainGrid.setHeight(height);

        Column<SelectItem, Boolean> checkBoxCell = new Column<SelectItem, Boolean>(new CheckBoxCell(true, true)) {
            @Override
            public Boolean getValue(SelectItem object) {
                if (object.isSelected()) {
                    selectedIDs.put(object.getId(), object);
                }
                return object.isSelected();
            }
        };
        mainGrid.addColumn(checkBoxCell, " ");
        mainGrid.setColumnWidth(checkBoxCell, 10, Style.Unit.PCT);

        checkBoxCell.setFieldUpdater((index, object, value) -> updateChecked(object, value));

        Column<SelectItem, String> nameCell = new Column<SelectItem, String>(new TextCell()) {
            @Override
            public String getValue(SelectItem object) {
                return mainGrid.refactor(object.getName());
            }
        };
        mainGrid.addColumn(nameCell);
        mainGrid.setColumnWidth(nameCell, 80, Style.Unit.PCT);
        MaterialPanel scrollPanel = new MaterialPanel("table__role-widget");
        scrollPanel.add(mainGrid);
        initWidget(scrollPanel);
    }

    private void updateChecked(SelectItem object, Boolean value) {
        object.setSelected(value);
        if (value) {
            selectedIDs.put(object.getId(), object);
        } else {
            selectedIDs.remove(object.getId());
        }
    }

    public void addItems(SelectItem[] itemList) {
        addItems(itemList, (Integer[]) null);
    }

    public void addItems(SelectItem[] itemList, Integer[] selectedItems) {
        if (selectedItems != null && selectedItems.length > 0) {
            for (SelectItem anItemList : itemList) {
                for (Integer selectedID : selectedItems) {
                    if (selectedID != null && selectedID.equals(anItemList.getId())) {
                        anItemList.setSelected(true);
                        break;
                    }
                }
            }
        }
        mainGrid.supplyProvider(itemList);
        mainGrid.refresh();
    }

    public void addItems(SelectItem[] itemList, List<SelectItem> selectedItems) {
        addItems(itemList, selectedItems != null && selectedItems.size() > 0 ? selectedItems.toArray(new SelectItem[]{}) : null);
    }

    public void addItems(SelectItem[] itemList, SelectItem[] selectedItems) {
        Integer[] ids = null;
        if (selectedItems != null && selectedItems.length > 0) {
            ids = new Integer[selectedItems.length];
            int counter = 0;
            for(SelectItem item : selectedItems){
                ids[counter++] = item.getId();
            }
        }
        addItems(itemList, ids);
    }

    public ArrayList<Integer> getSelectItemIDs() {
        return new ArrayList<>(selectedIDs.keySet());
    }

    public ArrayList<SelectItem> getSelectItems() {
        return new ArrayList<>(selectedIDs.values());
    }

    public void clearSelected() {
        selectedIDs.clear();
        mainGrid.getList().clear();
        mainGrid.refresh();
    }
}
