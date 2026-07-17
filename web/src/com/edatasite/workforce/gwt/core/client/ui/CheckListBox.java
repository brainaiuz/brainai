package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Virus
 * Date: 5/22/12
 * Time: 12:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class CheckListBox extends ScrollPanel {

    private HashMap<String, KpiCheckBox> checkBoxMap = new HashMap<>();
    boolean keyIsID = true;
    private FlexTable table = new FlexTable();
    private Command changeHandler;

    public CheckListBox() {
        add(table);
    }

    public CheckListBox(ArrayList<SelectItem> items) {
        this();
        init(items);
    }

    public void clearItems() {
        if (table != null) {
            table.removeFromParent();
            table = null;
        }
        if (checkBoxMap != null) {
            checkBoxMap.clear();
        }
    }

    public Widget init(ArrayList<SelectItem> items) {
        int i = -1;
        for (final SelectItem role : items) {
            KpiCheckBox roleCheckBox = new KpiCheckBox();
            String key = null;
            if (null == role.getId()) {
                keyIsID = false;
                key = role.getDescription();
            } else {
                keyIsID = true;
                key = role.getKey();
            }
            roleCheckBox.setValue(role.isSelected());
//            this.itemsMap.put(key, role);
            this.checkBoxMap.put(key, roleCheckBox);
            roleCheckBox.addValueChangeHandler(bool -> {
                String key1 = (null == role.getId()) ? role.getDescription() : role.getKey();
                checkBoxMap.get(key1).setValue(bool.getValue());
                if (changeHandler != null) {
                    changeHandler.execute();
                }
            });
            table.setWidget(++i, 0, roleCheckBox);
            table.setWidget(i, 1, new Label(role.getName()));
            table.setCellSpacing(3);
        }
        this.getElement().getStyle().setOverflowY(Style.Overflow.AUTO);
        this.getElement().getStyle().setOverflowX(Style.Overflow.HIDDEN);
        setStyleName("gwt-TabPanelBottom");
        return this;
    }

    @Override
    public void setWidth(String width) {
        this.table.setWidth(width);
        super.setWidth(width);
    }

    public ArrayList<SelectItem> getSelectedItems() {
        ArrayList<SelectItem> items = new ArrayList<>();
        if (!checkBoxMap.isEmpty()) {
            for (String key : checkBoxMap.keySet()) {
                if (checkBoxMap.get(key).getValue()) {
                    SelectItem selectItem = new SelectItem();
                    selectItem.setName(checkBoxMap.get(key).getText());
                    selectItem.setId(keyIsID ? Integer.valueOf(key) : null);
                    selectItem.setDescription(!keyIsID ? key : null);
                    selectItem.setSelected(true);
                    items.add(selectItem);
                }
            }
        }
        return items;
    }

    public void setSelected(String key, Boolean b) {
        if (checkBoxMap.containsKey(key)) {
            checkBoxMap.get(key).setValue(b);
        }
    }

    public void setSelectedItems(ArrayList<String> selectedItems) {
        for (String key : selectedItems) {
            setSelected(key, true);
        }
    }

    public void setSelectedItems(String[] selectedItems) {
        for (String key : selectedItems) {
            setSelected(key, true);
        }
    }

    public ArrayList<SelectItem> getItems() {
        ArrayList<SelectItem> items = new ArrayList<>();
        if (!checkBoxMap.isEmpty()) {
            for (String key : checkBoxMap.keySet()) {
                SelectItem selectItem = new SelectItem();
                selectItem.setName(checkBoxMap.get(key).getText());
                selectItem.setId(keyIsID ? Integer.valueOf(key) : null);
                selectItem.setDescription(!keyIsID ? key : null);
                selectItem.setSelected(true);
                items.add(selectItem);
            }
        }
        return items;
    }

    public void setChangeHandler(Command changeHandler) {
        this.changeHandler = changeHandler;
    }

    public void setEnabled(boolean enable) {
        for (KpiCheckBox checkBox : checkBoxMap.values()) {
            checkBox.setEnabled(enable);
        }
    }
}
