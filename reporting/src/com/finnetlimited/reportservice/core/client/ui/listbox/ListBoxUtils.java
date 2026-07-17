package com.finnetlimited.reportservice.core.client.ui.listbox;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.ui.ListBox;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: nodir
 * Date: 05.08.2010
 * Time: 21:36:27
 * To change this template use File | Settings | File Templates.
 */
public final class ListBoxUtils {
    public static final String NOT_SET = "Please select";

    public static void initListBox(final ListBox listBox, List<SelectItem> data, String nullLabel) {
        while (listBox.getItemCount() > 0) {
            listBox.removeItem(0);
        }
        if (nullLabel == null) {
            nullLabel = NOT_SET;
        }
        listBox.addItem(nullLabel, "");
        for (SelectItem aData : data) {
            listBox.addItem(aData.getName(), aData.getId().toString());
        }
    }

    public static void initListBox(final ListBox listBox, List<SelectItem> data) {
        while (listBox.getItemCount() > 0) {
            listBox.removeItem(0);
        }
        for (SelectItem aData : data) {
            listBox.addItem(aData.getName(), aData.getId().toString());
        }
    }

    public static Integer getListBoxValue(ListBox listBox) {
        String value = "";
        if (listBox.getSelectedIndex() > -1) {
            value = listBox.getValue(listBox.getSelectedIndex());
        }
        if ("".equals(value)) {
            return null;
        }
        return Integer.valueOf(value);
    }
}
