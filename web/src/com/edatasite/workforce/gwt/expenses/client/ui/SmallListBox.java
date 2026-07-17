package com.edatasite.workforce.gwt.expenses.client.ui;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.ListBox;

import java.util.HashMap;
import java.util.Map;

public class SmallListBox extends ListBox {

    private SelectItem selectedItem = null;
    private SelectItem previousSelectedItem = null;
    private Command changeEvent;
    private SelectItem[] items = null;
    private Map itemsById;

    public void onBrowserEvent(Event event) {
        if (DOM.eventGetType(event) == Event.ONCHANGE) {
            previousSelectedItem = selectedItem;
            int index = getSelectedIndex();

            selectedItem = items[index];

            if (changeEvent != null) {
                changeEvent.execute();
            }
        }
        super.onBrowserEvent(event);
    }

    public void setItems(SelectItem[] items) {
        Integer id = (getSelectedItem() == null) ? null : getSelectedItem()
                .getId();
        this.items = items;
        itemsById = new HashMap();
        for (int i = 0; i < items.length; i++) {
            itemsById.put(items[i].getId(), i);
        }
        previousSelectedItem = null;
        selectedItem = null;
        initListBox(this, items);
        setSelected(id);
    }

    public void setSelected(Integer id) {
        Integer index = (Integer) itemsById.get(id);
        if (index != null) {
            this.setSelectedIndex(index);
            selectedItem = items[index];
        }

    }

    private void initListBox(final ListBox listBox, SelectItem[] data) {
        while (listBox.getItemCount() > 0) {
            listBox.removeItem(0);
        }
        for (SelectItem aData : data) {
            listBox.addItem(aData.getName(), aData.getId() == null ? aData.getName() : aData.getId().toString());
        }
    }

    public SelectItem[] getItems() {
        return items;
    }

    public SelectItem getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(SelectItem selectedItem) {
        this.selectedItem = selectedItem;
    }

    public SelectItem getPreviousSelectedItem() {
        return previousSelectedItem;
    }

    public void setPreviousSelectedItem(SelectItem previousSelectedItem) {
        this.previousSelectedItem = previousSelectedItem;
    }

}
