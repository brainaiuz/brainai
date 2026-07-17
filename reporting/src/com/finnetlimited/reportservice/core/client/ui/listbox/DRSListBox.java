package com.finnetlimited.reportservice.core.client.ui.listbox;

import com.edatasite.workforce.gwt.reportingsystem.client.rpc.SelectListRpc;
import com.google.gwt.user.client.ui.ListBox;

import java.util.ArrayList;

/**
 * User: ${Dilsh0d}
 * Date: 19-Mar-2010
 * Time: 15:22:08
 */
public class DRSListBox extends ListBox {

    private ArrayList<SelectListRpc> items;
    private Integer selectId;

    public DRSListBox() {
        super();
        addItem("-None-");
    }

    public DRSListBox(ArrayList<SelectListRpc> items) {
        this.items = items;
        addItem("-None-");
        addItems(items);
    }

    public void addItems(ArrayList<SelectListRpc> items) {
        this.items = items;
        for (SelectListRpc item : items) {
            addItem(item.getName(), item.getId().toString());
        }
    }

    public void setItems(ArrayList<SelectListRpc> items) {
        this.items = items;
        clear();
        for (SelectListRpc item : items) {
            addItem(item.getName(), item.getId().toString());
        }
    }

    public void setItemsNoNone(ArrayList<SelectListRpc> items) {
        this.items = items;
        removeItem(0);
        for (SelectListRpc item : items) {
            addItem(item.getName(), item.getId().toString());
        }
    }

    public boolean setSelectedName(String name) {
        boolean p = false;
        if (name != null) {
            if (items != null) {
                for (int i = 0; i < getItemCount(); i++) {
                    if (name.equals(getItemText(i))) {
                        setSelectedIndex(i);
                        p = true;
                        break;
                    }
                }
            }
        }
        return p;
    }

    public boolean setSelectedValue(String value) {
        boolean p = false;
        if (value != null) {
            if (items != null) {
                for (int i = 0; i < getItemCount(); i++) {
                    if (value.equals(getValue(i))) {
                        setSelectedIndex(i);
                        p = true;
                        break;
                    }
                }
            }
        }
        return p;
    }

    public String getSelecedName() {
        if (getSelectedIndex() != 0 || (getItemCount() > 0 && !"-None-".equals(getItemText(0)))) {
            return getItemText(getSelectedIndex());
        }
        return null;
    }

    public String getSelectedValue() {
        if (getSelectedIndex() != 0 || (getItemCount() > 0 && !"-None-".equals(getItemText(0)))) {
            return getValue(getSelectedIndex());
        }
        return null;
    }

    @Override
    public void clear() {
        super.clear();
        addItem("-None-");
    }

    public Integer getSelectId() {
        if (getSelectedIndex() != 0 || (getItemCount() > 0 && !"-None-".equals(getItemText(0)))) {
            return Integer.valueOf(getValue(getSelectedIndex()));
        }
        return null;
    }
}
